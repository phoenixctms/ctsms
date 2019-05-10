// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFStatusAction
*/
@Test(groups = { "transform", "ECRFStatusActionDao" })
public class ECRFStatusActionDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ECRFStatusActionDao.toECRFStatusActionVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFStatusActionDao#toECRFStatusActionVO(org.phoenixctms.ctsms.domain.ECRFStatusAction source, org.phoenixctms.ctsms.vo.ECRFStatusActionVO target)
	 */
	@Test
	public void testToECRFStatusActionVO() {
		Assert.fail("Test 'ECRFStatusActionDaoTransformTest.testToECRFStatusActionVO' not implemented!");
	}

	/**
	* Test for method eCRFStatusActionVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFStatusActionDao#eCRFStatusActionVOToEntity(org.phoenixctms.ctsms.vo.ECRFStatusActionVO source, org.phoenixctms.ctsms.domain.ECRFStatusAction target, boolean copyIfNull)
	*/
	@Test
	public void testECRFStatusActionVOToEntity() {
		Assert.fail("Test 'ECRFStatusActionDaoTransformTest.testECRFStatusActionVOToEntity' not implemented!");
	}
}