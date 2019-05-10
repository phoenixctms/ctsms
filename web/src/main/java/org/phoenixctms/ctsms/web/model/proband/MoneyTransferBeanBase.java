package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.PaymentMethodSelector;
import org.phoenixctms.ctsms.web.model.PaymentMethodSelectorListener;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;

public abstract class MoneyTransferBeanBase extends ManagedBeanBase implements PaymentMethodSelectorListener {

	private static final int METHOD_PROPERTY_ID = 1;

	public static void copyMoneyTransferOutToIn(MoneyTransferInVO in, MoneyTransferOutVO out) {
		if (in != null && out != null) {
			ProbandOutVO probandVO = out.getProband();
			BankAccountOutVO bankAccountVO = out.getBankAccount();
			TrialOutVO trialVO = out.getTrial();
			PaymentMethodVO methodVO = out.getMethod();
			in.setAmount(out.getAmount());
			in.setBankAccountId(bankAccountVO == null ? null : bankAccountVO.getId());
			in.setId(out.getId());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setCostType(out.getCostType());
			in.setMethod(methodVO == null ? null : methodVO.getPaymentMethod());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setReasonForPayment(out.getReasonForPayment());
			in.setVoucherCode(out.getVoucherCode());
			in.setComment(out.getComment());
			in.setReference(out.getReference());
			in.setTransactionTimestamp(out.getTransactionTimestamp());
			in.setPaid(out.getPaid());
			in.setShowComment(out.getShowComment());
			in.setVersion(out.getVersion());
		}
	}

	protected MoneyTransferInVO in;
	protected MoneyTransferOutVO out;
	protected ArrayList<SelectItem> bankAccounts;
	private PaymentMethodSelector method;

	public MoneyTransferBeanBase() {
		super();
		setMethod(new PaymentMethodSelector(this, METHOD_PROPERTY_ID));
	}

	public List<String> completeCostType(String query) {
		in.setCostType(query);
		return getCompleteCostTypeList(query);
	}

	public ArrayList<SelectItem> getBankAccounts() {
		return bankAccounts;
	}

	private List<String> getCompleteCostTypeList(String query) {
		Collection<String> costTypes = null;
		try {
			costTypes = WebUtil.getServiceLocator().getProbandService().completeCostTypes(WebUtil.getAuthentication(),
					null, in.getTrialId(), null, null, query, null); // let permission argument override decide...
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (costTypes != null) {
			try {
				return ((List<String>) costTypes);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public MoneyTransferInVO getIn() {
		return in;
	}

	public PaymentMethodSelector getMethod() {
		return method;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public void getNewPaymentReference(ActionEvent event) {
		try {
			in.setReference(WebUtil.getServiceLocator().getProbandService().getNewPaymentReference(WebUtil.getAuthentication(), in));
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public MoneyTransferOutVO getOut() {
		return out;
	}

	@Override
	public PaymentMethod getPaymentMethod(int property) {
		switch (property) {
			case METHOD_PROPERTY_ID:
				return this.in.getMethod();
			default:
				return PaymentMethodSelectorListener.NO_SELECTION_PAYMENT_METHOD;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			if (out.getTrial() != null) {
				if (!CommonUtil.isEmptyString(out.getCostType())) {
					return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TRIAL_COST_TYPE_TITLE, Long.toString(out.getId()), WebUtil.trialOutVOToString(out.getTrial()), out
							.getMethod().getName(), out.getCostType(), out.getAmount(), WebUtil.getCurrencySymbol());
				} else {
					return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TRIAL_TITLE, Long.toString(out.getId()), WebUtil.trialOutVOToString(out.getTrial()), out.getMethod()
							.getName(), out.getAmount(), WebUtil.getCurrencySymbol());
				}
			} else {
				if (!CommonUtil.isEmptyString(out.getCostType())) {
					return Messages.getMessage(MessageCodes.MONEY_TRANSFER_COST_TYPE_TITLE, Long.toString(out.getId()), out.getMethod().getName(), out.getCostType(),
							out.getAmount(), WebUtil.getCurrencySymbol());
				} else {
					return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TITLE, Long.toString(out.getId()), out.getMethod().getName(), out.getAmount(),
							WebUtil.getCurrencySymbol());
				}
			}
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_MONEY_TRANSFER);
		}
	}

	public void handleCostTypeSelect(SelectEvent event) {
		in.setCostType((String) event.getObject());
	}

	public void handleMethodChange() {
		if (!isBankAccountVisible()) {
			in.setBankAccountId(null);
		}
		if (!isReferenceVisible()) {
			in.setReference(null);
		}
		if (!isReasonForPaymentVisible()) {
			in.setReasonForPayment(null);
		}
		if (!isVoucherCodeVisible()) {
			in.setVoucherCode(null);
		}
	}

	protected void initSets(boolean reset) {
		if (reset) {
			Collection<BankAccountOutVO> bankAccountVOs = null;
			if (in.getProbandId() != null) {
				try {
					bankAccountVOs = WebUtil.getServiceLocator().getProbandService().getBankAccounts(WebUtil.getAuthentication(), in.getProbandId(), true, in.getBankAccountId());
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			if (bankAccountVOs != null) {
				bankAccounts = new ArrayList<SelectItem>(bankAccountVOs.size());
				Iterator<BankAccountOutVO> it = bankAccountVOs.iterator();
				while (it.hasNext()) {
					BankAccountOutVO bankAccountVO = it.next();
					bankAccounts.add(new SelectItem(Long.toString(bankAccountVO.getId()), bankAccountVO.getName()));
				}
			} else {
				bankAccounts = new ArrayList<SelectItem>();
			}
		}
		initSpecificSets(reset);
	}

	protected abstract void initSpecificSets(boolean reset);

	public boolean isBankAccountVisible() {
		return PaymentMethod.WIRE_TRANSFER.equals(in.getMethod());
	}

	public boolean isReasonForPaymentVisible() {
		return isBankAccountVisible();
	}

	public boolean isReferenceVisible() {
		return isBankAccountVisible();
	}

	public boolean isVoucherCodeVisible() {
		return PaymentMethod.VOUCHER.equals(in.getMethod());
	}

	protected void sanitizeInVals() {
		if (!isBankAccountVisible()) {
			in.setBankAccountId(null);
		}
		if (!isReferenceVisible()) {
			in.setReference(null);
		}
		if (!isReasonForPaymentVisible()) {
			in.setReasonForPayment(null);
		}
		if (!isVoucherCodeVisible()) {
			in.setVoucherCode(null);
		}
	}

	public void setMethod(PaymentMethodSelector method) {
		this.method = method;
	}

	@Override
	public void setPaymentMethod(int property, PaymentMethod paymentMethod) {
		switch (property) {
			case METHOD_PROPERTY_ID:
				this.in.setMethod(paymentMethod);
				break;
			default:
		}
	}
}
