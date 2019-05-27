package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.ComparatorFactory;

import com.sun.jersey.api.model.AbstractSubResourceMethod;

public class SubResourceMethodComparator implements Comparator<AbstractSubResourceMethod> {

	@Override
	public int compare(AbstractSubResourceMethod a, AbstractSubResourceMethod b) {
		return ComparatorFactory.ALPHANUM_COMPARATOR.compare(a.getHttpMethod(), b.getHttpMethod());
	}
}
