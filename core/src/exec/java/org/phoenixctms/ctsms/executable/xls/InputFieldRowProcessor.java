package org.phoenixctms.ctsms.executable.xls;

import java.io.FileInputStream;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.shared.FileService;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.FilePathSplitter;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class InputFieldRowProcessor extends RowProcessor {

	private final static String SHEET_NAME = "inputfields";
	private final static int NAME_COLUMN_INDEX = 0;
	private final static int TITLE_COLUMN_INDEX = 1;
	private final static int LOCALIZED_COLUMN_INDEX = 2;
	private final static int CATEGORY_COLUMN_INDEX = 3;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 4;
	private final static int FIELD_TYPE_COLUMN_INDEX = 5;
	private final static int COMMENT_COLUMN_INDEX = 6;
	private final static int PRESET_COLUMN_INDEX = 7;
	private final static int TEXT_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int BOOLEAN_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int LONG_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int FLOAT_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int DATE_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int TIME_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int TIMESTAMP_PRESET_COLUMN_INDEX = PRESET_COLUMN_INDEX;
	private final static int VALIDATION_ERROR_MSG_COLUMN_INDEX = 8;
	private final static int PARAM_1_COLUMN_INDEX = 9;
	private final static int PARAM_2_COLUMN_INDEX = 10;
	private final static int PARAM_3_COLUMN_INDEX = 11;
	private final static int PARAM_4_COLUMN_INDEX = 12;
	private final static int PARAM_5_COLUMN_INDEX = 13;
	private final static int REGEXP_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int LEARN_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int STRICT_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int MIN_SELECTIONS_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int MAX_SELECTIONS_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int LONG_LOWER_LIMIT_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int LONG_UPPER_LIMIT_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int FLOAT_LOWER_LIMIT_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int FLOAT_UPPER_LIMIT_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int MIN_DATE_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int MAX_DATE_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int MIN_TIMESTAMP_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int MAX_TIMESTAMP_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int USER_TIME_ZONE_COLUMN_INDEX = PARAM_3_COLUMN_INDEX;
	private final static int MIN_TIME_COLUMN_INDEX = PARAM_1_COLUMN_INDEX;
	private final static int MAX_TIME_COLUMN_INDEX = PARAM_2_COLUMN_INDEX;
	private final static int WIDTH_COLUMN_INDEX = PARAM_3_COLUMN_INDEX;
	private final static int HEIGHT_COLUMN_INDEX = PARAM_4_COLUMN_INDEX;
	private final static int FILE_NAME_COLUMN_INDEX = PARAM_5_COLUMN_INDEX;
	private int nameColumnIndex;
	private int titleColumnIndex;
	private int localizedColumnIndex;
	private int categoryColumnIndex;
	private int externalIdColumnIndex;
	private int fieldTypeColumnIndex;
	private int commentColumnIndex;
	private int textPresetColumnIndex;
	private int booleanPresetColumnIndex;
	private int longPresetColumnIndex;
	private int floatPresetColumnIndex;
	private int datePresetColumnIndex;
	private int timestampPresetColumnIndex;
	private int timePresetColumnIndex;
	private int validationErrorMsgColumnIndex;
	private int regExpColumnIndex;
	private int learnColumnIndex;
	private int strictColumnIndex;
	private int minSelectionsColumnIndex;
	private int maxSelectionsColumnIndex;
	private int longLowerLimitColumnIndex;
	private int longUpperLimitColumnIndex;
	private int floatLowerLimitColumnIndex;
	private int floatUpperLimitColumnIndex;
	private int minDateColumnIndex;
	private int maxDateColumnIndex;
	private int minTimestampColumnIndex;
	private int maxTimestampColumnIndex;
	private int userTimeZoneColumnIndex;
	private int minTimeColumnIndex;
	private int maxTimeColumnIndex;
	private int widthColumnIndex;
	private int heightColumnIndex;
	private int fileNameColumnIndex;
	private String dateTimePattern;
	private String datePattern;
	private String timePattern;
	private FilePathSplitter filePath;
	@Autowired
	protected InputFieldDao inputFieldDao;
	@Autowired
	protected InputFieldService inputFieldService;
	@Autowired
	private FileService fileService;

	public InputFieldRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
	}

	private String getBooleanPreset(String[] values) {
		return getColumnValue(values, booleanPresetColumnIndex);
	}

	private String getCategory(String[] values) {
		return getColumnValue(values, categoryColumnIndex);
	}

	private String getComment(String[] values) {
		return getColumnValue(values, commentColumnIndex);
	}

	private String getDatePreset(String[] values) {
		return getColumnValue(values, datePresetColumnIndex);
	}

	private String getExternalId(String[] values) {
		return getColumnValue(values, externalIdColumnIndex);
	}

	private String getFieldType(String[] values) {
		return getColumnValue(values, fieldTypeColumnIndex);
	}

	private String getFileName(String[] values) {
		return getColumnValue(values, fileNameColumnIndex);
	}

	private String getFloatLowerLimit(String[] values) {
		return getColumnValue(values, floatLowerLimitColumnIndex);
	}

	private String getFloatPreset(String[] values) {
		return getColumnValue(values, floatPresetColumnIndex);
	}

	private String getFloatUpperLimit(String[] values) {
		return getColumnValue(values, floatUpperLimitColumnIndex);
	}

	private String getHeight(String[] values) {
		return getColumnValue(values, heightColumnIndex);
	}

	private String getLearn(String[] values) {
		return getColumnValue(values, learnColumnIndex);
	}

	private String getLocalized(String[] values) {
		return getColumnValue(values, localizedColumnIndex);
	}

	private String getLongLowerLimit(String[] values) {
		return getColumnValue(values, longLowerLimitColumnIndex);
	}

	private String getLongPreset(String[] values) {
		return getColumnValue(values, longPresetColumnIndex);
	}

	private String getLongUpperLimit(String[] values) {
		return getColumnValue(values, longUpperLimitColumnIndex);
	}

	private String getMaxDate(String[] values) {
		return getColumnValue(values, maxDateColumnIndex);
	}

	private String getMaxSelections(String[] values) {
		return getColumnValue(values, maxSelectionsColumnIndex);
	}

	private String getMaxTime(String[] values) {
		return getColumnValue(values, maxTimeColumnIndex);
	}

	private String getMaxTimestamp(String[] values) {
		return getColumnValue(values, maxTimestampColumnIndex);
	}

	private String getMinDate(String[] values) {
		return getColumnValue(values, minDateColumnIndex);
	}

	private String getMinSelections(String[] values) {
		return getColumnValue(values, minSelectionsColumnIndex);
	}

	private String getMinTime(String[] values) {
		return getColumnValue(values, minTimeColumnIndex);
	}

	private String getMinTimestamp(String[] values) {
		return getColumnValue(values, minTimestampColumnIndex);
	}

	private String getName(String[] values) {
		return getColumnValue(values, nameColumnIndex);
	}

	private String getRegExp(String[] values) {
		return getColumnValue(values, regExpColumnIndex);
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	private String getStrict(String[] values) {
		return getColumnValue(values, strictColumnIndex);
	}

	private String getTextPreset(String[] values) {
		return getColumnValue(values, textPresetColumnIndex);
	}

	private String getTimePreset(String[] values) {
		return getColumnValue(values, timePresetColumnIndex);
	}

	private String getTimestampPreset(String[] values) {
		return getColumnValue(values, timestampPresetColumnIndex);
	}

	private String getTitle(String[] values) {
		return getColumnValue(values, titleColumnIndex);
	}

	private String getUserTimeZone(String[] values) {
		return getColumnValue(values, userTimeZoneColumnIndex);
	}

	private String getValidationErrorMsg(String[] values) {
		return getColumnValue(values, validationErrorMsgColumnIndex);
	}

	private String getWidth(String[] values) {
		return getColumnValue(values, widthColumnIndex);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		nameColumnIndex = NAME_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		localizedColumnIndex = LOCALIZED_COLUMN_INDEX;
		categoryColumnIndex = CATEGORY_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		fieldTypeColumnIndex = FIELD_TYPE_COLUMN_INDEX;
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		textPresetColumnIndex = TEXT_PRESET_COLUMN_INDEX;
		booleanPresetColumnIndex = BOOLEAN_PRESET_COLUMN_INDEX;
		longPresetColumnIndex = LONG_PRESET_COLUMN_INDEX;
		floatPresetColumnIndex = FLOAT_PRESET_COLUMN_INDEX;
		datePresetColumnIndex = DATE_PRESET_COLUMN_INDEX;
		timestampPresetColumnIndex = TIMESTAMP_PRESET_COLUMN_INDEX;
		timePresetColumnIndex = TIME_PRESET_COLUMN_INDEX;
		validationErrorMsgColumnIndex = VALIDATION_ERROR_MSG_COLUMN_INDEX;
		regExpColumnIndex = REGEXP_COLUMN_INDEX;
		learnColumnIndex = LEARN_COLUMN_INDEX;
		strictColumnIndex = STRICT_COLUMN_INDEX;
		minSelectionsColumnIndex = MIN_SELECTIONS_COLUMN_INDEX;
		maxSelectionsColumnIndex = MAX_SELECTIONS_COLUMN_INDEX;
		longLowerLimitColumnIndex = LONG_LOWER_LIMIT_COLUMN_INDEX;
		longUpperLimitColumnIndex = LONG_UPPER_LIMIT_COLUMN_INDEX;
		floatLowerLimitColumnIndex = FLOAT_LOWER_LIMIT_COLUMN_INDEX;
		floatUpperLimitColumnIndex = FLOAT_UPPER_LIMIT_COLUMN_INDEX;
		minDateColumnIndex = MIN_DATE_COLUMN_INDEX;
		maxDateColumnIndex = MAX_DATE_COLUMN_INDEX;
		minTimestampColumnIndex = MIN_TIMESTAMP_COLUMN_INDEX;
		maxTimestampColumnIndex = MAX_TIMESTAMP_COLUMN_INDEX;
		userTimeZoneColumnIndex = USER_TIME_ZONE_COLUMN_INDEX;
		minTimeColumnIndex = MIN_TIME_COLUMN_INDEX;
		maxTimeColumnIndex = MAX_TIME_COLUMN_INDEX;
		widthColumnIndex = WIDTH_COLUMN_INDEX;
		heightColumnIndex = HEIGHT_COLUMN_INDEX;
		fileNameColumnIndex = FILE_NAME_COLUMN_INDEX;
		dateTimePattern = ExecSettings.getString(ExecSettingCodes.DATETIME_PATTERN, ExecDefaultSettings.DATETIME_PATTERN);
		datePattern = ExecSettings.getString(ExecSettingCodes.DATE_PATTERN, ExecDefaultSettings.DATE_PATTERN);
		timePattern = ExecSettings.getString(ExecSettingCodes.TIME_PATTERN, ExecDefaultSettings.TIME_PATTERN);
		filePath = new FilePathSplitter(context.getFileName());
		((XlsImporter) context.getImporter()).loadSelectionSetValues(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getName(values))
				.append(getTitle(values))
				.append(getLocalized(values))
				.append(getCategory(values))
				.append(getExternalId(values))
				.append(getFieldType(values))
				.append(getComment(values))
				.append(getTextPreset(values))
				.append(getBooleanPreset(values))
				.append(getLongPreset(values))
				.append(getFloatPreset(values))
				.append(getDatePreset(values))
				.append(getTimestampPreset(values))
				.append(getTimePreset(values))
				.append(getValidationErrorMsg(values))
				.append(getRegExp(values))
				.append(getLearn(values))
				.append(getStrict(values))
				.append(getMinSelections(values))
				.append(getMaxSelections(values))
				.append(getLongLowerLimit(values))
				.append(getLongUpperLimit(values))
				.append(getFloatLowerLimit(values))
				.append(getFloatUpperLimit(values))
				.append(getMinDate(values))
				.append(getMaxDate(values))
				.append(getMinTimestamp(values))
				.append(getMaxTimestamp(values))
				.append(getMinTime(values))
				.append(getMaxTime(values))
				.append(getWidth(values))
				.append(getHeight(values))
				.append(getFileName(values))
				.toHashCode();
	}

	private void loadFile(InputFieldInVO inputFieldIn, String fileName) throws Throwable {
		if (!CommonUtil.isEmptyString(fileName)) {
			try {
				long fileId = Long.parseLong(fileName);
				FileContentOutVO file = fileService.getFileContent(context.getAuth(), fileId);
				jobOutput.println("file ID " + fileName + " loaded (" + file.getFileName() + ")");
				inputFieldIn.setFileName(file.getFileName());
				inputFieldIn.setDatas(file.getDatas());
				inputFieldIn.setMimeType(file.getContentType().getMimeType());
			} catch (NumberFormatException e) {
				java.io.File file = new java.io.File(filePath.getDirectory(), CommonUtil.sanitizeFilePath(fileName));
				inputFieldIn.setFileName(file.getName());
				FileInputStream stream = new FileInputStream(file);
				inputFieldIn.setDatas(CommonUtil.inputStreamToByteArray(stream));
				inputFieldIn.setMimeType(CommonUtil.getMimeType(file));
			}
		}
	}

	@Override
	public void postProcess() {
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String name = getName(values);
		InputField inputField = ((XlsImporter) context.getImporter()).getSelectionSetValueRowProcessor().getInputField(name);
		InputFieldInVO inputFieldIn = new InputFieldInVO();
		inputFieldIn.setId(inputField != null ? inputField.getId() : null);
		inputFieldIn.setVersion(inputField != null ? inputField.getVersion() : 0l);
		inputFieldIn.setName(getName(values));
		inputFieldIn.setTitle(getTitle(values));
		inputFieldIn.setLocalized(Boolean.parseBoolean(getLocalized(values)));
		inputFieldIn.setCategory(getCategory(values));
		inputFieldIn.setExternalId(getExternalId(values));
		InputFieldType fieldType = InputFieldType.fromString(getFieldType(values));
		inputFieldIn.setFieldType(fieldType);
		switch (fieldType) {
			case SINGLE_LINE_TEXT:
			case MULTI_LINE_TEXT:
				inputFieldIn.setTextPreset(CommonUtil.isEmptyString(getTextPreset(values)) ? null : getTextPreset(values));
				inputFieldIn.setRegExp(CommonUtil.isEmptyString(getRegExp(values)) ? null : getRegExp(values));
				break;
			case AUTOCOMPLETE:
				inputFieldIn.setLearn(Boolean.parseBoolean(getLearn(values)));
				inputFieldIn.setStrict(Boolean.parseBoolean(getStrict(values)));
				break;
			case CHECKBOX:
				inputFieldIn.setBooleanPreset(Boolean.parseBoolean(getBooleanPreset(values)));
				break;
			case INTEGER:
				inputFieldIn.setLongPreset(CommonUtil.isEmptyString(getLongPreset(values)) ? null : Long.parseLong(getLongPreset(values)));
				inputFieldIn.setLongLowerLimit(CommonUtil.isEmptyString(getLongLowerLimit(values)) ? null : Long.parseLong(getLongLowerLimit(values)));
				inputFieldIn.setLongUpperLimit(CommonUtil.isEmptyString(getLongUpperLimit(values)) ? null : Long.parseLong(getLongUpperLimit(values)));
				break;
			case FLOAT:
				inputFieldIn.setFloatPreset(CommonUtil.isEmptyString(getFloatPreset(values)) ? null : Float.parseFloat(getFloatPreset(values)));
				inputFieldIn.setFloatLowerLimit(CommonUtil.isEmptyString(getFloatLowerLimit(values)) ? null : Float.parseFloat(getFloatLowerLimit(values)));
				inputFieldIn.setFloatUpperLimit(CommonUtil.isEmptyString(getFloatUpperLimit(values)) ? null : Float.parseFloat(getFloatUpperLimit(values)));
				break;
			case DATE:
				inputFieldIn.setDatePreset(CommonUtil.isEmptyString(getDatePreset(values)) ? null : CommonUtil.parseDate(getDatePreset(values), datePattern));
				inputFieldIn.setMinDate(CommonUtil.isEmptyString(getMinDate(values)) ? null : CommonUtil.parseDate(getMinDate(values), datePattern));
				inputFieldIn.setMaxDate(CommonUtil.isEmptyString(getMaxDate(values)) ? null : CommonUtil.parseDate(getMaxDate(values), datePattern));
				break;
			case TIME:
				inputFieldIn.setTimePreset(CommonUtil.isEmptyString(getTimePreset(values)) ? null : CommonUtil.parseDate(getTimePreset(values), timePattern));
				inputFieldIn.setMinTime(CommonUtil.isEmptyString(getMinTime(values)) ? null : CommonUtil.parseDate(getMinTime(values), timePattern));
				inputFieldIn.setMaxTime(CommonUtil.isEmptyString(getMaxTime(values)) ? null : CommonUtil.parseDate(getMaxTime(values), timePattern));
				break;
			case TIMESTAMP:
				inputFieldIn.setTimestampPreset(CommonUtil.isEmptyString(getTimestampPreset(values)) ? null : CommonUtil.parseDate(getTimestampPreset(values), dateTimePattern));
				inputFieldIn.setMinTimestamp(CommonUtil.isEmptyString(getMinTimestamp(values)) ? null : CommonUtil.parseDate(getMinTimestamp(values), dateTimePattern));
				inputFieldIn.setMaxTimestamp(CommonUtil.isEmptyString(getMaxTimestamp(values)) ? null : CommonUtil.parseDate(getMaxTimestamp(values), dateTimePattern));
				inputFieldIn.setUserTimeZone(Boolean.parseBoolean(getUserTimeZone(values)));
				break;
			case SKETCH:
				inputFieldIn.setWidth(CommonUtil.isEmptyString(getWidth(values)) ? null : Long.parseLong(getWidth(values)));
				inputFieldIn.setHeight(CommonUtil.isEmptyString(getHeight(values)) ? null : Long.parseLong(getHeight(values)));
				loadFile(inputFieldIn, getFileName(values));
				// no break
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				inputFieldIn.setMinSelections(CommonUtil.isEmptyString(getMinSelections(values)) ? null : Integer.parseInt(getMinSelections(values)));
				inputFieldIn.setMaxSelections(CommonUtil.isEmptyString(getMaxSelections(values)) ? null : Integer.parseInt(getMaxSelections(values)));
				break;
			default:
				break;
		}
		inputFieldIn.setComment(getComment(values));
		inputFieldIn.setValidationErrorMsg(CommonUtil.isEmptyString(getValidationErrorMsg(values)) ? null : getValidationErrorMsg(values));
		InputFieldOutVO inputFieldVO = inputFieldService.addUpdateInputField(context.getAuth(), inputFieldIn,
				((XlsImporter) context.getImporter()).getSelectionSetValueRowProcessor().getSelectionSetValues(name));
		jobOutput.println("input field '" + inputFieldVO.getName() + "' " + (inputFieldVO.getVersion() > 0l ? "updated" : "created"));
		((XlsImporter) context.getImporter()).getSelectionSetValueRowProcessor().clearInputField(name);
		return 1;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getName(values))) {
			return false;
		}
		return true;
	}
}
