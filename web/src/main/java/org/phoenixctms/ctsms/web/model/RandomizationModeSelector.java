package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class RandomizationModeSelector {

	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;
	private RandomizationModeSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> modes;

	public RandomizationModeSelector(RandomizationModeSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		modes = WebUtil.getRandomizationModes();
	}

	public RandomizationModeSelectorListener getBean() {
		return bean;
	}

	public String getRandomizationMode() {
		if (bean != null) {
			RandomizationMode mode = bean.getRandomizationMode(property);
			if (mode != null) {
				return mode.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getRandomizationModes() {
		return modes;
	}

	public void setBean(RandomizationModeSelectorListener bean) {
		this.bean = bean;
	}

	public void setRandomizationMode(String name) {
		if (bean != null) {
			RandomizationMode mode;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					mode = RandomizationMode.fromString(name);
				} catch (Exception e) {
					mode = RandomizationModeSelectorListener.NO_SELECTION_RANDOMIZATION_MODE;
				}
			} else {
				mode = RandomizationModeSelectorListener.NO_SELECTION_RANDOMIZATION_MODE;
			}
			bean.setRandomizationMode(property, mode);
		}
	}
}
