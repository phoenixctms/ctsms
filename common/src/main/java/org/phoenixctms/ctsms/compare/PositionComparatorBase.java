package org.phoenixctms.ctsms.compare;

import java.util.Comparator;

public abstract class PositionComparatorBase<T> implements Comparator<T> {

	private int posDesc;

	private PositionComparatorBase() {
	}

	public PositionComparatorBase(boolean posDesc) {
		this.posDesc = (posDesc ? -1 : 1);
	}

	@Override
	public int compare(T a, T b) {
		if (a != null && b != null) {
			Long posA = null;
			try {
				posA = getPosition(a);
			} catch (Exception e) {
				posA = null;
			}
			Long posB = null;
			try {
				posB = getPosition(b);
			} catch (Exception e) {
				posB = null;
			}
			if (posA != null && posB != null) {
				return posA.compareTo(posB) * posDesc;
			} else if (posA == null && posB != null) {
				return -1 * posDesc;
			} else if (posA != null && posB == null) {
				return 1 * posDesc;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1 * posDesc;
		} else if (a != null && b == null) {
			return 1 * posDesc;
		} else {
			return 0;
		}
	}

	protected abstract Long getPosition(T item) throws Exception;
}