package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.CourseParticipationStatusBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class CourseParticipationStatusBean extends CourseParticipationStatusBeanBase {

	public static void initParticipationStatusEntryDefaultValues(CourseParticipationStatusEntryInVO in, Long staffId) {
		if (in != null) {
			in.setComment(null);
			in.setCourseId(null);
			in.setId(null);
			in.setVersion(null);
			in.setSectionId(null);
			in.setShowCommentCv(false);
			in.setShowCv(false);
			in.setStaffId(staffId);
			in.setStatusId(null);
		}
	}

	private Date today;
	private Long staffId;
	private StaffOutVO staff;

	public CourseParticipationStatusBean() {
		super();
		today = new Date();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64,
				JSValues.AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT, MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE,
				MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, new Long(statusEntryModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("courseparticipationstatus_list");
		out = null;
		this.staffId = id;
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
			out = WebUtil.getServiceLocator().getStaffService().deleteSelfRegistrationCourseParticipation(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public Boolean getCourseExpired(CourseOutVO course) {
		return WebUtil.getCourseExpired(today, course);
	}

	public Boolean getCourseValid(CourseOutVO course) {
		Boolean courseExpired = WebUtil.getCourseExpired(today, course);
		if (courseExpired != null) {
			return !courseExpired;
		}
		return null;
	}

	public StreamedContent getCvPdfStreamedContent() throws Exception {
		return WebUtil.getCvPdfStreamedContent(in.getStaffId());
	}

	public boolean getShowTerminalStateMessage() {
		if (out != null && in.getStatusId() != null) {
			Collection<CourseParticipationStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
						.getCourseParticipationStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId(), false, out.getCourse().getSelfRegistration());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
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

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getCourse().getName(), out.getStatus().getName());
		} else {
			return Messages.getString(MessageCodes.MODIFY_COURSE_PARTICIPATION_STATUS_ENTRY);
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
			staffId = in.getStaffId();
		} else {
			initParticipationStatusEntryDefaultValues(in, staffId);
		}
	}

	@Override
	protected void initSets() {
		today = new Date();
		collidingStaffStatusEntryModelCache.clear();
		collidingDutyRosterTurnModelCache.clear();
		collidingInventoryBookingModelCache.clear();
		statusEntryModel.setStaffId(in.getStaffId());
		statusEntryModel.setCourseId(null);
		statusEntryModel.updateRowCount();
		cvSections = WebUtil.getCvSections(this.in.getSectionId());
		Collection<CourseParticipationStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
						.getCourseParticipationStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId(), false, out.getCourse().getSelfRegistration());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
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
		staff = WebUtil.getStaff(in.getStaffId(), null, null, null);
		loadSelectedSection();
		if (staff != null) {
			if (!WebUtil.isStaffPerson(staff)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_PERSON);
			} else if (out == null) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.SELECT_COURSE_PARTICIPATION_STATUS_ENTRY);
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && WebUtil.isStaffPerson(staff);
	}

	public boolean isInputVisible() {
		return isCreated();
	}

	public boolean isPerson() {
		return WebUtil.isStaffPerson(staff);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && WebUtil.isStaffPerson(staff) && WebUtil.isCourseSelfRegistration(out.getCourse());
	}

	public void refresh() {
		statusEntryModel.updateRowCount();
		DataTable.clearFilters("courseparticipationstatus_list");
	}

	@Override
	public String updateAction() {
		CourseParticipationStatusEntryInVO backup = new CourseParticipationStatusEntryInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().userUpdateCourseParticipationStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}
