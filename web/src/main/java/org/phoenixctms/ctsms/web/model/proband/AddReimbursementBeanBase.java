package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class AddReimbursementBeanBase extends MoneyTransferBeanBase {

	private final static String MONEY_TRANSFER_COMMENT_SEPRATOR = "\n";

	public static void initMoneyTransferDefaultValues(MoneyTransferInVO in, Long probandId, Long trialId) {
		if (in != null) {
			in.setAmount(Settings.getFloatNullable(SettingCodes.MONEY_TRANSFER_AMOUNT_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_AMOUNT_PRESET));
			in.setBankAccountId(null);
			in.setId(null);
			in.setTrialId(trialId);
			in.setCostType(Messages.getString(MessageCodes.MONEY_TRANSFER_COST_TYPE_PRESET));
			in.setMethod(Settings.getPaymentMethod(SettingCodes.MONEY_TRANSFER_METHOD_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_METHOD_PRESET));
			in.setProbandId(probandId);
			in.setReasonForPayment(Messages.getString(MessageCodes.REASON_FOR_PAYMENT_PRESET));
			in.setReference(Messages.getString(MessageCodes.REFERENCE_PRESET));
			in.setVoucherCode(Messages.getString(MessageCodes.VOUCHER_CODE_PRESET));
			in.setComment(Messages.getString(MessageCodes.MONEY_TRANSFER_COMMENT_PRESET));
			in.setTransactionTimestamp(new Date());
			in.setPaid(Settings.getBoolean(SettingCodes.MONEY_TRANSFER_PAID_PRESET, Bundle.SETTINGS, DefaultSettings.MONEY_TRANSFER_PAID_PRESET));
			in.setShowComment(Settings.getBoolean(SettingCodes.ADD_REIMBURSEMENT_SHOW_COMMENT, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_SHOW_COMMENT));
			in.setVersion(null);
		}
	}

	protected ProbandOutVO proband;
	protected Long trialId;
	protected Long probandId;
	private ProbandAddressOutVO address;
	private float distance;
	private boolean addTravelExpense;
	private boolean addAccommodationExpense;
	private boolean addTicketExpense;
	private float travelExpenseFactor;
	private boolean travelOneWay;
	private float travelExpenseAmount;
	private float accommodationExpenseAmount;
	private float ticketExpenseAmount;
	private ArrayList<SelectItem> ticketTypes;
	private String ticketType;
	protected boolean addReimbursement;
	protected float reimbursementAmount;

	public AddReimbursementBeanBase() {
		super();
		change();
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

	protected void appendAccommodationExpenseComment(StringBuilder comment, Date start, Date stop) {
		if (addAccommodationExpense) {
			if (comment.length() > 0) {
				comment.append(MONEY_TRANSFER_COMMENT_SEPRATOR);
			}
			comment.append(Messages.getMessage(
					MessageCodes.ADD_ACCOMMODATION_EXPENSE_COMMENT,
					CommonUtil.formatDate(start,
							Settings.getString(SettingCodes.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN)),
					CommonUtil.formatDate(stop,
							Settings.getString(SettingCodes.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN)),
					accommodationExpenseAmount,
					WebUtil.getCurrencySymbol()));
		}
	}

	protected void appendReimbursementComment(StringBuilder comment, Date start, String title) {
		if (CommonUtil.isEmptyString(title)) {
			title = "";
		}
		if (addReimbursement) {
			if (comment.length() > 0) {
				comment.append(MONEY_TRANSFER_COMMENT_SEPRATOR);
			}
			comment.append(Messages.getMessage(
					MessageCodes.ADD_REIMBURSEMENT_COMMENT,
					CommonUtil.formatDate(start,
							Settings.getString(SettingCodes.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN)),
					title,
					reimbursementAmount,
					WebUtil.getCurrencySymbol()));
		}
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		if (operationSuccess && in.getProbandId() != null) {
			RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64,
					JSValues.AJAX_MONEY_TRANSFER_COUNT,
					MessageCodes.MONEY_TRANSFERS_TAB_TITLE, MessageCodes.MONEY_TRANSFERS_TAB_TITLE_WITH_COUNT, WebUtil.getMoneyTransferCount(in.getProbandId(), null));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	protected void appendTicketExpenseComment(StringBuilder comment, Date start, String title) {
		if (CommonUtil.isEmptyString(title)) {
			title = "";
		}
		if (addTicketExpense) {
			if (comment.length() > 0) {
				comment.append(MONEY_TRANSFER_COMMENT_SEPRATOR);
			}
			comment.append(Messages.getMessage(
					MessageCodes.ADD_TICKET_EXPENSE_COMMENT,
					CommonUtil.formatDate(start,
							Settings.getString(SettingCodes.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN)),
					title,
					CommonUtil.isEmptyString(ticketType) ? Messages.getString(MessageCodes.EMPTY_TRAVEL_TICKET_TYPE) : ticketType,
					ticketExpenseAmount,
					WebUtil.getCurrencySymbol()));
		}
	}

	protected void appendTravelExpenseComment(StringBuilder comment, Date start, String title) {
		if (CommonUtil.isEmptyString(title)) {
			title = "";
		}
		if (addTravelExpense) {
			if (comment.length() > 0) {
				comment.append(MONEY_TRANSFER_COMMENT_SEPRATOR);
			}
			comment.append(Messages.getMessage(
					MessageCodes.ADD_TRAVEL_EXPENSE_COMMENT,
					CommonUtil.formatDate(start,
							Settings.getString(SettingCodes.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.ADD_REIMBURSEMENT_COMMENT_DATE_PATTERN)),
					title,
					travelOneWay ? "1" : "2",
					distance,
					travelExpenseFactor,
					travelExpenseAmount,
					WebUtil.getCurrencySymbol()));
		}
	}

	public float getAccommodationExpenseAmount() {
		return accommodationExpenseAmount;
	}

	public String getAddReimbursementLabel() {
		String reimbursementTitle = getReimbursementTitle();
		if (CommonUtil.isEmptyString(reimbursementTitle)) {
			return Messages.getString(MessageCodes.ADD_REIMBURSEMENT_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.ADD_REIMBURSEMENT_FOR_LABEL, reimbursementTitle);
		}
	}

	public ProbandAddressOutVO getAddress() {
		return address;
	}

	public String getAddTravelExpenseLabel() {
		if (address != null) {
			return Messages.getMessage(MessageCodes.ADD_TRAVEL_EXPENSE_ADDRESS_LABEL, address.getCivicName());
			// CommonUtil.clipString(address.getCivicName(), Settings.getInt(SettingCodes.ADD_REIMBURSEMENT_COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
			// DefaultSettings.ADD_REIMBURSEMENT_COMMENT_CLIP_MAX_LENGTH)));
		} else {
			return Messages.getString(MessageCodes.ADD_TRAVEL_EXPENSE_LABEL);
		}
	}

	protected abstract String getComment();

	// public String getAliquotReimbursementLabel() {
	// return "";
	// }
	public float getDistance() {
		return distance;
	}

	public float getReimbursementAmount() {
		return reimbursementAmount;
	}

	protected abstract String getReimbursementTitle();

	// @Override
	// public String getTitle() {
	// if (out != null) {
	// if (out.getTrial() != null) {
	// if (!CommonUtil.isEmptyString(out.getCostType())) {
	// return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TRIAL_COST_TYPE_TITLE, Long.toString(out.getId()), WebUtil.trialOutVOToString(out.getTrial()), out
	// .getMethod().getName(), out.getCostType(), out.getAmount(), WebUtil.getCurrencySymbol());
	// } else {
	// return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TRIAL_TITLE, Long.toString(out.getId()), WebUtil.trialOutVOToString(out.getTrial()), out.getMethod()
	// .getName(), out.getAmount(), WebUtil.getCurrencySymbol());
	// }
	// } else {
	// if (!CommonUtil.isEmptyString(out.getCostType())) {
	// return Messages.getMessage(MessageCodes.MONEY_TRANSFER_COST_TYPE_TITLE, Long.toString(out.getId()), out.getMethod().getName(), out.getCostType(),
	// out.getAmount(), WebUtil.getCurrencySymbol());
	// } else {
	// return Messages.getMessage(MessageCodes.MONEY_TRANSFER_TITLE, Long.toString(out.getId()), out.getMethod().getName(), out.getAmount(),
	// WebUtil.getCurrencySymbol());
	// }
	// }
	// } else {
	// return Messages.getString(MessageCodes.CREATE_NEW_MONEY_TRANSFER);
	// }
	// }
	public float getTicketExpenseAmount() {
		return ticketExpenseAmount;
	}
	// public float getTravelExpenseFactor() {
	// return travelExpenseFactor;
	// }

	public String getTicketType() {
		return ticketType;
	}

	public ArrayList<SelectItem> getTicketTypes() {
		return ticketTypes;
	}

	public float getTravelExpenseAmount() {
		return travelExpenseAmount;
	}

	public void handleDistanceOneWayChange() {
		travelExpenseAmount = (travelOneWay ? distance : 2.0f * distance) * travelExpenseFactor;
	}

	protected void initIn() {
		if (in == null) {
			in = new MoneyTransferInVO();
		}
		if (out != null) {
			copyMoneyTransferOutToIn(in, out);
			probandId = in.getProbandId();
			trialId = in.getTrialId();
		} else {
			initMoneyTransferDefaultValues(in, probandId, trialId);
		}
	}

	@Override
	protected void initSpecificSets(boolean reset) {
		if (reset) {
			distance = 0.0f;
			travelOneWay = false;
			travelExpenseFactor = Settings.getFloat(SettingCodes.ADD_REIMBURSEMENT_TRAVEL_EXPENSES_FACTOR, Bundle.SETTINGS,
					DefaultSettings.ADD_REIMBURSEMENT_TRAVEL_EXPENSES_FACTOR);
			proband = WebUtil.getProband(in.getProbandId(), null, null, null);
			address = null;
			if (in.getProbandId() != null) {
				try {
					address = WebUtil.getServiceLocator().getProbandService().getWireTransferProbandAddress(WebUtil.getAuthentication(), in.getProbandId());
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			travelExpenseAmount = 0.0f;
			addTravelExpense = address != null; // must be true, otherwise distance value is not retrieved && travelExpenseAmount > 0.0f;
			ticketExpenseAmount = 0.0f;
			ticketTypes = new ArrayList<SelectItem>();
			Iterator<String> it = Messages.getStringList(MessageCodes.TRAVEL_TICKET_TYPES).iterator();
			while (it.hasNext()) {
				String ticketType = it.next();
				ticketTypes.add(new SelectItem(ticketType, ticketType));
			}
			ticketType = null;
			addTicketExpense = ticketExpenseAmount > 0.0f;
			accommodationExpenseAmount = 0.0f;
			addAccommodationExpense = accommodationExpenseAmount > 0.0f;
		}
	}

	public boolean isAddAccommodationExpense() {
		return addAccommodationExpense;
	}

	public boolean isAddReimbursement() {
		return addReimbursement;
	}

	public boolean isAddTicketExpense() {
		return addTicketExpense;
	}

	public boolean isAddTravelExpense() {
		return addTravelExpense;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isTravelOneWay() {
		return travelOneWay;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets(true);
		return RESET_OUTCOME;
	}

	public void setAccommodationExpenseAmount(float accommodationExpenseAmount) {
		this.accommodationExpenseAmount = accommodationExpenseAmount;
	}

	public void setAddAccommodationExpense(boolean addAccommodationExpense) {
		this.addAccommodationExpense = addAccommodationExpense;
	}

	public void setAddReimbursement(boolean addReimbursement) {
		this.addReimbursement = addReimbursement;
	}

	// public void setTravelExpenseFactor(float travelExpenseFactor) {
	// this.travelExpenseFactor = travelExpenseFactor;
	// }
	public void setAddTicketExpense(boolean addTicketExpense) {
		this.addTicketExpense = addTicketExpense;
	}

	public void setAddTravelExpense(boolean addTravelExpense) {
		this.addTravelExpense = addTravelExpense;
	}

	public void setDistance(float distance) {
		// System.out.println(distance);
		this.distance = distance;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}

	public void setReimbursementAmount(float reimbursementAmount) {
		this.reimbursementAmount = reimbursementAmount;
	}

	public void setTicketExpenseAmount(float ticketExpenseAmount) {
		this.ticketExpenseAmount = ticketExpenseAmount;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public void setTravelExpenseAmount(float travelExpenseAmount) {
		this.travelExpenseAmount = travelExpenseAmount;
	}

	public void setTravelOneWay(boolean travelOneWay) {
		this.travelOneWay = travelOneWay;
	}

	public void updateCommentAmount(ActionEvent event) {
		in.setAmount((addTravelExpense ? travelExpenseAmount : 0.0f) + (addReimbursement ? reimbursementAmount : 0.0f)
				+ (addAccommodationExpense ? accommodationExpenseAmount : 0.0f) + (addTicketExpense ? ticketExpenseAmount : 0.0f));
		in.setComment(getComment());
	}
}
