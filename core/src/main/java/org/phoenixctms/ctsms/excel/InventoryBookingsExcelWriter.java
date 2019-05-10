package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.InventoryBookingsExcelVO;

public class InventoryBookingsExcelWriter extends WorkbookWriter {

	// public static String getCityNamesColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	//
	// public static String getCvAddressBlockColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.CV_ADDRESS_BLOCK_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	private InventoryBookingsExcelVO excelVO;
	private DepartmentVO probandDepartment;
	private DepartmentVO courseDepartment;
	private DepartmentVO trialDepartment;
	private String calendar;
	private Date from;
	private Date to;
	// private TrialOutVO trial;
	private static final String INVENTORY_BOOKINGS_EXCEL_FILENAME_PREFIX = "inventory_bookings_";

	// public static String getEmailContactDetailsColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.EMAIL_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	//
	// public static String getPhoneContactDetailsColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.PHONE_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	//
	// public static String getStreetsColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	//
	// public static String getZipCodesColumnName() {
	// return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	public InventoryBookingsExcelWriter(boolean omitFields) {
		super();
		excelVO = new InventoryBookingsExcelVO();
		getSpreadSheetWriters()
				.add(new SpreadSheetWriter(
						this,
						getColumnIndexMap(L10nUtil.getInventoryBookingsExcelColumns(Locales.USER, InventoryBookingsExcelLabelCodes.VO_FIELD_COLUMNS,
								InventoryBookingsExcelDefaultSettings.VO_FIELD_COLUMNS)),
						Settings.getInt(InventoryBookingsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.INVENTORY_BOOKINGS_EXCEL,
								InventoryBookingsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(InventoryBookingsExcelSettingCodes.AUTOSIZE, Bundle.INVENTORY_BOOKINGS_EXCEL, InventoryBookingsExcelDefaultSettings.AUTOSIZE),
						Settings.getBoolean(InventoryBookingsExcelSettingCodes.WRITEHEAD, Bundle.INVENTORY_BOOKINGS_EXCEL, InventoryBookingsExcelDefaultSettings.WRITEHEAD),
						Settings.getIntNullable(InventoryBookingsExcelSettingCodes.PAGE_BREAK_AT_ROW, Bundle.INVENTORY_BOOKINGS_EXCEL,
								InventoryBookingsExcelDefaultSettings.PAGE_BREAK_AT_ROW),
						Settings.getBoolean(InventoryBookingsExcelSettingCodes.ROW_COLORS, Bundle.INVENTORY_BOOKINGS_EXCEL, InventoryBookingsExcelDefaultSettings.ROW_COLORS),
						Settings.getExcelCellFormat(InventoryBookingsExcelSettingCodes.HEAD_FORMAT, Bundle.INVENTORY_BOOKINGS_EXCEL,
								InventoryBookingsExcelDefaultSettings.HEAD_FORMAT),
						Settings.getExcelCellFormat(InventoryBookingsExcelSettingCodes.ROW_FORMAT, Bundle.INVENTORY_BOOKINGS_EXCEL,
								InventoryBookingsExcelDefaultSettings.ROW_FORMAT)));
	}

