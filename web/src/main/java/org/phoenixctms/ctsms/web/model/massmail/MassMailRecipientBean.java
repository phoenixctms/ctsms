package org.phoenixctms.ctsms.web.model.massmail;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.MassMailRecipientBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandMultiPickerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class MassMailRecipientBean extends MassMailRecipientBeanBase {

	private static void initMassMailRecipientDefaultValues(MassMailRecipientInVO in, Long massMailId) {
		if (in != null) {
			in.setProbandId(null);
			in.setMassMailId(massMailId);
			in.setId(null);
			in.setVersion(null);
		}
	}

	private Long massMailId;
	private MassMailOutVO massMail;
	private ProbandMultiPickerModel probandMultiPicker;

	public MassMailRecipientBean() {
		super();
		probandMultiPicker = new ProbandMultiPickerModel();
	}

	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<MassMailRecipientOutVO> it = WebUtil.getServiceLocator().getMassMailService()
					.addMassMailRecipients(WebUtil.getAuthentication(), massMailId, ids).iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			massMailRecipientModel.updateRowCount();
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64,
				JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT,
				MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE, MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE_WITH_COUNT, new Long(massMailRecipientModel.getRowCount()));
		if (operationSuccess && in.getMassMailId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT,
					MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.MASS_MAIL_JOURNAL, in.getMassMailId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("mass_mail_recipient_list");
		out = null;
		this.massMailId = id;
		probandMultiPicker.clear();
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public final void deleteBulk() {
		actionPostProcess(deleteBulkAction());
	}

	public String deleteBulkAction() {
		try {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<MassMailRecipientOutVO> it = WebUtil.getServiceLocator().getMassMailService()
					.deleteMassMailRecipients(WebUtil.getAuthentication(), massMailId, ids).iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_DELETE_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_DELETE_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			massMailRecipientModel.updateRowCount();
			return BULK_DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public ProbandMultiPickerModel getProbandMultiPicker() {
		return probandMultiPicker;
	}

	public String getProbandName() {
		return WebUtil.probandIdToName(in.getProbandId());
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.MASS_MAIL_RECIPIENT_TITLE, Long.toString(out.getId()), out.getProband().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_MASS_MAIL_RECIPIENT);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_RECIPIENT_ID);
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
			in = new MassMailRecipientInVO();
		}
		if (out != null) {
			copyMassMailRecipientOutToIn(in, out);
			massMailId = in.getMassMailId();
		} else {
			initMassMailRecipientDefaultValues(in, massMailId);
		}
	}

	@Override
	protected void initSets() {
		massMailRecipientModel.setMassMailId(in.getMassMailId());
		massMailRecipientModel.updateRowCount();
		massMail = WebUtil.getMassMail(this.in.getMassMailId());
		if (WebUtil.isMassMailLocked(massMail)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MASS_MAIL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getMassMailId() == null ? false : !WebUtil.isMassMailLocked(massMail));
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isMassMailLocked(massMail);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isMassMailLocked(massMail);
	}

	public boolean isProbandBulkCreateable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	public boolean isProbandBulkRemovable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isMassMailLocked(massMail);
	}
}
