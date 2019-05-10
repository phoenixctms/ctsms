// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.AddressType
*/
@Test(groups = { "transform", "AddressTypeDao" })
public class AddressTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method AddressTypeDao.toAddressTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.AddressTypeDao#toAddressTypeVO(org.phoenixctms.ctsms.domain.AddressType source, org.phoenixctms.ctsms.vo.AddressTypeVO target)
	 */
	@Test
	public void testToAddressTypeVO() {
		Assert.fail("Test 'AddressTypeDaoTransformTest.testToAddressTypeVO' not implemented!");
	}

	/**
	* Test for method addressTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.AddressTypeDao#addressTypeVOToEntity(org.phoenixctms.ctsms.vo.AddressTypeVO source, org.phoenixctms.ctsms.domain.AddressType target, boolean copyIfNull)
	*/
	@Test
	public void testAddressTypeVOToEntity() {
		Assert.fail("Test 'AddressTypeDaoTransformTest.testAddressTypeVOToEntity' not implemented!");
	}
}