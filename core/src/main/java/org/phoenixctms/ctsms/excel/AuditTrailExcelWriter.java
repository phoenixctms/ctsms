package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.phoenixctms.ctsms.adapt.InputFieldValueStringAdapterBase;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

public class AuditTrailExcelWriter extends WorkbookWriter {

	private static final String AUDIT_TRAIL_EXCEL_FILENAME_PREFIX = "audit_trail_";
	private static final String AUDIT_TRAIL_EXCEL_FILENAME_TRIAL = "trial_";
	private static final String AUDIT_TRAIL_EXCEL_FILENAME_PROBAND = "proband_";
	private static final String AUDIT_TRAIL_EXCEL_FILENAME_ECRF = "ecrf_";
	private final static InputFieldValueStringAdapterBase INPUT_FIELD_VALUE_ADAPTER = new InputFieldValueStringAdapterBase<ECRFFieldValueOutVO>() {

		@Override
		protected boolean getBooleanValue(ECRFFieldValueOutVO value) {
			return value.getBooleanValue();
		}

		@Override
		protected String getCheckboxString(boolean value) {
			return L10nUtil.getAuditTrailExcelLabel(Locales.USER, value ? AuditTrailExcelLabelCodes.CHECKBOX_CHECKED : AuditTrailExcelLabelCodes.CHECKBOX_UNCHECKED,
					ExcelUtil.DEFAULT_LABEL);
		}

		@Override
		protected DateFormat getDateFormat(boolean isUserTimeZone) {
			return new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN);
		}

		@Override
		protected DateFormat getDateTimeFormat(boolean isUserTimeZone) {
			return new SimpleDateFormat(ExcelUtil.EXCEL_DATE_TIME_PATTERN);
		}

		@Override
		protected Date getDateValue(ECRFFieldValueOutVO value) {
			return value.getDateValue();
		}

		@Override
		protected String getDecimalSeparator() {
			return ExcelUtil.EXCEL_DECIMAL_SEPARATOR;
		}

		@Override
		protected Float getFloatValue(ECRFFieldValueOutVO value) {
			return value.getFloatValue();
		}

		@Override
		protected InputFieldOutVO getInputField(ECRFFieldValueOutVO value) {
			return value != null ? value.getEcrfField().getField() : null;
		}

		@Override
		protected Long getLongValue(ECRFFieldValueOutVO value) {
			return value.getLongValue();
		}

		@Override
		protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(ECRFFieldValueOutVO value) {
			return value.getSelectionValues();
		}

		@Override
		protected String getSelectionSetValuesSeparator() {
			return ExcelUtil.EXCEL_LINE_BREAK;
		}

		@Override
		protected Integer getTextClipMaxLength() {
			return null;
		}

		@Override
		protected String getTextValue(ECRFFieldValueOutVO value) {
			return value.getTextValue();
		}

		@Override
		protected DateFormat getTimeFormat(boolean isUserTimeZone) {
			return new SimpleDateFormat(ExcelUtil.EXCEL_TIME_PATTERN);
		}

		@Override
		protected Date getTimestampValue(ECRFFieldValueOutVO value) {
			return value.getTimestampValue();
		}

