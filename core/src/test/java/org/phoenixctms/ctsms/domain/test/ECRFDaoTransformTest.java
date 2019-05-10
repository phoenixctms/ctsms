// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRF
*/
@Test(groups = { "transform", "ECRFDao" })
public class ECRFDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ECRFDao.toECRFInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFDao#toECRFInVO(org.phoenixctms.ctsms.domain.ECRF source, org.phoenixctms.ctsms.vo.ECRFInVO target)
	 */
	@Test
	public void testToECRFInVO() {
		Assert.fail("Test 'ECRFDaoTransformTest.testToECRFInVO' not implemented!");
	}

	/**
	* Test for method eCRFInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFDao#eCRFInVOToEntity(org.phoenixctms.ctsms.vo.ECRFInVO source, org.phoenixctms.ctsms.domain.ECRF target, boolean copyIfNull)
	*/
	@Test
	public void testECRFInVOToEntity() {
		Assert.fail("Test 'ECRFDaoTransformTest.testECRFInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ECRFDao.toECRFOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFDao#toECRFOutVO(org.phoenixctms.ctsms.domain.ECRF source, org.phoenixctms.ctsms.vo.ECRFOutVO target)
	 */
	@Test
	public void testToECRFOutVO() {
		Assert.fail("Test 'ECRFDaoTransformTest.testToECRFOutVO' not implemented!");
	}

	/**
	* Test for method eCRFOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFDao#eCRFOutVOToEntity(org.phoenixctms.ctsms.vo.ECRFOutVO source, org.phoenixctms.ctsms.domain.ECRF target, boolean copyIfNull)
	*/
	@Test
	public void testECRFOutVOToEntity() {
		Assert.fail("Test 'ECRFDaoTransformTest.testECRFOutVOToEntity' not implemented!");
	}
}