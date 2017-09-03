package org.phoenixctms.ctsms.adapt;

import java.util.Date;
import java.util.Map;

import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;

public class ProbandAutoDeleteExpirationAdapter extends ExpirationEntityAdapter {

	private Proband proband;

	public ProbandAutoDeleteExpirationAdapter(Object proband, Date today, Map... caches) {
		setCaches(caches);
		setToday(today);
		setItem(proband);
	}

	@Override
	public Date getDate() {
		return proband.getAutoDeleteDeadline();
	}

	@Override
	public Object getItem() {
		return proband;
	}

	@Override
	public VariablePeriod getReminderPeriod() {
		return null;
	}

	@Override
	public Long getReminderPeriodDays() {
		return null;
	}

	@Override
	public VariablePeriod getValidityPeriod() {
		return null;
	}

	@Override
	public Long getValidityPeriodDays() {
		return null;
	}

	@Override
	public boolean isActive() {
		return proband.getCategory().isPrivacyConsentControl() && proband.getPrivacyConsentStatus().isAutoDelete();
	}

	@Override
	public boolean isNotify() {
		return true;
	}

	@Override
	public void setCaches(Map... caches) {
		// no cache required...
	}

	@Override
	protected void setItem(Object proband) {
		this.proband = (Proband) proband;
	}
}