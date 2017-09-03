package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EventImportanceSelector {

	private EventImportanceSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> importances;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public EventImportanceSelector(EventImportanceSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		importances = WebUtil.getEventImportances();
	}

	public EventImportanceSelectorListener getBean() {
		return bean;
	}

	public ArrayList<SelectItem> getEventImportances() {
		return importances;
	}

	public String getImportance() {
		if (bean != null) {
			EventImportance importance = bean.getImportance(property);
			if (importance != null) {
				return importance.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public void setBean(EventImportanceSelectorListener bean) {
		this.bean = bean;
	}

	public void setImportance(String name) {
		if (bean != null) {
			EventImportance importance;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					importance = EventImportance.fromString(name);
				} catch (Exception e) {
					importance = EventImportanceSelectorListener.NO_SELECTION_EVENT_IMPORTANCE;
				}
			} else {
				importance = EventImportanceSelectorListener.NO_SELECTION_EVENT_IMPORTANCE;
			}
			bean.setImportance(property, importance);
		}
	}
}
