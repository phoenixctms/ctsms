package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class MoneyTransferOverviewBean extends ManagedBeanBase {

	private ArrayList<SelectItem> departments;
	private ArrayList<SelectItem> filterCostTypes;
	private Collection<PaymentMethodVO> paymentMethods;
	private TrialMoneyTransferSummaryLazyModel trialMoneyTransferSummaryModel;

	public MoneyTransferOverviewBean() {
		super();
		trialMoneyTransferSummaryModel = new TrialMoneyTransferSummaryLazyModel();
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public ArrayList<SelectItem> getFilterCostTypes() {
		return filterCostTypes;
	}

	public Collection<PaymentMethodVO> getPaymentMethods() {
		return paymentMethods;
	}

	public TrialMoneyTransferSummaryLazyModel getTrialMoneyTransferSummaryModel() {
		return trialMoneyTransferSummaryModel;
	}

	public void handleDepartmentCostTypeChange() {
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
		if (departments == null) {
			StaffOutVO user = WebUtil.getUserIdentity();
			departments = WebUtil.getVisibleDepartments(user == null ? null : user.getDepartment().getId());
		}
		filterCostTypes = WebUtil.getMoneyTransferFilterCostTypes(trialMoneyTransferSummaryModel.getDepartmentId(), null, trialMoneyTransferSummaryModel.getProbandDepartmentId(),
				null);
		// Collection<PaymentMethodVO> paymentMethodVOs = null;
		if (paymentMethods == null) {
			try {
				paymentMethods = WebUtil.getServiceLocator().getSelectionSetService().getPaymentMethods(WebUtil.getAuthentication());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		trialMoneyTransferSummaryModel.updateRowCount();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}
}