	private void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		header.getLeft().clear();
		temp = getDepartmentsHeader();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft()
					.append(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.DEPARTMENTS_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
							temp));
		}
		header.getCentre().clear();
		temp = calendar;
		if (!CommonUtil.isEmptyString(temp)) {
			header.getCentre().append(
					L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.CALENDAR_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getRight().clear();
		temp = CommonUtil.getDateStartStopString(from, to, new SimpleDateFormat(ExcelUtil.EXCEL_DATE_TIME_PATTERN));
		if (!CommonUtil.isEmptyString(temp)) {
			header.getRight()
					.append(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.FROM_TO_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getLeft().clear();
		// footer.getLeft().append(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.VERSION_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getLeft().append(
					L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
			// (new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN)).format(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now)));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = Settings.getIntNullable(InventoryBookingsExcelSettingCodes.SCALE_FACTOR, Bundle.INVENTORY_BOOKINGS_EXCEL,
				InventoryBookingsExcelDefaultSettings.SCALE_FACTOR);
		if (Settings.getBoolean(InventoryBookingsExcelSettingCodes.APPEND_HEADER_FOOTER, Bundle.INVENTORY_BOOKINGS_EXCEL,
				InventoryBookingsExcelDefaultSettings.APPEND_HEADER_FOOTER)) {
			appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	public String getCalendar() {
		return calendar;
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public DepartmentVO getCourseDepartment() {
		return courseDepartment;
	}

	private String getDepartmentsHeader() {
		StringBuilder sb = new StringBuilder();
		if (probandDepartment != null) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(probandDepartment.getName());
		}
		if (courseDepartment != null) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(courseDepartment.getName());
		}
		if (trialDepartment != null) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(trialDepartment.getName());
		}
		return sb.toString();
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public InventoryBookingsExcelVO getExcelVO() {
		return excelVO;
	}

	public Date getFrom() {
		return from;
	}

	public DepartmentVO getProbandDepartment() {
		return probandDepartment;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		return Settings.getString(InventoryBookingsExcelSettingCodes.TEMPLATE_FILE_NAME, Bundle.INVENTORY_BOOKINGS_EXCEL, InventoryBookingsExcelDefaultSettings.TEMPLATE_FILE_NAME);
	}

	public Date getTo() {
		return to;
	}

	public DepartmentVO getTrialDepartment() {
		return trialDepartment;
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

	public void setCalendar(String calendar) {
		this.calendar = calendar;
		if (!CommonUtil.isEmptyString(calendar)) {
			setSpreadSheetName(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
					calendar));
		} else {
			setSpreadSheetName(L10nUtil.getInventoryBookingsExcelLabel(Locales.USER, InventoryBookingsExcelLabelCodes.SPREADSHEET_NAME_ALL_CALENDARS, ExcelUtil.DEFAULT_LABEL));
		}
	}

	public void setCourseDepartment(DepartmentVO courseDepartment) {
		this.courseDepartment = courseDepartment;
	}

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	public void setExcelVO(InventoryBookingsExcelVO excelVO) {
		this.excelVO = excelVO;
	}

	// public TrialOutVO getTrial() {
	// return trial;
	// }
	public void setFrom(Date from) {
		this.from = from;
	}

	public void setProbandDepartment(DepartmentVO probandDepartment) {
		this.probandDepartment = probandDepartment;
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		String templateSpreadSheetName = Settings.getString(InventoryBookingsExcelSettingCodes.SPREADSHEET_NAME, Bundle.INVENTORY_BOOKINGS_EXCEL,
				InventoryBookingsExcelDefaultSettings.SPREADSHEET_NAME);
		if (CommonUtil.isEmptyString(templateSpreadSheetName)) {
			getSpreadSheetWriters().get(0).setSpreadSheetName(spreadSheetName);
		} else {
			getSpreadSheetWriters().get(0).setSpreadSheetName(templateSpreadSheetName);
		}
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public void setTrialDepartment(DepartmentVO trialDepartment) {
		this.trialDepartment = trialDepartment;
	}

	// public void setTrial(TrialOutVO trial) {
	// this.trial = trial;
	// setSpreadSheetName(CommonUtil.trialOutVOToString(trial));
	// }
	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		// excelVO.setTrial(trial);
		excelVO.setProbandDepartment(probandDepartment);
		excelVO.setCourseDepartment(courseDepartment);
		excelVO.setTrialDepartment(trialDepartment);
		excelVO.setCalendar(calendar);
		excelVO.setFrom(from);
		excelVO.setTo(to);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName = new StringBuilder(INVENTORY_BOOKINGS_EXCEL_FILENAME_PREFIX);
		if (probandDepartment != null) {
			fileName.append(probandDepartment.getId());
			fileName.append("_");
		}
		if (courseDepartment != null) {
			fileName.append(courseDepartment.getId());
			fileName.append("_");
		}
		if (trialDepartment != null) {
			fileName.append(trialDepartment.getId());
			fileName.append("_");
		}
		if (!CommonUtil.isEmptyString(calendar)) {
			fileName.append(CommonUtil.getSafeFilename(calendar, "_"));
			fileName.append("_");
		}
		// if (from != null) {
		// fileName.append(CommonUtil.formatDate(from, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		// fileName.append("_");
		// }
		// if (to != null) {
		// fileName.append(CommonUtil.formatDate(to, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		// fileName.append("_");
		// }
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