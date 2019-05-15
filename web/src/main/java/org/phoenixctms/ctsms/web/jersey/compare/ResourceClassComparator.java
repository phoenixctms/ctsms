package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.AlphanumComparatorBase;

public class ResourceClassComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> a, Class<?> b) {
		return AlphanumComparatorBase.cmp(a.getName(), b.getName());
	}

}
