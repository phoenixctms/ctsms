// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Procedure
*/
@Test(groups = { "transform", "ProcedureDao" })
public class ProcedureDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ProcedureDao.toProcedureInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProcedureDao#toProcedureInVO(org.phoenixctms.ctsms.domain.Procedure source, org.phoenixctms.ctsms.vo.ProcedureInVO target)
	 */
	@Test
	public void testToProcedureInVO() {
		Assert.fail("Test 'ProcedureDaoTransformTest.testToProcedureInVO' not implemented!");
	}

	/**
	* Test for method procedureInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProcedureDao#procedureInVOToEntity(org.phoenixctms.ctsms.vo.ProcedureInVO source, org.phoenixctms.ctsms.domain.Procedure target, boolean copyIfNull)
	*/
	@Test
	public void testProcedureInVOToEntity() {
		Assert.fail("Test 'ProcedureDaoTransformTest.testProcedureInVOToEntity' not implemented!");
	}

	/**
	 * Test for method ProcedureDao.toProcedureOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ProcedureDao#toProcedureOutVO(org.phoenixctms.ctsms.domain.Procedure source, org.phoenixctms.ctsms.vo.ProcedureOutVO target)
	 */
	@Test
	public void testToProcedureOutVO() {
		Assert.fail("Test 'ProcedureDaoTransformTest.testToProcedureOutVO' not implemented!");
	}

	/**
	* Test for method procedureOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ProcedureDao#procedureOutVOToEntity(org.phoenixctms.ctsms.vo.ProcedureOutVO source, org.phoenixctms.ctsms.domain.Procedure target, boolean copyIfNull)
	*/
	@Test
	public void testProcedureOutVOToEntity() {
		Assert.fail("Test 'ProcedureDaoTransformTest.testProcedureOutVOToEntity' not implemented!");
	}
}