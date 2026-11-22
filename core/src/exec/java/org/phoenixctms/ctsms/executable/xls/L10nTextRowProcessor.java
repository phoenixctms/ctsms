package org.phoenixctms.ctsms.executable.xls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.InputFieldL10nTextUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.vo.InputFieldL10nTextInVO;
import org.phoenixctms.ctsms.vo.InputFieldL10nTextOutVO;

/**
 * Imports one sheet of localization texts (see {@link L10nTextRowWriter}): the non-empty cells of each
 * language column are merged into the text of that language, like an editor save; empty cells leave the
 * text of a key as it is, and nothing is removed. A cell is a change only if it differs from the text the key
 * has in effect (as exported: classpath, CTSMS_PROPERTIES overloads, database texts, with the fallback to the
 * default texts), so re-importing an export does not copy the .properties texts into the database texts.
 */
public class L10nTextRowProcessor extends RowProcessor {

	private final static int KEY_COLUMN_INDEX = 0;
	private final InputFieldL10nBundle bundle;
	private final InputFieldService inputFieldService;
	private ArrayList<String> columnLocales; // null entries for skipped columns
	private LinkedHashMap<String, LinkedHashMap<String, String>> texts; // locale -> key -> text

	public L10nTextRowProcessor(InputFieldL10nBundle bundle, InputFieldService inputFieldService) {
		super();
		this.bundle = bundle;
		this.inputFieldService = inputFieldService;
		acceptCommentsIndex = KEY_COLUMN_INDEX; // texts may contain the comment character
	}

	@Override
	public String getSheetName() {
		return L10nTextRowWriter.getSheetName(bundle);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		columnLocales = null;
		texts = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	}

	@Override
	protected int lineHashCode(String[] values) {
		return getColumnValue(values, KEY_COLUMN_INDEX).hashCode();
	}

	@Override
	public void postProcess() throws Exception {
		Iterator<Entry<String, LinkedHashMap<String, String>>> it = texts.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, LinkedHashMap<String, String>> localeTexts = it.next();
			String locale = localeTexts.getKey();
			InputFieldL10nTextOutVO current = inputFieldService.getInputFieldL10nText(context.getAuth(), bundle, locale);
			String currentText = current != null ? current.getText() : "";
			Map<String, String> currentEntries = InputFieldL10nTextUtil.parse(currentText);
			ResourceBundle effective = L10nUtil.getInputFieldL10nBundle(bundle, locale);
			LinkedHashMap<String, InputFieldL10nTextUtil.Change> changes = new LinkedHashMap<String, InputFieldL10nTextUtil.Change>();
			Iterator<Entry<String, String>> textIt = localeTexts.getValue().entrySet().iterator();
			while (textIt.hasNext()) {
				Entry<String, String> text = textIt.next();
				String effectiveValue = effective.containsKey(text.getKey()) ? effective.getString(text.getKey()) : null;
				if (!text.getValue().equals(effectiveValue)) {
					changes.put(text.getKey(), new InputFieldL10nTextUtil.Change(currentEntries.get(text.getKey()), text.getValue()));
				}
			}
			String textName = getSheetName() + (CommonUtil.isEmptyString(locale) ? "" : " " + locale);
			if (changes.size() > 0) {
				InputFieldL10nTextInVO in = new InputFieldL10nTextInVO();
				in.setBundle(bundle);
				in.setLocale(locale);
				in.setOriginalText(currentText);
				in.setText(InputFieldL10nTextUtil.apply(currentText, changes));
				in.setVersion(current != null ? current.getVersion() : null);
				inputFieldService.setInputFieldL10nText(context.getAuth(), in);
				jobOutput.println(textName + ": " + changes.size() + " texts updated");
			} else {
				jobOutput.println(textName + ": no texts changed");
			}
		}
	}

	@Override
	protected boolean processHeaderRow(String[] values) throws Throwable {
		// English is no language of its own (its texts are the default ones), so an "en" column is skipped:
		HashSet<String> supportedLocales = new HashSet<String>(L10nUtil.getInputFieldL10nLocales());
		columnLocales = new ArrayList<String>(values.length);
		for (int i = 0; i < values.length; i++) {
			String header = values[i] != null ? values[i].trim() : "";
			if (i == KEY_COLUMN_INDEX) {
				columnLocales.add(null);
			} else if (L10nTextRowWriter.DEFAULT_LOCALE_HEADER.equals(header)) {
				columnLocales.add("");
			} else if (supportedLocales.contains(header)) {
				columnLocales.add(header);
			} else {
				columnLocales.add(null);
				if (header.length() > 0) {
					jobOutput.println(getSheetName() + ": language column '" + header + "' not supported, skipping");
				}
			}
		}
		return true;
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String key = getColumnValue(values, KEY_COLUMN_INDEX);
		int count = 0;
		for (int i = KEY_COLUMN_INDEX + 1; i < values.length && i < columnLocales.size(); i++) {
			String locale = columnLocales.get(i);
			String text = values[i];
			if (locale != null && !CommonUtil.isEmptyString(text)) {
				LinkedHashMap<String, String> localeTexts = texts.get(locale);
				if (localeTexts == null) {
					localeTexts = new LinkedHashMap<String, String>();
					texts.put(locale, localeTexts);
				}
				localeTexts.put(key, text);
				count = 1;
			}
		}
		return count;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		// rows without a key (blank or commented out) are skipped:
		return !CommonUtil.isEmptyString(getColumnValue(values, KEY_COLUMN_INDEX));
	}
}
