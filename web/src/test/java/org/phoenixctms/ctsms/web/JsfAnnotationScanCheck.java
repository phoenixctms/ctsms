package org.phoenixctms.ctsms.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Build check (run by the exec-maven-plugin, see pom.xml): fails if the JSF 2.1 annotation scanner does not detect an annotated class.
 * <p>
 * The Mojarra 2.1 scanner parses class files itself and does not know the constant pool entries that javac emits for invokedynamic
 * (lambdas, method references, and string concatenation since Java 9). For an affected class it misreads the constant pool and may
 * miss its annotation, so a {@code @ManagedBean} silently resolves to null in EL. Classes also registered in faces-config.xml do not
 * depend on the scanner and are skipped.
 */
public final class JsfAnnotationScanCheck {

	private static final String[] SCANNED_ANNOTATIONS = {
			"Ljavax/faces/bean/ManagedBean;",
			"Ljavax/faces/component/FacesComponent;",
			"Ljavax/faces/convert/FacesConverter;",
			"Ljavax/faces/validator/FacesValidator;",
			"Ljavax/faces/render/FacesRenderer;",
			"Ljavax/faces/render/FacesBehaviorRenderer;",
			"Ljavax/faces/component/behavior/FacesBehavior;",
			"Ljavax/faces/event/NamedEvent;"
	};
	private static final String CLASS_FILE_CLASS = "com.sun.faces.config.JavaClassScanningAnnotationScanner$ClassFile";
	private static final String CONSTANT_POOL_INFO_CLASS = "com.sun.faces.config.JavaClassScanningAnnotationScanner$ConstantPoolInfo";

	private static String getClassName(Path classesDir, Path classFile) {
		String name = classesDir.relativize(classFile).toString().replace('\\', '/').replace('/', '.');
		return name.substring(0, name.length() - ".class".length());
	}

	private static boolean isAnnotated(byte[] bytes) {
		String content = new String(bytes, StandardCharsets.ISO_8859_1);
		for (int i = 0; i < SCANNED_ANNOTATIONS.length; i++) {
			if (content.contains(SCANNED_ANNOTATIONS[i])) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		Path classesDir = Paths.get(args[0]);
		String facesConfig = new String(Files.readAllBytes(Paths.get(args[1])), StandardCharsets.UTF_8);
		// the scanner logs each constant pool entry it does not know as an error:
		Field loggerField = Class.forName(CONSTANT_POOL_INFO_CLASS).getDeclaredField("LOGGER");
		loggerField.setAccessible(true);
		((Logger) loggerField.get(null)).setLevel(Level.OFF);
		Class<?> classFileClass = Class.forName(CLASS_FILE_CLASS);
		Constructor<?> classFileConstructor = classFileClass.getDeclaredConstructor();
		classFileConstructor.setAccessible(true);
		Method containsAnnotation = null;
		for (Method method : classFileClass.getDeclaredMethods()) {
			if ("containsAnnotation".equals(method.getName())) {
				containsAnnotation = method;
			}
		}
		containsAnnotation.setAccessible(true);
		List<Path> classFiles;
		try (Stream<Path> paths = Files.walk(classesDir)) {
			classFiles = paths.filter(path -> path.toString().endsWith(".class")).collect(Collectors.toList());
		}
		int count = 0;
		ArrayList<String> missed = new ArrayList<String>();
		for (Path classFile : classFiles) {
			if (!isAnnotated(Files.readAllBytes(classFile))) {
				continue;
			}
			count++;
			String className = getClassName(classesDir, classFile);
			if (facesConfig.contains(className)) {
				continue;
			}
			boolean detected;
			try (FileInputStream in = new FileInputStream(classFile.toFile()); FileChannel channel = in.getChannel()) {
				Object scanner = classFileConstructor.newInstance();
				detected = (Boolean) (containsAnnotation.getParameterCount() == 1 ? containsAnnotation.invoke(scanner, channel)
						: containsAnnotation.invoke(scanner, channel, channel.size()));
			} catch (InvocationTargetException e) {
				throw new IOException("cannot scan " + classFile, e.getCause());
			}
			if (!detected) {
				missed.add(className);
			}
		}
		if (missed.size() > 0) {
			throw new IllegalStateException("the JSF 2.1 annotation scanner does not detect " + missed
					+ ". Remove lambdas, method references and string concatenation (use a StringBuilder) from these classes,"
					+ " since javac compiles them to invokedynamic, or register them in faces-config.xml.");
		}
		System.out.println("JSF annotation scan check: " + count + " annotated classes ok");
	}

	private JsfAnnotationScanCheck() {
	}
}
