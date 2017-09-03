package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.Course;

public class CourseExpirationAdapter extends CourseExpirationAdapterBase {

	private static Course findNewest(Course course) {
		LinkedHashMap<Long, Course> resultMap = new LinkedHashMap<Long, Course>();
		findNewestLeaves(course, resultMap);
		return findNewest(resultMap.values());
	}

	private static void findNewestLeaves(Course course, LinkedHashMap<Long, Course> resultMap) {
		if (!resultMap.containsKey(course.getId())) {
			resultMap.put(course.getId(), course);
			if (course.isExpires()) {
				Collection<Course> renewals = course.getRenewals();
				if (renewals != null && renewals.size() > 0) {
					Iterator<Course> it = renewals.iterator();
					while (it.hasNext()) {
						findNewestLeaves(it.next(), resultMap);
					}
				}
			}
		}
	}

	public CourseExpirationAdapter(Object course, Date today, Map... caches) {
		setCaches(caches);
		setToday(today);
		setItem(course);
	}

	@Override
	public Object getItem() {
		return course;
	}

	@Override
	public void setCaches(Map... caches) {
		// no cache required...
	}

	@Override
	protected void setItem(Object course) {
		this.course = (Course) course;
		newest = findNewest(this.course);
	}
}
