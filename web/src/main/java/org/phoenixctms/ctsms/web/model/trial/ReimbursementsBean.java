package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class ReimbursementsBean extends ManagedBeanBase {

	private Long trialId;
	private ArrayList<SelectItem> filterProbandGroups;
	private LinkedHashMap<PaymentMethod, PaymentMethodVO> paymentMethodMap;
	private Boolean paid;
	private Collection<String> costTypes;
	private ProbandMoneyTransferSummaryLazyModel probandMoneyTransferSummaryModel;
	private ProbandMoneyTransferNoParticipationSummaryLazyModel probandMoneyTransferNoParticipationSummaryModel;
	private HashMap<Long, HashMap<Long, MoneyTransferSummaryVO>> noParticipationOpenSummaryCache;
	private HashMap<Long, MoneyTransferSummaryVO> openSummaryCache;
	private HashMap<Long, HashMap<Long, MoneyTransferSummaryVO>> noParticipationPayOffSummaryCache;
	private HashMap<Long, MoneyTransferSummaryVO> payOffSummaryCache;
	private MoneyTransferSummaryVO openPaidSummary;
	private MoneyTransferSummaryVO openSummary;
	private MoneyTransferSummaryVO paidSummary;
	private MoneyTransferSummaryVO payOffSummary;
	private PaymentMethod payOffPaymentMethod;
	private boolean allProbandListEntries;

	public ReimbursementsBean() {
		super();
		allProbandListEntries = Settings.getBoolean(SettingCodes.PROBAND_MONEY_TRANSFER_SUMMARY_SHOW_ALL_PROBAND_LIST_ENTRIES, Bundle.SETTINGS,
				DefaultSettings.PROBAND_MONEY_TRANSFER_SUMMARY_SHOW_ALL_PROBAND_LIST_ENTRIES);
		paid = Settings.getBooleanNullable(SettingCodes.PROBAND_MONEY_TRANSFER_SUMMARY_SHOW_PAID_PRESET, Bundle.SETTINGS,
				DefaultSettings.PROBAND_MONEY_TRANSFER_SUMMARY_SHOW_PAID_PRESET);
		payOffPaymentMethod = Settings.getPaymentMethod(SettingCodes.PAYOFF_PAYMENT_METHOD, Bundle.SETTINGS, DefaultSettings.PAYOFF_PAYMENT_METHOD);
		noParticipationOpenSummaryCache = new HashMap<Long, HashMap<Long, MoneyTransferSummaryVO>>();
		openSummaryCache = new HashMap<Long, MoneyTransferSummaryVO>();
		noParticipationPayOffSummaryCache = new HashMap<Long, HashMap<Long, MoneyTransferSummaryVO>>();
		payOffSummaryCache = new HashMap<Long, MoneyTransferSummaryVO>();
		probandMoneyTransferSummaryModel = new ProbandMoneyTransferSummaryLazyModel(allProbandListEntries);
		probandMoneyTransferNoParticipationSummaryModel = new ProbandMoneyTransferNoParticipationSummaryLazyModel(allProbandListEntries);
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandmoneytransfersummary_list");
		probandMoneyTransferSummaryModel.clearSelectedColumns();
		DataTable.clearFilters("probandmoneytransfernoparticipationsummary_list");
		probandMoneyTransferNoParticipationSummaryModel.clearSelectedColumns();
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public Collection<String> getCostTypes() {
		return costTypes;
	}

	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
	}

	public MoneyTransferSummaryVO getOpenNoParticipationSummary(ProbandOutVO proband) {
		if (trialId != null && proband != null) {
			HashMap<Long, MoneyTransferSummaryVO> cache;
			MoneyTransferSummaryVO summary;
			if (noParticipationOpenSummaryCache.containsKey(trialId)) {
				cache = noParticipationOpenSummaryCache.get(trialId);
				if (cache.containsKey(proband.getId())) {
					summary = cache.get(proband.getId());
				} else {
					summary = WebUtil.getProbandOpenReimbursementSummary(trialId, proband.getId(), null);
					cache.put(proband.getId(), summary);
				}
			} else {
				cache = new HashMap<Long, MoneyTransferSummaryVO>();
				noParticipationOpenSummaryCache.put(trialId, cache);
				summary = WebUtil.getProbandOpenReimbursementSummary(trialId, proband.getId(), null);
				cache.put(proband.getId(), summary);
			}
			return summary;
		}
		return null;
	}

	public MoneyTransferSummaryVO getOpenPaidSummary() {
		return openPaidSummary;
	}

	public MoneyTransferSummaryVO getOpenSummary() {
		return openSummary;
	}

	public MoneyTransferSummaryVO getOpenSummary(ProbandListEntryOutVO listEntry) {
		if (listEntry != null) {
			MoneyTransferSummaryVO summary;
			if (openSummaryCache.containsKey(listEntry.getId())) {
				summary = openSummaryCache.get(listEntry.getId());
			} else {
				summary = WebUtil.getProbandOpenReimbursementSummary(listEntry, null);
				openSummaryCache.put(listEntry.getId(), summary);
			}
			return summary;
		}
		return null;
	}

	public Boolean getPaid() {
		return paid;
	}

	public MoneyTransferSummaryVO getPaidSummary() {
		return paidSummary;
	}

	public Collection<PaymentMethodVO> getPaymentMethods() {
		return paymentMethodMap.values();
	}

	public MoneyTransferSummaryVO getPayOffNoParticipationSummary(ProbandOutVO proband) {
		if (trialId != null && proband != null) {
			HashMap<Long, MoneyTransferSummaryVO> cache;
			MoneyTransferSummaryVO summary;
			if (noParticipationPayOffSummaryCache.containsKey(trialId)) {
				cache = noParticipationPayOffSummaryCache.get(trialId);
				if (cache.containsKey(proband.getId())) {
					summary = cache.get(proband.getId());
				} else {
					summary = WebUtil.getProbandOpenReimbursementSummary(trialId, proband.getId(), payOffPaymentMethod);
					cache.put(proband.getId(), summary);
				}
			} else {
				cache = new HashMap<Long, MoneyTransferSummaryVO>();
				noParticipationPayOffSummaryCache.put(trialId, cache);
				summary = WebUtil.getProbandOpenReimbursementSummary(trialId, proband.getId(), payOffPaymentMethod);
				cache.put(proband.getId(), summary);
			}
			return summary;
		}
		return null;
	}

	public MoneyTransferSummaryVO getPayOffSummary() {
		return payOffSummary;
	}

	public MoneyTransferSummaryVO getPayOffSummary(ProbandListEntryOutVO listEntry) {
		if (listEntry != null) {
			MoneyTransferSummaryVO summary;
			if (payOffSummaryCache.containsKey(listEntry.getId())) {
				summary = payOffSummaryCache.get(listEntry.getId());
			} else {
				summary = WebUtil.getProbandOpenReimbursementSummary(listEntry, payOffPaymentMethod);
				payOffSummaryCache.put(listEntry.getId(), summary);
			}
			return summary;
		}
		return null;
	}

	public ProbandMoneyTransferNoParticipationSummaryLazyModel getProbandMoneyTransferNoParticipationSummaryModel() {
		return probandMoneyTransferNoParticipationSummaryModel;
	}

	public ProbandMoneyTransferSummaryLazyModel getProbandMoneyTransferSummaryModel() {
		return probandMoneyTransferSummaryModel;
	}

	public String getReimbursementsByCostTypeExcelButtonLabel(String costType) {
		return getReimbursementsExcelButtonLabel(costType, null, null);
	}

	public String getReimbursementsByCostTypeExcelButtonLabel(String costType, boolean paid) {
		return getReimbursementsExcelButtonLabel(costType, null, paid);
	}

	public StreamedContent getReimbursementsByCostTypeExcelStreamedContent(String costType) throws Exception {
		return getReimbursementsExcelStreamedContent(costType, null, null);
	}

	public StreamedContent getReimbursementsByCostTypeExcelStreamedContent(String costType, boolean paid) throws Exception {
		return getReimbursementsExcelStreamedContent(costType, null, paid);
	}

	public String getReimbursementsByPaymentMethodExcelButtonLabel(PaymentMethodVO methodVO) {
		return getReimbursementsExcelButtonLabel(null, methodVO.getPaymentMethod(), null);
	}

	public String getReimbursementsByPaymentMethodExcelButtonLabel(PaymentMethodVO methodVO, boolean paid) {
		return getReimbursementsExcelButtonLabel(null, methodVO.getPaymentMethod(), paid);
	}

	public StreamedContent getReimbursementsByPaymentMethodExcelStreamedContent(PaymentMethodVO methodVO) throws Exception {
		return getReimbursementsExcelStreamedContent(null, methodVO.getPaymentMethod(), null);
	}

	public StreamedContent getReimbursementsByPaymentMethodExcelStreamedContent(PaymentMethodVO methodVO, boolean paid) throws Exception {
		return getReimbursementsExcelStreamedContent(null, methodVO.getPaymentMethod(), paid);
	}

	public String getReimbursementsExcelButtonLabel() {
		return getReimbursementsExcelButtonLabel(null, null, null);
	}

	public String getReimbursementsExcelButtonLabel(boolean paid) {
		return getReimbursementsExcelButtonLabel(null, null, paid);
	}

	private String getReimbursementsExcelButtonLabel(String costType, PaymentMethod method, Boolean paid) {
		if (costType == null) {
			if (method == null) {
				if (paid == null) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_AND_NOT_PAID_LABEL);
				} else if (paid) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_LABEL);
				} else {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_NOT_PAID_LABEL);
				}
			} else {
				PaymentMethodVO methodVO = paymentMethodMap.get(method);
				String paymentMethodName = methodVO == null ? null : methodVO.getName();
				if (paid == null) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_AND_NOT_PAID_METHOD_LABEL, paymentMethodName);
				} else if (paid) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_METHOD_LABEL, paymentMethodName);
				} else {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_NOT_PAID_METHOD_LABEL, paymentMethodName);
				}
			}
		} else {
			String temp;
			if (costType.length() > 0) {
				temp = costType;
			} else {
				temp = Messages.getString(MessageCodes.EMPTY_COST_TYPE);
			}
			if (method == null) {
				if (paid == null) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_AND_NOT_PAID_COST_TYPE_LABEL, temp);
				} else if (paid) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_COST_TYPE_LABEL, temp);
				} else {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_NOT_PAID_COST_TYPE_LABEL, temp);
				}
			} else {
				PaymentMethodVO methodVO = paymentMethodMap.get(method);
				String paymentMethodName = methodVO == null ? null : methodVO.getName();
				if (paid == null) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_AND_NOT_PAID_METHOD_COST_TYPE_LABEL, paymentMethodName, temp);
				} else if (paid) {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_PAID_METHOD_COST_TYPE_LABEL, paymentMethodName, temp);
				} else {
					return Messages.getMessage(MessageCodes.REIMBURSEMENTS_NOT_PAID_METHOD_COST_TYPE_LABEL, paymentMethodName, temp);
				}
			}
		}
	}

	public StreamedContent getReimbursementsExcelStreamedContent() throws Exception {
		return getReimbursementsExcelStreamedContent(null, null, null);
	}

	public StreamedContent getReimbursementsExcelStreamedContent(boolean paid) throws Exception {
		return getReimbursementsExcelStreamedContent(null, null, paid);
	}

	private StreamedContent getReimbursementsExcelStreamedContent(String costType, PaymentMethod method, Boolean paid) throws Exception {
		try {
			ReimbursementsExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportReimbursements(WebUtil.getAuthentication(), trialId, costType, method, paid);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getReimbursementsPdfStreamedContent() throws Exception {
		return getReimbursementsPdfStreamedContent(null);
	}

	public StreamedContent getReimbursementsPdfStreamedContent(Long probandId) throws Exception {
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

	public StreamedContent getVisitScheduleExcelStreamedContent(Long probandId) throws Exception {
		try {
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitSchedule(WebUtil.getAuthentication(), trialId, probandId, null, null, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public void handleMoneyTransferPaidChange() {
		initSets();
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		noParticipationOpenSummaryCache.clear();
		openSummaryCache.clear();
		noParticipationPayOffSummaryCache.clear();
		payOffSummaryCache.clear();
		costTypes = null;
		if (trialId != null) {
			try {
				costTypes = WebUtil.getServiceLocator().getProbandService().getCostTypes(WebUtil.getAuthentication(), null, trialId, null, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (costTypes == null) {
			costTypes = new ArrayList<String>();
		}
		openPaidSummary = WebUtil.getTrialMoneyTransferSummary(trialId, null, null, null);
		paidSummary = WebUtil.getTrialMoneyTransferSummary(trialId, null, null, true);
		openSummary = WebUtil.getTrialMoneyTransferSummary(trialId, null, null, false);
		payOffSummary = WebUtil.getTrialMoneyTransferSummary(trialId, null, payOffPaymentMethod, false);
		probandMoneyTransferSummaryModel.setPaid(paid);
		probandMoneyTransferSummaryModel.setCostTypes(costTypes);
		probandMoneyTransferSummaryModel.setTrialId(trialId);
		probandMoneyTransferSummaryModel.updateRowCount();
		probandMoneyTransferNoParticipationSummaryModel.setPaid(paid);
		probandMoneyTransferNoParticipationSummaryModel.setCostTypes(costTypes);
		probandMoneyTransferNoParticipationSummaryModel.setTrialId(trialId);
		probandMoneyTransferNoParticipationSummaryModel.updateRowCount();
		filterProbandGroups = WebUtil.getProbandGroups(trialId);
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		if (paymentMethodMap == null) {
			paymentMethodMap = WebUtil.getPaymentMethodMap();
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isListEntriesCountOnly() {
		return !allProbandListEntries;
	}

	public void refreshProbandMoneyTransferSummaries() {
		initSets();
		DataTable.clearFilters("probandmoneytransfersummary_list");
		DataTable.clearFilters("probandmoneytransfernoparticipationsummary_list");
	}

	public void setListEntriesCountOnly(boolean listEntriesCountOnly) {
		this.allProbandListEntries = !listEntriesCountOnly;
		probandMoneyTransferSummaryModel.setTotal(this.allProbandListEntries);
		probandMoneyTransferNoParticipationSummaryModel.setTotal(this.allProbandListEntries);
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
		probandMoneyTransferSummaryModel.setPaid(paid);
		probandMoneyTransferNoParticipationSummaryModel.setPaid(paid);
	}

	public void updateMoneyTransfersPaid() {
		Long trialId = WebUtil.getLongParamValue(GetParamNames.TRIAL_ID);
		Long probandId = WebUtil.getLongParamValue(GetParamNames.PROBAND_ID);
		if (trialId != null && probandId != null) {
			try {
				WebUtil.getServiceLocator().getProbandService().setAllMoneyTransfersPaid(WebUtil.getAuthentication(), probandId, trialId, true);
				initSets();
				addOperationSuccessMessage("reimbursementsMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				Messages.addMessageClientId("reimbursementsMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessageClientId("reimbursementsMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}
}
