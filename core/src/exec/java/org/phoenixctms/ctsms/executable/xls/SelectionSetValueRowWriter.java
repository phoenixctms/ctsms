package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class SelectionSetValueRowWriter extends RowWriter {

	private final static String SHEET_NAME = "selectionsetvalues";
	private final static int FIELD_NAME_COLUMN_INDEX = 0;
	private final static int NAME_COLUMN_INDEX = 1;
	private final static int VALUE_COLUMN_INDEX = 2;
	private final static int LOCALIZED_COLUMN_INDEX = 3;
	private final static int PRESET_COLUMN_INDEX = 4;
	private final static int STROKES_ID_COLUMN_INDEX = 5;
	private final static int INK_REGIONS_COLUMN_INDEX = 6;
	private int fieldNameColumnIndex;
	private int nameColumnIndex;
	private int valueColumnIndex;
	private int localizedColumnIndex;
	private int presetColumnIndex;
	private int strokesIdColumnIndex;
	private int inkRegionsColumnIndex;
	private int maxColumnIndex;
	@Autowired
	protected InputFieldService inputFieldService;

	public SelectionSetValueRowWriter() {
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
		fieldNameColumnIndex = FIELD_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(fieldNameColumnIndex, maxColumnIndex);
		nameColumnIndex = NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(nameColumnIndex, maxColumnIndex);
		valueColumnIndex = VALUE_COLUMN_INDEX;
		maxColumnIndex = Math.max(valueColumnIndex, maxColumnIndex);
		localizedColumnIndex = LOCALIZED_COLUMN_INDEX;
		maxColumnIndex = Math.max(localizedColumnIndex, maxColumnIndex);
		presetColumnIndex = PRESET_COLUMN_INDEX;
		maxColumnIndex = Math.max(presetColumnIndex, maxColumnIndex);
		strokesIdColumnIndex = STROKES_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(strokesIdColumnIndex, maxColumnIndex);
		inkRegionsColumnIndex = INK_REGIONS_COLUMN_INDEX;
		maxColumnIndex = Math.max(inkRegionsColumnIndex, maxColumnIndex);
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(inputFieldService.getSelectionSetValueList(context.getAuth(), context.getEntityId(this), null, null));
	}

	public void printRows(Collection<InputFieldSelectionSetValueOutVO> selectionSetValues) throws Throwable {
		Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
			String[] values = new String[maxColumnIndex + 1];
			values[fieldNameColumnIndex] = selectionSetValue.getField().getNameL10nKey();
			values[nameColumnIndex] = selectionSetValue.getNameL10nKey();
			values[valueColumnIndex] = selectionSetValue.getValue();
			values[localizedColumnIndex] = Boolean.toString(selectionSetValue.getLocalized());
			values[presetColumnIndex] = Boolean.toString(selectionSetValue.getPreset());
			values[strokesIdColumnIndex] = selectionSetValue.getStrokesId();
			values[inkRegionsColumnIndex] = CommonUtil.inkValueToString(selectionSetValue.getInkRegions());
			printRow(values);
		}
	}
}
