package org.phoenixctms.ctsms.web.model.inventory;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel.BookingDurationSummaryType;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;

@ManagedBean
@ViewScoped
public class BookingDurationOverviewBean extends ManagedBeanBase {

	private Date now;
	private BookingDurationSummaryModel bookingDurationModel;

	public BookingDurationOverviewBean() {
		super();
		now = new Date();
		bookingDurationModel = new BookingDurationSummaryModel(BookingDurationSummaryType.INVENTORY_OVERVIEW);
	}

	public BookingDurationSummaryModel getBookingDurationModel() {
		return bookingDurationModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		now = new Date();
		bookingDurationModel.reset(now, BookingDurationSummaryType.INVENTORY_OVERVIEW, null);
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}
}
