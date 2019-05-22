package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

public abstract class UserSettingsBeanBase extends ManagedBeanBase {

	private ArrayList<SelectItem> locales;
	private TimeZoneVO timeZone;
	private ArrayList<SelectItem> themes;
	private ArrayList<SelectItem> dateFormats;
	private ArrayList<SelectItem> decimalSeparators;
	private long tableColumnCount;

	protected UserSettingsBeanBase() {
		timeZone = null;
		tableColumnCount = 0l;
	}

	public ArrayList<SelectItem> getDateFormats() {
		return dateFormats;
	}

	public ArrayList<SelectItem> getDecimalSeparators() {
		return decimalSeparators;
	}

	public ArrayList<SelectItem> getLocales() {
		return locales;
	}

	public ArrayList<SelectItem> getThemes() {
		return themes;
	}

	public TimeZoneVO getTimeZone() {
		return timeZone;
	}

	public void handleTimeZoneSelect(SelectEvent event) {
	}

	public void handleTimeZoneUnselect(UnselectEvent event) {
	}

	protected abstract void initSpecificSets();

	protected final void initSets() {
		if (this.locales == null) {
			this.locales = WebUtil.getLocales();
		}
		loadTimeZone();
		if (this.themes == null) {
			Map<String, String> themeMap = Settings.getThemes();
			this.themes = new ArrayList<SelectItem>(themeMap.size());
			Iterator<String> it = themeMap.keySet().iterator();
			while (it.hasNext()) {
				String themeName = it.next();
				this.themes.add(new SelectItem(themeName, themeMap.get(themeName)));
			}
		}
		this.dateFormats = WebUtil.getDateFormats(getDateFormat());
		if (this.decimalSeparators == null) {
			this.decimalSeparators = WebUtil.getDecimalSeparators();
		}
		loadTableColumnCount();
		initSpecificSets();
	}

	private void loadTableColumnCount() {
		tableColumnCount = 0l;
		if (getUserId() != null) {
			try {
				tableColumnCount = WebUtil.getServiceLocator().getUserService().getDataTableColumnCount(WebUtil.getAuthentication(),
						getUserId(), null, null);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
	}

	private void loadTimeZone() {
		this.timeZone = WebUtil.getTimeZone(getUserTimeZone());
	}

	protected void sanitizeInVals() {
		if (timeZone != null) {
			setUserTimeZone(timeZone.getTimeZoneID());
		} else {
			setUserTimeZone(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(getDateFormat())) {
			setDateFormat(null);
		}
		if (CommonUtil.NO_SELECTION_VALUE.equals(getDecimalSeparator())) {
			setDecimalSeparator(null);
		}
	}

	public void setTimeZone(TimeZoneVO timeZone) {
		this.timeZone = timeZone;
	}

	public final void clearTableColumns() {
		actionPostProcess(clearTableColumnsAction());
	}

	public String clearTableColumnsAction() {
		try {
			WebUtil.getServiceLocator().getUserService().clearDataTableColumns(WebUtil.getAuthentication(), getUserId(), null, null);
			loadTableColumnCount();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public long getTableColumnCount() {
		return tableColumnCount;
	}

	public List<TimeZoneVO> completeTimeZone(String query) {
		try {
			return (List<TimeZoneVO>) WebUtil.getServiceLocator().getToolsService().completeTimeZone(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<TimeZoneVO>();
	}

	public abstract Long getUserId();

	protected abstract String getUserTimeZone();

	public abstract String getLocale();

	public abstract String getDateFormat();

	public abstract String getDecimalSeparator();

	public abstract String getTheme();

	public abstract boolean isShowTooltips();

	protected abstract void setUserTimeZone(String timeZoneID);

	public abstract void setLocale(String locale);

	public abstract void setDateFormat(String dateFormat);

	public abstract void setDecimalSeparator(String decimalSeparator);

	public abstract void setTheme(String theme);

	public abstract void setShowTooltips(boolean showTooltips);
}
