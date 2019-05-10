package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ShiftDurationSummaryDetailVO;
import org.phoenixctms.ctsms.vo.ShiftDurationSummaryVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ShiftDurationSummaryModel extends EagerDataModelBase {

	public enum ShiftDurationSummaryType {
		STAFF, TRIAL, STAFF_OVERVIEW, TRIAL_OVERVIEW, UNDEFINED;
	}

	public static ShiftDurationSummaryModel getCachedShiftDurationModel(StaffOutVO staff, Date now, HashMap<Long, ShiftDurationSummaryModel> shiftDurationModelCache) {
		ShiftDurationSummaryModel model;
		if (staff != null && shiftDurationModelCache != null) {
			long staffId = staff.getId();
			if (shiftDurationModelCache.containsKey(staffId)) {
				model = shiftDurationModelCache.get(staffId);
			} else {
				model = new ShiftDurationSummaryModel(now, ShiftDurationSummaryType.STAFF, staffId);
				shiftDurationModelCache.put(staffId, model);
			}
		} else {
			model = new ShiftDurationSummaryModel(ShiftDurationSummaryType.STAFF);
		}
		return model;
	}

	public static ShiftDurationSummaryModel getCachedShiftDurationModel(TrialOutVO trial, Date now, HashMap<Long, ShiftDurationSummaryModel> shiftDurationModelCache) {
		ShiftDurationSummaryModel model;
		if (trial != null && shiftDurationModelCache != null) {
			long trialId = trial.getId();
			if (shiftDurationModelCache.containsKey(trialId)) {
				model = shiftDurationModelCache.get(trialId);
			} else {
				model = new ShiftDurationSummaryModel(now, ShiftDurationSummaryType.TRIAL, trialId);
				shiftDurationModelCache.put(trialId, model);
			}
		} else {
			model = new ShiftDurationSummaryModel(ShiftDurationSummaryType.TRIAL);
		}
		return model;
	}

	private Date start;
	private Date stop;
	private String calendar;
	private ArrayList<SelectItem> filterCalendars;
	private ShiftDurationSummaryType type;
	private ShiftDurationSummaryVO shiftDurationSummary;
	private Long id;

	public ShiftDurationSummaryModel(Date now, ShiftDurationSummaryType type, Long id) {
		super();
		reset(now, type, id);
	}

	public ShiftDurationSummaryModel(ShiftDurationSummaryType type) {
		super();
		reset(new Date(), type, null);
	}

	public String getCalendar() {
		return calendar;
	}

	public String getCalendarRe() {
		return calendar;
	}

	@Override
	protected Collection<ShiftDurationSummaryDetailVO> getEagerResult(PSFVO psf) {
		return getSummary().getAssigneds();
	}

	public ArrayList<SelectItem> getFilterCalendars() {
		if (filterCalendars == null) {
			switch (type) {
				case STAFF:
					filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(null, null, id);
					break;
				case TRIAL:
					filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(null, id, null);
					break;
				case STAFF_OVERVIEW:
				case TRIAL_OVERVIEW:
					filterCalendars = WebUtil.getDutyRosterTurnFilterCalendars(null, null, null);
					break;
				default:
					filterCalendars = new ArrayList<SelectItem>();
			}
		}
		return filterCalendars;
	}

	@Override
	protected ShiftDurationSummaryDetailVO getRowElement(Long id) {
		return null;
	}

	public Date getStart() {
		return start;
	}

	public Date getStartRe() {
		return start;
	}

	public Date getStop() {
		return stop;
	}

	public Date getStopRe() {
		return stop;
	}

	public ShiftDurationSummaryVO getSummary() {
		if (shiftDurationSummary == null) {
			if ((ShiftDurationSummaryType.STAFF.equals(type) && id != null) || ShiftDurationSummaryType.TRIAL_OVERVIEW.equals(type)) {
				try {
					shiftDurationSummary = WebUtil.getServiceLocator().getTrialService().getShiftDurationSummary(WebUtil.getAuthentication(), id, calendar, start, stop);
					return shiftDurationSummary;
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			} else if ((ShiftDurationSummaryType.TRIAL.equals(type) && id != null) || ShiftDurationSummaryType.STAFF_OVERVIEW.equals(type)) {
				try {
					shiftDurationSummary = WebUtil.getServiceLocator().getStaffService().getShiftDurationSummary(WebUtil.getAuthentication(), id, calendar, start, stop);
					return shiftDurationSummary;
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			shiftDurationSummary = new ShiftDurationSummaryVO();
		}
		return shiftDurationSummary;
	}

	public void reset(Date now, ShiftDurationSummaryType type, Long id) {
		this.stop = WebUtil.addIntervals(now,
				Settings.getVariablePeriod(SettingCodes.SHIFT_DURATION_SUMMARY_PERIOD_AFTER, Bundle.SETTINGS, DefaultSettings.SHIFT_DURATION_SUMMARY_PERIOD_AFTER),
				Settings.getLongNullable(SettingCodes.SHIFT_DURATION_SUMMARY_PERIOD_AFTER_DAYS, Bundle.SETTINGS, DefaultSettings.SHIFT_DURATION_SUMMARY_PERIOD_AFTER_DAYS), 1);
		this.start = WebUtil.subIntervals(now,
				Settings.getVariablePeriod(SettingCodes.SHIFT_DURATION_SUMMARY_PERIOD_BEFORE, Bundle.SETTINGS, DefaultSettings.SHIFT_DURATION_SUMMARY_PERIOD_BEFORE),
				Settings.getLongNullable(SettingCodes.SHIFT_DURATION_SUMMARY_PERIOD_BEFORE_DAYS, Bundle.SETTINGS, DefaultSettings.SHIFT_DURATION_SUMMARY_PERIOD_BEFORE_DAYS), 1);
		this.calendar = null;
		this.type = type;
		this.id = id;
		filterCalendars = null;
		resetRows();
	}

	@Override
	public void resetRows() {
		super.resetRows();
		shiftDurationSummary = null;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public void setCalendarRe(String calendar) {
		resetRows();
		this.calendar = calendar;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setStartRe(Date start) {
		resetRows();
		this.start = start;
	}

	public void setStop(Date stop) {
		this.stop = stop;
	}

	public void setStopRe(Date stop) {
		resetRows();
		this.stop = stop;
	}

	public void update(ActionEvent e) {
		resetRows();
	}
}
