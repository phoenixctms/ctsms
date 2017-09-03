package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

public class FieldComparator<T> implements Comparator<T> {

	private int desc;
	private String getterMethodName;

	private FieldComparator() {
	}

	public FieldComparator(boolean desc, String getterMethodName) {
		this.desc = (desc ? -1 : 1);
		this.getterMethodName = getterMethodName;
	}

	@Override
	public int compare(T a, T b) {
		if (a != null && b != null) {
			Comparable valueA = null;
			try {
				valueA = getFieldValue(a);
			} catch (Exception e) {
				valueA = null;
			}
			Comparable valueB = null;
			try {
				valueB = getFieldValue(b);
			} catch (Exception e) {
				valueB = null;
			}
			if (valueA != null && valueB != null) {
				return valueA.compareTo(valueB) * desc;
			} else if (valueA == null && valueB != null) {
				return -1 * desc;
			} else if (valueA != null && valueB == null) {
				return 1 * desc;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1 * desc;
		} else if (a != null && b == null) {
			return 1 * desc;
		} else {
			return 0;
		}
	}

	private Comparable getFieldValue(T entity) throws Exception {
		return (Comparable) entity.getClass().getMethod(getterMethodName).invoke(entity);
	}
}
