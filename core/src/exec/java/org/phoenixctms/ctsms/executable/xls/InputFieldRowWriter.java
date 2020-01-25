package org.phoenixctms.ctsms.executable.xls;

import java.io.IOException;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.FilePathSplitter;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.terracotta.agent.repkg.de.schlichtherle.io.FileOutputStream;

public class InputFieldRowWriter extends RowWriter {

	private final static String SHEET_NAME = "inputfields";
	private final static int NAME_COLUMN_INDEX = 0;
	private final static int TITLE_COLUMN_INDEX = 1;
	private final static int LOCALIZED_COLUMN_INDEX = 2;
	private final static int CATEGORY_COLUMN_INDEX = 3;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 4;
	private final static int FIELD_TYPE_COLUMN_INDEX = 5;
	private final static int COMMENT_COLUMN_INDEX = 6;
	private final static int TEXT_PRESET_COLUMN_INDEX = 7;
	private final static int BOOLEAN_PRESET_COLUMN_INDEX = 7;
	private final static int LONG_PRESET_COLUMN_INDEX = 7;
	private final static int FLOAT_PRESET_COLUMN_INDEX = 7;
	private final static int DATE_PRESET_COLUMN_INDEX = 7;
	private final static int TIME_PRESET_COLUMN_INDEX = 7;
	private final static int TIMESTAMP_PRESET_COLUMN_INDEX = 7;
	private final static int VALIDATION_ERROR_MSG_COLUMN_INDEX = 8;
	private final static int REGEXP_COLUMN_INDEX = 9;
	private final static int LEARN_COLUMN_INDEX = 9;
	private final static int STRICT_COLUMN_INDEX = 10;
	private final static int MIN_SELECTIONS_COLUMN_INDEX = 9;
	private final static int MAX_SELECTIONS_COLUMN_INDEX = 10;
	private final static int LONG_LOWER_LIMIT_COLUMN_INDEX = 9;
	private final static int LONG_UPPER_LIMIT_COLUMN_INDEX = 10;
	private final static int FLOAT_LOWER_LIMIT_COLUMN_INDEX = 9;
	private final static int FLOAT_UPPER_LIMIT_COLUMN_INDEX = 10;
	private final static int MIN_DATE_COLUMN_INDEX = 9;
	private final static int MAX_DATE_COLUMN_INDEX = 10;
	private final static int MIN_TIMESTAMP_COLUMN_INDEX = 9;
	private final static int MAX_TIMESTAMP_COLUMN_INDEX = 10;
	private final static int USER_TIME_ZONE_COLUMN_INDEX = 11;
	private final static int MIN_TIME_COLUMN_INDEX = 9;
	private final static int MAX_TIME_COLUMN_INDEX = 10;
	private final static int WIDTH_COLUMN_INDEX = 11;
	private final static int HEIGHT_COLUMN_INDEX = 12;
	private final static int FILE_NAME_COLUMN_INDEX = 13;
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
	private int userTimeZoneIndex;
	private int minTimeColumnIndex;
	private int maxTimeColumnIndex;
	private int widthColumnIndex;
	private int heightColumnIndex;
	private int fileNameColumnIndex;
	private String dateTimePattern;
	private String datePattern;
	private String timePattern;
	private FilePathSplitter filePath;
	private int maxColumnIndex;
	@Autowired
	protected InputFieldService inputFieldService;
	@Autowired
	protected ToolsService toolsService;

