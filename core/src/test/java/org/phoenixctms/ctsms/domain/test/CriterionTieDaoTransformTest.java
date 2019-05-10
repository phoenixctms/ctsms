// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CriterionTie
*/
@Test(groups = { "transform", "CriterionTieDao" })
public class CriterionTieDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CriterionTieDao.toCriterionTieVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CriterionTieDao#toCriterionTieVO(org.phoenixctms.ctsms.domain.CriterionTie source, org.phoenixctms.ctsms.vo.CriterionTieVO target)
	 */
	@Test
	public void testToCriterionTieVO() {
		Assert.fail("Test 'CriterionTieDaoTransformTest.testToCriterionTieVO' not implemented!");
	}

	/**
	* Test for method criterionTieVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CriterionTieDao#criterionTieVOToEntity(org.phoenixctms.ctsms.vo.CriterionTieVO source, org.phoenixctms.ctsms.domain.CriterionTie target, boolean copyIfNull)
	*/
	@Test
	public void testCriterionTieVOToEntity() {
		Assert.fail("Test 'CriterionTieDaoTransformTest.testCriterionTieVOToEntity' not implemented!");
	}
}