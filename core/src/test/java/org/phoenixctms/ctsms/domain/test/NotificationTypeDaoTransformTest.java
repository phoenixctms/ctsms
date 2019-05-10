// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.NotificationType
*/
@Test(groups = { "transform", "NotificationTypeDao" })
public class NotificationTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method NotificationTypeDao.toNotificationTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.NotificationTypeDao#toNotificationTypeVO(org.phoenixctms.ctsms.domain.NotificationType source, org.phoenixctms.ctsms.vo.NotificationTypeVO target)
	 */
	@Test
	public void testToNotificationTypeVO() {
		Assert.fail("Test 'NotificationTypeDaoTransformTest.testToNotificationTypeVO' not implemented!");
	}

	/**
	* Test for method notificationTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.NotificationTypeDao#notificationTypeVOToEntity(org.phoenixctms.ctsms.vo.NotificationTypeVO source, org.phoenixctms.ctsms.domain.NotificationType target, boolean copyIfNull)
	*/
	@Test
	public void testNotificationTypeVOToEntity() {
		Assert.fail("Test 'NotificationTypeDaoTransformTest.testNotificationTypeVOToEntity' not implemented!");
	}
}