	public InputFieldRowWriter() {
		super();
		maxColumnIndex = 0;
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	@Override
	public void init() {
		super.init();
		maxColumnIndex = 0;
		nameColumnIndex = NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(nameColumnIndex, maxColumnIndex);
		titleColumnIndex = TITLE_COLUMN_INDEX;
		maxColumnIndex = Math.max(titleColumnIndex, maxColumnIndex);
		localizedColumnIndex = LOCALIZED_COLUMN_INDEX;
		maxColumnIndex = Math.max(localizedColumnIndex, maxColumnIndex);
		categoryColumnIndex = CATEGORY_COLUMN_INDEX;
		maxColumnIndex = Math.max(categoryColumnIndex, maxColumnIndex);
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(externalIdColumnIndex, maxColumnIndex);
		fieldTypeColumnIndex = FIELD_TYPE_COLUMN_INDEX;
		maxColumnIndex = Math.max(fieldTypeColumnIndex, maxColumnIndex);
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		maxColumnIndex = Math.max(commentColumnIndex, maxColumnIndex);
		textPresetColumnIndex = TEXT_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(textPresetColumnIndex, maxColumnIndex);
		booleanPresetColumnIndex = BOOLEAN_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(booleanPresetColumnIndex, maxColumnIndex);
		longPresetColumnIndex = LONG_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(longPresetColumnIndex, maxColumnIndex);
		floatPresetColumnIndex = FLOAT_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(floatPresetColumnIndex, maxColumnIndex);
		datePresetColumnIndex = DATE_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(datePresetColumnIndex, maxColumnIndex);
		timestampPresetColumnIndex = TIMESTAMP_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(timestampPresetColumnIndex, maxColumnIndex);
		timePresetColumnIndex = TIME_PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(timePresetColumnIndex, maxColumnIndex);
		validationErrorMsgColumnIndex = VALIDATION_ERROR_MSG_COLUMN_INDEX;
		maxColumnIndex = Math.max(validationErrorMsgColumnIndex, maxColumnIndex);
		regExpColumnIndex = REGEXP_COLUMN_INDEX;
		maxColumnIndex = Math.max(regExpColumnIndex, maxColumnIndex);
		learnColumnIndex = LEARN_COLUMN_INDEX;
		maxColumnIndex = Math.max(learnColumnIndex, maxColumnIndex);
		strictColumnIndex = STRICT_COLUMN_INDEX;
		maxColumnIndex = Math.max(strictColumnIndex, maxColumnIndex);
		minSelectionsColumnIndex = MIN_SELECTIONS_COLUMN_INDEX;
		maxColumnIndex = Math.max(minSelectionsColumnIndex, maxColumnIndex);
		maxSelectionsColumnIndex = MAX_SELECTIONS_COLUMN_INDEX;
		maxColumnIndex = Math.max(maxSelectionsColumnIndex, maxColumnIndex);
		longLowerLimitColumnIndex = LONG_LOWER_LIMIT_COLUMN_INDEX;
		maxColumnIndex = Math.max(longLowerLimitColumnIndex, maxColumnIndex);
		longUpperLimitColumnIndex = LONG_UPPER_LIMIT_COLUMN_INDEX;
		maxColumnIndex = Math.max(longUpperLimitColumnIndex, maxColumnIndex);
		floatLowerLimitColumnIndex = FLOAT_LOWER_LIMIT_COLUMN_INDEX;
		maxColumnIndex = Math.max(floatLowerLimitColumnIndex, maxColumnIndex);
		floatUpperLimitColumnIndex = FLOAT_UPPER_LIMIT_COLUMN_INDEX;
		maxColumnIndex = Math.max(floatUpperLimitColumnIndex, maxColumnIndex);
		minDateColumnIndex = MIN_DATE_COLUMN_INDEX;
		maxColumnIndex = Math.max(minDateColumnIndex, maxColumnIndex);
		maxDateColumnIndex = MAX_DATE_COLUMN_INDEX;
		maxColumnIndex = Math.max(maxDateColumnIndex, maxColumnIndex);
		minTimestampColumnIndex = MIN_TIMESTAMP_COLUMN_INDEX;
		maxColumnIndex = Math.max(minTimestampColumnIndex, maxColumnIndex);
		maxTimestampColumnIndex = MAX_TIMESTAMP_COLUMN_INDEX;
		maxColumnIndex = Math.max(maxTimestampColumnIndex, maxColumnIndex);
		userTimeZoneIndex = USER_TIME_ZONE_COLUMN_INDEX;
		maxColumnIndex = Math.max(userTimeZoneIndex, maxColumnIndex);
		minTimeColumnIndex = MIN_TIME_COLUMN_INDEX;
		maxColumnIndex = Math.max(minTimeColumnIndex, maxColumnIndex);
		maxTimeColumnIndex = MAX_TIME_COLUMN_INDEX;
		maxColumnIndex = Math.max(maxTimeColumnIndex, maxColumnIndex);
		widthColumnIndex = WIDTH_COLUMN_INDEX;
		maxColumnIndex = Math.max(widthColumnIndex, maxColumnIndex);
		heightColumnIndex = HEIGHT_COLUMN_INDEX;
		maxColumnIndex = Math.max(heightColumnIndex, maxColumnIndex);
		fileNameColumnIndex = FILE_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(fileNameColumnIndex, maxColumnIndex);
		dateTimePattern = ExecSettings.getString(ExecSettingCodes.DATETIME_PATTERN, ExecDefaultSettings.DATETIME_PATTERN);
		datePattern = ExecSettings.getString(ExecSettingCodes.DATE_PATTERN, ExecDefaultSettings.DATE_PATTERN);
		timePattern = ExecSettings.getString(ExecSettingCodes.TIME_PATTERN, ExecDefaultSettings.TIME_PATTERN);
		filePath = new FilePathSplitter(context.getFileName());
		context.getExporter().getSelectionSetValueRowWriter().init();
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(inputFieldService.getInputField(context.getAuth(), context.getEntityId(this)));
	}

	public void printRows(InputFieldOutVO inputField) throws Throwable {
		String[] values = new String[maxColumnIndex + 1];
		values[nameColumnIndex] = inputField.getNameL10nKey();
		values[titleColumnIndex] = inputField.getTitleL10nKey();
		values[localizedColumnIndex] = Boolean.toString(inputField.getLocalized());
		values[categoryColumnIndex] = inputField.getCategory();
		values[externalIdColumnIndex] = inputField.getExternalId();
		InputFieldType fieldType = inputField.getFieldType().getType();
		values[fieldTypeColumnIndex] = fieldType.name();
		switch (fieldType) {
			case SINGLE_LINE_TEXT:
			case MULTI_LINE_TEXT:
				values[textPresetColumnIndex] = inputField.getTextPresetL10nKey();
				values[regExpColumnIndex] = inputField.getRegExp();
				break;
			case AUTOCOMPLETE:
				values[learnColumnIndex] = Boolean.toString(inputField.getLearn());
				values[strictColumnIndex] = Boolean.toString(inputField.getStrict());
				context.setEntityId(context.getExporter().getSelectionSetValueRowWriter(), inputField.getId());
				context.getExporter().getSelectionSetValueRowWriter().printRows();
				break;
			case CHECKBOX:
				values[booleanPresetColumnIndex] = Boolean.toString(inputField.getBooleanPreset());
				break;
			case INTEGER:
				values[longPresetColumnIndex] = (inputField.getLongPreset() != null ? Long.toString(inputField.getLongPreset()) : null);
				values[longLowerLimitColumnIndex] = (inputField.getLongLowerLimit() != null ? Long.toString(inputField.getLongLowerLimit()) : null);
				values[longUpperLimitColumnIndex] = (inputField.getLongUpperLimit() != null ? Long.toString(inputField.getLongUpperLimit()) : null);
				break;
			case FLOAT:
				values[floatPresetColumnIndex] = (inputField.getFloatPreset() != null ? Float.toString(inputField.getFloatPreset()) : null);
				values[floatLowerLimitColumnIndex] = (inputField.getFloatLowerLimit() != null ? Float.toString(inputField.getFloatLowerLimit()) : null);
				values[floatUpperLimitColumnIndex] = (inputField.getFloatUpperLimit() != null ? Float.toString(inputField.getFloatUpperLimit()) : null);
				break;
			case DATE:
				values[datePresetColumnIndex] = (inputField.getDatePreset() != null ? CommonUtil.formatDate(inputField.getDatePreset(), datePattern) : null);
				values[minDateColumnIndex] = (inputField.getMinDate() != null ? CommonUtil.formatDate(inputField.getMinDate(), datePattern) : null);
				values[maxDateColumnIndex] = (inputField.getMaxDate() != null ? CommonUtil.formatDate(inputField.getMaxDate(), datePattern) : null);
				break;
			case TIME:
				values[timePresetColumnIndex] = (inputField.getTimePreset() != null ? CommonUtil.formatDate(inputField.getTimePreset(), timePattern) : null);
				values[minTimeColumnIndex] = (inputField.getMinTime() != null ? CommonUtil.formatDate(inputField.getMinTime(), timePattern) : null);
				values[maxTimeColumnIndex] = (inputField.getMaxTime() != null ? CommonUtil.formatDate(inputField.getMaxTime(), timePattern) : null);
				break;
			case TIMESTAMP:
				values[timestampPresetColumnIndex] = (inputField.getTimestampPreset() != null ? CommonUtil.formatDate(inputField.getTimestampPreset(), dateTimePattern) : null);
				values[minTimestampColumnIndex] = (inputField.getMinTimestamp() != null ? CommonUtil.formatDate(inputField.getMinTimestamp(), dateTimePattern) : null);
				values[maxTimestampColumnIndex] = (inputField.getMaxTimestamp() != null ? CommonUtil.formatDate(inputField.getMaxTimestamp(), dateTimePattern) : null);
				values[userTimeZoneIndex] = Boolean.toString(inputField.getUserTimeZone());
				break;
			case SKETCH:
				values[widthColumnIndex] = (inputField.getWidth() != null ? Long.toString(inputField.getWidth()) : null);
				values[heightColumnIndex] = (inputField.getHeight() != null ? Long.toString(inputField.getHeight()) : null);
				if (inputField.getHasImage()) {
					values[fileNameColumnIndex] = writeFile(inputField);
				}
				// no break
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				values[minSelectionsColumnIndex] = (inputField.getMinSelections() != null ? Integer.toString(inputField.getMinSelections()) : null);
				values[maxSelectionsColumnIndex] = (inputField.getMaxSelections() != null ? Integer.toString(inputField.getMaxSelections()) : null);
				// no break
			case SELECT_ONE_DROPDOWN:
			case SELECT_ONE_RADIO_H:
			case SELECT_ONE_RADIO_V:
				context.getExporter().getSelectionSetValueRowWriter().printRows(inputField.getSelectionSetValues());
				break;
			default:
				break;
		}
		values[commentColumnIndex] = inputField.getCommentL10nKey();
		values[validationErrorMsgColumnIndex] = inputField.getValidationErrorMsgL10nKey();
		printRow(values);
	}

	private String writeFile(InputFieldOutVO inputField) throws Throwable {
		InputFieldImageVO inputFieldImage = toolsService.getInputFieldImage(inputField.getId());
		java.io.File file = new java.io.File(filePath.getDirectory(), inputFieldImage.getFileName());
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			stream.write(inputFieldImage.getDatas());
			stream.flush();
			stream.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		return inputFieldImage.getFileName();
	}
}
