// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Lecturer
*/
@Test(groups = { "transform", "LecturerDao" })
public class LecturerDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method LecturerDao.toLecturerInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.LecturerDao#toLecturerInVO(org.phoenixctms.ctsms.domain.Lecturer source, org.phoenixctms.ctsms.vo.LecturerInVO target)
	 */
	@Test
	public void testToLecturerInVO() {
		Assert.fail("Test 'LecturerDaoTransformTest.testToLecturerInVO' not implemented!");
	}

	/**
	* Test for method lecturerInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.LecturerDao#lecturerInVOToEntity(org.phoenixctms.ctsms.vo.LecturerInVO source, org.phoenixctms.ctsms.domain.Lecturer target, boolean copyIfNull)
	*/
	@Test
	public void testLecturerInVOToEntity() {
		Assert.fail("Test 'LecturerDaoTransformTest.testLecturerInVOToEntity' not implemented!");
	}

	/**
	 * Test for method LecturerDao.toLecturerOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.LecturerDao#toLecturerOutVO(org.phoenixctms.ctsms.domain.Lecturer source, org.phoenixctms.ctsms.vo.LecturerOutVO target)
	 */
	@Test
	public void testToLecturerOutVO() {
		Assert.fail("Test 'LecturerDaoTransformTest.testToLecturerOutVO' not implemented!");
	}

	/**
	* Test for method lecturerOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.LecturerDao#lecturerOutVOToEntity(org.phoenixctms.ctsms.vo.LecturerOutVO source, org.phoenixctms.ctsms.domain.Lecturer target, boolean copyIfNull)
	*/
	@Test
	public void testLecturerOutVOToEntity() {
		Assert.fail("Test 'LecturerDaoTransformTest.testLecturerOutVOToEntity' not implemented!");
	}
}