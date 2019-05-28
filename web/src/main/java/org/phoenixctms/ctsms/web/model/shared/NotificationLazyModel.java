package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.NotificationVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class NotificationLazyModel extends LazyDataModelBase<NotificationVO> {

	@Override
	protected Collection<NotificationVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getUserService().getNotificationList(WebUtil.getAuthentication(),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_OBSOLETE, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_OBSOLETE),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SEND, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SEND),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SHOW, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SHOW),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SENT, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SENT),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_DROPPED, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_DROPPED),
					psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<NotificationVO>();
	}

	@Override
	protected NotificationVO getRowElement(Long id) {
		try {
			return WebUtil.getServiceLocator().getUserService().getNotification(WebUtil.getAuthentication(), id);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return null;
	}
}
