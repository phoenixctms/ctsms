package org.phoenixctms.ctsms.web.model.trial;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.web.adapt.ProbandListEntryTagValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.BookingDurationSummaryModel.BookingDurationSummaryType;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class TrialAssociationBean extends ManagedBeanBase {

	private Date now;
	private Long trialId;
	private ShiftDurationSummaryModel shiftDurationModel;
	private TrialCourseLazyModel trialCourseModel;
	private TrialMassMailLazyModel trialMassMailModel;
	private BookingDurationSummaryModel bookingDurationModel;
	private EnrollmentChartBean enrollmentChartBean;
	private RandomizationListCodeLazyModel randomizationListCodeModel;

	public TrialAssociationBean() {
		super();
		now = new Date();
		shiftDurationModel = new ShiftDurationSummaryModel(ShiftDurationSummaryType.TRIAL);
		trialCourseModel = new TrialCourseLazyModel();
		trialMassMailModel = new TrialMassMailLazyModel();
		bookingDurationModel = new BookingDurationSummaryModel(BookingDurationSummaryType.TRIAL);
		enrollmentChartBean = new EnrollmentChartBean();
		randomizationListCodeModel = new RandomizationListCodeLazyModel();
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("trialcourses_list");
		DataTable.clearFilters("trialmassmails_list");
		DataTable.clearFilters("randomizationlistcodes_list");
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

	public String getMassMailProgressLabel(MassMailOutVO massMail) {
		return WebUtil.getMassMailProgressLabel(massMail, trialMassMailModel.getMassMailProgress(massMail));
	}

	public int getMassMailProgressValue(MassMailOutVO massMail) {
		return WebUtil.getMassMailProgressValue(massMail, trialMassMailModel.getMassMailProgress(massMail));
	}

	public String stratificationRandomizationListSelectionSetValuesToString(StratificationRandomizationListOutVO randomizationList) {
		if (randomizationList != null) {
			return (new ProbandListEntryTagValueOutVOStringAdapter(
					Settings.getInt(SettingCodes.STRATIFICATION_RANDOMIZATION_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
							DefaultSettings.STRATIFICATION_RANDOMIZATION_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH)))
									.selectionSetValuesToString(randomizationList.getSelectionSetValues(), true);
		}
		return null;
	}

	public ShiftDurationSummaryModel getShiftDurationModel() {
		return shiftDurationModel;
	}

	public TrialCourseLazyModel getTrialCourseModel() {
		return trialCourseModel;
	}

	public TrialMassMailLazyModel getTrialMassMailModel() {
		return trialMassMailModel;
	}

	public RandomizationListCodeLazyModel getRandomizationListCodeModel() {
		return randomizationListCodeModel;
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

	public void handleTrialMassMailsChange() {
		trialMassMailModel.setTrialId(trialId);
		trialMassMailModel.updateRowCount();
	}

	public void handleRandomizationListCodesChange() {
		randomizationListCodeModel.setTrialId(trialId);
		randomizationListCodeModel.updateRowCount();
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
		trialMassMailModel.setTrialId(trialId);
		trialMassMailModel.updateRowCount();
		randomizationListCodeModel.setTrialId(trialId);
		randomizationListCodeModel.updateRowCount();
		shiftDurationModel.reset(now, ShiftDurationSummaryType.TRIAL, null);
		bookingDurationModel.reset(now, BookingDurationSummaryType.TRIAL, null);
		enrollmentChartBean.changeRootEntity(null);
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
		DataTable.clearFilters("trialcourses_list");
	}

	public void refreshTrialMassMails() {
		trialMassMailModel.updateRowCount();
		DataTable.clearFilters("trialmassmails_list");
	}

	public void refreshRandomizationListCodes() {
		randomizationListCodeModel.updateRowCount();
		DataTable.clearFilters("randomizationlistcodes_list");
	}
}
