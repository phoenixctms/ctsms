package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel.BookingDurationSummaryType;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;

@ManagedBean
@ViewScoped
public class TrialAssociationBean extends ManagedBeanBase {

	private Date now;
	private Long trialId;
	private ShiftDurationSummaryModel shiftDurationModel;
	private TrialCourseLazyModel trialCourseModel;
	private BookingDurationSummaryModel bookingDurationModel;

	private EnrollmentChartBean enrollmentChartBean;

	public TrialAssociationBean() {
		super();
		now = new Date();
		shiftDurationModel = new ShiftDurationSummaryModel(ShiftDurationSummaryType.TRIAL);
		trialCourseModel = new TrialCourseLazyModel();
		bookingDurationModel = new BookingDurationSummaryModel(BookingDurationSummaryType.TRIAL);
		enrollmentChartBean = new EnrollmentChartBean();
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("trialcourses_list");
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public BookingDurationSummaryModel getBookingDurationModel() {
		return bookingDurationModel;
	}

	public EnrollmentChartBean getEnrollmentChartBean() {
		return enrollmentChartBean;
	}

	public ShiftDurationSummaryModel getShiftDurationModel() {
		return shiftDurationModel;
	}

	public TrialCourseLazyModel getTrialCourseModel() {
		return trialCourseModel;
	}



	public void handleBookingDurationSummaryChange() {
		now = new Date();
		bookingDurationModel.reset(now, BookingDurationSummaryType.TRIAL, trialId);
	}




	public void handleEnrollmentChartChange() {
		enrollmentChartBean.changeRootEntity(trialId);
	}

	public void handleShiftDurationSummaryChange() {
		now = new Date();
		shiftDurationModel.reset(now, ShiftDurationSummaryType.TRIAL, trialId);
	}

	public void handleTrialCoursesChange() {
		trialCourseModel.setTrialId(trialId);
		trialCourseModel.updateRowCount();
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
		trialCourseModel.setTrialId(trialId);
		trialCourseModel.updateRowCount();
		shiftDurationModel.reset(now, ShiftDurationSummaryType.TRIAL, null); //trialId);
		bookingDurationModel.reset(now, BookingDurationSummaryType.TRIAL, null); //trialId);
		enrollmentChartBean.changeRootEntity(null); // trialId);
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public void refreshTrialCourses() {
		trialCourseModel.updateRowCount();
		LazyDataModelBase.clearFilters("trialcourses_list");
	}
}
