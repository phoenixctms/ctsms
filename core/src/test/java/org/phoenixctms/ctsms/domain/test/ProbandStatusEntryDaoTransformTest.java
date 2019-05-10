// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandStatusEntry
*/
@Test(groups = { "transform", "ProbandStatusEntryDao" })
public class ProbandStatusEntryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandStatusEntryDao.toProbandStatusEntryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandStatusEntryDao#toProbandStatusEntryInVO(org.phoenixctms.ctsms.domain.ProbandStatusEntry source, org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO target)
	 */
	@Test
	public void testToProbandStatusEntryInVO() {
		Assert.fail("Test 'ProbandStatusEntryDaoTransformTest.testToProbandStatusEntryInVO' not implemented!");
	}

	/**
	* Test for method probandStatusEntryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandStatusEntryDao#probandStatusEntryInVOToEntity(org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO source, org.phoenixctms.ctsms.domain.ProbandStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testProbandStatusEntryInVOToEntity() {
		Assert.fail("Test 'ProbandStatusEntryDaoTransformTest.testProbandStatusEntryInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProbandStatusEntryDao.toProbandStatusEntryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandStatusEntryDao#toProbandStatusEntryOutVO(org.phoenixctms.ctsms.domain.ProbandStatusEntry source, org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO target)
	 */
	@Test
	public void testToProbandStatusEntryOutVO() {
		Assert.fail("Test 'ProbandStatusEntryDaoTransformTest.testToProbandStatusEntryOutVO' not implemented!");
	}

	/**
	* Test for method probandStatusEntryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandStatusEntryDao#probandStatusEntryOutVOToEntity(org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO source, org.phoenixctms.ctsms.domain.ProbandStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testProbandStatusEntryOutVOToEntity() {
		Assert.fail("Test 'ProbandStatusEntryDaoTransformTest.testProbandStatusEntryOutVOToEntity' not implemented!");
	}
}