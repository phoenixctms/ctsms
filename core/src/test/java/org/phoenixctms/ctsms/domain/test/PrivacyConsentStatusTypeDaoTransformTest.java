// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.PrivacyConsentStatusType
*/
@Test(groups = { "transform", "PrivacyConsentStatusTypeDao" })
public class PrivacyConsentStatusTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method PrivacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao#toPrivacyConsentStatusTypeVO(org.phoenixctms.ctsms.domain.PrivacyConsentStatusType source, org.phoenixctms.ctsms.vo.PrivacyConsentStatusTypeVO target)
	 */
	@Test
	public void testToPrivacyConsentStatusTypeVO() {
		Assert.fail("Test 'PrivacyConsentStatusTypeDaoTransformTest.testToPrivacyConsentStatusTypeVO' not implemented!");
	}

	/**
	* Test for method privacyConsentStatusTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao#privacyConsentStatusTypeVOToEntity(org.phoenixctms.ctsms.vo.PrivacyConsentStatusTypeVO source, org.phoenixctms.ctsms.domain.PrivacyConsentStatusType target, boolean copyIfNull)
	*/
	@Test
	public void testPrivacyConsentStatusTypeVOToEntity() {
		Assert.fail("Test 'PrivacyConsentStatusTypeDaoTransformTest.testPrivacyConsentStatusTypeVOToEntity' not implemented!");
	}
}