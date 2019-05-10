// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.InventoryStatusEntry
*/
@Test(groups = { "transform", "InventoryStatusEntryDao" })
public class InventoryStatusEntryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method InventoryStatusEntryDao.toInventoryStatusEntryInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.InventoryStatusEntryDao#toInventoryStatusEntryInVO(org.phoenixctms.ctsms.domain.InventoryStatusEntry source, org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO target)
	 */
	@Test
	public void testToInventoryStatusEntryInVO() {
		Assert.fail("Test 'InventoryStatusEntryDaoTransformTest.testToInventoryStatusEntryInVO' not implemented!");
	}

	/**
	* Test for method inventoryStatusEntryInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.InventoryStatusEntryDao#inventoryStatusEntryInVOToEntity(org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO source, org.phoenixctms.ctsms.domain.InventoryStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testInventoryStatusEntryInVOToEntity() {
		Assert.fail("Test 'InventoryStatusEntryDaoTransformTest.testInventoryStatusEntryInVOToEntity' not implemented!");
	}

	/**
	 * Test for method InventoryStatusEntryDao.toInventoryStatusEntryOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.InventoryStatusEntryDao#toInventoryStatusEntryOutVO(org.phoenixctms.ctsms.domain.InventoryStatusEntry source, org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO target)
	 */
	@Test
	public void testToInventoryStatusEntryOutVO() {
		Assert.fail("Test 'InventoryStatusEntryDaoTransformTest.testToInventoryStatusEntryOutVO' not implemented!");
	}

	/**
	* Test for method inventoryStatusEntryOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.InventoryStatusEntryDao#inventoryStatusEntryOutVOToEntity(org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO source, org.phoenixctms.ctsms.domain.InventoryStatusEntry target, boolean copyIfNull)
	*/
	@Test
	public void testInventoryStatusEntryOutVOToEntity() {
		Assert.fail("Test 'InventoryStatusEntryDaoTransformTest.testInventoryStatusEntryOutVOToEntity' not implemented!");
	}
}