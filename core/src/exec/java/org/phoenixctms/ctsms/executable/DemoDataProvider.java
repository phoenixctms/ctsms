package org.phoenixctms.ctsms.executable;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.CourseCategory;
import org.phoenixctms.ctsms.domain.CourseCategoryDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao;
import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandCategory;
import org.phoenixctms.ctsms.domain.ProbandCategoryDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.domain.SponsoringTypeDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.TeamMemberRole;
import org.phoenixctms.ctsms.domain.TeamMemberRoleDao;
import org.phoenixctms.ctsms.domain.TimelineEventType;
import org.phoenixctms.ctsms.domain.TimelineEventTypeDao;
import org.phoenixctms.ctsms.domain.TrialStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialTypeDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfile;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.domain.VisitTypeDao;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.CriterionRestriction;
import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.PDFStream;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.service.course.CourseService;
import org.phoenixctms.ctsms.service.inventory.InventoryService;
import org.phoenixctms.ctsms.service.proband.ProbandService;
import org.phoenixctms.ctsms.service.shared.FileService;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.service.shared.SearchService;
import org.phoenixctms.ctsms.service.shared.SelectionSetService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.service.staff.StaffService;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.GermanPersonNames;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.util.xeger.Xeger;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseInVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryInVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InventoryInVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffInVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class DemoDataProvider {

	private enum InputFields {
		HEIGHT("Körpergröße"),
		WEIGHT("Körpergewicht"),
		BMI("Body Mass Index"),
		DIABETES_YN("Diabetes J/N"),
		DIABETES_TYPE("Diabetes Typ"),
		DIABETES_SINCE("Diabetes seit"),
		DIABETES_HBA1C_MMOLPERMOL("HbA1C in mmol/mol"),
		DIABETES_HBA1C_PERCENT("HbA1C in prozent"),
		DIABETES_HBA1C_DATE("HbA1C Datum"),
		DIABETES_C_PEPTIDE("C-Peptid"), // µg/l
		DIABETES_ATTENDING_PHYSICIAN("Arzt in Behandlung"),
		DIABETES_METHOD_OF_TREATMENT("Diabetes Behandlungsmethode"), // Diät sport insulintherapie orale Antidiabetika
		DIABETES_MEDICATION("Diabetes Medikamente"),
		CLINICAL_TRIAL_EXPERIENCE_YN("Erfahrung mit klin. Studien J/N"),
		SMOKER_YN("Raucher J/N"),
		CIGARETTES_PER_DAY("Zigaretten pro Tag"),
		CHRONIC_DISEASE_YN("Chronische Erkrankung J/N"),
		CHRONIC_DISEASE("Chronische Erkrankung"),
		EPILEPSY_YN("Epilepsie J/N"),
		EPILEPSY("Epilepsie"),
		CARDIAC_PROBLEMS_YN("Herzprobleme J/N"),
		CARDIAC_PROBLEMS("Herzprobleme"),
		HYPERTENSION_YN("Bluthochdruck J/N"),
		HYPERTENSION("Bluthochdruck"),
		RENAL_INSUFFICIENCY_YN("Niereninsuffizienz/-erkrankung J/N"), // renal
		RENAL_INSUFFICIENCY("Niereninsuffizienz/-erkrankung"),
		LIVER_DISEASE_YN("Lebererkrankung J/N"), // liver diseaseYN
		LIVER_DISEASE("Lebererkrankung"),
		ANEMIA_YN("Anemie J/N"), // anemiaYN
		ANEMIA("Anemie"),
		IMMUNE_MEDAITED_DISEASE_YN("Autoimmunerkrankung J/N"), // immune mediated diseaseYN
		IMMUNE_MEDAITED_DISEASE("Autoimmunerkrankung"),
		GESTATION_YN("schwanger, stillen etc. J/N"), // gestationYN
		GESTATION("schwanger, stillen etc."),
		GESTATION_TYPE("schwanger, stillen etc. Auswahl"),
		CONTRACEPTION_YN("Empfängnisverhütung J/N"), // contraceptionYN
		CONTRACEPTION("Empfängnisverhütung"),
		CONTRACEPTION_TYPE("Empfängnisverhütung Auswahl"),
		ALCOHOL_DRUG_ABUSE_YN("Missbrauch von Alkohol/Drogen J/N"), // alcohol_drug_abuseYN
		ALCOHOL_DRUG_ABUSE("Missbrauch von Alkohol/Drogen"),
		PSYCHIATRIC_CONDITION_YN("Psychiatrische Erkrankung J/N"), // psychiatric_conditionYN
		PSYCHIATRIC_CONDITION("Psychiatrische Erkrankung"),
		ALLERGY_YN("Allergien J/N"), // allergyYN
		ALLERGY("Allergien"),
		MEDICATION_YN("Medikamente J/N"), // medicationYN
		MEDICATION("Medikamente"),
		EYE_PROBLEMS_YN("Probleme mit den Augen J/N"), // eye_probalemsYN
		EYE_PROBLEMS("Probleme mit den Augen"),
		FEET_PROBLEMS_YN("Probleme mit den Füßen J/N"), // feet_probalemsYN
		FEET_PROBLEMS("Probleme mit den Füßen"),
		DIAGNOSTIC_FINDINGS_AVAILABLE_YN("Befunde zuhause J/N"),
		DIAGNOSTIC_FINDINGS_AVAILABLE("Befunde zuhause"),
		GENERAL_STATE_OF_HEALTH("Allgemeiner Gesundheitszustand"),
		NOTE("Anmerkung"),
		SUBJECT_NUMBER("Subject Number"),
		IC_DATE("Informed Consent Date"),
		SCREENING_DATE("Screening Date"),
		LAB_NUMBER("Lab Number"),
		RANDOM_NUMBER("Random Number"),
		LETTER_TO_PHYSICIAN_SENT("Letter to physician sent"),
		PARTICIPATION_LETTER_IN_MEDOCS("Participation letter in MR/Medocs"),
		LETTER_TO_SUBJECT_AT_END_OF_STUDY("Letter to subject at end of study"),
		COMPLETION_LETTER_IN_MEDOCS("Completion letter in MR/Medocs"),
		BODY_HEIGHT("Body Height"),
		BODY_WEIGHT("Body Weight"),
		BODY_MASS_INDEX("BMI"),
		OBESITY("Obesity"),
		EGFR("eGFR"),
		SERUM_CREATININ_CONCENTRATION("Serum Creatinin Concentration"),
		ETHNICITY("Ethnicity"),
		HBA1C_PERCENT("HbA1C (percent)"),
		HBA1C_MMOLPERMOL("HbA1C (mmol/mol)"),
		MANNEQUIN("Mannequin"),
		ESR("ESR"),
		VAS("VAS"),
		DAS28("DAS28"),
		DISTANCE("Distance"),
		ALPHA_ID("Alpha-ID"),
		STRING_SINGLELINE("singleline text"),
		STRING_MULTILINE("multiline text"),
		FLOAT("decimal"),
		INTEGER("integer"),
		DIAGNOSIS_START("diagnosis from"),
		DIAGNOSIS_END("diagnosis to"),
		DIAGNOSIS_COUNT("diagnosis count");

		private final String value;

		private InputFields(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private enum InputFieldValues {
		TYP_1_DIABETES("Typ 1 Diabetes"),
		TYP_2_DIABETES_MIT_INSULINEIGENPRODUKTION("Typ 2 Diabetes mit Insulineigenproduktion"),
		TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION("Typ 2 Diabetes ohne Insulineigenproduktion"),
		DIAET("Diät"),
		SPORTLICHE_BETAETIGUNG("Sportliche Betätigung"),
		ORALE_ANTIDIABETIKA("Orale Antidiabetika"),
		INSULINTHERAPIE("Insulintherapie"),
		CIGARETTES_UNTER_5("Unter 5"),
		CIGARETTES_5_20("5-20"),
		CIGARETTES_20_40("20-40"),
		CIGARETTES_UEBER_40("über 40"),
		SCHWANGER("schwanger"),
		STILLEN("stillen"),
		HORMONELL("hormonell"),
		MECHANISCH("mechanisch"),
		INTRAUTERINPESSARE("Intrauterinpessare"),
		CHEMISCH("chemisch"),
		OPERATIV("operativ"),
		SHORTWEIGHT("shortweight"),
		NORMAL_WEIGHT("normal weight"),
		OVERWEIGHT("overweight"),
		ADIPOSITY_DEGREE_I("adiposity degree I"),
		ADIPOSITY_DEGREE_II("adiposity degree II"),
		ADIPOSITY_DEGREE_III("adiposity degree III"),
		AMERICAN_INDIAN_OR_ALASKA_NATIVE("American Indian or Alaska Native"),
		ASIAN("Asian"),
		BLACK("Black"),
		NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER("Native Hawaiian or Other Pacific Islander"),
		WHITE("White"),
		SHOULDER_RIGHT("shoulder right"),
		SHOULDER_LEFT("shoulder left"),
		ELLBOW_RIGHT("ellbow right"),
		ELLBOW_LEFT("ellbow left"),
		WRIST_RIGHT("wrist right"),
		WRIST_LEFT("wrist left"),
		THUMB_BASE_RIGHT("thumb base right"),
		THUMB_MIDDLE_RIGHT("thumb middle right"),
		THUMB_BASE_LEFT("thumb base left"),
		THUMB_MIDDLE_LEFT("thumb middle left"),
		INDEX_FINGER_BASE_RIGHT("index finger base right"),
		INDEX_FINGER_MIDDLE_RIGHT("index finger middle right"),
		MIDDLE_FINGER_BASE_RIGHT("middle finger base right"),
		MIDDLE_FINGER_MIDDLE_RIGHT("middle finger middle right"),
		RING_FINGER_BASE_RIGHT("ring finger base right"),
		RING_FINGER_MIDDLE_RIGHT("ring finger middle right"),
		LITTLE_FINGER_BASE_RIGHT("little finger base right"),
		LITTLE_FINGER_MIDDLE_RIGHT("little finger middle right"),
		INDEX_FINGER_BASE_LEFT("index finger base left"),
		INDEX_FINGER_MIDDLE_LEFT("index finger middle left"),
		MIDDLE_FINGER_BASE_LEFT("middle finger base left"),
		MIDDLE_FINGER_MIDDLE_LEFT("middle finger middle left"),
		RING_FINGER_BASE_LEFT("ring finger base left"),
		RING_FINGER_MIDDLE_LEFT("ring finger middle left"),
		LITTLE_FINGER_BASE_LEFT("little finger base left"),
		LITTLE_FINGER_MIDDLE_LEFT("little finger middle left"),
		KNEE_RIGHT("knee right"),
		KNEE_LEFT("knee left"),
		VAS_1("vas-1"),
		VAS_2("vas-2"),
		VAS_3("vas-3"),
		VAS_4("vas-4"),
		VAS_5("vas-5"),
		VAS_6("vas-6"),
		VAS_7("vas-7"),
		VAS_8("vas-8"),
		VAS_9("vas-9"),
		VAS_10("vas-10");

		private final String value;

		private InputFieldValues(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public enum SearchCriteria {
		ALL_INVENTORY("all inventory"),
		ALL_STAFF("all staff"),
		ALL_COURSES("all courses"),
		ALL_TRIALS("all trials"),
		ALL_PROBANDS("all probands"),
		ALL_INPUTFIELDS("all inputfields"),
		ALL_MASSMAILS("all_massmails"),
		ALL_USERS("all users"),
		SUBJECTS_1("subjects_1");

		private final String value;

		private SearchCriteria(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private final class SearchCriterion {

		private CriterionTie junction;
		private String property;
		private CriterionRestriction operator;
		private boolean booleanValue;
		private Date dateValue;
		private Float floatValue;
		private Long longValue;
		private String stringValue;

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator) {
			this.junction = junction;
			this.property = property;
			this.operator = operator;
			booleanValue = false;
			dateValue = null;
			floatValue = null;
			longValue = null;
			stringValue = null;
		}

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, boolean value) {
			this(junction, property, operator);
			booleanValue = value;
		}

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Date value) {
			this(junction, property, operator);
			dateValue = value;
		}

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Float value) {
			this(junction, property, operator);
			floatValue = value;
		}

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, Long value) {
			this(junction, property, operator);
			longValue = value;
		}

		private SearchCriterion(CriterionTie junction, String property, CriterionRestriction operator, String value) {
			this(junction, property, operator);
			stringValue = value;
		}

		public CriterionInVO buildCriterionInVO(DemoDataProvider d, DBModule module, int position) {
			CriterionInVO newCriterion = new CriterionInVO();
			newCriterion.setTieId(junction != null ? d.criterionTieDao.searchUniqueTie(junction).getId() : null);
			CriterionProperty p = CommonUtil.isEmptyString(property) ? null
					: (new ArrayList<CriterionProperty>(criterionPropertyDao.search(new Search(new SearchParameter[] {
							new SearchParameter("module", module, SearchParameter.EQUAL_COMPARATOR),
							new SearchParameter("property", property, SearchParameter.EQUAL_COMPARATOR),
					})))).iterator().next();
			newCriterion.setPropertyId(p != null ? p.getId() : null);
			newCriterion.setRestrictionId(operator != null ? d.criterionRestrictionDao.searchUniqueRestriction(operator).getId() : null);
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

	private final class Stroke {

		private final static String INK_VALUE_CHARSET = "UTF8";
		public String color;
		public String path;
		public String strokesId;
		public String value;
		public boolean valueSet;

		private Stroke() {
			color = null;
			path = null;
			strokesId = CommonUtil.generateUUID();
			value = null;
			valueSet = false;
		}

		public Stroke(String path) {
			this();
			this.color = "#00ff00";
			this.path = path;
		}

		public Stroke(String path, String value) {
			this(path);
			setValue(value);
		}

		public Stroke(String color, String path, String value) {
			this();
			this.color = color;
			this.path = path;
			setValue(value);
		}

		public byte[] getBytes() throws UnsupportedEncodingException {
			return toString().getBytes(INK_VALUE_CHARSET);
		}

		private void setValue(String value) {
			this.value = value;
			valueSet = true;
		}

		@Override
		public String toString() {
			return String.format("[{" +
					"\"fill\": \"%s\"," +
					"\"stroke\": \"%s\"," +
					"\"path\": \"%s\"," +
					"\"stroke-opacity\": 0.4," +
					"\"stroke-width\": 2," +
					"\"stroke-linecap\": \"round\"," +
					"\"stroke-linejoin\": \"round\"," +
					"\"transform\": []," +
					"\"type\": \"path\"," +
					"\"fill-opacity\": 0.2," +
					"\"strokes-id\": \"%s\"}]", color, color, path, strokesId);
		}
	}

	private static final int FILE_COUNT_PER_STAFF = 5;
	private static final int FILE_COUNT_PER_ORGANISATION = 5;
	private static final int FILE_COUNT_PER_COURSE = 10;
	private static final int FILE_COUNT_PER_INVENTORY = 10;
	private static final int FILE_COUNT_PER_PROBAND = 3;
	private static final int FILE_COUNT_PER_TRIAL = 500;
	private static final boolean CREATE_FILES = false;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserPermissionProfileDao userPermissionProfileDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private ToolsService toolsService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private SelectionSetService selectionSetService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ProbandService probandService;
	@Autowired
	private ProbandDao probandDao;
	@Autowired
	private ProbandCategoryDao probandCategoryDao;
	@Autowired
	private CourseCategoryDao courseCategoryDao;
	@Autowired
	private TrialStatusTypeDao trialStatusTypeDao;
	@Autowired
	private SponsoringTypeDao sponsoringTypeDao;
	@Autowired
	private TrialTypeDao trialTypeDao;
	@Autowired
	private VisitTypeDao visitTypeDao;
	@Autowired
	private TrialService trialService;
	@Autowired
	private FileService fileService;
	@Autowired
	private InputFieldService inputFieldService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private InputFieldDao inputFieldDao;
	@Autowired
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	@Autowired
	private TeamMemberRoleDao teamMemberRoleDao;
	@Autowired
	private TimelineEventTypeDao timelineEventTypeDao;
	@Autowired
	private CourseParticipationStatusTypeDao courseParticipationStatusTypeDao;
	@Autowired
	private ProbandListStatusEntryDao probandListStatusEntryDao;
	@Autowired
	private CriteriaDao criteriaDao;
	@Autowired
	private CriterionTieDao criterionTieDao;
	@Autowired
	private CriterionPropertyDao criterionPropertyDao;
	@Autowired
	private CriterionRestrictionDao criterionRestrictionDao;
	private Random random;
	private String prefix;
	private int year;
	private int departmentCount;
	private int usersPerDepartmentCount; // more users than persons intended
	private JobOutput jobOutput;
	private ApplicationContext context;

	public DemoDataProvider() {
		random = new Random();
		prefix = RandomStringUtils.randomAlphanumeric(4).toLowerCase();
		year = Calendar.getInstance().get(Calendar.YEAR);
	}

	private void addInkRegions(AuthenticationVO auth, InputFieldOutVO inputField, TreeMap<InputFieldValues, Stroke> inkRegions) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		Iterator<Entry<InputFieldValues, Stroke>> it = inkRegions.entrySet().iterator();
		while (it.hasNext()) {
			Entry<InputFieldValues, Stroke> inkRegion = it.next();
			Stroke stroke = inkRegion.getValue();
			InputFieldSelectionSetValueInVO newSelectionSetValue = new InputFieldSelectionSetValueInVO();
			newSelectionSetValue.setFieldId(inputField.getId());
			newSelectionSetValue.setName(inkRegion.getKey().toString());
			newSelectionSetValue.setPreset(false);
			newSelectionSetValue.setValue(stroke.valueSet ? stroke.value : inkRegion.getKey().toString());
			newSelectionSetValue.setInkRegions(stroke.getBytes());
			newSelectionSetValue.setStrokesId(stroke.strokesId);
			InputFieldSelectionSetValueOutVO out = inputFieldService.addSelectionSetValue(auth, newSelectionSetValue);
			jobOutput.println("ink region created: " + out.getName());
		}
	}

	private void addSelectionSetValues(AuthenticationVO auth, InputFieldOutVO inputField, TreeMap<InputFieldValues, Boolean> selectionSetValues) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		Iterator<Entry<InputFieldValues, Boolean>> it = selectionSetValues.entrySet().iterator();
		while (it.hasNext()) {
			Entry<InputFieldValues, Boolean> selectionSetValue = it.next();
			InputFieldSelectionSetValueInVO newSelectionSetValue = new InputFieldSelectionSetValueInVO();
			newSelectionSetValue.setFieldId(inputField.getId());
			newSelectionSetValue.setName(selectionSetValue.getKey().toString());
			newSelectionSetValue.setPreset(selectionSetValue.getValue());
			newSelectionSetValue.setValue(selectionSetValue.getKey().toString());
			InputFieldSelectionSetValueOutVO out = inputFieldService.addSelectionSetValue(auth, newSelectionSetValue);
			jobOutput.println("selection set value created: " + out.getName());
		}
	}

	private ArrayList<UserPermissionProfile> addUserPermissionProfiles(UserOutVO userVO, ArrayList<PermissionProfile> profiles) throws Exception {
		User user = userDao.load(userVO.getId());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ArrayList<UserPermissionProfile> result = new ArrayList<UserPermissionProfile>(profiles.size());
		Iterator<PermissionProfile> profilesIt = profiles.iterator();
		while (profilesIt.hasNext()) {
			PermissionProfile profile = profilesIt.next();
			UserPermissionProfile userPermissionProfile = UserPermissionProfile.Factory.newInstance();
			userPermissionProfile.setActive(true);
			userPermissionProfile.setProfile(profile);
			userPermissionProfile.setUser(user);
			CoreUtil.modifyVersion(userPermissionProfile, now, null);
			result.add(userPermissionProfileDao.create(userPermissionProfile));
			jobOutput.println("permission profile " + profile.toString() + " added");
		}
		return result;
	}

	private UserOutVO assignUser(StaffOutVO staff) throws Exception {
		Set<User> unassignedUsers = userDao.search(new Search(new SearchParameter[] {
				new SearchParameter("identity", SearchParameter.NULL_COMPARATOR),
				new SearchParameter("department.id", getDepartmentIds(), SearchParameter.IN_COMPARATOR) }));
		User user = getRandomElement(unassignedUsers);
		UserOutVO userVO = null;
		if (user != null) {
			UserInVO modifiedUser = new UserInVO();
			modifiedUser.setDepartmentId(user.getDepartment().getId());
			modifiedUser.setId(user.getId());
			modifiedUser.setIdentityId(staff.getId());
			modifiedUser.setName(user.getName());
			modifiedUser.setLocale(user.getLocale());
			modifiedUser.setTimeZone(user.getTimeZone());
			modifiedUser.setDateFormat(user.getDateFormat());
			modifiedUser.setDecimalSeparator(user.getDecimalSeparator());
			modifiedUser.setTheme(user.getTheme());
			modifiedUser.setLocked(user.isLocked());
			modifiedUser.setShowTooltips(user.isShowTooltips());
			modifiedUser.setDecrypt(user.isDecrypt());
			modifiedUser.setDecryptUntrusted(user.isDecryptUntrusted());
			modifiedUser.setEnableInventoryModule(true);
			modifiedUser.setEnableStaffModule(true);
			modifiedUser.setEnableCourseModule(true);
			modifiedUser.setEnableTrialModule(true);
			modifiedUser.setEnableInputFieldModule(true);
			modifiedUser.setEnableProbandModule(true);
			modifiedUser.setEnableMassMailModule(true);
			modifiedUser.setEnableUserModule(true);
			modifiedUser.setAuthMethod(user.getAuthMethod());
			modifiedUser.setVersion(user.getVersion());
			userVO = userService.updateUser(getRandomAuth(user.getDepartment().getId()), modifiedUser, null, null, null);
			jobOutput.println("user " + userVO.getName() + " assigned to " + staff.getName());
		}
		return userVO;
	}

	private InputFieldOutVO createCheckBoxField(AuthenticationVO auth, String name, String category, String title, String comment, boolean booleanPreset) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.CHECKBOX);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setBooleanPreset(booleanPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("check box input field created: " + out.getName());
		return out;
	}

	private CourseOutVO createCourse(AuthenticationVO auth, int courseNum, int departmentNum, Collection<Long> precedingCourseIds, Collection<Staff> institutions,
			int maxParticipants) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		CourseInVO newCourse = new CourseInVO();
		Date stop;
		VariablePeriod validityPeriod = getRandomElement(new VariablePeriod[] { VariablePeriod.MONTH, VariablePeriod.SIX_MONTHS, VariablePeriod.YEAR });
		Long validityPeriodDays = (validityPeriod == VariablePeriod.EXPLICIT ? getRandomElement(new Long[] { 7L, 14L, 21L }) : null);
		if (precedingCourseIds != null && precedingCourseIds.size() > 0) {
			stop = null;
			Iterator<Long> it = precedingCourseIds.iterator();
			while (it.hasNext()) {
				Long precedingCourseId = it.next();
				CourseOutVO precedingCourse = courseService.getCourse(auth, precedingCourseId, 1, null, null);
				if (precedingCourse.isExpires()) {
					Date precedingExpiration = DateCalc.addInterval(precedingCourse.getStop(), precedingCourse.getValidityPeriod().getPeriod(),
							precedingCourse.getValidityPeriodDays());
					if (stop == null || precedingExpiration.compareTo(stop) < 0) {
						stop = precedingExpiration;
					}
				}
			}
			stop = stop == null ? getRandomCourseStop() : DateCalc.subInterval(stop, VariablePeriod.EXPLICIT, new Long(random.nextInt(8)));
			newCourse.setPrecedingCourseIds(precedingCourseIds);
		} else {
			stop = getRandomCourseStop();
		}
		if (getRandomBoolean(50)) {
			newCourse.setExpires(true);
			newCourse.setValidityPeriod(validityPeriod);
			newCourse.setValidityPeriodDays(validityPeriodDays);
		} else {
			newCourse.setExpires(false);
		}
		Date start;
		if (getRandomBoolean(50)) {
			start = null;
		} else {
			VariablePeriod courseDurationPeriod = getRandomElement(new VariablePeriod[] { VariablePeriod.EXPLICIT, VariablePeriod.MONTH, VariablePeriod.SIX_MONTHS,
					VariablePeriod.YEAR });
			Long courseDurationPeriodDays = (courseDurationPeriod == VariablePeriod.EXPLICIT ? getRandomElement(new Long[] { 7L, 14L, 21L }) : null);
			start = DateCalc.subInterval(stop, courseDurationPeriod, courseDurationPeriodDays);
		}
		if (getRandomBoolean(50)) {
			newCourse.setSelfRegistration(false);
		} else {
			newCourse.setSelfRegistration(true);
			newCourse.setMaxNumberOfParticipants(1L + random.nextInt(maxParticipants));
			Date participationDeadline;
			if (getRandomBoolean(50)) {
				if (start != null) {
					participationDeadline = DateCalc.subInterval(start, VariablePeriod.EXPLICIT, new Long(random.nextInt(8)));
				} else {
					participationDeadline = DateCalc.subInterval(stop, VariablePeriod.EXPLICIT, new Long(random.nextInt(8)));
				}
			} else {
				participationDeadline = null;
			}
			newCourse.setParticipationDeadline(participationDeadline);
		}
		newCourse.setDepartmentId(getDepartmentId(departmentNum));
		Collection<CourseCategory> categories = courseCategoryDao.search(new Search(new SearchParameter[] {
				new SearchParameter("trialRequired", false, SearchParameter.EQUAL_COMPARATOR) }));
		newCourse.setCategoryId(getRandomElement(categories).getId());
		newCourse.setInstitutionId(getRandomBoolean(50) ? getRandomElement(institutions).getId() : null);
		newCourse.setName("course_" + (departmentNum + 1) + "_" + (courseNum + 1));
		newCourse.setDescription("description for " + newCourse.getName());
		newCourse.setStart(start);
		newCourse.setStop(stop);
		if (getRandomBoolean(50)) {
			newCourse.setShowCvPreset(true);
			newCourse.setCvTitle("Course " + (departmentNum + 1) + "-" + (courseNum + 1));
			if (getRandomBoolean(50)) {
				newCourse.setShowCommentCvPreset(true);
				newCourse.setCvCommentPreset("CV comment for " + newCourse.getCvTitle());
			} else {
				newCourse.setShowCommentCvPreset(false);
			}
			newCourse.setCvSectionPresetId(getRandomElement(selectionSetService.getCvSections(auth, null)).getId());
		} else {
			newCourse.setShowCvPreset(false);
		}
		if (getRandomBoolean(50)) {
			newCourse.setShowTrainingRecordPreset(true);
			//newCourse.setCvTitle(cvTitle);
			newCourse.setTrainingRecordSectionPresetId(getRandomElement(selectionSetService.getTrainingRecordSections(auth, null)).getId());
		} else {
			newCourse.setShowTrainingRecordPreset(false);
		}
		newCourse.setCertificate(false);
		CourseOutVO course = courseService.addCourse(auth, newCourse, null, null, null);
		jobOutput.println("course created: " + course.getName());
		ArrayList<Staff> persons = new ArrayList<Staff>(staffDao.search(new Search(new SearchParameter[] {
				new SearchParameter("person", true, SearchParameter.EQUAL_COMPARATOR) })));
		Iterator<Staff> participantStaffIt;
		if (course.getMaxNumberOfParticipants() != null) {
			participantStaffIt = getUniqueRandomElements(persons, CommonUtil.safeLongToInt(course.getMaxNumberOfParticipants())).iterator();
		} else {
			participantStaffIt = getUniqueRandomElements(persons, 1 + random.nextInt(maxParticipants)).iterator();
		}
		Collection<CourseParticipationStatusType> initialStates = courseParticipationStatusTypeDao.findInitialStates(true, course.isSelfRegistration());
		while (participantStaffIt.hasNext()) {
			CourseParticipationStatusEntryInVO newCourseParticipationStatusEntry = new CourseParticipationStatusEntryInVO();
			newCourseParticipationStatusEntry.setComment(course.getCvCommentPreset());
			newCourseParticipationStatusEntry.setCourseId(course.getId());
			newCourseParticipationStatusEntry.setCvSectionId(course.getCvSectionPreset() == null ? null : course.getCvSectionPreset().getId());
			newCourseParticipationStatusEntry.setShowCommentCv(course.getShowCommentCvPreset());
			newCourseParticipationStatusEntry.setShowCv(course.getShowCvPreset());
			newCourseParticipationStatusEntry.setTrainingRecordSectionId(course.getTrainingRecordSectionPreset() == null ? null : course.getTrainingRecordSectionPreset().getId());
			newCourseParticipationStatusEntry.setShowTrainingRecord(course.isShowTrainingRecordPreset());
			newCourseParticipationStatusEntry.setStaffId(participantStaffIt.next().getId());
			newCourseParticipationStatusEntry.setStatusId(getRandomElement(initialStates).getId());
			CourseParticipationStatusEntryOutVO participation = courseService.addCourseParticipationStatusEntry(auth, newCourseParticipationStatusEntry);
			jobOutput.println("participant " + participation.getStatus().getName() + ": " + participation.getStaff().getName());
		}
		return course;
	}

	public void createCourses(int courseCountPerDepartment) throws Exception {
		int newestCourseCount = (int) (0.5 * courseCountPerDepartment);
		for (int departmentNum = 0; departmentNum < departmentCount; departmentNum++) {
			Collection<Staff> institutions = staffDao.search(new Search(new SearchParameter[] {
					new SearchParameter("department.id", getDepartmentId(departmentNum), SearchParameter.EQUAL_COMPARATOR),
					new SearchParameter("person", false, SearchParameter.EQUAL_COMPARATOR) }));
			Collection<Staff> persons = staffDao.search(new Search(new SearchParameter[] {
					new SearchParameter("department.id", getDepartmentId(departmentNum), SearchParameter.EQUAL_COMPARATOR),
					new SearchParameter("person", true, SearchParameter.EQUAL_COMPARATOR) }));
			ArrayList<Long> createdIds = new ArrayList<Long>();
			for (int i = 0; i < newestCourseCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				CourseOutVO course = createCourse(auth, i, departmentNum, null, institutions, persons.size());
				createdIds.add(course.getId());
				createFiles(auth, FileModule.COURSE_DOCUMENT, course.getId(), FILE_COUNT_PER_COURSE);
			}
			for (int i = 0; i < (courseCountPerDepartment - newestCourseCount); i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				CourseOutVO course = createCourse(auth, newestCourseCount + i, departmentNum, getUniqueRandomElements(createdIds, random.nextInt(5)), institutions, persons.size());
				createdIds.add(course.getId());
				createFiles(auth, FileModule.COURSE_DOCUMENT, course.getId(), FILE_COUNT_PER_COURSE);
			}
		}
	}

	private CriteriaOutVO createCriteria(AuthenticationVO auth, DBModule module, List<SearchCriterion> criterions, String label, String comment, boolean loadByDefault)
			throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		CriteriaInVO newCriteria = new CriteriaInVO();
		newCriteria.setCategory("test_" + prefix);
		newCriteria.setComment(comment);
		newCriteria.setLabel(label);
		newCriteria.setLoadByDefault(loadByDefault);
		newCriteria.setModule(module);
		HashSet<CriterionInVO> newCriterions = new HashSet<CriterionInVO>(criterions.size());
		Iterator<SearchCriterion> it = criterions.iterator();
		int position = 1;
		while (it.hasNext()) {
			SearchCriterion criterion = it.next();
			newCriterions.add(criterion.buildCriterionInVO(this, module, position));
			position++;
		}
		CriteriaOutVO out = searchService.addCriteria(auth, newCriteria, newCriterions);
		jobOutput.println("criteria created: " + out.getLabel());
		return out;
	}

	public ArrayList<CriteriaOutVO> createCriterias() throws Throwable {
		AuthenticationVO auth = getRandomAuth();
		ArrayList<CriteriaOutVO> criterias = new ArrayList<CriteriaOutVO>();
		for (int i = 0; i < SearchCriteria.values().length; i++) {
			criterias.add(getCriteria(auth, SearchCriteria.values()[i]));
		}
		return criterias;
	}

	private InputFieldOutVO createDateField(AuthenticationVO auth, String name, String category, String title, String comment, Date datePreset, Date minDate, Date maxDate,
			String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.DATE);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinDate(minDate);
		newInputField.setMaxDate(maxDate);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setDatePreset(datePreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("date input field created: " + out.getName());
		return out;
	}

	private Long createDemoEcrfIncrement() {
		return null;
	}

	private ArrayList<ECRFFieldOutVO> createDemoEcrfMedicalHistory(AuthenticationVO auth, TrialOutVO trial, Long probandGroupId, int position, Long visitId) throws Throwable {
		ECRFOutVO ecrf = createEcrf(auth, trial, "medical history", "eCRF to capture ICD-10 coded medical history", probandGroupId, position, visitId, true, false, true, 0.0f,
				null);
		ArrayList<ECRFFieldOutVO> ecrfFields = new ArrayList<ECRFFieldOutVO>();
		ecrfFields.add(createEcrfField(auth, InputFields.DIAGNOSIS_COUNT, ecrf, "summary", 1, false, false, false, true, true, null, null, "count",
				"function(alphaid) {\n" +
						"  return alphaid.length - 1;\n" +
						"}",
				"function() {\n" +
						"  return sprintf('medical history: %d entries',$value);\n" +
						"}"));
		ecrfFields.add(createEcrfField(auth, InputFields.DIAGNOSIS_START, ecrf, "medical history", 1, true, true, false, true, true,
				null, "date when disease started, if known/applicable", null, null, null));
		ecrfFields.add(
				createEcrfField(auth, InputFields.DIAGNOSIS_END, ecrf, "medical history", 2, true, true, false, true, true, null, "date when disease ended, if known/applicable",
						null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.ALPHA_ID, ecrf, "medical history", 3, true, false, false, true, true, null, "disease name", "alphaid",
				null,
				"function() {\n" +
						"  if ($enteredValue != $oldValue) {\n" +
						"    var output = 'Matching AlphaID synonyms: ';\n" +
						"    var request = RestApi.createRequest('GET', 'tools/complete/alphaidtext');\n" +
						"    request.data = 'textInfix=' + $enteredValue;\n" +
						"    request.success = function(data, code, jqXHR) {\n" +
						"      if (data.length > 0) {\n" +
						"        output += '<select onchange=\"FieldCalculation.setVariable([\\'alphaid\\',' + $index + '],';\n" +
						"        output += 'this.options[this.selectedIndex].text,true);\">';\n" +
						"        for (var i = 0; i < data.length; i++) {\n" +
						"          output += '<option>' + data[i] + '</option>';\n" +
						"        }\n" +
						"        output += '</select>';\n" +
						"      } else {\n" +
						"        output += 'no hits';\n" +
						"      }\n" +
						"      setOutput(['alphaid',$index],output);\n" +
						"    };\n" +
						"    RestApi.executeRequest(request);\n" +
						"    return output;\n" +
						"  } else {\n" +
						"    return $output;\n" +
						"  }\n" +
						"}"));
		return ecrfFields;
	}

	private ArrayList<ECRFFieldOutVO> createDemoEcrfSum(AuthenticationVO auth, TrialOutVO trial, Long probandGroupId, int position, Long visitId) throws Throwable {
		ECRFOutVO ecrf = createEcrf(auth, trial, "some eCRF", "demo eCRF to show field calculations with series sections", probandGroupId, position, visitId, true, false, true,
				0.0f, null);
		ArrayList<ECRFFieldOutVO> ecrfFields = new ArrayList<ECRFFieldOutVO>();
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_SINGLELINE, ecrf, "series #1", 1, true, false, false, true, true, null, "some name", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "series #1", 2, true, false, false, true, true, null, "some repeatable value 1", "value1", null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "series #1", 3, true, false, false, true, true, null, "some repeatable value 2", "value2", null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_MULTILINE, ecrf, "series #1", 4, true, false, false, true, true, null, "some description", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.FLOAT, ecrf, "series #1", 5, true, false, false, true, true, null, "some repeatable value 3", "value3",
				"function(value1, value2) {\n" +
						"  return value1 + value2;\n" +
						"}",
				"function(value3) {\n" +
						"  return sprintf(\"value3 = value1 + value2 = %.3f\",value3);\n" +
						"}"));
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_SINGLELINE, ecrf, "series #2", 1, true, false, false, true, true, null, "some name", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "series #2", 2, true, false, false, true, true, null, "some repeatable value 4", "value4", null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "series #2", 3, true, false, false, true, true, null, "some repeatable value 5", "value5", null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_MULTILINE, ecrf, "series #2", 4, true, false, false, true, true, null, "some description", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.FLOAT, ecrf, "series #2", 5, true, false, false, true, true, null, "some repeatable value 6", "value6",
				"function(value4, value5) {\n" +
						"  return value4 + value5;\n" +
						"}",
				"function(value6) {\n" +
						"  return sprintf(\"value6 = value4 + value5 = %.3f\",value6);\n" +
						"}"));
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_SINGLELINE, ecrf, "totals section", 1, false, false, false, true, true, null, "some name", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "totals section", 2, false, false, false, true, true, null, "some total value 1", "total1",
				"function(value1, value4) {\n" +
						"  var sum = 0;\n" +
						"  var i;\n" +
						"  for (i = 0; i < value1.length; i++) {\n" +
						"    sum += value1[i];\n" +
						"  }\n" +
						"  for (i = 0; i < value4.length; i++) {\n" +
						"    sum += value4[i];\n" +
						"  }\n" +
						"  return sum;\n" +
						"}",
				"function(total1) {\n" +
						"  return sprintf(\"total1 = sum(value1) + sum(value4) = %.3f\",total1);\n" +
						"}"));
		ecrfFields.add(createEcrfField(auth, InputFields.INTEGER, ecrf, "totals section", 3, false, false, false, true, true, null, "some total value 2", "total2",
				"function(value2, value5) {\n" +
						"  var sum = 0;\n" +
						"  var i;\n" +
						"  for (i = 0; i < value2.length; i++) {\n" +
						"    sum += value2[i];\n" +
						"  }\n" +
						"  for (i = 0; i < value5.length; i++) {\n" +
						"    sum += value5[i];\n" +
						"  }\n" +
						"  return sum;\n" +
						"}",
				"function(total2) {\n" +
						"  return sprintf(\"total2 = sum(value2) + sum(value5) = %.3f\",total2);\n" +
						"}"));
		ecrfFields.add(createEcrfField(auth, InputFields.STRING_MULTILINE, ecrf, "totals section", 4, false, false, false, true, true, null, "some description", null, null, null));
		ecrfFields.add(createEcrfField(auth, InputFields.FLOAT, ecrf, "totals section", 5, false, false, false, true, true, null, "some total value 3", "total3",
				"function(total1, total2) {\n" +
						"  return total1 + total2;\n" +
						"}",
				"function(total3,value3,value6) {\n" +
						"  var sum = 0;\n" +
						"  var i;\n" +
						"  for (i = 0; i < value3.length; i++) {\n" +
						"    sum += value3[i];\n" +
						"  }\n" +
						"  for (i = 0; i < value6.length; i++) {\n" +
						"    sum += value6[i];\n" +
						"  }\n" +
						"  return sprintf(\"total3 = total1 + total2 = %.3f = sum(value3) + sum(value6) = %.3f\",total3,sum);\n" +
						"}"));
		return ecrfFields;
	}

	private DepartmentVO createDepartment(String nameL10nKey, boolean visible, String plainDepartmentPassword) throws Exception {
		Department department = Department.Factory.newInstance();
		department.setNameL10nKey(nameL10nKey);
		department.setVisible(visible);
		CryptoUtil.encryptDepartmentKey(department, CryptoUtil.createRandomKey().getEncoded(), plainDepartmentPassword);
		DepartmentVO out = departmentDao.toDepartmentVO(departmentDao.create(department));
		jobOutput.println("department created: " + out.getName());
		return out;
	}

	public void createDepartmentsAndUsers(int departmentCount, int usersPerDepartmentCount) throws Exception {
		this.usersPerDepartmentCount = usersPerDepartmentCount;
		for (int i = 0; i < departmentCount; i++) {
			DepartmentVO department = createDepartment(getDepartmentName(i), true, getDepartmentPassword(i));
			this.departmentCount++;
			for (int j = 0; j < usersPerDepartmentCount; j++) {
				createUser(getUsername(i, j), getUserPassword(i, j), department.getId(), getDepartmentPassword(i));
			}
		}
	}

	private Collection<DutyRosterTurnOutVO> createDuty(AuthenticationVO auth, ArrayList<Staff> staff, TrialOutVO trial, Date start, Date stop, String title) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		Collection<VisitScheduleItemOutVO> visitScheduleItems = trialService.getVisitScheduleItems(auth, trial.getId(), null, start, stop, null, false);
		DutyRosterTurnInVO newDutyRosterTurn = new DutyRosterTurnInVO();
		newDutyRosterTurn.setSelfAllocatable(staff.size() > 0);
		newDutyRosterTurn.setStart(start);
		newDutyRosterTurn.setStop(stop);
		newDutyRosterTurn.setTrialId(trial.getId());
		ArrayList<DutyRosterTurnOutVO> out = new ArrayList<DutyRosterTurnOutVO>();
		if (title == null && visitScheduleItems.size() > 0) {
			Iterator<Staff> staffIt = getUniqueRandomElements(staff, visitScheduleItems.size()).iterator();
			Iterator<VisitScheduleItemOutVO> visitScheduleItemsIt = visitScheduleItems.iterator();
			int dutyCount = 0;
			while (staffIt.hasNext() && visitScheduleItemsIt.hasNext()) {
				VisitScheduleItemOutVO visitScheduleItem = visitScheduleItemsIt.next();
				newDutyRosterTurn.setStaffId(staffIt.next().getId());
				newDutyRosterTurn.setTitle(null);
				newDutyRosterTurn.setComment(null);
				newDutyRosterTurn.setVisitScheduleItemId(visitScheduleItem.getId());
				try {
					out.add(staffService.addDutyRosterTurn(auth, newDutyRosterTurn));
					dutyCount++;
				} catch (ServiceException e) {
					jobOutput.println(e.getMessage());
				}
			}
			jobOutput.println(dutyCount + " duty roster turns for " + visitScheduleItems.size() + " visit schedule items created");
			return out;
		} else {
			newDutyRosterTurn.setStaffId(staff.size() == 0 ? null : getRandomElement(staff).getId());
			newDutyRosterTurn.setTitle(title);
			newDutyRosterTurn.setComment(null);
			newDutyRosterTurn.setVisitScheduleItemId(visitScheduleItems.size() > 0 ? visitScheduleItems.iterator().next().getId() : null);
			try {
				out.add(staffService.addDutyRosterTurn(auth, newDutyRosterTurn));
				jobOutput.println("duty roster turn created: " + title);
			} catch (ServiceException e) {
				jobOutput.println(e.getMessage());
			}
		}
		return out;
	}

	private ECRFOutVO createEcrf(AuthenticationVO auth, TrialOutVO trial, String name, String title, Long probandGroupId, int position, Long visitId, boolean active,
			boolean disabled, boolean enableBrowserFieldCalculation, float charge,
			String description) throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		ECRFInVO newEcrf = new ECRFInVO();
		newEcrf.setName(name);
		newEcrf.setTitle(title);
		newEcrf.setPosition(new Long(position));
		newEcrf.setTrialId(trial.getId());
		newEcrf.setActive(active);
		newEcrf.setDescription(description);
		newEcrf.setDisabled(disabled);
		newEcrf.setEnableBrowserFieldCalculation(enableBrowserFieldCalculation);
		newEcrf.setCharge(charge);
		newEcrf.setGroupId(probandGroupId);
		newEcrf.setVisitId(visitId);
		ECRFOutVO out = trialService.addEcrf(auth, newEcrf);
		jobOutput.println("eCRF created: " + out.getUniqueName());
		return out;
	}

	private ECRFFieldOutVO createEcrfField(AuthenticationVO auth,
			InputFields inputField, ECRFOutVO ecrf,
			String section, int position, boolean series, boolean optional, boolean disabled, boolean auditTrail, boolean reasonForChangeRequired, String title, String comment,
			String jsVariableName, String jsValueExpression, String jsOutputExpression) throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		ECRFFieldInVO newEcrfField = new ECRFFieldInVO();
		newEcrfField.setAuditTrail(auditTrail);
		newEcrfField.setComment(comment);
		newEcrfField.setTitle(title);
		newEcrfField.setDisabled(disabled);
		newEcrfField.setEcrfId(ecrf.getId());
		newEcrfField.setFieldId(getInputField(auth, inputField).getId());
		newEcrfField.setOptional(optional);
		newEcrfField.setPosition(new Long(position));
		newEcrfField.setReasonForChangeRequired(reasonForChangeRequired);
		newEcrfField.setNotify(false);
		newEcrfField.setSection(section);
		newEcrfField.setSeries(series);
		newEcrfField.setTrialId(ecrf.getTrial().getId());
		newEcrfField.setJsVariableName(jsVariableName);
		newEcrfField.setJsValueExpression(jsValueExpression);
		newEcrfField.setJsOutputExpression(jsOutputExpression);
		ECRFFieldOutVO out = trialService.addEcrfField(auth, newEcrfField);
		jobOutput.println("eCRF field created: " + out.getUniqueName());
		return out;
	}

	private FileOutVO createFile(AuthenticationVO auth, FileModule module, Long id, ArrayList<String> folders) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		FileInVO newFile = new FileInVO();
		newFile.setActive(getRandomBoolean(50));
		newFile.setPublicFile(true);
		switch (module) {
			case INVENTORY_DOCUMENT:
				newFile.setInventoryId(id);
				break;
			case STAFF_DOCUMENT:
				newFile.setStaffId(id);
				break;
			case COURSE_DOCUMENT:
				newFile.setCourseId(id);
				break;
			case TRIAL_DOCUMENT:
				newFile.setTrialId(id);
				break;
			case PROBAND_DOCUMENT:
				newFile.setProbandId(id);
				break;
			case MASS_MAIL_DOCUMENT:
				newFile.setMassMailId(id);
				break;
			default:
		}
		newFile.setModule(module);
		if (folders.size() == 0) {
			folders.add(CommonUtil.LOGICAL_PATH_SEPARATOR);
			folders.addAll(fileService.getFileFolders(auth, module, id, CommonUtil.LOGICAL_PATH_SEPARATOR, false, null, null, null));
		}
		StringBuilder logicalPath = new StringBuilder(getRandomElement(folders));
		if (getRandomBoolean(50)) {
			logicalPath.append(CommonUtil.generateUUID());
			logicalPath.append(CommonUtil.LOGICAL_PATH_SEPARATOR);
			folders.add(logicalPath.toString());
		}
		newFile.setLogicalPath(logicalPath.toString());
		PDFImprinter blankPDF = new PDFImprinter();
		PDFStream pdfStream = new PDFStream();
		blankPDF.setOutput(pdfStream);
		blankPDF.render();
		FileStreamInVO newFileStream = new FileStreamInVO();
		StringBuilder fileName = new StringBuilder(CommonUtil.generateUUID());
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		newFileStream.setFileName(fileName.toString());
		newFileStream.setMimeType(CoreUtil.PDF_MIMETYPE_STRING);
		newFileStream.setSize((long) pdfStream.getSize());
		newFileStream.setStream(pdfStream.getInputStream());
		newFile.setComment("test file");
		newFile.setTitle(fileName.toString());
		return fileService.addFile(auth, newFile, newFileStream);
	}

	private void createFiles(AuthenticationVO auth, FileModule module, Long id, int fileCount) throws Exception {
		if (CREATE_FILES) {
			auth = (auth == null ? getRandomAuth() : auth);
			ArrayList<String> folders = new ArrayList<String>();
			for (int i = 0; i < fileCount; i++) {
				createFile(auth, module, id, folders);
			}
			jobOutput.println(fileCount + " files created for " + module + " enitity " + id);
		}
	}

	private InputFieldOutVO createFloatField(AuthenticationVO auth, String name, String category, String title, String comment, Float floatPreset, Float lowerLimit,
			Float upperLimit, String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.FLOAT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setFloatLowerLimit(lowerLimit);
		newInputField.setFloatUpperLimit(upperLimit);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setFloatPreset(floatPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("float input field created: " + out.getName());
		return out;
	}

	public TrialOutVO createFormScriptingTrial() throws Throwable {
		int departmentNum = 0;
		AuthenticationVO auth = getRandomAuth(departmentNum);
		TrialInVO newTrial = new TrialInVO();
		newTrial.setStatusId(trialStatusTypeDao.searchUniqueNameL10nKey("migration_started").getId());
		newTrial.setDepartmentId(getDepartmentId(departmentNum));
		newTrial.setName("demo:form-scripting");
		newTrial.setTitle("Inquiry form showing various form field calculation examples.");
		newTrial.setDescription("");
		newTrial.setSignupProbandList(false);
		newTrial.setSignupInquiries(false);
		newTrial.setSignupRandomize(false);
		newTrial.setSignupDescription("");
		newTrial.setExclusiveProbands(false);
		newTrial.setProbandAliasFormat("");
		newTrial.setDutySelfAllocationLocked(false);
		newTrial.setTypeId(trialTypeDao.searchUniqueNameL10nKey("na").getId());
		newTrial.setSponsoringId(sponsoringTypeDao.searchUniqueNameL10nKey("na").getId());
		newTrial.setSurveyStatusId(getRandomElement(selectionSetService.getSurveyStatusTypes(auth, null)).getId());
		TrialOutVO trial = trialService.addTrial(auth, newTrial, null);
		jobOutput.println("trial created: " + trial.getName());
		ArrayList<InquiryOutVO> inquiries = new ArrayList<InquiryOutVO>();
		inquiries.add(createInquiry(auth, InputFields.BODY_HEIGHT, trial, "01 - BMI", 1, true, true, false, false, false, false, null,
				null,
				"size",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.BODY_WEIGHT, trial, "01 - BMI", 2, true, true, false, false, false, false, null,
				null,
				"weight",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.BODY_MASS_INDEX, trial, "01 - BMI", 3, true, true, false, false, false, false, null,
				null,
				"bmi",
				"function(weight,size) {\n" +
						"  return weight / (size * size / 10000.0);\n" +
						"}",
				"function(bmi) {\n" +
						"  return sprintf(\"BMI entered: %.6f<br>\" +\n" +
						"    \"BMI calculated: %.6f\",\n" +
						"    $enteredValue + 0.0,\n" +
						"    bmi);\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.OBESITY, trial, "01 - BMI", 4, true, true, false, false, false, false, null,
				null,
				"obesity",
				"function(bmi) {\n" +
						"  var selection;\n" +
						"  if (bmi < 18.5) {\n" +
						"    selection = \"shortweight\";\n" +
						"  } else if (bmi < 25) {\n" +
						"    selection = \"normal weight\";\n" +
						"  } else if (bmi < 30) {\n" +
						"    selection = \"overweight\"; \n" +
						"  } else if (bmi < 35) {\n" +
						"    selection = \"adiposity degree I\";\n" +
						"  } else if (bmi < 40) { \n" +
						"    selection = \"adiposity degree II\";\n" +
						"  } else {\n" +
						"    selection = \"adiposity degree III\";\n" +
						"  }\n" +
						"  return findSelectionSetValueIds(function(option){\n" +
						"    return option.name == selection;});\n" +
						"}",
				"function(obesity) {\n" +
						"  return \"entered: \" +\n" +
						"    printSelectionSetValues($enteredValue) + \"<br>\" +\n" +
						"    \"calculated: <em>\" +\n" +
						"    printSelectionSetValues(obesity) + \"</em>\";\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.ETHNICITY, trial, "02 - eGFR", 1, true, true, false, false, false, false, null,
				null,
				"ethnicity",
				"",
				"function() {\n" +
						"  return sprintf(\"<a href=\\\"http://www.fda.gov/RegulatoryInformation/Guidances/ucm126340.htm\\\" target=\\\"new\\\">FDA Information</a>\");\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.SERUM_CREATININ_CONCENTRATION, trial, "02 - eGFR", 2, true, true, false, false, false, false, null,
				null,
				"s_cr",
				"",
				""));
		inquiries.add(createInquiry(auth, InputFields.EGFR, trial, "02 - eGFR", 3, true, true, false, false, false, false, null,
				null,
				"egfr",
				"function(s_cr, ethnicity) { // s_cr: serum creatinin concentration (decimal), skin_color (single selection)\n" +
						"  var result = 175.0 * Math.pow(s_cr, -1.154) * Math.pow($proband.age, -0.203);\n" +
						"  if (getInputFieldSelectionSetValue(\'ethnicity\', ethnicity[0]).value == \'Black\') {\n" +
						"    result *= 1.210;\n" +
						"  }\n" +
						"  if ($proband.gender.sex == \'FEMALE\') {\n" +
						"    result *= 0.742;\n" +
						"  }\n" +
						"  return result;\n" +
						"}",
				"function(egfr) {\n" +
						"  return sprintf(\"eGFR entered: %.6f<br>\" + \n" +
						"    \"eGFR calculated: %.6f\",\n" +
						"    $enteredValue + 0.0,\n" +
						"    egfr);\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.HBA1C_PERCENT, trial, "03 - HbA1C", 1, true, true, false, false, false, false, null,
				null,
				"hba1c_percent",
				"//function(hba1c_mmol_per_mol) {\n" +
						"//  return hba1c_mmol_per_mol * 0.0915 + 2.15;\n" +
						"//}",
				"function() {\n" +
						"  return sprintf(\"HbA1c (%%): %.1f<br/>\" +\n" +
						"    \"HbA1c (mmol/mol): %.2f\",\n" +
						"    $enteredValue + 0.0,\n" +
						"    ($enteredValue - 2.15) * 10.929);\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.HBA1C_MMOLPERMOL, trial, "03 - HbA1C", 2, true, true, false, false, false, false, null,
				null,
				"hba1c_mmol_per_mol",
				"function(hba1c_percent) {\n" +
						"  return (hba1c_percent - 2.15) * 10.929;\n" +
						"}",
				"//function() {\n" +
						"//  return sprintf(\"HbA1c (mmol/mol): %.2f<br/>\" +\n" +
						"//    \"HbA1c (%%): %.1f\",\n" +
						"//    $enteredValue + 0.0,\n" +
						"//   $enteredValue * 0.0915 + 2.15);\n" +
						"//}\n" +
						"function(hba1c_mmol_per_mol) {\n" +
						"  return sprintf(\"HbA1c entered (mmol/mol): %.6f<br/>\" +\n" +
						"    \"HbA1c calculated (mmol/mol): %.6f\",\n" +
						"    $enteredValue + 0.0,\n" +
						"    hba1c_mmol_per_mol);\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.MANNEQUIN, trial, "04 - DAS28", 1, true, true, false, false, false, false, null,
				"tender joints",
				"tender",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.MANNEQUIN, trial, "04 - DAS28", 2, true, true, false, false, false, false, null,
				"swollen joints",
				"swollen",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.ESR, trial, "04 - DAS28", 3, true, true, false, false, false, false, null,
				null,
				"esr",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.VAS, trial, "04 - DAS28", 4, true, true, false, false, false, false, null,
				"The patient's general health condition (subjective)",
				"vas",
				null,
				null));
		inquiries.add(createInquiry(auth, InputFields.DAS28, trial, "04 - DAS28", 5, true, true, false, false, false, false, null,
				null,
				"das28",
				"function (tender, swollen, esr, vas) { //das (28 joints) computation:\n" +
						"  return 0.56 * Math.sqrt(tender.ids.length) + //tender joint count (0-28)\n" +
						"    0.28 * Math.sqrt(swollen.ids.length) + //swollen joint count (0-28)\n" +
						"    0.7 * Math.log(esr) + //erythrocyte sedimentation rate reading (5-30 mm/h)\n" +
						"    0.014 * getInputFieldSelectionSetValue(\"vas\",vas.ids[0]).value; //the proband\'s\n" +
						"    //subjective assessment of recent disease activity (visual analog scale, 0-100 mm)\n" +
						"}",
				"function(das28) {\n" +
						"  return sprintf(\"entered: %.2f<br/>\" +\n" +
						"    \"calculated: %.2f\", $enteredValue + 0.0,das28 + 0.0);\n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.DISTANCE, trial, "05 - Google Maps", 1, true, true, false, false, false, false, null,
				null,
				"distance",
				"function() {\n" +
						"  if ($enteredValue) {\n" +
						"    return $enteredValue; \n" +
						"  }\n" +
						"  if ($probandAddresses) {\n" +
						"    for (var i in $probandAddresses) {\n" +
						"      if ($probandAddresses[i].wireTransfer) {\n" +
						"        LocationDistance.calcRouteDistance(null,$probandAddresses[i].civicName,function(d,j){\n" +
						"          setVariable(\"distance\",Math.round(d/1000.0));\n" +
						"        },i);\n" +
						"        return;\n" +
						"      }\n" +
						"    }\n" +
						"  }\n" +
						"  return;\n" +
						"}",
				"function(distance){\n" +
						"  if ($enteredValue) {\n" +
						"    return \"\"; \n" +
						"  }\n" +
						"  if ($probandAddresses) {\n" +
						"    for (var i in $probandAddresses) {\n" +
						"      if ($probandAddresses[i].wireTransfer) {\n" +
						"        if (distance != null) {\n" +
						"          return sprintf(\"Distance from %s to %s: %d km\",LocationDistance.currentSubLocality,$probandAddresses[i].name,distance);\n" +
						"        } else {\n" +
						"          return \"querying Google Maps ...\"; \n" +
						"        }\n" +
						"      }\n" +
						"    }\n" +
						"  }\n" +
						"  return \"No subject address.\"; \n" +
						"}"));
		inquiries.add(createInquiry(auth, InputFields.ALPHA_ID, trial, "06 - Rest API", 1, true, true, false, false, false, false, null,
				"(Medical Coding example ...)",
				"alphaid",
				null,
				"function() {\n" +
						"  if ($enteredValue != $oldValue) {\n" +
						"    var output = 'Matching AlphaID synonyms: ';\n" +
						"    var request = RestApi.createRequest('GET', 'tools/complete/alphaidtext');\n" +
						"    request.data = 'textInfix=' + $enteredValue;\n" +
						"    request.success = function(data, code, jqXHR) {\n" +
						"      if (data.length > 0) {\n" +
						"        output += '<select onchange=\"FieldCalculation.setVariable(\\'alphaid\\',';\n" +
						"        output += 'this.options[this.selectedIndex].text,true);\">';\n" +
						"        for (var i = 0; i < data.length; i++) {\n" +
						"          output += '<option>' + data[i] + '</option>';\n" +
						"        }\n" +
						"        output += '</select>';\n" +
						"      } else {\n" +
						"        output += 'no hits';\n" +
						"      }\n" +
						"      setOutput('alphaid',output);\n" +
						"    };\n" +
						"    RestApi.executeRequest(request);\n" +
						"    return output;\n" +
						"  } else {\n" +
						"    return $output;\n" +
						"  }\n" +
						"}"));
		createDemoEcrfSum(auth, trial, null, 1, null);
		createDemoEcrfMedicalHistory(auth, trial, null, 2, null);
		return trial;
	}

	public TrialOutVO createGroupCoinRandomizationTrial(int probandGroupCount, int probandCount)
			throws Throwable {
		int departmentNum = 0;
		AuthenticationVO auth = getRandomAuth(departmentNum);
		TrialInVO newTrial = new TrialInVO();
		newTrial.setStatusId(trialStatusTypeDao.searchUniqueNameL10nKey("migration_started").getId());
		newTrial.setDepartmentId(getDepartmentId(departmentNum));
		newTrial.setName("demo:group coin randomization");
		newTrial.setTitle("Group coin randomization testcase.");
		newTrial.setDescription("");
		newTrial.setRandomization(RandomizationMode.GROUP_COIN);
		newTrial.setSignupProbandList(false);
		newTrial.setSignupInquiries(false);
		newTrial.setSignupRandomize(false);
		newTrial.setSignupDescription("");
		newTrial.setExclusiveProbands(false);
		newTrial.setProbandAliasFormat("");
		newTrial.setDutySelfAllocationLocked(false);
		newTrial.setTypeId(trialTypeDao.searchUniqueNameL10nKey("na").getId());
		newTrial.setSponsoringId(sponsoringTypeDao.searchUniqueNameL10nKey("na").getId());
		newTrial.setSurveyStatusId(getRandomElement(selectionSetService.getSurveyStatusTypes(auth, null)).getId());
		TrialOutVO trial = trialService.addTrial(auth, newTrial, null);
		jobOutput.println("trial created: " + trial.getName());
		ProbandGroupInVO newProbandGroup = new ProbandGroupInVO();
		newProbandGroup.setTitle("Screeninggruppe");
		newProbandGroup.setToken("SG");
		newProbandGroup.setTrialId(trial.getId());
		newProbandGroup.setRandomize(false);
		ProbandGroupOutVO screeningGroup = trialService.addProbandGroup(auth, newProbandGroup);
		jobOutput.println("proband group created: " + screeningGroup.getTitle());
		LinkedHashMap<Long, ProbandGroupOutVO> probandGroupMap = new LinkedHashMap<Long, ProbandGroupOutVO>();
		for (int i = 0; i < probandGroupCount; i++) {
			newProbandGroup = new ProbandGroupInVO();
			newProbandGroup.setTitle("Gruppe " + (i + 1));
			newProbandGroup.setToken("G" + (i + 1));
			newProbandGroup.setTrialId(trial.getId());
			newProbandGroup.setRandomize(true);
			ProbandGroupOutVO probandGroup = trialService.addProbandGroup(auth, newProbandGroup);
			jobOutput.println("proband group created: " + probandGroup.getTitle());
			probandGroupMap.put(probandGroup.getId(), probandGroup);
		}
		ProbandCategory probandCategory = probandCategoryDao.search(new Search(new SearchParameter[] {
				new SearchParameter("nameL10nKey", "test", SearchParameter.EQUAL_COMPARATOR) })).iterator().next();
		HashMap<Long, Long> groupSizes = new HashMap<Long, Long>();
		for (int i = 0; i < probandCount; i++) {
			ProbandInVO newProband = new ProbandInVO();
			newProband.setDepartmentId(getDepartmentId(departmentNum));
			newProband.setCategoryId(probandCategory.getId());
			newProband.setPerson(true);
			newProband.setBlinded(true);
			newProband.setAlias(MessageFormat.format("{0} {1}", trial.getName(), i + 1));
			newProband.setGender(getRandomBoolean(50) ? Sex.MALE : Sex.FEMALE);
			newProband.setDateOfBirth(getRandomDateOfBirth());
			ProbandOutVO proband = probandService.addProband(auth, newProband, null, null, null);
			jobOutput.println("proband created: " + proband.getName());
			ProbandListEntryInVO newProbandListEntry = new ProbandListEntryInVO();
			newProbandListEntry.setPosition(i + 1l);
			newProbandListEntry.setTrialId(trial.getId());
			newProbandListEntry.setProbandId(proband.getId());
			ProbandListEntryOutVO probandListEntry = trialService.addProbandListEntry(auth, false, false, true, newProbandListEntry);
			jobOutput.println("proband list entry created - trial: " + probandListEntry.getTrial().getName() + " position: " + probandListEntry.getPosition() + " proband: "
					+ probandListEntry.getProband().getName());
			if (groupSizes.containsKey(probandListEntry.getGroup().getId())) {
				groupSizes.put(probandListEntry.getGroup().getId(), groupSizes.get(probandListEntry.getGroup().getId()) + 1l);
			} else {
				groupSizes.put(probandListEntry.getGroup().getId(), 1l);
			}
		}
		Iterator<Long> probandGroupIdsIt = probandGroupMap.keySet().iterator();
		while (probandGroupIdsIt.hasNext()) {
			Long probandGroupId = probandGroupIdsIt.next();
			jobOutput.println(probandGroupMap.get(probandGroupId).getToken() + ": " + groupSizes.get(probandGroupId) + " probands");
		}
		return trial;
	}

	public ArrayList<InputFieldOutVO> createInputFields() throws Throwable {
		AuthenticationVO auth = getRandomAuth();
		ArrayList<InputFieldOutVO> inputFields = new ArrayList<InputFieldOutVO>();
		for (int i = 0; i < InputFields.values().length; i++) {
			inputFields.add(getInputField(auth, InputFields.values()[i]));
		}
		return inputFields;
	}

	private InquiryOutVO createInquiry(AuthenticationVO auth, InputFields inputField, TrialOutVO trial, String category, int position, boolean active, boolean activeSignup,
			boolean optional,
			boolean disabled, boolean excelValue, boolean excelDate, String title, String comment, String jsVariableName, String jsValueExpression, String jsOutputExpression)
			throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		InquiryInVO newInquiry = new InquiryInVO();
		newInquiry.setCategory(category);
		newInquiry.setActive(active);
		newInquiry.setActiveSignup(activeSignup);
		newInquiry.setOptional(optional);
		newInquiry.setDisabled(disabled);
		newInquiry.setExcelValue(excelValue);
		newInquiry.setExcelDate(excelDate);
		newInquiry.setFieldId(getInputField(auth, inputField).getId());
		newInquiry.setTrialId(trial.getId());
		newInquiry.setPosition(new Long(position));
		newInquiry.setComment(comment);
		newInquiry.setTitle(title);
		newInquiry.setJsVariableName(jsVariableName);
		newInquiry.setJsValueExpression(jsValueExpression);
		newInquiry.setJsOutputExpression(jsOutputExpression);
		InquiryOutVO out = trialService.addInquiry(auth, newInquiry);
		jobOutput.println("inquiry created: " + out.getUniqueName());
		return out;
	}

	private InputFieldOutVO createIntegerField(AuthenticationVO auth, String name, String category, String title, String comment, Long longPreset, Long lowerLimit,
			Long upperLimit, String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.INTEGER);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setLongLowerLimit(lowerLimit);
		newInputField.setLongUpperLimit(upperLimit);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setLongPreset(longPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("integer input field created: " + out.getName());
		return out;
	}

	private InventoryOutVO createInventory(AuthenticationVO auth, int inventoryNum, int departmentNum, Long parentId, Collection<Staff> owners) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InventoryInVO newInventory = new InventoryInVO();
		newInventory.setBookable(getRandomBoolean(50));
		if (newInventory.getBookable()) {
			newInventory.setMaxOverlappingBookings(1l);
		} else {
			newInventory.setMaxOverlappingBookings(0l);
		}
		newInventory.setOwnerId(getRandomBoolean(50) ? getRandomElement(owners).getId() : null);
		newInventory.setParentId(parentId);
		newInventory.setDepartmentId(getDepartmentId(departmentNum));
		newInventory.setCategoryId(getRandomElement(selectionSetService.getInventoryCategories(auth, null)).getId());
		newInventory.setName("inventory_" + (departmentNum + 1) + "_" + (inventoryNum + 1));
		newInventory.setPieces(random.nextInt(5) + 1L);
		InventoryOutVO out = inventoryService.addInventory(auth, newInventory, null, null, null);
		jobOutput.println("inventory created: " + out.getName());
		return out;
	}

	public void createInventory(int inventoryCountPerDepartment) throws Exception {
		int inventoryRootCount = (int) (0.1 * inventoryCountPerDepartment);
		for (int departmentNum = 0; departmentNum < departmentCount; departmentNum++) {
			Collection<Staff> owners = staffDao.search(new Search(new SearchParameter[] {
					new SearchParameter("department.id", getDepartmentId(departmentNum), SearchParameter.EQUAL_COMPARATOR),
					new SearchParameter("person", false, SearchParameter.EQUAL_COMPARATOR) }));
			ArrayList<Long> createdIds = new ArrayList<Long>();
			for (int i = 0; i < inventoryRootCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				InventoryOutVO inventory = createInventory(auth, i, departmentNum, null, owners);
				createdIds.add(inventory.getId());
				createFiles(auth, FileModule.INVENTORY_DOCUMENT, inventory.getId(), FILE_COUNT_PER_INVENTORY);
			}
			for (int i = 0; i < (inventoryCountPerDepartment - inventoryRootCount); i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				InventoryOutVO inventory = createInventory(auth, inventoryRootCount + i, departmentNum, getRandomElement(createdIds), owners);
				createdIds.add(inventory.getId());
				createFiles(auth, FileModule.INVENTORY_DOCUMENT, inventory.getId(), FILE_COUNT_PER_INVENTORY);
			}
		}
	}

	private InputFieldOutVO createMultiLineTextField(AuthenticationVO auth, String name, String category, String title, String comment, String textPreset, String regExp,
			String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.MULTI_LINE_TEXT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setRegExp(regExp);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTextPreset(textPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("multi line text input field created: " + out.getName());
		return out;
	}

	private ProbandOutVO createProband(AuthenticationVO auth, int departmentNum, Sex sex, Collection<Long> childIds, Collection<ProbandCategory> categories,
			Collection<String> titles)
			throws Exception {
		// todo: relatives graph
		auth = (auth == null ? getRandomAuth() : auth);
		ProbandInVO newProband = new ProbandInVO();
		newProband.setDepartmentId(getDepartmentId(departmentNum));
		newProband.setCategoryId(getRandomElement(categories).getId());
		if (getRandomBoolean(25)) {
			newProband.setPrefixedTitle1(getRandomElement(titles));
			if (getRandomBoolean(20)) {
				newProband.setPrefixedTitle2(getRandomElement(titles));
			}
		}
		if (sex == null) {
			if (getRandomBoolean(50)) {
				sex = Sex.MALE;
			} else {
				sex = Sex.FEMALE;
			}
		}
		newProband.setGender(sex);
		newProband.setFirstName(Sex.MALE == sex ? getRandomElement(GermanPersonNames.MALE_FIRST_NAMES) : getRandomElement(GermanPersonNames.FEMALE_FIRST_NAMES));
		newProband.setPerson(true);
		newProband.setBlinded(false);
		newProband.setLastName(getRandomElement(GermanPersonNames.LAST_NAMES));
		newProband.setCitizenship(getRandomBoolean(5) ? "Deutschland" : "österreich");
		Long oldestChildDoBTime = null;
		if (childIds != null && childIds.size() > 0) {
			newProband.setChildIds(childIds);
			Iterator<Long> it = childIds.iterator();
			while (it.hasNext()) {
				Long childId = it.next();
				ProbandOutVO child = probandService.getProband(auth, childId, 1, null, null);
				if (oldestChildDoBTime == null || oldestChildDoBTime > child.getDateOfBirth().getTime()) {
					oldestChildDoBTime = child.getDateOfBirth().getTime();
				}
			}
		}
		Date dOb;
		if (oldestChildDoBTime != null) {
			dOb = new Date();
			dOb.setTime(oldestChildDoBTime);
			dOb = DateCalc.subIntervals(dOb, VariablePeriod.YEAR, null, CommonUtil.safeLongToInt(getRandomLong(15l, 40l)));
		} else {
			dOb = getRandomDateOfBirth();
		}
		newProband.setDateOfBirth(dOb);
		ProbandOutVO out = probandService.addProband(auth, newProband, null, null, null);
		jobOutput.println("proband created: " + out.getName());
		return out;
	}

	private ProbandListEntryTagOutVO createProbandListEntryTag(AuthenticationVO auth, InputFields inputField, TrialOutVO trial, int position, boolean optional, boolean disabled,
			boolean excelValue, boolean excelDate, boolean ecrfValue, boolean stratification, boolean randomize, String title, String comment, String jsVariableName,
			String jsValueExpression,
			String jsOutputExpression)
			throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		ProbandListEntryTagInVO newProbandListEntryTag = new ProbandListEntryTagInVO();
		newProbandListEntryTag.setOptional(optional);
		newProbandListEntryTag.setDisabled(disabled);
		newProbandListEntryTag.setExcelValue(excelValue);
		newProbandListEntryTag.setEcrfValue(ecrfValue);
		newProbandListEntryTag.setStratification(stratification);
		newProbandListEntryTag.setRandomize(randomize);
		newProbandListEntryTag.setExcelDate(excelDate);
		newProbandListEntryTag.setFieldId(getInputField(auth, inputField).getId());
		newProbandListEntryTag.setTrialId(trial.getId());
		newProbandListEntryTag.setPosition(new Long(position));
		newProbandListEntryTag.setComment(comment);
		newProbandListEntryTag.setTitle(title);
		newProbandListEntryTag.setJsVariableName(jsVariableName);
		newProbandListEntryTag.setJsValueExpression(jsValueExpression);
		newProbandListEntryTag.setJsOutputExpression(jsOutputExpression);
		ProbandListEntryTagOutVO out = trialService.addProbandListEntryTag(auth, newProbandListEntryTag);
		jobOutput.println("proband list entry tag created: " + out.getUniqueName());
		return out;
	}

	public void createProbands(int probandCountPerDepartment) throws Exception {
		int grandChildrenCount = (int) (0.5 * probandCountPerDepartment);
		int childrenCount = (int) (0.5 * (probandCountPerDepartment - grandChildrenCount));
		Collection<String> titles = toolsService.completeTitle(null, null, -1);
		ArrayList<ProbandCategory> categories = new ArrayList<ProbandCategory>(probandCategoryDao.search(new Search(new SearchParameter[] {
				new SearchParameter("locked", false, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("signup", false, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("person", true, SearchParameter.EQUAL_COMPARATOR) })));
		for (int departmentNum = 0; departmentNum < departmentCount; departmentNum++) {
			ArrayList<Long> grandChildrentIds = new ArrayList<Long>();
			ArrayList<Long> childrentIds = new ArrayList<Long>();
			for (int i = 0; i < grandChildrenCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				ProbandOutVO proband = createProband(auth, departmentNum, null, null, categories, titles);
				grandChildrentIds.add(proband.getId());
				createFiles(auth, FileModule.PROBAND_DOCUMENT, proband.getId(), FILE_COUNT_PER_PROBAND);
			}
			HashSet<Long> childWoMaleParentIds = new HashSet<Long>(grandChildrentIds);
			HashSet<Long> childWoFemaleParentIds = new HashSet<Long>(grandChildrentIds);
			for (int i = 0; i < childrenCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				boolean isMale = getRandomBoolean(50);
				ArrayList<Long> childIds;
				if (isMale) {
					childIds = getUniqueRandomElements(new ArrayList<Long>(childWoMaleParentIds), random.nextInt(5));
					childWoMaleParentIds.removeAll(childIds);
				} else {
					childIds = getUniqueRandomElements(new ArrayList<Long>(childWoFemaleParentIds), random.nextInt(5));
					childWoFemaleParentIds.removeAll(childIds);
				}
				ProbandOutVO proband = createProband(auth, departmentNum, isMale ? Sex.MALE : Sex.FEMALE, childIds, categories, titles);
				childrentIds.add(proband.getId());
				createFiles(auth, FileModule.PROBAND_DOCUMENT, proband.getId(), FILE_COUNT_PER_PROBAND);
			}
			childWoMaleParentIds = new HashSet<Long>(childrentIds);
			childWoFemaleParentIds = new HashSet<Long>(childrentIds);
			for (int i = 0; i < probandCountPerDepartment - grandChildrenCount - childrenCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				boolean isMale = getRandomBoolean(50);
				ArrayList<Long> childIds;
				if (isMale) {
					childIds = getUniqueRandomElements(new ArrayList<Long>(childWoMaleParentIds), random.nextInt(5));
					childWoMaleParentIds.removeAll(childIds);
				} else {
					childIds = getUniqueRandomElements(new ArrayList<Long>(childWoFemaleParentIds), random.nextInt(5));
					childWoFemaleParentIds.removeAll(childIds);
				}
				ProbandOutVO proband = createProband(auth, departmentNum, isMale ? Sex.MALE : Sex.FEMALE, childIds, categories, titles);
				createFiles(auth, FileModule.PROBAND_DOCUMENT, proband.getId(), FILE_COUNT_PER_PROBAND);
			}
		}
	}

	private InputFieldOutVO createSelectManyField(AuthenticationVO auth, String name, String category, String title, String comment,
			boolean vertical, TreeMap<InputFieldValues, Boolean> selectionSetValues,
			Integer minSelections, Integer maxSelections, String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(vertical ? InputFieldType.SELECT_MANY_V : InputFieldType.SELECT_MANY_H);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinSelections(minSelections);
		newInputField.setMaxSelections(maxSelections);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("select many input field created: " + inputField.getName());
		addSelectionSetValues(auth, inputField, selectionSetValues);
		return inputField;
	}

	private InputFieldOutVO createSelectOneDropdownField(AuthenticationVO auth, String name, String category, String title, String comment,
			TreeMap<InputFieldValues, Boolean> selectionSetValues)
			throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.SELECT_ONE_DROPDOWN);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("select one dropdown input field created: " + inputField.getName());
		addSelectionSetValues(auth, inputField, selectionSetValues);
		return inputField;
	}

	private InputFieldOutVO createSelectOneRadioField(AuthenticationVO auth, String name, String category, String title, String comment, boolean vertical,
			TreeMap<InputFieldValues, Boolean> selectionSetValues)
			throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(vertical ? InputFieldType.SELECT_ONE_RADIO_V : InputFieldType.SELECT_ONE_RADIO_H);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("select one radio input field created: " + inputField.getName());
		addSelectionSetValues(auth, inputField, selectionSetValues);
		return inputField;
	}

	private InputFieldOutVO createSingleLineTextField(AuthenticationVO auth, String name, String category, String title, String comment, String textPreset, String regExp,
			String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.SINGLE_LINE_TEXT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setRegExp(regExp);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTextPreset(textPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("single line text input field created: " + out.getName());
		return out;
	}

	private InputFieldOutVO createSketchField(AuthenticationVO auth, String name, String category, String title, String comment, String resourceFileName,
			TreeMap<InputFieldValues, Stroke> inkRegions,
			Integer minSelections, Integer maxSelections, String validationErrorMessage) throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		ClassPathResource resource = new ClassPathResource("/" + resourceFileName);
		byte[] data = CommonUtil.inputStreamToByteArray(resource.getInputStream());
		newInputField.setFieldType(InputFieldType.SKETCH);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinSelections(minSelections);
		newInputField.setMaxSelections(maxSelections);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setDatas(data);
		newInputField.setMimeType(ExecUtil.getMimeType(data, resource.getFilename()));
		newInputField.setFileName(resource.getFilename());
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("select many input field created: " + inputField.getName());
		addInkRegions(auth, inputField, inkRegions);
		return inputField;
	}

	public void createStaff(int staffCountPerDepartment) throws Exception {
		Collection<String> titles = toolsService.completeTitle(null, null, -1);
		int personCount = (int) (0.6 * staffCountPerDepartment);
		int personRootCount = (int) (0.1 * personCount);
		int externalCount = (int) (0.05 * personCount);
		int organisationCount = staffCountPerDepartment - personCount;
		int organisationRootCount = (int) (0.1 * organisationCount);
		for (int departmentNum = 0; departmentNum < departmentCount; departmentNum++) {
			for (int i = 0; i < externalCount; i++) {
				createStaffPerson(getRandomAuth(departmentNum), departmentNum, false, null, titles).getId();
			}
			ArrayList<Long> createdIds = new ArrayList<Long>();
			for (int i = 0; i < personRootCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				StaffOutVO staff = createStaffPerson(auth, departmentNum, true, null, titles);
				createdIds.add(staff.getId());
				createFiles(auth, FileModule.STAFF_DOCUMENT, staff.getId(), FILE_COUNT_PER_STAFF);
			}
			for (int i = 0; i < (personCount - externalCount - personRootCount); i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				StaffOutVO staff = createStaffPerson(auth, departmentNum, true, getRandomElement(createdIds), titles);
				createdIds.add(staff.getId());
				createFiles(auth, FileModule.STAFF_DOCUMENT, staff.getId(), FILE_COUNT_PER_STAFF);
			}
			createdIds.clear();
			for (int i = 0; i < organisationRootCount; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				StaffOutVO organisation = createStaffOrganisation(auth, i, departmentNum, null);
				createdIds.add(organisation.getId());
				createFiles(auth, FileModule.STAFF_DOCUMENT, organisation.getId(), FILE_COUNT_PER_ORGANISATION);
			}
			for (int i = 0; i < (organisationCount - organisationRootCount); i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				StaffOutVO organisation = createStaffOrganisation(auth, organisationRootCount + i, departmentNum, getRandomElement(createdIds));
				createdIds.add(organisation.getId());
				createFiles(auth, FileModule.STAFF_DOCUMENT, organisation.getId(), FILE_COUNT_PER_ORGANISATION);
			}
		}
	}

	private StaffOutVO createStaffOrganisation(AuthenticationVO auth, int organisationNum, int departmentNum, Long parentId) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		StaffInVO newStaff = new StaffInVO();
		newStaff.setPerson(false);
		newStaff.setAllocatable(false);
		newStaff.setMaxOverlappingShifts(0l);
		newStaff.setParentId(parentId);
		newStaff.setDepartmentId(getDepartmentId(departmentNum));
		newStaff.setCategoryId(getRandomElement(selectionSetService.getStaffCategories(auth, false, true, null)).getId());
		newStaff.setOrganisationName("organisation_" + (departmentNum + 1) + "_" + (organisationNum + 1));
		StaffOutVO staff = staffService.addStaff(auth, newStaff, null, null, null);
		jobOutput.println("organisation created: " + staff.getName());
		assignUser(staff);
		return staff;
	}

	private StaffOutVO createStaffPerson(AuthenticationVO auth, int departmentNum, Boolean employee, Long parentId, Collection<String> titles) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		StaffInVO newStaff = new StaffInVO();
		newStaff.setPerson(true);
		newStaff.setEmployee(employee == null ? getRandomBoolean(80) : employee);
		newStaff.setAllocatable(newStaff.getEmployee() && getRandomBoolean(50));
		if (newStaff.getAllocatable()) {
			newStaff.setMaxOverlappingShifts(1l);
		} else {
			newStaff.setMaxOverlappingShifts(0l);
		}
		newStaff.setParentId(parentId);
		newStaff.setDepartmentId(getDepartmentId(departmentNum));
		newStaff.setCategoryId(getRandomElement(selectionSetService.getStaffCategories(auth, true, false, null)).getId());
		if (getRandomBoolean(25)) {
			newStaff.setPrefixedTitle1(getRandomElement(titles));
			if (getRandomBoolean(20)) {
				newStaff.setPrefixedTitle2(getRandomElement(titles));
			}
		}
		if (getRandomBoolean(50)) {
			newStaff.setGender(Sex.MALE);
			newStaff.setFirstName(getRandomElement(GermanPersonNames.MALE_FIRST_NAMES));
		} else {
			newStaff.setGender(Sex.FEMALE);
			newStaff.setFirstName(getRandomElement(GermanPersonNames.FEMALE_FIRST_NAMES));
		}
		newStaff.setLastName(getRandomElement(GermanPersonNames.LAST_NAMES));
		newStaff.setCitizenship(getRandomBoolean(5) ? "Deutschland" : "österreich");
		newStaff.setDateOfBirth(getRandomDateOfBirth());
		StaffOutVO staff = staffService.addStaff(auth, newStaff, null, null, null);
		jobOutput.println("person created: " + staff.getName());
		assignUser(staff);
		return staff;
	}

	private InputFieldOutVO createTimeField(AuthenticationVO auth, String name, String category, String title, String comment, Date timePreset, Date minTime, Date maxTime,
			String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.TIME);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinTime(minTime);
		newInputField.setMaxTime(maxTime);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTimePreset(timePreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("time input field created: " + out.getName());
		return out;
	}

	private TimelineEventOutVO createTimelineEvent(AuthenticationVO auth, Long trialId, String title, String typeNameL10nKey, Date start, Date stop) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		TimelineEventInVO newTimelineEvent = new TimelineEventInVO();
		TimelineEventType eventType = typeNameL10nKey != null ? timelineEventTypeDao.searchUniqueNameL10nKey(typeNameL10nKey) : getRandomElement(timelineEventTypeDao.loadAll());
		newTimelineEvent.setTrialId(trialId);
		newTimelineEvent.setTypeId(eventType.getId());
		newTimelineEvent.setImportance(getRandomElement(EventImportance.values()));
		newTimelineEvent.setNotify(eventType.isNotifyPreset());
		newTimelineEvent.setReminderPeriod(VariablePeriod.EXPLICIT);
		newTimelineEvent.setReminderPeriodDays(getRandomElement(new Long[] { 7L, 14L, 21L }));
		newTimelineEvent.setShow(eventType.isShowPreset());
		newTimelineEvent.setStart(start);
		newTimelineEvent.setStop(stop);
		newTimelineEvent.setParentId(null);
		newTimelineEvent.setTitle(title == null ? eventType.getNameL10nKey() : title);
		newTimelineEvent.setDescription("details eines weiteren " + eventType.getNameL10nKey() + " ereignisses");
		TimelineEventOutVO out = trialService.addTimelineEvent(auth, newTimelineEvent, null, null, null);
		jobOutput.println("timeline event created: " + out.getTitle());
		return out;
	}

	private InputFieldOutVO createTimestampField(AuthenticationVO auth, String name, String category, String title, String comment, Date timestampPreset, Date minTimestamp,
			Date maxTimestamp, boolean isUserTimeZone, String validationErrorMessage) throws Exception {
		auth = (auth == null ? getRandomAuth() : auth);
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.TIMESTAMP);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinTimestamp(minTimestamp);
		newInputField.setMaxTimestamp(maxTimestamp);
		newInputField.setUserTimeZone(isUserTimeZone);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTimestampPreset(timestampPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		jobOutput.println("timestamp input field created: " + out.getName());
		return out;
	}

	private TrialOutVO createTrial(AuthenticationVO auth, int departmentNum, int trialNum, int visitCount, int probandGroupCount, int avgGroupSize, SearchCriteria criteria)
			throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		TrialInVO newTrial = new TrialInVO();
		newTrial.setStatusId(getRandomElement(selectionSetService.getInitialTrialStatusTypes(auth)).getId());
		newTrial.setDepartmentId(getDepartmentId(departmentNum));
		newTrial.setName("TEST TRIAL " + (departmentNum + 1) + "-" + (trialNum + 1));
		newTrial.setTitle(newTrial.getName());
		newTrial.setDescription("");
		newTrial.setSignupProbandList(false);
		newTrial.setSignupInquiries(false);
		newTrial.setSignupRandomize(false);
		newTrial.setSignupDescription("");
		newTrial.setExclusiveProbands(false);
		newTrial.setProbandAliasFormat("");
		newTrial.setDutySelfAllocationLocked(false);
		newTrial.setTypeId(getRandomElement(selectionSetService.getTrialTypes(auth, null)).getId());
		newTrial.setSponsoringId(getRandomElement(selectionSetService.getSponsoringTypes(auth, null)).getId());
		newTrial.setSurveyStatusId(getRandomElement(selectionSetService.getSurveyStatusTypes(auth, null)).getId());
		TrialOutVO trial = trialService.addTrial(auth, newTrial, null);
		jobOutput.println("trial created: " + trial.getName());
		ArrayList<Staff> departmentStaff = new ArrayList<Staff>(staffDao.search(new Search(new SearchParameter[] {
				new SearchParameter("department.id", trial.getDepartment().getId(), SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("person", true, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("employee", true, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("allocatable", true, SearchParameter.EQUAL_COMPARATOR) })));
		Collection<TeamMemberRole> roles = teamMemberRoleDao.loadAll();
		ArrayList<TeamMemberOutVO> teamMembers = new ArrayList<TeamMemberOutVO>();
		Iterator<Staff> teamMemberStaffIt = getUniqueRandomElements(departmentStaff, 3 + random.nextInt(8)).iterator();
		while (teamMemberStaffIt.hasNext()) {
			Staff teamMemberStaff = teamMemberStaffIt.next();
			TeamMemberInVO newTeamMember = new TeamMemberInVO();
			newTeamMember.setStaffId(teamMemberStaff.getId());
			newTeamMember.setTrialId(trial.getId());
			newTeamMember.setAccess(true);
			newTeamMember.setNotifyTimelineEvent(true);
			newTeamMember.setNotifyEcrfValidatedStatus(true);
			newTeamMember.setNotifyEcrfReviewStatus(true);
			newTeamMember.setNotifyEcrfVerifiedStatus(true);
			newTeamMember.setNotifyEcrfFieldStatus(true);
			newTeamMember.setNotifyOther(true);
			boolean created = false;
			TeamMemberRole role;
			while (!created) {
				try {
					role = getRandomElement(roles);
					newTeamMember.setRoleId(role.getId());
					TeamMemberOutVO out = trialService.addTeamMember(auth, newTeamMember);
					jobOutput.println("team member created: " + out.getStaff().getName() + " (" + out.getRole().getName() + ")");
					teamMembers.add(out);
					created = true;
				} catch (ServiceException e) {
					jobOutput.println(e.getMessage());
				}
			}
		}
		VisitInVO newVisit = new VisitInVO();
		newVisit.setTitle("Screeningvisite");
		newVisit.setToken("SV");
		newVisit.setReimbursement(0.0f);
		newVisit.setTypeId(visitTypeDao.searchUniqueNameL10nKey("screening").getId());
		newVisit.setTrialId(trial.getId());
		VisitOutVO screeningVisit = trialService.addVisit(auth, newVisit);
		jobOutput.println("visit created: " + screeningVisit.getTitle());
		ArrayList<VisitOutVO> visits = new ArrayList<VisitOutVO>();
		for (int i = 0; i < visitCount; i++) {
			newVisit = new VisitInVO();
			newVisit.setTitle("Visite " + (i + 1));
			newVisit.setToken("V" + (i + 1));
			newVisit.setReimbursement(0.0f);
			newVisit.setTypeId(visitTypeDao.searchUniqueNameL10nKey("inpatient").getId());
			newVisit.setTrialId(trial.getId());
			VisitOutVO visit = trialService.addVisit(auth, newVisit);
			jobOutput.println("visit created: " + visit.getTitle());
			visits.add(visit);
		}
		newVisit = new VisitInVO();
		newVisit.setTitle("Abschlussvisite");
		newVisit.setToken("AV");
		newVisit.setReimbursement(0.0f);
		newVisit.setTypeId(visitTypeDao.searchUniqueNameL10nKey("final").getId());
		newVisit.setTrialId(trial.getId());
		VisitOutVO finalVisit = trialService.addVisit(auth, newVisit);
		jobOutput.println("visit created: " + finalVisit.getTitle());
		visits.add(finalVisit);
		ProbandGroupInVO newProbandGroup = new ProbandGroupInVO();
		newProbandGroup.setTitle("Screeninggruppe");
		newProbandGroup.setToken("SG");
		newProbandGroup.setTrialId(trial.getId());
		ProbandGroupOutVO screeningGroup = trialService.addProbandGroup(auth, newProbandGroup);
		jobOutput.println("proband group created: " + screeningGroup.getTitle());
		ArrayList<ProbandGroupOutVO> probandGroups = new ArrayList<ProbandGroupOutVO>();
		for (int i = 0; i < probandGroupCount; i++) {
			newProbandGroup = new ProbandGroupInVO();
			newProbandGroup.setTitle("Gruppe " + (i + 1));
			newProbandGroup.setToken("G" + (i + 1));
			newProbandGroup.setTrialId(trial.getId());
			ProbandGroupOutVO probandGroup = trialService.addProbandGroup(auth, newProbandGroup);
			jobOutput.println("proband group created: " + probandGroup.getTitle());
			probandGroups.add(probandGroup);
		}
		Date screeningDate = getRandomScreeningDate();
		Date visitDate = new Date();
		visitDate.setTime(screeningDate.getTime());
		HashMap<Long, ArrayList<VisitScheduleItemOutVO>> visitScheduleItemPerGroupMap = new HashMap<Long, ArrayList<VisitScheduleItemOutVO>>();
		long screeningDays = getRandomLong(2l, 14l);
		VisitScheduleItemInVO newVisitScheduleItem = new VisitScheduleItemInVO();
		newVisitScheduleItem.setVisitId(screeningVisit.getId());
		newVisitScheduleItem.setGroupId(screeningGroup.getId());
		newVisitScheduleItem.setToken("D1");
		newVisitScheduleItem.setTrialId(trial.getId());
		newVisitScheduleItem.setNotify(false);
		Date[] startStop = getFromTo(visitDate, 9, 0, 16, 0);
		newVisitScheduleItem.setStart(startStop[0]);
		newVisitScheduleItem.setStop(startStop[1]);
		VisitScheduleItemOutVO visitScheduleItem = trialService.addVisitScheduleItem(auth, newVisitScheduleItem);
		ArrayList<VisitScheduleItemOutVO> groupVisitScheduleItems = new ArrayList<VisitScheduleItemOutVO>((int) screeningDays);
		groupVisitScheduleItems.add(visitScheduleItem);
		jobOutput.println("visit schedule item created: " + visitScheduleItem.getName());
		createDuty(auth, departmentStaff, trial, startStop[0], startStop[1], "Dienst für Screening Tag 1");
		for (int i = 2; i <= screeningDays; i++) {
			newVisitScheduleItem = new VisitScheduleItemInVO();
			newVisitScheduleItem.setVisitId(screeningVisit.getId());
			newVisitScheduleItem.setGroupId(screeningGroup.getId());
			newVisitScheduleItem.setToken("D" + i);
			newVisitScheduleItem.setTrialId(trial.getId());
			newVisitScheduleItem.setNotify(false);
			startStop = getFromTo(DateCalc.addInterval(visitDate, VariablePeriod.EXPLICIT, i - 1l), 9, 0, 16, 0);
			newVisitScheduleItem.setStart(startStop[0]);
			newVisitScheduleItem.setStop(startStop[1]);
			visitScheduleItem = trialService.addVisitScheduleItem(auth, newVisitScheduleItem);
			groupVisitScheduleItems.add(visitScheduleItem);
			visitScheduleItemPerGroupMap.put(screeningGroup.getId(), groupVisitScheduleItems);
			jobOutput.println("visit schedule item created: " + visitScheduleItem.getName());
			createDuty(auth, departmentStaff, trial, startStop[0], startStop[1], "Dienst für Screening Tag " + i);
		}
		visitDate = DateCalc.addInterval(visitDate, VariablePeriod.EXPLICIT, getRandomElement(new Long[] { 2l, 3l, 7l }) + screeningDays - 1l);
		Iterator<VisitOutVO> visitIt = visits.iterator();
		while (visitIt.hasNext()) {
			VisitOutVO visit = visitIt.next();
			Iterator<ProbandGroupOutVO> probandGroupIt = probandGroups.iterator();
			int groupCount = 0;
			while (probandGroupIt.hasNext()) {
				ProbandGroupOutVO probandGroup = probandGroupIt.next();
				newVisitScheduleItem = new VisitScheduleItemInVO();
				newVisitScheduleItem.setVisitId(visit.getId());
				newVisitScheduleItem.setGroupId(probandGroup.getId());
				newVisitScheduleItem.setToken(null);
				newVisitScheduleItem.setTrialId(trial.getId());
				newVisitScheduleItem.setNotify(false);
				startStop = getFromTo(visitDate, 9, 0, 16, 0);
				newVisitScheduleItem.setStart(startStop[0]);
				newVisitScheduleItem.setStop(startStop[1]);
				if (!visitScheduleItemPerGroupMap.containsKey(probandGroup.getId())) {
					groupVisitScheduleItems = new ArrayList<VisitScheduleItemOutVO>(visits.size());
					visitScheduleItemPerGroupMap.put(probandGroup.getId(), groupVisitScheduleItems);
				} else {
					groupVisitScheduleItems = visitScheduleItemPerGroupMap.get(probandGroup.getId());
				}
				visitScheduleItem = trialService.addVisitScheduleItem(auth, newVisitScheduleItem);
				groupVisitScheduleItems.add(visitScheduleItem);
				jobOutput.println("visit schedule item created: " + visitScheduleItem.getName());
				if (groupCount % 2 == 1) {
					createDuty(auth, departmentStaff, trial, startStop[0], startStop[1], null);
					visitDate = DateCalc.addInterval(visitDate, VariablePeriod.EXPLICIT, 1L);
				}
				groupCount++;
			}
			visitDate = DateCalc.addInterval(visitDate, VariablePeriod.EXPLICIT, getRandomElement(new Long[] { 1l, 2l, 3l, 7L, 14L, 21L }));
		}
		Date finalVisitDate = new Date();
		finalVisitDate.setTime(visitDate.getTime());
		createTimelineEvent(auth, trial.getId(), "Screening", "deadline", screeningDate, null);
		createTimelineEvent(auth, trial.getId(), "Abschlussvisite", "deadline", finalVisitDate, null);
		createTimelineEvent(auth, trial.getId(), "Studienvisiten", "phase", screeningDate, finalVisitDate);
		Date screeningDatePlanned = getRandomDateAround(screeningDate, 5, 5);
		createTimelineEvent(auth, trial.getId(), "Screening geplant", "phase", screeningDatePlanned,
				DateCalc.addInterval(screeningDatePlanned, VariablePeriod.EXPLICIT, (long) DateCalc.dateDeltaDays(screeningDate, finalVisitDate)));
		int screeningDelay = random.nextInt(15);
		Date recruitmentStop = DateCalc.subInterval(screeningDate, VariablePeriod.EXPLICIT, (long) screeningDelay);
		VariablePeriod recruitmentDuration = getRandomElement(new VariablePeriod[] { VariablePeriod.EXPLICIT, VariablePeriod.MONTH, VariablePeriod.TWO_MONTHS,
				VariablePeriod.THREE_MONTHS });
		Date recruitmentStart = DateCalc.subInterval(recruitmentStop, recruitmentDuration, (recruitmentDuration == VariablePeriod.EXPLICIT ? getRandomElement(new Long[] { 21L,
				42L, 70L }) : null));
		createTimelineEvent(auth, trial.getId(), "Rekrutierung", "phase", recruitmentStart, recruitmentStop);
		Date trialStart = DateCalc.subInterval(recruitmentStart, VariablePeriod.EXPLICIT, getRandomElement(new Long[] { 14L, 21L, 28L, 42L }));
		Date trialEnd = getRandomDate(DateCalc.addInterval(finalVisitDate, VariablePeriod.EXPLICIT, 14L), DateCalc.addInterval(finalVisitDate, VariablePeriod.EXPLICIT, 60L));
		createTimelineEvent(auth, trial.getId(), "Studie", "trial", trialStart, trialEnd);
		for (int i = 0; i < 3 + random.nextInt(8); i++) {
			createTimelineEvent(auth, trial.getId(), "Einreichfrist " + (i + 1), "deadline", getRandomDate(trialStart, screeningDate), null);
		}
		for (int i = 0; i < 1 + random.nextInt(3); i++) {
			createTimelineEvent(auth, trial.getId(), "Audit " + (i + 1), "deadline", getRandomDate(screeningDate, trialEnd), null);
		}
		for (int i = 0; i < 1 + random.nextInt(3); i++) {
			createTimelineEvent(auth, trial.getId(), "Abgabefrist " + (i + 1), "deadline", getRandomDate(finalVisitDate, trialEnd), null);
		}
		ArrayList<InquiryOutVO> inquiries = new ArrayList<InquiryOutVO>();
		ArrayList<ProbandListEntryTagOutVO> probandListEntryTags = new ArrayList<ProbandListEntryTagOutVO>();
		if (criteria != null) {
			inquiries.add(createInquiry(auth, InputFields.HEIGHT, trial, "01 - Allgemeine information", 1, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.WEIGHT, trial, "01 - Allgemeine information", 2, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.BMI, trial, "01 - Allgemeine information", 3, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.CLINICAL_TRIAL_EXPERIENCE_YN, trial, "01 - Allgemeine information", 4, true, true, false, false, true, true, null, null, null,
							null,
							null));
			inquiries.add(createInquiry(auth, InputFields.SMOKER_YN, trial, "01 - Allgemeine information", 5, true, true, false, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.CIGARETTES_PER_DAY, trial, "01 - Allgemeine information", 6, true, true, true, false, true, true, null, null, null, null,
							null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_YN, trial, "02 - Diabetes", 1, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_TYPE, trial, "02 - Diabetes", 2, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_SINCE, trial, "02 - Diabetes", 3, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_HBA1C_MMOLPERMOL, trial, "02 - Diabetes", 4, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_HBA1C_DATE, trial, "02 - Diabetes", 5, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.DIABETES_ATTENDING_PHYSICIAN, trial, "02 - Diabetes", 6, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.DIABETES_METHOD_OF_TREATMENT, trial, "02 - Diabetes", 7, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.DIABETES_MEDICATION, trial, "02 - Diabetes", 8, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.CHRONIC_DISEASE_YN, trial, "03 - Krankheitsgeschichte", 1, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.CHRONIC_DISEASE, trial, "03 - Krankheitsgeschichte", 2, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.EPILEPSY_YN, trial, "03 - Krankheitsgeschichte", 3, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.EPILEPSY, trial, "03 - Krankheitsgeschichte", 4, true, true, true, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.CARDIAC_PROBLEMS_YN, trial, "03 - Krankheitsgeschichte", 5, true, true, false, false, true, true, null, null, null, null,
							null));
			inquiries.add(
					createInquiry(auth, InputFields.CARDIAC_PROBLEMS, trial, "03 - Krankheitsgeschichte", 6, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.HYPERTENSION_YN, trial, "03 - Krankheitsgeschichte", 7, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.HYPERTENSION, trial, "03 - Krankheitsgeschichte", 8, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.RENAL_INSUFFICIENCY_YN, trial, "03 - Krankheitsgeschichte", 9, true, true, false, false, true, true, null, null, null, null,
							null));
			inquiries
					.add(createInquiry(auth, InputFields.RENAL_INSUFFICIENCY, trial, "03 - Krankheitsgeschichte", 10, true, true, true, false, true, true, null, null, null, null,
							null));
			inquiries.add(
					createInquiry(auth, InputFields.LIVER_DISEASE_YN, trial, "03 - Krankheitsgeschichte", 11, true, true, false, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.LIVER_DISEASE, trial, "03 - Krankheitsgeschichte", 12, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.ANEMIA_YN, trial, "03 - Krankheitsgeschichte", 13, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.ANEMIA, trial, "03 - Krankheitsgeschichte", 14, true, true, true, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.IMMUNE_MEDAITED_DISEASE_YN, trial, "03 - Krankheitsgeschichte", 15, true, true, false, false, true, true, null, null, null,
							null,
							null));
			inquiries.add(
					createInquiry(auth, InputFields.IMMUNE_MEDAITED_DISEASE, trial, "03 - Krankheitsgeschichte", 16, true, true, true, false, true, true, null, null, null, null,
							null));
			inquiries
					.add(createInquiry(auth, InputFields.GESTATION_YN, trial, "03 - Krankheitsgeschichte", 17, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.GESTATION_TYPE, trial, "03 - Krankheitsgeschichte", 18, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.CONTRACEPTION_YN, trial, "03 - Krankheitsgeschichte", 19, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.CONTRACEPTION_TYPE, trial, "03 - Krankheitsgeschichte", 20, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.ALCOHOL_DRUG_ABUSE_YN, trial, "03 - Krankheitsgeschichte", 21, true, true, false, false, true, true, null, null, null, null,
							null));
			inquiries.add(
					createInquiry(auth, InputFields.ALCOHOL_DRUG_ABUSE, trial, "03 - Krankheitsgeschichte", 22, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.ALLERGY_YN, trial, "03 - Krankheitsgeschichte", 23, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.ALLERGY, trial, "03 - Krankheitsgeschichte", 24, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.MEDICATION_YN, trial, "03 - Krankheitsgeschichte", 25, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.MEDICATION, trial, "03 - Krankheitsgeschichte", 26, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.EYE_PROBLEMS_YN, trial, "03 - Krankheitsgeschichte", 27, true, true, false, false, true, true, null, null, null, null, null));
			inquiries.add(createInquiry(auth, InputFields.EYE_PROBLEMS, trial, "03 - Krankheitsgeschichte", 28, true, true, true, false, true, true, null, null, null, null, null));
			inquiries.add(
					createInquiry(auth, InputFields.FEET_PROBLEMS_YN, trial, "03 - Krankheitsgeschichte", 29, true, true, false, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.FEET_PROBLEMS, trial, "03 - Krankheitsgeschichte", 30, true, true, true, false, true, true, null, null, null, null, null));
			inquiries
					.add(createInquiry(auth, InputFields.DIAGNOSTIC_FINDINGS_AVAILABLE_YN, trial, "03 - Krankheitsgeschichte", 31, true, true, false, false, true, true, null, null,
							null,
							null, null));
			inquiries.add(
					createInquiry(auth, InputFields.DIAGNOSTIC_FINDINGS_AVAILABLE, trial, "03 - Krankheitsgeschichte", 32, true, true, true, false, true, true, null, null, null,
							null,
							null));
			inquiries.add(
					createInquiry(auth, InputFields.GENERAL_STATE_OF_HEALTH, trial, "03 - Krankheitsgeschichte", 33, true, true, true, false, true, true, null, null, null, null,
							null));
			inquiries.add(createInquiry(auth, InputFields.NOTE, trial, "03 - Krankheitsgeschichte", 34, true, true, true, false, true, true, null, null, null, null, null));
		}
		probandListEntryTags.add(createProbandListEntryTag(auth, InputFields.SUBJECT_NUMBER, trial, 1, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags.add(createProbandListEntryTag(auth, InputFields.IC_DATE, trial, 2, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags.add(createProbandListEntryTag(auth, InputFields.SCREENING_DATE, trial, 3, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags.add(createProbandListEntryTag(auth, InputFields.LAB_NUMBER, trial, 4, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags.add(createProbandListEntryTag(auth, InputFields.RANDOM_NUMBER, trial, 5, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags
				.add(createProbandListEntryTag(auth, InputFields.LETTER_TO_PHYSICIAN_SENT, trial, 6, false, false, true, true, true, false, false, null, null, null, null, null));
		probandListEntryTags
				.add(createProbandListEntryTag(auth, InputFields.PARTICIPATION_LETTER_IN_MEDOCS, trial, 7, false, false, true, true, true, false, false, null, null, null, null,
						null));
		probandListEntryTags
				.add(createProbandListEntryTag(auth, InputFields.COMPLETION_LETTER_IN_MEDOCS, trial, 8, false, false, true, true, true, false, false, null, null, null, null,
						null));
		if (!"migration_started".equals(trial.getStatus().getNameL10nKey())) {
			newTrial.setId(trial.getId());
			newTrial.setVersion(trial.getVersion());
			newTrial.setStatusId(trialStatusTypeDao.searchUniqueNameL10nKey("started").getId());
			trial = trialService.updateTrial(auth, newTrial, null, false);
			jobOutput.println("trial " + trial.getName() + " updated: " + trial.getStatus().getName());
		}
		Collection allProbands = probandDao.search(new Search(new SearchParameter[] {
				new SearchParameter("department.id", getDepartmentId(departmentNum), SearchParameter.EQUAL_COMPARATOR) }));
		ArrayList<ProbandOutVO> probands;
		if (criteria != null) {
			Iterator<Proband> probandsIt = allProbands.iterator();
			while (probandsIt.hasNext()) {
				Proband proband = probandsIt.next();
				ProbandOutVO probandVO = probandService.getProband(auth, proband.getId(), null, null, null);
				HashSet<InquiryValueInVO> inquiryValuesIns = new HashSet<InquiryValueInVO>();
				Iterator<InquiryOutVO> inquiriesIt = inquiries.iterator();
				while (inquiriesIt.hasNext()) {
					InquiryOutVO inquiry = inquiriesIt.next();
					if (inquiry.isActive()) {
						InquiryValueInVO newInquiryValue = new InquiryValueInVO();
						newInquiryValue.setProbandId(proband.getId());
						newInquiryValue.setInquiryId(inquiry.getId());
						setRandomInquiryValue(inquiry, newInquiryValue);
						inquiryValuesIns.add(newInquiryValue);
					}
				}
				jobOutput.println(probandVO.getName() + ": " + probandService.setInquiryValues(getRandomAuth(departmentNum), inquiryValuesIns, true).getPageValues().size()
						+ " inquiry values set/created");
			}
			probands = new ArrayList<ProbandOutVO>(performSearch(auth, criteria, null, null));
		} else {
			probands = new ArrayList<ProbandOutVO>(allProbands.size());
			Iterator<Proband> probandsIt = allProbands.iterator();
			while (probandsIt.hasNext()) {
				probands.add(probandService.getProband(auth, probandsIt.next().getId(), null, null, null));
			}
		}
		Collections.shuffle(probands, random);
		ArrayList<ProbandListEntryOutVO> probandListEntries = new ArrayList<ProbandListEntryOutVO>();
		for (int i = 0; i < probands.size() && i < (probandGroupCount * avgGroupSize); i++) {
			ProbandListEntryInVO newProbandListEntry = new ProbandListEntryInVO();
			newProbandListEntry.setGroupId(getRandomElement(probandGroups).getId());
			newProbandListEntry.setPosition(i + 1l);
			newProbandListEntry.setTrialId(trial.getId());
			newProbandListEntry.setProbandId(probands.get(i).getId());
			ProbandListEntryOutVO probandListEntry = trialService.addProbandListEntry(auth, false, false, false, newProbandListEntry);
			jobOutput.println("proband list entry created - trial: " + probandListEntry.getTrial().getName() + " position: " + probandListEntry.getPosition() + " proband: "
					+ probandListEntry.getProband().getName());
			updateProbandListStatusEntryRealTimestamp(probandListEntry.getLastStatus(), screeningGroup, visitScheduleItemPerGroupMap, 0);
			probandListEntry = trialService.getProbandListEntry(auth, probandListEntry.getId()); // valid laststatus
			probandListEntries.add(probandListEntry);
		}
		HashMap<String, ProbandListStatusTypeVO> probandListStatusTypeMap = new HashMap<String, ProbandListStatusTypeVO>();
		Iterator<ProbandListStatusTypeVO> probandListStatusTypeIt = selectionSetService.getAllProbandListStatusTypes(auth, true).iterator();
		while (probandListStatusTypeIt.hasNext()) {
			ProbandListStatusTypeVO probandListStatusType = probandListStatusTypeIt.next();
			probandListStatusTypeMap.put(probandListStatusType.getNameL10nKey(), probandListStatusType);
		}
		Iterator<ProbandListEntryOutVO> probandListEntryIt = probandListEntries.iterator();
		while (probandListEntryIt.hasNext()) {
			ProbandListEntryOutVO probandListEntry = probandListEntryIt.next();
			HashSet<ProbandListEntryTagValueInVO> probandListEntryTagValuesIns = new HashSet<ProbandListEntryTagValueInVO>();
			Iterator<ProbandListEntryTagOutVO> probandListEntryTagsIt = probandListEntryTags.iterator();
			while (probandListEntryTagsIt.hasNext()) {
				ProbandListEntryTagOutVO probandListEntryTag = probandListEntryTagsIt.next();
				ProbandListEntryTagValueInVO newProbandListEntryTagValue = new ProbandListEntryTagValueInVO();
				newProbandListEntryTagValue.setListEntryId(probandListEntry.getId());
				newProbandListEntryTagValue.setTagId(probandListEntryTag.getId());
				setRandomProbandListEntryTagValue(probandListEntryTag, newProbandListEntryTagValue);
				probandListEntryTagValuesIns.add(newProbandListEntryTagValue);
			}
			jobOutput.println(probandListEntry.getPosition() + ". " + probandListEntry.getProband().getName() + ": "
					+ trialService.setProbandListEntryTagValues(getRandomAuth(departmentNum), probandListEntryTagValuesIns, true).getPageValues().size()
					+ " proband list entry tag values set/created");
			HashSet<ProbandListStatusTypeVO> passedProbandListStatus = new HashSet<ProbandListStatusTypeVO>();
			passedProbandListStatus.add(probandListEntry.getLastStatus().getStatus());
			boolean allowPassed = false;
			int statusHistoryLength = 0;
			ProbandListStatusTypeVO newStatus;
			while ((newStatus = getNextProbandListStatusType(auth, probandListEntry, allowPassed, passedProbandListStatus, probandListStatusTypeMap)) != null) {
				passedProbandListStatus.add(newStatus);
				ProbandListStatusEntryInVO newProbandListStatusEntry = new ProbandListStatusEntryInVO();
				newProbandListStatusEntry.setListEntryId(probandListEntry.getId());
				newProbandListStatusEntry.setStatusId(newStatus.getId());
				newProbandListStatusEntry.setReason(newStatus.isReasonRequired() ? "eine erforderliche Begründung" : null);
				newProbandListStatusEntry.setRealTimestamp(new Date(probandListEntry.getLastStatus().getRealTimestamp().getTime() + 60000l));
				if (!updateProbandListStatusEntryRealTimestamp(trialService.addProbandListStatusEntry(auth, false, newProbandListStatusEntry), screeningGroup,
						visitScheduleItemPerGroupMap, statusHistoryLength)) {
					break;
				}
				probandListEntry = trialService.getProbandListEntry(auth, probandListEntry.getId()); // valid laststatus
				statusHistoryLength++;
			}
			jobOutput.println(probandListEntry.getPosition() + ". " + probandListEntry.getProband().getName() + ": " + statusHistoryLength
					+ " enrollment status entries added - final state: " + probandListEntry.getLastStatus().getStatus().getName());
		}
		return trial;
	}

	public void createTrials(int trialCountPerDepartment, SearchCriteria[] eligibilityCriterias, Integer[] visitCounts, Integer[] probandGroupCounts,
			Integer[] avgProbandGroupSizes)
			throws Throwable {
		for (int departmentNum = 0; departmentNum < departmentCount; departmentNum++) {
			for (int i = 0; i < trialCountPerDepartment; i++) {
				AuthenticationVO auth = getRandomAuth(departmentNum);
				SearchCriteria eligibilityCriteria = null;
				if (eligibilityCriterias != null && i < eligibilityCriterias.length) {
					eligibilityCriteria = eligibilityCriterias[i];
				}
				TrialOutVO trial = createTrial(auth, departmentNum, i, getRandomElement(visitCounts), getRandomElement(probandGroupCounts),
						getRandomElement(avgProbandGroupSizes), eligibilityCriteria);
				createFiles(auth, FileModule.TRIAL_DOCUMENT, trial.getId(), FILE_COUNT_PER_TRIAL);
			}
		}
	}

	private UserOutVO createUser(String name, String password, long departmentId, String departmentPassword) throws Exception {
		UserInVO newUser = new UserInVO();
		newUser.setLocale(CommonUtil.localeToString(Locale.getDefault()));
		newUser.setTimeZone(CommonUtil.timeZoneToString(TimeZone.getDefault()));
		newUser.setDateFormat(null);
		newUser.setDecimalSeparator(null);
		newUser.setLocked(false);
		newUser.setShowTooltips(false);
		newUser.setDecrypt(true);
		newUser.setDecryptUntrusted(false);
		newUser.setEnableInventoryModule(true);
		newUser.setEnableStaffModule(true);
		newUser.setEnableCourseModule(true);
		newUser.setEnableTrialModule(true);
		newUser.setEnableInputFieldModule(true);
		newUser.setEnableProbandModule(true);
		newUser.setEnableMassMailModule(true);
		newUser.setEnableUserModule(true);
		newUser.setAuthMethod(AuthenticationType.LOCAL);
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword);
		newUser.setDepartmentId(departmentId);
		newUser.setName(name);
		newPassword.setPassword(password);
		UserOutVO user = toolsService.addUser(newUser, newPassword, departmentPassword);
		jobOutput.println("user created: " + user.getName());
		addUserPermissionProfiles(user, new ArrayList<PermissionProfile>() {

			{
				add(PermissionProfile.INVENTORY_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.STAFF_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.COURSE_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.TRIAL_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.PROBAND_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.USER_ALL_DEPARTMENTS);
				add(PermissionProfile.INPUT_FIELD_MASTER);
				add(PermissionProfile.MASS_MAIL_DETAIL_ALL_DEPARTMENTS);
				add(PermissionProfile.INVENTORY_MASTER_SEARCH);
				add(PermissionProfile.STAFF_MASTER_SEARCH);
				add(PermissionProfile.COURSE_MASTER_SEARCH);
				add(PermissionProfile.TRIAL_MASTER_SEARCH);
				add(PermissionProfile.PROBAND_MASTER_SEARCH);
				add(PermissionProfile.USER_MASTER_SEARCH);
				add(PermissionProfile.INPUT_FIELD_MASTER_SEARCH);
				add(PermissionProfile.MASS_MAIL_MASTER_SEARCH);
			}
		});
		return user;
	}

	private AuthenticationVO getAuth(int departmentNum, int userNum) {
		return new AuthenticationVO(getUsername(departmentNum, userNum), getUserPassword(departmentNum, userNum), null, "localhost");
	}

	private CriteriaOutVO getCriteria(AuthenticationVO auth, SearchCriteria criteria) throws Throwable {
		final AuthenticationVO a = (auth == null ? getRandomAuth() : auth);
		String name = criteria.toString();
		Iterator<Criteria> it = (new ArrayList<Criteria>(criteriaDao.search(new Search(new SearchParameter[] {
				new SearchParameter("category", "test_" + prefix, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("label", name, SearchParameter.EQUAL_COMPARATOR) })))).iterator();
		if (it.hasNext()) {
			return searchService.getCriteria(a, it.next().getId());
		} else {
			switch (criteria) {
				case ALL_INVENTORY:
					return createCriteria(a, DBModule.INVENTORY_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all inventory items.", true);
				case ALL_STAFF:
					return createCriteria(a, DBModule.STAFF_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all person and organisation records.", true);
				case ALL_COURSES:
					return createCriteria(a, DBModule.COURSE_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all courses.", true);
				case ALL_TRIALS:
					return createCriteria(a, DBModule.TRIAL_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all trials.", true);
				case ALL_PROBANDS:
					return createCriteria(a, DBModule.PROBAND_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all probands.", true);
				case ALL_INPUTFIELDS:
					return createCriteria(a, DBModule.INPUT_FIELD_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all input fields.", true);
				case ALL_USERS:
					return createCriteria(a, DBModule.USER_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all users.", true);
				case ALL_MASSMAILS:
					return createCriteria(a, DBModule.MASS_MAIL_DB,
							new ArrayList<SearchCriterion>(), name, "This query lists all mass mails.", true);
				case SUBJECTS_1:
					return createCriteria(a, DBModule.PROBAND_DB,
							new ArrayList<SearchCriterion>() {

								{
									InputFieldOutVO field1 = getInputField(a, InputFields.DIABETES_TYPE);
									InputFieldSelectionSetValue value1 = getInputFieldValue(field1.getId(), InputFieldValues.TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION);
									add(new SearchCriterion(null, "proband.inquiryValues.inquiry.field.id", CriterionRestriction.EQ, field1.getId()));
									add(new SearchCriterion(CriterionTie.AND, "proband.inquiryValues.value.selectionValues.id", CriterionRestriction.EQ, value1.getId()));
								}
							}, name, "", true);
				default:
					return null;
			}
		}
	}

	private Long getDepartmentId(int departmentNum) throws Exception {
		return ExecUtil.departmentL10nKeyToId(getDepartmentName(departmentNum), departmentDao, jobOutput);
	}

	private Object[] getDepartmentIds() {
		ArrayList<Long> result = new ArrayList<Long>();
		Pattern departmentNameRegExp = Pattern.compile("^department " + Pattern.quote(prefix) + " [0-9]+$");
		Iterator<Department> departmentIt = departmentDao.loadAll().iterator();
		while (departmentIt.hasNext()) {
			Department department = departmentIt.next();
			if (departmentNameRegExp.matcher(department.getNameL10nKey()).find()) {
				result.add(department.getId());
			}
		}
		return result.toArray();
	}

	private String getDepartmentName(int num) {
		return String.format("department %s %d", prefix, num + 1);
	}

	private String getDepartmentPassword(int num) {
		return getDepartmentName(num);
	}

	private Date[] getFromTo(Date date, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Date[] result = new Date[2];
		result[0] = (new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hourStart, minuteStart, 0))
				.getTime();
		result[1] = (new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hourEnd, minuteEnd, 0))
				.getTime();
		return result;
	}

	private InputFieldOutVO getInputField(AuthenticationVO auth, InputFields inputField) throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		String name = inputField.toString();
		Iterator<InputField> it = inputFieldDao.findByNameL10nKeyLocalized(name, false).iterator();
		if (it.hasNext()) {
			return inputFieldService.getInputField(auth, it.next().getId());
		} else {
			String inquiryQuestionsCategory = "Erhebungsfragen";
			String probandListEntryTagCategory = "Probandenlistenspalten";
			String ecrfFieldCategory = "eCRF Felder";
			switch (inputField) {
				case HEIGHT:
					return createIntegerField(auth, name, inquiryQuestionsCategory, "Körpergröße (cm):", "", null, 50l, 300l,
							"Die Körpergröße erfordert einen Ganzzahlwert zwischen 50 und 300 cm.");
				case WEIGHT:
					return createIntegerField(auth, name, inquiryQuestionsCategory, "Körpergewicht (kg):", "", null, 3l, 500l,
							"Das Körpergewicht erfordert einen Ganzzahlwert zwischen 3 und 500 kg.");
				case BMI:
					return createFloatField(auth, name, inquiryQuestionsCategory, "BMI (kg/m²):", "", null, 5f, 60f,
							"Der BMI erfordert eine Fließkommazahl zwischen 5.0 und 200.0 kg/m².");
				case DIABETES_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Diabetes:", null, false);
				case DIABETES_TYPE:
					return createSelectOneRadioField(auth, name, inquiryQuestionsCategory, "Diabetes Typ:", null, false, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.TYP_1_DIABETES, false);
							put(InputFieldValues.TYP_2_DIABETES_MIT_INSULINEIGENPRODUKTION, false);
							put(InputFieldValues.TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION, false);
						}
					});
				case DIABETES_SINCE:
					return createIntegerField(auth, name, inquiryQuestionsCategory, "Diabetes seit (Jahr):", null, null, 1930l, null,
							"Diabetes seit erfordert eine Jahreszahl größer gleich 1930.");
				case DIABETES_HBA1C_MMOLPERMOL:
					return createFloatField(auth, name, inquiryQuestionsCategory, "HbA1C-Wert (mmol/mol):", null, null, 20f, 130f,
							"Der HbA1C-Wert erfordert eine Fließkommazahl zwischen 20.0 und 130.0 mmol/mol");
				case DIABETES_HBA1C_PERCENT:
					return createFloatField(auth, name, inquiryQuestionsCategory, "HbA1C-Wert (Prozent):", null, null, 2.5f, 15f,
							"Der HbA1C-Wert erfordert eine Fließkommazahl zwischen 2.5 und 15.0 %");
				case DIABETES_HBA1C_DATE:
					return createDateField(auth, name, inquiryQuestionsCategory, "HbA1C zuletzt bestimmt:", null, null, (new GregorianCalendar(1980, 0, 1)).getTime(), null,
							"HbA1C zuletzt bestimmt muss nach 1980-01-01 liegen");
				case DIABETES_C_PEPTIDE:
					return createFloatField(auth, name, inquiryQuestionsCategory, "C-Peptid (µg/l):", null, null, 0.5f, 7.0f,
							"Die C-Peptid Konzentration erfordert eine Fließkommazahl zwischen 0.5 und 7.0 µg/l");
				case DIABETES_ATTENDING_PHYSICIAN:
					return createSingleLineTextField(auth, name, inquiryQuestionsCategory, "Arzt in Behandlung:", "Name/Anschrift des Arztes", null, null, null);
				case DIABETES_METHOD_OF_TREATMENT:
					return createSelectManyField(auth, name, inquiryQuestionsCategory, "Behandlungsmethode:", null, true, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.DIAET, false);
							put(InputFieldValues.SPORTLICHE_BETAETIGUNG, false);
							put(InputFieldValues.ORALE_ANTIDIABETIKA, false);
							put(InputFieldValues.INSULINTHERAPIE, false);
						}
					}, null, null, null);
				case DIABETES_MEDICATION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Medikamente:", null, null, null, null);
				case CLINICAL_TRIAL_EXPERIENCE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Haben Sie Erfahrung mit klinischen Studien?", null, false);
				case SMOKER_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Raucher:", null, false);
				case CIGARETTES_PER_DAY:
					return createSelectOneDropdownField(auth, name, inquiryQuestionsCategory, "Zigaretten/Tag:", null, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.CIGARETTES_UNTER_5, false);
							put(InputFieldValues.CIGARETTES_5_20, false);
							put(InputFieldValues.CIGARETTES_20_40, false);
							put(InputFieldValues.CIGARETTES_UEBER_40, false);
						}
					});
				case CHRONIC_DISEASE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Chronische Erkrankung:", null, false);
				case CHRONIC_DISEASE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Chronische Erkrankung:", null, null, null, null);
				case EPILEPSY_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Epilepsie:", null, false);
				case EPILEPSY:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Epilepsie:", null, null, null, null);
				case CARDIAC_PROBLEMS_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Herzprobleme:", null, false);
				case CARDIAC_PROBLEMS:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "herzprobleme:", null, null, null, null);
				case HYPERTENSION_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Bluthochdruck:", null, false);
				case HYPERTENSION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Bluthochdruck:", null, null, null, null);
				case RENAL_INSUFFICIENCY_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Niereninsuffizienz/-erkrankung:", null, false);
				case RENAL_INSUFFICIENCY:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Niereninsuffizienz/-erkrankung:", null, null, null, null);
				case LIVER_DISEASE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Lebererkrankung:", null, false);
				case LIVER_DISEASE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Lebererkrankung:", null, null, null, null);
				case ANEMIA_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Anämie (Blutarmut):", null, false);
				case ANEMIA:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Anämie (Blutarmut):", null, null, null, null);
				case IMMUNE_MEDAITED_DISEASE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Autoimmunerkrankung:", null, false);
				case IMMUNE_MEDAITED_DISEASE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Autoimmunerkrankung:", null, null, null, null);
				case GESTATION_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "schwanger, stillen etc.", null, false);
				case GESTATION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "schwanger, stillen etc.", null, null, null, null);
				case GESTATION_TYPE:
					return createSelectManyField(auth, name, inquiryQuestionsCategory, "schwanger, stillen etc.", null, false, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.SCHWANGER, false);
							put(InputFieldValues.STILLEN, false);
						}
					}, null, null, null);
				case CONTRACEPTION_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Empfängnisverhütung:", null, false);
				case CONTRACEPTION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Empfängnisverhütung:", null, null, null, null);
				case CONTRACEPTION_TYPE:
					return createSelectManyField(auth, name, inquiryQuestionsCategory, "Empfängnisverhütung:", null, true, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.HORMONELL, false);
							put(InputFieldValues.MECHANISCH, false);
							put(InputFieldValues.INTRAUTERINPESSARE, false);
							put(InputFieldValues.CHEMISCH, false);
							put(InputFieldValues.OPERATIV, false);
						}
					}, null, null, null);
				case ALCOHOL_DRUG_ABUSE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Missbrauch von Alkohol/Drogen:", null, false);
				case ALCOHOL_DRUG_ABUSE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Missbrauch von Alkohol/Drogen:", null, null, null, null);
				case PSYCHIATRIC_CONDITION_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Psychiatrische Erkrankung:", null, false);
				case PSYCHIATRIC_CONDITION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Psychiatrische Erkrankung:", null, null, null, null);
				case ALLERGY_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Allergien:", null, false);
				case ALLERGY:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Allergien:", null, null, null, null);
				case MEDICATION_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Medikamente:", null, false);
				case MEDICATION:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Medikamente:", null, null, null, null);
				case EYE_PROBLEMS_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Probleme mit den Augen:", null, false);
				case EYE_PROBLEMS:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Probleme mit den Augen:", null, null, null, null);
				case FEET_PROBLEMS_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Probleme mit den Füßen:", null, false);
				case FEET_PROBLEMS:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Probleme mit den Füßen:", null, null, null, null);
				case DIAGNOSTIC_FINDINGS_AVAILABLE_YN:
					return createCheckBoxField(auth, name, inquiryQuestionsCategory, "Haben Sie eventuell Befunde zu Hause?", null, false);
				case DIAGNOSTIC_FINDINGS_AVAILABLE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Haben Sie eventuell Befunde zu Hause?", null, null, null, null);
				case GENERAL_STATE_OF_HEALTH:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Allgemeiner Gesundheitszustand:", null, null, null, null);
				case NOTE:
					return createMultiLineTextField(auth, name, inquiryQuestionsCategory, "Anmerkungen:", null, null, null, null);
				case SUBJECT_NUMBER:
					// [0-9]{4}: omit ^ and $ here for xeger
					return createSingleLineTextField(auth, name, probandListEntryTagCategory, "Subject Number:", null, null, "[0-9]{4}",
							"Vierstellige Zahl (mit führenden Nullen) erforderlich.");
				case IC_DATE:
					return createDateField(auth, name, probandListEntryTagCategory, "Informed Consent Date:", null, null, null, null, null);
				case SCREENING_DATE:
					return createDateField(auth, name, probandListEntryTagCategory, "Screening Date:", null, null, null, null, null);
				case LAB_NUMBER:
					return createSingleLineTextField(auth, name, probandListEntryTagCategory, "Lab Nummer:", null, null, "[0-9]{4}",
							"Vierstellige Zahl (mit führenden Nullen) erforderlich.");
				case RANDOM_NUMBER:
					return createSingleLineTextField(auth, name, probandListEntryTagCategory, "Random Number:", null, null, "[0-9]{3}",
							"Dreistellige Nummber (mit führenden Nullen) erforderlich.");
				case LETTER_TO_PHYSICIAN_SENT:
					return createCheckBoxField(auth, name, probandListEntryTagCategory, "Letter to physician sent:", null, false);
				case PARTICIPATION_LETTER_IN_MEDOCS:
					return createCheckBoxField(auth, name, probandListEntryTagCategory, "Participation letter in MR/Medocs:", null, false);
				case LETTER_TO_SUBJECT_AT_END_OF_STUDY:
					return createCheckBoxField(auth, name, probandListEntryTagCategory, "Letter to subject at end of study:", null, false);
				case COMPLETION_LETTER_IN_MEDOCS:
					return createCheckBoxField(auth, name, probandListEntryTagCategory, "Completion letter in MR/Medocs:", null, false);
				case BODY_HEIGHT:
					return createIntegerField(auth, name, inquiryQuestionsCategory, "Body Height (cm):", "", null, 50l, 300l,
							"Body height requires an integer value between 50 and 300 cm.");
				case BODY_WEIGHT:
					return createIntegerField(auth, name, inquiryQuestionsCategory, "Body Weight (kg):", "", null, 3l, 500l,
							"Body weight requires an integer value between 3 and 500 kg.");
				case BODY_MASS_INDEX:
					return createFloatField(auth, name, inquiryQuestionsCategory, "BMI (kg/m²):", "", null, 5f, 60f,
							"Body mass index requires a decimal value between 5.0 and 200.0 kg/m².");
				case OBESITY:
					return createSelectOneRadioField(auth, name, inquiryQuestionsCategory, "Obesity:",
							"BMI < 18.5: shortweight\n" +
									"18.5-24.9: normal weight\n" +
									"25-29.9: overweight\n" +
									"30-34.9: adiposity degree I\n" +
									"35-39.9: adiposity degree II\n" +
									">= 40: adiposity degree III (morbid)",
							true, new TreeMap<InputFieldValues, Boolean>() {

								{
									put(InputFieldValues.SHORTWEIGHT, false);
									put(InputFieldValues.NORMAL_WEIGHT, false);
									put(InputFieldValues.OVERWEIGHT, false);
									put(InputFieldValues.ADIPOSITY_DEGREE_I, false);
									put(InputFieldValues.ADIPOSITY_DEGREE_II, false);
									put(InputFieldValues.ADIPOSITY_DEGREE_III, false);
								}
							});
				case EGFR:
					return createFloatField(auth, name, inquiryQuestionsCategory, "Estimated Glomerular Filtration Rate (ml/min):", "", null, 1f, 50f,
							"Estimated glomerular filtration rate requires a decimal value between 1.0 and 50.0 ml/min.");
				case ETHNICITY:
					return createSelectOneDropdownField(auth, name, inquiryQuestionsCategory, "Ethnicity:", null, new TreeMap<InputFieldValues, Boolean>() {

						{
							put(InputFieldValues.AMERICAN_INDIAN_OR_ALASKA_NATIVE, false);
							put(InputFieldValues.ASIAN, false);
							put(InputFieldValues.BLACK, false);
							put(InputFieldValues.NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER, false);
							put(InputFieldValues.WHITE, false);
						}
					});
				case SERUM_CREATININ_CONCENTRATION:
					return createFloatField(auth, name, inquiryQuestionsCategory, "Serum Creatinin Concentration (mg/dl):", "", null, 1f, 50f,
							"Serum creatinin concentration requires a decimal value between 1.0 and 50.0 mg/dl."); // range?
				case HBA1C_PERCENT:
					return createFloatField(auth, name, inquiryQuestionsCategory, "HbA1C (%):", "", null, 2.5f, 15.0f,
							"HbA1C in percent requires a decimal value between 2.5 and 15.0 %.");
				case HBA1C_MMOLPERMOL:
					return createFloatField(auth, name, inquiryQuestionsCategory, "HbA1C (mmol/mol):", "", null, 20.0f, 130.0f,
							"HbA1C in mmol/mol requires a decimal value between 20.0 and 130.0 mmol/mol.");
				case MANNEQUIN:
					return createSketchField(auth, name, inquiryQuestionsCategory, "Joints:", "",
							"mannequin.png", new TreeMap<InputFieldValues, Stroke>() {

								{
									put(InputFieldValues.SHOULDER_RIGHT, new Stroke("M111.91619,64.773336L134.22667,64.773336L134.22667,86.512383L111.91619,86.512383Z"));
									put(InputFieldValues.SHOULDER_LEFT, new Stroke("M196.2019,66.916193L218.51238,66.916193L218.51238,88.65524L196.2019,88.65524Z"));
									put(InputFieldValues.ELLBOW_RIGHT, new Stroke("M114.05904,111.20191L136.36952,111.20191L136.36952,132.94095L114.05904,132.94095Z"));
									put(InputFieldValues.ELLBOW_LEFT, new Stroke("M197.63047,113.34477L219.94095,113.34477L219.94095,135.08381L197.63047,135.08381Z"));
									put(InputFieldValues.WRIST_RIGHT, new Stroke("M94.773327,156.9162L117.08381,156.9162L117.08381,178.65524L94.773327,178.65524Z"));
									put(InputFieldValues.WRIST_LEFT, new Stroke("M215.48761,161.20191L237.7981,161.20191L237.7981,182.94095L215.48761,182.94095Z"));
									put(InputFieldValues.THUMB_BASE_RIGHT, new Stroke("M29.891238,214.89125L49.823043,214.89125L49.823043,254.9659L29.891238,254.9659Z"));
									put(InputFieldValues.THUMB_MIDDLE_RIGHT, new Stroke("M9.0200169,246.16289L29.265697,246.16289L29.265697,265.83713L9.0200169,265.83713Z"));
									put(InputFieldValues.THUMB_BASE_LEFT, new Stroke("M282.0341,217.03411L301.9659,217.03411L301.9659,257.10876L282.0341,257.10876Z"));
									put(InputFieldValues.THUMB_MIDDLE_LEFT, new Stroke("M301.87716,250.4486L322.12284,250.4486L322.12284,270.12284L301.87716,270.12284Z"));
									put(InputFieldValues.INDEX_FINGER_BASE_RIGHT, new Stroke("M28.305731,271.87717L48.551411,271.87717L48.551411,291.55141L28.305731,291.55141Z"));
									put(InputFieldValues.INDEX_FINGER_MIDDLE_RIGHT,
											new Stroke("M22.591445,294.73431L42.837125,294.73431L42.837125,314.40855L22.591445,314.40855Z"));
									put(InputFieldValues.MIDDLE_FINGER_BASE_RIGHT, new Stroke("M48.305731,276.16288L68.551411,276.16288L68.551411,295.83712L48.305731,295.83712Z"));
									put(InputFieldValues.MIDDLE_FINGER_MIDDLE_RIGHT,
											new Stroke("M44.734302,298.30574L64.979982,298.30574L64.979982,317.97998L44.734302,317.97998Z"));
									put(InputFieldValues.RING_FINGER_BASE_RIGHT, new Stroke("M66.162873,279.02003L86.408553,279.02003L86.408553,298.69427L66.162873,298.69427Z"));
									put(InputFieldValues.RING_FINGER_MIDDLE_RIGHT, new Stroke("M65.448587,302.59146L85.694267,302.59146L85.694267,322.2657L65.448587,322.2657Z"));
									put(InputFieldValues.LITTLE_FINGER_BASE_RIGHT, new Stroke("M85.448587,276.16289L105.69427,276.16289L105.69427,295.83713L85.448587,295.83713Z"));
									put(InputFieldValues.LITTLE_FINGER_MIDDLE_RIGHT,
											new Stroke("M87.591444,299.02003L107.83713,299.02003L107.83713,318.69427L87.591444,318.69427Z"));
									put(InputFieldValues.INDEX_FINGER_BASE_LEFT, new Stroke("M281.16287,274.73432L301.40856,274.73432L301.40856,294.40856L281.16287,294.40856Z"));
									put(InputFieldValues.INDEX_FINGER_MIDDLE_LEFT, new Stroke("M286.16287,296.16289L306.40856,296.16289L306.40856,315.83713L286.16287,315.83713Z"));
									put(InputFieldValues.MIDDLE_FINGER_BASE_LEFT, new Stroke("M262.59144,279.73432L282.83713,279.73432L282.83713,299.40856L262.59144,299.40856Z"));
									put(InputFieldValues.MIDDLE_FINGER_MIDDLE_LEFT, new Stroke("M264.02001,301.87718L284.2657,301.87718L284.2657,321.55142L264.02001,321.55142Z"));
									put(InputFieldValues.RING_FINGER_BASE_LEFT, new Stroke("M244.7343,282.59147L264.97999,282.59147L264.97999,302.26571L244.7343,302.26571Z"));
									put(InputFieldValues.RING_FINGER_MIDDLE_LEFT, new Stroke("M244.02001,304.02004L264.2657,304.02004L264.2657,323.69428L244.02001,323.69428Z"));
									put(InputFieldValues.LITTLE_FINGER_BASE_LEFT, new Stroke("M224.7343,279.02004L244.97999,279.02004L244.97999,298.69428L224.7343,298.69428Z"));
									put(InputFieldValues.LITTLE_FINGER_MIDDLE_LEFT, new Stroke("M224.02001,301.1629L244.2657,301.1629L244.2657,320.83714L224.02001,320.83714Z"));
									put(InputFieldValues.KNEE_RIGHT, new Stroke("M133.4355,241.29267L161.27879,241.29267L161.27879,267.13594L133.4355,267.13594Z"));
									put(InputFieldValues.KNEE_LEFT, new Stroke("M166.29264,242.00696L194.13593,242.00696L194.13593,267.85023L166.29264,267.85023Z"));
								}
							},
							0, 28, "Mark up to 28 joints.");
				case VAS:
					return createSketchField(auth, name, inquiryQuestionsCategory, "Visual Analogue Scale:", "",
							"vas.png", new TreeMap<InputFieldValues, Stroke>() {

								{
									final int VAS_STEPS = 10;
									final float VAS_MAX_VALUE = 100.0f;
									final float VAS_X_OFFSET = 10.0f;
									final float VAS_LENGTH = 382.0f;
									final String[] VAS_COLORS = new String[] {
											"#24FF00", "#58FF00", "#8DFF00", "#C2FF00",
											"#F7FF00", "#FFD300", "#FF9E00", "#FF6900", "#FF3400", "#FF0000" };
									for (int i = 0; i < VAS_STEPS; i++) {
										float valueFrom = i * VAS_MAX_VALUE / VAS_STEPS;
										float valueTo = (i + 1) * VAS_MAX_VALUE / VAS_STEPS;
										float value = (valueFrom + valueTo) / 2;
										float x1 = VAS_X_OFFSET + i * VAS_LENGTH / VAS_STEPS;
										float x2 = VAS_X_OFFSET + (i + 1) * VAS_LENGTH / VAS_STEPS;
										int colorIndex = i * VAS_COLORS.length / VAS_STEPS;
										put(InputFieldValues.valueOf(String.format("VAS_%d", i + 1)), new Stroke(VAS_COLORS[colorIndex], "M" + x1 + ",10L" + x2 + ",10L" + x2
												+ ",50L" + x1 + ",50Z", Float.toString(value)));
									}
								}
							},
							1, 1, "Mark exactly one position.");
				case ESR:
					return createFloatField(auth, name, inquiryQuestionsCategory, "Erythrocyte Sedimentation Rate (mm/h):", "", null, 1.0f, 30.0f,
							"Erythrocyte sedimentation rate requires a decimal value between 1.0 and 30.0 mm/h.");
				case DAS28:
					return createFloatField(auth, name, inquiryQuestionsCategory, "Disease Activity Score 28:", "", null, 2.0f, 10.0f,
							"Disease activity score 28 requires a decimal value between 2.0 and 10.0.");
				case DISTANCE:
					return createFloatField(auth, name, inquiryQuestionsCategory, "Distance (km):",
							"Travel distance from trial site to subject home address according to Google Maps.", null, 0.1f, 21000.0f,
							"Distance requires a decimal value between 0.1 and 21000.0 km.");
				case ALPHA_ID:
					return createSingleLineTextField(auth, name, inquiryQuestionsCategory, "Diagnosis Alpha ID Synonym:", null, null, null, null);
				case STRING_SINGLELINE:
					return createSingleLineTextField(auth, name, ecrfFieldCategory, "singleline text:", null, null, null, null);
				case STRING_MULTILINE:
					return createMultiLineTextField(auth, name, ecrfFieldCategory, "multiline text:", null, null, null, null);
				case FLOAT:
					return createFloatField(auth, name, ecrfFieldCategory, "decimal value:",
							"some decimal value", null, null, null, null);
				case INTEGER:
					return createIntegerField(auth, name, ecrfFieldCategory, "integer value:",
							"some integer value", null, null, null, null);
				case DIAGNOSIS_START:
					return createDateField(auth, name, inquiryQuestionsCategory, "Diagnosis Start:", null, null, null, null, null);
				case DIAGNOSIS_END:
					return createDateField(auth, name, inquiryQuestionsCategory, "Diagnosis End:", null, null, null, null, null);
				case DIAGNOSIS_COUNT:
					return createIntegerField(auth, name, ecrfFieldCategory, "Number of diagnoses:", null, null, 1l, null, "at least one diagnosis required");
				default:
					return null;
			}
		}
	}

	private InputFieldSelectionSetValue getInputFieldValue(Long fieldId, InputFieldValues value) {
		return inputFieldSelectionSetValueDao.findByFieldNameL10nKeyLocalized(fieldId, value.toString(), false).iterator().next();
	}

	private ProbandListStatusTypeVO getNextProbandListStatusType(AuthenticationVO auth, ProbandListEntryOutVO probandListEntry, boolean allowPassed,
			HashSet<ProbandListStatusTypeVO> passedProbandListStatus, HashMap<String, ProbandListStatusTypeVO> probandListStatusTypeMap) throws Exception {
		ProbandListStatusTypeVO newState = null;
		Collection<ProbandListStatusTypeVO> newStates = selectionSetService.getProbandListStatusTypeTransitions(auth, probandListEntry.getLastStatus().getStatus().getId());
		if (newStates != null && newStates.size() > 0) {
			HashSet<Long> newStateIds = new HashSet<Long>(newStates.size());
			Iterator<ProbandListStatusTypeVO> it = newStates.iterator();
			while (it.hasNext()) {
				newStateIds.add(it.next().getId());
			}
			if (!allowPassed) {
				while (newState == null) {
					newState = getRandomElement(newStates);
					newState = probandListStatusMarkov(newState, probandListStatusTypeMap);
					if (!newStateIds.contains(newState.getId())) {
						newState = null;
					} else if (passedProbandListStatus.contains(newState)) {
						newState = null;
					}
				}
			} else {
				while (newState == null) {
					newState = getRandomElement(newStates);
					newState = probandListStatusMarkov(newState, probandListStatusTypeMap);
					if (!newStateIds.contains(newState.getId())) {
						newState = null;
					}
				}
			}
		}
		return newState;
	}

	private AuthenticationVO getRandomAuth() {
		return getRandomAuth(random.nextInt(departmentCount));
	}

	private AuthenticationVO getRandomAuth(int departmentNum) {
		int userNum = random.nextInt(usersPerDepartmentCount);
		return new AuthenticationVO(getUsername(departmentNum, userNum), getUserPassword(departmentNum, userNum), null, "localhost");
	}

	private AuthenticationVO getRandomAuth(long departmentId) {
		String departmentL10nKey = departmentDao.load(departmentId).getNameL10nKey();
		int departmentNum = Integer.parseInt(departmentL10nKey.replaceFirst("department " + Pattern.quote(prefix) + " ", "")) - 1;
		return getRandomAuth(departmentNum);
	}

	private Date getRandomAutoDeleteDeadline() {
		return getRandomDate((new GregorianCalendar(year, 0, 1)).getTime(), (new GregorianCalendar(year, 11, 31)).getTime());
	}

	private boolean getRandomBoolean() {
		return random.nextBoolean();
	}

	private boolean getRandomBoolean(int p) {
		if (p <= 0) {
			return false;
		} else if (p >= 100) {
			return true;
		} else {
			return random.nextDouble() < ((p) / 100.0d);
		}
	}

	private Date getRandomCourseStop() {
		return getRandomDate((new GregorianCalendar(year, 0, 1)).getTime(), (new GregorianCalendar(year, 11, 31)).getTime());
	}

	private Date getRandomDate(Date minDate, Date maxDate) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minDate == null ? (new GregorianCalendar(1900, 0, 1)).getTime() : minDate);
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.add(Calendar.DAY_OF_YEAR, random.nextInt(DateCalc.dateDeltaDays(minDate == null ? (new GregorianCalendar(1900, 0, 1)).getTime() : minDate,
				maxDate == null ? (new GregorianCalendar(year, 11, 31)).getTime() : maxDate)));
		return cal.getTime();
	}

	private Date getRandomDateAround(Date date, int maxDaysBefore, int maxDaysAfter) {
		return DateCalc.addInterval(date, VariablePeriod.EXPLICIT, (long) (getRandomBoolean() ? random.nextInt(maxDaysAfter + 1) : (-1 * random.nextInt(maxDaysBefore + 1))));
	}

	private Date getRandomDateOfBirth() {
		return getRandomDate((new GregorianCalendar(year - 90, 0, 1)).getTime(), (new GregorianCalendar(year - 20, 0, 1)).getTime());
	}

	private Long getRandomDepartmentId() throws Exception {
		return getDepartmentId(random.nextInt(departmentCount));
	}

	private <E> E getRandomElement(ArrayList<E> list) {
		if (list != null && list.size() > 0) {
			return list.get(random.nextInt(list.size()));
		}
		return null;
	}

	private <E> E getRandomElement(Collection<E> collection) {
		E result = null;
		if (collection != null && collection.size() > 0) {
			int index = random.nextInt(collection.size());
			Iterator<E> it = collection.iterator();
			for (int i = 0; i <= index; i++) {
				result = it.next();
			}
		}
		return result;
	}

	private <E> E getRandomElement(E[] array) {
		if (array != null && array.length > 0) {
			return array[random.nextInt(array.length)];
		}
		return null;
	}

	private float getRandomFloat(Float lowerLimit, Float upperLimit) {
		float lower = (lowerLimit == null ? 0f : lowerLimit.floatValue());
		return lower + random.nextFloat() * ((upperLimit == null ? Float.MAX_VALUE : upperLimit.longValue()) - lower);
	}

	private long getRandomLong(Long lowerLimit, Long upperLimit) {
		long lower = (lowerLimit == null ? 0l : lowerLimit.longValue());
		return lower + nextLong(random, (upperLimit == null ? Integer.MAX_VALUE : upperLimit.longValue()) - lower);
	}

	private Date getRandomScreeningDate() {
		return getRandomDate((new GregorianCalendar(year, 0, 1)).getTime(), (new GregorianCalendar(year, 11, 31)).getTime());
	}

	private Date getRandomTime(Date minTime, Date maxTime) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minTime == null ? (new GregorianCalendar(1970, 0, 1, 0, 0, 0)).getTime() : minTime);
		cal.setTimeInMillis(cal.getTimeInMillis()
				+ nextLong(random, (maxTime == null ? (new GregorianCalendar(1970, 0, 1, 23, 59, 59)).getTime() : maxTime).getTime() - cal.getTimeInMillis()));
		return cal.getTime();
	}

	private Date getRandomTimestamp(Date minTimestamp, Date maxTimestamp) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(minTimestamp == null ? (new GregorianCalendar(1900, 0, 1, 0, 0, 0)).getTime() : minTimestamp);
		cal.setTimeInMillis(cal.getTimeInMillis()
				+ nextLong(random, (maxTimestamp == null ? (new GregorianCalendar(year, 11, 31)).getTime() : maxTimestamp).getTime() - cal.getTimeInMillis()));
		return cal.getTime();
	}

	private Long getRandomUserId() {
		return getRandomUserId(random.nextInt(departmentCount));
	}

	private Long getRandomUserId(int departmentNum) {
		return getUserId(departmentNum, random.nextInt(usersPerDepartmentCount));
	}

	private <E> ArrayList<E> getUniqueRandomElements(ArrayList<E> list, int n) {
		if (list != null) {
			int listSize = list.size();
			if (listSize > 0 && n > 0) {
				if (listSize <= n) {
					return new ArrayList<E>(list);
				} else {
					HashSet<E> result = new HashSet<E>(n);
					while (result.size() < n && result.size() < listSize) {
						result.add(list.get(random.nextInt(listSize)));
					}
					return new ArrayList<E>(result);
				}
			} else {
				return new ArrayList<E>();
			}
		}
		return null;
	}

	private Long getUserId(int departmentNum, int userNum) {
		return userDao.searchUniqueName(getUsername(departmentNum, userNum)).getId();
	}

	private String getUsername(int departmentNum, int num) {
		return String.format("user_%s_%d_%d", prefix, departmentNum + 1, num + 1);
	}

	private String getUserPassword(int departmentNum, int num) {
		return getUsername(departmentNum, num);
	}

	private long nextLong(Random rng, long n) {
		// error checking and 2^x checking removed for simplicity.
		if (n <= 0) {
			throw new IllegalArgumentException("n must be positive");
		}
		// if ((n & -n) == n) // i.e., n is a power of 2
		// return (int)((n * (long)next(31)) >> 31);
		long bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}

	private Collection performSearch(AuthenticationVO auth, SearchCriteria criteria, PSFVO psf, Integer maxInstances) throws Throwable {
		auth = (auth == null ? getRandomAuth() : auth);
		CriteriaInVO newCriteria = new CriteriaInVO();
		HashSet<CriterionInVO> newCriterions = new HashSet<CriterionInVO>();
		ExecUtil.criteriaOutToIn(newCriteria, newCriterions, getCriteria(auth, criteria));
		Collection result;
		String type;
		switch (newCriteria.getModule()) {
			case INVENTORY_DB:
				result = searchService.searchInventory(auth, newCriteria, newCriterions, maxInstances, psf);
				type = "inventory";
				break;
			case STAFF_DB:
				result = searchService.searchStaff(auth, newCriteria, newCriterions, maxInstances, psf);
				type = "staff";
				break;
			case COURSE_DB:
				result = searchService.searchCourse(auth, newCriteria, newCriterions, maxInstances, psf);
				type = "course";
				break;
			case TRIAL_DB:
				result = searchService.searchTrial(auth, newCriteria, newCriterions, psf);
				type = "trial";
				break;
			case PROBAND_DB:
				result = searchService.searchProband(auth, newCriteria, newCriterions, maxInstances, psf);
				type = "proband";
				break;
			case INPUT_FIELD_DB:
				result = searchService.searchInputField(auth, newCriteria, newCriterions, psf);
				type = "inputfield";
				break;
			case USER_DB:
				result = searchService.searchUser(auth, newCriteria, newCriterions, maxInstances, psf);
				type = "user";
				break;
			case MASS_MAIL_DB:
				result = searchService.searchMassMail(auth, newCriteria, newCriterions, psf);
				type = "massmail";
				break;
			default:
				result = null;
				type = null;
		}
		jobOutput.println("search '" + criteria.toString() + "' returned " + result.size() + (psf != null ? " of " + psf.getRowCount() : "") + " " + type + " items");
		return result;
	}

	private ProbandListStatusTypeVO probandListStatusMarkov(ProbandListStatusTypeVO state, HashMap<String, ProbandListStatusTypeVO> probandListStatusTypeMap) throws Exception {
		boolean negative = getRandomBoolean(10);
		if ("cancelled".equals(state.getNameL10nKey())) {
			return negative ? state : probandListStatusTypeMap.get("acceptance");
		} else if ("screening_failure".equals(state.getNameL10nKey())) {
			return negative ? state : probandListStatusTypeMap.get("screening_ok");
		} else if ("dropped_out".equals(state.getNameL10nKey())) {
			return negative ? state : probandListStatusTypeMap.get("completed");
		}
		return state;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	private void setRandomInquiryValue(InquiryOutVO inquiry, InquiryValueInVO inquiryValue) {
		if (!inquiry.isOptional() || getRandomBoolean()) {
			HashSet<Long> selectionSetValues = new HashSet<Long>();
			switch (inquiry.getField().getFieldType().getType()) {
				case CHECKBOX:
					inquiryValue.setBooleanValue(inquiry.isDisabled() ? inquiry.getField().getBooleanPreset() : getRandomBoolean());
					break;
				case SELECT_ONE_DROPDOWN:
				case SELECT_ONE_RADIO_H:
				case SELECT_ONE_RADIO_V:
					if (inquiry.isDisabled()) {
						Iterator<InputFieldSelectionSetValueOutVO> selectionSetValueIt = inquiry.getField().getSelectionSetValues().iterator();
						while (selectionSetValueIt.hasNext()) {
							InputFieldSelectionSetValueOutVO selectionSetValue = selectionSetValueIt.next();
							if (selectionSetValue.isPreset()) {
								selectionSetValues.add(selectionSetValue.getId());
								break;
							}
						}
					} else {
						selectionSetValues.add(getRandomElement(inquiry.getField().getSelectionSetValues()).getId());
					}
					inquiryValue.setSelectionValueIds(new ArrayList<Long>(selectionSetValues));
					break;
				case SELECT_MANY_H:
				case SELECT_MANY_V:
					if (inquiry.isDisabled()) {
						Iterator<InputFieldSelectionSetValueOutVO> selectionSetValueIt = inquiry.getField().getSelectionSetValues().iterator();
						while (selectionSetValueIt.hasNext()) {
							InputFieldSelectionSetValueOutVO selectionSetValue = selectionSetValueIt.next();
							if (selectionSetValue.isPreset()) {
								selectionSetValues.add(selectionSetValue.getId());
							}
						}
					} else {
						for (int i = 0; i <= random.nextInt(inquiry.getField().getSelectionSetValues().size()); i++) {
							selectionSetValues.add(getRandomElement(inquiry.getField().getSelectionSetValues()).getId());
						}
					}
					inquiryValue.setSelectionValueIds(new ArrayList<Long>(selectionSetValues));
					break;
				case SINGLE_LINE_TEXT:
				case MULTI_LINE_TEXT:
					if (inquiry.isDisabled()) {
						inquiryValue.setTextValue(inquiry.getField().getTextPreset());
					} else {
						String regExp = inquiry.getField().getRegExp();
						if (regExp != null && regExp.length() > 0) {
							Xeger generator = new Xeger(regExp);
							inquiryValue.setTextValue(generator.generate());
						} else {
							inquiryValue.setTextValue("random text");
						}
					}
					break;
				case INTEGER:
					inquiryValue.setLongValue(inquiry.isDisabled() ? inquiry.getField().getLongPreset()
							: getRandomLong(inquiry.getField().getLongLowerLimit(), inquiry.getField()
									.getLongUpperLimit()));
					break;
				case FLOAT:
					inquiryValue.setFloatValue(inquiry.isDisabled() ? inquiry.getField().getFloatPreset()
							: getRandomFloat(inquiry.getField().getFloatLowerLimit(), inquiry
									.getField().getFloatUpperLimit()));
					break;
				case DATE:
					inquiryValue.setDateValue(inquiry.isDisabled() ? inquiry.getField().getDatePreset()
							: getRandomDate(inquiry.getField().getMinDate(), inquiry.getField()
									.getMaxDate()));
					break;
				case TIME:
					inquiryValue.setTimeValue(inquiry.isDisabled() ? inquiry.getField().getTimePreset()
							: getRandomTime(inquiry.getField().getMinTime(), inquiry.getField()
									.getMaxTime()));
					break;
				case TIMESTAMP:
					inquiryValue.setTimestampValue(inquiry.isDisabled() ? inquiry.getField().getTimestampPreset()
							: getRandomTimestamp(inquiry.getField().getMinTimestamp(),
									inquiry.getField().getMaxTimestamp()));
					break;
				default:
			}
		}
	}

	private void setRandomProbandListEntryTagValue(ProbandListEntryTagOutVO probandListEntryTag, ProbandListEntryTagValueInVO probandListEntryTagValue) {
		if (!probandListEntryTag.isOptional() || getRandomBoolean()) {
			HashSet<Long> selectionSetValues = new HashSet<Long>();
			switch (probandListEntryTag.getField().getFieldType().getType()) {
				case CHECKBOX:
					probandListEntryTagValue.setBooleanValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getBooleanPreset() : getRandomBoolean());
					break;
				case SELECT_ONE_DROPDOWN:
				case SELECT_ONE_RADIO_H:
				case SELECT_ONE_RADIO_V:
					if (probandListEntryTag.isDisabled()) {
						Iterator<InputFieldSelectionSetValueOutVO> selectionSetValueIt = probandListEntryTag.getField().getSelectionSetValues().iterator();
						while (selectionSetValueIt.hasNext()) {
							InputFieldSelectionSetValueOutVO selectionSetValue = selectionSetValueIt.next();
							if (selectionSetValue.isPreset()) {
								selectionSetValues.add(selectionSetValue.getId());
								break;
							}
						}
					} else {
						selectionSetValues.add(getRandomElement(probandListEntryTag.getField().getSelectionSetValues()).getId());
					}
					probandListEntryTagValue.setSelectionValueIds(new ArrayList<Long>(selectionSetValues));
					break;
				case SELECT_MANY_H:
				case SELECT_MANY_V:
					if (probandListEntryTag.isDisabled()) {
						Iterator<InputFieldSelectionSetValueOutVO> selectionSetValueIt = probandListEntryTag.getField().getSelectionSetValues().iterator();
						while (selectionSetValueIt.hasNext()) {
							InputFieldSelectionSetValueOutVO selectionSetValue = selectionSetValueIt.next();
							if (selectionSetValue.isPreset()) {
								selectionSetValues.add(selectionSetValue.getId());
							}
						}
					} else {
						for (int i = 0; i <= random.nextInt(probandListEntryTag.getField().getSelectionSetValues().size()); i++) {
							selectionSetValues.add(getRandomElement(probandListEntryTag.getField().getSelectionSetValues()).getId());
						}
					}
					probandListEntryTagValue.setSelectionValueIds(new ArrayList<Long>(selectionSetValues));
					break;
				case SINGLE_LINE_TEXT:
				case MULTI_LINE_TEXT:
					if (probandListEntryTag.isDisabled()) {
						probandListEntryTagValue.setTextValue(probandListEntryTag.getField().getTextPreset());
					} else {
						String regExp = probandListEntryTag.getField().getRegExp();
						if (regExp != null && regExp.length() > 0) {
							Xeger generator = new Xeger(regExp);
							probandListEntryTagValue.setTextValue(generator.generate());
						} else {
							probandListEntryTagValue.setTextValue("random text");
						}
					}
					break;
				case INTEGER:
					probandListEntryTagValue.setLongValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getLongPreset()
							: getRandomLong(probandListEntryTag
									.getField().getLongLowerLimit(), probandListEntryTag.getField().getLongUpperLimit()));
					break;
				case FLOAT:
					probandListEntryTagValue.setFloatValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getFloatPreset()
							: getRandomFloat(probandListEntryTag
									.getField().getFloatLowerLimit(), probandListEntryTag.getField().getFloatUpperLimit()));
					break;
				case DATE:
					probandListEntryTagValue.setDateValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getDatePreset()
							: getRandomDate(probandListEntryTag
									.getField().getMinDate(), probandListEntryTag.getField().getMaxDate()));
					break;
				case TIME:
					probandListEntryTagValue.setTimeValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getTimePreset()
							: getRandomTime(probandListEntryTag
									.getField().getMinTime(), probandListEntryTag.getField().getMaxTime()));
					break;
				case TIMESTAMP:
					probandListEntryTagValue.setTimestampValue(probandListEntryTag.isDisabled() ? probandListEntryTag.getField().getTimestampPreset()
							: getRandomTimestamp(
									probandListEntryTag.getField().getMinTimestamp(), probandListEntryTag.getField().getMaxTimestamp()));
					break;
				default:
			}
		}
	}

	private boolean updateProbandListStatusEntryRealTimestamp(ProbandListStatusEntryOutVO probandListStatusEntryVO, ProbandGroupOutVO screeningGroup,
			HashMap<Long, ArrayList<VisitScheduleItemOutVO>> visitScheduleItemPerGroupMap, int statusHistoryLength) throws Exception {
		boolean result = true;
		VisitScheduleItemOutVO visitScheduleItem;
		if (probandListStatusEntryVO.getStatus().isInitial()) {
			visitScheduleItem = getRandomElement(visitScheduleItemPerGroupMap.get(screeningGroup.getId()));
		} else {
			ArrayList<VisitScheduleItemOutVO> visitScheduleItems = visitScheduleItemPerGroupMap.get(probandListStatusEntryVO.getListEntry().getGroup().getId());
			if (statusHistoryLength >= visitScheduleItems.size()) {
				result = false;
				visitScheduleItem = visitScheduleItems.get(visitScheduleItems.size() - 1);
			} else {
				visitScheduleItem = visitScheduleItems.get(statusHistoryLength);
			}
		}
		ProbandListStatusEntry probandListStatusEntry = probandListStatusEntryDao.load(probandListStatusEntryVO.getId());
		probandListStatusEntry.setRealTimestamp(new Timestamp(visitScheduleItem.getStop().getTime()));
		probandListStatusEntryDao.update(probandListStatusEntry);
		return result;
	}
}
