package org.phoenixctms.ctsms.selenium.proband;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.selenium.SeleniumTestBase;
import org.phoenixctms.ctsms.test.InputFieldValuesEnum;
import org.phoenixctms.ctsms.test.InputFieldsEnum;
import org.phoenixctms.ctsms.test.SearchCriteriaEnum;
import org.phoenixctms.ctsms.test.SearchCriterion;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(CandidateSelectionTest.class)
public class CandidateSelectionTest extends SeleniumTestBase {

	private String departmentPassword;
	private DepartmentVO department;
	private String userName;
	private String userPassword;
	private TrialOutVO trial;
	private String criteriaCategory;
	private final static int PROBAND_COUNT = 10;
	private Set<Long> probandIds = new LinkedHashSet<Long>(PROBAND_COUNT);
	private Set<Long> expectedProbandIds = new LinkedHashSet<Long>(PROBAND_COUNT);
	private Set<Long> actualProbandIds = new LinkedHashSet<Long>(PROBAND_COUNT);

	private enum InputFields implements InputFieldsEnum {

		TEST("xx");
		//		HEIGHT("Körpergröße"),
		//		WEIGHT("Körpergewicht"),
		//		BMI("Body Mass Index"),
		//		DIABETES_YN("Diabetes J/N"),
		//		DIABETES_TYPE("Diabetes Typ"),
		//		DIABETES_SINCE("Diabetes seit"),
		//		DIABETES_HBA1C_MMOLPERMOL("HbA1C in mmol/mol"),
		//		DIABETES_HBA1C_PERCENT("HbA1C in prozent"),
		//		DIABETES_HBA1C_DATE("HbA1C Datum"),
		//		DIABETES_C_PEPTIDE("C-Peptid"), // µg/l
		//		DIABETES_ATTENDING_PHYSICIAN("Arzt in Behandlung"),
		//		DIABETES_METHOD_OF_TREATMENT("Diabetes Behandlungsmethode"), // Diät sport insulintherapie orale Antidiabetika
		//		DIABETES_MEDICATION("Diabetes Medikamente"),
		//		CLINICAL_TRIAL_EXPERIENCE_YN("Erfahrung mit klin. Studien J/N"),
		//		SMOKER_YN("Raucher J/N"),
		//		CIGARETTES_PER_DAY("Zigaretten pro Tag"),
		//		CHRONIC_DISEASE_YN("Chronische Erkrankung J/N"),
		//		CHRONIC_DISEASE("Chronische Erkrankung"),
		//		EPILEPSY_YN("Epilepsie J/N"),
		//		EPILEPSY("Epilepsie"),
		//		CARDIAC_PROBLEMS_YN("Herzprobleme J/N"),
		//		CARDIAC_PROBLEMS("Herzprobleme"),
		//		HYPERTENSION_YN("Bluthochdruck J/N"),
		//		HYPERTENSION("Bluthochdruck"),
		//		RENAL_INSUFFICIENCY_YN("Niereninsuffizienz/-erkrankung J/N"), // renal
		//		RENAL_INSUFFICIENCY("Niereninsuffizienz/-erkrankung"),
		//		LIVER_DISEASE_YN("Lebererkrankung J/N"), // liver diseaseYN
		//		LIVER_DISEASE("Lebererkrankung"),
		//		ANEMIA_YN("Anemie J/N"), // anemiaYN
		//		ANEMIA("Anemie"),
		//		IMMUNE_MEDIATED_DISEASE_YN("Autoimmunerkrankung J/N"), // immune mediated diseaseYN
		//		IMMUNE_MEDIATED_DISEASE("Autoimmunerkrankung"),
		//		GESTATION_YN("schwanger, stillen etc. J/N"), // gestationYN
		//		GESTATION("schwanger, stillen etc."),
		//		GESTATION_TYPE("schwanger, stillen etc. Auswahl"),
		//		CONTRACEPTION_YN("Empfängnisverhütung J/N"), // contraceptionYN
		//		CONTRACEPTION("Empfängnisverhütung"),
		//		CONTRACEPTION_TYPE("Empfängnisverhütung Auswahl"),
		//		ALCOHOL_DRUG_ABUSE_YN("Missbrauch von Alkohol/Drogen J/N"), // alcohol_drug_abuseYN
		//		ALCOHOL_DRUG_ABUSE("Missbrauch von Alkohol/Drogen"),
		//		PSYCHIATRIC_CONDITION_YN("Psychiatrische Erkrankung J/N"), // psychiatric_conditionYN
		//		PSYCHIATRIC_CONDITION("Psychiatrische Erkrankung"),
		//		ALLERGY_YN("Allergien J/N"), // allergyYN
		//		ALLERGY("Allergien"),
		//		MEDICATION_YN("Medikamente J/N"), // medicationYN
		//		MEDICATION("Medikamente"),
		//		EYE_PROBLEMS_YN("Probleme mit den Augen J/N"), // eye_probalemsYN
		//		EYE_PROBLEMS("Probleme mit den Augen"),
		//		FEET_PROBLEMS_YN("Probleme mit den Füßen J/N"), // feet_probalemsYN
		//		FEET_PROBLEMS("Probleme mit den Füßen"),
		//		DIAGNOSTIC_FINDINGS_AVAILABLE_YN("Befunde zuhause J/N"),
		//		DIAGNOSTIC_FINDINGS_AVAILABLE("Befunde zuhause"),
		//		GENERAL_STATE_OF_HEALTH("Allgemeiner Gesundheitszustand"),
		//		NOTE("Anmerkung"),
		//		SUBJECT_NUMBER("Subject Number"),
		//		IC_DATE("Informed Consent Date"),
		//		SCREENING_DATE("Screening Date"),
		//		LAB_NUMBER("Lab Number"),
		//		RANDOM_NUMBER("Random Number"),
		//		LETTER_TO_PHYSICIAN_SENT("Letter to physician sent"),
		//		PARTICIPATION_LETTER_IN_MEDOCS("Participation letter in MR/Medocs"),
		//		LETTER_TO_SUBJECT_AT_END_OF_STUDY("Letter to subject at end of study"),
		//		COMPLETION_LETTER_IN_MEDOCS("Completion letter in MR/Medocs"),
		//		BODY_HEIGHT("Body Height"),
		//		BODY_WEIGHT("Body Weight"),
		//		BODY_MASS_INDEX("BMI"),
		//		OBESITY("Obesity"),
		//		EGFR("eGFR"),
		//		SERUM_CREATININ_CONCENTRATION("Serum Creatinin Concentration"),
		//		ETHNICITY("Ethnicity"),
		//		HBA1C_PERCENT("HbA1C (percent)"),
		//		HBA1C_MMOLPERMOL("HbA1C (mmol/mol)"),
		//		MANNEQUIN("Mannequin"),
		//		ESR("ESR"),
		//		VAS("VAS"),
		//		DAS28("DAS28"),
		//		DISTANCE("Distance"),
		//		ALPHA_ID("Alpha-ID"),
		//		STRING_SINGLELINE("singleline text"),
		//		STRING_MULTILINE("multiline text"),
		//		FLOAT("decimal"),
		//		INTEGER("integer"),
		//		DIAGNOSIS_START("diagnosis from"),
		//		DIAGNOSIS_END("diagnosis to"),
		//		DIAGNOSIS_COUNT("diagnosis count");

