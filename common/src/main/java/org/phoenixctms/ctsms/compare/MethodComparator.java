package org.phoenixctms.ctsms.compare;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodComparator implements Comparator<Method> {

	private int numOfParamsDesc;

	public MethodComparator(boolean numOfParamsDesc) {
		this.numOfParamsDesc = (numOfParamsDesc ? -1 : 1);
	}

	@Override
	public int compare(Method a, Method b) {
		if (a != null && b != null) {
			int nameComparison = a.getName().compareTo(b.getName());
			if (nameComparison != 0) {
				return nameComparison;
			} else {
				int paramsLengthA = a.getParameterTypes().length;
				int paramsLengthB = b.getParameterTypes().length;
				if (paramsLengthA > paramsLengthB) {
					return 1 * numOfParamsDesc;
				} else if (paramsLengthA < paramsLengthB) {
					return -1 * numOfParamsDesc;
				} else {
					return 0;
				}
			}
		} else if (a == null && b != null) {
			return -1;
		} else if (a != null && b == null) {
			return 1;
		} else {
			return 0;
		}
	}
}
