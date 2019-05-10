// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.MassMailType
*/
@Test(groups = { "transform", "MassMailTypeDao" })
public class MassMailTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method MassMailTypeDao.toMassMailTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.MassMailTypeDao#toMassMailTypeVO(org.phoenixctms.ctsms.domain.MassMailType source, org.phoenixctms.ctsms.vo.MassMailTypeVO target)
	 */
	@Test
	public void testToMassMailTypeVO() {
		Assert.fail("Test 'MassMailTypeDaoTransformTest.testToMassMailTypeVO' not implemented!");
	}

	/**
	* Test for method massMailTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.MassMailTypeDao#massMailTypeVOToEntity(org.phoenixctms.ctsms.vo.MassMailTypeVO source, org.phoenixctms.ctsms.domain.MassMailType target, boolean copyIfNull)
	*/
	@Test
	public void testMassMailTypeVOToEntity() {
		Assert.fail("Test 'MassMailTypeDaoTransformTest.testMassMailTypeVOToEntity' not implemented!");
	}
}