package org.phoenixctms.ctsms.web.model.course;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class AdminUpcomingCourseOverviewBean extends ManagedBeanBase {

	private AdminUpcomingCourseLazyModel adminUpcomingCourseModel;

	public AdminUpcomingCourseOverviewBean() {
		super();
		adminUpcomingCourseModel = new AdminUpcomingCourseLazyModel();
	}

	public String courseToColor(CourseOutVO course) {
		if (course != null) {
			int acknowledgeCount = 0;
			int passedCount = 0;
			int cancelledCount = 0;
			int totalCount = 0;
			Iterator<CourseParticipationStatusEntryOutVO> it = WebUtil.getCourseParticipationStatusEntries(null, course.getId()).iterator();
			while (it.hasNext()) {
				CourseParticipationStatusTypeVO status = it.next().getStatus();
				if (status.getAcknowledge()) {
					acknowledgeCount++;
				}
				if (status.getPass()) {
					passedCount++;
				}
				if (status.getCancel()) {
					cancelledCount++;
				}
				totalCount++;
			}
			if (cancelledCount > 0 && !course.isSelfRegistration()) {
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.COURSE_SOME_CANCELLED, Bundle.SETTINGS, DefaultSettings.COURSE_SOME_CANCELLED));
			} else if (acknowledgeCount + passedCount >= totalCount && totalCount > 0 && passedCount > 0) { // flag could both be set...
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.COURSE_ALL_ACK_COMPLETED, Bundle.SETTINGS, DefaultSettings.COURSE_ALL_ACK_COMPLETED));
			} else if (passedCount == 0) {
				if (acknowledgeCount > 0) {
					// orange
					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.COURSE_SOME_ACK, Bundle.SETTINGS, DefaultSettings.COURSE_SOME_ACK));
				} else {
					// gelb
					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.COURSE_NONE_ACK_YET, Bundle.SETTINGS, DefaultSettings.COURSE_NONE_ACK_YET));
				}
			} else {
				// ....passed AND failed, still invited/registered... cancelled (non-compulsory)
				// grey/green?
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.COURSE_SOME_FAILED_NACK_CANCELLED, Bundle.SETTINGS,
						DefaultSettings.COURSE_SOME_FAILED_NACK_CANCELLED));
			}
		}
		return "";
	}

	public AdminUpcomingCourseLazyModel getAdminUpcomingCourseModel() {
		return adminUpcomingCourseModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		adminUpcomingCourseModel.updateRowCount();
		DataTable.clearFilters("upcomingcourse_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}
}
