package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class VariablePeriodSelector {

	private VariablePeriodSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> variablePeriods;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public VariablePeriodSelector(VariablePeriodSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		variablePeriods = WebUtil.getVariablePeriods();
	}

	public VariablePeriodSelectorListener getBean() {
		return bean;
	}

	public String getPeriod() {
		if (bean != null) {
			VariablePeriod period = bean.getPeriod(property);
			if (period != null) {
				return period.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getVariablePeriods() {
		return variablePeriods;
	}

	public void setBean(VariablePeriodSelectorListener bean) {
		this.bean = bean;
	}

	public void setPeriod(String name) {
		if (bean != null) {
			VariablePeriod period;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					period = VariablePeriod.fromString(name);
				} catch (Exception e) {
					period = VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
				}
			} else {
				period = VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
			}
			bean.setPeriod(property, period);
		}
	}
}
