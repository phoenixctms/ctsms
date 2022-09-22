package org.phoenixctms.ctsms.test;

import java.util.Date;

import org.phoenixctms.ctsms.TestDataProvider;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.enumeration.CriterionRestriction;
import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.vo.CriterionInVO;

public final class SearchCriterion {

	private CriterionTie junction;
	private String property;
	private CriterionRestriction operator;
	private boolean booleanValue;
	private Date dateValue;
	private Float floatValue;
	private Long longValue;
	private String stringValue;

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator) {
		this.junction = junction;
		this.property = property;
		this.operator = operator;
		booleanValue = false;
		dateValue = null;
		floatValue = null;
		longValue = null;
		stringValue = null;
	}

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, boolean value) {
		this(junction, property, operator);
		booleanValue = value;
	}

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Date value) {
		this(junction, property, operator);
		dateValue = value;
	}

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Float value) {
		this(junction, property, operator);
		floatValue = value;
	}

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Long value) {
		this(junction, property, operator);
		longValue = value;
	}

	public SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, String value) {
		this(junction, property, operator);
		stringValue = value;
	}

	public CriterionInVO buildCriterionInVO(TestDataProvider d, DBModule module, int position) {
		CriterionInVO newCriterion = new CriterionInVO();
		org.phoenixctms.ctsms.domain.CriterionTie t = d.getCriterionTie(junction);
		newCriterion.setTieId(t != null ? t.getId() : null);
		CriterionProperty p = d.getCriterionProperty(property, module);
		newCriterion.setPropertyId(p != null ? p.getId() : null);
		org.phoenixctms.ctsms.domain.CriterionRestriction r = d.getCriterionRestriction(operator);
		newCriterion.setRestrictionId(r != null ? r.getId() : null);
		newCriterion.setPosition((long) position);
		newCriterion.setBooleanValue(booleanValue);
		newCriterion.setFloatValue(floatValue);
		newCriterion.setLongValue(longValue);
		newCriterion.setStringValue(stringValue);
		if (p != null && (p.getValueType() == CriterionValueType.DATE || p.getValueType() == CriterionValueType.DATE_HASH)) {
			newCriterion.setDateValue(dateValue);
			newCriterion.setTimeValue(null);
			newCriterion.setTimestampValue(null);
		} else if (p != null && (p.getValueType() == CriterionValueType.TIME || p.getValueType() == CriterionValueType.TIME_HASH)) {
			newCriterion.setDateValue(null);
			newCriterion.setTimeValue(dateValue);
			newCriterion.setTimestampValue(null);
		} else if (p != null && (p.getValueType() == CriterionValueType.TIMESTAMP || p.getValueType() == CriterionValueType.TIMESTAMP_HASH)) {
			newCriterion.setDateValue(null);
			newCriterion.setTimeValue(null);
			newCriterion.setTimestampValue(dateValue);
		} else {
			newCriterion.setDateValue(null);
			newCriterion.setTimeValue(null);
			newCriterion.setTimestampValue(null);
		}
		return newCriterion;
	}
}
