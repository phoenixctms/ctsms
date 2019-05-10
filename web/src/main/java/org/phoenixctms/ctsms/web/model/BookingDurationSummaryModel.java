package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryBookingDurationSummaryDetailVO;
import org.phoenixctms.ctsms.vo.InventoryBookingDurationSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class BookingDurationSummaryModel extends EagerDataModelBase {

	public enum BookingDurationSummaryType {
		INVENTORY_OVERVIEW, INVENTORY, TRIAL, UNDEFINED;
	}

	private Date start;
	private Date stop;
	private String calendar;
	private ArrayList<SelectItem> filterCalendars;
	private BookingDurationSummaryType type;
	private InventoryBookingDurationSummaryVO bookingDurationSummary;
	private Long id;

	public BookingDurationSummaryModel(BookingDurationSummaryType type) {
		super();
		reset(new Date(), type, null);
	}

	public BookingDurationSummaryModel(Date now, BookingDurationSummaryType type, Long id) {
		super();
		reset(now, type, id);
	}

	public String getCalendar() {
		return calendar;
	}

	public String getCalendarRe() {
		return calendar;
	}

	@Override
	protected Collection<InventoryBookingDurationSummaryDetailVO> getEagerResult(PSFVO psf) {
		return getSummary().getAssigneds();
	}

	public ArrayList<SelectItem> getFilterCalendars() {
		if (filterCalendars == null) {
			switch (type) {
				case INVENTORY:
					filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, id, null, null, null);
					break;
				case TRIAL:
					filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, null, null, id);
					break;
				case INVENTORY_OVERVIEW:
					filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, null, null, null);
					break;
				default:
					filterCalendars = new ArrayList<SelectItem>();
			}
		}
		return filterCalendars;
	}

	@Override
	protected InventoryBookingDurationSummaryDetailVO getRowElement(Long id) {
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

	public InventoryBookingDurationSummaryVO getSummary() {
		if (bookingDurationSummary == null) {
			if ((BookingDurationSummaryType.INVENTORY.equals(type) && id != null) || BookingDurationSummaryType.INVENTORY_OVERVIEW.equals(type)) {
				try {
					bookingDurationSummary = WebUtil.getServiceLocator().getInventoryService().getBookingDurationSummary(WebUtil.getAuthentication(), id, calendar, start, stop);
					return bookingDurationSummary;
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			} else if (BookingDurationSummaryType.TRIAL.equals(type) && id != null) {
				try {
					bookingDurationSummary = WebUtil.getServiceLocator().getTrialService().getBookingDurationSummary(WebUtil.getAuthentication(), id, calendar, start, stop);
					return bookingDurationSummary;
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			bookingDurationSummary = new InventoryBookingDurationSummaryVO();
		}
		return bookingDurationSummary;
	}

	public void reset(Date now, BookingDurationSummaryType type, Long id) {
		this.stop = WebUtil.addIntervals(now,
				Settings.getVariablePeriod(SettingCodes.BOOKING_DURATION_SUMMARY_PERIOD_AFTER, Bundle.SETTINGS, DefaultSettings.BOOKING_DURATION_SUMMARY_PERIOD_AFTER),
				Settings.getLongNullable(SettingCodes.BOOKING_DURATION_SUMMARY_PERIOD_AFTER_DAYS, Bundle.SETTINGS, DefaultSettings.BOOKING_DURATION_SUMMARY_PERIOD_AFTER_DAYS), 1);
		this.start = WebUtil
				.subIntervals(now, Settings.getVariablePeriod(SettingCodes.BOOKING_DURATION_SUMMARY_PERIOD_BEFORE, Bundle.SETTINGS,
						DefaultSettings.BOOKING_DURATION_SUMMARY_PERIOD_BEFORE),
						Settings.getLongNullable(SettingCodes.BOOKING_DURATION_SUMMARY_PERIOD_BEFORE_DAYS,
								Bundle.SETTINGS, DefaultSettings.BOOKING_DURATION_SUMMARY_PERIOD_BEFORE_DAYS),
						1);
		this.calendar = null;
		this.type = type;
		this.id = id;
		filterCalendars = null;
		resetRows();
	}

	@Override
	public void resetRows() {
		super.resetRows();
		bookingDurationSummary = null;
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
