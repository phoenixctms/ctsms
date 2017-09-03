package org.phoenixctms.ctsms.executable.csv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.andromda.spring.MethodParameterNames;

import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecUtil;

public class PermissionTemplateWriter extends LineWriter {

	private static final ArrayList<String> PARAMETERS_TO_EXCLUDE = new ArrayList<String>();
	static
	{
		PARAMETERS_TO_EXCLUDE.add("auth");
	}

	public PermissionTemplateWriter() {
		super();
	}

	@Override
	public void printLines() {
		Iterator<Class> authorizedServiceClassesIt = ExecUtil.AUTHORIZED_SERVICE_CLASSES.iterator();
		while (authorizedServiceClassesIt.hasNext()) {
			printLines(authorizedServiceClassesIt.next());
			if (authorizedServiceClassesIt.hasNext()) {
				printBlankLine();
			}
		}
	}

	private void printLines(Class serviceClass) {
		Method[] serviceMethods = serviceClass.getMethods();
		printComment("service " + serviceClass.getName() + ":");
		long parameterCount = 0;
		for (int i = 0; i < serviceMethods.length; i++) {
			String serviceMethodName = CoreUtil.getServiceMethodName(serviceMethods[i]);
			printComment("service method " + serviceMethodName + ":");
			String[] parameterNames = serviceMethods[i].getAnnotation(MethodParameterNames.class).value();
			if (parameterNames != null && parameterNames.length > 0) {
				for (int j = 0; j < parameterNames.length; j++) {
					if (!PARAMETERS_TO_EXCLUDE.contains(parameterNames[j])) {
						ArrayList<String> permissionLine = new ArrayList<String>();
						permissionLine.add(serviceMethodName);
						permissionLine.add(parameterNames[j]);
						printLine(permissionLine);
						parameterCount++;
					}
				}
			} else {
				ArrayList<String> permissionLine = new ArrayList<String>();
				permissionLine.add(serviceMethodName);
				printLine(permissionLine);
			}
		}
		jobOutput.println(serviceClass.getName() + ": " + serviceMethods.length + " service method(s) with " + parameterCount + " parameter(s)");
	}
}
