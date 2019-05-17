package org.phoenixctms.ctsms.web.model.trial;

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
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagVO;
import org.phoenixctms.ctsms.vo.TrialTagValueInVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class TrialTagBean extends ManagedBeanBase {

	public static void copyTagValueOutToIn(TrialTagValueInVO in, TrialTagValueOutVO out) {
		if (in != null && out != null) {
			TrialTagVO trialTagVO = out.getTag();
			TrialOutVO trialVO = out.getTrial();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setTagId(trialTagVO == null ? null : trialTagVO.getId());
			in.setValue(out.getValue());
		}
	}

	public static void initTagValueDefaultValues(TrialTagValueInVO in, Long trialId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setTrialId(trialId);
			in.setTagId(null);
			in.setValue(Messages.getString(MessageCodes.TRIAL_TAG_VALUE_PRESET));
		}
	}

	private TrialTagValueInVO in;
	private TrialTagValueOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> availableTags;
	private TrialTagValueLazyModel tagValueModel;

	public TrialTagBean() {
		super();
		tagValueModel = new TrialTagValueLazyModel();
	}

	@Override
	public String addAction() {
		TrialTagValueInVO backup = new TrialTagValueInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addTrialTagValue(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_TAG_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_TAG_VALUE_COUNT,
				MessageCodes.TRIAL_TAGS_TAB_TITLE, MessageCodes.TRIAL_TAGS_TAB_TITLE_WITH_COUNT, new Long(tagValueModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("trialtag_list");
		out = null;
		this.trialId = id;
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
			out = WebUtil.getServiceLocator().getTrialService().deleteTrialTagValue(WebUtil.getAuthentication(), id);
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

	public TrialTagValueInVO getIn() {
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

	public TrialTagValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedTrialTagValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public TrialTagValueLazyModel getTagValueModel() {
		return tagValueModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.TRIAL_TAG_VALUE_TITLE, Long.toString(out.getId()), out.getTag().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_TRIAL_TAG_VALUE);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.TRIAL_TAG_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new TrialTagValueInVO();
		}
		if (out != null) {
			copyTagValueOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initTagValueDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		tagValueModel.setTrialId(in.getTrialId());
		tagValueModel.updateRowCount();
		Collection<TrialTagVO> tagVOs = null;
		try {
			tagVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableTrialTags(WebUtil.getAuthentication(), this.in.getTrialId(), this.in.getTagId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (tagVOs != null) {
			availableTags = new ArrayList<SelectItem>(tagVOs.size());
			Iterator<TrialTagVO> it = tagVOs.iterator();
			while (it.hasNext()) {
				TrialTagVO tagVO = it.next();
				availableTags.add(new SelectItem(tagVO.getId().toString(), tagVO.getName()));
			}
		} else {
			availableTags = new ArrayList<SelectItem>();
		}
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getTrialTagValue(WebUtil.getAuthentication(), id);
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

	public void setSelectedTrialTagValue(IDVO trialTagValue) {
		if (trialTagValue != null) {
			this.out = (TrialTagValueOutVO) trialTagValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateTrialTagValue(WebUtil.getAuthentication(), in);
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
