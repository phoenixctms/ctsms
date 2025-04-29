package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

public class VisitScheduleExcelWriter extends WorkbookWriter {

	public enum Styles {
		TRIAL_VISIT_SCHEDULE, PROBAND_VISIT_SCHEDULE, PROBAND_TRIAL_VISIT_SCHEDULE, TRAVEL_EXPENSES_VISIT_SCHEDULE, PROBAND_APPOINTMENT_SCHEDULE, VISIT_PLAN
	}

	public static String getAliquotVisitReimbursementColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ALIQUOT_VISIT_REIMBURSEMENT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusReasonColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_REASON_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusTimestampColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_TIMESTAMP_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusTypeIsCountColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_TYPE_IS_COUNT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getFirstVisitReimbursementColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.FIRST_VISIT_REIMBURSEMENT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	protected VisitScheduleExcelVO excelVO;
	protected TrialOutVO trial;
	protected ProbandOutVO proband;
	protected ProbandAddressOutVO address;
	protected ProbandGroupOutVO group;
	protected DepartmentVO trialDepartment;
	protected Date from;
	protected Date to;
	protected Styles style;
	protected static final String VISIT_SCHEDULE_EXCEL_FILENAME_TRIAL = "trial_";
	protected static final String VISIT_SCHEDULE_EXCEL_FILENAME_PROBAND = "proband_";
	public static final String RECENT_PROBAND_LIST_STATUS_ENTRY = "_RECENT_PROBAND_LIST_STATUS_ENTRY";

	protected VisitScheduleExcelWriter() {
		super();
	}

	public VisitScheduleExcelWriter(boolean omitFields, Styles style) {
		super();
		this.style = style;
		excelVO = new VisitScheduleExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
	}

