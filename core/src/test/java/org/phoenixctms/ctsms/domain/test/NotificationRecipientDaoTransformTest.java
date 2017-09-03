// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.NotificationRecipient
*/
@Test(groups={"transform","NotificationRecipientDao"})
public class NotificationRecipientDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method NotificationRecipientDao.toNotificationRecipientVO
   *
   * @see org.phoenixctms.ctsms.domain.NotificationRecipientDao#toNotificationRecipientVO(org.phoenixctms.ctsms.domain.NotificationRecipient source, org.phoenixctms.ctsms.vo.NotificationRecipientVO target)
   */
  @Test
  public void testToNotificationRecipientVO() {
    Assert.fail("Test 'NotificationRecipientDaoTransformTest.testToNotificationRecipientVO' not implemented!");
  }

    /**
   * Test for method notificationRecipientVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.NotificationRecipientDao#notificationRecipientVOToEntity(org.phoenixctms.ctsms.vo.NotificationRecipientVO source, org.phoenixctms.ctsms.domain.NotificationRecipient target, boolean copyIfNull)
   */
  @Test
  public void testNotificationRecipientVOToEntity() {
    Assert.fail("Test 'NotificationRecipientDaoTransformTest.testNotificationRecipientVOToEntity' not implemented!");
  }

}