package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.AlphanumComparatorBase;

import com.sun.jersey.api.model.AbstractSubResourceMethod;

public class SubResourceMethodComparator implements Comparator<AbstractSubResourceMethod> {

	@Override
	public int compare(AbstractSubResourceMethod a, AbstractSubResourceMethod b) {
		int pathComparison = AlphanumComparatorBase.cmp(a.getPath(), b.getPath());
		if (pathComparison != 0) {
			return pathComparison;
		} else {
			return AlphanumComparatorBase.cmp(a.getHttpMethod(), b.getHttpMethod());
		}
	}
}
