package org.phoenixctms.ctsms.executable.migration;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class JournalSystemMessageCodeInitializer extends EncryptedFieldInitializer {


	private final static ArrayList<Entry<String, Pattern>> LEGACY_TITLE_REGEXP = new ArrayList<Map.Entry<String, Pattern>>();
	static {
		addLegacyTitleRegexp(SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_CREATED, "Wartungseintrag erstellt - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_UPDATED, "Wartungseintrag aktualisiert - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DELETED, "Wartungseintrag gelöscht - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DISMISSED_SET, "Wartungserinnerung ignorieren - {0}");
		// maintenance_schedule_item_dismissed_unset=Wartungserinnerung schlie\u00DFen zur\u00FCckgesetzt - {0}
		addLegacyTitleRegexp("course_deleted_renewal_removed", "course {0} deleted - renewal course removed");
		addLegacyTitleRegexp("course_deleted_renewal_removed", "Kurs {0} gelöscht - Auffrischungskurs entfernt");
		addLegacyTitleRegexp(SystemMessageCodes.VISIT_CREATED, "visite erstellt - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.VISIT_UPDATED, "visite aktualisiert - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.VISIT_DELETED, "visite gelöscht - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.PROBAND_CREATED, "proband {0} erstellt");
		addLegacyTitleRegexp(SystemMessageCodes.PROBAND_UPDATED, "proband {0} aktualisiert");
		addLegacyTitleRegexp(SystemMessageCodes.PROBAND_AUTO_DELETE_DEADLINE_RESET, "Deadline für automatische Löschung geändert - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, "aggregierte PDF Dateien exportiert - {0}");
		addLegacyTitleRegexp(SystemMessageCodes.SELECTION_SET_VALUE_DELETED, "a selected inquiry selection set value was removed");
	}

	private static void addLegacyTitleRegexp(String code, String titleFormat) {
		try {
			LEGACY_TITLE_REGEXP.add(new AbstractMap.SimpleEntry<String, Pattern>(code, CommonUtil.createMessageFormatRegexp(titleFormat, false)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}



	private static ArrayList<Entry<String, Pattern>> createTitleRegexps(JobOutput jobOutput) throws Exception {
		// if (titleRegexps == null) {
		ArrayList<Entry<String, Pattern>> titleRegexps = new ArrayList<Entry<String, Pattern>>();
		LinkedHashMap<Locale, Locales> locales = new LinkedHashMap<Locale, Locales>();

		locales.put(L10nUtil.getLocale(Locales.JOURNAL), Locales.JOURNAL);
		locales.put(L10nUtil.getLocale(Locales.EN), Locales.EN);
		locales.put(L10nUtil.getLocale(Locales.DE), Locales.DE);
		Iterator<Locales> localesIt = locales.values().iterator();
		while (localesIt.hasNext()) {
			Locales locale = localesIt.next();
			TreeMap<String, Pattern> titleRegexpMap = new TreeMap<String, Pattern>();
			Iterator<String> codesIt = CoreUtil.SYSTEM_MESSAGE_CODES.iterator();
			while (codesIt.hasNext()) {
				String code = codesIt.next();
				// if (!titleRegexpMap.containsKey(code)) {
				String titleFormat = L10nUtil.getSystemMessageTitleFormat(locale, code);
				if (!CommonUtil.isEmptyString(titleFormat)) {
					titleRegexpMap.put(code, CommonUtil.createMessageFormatRegexp(titleFormat, false));
				} else {
					throw new Exception("empty " + locale.name() + " system message title format for " + code);
				}
			}
			jobOutput.println(locale.name() + ": " + titleRegexpMap.entrySet().size() + " system message code patterns");
			titleRegexps.addAll(titleRegexpMap.entrySet());
		}
		jobOutput.println(LEGACY_TITLE_REGEXP.size() + " legacy system message code patterns");
		titleRegexps.addAll(LEGACY_TITLE_REGEXP);
		jobOutput.println(titleRegexps.size() + " system message code patterns overall");
		// }
		return titleRegexps;
	}

	private static String getSystemMessageCodeFromTitle(String title, ArrayList<Map.Entry<String, Pattern>> titleRegexps) {
		Iterator<Entry<String, Pattern>> it = titleRegexps.iterator();
		while (it.hasNext()) {
			Entry<String, Pattern> titleRegexp = it.next();
			if (titleRegexp.getValue().matcher(title).find()) {
				return titleRegexp.getKey();
			}
		}
		return null;
	}

	@Autowired
	private JournalEntryDao journalEntryDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DepartmentDao departmentDao;
	private ChunkedDaoOperationAdapter<JournalEntryDao, JournalEntry> journalProcessor;

	public JournalSystemMessageCodeInitializer() throws Exception {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		// final FileModule module = FileModule.TRIAL_DOCUMENT;
		// ((JournalEntryDaoImpl) journalEntryDao).setUserDao(null);
		final ArrayList<Map.Entry<String, Pattern>> titleRegexps = createTitleRegexps(jobOutput);
		journalProcessor = new ChunkedDaoOperationAdapter<JournalEntryDao, JournalEntry>(journalEntryDao
				) {

			// new Search(new SearchParameter[] {
			// new SearchParameter("systemMessage", true, SearchParameter.EQUAL_COMPARATOR),
			// new SearchParameter("systemMessageCode", SearchParameter.NULL_COMPARATOR)
			// }
			// @Override
			// protected boolean isIncrementPageNumber() {
			// return false;
			// }
			@Override
			protected boolean process(Collection<JournalEntry> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(JournalEntry journalEntry, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				if (journalEntry.isSystemMessage() && CommonUtil.isEmptyString(journalEntry.getSystemMessageCode())) {
					try {
						String title;
						if (CommonUtil.getUseJournalEncryption(journalEntry.getSystemMessageModule(), null)) {
							try {
								title = (String) CryptoUtil.decryptValue(journalEntry.getTitleIv(), journalEntry.getEncryptedTitle());
							} catch (Exception e) {
								User modifiedUser = userDao.get(journalEntry.getModifiedUser().getId());
								((HashSet<Long>) inOut.get("departments")).add(modifiedUser.getDepartment().getId());
								throw new Exception("cannot decrypt journal entry ID " + journalEntry.getId() + " (" + modifiedUser.getName() + ")");
							}
						} else {
							title = journalEntry.getTitle();
						}
						if (CommonUtil.isEmptyString(title)) {
							throw new Exception("empty title");
						}
						String systemMessageCode = getSystemMessageCodeFromTitle(title, titleRegexps);
						if (systemMessageCode == null) {
							throw new Exception("no system message code found for title '" + title + "'");
						} else {
							journalEntry.setSystemMessageCode(systemMessageCode);
						}
						this.dao.update(journalEntry);
						// jobOutput.println("row updated");
						inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
					} catch (Exception e) {
						jobOutput.println("row skipped: " + e.getMessage());
						inOut.put("skipped", ((Long) inOut.get("skipped")) + 1l);
					}
				}
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		passThrough.put("departments", new HashSet<Long>());
		journalProcessor.processEach(PageSizes.BIG, passThrough); // huge: out of mem
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		Iterator<Long> departmentsIt = ((HashSet<Long>) passThrough.get("departments")).iterator();
		while (departmentsIt.hasNext()) {
			Department department = departmentDao.get(departmentsIt.next());
			jobOutput.println("encrypted records of department " + department.getNameL10nKey());
		}
		return updated;
	}
}
