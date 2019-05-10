// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.VisitType
*/
@Test(groups = { "transform", "VisitTypeDao" })
public class VisitTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method VisitTypeDao.toVisitTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.VisitTypeDao#toVisitTypeVO(org.phoenixctms.ctsms.domain.VisitType source, org.phoenixctms.ctsms.vo.VisitTypeVO target)
	 */
	@Test
	public void testToVisitTypeVO() {
		Assert.fail("Test 'VisitTypeDaoTransformTest.testToVisitTypeVO' not implemented!");
	}

	/**
	* Test for method visitTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.VisitTypeDao#visitTypeVOToEntity(org.phoenixctms.ctsms.vo.VisitTypeVO source, org.phoenixctms.ctsms.domain.VisitType target, boolean copyIfNull)
	*/
	@Test
	public void testVisitTypeVOToEntity() {
		Assert.fail("Test 'VisitTypeDaoTransformTest.testVisitTypeVOToEntity' not implemented!");
	}
}