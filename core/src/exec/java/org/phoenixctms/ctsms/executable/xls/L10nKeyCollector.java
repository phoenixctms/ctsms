package org.phoenixctms.ctsms.executable.xls;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;

/**
 * The localization keys used by the rows of a setup export, per bundle: the keys of localized input fields
 * and selection set values, the titles and comments of usages of localized input fields, and eCRF sections
 * and inquiry categories.
 */
public class L10nKeyCollector {

	private final TreeMap<InputFieldL10nBundle, TreeSet<String>> keys = new TreeMap<InputFieldL10nBundle, TreeSet<String>>();

	private void add(InputFieldL10nBundle bundle, String key) {
		if (!CommonUtil.isEmptyString(key)) {
			TreeSet<String> bundleKeys = keys.get(bundle);
			if (bundleKeys == null) {
				bundleKeys = new TreeSet<String>();
				keys.put(bundle, bundleKeys);
			}
			bundleKeys.add(key);
		}
	}

	public void addEcrfField(ECRFFieldOutVO ecrfField) {
		if (ecrfField.getField() != null && ecrfField.getField().getLocalized()) {
			add(InputFieldL10nBundle.TITLES, ecrfField.getTitleL10nKey());
			add(InputFieldL10nBundle.COMMENTS, ecrfField.getCommentL10nKey());
		}
		add(InputFieldL10nBundle.ECRF_FIELD_SECTIONS, ecrfField.getSection());
	}

	public void addInputField(InputFieldOutVO inputField) {
		if (inputField.getLocalized()) {
			add(InputFieldL10nBundle.NAMES, inputField.getNameL10nKey());
			add(InputFieldL10nBundle.TITLES, inputField.getTitleL10nKey());
			add(InputFieldL10nBundle.COMMENTS, inputField.getTopCommentL10nKey());
			add(InputFieldL10nBundle.COMMENTS, inputField.getBottomCommentL10nKey());
			add(InputFieldL10nBundle.TEXT_PRESETS, inputField.getTextPresetL10nKey());
			add(InputFieldL10nBundle.VALIDATION_ERROR_MSGS, inputField.getValidationErrorMsgL10nKey());
		}
		Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
		while (it.hasNext()) {
			addSelectionSetValue(it.next());
		}
	}

	public void addInquiry(InquiryOutVO inquiry) {
		if (inquiry.getField() != null && inquiry.getField().getLocalized()) {
			add(InputFieldL10nBundle.TITLES, inquiry.getTitleL10nKey());
			add(InputFieldL10nBundle.COMMENTS, inquiry.getCommentL10nKey());
		}
		add(InputFieldL10nBundle.INQUIRY_CATEGORIES, inquiry.getCategory());
	}

	public void addProbandListEntryTag(ProbandListEntryTagOutVO tag) {
		if (tag.getField() != null && tag.getField().getLocalized()) {
			add(InputFieldL10nBundle.TITLES, tag.getTitleL10nKey());
			add(InputFieldL10nBundle.COMMENTS, tag.getCommentL10nKey());
		}
	}

	public void addSelectionSetValue(InputFieldSelectionSetValueOutVO value) {
		if (value.getLocalized()) {
			add(InputFieldL10nBundle.SELECTION_SET_VALUE_NAMES, value.getNameL10nKey());
			add(InputFieldL10nBundle.SELECTION_SET_VALUE_ROWS, value.getRowL10nKey());
			add(InputFieldL10nBundle.SELECTION_SET_VALUE_COLUMNS, value.getColumnL10nKey());
		}
	}

	public void clear() {
		keys.clear();
	}

	public TreeMap<InputFieldL10nBundle, TreeSet<String>> getKeys() {
		return keys;
	}
}
