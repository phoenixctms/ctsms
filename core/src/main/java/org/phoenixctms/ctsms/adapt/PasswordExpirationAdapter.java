package org.phoenixctms.ctsms.adapt;

import java.util.Date;
import java.util.Map;

import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.ServiceUtil;

public class PasswordExpirationAdapter extends ExpirationEntityAdapter {

	private Password password;

	public PasswordExpirationAdapter(Object proband, Date today, Map... caches) {
		setCaches(caches);
		setToday(today);
		setItem(proband);
	}

	@Override
	public Date getDate() {
		return ServiceUtil.getPasswordDate(password);
	}

	@Override
	public Object getItem() {
		return password;
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
		return password.getValidityPeriod();
	}

	@Override
	public Long getValidityPeriodDays() {
		return password.getValidityPeriodDays();
	}

	@Override
	public boolean isActive() {
		return password.isExpires();
	}

	@Override
	public boolean isNotify() {
		return password.isProlongable();
	}

	@Override
	public void setCaches(Map... caches) {
		// no cache required...
	}

	@Override
	protected void setItem(Object password) {
		this.password = (Password) password;
	}
}
