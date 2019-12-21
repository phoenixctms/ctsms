package org.phoenixctms.ctsms.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

public class FileOverloads {

	private final static Pattern PATH_ENVIRONMENT_VARIABLE_REGEXP = Pattern.compile("\\s*[,;]\\s*");
	private final static String PROPERTIES_PATH_ENVIRONMENT_VARIABLE = "CTSMS_PROPERTIES";
	public final static LinkedHashSet<String> PROPERTIES_SEARCH_PATHS = getPropertiesSearchPaths();
	final static String JAVA_PATH_ENVIRONMENT_VARIABLE = "CTSMS_JAVA"; //friend
	public final static LinkedHashSet<String> JAVA_SEARCH_PATHS = getJavaSearchPaths();

	private static LinkedHashSet<String> getPropertiesSearchPaths() {
		return getSearchPaths(PROPERTIES_PATH_ENVIRONMENT_VARIABLE);
	}

	private static LinkedHashSet<String> getJavaSearchPaths() {
		return getSearchPaths(JAVA_PATH_ENVIRONMENT_VARIABLE);
	}

	private static LinkedHashSet<String> getSearchPaths(String variable) {
		String[] searchPaths = null;
		String envVar = null;
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		try {
			envVar = System.getProperty(variable);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (!(envVar != null && envVar.length() > 0)) {
			try {
				envVar = System.getenv(variable);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if (envVar != null && envVar.length() > 0) {
			System.out.println(variable + ": " + envVar);
			searchPaths = PATH_ENVIRONMENT_VARIABLE_REGEXP.split(envVar);
		}
		if (searchPaths != null && searchPaths.length > 0) {
			for (int i = 0; i < searchPaths.length; i++) {
				String searchPath = searchPaths[i];
				if (searchPath != null && searchPath.length() > 0) {
					File file = new File(searchPath);
					if (file.exists()) {
						if (file.isDirectory()) {
							if (file.isAbsolute()) {
								if (file.canRead()) {
									try {
										result.add(file.getCanonicalPath());
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
}
