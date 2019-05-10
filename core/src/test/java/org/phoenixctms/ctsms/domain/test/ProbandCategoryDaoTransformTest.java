// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.ProbandCategory
*/
@Test(groups = { "transform", "ProbandCategoryDao" })
public class ProbandCategoryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProbandCategoryDao.toProbandCategoryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProbandCategoryDao#toProbandCategoryVO(org.phoenixctms.ctsms.domain.ProbandCategory source, org.phoenixctms.ctsms.vo.ProbandCategoryVO target)
	 */
	@Test
	public void testToProbandCategoryVO() {
		Assert.fail("Test 'ProbandCategoryDaoTransformTest.testToProbandCategoryVO' not implemented!");
	}

	/**
	* Test for method probandCategoryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProbandCategoryDao#probandCategoryVOToEntity(org.phoenixctms.ctsms.vo.ProbandCategoryVO source, org.phoenixctms.ctsms.domain.ProbandCategory target, boolean copyIfNull)
	*/
	@Test
	public void testProbandCategoryVOToEntity() {
		Assert.fail("Test 'ProbandCategoryDaoTransformTest.testProbandCategoryVOToEntity' not implemented!");
	}
}