	protected void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		//header.getLeft().clear();
		temp = CommonUtil.trialOutVOToString(trial);
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		} else {
			temp = getDepartmentsHeader();
			if (!CommonUtil.isEmptyString(temp)) {
				header.getLeft()
						.append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.DEPARTMENTS_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
								temp));
			} else {
				header.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.ALL_TRIALS_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
			}
		}
		//header.getCentre().clear();
		temp = CommonUtil.probandOutVOToString(proband);
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil
							.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case PROBAND_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
							temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case VISIT_PLAN:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL);
				}
				break;
			default:
		}
		if (!CommonUtil.isEmptyString(temp)) {
			header.getCentre().append(temp);
		}
		//header.getRight().clear();
		temp = (from == null && to == null) ? "" : CommonUtil.getDateStartStopString(from, to, new SimpleDateFormat(ExcelUtil.EXCEL_DATE_TIME_PATTERN));
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			case PROBAND_VISIT_SCHEDULE:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			case VISIT_PLAN:
				temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_ADDRESS_GROUP_FROM_TO_HEADER_FOOTER,
						ExcelUtil.DEFAULT_LABEL, address == null ? "" : address.getName(), group == null ? "" : group.getTitle(), temp);
				break;
			default:
		}
		if (!CommonUtil.isEmptyString(temp)) {
			header.getRight().append(temp);
		}
		//footer.getLeft().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		//footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		//footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
		}
	}

	protected String getDepartmentsHeader() {
		StringBuilder sb = new StringBuilder();
		if (trialDepartment != null) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(trialDepartment.getName());
		}
		return sb.toString();
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case PROBAND_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case VISIT_PLAN:
				scaleFactor = Settings.getIntNullable(VisitScheduleExcelSettingCodes.VISIT_PLAN_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.VISIT_PLAN_SCALE_FACTOR);
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.VISIT_PLAN_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.VISIT_PLAN_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			default:
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	@Override
	protected int getRowIncrement(Object lastRow, Object row) {
		int increment = 1;
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			case PROBAND_VISIT_SCHEDULE:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			case VISIT_PLAN:
				if (Settings.getBoolean(VisitScheduleExcelSettingCodes.VISIT_PLAN_GROUP_VISITS, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.VISIT_PLAN_GROUP_VISITS)) {
					increment = 2;
				}
				break;
			default:
				break;
		}
		if (lastRow != null) {
			VisitOutVO visit = null;
			VisitOutVO lastVisit = null;
			if (lastRow instanceof VisitScheduleItemOutVO) {
				lastVisit = ((VisitScheduleItemOutVO) lastRow).getVisit();
			} else if (lastRow instanceof VisitScheduleAppointmentVO) {
				lastVisit = ((VisitScheduleAppointmentVO) lastRow).getVisit();
			}
			if (row instanceof VisitScheduleItemOutVO) {
				visit = ((VisitScheduleItemOutVO) row).getVisit();
			} else if (row instanceof VisitScheduleAppointmentVO) {
				visit = ((VisitScheduleAppointmentVO) row).getVisit();
			}
			boolean visitEqual;
			if (visit != null && lastVisit != null) {
				String prefix = CommonUtil.longestCommonPrefix(visit.getToken(), lastVisit.getToken()).replaceFirst("^[^0-9]+", "");
				if (prefix.length() > 0) {
					String suffix = visit.getToken().replaceFirst(prefix, "");
					visitEqual = suffix.length() == 0 || Pattern.compile("^[^0-9]+$").matcher(suffix).find();
				} else {
					visitEqual = false;
				}
				//visitEqual = visit.getToken().replaceFirst(prefix, "").replaceFirst("^[0-9]+", "").length() > 0;
			} else if (visit == null && lastVisit == null) {
				visitEqual = true;
			} else {
				visitEqual = false;
			}
			if (visitEqual) {
				return 1;
			} else {
				return increment;
			}
		} else if (row == null) {
			return increment;
		} else {
			return 0;
		}
	}

	public static String getVisitScheduleAppointmentValue(VisitScheduleItemOutVO visitScheduleItem) {
		if (visitScheduleItem != null) {
			Boolean isDay = DateCalc.isSingleDay(visitScheduleItem.getStart(), visitScheduleItem.getStop());
			if (isDay != null && isDay) {
				return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
			} else {
				if (visitScheduleItem.getStart() != null && visitScheduleItem.getStop() != null) {
					if (DateCalc.isStartOfDay(visitScheduleItem.getStart()) &&
							(DateCalc.isEndOfDay(visitScheduleItem.getStop()) || DateCalc.isStartOfDay(visitScheduleItem.getStop()))) {
						return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_SCHEDULE_APPOINTMENT_START_STOP_VALUE, ExcelUtil.DEFAULT_LABEL,
								CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)),
								CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)));
					} else {
						return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_SCHEDULE_APPOINTMENT_START_STOP_VALUE, ExcelUtil.DEFAULT_LABEL,
								CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)),
								CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)));
					}
				} else if (visitScheduleItem.getStart() != null) {
					if (DateCalc.isStartOfDay(visitScheduleItem.getStart())) {
						return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
					} else {
						return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER));
					}
				} else if (visitScheduleItem.getStop() != null) {
					if (DateCalc.isEndOfDay(visitScheduleItem.getStop()) || DateCalc.isStartOfDay(visitScheduleItem.getStop())) {
						return CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
					} else {
						return CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER));
					}
				}
			}
		}
		return null;
	}

	public static String getVisitScheduleAppointmentValue(VisitScheduleAppointmentVO visitScheduleItem) {
		if (visitScheduleItem != null) {
			Boolean isDay = DateCalc.isSingleDay(visitScheduleItem.getStart(), visitScheduleItem.getStop());
			if (isDay != null && isDay) {
				return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
			} else {
				if (visitScheduleItem.getStart() != null && visitScheduleItem.getStop() != null) {
					if (DateCalc.isStartOfDay(visitScheduleItem.getStart()) &&
							(DateCalc.isEndOfDay(visitScheduleItem.getStop()) || DateCalc.isStartOfDay(visitScheduleItem.getStop()))) {
						return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_SCHEDULE_APPOINTMENT_START_STOP_VALUE, ExcelUtil.DEFAULT_LABEL,
								CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)),
								CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)));
					} else {
						return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_SCHEDULE_APPOINTMENT_START_STOP_VALUE, ExcelUtil.DEFAULT_LABEL,
								CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)),
								CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)));
					}
				} else if (visitScheduleItem.getStart() != null) {
					if (DateCalc.isStartOfDay(visitScheduleItem.getStart())) {
						return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
					} else {
						return CommonUtil.formatDate(visitScheduleItem.getStart(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER));
					}
				} else if (visitScheduleItem.getStop() != null) {
					if (DateCalc.isEndOfDay(visitScheduleItem.getStop()) || DateCalc.isStartOfDay(visitScheduleItem.getStop())) {
						return CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER));
					} else {
						return CommonUtil.formatDate(visitScheduleItem.getStop(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER));
					}
				}
			}
		}
		return null;
	}

	public static String getVisitScheduleAppointmentsStartStopColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_SCHEDULE_APPOINTMENT_START_STOP_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	protected SpreadSheetWriter createSpreadSheetWriter(boolean omitFields) {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_FORMAT));
			case PROBAND_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_FORMAT));
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_FORMAT));
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_FORMAT));
			case PROBAND_APPOINTMENT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_ROW_FORMAT));
			case VISIT_PLAN:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_VO_FIELD_COLUMNS,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_VO_FIELD_COLUMNS)),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(VisitScheduleExcelSettingCodes.VISIT_PLAN_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_AUTOSIZE),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.VISIT_PLAN_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_WRITEHEAD),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.VISIT_PLAN_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_PAGE_BREAK_AT_ROW),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.VISIT_PLAN_ROW_OFFSET_FIRST_PAGE, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_ROW_OFFSET_FIRST_PAGE),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.VISIT_PLAN_ROW_OFFSET_OTHER_PAGES, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_ROW_OFFSET_OTHER_PAGES),
						Settings.getIntNullable(VisitScheduleExcelSettingCodes.VISIT_PLAN_COL_OFFSET, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_COL_OFFSET),
						Settings.getBoolean(VisitScheduleExcelSettingCodes.VISIT_PLAN_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_ROW_COLORS),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.VISIT_PLAN_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_HEAD_FORMAT),
						Settings.getExcelCellFormat(VisitScheduleExcelSettingCodes.VISIT_PLAN_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VISIT_PLAN_ROW_FORMAT));
			default:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(new ArrayList<String>()),
						Settings.getInt(VisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								VisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						false,
						true,
						null,
						null,
						null,
						null,
						true,
						ExcelCellFormat.getDefaultHeadFormat(),
						ExcelCellFormat.getDefaultRowFormat());
		}
	}

	public ProbandAddressOutVO getAddress() {
		return address;
	}

	public DepartmentVO getTrialDepartment() {
		return trialDepartment;
	}

	public void setTrialDepartment(DepartmentVO trialDepartment) {
		this.trialDepartment = trialDepartment;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public VisitScheduleExcelVO getExcelVO() {
		return excelVO;
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	protected String getStyleName() {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case PROBAND_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case PROBAND_APPOINTMENT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case VISIT_PLAN:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_NAME, ExcelUtil.DEFAULT_LABEL);
			default:
		}
		return "";
	}

	@Override
	public String getTemplateFileName() throws Exception {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return Settings.getString(VisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case PROBAND_VISIT_SCHEDULE:
				return Settings.getString(VisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return Settings.getString(VisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return Settings.getString(VisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case PROBAND_APPOINTMENT_SCHEDULE:
				return Settings.getString(VisitScheduleExcelSettingCodes.PROBAND_APPOINTMENT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.PROBAND_APPOINTMENT_SCHEDULE_TEMPLATE_FILE_NAME);
			case VISIT_PLAN:
				return Settings.getString(VisitScheduleExcelSettingCodes.VISIT_PLAN_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						VisitScheduleExcelDefaultSettings.VISIT_PLAN_TEMPLATE_FILE_NAME);
			default:
				return null;
		}
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public Collection getVOs() {
		return getSpreadSheetWriters().get(0).getVOs();
	}

	@Override
	public boolean save() throws Exception {
		byte[] documentData = buffer.toByteArray();
		excelVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		excelVO.setSize(documentData.length);
		excelVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAddress(ProbandAddressOutVO address) {
		this.address = address;
	}

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
		setSpreadSheetName(null);
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		if (CommonUtil.isEmptyString(spreadSheetName)) {
			String templateSpreadSheetName = null;
			switch (style) {
				case TRIAL_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case PROBAND_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case PROBAND_TRIAL_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case TRAVEL_EXPENSES_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case PROBAND_APPOINTMENT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_APPOINTMENT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case VISIT_PLAN:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.VISIT_PLAN_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				default:
			}
			String templateFileName = null;
			try {
				templateFileName = getTemplateFileName();
			} catch (Exception e) {
			}
			if (CommonUtil.isEmptyString(templateSpreadSheetName) && CommonUtil.isEmptyString(templateFileName)) {
				if (trial != null && proband != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_PROBAND_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL, getStyleName(), trial.getId(), trial.getName(), proband.getId(), proband.getName());
				} else if (trial != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.TRIAL_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName(), trial.getId(), trial.getName());
				} else if (proband != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.PROBAND_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName(), proband.getId(), proband.getName());
				} else {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, VisitScheduleExcelLabelCodes.SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName());
				}
			}
			getSpreadSheetWriters().get(0).setSpreadSheetName(templateSpreadSheetName);
		} else {
			getSpreadSheetWriters().get(0).setSpreadSheetName(spreadSheetName);
		}
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
		setSpreadSheetName(null);
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setTrial(trial);
		excelVO.setProband(proband);
		excelVO.setTrialDepartment(trialDepartment);
		excelVO.setFrom(from);
		excelVO.setTo(to);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName = new StringBuilder(style.toString());
		fileName.append("_");
		if (trial != null) {
			fileName.append(VISIT_SCHEDULE_EXCEL_FILENAME_TRIAL);
			fileName.append(trial.getId());
			fileName.append("_");
		}
		if (proband != null) {
			fileName.append(VISIT_SCHEDULE_EXCEL_FILENAME_PROBAND);
			fileName.append(proband.getId());
			fileName.append("_");
		}
		if (trialDepartment != null) {
			fileName.append(trialDepartment.getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.EXCEL_FILENAME_EXTENSION);
		excelVO.setFileName(fileName.toString());
	}

	@Override
	public Color voToColor(Object vo) {
		if (vo instanceof VisitScheduleItemOutVO) {
			VisitScheduleItemOutVO visitScheduleItem = (VisitScheduleItemOutVO) vo;
			Object distinctVo = getDistinctFieldRows().get(visitScheduleItem.getId()).get(RECENT_PROBAND_LIST_STATUS_ENTRY);
			ProbandListStatusEntryOutVO statusEntry = null;
			if (distinctVo instanceof ProbandListStatusEntryOutVO) {
				statusEntry = (ProbandListStatusEntryOutVO) distinctVo;
			}
			VisitOutVO visit = visitScheduleItem.getVisit();
			switch (style) {
				case VISIT_PLAN:
					return visit != null ? visit.getType().getColor() : null;
				default:
					if (statusEntry != null) {
						if (statusEntry.getStatus().isCount()) {
							return Settings.getColor(VisitScheduleExcelSettingCodes.ENROLLMENT_STATUS_IS_COUNT_COLOR, Bundle.VISIT_SCHEDULE_EXCEL,
									VisitScheduleExcelDefaultSettings.ENROLLMENT_STATUS_IS_COUNT_COLOR);
						} else {
							return Settings.getColor(VisitScheduleExcelSettingCodes.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR, Bundle.VISIT_SCHEDULE_EXCEL,
									VisitScheduleExcelDefaultSettings.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR);
						}
					}
					break;
			}
		}
		return null;
	}

	public ProbandGroupOutVO getGroup() {
		return group;
	}

	public void setGroup(ProbandGroupOutVO group) {
		this.group = group;
	}
}
