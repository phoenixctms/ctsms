package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryFileVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordPDFVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

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
			in.setDatas(null);
			in.setFileName(null);
			in.setMimeType(null);
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
	protected ArrayList<SelectItem> courseTrials;

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

	public ArrayList<SelectItem> getCourseTrials() {
		return courseTrials;
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

	public StreamedContent getCvPdfStreamedContent() throws Exception {
		return WebUtil.getCvPdfStreamedContent(in.getStaffId());
	}

	public StreamedContent getTrainingRecordPdfStreamedContent(boolean allTrials, boolean appendCertificates) throws Exception {
		if (in.getStaffId() != null) {
			try {
				TrainingRecordPDFVO trainingRecord = WebUtil.getServiceLocator().getStaffService().renderTrainingRecordPDF(WebUtil.getAuthentication(), in.getStaffId(), allTrials,
						appendCertificates);
				return new DefaultStreamedContent(new ByteArrayInputStream(trainingRecord.getDocumentDatas()), trainingRecord.getContentType().getMimeType(),
						trainingRecord.getFileName());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			}
		}
		return null;
	}

	public StreamedContent getTrialTrainingRecordPdfStreamedContent(Long trialId, boolean appendCertificates) throws Exception {
		Set<Long> trialIds = new HashSet<Long>(1);
		if (trialId != null) {
			trialIds.add(trialId);
		}
		if (in.getStaffId() != null) {
			try {
				TrainingRecordPDFVO trainingRecord = WebUtil.getServiceLocator().getStaffService().renderTrialTrainingRecordPDF(WebUtil.getAuthentication(), in.getStaffId(),
						trialIds, appendCertificates);
				return new DefaultStreamedContent(new ByteArrayInputStream(trainingRecord.getDocumentDatas()), trainingRecord.getContentType().getMimeType(),
						trainingRecord.getFileName());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			}
		}
		return null;
	}

	protected static ArrayList<SelectItem> getCourseTrials(Long staffId) {
		Collection<TrialOutVO> trialVOs = null;
		ArrayList<SelectItem> trials;
		if (staffId != null) {
			try {
				trialVOs = WebUtil.getServiceLocator().getStaffService().getCourseTrials(WebUtil.getAuthentication(), staffId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (trialVOs != null) {
			trials = new ArrayList<SelectItem>(trialVOs.size());
			Iterator<TrialOutVO> it = trialVOs.iterator();
			while (it.hasNext()) {
				TrialOutVO trialVO = it.next();
				trials.add(new SelectItem(Long.toString(trialVO.getId()), CommonUtil.trialOutVOToString(trialVO)));
			}
		} else {
			trials = new ArrayList<SelectItem>();
		}
		return trials;
	}

	public abstract boolean isPerson();

	public String getTrialTrainingRecordPdfButtonLabel(SelectItem trial, boolean appendCertificates) {
		if (appendCertificates) {
			return Messages.getMessage(MessageCodes.TRAINING_RECORD_CERTIFICATES_TRIAL_LABEL, trial.getLabel());
		} else {
			return Messages.getMessage(MessageCodes.TRAINING_RECORD_TRIAL_LABEL, trial.getLabel());
		}
	}

	public String getTrainingRecordPdfButtonLabel(boolean allTrials, boolean appendCertificates) {
		if (allTrials) {
			if (appendCertificates) {
				return Messages.getMessage(MessageCodes.TRAINING_RECORD_CERTIFICATES_ALL_TRIALS_LABEL);
			} else {
				return Messages.getMessage(MessageCodes.TRAINING_RECORD_ALL_TRIALS_LABEL);
			}
		} else {
			if (appendCertificates) {
				return Messages.getMessage(MessageCodes.TRAINING_RECORD_CERTIFICATES_LABEL);
			} else {
				return Messages.getMessage(MessageCodes.TRAINING_RECORD_LABEL);
			}
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

	protected abstract void initSpecificSets();

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

	private Integer uploadSizeLimit;
	private String allowTypes;
	private CourseParticipationStatusEntryFileVO courseParticipationFile;

	public void clearFile() {
		in.setFileName(null);
		in.setMimeType(null);
		in.setDatas(null);
		courseParticipationFile = null;
	}

	public CourseParticipationStatusEntryFileVO getCourseParticipationFile() {
		return courseParticipationFile;
	}

	public String getFileDownloadLinkLabel() {
		if (isHasFile()) {
			return in.getFileName();
		} else {
			return Messages.getString(MessageCodes.NO_FILE_LINK_LABEL);
		}
	}

	public StreamedContent getFileStreamedContent() throws Exception {
		if (isHasFile()) {
			return new DefaultStreamedContent(new ByteArrayInputStream(in.getDatas()), in.getMimeType(), in.getFileName());
		}
		return null;
	}

	public Integer getUploadSizeLimit() {
		return uploadSizeLimit;
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		in.setFileName(uploadedFile.getFileName());
		in.setMimeType(uploadedFile.getContentType());
		in.setDatas(uploadedFile.getContents());
		addOperationSuccessMessage(MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
	}

	protected void initSets() {
		courseParticipationFile = null;
		if (out != null) {
			try {
				courseParticipationFile = WebUtil.getServiceLocator().getCourseService().getCourseParticipationStatusEntryFile(WebUtil.getAuthentication(), out.getId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (courseParticipationFile != null) {
				MimeTypeVO contentTypeVO = courseParticipationFile.getContentType();
				in.setFileName(courseParticipationFile.getFileName());
				in.setMimeType(contentTypeVO == null ? null : contentTypeVO.getMimeType());
				in.setDatas(courseParticipationFile.getDatas());
			}
		}
		allowTypes = WebUtil.getAllowedFileExtensionsPattern(FileModule.COURSE_CERTIFICATE, false);
		uploadSizeLimit = null;
		try {
			uploadSizeLimit = WebUtil.getServiceLocator().getToolsService().getCourseParticipationFileUploadSizeLimit();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		initSpecificSets();
	}

	public boolean isHasFile() {
		byte[] data = in.getDatas();
		if (data != null && data.length > 0) {
			return true;
		}
		return false;
	}

	public String getAllowTypes() {
		return allowTypes;
	}
}
