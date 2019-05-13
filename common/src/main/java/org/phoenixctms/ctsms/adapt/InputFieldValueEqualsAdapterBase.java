package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.phoenixctms.ctsms.enumeration.InputFieldType;

public abstract class InputFieldValueEqualsAdapterBase<A, B> {

	protected abstract Boolean getABoolean(A a);

	protected abstract Date getADate(A a);

	protected abstract Float getAFloat(A a);

	protected abstract byte[] getAInk(A a);

	protected abstract Long getALong(A a);

	protected abstract ArrayList getASelectionSetSorted(A a);

	protected abstract String getAText(A a);

	protected abstract Date getATime(A a);

	protected abstract Date getATimestamp(A a);

	protected abstract Boolean getBBoolean(B b);

	protected abstract Date getBDate(B b);

	protected abstract Float getBFloat(B b);

	protected abstract byte[] getBInk(B b);

	protected abstract Long getBLong(B b);

	protected abstract ArrayList getBSelectionSetSorted(B b);

	protected abstract String getBText(B b);

	protected abstract Date getBTime(B b);

	protected abstract Date getBTimestamp(B b);

	protected boolean selectionSetValueEquals(Object a, Object b) {
		return a.equals(b);
	}

	public final boolean valueEquals(A a, B b) {
		return valueEquals(a, b, null, false, false);
	}

	public final boolean valueEquals(A a, B b, InputFieldType fieldType, boolean normalizeLineEndings, boolean normalizeNullStrings) {
		EqualsBuilder eb = new EqualsBuilder();
		if (fieldType == null ||
				InputFieldType.CHECKBOX.equals(fieldType)) {
			eb.append(getABoolean(a), getBBoolean(b));
		}
		if (fieldType == null ||
				InputFieldType.INTEGER.equals(fieldType)) {
			eb.append(getALong(a), getBLong(b));
		}
		if (fieldType == null ||
				InputFieldType.FLOAT.equals(fieldType)) {
			eb.append(getAFloat(a), getBFloat(b));
		}
		if (fieldType == null ||
				InputFieldType.DATE.equals(fieldType)) {
			eb.append(getADate(a), getBDate(b));
		}
		if (fieldType == null ||
				InputFieldType.TIME.equals(fieldType)) {
			eb.append(getATime(a), getBTime(b));
		}
		if (fieldType == null ||
				InputFieldType.TIMESTAMP.equals(fieldType)) {
			eb.append(getATimestamp(a), getBTimestamp(b));
		}
		if (fieldType == null ||
				InputFieldType.SINGLE_LINE_TEXT.equals(fieldType) ||
				InputFieldType.MULTI_LINE_TEXT.equals(fieldType) ||
				InputFieldType.AUTOCOMPLETE.equals(fieldType)) {
			String aString = getAText(a) == null ? (normalizeNullStrings ? "" : null) : getAText(a);
			String bString = getBText(b) == null ? (normalizeNullStrings ? "" : null) : getBText(b);
			if (normalizeLineEndings) {
				eb.append(aString == null ? null : aString.replaceAll("\\r\\n?", "\n"),
						bString == null ? null : bString.replaceAll("\\r\\n?", "\n"));
			} else {
				eb.append(aString, bString);
			}
		}
		if (!eb.isEquals()) {
			return false;
		}
		eb.reset();
		if (fieldType == null ||
				InputFieldType.SKETCH.equals(fieldType)) {
			eb.append(getAInk(a), getBInk(b)); // deep
		}
		if (!eb.isEquals()) {
			return false;
		}
		eb.reset();
		if (fieldType == null ||
				InputFieldType.SELECT_ONE_DROPDOWN.equals(fieldType) ||
				InputFieldType.SELECT_ONE_RADIO_H.equals(fieldType) ||
				InputFieldType.SELECT_ONE_RADIO_V.equals(fieldType) ||
				InputFieldType.SELECT_MANY_H.equals(fieldType) ||
				InputFieldType.SELECT_MANY_V.equals(fieldType)) {
			ArrayList aSelectionSet = getASelectionSetSorted(a);
			if (aSelectionSet == null) {
				aSelectionSet = new ArrayList();
			}
			ArrayList<Object> bSelectionSet = getBSelectionSetSorted(b);
			if (bSelectionSet == null) {
				bSelectionSet = new ArrayList();
			}
			if (aSelectionSet.size() == bSelectionSet.size()) {
				Iterator aSelectionSetIt = aSelectionSet.iterator();
				Iterator bSelectionSetIt = bSelectionSet.iterator();
				while (aSelectionSetIt.hasNext() && bSelectionSetIt.hasNext()) {
					Object aSelectionSetValue = aSelectionSetIt.next();
					Object bSelectionSetValue = bSelectionSetIt.next();
					if (aSelectionSetValue != null && bSelectionSetValue != null) {
						if (!selectionSetValueEquals(aSelectionSetValue, bSelectionSetValue)) {
							return false;
						}
					} else if (aSelectionSetValue != null && bSelectionSetValue == null) {
						return false;
					} else if (aSelectionSetValue == null && bSelectionSetValue != null) {
						return false;
					}
				}
				return aSelectionSetIt.hasNext() == bSelectionSetIt.hasNext();
			} else {
				return false;
			}
		}
		return true;
	}
}
