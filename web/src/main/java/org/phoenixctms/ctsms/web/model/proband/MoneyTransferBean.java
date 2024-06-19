package org.phoenixctms.ctsms.web.model.proband;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class MoneyTransferBean extends MoneyTransferBeanBase {

	public static void initMoneyTransferDefaultValues(MoneyTransferInVO in, Long probandId) {
		if (in != null) {
			in.setAmount(Settings.getFloatNullable(SettingCodes.MONEY_TRANSFER_AMOUNT_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_AMOUNT_PRESET));
			in.setBankAccountId(null);
			in.setId(null);
			in.setTrialId(null);
			in.setCostType(Messages.getString(MessageCodes.MONEY_TRANSFER_COST_TYPE_PRESET));
			in.setMethod(Settings.getPaymentMethod(SettingCodes.MONEY_TRANSFER_METHOD_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_METHOD_PRESET));
			in.setProbandId(probandId);
			in.setReasonForPayment(Messages.getString(MessageCodes.REASON_FOR_PAYMENT_PRESET));
			in.setReference(Messages.getString(MessageCodes.REFERENCE_PRESET));
			in.setVoucherCode(Messages.getString(MessageCodes.VOUCHER_CODE_PRESET));
			in.setComment(Messages.getString(MessageCodes.MONEY_TRANSFER_COMMENT_PRESET));
			in.setTransactionTimestamp(new Date());
			in.setPaid(Settings.getBoolean(SettingCodes.MONEY_TRANSFER_PAID_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_PAID_PRESET));
			in.setShowComment(Settings.getBoolean(SettingCodes.MONEY_TRANSFER_SHOW_COMMENT_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_SHOW_COMMENT_PRESET));
			in.setVersion(null);
		}
	}

	private Long probandId;
	private ProbandOutVO proband;
	private TrialOutVO trial;
	private ArrayList<SelectItem> filterTrials;
	private ArrayList<SelectItem> reimbursementTrials;
	private ArrayList<SelectItem> filterBankAccounts;
	private ArrayList<SelectItem> filterCostTypes;
	private MoneyTransferLazyModel moneyTransferModel;
	private MoneyTransferSummaryVO openSummary;
	private PaymentMethod payOffPaymentMethod;

	public MoneyTransferBean() {
		super();
		payOffPaymentMethod = Settings.getPaymentMethod(SettingCodes.PAYOFF_PAYMENT_METHOD, Bundle.SETTINGS, DefaultSettings.PAYOFF_PAYMENT_METHOD);
		moneyTransferModel = new MoneyTransferLazyModel();
	}

	@Override
	public String addAction() {
		MoneyTransferInVO backup = new MoneyTransferInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addMoneyTransfer(WebUtil.getAuthentication(), in,
					Settings.getLongNullable(SettingCodes.MAX_COST_TYPES_PER_TRIAL, Bundle.SETTINGS, DefaultSettings.MAX_COST_TYPES_PER_TRIAL));
			initIn();
			initSets(false);
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64, JSValues.AJAX_MONEY_TRANSFER_COUNT,
				MessageCodes.MONEY_TRANSFERS_TAB_TITLE, MessageCodes.MONEY_TRANSFERS_TAB_TITLE_WITH_COUNT, new Long(moneyTransferModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCountSafe(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("moneytransfer_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets(true);
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteMoneyTransfer(WebUtil.getAuthentication(), id);
			initIn();
			initSets(false);
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

	public ArrayList<SelectItem> getFilterBankAccounts() {
		return filterBankAccounts;
	}

	public ArrayList<SelectItem> getFilterCostTypes() {
		return filterCostTypes;
	}

	public ArrayList<SelectItem> getFilterTrials() {
		return filterTrials;
	}

	public MoneyTransferLazyModel getMoneyTransferModel() {
		return moneyTransferModel;
	}

	public MoneyTransferSummaryVO getOpenSummary() {
		return openSummary;
	}

	public StreamedContent getReimbursementsExcelStreamedContent() throws Exception {
		try {
			ReimbursementsExcelVO excel = WebUtil.getServiceLocator().getProbandService().exportReimbursements(WebUtil.getAuthentication(), probandId, null, null, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public String getReimbursementsPdfButtonLabel(SelectItem trial) {
		if (trial != null) {
			return Messages.getMessage(MessageCodes.REIMBURSEMENTS_TRIAL_LABEL, trial.getLabel());
		} else {
			return Messages.getString(MessageCodes.REIMBURSEMENTS_NO_TRIAL_LABEL);
		}
	}

	public String getReimbursementsPdfNoTrialButtonLabel() {
		return getReimbursementsPdfButtonLabel(null);
	}

	public StreamedContent getReimbursementsPdfNoTrialStreamedContent() throws Exception {
		return getReimbursementsPdfStreamedContent(null);
	}

	public StreamedContent getReimbursementsPdfStreamedContent(Long trialId) throws Exception {
		try {
			ReimbursementsPDFVO reimbursementPdf = WebUtil.getServiceLocator().getTrialService()
					.renderReimbursements(WebUtil.getAuthentication(), trialId, probandId, payOffPaymentMethod, false);
			return new DefaultStreamedContent(new ByteArrayInputStream(reimbursementPdf.getDocumentDatas()), reimbursementPdf.getContentType().getMimeType(),
					reimbursementPdf.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public ArrayList<SelectItem> getReimbursementTrials() {
		return reimbursementTrials;
	}

	public IDVO getSelectedMoneyTransfer() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(in.getTrialId());
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MONEY_TRANSFER_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets(true);
		}
	}

	private void initIn() {
		if (in == null) {
			in = new MoneyTransferInVO();
		}
		if (out != null) {
			copyMoneyTransferOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initMoneyTransferDefaultValues(in, probandId);
		}
	}

	@Override
	protected void initSpecificSets(boolean reset) {
		moneyTransferModel.setProbandId(in.getProbandId());
		moneyTransferModel.updateRowCount();
		filterBankAccounts = new ArrayList<SelectItem>(bankAccounts);
		filterBankAccounts.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		filterTrials = WebUtil.getReimbursementTrials(probandId, null, null, null);
		filterTrials.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		reimbursementTrials = WebUtil.getReimbursementTrials(probandId, null, payOffPaymentMethod, false);
		filterCostTypes = WebUtil.getMoneyTransferFilterCostTypes(null, null, null, probandId);
		openSummary = WebUtil.getProbandOpenReimbursementSummary(null, probandId, payOffPaymentMethod);
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (proband != null) { // this bean is postconstructed for some reason
			if (!WebUtil.isProbandPerson(proband)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.PROBAND_NOT_PERSON);
			} else {
				if (WebUtil.isProbandLocked(proband)) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
				}
				if (WebUtil.isTrialLocked(trial)) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
				}
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getProbandId() == null ? false : !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband);
	}

	public boolean isInputVisible() {
		return isCreated() || (!WebUtil.isProbandLocked(proband) && !WebUtil.isTrialLocked(trial) && WebUtil.isProbandPerson(proband));
	}

	public boolean isPerson() {
		return WebUtil.isProbandPerson(proband);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getMoneyTransfer(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_MONEY_TRANSFER, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets(true);
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets(true);
		return RESET_OUTCOME;
	}

	public void setSelectedMoneyTransfer(IDVO moneyTransfer) {
		if (moneyTransfer != null) {
			this.out = (MoneyTransferOutVO) moneyTransfer.getVo();
			this.initIn();
			initSets(true);
		}
	}

	@Override
	public String updateAction() {
		MoneyTransferInVO backup = new MoneyTransferInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateMoneyTransfer(WebUtil.getAuthentication(), in,
					Settings.getLongNullable(SettingCodes.MAX_COST_TYPES_PER_TRIAL, Bundle.SETTINGS, DefaultSettings.MAX_COST_TYPES_PER_TRIAL));
			initIn();
			initSets(false);
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public void updatePaid() {
		Long moneyTransferId = WebUtil.getLongParamValue(GetParamNames.MONEY_TRANSFER_ID);
		Long version = WebUtil.getLongParamValue(GetParamNames.VERSION);
		if (moneyTransferId != null && version != null) {
			try {
				WebUtil.getServiceLocator().getProbandService().setMoneyTransferPaid(WebUtil.getAuthentication(), moneyTransferId, version, true);
				initIn();
				initSets(false);
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}
}
