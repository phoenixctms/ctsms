// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MoneyTransfer
*/
@Test(groups = { "transform", "MoneyTransferDao" })
public class MoneyTransferDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MoneyTransferDao.toMoneyTransferInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MoneyTransferDao#toMoneyTransferInVO(org.phoenixctms.ctsms.domain.MoneyTransfer source, org.phoenixctms.ctsms.vo.MoneyTransferInVO target)
	 */
	@Test
	public void testToMoneyTransferInVO() {
		Assert.fail("Test 'MoneyTransferDaoTransformTest.testToMoneyTransferInVO' not implemented!");
	}

	/**
	* Test for method moneyTransferInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MoneyTransferDao#moneyTransferInVOToEntity(org.phoenixctms.ctsms.vo.MoneyTransferInVO source, org.phoenixctms.ctsms.domain.MoneyTransfer target, boolean copyIfNull)
	*/
	@Test
	public void testMoneyTransferInVOToEntity() {
		Assert.fail("Test 'MoneyTransferDaoTransformTest.testMoneyTransferInVOToEntity' not implemented!");
	}

	/**
	 * Test for method MoneyTransferDao.toMoneyTransferOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MoneyTransferDao#toMoneyTransferOutVO(org.phoenixctms.ctsms.domain.MoneyTransfer source, org.phoenixctms.ctsms.vo.MoneyTransferOutVO target)
	 */
	@Test
	public void testToMoneyTransferOutVO() {
		Assert.fail("Test 'MoneyTransferDaoTransformTest.testToMoneyTransferOutVO' not implemented!");
	}

	/**
	* Test for method moneyTransferOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MoneyTransferDao#moneyTransferOutVOToEntity(org.phoenixctms.ctsms.vo.MoneyTransferOutVO source, org.phoenixctms.ctsms.domain.MoneyTransfer target, boolean copyIfNull)
	*/
	@Test
	public void testMoneyTransferOutVOToEntity() {
		Assert.fail("Test 'MoneyTransferDaoTransformTest.testMoneyTransferOutVOToEntity' not implemented!");
	}
}