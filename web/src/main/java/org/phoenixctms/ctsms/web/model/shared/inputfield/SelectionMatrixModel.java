package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;
import java.util.List;

import org.phoenixctms.ctsms.util.SelectionMatrix;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

/**
 * Grid of a select matrix input field for rendering. The rows are always the
 * option groups. Without transposition, the head shows the column labels and
 * each line is one option group. With transposition, the head shows the row
 * labels and each option group is rendered as a vertical column.
 */
public class SelectionMatrixModel {

	private final SelectionMatrix matrix;
	private final boolean transposed;
	private final ArrayList<SelectionMatrixRowModel> rows;

	public SelectionMatrixModel(InputModel inputModel, SelectionMatrix matrix, boolean transposed) {
		this.matrix = matrix;
		this.transposed = transposed;
		rows = new ArrayList<SelectionMatrixRowModel>(matrix.getRowCount());
		for (int r = 0; r < matrix.getRowCount(); r++) {
			rows.add(new SelectionMatrixRowModel(inputModel, r, matrix.getRowLabels().get(r), matrix.getRow(r)));
		}
	}

	public List<String> getColumnLabels() {
		return matrix.getColumnLabels();
	}

	public List<String> getHeadLabels() {
		return transposed ? matrix.getRowLabels() : matrix.getColumnLabels();
	}

	public List<SelectionMatrixRowModel> getRows() {
		return rows;
	}

	public int getRowCount() {
		return rows.size();
	}

	public boolean isTransposed() {
		return transposed;
	}

	/**
	 * With many columns, the head labels are rendered vertically so the columns stay narrow.
	 */
	public boolean isVerticalHead() {
		return getHeadLabels().size() > Settings.getInt(SettingCodes.INPUT_MODEL_SELECT_MATRIX_VERTICAL_HEAD_COUNT, Bundle.SETTINGS,
				DefaultSettings.INPUT_MODEL_SELECT_MATRIX_VERTICAL_HEAD_COUNT);
	}
}
