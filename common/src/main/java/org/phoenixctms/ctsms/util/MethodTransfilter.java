package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.compare.MethodComparator;

public class MethodTransfilter {

	public static final MethodTransfilter DEFAULT_TRANSFILTER = new MethodTransfilter();
	protected final static Comparator<Method> METHOD_COMPARATOR = new MethodComparator(false);

	public final static MethodTransfilter getEntityMethodTransfilter(final boolean lowerCaseFieldNames) {
		return new MethodTransfilter() {

			private final Pattern ENTITY_GETTER_METHOD_NAME_EXCLUSION_REGEXP = Pattern.compile("^((getClass)|(getDeclaringClass))");

			@Override
			public boolean exclude(Method method) {
				return ENTITY_GETTER_METHOD_NAME_EXCLUSION_REGEXP.matcher(method.getName()).lookingAt();
			}

			@Override
			public boolean include(Method method) {
				return CommonUtil.ENTITY_GETTER_METHOD_NAME_REGEXP.matcher(method.getName()).lookingAt();
			}

			@Override
			public String reverseTransform(String transformedMethodName) {
				return "get" + CommonUtil.capitalizeFirstChar(transformedMethodName, true);
			}

			@Override
			public String transform(String methodName) {
				String fieldName = CommonUtil.ENTITY_GETTER_METHOD_NAME_REGEXP.matcher(methodName).replaceAll("");
				return lowerCaseFieldNames ? fieldName.toLowerCase() : CommonUtil.capitalizeFirstChar(fieldName, false);
			}
		};
	}

	public final static MethodTransfilter getVoMethodTransfilter(final boolean lowerCaseFieldNames) {
		return new MethodTransfilter() {

			private final Pattern VO_GETTER_METHOD_NAME_EXCLUSION_REGEXP = Pattern.compile("^((isSet)|(getClass)|(getDeclaringClass))");

			@Override
			public boolean exclude(Method method) {
				return VO_GETTER_METHOD_NAME_EXCLUSION_REGEXP.matcher(method.getName()).lookingAt();
			}

			@Override
			public boolean include(Method method) {
				return CommonUtil.VO_GETTER_METHOD_NAME_REGEXP.matcher(method.getName()).lookingAt();
			}

			@Override
			public String reverseTransform(String transformedMethodName) {
				return "get" + CommonUtil.capitalizeFirstChar(transformedMethodName, true);
			}

			@Override
			public String transform(String methodName) {
				String fieldName = CommonUtil.VO_GETTER_METHOD_NAME_REGEXP.matcher(methodName).replaceAll("");
				return lowerCaseFieldNames ? fieldName.toLowerCase() : CommonUtil.capitalizeFirstChar(fieldName, false);
			}
		};
	}

	public boolean exclude(Method method) {
		return false;
	}

	public Comparator<Method> getMethodComparator() {
		return METHOD_COMPARATOR;
	}

	public boolean include(Method method) {
		return true;
	}

	public final boolean isTransformedEmpty(String methodName) {
		String transformed = transform(methodName);
		return transformed == null || transformed.length() == 0;
	}

	public final boolean isTransformedEqual(String methodNameA, String methodNameB) {
		String transformedA = transform(methodNameA);
		if (transformedA == null || transformedA.length() == 0) {
			return false;
		}
		String transformedB = transform(methodNameB);
		if (transformedB == null || transformedB.length() == 0) {
			return false;
		}
		return transformedA.equals(transformedB);
	}

	public String reverseTransform(String transformedMethodName) {
		return transformedMethodName;
	}

	public String transform(String methodName) {
		return methodName;
	}
}