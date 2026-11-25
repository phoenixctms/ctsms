package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.Staff;

public class CourseParticipationStatusEntryExpirationAdapter extends CourseExpirationAdapterBase {

	public static boolean isTaken(CourseParticipationStatusType status) {
		// most participations keep their initial status, so not only passed ones count
		return status.isPass()
				|| status.isUserInitial()
				|| status.isUserSelfRegistrationInitial()
				|| status.isAdminInitial()
				|| status.isAdminSelfRegistrationInitial();
	}

	private static Set<Long> getTakenCourseIds(Staff staff) {
		Collection<CourseParticipationStatusEntry> participations = staff.getParticipations();
		HashSet<Long> courseIds = new HashSet<Long>(participations.size());
		Iterator<CourseParticipationStatusEntry> it = participations.iterator();
		while (it.hasNext()) {
			CourseParticipationStatusEntry participation = it.next();
			if (isTaken(participation.getStatus())) {
				courseIds.add(participation.getCourse().getId());
			}
		}
		return courseIds;
	}

	private static void findNewestParticipatingLeaves(Course course, Set<Long> takenCourseIds, LinkedHashMap<Long, Course> resultMap, Set<Long> visited) {
		if (visited.add(course.getId())) {
			if (!course.isExpires() && resultMap.containsKey(course.getId())) {
				return;
			}
			Collection<Course> renewals = course.getRenewals();
			if (renewals != null && renewals.size() > 0) {
				Iterator<Course> it = renewals.iterator();
				while (it.hasNext()) {
					Course renewal = it.next();
					if (takenCourseIds.contains(renewal.getId()) && !resultMap.containsKey(renewal.getId())) {
						resultMap.put(renewal.getId(), renewal);
					}
					findNewestParticipatingLeaves(renewal, takenCourseIds, resultMap, visited);
				}
			}
		}
	}

	private CourseParticipationStatusEntry courseParticipationStatusEntry;
	private Map<Long, Set<Long>> takenCourseIdsMap;

	public CourseParticipationStatusEntryExpirationAdapter(Object courseParticipationStatusEntry, Date today, Map... caches) {
		setCaches(caches);
		setToday(today);
		setItem(courseParticipationStatusEntry);
	}

	private Course findNewestParticipating(Course course, Staff staff) {
		LinkedHashMap<Long, Course> resultMap = new LinkedHashMap<Long, Course>();
		resultMap.put(course.getId(), course);
		findNewestParticipatingLeaves(course, getCachedTakenCourseIds(staff), resultMap, new HashSet<Long>());
		return findNewest(resultMap.values());
	}

	private Set<Long> getCachedTakenCourseIds(Staff staff) {
		Set<Long> takenCourseIds;
		if (takenCourseIdsMap != null) {
			if (takenCourseIdsMap.containsKey(staff.getId())) {
				takenCourseIds = takenCourseIdsMap.get(staff.getId());
			} else {
				takenCourseIds = getTakenCourseIds(staff);
				takenCourseIdsMap.put(staff.getId(), takenCourseIds);
			}
		} else {
			takenCourseIds = getTakenCourseIds(staff);
		}
		return takenCourseIds;
	}

	@Override
	public Object getItem() {
		return courseParticipationStatusEntry;
	}

	@Override
	public boolean isActive() {
		return isTaken(courseParticipationStatusEntry.getStatus()) && super.isActive();
	}

	@Override
	public void setCaches(Map... caches) {
		takenCourseIdsMap = caches != null && caches.length > 0 ? caches[0] : null;
	}

	@Override
	protected void setItem(Object courseParticipationStatusEntry) {
		this.courseParticipationStatusEntry = (CourseParticipationStatusEntry) courseParticipationStatusEntry;
		course = this.courseParticipationStatusEntry.getCourse();
		newest = findNewestParticipating(course, this.courseParticipationStatusEntry.getStaff());
	}
}
