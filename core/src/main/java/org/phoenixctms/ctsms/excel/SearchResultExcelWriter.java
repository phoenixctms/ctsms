package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

public class SearchResultExcelWriter extends WorkbookWriter {

	public static String getInquiryColumnName(InquiryOutVO inquiry) {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.INQUIRY_HEAD, ExcelUtil.DEFAULT_LABEL, inquiry == null ? null : inquiry.getUniqueName(),
				inquiry == null ? null : CommonUtil.inputFieldOutVOToString(inquiry.getField()));
	}

	public static String getInquiryDateColumnName(InquiryOutVO inquiry) {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.INQUIRY_DATE_HEAD, ExcelUtil.DEFAULT_LABEL,
				inquiry == null ? null : inquiry.getUniqueName(), inquiry == null ? null : CommonUtil.inputFieldOutVOToString(inquiry.getField()));
	}

	public static String getStreetsColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getProvincesColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.PROVINCES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getZipCodesColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getCityNamesColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getPhoneContactDetailsColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.PHONE_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEmailContactDetailsColumnName() {
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.EMAIL_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	protected CriteriaInstantVO criteria;
	protected DBModule module;
	protected SearchResultExcelVO excelVO;
	protected static final String SEARCH_RESULT_EXCEL_FILENAME_PREFIX = "search_result_";

	protected SearchResultExcelWriter() {
		super();
	}

	public SearchResultExcelWriter(DBModule module, boolean omitFields) {
		super();
		this.module = module;
		excelVO = new SearchResultExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
		// further spreadsheets...
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		if (module != null) {
			switch (module) {
				case INVENTORY_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.INVENTORY_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INVENTORY_SCALE_FACTOR);
					break;
				case STAFF_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.STAFF_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.STAFF_SCALE_FACTOR);
					break;
				case COURSE_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.COURSE_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.COURSE_SCALE_FACTOR);
					break;
				case TRIAL_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.TRIAL_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.TRIAL_SCALE_FACTOR);
					break;
				case INPUT_FIELD_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.INPUT_FIELD_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INPUT_FIELD_SCALE_FACTOR);
					break;
				case PROBAND_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.PROBAND_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.PROBAND_SCALE_FACTOR);
					break;
				case USER_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.USER_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.USER_SCALE_FACTOR);
					break;
				case MASS_MAIL_DB:
					scaleFactor = Settings.getIntNullable(SearchResultExcelSettingCodes.MASS_MAIL_SCALE_FACTOR, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.MASS_MAIL_SCALE_FACTOR);
					break;
				default:
			}
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	protected SpreadSheetWriter createSpreadSheetWriter(boolean omitFields) {
		if (module != null) {
			switch (module) {
				case INVENTORY_DB:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.INVENTORY_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.INVENTORY_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.INVENTORY_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.INVENTORY_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.INVENTORY_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INVENTORY_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.INVENTORY_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INVENTORY_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.INVENTORY_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INVENTORY_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.INVENTORY_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INVENTORY_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.INVENTORY_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INVENTORY_ROW_FORMAT));
				case STAFF_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.STAFF_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.STAFF_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.STAFF_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.STAFF_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.STAFF_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.STAFF_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.STAFF_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.STAFF_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.STAFF_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.STAFF_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.STAFF_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.STAFF_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.STAFF_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.STAFF_ROW_FORMAT));
				case COURSE_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.COURSE_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.COURSE_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.COURSE_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.COURSE_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.COURSE_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.COURSE_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.COURSE_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.COURSE_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.COURSE_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.COURSE_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.COURSE_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.COURSE_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.COURSE_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.COURSE_ROW_FORMAT));
				case TRIAL_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.TRIAL_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.TRIAL_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.TRIAL_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.TRIAL_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.TRIAL_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.TRIAL_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.TRIAL_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.TRIAL_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.TRIAL_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.TRIAL_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.TRIAL_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.TRIAL_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.TRIAL_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.TRIAL_ROW_FORMAT));
				case INPUT_FIELD_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.INPUT_FIELD_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.INPUT_FIELD_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.INPUT_FIELD_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.INPUT_FIELD_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.INPUT_FIELD_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.INPUT_FIELD_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.INPUT_FIELD_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.INPUT_FIELD_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.INPUT_FIELD_ROW_FORMAT));
				case PROBAND_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.PROBAND_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.PROBAND_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.PROBAND_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.PROBAND_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.PROBAND_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.PROBAND_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.PROBAND_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.PROBAND_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.PROBAND_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.PROBAND_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.PROBAND_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.PROBAND_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.PROBAND_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.PROBAND_ROW_FORMAT));
				case USER_DB:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.USER_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.USER_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.USER_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.USER_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.USER_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.USER_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.USER_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.USER_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.USER_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.USER_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.USER_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.USER_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.USER_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.USER_ROW_FORMAT));
				case MASS_MAIL_DB:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getSearchResultExcelColumns(Locales.USER, SearchResultExcelLabelCodes.MASS_MAIL_VO_FIELD_COLUMNS,
									SearchResultExcelDefaultSettings.MASS_MAIL_VO_FIELD_COLUMNS)),
							Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(SearchResultExcelSettingCodes.MASS_MAIL_AUTOSIZE, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_AUTOSIZE),
							Settings.getBoolean(SearchResultExcelSettingCodes.MASS_MAIL_WRITEHEAD, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_WRITEHEAD),
							Settings.getIntNullable(SearchResultExcelSettingCodes.MASS_MAIL_PAGE_BREAK_AT_ROW, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(SearchResultExcelSettingCodes.MASS_MAIL_ROW_COLORS, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_ROW_COLORS),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.MASS_MAIL_HEAD_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_HEAD_FORMAT),
							Settings.getExcelCellFormat(SearchResultExcelSettingCodes.MASS_MAIL_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
									SearchResultExcelDefaultSettings.MASS_MAIL_ROW_FORMAT));
				default:
			}
		}
		return new SpreadSheetWriter(this,
				getColumnIndexMap(new ArrayList<String>()),
				Settings.getInt(SearchResultExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
				omitFields,
				false,
				true,
				null,
				true,
				ExcelCellFormat.getDefaultHeadFormat(),
				ExcelCellFormat.getDefaultRowFormat());
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		StringBuilder key = new StringBuilder(module.toString());
		key.append(ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR);
		key.append(l10nKey);
		return L10nUtil.getSearchResultExcelLabel(Locales.USER, key.toString(), ExcelUtil.DEFAULT_LABEL);
	}

	public CriteriaInstantVO getCriteria() {
		return criteria;
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public SearchResultExcelVO getExcelVO() {
		return excelVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		if (module != null) {
			switch (module) {
				case INVENTORY_DB:
					return Settings.getString(SearchResultExcelSettingCodes.INVENTORY_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INVENTORY_TEMPLATE_FILE_NAME);
				case STAFF_DB:
					return Settings.getString(SearchResultExcelSettingCodes.STAFF_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.STAFF_TEMPLATE_FILE_NAME);
				case COURSE_DB:
					return Settings.getString(SearchResultExcelSettingCodes.COURSE_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.COURSE_TEMPLATE_FILE_NAME);
				case TRIAL_DB:
					return Settings.getString(SearchResultExcelSettingCodes.TRIAL_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.TRIAL_TEMPLATE_FILE_NAME);
				case PROBAND_DB:
					return Settings.getString(SearchResultExcelSettingCodes.PROBAND_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.PROBAND_TEMPLATE_FILE_NAME);
				case USER_DB:
					return Settings.getString(SearchResultExcelSettingCodes.USER_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.USER_TEMPLATE_FILE_NAME);
				case INPUT_FIELD_DB:
					return Settings.getString(SearchResultExcelSettingCodes.INPUT_FIELD_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INPUT_FIELD_TEMPLATE_FILE_NAME);
				case MASS_MAIL_DB:
					return Settings.getString(SearchResultExcelSettingCodes.MASS_MAIL_TEMPLATE_FILE_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.MASS_MAIL_TEMPLATE_FILE_NAME);
				default:
			}
		}
		return null;
	}

	public String getTimelineEventFieldValue(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			Date start = timelineEvent.getStart();
			Date stop = timelineEvent.getStop();
			if (start != null && stop != null) {
				return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.TIMELINE_EVENT_FROM_TO_FIELD_VALUE, ExcelUtil.DEFAULT_LABEL,
						timelineEvent.getTitle(),
						CommonUtil.formatDate(start, ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)),
						CommonUtil.formatDate(stop, ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)));
			} else if (start != null) {
				return L10nUtil.getSearchResultExcelLabel(Locales.USER, SearchResultExcelLabelCodes.TIMELINE_EVENT_FROM_FIELD_VALUE, ExcelUtil.DEFAULT_LABEL,
						timelineEvent.getTitle(), CommonUtil.formatDate(start, ExcelUtil.EXCEL_DATE_PATTERN, L10nUtil.getLocale(Locales.USER)));
			} else {
				return timelineEvent.getTitle(); // obsolete
			}
		}
		return "";
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

	public void setCriteria(CriteriaInstantVO criteria) {
		this.criteria = criteria;
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
		String templateSpreadSheetName = null;
		if (module != null) {
			switch (module) {
				case INVENTORY_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.INVENTORY_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INVENTORY_SPREADSHEET_NAME);
					break;
				case STAFF_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.STAFF_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.STAFF_SPREADSHEET_NAME);
					break;
				case COURSE_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.COURSE_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.COURSE_SPREADSHEET_NAME);
					break;
				case TRIAL_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.TRIAL_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.TRIAL_SPREADSHEET_NAME);
					break;
				case PROBAND_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.PROBAND_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.PROBAND_SPREADSHEET_NAME);
					break;
				case USER_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.USER_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.USER_SPREADSHEET_NAME);
					break;
				case INPUT_FIELD_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.INPUT_FIELD_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.INPUT_FIELD_SPREADSHEET_NAME);
					break;
				case MASS_MAIL_DB:
					templateSpreadSheetName = Settings.getString(SearchResultExcelSettingCodes.MASS_MAIL_SPREADSHEET_NAME, Bundle.SEARCH_RESULT_EXCEL,
							SearchResultExcelDefaultSettings.MASS_MAIL_SPREADSHEET_NAME);
					break;
				default:
			}
		}
		if (CommonUtil.isEmptyString(templateSpreadSheetName)) {
			getSpreadSheetWriters().get(0).setSpreadSheetName(spreadSheetName);
		} else {
			getSpreadSheetWriters().get(0).setSpreadSheetName(templateSpreadSheetName);
		}
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setModule(module);
		excelVO.setCriteria(criteria);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName;
		if (module != null) {
			fileName = new StringBuilder(module.value());
			fileName.append("_");
		} else {
			fileName = new StringBuilder(SEARCH_RESULT_EXCEL_FILENAME_PREFIX);
		}
		if (criteria != null && criteria.getCriterions().size() == 1) {
			fileName.append(criteria.getCriterions().iterator().next().getPropertyId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.EXCEL_FILENAME_EXTENSION);
		excelVO.setFileName(fileName.toString());
	}

	@Override
	public Color voToColor(Object vo) {
		return null;
	}
}
