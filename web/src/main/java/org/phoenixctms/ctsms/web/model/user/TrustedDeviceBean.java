package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.security.TrustedDeviceUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.TrustedDeviceOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class TrustedDeviceBean extends ManagedBeanBase {

	private Long userId;
	private TrustedDeviceOutVO selected;
	private List<TrustedDeviceOutVO> devices;

	public TrustedDeviceBean() {
		devices = new ArrayList<TrustedDeviceOutVO>();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRUSTED_DEVICE_TAB_TITLE_BASE64,
				JSValues.AJAX_TRUSTED_DEVICE_COUNT, MessageCodes.TRUSTED_DEVICES_TAB_TITLE, MessageCodes.TRUSTED_DEVICES_TAB_TITLE_WITH_COUNT,
				new Long(devices.size()));
		if (operationSuccess && userId != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_USER_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT,
					MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCountSafe(JournalModule.USER_JOURNAL, userId));
		}
	}

	@Override
	protected String changeAction(Long id) {
		selected = null;
		userId = id;
		loadDevices();
		return CHANGE_OUTCOME;
	}

	public void deleteAll() {
		actionPostProcess(deleteAllAction());
	}

	public String deleteAllAction() {
		if (userId == null) {
			return ERROR_OUTCOME;
		}
		try {
			WebUtil.getServiceLocator().getUserService().deleteTrustedDevices(WebUtil.getAuthentication(), userId);
			clearCurrentDeviceCookie();
			selected = null;
			loadDevices();
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

	@Override
	public String deleteAction() {
		if (selected == null) {
			return ERROR_OUTCOME;
		}
		try {
			WebUtil.getServiceLocator().getUserService().deleteTrustedDevice(WebUtil.getAuthentication(), selected.getId());
			clearCurrentDeviceCookie();
			selected = null;
			loadDevices();
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

	public List<TrustedDeviceOutVO> getDevices() {
		return devices;
	}

	public TrustedDeviceOutVO getSelected() {
		return selected;
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return selected != null;
	}

	public boolean isDropAllEnabled() {
		return userId != null && !devices.isEmpty();
	}

	public void refresh() {
		Long selectedId = selected == null ? null : selected.getId();
		loadDevices();
		selected = null;
		if (selectedId != null) {
			for (int i = 0; i < devices.size(); i++) {
				if (selectedId.equals(devices.get(i).getId())) {
					selected = devices.get(i);
					break;
				}
			}
		}
		appendRequestContextCallbackArgs(false);
	}

	public void setSelected(TrustedDeviceOutVO selected) {
		this.selected = selected;
	}

	private void clearCurrentDeviceCookie() {
		if (TrustedDeviceUtil.consumeRemovedCurrent()) {
			AuthenticationVO auth = WebUtil.getAuthentication();
			if (auth != null) {
				WebUtil.clearTrustedDeviceToken(auth.getUsername());
			}
		}
	}

	private static int compareLastUsedDescending(TrustedDeviceOutVO left, TrustedDeviceOutVO right) {
		Date leftUsed = left == null ? null : left.getLastUsed();
		Date rightUsed = right == null ? null : right.getLastUsed();
		if (leftUsed == null && rightUsed == null) {
			return 0;
		}
		if (leftUsed == null) {
			return 1;
		}
		if (rightUsed == null) {
			return -1;
		}
		return rightUsed.compareTo(leftUsed);
	}

	private void loadDevices() {
		devices.clear();
		if (userId == null) {
			return;
		}
		try {
			Collection<TrustedDeviceOutVO> loaded = WebUtil.getServiceLocator().getUserService().getTrustedDevices(WebUtil.getAuthentication(), userId);
			if (loaded != null) {
				devices.addAll(loaded);
				Collections.sort(devices, new Comparator<TrustedDeviceOutVO>() {

					@Override
					public int compare(TrustedDeviceOutVO left, TrustedDeviceOutVO right) {
						return compareLastUsedDescending(left, right);
					}
				});
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
	}
}
