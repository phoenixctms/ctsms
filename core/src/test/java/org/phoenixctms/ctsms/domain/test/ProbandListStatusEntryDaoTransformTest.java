// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandListStatusEntry
*/
@Test(groups = { "transform", "ProbandListStatusEntryDao" })
public class ProbandListStatusEntryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandListStatusEntryDao.toProbandListStatusEntryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao#toProbandListStatusEntryOutVO(org.phoenixctms.ctsms.domain.ProbandListStatusEntry source, org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO target)
	 */
	@Test
	public void testToProbandListStatusEntryOutVO() {
		Assert.fail("Test 'ProbandListStatusEntryDaoTransformTest.testToProbandListStatusEntryOutVO' not implemented!");
	}

	/**
	* Test for method probandListStatusEntryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao#probandListStatusEntryOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO source, org.phoenixctms.ctsms.domain.ProbandListStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testProbandListStatusEntryOutVOToEntity() {
		Assert.fail("Test 'ProbandListStatusEntryDaoTransformTest.testProbandListStatusEntryOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandListStatusEntryDao.toProbandListStatusEntryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao#toProbandListStatusEntryInVO(org.phoenixctms.ctsms.domain.ProbandListStatusEntry source, org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO target)
	 */
	@Test
	public void testToProbandListStatusEntryInVO() {
		Assert.fail("Test 'ProbandListStatusEntryDaoTransformTest.testToProbandListStatusEntryInVO' not implemented!");
	}

	/**
	* Test for method probandListStatusEntryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao#probandListStatusEntryInVOToEntity(org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO source, org.phoenixctms.ctsms.domain.ProbandListStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testProbandListStatusEntryInVOToEntity() {
		Assert.fail("Test 'ProbandListStatusEntryDaoTransformTest.testProbandListStatusEntryInVOToEntity' not implemented!");
	}
}