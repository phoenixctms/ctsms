package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.ComparatorFactory;

public class ResourceClassComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> a, Class<?> b) {
		return ComparatorFactory.ALPHANUM_COMPARATOR.compare(a.getName(), b.getName());
	}

}
