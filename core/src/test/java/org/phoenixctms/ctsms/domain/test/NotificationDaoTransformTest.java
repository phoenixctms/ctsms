// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Notification
*/
@Test(groups={"transform","NotificationDao"})
public class NotificationDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method NotificationDao.toNotificationVO
   *
   * @see org.phoenixctms.ctsms.domain.NotificationDao#toNotificationVO(org.phoenixctms.ctsms.domain.Notification source, org.phoenixctms.ctsms.vo.NotificationVO target)
   */
  @Test
  public void testToNotificationVO() {
    Assert.fail("Test 'NotificationDaoTransformTest.testToNotificationVO' not implemented!");
  }

    /**
   * Test for method notificationVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.NotificationDao#notificationVOToEntity(org.phoenixctms.ctsms.vo.NotificationVO source, org.phoenixctms.ctsms.domain.Notification target, boolean copyIfNull)
   */
  @Test
  public void testNotificationVOToEntity() {
    Assert.fail("Test 'NotificationDaoTransformTest.testNotificationVOToEntity' not implemented!");
  }

}