package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.OTPAuthenticatorTypeVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.OTPAuthenticatorTypeSelector;
import org.phoenixctms.ctsms.web.model.OTPAuthenticatorTypeSelectorListener;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class PasswordBean extends ManagedBeanBase implements VariablePeriodSelectorListener, OTPAuthenticatorTypeSelectorListener {

	private static final int VALIDITY_PERIOD_PROPERTY_ID = 1;
	private static final int OTP_AUTHENTICATOR_TYPE_PROPERTY_ID = 1;

	public static void copyPasswordOutToIn(PasswordInVO in, PasswordOutVO out) {
		if (in != null && out != null) {
			VariablePeriodVO validityPeriodVO = out.getValidityPeriod();
			OTPAuthenticatorTypeVO otpTyoeVO = out.getOtpType();
			in.setExpires(out.getExpires());
			in.setProlongable(out.getProlongable());
			in.setLimitLogons(out.getLimitLogons());
			in.setLimitWrongPasswordAttempts(out.getLimitWrongPasswordAttempts());
			in.setMaxSuccessfulLogons(out.getMaxSuccessfulLogons());
			in.setMaxWrongPasswordAttemptsSinceLastSuccessfulLogon(out.getMaxWrongPasswordAttemptsSinceLastSuccessfulLogon());
			in.setPassword(null);
			in.setValidityPeriod(validityPeriodVO == null ? null : validityPeriodVO.getPeriod());
			in.setValidityPeriodDays(out.getValidityPeriodDays());
			in.setEnable2fa(out.getEnable2fa());
			in.setOtpType(otpTyoeVO == null ? null : otpTyoeVO.getType());
		}
	}

	public static void initPasswordDefaultValues(PasswordInVO in) {
		if (in != null) {
			PasswordInVO newPassword = WebUtil.getNewPassword();
			in.setExpires(newPassword.getExpires());
			in.setProlongable(newPassword.getProlongable());
			in.setLimitLogons(newPassword.getLimitLogons());
			in.setLimitWrongPasswordAttempts(newPassword.getLimitWrongPasswordAttempts());
			in.setMaxSuccessfulLogons(newPassword.getMaxSuccessfulLogons());
			in.setMaxWrongPasswordAttemptsSinceLastSuccessfulLogon(newPassword.getMaxWrongPasswordAttemptsSinceLastSuccessfulLogon());
			in.setPassword(null);
			in.setValidityPeriod(newPassword.getValidityPeriod());
			in.setValidityPeriodDays(newPassword.getValidityPeriodDays());
			in.setEnable2fa(newPassword.getEnable2fa());
			in.setOtpType(newPassword.getOtpType());
		}
	}

	private Date now;
	private PasswordInVO in;
	private PasswordOutVO out;
	private Long userId;
	private UserOutVO user;
	private VariablePeriodSelector validity;
	private String departmentPassword;
	private OTPAuthenticatorTypeSelector otpType;

	public PasswordBean() {
		super();
		setValidity(new VariablePeriodSelector(this, VALIDITY_PERIOD_PROPERTY_ID));
		setOtpType(new OTPAuthenticatorTypeSelector(this, OTP_AUTHENTICATOR_TYPE_PROPERTY_ID));
	}

	public String addAction(boolean resetOtpSecret) {
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().adminSetPassword(WebUtil.getAuthentication(), userId, in, departmentPassword, resetOtpSecret);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			departmentPassword = null;
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String addAction() {
		return addAction(false);
	}

	public final void addResetOtpSecret() {
		actionPostProcess(addResetOtpSecretAction());
	}

	public String addResetOtpSecretAction() {
		return addAction(true);
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if (ADD_OUTCOME.equals(outcome) && WebUtil.isUserIdLoggedIn(userId)) {
			WebUtil.logout();
		} else {
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
				if (operationSuccess && userId != null) {
					WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PASSWORD_TAB_TITLE_BASE64, JSValues.AJAX_PASSWORD_COUNT,
							MessageCodes.PASSWORD_TAB_TITLE, MessageCodes.PASSWORD_TAB_TITLE_WITH_COUNT, userId == null ? null : out != null ? 1l : 0l);
					WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT,
							MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT,
							WebUtil.getJournalCountSafe(JournalModule.USER_JOURNAL, userId));
				}
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getUserService().getPassword(WebUtil.getAuthentication(), id);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		this.userId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void generatePassword() {
		in.setPassword(WebUtil.getNewPassword().getPassword());
	}

	public Boolean getExpired() {
		return WebUtil.isPasswordExpired(now, out);
	}

	public PasswordInVO getIn() {
		return in;
	}

	public Boolean getLocalAuthMethod() {
		if (out != null) {
			return WebUtil.isLocalAuthMethod(out.getInheritedUser());
		}
		return null;
	}

	public Boolean getLogonLimitExceeded() {
		return WebUtil.isLogonLimitExceeded(out);
	}

	public PasswordOutVO getOut() {
		return out;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case VALIDITY_PERIOD_PROPERTY_ID:
				return this.in.getValidityPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	@Override
	public String getTitle() {
		return Messages.getString(MessageCodes.SET_NEW_PASSWORD_TITLE);
	}

	public VariablePeriodSelector getValidity() {
		return validity;
	}

	public String getValidityPeriodString() {
		if (out != null && out.getValidityPeriod() != null) {
			return WebUtil.variablePeriodToString(out.getValidityPeriod(), out.getValidityPeriodDays());
		}
		return "";
	}

	public Boolean getWrongPasswordAttemtpsLimitExceeded() {
		return WebUtil.isWrongPasswordAttemtpsLimitExceeded(out);
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.USER_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new PasswordInVO();
		}
		if (out != null) {
			copyPasswordOutToIn(in, out);
		} else {
			initPasswordDefaultValues(in);
		}
	}

	private void initSets() {
		departmentPassword = null;
		now = new Date();
		user = WebUtil.getUser(userId, null, null, null);
		if (Messages.getMessageList().isEmpty()) {
			if (WebUtil.isUserIdLoggedIn(userId)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ACTIVE_USER);
			}
			ArrayList<String> messageCodes = new ArrayList<String>();
			if (WebUtil.getUserAuthMessages(user, now, messageCodes)) {
				Iterator<String> it = messageCodes.iterator();
				while (it.hasNext()) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, it.next());
				}
			} else {
				Iterator<String> it = messageCodes.iterator();
				while (it.hasNext()) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, it.next());
				}
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return (userId == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public boolean isMaxSuccessfulLogonsSpinnerEnabled() {
		return this.in.getLimitLogons();
	}

	public boolean isMaxWrongPasswordAttemptsSinceLastSuccessfulLogonSpinnerEnabled() {
		return this.in.getLimitWrongPasswordAttempts();
	}

	public boolean isProlongableEnabled() {
		return this.in.isExpires();
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	public boolean isValidityPeriodSelectorEnabled() {
		return this.in.getExpires();
	}

	public boolean isValidityPeriodSpinnerEnabled() {
		return (this.in.getExpires() && (this.in.getValidityPeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getValidityPeriod())));
	}

	@Override
	public String loadAction() {
		return loadAction(this.userId);
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getUserService().getPassword(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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
		initIn();
		initSets();
		if (out != null) {
			initPasswordDefaultValues(in);
		}
		return RESET_OUTCOME;
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case VALIDITY_PERIOD_PROPERTY_ID:
				this.in.setValidityPeriod(period);
				break;
			default:
		}
	}

	public void setValidity(VariablePeriodSelector validity) {
		this.validity = validity;
	}

	public void setOtpType(OTPAuthenticatorTypeSelector otpType) {
		this.otpType = otpType;
	}

	public OTPAuthenticatorTypeSelector getOtpType() {
		return otpType;
	}

	private void sanitizeInVals() {
		if (!in.getExpires()) {
			in.setProlongable(false);
		}
		if (!isDepartmentPasswordRequired()) {
			departmentPassword = null;
		}
	}

	public boolean isDepartmentPasswordRequired() {
		return !WebUtil.getUser().getDepartment().getId().equals(user != null ? user.getDepartment().getId() : null);
	}

	public String getDepartmentPasswordLabel() {
		return Messages.getMessage(MessageCodes.PASSWORD_DEPARTMENT_PASSWORD_LABEL,
				user != null ? user.getDepartment().getName() : null);
	}

	public String getDepartmentPassword() {
		return departmentPassword;
	}

	public void setDepartmentPassword(String departmentPassword) {
		this.departmentPassword = departmentPassword;
	}

	@Override
	public void setOtpAuthenticatorType(int property, OTPAuthenticatorType type) {
		switch (property) {
			case OTP_AUTHENTICATOR_TYPE_PROPERTY_ID:
				this.in.setOtpType(type);
				break;
			default:
		}
	}

	@Override
	public OTPAuthenticatorType getOtpAuthenticatorType(int property) {
		switch (property) {
			case OTP_AUTHENTICATOR_TYPE_PROPERTY_ID:
				return this.in.getOtpType();
			default:
				return OTPAuthenticatorTypeSelectorListener.NO_SELECTION_OTP_AUTHENTICATION_TYPE;
		}
	}
}