		private final String value;

		private InputFields(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private enum InputFieldValues implements InputFieldValuesEnum {

		TEST("xx");
		//		TYP_1_DIABETES("Typ 1 Diabetes"),
		//		TYP_2_DIABETES_MIT_INSULINEIGENPRODUKTION("Typ 2 Diabetes mit Insulineigenproduktion"),
		//		TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION("Typ 2 Diabetes ohne Insulineigenproduktion"),
		//		DIAET("Diät"),
		//		SPORTLICHE_BETAETIGUNG("Sportliche Betätigung"),
		//		ORALE_ANTIDIABETIKA("Orale Antidiabetika"),
		//		INSULINTHERAPIE("Insulintherapie"),
		//		CIGARETTES_UNTER_5("Unter 5"),
		//		CIGARETTES_5_20("5-20"),
		//		CIGARETTES_20_40("20-40"),
		//		CIGARETTES_UEBER_40("über 40"),
		//		SCHWANGER("schwanger"),
		//		STILLEN("stillen"),
		//		HORMONELL("hormonell"),
		//		MECHANISCH("mechanisch"),
		//		INTRAUTERINPESSARE("Intrauterinpessare"),
		//		CHEMISCH("chemisch"),
		//		OPERATIV("operativ"),
		//		SHORTWEIGHT("shortweight"),
		//		NORMAL_WEIGHT("normal weight"),
		//		OVERWEIGHT("overweight"),
		//		ADIPOSITY_DEGREE_I("adiposity degree I"),
		//		ADIPOSITY_DEGREE_II("adiposity degree II"),
		//		ADIPOSITY_DEGREE_III("adiposity degree III"),
		//		AMERICAN_INDIAN_OR_ALASKA_NATIVE("American Indian or Alaska Native"),
		//		ASIAN("Asian"),
		//		BLACK("Black"),
		//		NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER("Native Hawaiian or Other Pacific Islander"),
		//		WHITE("White"),
		//		SHOULDER_RIGHT("shoulder right"),
		//		SHOULDER_LEFT("shoulder left"),
		//		ELLBOW_RIGHT("ellbow right"),
		//		ELLBOW_LEFT("ellbow left"),
		//		WRIST_RIGHT("wrist right"),
		//		WRIST_LEFT("wrist left"),
		//		THUMB_BASE_RIGHT("thumb base right"),
		//		THUMB_MIDDLE_RIGHT("thumb middle right"),
		//		THUMB_BASE_LEFT("thumb base left"),
		//		THUMB_MIDDLE_LEFT("thumb middle left"),
		//		INDEX_FINGER_BASE_RIGHT("index finger base right"),
		//		INDEX_FINGER_MIDDLE_RIGHT("index finger middle right"),
		//		MIDDLE_FINGER_BASE_RIGHT("middle finger base right"),
		//		MIDDLE_FINGER_MIDDLE_RIGHT("middle finger middle right"),
		//		RING_FINGER_BASE_RIGHT("ring finger base right"),
		//		RING_FINGER_MIDDLE_RIGHT("ring finger middle right"),
		//		LITTLE_FINGER_BASE_RIGHT("little finger base right"),
		//		LITTLE_FINGER_MIDDLE_RIGHT("little finger middle right"),
		//		INDEX_FINGER_BASE_LEFT("index finger base left"),
		//		INDEX_FINGER_MIDDLE_LEFT("index finger middle left"),
		//		MIDDLE_FINGER_BASE_LEFT("middle finger base left"),
		//		MIDDLE_FINGER_MIDDLE_LEFT("middle finger middle left"),
		//		RING_FINGER_BASE_LEFT("ring finger base left"),
		//		RING_FINGER_MIDDLE_LEFT("ring finger middle left"),
		//		LITTLE_FINGER_BASE_LEFT("little finger base left"),
		//		LITTLE_FINGER_MIDDLE_LEFT("little finger middle left"),
		//		KNEE_RIGHT("knee right"),
		//		KNEE_LEFT("knee left"),
		//		VAS_1("vas-1"),
		//		VAS_2("vas-2"),
		//		VAS_3("vas-3"),
		//		VAS_4("vas-4"),
		//		VAS_5("vas-5"),
		//		VAS_6("vas-6"),
		//		VAS_7("vas-7"),
		//		VAS_8("vas-8"),
		//		VAS_9("vas-9"),
		//		VAS_10("vas-10");

		private final String value;

		private InputFieldValues(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public enum SearchCriteria implements SearchCriteriaEnum {

		TEST("xxx");
		//		ALL_INVENTORY("all inventory"),
		//		ALL_STAFF("all staff"),
		//		ALL_COURSES("all courses"),
		//		ALL_TRIALS("all trials"),
		//		ALL_PROBANDS("all probands"),
		//		ALL_INPUTFIELDS("all inputfields"),
		//		ALL_MASSMAILS("all massmails"),
		//		ALL_USERS("all users"),
		//		SUBJECTS_1("subjects_1");

		private final String value;

		private SearchCriteria(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	@BeforeClass
	public void init_00_setup_department() throws Exception {
		String departmentName = "dept-" + getTestId();
		departmentPassword = departmentName;
		department = getTestDataProvider().createDepartment(departmentName, true, departmentPassword);
	}

	@BeforeClass
	public void init_01_setup_user() throws Exception {
		userName = "user-" + getTestId();
		userPassword = userName;
		UserOutVO user = createUser(userName, userPassword, department.getId(), departmentPassword);
		setProviderAuth(userName, userPassword);
	}

	@BeforeClass
	public void init_02_setup_trial() throws Exception {
		String trialName = "trial-" + getTestId();
		trial = createTrial(trialName, department.getId());
	}

	@BeforeClass
	public void init_03_setup_inquiry_form() throws Throwable {
		ArrayList<InquiryOutVO> inquiries = createInquiryForm(trial);
	}

	@BeforeClass
	public void init_04_setup_proband_criteria() throws Throwable {
		criteriaCategory = getTestId();
		ArrayList<CriteriaOutVO> criterias = createCriterias(criteriaCategory);
	}

	@Test
	public void test_01_open_proband_page() {
		load(getUrl("/proband/proband.jsf"));
	}

	@Test
	public void test_02_login() {
		login(userName, userPassword);
	}

	@Test
	public void test_03_create_proband() {
		String subjectId = "subject-" + String.format("%03d", probandIds.size() + 1) + "-" + getTestId();
		info("subject id: " + subjectId);
		clickCheckbox("tabView:probandmain_form:blinded");
		sendKeys("tabView:probandmain_form:alias", subjectId);
		clickButton("tabView:probandmain_form:add");
		if (waitForAddOperationSuccessful("tabView:probandmain_form")) {
			Long probandId = getProbandIdFromProbandEntityWindowName(getWindowName());
			testOK("proband ID " + probandId.toString() + " created: " + subjectId);
			probandIds.add(probandId);
			expectedProbandIds.add(probandId);
			return;
		} else {
			testFailed("creating proband failed: " + subjectId);
			return;
		}
	}

	@Test
	public void test_04_load_inquiry_form() {
		clickTab("tabView:inquiryvalues");
		clickSelectOneMenu("tabView:inquiryvalue_form:inquiriestrial", trial.getName());
	}

	@Test
	public void test_05_enter_inquiry_values() {
		//		sendKeys(getFieldIdByLabel("Körpergröße"), "123");
		//		sendKeys(getFieldIdByLabel("Körpergewicht"), "123");
		//		clickSelectOneMenu(getFieldIdByLabel("Venenstatus (Einschätzung des Probanden)"), "mittel");
		//		clickSelectMany(getFieldIdByLabel("Grunderkrankung"), "Lifestyle");
		clickButton("tabView:inquiryvalue_form:update");
		if (waitForUpdateOperationSuccessful("tabView:inquiryvalue_form")) {
			//createScreenshot();
			testOK("inquiry values saved");
			return;
		} else {
			testFailed("saving inquiry values failed");
			return;
		}
		//		sendKeys(getFieldIdByLabel("Körpergröße"), "123");
		//		sendKeys(getFieldIdByLabel("Körpergewicht"), "123");
		//		clickSelectOneMenu(getFieldIdByLabel("Venenstatus (Einschätzung des Probanden)"), "mittel");
		//		clickSelectMany(getFieldIdByLabel("Grunderkrankung"), "Lifestyle");
		//		clickButton("tabView:inquiryvalue_form:update");
		//		if (waitForUpdateOperationSuccessful("tabView:inquiryvalue_form")) {
		//			//createScreenshot();
		//			testOK("inquiry values saved");
		//			return;
		//		} else {
		//			testFailed("saving inquiry values failed");
		//			return;
		//		}
	}

	@Test
	public void test_06_create_probands() {
		for (int i = 2; i <= PROBAND_COUNT; i++) {
			//info("entering proband " + i + "...");
			clickMenuItem("menubar_form:newEntityMenuItem_proband");
			closeWindow(getWindowName());
			switchToWindow(getNewProbandEntityWindowName());
			test_03_create_proband();
			test_04_load_inquiry_form();
			test_05_enter_inquiry_values();
		}
	}

	@Test
	public void test_07_open_proband_search_page() throws Throwable {
		CriteriaOutVO criteria = getCriteria(SearchCriteria.TEST, criteriaCategory);
		load(getUrl("/proband/probandSearch.jsf?criteriaid=" + criteria.getId())); //8368105
	}

	@Test
	public void test_08_search_probands_result_size() throws Throwable {
		clickButton(getButtonIdByLabel("Perform search"));
		Long count = getCountFromDatatableHead("search_form:proband_result_list");
		if (expectedProbandIds.size() == count) {
			testOK("search returns expected number of items");
			return;
		} else {
			testFailed("search returns different number of items");
			return;
		}
	}

	@Test
	public void test_09_search_probands_all_expected_probands() throws Throwable {
		do {
			actualProbandIds.addAll(getDatatableRowIds("search_form:proband_result_list"));
		} while (clickDatatableNextPage("search_form:proband_result_list"));
		LinkedHashSet<Long> diff = new LinkedHashSet<Long>();
		diff.addAll(expectedProbandIds);
		diff.removeAll(actualProbandIds);
		if (diff.size() == 0) {
			testOK("search returned all expected probands");
			return;
		} else {
			testFailed("search did not return expected proband IDs: " + diff.toString());
			return;
		}
	}

	@Test
	public void test_10_search_probands_no_unexpected_probands() throws Throwable {
		LinkedHashSet<Long> diff = new LinkedHashSet<Long>();
		diff.addAll(actualProbandIds);
		diff.removeAll(expectedProbandIds);
		if (diff.size() == 0) {
			testOK("search returned no unexpected probands");
			return;
		} else {
			testFailed("search returned unexpected proband IDs: " + diff.toString());
			return;
		}
	}

	private UserOutVO createUser(String name, String password, long departmentId, String departmentPassword) throws Exception {
		UserInVO newUser = new UserInVO();
		newUser.setLocale(CommonUtil.localeToString(Locale.getDefault()));
		newUser.setTimeZone(CommonUtil.timeZoneToString(TimeZone.getDefault()));
		newUser.setDateFormat(null);
		newUser.setDecimalSeparator(null);
		newUser.setLocked(false);
		newUser.setLockedUntrusted(false);
		newUser.setShowTooltips(false);
		newUser.setDecrypt(true);
		newUser.setDecryptUntrusted(false);
		newUser.setEnableInventoryModule(true);
		newUser.setVisibleInventoryTabList(null);
		newUser.setEnableStaffModule(true);
		newUser.setVisibleStaffTabList(null);
		newUser.setEnableCourseModule(true);
		newUser.setVisibleCourseTabList(null);
		newUser.setEnableTrialModule(true);
		newUser.setVisibleTrialTabList(null);
		newUser.setEnableInputFieldModule(true);
		newUser.setVisibleInputFieldTabList(null);
		newUser.setEnableProbandModule(true);
		newUser.setVisibleProbandTabList("inquiryvalues");
		newUser.setEnableMassMailModule(true);
		newUser.setVisibleMassMailTabList(null);
		newUser.setEnableUserModule(true);
		newUser.setVisibleUserTabList(null);
		newUser.setAuthMethod(AuthenticationType.LOCAL);
		newUser.setParentId(null);
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword);
		newUser.setDepartmentId(departmentId);
		newUser.setName(name);
		newPassword.setPassword(password);
		return getTestDataProvider().createUser(newUser, newPassword, departmentPassword, new ArrayList<PermissionProfile>() {

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
	}

	private InputFieldOutVO getInputField(InputFields inputField) throws Throwable {
		InputFieldOutVO field = getTestDataProvider().getInputField(inputField);
		if (field != null) {
			return field;
		} else {
			String name = inputField.toString();
			switch (inputField) {
				//case HEIGHT:
				case TEST:
					return getTestDataProvider().createIntegerField(name);
				//				case WEIGHT:
				//					return getTestDataProvider().createIntegerField(name);
				//				case BMI:
				//					return getTestDataProvider().createFloatField(name);
				//				case DIABETES_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case DIABETES_TYPE:
				//					return getTestDataProvider().createSelectOneRadioField(name,
				//							new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//								{
				//									put(InputFieldValues.TYP_1_DIABETES, false);
				//									put(InputFieldValues.TYP_2_DIABETES_MIT_INSULINEIGENPRODUKTION, false);
				//									put(InputFieldValues.TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION, false);
				//								}
				//							});
				//				case DIABETES_SINCE:
				//					return getTestDataProvider().createIntegerField(name);
				//				case DIABETES_HBA1C_MMOLPERMOL:
				//					return getTestDataProvider().createFloatField(name);
				//				case DIABETES_HBA1C_PERCENT:
				//					return getTestDataProvider().createFloatField(name);
				//				case DIABETES_HBA1C_DATE:
				//					return getTestDataProvider().createDateField(name);
				//				case DIABETES_C_PEPTIDE:
				//					return getTestDataProvider().createFloatField(name);
				//				case DIABETES_ATTENDING_PHYSICIAN:
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case DIABETES_METHOD_OF_TREATMENT:
				//					return getTestDataProvider().createSelectManyField(name,
				//							new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//								{
				//									put(InputFieldValues.DIAET, false);
				//									put(InputFieldValues.SPORTLICHE_BETAETIGUNG, false);
				//									put(InputFieldValues.ORALE_ANTIDIABETIKA, false);
				//									put(InputFieldValues.INSULINTHERAPIE, false);
				//								}
				//							});
				//				case DIABETES_MEDICATION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case CLINICAL_TRIAL_EXPERIENCE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case SMOKER_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case CIGARETTES_PER_DAY:
				//					return getTestDataProvider().createSelectOneDropdownField(name,
				//							new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//								{
				//									put(InputFieldValues.CIGARETTES_UNTER_5, false);
				//									put(InputFieldValues.CIGARETTES_5_20, false);
				//									put(InputFieldValues.CIGARETTES_20_40, false);
				//									put(InputFieldValues.CIGARETTES_UEBER_40, false);
				//								}
				//							});
				//				case CHRONIC_DISEASE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case CHRONIC_DISEASE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case EPILEPSY_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case EPILEPSY:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case CARDIAC_PROBLEMS_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case CARDIAC_PROBLEMS:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case HYPERTENSION_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case HYPERTENSION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case RENAL_INSUFFICIENCY_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case RENAL_INSUFFICIENCY:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case LIVER_DISEASE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case LIVER_DISEASE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case ANEMIA_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case ANEMIA:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case IMMUNE_MEDIATED_DISEASE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case IMMUNE_MEDIATED_DISEASE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case GESTATION_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case GESTATION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case GESTATION_TYPE:
				//					return getTestDataProvider().createSelectManyField(name,
				//							new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//								{
				//									put(InputFieldValues.SCHWANGER, false);
				//									put(InputFieldValues.STILLEN, false);
				//								}
				//							});
				//				case CONTRACEPTION_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case CONTRACEPTION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case CONTRACEPTION_TYPE:
				//					return getTestDataProvider().createSelectManyField(name,
				//							new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//								{
				//									put(InputFieldValues.HORMONELL, false);
				//									put(InputFieldValues.MECHANISCH, false);
				//									put(InputFieldValues.INTRAUTERINPESSARE, false);
				//									put(InputFieldValues.CHEMISCH, false);
				//									put(InputFieldValues.OPERATIV, false);
				//								}
				//							});
				//				case ALCOHOL_DRUG_ABUSE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case ALCOHOL_DRUG_ABUSE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case PSYCHIATRIC_CONDITION_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case PSYCHIATRIC_CONDITION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case ALLERGY_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case ALLERGY:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case MEDICATION_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case MEDICATION:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case EYE_PROBLEMS_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case EYE_PROBLEMS:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case FEET_PROBLEMS_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case FEET_PROBLEMS:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case DIAGNOSTIC_FINDINGS_AVAILABLE_YN:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case DIAGNOSTIC_FINDINGS_AVAILABLE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case GENERAL_STATE_OF_HEALTH:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case NOTE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case SUBJECT_NUMBER:
				//					// [0-9]{4}: omit ^ and $ here for xeger
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case IC_DATE:
				//					return getTestDataProvider().createDateField(name);
				//				case SCREENING_DATE:
				//					return getTestDataProvider().createDateField(name);
				//				case LAB_NUMBER:
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case RANDOM_NUMBER:
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case LETTER_TO_PHYSICIAN_SENT:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case PARTICIPATION_LETTER_IN_MEDOCS:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case LETTER_TO_SUBJECT_AT_END_OF_STUDY:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case COMPLETION_LETTER_IN_MEDOCS:
				//					return getTestDataProvider().createCheckBoxField(name);
				//				case BODY_HEIGHT:
				//					return getTestDataProvider().createIntegerField(name);
				//				case BODY_WEIGHT:
				//					return getTestDataProvider().createIntegerField(name);
				//				case BODY_MASS_INDEX:
				//					return getTestDataProvider().createFloatField(name);
				//				case OBESITY:
				//					return getTestDataProvider().createSelectOneRadioField(name, new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//						{
				//							put(InputFieldValues.SHORTWEIGHT, false);
				//							put(InputFieldValues.NORMAL_WEIGHT, false);
				//							put(InputFieldValues.OVERWEIGHT, false);
				//							put(InputFieldValues.ADIPOSITY_DEGREE_I, false);
				//							put(InputFieldValues.ADIPOSITY_DEGREE_II, false);
				//							put(InputFieldValues.ADIPOSITY_DEGREE_III, false);
				//						}
				//					});
				//				case EGFR:
				//					return getTestDataProvider().createFloatField(name);
				//				case ETHNICITY:
				//					return getTestDataProvider().createSelectOneDropdownField(name, new TreeMap<InputFieldValuesEnum, Boolean>() {
				//
				//						{
				//							put(InputFieldValues.AMERICAN_INDIAN_OR_ALASKA_NATIVE, false);
				//							put(InputFieldValues.ASIAN, false);
				//							put(InputFieldValues.BLACK, false);
				//							put(InputFieldValues.NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER, false);
				//							put(InputFieldValues.WHITE, false);
				//						}
				//					});
				//				case SERUM_CREATININ_CONCENTRATION:
				//					return getTestDataProvider().createFloatField(name); // range?
				//				case HBA1C_PERCENT:
				//					return getTestDataProvider().createFloatField(name);
				//				case HBA1C_MMOLPERMOL:
				//					return getTestDataProvider().createFloatField(name);
				//				case MANNEQUIN:
				//					return getTestDataProvider().createSketchField(name, new TreeMap<InputFieldValuesEnum, InkStroke>() {
				//
				//						{
				//							put(InputFieldValues.SHOULDER_RIGHT,
				//									new InkStroke("M111.91619,64.773336L134.22667,64.773336L134.22667,86.512383L111.91619,86.512383Z"));
				//							put(InputFieldValues.SHOULDER_LEFT,
				//									new InkStroke("M196.2019,66.916193L218.51238,66.916193L218.51238,88.65524L196.2019,88.65524Z"));
				//							put(InputFieldValues.ELLBOW_RIGHT,
				//									new InkStroke("M114.05904,111.20191L136.36952,111.20191L136.36952,132.94095L114.05904,132.94095Z"));
				//							put(InputFieldValues.ELLBOW_LEFT,
				//									new InkStroke("M197.63047,113.34477L219.94095,113.34477L219.94095,135.08381L197.63047,135.08381Z"));
				//							put(InputFieldValues.WRIST_RIGHT,
				//									new InkStroke("M94.773327,156.9162L117.08381,156.9162L117.08381,178.65524L94.773327,178.65524Z"));
				//							put(InputFieldValues.WRIST_LEFT,
				//									new InkStroke("M215.48761,161.20191L237.7981,161.20191L237.7981,182.94095L215.48761,182.94095Z"));
				//							put(InputFieldValues.THUMB_BASE_RIGHT,
				//									new InkStroke("M29.891238,214.89125L49.823043,214.89125L49.823043,254.9659L29.891238,254.9659Z"));
				//							put(InputFieldValues.THUMB_MIDDLE_RIGHT,
				//									new InkStroke("M9.0200169,246.16289L29.265697,246.16289L29.265697,265.83713L9.0200169,265.83713Z"));
				//							put(InputFieldValues.THUMB_BASE_LEFT,
				//									new InkStroke("M282.0341,217.03411L301.9659,217.03411L301.9659,257.10876L282.0341,257.10876Z"));
				//							put(InputFieldValues.THUMB_MIDDLE_LEFT,
				//									new InkStroke("M301.87716,250.4486L322.12284,250.4486L322.12284,270.12284L301.87716,270.12284Z"));
				//							put(InputFieldValues.INDEX_FINGER_BASE_RIGHT,
				//									new InkStroke("M28.305731,271.87717L48.551411,271.87717L48.551411,291.55141L28.305731,291.55141Z"));
				//							put(InputFieldValues.INDEX_FINGER_MIDDLE_RIGHT,
				//									new InkStroke("M22.591445,294.73431L42.837125,294.73431L42.837125,314.40855L22.591445,314.40855Z"));
				//							put(InputFieldValues.MIDDLE_FINGER_BASE_RIGHT,
				//									new InkStroke("M48.305731,276.16288L68.551411,276.16288L68.551411,295.83712L48.305731,295.83712Z"));
				//							put(InputFieldValues.MIDDLE_FINGER_MIDDLE_RIGHT,
				//									new InkStroke("M44.734302,298.30574L64.979982,298.30574L64.979982,317.97998L44.734302,317.97998Z"));
				//							put(InputFieldValues.RING_FINGER_BASE_RIGHT,
				//									new InkStroke("M66.162873,279.02003L86.408553,279.02003L86.408553,298.69427L66.162873,298.69427Z"));
				//							put(InputFieldValues.RING_FINGER_MIDDLE_RIGHT,
				//									new InkStroke("M65.448587,302.59146L85.694267,302.59146L85.694267,322.2657L65.448587,322.2657Z"));
				//							put(InputFieldValues.LITTLE_FINGER_BASE_RIGHT,
				//									new InkStroke("M85.448587,276.16289L105.69427,276.16289L105.69427,295.83713L85.448587,295.83713Z"));
				//							put(InputFieldValues.LITTLE_FINGER_MIDDLE_RIGHT,
				//									new InkStroke("M87.591444,299.02003L107.83713,299.02003L107.83713,318.69427L87.591444,318.69427Z"));
				//							put(InputFieldValues.INDEX_FINGER_BASE_LEFT,
				//									new InkStroke("M281.16287,274.73432L301.40856,274.73432L301.40856,294.40856L281.16287,294.40856Z"));
				//							put(InputFieldValues.INDEX_FINGER_MIDDLE_LEFT,
				//									new InkStroke("M286.16287,296.16289L306.40856,296.16289L306.40856,315.83713L286.16287,315.83713Z"));
				//							put(InputFieldValues.MIDDLE_FINGER_BASE_LEFT,
				//									new InkStroke("M262.59144,279.73432L282.83713,279.73432L282.83713,299.40856L262.59144,299.40856Z"));
				//							put(InputFieldValues.MIDDLE_FINGER_MIDDLE_LEFT,
				//									new InkStroke("M264.02001,301.87718L284.2657,301.87718L284.2657,321.55142L264.02001,321.55142Z"));
				//							put(InputFieldValues.RING_FINGER_BASE_LEFT,
				//									new InkStroke("M244.7343,282.59147L264.97999,282.59147L264.97999,302.26571L244.7343,302.26571Z"));
				//							put(InputFieldValues.RING_FINGER_MIDDLE_LEFT,
				//									new InkStroke("M244.02001,304.02004L264.2657,304.02004L264.2657,323.69428L244.02001,323.69428Z"));
				//							put(InputFieldValues.LITTLE_FINGER_BASE_LEFT,
				//									new InkStroke("M224.7343,279.02004L244.97999,279.02004L244.97999,298.69428L224.7343,298.69428Z"));
				//							put(InputFieldValues.LITTLE_FINGER_MIDDLE_LEFT,
				//									new InkStroke("M224.02001,301.1629L244.2657,301.1629L244.2657,320.83714L224.02001,320.83714Z"));
				//							put(InputFieldValues.KNEE_RIGHT,
				//									new InkStroke("M133.4355,241.29267L161.27879,241.29267L161.27879,267.13594L133.4355,267.13594Z"));
				//							put(InputFieldValues.KNEE_LEFT,
				//									new InkStroke("M166.29264,242.00696L194.13593,242.00696L194.13593,267.85023L166.29264,267.85023Z"));
				//						}
				//					});
				//				case VAS:
				//					return getTestDataProvider().createSketchField(name, new TreeMap<InputFieldValuesEnum, InkStroke>() {
				//
				//						{
				//							final int VAS_STEPS = 10;
				//							final float VAS_MAX_VALUE = 100.0f;
				//							final float VAS_X_OFFSET = 10.0f;
				//							final float VAS_LENGTH = 382.0f;
				//							final String[] VAS_COLORS = new String[] {
				//									"#24FF00", "#58FF00", "#8DFF00", "#C2FF00",
				//									"#F7FF00", "#FFD300", "#FF9E00", "#FF6900", "#FF3400", "#FF0000" };
				//							for (int i = 0; i < VAS_STEPS; i++) {
				//								float valueFrom = i * VAS_MAX_VALUE / VAS_STEPS;
				//								float valueTo = (i + 1) * VAS_MAX_VALUE / VAS_STEPS;
				//								float value = (valueFrom + valueTo) / 2;
				//								float x1 = VAS_X_OFFSET + i * VAS_LENGTH / VAS_STEPS;
				//								float x2 = VAS_X_OFFSET + (i + 1) * VAS_LENGTH / VAS_STEPS;
				//								int colorIndex = i * VAS_COLORS.length / VAS_STEPS;
				//								put(InputFieldValues.valueOf(String.format("VAS_%d", i + 1)),
				//										new InkStroke(VAS_COLORS[colorIndex], "M" + x1 + ",10L" + x2 + ",10L" + x2
				//												+ ",50L" + x1 + ",50Z", Float.toString(value)));
				//							}
				//						}
				//					});
				//				case ESR:
				//					return getTestDataProvider().createFloatField(name);
				//				case DAS28:
				//					return getTestDataProvider().createFloatField(name);
				//				case DISTANCE:
				//					return getTestDataProvider().createFloatField(name);
				//				case ALPHA_ID:
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case STRING_SINGLELINE:
				//					return getTestDataProvider().createSingleLineTextField(name);
				//				case STRING_MULTILINE:
				//					return getTestDataProvider().createMultiLineTextField(name);
				//				case FLOAT:
				//					return getTestDataProvider().createFloatField(name);
				//				case INTEGER:
				//					return getTestDataProvider().createIntegerField(name);
				//				case DIAGNOSIS_START:
				//					return getTestDataProvider().createDateField(name);
				//				case DIAGNOSIS_END:
				//					return getTestDataProvider().createDateField(name);
				//				case DIAGNOSIS_COUNT:
				//					return getTestDataProvider().createIntegerField(name);
				default:
					return null;
			}
		}
	}

	private TrialOutVO createTrial(String name, long departmentId) throws Exception {
		TrialInVO newTrial = new TrialInVO();
		newTrial.setDepartmentId(departmentId);
		newTrial.setName(name);
		newTrial.setTitle(newTrial.getName());
		newTrial.setDescription(null);
		newTrial.setSignupProbandList(false);
		newTrial.setSignupInquiries(false);
		newTrial.setSignupRandomize(false);
		newTrial.setSignupDescription(null);
		newTrial.setExclusiveProbands(false);
		newTrial.setProbandAliasFormat(null);
		newTrial.setDutySelfAllocationLocked(false);
		return getTestDataProvider().createTrial(newTrial, "migration_started", "na", "na", "na");
	}

	private ArrayList<InquiryOutVO> createInquiryForm(TrialOutVO trial) throws Throwable {
		ArrayList<InquiryOutVO> inquiries = new ArrayList<InquiryOutVO>();
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.TEST), trial, "section", 1));
		//		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.HEIGHT), trial, "01 - Allgemeine information", 1, true, true, false, false, true, true, null,
		//				null, null, null,
		//				null));
		return inquiries;
	}

	private CriteriaOutVO getCriteria(SearchCriteria criteria, String category) throws Throwable {
		CriteriaOutVO crit = getTestDataProvider().getCriteria(criteria, category);
		if (crit != null) {
			return crit;
		} else {
			CriteriaInVO newCriteria = new CriteriaInVO();
			newCriteria.setLabel(criteria.toString());
			newCriteria.setCategory(category);
			switch (criteria) {
				//case SUBJECTS_1:
				case TEST:
					newCriteria.setModule(DBModule.PROBAND_DB);
					return getTestDataProvider().createCriteria(newCriteria,
							new ArrayList<SearchCriterion>() {

								{
									//InputFieldOutVO field1 = getInputField(InputFields.DIABETES_TYPE);
									//InputFieldSelectionSetValue value1 = getTestDataProvider().getInputFieldValue(field1.getId(),
									//		InputFieldValues.TYP_2_DIABETES_OHNE_INSULINEIGENPRODUKTION);
									//add(new SearchCriterion(null, "proband.inquiryValues.inquiry.field.id", CriterionRestriction.EQ, field1.getId()));
									//add(new SearchCriterion(CriterionTie.AND, "proband.inquiryValues.value.selectionValues.id", CriterionRestriction.EQ, value1.getId()));
								}
							});
				default:
					return null;
			}
		}
	}

	private ArrayList<CriteriaOutVO> createCriterias(String category) throws Throwable {
		ArrayList<CriteriaOutVO> criterias = new ArrayList<CriteriaOutVO>();
		for (int i = 0; i < SearchCriteria.values().length; i++) {
			criterias.add(getCriteria(SearchCriteria.values()[i], category));
		}
		return criterias;
	}
}
