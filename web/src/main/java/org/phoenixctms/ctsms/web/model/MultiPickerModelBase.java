package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class MultiPickerModelBase<VO> {

	private HashMap<Long, IDVO> idvoMap;
	private ArrayList<IDVO> selection;
	private HashMap<Long, Integer> indexMap;
	private Long lastPickedId;
	private String lastPickedIds;

	protected MultiPickerModelBase() {
		idvoMap = new HashMap<Long, IDVO>();
		selection = new ArrayList<IDVO>();
		indexMap = new HashMap<Long, Integer>();
		lastPickedId = null;
		lastPickedIds = null;
	}

	public void addId(Long id) {
		if (id != null) {
			if (!idvoMap.containsKey(id)) {
				VO vo = loadSelectionElement(id);
				if (vo != null) {
					IDVO idvo = new IDVO(vo);
					idvo.setRowIndex(WebUtil.FACES_INITIAL_ROW_INDEX + selection.size());
					idvoMap.put(id, idvo);
					indexMap.put(id, selection.size());
					selection.add(idvo);
				} else {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.ID_NOT_PICKED, Long.toString(id));
				}
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.ID_ALREADY_PICKED, Long.toString(id));
			}
		}
	}

	public void addIds(String idsString) {
		if (idsString != null && idsString.length() > 0) {
			String[] ids = WebUtil.ID_SEPARATOR_REGEXP.split(idsString, -1);
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].length() > 0) {
					addId(WebUtil.stringToLong(ids[i]));
				}
			}
		}
	}

	public void clear() {
		idvoMap.clear();
		indexMap.clear();
		selection.clear();
		lastPickedId = null;
		lastPickedIds = null;
	}

	public int getCount() {
		return idvoMap.size();
	}

	public Long getId() {
		return lastPickedId;
	}

	public String getIds() {
		return lastPickedIds;
	}

	public boolean getIsEnabled() {
		return idvoMap.size() > 0;
	}

	public ArrayList<IDVO> getSelection() {
		return selection;
	}

	public LinkedHashSet<Long> getSelectionIds() {
		LinkedHashSet<Long> result = new LinkedHashSet<Long>();
		for (int i = 0; i < selection.size(); i++) {
			result.add(selection.get(i).getId());
		}
		return result;
	}

	public boolean isDownEnabled(Long id) {
		if (idvoMap.containsKey(id)) {
			if (indexMap.get(id) > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isUpEnabled(Long id) {
		if (idvoMap.containsKey(id)) {
			if (indexMap.get(id) < indexMap.size() - 1) {
				return true;
			}
		}
		return false;
	}

	protected abstract VO loadSelectionElement(Long id);

	public void moveDown(Long id) {
		if (idvoMap.containsKey(id)) {
			int i = indexMap.get(id);
			if (i > 0) {
				IDVO element = selection.set(i, selection.get(i - 1));
				IDVO previousElement = selection.set(i - 1, element);
				indexMap.put(element.getId(), i - 1);
				element.setRowIndex(element.getRowIndex() - 1);
				indexMap.put(previousElement.getId(), i);
				previousElement.setRowIndex(previousElement.getRowIndex() + 1);
			}
		}
	}

	public void moveUp(Long id) {
		if (idvoMap.containsKey(id)) {
			int i = indexMap.get(id);
			if (i < indexMap.size() - 1) {
				IDVO element = selection.set(i, selection.get(i + 1));
				IDVO nextElement = selection.set(i + 1, element);
				indexMap.put(element.getId(), i + 1);
				element.setRowIndex(element.getRowIndex() + 1);
				indexMap.put(nextElement.getId(), i);
				nextElement.setRowIndex(nextElement.getRowIndex() - 1);
			}
		}
	}

	public void removeId(Long id) {
		if (idvoMap.containsKey(id)) {
			selection.remove(idvoMap.get(id));
			updateIndexMap(indexMap.remove(id));
			idvoMap.remove(id);
		}
	}

	public void setId(Long id) {
		lastPickedId = id;
	}

	public void setIds(Collection<Long> ids) {
		clear();
		if (ids != null && ids.size() > 0) {
			Iterator<Long> it = ids.iterator();
			while (it.hasNext()) {
				addId(it.next());
			}
		}
	}

	public void setIds(String ids) {
		lastPickedIds = ids;
	}

	private void updateIndexMap(int start) {
		for (int i = start; i < selection.size(); i++) {
			IDVO element = selection.get(i);
			indexMap.put(element.getId(), i);
			element.setRowIndex(WebUtil.FACES_INITIAL_ROW_INDEX + i);
		}
	}
}
