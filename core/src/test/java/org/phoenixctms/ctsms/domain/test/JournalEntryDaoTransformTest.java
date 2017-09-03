// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.JournalEntry
*/
@Test(groups={"transform","JournalEntryDao"})
public class JournalEntryDaoTransformTest extends DaoTransformTestBase {

  /**
   * Test for method JournalEntryDao.toJournalEntryInVO
   *
   * @see org.phoenixctms.ctsms.domain.JournalEntryDao#toJournalEntryInVO(org.phoenixctms.ctsms.domain.JournalEntry source, org.phoenixctms.ctsms.vo.JournalEntryInVO target)
   */
  @Test
  public void testToJournalEntryInVO() {
    Assert.fail("Test 'JournalEntryDaoTransformTest.testToJournalEntryInVO' not implemented!");
  }

    /**
   * Test for method journalEntryInVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JournalEntryDao#journalEntryInVOToEntity(org.phoenixctms.ctsms.vo.JournalEntryInVO source, org.phoenixctms.ctsms.domain.JournalEntry target, boolean copyIfNull)
   */
  @Test
  public void testJournalEntryInVOToEntity() {
    Assert.fail("Test 'JournalEntryDaoTransformTest.testJournalEntryInVOToEntity' not implemented!");
  }

  /**
   * Test for method JournalEntryDao.toJournalEntryOutVO
   *
   * @see org.phoenixctms.ctsms.domain.JournalEntryDao#toJournalEntryOutVO(org.phoenixctms.ctsms.domain.JournalEntry source, org.phoenixctms.ctsms.vo.JournalEntryOutVO target)
   */
  @Test
  public void testToJournalEntryOutVO() {
    Assert.fail("Test 'JournalEntryDaoTransformTest.testToJournalEntryOutVO' not implemented!");
  }

    /**
   * Test for method journalEntryOutVOToEntity
   *
   * @see org.phoenixctms.ctsms.domain.JournalEntryDao#journalEntryOutVOToEntity(org.phoenixctms.ctsms.vo.JournalEntryOutVO source, org.phoenixctms.ctsms.domain.JournalEntry target, boolean copyIfNull)
   */
  @Test
  public void testJournalEntryOutVOToEntity() {
    Assert.fail("Test 'JournalEntryDaoTransformTest.testJournalEntryOutVOToEntity' not implemented!");
  }

}