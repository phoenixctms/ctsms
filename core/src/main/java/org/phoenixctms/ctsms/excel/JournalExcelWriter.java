package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.JournalExcelVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public class JournalExcelWriter extends WorkbookWriter {

	private JournalModule module;
	private InventoryOutVO inventory;
	private StaffOutVO staff;
	private CourseOutVO course;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	private UserOutVO user;
	private CriteriaOutVO criteria;
	private InputFieldOutVO inputField;
	private JournalExcelVO excelVO;
	private static final String JOURNAL_EXCEL_FILENAME_PREFIX = "journal_";

	public JournalExcelWriter(JournalModule module, boolean omitFields) {
		super();
		this.module = module;
		excelVO = new JournalExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					scaleFactor = Settings
					.getIntNullable(JournalExcelSettingCodes.INVENTORY_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_SCALE_FACTOR);
					break;
				case STAFF_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.STAFF_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_SCALE_FACTOR);
					break;
				case COURSE_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.COURSE_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_SCALE_FACTOR);
					break;
				case USER_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.USER_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_SCALE_FACTOR);
					break;
				case TRIAL_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.TRIAL_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_SCALE_FACTOR);
					break;
				case PROBAND_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.PROBAND_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_SCALE_FACTOR);
					break;
				case CRITERIA_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.CRITERIA_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_SCALE_FACTOR);
					break;
				case INPUT_FIELD_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.INPUT_FIELD_SCALE_FACTOR, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.INPUT_FIELD_SCALE_FACTOR);
					break;
				case ECRF_JOURNAL:
					scaleFactor = Settings.getIntNullable(JournalExcelSettingCodes.ECRF_SCALE_FACTOR, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_SCALE_FACTOR);
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

	private SpreadSheetWriter createSpreadSheetWriter(boolean omitFields) {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.INVENTORY_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.INVENTORY_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.INVENTORY_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.INVENTORY_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.INVENTORY_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL,
											JournalExcelDefaultSettings.INVENTORY_PAGE_BREAK_AT_ROW),
											Settings.getBoolean(JournalExcelSettingCodes.INVENTORY_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_ROW_COLORS),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.INVENTORY_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_HEAD_FORMAT),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.INVENTORY_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_ROW_FORMAT));
				case STAFF_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.STAFF_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.STAFF_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.STAFF_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.STAFF_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.STAFF_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(JournalExcelSettingCodes.STAFF_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_ROW_COLORS),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.STAFF_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_HEAD_FORMAT),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.STAFF_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_ROW_FORMAT));
				case COURSE_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.COURSE_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.COURSE_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.COURSE_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.COURSE_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.COURSE_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(JournalExcelSettingCodes.COURSE_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_ROW_COLORS),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.COURSE_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_HEAD_FORMAT),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.COURSE_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_ROW_FORMAT));
				case USER_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.USER_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.USER_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.USER_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.USER_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.USER_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(JournalExcelSettingCodes.USER_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_ROW_COLORS),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.USER_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_HEAD_FORMAT),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.USER_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_ROW_FORMAT));
				case TRIAL_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.TRIAL_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.TRIAL_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.TRIAL_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.TRIAL_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.TRIAL_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(JournalExcelSettingCodes.TRIAL_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_ROW_COLORS),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.TRIAL_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_HEAD_FORMAT),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.TRIAL_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_ROW_FORMAT));
				case PROBAND_JOURNAL:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.PROBAND_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.PROBAND_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.PROBAND_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.PROBAND_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.PROBAND_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_PAGE_BREAK_AT_ROW),
									Settings.getBoolean(JournalExcelSettingCodes.PROBAND_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_ROW_COLORS),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.PROBAND_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_HEAD_FORMAT),
									Settings.getExcelCellFormat(JournalExcelSettingCodes.PROBAND_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_ROW_FORMAT));
				case CRITERIA_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.CRITERIA_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.CRITERIA_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.CRITERIA_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.CRITERIA_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.CRITERIA_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL,
											JournalExcelDefaultSettings.CRITERIA_PAGE_BREAK_AT_ROW),
											Settings.getBoolean(JournalExcelSettingCodes.CRITERIA_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_ROW_COLORS),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.CRITERIA_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_HEAD_FORMAT),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.CRITERIA_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_ROW_FORMAT));
				case INPUT_FIELD_JOURNAL:
					return new SpreadSheetWriter(
							this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.INPUT_FIELD_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.INPUT_FIELD_VO_FIELD_COLUMNS)),
									Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
									omitFields,
									Settings.getBoolean(JournalExcelSettingCodes.INPUT_FIELD_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INPUT_FIELD_AUTOSIZE),
									Settings.getBoolean(JournalExcelSettingCodes.INPUT_FIELD_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INPUT_FIELD_WRITEHEAD),
									Settings.getIntNullable(JournalExcelSettingCodes.INPUT_FIELD_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL,
											JournalExcelDefaultSettings.INPUT_FIELD_PAGE_BREAK_AT_ROW),
											Settings.getBoolean(JournalExcelSettingCodes.INPUT_FIELD_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INPUT_FIELD_ROW_COLORS),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.INPUT_FIELD_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INPUT_FIELD_HEAD_FORMAT),
											Settings.getExcelCellFormat(JournalExcelSettingCodes.INPUT_FIELD_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INPUT_FIELD_ROW_FORMAT));
				case ECRF_JOURNAL:
					return new SpreadSheetWriter(this,
							getColumnIndexMap(L10nUtil.getJournalExcelColumns(Locales.USER, JournalExcelLabelCodes.ECRF_VO_FIELD_COLUMNS,
									JournalExcelDefaultSettings.ECRF_VO_FIELD_COLUMNS)),
							Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
							omitFields,
							Settings.getBoolean(JournalExcelSettingCodes.ECRF_AUTOSIZE, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_AUTOSIZE),
							Settings.getBoolean(JournalExcelSettingCodes.ECRF_WRITEHEAD, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_WRITEHEAD),
							Settings.getIntNullable(JournalExcelSettingCodes.ECRF_PAGE_BREAK_AT_ROW, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_PAGE_BREAK_AT_ROW),
							Settings.getBoolean(JournalExcelSettingCodes.ECRF_ROW_COLORS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_ROW_COLORS),
							Settings.getExcelCellFormat(JournalExcelSettingCodes.ECRF_HEAD_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_HEAD_FORMAT),
							Settings.getExcelCellFormat(JournalExcelSettingCodes.ECRF_ROW_FORMAT, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_ROW_FORMAT));
				default:
			}
		}
		return new SpreadSheetWriter(this,
				getColumnIndexMap(new ArrayList<String>()),
				Settings.getInt(JournalExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
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
		return L10nUtil.getJournalExcelLabel(Locales.USER, key.toString(), ExcelUtil.DEFAULT_LABEL);
	}

	public CourseOutVO getCourse() {
		return course;
	}

	public CriteriaOutVO getCriteria() {
		return criteria;
	}

	public JournalExcelVO getExcelVO() {
		return excelVO;
	}

	public InputFieldOutVO getInputField() {
		return inputField;
	}

	public InventoryOutVO getInventory() {
		return inventory;
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	public StaffOutVO getStaff() {
		return staff;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return Settings
							.getString(JournalExcelSettingCodes.INVENTORY_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.INVENTORY_TEMPLATE_FILE_NAME);
				case STAFF_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.STAFF_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.STAFF_TEMPLATE_FILE_NAME);
				case COURSE_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.COURSE_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.COURSE_TEMPLATE_FILE_NAME);
				case TRIAL_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.TRIAL_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.TRIAL_TEMPLATE_FILE_NAME);
				case PROBAND_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.PROBAND_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PROBAND_TEMPLATE_FILE_NAME);
				case USER_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.USER_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.USER_TEMPLATE_FILE_NAME);
				case INPUT_FIELD_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.INPUT_FIELD_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.INPUT_FIELD_TEMPLATE_FILE_NAME);
				case CRITERIA_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.CRITERIA_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.CRITERIA_TEMPLATE_FILE_NAME);
				case ECRF_JOURNAL:
					return Settings.getString(JournalExcelSettingCodes.ECRF_TEMPLATE_FILE_NAME, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.ECRF_TEMPLATE_FILE_NAME);
				default:
			}
		}
		return null;
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public UserOutVO getUser() {
		return user;
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

	public void setCourse(CourseOutVO course) {
		this.course = course;
		setSpreadSheetName(CommonUtil.courseOutVOToString(course));
	}

	public void setCriteria(CriteriaOutVO criteria) {
		this.criteria = criteria;
		setSpreadSheetName(CommonUtil.criteriaOutVOToString(criteria));
	}

	public void setInputField(InputFieldOutVO inputField) {
		this.inputField = inputField;
		setSpreadSheetName(CommonUtil.inputFieldOutVOToString(inputField));
	}

	public void setInventory(InventoryOutVO inventory) {
		this.inventory = inventory;
		setSpreadSheetName(CommonUtil.inventoryOutVOToString(inventory));
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
		setSpreadSheetName(CommonUtil.probandOutVOToString(proband));
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		String templateSpreadSheetName = null;
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.INVENTORY_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.INVENTORY_SPREADSHEET_NAME);
					break;
				case STAFF_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.STAFF_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.STAFF_SPREADSHEET_NAME);
					break;
				case COURSE_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.COURSE_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.COURSE_SPREADSHEET_NAME);
					break;
				case TRIAL_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.TRIAL_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.TRIAL_SPREADSHEET_NAME);
					break;
				case PROBAND_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.PROBAND_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.PROBAND_SPREADSHEET_NAME);
					break;
				case USER_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.USER_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.USER_SPREADSHEET_NAME);
					break;
				case INPUT_FIELD_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.INPUT_FIELD_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.INPUT_FIELD_SPREADSHEET_NAME);
					break;
				case CRITERIA_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.CRITERIA_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.CRITERIA_SPREADSHEET_NAME);
					break;
				case ECRF_JOURNAL:
					templateSpreadSheetName = Settings.getString(JournalExcelSettingCodes.ECRF_SPREADSHEET_NAME, Bundle.JOURNAL_EXCEL,
							JournalExcelDefaultSettings.ECRF_SPREADSHEET_NAME);
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

	public void setStaff(StaffOutVO staff) {
		this.staff = staff;
		setSpreadSheetName(CommonUtil.staffOutVOToString(staff));
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
		setSpreadSheetName(CommonUtil.trialOutVOToString(trial));
	}

	public void setUser(UserOutVO user) {
		this.user = user;
		setSpreadSheetName(CommonUtil.userOutVOToString(user));
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setModule(module);
		excelVO.setInventory(null);
		excelVO.setStaff(null);
		excelVO.setCourse(null);
		excelVO.setTrial(null);
		excelVO.setProband(null);
		excelVO.setUser(null);
		excelVO.setCriteria(null);
		excelVO.setInputField(null);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName;
		if (module != null) {
			fileName = new StringBuilder(module.value());
			fileName.append("_");
			switch (module) {
				case INVENTORY_JOURNAL:
					excelVO.setInventory(inventory);
					if (inventory != null) {
						fileName.append(inventory.getId());
						fileName.append("_");
					}
					break;
				case STAFF_JOURNAL:
					excelVO.setStaff(staff);
					if (staff != null) {
						fileName.append(staff.getId());
						fileName.append("_");
					}
					break;
				case COURSE_JOURNAL:
					excelVO.setCourse(course);
					if (course != null) {
						fileName.append(course.getId());
						fileName.append("_");
					}
					break;
				case USER_JOURNAL:
					excelVO.setUser(user);
					if (user != null) {
						fileName.append(user.getId());
						fileName.append("_");
					}
					break;
				case TRIAL_JOURNAL:
					excelVO.setTrial(trial);
					if (trial != null) {
						fileName.append(trial.getId());
						fileName.append("_");
					}
					break;
				case PROBAND_JOURNAL:
					excelVO.setProband(proband);
					if (proband != null) {
						fileName.append(proband.getId());
						fileName.append("_");
					}
					break;
				case CRITERIA_JOURNAL:
					excelVO.setCriteria(criteria);
					if (criteria != null) {
						fileName.append(criteria.getId());
						fileName.append("_");
					}
					break;
				case INPUT_FIELD_JOURNAL:
					excelVO.setInputField(inputField);
					if (inputField != null) {
						fileName.append(inputField.getId());
						fileName.append("_");
					}
					break;
				case ECRF_JOURNAL:
					excelVO.setTrial(trial);
					if (trial != null) {
						fileName.append(trial.getId());
						fileName.append("_");
					}
					break;
				default:
			}
		} else {
			fileName = new StringBuilder(JOURNAL_EXCEL_FILENAME_PREFIX);
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
