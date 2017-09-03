package org.phoenixctms.ctsms.web.jersey.compare;

import java.util.Comparator;

import org.phoenixctms.ctsms.compare.AlphanumComparatorBase;

import com.sun.jersey.api.model.AbstractResourceMethod;

public class ResourceMethodComparator implements Comparator<AbstractResourceMethod> {

	@Override
	public int compare(AbstractResourceMethod a, AbstractResourceMethod b) {
		return AlphanumComparatorBase.cmp(a.getHttpMethod(), b.getHttpMethod());
	}
}
