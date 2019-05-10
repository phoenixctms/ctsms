// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.TimelineEvent
*/
@Test(groups = { "transform", "TimelineEventDao" })
public class TimelineEventDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method TimelineEventDao.toTimelineEventInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TimelineEventDao#toTimelineEventInVO(org.phoenixctms.ctsms.domain.TimelineEvent source, org.phoenixctms.ctsms.vo.TimelineEventInVO target)
	 */
	@Test
	public void testToTimelineEventInVO() {
		Assert.fail("Test 'TimelineEventDaoTransformTest.testToTimelineEventInVO' not implemented!");
	}

	/**
	* Test for method timelineEventInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TimelineEventDao#timelineEventInVOToEntity(org.phoenixctms.ctsms.vo.TimelineEventInVO source, org.phoenixctms.ctsms.domain.TimelineEvent target, boolean copyIfNull)
	*/
	@Test
	public void testTimelineEventInVOToEntity() {
		Assert.fail("Test 'TimelineEventDaoTransformTest.testTimelineEventInVOToEntity' not implemented!");
	}

	/**
	 * Test for method TimelineEventDao.toTimelineEventOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.TimelineEventDao#toTimelineEventOutVO(org.phoenixctms.ctsms.domain.TimelineEvent source, org.phoenixctms.ctsms.vo.TimelineEventOutVO target)
	 */
	@Test
	public void testToTimelineEventOutVO() {
		Assert.fail("Test 'TimelineEventDaoTransformTest.testToTimelineEventOutVO' not implemented!");
	}

	/**
	* Test for method timelineEventOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.TimelineEventDao#timelineEventOutVOToEntity(org.phoenixctms.ctsms.vo.TimelineEventOutVO source, org.phoenixctms.ctsms.domain.TimelineEvent target, boolean copyIfNull)
	*/
	@Test
	public void testTimelineEventOutVOToEntity() {
		Assert.fail("Test 'TimelineEventDaoTransformTest.testTimelineEventOutVOToEntity' not implemented!");
	}
}