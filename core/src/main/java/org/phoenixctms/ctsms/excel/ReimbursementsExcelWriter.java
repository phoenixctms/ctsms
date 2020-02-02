package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

public class ReimbursementsExcelWriter extends WorkbookWriter {

	public static String getCityNamesColumnName() {
		return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	private ReimbursementsExcelVO excelVO;
	private String costType;
	private PaymentMethodVO method;
	private Boolean paid;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_PREFIX = "reimbursements_";
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_TRIAL = "trial_";
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_PROBAND = "proband_";
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_EMPTY_COSTTYPE = "no_asset";
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_PAID = "booked";
	private static final String REIMBURSEMENTS_EXCEL_FILENAME_NOT_PAID = "open";

	public static String getStreetsColumnName() {
		return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getZipCodesColumnName() {
		return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	protected ReimbursementsExcelWriter() {
		super();
	}

	public ReimbursementsExcelWriter(boolean omitFields, PaymentMethod method) {
		super();
		this.method = L10nUtil.createPaymentMethodVO(Locales.USER, method);
		excelVO = new ReimbursementsExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
	}

	private void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		String temp1;
		String temp2;
		header.getLeft().clear();
		temp = getPaidMethodString();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAID_METHOD_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		header.getCentre().clear();
		temp = costType;
		if (temp != null) {
			if (temp.length() > 0) {
				header.getCentre().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.COST_TYPE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
			} else {
				header.getCentre().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.EMPTY_COST_TYPE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
			}
		}
		header.getRight().clear();
		temp1 = CommonUtil.trialOutVOToString(trial);
		temp2 = CommonUtil.probandOutVOToString(proband);
		if (!CommonUtil.isEmptyString(temp1) && !CommonUtil.isEmptyString(temp2)) {
			header.getRight().append(
					L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.TRIAL_PROBAND_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp1, temp2));
		} else if (!CommonUtil.isEmptyString(temp1)) {
			header.getRight().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.TRIAL_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp1));
		} else if (!CommonUtil.isEmptyString(temp2)) {
			header.getRight().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PROBAND_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp2));
		}
		footer.getLeft().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getLeft().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		if (method != null) {
			switch (method.getPaymentMethod()) {
				case PETTY_CASH:
					scaleFactor = Settings.getIntNullable(ReimbursementsExcelSettingCodes.PETTY_CASH_SCALE_FACTOR, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.PETTY_CASH_SCALE_FACTOR);
					if (Settings.getBoolean(ReimbursementsExcelSettingCodes.PETTY_CASH_APPEND_HEADER_FOOTER, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.PETTY_CASH_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				case VOUCHER:
					scaleFactor = Settings.getIntNullable(ReimbursementsExcelSettingCodes.VOUCHER_SCALE_FACTOR, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.VOUCHER_SCALE_FACTOR);
					if (Settings.getBoolean(ReimbursementsExcelSettingCodes.VOUCHER_APPEND_HEADER_FOOTER, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.VOUCHER_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				case WIRE_TRANSFER:
					scaleFactor = Settings.getIntNullable(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_SCALE_FACTOR, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_SCALE_FACTOR);
					if (Settings.getBoolean(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_APPEND_HEADER_FOOTER, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_APPEND_HEADER_FOOTER)) {
						appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
					}
					break;
				default:
			}
		} else {
			scaleFactor = Settings.getIntNullable(ReimbursementsExcelSettingCodes.SCALE_FACTOR, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.SCALE_FACTOR);
			if (Settings.getBoolean(ReimbursementsExcelSettingCodes.APPEND_HEADER_FOOTER, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.APPEND_HEADER_FOOTER)) {
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
		if (method != null) {
			switch (method.getPaymentMethod()) {
				case PETTY_CASH:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getReimbursementsExcelColumns(Locales.USER, ReimbursementsExcelLabelCodes.PETTY_CASH_VO_FIELD_COLUMNS,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_VO_FIELD_COLUMNS)),
							Settings.getInt(ReimbursementsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(ReimbursementsExcelSettingCodes.PETTY_CASH_AUTOSIZE, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_AUTOSIZE),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.PETTY_CASH_WRITEHEAD, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_WRITEHEAD),
							Settings.getIntNullable(ReimbursementsExcelSettingCodes.PETTY_CASH_PAGE_BREAK_AT_ROW, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.PETTY_CASH_ROW_COLORS, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_ROW_COLORS),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.PETTY_CASH_HEAD_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_HEAD_FORMAT),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.PETTY_CASH_ROW_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.PETTY_CASH_ROW_FORMAT));
				case VOUCHER:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getReimbursementsExcelColumns(Locales.USER, ReimbursementsExcelLabelCodes.VOUCHER_VO_FIELD_COLUMNS,
									ReimbursementsExcelDefaultSettings.VOUCHER_VO_FIELD_COLUMNS)),
							Settings.getInt(ReimbursementsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(ReimbursementsExcelSettingCodes.VOUCHER_AUTOSIZE, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.VOUCHER_AUTOSIZE),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.VOUCHER_WRITEHEAD, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VOUCHER_WRITEHEAD),
							Settings.getIntNullable(ReimbursementsExcelSettingCodes.VOUCHER_PAGE_BREAK_AT_ROW, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VOUCHER_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.VOUCHER_ROW_COLORS, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VOUCHER_ROW_COLORS),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.VOUCHER_HEAD_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VOUCHER_HEAD_FORMAT),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.VOUCHER_ROW_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VOUCHER_ROW_FORMAT));
				case WIRE_TRANSFER:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getReimbursementsExcelColumns(Locales.USER, ReimbursementsExcelLabelCodes.WIRE_TRANSFER_VO_FIELD_COLUMNS,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_VO_FIELD_COLUMNS)),
							Settings.getInt(ReimbursementsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_AUTOSIZE, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_AUTOSIZE),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_WRITEHEAD, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_WRITEHEAD),
							Settings.getIntNullable(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_PAGE_BREAK_AT_ROW, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_ROW_COLORS, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_ROW_COLORS),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_HEAD_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_HEAD_FORMAT),
							Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_ROW_FORMAT, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_ROW_FORMAT));
				default:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(new ArrayList<String>()),
							Settings.getInt(ReimbursementsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.REIMBURSEMENTS_EXCEL,
									ReimbursementsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
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
					getColumnIndexMap(L10nUtil.getReimbursementsExcelColumns(Locales.USER, ReimbursementsExcelLabelCodes.VO_FIELD_COLUMNS,
							ReimbursementsExcelDefaultSettings.VO_FIELD_COLUMNS)),
					Settings.getInt(ReimbursementsExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
					omitFields,
					Settings.getBoolean(ReimbursementsExcelSettingCodes.AUTOSIZE, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.AUTOSIZE),
					Settings.getBoolean(ReimbursementsExcelSettingCodes.WRITEHEAD, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.WRITEHEAD),
					Settings.getIntNullable(ReimbursementsExcelSettingCodes.PAGE_BREAK_AT_ROW, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.PAGE_BREAK_AT_ROW),
					Settings.getBoolean(ReimbursementsExcelSettingCodes.ROW_COLORS, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.ROW_COLORS),
					Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.HEAD_FORMAT, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.HEAD_FORMAT),
					Settings.getExcelCellFormat(ReimbursementsExcelSettingCodes.ROW_FORMAT, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.ROW_FORMAT));
		}
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getReimbursementsExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public String getCostType() {
		return costType;
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public ReimbursementsExcelVO getExcelVO() {
		return excelVO;
	}

	public Boolean getPaid() {
		return paid;
	}

	private String getPaidMethodString() {
		if (method == null) {
			if (paid == null) {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAID_AND_NOT_PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
			} else if (paid) {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
			} else {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.NOT_PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
			}
		} else {
			if (paid == null) {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAID_AND_NOT_PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
			} else if (paid) {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
			} else {
				return L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.NOT_PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
			}
		}
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		if (method != null) {
			switch (method.getPaymentMethod()) {
				case PETTY_CASH:
					return Settings.getString(ReimbursementsExcelSettingCodes.PETTY_CASH_TEMPLATE_FILE_NAME, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.PETTY_CASH_TEMPLATE_FILE_NAME);
				case VOUCHER:
					return Settings.getString(ReimbursementsExcelSettingCodes.VOUCHER_TEMPLATE_FILE_NAME, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.VOUCHER_TEMPLATE_FILE_NAME);
				case WIRE_TRANSFER:
					return Settings.getString(ReimbursementsExcelSettingCodes.WIRE_TRANSFER_TEMPLATE_FILE_NAME, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.WIRE_TRANSFER_TEMPLATE_FILE_NAME);
				default:
					return null;
			}
		} else {
			return Settings.getString(ReimbursementsExcelSettingCodes.TEMPLATE_FILE_NAME, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.TEMPLATE_FILE_NAME);
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

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
		setSpreadSheetName(null);
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
		setSpreadSheetName(null);
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		if (CommonUtil.isEmptyString(spreadSheetName)) {
			String templateSpreadSheetName = null;
			if (trial != null && proband != null) {
				templateSpreadSheetName = L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.TRIAL_PROBAND_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
						getPaidMethodString(), trial.getId(), trial.getName(), proband.getId(), proband.getName());
			} else if (trial != null) {
				templateSpreadSheetName = L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.TRIAL_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
						getPaidMethodString(), trial.getId(), trial.getName());
			} else if (proband != null) {
				templateSpreadSheetName = L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.PROBAND_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
						getPaidMethodString(), proband.getId(), proband.getName());
			} else {
				templateSpreadSheetName = L10nUtil.getReimbursementsExcelLabel(Locales.USER, ReimbursementsExcelLabelCodes.SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
						getPaidMethodString());
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
		excelVO.setCostType(costType);
		excelVO.setMethod(method);
		excelVO.setPaid(paid);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName = new StringBuilder(REIMBURSEMENTS_EXCEL_FILENAME_PREFIX);
		if (trial != null) {
			fileName.append(REIMBURSEMENTS_EXCEL_FILENAME_TRIAL);
			fileName.append(trial.getId());
			fileName.append("_");
		}
		if (proband != null) {
			fileName.append(REIMBURSEMENTS_EXCEL_FILENAME_PROBAND);
			fileName.append(proband.getId());
			fileName.append("_");
		}
		if (costType != null) {
			if (costType.length() > 0) {
				fileName.append(CommonUtil.getSafeFilename(costType, "_"));
			} else {
				fileName.append(REIMBURSEMENTS_EXCEL_FILENAME_EMPTY_COSTTYPE);
			}
			fileName.append("_");
		}
		if (method != null) {
			fileName.append(method.getPaymentMethod().value());
			fileName.append("_");
		}
		if (paid != null) {
			if (paid) {
				fileName.append(REIMBURSEMENTS_EXCEL_FILENAME_PAID);
			} else {
				fileName.append(REIMBURSEMENTS_EXCEL_FILENAME_NOT_PAID);
			}
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
