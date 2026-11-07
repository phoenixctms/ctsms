package org.phoenixctms.ctsms.executable;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

import org.hibernate.bytecode.javassist.FieldFilter;
import org.hibernate.bytecode.javassist.FieldHandled;
import org.hibernate.bytecode.javassist.FieldTransformer;

/**
 * Build step (run by the exec-maven-plugin in the process-classes phase, see core/pom.xml): Hibernate 3 only honours
 * &lt;property lazy="true"&gt; for build-time instrumented entity classes, and loads the column with the entity otherwise.
 * <p>
 * Instruments every entity with a lazy property in its mapping like Hibernate's javassist InstrumentTask, with two
 * additions: the fields and their interceptor are added to the AndroMDA base class that declares them, while the mapped
 * Impl class only gets the FieldHandled interface, since Hibernate checks the interfaces of the mapped class itself.
 * And the stack map frames are rebuilt, which Java 7+ class files require (needs javassist 3.x newer than the one
 * Hibernate 3.5 comes with on the classpath first).
 */
public final class LazyPropertyInstrumenter {

	private final static Pattern CLASS_NAME_PATTERN = Pattern.compile("<class\\s+name=\"([^\"]+)\"");
	private final static Pattern LAZY_PROPERTY_PATTERN = Pattern.compile("<property\\s[^>]*lazy=\"true\"");
	private final static String MAPPING_FILE_SUFFIX = ".hbm.xml";
	private final static String CLASS_FILE_SUFFIX = ".class";

	private static File getClassFile(File classesDir, String className) {
		return new File(classesDir, className.replace('.', File.separatorChar) + CLASS_FILE_SUFFIX);
	}

	private static boolean isInstrumented(ClassFile classFile) {
		String[] interfaces = classFile.getInterfaces();
		return interfaces != null && Arrays.asList(interfaces).contains(FieldHandled.class.getName());
	}

	private static TreeSet<String> getLazyPropertyEntityClassNames(File mappingsDir) throws IOException {
		TreeSet<String> classNames = new TreeSet<String>();
		File[] mappingFiles = mappingsDir.listFiles();
		if (mappingFiles != null) {
			for (int i = 0; i < mappingFiles.length; i++) {
				if (mappingFiles[i].getName().endsWith(MAPPING_FILE_SUFFIX)) {
					String mapping = new String(Files.readAllBytes(mappingFiles[i].toPath()), StandardCharsets.UTF_8);
					if (LAZY_PROPERTY_PATTERN.matcher(mapping).find()) {
						Matcher classNameMatcher = CLASS_NAME_PATTERN.matcher(mapping);
						if (!classNameMatcher.find()) {
							throw new IllegalStateException("no class name in " + mappingFiles[i]);
						}
						classNames.add(classNameMatcher.group(1));
					}
				}
			}
		}
		return classNames;
	}

	public static void main(String[] args) throws Exception {
		File classesDir = new File(args[0]);
		File mappingsDir = new File(classesDir, args[1].replace('.', File.separatorChar));
		ClassPool pool = new ClassPool(true);
		pool.appendClassPath(classesDir.getAbsolutePath());
		Iterator<String> it = getLazyPropertyEntityClassNames(mappingsDir).iterator();
		while (it.hasNext()) {
			String mappedClassName = it.next();
			ClassFile mappedClass = readClassFile(getClassFile(classesDir, mappedClassName));
			String baseClassName = mappedClass.getSuperclass();
			ClassFile baseClass = readClassFile(getClassFile(classesDir, baseClassName));
			if (!isInstrumented(baseClass)) {
				transform(baseClass, pool);
				writeClassFile(baseClass, getClassFile(classesDir, baseClassName));
				System.out.println("instrumented fields of " + baseClassName);
			}
			if (!isInstrumented(mappedClass)) {
				mappedClass.addInterface(FieldHandled.class.getName());
				writeClassFile(mappedClass, getClassFile(classesDir, mappedClassName));
				System.out.println("marked " + mappedClassName + " as instrumented");
			}
		}
	}

	private static ClassFile readClassFile(File file) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		try {
			return new ClassFile(in);
		} finally {
			in.close();
		}
	}

	private static void transform(final ClassFile classFile, ClassPool pool) throws Exception {
		// intercept the class' own fields and their accesses within the class, like hibernate's (not extended) instrumentation:
		new FieldTransformer(new FieldFilter() {

			@Override
			public boolean handleRead(String desc, String name) {
				return true;
			}

			@Override
			public boolean handleReadAccess(String fieldOwnerClassName, String fieldName) {
				return classFile.getName().equals(fieldOwnerClassName);
			}

			@Override
			public boolean handleWrite(String desc, String name) {
				return true;
			}

			@Override
			public boolean handleWriteAccess(String fieldOwnerClassName, String fieldName) {
				return classFile.getName().equals(fieldOwnerClassName);
			}
		}).transform(classFile);
		Iterator<?> methodsIt = classFile.getMethods().iterator();
		while (methodsIt.hasNext()) {
			((MethodInfo) methodsIt.next()).rebuildStackMapIf6(pool, classFile);
		}
	}

	private static void writeClassFile(ClassFile classFile, File file) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bytes);
		try {
			classFile.write(out);
		} finally {
			out.close();
		}
		FileOutputStream fileOut = new FileOutputStream(file);
		try {
			fileOut.write(bytes.toByteArray());
		} finally {
			fileOut.close();
		}
	}

	private LazyPropertyInstrumenter() {
	}
}
