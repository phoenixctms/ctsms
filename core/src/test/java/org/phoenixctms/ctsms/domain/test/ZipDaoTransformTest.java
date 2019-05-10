// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Zip
*/
@Test(groups = { "transform", "ZipDao" })
public class ZipDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method ZipDao.toZipVO
	 *
	 * @see org.phoenixctms.ctsms.domain.ZipDao#toZipVO(org.phoenixctms.ctsms.domain.Zip source, org.phoenixctms.ctsms.vo.ZipVO target)
	 */
	@Test
	public void testToZipVO() {
		Assert.fail("Test 'ZipDaoTransformTest.testToZipVO' not implemented!");
	}

	/**
	* Test for method zipVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.ZipDao#zipVOToEntity(org.phoenixctms.ctsms.vo.ZipVO source, org.phoenixctms.ctsms.domain.Zip target, boolean copyIfNull)
	*/
	@Test
	public void testZipVOToEntity() {
		Assert.fail("Test 'ZipDaoTransformTest.testZipVOToEntity' not implemented!");
	}
}