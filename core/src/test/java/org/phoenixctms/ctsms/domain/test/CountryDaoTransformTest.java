// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Country
*/
@Test(groups = { "transform", "CountryDao" })
public class CountryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CountryDao.toCountryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CountryDao#toCountryVO(org.phoenixctms.ctsms.domain.Country source, org.phoenixctms.ctsms.vo.CountryVO target)
	 */
	@Test
	public void testToCountryVO() {
		Assert.fail("Test 'CountryDaoTransformTest.testToCountryVO' not implemented!");
	}

	/**
	* Test for method countryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CountryDao#countryVOToEntity(org.phoenixctms.ctsms.vo.CountryVO source, org.phoenixctms.ctsms.domain.Country target, boolean copyIfNull)
	*/
	@Test
	public void testCountryVOToEntity() {
		Assert.fail("Test 'CountryDaoTransformTest.testCountryVOToEntity' not implemented!");
	}
}