		@Override
		protected Date getTimeValue(ECRFFieldValueOutVO value) {
			return value.getTimeValue();
		}
	};

	private static String getEcrfFieldValueColumnName() {
		return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.ECRF_FIELD_VALUE_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	// public static String getCityNamesColumnName() {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.CITY_NAMES_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	private AuditTrailExcelVO excelVO;
	// private String costType;
	// private PaymentMethodVO method;
	// private Boolean paid;
	private TrialOutVO trial;
	private ProbandListEntryOutVO listEntry;
	private ECRFOutVO ecrf;
	// private HashMap<Integer,ECRFFieldStatusQueue> sheetIndexQueueMap;
	private LinkedHashMap<ECRFFieldStatusQueue, Integer> queueSheetIndexMap;

	// public static String getStreetsColumnName() {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.STREETS_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	// public static String getZipCodesColumnName() {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.ZIP_CODES_HEAD, ExcelUtil.DEFAULT_LABEL);
	// }
	public AuditTrailExcelWriter(boolean omitFields, ECRFFieldStatusQueue... queues) {
		super();
		// this.method = L10nUtil.createPaymentMethodVO(Locales.USER, method);
		excelVO = new AuditTrailExcelVO();
		getSpreadSheetWriters().add(createAuditTrailSpreadSheetWriter(omitFields));
		queueSheetIndexMap = new LinkedHashMap<ECRFFieldStatusQueue, Integer>(queues.length);
		for (int i = 0; i < queues.length; i++) {
			getSpreadSheetWriters().add(createEcrfFieldStatusSpreadSheetWriter(omitFields));
			queueSheetIndexMap.put(queues[i], i + 1);
		}
	}

	private void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		header.getLeft().clear();
		// temp = CommonUtil.trialOutVOToString(trial);
		// if (!CommonUtil.isEmptyString(temp)) {
		if (trial != null) {
			header.getLeft().append(
					L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.TRIAL_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, CommonUtil.trialOutVOToString(trial)));
		} else {
			if (listEntry != null) {
				// temp = CommonUtil.trialOutVOToString(listEntry.getTrial());
				header.getLeft().append(
						L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.TRIAL_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
								CommonUtil.trialOutVOToString(listEntry.getTrial())));
			}
		}
		header.getCentre().clear();
		if (listEntry != null) {
			header.getCentre()
					.append(L10nUtil
							.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PROBAND_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
									Long.toString(listEntry.getProband().getId())));
		}
		header.getRight().clear();
		if (ecrf != null) {
			header.getRight().append(L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.ECRF_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, ecrf.getName()));
		}
		footer.getLeft().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getLeft().append(L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
			// (new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN)).format(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now)));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor;
		switch (sheetIndex) {
			case 0:
				scaleFactor = Settings.getIntNullable(AuditTrailExcelSettingCodes.AUDIT_TRAIL_SCALE_FACTOR, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_SCALE_FACTOR);
				if (Settings.getBoolean(AuditTrailExcelSettingCodes.AUDIT_TRAIL_APPEND_HEADER_FOOTER, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			default:
				scaleFactor = Settings.getIntNullable(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_SCALE_FACTOR, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_SCALE_FACTOR);
				if (Settings.getBoolean(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_APPEND_HEADER_FOOTER, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	private SpreadSheetWriter createAuditTrailSpreadSheetWriter(boolean omitFields) {
		return new SpreadSheetWriter(this,
				getColumnIndexMap(L10nUtil.getAuditTrailExcelColumns(Locales.USER, AuditTrailExcelLabelCodes.AUDIT_TRAIL_VO_FIELD_COLUMNS,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_VO_FIELD_COLUMNS)),
				Settings.getInt(AuditTrailExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
				omitFields,
				Settings.getBoolean(AuditTrailExcelSettingCodes.AUDIT_TRAIL_AUTOSIZE, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_AUTOSIZE),
				Settings.getBoolean(AuditTrailExcelSettingCodes.AUDIT_TRAIL_WRITEHEAD, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_WRITEHEAD),
				Settings.getIntNullable(AuditTrailExcelSettingCodes.AUDIT_TRAIL_PAGE_BREAK_AT_ROW, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_PAGE_BREAK_AT_ROW),
				Settings.getBoolean(AuditTrailExcelSettingCodes.AUDIT_TRAIL_ROW_COLORS, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_ROW_COLORS),
				Settings.getExcelCellFormat(AuditTrailExcelSettingCodes.AUDIT_TRAIL_HEAD_FORMAT, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_HEAD_FORMAT),
				Settings.getExcelCellFormat(AuditTrailExcelSettingCodes.AUDIT_TRAIL_ROW_FORMAT, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.AUDIT_TRAIL_ROW_FORMAT));
	}

	private SpreadSheetWriter createEcrfFieldStatusSpreadSheetWriter(boolean omitFields) {
		return new SpreadSheetWriter(this,
				getColumnIndexMap(L10nUtil.getAuditTrailExcelColumns(Locales.USER, AuditTrailExcelLabelCodes.ECRF_FIELD_STATUS_VO_FIELD_COLUMNS,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_VO_FIELD_COLUMNS)),
				Settings.getInt(AuditTrailExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
				omitFields,
				Settings.getBoolean(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_AUTOSIZE, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_AUTOSIZE),
				Settings.getBoolean(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_WRITEHEAD, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_WRITEHEAD),
				Settings.getIntNullable(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_PAGE_BREAK_AT_ROW, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_PAGE_BREAK_AT_ROW),
				Settings.getBoolean(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_ROW_COLORS, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_ROW_COLORS),
				Settings.getExcelCellFormat(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_HEAD_FORMAT, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_HEAD_FORMAT),
				Settings.getExcelCellFormat(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_ROW_FORMAT, Bundle.AUDIT_TRAIL_EXCEL,
						AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_ROW_FORMAT));
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getAuditTrailExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public ECRFOutVO getEcrf() {
		return ecrf;
	}

	// public String getCostType() {
	// return costType;
	// }
	// public ArrayList<String> getDistinctColumnNames() {
	// return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	// }
	//
	// public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
	// return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	// }
	public AuditTrailExcelVO getExcelVO() {
		return excelVO;
	}
	// public void setCostType(String costType) {
	// this.costType = costType;
	// }
	// public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
	// getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	// }
	//
	// public void setDistinctFieldRows(
	// HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
	// getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	// }
	// public void setPaid(Boolean paid) {
	// this.paid = paid;
	// setSpreadSheetName(null);
	// }
	// public void setProband(ProbandOutVO proband) {
	// this.proband = proband;
	// setSpreadSheetName(null);
	// }

	public ProbandListEntryOutVO getListEntry() {
		return listEntry;
	}

	// public Boolean getPaid() {
	// return paid;
	// }
	// private String getPaidMethodString() {
	// if (method == null) {
	// if (paid == null) {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAID_AND_NOT_PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
	// } else if (paid) {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
	// } else {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.NOT_PAID_LABEL, ExcelUtil.DEFAULT_LABEL);
	// }
	// } else {
	// if (paid == null) {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAID_AND_NOT_PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
	// } else if (paid) {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
	// } else {
	// return L10nUtil.getAuditTrailExcelLabel(Locales.USER, AuditTrailExcelLabelCodes.NOT_PAID_METHOD_LABEL, ExcelUtil.DEFAULT_LABEL, method.getName());
	// }
	// }
	// }
	// public ProbandOutVO getProband() {
	// return proband;
	// }
	@Override
	public String getTemplateFileName() throws Exception {
		return Settings.getString(AuditTrailExcelSettingCodes.TEMPLATE_FILE_NAME, Bundle.AUDIT_TRAIL_EXCEL, AuditTrailExcelDefaultSettings.TEMPLATE_FILE_NAME);
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public Collection getVOs() {
		return getSpreadSheetWriters().get(0).getVOs();
	}

	// public TrialOutVO getTrial() {
	// return trial;
	// }
	public Collection getVOs(ECRFFieldStatusQueue queue) {
		return getSpreadSheetWriters().get(queueSheetIndexMap.get(queue)).getVOs();
	}

	@Override
	public boolean save() throws Exception {
		byte[] documentData = buffer.toByteArray();
		excelVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		excelVO.setSize(documentData.length);
		excelVO.setDocumentDatas(documentData);
		return true;
	}

	public void setEcrf(ECRFOutVO ecrf) {
		this.ecrf = ecrf;
		setSpreadSheetName(ecrf != null ? ecrf.getName() : null);
	}

	public void setListEntry(ProbandListEntryOutVO listEntry) {
		this.listEntry = listEntry;
		setSpreadSheetName(listEntry != null ? Long.toString(listEntry.getProband().getId()) : null);
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		String labelCode = AuditTrailExcelLabelCodes.AUDIT_TRAIL_SPREADSHEET_NAME;
		getSpreadSheetWriters().get(0).setSpreadSheetName(
				L10nUtil.getAuditTrailExcelLabel(Locales.USER, labelCode, ExcelUtil.DEFAULT_LABEL, spreadSheetName));
		Iterator<Entry<ECRFFieldStatusQueue, Integer>> it = queueSheetIndexMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ECRFFieldStatusQueue, Integer> queueSheetIndex = it.next();
			switch (queueSheetIndex.getKey()) {
				case VALIDATION:
					labelCode = AuditTrailExcelLabelCodes.VALIDATION_ECRF_STATUS_SPREADSHEET_NAME;
					break;
				case QUERY:
					labelCode = AuditTrailExcelLabelCodes.QUERY_ECRF_STATUS_SPREADSHEET_NAME;
					break;
				case ANNOTATION:
					labelCode = AuditTrailExcelLabelCodes.ANNOTATION_ECRF_STATUS_SPREADSHEET_NAME;
					break;
				default:
					continue;
			}
			getSpreadSheetWriters().get(queueSheetIndex.getValue()).setSpreadSheetName(
					L10nUtil.getAuditTrailExcelLabel(Locales.USER, labelCode, ExcelUtil.DEFAULT_LABEL, spreadSheetName));
		}
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
		setSpreadSheetName(CommonUtil.trialOutVOToString(trial));
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
		ArrayList<String> distinctColumnNames = new ArrayList<String>();
		String fieldKey = getEcrfFieldValueColumnName();
		distinctColumnNames.add(fieldKey);
		HashMap<Long, HashMap<String, Object>> distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(VOs.size());
		Iterator<ECRFFieldValueOutVO> fieldValuesIt = VOs.iterator();
		while (fieldValuesIt.hasNext()) {
			ECRFFieldValueOutVO vo = fieldValuesIt.next();
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			fieldRow.put(fieldKey, INPUT_FIELD_VALUE_ADAPTER.toString(vo));
			distinctFieldRows.put(vo.getId(), fieldRow);
		}
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	// public void setTrial(TrialOutVO trial) {
	// this.trial = trial;
	// setSpreadSheetName(null);
	// }
	public void setVOs(ECRFFieldStatusQueue queue, Collection VOs) {
		getSpreadSheetWriters().get(queueSheetIndexMap.get(queue)).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setTrial(trial);
		excelVO.setListEntry(listEntry);
		excelVO.setEcrf(ecrf);
		// excelVO.setMethod(method);
		// excelVO.setPaid(paid);
		excelVO.setAuditTrailRowCount(getVOs().size());
		LinkedHashMap<ECRFFieldStatusQueue, Long> ecrfFieldStatusRowCountMap = new LinkedHashMap<ECRFFieldStatusQueue, Long>(queueSheetIndexMap.size());
		Iterator<ECRFFieldStatusQueue> it = queueSheetIndexMap.keySet().iterator();
		while (it.hasNext()) {
			ECRFFieldStatusQueue queue = it.next();
			ecrfFieldStatusRowCountMap.put(queue, new Long(getVOs(queue).size()));
		}
		excelVO.setEcrfFieldStatusRowCountMap(ecrfFieldStatusRowCountMap);
		// excelVO.setValidationRowCount(getValidationVOs().size());
		// excelVO.setQueryRowCount(getQueryVOs().size());
		// excelVO.setAnnotationRowCount(getAnnotationVOs().size());
		StringBuilder fileName = new StringBuilder(AUDIT_TRAIL_EXCEL_FILENAME_PREFIX);
		if (trial != null) {
			fileName.append(AUDIT_TRAIL_EXCEL_FILENAME_TRIAL);
			fileName.append(trial.getId());
			fileName.append("_");
		}
		if (listEntry != null) {
			fileName.append(AUDIT_TRAIL_EXCEL_FILENAME_PROBAND);
			fileName.append(listEntry.getProband().getId());
			fileName.append("_");
		}
		if (ecrf != null) {
			fileName.append(AUDIT_TRAIL_EXCEL_FILENAME_ECRF);
			fileName.append(ecrf.getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.EXCEL_FILENAME_EXTENSION);
		excelVO.setFileName(fileName.toString());
	}

	@Override
	public Color voToColor(Object vo) {
		if (vo instanceof ECRFFieldStatusEntryOutVO) {
			return ((ECRFFieldStatusEntryOutVO) vo).getStatus().getColor();
			// if (lastStatus != null) {
			// return lastStatus.getStatus().getColor();
			// }
		}
		return null;
	}
}
