package org.phoenixctms.ctsms.compare;

import java.util.Comparator;


public abstract class IDComparatorBase<T> implements Comparator<T> {

	private int idDesc;

	private IDComparatorBase() {
	}

	public IDComparatorBase(boolean idDesc) {
		this.idDesc = (idDesc ? -1 : 1);
	}

	@Override
	public int compare(T a, T b) {
		if (a != null && b != null) {
			Long idA = null;
			try {
				idA = getId(a);
			} catch (Exception e) {
				idA = null;
			}
			Long idB = null;
			try {
				idB = getId(b);
			} catch (Exception e) {
				idB = null;
			}
			if (idA != null && idB != null) {
				return idA.compareTo(idB) * idDesc;
			} else if (idA == null && idB != null) {
				return -1 * idDesc;
			} else if (idA != null && idB == null) {
				return 1 * idDesc;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1 * idDesc;
		} else if (a != null && b == null) {
			return 1 * idDesc;
		} else {
			return 0;
		}
	}

	protected abstract Long getId(T item) throws Exception;
}
