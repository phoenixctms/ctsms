// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.BankAccount
*/
@Test(groups = { "transform", "BankAccountDao" })
public class BankAccountDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method BankAccountDao.toBankAccountInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.BankAccountDao#toBankAccountInVO(org.phoenixctms.ctsms.domain.BankAccount source, org.phoenixctms.ctsms.vo.BankAccountInVO target)
	 */
	@Test
	public void testToBankAccountInVO() {
		Assert.fail("Test 'BankAccountDaoTransformTest.testToBankAccountInVO' not implemented!");
	}

	/**
	* Test for method bankAccountInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.BankAccountDao#bankAccountInVOToEntity(org.phoenixctms.ctsms.vo.BankAccountInVO source, org.phoenixctms.ctsms.domain.BankAccount target, boolean copyIfNull)
	*/
	@Test
	public void testBankAccountInVOToEntity() {
		Assert.fail("Test 'BankAccountDaoTransformTest.testBankAccountInVOToEntity' not implemented!");
	}

	/**
	 * Test for method BankAccountDao.toBankAccountOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.BankAccountDao#toBankAccountOutVO(org.phoenixctms.ctsms.domain.BankAccount source, org.phoenixctms.ctsms.vo.BankAccountOutVO target)
	 */
	@Test
	public void testToBankAccountOutVO() {
		Assert.fail("Test 'BankAccountDaoTransformTest.testToBankAccountOutVO' not implemented!");
	}

	/**
	* Test for method bankAccountOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.BankAccountDao#bankAccountOutVOToEntity(org.phoenixctms.ctsms.vo.BankAccountOutVO source, org.phoenixctms.ctsms.domain.BankAccount target, boolean copyIfNull)
	*/
	@Test
	public void testBankAccountOutVOToEntity() {
		Assert.fail("Test 'BankAccountDaoTransformTest.testBankAccountOutVOToEntity' not implemented!");
	}
}