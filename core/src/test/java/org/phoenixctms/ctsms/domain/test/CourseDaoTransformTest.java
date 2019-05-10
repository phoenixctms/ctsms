// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Course
*/
@Test(groups = { "transform", "CourseDao" })
public class CourseDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method CourseDao.toCourseOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CourseDao#toCourseOutVO(org.phoenixctms.ctsms.domain.Course source, org.phoenixctms.ctsms.vo.CourseOutVO target)
	 */
	@Test
	public void testToCourseOutVO() {
		Assert.fail("Test 'CourseDaoTransformTest.testToCourseOutVO' not implemented!");
	}

	/**
	* Test for method courseOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CourseDao#courseOutVOToEntity(org.phoenixctms.ctsms.vo.CourseOutVO source, org.phoenixctms.ctsms.domain.Course target, boolean copyIfNull)
	*/
	@Test
	public void testCourseOutVOToEntity() {
		Assert.fail("Test 'CourseDaoTransformTest.testCourseOutVOToEntity' not implemented!");
	}

	/**
	 * Test for method CourseDao.toCourseInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.CourseDao#toCourseInVO(org.phoenixctms.ctsms.domain.Course source, org.phoenixctms.ctsms.vo.CourseInVO target)
	 */
	@Test
	public void testToCourseInVO() {
		Assert.fail("Test 'CourseDaoTransformTest.testToCourseInVO' not implemented!");
	}

	/**
	* Test for method courseInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.CourseDao#courseInVOToEntity(org.phoenixctms.ctsms.vo.CourseInVO source, org.phoenixctms.ctsms.domain.Course target, boolean copyIfNull)
	*/
	@Test
	public void testCourseInVOToEntity() {
		Assert.fail("Test 'CourseDaoTransformTest.testCourseInVOToEntity' not implemented!");
	}
}