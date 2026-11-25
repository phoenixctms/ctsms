// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2026.
//
package org.phoenixctms.ctsms.adapt;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A course chain is extended by renewal courses. Only the newest course of a chain (per staff: the newest one taken) may remind.
 */
public class CourseExpirationAdapterTest {

	private static final Date TODAY = date(2026, 11, 25);
	private static long nextId = 1l;

	private static Date date(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day).getTime();
	}

	private static Course course(Date stop, boolean expires) {
		Course course = Course.Factory.newInstance();
		course.setId(nextId++);
		course.setStop(stop);
		course.setExpires(expires);
		if (expires) {
			course.setValidityPeriod(VariablePeriod.YEAR);
		}
		course.setRenewals(new LinkedHashSet<Course>());
		course.setPrecedingCourses(new LinkedHashSet<Course>());
		return course;
	}

	private static void renew(Course course, Course... renewals) {
		for (Course renewal : renewals) {
			course.getRenewals().add(renewal);
			renewal.getPrecedingCourses().add(course);
		}
	}

	private static Staff staff() {
		Staff staff = Staff.Factory.newInstance();
		staff.setId(nextId++);
		staff.setParticipations(new LinkedHashSet<CourseParticipationStatusEntry>());
		return staff;
	}

	private static CourseParticipationStatusType registered() {
		CourseParticipationStatusType status = CourseParticipationStatusType.Factory.newInstance();
		status.setAdminInitial(true);
		return status;
	}

	private static CourseParticipationStatusType passed() {
		CourseParticipationStatusType status = CourseParticipationStatusType.Factory.newInstance();
		status.setPass(true);
		return status;
	}

	private static CourseParticipationStatusType cancelled() {
		CourseParticipationStatusType status = CourseParticipationStatusType.Factory.newInstance();
		status.setCancel(true);
		return status;
	}

	private static CourseParticipationStatusEntry participate(Staff staff, Course course, CourseParticipationStatusType status) {
		CourseParticipationStatusEntry participation = CourseParticipationStatusEntry.Factory.newInstance();
		participation.setId(nextId++);
		participation.setStaff(staff);
		participation.setCourse(course);
		participation.setStatus(status);
		staff.getParticipations().add(participation);
		return participation;
	}

	private static boolean active(Object item) {
		return ExpirationEntityAdapter.getInstance(item, TODAY, new HashMap()).isActive();
	}

	private static boolean reminderDue(Object item) {
		return ExpirationEntityAdapter.getInstance(item, TODAY, new HashMap()).isReminderDue(false, null, null, VariablePeriod.MONTH, null);
	}

	@Test
	public void testSingleCourse() {
		Assert.assertTrue(active(course(date(2025, 1, 1), true)));
		Assert.assertFalse(active(course(date(2025, 1, 1), false)));
	}

	@Test
	public void testRenewedCourseIsInactive() {
		Course a = course(date(2024, 1, 1), true);
		Course b = course(date(2026, 6, 1), true);
		renew(a, b);
		Assert.assertFalse(active(a));
		Assert.assertTrue(active(b));
	}

	@Test
	public void testExpiredRenewalActivatesChain() {
		Course a = course(date(2023, 1, 1), true);
		Course b = course(date(2024, 1, 1), true);
		Course c = course(date(2025, 1, 1), true);
		renew(a, b);
		renew(b, c);
		Assert.assertTrue(active(a));
		Assert.assertTrue(active(b));
		Assert.assertTrue(active(c));
	}

	@Test
	public void testNonExpiringRenewalWinsInAnyOrder() {
		Course a1 = course(date(2023, 1, 1), true);
		Course nonExpiring1 = course(date(2023, 6, 1), false);
		Course expiring1 = course(date(2024, 6, 1), true);
		renew(a1, nonExpiring1, expiring1);
		Assert.assertFalse(active(a1));
		Course a2 = course(date(2023, 1, 1), true);
		Course nonExpiring2 = course(date(2023, 6, 1), false);
		Course expiring2 = course(date(2024, 6, 1), true);
		renew(a2, expiring2, nonExpiring2);
		Assert.assertFalse(active(a2));
	}

	@Test
	public void testReminderNotDueForRenewedOrLongExpiredCourse() {
		Course expiringSoon = course(date(2025, 12, 10), true);
		Assert.assertTrue(reminderDue(expiringSoon));
		Course renewed = course(date(2025, 12, 10), true);
		renew(renewed, course(date(2026, 10, 1), true));
		Assert.assertFalse(reminderDue(renewed));
		Course longExpired = course(date(2019, 1, 1), true);
		Assert.assertTrue(active(longExpired));
		Assert.assertFalse(reminderDue(longExpired));
	}

	@Test
	public void testInitialAndPassedStatusesAreTaken() {
		Assert.assertTrue(CourseParticipationStatusEntryExpirationAdapter.isTaken(registered()));
		Assert.assertTrue(CourseParticipationStatusEntryExpirationAdapter.isTaken(passed()));
		Assert.assertFalse(CourseParticipationStatusEntryExpirationAdapter.isTaken(cancelled()));
	}

	@Test
	public void testParticipationRenewedByRegisteredRenewal() {
		Course a = course(date(2024, 1, 1), true);
		Course b = course(date(2026, 6, 1), true);
		renew(a, b);
		Staff staff = staff();
		CourseParticipationStatusEntry participationA = participate(staff, a, registered());
		Assert.assertTrue(active(participationA));
		participate(staff, b, registered());
		Assert.assertFalse(active(participationA));
	}

	@Test
	public void testParticipationNotRenewedByCancelledRenewal() {
		Course a = course(date(2024, 1, 1), true);
		Course b = course(date(2026, 6, 1), true);
		renew(a, b);
		Staff staff = staff();
		CourseParticipationStatusEntry participationA = participate(staff, a, passed());
		participate(staff, b, cancelled());
		Assert.assertTrue(active(participationA));
	}

	@Test
	public void testCancelledParticipationIsInactive() {
		Course a = course(date(2025, 1, 1), true);
		Assert.assertFalse(active(participate(staff(), a, cancelled())));
	}

	@Test
	public void testParticipationRenewedAcrossSkippedCourse() {
		Course a = course(date(2023, 1, 1), true);
		Course b = course(date(2024, 1, 1), true);
		Course c = course(date(2026, 6, 1), true);
		renew(a, b);
		renew(b, c);
		Staff staff = staff();
		CourseParticipationStatusEntry participationA = participate(staff, a, registered());
		participate(staff, c, registered());
		Assert.assertFalse(active(participationA));
	}

	@Test
	public void testParticipationRenewedAcrossSkippedNonExpiringCourse() {
		Course a = course(date(2023, 1, 1), true);
		Course b = course(date(2024, 1, 1), false);
		Course c = course(date(2026, 6, 1), true);
		renew(a, b);
		renew(b, c);
		Staff staff = staff();
		CourseParticipationStatusEntry participationA = participate(staff, a, registered());
		Assert.assertTrue(active(participationA));
		participate(staff, c, registered());
		Assert.assertFalse(active(participationA));
	}

	@Test
	public void testTakenNonExpiringCourseEndsChain() {
		Course a = course(date(2023, 1, 1), true);
		Course b = course(date(2024, 1, 1), false);
		Course c = course(date(2025, 1, 1), true);
		renew(a, b);
		renew(b, c);
		Staff staff = staff();
		CourseParticipationStatusEntry participationA = participate(staff, a, registered());
		participate(staff, b, registered());
		participate(staff, c, registered());
		Assert.assertFalse(active(participationA));
	}
}
