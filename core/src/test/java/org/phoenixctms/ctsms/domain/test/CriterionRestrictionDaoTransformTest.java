// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.CriterionRestriction
*/
@Test(groups = { "transform", "CriterionRestrictionDao" })
public class CriterionRestrictionDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CriterionRestrictionDao.toCriterionRestrictionVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CriterionRestrictionDao#toCriterionRestrictionVO(org.phoenixctms.ctsms.domain.CriterionRestriction source, org.phoenixctms.ctsms.vo.CriterionRestrictionVO target)
	 */
	@Test
	public void testToCriterionRestrictionVO() {
		Assert.fail("Test 'CriterionRestrictionDaoTransformTest.testToCriterionRestrictionVO' not implemented!");
	}

	/**
	* Test for method criterionRestrictionVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CriterionRestrictionDao#criterionRestrictionVOToEntity(org.phoenixctms.ctsms.vo.CriterionRestrictionVO source, org.phoenixctms.ctsms.domain.CriterionRestriction target, boolean copyIfNull)
	*/
	@Test
	public void testCriterionRestrictionVOToEntity() {
		Assert.fail("Test 'CriterionRestrictionDaoTransformTest.testCriterionRestrictionVOToEntity' not implemented!");
	}
}