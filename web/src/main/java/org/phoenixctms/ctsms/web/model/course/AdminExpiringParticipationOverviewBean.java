package org.phoenixctms.ctsms.web.model.course;

import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.model.shared.UpcomingRenewalCourseEagerModel;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class AdminExpiringParticipationOverviewBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int REMINDER_PERIOD_PROPERTY_ID = 1;
	private Date today;
	private VariablePeriodSelector reminder;
	private AdminExpiringParticipationLazyModel adminExpiringParticipationModel;
	private HashMap<Long, UpcomingRenewalCourseEagerModel> upcomingRenewalCourseModelCache;
	private HashMap<Long, HashMap<Long, CourseParticipationStatusEntryOutVO>> courseParticipationCache;
	private HashMap<Long, HashMap<Long, Long>> courseParticipationCountCache;

	public AdminExpiringParticipationOverviewBean() {
		super();
		today = new Date();
		adminExpiringParticipationModel = new AdminExpiringParticipationLazyModel();
		upcomingRenewalCourseModelCache = new HashMap<Long, UpcomingRenewalCourseEagerModel>();
		courseParticipationCache = new HashMap<Long, HashMap<Long, CourseParticipationStatusEntryOutVO>>();
		courseParticipationCountCache = new HashMap<Long, HashMap<Long, Long>>();
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
	}

	public String courseToColor(CourseOutVO course) {
		if (course != null) {
			return WebUtil.expirationToColor(today, course.getExpiration(), Settings.getCourseExpirationDueInColorMap(),
					Settings.getColor(SettingCodes.COURSE_EXPIRATION_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRATION_OVERDUE_COLOR));
		}
		return "";
	}

	public AdminExpiringParticipationLazyModel getAdminExpiringParticipationModel() {
		return adminExpiringParticipationModel;
	}

	private CourseParticipationStatusEntryOutVO getCachedCourseParticipationStatusEntry(StaffOutVO staff, CourseOutVO course) {
		if (course != null && staff != null) {
			HashMap<Long, CourseParticipationStatusEntryOutVO> staffCourseParticipationCache;
			if (courseParticipationCache.containsKey(staff.getId())) {
				staffCourseParticipationCache = courseParticipationCache.get(staff.getId());
			} else {
				staffCourseParticipationCache = new HashMap<Long, CourseParticipationStatusEntryOutVO>();
				courseParticipationCache.put(staff.getId(), staffCourseParticipationCache);
			}
			CourseParticipationStatusEntryOutVO statusEntry;
			if (staffCourseParticipationCache.containsKey(course.getId())) {
				statusEntry = staffCourseParticipationCache.get(course.getId());
			} else {
				statusEntry = WebUtil.getCourseParticipationStatusEntry(staff.getId(), course.getId());
				staffCourseParticipationCache.put(course.getId(), statusEntry);
			}
			return statusEntry;
		}
		return null;
	}

	private Long getCachedCourseParticipationStatusEntryCount(StaffOutVO staff, CourseOutVO course) {
		if (course != null && staff != null) {
			HashMap<Long, Long> staffCourseParticipationCountCache;
			if (courseParticipationCountCache.containsKey(staff.getId())) {
				staffCourseParticipationCountCache = courseParticipationCountCache.get(staff.getId());
			} else {
				staffCourseParticipationCountCache = new HashMap<Long, Long>();
				courseParticipationCountCache.put(staff.getId(), staffCourseParticipationCountCache);
			}
			Long count;
			if (staffCourseParticipationCountCache.containsKey(course.getId())) {
				count = staffCourseParticipationCountCache.get(course.getId());
			} else {
				count = WebUtil.getCourseParticipationStatusEntryCount(staff.getId(), course.getId());
				staffCourseParticipationCountCache.put(course.getId(), count);
			}
			return count;
		}
		return null;
	}

	public UpcomingRenewalCourseEagerModel getCachedUpcomingRenewalCourseModel(CourseParticipationStatusEntryOutVO statusEntry) {
		return UpcomingRenewalCourseEagerModel.getCachedUpcomingRenewalCourseModel(statusEntry, upcomingRenewalCourseModelCache);
	}

	public String getCourseDueInString(CourseOutVO course) {
		return WebUtil.getExpirationDueInString(today, course == null ? null : course.getExpiration());
	}

	public String getCourseParticipationStatusTypeName(StaffOutVO staff, CourseOutVO course) {
		CourseParticipationStatusEntryOutVO statusEntry = getCachedCourseParticipationStatusEntry(staff, course);
		if (statusEntry != null) {
			return statusEntry.getStatus().getName();
		}
		return "";
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				return adminExpiringParticipationModel.getReminderPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public VariablePeriodSelector getReminder() {
		return reminder;
	}

	public void handleReminderChange() {
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		today = new Date();
		upcomingRenewalCourseModelCache.clear();
		courseParticipationCache.clear();
		courseParticipationCountCache.clear();
		adminExpiringParticipationModel.updateRowCount();
		LazyDataModelBase.clearFilters("expiringparticipation_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public Boolean isParticipating(StaffOutVO staff, CourseOutVO course) {
		Long courseParticipationStatusEntryCount = getCachedCourseParticipationStatusEntryCount(staff, course);
		if (courseParticipationStatusEntryCount != null) {
			return courseParticipationStatusEntryCount > 0;
		}
		return null;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return adminExpiringParticipationModel.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(adminExpiringParticipationModel.getReminderPeriod());
	}

	public void participateSelfRegistrationCourse(StaffOutVO staff, CourseOutVO course) {
		if (course != null && staff != null) {
			CourseParticipationStatusEntryInVO participation = new CourseParticipationStatusEntryInVO();
			CvSectionVO cvSectionPreset = course.getCvSectionPreset();
			participation.setComment(course.getCvCommentPreset());
			participation.setCourseId(course.getId());
			participation.setId(null);
			participation.setVersion(null);
			participation.setSectionId(cvSectionPreset != null ? cvSectionPreset.getId() : null);
			participation.setShowCommentCv(course.getShowCommentCvPreset());
			participation.setShowCv(course.getShowCvPreset());
			participation.setStaffId(staff.getId());
			try {
				participation.setStatusId(WebUtil.getServiceLocator().getSelectionSetService().getInitialCourseParticipationStatusTypes(WebUtil.getAuthentication(), true, true)
						.iterator().next().getId());
				WebUtil.getServiceLocator().getCourseService().addCourseParticipationStatusEntry(WebUtil.getAuthentication(), participation);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException|NoSuchElementException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}

	public String renewalCourseToColor(StaffOutVO staff, CourseOutVO course) {
		CourseParticipationStatusEntryOutVO statusEntry = getCachedCourseParticipationStatusEntry(staff, course);
		if (statusEntry != null) {
			return WebUtil.colorToStyleClass(statusEntry.getStatus().getColor());
		}
		return "";
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				adminExpiringParticipationModel.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}
}
