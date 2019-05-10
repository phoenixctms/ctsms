// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Inquiry
*/
@Test(groups = { "transform", "InquiryDao" })
public class InquiryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method InquiryDao.toInquiryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.InquiryDao#toInquiryOutVO(org.phoenixctms.ctsms.domain.Inquiry source, org.phoenixctms.ctsms.vo.InquiryOutVO target)
	 */
	@Test
	public void testToInquiryOutVO() {
		Assert.fail("Test 'InquiryDaoTransformTest.testToInquiryOutVO' not implemented!");
	}

	/**
	* Test for method inquiryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.InquiryDao#inquiryOutVOToEntity(org.phoenixctms.ctsms.vo.InquiryOutVO source, org.phoenixctms.ctsms.domain.Inquiry target, boolean copyIfNull)
	*/
	@Test
	public void testInquiryOutVOToEntity() {
		Assert.fail("Test 'InquiryDaoTransformTest.testInquiryOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method InquiryDao.toInquiryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.InquiryDao#toInquiryInVO(org.phoenixctms.ctsms.domain.Inquiry source, org.phoenixctms.ctsms.vo.InquiryInVO target)
	 */
	@Test
	public void testToInquiryInVO() {
		Assert.fail("Test 'InquiryDaoTransformTest.testToInquiryInVO' not implemented!");
	}

	/**
	* Test for method inquiryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.InquiryDao#inquiryInVOToEntity(org.phoenixctms.ctsms.vo.InquiryInVO source, org.phoenixctms.ctsms.domain.Inquiry target, boolean copyIfNull)
	*/
	@Test
	public void testInquiryInVOToEntity() {
		Assert.fail("Test 'InquiryDaoTransformTest.testInquiryInVOToEntity' not implemented!");
	}

	/**
	 * Test for method InquiryDao.toLightInquiryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.InquiryDao#toLightInquiryOutVO(org.phoenixctms.ctsms.domain.Inquiry source, org.phoenixctms.ctsms.vo.LightInquiryOutVO target)
	 */
	@Test
	public void testToLightInquiryOutVO() {
		Assert.fail("Test 'InquiryDaoTransformTest.testToLightInquiryOutVO' not implemented!");
	}

	/**
	* Test for method lightInquiryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.InquiryDao#lightInquiryOutVOToEntity(org.phoenixctms.ctsms.vo.LightInquiryOutVO source, org.phoenixctms.ctsms.domain.Inquiry target, boolean copyIfNull)
	*/
	@Test
	public void testLightInquiryOutVOToEntity() {
		Assert.fail("Test 'InquiryDaoTransformTest.testLightInquiryOutVOToEntity' not implemented!");
	}
}