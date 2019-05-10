// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CvPosition
*/
@Test(groups = { "transform", "CvPositionDao" })
public class CvPositionDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CvPositionDao.toCvPositionInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CvPositionDao#toCvPositionInVO(org.phoenixctms.ctsms.domain.CvPosition source, org.phoenixctms.ctsms.vo.CvPositionInVO target)
	 */
	@Test
	public void testToCvPositionInVO() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testToCvPositionInVO' not implemented!");
	}

	/**
	* Test for method cvPositionInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CvPositionDao#cvPositionInVOToEntity(org.phoenixctms.ctsms.vo.CvPositionInVO source, org.phoenixctms.ctsms.domain.CvPosition target, boolean copyIfNull)
	*/
	@Test
	public void testCvPositionInVOToEntity() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testCvPositionInVOToEntity' not implemented!");
	}

	/**
	 * Test for method CvPositionDao.toCvPositionOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CvPositionDao#toCvPositionOutVO(org.phoenixctms.ctsms.domain.CvPosition source, org.phoenixctms.ctsms.vo.CvPositionOutVO target)
	 */
	@Test
	public void testToCvPositionOutVO() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testToCvPositionOutVO' not implemented!");
	}

	/**
	* Test for method cvPositionOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CvPositionDao#cvPositionOutVOToEntity(org.phoenixctms.ctsms.vo.CvPositionOutVO source, org.phoenixctms.ctsms.domain.CvPosition target, boolean copyIfNull)
	*/
	@Test
	public void testCvPositionOutVOToEntity() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testCvPositionOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method CvPositionDao.toCvPositionPDFVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CvPositionDao#toCvPositionPDFVO(org.phoenixctms.ctsms.domain.CvPosition source, org.phoenixctms.ctsms.vo.CvPositionPDFVO target)
	 */
	@Test
	public void testToCvPositionPDFVO() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testToCvPositionPDFVO' not implemented!");
	}

	/**
	* Test for method cvPositionPDFVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CvPositionDao#cvPositionPDFVOToEntity(org.phoenixctms.ctsms.vo.CvPositionPDFVO source, org.phoenixctms.ctsms.domain.CvPosition target, boolean copyIfNull)
	*/
	@Test
	public void testCvPositionPDFVOToEntity() {
		Assert.fail("Test 'CvPositionDaoTransformTest.testCvPositionPDFVOToEntity' not implemented!");
	}
}