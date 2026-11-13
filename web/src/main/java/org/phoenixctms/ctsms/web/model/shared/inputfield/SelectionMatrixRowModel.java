package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

/**
 * One option group (data row) of a select matrix input field. It reads and writes
 * only the selection set value ids of its own row in the input model's selection.
 */
public class SelectionMatrixRowModel {

	private final InputModel inputModel;
	private final int index;
	private final String label;
	private final ArrayList<SelectItem> items;
	private final HashSet<String> rowIds;

	public SelectionMatrixRowModel(InputModel inputModel, int index, String label, List<InputFieldSelectionSetValueOutVO> cells) {
		this.inputModel = inputModel;
		this.index = index;
		this.label = label;
		items = new ArrayList<SelectItem>(cells.size());
		rowIds = new HashSet<String>(cells.size());
		Iterator<InputFieldSelectionSetValueOutVO> it = cells.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO cell = it.next();
			if (cell != null) {
				String id = Long.toString(cell.getId());
				rowIds.add(id);
				items.add(new SelectItem(id, "", cell.getName()));
			} else {
				items.add(new SelectItem(CommonUtil.NO_SELECTION_VALUE, "", null, true));
			}
		}
	}

	public int getIndex() {
		return index;
	}

	public List<SelectItem> getItems() {
		return items;
	}

	public String getLabel() {
		return label;
	}

	public Long getSelectionValueId() {
		List<String> ids = getSelectionValueIds();
		return ids.size() > 0 ? WebUtil.stringToLong(ids.get(0)) : null;
	}

	public List<String> getSelectionValueIds() {
		ArrayList<String> result = new ArrayList<String>();
		List<String> ids = inputModel.getSelectionValueIds();
		if (ids != null) {
			Iterator<String> it = ids.iterator();
			while (it.hasNext()) {
				String id = it.next();
				if (rowIds.contains(id)) {
					result.add(id);
				}
			}
		}
		return result;
	}

	public void setSelectionValueId(Long value) {
		ArrayList<String> ids = new ArrayList<String>(1);
		if (value != null) {
			ids.add(value.toString());
		}
		setSelectionValueIds(ids);
	}

	public void setSelectionValueIds(List<String> value) {
		ArrayList<String> result = new ArrayList<String>();
		HashSet<String> dupeCheck = new HashSet<String>();
		List<String> ids = inputModel.getSelectionValueIds();
		if (ids != null) {
			addIds(result, dupeCheck, ids, false);
		}
		if (value != null) {
			addIds(result, dupeCheck, value, true);
		}
		inputModel.setSelectionValueIds(result);
	}

	private void addIds(ArrayList<String> result, HashSet<String> dupeCheck, Collection<String> ids, boolean ownRow) {
		Iterator<String> it = ids.iterator();
		while (it.hasNext()) {
			String id = it.next();
			if (id != null && rowIds.contains(id) == ownRow && dupeCheck.add(id)) {
				result.add(id);
			}
		}
	}
}
