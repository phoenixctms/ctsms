package org.phoenixctms.ctsms.web.model.course;

import java.util.ArrayList;
import java.util.Collection;
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
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LecturerInVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class LecturerBean extends ManagedBeanBase {

	public static void copyLecturerOutToIn(LecturerInVO in, LecturerOutVO out) {
		if (in != null && out != null) {
			LecturerCompetenceVO competenceVO = out.getCompetence();
			StaffOutVO staffVO = out.getStaff();
			CourseOutVO courseVO = out.getCourse();
			in.setCompetenceId(competenceVO == null ? null : competenceVO.getId());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
		}
	}

	public static void initLecturerDefaultValues(LecturerInVO in, Long courseId) {
		if (in != null) {
			in.setCompetenceId(null);
			in.setCourseId(courseId);
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(null);
		}
	}

	private LecturerInVO in;
	private LecturerOutVO out;
	private Long courseId;
	private ArrayList<SelectItem> availableCompetences;
	private LecturerLazyModel lecturerModel;

	public LecturerBean() {
		super();
		lecturerModel = new LecturerLazyModel();
	}

	@Override
	public String addAction() {
		LecturerInVO backup = new LecturerInVO(in);
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getCourseService().addLecturer(WebUtil.getAuthentication(), in);
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_LECTURER_TAB_TITLE_BASE64, JSValues.AJAX_LECTURER_COUNT,
				MessageCodes.LECTURERS_TAB_TITLE, MessageCodes.LECTURERS_TAB_TITLE_WITH_COUNT, new Long(lecturerModel.getRowCount()));
		if (operationSuccess && in.getCourseId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
					MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getCourseId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("lecturer_list");
		out = null;
		this.courseId = id;
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
			out = WebUtil.getServiceLocator().getCourseService().deleteLecturer(WebUtil.getAuthentication(), id);
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

	public ArrayList<SelectItem> getAvailableCompetences() {
		return availableCompetences;
	}

	public LecturerInVO getIn() {
		return in;
	}

	public LecturerLazyModel getLecturerModel() {
		return lecturerModel;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public LecturerOutVO getOut() {
		return out;
	}

	public IDVO getSelectedLecturer() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public String getStaffName() {
		return WebUtil.staffIdToName(in.getStaffId());
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.LECTURER_TITLE, Long.toString(out.getId()), out.getCompetence().getName(), out.getStaff().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_LECTURER);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.LECTURER_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new LecturerInVO();
		}
		if (out != null) {
			copyLecturerOutToIn(in, out);
			courseId = in.getCourseId();
		} else {
			initLecturerDefaultValues(in, courseId);
		}
	}

	private void initSets() {
		lecturerModel.setCourseId(in.getCourseId());
		lecturerModel.updateRowCount();
		Collection<LecturerCompetenceVO> competenceVOs = null;
		try {
			competenceVOs = WebUtil.getServiceLocator().getSelectionSetService()
					.getAvailableLecturerCompetences(WebUtil.getAuthentication(), in.getCourseId(), in.getCompetenceId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (competenceVOs != null) {
			availableCompetences = new ArrayList<SelectItem>(competenceVOs.size());
			Iterator<LecturerCompetenceVO> it = competenceVOs.iterator();
			while (it.hasNext()) {
				LecturerCompetenceVO competenceVO = it.next();
				availableCompetences.add(new SelectItem(competenceVO.getId().toString(), competenceVO.getName()));
			}
		} else {
			availableCompetences = new ArrayList<SelectItem>();
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getCourseId() == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getCourseService().getLecturer(WebUtil.getAuthentication(), id);
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedLecturer(IDVO lecturer) {
		if (lecturer != null) {
			this.out = (LecturerOutVO) lecturer.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getCourseService().updateLecturer(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
