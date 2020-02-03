package org.phoenixctms.ctsms.web.model.proband;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.shared.MassMailRecipientBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ProbandRecipientBean extends MassMailRecipientBeanBase {

	private static void initMassMailRecipientDefaultValues(MassMailRecipientInVO in, Long probandId) {
		if (in != null) {
			in.setProbandId(probandId);
			in.setMassMailId(null);
			in.setId(null);
			in.setVersion(null);
		}
	}

	private Long probandId;
	private ProbandOutVO proband;

	public ProbandRecipientBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_RECIPIENT_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_RECIPIENT_COUNT,
				MessageCodes.PROBAND_RECIPIENTS_TAB_TITLE, MessageCodes.PROBAND_RECIPIENTS_TAB_TITLE_WITH_COUNT, new Long(massMailRecipientModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandrecipient_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public String getMassMailName() {
		return WebUtil.massMailIdToName(in.getMassMailId());
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.MASS_MAIL_RECIPIENT_TITLE, Long.toString(out.getId()), out.getMassMail().getName());
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
			probandId = in.getProbandId();
		} else {
			initMassMailRecipientDefaultValues(in, probandId);
		}
	}

	@Override
	protected void initSets() {
		massMailRecipientModel.setProbandId(in.getProbandId());
		massMailRecipientModel.updateRowCount();
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
}
