package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.ComparatorFactory;

import com.sun.jersey.api.model.AbstractResourceMethod;

public class ResourceMethodComparator implements Comparator<AbstractResourceMethod> {

	@Override
	public int compare(AbstractResourceMethod a, AbstractResourceMethod b) {
		return ComparatorFactory.ALPHANUM_COMPARATOR.compare(a.getHttpMethod(), b.getHttpMethod());
	}
}
