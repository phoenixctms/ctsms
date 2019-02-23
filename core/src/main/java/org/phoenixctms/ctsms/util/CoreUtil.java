package org.phoenixctms.ctsms.util;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.phoenixctms.ctsms.PrincipalStore;
import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.compare.AlphanumStringComparator;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.security.IPAddressValidation;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.diff_match_patch.Diff;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseCertificatePDFVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CvPDFVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.JournalExcelVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.TeamMembersExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;

import com.thoughtworks.xstream.XStream;

public final class CoreUtil {

	public final static String RANDOM_ALGORITHM = "SHA1PRNG";
	private final static String JAVASCRIPT_ENGINE_NAME = "JavaScript";
	public static final String PDF_FILENAME_EXTENSION = "pdf";
	public static final String PDF_MIMETYPE_STRING = "application/pdf"; // public for demodataprovider
	public static final String EXCEL_FILENAME_EXTENSION = "xls";
	public static final String EXCEL_MIMETYPE_STRING = "application/vnd.ms-excel";

	// private static final String HEX_DIGITS = "0123456789ABCDEF";
	private static final String DAO_LOAD_METHOD_NAME = "load";
	private static final String DAO_TRANSFORM_METHOD_PREFIX = "to";
	private static HashMap<Class, HashSet<String>> journalExcludedOutVOFieldMap = new HashMap<Class, HashSet<String>>();
	private static HashMap<Class, HashSet<String>> auditTrailExcludedOutVOFieldMap = new HashMap<Class, HashSet<String>>();
	static {
		addExcludedField(journalExcludedOutVOFieldMap, JournalEntryOutVO.class, "comment");
		addExcludedField(journalExcludedOutVOFieldMap, ECRFFieldValueOutVO.class, "changeComment");
		addExcludedField(journalExcludedOutVOFieldMap, CourseCertificatePDFVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, CourseParticipantListPDFVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, ReimbursementsExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, VisitScheduleExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, ProbandLetterPDFVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, JournalExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, CourseParticipantListPDFVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, CvPDFVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, SearchResultExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, ProbandListExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, TeamMembersExcelVO.class, "documentData");
		addExcludedField(journalExcludedOutVOFieldMap, ECRFPDFVO.class, "documentData");
		addExcludedField(auditTrailExcludedOutVOFieldMap, ECRFFieldValueOutVO.class, "changeComment");
		addExcludedField(auditTrailExcludedOutVOFieldMap, ECRFFieldValueOutVO.class, "reasonForChange");
		addExcludedField(auditTrailExcludedOutVOFieldMap, ECRFFieldValueOutVO.class, "id");
		// addExcludedField(auditTrailExcludedOutVOFieldMap, ECRFFieldValueOutVO.class, "listEntry");
	}
	private final static String VALUE_OBJECTS_PACKAGE_NAME = "org.phoenixctms.ctsms.vo";
	private final static String ENTITIES_PACKAGE_NAME = "org.phoenixctms.ctsms.domain";
	private final static String ENUMERATIONS_PACKAGE_NAME = "org.phoenixctms.ctsms.enumeration";
	private final static String OUT_VO_CLASS_SUFFIX = "OutVO";
	private final static String IN_VO_CLASS_SUFFIX = "InVO";
	private final static String VO_CLASS_SUFFIX = "VO";
	private final static String DAO_CLASS_SUFFIX = "Dao";

	public final static String URL_PATTERN = "^([hH][tT][tT][pP][sS]?|[fF][tT][pP])://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+([/?].+)?$"; // http://answers.oreilly.com/topic/280-how-to-validate-urls-with-regular-expressions/
	public final static String EMAIL_ADDRESS_PATTERN = "^[\\w-]+(\\.[\\w-]+)*@([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*?\\.[a-zA-Z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$"; // http://regexlib.com/UserPatterns.aspx?authorId=2c58598d-b6ac-4952-9a6a-bf2e6ae7dddc
	public final static String PHONE_NUMBER_PATTERN = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$"; // http://stackoverflow.com/questions/2113908/what-regular-expression-will-match-valid-international-phone-numbers
	// String phoneNumberRegExp =
	// "^+(999|998|997|996|995|994|993|992|991|990|979|978|977|976|975|974|973|972|971|970|969|968|967|966|965|964|963|962|961|960|899|898|897|896|895|894|893|892|891|890|889|888|887|886|885|884|883|882|881|880|879|878|877|876|875|874|873|872|871|870|859|858|857|856|855|854|853|852|851|850|839|838|837|836|835|834|833|832|831|830|809|808|807|806|805|804|803|802|801|800|699|698|697|696|695|694|693|692|691|690|689|688|687|686|685|684|683|682|681|680|679|678|677|676|675|674|673|672|671|670|599|598|597|596|595|594|593|592|591|590|509|508|507|506|505|504|503|502|501|500|429|428|427|426|425|424|423|422|421|420|389|388|387|386|385|384|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|296|295|294|293|292|291|290|289|288|287|286|285|284|283|282|281|280|269|268|267|266|265|264|263|262|261|260|259|258|257|256|255|254|253|252|251|250|249|248|247|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|219|218|217|216|215|214|213|212|211|210|98|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1)[0-9]{0,14}$";
	private final static Pattern EMAIL_ADDRESS_REGEXP = Pattern.compile(EMAIL_ADDRESS_PATTERN);
	private final static HashSet<String> PASS_DECRYPTION_REALMS = new HashSet<String>();

