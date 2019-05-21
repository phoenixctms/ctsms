package org.phoenixctms.ctsms.web.model.proband;

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
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
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
public class ProbandTagBean extends ManagedBeanBase {

	public static void copyTagValueOutToIn(ProbandTagValueInVO in, ProbandTagValueOutVO out) {
		if (in != null && out != null) {
			ProbandTagVO probandTagVO = out.getTag();
			ProbandOutVO probandVO = out.getProband();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setTagId(probandTagVO == null ? null : probandTagVO.getId());
			in.setValue(out.getValue());
		}
	}

	public static void initTagValueDefaultValues(ProbandTagValueInVO in, Long probandId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setTagId(null);
			in.setValue(Messages.getString(MessageCodes.PROBAND_TAG_VALUE_PRESET));
		}
	}

	private ProbandTagValueInVO in;
	private ProbandTagValueOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ArrayList<SelectItem> availableTags;
	private ProbandTagValueLazyModel tagValueModel;

	public ProbandTagBean() {
		super();
		tagValueModel = new ProbandTagValueLazyModel();
	}

	@Override
	public String addAction() {
		ProbandTagValueInVO backup = new ProbandTagValueInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProbandTagValue(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_TAG_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_TAG_VALUE_COUNT,
				MessageCodes.PROBAND_TAGS_TAB_TITLE, MessageCodes.PROBAND_TAGS_TAB_TITLE_WITH_COUNT, new Long(tagValueModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandtag_list");
		out = null;
		this.probandId = id;
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
			out = WebUtil.getServiceLocator().getProbandService().deleteProbandTagValue(WebUtil.getAuthentication(), id);
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

	public ArrayList<SelectItem> getAvailableTags() {
		return availableTags;
	}

	public ProbandTagValueInVO getIn() {
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

	public ProbandTagValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedProbandTagValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public ProbandTagValueLazyModel getTagValueModel() {
		return tagValueModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_TAG_VALUE_TITLE, Long.toString(out.getId()), out.getTag().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_TAG_VALUE);
		}
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_TAG_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandTagValueInVO();
		}
		if (out != null) {
			copyTagValueOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initTagValueDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		tagValueModel.setProbandId(in.getProbandId());
		tagValueModel.updateRowCount();
		Collection<ProbandTagVO> tagVOs = null;
		try {
			tagVOs = WebUtil.getServiceLocator().getSelectionSetService()
					.getAvailableProbandTags(WebUtil.getAuthentication(), null, null, this.in.getProbandId(), this.in.getTagId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (tagVOs != null) {
			availableTags = new ArrayList<SelectItem>(tagVOs.size());
			Iterator<ProbandTagVO> it = tagVOs.iterator();
			while (it.hasNext()) {
				ProbandTagVO tagVO = it.next();
				availableTags.add(new SelectItem(tagVO.getId().toString(), tagVO.getName()));
			}
		} else {
			availableTags = new ArrayList<SelectItem>();
		}
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
			out = WebUtil.getServiceLocator().getProbandService().getProbandTagValue(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_TAG_VALUE, Long.toString(out.getId()));
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedProbandTagValue(IDVO probandTagValue) {
		if (probandTagValue != null) {
			this.out = (ProbandTagValueOutVO) probandTagValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProbandTagValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
