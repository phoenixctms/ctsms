package org.phoenixctms.ctsms.web.model.massmail;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.shared.MassMailRecipientLazyModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class RecipientOverviewLazyModel extends MassMailRecipientLazyModelBase {

	private Boolean pending;
	private boolean scheduled;

	public RecipientOverviewLazyModel() {
		super();
		pending = Settings.getBooleanNullable(SettingCodes.RECIPIENT_OVERVIEW_SHOW_PENDING_PRESET, Bundle.SETTINGS,
				DefaultSettings.RECIPIENT_OVERVIEW_SHOW_PENDING_PRESET);
		scheduled = Settings.getBoolean(SettingCodes.RECIPIENT_OVERVIEW_SHOW_SCHEDULED_PRESET, Bundle.SETTINGS,
				DefaultSettings.RECIPIENT_OVERVIEW_SHOW_SCHEDULED_PRESET);
	}

	@Override
	protected Collection<MassMailRecipientOutVO> getLazyResult(PSFVO psf) {
		emailMessageCache.clear();
		try {
			return WebUtil.getServiceLocator().getMassMailService().getPendingRecipientList(
					WebUtil.getAuthentication(), null, pending, scheduled, psf);
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<MassMailRecipientOutVO>();
	}

	public Boolean getPending() {
		return pending;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}
}
