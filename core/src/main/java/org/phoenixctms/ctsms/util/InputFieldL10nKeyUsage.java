package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.enumeration.InputFieldL10nTarget;

/**
 * A localization key as it is applied to a property: the input field's own texts and selection
 * set values (if localized), the titles and comments of its usages (if the input field is
 * localized), and eCRF sections and inquiry categories (always).
 */
public final class InputFieldL10nKeyUsage {

	private static void add(ArrayList<InputFieldL10nKeyUsage> result, InputFieldL10nBundle bundle, String key, InputFieldL10nTarget target,
			InputFieldSelectionSetValue selectionSetValue) {
		if (!CommonUtil.isEmptyString(key)) {
			result.add(new InputFieldL10nKeyUsage(bundle, key, target, selectionSetValue));
		}
	}

	/**
	 * The keys of an eCRF field: those of its input field, plus its title, comment and section.
	 */
	public static ArrayList<InputFieldL10nKeyUsage> getKeys(ECRFField ecrfField) {
		ArrayList<InputFieldL10nKeyUsage> result = getKeys(ecrfField.getField());
		if (ecrfField.getField().isLocalized()) {
			add(result, InputFieldL10nBundle.TITLES, ecrfField.getTitleL10nKey(), InputFieldL10nTarget.ECRF_FIELD_TITLE, null);
			add(result, InputFieldL10nBundle.COMMENTS, ecrfField.getCommentL10nKey(), InputFieldL10nTarget.ECRF_FIELD_COMMENT, null);
		}
		add(result, InputFieldL10nBundle.ECRF_FIELD_SECTIONS, ecrfField.getSection(), InputFieldL10nTarget.ECRF_FIELD_SECTION, null);
		return result;
	}

	/**
	 * The keys of an input field itself and of its selection set values.
	 */
	public static ArrayList<InputFieldL10nKeyUsage> getKeys(InputField field) {
		ArrayList<InputFieldL10nKeyUsage> result = new ArrayList<InputFieldL10nKeyUsage>();
		if (field.isLocalized()) {
			add(result, InputFieldL10nBundle.NAMES, field.getNameL10nKey(), InputFieldL10nTarget.NAME, null);
			add(result, InputFieldL10nBundle.TITLES, field.getTitleL10nKey(), InputFieldL10nTarget.TITLE, null);
			add(result, InputFieldL10nBundle.COMMENTS, field.getTopCommentL10nKey(), InputFieldL10nTarget.TOP_COMMENT, null);
			add(result, InputFieldL10nBundle.COMMENTS, field.getBottomCommentL10nKey(), InputFieldL10nTarget.BOTTOM_COMMENT, null);
			add(result, InputFieldL10nBundle.TEXT_PRESETS, field.getTextPresetL10nKey(), InputFieldL10nTarget.TEXT_PRESET, null);
			add(result, InputFieldL10nBundle.VALIDATION_ERROR_MSGS, field.getValidationErrorMsgL10nKey(), InputFieldL10nTarget.VALIDATION_ERROR_MSG, null);
		}
		Iterator<InputFieldSelectionSetValue> it = field.getSelectionSetValues().iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValue value = it.next();
			if (value.isLocalized()) {
				add(result, InputFieldL10nBundle.SELECTION_SET_VALUE_NAMES, value.getNameL10nKey(), InputFieldL10nTarget.SELECTION_SET_VALUE_NAME, value);
				add(result, InputFieldL10nBundle.SELECTION_SET_VALUE_ROWS, value.getRowL10nKey(), InputFieldL10nTarget.SELECTION_SET_VALUE_ROW, value);
				add(result, InputFieldL10nBundle.SELECTION_SET_VALUE_COLUMNS, value.getColumnL10nKey(), InputFieldL10nTarget.SELECTION_SET_VALUE_COLUMN, value);
			}
		}
		return result;
	}

	/**
	 * The keys of an inquiry's own properties (title, comment, category), without those of its input field.
	 */
	public static ArrayList<InputFieldL10nKeyUsage> getUsageKeys(Inquiry inquiry) {
		ArrayList<InputFieldL10nKeyUsage> result = new ArrayList<InputFieldL10nKeyUsage>();
		if (inquiry.getField().isLocalized()) {
			add(result, InputFieldL10nBundle.TITLES, inquiry.getTitleL10nKey(), InputFieldL10nTarget.INQUIRY_TITLE, null);
			add(result, InputFieldL10nBundle.COMMENTS, inquiry.getCommentL10nKey(), InputFieldL10nTarget.INQUIRY_COMMENT, null);
		}
		add(result, InputFieldL10nBundle.INQUIRY_CATEGORIES, inquiry.getCategory(), InputFieldL10nTarget.INQUIRY_CATEGORY, null);
		return result;
	}

	/**
	 * The keys of an eCRF field's own properties (title, comment, section), without those of its input field.
	 */
	public static ArrayList<InputFieldL10nKeyUsage> getUsageKeys(ECRFField ecrfField) {
		ArrayList<InputFieldL10nKeyUsage> result = getKeys(ecrfField);
		result.removeAll(getKeys(ecrfField.getField()));
		return result;
	}

	/**
	 * The keys of a proband list entry tag's own properties (title, comment), without those of its input field.
	 */
	public static ArrayList<InputFieldL10nKeyUsage> getUsageKeys(ProbandListEntryTag tag) {
		ArrayList<InputFieldL10nKeyUsage> result = new ArrayList<InputFieldL10nKeyUsage>();
		if (tag.getField().isLocalized()) {
			add(result, InputFieldL10nBundle.TITLES, tag.getTitleL10nKey(), InputFieldL10nTarget.PROBAND_LIST_ENTRY_TAG_TITLE, null);
			add(result, InputFieldL10nBundle.COMMENTS, tag.getCommentL10nKey(), InputFieldL10nTarget.PROBAND_LIST_ENTRY_TAG_COMMENT, null);
		}
		return result;
	}

	private final InputFieldL10nBundle bundle;
	private final String key;
	private final InputFieldL10nTarget target;
	private final InputFieldSelectionSetValue selectionSetValue;

	private InputFieldL10nKeyUsage(InputFieldL10nBundle bundle, String key, InputFieldL10nTarget target, InputFieldSelectionSetValue selectionSetValue) {
		this.bundle = bundle;
		this.key = key;
		this.target = target;
		this.selectionSetValue = selectionSetValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InputFieldL10nKeyUsage)) {
			return false;
		}
		InputFieldL10nKeyUsage other = (InputFieldL10nKeyUsage) obj;
		return bundle == other.bundle && key.equals(other.key) && target == other.target
				&& (selectionSetValue == null ? other.selectionSetValue == null : selectionSetValue.equals(other.selectionSetValue));
	}

	public InputFieldL10nBundle getBundle() {
		return bundle;
	}

	public String getKey() {
		return key;
	}

	public InputFieldSelectionSetValue getSelectionSetValue() {
		return selectionSetValue;
	}

	public InputFieldL10nTarget getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		return (bundle.hashCode() * 31 + key.hashCode()) * 31 + target.hashCode();
	}
}
