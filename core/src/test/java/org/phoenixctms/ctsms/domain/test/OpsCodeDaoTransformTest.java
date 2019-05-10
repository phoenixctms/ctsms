// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.OpsCode
*/
@Test(groups = { "transform", "OpsCodeDao" })
public class OpsCodeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method OpsCodeDao.toOpsCodeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.OpsCodeDao#toOpsCodeVO(org.phoenixctms.ctsms.domain.OpsCode source, org.phoenixctms.ctsms.vo.OpsCodeVO target)
	 */
	@Test
	public void testToOpsCodeVO() {
		Assert.fail("Test 'OpsCodeDaoTransformTest.testToOpsCodeVO' not implemented!");
	}

	/**
	* Test for method opsCodeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.OpsCodeDao#opsCodeVOToEntity(org.phoenixctms.ctsms.vo.OpsCodeVO source, org.phoenixctms.ctsms.domain.OpsCode target, boolean copyIfNull)
	*/
	@Test
	public void testOpsCodeVOToEntity() {
		Assert.fail("Test 'OpsCodeDaoTransformTest.testOpsCodeVOToEntity' not implemented!");
	}
}