package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.springframework.beans.factory.annotation.Autowired;

public class SelectionSetValueRowProcessor extends RowProcessor {

	private final static String SHEET_NAME = "selectionsetvalues";
	private final static int FIELD_NAME_COLUMN_INDEX = 0;
	private final static int NAME_COLUMN_INDEX = 1;
	private final static int VALUE_COLUMN_INDEX = 2;
	private final static int LOCALIZED_COLUMN_INDEX = 3;
	private final static int PRESET_COLUMN_INDEX = 4;
	private final static int STROKES_ID_COLUMN_INDEX = 5;
	private final static int INK_REGIONS_COLUMN_INDEX = 6;
	// @Autowired
	// protected InputFieldService inputFieldService;
	private int fieldNameColumnIndex;
	private int nameColumnIndex;
	private int valueColumnIndex;
	private int localizedColumnIndex;
	private int presetColumnIndex;
	private int strokesIdColumnIndex;
	private int inkRegionsColumnIndex;
	// private AuthenticationVO auth;
	// private Long inputFieldId;
	private HashMap<String, InputField> inputFieldMap;
	private HashMap<String, Set<InputFieldSelectionSetValueInVO>> selectionSetValueMap;
	@Autowired
	protected InputFieldDao inputFieldDao;
	@Autowired
	protected InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;

	public SelectionSetValueRowProcessor() {
		super();
		filterDupes = false;
		// this.setCommentChar(null);
		acceptCommentsIndex = 0;
		inputFieldMap = new HashMap<String, InputField>();
		selectionSetValueMap = new HashMap<String, Set<InputFieldSelectionSetValueInVO>>();
	}
	// public AuthenticationVO getAuth() {
	// return auth;
	// }

	public void clearInputField(String fieldName) {
		inputFieldMap.remove(fieldName);
	}

	private String getFieldName(String[] values) {
		return getColumnValue(values, fieldNameColumnIndex);
	}

	private String getInkRegions(String[] values) {
		return getColumnValue(values, inkRegionsColumnIndex);
	}

	public InputField getInputField(String fieldName) {
		InputField inputField = null;
		// inputFieldDao.clearEhcache
		if (inputFieldMap.containsKey(fieldName)) {
			inputField = inputFieldMap.get(fieldName);
		} else {
			inputField = inputFieldDao.searchUniqueNameL10nKey(fieldName);
			inputFieldMap.put(fieldName, inputField);
		}
		return inputField;
	}

	// public Long getInputFieldId() {
	// return inputFieldId;
	// }
	private String getLocalized(String[] values) {
		return getColumnValue(values, localizedColumnIndex);
	}

	private String getName(String[] values) {
		return getColumnValue(values, nameColumnIndex);
	}

	private String getPreset(String[] values) {
		return getColumnValue(values, presetColumnIndex);
	}

	// public HashMap<String, Set<InputFieldSelectionSetValueInVO>> getSelectionSetValueMap() {
	// return selectionSetValueMap;
	// }
	public Set<InputFieldSelectionSetValueInVO> getSelectionSetValues(String fieldName) {
		return selectionSetValueMap.get(fieldName);
	}

	@Override
	public String getSheetName() {
		// return context.getSheetName(this);
		return SHEET_NAME;
	}

	private String getStrokesId(String[] values) {
		return getColumnValue(values, strokesIdColumnIndex);
	}

	private String getValue(String[] values) {
		return getColumnValue(values, valueColumnIndex);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		fieldNameColumnIndex = FIELD_NAME_COLUMN_INDEX;
		nameColumnIndex = NAME_COLUMN_INDEX;
		valueColumnIndex = VALUE_COLUMN_INDEX;
		localizedColumnIndex = LOCALIZED_COLUMN_INDEX;
		presetColumnIndex = PRESET_COLUMN_INDEX;
		strokesIdColumnIndex = STROKES_ID_COLUMN_INDEX;
		inkRegionsColumnIndex = INK_REGIONS_COLUMN_INDEX;
		inputFieldMap.clear();
		selectionSetValueMap.clear();
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				// .append(getFieldName(values))
				// .append(getValue(values))
				.append(getFieldName(values))
				.append(getName(values))
				.append(getValue(values))
				.append(getLocalized(values))
				.append(getPreset(values))
				.append(getStrokesId(values))
				.append(getInkRegions(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String fieldName = getFieldName(values);
		String name = getName(values);
		String value = getValue(values);
		if (CommonUtil.isEmptyString(name)) {
			name = value;
		}
		InputField inputField = getInputField(fieldName);
		InputFieldSelectionSetValue selectionSetValue = null;
		if (inputField != null) {
			try {
				selectionSetValue = inputFieldSelectionSetValueDao.findByFieldValue(inputField.getId(), value).iterator().next();
			} catch (NoSuchElementException e) {
			}
		}
		InputFieldSelectionSetValueInVO selectionSetValueIn = new InputFieldSelectionSetValueInVO();
		selectionSetValueIn.setId(selectionSetValue != null ? selectionSetValue.getId() : null);
		selectionSetValueIn.setVersion(selectionSetValue != null ? selectionSetValue.getVersion() : 0l);
		selectionSetValueIn.setFieldId(inputField != null ? inputField.getId() : null);
		selectionSetValueIn.setName(name);
		selectionSetValueIn.setLocalized(Boolean.parseBoolean(getLocalized(values)));
		selectionSetValueIn.setValue(value);
		selectionSetValueIn.setPreset(Boolean.parseBoolean(getPreset(values)));
		selectionSetValueIn.setInkRegions(CommonUtil.isEmptyString(getInkRegions(values)) ? null : CommonUtil.stringToInkValue(getInkRegions(values)));
		selectionSetValueIn.setStrokesId(CommonUtil.isEmptyString(getStrokesId(values)) ? null : getStrokesId(values));
		Set<InputFieldSelectionSetValueInVO> selectionSetValues;
		if (selectionSetValueMap.containsKey(fieldName)) {
			selectionSetValues = selectionSetValueMap.get(fieldName);
		} else {
			selectionSetValues = new LinkedHashSet<InputFieldSelectionSetValueInVO>();
			selectionSetValueMap.put(fieldName, selectionSetValues);
		}
		if (selectionSetValues.add(selectionSetValueIn)) {
			jobOutput.println("selection set value for field '" + fieldName + "' read: " + value);
			return 1;
		} else {
			jobOutput.println("selection set value for field '" + fieldName + "' SKIPPED: " + value);
			return 0;
		}
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getValue(values))) {
			// jobOutput.println("row " + rowNumber + ": empty value");
			return false;
		}
		return true;
	}
}
