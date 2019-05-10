// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFFieldValue
*/
@Test(groups = { "transform", "ECRFFieldValueDao" })
public class ECRFFieldValueDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ECRFFieldValueDao.toECRFFieldValueInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#toECRFFieldValueInVO(org.phoenixctms.ctsms.domain.ECRFFieldValue source, org.phoenixctms.ctsms.vo.ECRFFieldValueInVO target)
	 */
	@Test
	public void testToECRFFieldValueInVO() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testToECRFFieldValueInVO' not implemented!");
	}

	/**
	* Test for method eCRFFieldValueInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#eCRFFieldValueInVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldValueInVO source, org.phoenixctms.ctsms.domain.ECRFFieldValue target, boolean copyIfNull)
	*/
	@Test
	public void testECRFFieldValueInVOToEntity() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testECRFFieldValueInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ECRFFieldValueDao.toECRFFieldValueJsonVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#toECRFFieldValueJsonVO(org.phoenixctms.ctsms.domain.ECRFFieldValue source, org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO target)
	 */
	@Test
	public void testToECRFFieldValueJsonVO() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testToECRFFieldValueJsonVO' not implemented!");
	}

	/**
	* Test for method eCRFFieldValueJsonVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#eCRFFieldValueJsonVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO source, org.phoenixctms.ctsms.domain.ECRFFieldValue target, boolean copyIfNull)
	*/
	@Test
	public void testECRFFieldValueJsonVOToEntity() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testECRFFieldValueJsonVOToEntity' not implemented!");
	}

	/**
	 * Test for method ECRFFieldValueDao.toECRFFieldValueOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#toECRFFieldValueOutVO(org.phoenixctms.ctsms.domain.ECRFFieldValue source, org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO target)
	 */
	@Test
	public void testToECRFFieldValueOutVO() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testToECRFFieldValueOutVO' not implemented!");
	}

	/**
	* Test for method eCRFFieldValueOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFFieldValueDao#eCRFFieldValueOutVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO source, org.phoenixctms.ctsms.domain.ECRFFieldValue target, boolean copyIfNull)
	*/
	@Test
	public void testECRFFieldValueOutVOToEntity() {
		Assert.fail("Test 'ECRFFieldValueDaoTransformTest.testECRFFieldValueOutVOToEntity' not implemented!");
	}
}