package org.phoenixctms.ctsms.web.model.staff;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class UpcomingCourseOverviewBean extends ManagedBeanBase {

	private StaffOutVO identity;
	private UpcomingCourseLazyModel upcomingCourseModel;

	public UpcomingCourseOverviewBean() {
		super();
		upcomingCourseModel = new UpcomingCourseLazyModel();
	}

	public String courseToColor(CourseOutVO course) {
		if (course != null && identity != null) {
			CourseParticipationStatusEntryOutVO statusEntry = WebUtil.getCourseParticipationStatusEntry(identity.getId(), course.getId());
			if (statusEntry != null) {
				return WebUtil.colorToStyleClass(statusEntry.getStatus().getColor());
			}
		}
		return "";
	}

	public String getCourseParticipationStatusTypeName(CourseOutVO course) {
		if (course != null && identity != null) {
			CourseParticipationStatusEntryOutVO statusEntry = WebUtil.getCourseParticipationStatusEntry(identity.getId(), course.getId());
			if (statusEntry != null) {
				return statusEntry.getStatus().getName();
			}
		}
		return "";
	}

	public UpcomingCourseLazyModel getUpcomingCourseModel() {
		return upcomingCourseModel;
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
		upcomingCourseModel.setStaffId(identity != null ? identity.getId() : null);
		upcomingCourseModel.updateRowCount();
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

	public Boolean isParticipating(CourseOutVO course) {
		if (course != null && identity != null) {
			Long courseParticipationStatusEntryCount = WebUtil.getCourseParticipationStatusEntryCount(identity.getId(), course.getId());
			if (courseParticipationStatusEntryCount != null) {
				return courseParticipationStatusEntryCount > 0;
			}
		}
		return null;
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
}
