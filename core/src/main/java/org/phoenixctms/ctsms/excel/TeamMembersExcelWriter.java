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
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.TeamMembersExcelVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class TeamMembersExcelWriter extends WorkbookWriter {

	public static String getCityNamesColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getCvAddressBlockColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.CV_ADDRESS_BLOCK_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	private TeamMembersExcelVO excelVO;
	private TrialOutVO trial;
	private static final String TEAM_MEMBERS_EXCEL_FILENAME_PREFIX = "team_members_";

	public static String getEmailContactDetailsColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.EMAIL_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getPhoneContactDetailsColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.PHONE_CONTACT_DETAILS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getStreetsColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getZipCodesColumnName() {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public TeamMembersExcelWriter(boolean omitFields) {
		super();
		excelVO = new TeamMembersExcelVO();
		getSpreadSheetWriters()
				.add(new SpreadSheetWriter(
						this,
						getColumnIndexMap(L10nUtil.getTeamMembersExcelColumns(Locales.USER, TeamMembersExcelLabelCodes.VO_FIELD_COLUMNS,
								TeamMembersExcelDefaultSettings.VO_FIELD_COLUMNS)),
						Settings.getInt(TeamMembersExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(TeamMembersExcelSettingCodes.AUTOSIZE, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.AUTOSIZE),
						Settings.getBoolean(TeamMembersExcelSettingCodes.WRITEHEAD, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.WRITEHEAD),
						Settings.getIntNullable(TeamMembersExcelSettingCodes.PAGE_BREAK_AT_ROW, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.PAGE_BREAK_AT_ROW),
						Settings.getBoolean(TeamMembersExcelSettingCodes.ROW_COLORS, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.ROW_COLORS),
						Settings.getExcelCellFormat(TeamMembersExcelSettingCodes.HEAD_FORMAT, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.HEAD_FORMAT),
						Settings.getExcelCellFormat(TeamMembersExcelSettingCodes.ROW_FORMAT, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.ROW_FORMAT)));
	}

	private void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		header.getLeft().clear();
		temp = CommonUtil.trialOutVOToString(trial);
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft().append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.TRIAL_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getCentre().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getCentre().append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getRight().clear();
		temp = getTemplateFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getRight()
					.append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.TEMPLATE_FILENAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
							(new File(temp)).getName()));
		}
		footer.getLeft().clear();
		footer.getLeft().append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.VERSION_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getTeamMembersExcelLabel(Locales.USER, TeamMembersExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
			// (new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN)).format(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now)));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = Settings.getIntNullable(TeamMembersExcelSettingCodes.SCALE_FACTOR, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.SCALE_FACTOR);
		if (Settings.getBoolean(TeamMembersExcelSettingCodes.APPEND_HEADER_FOOTER, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.APPEND_HEADER_FOOTER)) {
			appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getTeamMembersExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public TeamMembersExcelVO getExcelVO() {
		return excelVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		return Settings.getString(TeamMembersExcelSettingCodes.TEMPLATE_FILE_NAME, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.TEMPLATE_FILE_NAME);
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
		String templateSpreadSheetName = Settings.getString(TeamMembersExcelSettingCodes.SPREADSHEET_NAME, Bundle.TEAM_MEMBERS_EXCEL,
				TeamMembersExcelDefaultSettings.SPREADSHEET_NAME);
		if (CommonUtil.isEmptyString(templateSpreadSheetName)) {
			getSpreadSheetWriters().get(0).setSpreadSheetName(spreadSheetName);
		} else {
			getSpreadSheetWriters().get(0).setSpreadSheetName(templateSpreadSheetName);
		}
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
		setSpreadSheetName(CommonUtil.trialOutVOToString(trial));
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setTrial(trial);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName = new StringBuilder(TEAM_MEMBERS_EXCEL_FILENAME_PREFIX);
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
		return null;
	}
}