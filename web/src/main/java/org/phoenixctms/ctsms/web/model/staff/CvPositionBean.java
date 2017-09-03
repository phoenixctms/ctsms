package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.vo.CvPositionInVO;
import org.phoenixctms.ctsms.vo.CvPositionOutVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class CvPositionBean extends ManagedBeanBase {

	public static void copyCvPositionOutToIn(CvPositionInVO in, CvPositionOutVO out) {
		if (in != null && out != null) {
			StaffOutVO staffVO = out.getStaff();
			StaffOutVO institutionVO = out.getInstitution();
			CvSectionVO sectionVO = out.getSection();
			in.setComment(out.getComment());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInstitutionId(institutionVO == null ? null : institutionVO.getId());
			in.setSectionId(sectionVO == null ? null : sectionVO.getId());
			in.setShowCommentCv(out.getShowCommentCv());
			in.setShowCv(out.getShowCv());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTitle(out.getTitle());
		}
	}

	public static void initCvPositionDefaultValues(CvPositionInVO in, Long staffId) {
		if (in != null) {
			in.setComment(Messages.getString(MessageCodes.CV_POSITION_COMMENT_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setInstitutionId(null);
			in.setSectionId(null);
			in.setShowCommentCv(Settings.getBoolean(SettingCodes.CV_POSITION_SHOW_COMMENT_CV_PRESET, Bundle.SETTINGS, DefaultSettings.CV_POSITION_SHOW_COMMENT_CV_PRESET));
			in.setShowCv(Settings.getBoolean(SettingCodes.CV_POSITION_SHOW_CV_PRESET, Bundle.SETTINGS, DefaultSettings.CV_POSITION_SHOW_CV_PRESET));
			in.setStaffId(staffId);
			in.setStart(null);
			in.setStop(null);
			in.setTitle(Messages.getString(MessageCodes.CV_POSITION_TITLE_PRESET));
		}
	}

	private CvPositionInVO in;
	private CvPositionOutVO out;
	private Long staffId;
	private StaffOutVO staff;
	private CvPositionLazyModel cvPositionModel;
	private ArrayList<SelectItem> cvSections;
	private CvSectionVO cvSection;

	public CvPositionBean() {
		super();
		cvPositionModel = new CvPositionLazyModel();
	}

	@Override
	public String addAction()
	{
		CvPositionInVO backup = new CvPositionInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().addCvPosition(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_CV_POSITION_TAB_TITLE_BASE64, JSValues.AJAX_CV_POSITION_COUNT,
				MessageCodes.CV_POSITIONS_TAB_TITLE, MessageCodes.CV_POSITIONS_TAB_TITLE_WITH_COUNT, new Long(cvPositionModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("cvposition_list");
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
			out = WebUtil.getServiceLocator().getStaffService().deleteCvPosition(WebUtil.getAuthentication(), id);
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

	public StreamedContent getCvPdfStreamedContent() throws Exception {
		return WebUtil.getCvPdfStreamedContent(in.getStaffId());
	}

	public CvPositionLazyModel getCvPositionModel() {
		return cvPositionModel;
	}

	public ArrayList<SelectItem> getCvSections() {
		return cvSections;
	}

	public CvPositionInVO getIn() {
		return in;
	}

	public String getInstitutionName() {
		return WebUtil.staffIdToName(in.getInstitutionId());
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public CvPositionOutVO getOut() {
		return out;
	}

	public IDVO getSelectedCvPosition() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.CV_POSITION_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_CV_POSITION);
		}
	}

	public void handleSectionChange() {
		loadSelectedSection();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && cvSection != null) {
			requestContext.addCallbackParam(JSValues.AJAX_CV_SECTION_SHOW_CV_PRESET.toString(), cvSection.getShowCvPreset());
			requestContext.addCallbackParam(JSValues.AJAX_CV_SECTION_TITLE_PRESET_BASE64.toString(), JsUtil.encodeBase64(cvSection.getTitlePreset(), false));
		}
	}

	public void handleShowCvChange() {
		if (!in.getShowCv()) {
			in.setShowCommentCv(false);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.CV_POSITION_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new CvPositionInVO();
		}
		if (out != null) {
			copyCvPositionOutToIn(in, out);
			staffId = in.getStaffId();
		} else {
			initCvPositionDefaultValues(in, staffId);
		}
	}

	private void initSets() {
		cvPositionModel.setStaffId(in.getStaffId());
		cvPositionModel.updateRowCount();
		cvSections = WebUtil.getCvSections(this.in.getSectionId());
		staff = WebUtil.getStaff(in.getStaffId(), null, null);
		loadSelectedSection();
		if (!WebUtil.isStaffPerson(staff)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_PERSON);
		}
	}

	@Override
	public boolean isCreateable() {
		return this.in.getStaffId() == null ? false : WebUtil.isStaffPerson(staff);
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
		return isCreated() || WebUtil.isStaffPerson(staff);
	}

	public boolean isPerson() {
		return WebUtil.isStaffPerson(staff);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && WebUtil.isStaffPerson(staff);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getStaffService().getCvPosition(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectedSection() {
		cvSection = WebUtil.getCvSection(in.getSectionId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getShowCv()) {
			in.setShowCommentCv(false);
		}
	}

	public void setSelectedCvPosition(IDVO cvPosition) {
		if (cvPosition != null) {
			this.out = (CvPositionOutVO) cvPosition.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		CvPositionInVO backup = new CvPositionInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateCvPosition(WebUtil.getAuthentication(), in);
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
