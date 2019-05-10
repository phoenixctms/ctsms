// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Proband
*/
@Test(groups = { "transform", "ProbandDao" })
public class ProbandDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandDao.toProbandOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandDao#toProbandOutVO(org.phoenixctms.ctsms.domain.Proband source, org.phoenixctms.ctsms.vo.ProbandOutVO target)
	 */
	@Test
	public void testToProbandOutVO() {
		Assert.fail("Test 'ProbandDaoTransformTest.testToProbandOutVO' not implemented!");
	}

	/**
	* Test for method probandOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandDao#probandOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandOutVO source, org.phoenixctms.ctsms.domain.Proband target, boolean copyIfNull)
	*/
	@Test
	public void testProbandOutVOToEntity() {
		Assert.fail("Test 'ProbandDaoTransformTest.testProbandOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandDao.toProbandInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandDao#toProbandInVO(org.phoenixctms.ctsms.domain.Proband source, org.phoenixctms.ctsms.vo.ProbandInVO target)
	 */
	@Test
	public void testToProbandInVO() {
		Assert.fail("Test 'ProbandDaoTransformTest.testToProbandInVO' not implemented!");
	}

	/**
	* Test for method probandInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandDao#probandInVOToEntity(org.phoenixctms.ctsms.vo.ProbandInVO source, org.phoenixctms.ctsms.domain.Proband target, boolean copyIfNull)
	*/
	@Test
	public void testProbandInVOToEntity() {
		Assert.fail("Test 'ProbandDaoTransformTest.testProbandInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandDao.toProbandImageOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandDao#toProbandImageOutVO(org.phoenixctms.ctsms.domain.Proband source, org.phoenixctms.ctsms.vo.ProbandImageOutVO target)
	 */
	@Test
	public void testToProbandImageOutVO() {
		Assert.fail("Test 'ProbandDaoTransformTest.testToProbandImageOutVO' not implemented!");
	}

	/**
	* Test for method probandImageOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandDao#probandImageOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandImageOutVO source, org.phoenixctms.ctsms.domain.Proband target, boolean copyIfNull)
	*/
	@Test
	public void testProbandImageOutVOToEntity() {
		Assert.fail("Test 'ProbandDaoTransformTest.testProbandImageOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandDao.toProbandImageInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandDao#toProbandImageInVO(org.phoenixctms.ctsms.domain.Proband source, org.phoenixctms.ctsms.vo.ProbandImageInVO target)
	 */
	@Test
	public void testToProbandImageInVO() {
		Assert.fail("Test 'ProbandDaoTransformTest.testToProbandImageInVO' not implemented!");
	}

	/**
	* Test for method probandImageInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandDao#probandImageInVOToEntity(org.phoenixctms.ctsms.vo.ProbandImageInVO source, org.phoenixctms.ctsms.domain.Proband target, boolean copyIfNull)
	*/
	@Test
	public void testProbandImageInVOToEntity() {
		Assert.fail("Test 'ProbandDaoTransformTest.testProbandImageInVOToEntity' not implemented!");
	}
}