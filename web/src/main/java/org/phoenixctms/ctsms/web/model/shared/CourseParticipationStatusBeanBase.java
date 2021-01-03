package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class CourseParticipationStatusBeanBase extends ManagedBeanBase {

	public static void copyParticipationStatusEntryOutToIn(CourseParticipationStatusEntryInVO in, CourseParticipationStatusEntryOutVO out) {
		if (in != null && out != null) {
			CourseOutVO courseVO = out.getCourse();
			StaffOutVO staffVO = out.getStaff();
			CvSectionVO cvSectionVO = out.getCvSection();
			TrainingRecordSectionVO trainingRecordSectionVO = out.getTrainingRecordSection();
			CourseParticipationStatusTypeVO statusVO = out.getStatus();
			in.setComment(out.getComment());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setCvSectionId(cvSectionVO == null ? null : cvSectionVO.getId());
			in.setShowCommentCv(out.getShowCommentCv());
			in.setShowCv(out.getShowCv());
			in.setTrainingRecordSectionId(trainingRecordSectionVO == null ? null : trainingRecordSectionVO.getId());
			in.setShowTrainingRecord(out.getShowTrainingRecord());
			in.setShowCommentTrainingRecord(out.getShowCommentTrainingRecord());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
		}
	}

	protected CourseParticipationStatusEntryInVO in;
	protected CourseParticipationStatusEntryOutVO out;
	protected CourseParticipationStatusEntryLazyModel statusEntryModel;
	protected ArrayList<SelectItem> cvSections;
	protected ArrayList<SelectItem> trainingRecordSections;
	protected ArrayList<SelectItem> statusTypes;
	private CvSectionVO cvSection;
	private TrainingRecordSectionVO trainingRecordSection;
	protected HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	protected HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	protected HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;

	public CourseParticipationStatusBeanBase() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		collidingDutyRosterTurnModelCache = new HashMap<Long, CollidingDutyRosterTurnEagerModel>();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
		statusEntryModel = new CourseParticipationStatusEntryLazyModel();
	}

	public CollidingDutyRosterTurnEagerModel getCollidingDutyRosterTurnModel(CourseParticipationStatusEntryOutVO courseParticipation) {
		return CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(courseParticipation, true, collidingDutyRosterTurnModelCache);
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(CourseParticipationStatusEntryOutVO courseParticipation) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(courseParticipation, true, collidingInventoryBookingModelCache);
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(CourseParticipationStatusEntryOutVO courseParticipation) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(courseParticipation, true, collidingStaffStatusEntryModelCache);
	}

	public ArrayList<SelectItem> getCvSections() {
		return cvSections;
	}

	public ArrayList<SelectItem> getTrainingRecordSections() {
		return trainingRecordSections;
	}

	public CourseParticipationStatusEntryInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public CourseParticipationStatusEntryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedCourseParticipationStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public CourseParticipationStatusEntryLazyModel getStatusEntryModel() {
		return statusEntryModel;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	public void handleCvSectionChange() {
		loadSelectedCvSection();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && cvSection != null) {
			requestContext.addCallbackParam(JSValues.AJAX_CV_SECTION_SHOW_CV_PRESET.toString(), cvSection.getShowCvPreset());
		}
	}

	public void handleShowCvChange() {
		if (!in.getShowCv()) {
			in.setShowCommentCv(false);
		}
	}

	public void handleTrainingRecordSectionChange() {
		loadSelectedTrainingRecordSection();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && trainingRecordSection != null) {
			requestContext.addCallbackParam(JSValues.AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET.toString(), trainingRecordSection.getShowTrainingRecordPreset());
		}
	}

	public void handleShowTrainingRecordChange() {
		if (!in.getShowTrainingRecord()) {
			in.setShowCommentTrainingRecord(false);
		}
	}

	protected abstract void initIn();

	protected abstract void initSets();

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getCourseService().getCourseParticipationStatusEntry(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	protected void loadSelectedCvSection() {
		cvSection = WebUtil.getCvSection(in.getCvSectionId());
	}

	protected void loadSelectedTrainingRecordSection() {
		trainingRecordSection = WebUtil.getTrainingRecordSection(in.getTrainingRecordSectionId());
	}

	protected void sanitizeInVals() {
		if (!in.getShowCv()) {
			in.setShowCommentCv(false);
		}
		if (!in.getShowTrainingRecord()) {
			in.setShowCommentTrainingRecord(false);
		}
	}

	public void setSelectedCourseParticipationStatusEntry(IDVO courseParticipationStatusEntry) {
		if (courseParticipationStatusEntry != null) {
			this.out = (CourseParticipationStatusEntryOutVO) courseParticipationStatusEntry.getVo();
			this.initIn();
			initSets();
		}
	}
}
