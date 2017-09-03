package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class SexSelector {

	private SexSelectorListener bean;
	private int property;
	private ArrayList<SelectItem> sexes;
	private final static String NO_SELECTION_STRING = CommonUtil.NO_SELECTION_VALUE;

	public SexSelector(SexSelectorListener bean, int property) {
		this.bean = bean;
		this.property = property;
		sexes = WebUtil.getSexes();
	}

	public SexSelectorListener getBean() {
		return bean;
	}

	public String getSex() {
		if (bean != null) {
			Sex sex = bean.getSex(property);
			if (sex != null) {
				return sex.name();
			} else {
				return NO_SELECTION_STRING;
			}
		}
		return NO_SELECTION_STRING;
	}

	public ArrayList<SelectItem> getSexes() {
		return sexes;
	}

	public void setBean(SexSelectorListener bean) {
		this.bean = bean;
	}

	public void setSex(String name) {
		if (bean != null) {
			Sex sex;
			if (name != null && !name.equals(NO_SELECTION_STRING)) {
				try {
					sex = Sex.fromString(name);
				} catch (Exception e) {
					sex = SexSelectorListener.NO_SELECTION_SEX;
				}
			} else {
				sex = SexSelectorListener.NO_SELECTION_SEX;
			}
			bean.setSex(property, sex);
		}
	}
}
