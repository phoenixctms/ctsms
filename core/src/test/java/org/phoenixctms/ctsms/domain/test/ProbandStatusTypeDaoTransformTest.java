// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandStatusType
*/
@Test(groups = { "transform", "ProbandStatusTypeDao" })
public class ProbandStatusTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandStatusTypeDao.toProbandStatusTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandStatusTypeDao#toProbandStatusTypeVO(org.phoenixctms.ctsms.domain.ProbandStatusType source, org.phoenixctms.ctsms.vo.ProbandStatusTypeVO target)
	 */
	@Test
	public void testToProbandStatusTypeVO() {
		Assert.fail("Test 'ProbandStatusTypeDaoTransformTest.testToProbandStatusTypeVO' not implemented!");
	}

	/**
	* Test for method probandStatusTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandStatusTypeDao#probandStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.ProbandStatusTypeVO source, org.phoenixctms.ctsms.domain.ProbandStatusType target, boolean copyIfNull)
	*/
	@Test
	public void testProbandStatusTypeVOToEntity() {
		Assert.fail("Test 'ProbandStatusTypeDaoTransformTest.testProbandStatusTypeVOToEntity' not implemented!");
	}
}