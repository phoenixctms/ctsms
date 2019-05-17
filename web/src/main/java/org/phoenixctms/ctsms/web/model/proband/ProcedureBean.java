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
import org.phoenixctms.ctsms.vo.OpsCodeVO;
import org.phoenixctms.ctsms.vo.OpsSystCategoryVO;
import org.phoenixctms.ctsms.vo.OpsSystVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProcedureInVO;
import org.phoenixctms.ctsms.vo.ProcedureOutVO;
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
public class ProcedureBean extends ManagedBeanBase {

	public static void copyProcedureOutToIn(ProcedureInVO in, ProcedureOutVO out) {
		if (in != null && out != null) {
			OpsCodeVO codeVO = out.getCode();
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

	public static void initProcedureDefaultValues(ProcedureInVO in, Long probandId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setComment(Messages.getString(MessageCodes.PROCEDURE_COMMENT_PRESET));
			in.setStart(null);
			in.setStop(null);
			in.setCodeId(null);
		}
	}

	private ProcedureInVO in;
	private ProcedureOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ProcedureLazyModel procedureModel;
	private OpsCodeVO code;

	public ProcedureBean() {
		super();
		procedureModel = new ProcedureLazyModel();
	}

	@Override
	public String addAction() {
		ProcedureInVO backup = new ProcedureInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProcedure(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROCEDURE_TAB_TITLE_BASE64, JSValues.AJAX_PROCEDURE_COUNT,
				MessageCodes.PROCEDURES_TAB_TITLE, MessageCodes.PROCEDURES_TAB_TITLE_WITH_COUNT, new Long(procedureModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("procedure_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<IDVO> completeOpsCode(String query) {
		try {
			Collection opsCodeVOs = WebUtil.getServiceLocator().getToolsService().completeOpsCode(WebUtil.getAuthentication(), query, null);
			IDVO.transformVoCollection(opsCodeVOs);
			return (List<IDVO>) opsCodeVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
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
			out = WebUtil.getServiceLocator().getProbandService().deleteProcedure(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public IDVO getCode() {
		if (code != null) {
			return IDVO.transformVo(code);
		}
		return null;
	}

	public ProcedureInVO getIn() {
		return in;
	}

	public String getLastCategoryLabel(OpsCodeVO code) {
		OpsSystVO systematics;
		if (code != null && (systematics = code.getSystematics()) != null) {
			Iterator<OpsSystCategoryVO> it = systematics.getCategories().iterator();
			while (it.hasNext()) {
				OpsSystCategoryVO category = it.next();
				if (category.getLast()) {
					return category.getPreferredRubricLabel();
				}
			}
		}
		return "";
	}

	public Long getMedicationCount(ProcedureOutVO procedure) {
		if (procedure != null) {
			return WebUtil.getMedicationCount(null, null, procedure.getId());
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

	public ProcedureOutVO getOut() {
		return out;
	}

	public ProcedureLazyModel getProcedureModel() {
		return procedureModel;
	}

	public IDVO getSelectedProcedure() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROCEDURE_TITLE, Long.toString(out.getId()), out.getName(), DateUtil.getDateStartStopString(out.getStart(), out.getStop())); // out.getCode().getText()
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROCEDURE);
		}
	}

	public void handleCodeSelect(SelectEvent event) {
	}

	public void handleCodeUnselect(UnselectEvent event) {
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.PROCEDURE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProcedureInVO();
		}
		if (out != null) {
			copyProcedureOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initProcedureDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		procedureModel.setProbandId(in.getProbandId());
		procedureModel.updateRowCount();
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
			out = WebUtil.getServiceLocator().getProbandService().getProcedure(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROCEDURE, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	private void loadCode() {
		code = WebUtil.getOpsCode(in.getCodeId());
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
			this.code = (OpsCodeVO) code.getVo();
			//in.setCodeId(code.getId());
		} else {
			this.code = null;
		}
	}

	public void setSelectedProcedure(IDVO procedure) {
		if (procedure != null) {
			this.out = (ProcedureOutVO) procedure.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		ProcedureInVO backup = new ProcedureInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProcedure(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
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
