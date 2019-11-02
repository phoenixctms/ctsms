// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Announcement
*/
@Test(groups={"transform","AnnouncementDao"})
public class AnnouncementDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method AnnouncementDao.toAnnouncementVO
   *
   * @see org.phoenixctms.ctsms.domain.AnnouncementDao#toAnnouncementVO(org.phoenixctms.ctsms.domain.Announcement source, org.phoenixctms.ctsms.vo.AnnouncementVO target)
   */
  @Test
  public void testToAnnouncementVO() {
    Assert.fail("Test 'AnnouncementDaoTransformTest.testToAnnouncementVO' not implemented!");
  }

    /**
   * Test for method announcementVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.AnnouncementDao#announcementVOToEntity(org.phoenixctms.ctsms.vo.AnnouncementVO source, org.phoenixctms.ctsms.domain.Announcement target, boolean copyIfNull)
   */
  @Test
  public void testAnnouncementVOToEntity() {
    Assert.fail("Test 'AnnouncementDaoTransformTest.testAnnouncementVOToEntity' not implemented!");
  }

}