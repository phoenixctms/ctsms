package org.phoenixctms.ctsms.util;

import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.phoenixctms.ctsms.domain.InputFieldL10nText;
import org.phoenixctms.ctsms.domain.InputFieldL10nTextDao;
import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Puts the latest database revisions of the editable localization texts on top of the
 * .properties bundles (see {@link BundleControl}).
 *
 * The texts are kept in memory, so loading a bundle never touches the database: the snapshot is
 * read once on startup, and each save pushes its new text afterwards.
 */
public class InputFieldL10nOverlay implements BundleOverlayProvider {

	private final static String KEY_SEPARATOR = "\u0000";
	private static InputFieldL10nOverlay instance = null;
	private final ConcurrentHashMap<String, String> texts = new ConcurrentHashMap<String, String>();
	private InputFieldL10nTextDao inputFieldL10nTextDao;
	private PlatformTransactionManager transactionManager;

	private static String getKey(InputFieldL10nBundle bundle, String locale) {
		return bundle.name() + KEY_SEPARATOR + (locale == null ? "" : locale);
	}

	public static InputFieldL10nOverlay getInstance() {
		return instance;
	}

	/**
	 * Drops all cached bundles, so the next lookup sees the current texts.
	 */
	public static void clearResourceBundleCache() {
		ResourceBundle.clearCache(InputFieldL10nOverlay.class.getClassLoader());
		ResourceBundle.clearCache(CommonUtil.class.getClassLoader());
		Compile.clearResourceBundleCache();
		L10nUtil.clearInputFieldL10nOwnKeysCache();
	}

	@Override
	public String getOverlay(String baseName, Locale locale) {
		InputFieldL10nBundle bundle = L10nUtil.getInputFieldL10nBundle(baseName);
		// the texts are saved per language: a country or variant bundle (e.g. de_AT) gets them from its parent (de) instead,
		// so its own .properties texts still take precedence for it
		if (bundle != null && (locale == null || (locale.getCountry().isEmpty() && locale.getVariant().isEmpty() && locale.getScript().isEmpty()))) {
			return texts.get(getKey(bundle, locale == null ? "" : CommonUtil.localeToString(locale)));
		}
		return null;
	}

	/**
	 * Loads the snapshot and installs this provider (Spring init-method).
	 */
	public void init() {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setReadOnly(true);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				texts.clear();
				Iterator<InputFieldL10nText> it = inputFieldL10nTextDao.findAllLatest().iterator();
				while (it.hasNext()) {
					InputFieldL10nText text = it.next();
					texts.put(getKey(text.getBundle(), text.getLocale()), text.getText());
				}
			}
		});
		instance = this;
		BundleControl.setOverlayProvider(this);
		clearResourceBundleCache();
	}

	public void setInputFieldL10nTextDao(InputFieldL10nTextDao inputFieldL10nTextDao) {
		this.inputFieldL10nTextDao = inputFieldL10nTextDao;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * Publishes a saved text. Inside a transaction, this happens after its commit only.
	 */
	public void update(final InputFieldL10nBundle bundle, final String locale, final String text) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

				@Override
				public void afterCommit() {
					put(bundle, locale, text);
				}
			});
		} else {
			put(bundle, locale, text);
		}
	}

	private void put(InputFieldL10nBundle bundle, String locale, String text) {
		if (text != null) {
			texts.put(getKey(bundle, locale), text);
		} else {
			texts.remove(getKey(bundle, locale));
		}
		clearResourceBundleCache();
	}
}
