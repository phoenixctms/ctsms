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
import org.phoenixctms.ctsms.domain.Staff;

public class CourseParticipationStatusEntryExpirationAdapter extends CourseExpirationAdapterBase {

	private static Set<Long> getParticiaptionCourseIds(Staff staff) {
		Collection<CourseParticipationStatusEntry> participations = staff.getParticipations();
		HashSet<Long> courseIds = new HashSet<Long>(participations.size());
		Iterator<CourseParticipationStatusEntry> it = participations.iterator();
		while (it.hasNext()) {
			CourseParticipationStatusEntry participation = it.next();
			if (participation.getStatus().isPass()) {
				courseIds.add(participation.getCourse().getId());
			}
		}
		return courseIds;
	}

	private CourseParticipationStatusEntry courseParticipationStatusEntry;
	private Map<Long, Set<Long>> particiaptionCourseIdsMap;

	public CourseParticipationStatusEntryExpirationAdapter(Object courseParticipationStatusEntry, Date today, Map... caches) {
		setCaches(caches);
		setToday(today);
		setItem(courseParticipationStatusEntry);
	}

	private Course findNewestParticipating(Course course, Staff staff) {
		LinkedHashMap<Long, Course> resultMap = new LinkedHashMap<Long, Course>();
		findNewestParticipatingLeaves(course, staff, resultMap);
		return findNewest(resultMap.values());
	}

	private void findNewestParticipatingLeaves(Course course, Staff staff, LinkedHashMap<Long, Course> resultMap) {
		if (!resultMap.containsKey(course.getId())) {
			resultMap.put(course.getId(), course);
			if (course.isExpires()) {
				Collection<Course> renewals = course.getRenewals();
				if (renewals != null && renewals.size() > 0) {
					Iterator<Course> it = renewals.iterator();
					while (it.hasNext()) {
						Course renewal = it.next();
						if (getCachedParticiaptionCourseIds(staff).contains(renewal.getId())) {
							findNewestParticipatingLeaves(renewal, staff, resultMap);
						}
					}
				}
			}
		}
	}

	private Set<Long> getCachedParticiaptionCourseIds(Staff staff) {
		Set<Long> particiaptionCourseIds;
		if (particiaptionCourseIdsMap != null) {
			if (particiaptionCourseIdsMap.containsKey(staff.getId())) {
				particiaptionCourseIds = particiaptionCourseIdsMap.get(staff.getId());
			} else {
				particiaptionCourseIds = getParticiaptionCourseIds(staff);
				particiaptionCourseIdsMap.put(staff.getId(), particiaptionCourseIds);
			}
		} else {
			particiaptionCourseIds = getParticiaptionCourseIds(staff);
		}
		return particiaptionCourseIds;
	}

	@Override
	public Object getItem() {
		return courseParticipationStatusEntry;
	}

	@Override
	public void setCaches(Map... caches) {
		particiaptionCourseIdsMap = caches[0];
	}

	@Override
	protected void setItem(Object courseParticipationStatusEntry) {
		this.courseParticipationStatusEntry = (CourseParticipationStatusEntry) courseParticipationStatusEntry;
		course = this.courseParticipationStatusEntry.getCourse();
		newest = findNewestParticipating(course, this.courseParticipationStatusEntry.getStaff());
	}
}
