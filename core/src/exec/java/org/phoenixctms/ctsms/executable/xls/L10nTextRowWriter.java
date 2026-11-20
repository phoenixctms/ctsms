package org.phoenixctms.ctsms.executable.xls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.fileprocessors.xls.RowWriter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;

/**
 * One sheet of localization texts for a setup export: the keys used by the exported rows, with their
 * effective texts (.properties files and database) of the default (root) bundle and of each supported language.
 * A language column holds a text only if it differs from the default text.
 */
public class L10nTextRowWriter extends RowWriter {

	public final static String SHEET_NAME_PREFIX = "l10n_";
	public final static String KEY_HEADER = "key";
	public final static String DEFAULT_LOCALE_HEADER = "default";

	public static String getSheetName(InputFieldL10nBundle bundle) {
		return SHEET_NAME_PREFIX + bundle.name().toLowerCase().replace("_", ""); // at most 31 characters
	}

	private final InputFieldL10nBundle bundle;
	private final Collection<String> keys;
	private ArrayList<String> locales;

	public L10nTextRowWriter(InputFieldL10nBundle bundle, Collection<String> keys) {
		super();
		this.bundle = bundle;
		this.keys = keys;
	}

	@Override
	public String getSheetName() {
		return getSheetName(bundle);
	}

	@Override
	public int getSheetNum() {
		// appended after the sheets of the setup rows:
		return context.getWorkbook().getNumberOfSheets();
	}

	private static String getText(ResourceBundle resourceBundle, String key) {
		return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : null;
	}

	@Override
	public void init() {
		super.init();
		locales = new ArrayList<String>();
		Iterator<Locale> it = CoreUtil.getSupportedLocales().iterator();
		while (it.hasNext()) {
			locales.add(CommonUtil.localeToString(it.next()));
		}
		context.getSpreadSheet(this);
	}

	@Override
	public void printHeaderRow() throws Exception {
		String[] values = new String[locales.size() + 2];
		values[0] = KEY_HEADER;
		values[1] = DEFAULT_LOCALE_HEADER;
		for (int i = 0; i < locales.size(); i++) {
			values[i + 2] = locales.get(i);
		}
		printRow(values, true);
	}

	@Override
	public void printRows() throws Throwable {
		ResourceBundle root = L10nUtil.getInputFieldL10nBundle(bundle, null);
		ArrayList<ResourceBundle> resourceBundles = new ArrayList<ResourceBundle>(locales.size());
		for (int i = 0; i < locales.size(); i++) {
			resourceBundles.add(L10nUtil.getInputFieldL10nBundle(bundle, locales.get(i)));
		}
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String[] values = new String[locales.size() + 2];
			values[0] = key;
			String rootText = getText(root, key);
			values[1] = rootText;
			for (int i = 0; i < locales.size(); i++) {
				String text = getText(resourceBundles.get(i), key);
				values[i + 2] = (text != null && !text.equals(rootText)) ? text : null;
			}
			printRow(values);
		}
	}
}
