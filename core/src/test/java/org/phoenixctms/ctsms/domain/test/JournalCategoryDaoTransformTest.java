// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.JournalCategory
*/
@Test(groups = { "transform", "JournalCategoryDao" })
public class JournalCategoryDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method JournalCategoryDao.toJournalCategoryVO
	 *
	 * @see org.phoenixctms.ctsms.domain.JournalCategoryDao#toJournalCategoryVO(org.phoenixctms.ctsms.domain.JournalCategory source, org.phoenixctms.ctsms.vo.JournalCategoryVO target)
	 */
	@Test
	public void testToJournalCategoryVO() {
		Assert.fail("Test 'JournalCategoryDaoTransformTest.testToJournalCategoryVO' not implemented!");
	}

	/**
	* Test for method journalCategoryVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.JournalCategoryDao#journalCategoryVOToEntity(org.phoenixctms.ctsms.vo.JournalCategoryVO source, org.phoenixctms.ctsms.domain.JournalCategory target, boolean copyIfNull)
	*/
	@Test
	public void testJournalCategoryVOToEntity() {
		Assert.fail("Test 'JournalCategoryDaoTransformTest.testJournalCategoryVOToEntity' not implemented!");
	}
}