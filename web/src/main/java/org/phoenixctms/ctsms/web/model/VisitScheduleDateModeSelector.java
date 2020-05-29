package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class VisitScheduleDateModeSelector {

	private VisitScheduleDateModeSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> visitScheduleDateModes;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public VisitScheduleDateModeSelector(VisitScheduleDateModeSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		visitScheduleDateModes = WebUtil.getVisitScheduleDateModes();
	}

	public VisitScheduleDateModeSelectorListener getBean() {
		return bean;
	}

	public String getVisitScheduleDateMode() {
		if (bean != null) {
			VisitScheduleDateMode visitScheduleDateMode = bean.getVisitScheduleDateMode(property);
			if (visitScheduleDateMode != null) {
				return visitScheduleDateMode.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getVisitScheduleDateModes() {
		return visitScheduleDateModes;
	}

	public void setBean(VisitScheduleDateModeSelectorListener bean) {
		this.bean = bean;
	}

	public void setVisitScheduleDateMode(String name) {
		if (bean != null) {
			VisitScheduleDateMode visitScheduleDateMode;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					visitScheduleDateMode = VisitScheduleDateMode.fromString(name);
				} catch (Exception e) {
					visitScheduleDateMode = VisitScheduleDateModeSelectorListener.NO_SELECTION_VISIT_SCHEDULE_DATE_MODE;
				}
			} else {
				visitScheduleDateMode = VisitScheduleDateModeSelectorListener.NO_SELECTION_VISIT_SCHEDULE_DATE_MODE;
			}
			bean.setVisitScheduleDateMode(property, visitScheduleDateMode);
		}
	}
}
