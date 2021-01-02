package org.phoenixctms.ctsms.web.model.course;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseCertificatePDFVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.CourseParticipationStatusBeanBase;
import org.phoenixctms.ctsms.web.model.shared.StaffMultiPickerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class AdminCourseParticipationStatusBean extends CourseParticipationStatusBeanBase {

	public static void initParticipationStatusEntryDefaultValues(CourseParticipationStatusEntryInVO in, Long courseId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setCourseId(courseId);
			in.setStaffId(null);
			in.setStatusId(null);
			CourseOutVO course = WebUtil.getCourse(courseId, null, null, null);
			if (course != null) {
				CvSectionVO cvSectionVO = course.getCvSectionPreset();
				TrainingRecordSectionVO trainingRecordSectionVO = course.getTrainingRecordSectionPreset();
				in.setComment(course.getCvCommentPreset());
				in.setCvSectionId(cvSectionVO == null ? null : cvSectionVO.getId());
				in.setShowCommentCv(course.getShowCommentCvPreset());
				in.setShowCv(course.getShowCvPreset());
				in.setTrainingRecordSectionId(trainingRecordSectionVO == null ? null : trainingRecordSectionVO.getId());
				in.setShowTrainingRecord(course.getShowTrainingRecordPreset());
				in.setShowCommentTrainingRecord(course.getShowCommentTrainingRecordPreset());
				return;
			}
			in.setComment(null);
			in.setCvSectionId(null);
			in.setShowCommentCv(false);
			in.setShowCv(false);
			in.setTrainingRecordSectionId(null);
			in.setShowTrainingRecord(false);
			in.setShowCommentTrainingRecord(false);
		}
	}

	private StaffMultiPickerModel staffMultiPicker;
	private Long courseId;

	public AdminCourseParticipationStatusBean() {
		super();
		staffMultiPicker = new StaffMultiPickerModel();
	}

	@Override
	public String addAction() {
		CourseParticipationStatusEntryInVO backup = new CourseParticipationStatusEntryInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getCourseService().addCourseParticipationStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			Set<Long> ids = this.staffMultiPicker.getSelectionIds();
			Iterator<CourseParticipationStatusEntryOutVO> it = WebUtil.getServiceLocator().getCourseService()
					.addCourseParticipationStatusEntries(WebUtil.getAuthentication(), courseId, ids).iterator();
			while (it.hasNext()) {
				this.staffMultiPicker.removeId(it.next().getStaff().getId());
			}
			int itemsLeft = staffMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			statusEntryModel.updateRowCount();
			return BULK_ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64,
				JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT, MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE,
				MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, new Long(statusEntryModel.getRowCount()));
		if (operationSuccess && in.getCourseId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
					MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getCourseId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("courseparticipationstatus_list");
		out = null;
		this.courseId = id;
		staffMultiPicker.clear();
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getCourseService().deleteCourseParticipationStatusEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public StreamedContent getCourseCertificatePdfStreamedContent(CourseParticipationStatusEntryOutVO statusEntry) throws Exception {
		if (statusEntry != null) {
			try {
				CourseCertificatePDFVO certificate = WebUtil.getServiceLocator().getCourseService().renderCourseCertificate(WebUtil.getAuthentication(), statusEntry.getId());
				return new DefaultStreamedContent(new ByteArrayInputStream(certificate.getDocumentDatas()), certificate.getContentType().getMimeType(), certificate.getFileName());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			}
		}
		return null;
	}

	public StreamedContent getCourseCertificatesPdfStreamedContent() throws Exception {
		try {
			CourseCertificatePDFVO certificates = WebUtil.getServiceLocator().getCourseService().renderCourseCertificates(WebUtil.getAuthentication(), courseId);
			return new DefaultStreamedContent(new ByteArrayInputStream(certificates.getDocumentDatas()), certificates.getContentType().getMimeType(), certificates.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getCourseParticipantListPdfStreamedContent() throws Exception {
		try {
			CourseParticipantListPDFVO participantList = WebUtil.getServiceLocator().getCourseService().renderCourseParticipantList(WebUtil.getAuthentication(), courseId, false);
			return new DefaultStreamedContent(new ByteArrayInputStream(participantList.getDocumentDatas()), participantList.getContentType().getMimeType(),
					participantList.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getCvPdfStreamedContent() throws Exception {
		return WebUtil.getCvPdfStreamedContent(in.getStaffId());
	}

	public StreamedContent getTrainingRecordPdfStreamedContent() throws Exception {
		return WebUtil.getTrainingRecordPdfStreamedContent(in.getStaffId());
	}

	public boolean getShowTerminalStateMessage() {
		if (out != null && in.getStatusId() != null) {
			Collection<CourseParticipationStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
						.getCourseParticipationStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId(), true, out.getCourse().isSelfRegistration());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusTypeVOs != null) {
				if (statusTypeVOs.size() > 1 || (statusTypeVOs.size() == 1 && !statusTypeVOs.iterator().next().getId().equals(in.getStatusId()))) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public StaffMultiPickerModel getStaffMultiPicker() {
		return staffMultiPicker;
	}

	public String getStaffName() {
		return WebUtil.staffIdToName(in.getStaffId());
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getStaff().getName(), out.getStatus().getName());
		} else {
			return Messages.getString(MessageCodes.ADMIN_CREATE_NEW_COURSE_PARTICIPATION_STATUS_ENTRY);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.COURSE_PARTICIPATION_STATUS_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	@Override
	protected void initIn() {
		if (in == null) {
			in = new CourseParticipationStatusEntryInVO();
		}
		if (out != null) {
			copyParticipationStatusEntryOutToIn(in, out);
			courseId = in.getCourseId();
		} else {
			initParticipationStatusEntryDefaultValues(in, courseId);
		}
	}

	@Override
	protected void initSets() {
		collidingStaffStatusEntryModelCache.clear();
		collidingDutyRosterTurnModelCache.clear();
		collidingInventoryBookingModelCache.clear();
		statusEntryModel.setStaffId(null);
		statusEntryModel.setCourseId(in.getCourseId());
		statusEntryModel.updateRowCount();
		cvSections = WebUtil.getCvSections(this.in.getCvSectionId());
		trainingRecordSections = WebUtil.getTrainingRecordSections(this.in.getTrainingRecordSectionId());
		Collection<CourseParticipationStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
						.getCourseParticipationStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId(), true, out.getCourse().isSelfRegistration());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else {
			CourseOutVO course = WebUtil.getCourse(courseId, null, null, null);
			if (course != null) {
				try {
					statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
							.getInitialCourseParticipationStatusTypes(WebUtil.getAuthentication(), true, course.isSelfRegistration());
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<CourseParticipationStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				CourseParticipationStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadSelectedCvSection();
		loadSelectedTrainingRecordSection();
	}

	@Override
	public boolean isCreateable() {
		return (in.getCourseId() == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isStaffBulkCreateable() {
		return isCreateable() && staffMultiPicker.getIsEnabled();
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	@Override
	public String updateAction() {
		CourseParticipationStatusEntryInVO backup = new CourseParticipationStatusEntryInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getCourseService().adminUpdateCourseParticipationStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