	static {
		// methods that return inserted data but suffer from CANNOT_DECRYPT_PROBAND when requested from untrusted hosts:
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProband");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProband");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProbandTagValue");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProbandTagValue");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProbandStatusEntry");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProbandStatusEntry");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProbandContactDetailValue");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProbandContactDetailValue");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProbandAddress");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProbandAddress");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addDiagnosis");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateDiagnosis");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addProcedure");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateProcedure");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addMedication");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateMedication");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addBankAccount");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateBankAccount");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.addMoneyTransfer");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.updateMoneyTransfer");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.setProbandImage");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.setProbandAddressWireTransfer");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.setMoneyTransferPaid");
		PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.ProbandService.setAllMoneyTransfersPaid");
		// PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.MassMailService.addMassMailRecipient");
		// PASS_DECRYPTION_REALMS.add("org.phoenixctms.ctsms.service.proband.MassMailService.resetMassMailRecipient");
	}
	public final static HashSet<String> VO_VERSION_EQUALS_EXCLUDES = new HashSet<String>();

	static {
		VO_VERSION_EQUALS_EXCLUDES.addAll(CommonUtil.VO_EQUALS_EXCLUDES);
		VO_VERSION_EQUALS_EXCLUDES.add("*.getVersion");
		VO_VERSION_EQUALS_EXCLUDES.add("*.getModifiedUser");
		VO_VERSION_EQUALS_EXCLUDES.add("*.getModifiedTimestamp");
	}
	private static final String ENTITY_VERSION_GETTER_METHOD_NAME = "getVersion";

	private static final String ENTITY_VERSION_SETTER_METHOD_NAME = "setVersion";
	private static final String ENTITY_MODIFIED_USER_GETTER_METHOD_NAME = "getModifiedUser";
	private static final String ENTITY_MODIFIED_USER_SETTER_METHOD_NAME = "setModifiedUser";
	private static final String ENTITY_MODIFIED_TIMESTAMP_SETTER_METHOD_NAME = "setModifiedTimestamp";
	public final static Set<String> SYSTEM_MESSAGE_CODES = createSystemMessageCodeSet();
	private static String PRNG_CLASS_DESCRIPTION = "{0} ({1})";

	private static void addExcludedField(HashMap<Class, HashSet<String>> fieldMap, Class vo, String fieldName) {
		if (fieldMap.containsKey(vo)) {
			fieldMap.get(vo).add(fieldName);
		} else {
			HashSet<String> fieldNames = new HashSet<String>();
			fieldMap.put(vo, fieldNames);
			fieldNames.add(fieldName);
		}
	}

	public static boolean checkClassExists(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
		}
		return false;
	}

	public static void checkEmailAddress(String email, boolean allowResolveMailAddress) throws ServiceException {
		if (!EMAIL_ADDRESS_REGEXP.matcher(email).find()) {
			if (allowResolveMailAddress) {
				String resolveMailAddressDomainName = Settings.getString(SettingCodes.RESOLVE_MAIL_ADDRESS_DOMAIN_NAME, Bundle.SETTINGS,
						DefaultSettings.RESOLVE_MAIL_ADDRESS_DOMAIN_NAME);
				if (!CommonUtil.isEmptyString(resolveMailAddressDomainName)) {
					Pattern resolveMailAddressRegExp = Pattern.compile("^" + AssociationPath.ASSOCIATION_PATH_PATTERN + "@" + Pattern.quote(resolveMailAddressDomainName) + "$");
					if (resolveMailAddressRegExp.matcher(email).find()) {
						return;
					}
				}
			}
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_EMAIL_ADDRESS, email);
		}
	}

	public static boolean checkHostIp(String host) {
		try {
			checkHostIp(host,
					Settings.getString(SettingCodes.TRUSTED_IP_RANGES, Bundle.SETTINGS, DefaultSettings.TRUSTED_IP_RANGES),
					null);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void checkHostIp(String ipRanges, String serviceMethod) throws Exception {
		checkHostIp(getHost(), ipRanges, serviceMethod);
	}

	private static void checkHostIp(String host, String ipRanges, String serviceMethod) throws Exception {
		if (!CommonUtil.isEmptyString(ipRanges)) {
			if (CommonUtil.isEmptyString(host)) {
				throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_HOST, serviceMethod);
			}
			Iterator<String[]> it = IPAddressValidation.splitIpRanges(ipRanges).iterator();
			boolean ipAllowed = false;
			while (it.hasNext()) {
				if (IPAddressValidation.isWithinIpRange(it.next(), host)) {
					ipAllowed = true;
					break;
				}
			}
			if (!ipAllowed) {
				throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.HOST_NOT_ALLOWED_OR_UNKNOWN_HOST, serviceMethod, host);
			}
		}
	}

	public static boolean checkSupportedLocale(String language) {
		Locale locale = CommonUtil.localeFromString(language);
		Iterator<Locale> it = getSupportedLocales().iterator();
		while (it.hasNext()) {
			if (it.next().equals(locale)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkSystemLocale(String language) {
		Locale locale = CommonUtil.localeFromString(language);
		Locale[] availableLocales = Locale.getAvailableLocales();
		for (int i = 0; i < availableLocales.length; i++) {
			if (availableLocales[i].equals(locale)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkTimeZone(String timeZoneID) {
		TimeZone timeZone = CommonUtil.timeZoneFromString(timeZoneID);
		Iterator<TimeZone> it = getTimeZones().iterator();
		while (it.hasNext()) {
			if (it.next().equals(timeZone)) {
				return true;
			}
			// String timeZoneIDSuffix = "/" + timeZoneID;
			// if (availableTimeZoneID.equals(timeZoneID) || availableTimeZoneID.endsWith(timeZoneIDSuffix)) {
			// return true;
			// }
		}
		return false;
	}

	public static void clearUserContext() {
		PrincipalStore.set(null);
	}

	public static Map createEmptyTemplateModel() {
		return new HashMap<String, Object>();
	}

	public static String createExternalFileName(String prefix, String fileName) {
		FilePathSplitter filePath = new FilePathSplitter(fileName);
		StringBuilder result = new StringBuilder();
		if (prefix != null && prefix.length() > 0) {
			result.append(prefix);
		}
		result.append(CommonUtil.generateUUID());
		String extension = filePath.getExtension();
		if (extension != null && extension.length() > 0) {
			result.append(".");
			result.append(extension);
		}
		return result.toString();
	}

	public static FileInputStream createFileServiceFileInputStream(String fileName) throws FileNotFoundException, IllegalArgumentException, IOException {
		return new FileInputStream(getFileServiceExternalFilename(fileName));
	}

	private final static Set<String> createSystemMessageCodeSet() {
		Field[] fields = SystemMessageCodes.class.getDeclaredFields();
		TreeSet<String> codes = new TreeSet<String>();
		for (int i = 0; i < fields.length; i++) {
			String code = null;
			try {
				code = (String) fields[i].get(null);
			} catch (Exception e) {
			}
			if (!CommonUtil.isEmptyString(code)) {
				codes.add(code);
			}
		}
		return codes;
	}

	public static Object deserialize(byte[] serialized) throws IOException, ClassNotFoundException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(serialized);
		ObjectInputStream objectStream = new ObjectInputStream(buffer);
		Object value = objectStream.readObject();
		objectStream.close();
		buffer.close();
		return value;
	}

	private static String dumpAuditTrailVo(ArrayList<KeyValueString> voFields, Object vo, boolean enumerateEntities, boolean omitFields) throws Exception {
		StringBuilder result = new StringBuilder();
		String lineSeparator = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_LINE_SEPARATOR, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_LINE_SEPARATOR);
		String lineFormat = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_SINGLE_LINE, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_SINGLE_LINE);
		String multiLineHeadFormat = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_MULTI_LINE_HEAD, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_MULTI_LINE_HEAD);
		String multiLineTailFormat = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_MULTI_LINE_TAIL, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_MULTI_LINE_TAIL);
		boolean normalizeMultiLineSeparator = Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_NORMALIZE_MULTI_LINE_SEPARATOR, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_NORMALIZE_MULTI_LINE_SEPARATOR);
		String indent = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_INDENT, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_INDENT);
		// String indentChangeSeparator = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_INDENT_CHANGE_SEPARATOR, Bundle.SETTINGS,
		// DefaultSettings.JOURNAL_ENTRY_COMMENT_INDENT_CHANGE_SEPARATOR);
		String datetimePattern = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_DATETIME_PATTERN, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_DATETIME_PATTERN);
		String datePattern = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_DATE_PATTERN);
		String timePattern = Settings.getString(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_TIME_PATTERN);
		boolean skipEmptyFields = Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_SKIP_EMPTY_FIELDS, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_SKIP_EMPTY_FIELDS);
		boolean skipFieldsWithoutLocalization = Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_SKIP_FIELDS_WITHOUT_LOCALIZATION, Bundle.SETTINGS,
				DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_SKIP_FIELDS_WITHOUT_LOCALIZATION);
		// int oldIndentation = 0;
		ArrayList<String> referenceFields = new ArrayList<String>();
		ArrayList<String> deferredCollectionMapFields = new ArrayList<String>();
		Iterator<KeyValueString> voFieldIt = voFields.iterator();
		while (voFieldIt.hasNext()) {
			KeyValueString keyValuePair = voFieldIt.next();
			Iterator<ArrayList<Object>> indexesKeysIt = keyValuePair.getIndexesKeys(vo).iterator();
			while (indexesKeysIt.hasNext()) {
				ArrayList<Object> indexesKeys = indexesKeysIt.next();
				String key = keyValuePair.getKey(indexesKeys);
				if (!skipFieldsWithoutLocalization || L10nUtil.containsAuditTrailChangeCommentFieldLabel(Locales.AUDIT_TRAIL, key)) {
					String value = keyValuePair.getValue(Locales.AUDIT_TRAIL, vo, indexesKeys, datetimePattern, datePattern, timePattern, enumerateEntities, omitFields);
					if (!skipEmptyFields || (value != null && value.length() > 0)) {
						StringBuilder indentation = new StringBuilder();
						for (int i = 0; i < keyValuePair.getIndentation(); i++) {
							indentation.append(indent);
						}
						String line = formatDumpLine(L10nUtil.getAuditTrailChangeCommentFieldLabel(Locales.AUDIT_TRAIL, key),
								value,
								lineFormat,
								multiLineHeadFormat,
								multiLineTailFormat,
								indentation.toString(),
								indent,
								lineSeparator,
								normalizeMultiLineSeparator);
						if (indexesKeys.size() > 0) {
							deferredCollectionMapFields.add(line);
						} else {
							referenceFields.add(line);
						}
					}
				}
			}
		}
		AlphanumStringComparator stringComparator = new AlphanumStringComparator(true);
		Collections.sort(referenceFields, stringComparator);
		Iterator<String> referenceFieldsIt = referenceFields.iterator();
		while (referenceFieldsIt.hasNext()) {
			if (result.length() > 0) {
				result.append(lineSeparator);
			}
			result.append(referenceFieldsIt.next());
		}
		Collections.sort(deferredCollectionMapFields, stringComparator);
		Iterator<String> deferredCollectionMapFieldsIt = deferredCollectionMapFields.iterator();
		while (deferredCollectionMapFieldsIt.hasNext()) {
			if (result.length() > 0) {
				result.append(lineSeparator);
			}
			result.append(deferredCollectionMapFieldsIt.next());
		}
		return result.toString();
	}

	private static String dumpJournalVo(ArrayList<KeyValueString> voFields, Object vo, boolean enumerateEntities, boolean omitFields) throws Exception {
		StringBuilder result = new StringBuilder();
		String lineSeparator = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_LINE_SEPARATOR, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_LINE_SEPARATOR);
		String lineFormat = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_SINGLE_LINE, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_SINGLE_LINE);
		String multiLineHeadFormat = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_MULTI_LINE_HEAD, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_MULTI_LINE_HEAD);
		String multiLineTailFormat = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_MULTI_LINE_TAIL, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_MULTI_LINE_TAIL);
		boolean normalizeMultiLineSeparator = Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_NORMALIZE_MULTI_LINE_SEPARATOR, Bundle.SETTINGS,
				DefaultSettings.JOURNAL_ENTRY_COMMENT_NORMALIZE_MULTI_LINE_SEPARATOR);
		String indent = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_INDENT, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_INDENT);
		// String indentChangeSeparator = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_INDENT_CHANGE_SEPARATOR, Bundle.SETTINGS,
		// DefaultSettings.JOURNAL_ENTRY_COMMENT_INDENT_CHANGE_SEPARATOR);
		String datetimePattern = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_DATETIME_PATTERN, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_DATETIME_PATTERN);
		String datePattern = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_DATE_PATTERN, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_DATE_PATTERN);
		String timePattern = Settings.getString(SettingCodes.JOURNAL_ENTRY_COMMENT_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_TIME_PATTERN);
		boolean skipEmptyFields = Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_SKIP_EMPTY_FIELDS, Bundle.SETTINGS,
				DefaultSettings.JOURNAL_ENTRY_COMMENT_SKIP_EMPTY_FIELDS);
		boolean skipFieldsWithoutLocalization = Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_SKIP_FIELDS_WITHOUT_LOCALIZATION, Bundle.SETTINGS,
				DefaultSettings.JOURNAL_ENTRY_COMMENT_SKIP_FIELDS_WITHOUT_LOCALIZATION);
		// int oldIndentation = 0;
		ArrayList<String> referenceFields = new ArrayList<String>();
		ArrayList<String> deferredCollectionMapFields = new ArrayList<String>();
		Iterator<KeyValueString> voFieldIt = voFields.iterator();
		while (voFieldIt.hasNext()) {
			KeyValueString keyValuePair = voFieldIt.next();
			Iterator<ArrayList<Object>> indexesKeysIt = keyValuePair.getIndexesKeys(vo).iterator();
			while (indexesKeysIt.hasNext()) {
				ArrayList<Object> indexesKeys = indexesKeysIt.next();
				String key = keyValuePair.getKey(indexesKeys);
				if (!skipFieldsWithoutLocalization || L10nUtil.containsSystemMessageCommentFieldLabel(Locales.JOURNAL, key)) {
					String value = keyValuePair.getValue(Locales.JOURNAL, vo, indexesKeys, datetimePattern, datePattern, timePattern, enumerateEntities, omitFields);
					if (!skipEmptyFields || (value != null && value.length() > 0)) {
						StringBuilder indentation = new StringBuilder();
						for (int i = 0; i < keyValuePair.getIndentation(); i++) {
							indentation.append(indent);
						}
						String line = formatDumpLine(L10nUtil.getSystemMessageCommentFieldLabel(Locales.JOURNAL, key),
								value,
								lineFormat,
								multiLineHeadFormat,
								multiLineTailFormat,
								indentation.toString(),
								indent,
								lineSeparator,
								normalizeMultiLineSeparator);
						if (indexesKeys.size() > 0) {
							deferredCollectionMapFields.add(line);
						} else {
							referenceFields.add(line);
						}
					}
				}
			}
		}
		AlphanumStringComparator stringComparator = new AlphanumStringComparator(true);
		Collections.sort(referenceFields, stringComparator);
		Iterator<String> referenceFieldsIt = referenceFields.iterator();
		while (referenceFieldsIt.hasNext()) {
			if (result.length() > 0) {
				result.append(lineSeparator);
			}
			result.append(referenceFieldsIt.next());
		}
		Collections.sort(deferredCollectionMapFields, stringComparator);
		Iterator<String> deferredCollectionMapFieldsIt = deferredCollectionMapFields.iterator();
		while (deferredCollectionMapFieldsIt.hasNext()) {
			if (result.length() > 0) {
				result.append(lineSeparator);
			}
			result.append(deferredCollectionMapFieldsIt.next());
		}
		return result.toString();
	}

	public static Date forceDate(Date timestamp) {
		if (timestamp != null) {
			return new Date(timestamp.getTime());
		} else {
			return null;
		}
	}

	private static String formatDumpLine(String key, String value, String lineFormat, String multiLineHeadFormat, String multiLineTailFormat,
			String indentation, String indent, String lineSeparator, boolean normalizeLineSeparator) {
		if (CommonUtil.isMultiLineString(value)) {
			// if (indent.length() > 0 || normalizeLineSeparator) {
			StringBuilder result = new StringBuilder(MessageFormat.format(multiLineHeadFormat, indentation, key, lineSeparator));
			String[] lines = CommonUtil.splitMultiLineString(value, true);
			for (int i = 0; i < lines.length; i = i + 2) {
				result.append(MessageFormat.format(multiLineTailFormat, indentation, indent, lines[i]));
				if (i < lines.length - 1) {
					if (normalizeLineSeparator) {
						result.append(lineSeparator);
					} else {
						result.append(lines[i + 1]);
					}
				}
			}
			return result.toString();
			// } else {
			// return MessageFormat.format(multiLineFormat, indentation, key, value);
			// }
		} else {
			return MessageFormat.format(lineFormat, indentation, key, value);
		}
	}

	public static String getAuditTrailChangeCommentContent(Object vo, Object original, boolean excludeEncryptedFields) throws Exception {
		// System.out.println((new Date()).toString() + " getSystemMessageCommentContent START");
		if (vo != null) { // department password change message sets a null user for journal comment...
			boolean enumerateEntities = Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_ENTITIES, Bundle.SETTINGS,
					DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_ENTITIES);
			ArrayList<KeyValueString> voFields = KeyValueString
					.getKeyValuePairs(vo.getClass(),
							Settings.getInt(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_VO_DEPTH, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_VO_DEPTH),
							excludeEncryptedFields,
							auditTrailExcludedOutVOFieldMap,
							enumerateEntities,
							Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_REFERENCES, Bundle.SETTINGS,
									DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_REFERENCES),
							Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_COLLECTIONS, Bundle.SETTINGS,
									DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_COLLECTIONS),
							Settings.getBoolean(SettingCodes.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_MAPS, Bundle.SETTINGS, DefaultSettings.AUDIT_TRAIL_CHANGE_COMMENT_ENUMERATE_MAPS),
							AssociationPath.ASSOCIATION_PATH_SEPARATOR,
							false);
			String voDump = dumpAuditTrailVo(voFields, vo, enumerateEntities, excludeEncryptedFields);
			if (original != null) {
				if (vo.getClass().equals(original.getClass())) {
					String originalDump = dumpAuditTrailVo(voFields, original, enumerateEntities, excludeEncryptedFields);
					// // Compute diff. Get the Patch object. Patch is the container for computed deltas.
					// Patch patch = DiffUtils.diff(originalList, voList);
					//
					// for (Delta delta: patch.getDeltas()) {
					// if (result.length() > 0) {
					// result.append(separator);
					// }
					// delta.
					// result.append(delta);
					// }
					diff_match_patch diff = new diff_match_patch();
					LinkedList<Diff> diffs = diff.diff_main(originalDump, voDump, true);
					diff.diff_cleanupSemantic(diffs);
					diff.diff_cleanupEfficiency(diffs);
					if (CommonUtil.HTML_AUDIT_TRAIL_CHANGE_COMMENTS) {
						return diff.diff_prettyHtml(diffs, false);
					} else {
						return diff.diff_text(diffs);
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.DIFFERRING_ORIGINAL_AND_UPDATED_VO_TYPES,
							DefaultMessages.DIFFERRING_ORIGINAL_AND_UPDATED_VO_TYPES, new Object[] { original.getClass().toString(), vo.getClass().toString() }));
				}
			} else {
				if (CommonUtil.HTML_AUDIT_TRAIL_CHANGE_COMMENTS) {
					return diff_match_patch.escapeHtml(voDump, false);
				} else {
					return voDump;
				}
			}
		}
		return "";
	}

	public static Method getDaoInVOTransformMethod(String entityName, Object dao) throws Exception {
		return getDaoTransformMethod(entityName, IN_VO_CLASS_SUFFIX, dao);
	}

	public static Method getDaoLoadMethod(Object dao) throws NoSuchMethodException, SecurityException {
		return dao.getClass().getMethod(DAO_LOAD_METHOD_NAME, Long.class);
	}

	public static String getDaoNameFromEntityName(String entityName) {
		StringBuilder daoName = new StringBuilder(entityName);
		daoName.append(DAO_CLASS_SUFFIX);
		return daoName.toString();
	}

	public static Method getDaoOutVOTransformMethod(String entityName, Object dao) throws Exception {
		return getDaoTransformMethod(entityName, OUT_VO_CLASS_SUFFIX, dao);
	}

	private static Method getDaoTransformMethod(String entityName, String voTypeSuffix, Object dao) throws Exception {
		return AssociationPath.findMethod(DAO_TRANSFORM_METHOD_PREFIX + getValueObjectNameFromEntityName(entityName, voTypeSuffix),
				getDaoTransformMethodTransfilter(getEntityClassNameFromEntityName(entityName)),
				false, dao.getClass().getMethods());
	}

	private final static MethodTransfilter getDaoTransformMethodTransfilter(final String entityClassName) {
		return new MethodTransfilter() {

			@Override
			public boolean include(Method method) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				return parameterTypes.length == 1 && parameterTypes[0].getName().equals(entityClassName);
			}
		};
	}

	public static Method getDaoVOTransformMethod(String entityName, Object dao) throws Exception {
		return getDaoTransformMethod(entityName, VO_CLASS_SUFFIX, dao);
	}

	private static String getEntityClassNameFromEntityName(String entityName) {
		StringBuilder voClassName = new StringBuilder(ENTITIES_PACKAGE_NAME);
		voClassName.append(".");
		voClassName.append(entityName);
		return voClassName.toString();
	}

	public static String getEnumerationClassName(String enumName) {
		StringBuilder enumClassName = new StringBuilder(ENUMERATIONS_PACKAGE_NAME);
		enumClassName.append(".");
		enumClassName.append(enumName);
		return enumClassName.toString();
	}

	public static Object getEnumerationItem(String enumName, String enumItemName) throws Exception {
		//
		return Class.forName(CoreUtil.getEnumerationClassName(enumName)).getMethod("fromString", String.class).invoke(null, enumItemName);
		// } catch (ClassNotFoundException e) {
		// throw e;
		// } catch (Exception e) {
		//
		// }
		// return null;
	}

	public static String getEnumerationValueObjectName(String enumName) {
		StringBuilder enumVoName = new StringBuilder(enumName);
		enumVoName.append(VO_CLASS_SUFFIX);
		return enumVoName.toString();
	}

	public static MimeTypeVO getExcelMimeType() {
		MimeTypeVO excel = new MimeTypeVO();
		excel.setMimeType(EXCEL_MIMETYPE_STRING);
		if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
			excel.setFileNameExtensions(EXCEL_FILENAME_EXTENSION);
		} else {
			excel.setFileNameExtensions("*." + EXCEL_FILENAME_EXTENSION);
		}
		return excel;
	}

	public static String getExternalFileDirectoryPrefix(FileModule module) {
		StringBuilder result = new StringBuilder();
		result.append(module.getValue());
		result.append(java.io.File.separator);
		return result.toString();
	}

	public static String getFileServiceExternalFilename(String fileName) throws IllegalArgumentException, IOException {
		return (new java.io.File(Settings.getDirectory(SettingCodes.EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_DATA_DIR),
				CommonUtil.sanitizeFilePath(fileName))).getCanonicalPath();
	}

	public static String getHost() {
		return getUserContext().getHost();
		// UserContext userContext = CoreUtil.getUserContext();
		// return (User) userDao.searchUniqueUsername(UserDao.TRANSFORM_NONE, userContext.getUsername());
	}

	public static Dimension getImageDimension(byte[] imageData) {
		if (imageData != null && imageData.length > 0) {
			ImageInputStream in = null;
			try {
				in = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));
				final Iterator readers = ImageIO.getImageReaders(in);
				if (readers.hasNext()) {
					ImageReader reader = (ImageReader) readers.next();
					try {
						reader.setInput(in);
						return new Dimension(reader.getWidth(0), reader.getHeight(0));
					} finally {
						reader.dispose();
					}
				}
			} catch (Exception e) {
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return null;
	}

	public static String getInVOClassNameFromEntityName(String entityName) {
		return getValueObjectClassNameFromEntityName(entityName, IN_VO_CLASS_SUFFIX);
	}

	public static ScriptEngine getJsEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		return manager.getEngineByName(JAVASCRIPT_ENGINE_NAME);
	}

	public static Password getLastPassword() {
		return getUserContext().getLastPassword();
		// UserContext userContext = CoreUtil.getUserContext();
		// return (User) userDao.searchUniqueUsername(UserDao.TRANSFORM_NONE, userContext.getUsername());
	}

	public static <E> long getNewVersionChecked(E original, long modifiedVersion) throws Exception {
		if (original != null) {
			long originalVersion = ((Long) original.getClass().getMethod(ENTITY_VERSION_GETTER_METHOD_NAME).invoke(original)).longValue();
			if (modifiedVersion != originalVersion) {
				User originalModifiedUser = (User) original.getClass().getMethod(ENTITY_MODIFIED_USER_GETTER_METHOD_NAME).invoke(original);
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_WAS_MODIFIED_SINCE, originalModifiedUser.getName());
			}
			return originalVersion + 1l;
		} else {
			if (modifiedVersion != 0l) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_VERSION_NOT_ZERO); // or null");
			}
			return 0l;
		}
	}

	// public static String getHex(byte[] data) {
	// if (data == null) {
	// return null;
	// }
	// final StringBuilder hex = new StringBuilder(2 * data.length);
	// for (final byte b : data) {
	// hex.append(HEX_DIGITS.charAt((b & 0xF0) >> 4)).append(
	// HEX_DIGITS.charAt((b & 0x0F)));
	// }
	// return hex.toString();
	// }

	public static String getOutVOClassNameFromEntityName(String entityName) {
		return getValueObjectClassNameFromEntityName(entityName, OUT_VO_CLASS_SUFFIX);
	}

	public static MimeTypeVO getPDFMimeType() {
		MimeTypeVO pdf = new MimeTypeVO();
		pdf.setMimeType(PDF_MIMETYPE_STRING);
		if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
			pdf.setFileNameExtensions(PDF_FILENAME_EXTENSION);
		} else {
			pdf.setFileNameExtensions("*." + PDF_FILENAME_EXTENSION);
		}
		return pdf;
	}

	public static String getPrngClassDescription(Random random) {
		return MessageFormat.format(PRNG_CLASS_DESCRIPTION,random.getClass().getCanonicalName(),System.getProperty("java.version"));
	}

	public static Class getPropertyClass(Class entity, String propertyName) {
		if (propertyName != null && propertyName.length() > 0 && entity != null) {
			Method[] methods = entity.getMethods();
			String setterName = "set" + CommonUtil.capitalizeFirstChar(propertyName, true);
			String adderName = "add" + CommonUtil.capitalizeFirstChar(propertyName, true);
			if (methods != null) {
				Method setterMethod = null;
				Method adderMethod = null;
				for (int i = 0; i < methods.length; i++) {
					String methodName = methods[i].getName();
					if (methodName.equals(setterName)) {
						Class[] parameterTypes = methods[i].getParameterTypes();
						if (parameterTypes != null && parameterTypes.length == 1) {
							setterMethod = methods[i];
							if (adderMethod != null) {
								break;
							}
						}
					} else if (methodName.equals(adderName)) {
						Class[] parameterTypes = methods[i].getParameterTypes();
						if (parameterTypes != null && parameterTypes.length == 1) {
							adderMethod = methods[i];
							if (setterMethod != null) {
								break;
							}
						}
					}
				}
				if (adderMethod != null) {
					return adderMethod.getParameterTypes()[0];
				}
				if (setterMethod != null) {
					return setterMethod.getParameterTypes()[0];
				}
			}
		}
		// cannot determine property class.....
		throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PROPERTY_NOT_FOUND_OR_INVALID_TYPE, DefaultMessages.PROPERTY_NOT_FOUND_OR_INVALID_TYPE, new Object[] {
				propertyName, entity == null ? null : entity.toString() }));
	}

	public static String getServiceMethodName(Method method) {
		StringBuilder result = new StringBuilder(method.getDeclaringClass().getName());
		result.append(AssociationPath.ASSOCIATION_PATH_SEPARATOR);
		result.append(method.getName());
		return result.toString();
	}

	public static String getStackTrace(Throwable t) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);
		return result.toString();
	}

	// public static String mergeVslTemplate1(String vslFileName, String contextEntityName, Object contextEntity) {
	// //if (messageVslFileName != null && messageVslFileName.length() > 0) {
	// VelocityEngine velocityEngine = new VelocityEngine();
	// //ve.setProperty(VelocityEngine..RUNTIME_LOG_LOGSYSTEM, this);
	//
	// velocityEngine.setProperty(VelocityEngine.RESOURCE_MANAGER_CLASS, "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	//
	// velocityEngine.init();
	// VelocityContext context = new VelocityContext();
	//
	// context.put( contextEntityName, contextEntity );
	//
	// Template template = null;
	// //try {
	// template = velocityEngine.getTemplate(vslFileName);
	// // } catch (ResourceNotFoundException e) {
	// // // couldn't find the template
	// // } catch (ParseErrorException e) {
	// // // syntax error: problem parsing the template
	// // } catch (MethodInvocationException e) {
	// // // something invoked in the template
	// // // threw an exception
	// // //} catch (Exception e) {
	// //
	// // }
	//
	// StringWriter stringWriter = new StringWriter();
	// template.merge( context, stringWriter );
	//
	// return stringWriter.toString();
	// //}
	// }
	public static <T> ArrayList<T> getSubList(List<T> list, int offset, int count) {
		if (list != null && offset >= 0 && count >= 0) {
			ArrayList<T> result = new ArrayList<T>(count);
			for (int i = offset; (i < offset + count) && (i < list.size()); i++) {
				result.add(list.get(i));
			}
			return result;
		} else {
			return new ArrayList<T>();
		}
	}

	public static ArrayList<Locale> getSupportedLocales() {
		ArrayList<String> localeList = Settings.getStringList(SettingCodes.SUPPORTED_LOCALES, Settings.Bundle.SETTINGS, null);
		ArrayList<Locale> locales = new ArrayList<Locale>(localeList.size());
		Iterator<String> it = localeList.iterator();
		while (it.hasNext()) {
			locales.add(CommonUtil.localeFromString(it.next()));
		}
		// if (locales.size() == 0) {
		// locales.add(Locale.getDefault());
		// }
		return locales;
	}

	public static String getSystemMessageCommentContent(Object vo, Object original, boolean excludeEncryptedFields) throws Exception {
		// System.out.println((new Date()).toString() + " getSystemMessageCommentContent START");
		if (vo != null) { // department password change message sets a null user for journal comment...
			boolean enumerateEntities = Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_ENUMERATE_ENTITIES, Bundle.SETTINGS,
					DefaultSettings.JOURNAL_ENTRY_COMMENT_ENUMERATE_ENTITIES);
			ArrayList<KeyValueString> voFields = KeyValueString.getKeyValuePairs(vo.getClass(),
					Settings.getInt(SettingCodes.JOURNAL_ENTRY_COMMENT_VO_DEPTH, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_VO_DEPTH),
					excludeEncryptedFields,
					journalExcludedOutVOFieldMap,
					enumerateEntities,
					Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_ENUMERATE_REFERENCES, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_ENUMERATE_REFERENCES),
					Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_ENUMERATE_COLLECTIONS, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_ENUMERATE_COLLECTIONS),
					Settings.getBoolean(SettingCodes.JOURNAL_ENTRY_COMMENT_ENUMERATE_MAPS, Bundle.SETTINGS, DefaultSettings.JOURNAL_ENTRY_COMMENT_ENUMERATE_MAPS),
					AssociationPath.ASSOCIATION_PATH_SEPARATOR,
					false);
			String voDump = dumpJournalVo(voFields, vo, enumerateEntities, excludeEncryptedFields);
			if (original != null) {
				if (vo.getClass().equals(original.getClass())) {
					String originalDump = dumpJournalVo(voFields, original, enumerateEntities, excludeEncryptedFields);
					// // Compute diff. Get the Patch object. Patch is the container for computed deltas.
					// Patch patch = DiffUtils.diff(originalList, voList);
					//
					// for (Delta delta: patch.getDeltas()) {
					// if (result.length() > 0) {
					// result.append(separator);
					// }
					// delta.
					// result.append(delta);
					// }
					diff_match_patch diff = new diff_match_patch();
					LinkedList<Diff> diffs = diff.diff_main(originalDump, voDump, true);
					diff.diff_cleanupSemantic(diffs);
					diff.diff_cleanupEfficiency(diffs);
					if (CommonUtil.HTML_SYSTEM_MESSAGES_COMMENTS) {
						return diff.diff_prettyHtml(diffs, false);
					} else {
						return diff.diff_text(diffs);
					}
				} else {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.DIFFERRING_ORIGINAL_AND_UPDATED_VO_TYPES,
							DefaultMessages.DIFFERRING_ORIGINAL_AND_UPDATED_VO_TYPES, new Object[] { original.getClass().toString(), vo.getClass().toString() }));
				}
			} else {
				if (CommonUtil.HTML_SYSTEM_MESSAGES_COMMENTS) {
					return diff_match_patch.escapeHtml(voDump, false);
				} else {
					return voDump;
				}
			}
		}
		return "";
	}

	public static ArrayList<TimeZone> getTimeZones() {
		return getTimeZones(TimeZone.getAvailableIDs());
	}

	public static ArrayList<TimeZone> getTimeZones(int timeZoneOffset) {
		return getTimeZones(TimeZone.getAvailableIDs(timeZoneOffset));
	}

	private static ArrayList<TimeZone> getTimeZones(String[] availableIDs) {
		ArrayList<TimeZone> result;
		if (availableIDs != null && availableIDs.length > 0) {
			result = new ArrayList<TimeZone>(availableIDs.length);
			for (int i = 0; i < availableIDs.length; i++) {
				result.add(CommonUtil.timeZoneFromString(availableIDs[i]));
			}
		} else {
			result = new ArrayList<TimeZone>();
		}
		return result;
	}

	public static User getUser() {
		return getUserContext().getUser();
		// UserContext userContext = CoreUtil.getUserContext();
		// return (User) userDao.searchUniqueUsername(UserDao.TRANSFORM_NONE, userContext.getUsername());
	}

	public static UserContext getUserContext() {
		UserContext userContext = (UserContext) PrincipalStore.get();
		if (userContext == null) {
			userContext = new UserContext();
			PrincipalStore.set(userContext);
		}
		return userContext;
	}

	private static String getValueObjectClassNameFromEntityName(String entityName, String voTypeSuffix) {
		StringBuilder voClassName = new StringBuilder(VALUE_OBJECTS_PACKAGE_NAME);
		voClassName.append(".");
		voClassName.append(getValueObjectNameFromEntityName(entityName, voTypeSuffix));
		return voClassName.toString();
	}

	public static String getValueObjectNameFromEntityName(String entityName, String voTypeSuffix) {
		StringBuilder voName = new StringBuilder(entityName);
		voName.append(voTypeSuffix);
		return voName.toString();
	}

	public static String getVOClassNameFromEntityName(String entityName) {
		return getValueObjectClassNameFromEntityName(entityName, VO_CLASS_SUFFIX);
	}

	public static boolean isEnumerationClass(String name) {
		return checkClassExists(getEnumerationClassName(name));
	}

	// public static boolean valueObjectClassExists(String entityName) {
	// return classExists(getValueObjectClassNameFromEntityName(entityName,OUT_VO_CLASS_SUFFIX))
	// || classExists(getValueObjectClassNameFromEntityName(entityName,VO_CLASS_SUFFIX));
	// }
	public static boolean isPassDecryption() {
		// return false;
		UserContext userContext = getUserContext();
		if (CommonUtil.isEmptyString(userContext.getRealm())) {
			// skip trusted host check when using dbtool:
			return userContext.getUser().isDecrypt();
		} else if (PASS_DECRYPTION_REALMS.contains(userContext.getRealm())
				&& Settings.getBoolean(SettingCodes.SIGNUP_FROM_UNTRUSTED_HOSTS, Bundle.SETTINGS, DefaultSettings.SIGNUP_FROM_UNTRUSTED_HOSTS)) {
			// skip trusted host check for method used by signup (if enabled):
			return userContext.getUser().isDecrypt();
		} else {
			return userContext.getUser().isDecrypt() && userContext.isTrustedHost();
		}

	}

	public static <E> void modifyVersion(E original, E modified, Timestamp now, User modifiedUser) throws Exception {
		if (original == null) {
			Long id = CommonUtil.getEntityId(modified);
			if (id != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_ID_NOT_NULL, id.toString());
			}
		}
		long modifiedVersion = ((Long) modified.getClass().getMethod(ENTITY_VERSION_GETTER_METHOD_NAME).invoke(modified)).longValue();
		long newVersion = getNewVersionChecked(original, modifiedVersion);
		updateEntity(modified, newVersion, now, modifiedUser);
	}

	public static void modifyVersion(Object entity, long version, Timestamp now, User modifiedUser) throws Exception {
		long newVersion = getNewVersionChecked(entity, version);
		updateEntity(entity, newVersion, now, modifiedUser);
	}

	public static void modifyVersion(Object newEntity, Timestamp now, User modifiedUser) throws Exception {
		modifyVersion(null, newEntity, now, modifiedUser);
	}

	public static byte[] serialize(Serializable value) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(buffer);
		objectStream.writeObject(value);
		// buffer.flush();
		objectStream.close();
		return buffer.toByteArray();
	}

	public static void setUser(AuthenticationVO auth, UserDao userDao) {
		User user = null;
		if (auth != null) {
			try {
				user = (User) userDao.searchUniqueName(UserDao.TRANSFORM_NONE, auth.getUsername());
			} catch (Throwable t) {
			}
		}
		getUserContext().setUser(user);
		// CoreUtil.setUserContext(new UserContext(user,null,null));
	}

	// public static void applyLocaleContext(Locales locale) {
	//
	// CoreUtil.getUserContext().applyLocaleContext(locale);
	// //UserContext userContext = CoreUtil.getUserContext();
	// //return (User) userDao.searchUniqueUsername(UserDao.TRANSFORM_NONE, userContext.getUsername());
	//
	// }
	//
	// public static void clearLocaleContext() {
	//
	// CoreUtil.getUserContext().clearLocaleContext();
	// //UserContext userContext = CoreUtil.getUserContext();
	// //return (User) userDao.searchUniqueUsername(UserDao.TRANSFORM_NONE, userContext.getUsername());
	//
	// }
	// public static Object getFirstArgOfType(Object[] args,Class type) {
	// if (args != null) {
	// for (int i = 0; i < args.length; i++) {
	// if (args[i] != null && args[i].getClass().equals(type)) { // instanceof AuthenticationVO) {
	// return args[i];
	// }
	// }
	// }
	// return null;
	// }
	public static String toXML(Object obj, boolean excludeEncryptedFields) throws Exception {
		XStream xstream = new XStream();
		if (excludeEncryptedFields) {
			OmittedFields.omitFields(xstream);
		}
		xstream.setMode(XStream.ID_REFERENCES);
		return xstream.toXML(obj);
	}

	private static <E> void updateEntity(E entity, long newVersion, Timestamp now, User modifiedUser) throws Exception {
		entity.getClass().getMethod(ENTITY_VERSION_SETTER_METHOD_NAME, long.class).invoke(entity, newVersion);
		entity.getClass().getMethod(ENTITY_MODIFIED_TIMESTAMP_SETTER_METHOD_NAME, Timestamp.class).invoke(entity, now);
		entity.getClass().getMethod(ENTITY_MODIFIED_USER_SETTER_METHOD_NAME, User.class).invoke(entity, modifiedUser);
	}


	private CoreUtil() {
	}
}
