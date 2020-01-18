package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public final class Compile {

	private static URLClassLoader classLoader = null;
	private static String javaSearchPath = null; //once a search path containing .java files was determined, use always the same
	private static JavaCompiler compiler = null;
	private static String compilerClassPath = null;
	private static boolean CLEAN = false; //true for no initial .class file cleanup
	private final static String NO_CLASS_NAME = "no class to instantiate specified";
	private final static String NO_JAVA_SEARCH_PATHS = "no valid path for external .java/.class files defined ({0})";
	private final static String SOURCE_FILE_NOT_FOUND = "cannot find source file {0}";
	private final static String SOURCE_FILE_NOT_FOUND_IN_SEARCH_PATH = "cannot find source file {0} in {1}";
	private final static String INVALID_JAVA_SEARCH_PATH = "cannot load classes from {0}";
	private final static String NO_JAVA_COMPILER = "no java compiler, please run with JDK (java.home: {0})";
	private final static String SOURCE_FILE_ERROR_LINE = "\nError on line {0} in {1}: {2}";
	private final static String SOURCE_FILE_MORE_ERRORS = "\n... and {0} more";
	private final static String NO_SOURCE_FILES = ".java source files required to instantiate {0}";
	private final static String CANNOT_DELETE_FILE = "cannot delete {0}";
	private final static int ERROR_LIMIT = 10;

	public static Class<?> loadClass(String className) throws IllegalAccessException, ClassNotFoundException {
		return loadClass(className, null);
	}

	public synchronized static Class<?> loadClass(String className, ArrayList<String> sourceFiles)
			throws IllegalAccessException, ClassNotFoundException {
		if (CommonUtil.isEmptyString(className)) {
			throw new IllegalArgumentException(NO_CLASS_NAME);
		}
		ArrayList<File> fileList = getFileList(sourceFiles);
		try {
			return getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
		}
		if (fileList.size() > 0) {
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			StandardJavaFileManager fileManager = getCompiler().getStandardFileManager(diagnostics, null, null);
			List<String> optionList = new ArrayList<String>();
			optionList.add("-classpath");
			optionList.add(getCompilerClassPath());
			try {
				Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(fileList);
				JavaCompiler.CompilationTask task = getCompiler().getTask(
						null,
						fileManager,
						diagnostics,
						optionList,
						null,
						compilationUnit);
				if (task.call()) {
					return getClassLoader().loadClass(className);
				} else {
					Iterator<Diagnostic<? extends JavaFileObject>> it = diagnostics.getDiagnostics().iterator();
					StringBuilder sb = new StringBuilder();
					int cnt = 0;
					while (it.hasNext()) {
						Diagnostic<? extends JavaFileObject> diagnostic = it.next();
						if (cnt < ERROR_LIMIT) {
							sb.append(MessageFormat.format(SOURCE_FILE_ERROR_LINE, diagnostic.getLineNumber(), diagnostic.getSource().toUri(), diagnostic.getMessage(null)));
						}
						cnt += 1;
					}
					if (cnt > ERROR_LIMIT) {
						sb.append(MessageFormat.format(SOURCE_FILE_MORE_ERRORS, cnt - ERROR_LIMIT));
					}
					throw new IllegalArgumentException(sb.toString());
				}
			} finally {
				try {
					fileManager.close();
				} catch (IOException e) {
				}
			}
		} else {
			throw new IllegalArgumentException(MessageFormat.format(NO_SOURCE_FILES, className));
		}
	}

	public synchronized static void clean() {
		Queue<File> dirs = new LinkedList<File>();
		dirs.add(new File(getSearchClassPath()));
		while (!dirs.isEmpty()) {
			File[] files = dirs.poll().listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					dirs.add(files[i]);
				} else if (files[i].isFile()) {
					if (files[i].getName().endsWith(".class")) {
						if (!files[i].delete()) {
							throw new IllegalArgumentException(MessageFormat.format(CANNOT_DELETE_FILE, files[i].getName()));
						}
					}
				}
			}
		}
	}

	private static JavaCompiler getCompiler() {
		if (compiler == null) {
			compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				throw new IllegalArgumentException(MessageFormat.format(NO_JAVA_COMPILER, System.getProperty("java.home")));
			}
		}
		return compiler;
	}

	private static String getSearchClassPath() {
		String classPath = javaSearchPath;
		if (classPath == null) {
			if (FileOverloads.JAVA_SEARCH_PATHS.size() == 0) {
				throw new IllegalArgumentException(MessageFormat.format(NO_JAVA_SEARCH_PATHS, FileOverloads.JAVA_PATH_ENVIRONMENT_VARIABLE));
			}
			classPath = FileOverloads.JAVA_SEARCH_PATHS.iterator().next();
		}
		if (File.separatorChar != classPath.charAt(classPath.length() - 1)) {
			classPath += File.separatorChar;
		}
		return classPath;
	}

	private static String getCompilerClassPath() {
		if (compilerClassPath == null) {
			File jar = new File(Compile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			StringBuilder sb = new StringBuilder(getSearchClassPath());
			if (jar.isFile()) {
				Iterator<File> it = Arrays.asList(jar.getParentFile().listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".jar") || name.endsWith(".JAR");
					}
				})).iterator();
				while (it.hasNext()) {
					if (sb.length() > 0) {
						sb.append(File.pathSeparator);
					}
					sb.append(it.next().getPath());
				}
			} else {
				if (sb.length() > 0) {
					sb.append(File.pathSeparator);
				}
				sb.append(System.getProperty("java.class.path"));
			}
			compilerClassPath = sb.toString();
		}
		return compilerClassPath;
	}

	private static URLClassLoader getClassLoader() {
		if (classLoader == null) {
			String classPath = getSearchClassPath();
			try {
				classLoader = new URLClassLoader(new URL[] { new File(classPath).toURI().toURL() }, Compile.class.getClassLoader());
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(MessageFormat.format(INVALID_JAVA_SEARCH_PATH, classPath));
			}
		}
		return classLoader;
	}

	private static ArrayList<File> getFileList(ArrayList<String> sourceFiles) {
		if (FileOverloads.JAVA_SEARCH_PATHS.size() == 0) {
			throw new IllegalArgumentException(MessageFormat.format(NO_JAVA_SEARCH_PATHS, FileOverloads.JAVA_PATH_ENVIRONMENT_VARIABLE));
		}
		ArrayList<File> fileList = new ArrayList<File>();
		if (sourceFiles != null) {
			Iterator<String> sourceFilesIt = sourceFiles.iterator();
			while (sourceFilesIt.hasNext()) {
				String fileName = sourceFilesIt.next();
				if (fileName != null) {
					boolean found = false;
					if (javaSearchPath != null) {
						File file = new java.io.File(javaSearchPath, fileName);
						if (file.exists() && file.isFile()) {
							found = fileList.add(file);
						}
					} else {
						Iterator<String> searchPathsIt = FileOverloads.JAVA_SEARCH_PATHS.iterator();
						while (searchPathsIt.hasNext()) {
							String searchPath = searchPathsIt.next();
							File file = new java.io.File(searchPath, fileName);
							if (file.exists() && file.isFile()) {
								javaSearchPath = searchPath;
								if (!CLEAN) {
									clean();
									CLEAN = true;
								}
								found = fileList.add(file);
								break;
							}
						}
					}
					if (!found) {
						if (javaSearchPath != null) {
							throw new IllegalArgumentException(MessageFormat.format(SOURCE_FILE_NOT_FOUND_IN_SEARCH_PATH, fileName, javaSearchPath));
						} else {
							throw new IllegalArgumentException(MessageFormat.format(SOURCE_FILE_NOT_FOUND, fileName));
						}
					}
				}
			}
		}
		return fileList;
	}

	private Compile() {
	}
}
