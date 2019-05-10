// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry
*/
@Test(groups = { "transform", "ECRFFieldStatusEntryDao" })
public class ECRFFieldStatusEntryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ECRFFieldStatusEntryDao.toECRFFieldStatusEntryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao#toECRFFieldStatusEntryInVO(org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry source, org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryInVO target)
	 */
	@Test
	public void testToECRFFieldStatusEntryInVO() {
		Assert.fail("Test 'ECRFFieldStatusEntryDaoTransformTest.testToECRFFieldStatusEntryInVO' not implemented!");
	}

	/**
	* Test for method eCRFFieldStatusEntryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao#eCRFFieldStatusEntryInVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryInVO source, org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testECRFFieldStatusEntryInVOToEntity() {
		Assert.fail("Test 'ECRFFieldStatusEntryDaoTransformTest.testECRFFieldStatusEntryInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ECRFFieldStatusEntryDao.toECRFFieldStatusEntryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao#toECRFFieldStatusEntryOutVO(org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry source, org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO target)
	 */
	@Test
	public void testToECRFFieldStatusEntryOutVO() {
		Assert.fail("Test 'ECRFFieldStatusEntryDaoTransformTest.testToECRFFieldStatusEntryOutVO' not implemented!");
	}

	/**
	* Test for method eCRFFieldStatusEntryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao#eCRFFieldStatusEntryOutVOToEntity(org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO source, org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testECRFFieldStatusEntryOutVOToEntity() {
		Assert.fail("Test 'ECRFFieldStatusEntryDaoTransformTest.testECRFFieldStatusEntryOutVOToEntity' not implemented!");
	}
}