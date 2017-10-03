package org.phoenixctms.ctsms.excel;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class ProbandListExcelWriter extends WorkbookWriter {

	public static String getCityNamesColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEmailContactDetailsColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.EMAIL_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusLogColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.ENROLLMENT_STATUS_LOG_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusLogValue(ProbandListStatusEntryOutVO statusEntry) {
		if (CommonUtil.isEmptyString(statusEntry.getReason())) {
			return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.ENROLLMENT_STATUS_LOG_VALUE_NO_REASON, ExcelUtil.DEFAULT_LABEL,
					CommonUtil.formatDate(statusEntry.getRealTimestamp(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)),
					getIdentityName(statusEntry.getModifiedUser()), statusEntry.getStatus().getName());
		} else {
			return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.ENROLLMENT_STATUS_LOG_VALUE, ExcelUtil.DEFAULT_LABEL,
					CommonUtil.formatDate(statusEntry.getRealTimestamp(), ExcelUtil.EXCEL_DATE_TIME_PATTERN, L10nUtil.getLocale(Locales.USER)),
					getIdentityName(statusEntry.getModifiedUser()), statusEntry.getStatus().getName(), statusEntry.getReason());
		}
	}

	public static String getICDateColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.IC_DATE_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	private static String getIdentityName(UserOutVO user) {
		StaffOutVO identity = user.getIdentity();
		if (identity != null) {
			return identity.getName();
		} else {
			return user.getName();
		}
	}

	public static String getPhoneContactDetailsColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PHONE_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getScreeningDateColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.SCREENING_DATE_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getScreeningReasonColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.SCREENING_REASON_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getStreetsColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getZipCodesColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	private ProbandListExcelVO excelVO;
	private ProbandListStatusLogLevel logLevel;
	private TrialOutVO trial;
	private static final String PROBAND_LIST_EXCEL_FILENAME_PREFIX = "proband_list_";

	public static String getICAgeColumnName() {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.IC_AGE_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getInquiryColumnName(InquiryOutVO inquiry) {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.INQUIRY_HEAD, ExcelUtil.DEFAULT_LABEL, inquiry == null ? null : inquiry.getUniqueName(),
				inquiry == null ? null : CommonUtil.inputFieldOutVOToString(inquiry.getField()));
	}

	public static String getInquiryDateColumnName(InquiryOutVO inquiry) {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.INQUIRY_DATE_HEAD, ExcelUtil.DEFAULT_LABEL,
				inquiry == null ? null : inquiry.getUniqueName(), inquiry == null ? null : CommonUtil.inputFieldOutVOToString(inquiry.getField()));
	}

	public static String getProbandListEntryTagColumnName(ProbandListEntryTagOutVO probandListEntryTag) {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PROBAND_LIST_ENTRY_TAG_HEAD, ExcelUtil.DEFAULT_LABEL, probandListEntryTag == null ? null
				: probandListEntryTag.getUniqueName(), probandListEntryTag == null ? null : CommonUtil.inputFieldOutVOToString(probandListEntryTag.getField()));
	}

	public static String getProbandListEntryTagDateColumnName(ProbandListEntryTagOutVO probandListEntryTag) {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PROBAND_LIST_ENTRY_TAG_DATE_HEAD, ExcelUtil.DEFAULT_LABEL,
				probandListEntryTag == null ? null : probandListEntryTag.getUniqueName(),
						probandListEntryTag == null ? null : CommonUtil.inputFieldOutVOToString(probandListEntryTag.getField()));
	}

	public ProbandListExcelWriter(ProbandListStatusLogLevel logLevel, boolean omitFields) {
		super();
		this.logLevel = logLevel;
		excelVO = new ProbandListExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
	}

	private void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		header.getLeft().clear();
		temp = CommonUtil.trialOutVOToString(trial);
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft().append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.TRIAL_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getCentre().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getCentre().append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getRight().clear();
		temp = getTemplateFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getRight()
			.append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.TEMPLATE_FILENAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
					(new File(temp)).getName()));
		}
		footer.getLeft().clear();
		footer.getLeft().append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.VERSION_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
			// (new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN)).format()));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		if (logLevel != null) {
			switch (logLevel) {
				case ENROLLMENT:
					scaleFactor = Settings.getIntNullable(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SCALE_FACTOR, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SCALE_FACTOR);
					if (Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_APPEND_HEADER_FOOTER, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				case PRE_SCREENING:
					scaleFactor = Settings.getIntNullable(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SCALE_FACTOR, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SCALE_FACTOR);
					if (Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_APPEND_HEADER_FOOTER, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				case SCREENING:
					scaleFactor = Settings.getIntNullable(ProbandListExcelSettingCodes.SCREENING_LOG_SCALE_FACTOR, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SCALE_FACTOR);
					if (Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_APPEND_HEADER_FOOTER, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				case SICL:
					scaleFactor = Settings.getIntNullable(ProbandListExcelSettingCodes.SICL_SCALE_FACTOR, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SCALE_FACTOR);
					if (Settings.getBoolean(ProbandListExcelSettingCodes.SICL_APPEND_HEADER_FOOTER, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				default:
			}
		} else {
			scaleFactor = Settings.getIntNullable(ProbandListExcelSettingCodes.PROBAND_LIST_SCALE_FACTOR, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SCALE_FACTOR);
			if (Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_APPEND_HEADER_FOOTER, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_APPEND_HEADER_FOOTER)) {
				appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
			}
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	private SpreadSheetWriter createSpreadSheetWriter(boolean omitFields) {
		if (logLevel != null) {
			switch (logLevel) {
				case ENROLLMENT:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getProbandListExcelColumns(Locales.USER, ProbandListExcelLabelCodes.ENROLLMENT_LOG_VO_FIELD_COLUMNS,
									ProbandListExcelDefaultSettings.ENROLLMENT_LOG_VO_FIELD_COLUMNS)),
									Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL,
											ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
											omitFields,
											Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_AUTOSIZE, Bundle.PROBAND_LIST_EXCEL,
													ProbandListExcelDefaultSettings.ENROLLMENT_LOG_AUTOSIZE),
													Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_WRITEHEAD, Bundle.PROBAND_LIST_EXCEL,
															ProbandListExcelDefaultSettings.ENROLLMENT_LOG_WRITEHEAD),
															Settings.getIntNullable(ProbandListExcelSettingCodes.ENROLLMENT_LOG_PAGE_BREAK_AT_ROW, Bundle.PROBAND_LIST_EXCEL,
																	ProbandListExcelDefaultSettings.ENROLLMENT_LOG_PAGE_BREAK_AT_ROW),
																	Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_ROW_COLORS, Bundle.PROBAND_LIST_EXCEL,
																			ProbandListExcelDefaultSettings.ENROLLMENT_LOG_ROW_COLORS),
																			Settings.getExcelCellFormat(ProbandListExcelSettingCodes.ENROLLMENT_LOG_HEAD_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																					ProbandListExcelDefaultSettings.ENROLLMENT_LOG_HEAD_FORMAT),
																					Settings.getExcelCellFormat(ProbandListExcelSettingCodes.ENROLLMENT_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_ROW_FORMAT));
				case PRE_SCREENING:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getProbandListExcelColumns(Locales.USER, ProbandListExcelLabelCodes.PRE_SCREENING_LOG_VO_FIELD_COLUMNS,
									ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_VO_FIELD_COLUMNS)),
									Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL,
											ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
											omitFields,
											Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_AUTOSIZE, Bundle.PROBAND_LIST_EXCEL,
													ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_AUTOSIZE),
													Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_WRITEHEAD, Bundle.PROBAND_LIST_EXCEL,
															ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_WRITEHEAD),
															Settings.getIntNullable(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_PAGE_BREAK_AT_ROW, Bundle.PROBAND_LIST_EXCEL,
																	ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_PAGE_BREAK_AT_ROW),
																	Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_ROW_COLORS, Bundle.PROBAND_LIST_EXCEL,
																			ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_ROW_COLORS),
																			Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_HEAD_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																					ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_HEAD_FORMAT),
																					Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_ROW_FORMAT));
				case SCREENING:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getProbandListExcelColumns(Locales.USER, ProbandListExcelLabelCodes.SCREENING_LOG_VO_FIELD_COLUMNS,
									ProbandListExcelDefaultSettings.SCREENING_LOG_VO_FIELD_COLUMNS)),
									Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL,
											ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
											omitFields,
											Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_AUTOSIZE, Bundle.PROBAND_LIST_EXCEL,
													ProbandListExcelDefaultSettings.SCREENING_LOG_AUTOSIZE),
													Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_WRITEHEAD, Bundle.PROBAND_LIST_EXCEL,
															ProbandListExcelDefaultSettings.SCREENING_LOG_WRITEHEAD),
															Settings.getIntNullable(ProbandListExcelSettingCodes.SCREENING_LOG_PAGE_BREAK_AT_ROW, Bundle.PROBAND_LIST_EXCEL,
																	ProbandListExcelDefaultSettings.SCREENING_LOG_PAGE_BREAK_AT_ROW),
																	Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_ROW_COLORS, Bundle.PROBAND_LIST_EXCEL,
																			ProbandListExcelDefaultSettings.SCREENING_LOG_ROW_COLORS),
																			Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SCREENING_LOG_HEAD_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																					ProbandListExcelDefaultSettings.SCREENING_LOG_HEAD_FORMAT),
																					Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SCREENING_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
																							ProbandListExcelDefaultSettings.SCREENING_LOG_ROW_FORMAT));
				case SICL:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getProbandListExcelColumns(Locales.USER, ProbandListExcelLabelCodes.SICL_VO_FIELD_COLUMNS,
									ProbandListExcelDefaultSettings.SICL_VO_FIELD_COLUMNS)),
									Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL,
											ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
											omitFields,
											Settings.getBoolean(ProbandListExcelSettingCodes.SICL_AUTOSIZE, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_AUTOSIZE),
											Settings.getBoolean(ProbandListExcelSettingCodes.SICL_WRITEHEAD, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_WRITEHEAD),
											Settings.getIntNullable(ProbandListExcelSettingCodes.SICL_PAGE_BREAK_AT_ROW, Bundle.PROBAND_LIST_EXCEL,
													ProbandListExcelDefaultSettings.SICL_PAGE_BREAK_AT_ROW),
													Settings.getBoolean(ProbandListExcelSettingCodes.SICL_ROW_COLORS, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_ROW_COLORS),
													Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SICL_HEAD_FORMAT, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_HEAD_FORMAT),
													Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SICL_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_ROW_FORMAT));
				default:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(new ArrayList<String>()),
							Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL,
									ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									false,
									true,
									null,
									true,
									ExcelCellFormat.getDefaultHeadFormat(),
									ExcelCellFormat.getDefaultRowFormat());
			}
		} else {
			return new SpreadSheetWriter(this,
					getColumnIndexMap(L10nUtil.getProbandListExcelColumns(Locales.USER, ProbandListExcelLabelCodes.PROBAND_LIST_VO_FIELD_COLUMNS,
							ProbandListExcelDefaultSettings.PROBAND_LIST_VO_FIELD_COLUMNS)),
							Settings.getInt(ProbandListExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_AUTOSIZE, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PROBAND_LIST_AUTOSIZE),
							Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_WRITEHEAD, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PROBAND_LIST_WRITEHEAD),
							Settings.getIntNullable(ProbandListExcelSettingCodes.PROBAND_LIST_PAGE_BREAK_AT_ROW, Bundle.PROBAND_LIST_EXCEL,
									ProbandListExcelDefaultSettings.PROBAND_LIST_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_ROW_COLORS, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PROBAND_LIST_ROW_COLORS),
									Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PROBAND_LIST_HEAD_FORMAT, Bundle.PROBAND_LIST_EXCEL,
											ProbandListExcelDefaultSettings.PROBAND_LIST_HEAD_FORMAT),
											Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PROBAND_LIST_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
													ProbandListExcelDefaultSettings.PROBAND_LIST_ROW_FORMAT));
		}
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getProbandListExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public ProbandListExcelVO getExcelVO() {
		return excelVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		if (logLevel != null) {
			switch (logLevel) {
				case ENROLLMENT:
					return Settings.getString(ProbandListExcelSettingCodes.ENROLLMENT_LOG_TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_TEMPLATE_FILE_NAME);
				case PRE_SCREENING:
					return Settings.getString(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_TEMPLATE_FILE_NAME);
				case SCREENING:
					return Settings.getString(ProbandListExcelSettingCodes.SCREENING_LOG_TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_TEMPLATE_FILE_NAME);
				case SICL:
					return Settings.getString(ProbandListExcelSettingCodes.SICL_TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_TEMPLATE_FILE_NAME);
				default:
					return null;
			}
		} else {
			return Settings.getString(ProbandListExcelSettingCodes.PROBAND_LIST_TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_TEMPLATE_FILE_NAME);
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

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		if (CommonUtil.isEmptyString(spreadSheetName)) {
			String templateSpreadSheetName = null;
			if (logLevel != null && trial != null) {
				switch (logLevel) {
					case ENROLLMENT:
						templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.ENROLLMENT_LOG_SPREADSHEET_NAME,
								ExcelUtil.DEFAULT_LABEL, trial.getId(), trial.getName(), trial.getTitle());
						break;
					case PRE_SCREENING:
						templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PRE_SCREENING_LOG_SPREADSHEET_NAME,
								ExcelUtil.DEFAULT_LABEL, trial.getId(), trial.getName(), trial.getTitle());
						break;
					case SCREENING:
						templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.SCREENING_LOG_SPREADSHEET_NAME,
								ExcelUtil.DEFAULT_LABEL, trial.getId(), trial.getName(), trial.getTitle());
						break;
					case SICL:
						templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.SICL_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
								trial.getId(), trial.getName(), trial.getTitle());
						break;
					default:
				}
			} else {
				if (trial != null) {
					templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PROBAND_LIST_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							trial.getId(), trial.getName(), trial.getTitle());
				} else {
					templateSpreadSheetName = L10nUtil.getProbandListExcelLabel(Locales.USER, ProbandListExcelLabelCodes.PROBAND_LIST_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL);
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
		excelVO.setLogLevel(logLevel);
		excelVO.setTrial(trial);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName;
		if (logLevel != null) {
			fileName = new StringBuilder(logLevel.value());
			fileName.append("_");
		} else {
			fileName = new StringBuilder(PROBAND_LIST_EXCEL_FILENAME_PREFIX);
		}
		if (trial != null) {
			fileName.append(trial.getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.EXCEL_FILENAME_EXTENSION);
		excelVO.setFileName(fileName.toString());
	}

	@Override
	public Color voToColor(Object vo) {
		if (vo instanceof ProbandListEntryOutVO) {
			ProbandListStatusEntryOutVO lastStatus = ((ProbandListEntryOutVO) vo).getLastStatus();
			if (lastStatus != null) {
				return lastStatus.getStatus().getColor();
			}
		}
		return null;
	}
}
