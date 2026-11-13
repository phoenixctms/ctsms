package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

/**
 * Arranges the selection set values of a select matrix input field in a grid.
 * Rows and columns appear in the order their labels first occur in the given
 * (sorted) selection set values. A row/column pair without a selection set
 * value is an empty (null) cell. Rows are the option groups, regardless of
 * the field's transposed flag, which only affects rendering.
 */
public class SelectionMatrix {

	private static String getRowKey(InputFieldSelectionSetValueOutVO selectionSetValue) {
		return selectionSetValue.getRowL10nKey() != null ? selectionSetValue.getRowL10nKey() : selectionSetValue.getRow();
	}

	private static String getColumnKey(InputFieldSelectionSetValueOutVO selectionSetValue) {
		return selectionSetValue.getColumnL10nKey() != null ? selectionSetValue.getColumnL10nKey() : selectionSetValue.getColumn();
	}

	private final ArrayList<String> rowLabels;
	private final ArrayList<String> columnLabels;
	private final ArrayList<ArrayList<InputFieldSelectionSetValueOutVO>> cells;

	public SelectionMatrix(Collection<InputFieldSelectionSetValueOutVO> selectionSetValues) {
		this(selectionSetValues, InputFieldSelectionSetValueOutVO::getRow, InputFieldSelectionSetValueOutVO::getColumn);
	}

	/**
	 * @param rowLabel
	 *            gives the label of a selection set value's row, e.g. translated to a PDF's language
	 * @param columnLabel
	 *            gives the label of a selection set value's column
	 */
	public SelectionMatrix(Collection<InputFieldSelectionSetValueOutVO> selectionSetValues, Function<InputFieldSelectionSetValueOutVO, String> rowLabel,
			Function<InputFieldSelectionSetValueOutVO, String> columnLabel) {
		LinkedHashMap<String, Integer> rowIndexes = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> columnIndexes = new LinkedHashMap<String, Integer>();
		rowLabels = new ArrayList<String>();
		columnLabels = new ArrayList<String>();
		if (selectionSetValues != null) {
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
				String rowKey = getRowKey(selectionSetValue);
				if (!rowIndexes.containsKey(rowKey)) {
					rowIndexes.put(rowKey, rowLabels.size());
					rowLabels.add(rowLabel.apply(selectionSetValue));
				}
				String columnKey = getColumnKey(selectionSetValue);
				if (!columnIndexes.containsKey(columnKey)) {
					columnIndexes.put(columnKey, columnLabels.size());
					columnLabels.add(columnLabel.apply(selectionSetValue));
				}
			}
		}
		cells = new ArrayList<ArrayList<InputFieldSelectionSetValueOutVO>>(rowLabels.size());
		for (int r = 0; r < rowLabels.size(); r++) {
			cells.add(new ArrayList<InputFieldSelectionSetValueOutVO>(Collections.nCopies(columnLabels.size(), (InputFieldSelectionSetValueOutVO) null)));
		}
		if (selectionSetValues != null) {
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
				ArrayList<InputFieldSelectionSetValueOutVO> row = cells.get(rowIndexes.get(getRowKey(selectionSetValue)));
				int columnIndex = columnIndexes.get(getColumnKey(selectionSetValue));
				if (row.get(columnIndex) == null) {
					row.set(columnIndex, selectionSetValue);
				}
			}
		}
	}

	public InputFieldSelectionSetValueOutVO getCell(int rowIndex, int columnIndex) {
		return cells.get(rowIndex).get(columnIndex);
	}

	public int getColumnCount() {
		return columnLabels.size();
	}

	public List<String> getColumnLabels() {
		return Collections.unmodifiableList(columnLabels);
	}

	public List<InputFieldSelectionSetValueOutVO> getRow(int rowIndex) {
		return Collections.unmodifiableList(cells.get(rowIndex));
	}

	public int getRowCount() {
		return rowLabels.size();
	}

	public List<String> getRowLabels() {
		return Collections.unmodifiableList(rowLabels);
	}
}
