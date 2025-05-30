package org.phoenixctms.ctsms.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueJsonVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SponsoringTypeVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.SurveyStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialTypeVO;
import org.phoenixctms.ctsms.vo.UserInheritedVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.UserSettingsInVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.marcwrobel.jbanking.bic.Bic;
import fr.marcwrobel.jbanking.iban.Iban;

public final class CommonUtil {

	public enum EllipsisPlacement {
		LEADING, MID, TRAILING
	}

	public final static HashSet<String> VO_EQUALS_EXCLUDES = new HashSet<String>();
	static {
		VO_EQUALS_EXCLUDES.add("CourseOutVO.getPrecedingCourses");
		VO_EQUALS_EXCLUDES.add("CriterionOutVO.getCriteria");
		VO_EQUALS_EXCLUDES.add("InputFieldSelectionSetValueOutVO.getField");
		VO_EQUALS_EXCLUDES.add("InventoryOutVO.getParent");
		VO_EQUALS_EXCLUDES.add("ProbandListStatusEntryOutVO.getListEntry");
		VO_EQUALS_EXCLUDES.add("ProbandOutVO.getParents");
		VO_EQUALS_EXCLUDES.add("StaffOutVO.getParent");
		VO_EQUALS_EXCLUDES.add("UserOutVO.getModifiedUser");
		VO_EQUALS_EXCLUDES.add("UserOutVO.getIdentity");
	}
	private final static String UNKNOWN_LOCAL_HOST_NAME = "localhost";
	public final static String LOCAL_HOST_NAME = getLocalHostName();
	public final static String TIME_SEPARATOR = ":";
	public final static String DEFAULT_INPUT_TIME_PATTERN = "HH" + TIME_SEPARATOR + "mm"; // must not be locale dependent
	private final static String DEFAULT_INPUT_DATE_PATTERN = "yyyy-MM-dd"; // "dd.MM.yyyy"; //must not be locale dependent
	private final static String DEFAULT_INPUT_DATETIME_PATTERN = DEFAULT_INPUT_DATE_PATTERN + " " + DEFAULT_INPUT_TIME_PATTERN; // "yyyy-MM-dd HH:mm"; //must not be locale
	public final static String DIGITS_ONLY_DATETIME_PATTERN = "yyyyMMddHHmmss";
	public final static boolean FILE_EXTENSION_REGEXP_MODE = true; // different primefaces versions(?), etc...
	public final static String DEFAULT_FILE_EXTENSION_PATTERN = (FILE_EXTENSION_REGEXP_MODE ? "/(\\.|\\/)([a-zA-Z0-9]+)$/" : "*.*");
	public final static Pattern FILE_EXTENSION_PATTERN_REGEXP = Pattern.compile("^\\*\\.[a-zA-Z0-9]+$");
	public final static boolean HTML_SYSTEM_MESSAGES_COMMENTS = true;
	public final static boolean HTML_AUDIT_TRAIL_CHANGE_COMMENTS = true;
	public final static String NO_SELECTION_VALUE = "";
	public final static String LOGICAL_PATH_SEPARATOR = "/"; // should not interfere with regexp replace strings
	public final static String DEFAULT_ELLIPSIS = "...";
	private final static String PROPERTIES_STRING_LIST_DEFAULT_SEPARATOR = ",";
	private final static Pattern PROPERTIES_STRING_LIST_REGEXP = Pattern.compile(Pattern.quote(PROPERTIES_STRING_LIST_DEFAULT_SEPARATOR));
	private final static String PROPERTIES_NULL_LITERAL = "null";
	public final static long LIST_INITIAL_POSITION = 1L;
	private final static String FILE_PATH_SEPARATOR_PATTERN = "[/\\\\]+";
	private final static String UNIX_FILE_PATH_SEPARATOR = "/";
	public final static String[] FILE_PATH_SEPARATORS = { UNIX_FILE_PATH_SEPARATOR, "\\" };
	private final static String ILLEGAL_FILENAME_CHARS_PATTERN = "[^a-zA-Z0-9.-]";
	private final static String DEFAULT_FILENAME_ESCAPE_CHAR = "_";
	// the only custom error messages that are not localized.
	private final static String INVALID_LONG_CAST = "long ({0}) cannot be converted to int";
	public static final String INPUT_TYPE_NOT_SUPPORTED = "type {0} not supported";
	public static final String UNSUPPORTED_CRITERION_VALUE_TYPE = "unsupported criterion value type {0}";
	private final static String INVALID_MESSAGE_FORMAT_PATTERN = "invalid message format pattern {0}";
	private final static String INVALID_SQL_LIKE_PATTERN = "invalid sql like pattern {0}";
	private final static HashSet<org.phoenixctms.ctsms.enumeration.CriterionRestriction> UNARY_RESTRICTIONS = new HashSet<org.phoenixctms.ctsms.enumeration.CriterionRestriction>();
	static {
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.TRUE);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EMPTY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NOT_EMPTY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NOT_NULL);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NULL);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_GT_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_GE_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_LT_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_LE_TODAY);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_GT_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_GE_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_LT_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_LE_NOW);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_CONTEXT_USER_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_CONTEXT_USER_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_EQ_CONTEXT_USER_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_NE_CONTEXT_USER_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_CONTEXT_USER_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_CONTEXT_USER_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_EQ_CONTEXT_USER_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_NE_CONTEXT_USER_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_CONTEXT_IDENTITY_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_CONTEXT_IDENTITY_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_EQ_CONTEXT_IDENTITY_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_NE_CONTEXT_IDENTITY_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_EQ_CONTEXT_IDENTITY_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_NE_CONTEXT_IDENTITY_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_EQ_CONTEXT_IDENTITY_DEPARTMENT_ID);
		UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_NE_CONTEXT_IDENTITY_DEPARTMENT_ID);
		// UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_EQ_ENTITY_ID);
		// UNARY_RESTRICTIONS.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.IS_ID_NE_ENTITY_ID);
	}
	private final static HashSet<org.phoenixctms.ctsms.enumeration.CriterionTie> BLANK_TIES = new HashSet<org.phoenixctms.ctsms.enumeration.CriterionTie>();
	static {
		BLANK_TIES.add(org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS);
		BLANK_TIES.add(org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS);
		BLANK_TIES.add(org.phoenixctms.ctsms.enumeration.CriterionTie.UNION);
		BLANK_TIES.add(org.phoenixctms.ctsms.enumeration.CriterionTie.INTERSECT);
		BLANK_TIES.add(org.phoenixctms.ctsms.enumeration.CriterionTie.EXCEPT);
	}
	public final static String BASE64_CHARSET = "UTF8";
	public final static BundleControl BUNDLE_CONTROL = new BundleControl();
	public final static String SET_PROPERTY_METHOD_NAME_PREFIX = "set";
	public final static String GET_PROPERTY_METHOD_NAME_PREFIX = "get";
	public final static String IS_PROPERTY_METHOD_NAME_PREFIX = "is";
	private final static String VO_ID_GETTER_METHOD_NAME = GET_PROPERTY_METHOD_NAME_PREFIX + "Id";
	private static final String ENTITY_ID_GETTER_METHOD_NAME = GET_PROPERTY_METHOD_NAME_PREFIX + "Id";
	private static final String VO_POSITION_GETTER_METHOD_NAME = GET_PROPERTY_METHOD_NAME_PREFIX + "Position";
	private static final String ENTITY_POSITION_GETTER_METHOD_NAME = GET_PROPERTY_METHOD_NAME_PREFIX + "Position";
	public final static Pattern VO_GETTER_METHOD_NAME_REGEXP = Pattern.compile("^" + GET_PROPERTY_METHOD_NAME_PREFIX); // Pattern.compile("^((get)|(is))");
	public final static Pattern ENTITY_GETTER_METHOD_NAME_REGEXP = Pattern.compile("^((" + GET_PROPERTY_METHOD_NAME_PREFIX + ")|(" + IS_PROPERTY_METHOD_NAME_PREFIX + "))");
	public final static boolean ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON = false;
	private final static HashSet<org.phoenixctms.ctsms.enumeration.FileModule> ENCRYPTED_FILE_MODULE = new HashSet<org.phoenixctms.ctsms.enumeration.FileModule>();
	static {
		ENCRYPTED_FILE_MODULE.add(org.phoenixctms.ctsms.enumeration.FileModule.PROBAND_DOCUMENT);
		// ENCRYPTED_FILE_MODULE.add(org.phoenixctms.ctsms.enumeration.FileModule.TRIAL_DOCUMENT); //enable encryption by adding module here
	}
	private final static HashSet<org.phoenixctms.ctsms.enumeration.JournalModule> ENCRYPTED_JOURNAL_MODULE = new HashSet<org.phoenixctms.ctsms.enumeration.JournalModule>();
	static {
		ENCRYPTED_JOURNAL_MODULE.add(org.phoenixctms.ctsms.enumeration.JournalModule.PROBAND_JOURNAL);
		ENCRYPTED_JOURNAL_MODULE.add(org.phoenixctms.ctsms.enumeration.JournalModule.TRIAL_JOURNAL);
		ENCRYPTED_JOURNAL_MODULE.add(org.phoenixctms.ctsms.enumeration.JournalModule.MASS_MAIL_JOURNAL);
	}
	private static final Iterator<Object> EMPTY_ITERATOR = new Iterator<Object>() {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException();
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}
	};
	public final static int INPUTSTREAM_BUFFER_BLOCKSIZE = 512 * 1024; // 1024*1024*4;
	private static final boolean SET_OPERATION_EXPRESSION_SELECT_LABEL_ALPHABETIC = true;
	private static final String SET_OPERATION_EXPRESSION_SELECT_LABEL = "{0}"; // "select {0}";
	public static final int SET_OPERATION_EXPRESSION_FIRST_INDEX = 1;
	private final static String INK_VALUE_CHARSET = "UTF8";
	private final static String ASP_SUBSTANCES_SEPARATOR = "; ";
	private final static String LINE_BREAK_SPLIT_REGEXP_PATTERN = "(\\r\\n)|(\\r)|(\\n)";
	// private final static String LINE_BREAK_SPLIT_EXTRACT_LINE_SEPARATORS_REGEXP_PATTERN = "((?<=(" + LINE_BREAK_SPLIT_REGEXP_PATTERN + "))|(?=(" +
	// LINE_BREAK_SPLIT_REGEXP_PATTERN
	// + ")))";
	private final static Pattern LINE_BREAK_SPLIT_REGEXP = Pattern.compile(LINE_BREAK_SPLIT_REGEXP_PATTERN);
	private final static StringSplitter LINE_BREAK_KEEP_SEPARATORS_SPLITTER = new StringSplitter(LINE_BREAK_SPLIT_REGEXP, true);
	private static final String HEX_DIGITS = "0123456789ABCDEF";
	public final static String GIF_FILENAME_EXTENSION = "gif";
	public static final String GIF_MIMETYPE_STRING = "image/gif";
	public static final String PNG_FILENAME_EXTENSION = "png";
	public static final String PNG_MIMETYPE_STRING = "image/png";
	public static final String HTML_MIMETYPE_STRING = "text/html";
	public static final String BEACON_PATH = "beacon";
	public static final String UNSUBSCRIBE_PATH = "unsubscribe";
	public static final String CONFIRM_PATH = "confirm";
	private final static Pattern MESSAGE_FORMAT_PLACEHOLDER_REGEXP = Pattern.compile("(\\{\\d+\\})");
	public static String SQL_LIKE_PERCENT_WILDCARD = "%";
	public static String SQL_LIKE_UNDERSCORE_WILDCARD = "_";
	private final static Pattern SQL_LIKE_WILDCARD_REGEXP = Pattern.compile("(" + SQL_LIKE_PERCENT_WILDCARD + "|" + SQL_LIKE_UNDERSCORE_WILDCARD + ")");
	public static final String LOCAL_HOST_ADDRESS = getLocalHostAddress();
	private static final String ECRF_NAME = "{0}";
	private static final String ECRF_VISIT_NAME = "{0}@{1}";
	public static Set<String> USER_INHERITABLE_PROPERTIES = new HashSet<String>();
	public static Set<String> USER_SETTINGS_INHERITABLE_PROPERTIES = new HashSet<String>();
	static {
		MethodTransfilter transFilter = MethodTransfilter.getVoMethodTransfilter(false);
		try {
			Iterator<Method> it = AssociationPath.listMethods(UserInheritedVO.class, transFilter).iterator();
			while (it.hasNext()) {
				USER_INHERITABLE_PROPERTIES.add(transFilter.transform(it.next().getName()));
			}
			// derive properties allowed to inherit/override by any user directly from UserSettingsInVO:
			it = AssociationPath.listMethods(UserSettingsInVO.class, transFilter).iterator();
			while (it.hasNext()) {
				USER_SETTINGS_INHERITABLE_PROPERTIES.add(transFilter.transform(it.next().getName()));
			}
		} catch (Exception e) {
		}
	}
	private final static Detector MIME_DETECTOR = new DefaultDetector(); // All build-in Tika detectors are thread-safe, so it is ok to share the detector globally.
	private final static int SHORT_UUID_LENGTH = 8;
	private final static String COUNT_LIMIT_EXCEEDED_PREFIX = "";
	private final static String COUNT_LIMIT_EXCEEDED_SUFFIX = "+";

	public static String toCountLimitExceeded(int count) {
		return COUNT_LIMIT_EXCEEDED_PREFIX + Integer.toString(count) + COUNT_LIMIT_EXCEEDED_SUFFIX;
	}

	public static long fromCountLimitExceeded(Object countSafe) {
		try {
			return Long.parseLong(countSafe.toString()
					.replaceFirst(Pattern.quote("^" + COUNT_LIMIT_EXCEEDED_PREFIX), "")
					.replaceFirst(Pattern.quote(COUNT_LIMIT_EXCEEDED_SUFFIX) + "$", ""));
		} catch (Exception e) {
		}
		return 0;
	}

	public static String getMimeType(byte[] data, String fileName) throws Throwable {
		TikaInputStream tikaStream = null;
		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, fileName);
		try {
			tikaStream = TikaInputStream.get(data, metadata);
			return MIME_DETECTOR.detect(tikaStream, metadata).toString();
		} catch (Throwable t) {
			throw t;
		} finally {
			if (tikaStream != null) {
				try {
					tikaStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getMimeType(File file) throws Throwable {
		TikaInputStream tikaStream = null;
		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());
		try {
			tikaStream = TikaInputStream.get(file, metadata);
			return MIME_DETECTOR.detect(tikaStream, metadata).toString();
		} catch (Throwable t) {
			throw t;
		} finally {
			if (tikaStream != null) {
				try {
					tikaStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getEcrfVisitName(ECRFOutVO ecrfVO, VisitOutVO visitVO) {
		return getEcrfVisitName(ecrfVO != null ? ecrfVO.getName() : null, visitVO != null ? visitVO.getToken() : null, ecrfVO != null ? ecrfVO.getVisits().size() : null);
	}

	public static String getEcrfVisitName(String ecrfName, String visitToken, Integer visits) {
		if (!isEmptyString(ecrfName)) {
			if (visits != null && visits > 1 && !isEmptyString(visitToken)) {
				return MessageFormat.format(ECRF_VISIT_NAME, ecrfName, visitToken);
			} else {
				return MessageFormat.format(ECRF_NAME, ecrfName);
			}
		}
		return "";
	}

	public static final String UNIQUE_ECRF_NAME_SECTION_ECRF_FIELD_NAME = "{0} - {1} - {2} - {3}. {4}";
	public static final String UNIQUE_ECRF_NAME_ECRF_FIELD_NAME = "{0} - {1} - {2}. {3}";
	public static final String UNIQUE_ECRF_NAME_REVISION_SECTION_ECRF_FIELD_NAME = "{0} - {1} ({2}) - {3} - {4}. {5}";
	public static final String UNIQUE_ECRF_NAME_REVISION_ECRF_FIELD_NAME = "{0} - {1} ({2}) - {3}. {4}";

	public static String getUniqueECRFFieldName(ECRFFieldOutVO ecrfFieldVO, VisitOutVO visit) {
		if (ecrfFieldVO != null && ecrfFieldVO.getTrial() != null && ecrfFieldVO.getField() != null && ecrfFieldVO.getEcrf() != null) {
			String section = ecrfFieldVO.getSection();
			String revision = ecrfFieldVO.getEcrf().getRevision();
			if (section != null && section.length() > 0) {
				if (revision != null && revision.length() > 0) {
					return MessageFormat.format(UNIQUE_ECRF_NAME_REVISION_SECTION_ECRF_FIELD_NAME, ecrfFieldVO.getTrial().getName(), getEcrfVisitName(ecrfFieldVO.getEcrf(), visit),
							revision,
							section,
							Long.toString(ecrfFieldVO.getPosition()), ecrfFieldVO.getField().getName());
				} else {
					return MessageFormat.format(UNIQUE_ECRF_NAME_SECTION_ECRF_FIELD_NAME, ecrfFieldVO.getTrial().getName(), getEcrfVisitName(ecrfFieldVO.getEcrf(), visit), section,
							Long.toString(ecrfFieldVO.getPosition()), ecrfFieldVO.getField().getName());
				}
			} else {
				if (revision != null && revision.length() > 0) {
					return MessageFormat.format(UNIQUE_ECRF_NAME_REVISION_ECRF_FIELD_NAME, ecrfFieldVO.getTrial().getName(), getEcrfVisitName(ecrfFieldVO.getEcrf(), visit),
							revision,
							Long.toString(ecrfFieldVO.getPosition()), ecrfFieldVO.getField().getName());
				} else {
					return MessageFormat.format(UNIQUE_ECRF_NAME_ECRF_FIELD_NAME, ecrfFieldVO.getTrial().getName(), getEcrfVisitName(ecrfFieldVO.getEcrf(), visit),
							Long.toString(ecrfFieldVO.getPosition()), ecrfFieldVO.getField().getName());
				}
			}
		}
		return null;
	}

	private static void appendProbandAlias(StringBuilder sb, ProbandOutVO proband, String newBlindedProbandNameLabel, String blindedProbandNameLabel) {
		String alias = proband.getAlias();
		if (alias != null && alias.trim().length() > 0) {
			sb.append(alias.trim());
		} else if (proband.getId() > 0) {
			if (blindedProbandNameLabel != null) {
				sb.append(MessageFormat.format(blindedProbandNameLabel, Long.toString(proband.getId())));
			} else {
				sb.append(Long.toString(proband.getId()));
			}
		} else if (newBlindedProbandNameLabel != null) {
			sb.append(newBlindedProbandNameLabel);
		}
	}

	public static boolean appendString(StringBuilder sb, String string, String separator) {
		return appendString(sb, string, separator, null);
	}

	public static boolean appendString(StringBuilder sb, String string, String separator, String placeHolder) {
		boolean a = (string != null && string.length() > 0);
		boolean b = (placeHolder != null && placeHolder.length() > 0);
		if (a || b) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (a) {
				sb.append(string);
			} else {
				sb.append(placeHolder);
			}
			return true;
		}
		return false;
	}

	public static String aspSubstanceVOCollectionToString(Collection<AspSubstanceVO> aspSubstanceVOs) {
		return aspSubstanceVOCollectionToString(aspSubstanceVOs, ASP_SUBSTANCES_SEPARATOR);
	}

	public static String aspSubstanceVOCollectionToString(Collection<AspSubstanceVO> aspSubstanceVOs, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<AspSubstanceVO> aspSubstanceVOsIt = aspSubstanceVOs.iterator();
		while (aspSubstanceVOsIt.hasNext()) {
			sb.append(aspSubstanceVOsIt.next().getName());
			if (aspSubstanceVOsIt.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static boolean bundleContainsKey(String l10nKey, ResourceBundle bundle) {
		if (l10nKey == null || l10nKey.trim().length() == 0) {
			return false;
		}
		if (bundle == null) {
			return false;
		}
		return bundle.containsKey(l10nKey);
	}

	public static String capitalizeFirstChar(String string, boolean upperCase) {
		if (string != null) {
			if (string.length() > 0) {
				StringBuilder result = new StringBuilder();
				if (upperCase) {
					result.append(Character.toUpperCase(string.charAt(0)));
				} else {
					result.append(Character.toLowerCase(string.charAt(0)));
				}
				result.append(string.substring(1));
				return result.toString();
			} else {
				return "";
			}
		}
		return null;
	}

	public static boolean checkBic(String bic) {
		return Bic.isValid(bic);
	}

	public static boolean checkIban(String iban) {
		return Iban.isValid(iban);
	}

	public static String clipString(String string, int length) {
		return clipString(string, length, DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING);
	}

	public static String clipString(String string, int length, EllipsisPlacement ellipsisPlacement) {
		return clipString(string, length, DEFAULT_ELLIPSIS, ellipsisPlacement);
	}

	public static String clipString(String string, int length, String ellipsis, EllipsisPlacement ellipsisPlacement) {
		StringBuilder sb = new StringBuilder();
		if (ellipsis != null && ellipsis.length() > 0) {
			if (string != null) {
				if (string.length() > length - ellipsis.length()) {
					switch (ellipsisPlacement) {
						case LEADING:
							sb.append(ellipsis);
							sb.append(string.substring(string.length() - (length - ellipsis.length())));
							break;
						case MID:
							sb.append(string.substring(0, (int) Math.ceil((length - ellipsis.length()) / 2.0f)));
							sb.append(ellipsis);
							sb.append(string.substring(string.length() - (int) Math.floor((length - ellipsis.length()) / 2.0f)));
							break;
						case TRAILING:
							sb.append(string.substring(0, length - ellipsis.length()));
							sb.append(ellipsis);
							break;
						default:
							sb.append(string);
					}
				} else {
					sb.append(string);
				}
			}
		} else {
			if (string != null) {
				if (string.length() > length) {
					switch (ellipsisPlacement) {
						case LEADING:
							sb.append(string.substring(string.length() - length));
							break;
						case MID:
							sb.append(string.substring(0, (int) Math.ceil(length / 2.0f)));
							sb.append(string.substring(string.length() - (int) Math.floor(length / 2.0f)));
							break;
						case TRAILING:
							sb.append(string.substring(0, length));
							break;
						default:
							sb.append(string);
					}
				} else {
					sb.append(string);
				}
			}
		}
		return sb.toString();
	}

	public static java.awt.Color convertColor(org.phoenixctms.ctsms.enumeration.Color color) {
		String hexValue = color.getValue();
		int r = Integer.parseInt(hexValue.substring(0, 2), 16);
		int g = Integer.parseInt(hexValue.substring(2, 4), 16);
		int b = Integer.parseInt(hexValue.substring(4, 6), 16);
		return new java.awt.Color(r, g, b);
	}

	private static void copyInquiryValueInToOut(InquiryValueOutVO out, InquiryValueInVO in, InquiryOutVO inquiryVO, ProbandOutVO probandVO, UserOutVO modifiedUserVO, Date now) {
		if (in != null && out != null) {
			out.setId(in.getId()); // nullable!
			out.setBooleanValue(in.getBooleanValue());
			out.setDateValue(in.getDateValue());
			out.setTimeValue(in.getTimeValue());
			out.setFloatValue(in.getFloatValue());
			out.setTextValue(in.getTextValue());
			out.setTimestampValue(in.getTimestampValue());
			out.setLongValue(in.getLongValue());
			out.setInkValues(in.getInkValues());
			out.setModifiedTimestamp(now);
			out.setVersion(in.getVersion());
			out.setProband(probandVO);
			out.setModifiedUser(modifiedUserVO);
			out.getSelectionValues().clear();
			if (inquiryVO != null) {
				out.setInquiry(inquiryVO);
				InputFieldOutVO inputField = inquiryVO.getField();
				if (inputField != null) {
					HashSet<Long> ids = new HashSet<Long>(in.getSelectionValueIds());
					Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
					while (it.hasNext()) {
						InputFieldSelectionSetValueOutVO selectionValue = it.next();
						if (ids.contains(selectionValue.getId())) {
							out.getSelectionValues().add(selectionValue);
						}
					}
				}
			}
		}
	}

	public static void copyInheritedUserToOut(UserInheritedVO in, UserOutVO out) {
		if (in != null && out != null) {
			out.setId(in.getId());
			out.setName(in.getName());
			out.setEnableInventoryModule(in.getEnableInventoryModule());
			out.setEnableStaffModule(in.getEnableStaffModule());
			out.setEnableCourseModule(in.getEnableCourseModule());
			out.setEnableTrialModule(in.getEnableTrialModule());
			out.setEnableInputFieldModule(in.getEnableInputFieldModule());
			out.setEnableProbandModule(in.getEnableProbandModule());
			out.setEnableMassMailModule(in.getEnableMassMailModule());
			out.setEnableUserModule(in.getEnableUserModule());
			out.setVisibleInventoryTabList(in.getVisibleInventoryTabList());
			out.setVisibleStaffTabList(in.getVisibleStaffTabList());
			out.setVisibleCourseTabList(in.getVisibleCourseTabList());
			out.setVisibleTrialTabList(in.getVisibleTrialTabList());
			out.setVisibleProbandTabList(in.getVisibleProbandTabList());
			out.setVisibleInputFieldTabList(in.getVisibleInputFieldTabList());
			out.setVisibleUserTabList(in.getVisibleUserTabList());
			out.setVisibleMassMailTabList(in.getVisibleMassMailTabList());
			out.setDecrypt(in.getDecrypt());
			out.setDecryptUntrusted(in.getDecryptUntrusted());
			out.setLocked(in.getLocked());
			out.setLockedUntrusted(in.getLockedUntrusted());
			out.setTimeZone(in.getTimeZone());
			out.setLocale(in.getLocale());
			out.setShowTooltips(in.getShowTooltips());
			out.setTheme(in.getTheme());
			out.setTabOrientation(in.getTabOrientation());
			out.setDecimalSeparator(in.getDecimalSeparator());
			out.setDateFormat(in.getDateFormat());
		}
	}

	public static void copyTrialOutToIn(TrialInVO in, TrialOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			TrialStatusTypeVO statusVO = out.getStatus();
			TrialTypeVO typeVO = out.getType();
			SponsoringTypeVO sponsoringVO = out.getSponsoring();
			SurveyStatusTypeVO surveyStatusVO = out.getSurveyStatus();
			VariablePeriodVO blockingPeriodVO = out.getBlockingPeriod();
			RandomizationModeVO randomizationVO = out.getRandomization();
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setDescription(out.getDescription());
			in.setSignupProbandList(out.getSignupProbandList());
			in.setSignupInquiries(out.getSignupInquiries());
			in.setSignupRandomize(out.getSignupRandomize());
			in.setSignupDescription(out.getSignupDescription());
			in.setId(out.getId());
			in.setName(out.getName());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
			in.setTitle(out.getTitle());
			in.setVersion(out.getVersion());
			in.setTypeId(typeVO == null ? null : typeVO.getId());
			in.setSponsoringId(sponsoringVO == null ? null : sponsoringVO.getId());
			in.setSurveyStatusId(surveyStatusVO == null ? null : surveyStatusVO.getId());
			in.setExclusiveProbands(out.getExclusiveProbands());
			in.setProbandAliasFormat(out.getProbandAliasFormat());
			in.setBlockingPeriod(blockingPeriodVO == null ? null : blockingPeriodVO.getPeriod());
			in.setBlockingPeriodDays(out.getBlockingPeriodDays());
			in.setDutySelfAllocationLocked(out.getDutySelfAllocationLocked());
			in.setDutySelfAllocationLockedUntil(out.getDutySelfAllocationLockedUntil());
			in.setDutySelfAllocationLockedFrom(out.getDutySelfAllocationLockedFrom());
			in.setRandomization(randomizationVO == null ? null : randomizationVO.getMode());
			in.setRandomizationList(null); // out.getRandomizationList());
		}
	}

	public static void copyEcrfFieldValueInToJson(ECRFFieldValueJsonVO out, ECRFFieldValueInVO in, ECRFFieldOutVO ecrfFieldVO, Long visitId) {
		if (in != null && out != null) {
			out.setId(in.getId());
			out.setIndex(in.getIndex());
			out.setBooleanValue(in.getBooleanValue());
			out.setDateValue(in.getDateValue());
			out.setTimeValue(in.getTimeValue());
			out.setFloatValue(in.getFloatValue());
			out.setTextValue(in.getTextValue());
			out.setTimestampValue(in.getTimestampValue());
			out.setLongValue(in.getLongValue());
			out.setInkValues(in.getInkValues());
			out.setSelectionValueIds(in.getSelectionValueIds());
			if (ecrfFieldVO != null) {
				out.setEcrfFieldId(ecrfFieldVO.getId());
				out.setSeries(ecrfFieldVO.getSeries());
				out.setPosition(ecrfFieldVO.getPosition());
				out.setJsVariableName(ecrfFieldVO.getJsVariableName());
				out.setJsValueExpression(ecrfFieldVO.getJsValueExpression());
				out.setJsOutputExpression(ecrfFieldVO.getJsOutputExpression());
				out.setSection(ecrfFieldVO.getSection());
				//out.setRef(ecrfFieldVO.getRef());
				ECRFOutVO ecrfVO = ecrfFieldVO.getEcrf();
				if (ecrfVO != null) {
					Iterator it = ecrfVO.getGroups().iterator();
					out.getProbandGroupTokens().clear();
					while (it.hasNext()) {
						out.getProbandGroupTokens().add(((ProbandGroupOutVO) it.next()).getToken());
					}
					it = ecrfVO.getVisits().iterator();
					out.getVisitTokens().clear();
					out.setVisitToken(null);
					while (it.hasNext()) {
						VisitOutVO visit = (VisitOutVO) it.next();
						out.getVisitTokens().add(visit.getToken());
						if (visitId != null && visitId.equals(visit.getId())) {
							out.setVisitToken(visit.getToken());
						}
					}
				}
				out.setDisabled(ecrfFieldVO.getDisabled());
				InputFieldOutVO inputField = ecrfFieldVO.getField();
				if (inputField != null) {
					out.setInputFieldId(inputField.getId());
					out.setInputFieldType(inputField.getFieldType().getType());
					out.setInputFieldName(inputField.getName());
					out.setUserTimeZone(inputField.getUserTimeZone());
					copySelectionSetValuesOutToJson(inputField.getSelectionSetValues(), out.getInputFieldSelectionSetValues());
				}
			}
			out.setVisitId(visitId);
		}
	}

	public static void copyInquiryValueInToJson(InquiryValueJsonVO out, InquiryValueInVO in, InquiryOutVO inquiryVO) {
		if (in != null && out != null) {
			out.setId(in.getId()); // nullable!
			out.setBooleanValue(in.getBooleanValue());
			out.setDateValue(in.getDateValue());
			out.setTimeValue(in.getTimeValue());
			out.setFloatValue(in.getFloatValue());
			out.setTextValue(in.getTextValue());
			out.setTimestampValue(in.getTimestampValue());
			out.setLongValue(in.getLongValue());
			out.setInkValues(in.getInkValues());
			out.setSelectionValueIds(in.getSelectionValueIds());
			if (inquiryVO != null) {
				out.setInquiryId(inquiryVO.getId());
				out.setPosition(inquiryVO.getPosition());
				out.setJsVariableName(inquiryVO.getJsVariableName());
				out.setJsValueExpression(inquiryVO.getJsValueExpression());
				out.setJsOutputExpression(inquiryVO.getJsOutputExpression());
				out.setCategory(inquiryVO.getCategory());
				out.setDisabled(inquiryVO.getDisabled());
				InputFieldOutVO inputField = inquiryVO.getField();
				if (inputField != null) {
					out.setInputFieldId(inputField.getId());
					out.setInputFieldType(inputField.getFieldType().getType());
					out.setInputFieldName(inputField.getName());
					out.setUserTimeZone(inputField.getUserTimeZone());
					copySelectionSetValuesOutToJson(inputField.getSelectionSetValues(), out.getInputFieldSelectionSetValues());
				}
			}
		}
	}

	public static void copyProbandListEntryTagValueInToJson(ProbandListEntryTagValueJsonVO out, ProbandListEntryTagValueInVO in, ProbandListEntryTagOutVO tagVO) {
		if (in != null && out != null) {
			out.setId(in.getId());
			out.setBooleanValue(in.getBooleanValue());
			out.setDateValue(in.getDateValue());
			out.setTimeValue(in.getTimeValue());
			out.setFloatValue(in.getFloatValue());
			out.setTextValue(in.getTextValue());
			out.setTimestampValue(in.getTimestampValue());
			out.setLongValue(in.getLongValue());
			out.setInkValues(in.getInkValues());
			out.setSelectionValueIds(in.getSelectionValueIds());
			if (tagVO != null) {
				out.setTagId(tagVO.getId());
				out.setPosition(tagVO.getPosition());
				out.setJsVariableName(tagVO.getJsVariableName());
				out.setJsValueExpression(tagVO.getJsValueExpression());
				out.setJsOutputExpression(tagVO.getJsOutputExpression());
				out.setDisabled(tagVO.getDisabled());
				InputFieldOutVO inputField = tagVO.getField();
				if (inputField != null) {
					out.setInputFieldId(inputField.getId());
					out.setInputFieldType(inputField.getFieldType().getType());
					out.setInputFieldName(inputField.getName());
					out.setUserTimeZone(inputField.getUserTimeZone());
					copySelectionSetValuesOutToJson(inputField.getSelectionSetValues(), out.getInputFieldSelectionSetValues());
				}
			}
		}
	}

	public static void copySelectionSetValueOutToJson(InputFieldSelectionSetValueOutVO in, InputFieldSelectionSetValueJsonVO out) {
		if (in != null && out != null) {
			out.setId(in.getId());
			out.setName(in.getName());
			out.setPreset(in.getPreset());
			out.setValue(in.getValue());
			out.setInkRegions(in.getInkRegions());
			out.setStrokesId(in.getStrokesId());
		}
	}

	public static void copySelectionSetValuesOutToJson(Collection<InputFieldSelectionSetValueOutVO> inValues, Collection<InputFieldSelectionSetValueJsonVO> outValues) {
		if (outValues != null) {
			outValues.clear();
			if (inValues != null) {
				Iterator<InputFieldSelectionSetValueOutVO> it = inValues.iterator();
				while (it.hasNext()) {
					InputFieldSelectionSetValueJsonVO out = new InputFieldSelectionSetValueJsonVO();
					copySelectionSetValueOutToJson(it.next(), out);
					outValues.add(out);
				}
			}
		}
	}

	public static String courseOutVOToString(CourseOutVO course) {
		if (course != null) {
			return course.getName();
		}
		return null;
	}

	public static Pattern createMessageFormatRegexp(String messageFormat, boolean ignoreCase) throws Exception {
		Matcher matcher = MESSAGE_FORMAT_PLACEHOLDER_REGEXP.matcher(messageFormat);
		StringBuilder messageFormatPattern = new StringBuilder();
		messageFormatPattern.append("^");
		int lastMatch = 0;
		while (matcher.find()) {
			messageFormatPattern.append(messageFormat.substring(lastMatch, matcher.start()).length() > 0 ? Pattern.quote(messageFormat.substring(lastMatch, matcher.start())) : "");
			messageFormatPattern.append("(.*)");
			lastMatch = matcher.end();
		}
		messageFormatPattern.append(messageFormat.substring(lastMatch).length() > 0 ? Pattern.quote(messageFormat.substring(lastMatch)) : "");
		messageFormatPattern.append("$");
		try {
			if (ignoreCase) {
				return Pattern.compile(messageFormatPattern.toString(), Pattern.CASE_INSENSITIVE);
			} else {
				return Pattern.compile(messageFormatPattern.toString());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(MessageFormat.format(INVALID_MESSAGE_FORMAT_PATTERN, messageFormatPattern.toString()));
		}
	}

	public static Pattern createSqlLikeRegexp(String queryString, boolean ignoreCase) throws Exception {
		Matcher matcher = SQL_LIKE_WILDCARD_REGEXP.matcher(queryString);
		StringBuilder sqlLikePattern = new StringBuilder();
		sqlLikePattern.append("^");
		int lastMatch = 0;
		while (matcher.find()) {
			sqlLikePattern.append(queryString.substring(lastMatch, matcher.start()).length() > 0 ? Pattern.quote(queryString.substring(lastMatch, matcher.start())) : "");
			if (SQL_LIKE_PERCENT_WILDCARD.equals(matcher.group())) {
				sqlLikePattern.append("(.*)");
			} else if (SQL_LIKE_UNDERSCORE_WILDCARD.equals(matcher.group())) {
				sqlLikePattern.append("(.)");
				// } else {
				// sqlLikePattern.append(matcher.group());
			}
			lastMatch = matcher.end();
		}
		sqlLikePattern.append(queryString.substring(lastMatch).length() > 0 ? Pattern.quote(queryString.substring(lastMatch)) : "");
		sqlLikePattern.append("$");
		try {
			if (ignoreCase) {
				return Pattern.compile(sqlLikePattern.toString(), Pattern.CASE_INSENSITIVE);
			} else {
				return Pattern.compile(sqlLikePattern.toString());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(MessageFormat.format(INVALID_SQL_LIKE_PATTERN, queryString.toString()));
		}
	}

	public static String escapeSqlLikeWildcards(String queryString) throws Exception {
		return escapeSqlLikeWildcards(queryString, "\\");
	}

	public static String escapeSqlLikeWildcards(String queryString, String escape) throws Exception {
		if (escape != null && escape.length() > 0) {
			Matcher matcher = SQL_LIKE_WILDCARD_REGEXP.matcher(queryString);
			StringBuilder sqlLikePattern = new StringBuilder();
			int lastMatch = 0;
			while (matcher.find()) {
				sqlLikePattern.append(queryString.substring(lastMatch, matcher.start()).length() > 0 ? queryString.substring(lastMatch, matcher.start()) : "");
				if (SQL_LIKE_PERCENT_WILDCARD.equals(matcher.group())) {
					sqlLikePattern.append(escape);
					sqlLikePattern.append(SQL_LIKE_PERCENT_WILDCARD);
				} else if (SQL_LIKE_UNDERSCORE_WILDCARD.equals(matcher.group())) {
					sqlLikePattern.append(escape);
					sqlLikePattern.append(SQL_LIKE_UNDERSCORE_WILDCARD);
				}
				lastMatch = matcher.end();
			}
			sqlLikePattern.append(queryString.substring(lastMatch).length() > 0 ? queryString.substring(lastMatch) : "");
			return sqlLikePattern.toString();
		}
		return queryString;
	}

	public static String criteriaOutVOToString(CriteriaOutVO criteria) {
		if (criteria != null) {
			return criteria.getLabel();
		}
		return null;
	}

	public static long dateDeltaSecs(Date start, Date stop) {
		return (stop.getTime() - start.getTime()) / 1000;
	}

	public static Timestamp dateToTimestamp(Date date) {
		if (date != null) {
			return new Timestamp(date.getTime());
		} else {
			return null;
		}
	}

	public static String fixLogicalPathFolderName(String logicalPath) {
		return fixLogicalPathFolderName(logicalPath, true);
	}

	public static String fixLogicalPathFolderName(String logicalPath, boolean fixTrailing) {
		if (logicalPath != null && logicalPath.length() > 0) {
			String result = logicalPath.replaceAll(java.util.regex.Pattern.quote(LOGICAL_PATH_SEPARATOR) + "+", java.util.regex.Matcher.quoteReplacement(LOGICAL_PATH_SEPARATOR));
			if (!result.startsWith(LOGICAL_PATH_SEPARATOR)) {
				result = LOGICAL_PATH_SEPARATOR + result;
			}
			if (fixTrailing && !result.endsWith(LOGICAL_PATH_SEPARATOR) && result.length() > 1) {
				result = result + LOGICAL_PATH_SEPARATOR;
			}
			return result;
		} else {
			return LOGICAL_PATH_SEPARATOR;
		}
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String formatDate(Date date, String pattern, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		return sdf.format(date);
	}

	public static String formatDate(Date date, String pattern, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	public static String formatDate(Date date, String pattern, Locale locale, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	public static String formatDecimal(String decimalValue, String userDecimalSeparator) {
		if (decimalValue != null && userDecimalSeparator != null && userDecimalSeparator.length() > 0) {
			return decimalValue.replace('.', userDecimalSeparator.charAt(0));
		}
		return decimalValue;
	}

	public static String formatDouble(Double doubleValue, String userDecimalSeparator) {
		String result = null;
		if (doubleValue != null) {
			result = formatDecimal(doubleValue.toString(), userDecimalSeparator);
		}
		return result;
	}

	public static String formatFloat(Float floatValue, String userDecimalSeparator) {
		String result = null;
		if (floatValue != null) {
			result = formatDecimal(floatValue.toString(), userDecimalSeparator);
		}
		return result;
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateShortUUID() {
		return generateUUID(SHORT_UUID_LENGTH);
	}

	public static String generateUUID(int n) { // for cosmetic reasons, lets use the same trivial method as in sql
		return generateUUID().replace("-", "").substring(0, n);
	}

	public static Integer getAge(Date dateOfBirth) {
		return getAge(dateOfBirth, null);
	}

	public static Integer getAge(Date dateOfBirth, Date reference) {
		if (dateOfBirth != null) {
			GregorianCalendar today = new GregorianCalendar();
			GregorianCalendar dob = new GregorianCalendar();
			if (reference != null) {
				today.setTime(reference);
				dob.setTime(reference);
			}
			int sign;
			if (dateOfBirth.compareTo(today.getTime()) > 0) {
				sign = -1;
				today.setTime(dateOfBirth);
			} else {
				sign = 1;
				dob.setTime(dateOfBirth);
			}
			int year1 = today.get(Calendar.YEAR);
			int year2 = dob.get(Calendar.YEAR);
			int age = year1 - year2;
			int month1 = today.get(Calendar.MONTH);
			int month2 = dob.get(Calendar.MONTH);
			if (month2 > month1) {
				age--;
			} else if (month1 == month2) {
				int day1 = today.get(Calendar.DAY_OF_MONTH);
				int day2 = dob.get(Calendar.DAY_OF_MONTH);
				if (day2 > day1) {
					age--;
				}
			}
			return sign * age;
		} else {
			return null;
		}
	}

	public static ResourceBundle getBundle(String baseName, Locale locale) throws MissingResourceException, NullPointerException, IllegalArgumentException {
		if (locale != null) {
			try {
				return ResourceBundle.getBundle(baseName, locale, BUNDLE_CONTROL);
			} catch (MissingResourceException e) {
				// log this...
			}
		}
		return ResourceBundle.getBundle(baseName, BUNDLE_CONTROL); // must fail if bundle not found!
	}

	public static Map<String, String> getBundleSymbolMap(ResourceBundle bundle, boolean sorted) {
		if (bundle != null) {
			Iterator<String> it;
			Map<String, String> symbols;
			if (sorted) {
				ArrayList<String> keys = new ArrayList<String>(bundle.keySet());
				Collections.sort(keys);
				it = keys.iterator();
				symbols = new TreeMap<String, String>();
			} else {
				Set<String> keys = bundle.keySet();
				it = keys.iterator();
				symbols = new HashMap<String, String>(keys.size());
			}
			while (it.hasNext()) {
				String key = it.next();
				try {
					symbols.put(key, bundle.getString(key));
				} catch (MissingResourceException | ClassCastException e) {
				}
			}
			return symbols;
		} else {
			return new HashMap<String, String>();
		}
	}

	public static <T> Iterator<T> getCircularIterator(final Iterable<T> iterable) {
		return new Iterator<T>() {

			Iterator<T> iterator = (Iterator<T>) EMPTY_ITERATOR;

			@Override
			public boolean hasNext() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.hasNext();
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return iterator.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static final String getCourseName(CourseOutVO course) {
		StringBuilder sb = new StringBuilder();
		if (course != null) {
			appendString(sb, course.getName(), "", "?");
		}
		return sb.toString();
	}

	public static String getCriterionValueAsString(CriterionInstantVO criterion, CriterionValueType valueType, String userDateFormatPattern, String userDecimalSeparator) {
		return getCriterionValueAsString(criterion, valueType, NO_SELECTION_VALUE, userDateFormatPattern, userDecimalSeparator);
	}

	public static String getCriterionValueAsString(CriterionInstantVO criterion, CriterionValueType valueType, String emptyValue, String userDateFormatPattern,
			String userDecimalSeparator) {
		switch (valueType) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				return ((Boolean) criterion.getBooleanValue()).toString();
			case DATE:
			case DATE_HASH:
				if (criterion.getDateValue() != null) {
					return formatDate(criterion.getDateValue(), getInputDatePattern(userDateFormatPattern));
				} else {
					return emptyValue;
				}
			case TIME:
			case TIME_HASH:
				if (criterion.getTimeValue() != null) {
					return formatDate(criterion.getTimeValue(), getInputTimePattern(userDateFormatPattern));
				} else {
					return emptyValue;
				}
			case FLOAT:
			case FLOAT_HASH:
				if (criterion.getFloatValue() != null) {
					return formatFloat(criterion.getFloatValue(), userDecimalSeparator);
				} else {
					return emptyValue;
				}
			case LONG:
			case LONG_HASH:
				if (criterion.getLongValue() != null) {
					return criterion.getLongValue().toString();
				} else {
					return emptyValue;
				}
			case STRING:
			case STRING_HASH:
				if (criterion.getStringValue() != null) {
					return criterion.getStringValue();
				} else {
					return emptyValue;
				}
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				if (criterion.getTimestampValue() != null) {
					return formatDate(criterion.getTimestampValue(), getInputDateTimePattern(userDateFormatPattern));
				} else {
					return emptyValue;
				}
			case NONE:
				return emptyValue;
			default:
				// datatype unimplemented
				throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_CRITERION_VALUE_TYPE, valueType.toString()));
		}
	}

	public static String getCriterionValueAsString(CriterionInVO criterion, CriterionValueType valueType, String userDateFormatPattern, String userDecimalSeparator) {
		switch (valueType) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				return ((Boolean) criterion.getBooleanValue()).toString();
			case DATE:
			case DATE_HASH:
				if (criterion.getDateValue() != null) {
					return formatDate(criterion.getDateValue(), getInputDatePattern(userDateFormatPattern));
				} else {
					return NO_SELECTION_VALUE;
				}
			case TIME:
			case TIME_HASH:
				if (criterion.getTimeValue() != null) {
					return formatDate(criterion.getTimeValue(), getInputTimePattern(userDateFormatPattern));
				} else {
					return NO_SELECTION_VALUE;
				}
			case FLOAT:
			case FLOAT_HASH:
				if (criterion.getFloatValue() != null) {
					return formatFloat(criterion.getFloatValue(), userDecimalSeparator);
				} else {
					return NO_SELECTION_VALUE;
				}
			case LONG:
			case LONG_HASH:
				if (criterion.getLongValue() != null) {
					return criterion.getLongValue().toString();
				} else {
					return NO_SELECTION_VALUE;
				}
			case STRING:
			case STRING_HASH:
				if (criterion.getStringValue() != null) {
					return criterion.getStringValue();
				} else {
					return NO_SELECTION_VALUE;
				}
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				if (criterion.getTimestampValue() != null) {
					return formatDate(criterion.getTimestampValue(), getInputDateTimePattern(userDateFormatPattern));
				} else {
					return NO_SELECTION_VALUE;
				}
			case NONE:
				return NO_SELECTION_VALUE;
			default:
				// datatype unimplemented
				throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_CRITERION_VALUE_TYPE, valueType.toString()));
		}
	}

	public static String getCvStaffName(StaffOutVO staff) {
		return getCvStaffName(staff, false, null);
	}

	public static String getCvStaffName(StaffOutVO staff, boolean lastNameUpperCase, Locale locale) {
		StringBuilder sb = new StringBuilder();
		if (staff != null) {
			if (staff.isPerson()) {
				String lastName = staff.getLastName();
				if (lastNameUpperCase && !isEmptyString(lastName)) {
					if (locale != null) {
						lastName = lastName.toUpperCase(locale);
					} else {
						lastName = lastName.toUpperCase();
					}
				}
				if (CommonUtil.isEmptyString(staff.getCvAcademicTitle())) {
					CommonUtil.appendString(sb, staff.getPrefixedTitle1(), null);
					CommonUtil.appendString(sb, staff.getPrefixedTitle2(), " ");
					CommonUtil.appendString(sb, staff.getPrefixedTitle3(), " ");
					CommonUtil.appendString(sb, staff.getFirstName(), " ");
					CommonUtil.appendString(sb, lastName, " ", "?");
					CommonUtil.appendString(sb, staff.getPostpositionedTitle1(), ", ");
					CommonUtil.appendString(sb, staff.getPostpositionedTitle2(), ", ");
					CommonUtil.appendString(sb, staff.getPostpositionedTitle3(), ", ");
				} else {
					CommonUtil.appendString(sb, staff.getFirstName(), null);
					CommonUtil.appendString(sb, lastName, " ", "?");
					CommonUtil.appendString(sb, staff.getCvAcademicTitle(), ", ");
				}
			} else {
				if (CommonUtil.isEmptyString(staff.getCvOrganisationName())) {
					CommonUtil.appendString(sb, staff.getOrganisationName(), null, "?");
				} else {
					CommonUtil.appendString(sb, staff.getCvOrganisationName(), null);
				}
			}
		}
		return sb.toString();
	}

	public static String getDateStartStopString(Date start, Date stop, DateFormat dateFormat) {
		StringBuilder sb = new StringBuilder();
		if (start != null) {
			sb.append(dateFormat.format(start));
		} else {
			sb.append("?");
		}
		sb.append(" - ");
		if (stop != null) {
			sb.append(dateFormat.format(stop));
		} else {
			sb.append("?");
		}
		return sb.toString();
	}

	public static Long getEntityId(Object entity) throws Exception {
		return (Long) entity.getClass().getMethod(ENTITY_ID_GETTER_METHOD_NAME).invoke(entity);
	}

	public static Method getPropertySetter(Class clazz, String propertyName, Class type) throws NoSuchMethodException, SecurityException {
		String propertySetterMethodName = SET_PROPERTY_METHOD_NAME_PREFIX + capitalizeFirstChar(propertyName, true);
		return clazz.getMethod(propertySetterMethodName, type);
	}

	public static Method getPropertyGetter(Class clazz, String propertyName) throws NoSuchMethodException, SecurityException {
		String propertyGetterMethodName = GET_PROPERTY_METHOD_NAME_PREFIX + capitalizeFirstChar(propertyName, true);
		try {
			return clazz.getMethod(propertyGetterMethodName);
		} catch (NoSuchMethodException e) {
			propertyGetterMethodName = IS_PROPERTY_METHOD_NAME_PREFIX + capitalizeFirstChar(propertyName, true);
			return clazz.getMethod(propertyGetterMethodName);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T getDeclaredFieldValue(Object object, String fieldName, Class clazz) {
		//https://stackoverflow.com/questions/4325164/how-to-reload-resource-bundle-in-web-application
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(object);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDeclaredFieldValue(Object object, String fieldName) {
		return getDeclaredFieldValue(object, fieldName, object.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDeclaredFieldValue(Object object, Class superClass, String fieldName) {
		//https://stackoverflow.com/questions/15420968/nosuchfieldexception-when-field-exists/69946642#69946642
		Class clazz = object.getClass();
		while (clazz != null && !clazz.equals(superClass)) {
			clazz = clazz.getSuperclass();
		}
		return getDeclaredFieldValue(object, fieldName, clazz);
	}

	public static Long getEntityPosition(Object entity) throws Exception {
		return (Long) entity.getClass().getMethod(ENTITY_POSITION_GETTER_METHOD_NAME).invoke(entity);
	}

	public final static String getGenderSpecificSalutation(ProbandOutVO proband, String maleSalutation, String femaleSalutation) {
		if (proband != null && proband.getGender() != null) {
			return getGenderSpecificSalutation(proband.getGender().getSex(), maleSalutation, femaleSalutation);
		}
		return "";
	}

	private final static String getGenderSpecificSalutation(Sex gender, String maleSalutation, String femaleSalutation) {
		if (gender != null) {
			switch (gender) {
				case MALE:
				case TRANSGENDER_MALE:
					return maleSalutation;
				case FEMALE:
				case TRANSGENDER_FEMALE:
					return femaleSalutation;
				case DIVERSE:
					return maleSalutation + '/' + femaleSalutation;
				default:
					break;
			}
		}
		return "";
	}

	public final static String getGenderSpecificSalutation(StaffOutVO staff, String maleSalutation, String femaleSalutation) {
		if (staff != null && staff.getGender() != null) {
			return getGenderSpecificSalutation(staff.getGender().getSex(), maleSalutation, femaleSalutation);
		}
		return "";
	}

	public static String getHex(byte[] data) {
		if (data == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * data.length);
		for (final byte b : data) {
			hex.append(HEX_DIGITS.charAt((b & 0xF0) >> 4)).append(
					HEX_DIGITS.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	public static String getInputDatePattern(String userDateFormatPattern) {
		if (isEmptyString(userDateFormatPattern)) {
			return DEFAULT_INPUT_DATE_PATTERN;
		} else {
			return userDateFormatPattern;
		}
	}

	public static String getInputDateTimePattern(String userDateFormatPattern) {
		if (isEmptyString(userDateFormatPattern)) {
			return DEFAULT_INPUT_DATETIME_PATTERN;
		} else {
			return userDateFormatPattern + " " + DEFAULT_INPUT_TIME_PATTERN;
		}
	}

	public static String getInputTimePattern(String userDateFormatPattern) {
		return DEFAULT_INPUT_TIME_PATTERN;
	}

	public static final String getInventoryName(InventoryOutVO inventory) {
		StringBuilder sb = new StringBuilder();
		if (inventory != null) {
			appendString(sb, inventory.getName(), "", "?");
		}
		return sb.toString();
	}

	public static final String getUserName(UserOutVO user) {
		StringBuilder sb = new StringBuilder();
		if (user != null) {
			appendString(sb, user.getName(), "", "?");
		}
		return sb.toString();
	}

	public static boolean getIsValueNull(String value) {
		return value != null && PROPERTIES_NULL_LITERAL.equalsIgnoreCase(value); // properties value must explicitly be "null" - if empty, default value is used.
	}

	private static String getLocalHostAddress() {
		DatagramSocket socket = null;
		String ip = null;
		try {
			socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
			}
		}
		if (ip == null) {
			try {
				ip = getLocalHostLANAddress().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return ip;
	}

	// https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {
						if (inetAddr.isSiteLocalAddress()) {
							// Found non-loopback site-local address. Return it immediately...
							return inetAddr;
						} else if (candidateAddress == null) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other non-loopback address.
				// Server might have a non-site-local address assigned to its NIC (or it might be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}

	private static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			return UNKNOWN_LOCAL_HOST_NAME;
		}
	}

	public static int getLogicalPathFolderDepth(String logicalPath) {
		if (logicalPath != null && logicalPath.length() > 0) {
			int lastIndex = 0;
			int count = 0;
			while (lastIndex != -1) {
				lastIndex = logicalPath.indexOf(LOGICAL_PATH_SEPARATOR, lastIndex);
				if (lastIndex != -1) {
					lastIndex += LOGICAL_PATH_SEPARATOR.length();
					count++;
				}
			}
			return count - 1;
		} else {
			return -1;
		}
	}

	public static String getLogicalPathFolderToDepth(String logicalPath, int depth) {
		int length;
		if (logicalPath != null && (length = logicalPath.length()) > 0) {
			if (depth == 0) {
				return LOGICAL_PATH_SEPARATOR;
			}
			int lastIndex = 0;
			int count = 0;
			while (lastIndex != -1) {
				lastIndex = logicalPath.indexOf(LOGICAL_PATH_SEPARATOR, lastIndex);
				if (lastIndex != -1) {
					lastIndex += LOGICAL_PATH_SEPARATOR.length();
					count++;
					if (count == depth) {
						if (lastIndex < length) {
							int nextIndex = logicalPath.indexOf(LOGICAL_PATH_SEPARATOR, lastIndex);
							return logicalPath.substring(0, nextIndex != -1 ? nextIndex + LOGICAL_PATH_SEPARATOR.length() : length - 1);
						}
					}
				}
			}
			return logicalPath;
		} else {
			return "";
		}
	}

	public static String getMessage(String l10nKey, Object[] args, ResourceBundle bundle, String defaultString) {
		if (l10nKey == null || l10nKey.trim().length() == 0) {
			return MessageFormat.format(defaultString, args);
		}
		if (bundle == null) {
			return MessageFormat.format(l10nKey, args);
		}
		try {
			return MessageFormat.format(bundle.getString(l10nKey), args);
		} catch (MissingResourceException | ClassCastException | IllegalArgumentException e) {
			return MessageFormat.format(l10nKey, args);
		}
	}

	public static final String getNameSortable(ProbandOutVO proband) {
		StringBuilder sb = new StringBuilder();
		if (proband != null) {
			if (!proband.isBlinded()) {
				if (proband.isDecrypted()) {
					if (proband.isPerson()) {
						String lastName = proband.getLastName();
						if (lastName != null && lastName.trim().length() > 0) {
							sb.append(lastName.trim());
						}
						String firstName = proband.getFirstName();
						if (firstName != null && firstName.trim().length() > 0) {
							if (sb.length() > 0) {
								sb.append(", ");
							}
							sb.append(firstName.trim());
						}
					} else {
						String animalName = proband.getAnimalName();
						if (animalName != null && animalName.trim().length() > 0) {
							sb.append(animalName.trim().substring(0, 1));
						}
					}
				} else {
					if (proband.getId() > 0) {
						sb.append(Long.toString(proband.getId()));
					}
				}
			} else {
				appendProbandAlias(sb, proband, null, null);
			}
		}
		return sb.toString();
	}

	public static final String getNameSortable(StaffOutVO staff) {
		StringBuilder sb = new StringBuilder();
		if (staff != null) {
			if (staff.isPerson()) {
				String lastName = staff.getLastName();
				if (lastName != null && lastName.trim().length() > 0) {
					sb.append(lastName.trim());
				}
				String firstName = staff.getFirstName();
				if (firstName != null && firstName.trim().length() > 0) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(firstName.trim());
				}
			} else {
				String organisationName = staff.getOrganisationName();
				if (organisationName != null && organisationName.trim().length() > 0) {
					sb.append(organisationName.trim().substring(0, 1));
				}
			}
		}
		return sb.toString();
	}

	public static String getProbandAlias(ProbandOutVO proband, String newBlindedProbandNameLabel, String blindedProbandNameLabel) {
		StringBuilder sb = new StringBuilder();
		if (proband != null) {
			appendProbandAlias(sb, proband, newBlindedProbandNameLabel, blindedProbandNameLabel);
		}
		return sb.toString();
	}

	public static final String getProbandInitials(ProbandOutVO proband, String ecryptedProbandNameLabel, String newBlindedProbandNameLabel, String blindedProbandNameLabel) {
		StringBuilder sb = new StringBuilder();
		if (proband != null) {
			if (!proband.isBlinded()) {
				if (proband.isDecrypted()) {
					if (proband.isPerson()) {
						String firstName = proband.getFirstName();
						if (firstName != null && firstName.trim().length() > 0) {
							sb.append(firstName.trim().substring(0, 1).toUpperCase());
						}
						String lastName = proband.getLastName();
						if (lastName != null && lastName.trim().length() > 0) {
							sb.append(lastName.trim().substring(0, 1).toUpperCase());
						}
					} else {
						String animalName = proband.getAnimalName();
						if (animalName != null && animalName.trim().length() > 0) {
							sb.append(animalName.trim().substring(0, 3).toUpperCase());
						}
					}
				} else {
					sb.append(ecryptedProbandNameLabel);
				}
			} else {
				appendProbandAlias(sb, proband, newBlindedProbandNameLabel, blindedProbandNameLabel);
			}
		}
		return sb.toString();
	}

	public static final String getProbandName(ProbandOutVO proband, boolean withTitles, boolean withFirstName, String ecryptedProbandNameLabel, String newBlindedProbandNameLabel,
			String blindedProbandNameLabel) {
		StringBuilder sb = new StringBuilder();
		if (proband != null) {
			if (!proband.isBlinded()) {
				if (proband.isDecrypted()) {
					if (proband.isPerson()) {
						if (withTitles) {
							CommonUtil.appendString(sb, proband.getPrefixedTitle1(), null);
							CommonUtil.appendString(sb, proband.getPrefixedTitle2(), " ");
							CommonUtil.appendString(sb, proband.getPrefixedTitle3(), " ");
							if (withFirstName) {
								CommonUtil.appendString(sb, proband.getFirstName(), " ");
							}
							CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
							CommonUtil.appendString(sb, proband.getPostpositionedTitle1(), ", ");
							CommonUtil.appendString(sb, proband.getPostpositionedTitle2(), ", ");
							CommonUtil.appendString(sb, proband.getPostpositionedTitle3(), ", ");
						} else {
							if (withFirstName) {
								CommonUtil.appendString(sb, proband.getFirstName(), null);
								CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
							} else {
								CommonUtil.appendString(sb, proband.getLastName(), null, "?");
							}
						}
					} else {
						CommonUtil.appendString(sb, proband.getAnimalName(), null, "?");
					}
				} else {
					sb.append(ecryptedProbandNameLabel);
				}
			} else {
				appendProbandAlias(sb, proband, newBlindedProbandNameLabel, blindedProbandNameLabel);
			}
		}
		return sb.toString();
	}

	public static String getSafeFilename(String filenamePart) {
		return getSafeFilename(filenamePart, DEFAULT_FILENAME_ESCAPE_CHAR);
	}

	public static String getSafeFilename(String filenamePart, String escapeChar) {
		if (filenamePart == null) {
			return null;
		}
		return filenamePart.replaceAll(ILLEGAL_FILENAME_CHARS_PATTERN, Matcher.quoteReplacement(escapeChar));
	}

	public static String getSetOperationExpressionSelectLabel(int selectCount) {
		return MessageFormat.format(SET_OPERATION_EXPRESSION_SELECT_LABEL,
				SET_OPERATION_EXPRESSION_SELECT_LABEL_ALPHABETIC ? toAlphabetic(selectCount - SET_OPERATION_EXPRESSION_FIRST_INDEX) : selectCount);
	}

	public static final String getStaffInitials(StaffOutVO staff) {
		StringBuilder sb = new StringBuilder();
		if (staff != null) {
			if (staff.isPerson()) {
				String firstName = staff.getFirstName();
				if (firstName != null && firstName.trim().length() > 0) {
					sb.append(firstName.trim().substring(0, 1).toUpperCase());
				}
				String lastName = staff.getLastName();
				if (lastName != null && lastName.trim().length() > 0) {
					sb.append(lastName.trim().substring(0, 1).toUpperCase());
				}
			} else {
				String organisationName = staff.getOrganisationName();
				if (organisationName != null && organisationName.trim().length() > 0) {
					sb.append(organisationName.trim().substring(0, 3).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public static String getStaffName(StaffOutVO staff, boolean withTitles, boolean withFirstName) {
		return getStaffName(staff, withTitles, withFirstName, false, null);
	}

	public static final String getStaffName(StaffOutVO staff, boolean withTitles, boolean withFirstName, boolean lastNameUpperCase, Locale locale) {
		StringBuilder sb = new StringBuilder();
		if (staff != null) {
			if (staff.isPerson()) {
				String lastName = staff.getLastName();
				if (lastNameUpperCase && !isEmptyString(lastName)) {
					if (locale != null) {
						lastName = lastName.toUpperCase(locale);
					} else {
						lastName = lastName.toUpperCase();
					}
				}
				if (withTitles) {
					appendString(sb, staff.getPrefixedTitle1(), null);
					appendString(sb, staff.getPrefixedTitle2(), " ");
					appendString(sb, staff.getPrefixedTitle3(), " ");
					if (withFirstName) {
						appendString(sb, staff.getFirstName(), " ");
					}
					appendString(sb, lastName, " ", "?");
					appendString(sb, staff.getPostpositionedTitle1(), ", ");
					appendString(sb, staff.getPostpositionedTitle2(), ", ");
					appendString(sb, staff.getPostpositionedTitle3(), ", ");
				} else {
					if (withFirstName) {
						appendString(sb, staff.getFirstName(), null);
						appendString(sb, lastName, " ", "?");
					} else {
						CommonUtil.appendString(sb, lastName, null, "?");
					}
				}
			} else {
				sb.append(staff.getOrganisationName());
			}
		}
		return sb.toString();
	}

	public static String getStreetString(String streetName, String houseNumber, String entrance, String doorNumber) {
		StringBuilder sb = new StringBuilder();
		StringBuilder hed = new StringBuilder();
		CommonUtil.appendString(sb, streetName, null, "?");
		CommonUtil.appendString(hed, houseNumber, null, "?");
		CommonUtil.appendString(hed, entrance, "/");
		CommonUtil.appendString(hed, doorNumber, "/");
		CommonUtil.appendString(sb, hed.toString(), " ");
		return sb.toString();
	}

	public static String getString(String l10nKey, ResourceBundle bundle, String defaultString) {
		if (l10nKey == null || l10nKey.trim().length() == 0) {
			return defaultString;
		}
		if (bundle == null) {
			return l10nKey;
		}
		try {
			return bundle.getString(l10nKey);
		} catch (MissingResourceException | ClassCastException e) {
			return l10nKey;
		}
	}

	private static void getSvgPath(JsonElement path, StringBuilder d) {
		if (path != null && !path.isJsonNull()) {
			if (path.isJsonArray()) {
				Iterator<JsonElement> linesIt = path.getAsJsonArray().iterator();
				while (linesIt.hasNext()) {
					if (d.length() > 0) {
						d.append(" ");
					}
					getSvgPath(linesIt.next(), d);
				}
			} else {
				d.append(path.getAsString());
			}
		}
	}

	public static boolean getUseFileEncryption(FileModule module) {
		if (module != null && ENCRYPTED_FILE_MODULE.contains(module)) {
			return true;
		}
		return false;
	}

	public static boolean getUseJournalEncryption(JournalModule systemMessageModule, JournalModule categoryModule) {
		if (systemMessageModule != null) {
			if (ENCRYPTED_JOURNAL_MODULE.contains(systemMessageModule)) {
				return true;
			}
		} else {
			if (categoryModule != null && ENCRYPTED_JOURNAL_MODULE.contains(categoryModule)) {
				return true;
			}
		}
		return false;
	}

	public static boolean getValue(String key, ResourceBundle bundle, boolean defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(bundle.getString(key));
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static double getValue(String key, ResourceBundle bundle, double defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(bundle.getString(key));
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static float getValue(String key, ResourceBundle bundle, float defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(bundle.getString(key));
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static int getValue(String key, ResourceBundle bundle, int defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(bundle.getString(key));
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static long getValue(String key, ResourceBundle bundle, long defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(bundle.getString(key));
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static String getValue(String key, ResourceBundle bundle, String defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			return bundle.getString(key);
		} catch (MissingResourceException | ClassCastException e) {
			return defaultValue;
		}
	}

	public static Boolean getValueNullable(String key, ResourceBundle bundle, Boolean defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			String tristate = bundle.getString(key);
			if (getIsValueNull(tristate)) {
				return null;
			} else {
				return Boolean.parseBoolean(tristate);
			}
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Double getValueNullable(String key, ResourceBundle bundle, Double defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			String tristate = bundle.getString(key);
			if (getIsValueNull(tristate)) {
				return null;
			} else {
				return Double.parseDouble(tristate);
			}
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Float getValueNullable(String key, ResourceBundle bundle, Float defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			String tristate = bundle.getString(key);
			if (getIsValueNull(tristate)) {
				return null;
			} else {
				return Float.parseFloat(tristate);
			}
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Integer getValueNullable(String key, ResourceBundle bundle, Integer defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			String tristate = bundle.getString(key);
			if (getIsValueNull(tristate)) {
				return null;
			} else {
				return Integer.parseInt(tristate);
			}
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Long getValueNullable(String key, ResourceBundle bundle, Long defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue;
		}
		try {
			String tristate = bundle.getString(key);
			if (getIsValueNull(tristate)) {
				return null;
			} else {
				return Long.parseLong(tristate);
			}
		} catch (MissingResourceException | ClassCastException | NumberFormatException e) {
			return defaultValue;
		}
	}

	public static ArrayList<String> getValueStringList(String key, ResourceBundle bundle, ArrayList<String> defaultValue) {
		if (key == null || key.trim().length() == 0 || bundle == null) {
			return defaultValue == null ? new ArrayList<String>() : (ArrayList<String>) defaultValue.clone();
		}
		String value;
		try {
			value = bundle.getString(key);
		} catch (MissingResourceException | ClassCastException e) {
			return defaultValue == null ? new ArrayList<String>() : (ArrayList<String>) defaultValue.clone();
		}
		ArrayList<String> result;
		if (value != null && value.length() > 0) {
			String[] list = PROPERTIES_STRING_LIST_REGEXP.split(value, -1);
			result = new ArrayList<String>(list.length);
			for (int i = 0; i < list.length; i++) {
				if (list[i].length() > 0) {
					result.add(list[i]);
				}
			}
		} else {
			result = new ArrayList<String>();
		}
		return result;
	}

	public static Long getVOId(Object vo) {
		if (vo != null) {
			try {
				return (Long) vo.getClass().getMethod(VO_ID_GETTER_METHOD_NAME).invoke(vo);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Long getVOPosition(Object vo) {
		if (vo != null) {
			try {
				return (Long) vo.getClass().getMethod(VO_POSITION_GETTER_METHOD_NAME).invoke(vo);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Integer getYearOfBirth(Date dateOfBirth) {
		if (dateOfBirth != null) {
			GregorianCalendar dObCal = new GregorianCalendar();
			dObCal.setTime(dateOfBirth);
			return dObCal.get(Calendar.YEAR);
		}
		return null;
	}

	public static String humanReadableByteCount(long bytes, String userDecimalSeparator) {
		return humanReadableByteCount(bytes, false, userDecimalSeparator);
	}

	public static String humanReadableByteCount(long bytes, boolean si, String userDecimalSeparator) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return formatDecimal(String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre), userDecimalSeparator);
	}

	public static String inkValueToString(byte[] inkValue) throws UnsupportedEncodingException {
		return inkValue == null ? null : new String(inkValue, INK_VALUE_CHARSET);
	}

	public static Document inkValueToSvg(Integer width, Integer height, byte[]... inkValues) throws Exception {
		if (inkValues != null && inkValues.length > 0) {
			DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
			String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
			Document doc = impl.createDocument(svgNS, "svg", null);
			// Get the root element (the 'svg' element).
			Element svgRoot = doc.getDocumentElement();
			// Set the width and height attributes on the root 'svg' element.
			svgRoot.setAttributeNS(null, "width", Integer.toString(width)); //defaults to 400
			svgRoot.setAttributeNS(null, "height", Integer.toString(height)); //defaults to 400
			for (int i = 0; i < inkValues.length; i++) {
				String inkJson = inkValueToString(inkValues[i]);
				if (inkJson != null) {
					JsonElement strokes = new JsonParser().parse(inkJson);
					Iterator<JsonElement> strokesIt = strokes.getAsJsonArray().iterator();
					while (strokesIt.hasNext()) {
						JsonObject stroke = strokesIt.next().getAsJsonObject();
						// Create the rectangle.
						Element path = doc.createElementNS(svgNS, "path");
						StringBuilder d = new StringBuilder();
						getSvgPath(stroke.get("path"), d);
						if (d.length() > 0) {
							path.setAttributeNS(null, "d", d.toString());
							stroke.remove("path");
							Iterator<Entry<String, JsonElement>> strokeAttributesIt = stroke.entrySet().iterator();
							while (strokeAttributesIt.hasNext()) {
								Entry<String, JsonElement> strokeAttribute = strokeAttributesIt.next();
								if (strokeAttribute.getValue().isJsonPrimitive()) {
									path.setAttributeNS(null, strokeAttribute.getKey(), strokeAttribute.getValue().getAsString());
								}
							}
							// rectangle.setAttributeNS(null, "y", "20");
							// rectangle.setAttributeNS(null, "width", "100");
							// rectangle.setAttributeNS(null, "height", "50");
							// rectangle.setAttributeNS(null, "fill", "red");
							// Attach the rectangle to the root 'svg' element.
							svgRoot.appendChild(path);
						}
					}
				}
			}
			return doc;
		}
		return null;
	}

	public static String inputFieldOutVOToString(InputFieldOutVO inputField) {
		if (inputField != null) {
			return inputField.getName();
		}
		return null;
	}

	public static byte[] inputStreamToByteArray(InputStream inputStream) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] block = new byte[INPUTSTREAM_BUFFER_BLOCKSIZE];
		try {
			while ((nRead = inputStream.read(block, 0, block.length)) != -1) {
				buffer.write(block, 0, nRead);
			}
			return buffer.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
	}

	public static String inputValueToString(Object value, String userDateFormatPattern, String userDecimalSeparator) {
		Class valueClass = value.getClass();
		if (valueClass.equals(String.class)) {
			return (String) value;
		} else if (valueClass.equals(Long.class)) {
			return ((Long) value).toString();
		} else if (valueClass.equals(java.lang.Long.TYPE)) {
			return ((Long) value).toString();
		} else if (valueClass.equals(Integer.class)) {
			return ((Integer) value).toString();
		} else if (valueClass.equals(java.lang.Integer.TYPE)) {
			return ((Integer) value).toString();
		} else if (valueClass.equals(Boolean.class)) {
			return ((Boolean) value).toString();
		} else if (valueClass.equals(java.lang.Boolean.TYPE)) {
			return ((Boolean) value).toString();
		} else if (valueClass.equals(Float.class)) {
			return formatFloat((Float) value, userDecimalSeparator);
		} else if (valueClass.equals(java.lang.Float.TYPE)) {
			return formatFloat((Float) value, userDecimalSeparator);
		} else if (valueClass.equals(Double.class)) {
			return formatDouble((Double) value, userDecimalSeparator);
		} else if (valueClass.equals(java.lang.Double.TYPE)) {
			return formatDouble((Double) value, userDecimalSeparator);
		} else if (valueClass.equals(Date.class)) {
			return formatDate((Date) value, getInputDatePattern(userDateFormatPattern));
		} else if (valueClass.equals(Timestamp.class)) {
			return formatDate((Date) value, getInputDateTimePattern(userDateFormatPattern));
		} else if (valueClass.equals(Time.class)) {
			return formatDate((Date) value, getInputTimePattern(userDateFormatPattern));
		} else if (valueClass.equals(VariablePeriod.class)) {
			return ((VariablePeriod) value).name();
		} else if (valueClass.equals(AuthenticationType.class)) {
			return ((AuthenticationType) value).name();
		} else if (valueClass.equals(Sex.class)) {
			return ((Sex) value).name();
		} else if (valueClass.equals(RandomizationMode.class)) {
			return ((RandomizationMode) value).name();
		} else if (valueClass.equals(DBModule.class)) {
			return ((DBModule) value).name();
		} else if (valueClass.equals(HyperlinkModule.class)) {
			return ((HyperlinkModule) value).name();
		} else if (valueClass.equals(JournalModule.class)) {
			return ((JournalModule) value).name();
		} else if (valueClass.equals(FileModule.class)) {
			return ((FileModule) value).name();
		} else if (valueClass.equals(org.phoenixctms.ctsms.enumeration.Color.class)) {
			return ((org.phoenixctms.ctsms.enumeration.Color) value).name();
		} else if (valueClass.equals(InputFieldType.class)) {
			return ((InputFieldType) value).name();
		} else if (valueClass.equals(EventImportance.class)) {
			return ((EventImportance) value).name();
		} else if (valueClass.equals(JobStatus.class)) {
			return ((JobStatus) value).name();
		} else {
			// illegal type...
			throw new IllegalArgumentException(MessageFormat.format(INPUT_TYPE_NOT_SUPPORTED, valueClass.toString()));
		}
	}

	public static String inventoryOutVOToString(InventoryOutVO inventory) {
		if (inventory != null) {
			return inventory.getName();
		}
		return null;
	}

	public static boolean isBlankCriterionTie(org.phoenixctms.ctsms.enumeration.CriterionTie tie) {
		return BLANK_TIES.contains(tie);
	}

	public static boolean isEmptyString(String string) {
		return !(string != null && string.trim().length() > 0);
	}

	public static boolean isMultiLineString(String string) {
		return string != null && (string.indexOf("\n") > 0 || string.indexOf("\r") > 0);
	}

	public static boolean isSameDay(Date a, Date b) {
		Calendar cal_a = Calendar.getInstance();
		Calendar cal_b = Calendar.getInstance();
		cal_a.setTime(a);
		cal_b.setTime(b);
		return cal_a.get(Calendar.YEAR) == cal_b.get(Calendar.YEAR) &&
				cal_a.get(Calendar.DAY_OF_YEAR) == cal_b.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean isUnaryCriterionRestriction(org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction) {
		return UNARY_RESTRICTIONS.contains(restriction);
	}

	public static Locale localeFromString(String language) {
		return language == null ? null : new Locale(language);
	}

	public static String localeToDisplayString(Locale locale, Locale displayLocale) {
		return locale == null ? null : (displayLocale == null ? locale.getDisplayLanguage() : locale.getDisplayLanguage(displayLocale));
	}

	public static String localeToString(Locale locale) {
		return locale == null ? null : locale.getLanguage();
	}

	public static String massMailOutVOToString(MassMailOutVO massMail) {
		if (massMail != null) {
			return massMail.getName();
		}
		return null;
	}

	public static Date parseDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date parseDate(String date, String pattern, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date parseDate(String date, String pattern, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timeZone);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Date parseDate(String date, String pattern, Locale locale, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		sdf.setTimeZone(timeZone);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String parseDecimal(String decimalValue, String userDecimalSeparator) {
		if (decimalValue != null && userDecimalSeparator != null && userDecimalSeparator.length() > 0) {
			return decimalValue.replace(userDecimalSeparator.charAt(0), '.');
		}
		return decimalValue;
	}

	public static Double parseDouble(String doubleValue, String userDecimalSeparator) {
		Double result = null;
		if (doubleValue != null) {
			doubleValue = parseDecimal(doubleValue, userDecimalSeparator);
			try {
				result = Double.parseDouble(doubleValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return result;
	}

	public static Float parseFloat(String floatValue, String userDecimalSeparator) {
		Float result = null;
		if (floatValue != null) {
			floatValue = parseDecimal(floatValue, userDecimalSeparator);
			try {
				result = Float.parseFloat(floatValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return result;
	}

	public static UUID parseUUID(String uuid) {
		return UUID.fromString(uuid);
	}

	private static void populateOrganisationList(ArrayList<String> organisationList, StaffOutVO staff) {
		if (staff != null) {
			if (!staff.isPerson()) {
				organisationList.add(0, CommonUtil.getCvStaffName(staff));
			}
			populateOrganisationList(organisationList, staff.getParent());
		}
	}

	public static String getZipCity(String province, String zipCode, String cityName) {
		StringBuilder zc = new StringBuilder();
		if (CommonUtil.isEmptyString(province)) {
			CommonUtil.appendString(zc, zipCode, null, "?");
			CommonUtil.appendString(zc, cityName, " ", "?");
		} else {
			CommonUtil.appendString(zc, cityName, null, "?");
			CommonUtil.appendString(zc, province, ", ", "?");
			CommonUtil.appendString(zc, zipCode, " ", "?");
		}
		return zc.toString();
	}

	public static String getAddressBlock(StaffAddressOutVO address, ArrayList<String> organisationList, String lineBreak) {
		StringBuilder sb = new StringBuilder();
		if (address != null) {
			if (organisationList != null) {
				Iterator<String> it = organisationList.iterator();
				while (it.hasNext()) {
					CommonUtil.appendString(sb, it.next(), lineBreak);
				}
			}
			StringBuilder hed = new StringBuilder();
			CommonUtil.appendString(sb, address.getStreetName(), lineBreak, "?");
			CommonUtil.appendString(hed, address.getHouseNumber(), null, "?");
			CommonUtil.appendString(hed, address.getEntrance(), "/");
			CommonUtil.appendString(hed, address.getDoorNumber(), "/");
			CommonUtil.appendString(sb, hed.toString(), " ");
			CommonUtil.appendString(sb, getZipCity(address.getProvince(), address.getZipCode(), address.getCityName()), lineBreak);
			CommonUtil.appendString(sb, address.getCountryName(), lineBreak);
		}
		return sb.toString();
	}

	public static String getCvAddressBlock(StaffAddressOutVO address, StaffOutVO organisation, String lineBreak) {
		ArrayList<String> organisationList = new ArrayList<String>();
		populateOrganisationList(organisationList, organisation);
		return getAddressBlock(address, organisationList, lineBreak);
	}

	public static String probandOutVOToString(ProbandOutVO proband) {
		if (proband != null) {
			return proband.getName();
		}
		return null;
	}

	public static String repeatString(String s, int n) {
		if (s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(s.length() * n);
		for (int i = 0; i < n; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	public static int safeLongToInt(long lng) {
		if (lng < Integer.MIN_VALUE || lng > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(MessageFormat.format(INVALID_LONG_CAST, Long.toString(lng)));
		}
		return (int) lng;
	}

	public static String sanitizeFilePath(String filePath) {
		// http://stackoverflow.com/questions/3548775/platform-independent-paths-in-java/3554456#3554456
		return sanitizeFilePath(filePath, UNIX_FILE_PATH_SEPARATOR);
	}

	public static String sanitizeFilePath(String filePath, String pathSeparator) {
		if (filePath == null) {
			return null;
		}
		return filePath.replaceAll(FILE_PATH_SEPARATOR_PATTERN, Matcher.quoteReplacement(pathSeparator));
	}

	public static void setCriterionValueFromString(CriterionInstantVO criterion, CriterionValueType valueType, String value, String userDateFormatPattern,
			String userDecimalSeparator) {
		setCriterionValueFromString(criterion, valueType, value, NO_SELECTION_VALUE, userDateFormatPattern, userDecimalSeparator);
	}

	public static void setCriterionValueFromString(CriterionInstantVO criterion, CriterionValueType valueType, String value, String emptyValue, String userDateFormatPattern,
			String userDecimalSeparator) {
		switch (valueType) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setBooleanValue(false);
				} else {
					criterion.setBooleanValue(Boolean.parseBoolean(value));
				}
				break;
			case DATE:
			case DATE_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setDateValue(null);
				} else {
					criterion.setDateValue(parseDate(value, getInputDatePattern(userDateFormatPattern)));
				}
				break;
			case TIME:
			case TIME_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setTimeValue(null);
				} else {
					criterion.setTimeValue(parseDate(value, getInputTimePattern(userDateFormatPattern)));
				}
				break;
			case FLOAT:
			case FLOAT_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setFloatValue(null);
				} else {
					criterion.setFloatValue(parseFloat(value, userDecimalSeparator));
				}
				break;
			case LONG:
			case LONG_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setLongValue(null);
				} else {
					criterion.setLongValue(new Long(value));
				}
				break;
			case STRING:
			case STRING_HASH:
				// problem: p:selectone selection is initially null, not ""
				// decide wether to search for null or "" ...
				if (value == null || emptyValue.equals(value)) {
					criterion.setStringValue(null);
				} else {
					criterion.setStringValue(value);
				}
				break;
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				if (value == null || emptyValue.equals(value)) {
					criterion.setTimestampValue(null);
				} else {
					criterion.setTimestampValue(dateToTimestamp(parseDate(value, getInputDateTimePattern(userDateFormatPattern))));
				}
				break;
			case NONE:
				break;
			default:
				// datatype unimplemented
				throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_CRITERION_VALUE_TYPE, valueType.toString()));
		}
	}

	public static void setCriterionValueFromString(CriterionInVO criterion, CriterionValueType valueType, String value, String userDateFormatPattern, String userDecimalSeparator) {
		switch (valueType) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setBooleanValue(false);
				} else {
					criterion.setBooleanValue(Boolean.parseBoolean(value));
				}
				break;
			case DATE:
			case DATE_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setDateValue(null);
				} else {
					criterion.setDateValue(parseDate(value, getInputDatePattern(userDateFormatPattern)));
				}
				break;
			case TIME:
			case TIME_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setTimeValue(null);
				} else {
					criterion.setTimeValue(parseDate(value, getInputTimePattern(userDateFormatPattern)));
				}
				break;
			case FLOAT:
			case FLOAT_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setFloatValue(null);
				} else {
					criterion.setFloatValue(parseFloat(value, userDecimalSeparator));
				}
				break;
			case LONG:
			case LONG_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setLongValue(null);
				} else {
					criterion.setLongValue(new Long(value));
				}
				break;
			case STRING:
			case STRING_HASH:
				// problem: selectone selection is initially null, not ""
				// decide wether to search for null or "" ...
				// we decided to map null from selectionsetservice.x.getID to ""; also corresponds to SearchBeanBase
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setStringValue(null);
				} else {
					criterion.setStringValue(value);
				}
				break;
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				if (value == null || NO_SELECTION_VALUE.equals(value)) {
					criterion.setTimestampValue(null);
				} else {
					criterion.setTimestampValue(dateToTimestamp(parseDate(value, getInputDateTimePattern(userDateFormatPattern))));
				}
				break;
			case NONE:
				break;
			default:
				// datatype unimplemented
				throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_CRITERION_VALUE_TYPE, valueType.toString()));
		}
	}

	public static String[] splitMultiLineString(String string, boolean keepSeparators) {
		if (string != null) {
			if (keepSeparators) {
				return LINE_BREAK_KEEP_SEPARATORS_SPLITTER.split(string);
			} else {
				return LINE_BREAK_SPLIT_REGEXP.split(string, -1);
			}
		}
		return null;
	}

	public static String staffOutVOToString(StaffOutVO staff) {
		if (staff != null) {
			return staff.getName();
		}
		return null;
	}

	public static String stringListToString(ArrayList<String> stringList) {
		StringBuilder result = new StringBuilder();
		if (stringList != null && stringList.size() > 0) {
			Iterator<String> it = stringList.iterator();
			while (it.hasNext()) {
				String element = it.next();
				if (element != null && element.length() > 0) {
					if (result.length() > 0) {
						result.append(PROPERTIES_STRING_LIST_DEFAULT_SEPARATOR);
					}
					result.append(element);
				}
			}
		}
		return result.toString();
	}

	public static byte[] stringToInkValue(String inkValue) throws UnsupportedEncodingException {
		return inkValue == null ? null : inkValue.getBytes(INK_VALUE_CHARSET);
	}

	public static TimeZone timeZoneFromString(String timeZoneID) {
		return TimeZone.getTimeZone(timeZoneID);
	}

	public static String timeZoneToDisplayString(TimeZone timeZone, Locale displayLocale) {
		return timeZone == null ? null : timeZone.getID();
	}

	public static String timeZoneToString(TimeZone timeZone) {
		return timeZone == null ? null : timeZone.getID();
	}

	public static String toAlphabetic(int i) {
		// https://stackoverflow.com/questions/10813154/converting-number-to-letter/30259745#30259745
		if (i < 0) {
			return "-" + toAlphabetic(-i - 1);
		}
		int quot = i / 26;
		int rem = i % 26;
		char letter = (char) ('A' + rem);
		if (quot == 0) {
			return "" + letter;
		} else {
			return toAlphabetic(quot - 1) + letter;
		}
	}

	public static String trialOutVOToString(TrialOutVO trial) {
		if (trial != null) {
			return trial.getName();
		}
		return null;
	}

	public static String truncateStringValue(String string, Integer maxLength) {
		if (!isEmptyString(string)) {
			String firstLine = string.trim();
			if (firstLine.indexOf("\n") > 0) {
				firstLine = firstLine.substring(0, firstLine.indexOf("\n")).replaceAll("[\r\n]+", "");
			} else if (firstLine.indexOf("\r") > 0) {
				firstLine = string.substring(0, firstLine.indexOf("\r")).replaceAll("[\r\n]+", "");
			}
			return maxLength != null ? clipString(firstLine, maxLength, null, null) : firstLine;
		} else if (string != null) {
			return string.trim();
		}
		return null;
	}

	public static String userOutVOToString(UserOutVO user) {
		if (user != null) {
			return user.getName();
		}
		return null;
	}

	public static String userInheritedVOToString(UserInheritedVO user) {
		if (user != null) {
			return user.getName();
		}
		return null;
	}

	public static List<String[]> getPersonNameVariants(String firstName, String lastName) {
		List<String[]> result = new ArrayList<>();
		Iterator<List<String>> firstNameVariantsIt = getPowerSet(
				normalizeName(firstName, true)).iterator();
		while (firstNameVariantsIt.hasNext()) {
			List<String> firstNames = firstNameVariantsIt.next();
			if (firstNames.isEmpty())
				continue;
			Iterator<List<String>> lastNameVariantsIt = getPowerSet(
					normalizeName(lastName, true)).iterator();
			while (lastNameVariantsIt.hasNext()) {
				List<String> lastNames = lastNameVariantsIt.next();
				if (lastNames.isEmpty())
					continue;
				result.add(new String[] { String.join("", firstNames), String.join("", lastNames) });
			}
		}
		return result;
	}

	public static List<String[]> getOrganisationNameVariants(String organisationName) {
		List<String[]> result = new ArrayList<>();
		Iterator<List<String>> organisationNameVariantsIt = getPowerSet(
				normalizeName(organisationName, true)).iterator();
		while (organisationNameVariantsIt.hasNext()) {
			List<String> organisationNames = organisationNameVariantsIt.next();
			if (organisationNames.isEmpty())
				continue;
			result.add(new String[] { String.join("", organisationNames) });
		}
		return result;
	}

	public static List<String[]> getAliasVariants(String alias) {
		List<String[]> result = new ArrayList<>();
		Iterator<List<String>> aliasVariantsIt = getPowerSet(
				normalizeName(alias, true)).iterator();
		while (aliasVariantsIt.hasNext()) {
			List<String> aliasVariants = aliasVariantsIt.next();
			if (aliasVariants.isEmpty())
				continue;
			result.add(new String[] { String.join("", aliasVariants) });
		}
		return result;
	}

	public static String normalizeFirstName(String firstName) {
		try {
			return normalizeName(firstName, false).iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static String normalizeLastName(String lastName) {
		try {
			return normalizeName(lastName, false).iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static String normalizeOrganisationName(String organisationName) {
		try {
			return normalizeName(organisationName, false).iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static String normalizeAlias(String alias) {
		try {
			return normalizeName(alias, false).iterator().next();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	private static List<String> normalizeName(String name, boolean split) {
		List<String> parts = new ArrayList<String>();
		if (name == null) {
			return parts;
		}
		// Normalize to NFKD and remove diacritics
		name = Normalizer.normalize(name, Normalizer.Form.NFKD);
		name = name.replaceAll("\\p{M}", "");
		// Convert to lowercase
		name = name.toLowerCase();
		// Replace underscores and hyphens with spaces
		name = name.replaceAll("[_-]", " ");
		// Trim and normalize spaces
		name = name.trim().replaceAll("\\s+", " ");
		if (split) {
			parts.addAll(Arrays.asList(name.split(" ")));
		} else {
			parts.add(name.replaceAll(" ", ""));
		}
		return parts;
	}

	public static <T> List<List<T>> getPowerSet(List<T> input) {
		List<List<T>> result = new ArrayList<>();
		result.add(new ArrayList<>()); // Add empty set
		Iterator<T> it = input.iterator();
		while (it.hasNext()) {
			T element = it.next();
			List<List<T>> newSubsets = new ArrayList<>();
			Iterator<List<T>> resultIt = result.iterator();
			while (resultIt.hasNext()) {
				List<T> subset = new ArrayList<>(resultIt.next());
				subset.add(element);
				newSubsets.add(subset);
			}
			result.addAll(newSubsets);
		}
		return result;
	}

	public static String longestCommonPrefix(String... strs) {
		if (strs == null || strs.length == 0)
			return "";
		String prefix = strs[0];
		for (int i = 1; i < strs.length; i++) {
			while (!strs[i].startsWith(prefix)) {
				prefix = prefix.substring(0, prefix.length() - 1);
				if (prefix.isEmpty())
					return "";
			}
		}
		return prefix;
	}

	private CommonUtil() {
	}
}
