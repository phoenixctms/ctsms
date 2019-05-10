// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TimelineEventType
*/
@Test(groups = { "transform", "TimelineEventTypeDao" })
public class TimelineEventTypeDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TimelineEventTypeDao.toTimelineEventTypeVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TimelineEventTypeDao#toTimelineEventTypeVO(org.phoenixctms.ctsms.domain.TimelineEventType source, org.phoenixctms.ctsms.vo.TimelineEventTypeVO target)
	 */
	@Test
	public void testToTimelineEventTypeVO() {
		Assert.fail("Test 'TimelineEventTypeDaoTransformTest.testToTimelineEventTypeVO' not implemented!");
	}

	/**
	* Test for method timelineEventTypeVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TimelineEventTypeDao#timelineEventTypeVOToEntity(org.phoenixctms.ctsms.vo.TimelineEventTypeVO source, org.phoenixctms.ctsms.domain.TimelineEventType target, boolean copyIfNull)
	*/
	@Test
	public void testTimelineEventTypeVOToEntity() {
		Assert.fail("Test 'TimelineEventTypeDaoTransformTest.testTimelineEventTypeVOToEntity' not implemented!");
	}
}