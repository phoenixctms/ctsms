package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
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
public class UserSearchBean extends SearchBeanBase {

	private UserSearchResultLazyModel userResultModel;
	private Date now;

	public UserSearchBean() {
		super();
		now = new Date();
		userResultModel = new UserSearchResultLazyModel();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.USER_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.USER_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.userResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.USER_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_USER_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_USER_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportUser(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	public Boolean getPasswordInvalid(UserOutVO user) {
		if (user != null && WebUtil.isLocalAuthMethod(user)) { // unboxing null ok..
			PasswordOutVO password;
			try {
				password = WebUtil.getServiceLocator().getUserService().getPassword(WebUtil.getAuthentication(), user.getId());
			} catch (ServiceException e) {
				return null;
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				return null;
			} catch (AuthorisationException e) {
				return null;
			} catch (IllegalArgumentException e) {
				return null;
			}
			if (password == null) {
				return true;
			}
			Boolean isPasswordExpired = WebUtil.isPasswordExpired(now, password);
			if (isPasswordExpired != null && isPasswordExpired) {
				return true;
			}
			Boolean isLogonLimitExceeded = WebUtil.isLogonLimitExceeded(password);
			if (isLogonLimitExceeded != null && isLogonLimitExceeded) {
				return true;
			}
			Boolean isWrongPasswordAttemtpsLimitExceeded = WebUtil.isWrongPasswordAttemtpsLimitExceeded(password);
			if (isWrongPasswordAttemtpsLimitExceeded != null && isWrongPasswordAttemtpsLimitExceeded) {
				return true;
			}
			return false;
		}
		return null;
	}

	public Boolean getPasswordValid(UserOutVO user) {
		Boolean passwordInvalid = getPasswordInvalid(user);
		if (passwordInvalid != null) {
			return !passwordInvalid;
		}
		return null;
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(userResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_USER_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(UserOutVO user) {
		return getSetPickerIDJSCall(user == null ? null : user.getId(), WebUtil.clipStringPicker(WebUtil.userOutVOToString(user)));
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.USER_CRITERIA_TITLE, MessageCodes.CREATE_NEW_USER_CRITERIA, operationSuccess);
	}

	public UserSearchResultLazyModel getUserResultModel() {
		return userResultModel;
	}

	@PostConstruct
	private void init() {
		initPickTarget();
		Long id = WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID);
		if (id != null) {
			this.load(id);
		} else {
			loadDefault();
		}
	}

	@Override
	protected void initSpecificSets() {
		now = new Date();
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return false;
	}

	@Override
	public String searchAction() {
		userResultModel.setCriteriaIn(criteriaIn);
		userResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		userResultModel.updateRowCount();
		DataTable.clearFilters(getResultListId());
		return SEARCH_OUTCOME;
	}

	public String userToColor(UserOutVO user) {
		if (user != null) {
			if (user.getLocked()) {
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.USER_LOCKED_COLOR, Bundle.SETTINGS, DefaultSettings.USER_LOCKED_COLOR));
			}
			Boolean passwordValid = getPasswordValid(user);
			if (passwordValid != null) {
				if (passwordValid) {
					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.PASSWORD_VALID_COLOR, Bundle.SETTINGS, DefaultSettings.PASSWORD_VALID_COLOR));
				} else {
					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.PASSWORD_INVALID_COLOR, Bundle.SETTINGS, DefaultSettings.PASSWORD_INVALID_COLOR));
				}
			}
		}
		return null;
	}
}
