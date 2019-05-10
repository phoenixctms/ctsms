// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.LecturerCompetence
*/
@Test(groups = { "transform", "LecturerCompetenceDao" })
public class LecturerCompetenceDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method LecturerCompetenceDao.toLecturerCompetenceVO
	 *
	 * @see org.phoenixctms.ctsms.domain.LecturerCompetenceDao#toLecturerCompetenceVO(org.phoenixctms.ctsms.domain.LecturerCompetence source, org.phoenixctms.ctsms.vo.LecturerCompetenceVO target)
	 */
	@Test
	public void testToLecturerCompetenceVO() {
		Assert.fail("Test 'LecturerCompetenceDaoTransformTest.testToLecturerCompetenceVO' not implemented!");
	}

	/**
	* Test for method lecturerCompetenceVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.LecturerCompetenceDao#lecturerCompetenceVOToEntity(org.phoenixctms.ctsms.vo.LecturerCompetenceVO source, org.phoenixctms.ctsms.domain.LecturerCompetence target, boolean copyIfNull)
	*/
	@Test
	public void testLecturerCompetenceVOToEntity() {
		Assert.fail("Test 'LecturerCompetenceDaoTransformTest.testLecturerCompetenceVOToEntity' not implemented!");
	}
}