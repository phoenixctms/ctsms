// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Criteria
*/
@Test(groups = { "transform", "CriteriaDao" })
public class CriteriaDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CriteriaDao.toCriteriaInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CriteriaDao#toCriteriaInVO(org.phoenixctms.ctsms.domain.Criteria source, org.phoenixctms.ctsms.vo.CriteriaInVO target)
	 */
	@Test
	public void testToCriteriaInVO() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testToCriteriaInVO' not implemented!");
	}

	/**
	* Test for method criteriaInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CriteriaDao#criteriaInVOToEntity(org.phoenixctms.ctsms.vo.CriteriaInVO source, org.phoenixctms.ctsms.domain.Criteria target, boolean copyIfNull)
	*/
	@Test
	public void testCriteriaInVOToEntity() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testCriteriaInVOToEntity' not implemented!");
	}

	/**
	 * Test for method CriteriaDao.toCriteriaOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CriteriaDao#toCriteriaOutVO(org.phoenixctms.ctsms.domain.Criteria source, org.phoenixctms.ctsms.vo.CriteriaOutVO target)
	 */
	@Test
	public void testToCriteriaOutVO() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testToCriteriaOutVO' not implemented!");
	}

	/**
	* Test for method criteriaOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CriteriaDao#criteriaOutVOToEntity(org.phoenixctms.ctsms.vo.CriteriaOutVO source, org.phoenixctms.ctsms.domain.Criteria target, boolean copyIfNull)
	*/
	@Test
	public void testCriteriaOutVOToEntity() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testCriteriaOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method CriteriaDao.toCriteriaInstantVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CriteriaDao#toCriteriaInstantVO(org.phoenixctms.ctsms.domain.Criteria source, org.phoenixctms.ctsms.vo.CriteriaInstantVO target)
	 */
	@Test
	public void testToCriteriaInstantVO() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testToCriteriaInstantVO' not implemented!");
	}

	/**
	* Test for method criteriaInstantVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CriteriaDao#criteriaInstantVOToEntity(org.phoenixctms.ctsms.vo.CriteriaInstantVO source, org.phoenixctms.ctsms.domain.Criteria target, boolean copyIfNull)
	*/
	@Test
	public void testCriteriaInstantVOToEntity() {
		Assert.fail("Test 'CriteriaDaoTransformTest.testCriteriaInstantVOToEntity' not implemented!");
	}
}