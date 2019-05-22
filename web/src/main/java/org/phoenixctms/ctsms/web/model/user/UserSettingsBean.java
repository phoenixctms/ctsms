package org.phoenixctms.ctsms.web.model.user;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserSettingsInVO;
import org.phoenixctms.ctsms.web.model.shared.UserSettingsBeanBase;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class UserSettingsBean extends UserSettingsBeanBase {

	public static void copyUserOutToIn(UserSettingsInVO in, UserOutVO out) {
		if (in != null && out != null) {
			in.setId(out.getId());
			in.setLocale(out.getLocale());
			in.setShowTooltips(out.getShowTooltips());
			in.setTimeZone(out.getTimeZone());
			in.setDateFormat(out.getDateFormat());
			in.setDecimalSeparator(out.getDecimalSeparator());
			in.setVersion(out.getVersion());
			in.setTheme(out.getTheme());
		}
	}

	private UserSettingsInVO in;
	private UserOutVO out;

	public UserSettingsBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if ((UPDATE_OUTCOME.equals(outcome)) && WebUtil.isUserIdLoggedIn(in.getId())) {
			WebUtil.logout();
		} else {
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			}
		}
	}

	public UserSettingsInVO getIn() {
		return in;
	}

	public UserOutVO getOut() {
		return out;
	}

	@PostConstruct
	private void init() {
		out = WebUtil.getUser();
		initIn();
		initSets();
	}

	private void initIn() {
		if (in == null) {
			in = new UserSettingsInVO();
		}
		//if (out != null) {
		copyUserOutToIn(in, out);
		//		} else {
		//			initUserDefaultValues(in, WebUtil.getUser());
		//		}
	}

	@Override
	public String resetAction() {
		out = WebUtil.getUser();
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	@Override
	public String updateAction() {
		UserSettingsInVO backup = new UserSettingsInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getUserService().updateUserSettings(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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
	public Long getUserId() {
		return in != null ? in.getId() : null;
	}

	@Override
	protected String getUserTimeZone() {
		return in != null ? in.getTimeZone() : null;
	}

	@Override
	public String getLocale() {
		return in != null ? in.getLocale() : null;
	}

	@Override
	public String getDateFormat() {
		return in != null ? in.getDateFormat() : null;
	}

	@Override
	public String getDecimalSeparator() {
		return in != null ? in.getDecimalSeparator() : null;
	}

	@Override
	public String getTheme() {
		return in != null ? in.getTheme() : null;
	}

	@Override
	public boolean isShowTooltips() {
		return in != null ? in.getShowTooltips() : false;
	}

	@Override
	protected void setUserTimeZone(String timeZoneID) {
		if (in != null) {
			in.setTimeZone(timeZoneID);
		}
	}

	@Override
	public void setLocale(String locale) {
		if (in != null) {
			in.setLocale(locale);
		}
	}

	@Override
	public void setDateFormat(String dateFormat) {
		if (in != null) {
			in.setDateFormat(dateFormat);
		}
	}

	@Override
	public void setDecimalSeparator(String decimalSeparator) {
		if (in != null) {
			in.setDecimalSeparator(decimalSeparator);
		}
	}

	@Override
	public void setTheme(String theme) {
		if (in != null) {
			in.setTheme(theme);
		}
	}

	@Override
	public void setShowTooltips(boolean showTooltips) {
		if (in != null) {
			in.setShowTooltips(showTooltips);
		}
	}

	@Override
	protected void initSpecificSets() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isCreateable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCreated() {
		return WebUtil.getSessionScopeBean().isLoggedIn();
	}
}