package org.phoenixctms.ctsms.web.model.staff;

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
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
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
public class ExpiringParticipationOverviewBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int REMINDER_PERIOD_PROPERTY_ID = 1;
	private Date today;
	private StaffOutVO identity;
	private VariablePeriodSelector reminder;
	private ExpiringParticipationLazyModel expiringParticipationModel;
	private HashMap<Long, UpcomingRenewalCourseEagerModel> upcomingRenewalCourseModelCache;
	private HashMap<Long, CourseParticipationStatusEntryOutVO> courseParticipationCache;
	private HashMap<Long, Long> courseParticipationCountCache;

	public ExpiringParticipationOverviewBean() {
		super();
		today = new Date();
		expiringParticipationModel = new ExpiringParticipationLazyModel();
		upcomingRenewalCourseModelCache = new HashMap<Long, UpcomingRenewalCourseEagerModel>();
		courseParticipationCache = new HashMap<Long, CourseParticipationStatusEntryOutVO>();
		courseParticipationCountCache = new HashMap<Long, Long>();
		setReminder(new VariablePeriodSelector(this, REMINDER_PERIOD_PROPERTY_ID));
	}

	public String courseToColor(CourseOutVO course) {
		if (course != null) {
			return WebUtil.expirationToColor(today, course.getExpiration(), Settings.getCourseExpirationDueInColorMap(),
					Settings.getColor(SettingCodes.COURSE_EXPIRATION_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRATION_OVERDUE_COLOR));
		}
		return "";
	}

	private CourseParticipationStatusEntryOutVO getCachedCourseParticipationStatusEntry(CourseOutVO course) {
		if (course != null && identity != null) {
			CourseParticipationStatusEntryOutVO statusEntry;
			if (courseParticipationCache.containsKey(course.getId())) {
				statusEntry = courseParticipationCache.get(course.getId());
			} else {
				statusEntry = WebUtil.getCourseParticipationStatusEntry(identity.getId(), course.getId());
				courseParticipationCache.put(course.getId(), statusEntry);
			}
			return statusEntry;
		}
		return null;
	}

	private Long getCachedCourseParticipationStatusEntryCount(CourseOutVO course) {
		if (course != null && identity != null) {
			Long count;
			if (courseParticipationCountCache.containsKey(course.getId())) {
				count = courseParticipationCountCache.get(course.getId());
			} else {
				count = WebUtil.getCourseParticipationStatusEntryCount(identity.getId(), course.getId());
				courseParticipationCountCache.put(course.getId(), count);
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

	public String getCourseParticipationStatusTypeName(CourseOutVO course) {
		CourseParticipationStatusEntryOutVO statusEntry = getCachedCourseParticipationStatusEntry(course);
		if (statusEntry != null) {
			return statusEntry.getStatus().getName();
		}
		return "";
	}

	public ExpiringParticipationLazyModel getExpiringParticipationModel() {
		return expiringParticipationModel;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				return expiringParticipationModel.getReminderPeriod();
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
		identity = WebUtil.getUserIdentity();
	}

	private void initSets() {
		today = new Date();
		upcomingRenewalCourseModelCache.clear();
		courseParticipationCache.clear();
		courseParticipationCountCache.clear();
		expiringParticipationModel.setStaffId(identity != null ? identity.getId() : null);
		expiringParticipationModel.updateRowCount();
		DataTable.clearFilters("expiringparticipation_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public Boolean isParticipating(CourseOutVO course) {
		Long courseParticipationStatusEntryCount = getCachedCourseParticipationStatusEntryCount(course);
		if (courseParticipationStatusEntryCount != null) {
			return courseParticipationStatusEntryCount > 0;
		}
		return null;
	}

	public boolean isReminderPeriodSpinnerEnabled() {
		return expiringParticipationModel.getReminderPeriod() == null || VariablePeriod.EXPLICIT.equals(expiringParticipationModel.getReminderPeriod());
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	public void participateSelfRegistrationCourse(CourseOutVO course) {
		if (course != null && identity != null) {
			CourseParticipationStatusEntryInVO participation = new CourseParticipationStatusEntryInVO();
			CvSectionVO cvSectionPreset = course.getCvSectionPreset();
			TrainingRecordSectionVO trainingRecordSectionVO = course.getTrainingRecordSectionPreset();
			participation.setComment(course.getCvCommentPreset());
			participation.setCourseId(course.getId());
			participation.setId(null);
			participation.setVersion(null);
			participation.setCvSectionId(cvSectionPreset != null ? cvSectionPreset.getId() : null);
			participation.setShowCommentCv(course.getShowCommentCvPreset());
			participation.setShowCv(course.getShowCvPreset());
			participation.setTrainingRecordSectionId(trainingRecordSectionVO == null ? null : trainingRecordSectionVO.getId());
			participation.setShowTrainingRecord(course.getShowTrainingRecordPreset());
			participation.setShowCommentTrainingRecord(course.getShowCommentTrainingRecordPreset());
			participation.setStaffId(identity.getId());
			participation.setDatas(null);
			participation.setFileName(null);
			participation.setMimeType(null);
			try {
				participation.setStatusId(WebUtil.getServiceLocator().getSelectionSetService().getInitialCourseParticipationStatusTypes(WebUtil.getAuthentication(), false, true)
						.iterator().next().getId());
				WebUtil.getServiceLocator().getStaffService().participateSelfRegistrationCourse(WebUtil.getAuthentication(), participation);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			} catch (NoSuchElementException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		}
	}

	public String renewalCourseToColor(CourseOutVO course) {
		CourseParticipationStatusEntryOutVO statusEntry = getCachedCourseParticipationStatusEntry(course);
		if (statusEntry != null) {
			return WebUtil.colorToStyleClass(statusEntry.getStatus().getColor());
		}
		return "";
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case REMINDER_PERIOD_PROPERTY_ID:
				expiringParticipationModel.setReminderPeriod(period);
				break;
			default:
		}
	}

	public void setReminder(VariablePeriodSelector reminder) {
		this.reminder = reminder;
	}
}
