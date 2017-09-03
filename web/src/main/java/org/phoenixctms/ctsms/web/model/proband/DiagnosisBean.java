package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AlphaIdVO;
import org.phoenixctms.ctsms.vo.DiagnosisInVO;
import org.phoenixctms.ctsms.vo.DiagnosisOutVO;
import org.phoenixctms.ctsms.vo.IcdSystCategoryVO;
import org.phoenixctms.ctsms.vo.IcdSystVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class DiagnosisBean extends ManagedBeanBase {

	public static void copyDiagnosisOutToIn(DiagnosisInVO in, DiagnosisOutVO out) {
		if (in != null && out != null) {
			AlphaIdVO codeVO = out.getCode();
			ProbandOutVO probandVO = out.getProband();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setComment(out.getComment());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setCodeId(codeVO == null ? null : codeVO.getId());
		}
	}

	public static void initDiagnosisDefaultValues(DiagnosisInVO in, Long probandId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setComment(Messages.getString(MessageCodes.DIAGNOSIS_COMMENT_PRESET));
			in.setStart(null);
			in.setStop(null);
			in.setCodeId(null);
		}
	}

	private DiagnosisInVO in;
	private DiagnosisOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private DiagnosisLazyModel diagnosisModel;
	private AlphaIdVO code;

	public DiagnosisBean() {
		super();
		diagnosisModel = new DiagnosisLazyModel();
	}

	@Override
	public String addAction()
	{
		DiagnosisInVO backup = new DiagnosisInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addDiagnosis(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_DIAGNOSIS_TAB_TITLE_BASE64, JSValues.AJAX_DIAGNOSIS_COUNT,
				MessageCodes.DIAGNOSES_TAB_TITLE, MessageCodes.DIAGNOSES_TAB_TITLE_WITH_COUNT, new Long(diagnosisModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("diagnosis_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<IDVO> completeAlphaId(String query) {
		try {
			Collection alphaIdVOs = WebUtil.getServiceLocator().getToolsService().completeAlphaId(WebUtil.getAuthentication(), query, null);
			IDVO.transformVoCollection(alphaIdVOs);
			return (List<IDVO>) alphaIdVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteDiagnosis(WebUtil.getAuthentication(), id);
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

	public IDVO getCode() {
		if (code != null) {
			return IDVO.transformVo(code);
		}
		return null;
	}

	public DiagnosisLazyModel getDiagnosisModel() {
		return diagnosisModel;
	}

	public DiagnosisInVO getIn() {
		return in;
	}

	public String getLastCategoryLabel(AlphaIdVO code) {
		IcdSystVO systematics;
		if (code != null && (systematics = code.getSystematics()) != null) {
			Iterator<IcdSystCategoryVO> it = systematics.getCategories().iterator();
			while (it.hasNext()) {
				IcdSystCategoryVO category = it.next();
				if (category.getLast()) {
					return category.getPreferredRubricLabel();
				}
			}
		}
		return "";
	}

	public Long getMedicationCount(DiagnosisOutVO diagnosis) {
		if (diagnosis != null) {
			return WebUtil.getMedicationCount(null, diagnosis.getId(), null);
		}
		return null;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public DiagnosisOutVO getOut() {
		return out;
	}

	public IDVO getSelectedDiagnosis() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.DIAGNOSIS_TITLE, Long.toString(out.getId()), out.getName(), DateUtil.getDateStartStopString(out.getStart(), out.getStop())); // out.getCode().getText()
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_DIAGNOSIS);
		}
	}

	public void handleCodeSelect( SelectEvent event) {

	}

	public void handleCodeUnselect( UnselectEvent event) {

	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.DIAGNOSIS_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new DiagnosisInVO();
		}
		if (out != null) {
			copyDiagnosisOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initDiagnosisDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		diagnosisModel.setProbandId(in.getProbandId());
		diagnosisModel.updateRowCount();
		loadCode();
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getProbandId() == null ? false : !WebUtil.isProbandLocked(proband));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isProbandLocked(proband);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getDiagnosis(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_DIAGNOSIS, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
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

	private void loadCode() {
		code = WebUtil.getAlphaId(in.getCodeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (code != null) {
			in.setCodeId(code.getId());
		} else {
			in.setCodeId(null);
		}
	}

	public void setCode(IDVO code) {
		if (code != null) {
			this.code = (AlphaIdVO) code.getVo();
			//in.setCodeId(code.getId());
		} else {
			this.code = null;
		}
	}

	public void setSelectedDiagnosis(IDVO diagnosis) {
		if (diagnosis != null) {
			this.out = (DiagnosisOutVO) diagnosis.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		DiagnosisInVO backup = new DiagnosisInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateDiagnosis(WebUtil.getAuthentication(), in);
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
