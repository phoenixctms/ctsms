package org.phoenixctms.ctsms.web.model.proband;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.BankAccountInVO;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
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
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class BankAccountBean extends ManagedBeanBase {

	public static void copyBankAccountOutToIn(BankAccountInVO in, BankAccountOutVO out) {
		if (in != null && out != null) {
			ProbandOutVO probandVO = out.getProband();
			in.setAccountHolderName(out.getAccountHolderName());
			in.setAccountNumber(out.getAccountNumber());
			in.setActive(out.getActive());
			in.setNa(out.getNa());
			in.setBankCodeNumber(out.getBankCodeNumber());
			in.setBankName(out.getBankName());
			in.setBic(out.getBic());
			in.setIban(out.getIban());
			in.setId(out.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	public static void initBankAccountDefaultValues(BankAccountInVO in, Long probandId) {
		if (in != null) {
			// ProbandOutVO probandVO = WebUtil.getProband(probandId, null, null, null);
			boolean naPreset = Settings.getBoolean(SettingCodes.BANK_ACCOUNT_NA_PRESET, Bundle.SETTINGS, DefaultSettings.BANK_ACCOUNT_NA_PRESET);
			in.setAccountHolderName(naPreset ? null : loadAccountHolderName(probandId));
			in.setAccountNumber(Messages.getString(MessageCodes.ACCOUNT_NUMBER_PRESET));
			in.setActive(Settings.getBoolean(SettingCodes.BANK_ACCOUNT_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.BANK_ACCOUNT_ACTIVE_PRESET));
			in.setNa(naPreset);
			in.setBankCodeNumber(Messages.getString(MessageCodes.BANK_CODE_NUMBER_PRESET));
			in.setBankName(Messages.getString(MessageCodes.BANK_NAME_PRESET));
			in.setBic(Messages.getString(MessageCodes.BIC_PRESET));
			in.setIban(Messages.getString(MessageCodes.IBAN_PRESET));
			in.setId(null);
			in.setProbandId(probandId);
			in.setVersion(null);
		}
	}

	private static String loadAccountHolderName(Long probandId) {
		ProbandOutVO probandVO = WebUtil.getProband(probandId, null, null, null);
		return (probandVO == null ? Messages.getString(MessageCodes.ACCOUNT_HOLDER_NAME_PRESET) : probandVO.getName());
	}

	private BankAccountInVO in;
	private BankAccountOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private BankAccountLazyModel bankAccountModel;

	public BankAccountBean() {
		super();
		bankAccountModel = new BankAccountLazyModel();
	}

	@Override
	public String addAction() {
		BankAccountInVO backup = new BankAccountInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addBankAccount(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64, JSValues.AJAX_BANK_ACCOUNT_COUNT,
				MessageCodes.BANK_ACCOUNTS_TAB_TITLE, MessageCodes.BANK_ACCOUNTS_TAB_TITLE_WITH_COUNT, new Long(bankAccountModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64, JSValues.AJAX_MONEY_TRANSFER_COUNT,
					MessageCodes.MONEY_TRANSFERS_TAB_TITLE, MessageCodes.MONEY_TRANSFERS_TAB_TITLE_WITH_COUNT, WebUtil.getMoneyTransferCount(in.getProbandId(), null));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("bankaccount_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<String> completeBankCodeNumber(String query) {
		this.in.setBankCodeNumber(query);
		return WebUtil.completeBankCodeNumber(query, this.in.getBic(), this.in.getBankName());
	}

	public List<String> completeBankName(String query) {
		this.in.setBankName(query);
		return WebUtil.completeBankName(this.in.getBankCodeNumber(), this.in.getBic(), query);
	}

	public List<String> completeBic(String query) {
		this.in.setBic(query);
		return WebUtil.completeBic(this.in.getBankCodeNumber(), query, this.in.getBankName());
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteBankAccount(WebUtil.getAuthentication(), id);
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

	public BankAccountLazyModel getBankAccountModel() {
		return bankAccountModel;
	}

	public BankAccountInVO getIn() {
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

	public Long getMoneyTransferCount(BankAccountOutVO bankAccount) {
		if (bankAccount != null) {
			return WebUtil.getMoneyTransferCount(null, bankAccount.getId());
		}
		return null;
	}

	public BankAccountOutVO getOut() {
		return out;
	}

	public IDVO getSelectedBankAccount() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.BANK_ACCOUNT_TITLE, Long.toString(out.getId()), out.getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_BANK_ACCOUNT);
		}
	}

	public void handleBankCodeNumberSelect(SelectEvent event) {
		in.setBankCodeNumber((String) event.getObject());
	}

	public void handleBankNameSelect(SelectEvent event) {
		in.setBankName((String) event.getObject());
	}

	public void handleBicSelect(SelectEvent event) {
		in.setBic((String) event.getObject());
	}

	public void handleNaChange() {
		if (in.isNa()) {
			in.setAccountHolderName(null);
			in.setBankName(null);
			in.setIban(null);
			in.setBic(null);
			in.setAccountNumber(null);
			in.setBankCodeNumber(null);
		} else if (CommonUtil.isEmptyString(in.getAccountHolderName())) {
			in.setAccountHolderName(loadAccountHolderName(probandId));
		}
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.BANK_ACCOUNT_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new BankAccountInVO();
		}
		if (out != null) {
			copyBankAccountOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initBankAccountDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		bankAccountModel.setProbandId(in.getProbandId());
		bankAccountModel.updateRowCount();
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (proband != null) {
			if (!WebUtil.isProbandPerson(proband)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.PROBAND_NOT_PERSON);
			} else if (WebUtil.isProbandLocked(proband)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
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
		return isCreated() || (!WebUtil.isProbandLocked(proband) && WebUtil.isProbandPerson(proband));
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
			out = WebUtil.getServiceLocator().getProbandService().getBankAccount(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_BANK_ACCOUNT, Long.toString(out.getId()));
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (in.isNa()) {
			in.setAccountHolderName(null);
			in.setBankName(null);
			in.setIban(null);
			in.setBic(null);
			in.setAccountNumber(null);
			in.setBankCodeNumber(null);
			//} else if (CommonUtil.isEmptyString(in.getAccountHolderName())) {
			//	in.setNotify(false);
		}
	}

	public void setSelectedBankAccount(IDVO bankAccount) {
		if (bankAccount != null) {
			this.out = (BankAccountOutVO) bankAccount.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		BankAccountInVO backup = new BankAccountInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateBankAccount(WebUtil.getAuthentication(), in);
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
