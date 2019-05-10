// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFStatusType
*/
@Test(groups = { "transform", "ECRFStatusTypeDao" })
public class ECRFStatusTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ECRFStatusTypeDao.toECRFStatusTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFStatusTypeDao#toECRFStatusTypeVO(org.phoenixctms.ctsms.domain.ECRFStatusType source, org.phoenixctms.ctsms.vo.ECRFStatusTypeVO target)
	 */
	@Test
	public void testToECRFStatusTypeVO() {
		Assert.fail("Test 'ECRFStatusTypeDaoTransformTest.testToECRFStatusTypeVO' not implemented!");
	}

	/**
	* Test for method eCRFStatusTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFStatusTypeDao#eCRFStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.ECRFStatusTypeVO source, org.phoenixctms.ctsms.domain.ECRFStatusType target, boolean copyIfNull)
	*/
	@Test
	public void testECRFStatusTypeVOToEntity() {
		Assert.fail("Test 'ECRFStatusTypeDaoTransformTest.testECRFStatusTypeVOToEntity' not implemented!");
	}
}