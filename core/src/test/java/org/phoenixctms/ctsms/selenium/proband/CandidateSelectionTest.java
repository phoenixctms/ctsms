package org.phoenixctms.ctsms.selenium.proband;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.openqa.selenium.Keys;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.CriterionRestriction;
import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.selenium.SeleniumTestBase;
import org.phoenixctms.ctsms.test.InputFieldValuesEnum;
import org.phoenixctms.ctsms.test.InputFieldsEnum;
import org.phoenixctms.ctsms.test.Random;
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
	private Long probandId;
	private Set<Long> actualProbandIds = new LinkedHashSet<Long>(PROBAND_COUNT);
	private Map<SearchCriteria, LinkedHashSet<Long>> expectedProbandIdsMap = new HashMap<SearchCriteria, LinkedHashSet<Long>>();
	private Random random = new Random();

	private enum InputFields implements InputFieldsEnum {

		CST_HEIGHT("Körpergröße"),
		CST_WEIGHT("Körpergewicht"),
		CST_BMI("Body Mass Index"),
		CST_SMOKER_YN("Raucher J/N"),
		CST_CIGARETTES_PER_DAY("Zigaretten pro Tag"),
		CST_VEIN_CONDITION_PROBAND("Venenstatus (Proband)"),
		CST_VEIN_CONDITION_PYSICIAN("Venenstatus (Arzt)"),
		CST_NOTE("Anmerkung"),
		CST_UNDERLYING_DISEASE("Grunderkrankung"),
		CST_DIABETES_THERAPY("Diabetestherapie"),
		CST_CSII_TRADE_MARK("Pumpenmarke"),
		CST_CSII_TRADE_MARK_OTHER("Diabetestherapie (andere)"),
		CST_SENSOR_THERAPY("Sensortherapie"),
		CST_SENSOR_TRADE_MARK("Sensormarke"),
		CST_BOLUS_INSULIN("Bolusinsulin"),
		CST_BOLUS_INSULIN_IU_BE("Bolusinsulin IU/BE"),
		CST_BOLUS_INSULIN_TDD("Bolusinsulin TDD"),
		CST_BASAL_INSULIN("Basalinsulin"),
		CST_BASAL_INSULIN_TDD("Basalinsulin TDD"),
		CST_MIXED_INSULIN("Mischinsulin"),
		CST_MIXED_INSULIN_TDD("Mischinsulin TDD"),
		CST_OAD_METFORMIN("Orale Antidiabetika-Metformin"),
		CST_OAD_METFORMIN_TDD("Metformin TDD"),
		CST_OAD_OTHER("OAD - Andere"),
		CST_OTHER_MEDICATION("Weitere Medikamente"),
		CST_DIABETES_SINCE("Diabetes bekannt seit"),
		CST_HBA1C_MMOLPERMOL("HbA1C in mmol/mol"),
		CST_HBA1C_PERCENT("HbA1C in prozent"),
		CST_C_PEPTIDE("C-Peptid"), // µg/l
		CST_RECRUITED_FROM("Geworben durch"),
		CST_SUBJECT_FILE("ProbandInnenakte"),
		CST_DISTANCE("Entfernung"),
		CST_UPDATED("Aktualisierung"),
		CST_UPDATED_DATE("Aktualisierung Datum");

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

		CST_CIGARETTES_BELOW_5("Unter 5"),
		CST_CIGARETTES_5_20("5-20"),
		CST_CIGARETTES_20_40("20-40"),
		CST_CIGARETTES_ABOVE_40("über 40"),
		CST_VEIN_CONDITION_PYSICIAN_1("1: Sehr gut = mehrere starke Venen auf beiden Armen"),
		CST_VEIN_CONDITION_PYSICIAN_2("2: Gut = punktierbare Venen tastbar/sichtbar auf beiden Armen"),
		CST_VEIN_CONDITION_PYSICIAN_3("3: Schlecht = ein-zwei Venen, Venflon fraglich"),
		CST_VEIN_CONDITION_PYSICIAN_4("4: Sehr schlecht = kein Venflon möglich"),
		CST_VEIN_CONDITION_PROBAND_GOOD("gut"),
		CST_VEIN_CONDITION_PROBAND_MEDIOCRE("mittel"),
		CST_VEIN_CONDITION_PROBAND_BAD("schlecht"),
		CST_DIABETES_TYPE_1("Diabetes Typ I"),
		CST_DIABETES_TYPE_2("Diabetes Typ II"),
		CST_DIABETES_TYPE_UNKNOWN("Diabetestyp unbekannt"),
		CST_DIABETES_TYPE_OTHER("Diabetestyp anders"),
		CST_HEALTHY("Gesund"),
		CST_CSII("Insulinpumpe (CSII)"),
		CST_MDI_IIT("Basal-Bolus Therapie (MDI/IIT)"),
		CST_MDI_IIT_OAD("Basal-Bolus Therapie + Orale Antidiabetika (OAD)"),
		CST_PRANDIAL("Prandiale Insulintherapie"),
		CST_MIXED_INSULIN("Mischinsulintherapie"),
		CST_BASAL_INSULIN("Basalinsulintherapie"),
		CST_OAD("Orale Antidiabetika (OAD)"),
		CST_OAD_BASAL("Orale Antidiabetika (OAD) + Basalinsulin"),
		CST_OAD_BOLUS("Orale Antidiabetika (OAD) + Bolusinsulin"),
		CST_OAD_MIXED("Orale Antidiabetika (OAD) + Mischinsulin"),
		CST_CSII_OAD("Insulinpumpe (CSII)+OAD"),
		CST_MEDTRONIC("Medtronic (640G, Paradigm, Minimed)"),
		CST_ANIMAS("Animas (Vibe)"),
		CST_YPSOMED("Ypsomed (Omnipod)"),
		CST_ROCHE("Roche (Insight, Spirit Combo)"),
		CST_NO_SENSOR("kein Sensor"),
		CST_CGM("CGM (continous glucose measurement)"),
		CST_FGM("FGM (flash glucose measurement) Libre"),
		CST_ACTRAPID("Actrapid"),
		CST_APIDRA("Apidra"),
		CST_HUMALOG("Humalog"),
		CST_HUMINSULIN_LILLY_NORMAL("Huminsulin Lilly Normal"),
		CST_INSUMAN_RAPID("Insuman Rapid"),
		CST_NOVORAPID("Novorapid"),
		CST_BOLUS_INSULIN_OTHER("Andere"),
		CST_FIASP("Fiasp"),
		CST_LEVEMIR("Levemir"),
		CST_LANTUS("Lantus"),
		CST_HUMINSULIN_LILLY_BASAL("Huminsulin Lilly Basal"),
		CST_INSULATARD("Insulatard"),
		CST_INSUMAN_BASAL("Insuman Basal"),
		CST_TRESIBA("Tresiba"),
		CST_BASAL_INSULIN_OTHER("Andere"),
		CST_TOUJEO("Toujeo"),
		CST_HUMALOG_MIX_25("Humalog Mix 25"),
		CST_HUMALOG_MIX_50("Humalog Mix 50"),
		CST_HUMINSULIN_LILLY_PROFIL_3("Huminsulin Lilly Profil III"),
		CST_INSUMAN_COMB_25("Insuman Comb 25"),
		CST_INSUMAN_COMB_50("Insuman Comb 50"),
		CST_MIXTARD_30("Mixtard 30"),
		CST_MIXTARD_50("Mixtard 50"),
		CST_NOVOMIX_30("Novomix 30"),
		CST_NOVOMIX_70("Novomix 70"),
		CST_MIXED_INSULIN_OTHER("Andere"),
		CST_SULFONYLUREAS("Sulfonylharnstoffe (Amaryl, Daonil, Diamicron, Gliclazid, Glimepirid, Glucobene, Glurenorm, Minidiab)"),
		CST_ALPHA_GLUCOSIDASE_INHIBITORS("Alpha-Glukosidasehemmer (Diastabol, Glucobay)"),
		CST_GLINIDE("Glinide (Repaglinid, Starlix)"),
		CST_GLITAZONE("Glitazone (Actos, Diabetalan, Pioglitazon)"),
		CST_GLIPTINE("Gliptine/DPP-4-Inhibitoren (Galvus, Januvia, Onglyza, Tesavel, Trajenta, Vipdomet)"),
		CST_INCRETINE("GLP1-Analoga/Inkretine (Byetta, Lyxumia, Victoza)"),
		CST_GLIFLOZINE("SGLT2-Inhibitoren/Gliflozine (Forxiga, Jardiance)"),
		CST_METFORMIN_GLIPTINE("Kombination Metformin + Gliptin (Eucreas, Janumet, Jentadueto, Komboglyze, Velmetia)"),
		CST_METFORMIN_GLITAZONE("Kombination Metformin + Glitazon (Competact)"),
		CST_GLITAZONE_GLIMEPIRID("Kombination Glitazon + Glimepirid (Sulfonylharnstoff) (Tandemact)"),
		CST_OAD_OTHER("Andere OAD´s (bitte im Komentarfeld angeben)"),
		CST_METFORMIN_DAPAGLIFLOZINE("Metformin + Dapagliflozine (Xigduo)"),
		CST_ANTIHYPERTENSIVE_DRUGS("Antihypertensive Medikamente"),
		CST_LIPID_LOWERING_DRUGS("Lipidsenkende Medikamente"),
		CST_ANTICOAGULANT_DRUGS("Gerinnungshemmende Medikamente"),
		CST_PSYCHOTROPIC_DRUGS("Psychopharmaka"),
		CST_CORTISONE("Kortison"),
		CST_SUBJECT_FILE_YES("Ja"),
		CST_SUBJECT_FILE_NO("Nein"),
		CST_UPDATED_DONE("erledigt"),
		CST_UPDATED_PENDING("noch nicht erfolgt"),
		CST_UPDATED_UNREACHABLE("Proband nicht erreicht");

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

		DEPARTMENT_PROBANDS("department probands"),
		DISEASE("probands with particular disease"),
		DISEASE_THERAPY("probands with particular disease and particular therapy");

		private final String value;

		private SearchCriteria(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	@BeforeClass(description = "Create a department for test user subjects, trial and user.")
	public void init_00_setup_department() throws Exception {
		String departmentName = "dept-" + getTestId();
		departmentPassword = departmentName;
		department = getTestDataProvider().createDepartment(departmentName, true, departmentPassword);
	}

	@BeforeClass(description = "Create a test user.")
	public void init_01_setup_user() throws Exception {
		userName = "user-" + getTestId();
		userPassword = userName;
		UserOutVO user = createUser(userName, userPassword, department.getId(), departmentPassword);
		setProviderAuth(userName, userPassword);
	}

	@BeforeClass(description = "Create a test trial.")
	public void init_02_setup_trial() throws Exception {
		String trialName = "trial-" + getTestId();
		trial = createTrial(trialName, department.getId());
	}

	@BeforeClass(description = "Setup a test inquiry form for the trial.")
	public void init_03_setup_inquiry_form() throws Throwable {
		ArrayList<InquiryOutVO> inquiries = createInquiryForm(trial);
	}

	@BeforeClass(description = "Setup the test test search criteria.")
	public void init_04_setup_proband_criteria() throws Throwable {
		criteriaCategory = getTestId();
		ArrayList<CriteriaOutVO> criterias = createCriterias(criteriaCategory);
	}

	@Test(description = "Try to open the proband page of the Phoenix CTMS test instance. It will redirect to the login page at first.")
	public void test_01_open_proband_page() {
		load(getUrl("/proband/proband.jsf"));
	}

	@Test(description = "Log in with the user created for this test.")
	public void test_02_login() {
		login(userName, userPassword);
	}

	@Test(description = "Create a blinded subject that belongs to the departement created for this testclass. The subject alias look like \"subject-xxx\".")
	public void test_03_create_proband() {
		String subjectId = "subject-" + String.format("%03d", probandIds.size() + 1) + "-" + getTestId();
		info("subject id: " + subjectId);
		clickCheckbox("tabView:probandmain_form:blinded");
		sendKeys("tabView:probandmain_form:alias", subjectId);
		clickButton("tabView:probandmain_form:add");
		if (waitForAddOperationSuccessful("tabView:probandmain_form")) {
			probandId = getProbandIdFromProbandEntityWindowName(getWindowName());
			testOK("proband ID " + probandId.toString() + " created: " + subjectId);
			probandIds.add(probandId);
			return;
		} else {
			testFailed("creating proband failed: " + subjectId);
			return;
		}
	}

	@Test(description = "Open the test inquiry form for the created subject.")
	public void test_04_load_inquiry_form() {
		clickTab("tabView:inquiryvalues");
		clickSelectOneMenu("tabView:inquiryvalue_form:inquiriestrial", trial.getName());
	}

	@Test(description = "Fill in values for the test inquiry form of the created subject.")
	public void test_05_enter_inquiry_values() {
		//01 - Basisinformation
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_HEIGHT.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_WEIGHT.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_BMI.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickCheckbox(getFieldIdByLabel(InputFields.CST_SMOKER_YN.toString()));
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_CIGARETTES_PER_DAY.toString()), InputFieldValues.CST_CIGARETTES_BELOW_5.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_CIGARETTES_PER_DAY.toString()), InputFieldValues.CST_CIGARETTES_5_20.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_CIGARETTES_PER_DAY.toString()), InputFieldValues.CST_CIGARETTES_20_40.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_CIGARETTES_PER_DAY.toString()), InputFieldValues.CST_CIGARETTES_ABOVE_40.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PROBAND.toString()), InputFieldValues.CST_VEIN_CONDITION_PROBAND_GOOD.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PROBAND.toString()), InputFieldValues.CST_VEIN_CONDITION_PROBAND_MEDIOCRE.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PROBAND.toString()), InputFieldValues.CST_VEIN_CONDITION_PROBAND_BAD.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PYSICIAN.toString()), InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_1.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PYSICIAN.toString()), InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_2.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PYSICIAN.toString()), InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_3.toString());
				//clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_VEIN_CONDITION_PYSICIAN.toString()), InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_4.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdBySectionPositionName("01 - Basisinformation", 8, InputFields.CST_NOTE.name()), "123");
			}
		}.enter();
		//02 - Medikamentöse Therapie
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				if (random.getRandomBoolean(50)) {
					clickSelectMany(getFieldIdByLabel(InputFields.CST_UNDERLYING_DISEASE.toString()), InputFieldValues.CST_DIABETES_TYPE_1.toString());
					addExpectedProbandId(SearchCriteria.DISEASE);
					addExpectedProbandId(SearchCriteria.DISEASE_THERAPY);
				}
				clickSelectMany(getFieldIdByLabel(InputFields.CST_UNDERLYING_DISEASE.toString()), InputFieldValues.CST_DIABETES_TYPE_2.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_UNDERLYING_DISEASE.toString()), InputFieldValues.CST_DIABETES_TYPE_UNKNOWN.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_UNDERLYING_DISEASE.toString()), InputFieldValues.CST_DIABETES_TYPE_OTHER.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_UNDERLYING_DISEASE.toString()), InputFieldValues.CST_HEALTHY.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				if (random.getRandomBoolean(50)) {
					clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_MDI_IIT_OAD.toString());
				} else {
					removeExpectedProbandId(SearchCriteria.DISEASE_THERAPY);
				}
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_CSII.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_MDI_IIT.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_MDI_IIT_OAD.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_PRANDIAL.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_MIXED_INSULIN.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_BASAL_INSULIN.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_OAD.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_OAD_BASAL.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_OAD_BOLUS.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_OAD_MIXED.toString());
				//				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_DIABETES_THERAPY.toString()), InputFieldValues.CST_CSII_OAD.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdBySectionPositionName("02 - Medikamentöse Therapie", 3, InputFields.CST_NOTE.name()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_CSII_TRADE_MARK.toString()), InputFieldValues.CST_MEDTRONIC.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_CSII_TRADE_MARK.toString()), InputFieldValues.CST_ANIMAS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_CSII_TRADE_MARK.toString()), InputFieldValues.CST_YPSOMED.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_CSII_TRADE_MARK.toString()), InputFieldValues.CST_ROCHE.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_CSII_TRADE_MARK_OTHER.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_SENSOR_THERAPY.toString()), InputFieldValues.CST_NO_SENSOR.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_SENSOR_THERAPY.toString()), InputFieldValues.CST_CGM.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_SENSOR_THERAPY.toString()), InputFieldValues.CST_FGM.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_SENSOR_TRADE_MARK.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_ACTRAPID.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_APIDRA.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_HUMALOG.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_HUMINSULIN_LILLY_NORMAL.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_INSUMAN_RAPID.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_NOVORAPID.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_BOLUS_INSULIN_OTHER.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN.toString()), InputFieldValues.CST_FIASP.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN_IU_BE.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_BOLUS_INSULIN_TDD.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_LEVEMIR.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_LANTUS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_HUMINSULIN_LILLY_BASAL.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_INSULATARD.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_INSUMAN_BASAL.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_TRESIBA.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_BASAL_INSULIN_OTHER.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN.toString()), InputFieldValues.CST_TOUJEO.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_BASAL_INSULIN_TDD.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_HUMALOG_MIX_25.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_HUMALOG_MIX_50.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_HUMINSULIN_LILLY_PROFIL_3.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_INSUMAN_COMB_25.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_INSUMAN_COMB_50.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_MIXTARD_30.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_MIXTARD_50.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_NOVOMIX_30.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_NOVOMIX_70.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN.toString()), InputFieldValues.CST_MIXED_INSULIN_OTHER.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_MIXED_INSULIN_TDD.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickCheckbox(getFieldIdByLabel(InputFields.CST_OAD_METFORMIN.toString()));
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_OAD_METFORMIN_TDD.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_SULFONYLUREAS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_ALPHA_GLUCOSIDASE_INHIBITORS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_GLINIDE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_GLITAZONE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_GLIPTINE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_INCRETINE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_GLIFLOZINE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_METFORMIN_GLIPTINE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_METFORMIN_GLITAZONE.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_GLITAZONE_GLIMEPIRID.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_OAD_OTHER.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OAD_OTHER.toString()), InputFieldValues.CST_METFORMIN_DAPAGLIFLOZINE.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OTHER_MEDICATION.toString()), InputFieldValues.CST_ANTIHYPERTENSIVE_DRUGS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OTHER_MEDICATION.toString()), InputFieldValues.CST_LIPID_LOWERING_DRUGS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OTHER_MEDICATION.toString()), InputFieldValues.CST_ANTICOAGULANT_DRUGS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OTHER_MEDICATION.toString()), InputFieldValues.CST_PSYCHOTROPIC_DRUGS.toString());
				clickSelectMany(getFieldIdByLabel(InputFields.CST_OTHER_MEDICATION.toString()), InputFieldValues.CST_CORTISONE.toString());
			}
		}.enter();
		//03 - Sonstiges
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_DIABETES_SINCE.toString()), "2022-10-10");
				sendKeys(getFieldIdByLabel(InputFields.CST_DIABETES_SINCE.toString()), Keys.ESCAPE); //close the dateselector popup as it can hide a button to click next
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_HBA1C_PERCENT.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_HBA1C_MMOLPERMOL.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_C_PEPTIDE.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdBySectionPositionName("03 - Sonstiges", 5, InputFields.CST_NOTE.name()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_RECRUITED_FROM.toString()), "123");
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_SUBJECT_FILE.toString()), InputFieldValues.CST_SUBJECT_FILE_YES.toString());
				clickSelectOneMenu(getFieldIdByLabel(InputFields.CST_SUBJECT_FILE.toString()), InputFieldValues.CST_SUBJECT_FILE_NO.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_DISTANCE.toString()), "123");
			}
		}.enter();
		//04 - Aktualisierung der Daten
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				clickSelectOneRadio(getFieldIdByLabel(InputFields.CST_UPDATED.toString()), InputFieldValues.CST_UPDATED_DONE.toString());
				//clickSelectOneRadio(getFieldIdByLabel(InputFields.CST_UPDATED.toString()),InputFieldValues.CST_UPDATED_PENDING.toString());
				//clickSelectOneRadio(getFieldIdByLabel(InputFields.CST_UPDATED.toString()),InputFieldValues.CST_UPDATED_UNREACHABLE.toString());
			}
		}.enter();
		new InquiryFormFieldEntry() {

			@Override
			protected void entry() {
				sendKeys(getFieldIdByLabel(InputFields.CST_UPDATED_DATE.toString()), "2022-10-10");
				sendKeys(getFieldIdByLabel(InputFields.CST_UPDATED_DATE.toString()), Keys.ESCAPE); //close the dateselector popup as it can hide a button to click next
			}
		}.enter();
		clickButton("tabView:inquiryvalue_form:update");
		if (waitForUpdateOperationSuccessful("tabView:inquiryvalue_form")) {
			//createScreenshot();
			addExpectedProbandId(SearchCriteria.DEPARTMENT_PROBANDS);
			testOK("inquiry values saved");
			return;
		} else {
			testFailed("saving inquiry values failed");
			return;
		}
	}

	private LinkedHashSet<Long> getExpectedProbandIds(SearchCriteria criteria) {
		LinkedHashSet<Long> probandIds;
		if (expectedProbandIdsMap.containsKey(criteria)) {
			probandIds = expectedProbandIdsMap.get(criteria);
		} else {
			probandIds = new LinkedHashSet<Long>();
			expectedProbandIdsMap.put(criteria, probandIds);
		}
		return probandIds;
	}
	//	private boolean isProbandIdExpected(SearchCriteria criteria) {
	//		return getExpectedProbandIds(criteria).contains(probandId);
	//	}

	private boolean addExpectedProbandId(SearchCriteria criteria) {
		return getExpectedProbandIds(criteria).add(probandId);
	}

	private boolean removeExpectedProbandId(SearchCriteria criteria) {
		return getExpectedProbandIds(criteria).remove(probandId);
	}

	@Test(description = "Create more subjects, and populate their inquiry forms.")
	public void test_06_create_probands() {
		for (int i = 2; i <= PROBAND_COUNT; i++) {
			//info("entering proband " + i + "...");
			clickMenuItem("menubar_form:newEntityMenuItem_proband");
			closeWindow(getWindowName());
			switchToWindow(getNewProbandEntityWindowName());
			test_03_create_proband();
			createScreenshot();
			test_04_load_inquiry_form();
			test_05_enter_inquiry_values();
		}
	}

	private void openSearch(SearchCriteria criteria) throws Throwable {
		load(getUrl("/proband/probandSearch.jsf?criteriaid=" + getCriteria(criteria, criteriaCategory).getId()));
	}

	public void checkSearchResultSize(SearchCriteria criteria) throws Throwable {
		setSkipScreenshot(true);
		info("performing search '" + criteria.toString() + "'");
		clickButton(getButtonIdByLabel("Perform search"));
		Long count = getCountFromDatatableHead("search_form:proband_result_list");
		if (getExpectedProbandIds(criteria).size() == count) {
			testOK("search returns expected number of items");
			return;
		} else {
			testFailed("search returns different number of items");
			return;
		}
	}

	public void checkSearchAllExpectedProbands(SearchCriteria criteria) throws Throwable {
		setSkipScreenshot(true);
		do {
			actualProbandIds.addAll(getDatatableRowIds("search_form:proband_result_list"));
			break;
		} while (clickDatatableNextPage("search_form:proband_result_list"));
		LinkedHashSet<Long> diff = new LinkedHashSet<Long>();
		diff.addAll(getExpectedProbandIds(criteria));
		diff.removeAll(actualProbandIds);
		if (diff.size() == 0) {
			testOK("search returned all expected probands");
			return;
		} else {
			testFailed("search did not return expected proband IDs: " + diff.toString());
			return;
		}
	}

	public void checkSearchNoUnexpectedExpectedProbands(SearchCriteria criteria) throws Throwable {
		setSkipScreenshot(true);
		LinkedHashSet<Long> diff = new LinkedHashSet<Long>();
		diff.addAll(actualProbandIds);
		diff.removeAll(getExpectedProbandIds(criteria));
		if (diff.size() == 0) {
			testOK("search returned no unexpected probands");
			return;
		} else {
			testFailed("search returned unexpected proband IDs: " + diff.toString());
			return;
		}
	}

	@Test(description = "Load a predefined search to list any subjects created by this test.")
	public void test_07_search_DEPARTMENT_PROBANDS_open() throws Throwable {
		openSearch(SearchCriteria.DEPARTMENT_PROBANDS);
	}

	@Test(description = "Search any subjects created by this test - check expected result size")
	public void test_08_search_DEPARTMENT_PROBANDS_result_size() throws Throwable {
		checkSearchResultSize(SearchCriteria.DEPARTMENT_PROBANDS);
	}

	@Test(description = "Search any subjects created by this test - check if all expected subjects are listed")
	public void test_09_search_DEPARTMENT_PROBANDS_all_expected_probands() throws Throwable {
		checkSearchAllExpectedProbands(SearchCriteria.DEPARTMENT_PROBANDS);
	}

	@Test(description = "Search any subjects created by this test - check if no unexpected subjects are listed")
	public void test_10_search_DEPARTMENT_PROBANDS_no_unexpected_probands() throws Throwable {
		checkSearchNoUnexpectedExpectedProbands(SearchCriteria.DEPARTMENT_PROBANDS);
	}

	@Test(description = "Load a predefined search to list subjects with a particular disease.")
	public void test_11_search_DISEASE_open() throws Throwable {
		openSearch(SearchCriteria.DISEASE);
	}

	@Test(description = "Search subjects with a particular disease - check expected result size")
	public void test_12_search_DISEASE_result_size() throws Throwable {
		checkSearchResultSize(SearchCriteria.DISEASE);
	}

	@Test(description = "Search subjects with a particular disease - check if all expected subjects are listed")
	public void test_13_search_DISEASE_all_expected_probands() throws Throwable {
		checkSearchAllExpectedProbands(SearchCriteria.DISEASE);
	}

	@Test(description = "Search subjects with a particular disease - check if no unexpected subjects are listed")
	public void test_14_search_DISEASE_no_unexpected_probands() throws Throwable {
		checkSearchNoUnexpectedExpectedProbands(SearchCriteria.DISEASE);
	}

	@Test(description = "Load a predefined search to list subjects with a particular disease and particular therapy.")
	public void test_15_search_DISEASE_THERAPY_open() throws Throwable {
		openSearch(SearchCriteria.DISEASE_THERAPY);
	}

	@Test(description = "List subjects with a particular disease and particular therapy - check expected result size")
	public void test_16_search_DISEASE_THERAPY_result_size() throws Throwable {
		checkSearchResultSize(SearchCriteria.DISEASE_THERAPY);
	}

	@Test(description = "List subjects with a particular disease and particular therapy - check if all expected subjects are listed")
	public void test_17_search_DISEASE_THERAPY_all_expected_probands() throws Throwable {
		checkSearchAllExpectedProbands(SearchCriteria.DISEASE_THERAPY);
	}

	@Test(description = "List subjects with a particular disease and particular therapy - check if no unexpected subjects are listed")
	public void test_18_search_DISEASE_THERAPY_no_unexpected_probands() throws Throwable {
		checkSearchNoUnexpectedExpectedProbands(SearchCriteria.DISEASE_THERAPY);
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
			String name = inputField.name();
			String title = inputField.toString();
			switch (inputField) {
				//case HEIGHT:
				case CST_HEIGHT:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_WEIGHT:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_BMI:
					return getTestDataProvider().createFloatField(name, title);
				case CST_SMOKER_YN:
					return getTestDataProvider().createCheckBoxField(name, title);
				case CST_CIGARETTES_PER_DAY:
					return getTestDataProvider().createSelectOneDropdownField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_CIGARETTES_BELOW_5, false);
									put(InputFieldValues.CST_CIGARETTES_5_20, false);
									put(InputFieldValues.CST_CIGARETTES_20_40, false);
									put(InputFieldValues.CST_CIGARETTES_ABOVE_40, false);
								}
							});
				case CST_VEIN_CONDITION_PROBAND:
					return getTestDataProvider().createSelectOneDropdownField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_VEIN_CONDITION_PROBAND_GOOD, false);
									put(InputFieldValues.CST_VEIN_CONDITION_PROBAND_MEDIOCRE, false);
									put(InputFieldValues.CST_VEIN_CONDITION_PROBAND_BAD, false);
								}
							});
				case CST_VEIN_CONDITION_PYSICIAN:
					return getTestDataProvider().createSelectOneDropdownField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_1, false);
									put(InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_2, false);
									put(InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_3, false);
									put(InputFieldValues.CST_VEIN_CONDITION_PYSICIAN_4, false);
								}
							});
				case CST_NOTE:
					return getTestDataProvider().createMultiLineTextField(name, title);
				case CST_UNDERLYING_DISEASE:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_DIABETES_TYPE_1, false);
									put(InputFieldValues.CST_DIABETES_TYPE_2, false);
									put(InputFieldValues.CST_DIABETES_TYPE_UNKNOWN, false);
									put(InputFieldValues.CST_DIABETES_TYPE_OTHER, false);
									put(InputFieldValues.CST_HEALTHY, false);
								}
							});
				case CST_DIABETES_THERAPY:
					return getTestDataProvider().createSelectOneDropdownField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_CSII, false);
									put(InputFieldValues.CST_MDI_IIT, false);
									put(InputFieldValues.CST_MDI_IIT_OAD, false);
									put(InputFieldValues.CST_PRANDIAL, false);
									put(InputFieldValues.CST_MIXED_INSULIN, false);
									put(InputFieldValues.CST_BASAL_INSULIN, false);
									put(InputFieldValues.CST_OAD, false);
									put(InputFieldValues.CST_OAD_BASAL, false);
									put(InputFieldValues.CST_OAD_BOLUS, false);
									put(InputFieldValues.CST_OAD_MIXED, false);
									put(InputFieldValues.CST_CSII_OAD, false);
								}
							});
				case CST_CSII_TRADE_MARK:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_MEDTRONIC, false);
									put(InputFieldValues.CST_ANIMAS, false);
									put(InputFieldValues.CST_YPSOMED, false);
									put(InputFieldValues.CST_ROCHE, false);
								}
							});
				case CST_CSII_TRADE_MARK_OTHER:
					return getTestDataProvider().createAutoCompleteField(name, title);
				case CST_SENSOR_THERAPY:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_NO_SENSOR, false);
									put(InputFieldValues.CST_CGM, false);
									put(InputFieldValues.CST_FGM, false);
								}
							});
				case CST_SENSOR_TRADE_MARK:
					return getTestDataProvider().createAutoCompleteField(name, title);
				case CST_BOLUS_INSULIN:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_ACTRAPID, false);
									put(InputFieldValues.CST_APIDRA, false);
									put(InputFieldValues.CST_HUMALOG, false);
									put(InputFieldValues.CST_HUMINSULIN_LILLY_NORMAL, false);
									put(InputFieldValues.CST_INSUMAN_RAPID, false);
									put(InputFieldValues.CST_NOVORAPID, false);
									//put(InputFieldValues.CST_NOVORAPID, false);
									put(InputFieldValues.CST_BOLUS_INSULIN_OTHER, false);
									put(InputFieldValues.CST_FIASP, false);
								}
							});
				case CST_BOLUS_INSULIN_IU_BE:
					return getTestDataProvider().createFloatField(name, title);
				case CST_BOLUS_INSULIN_TDD:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_BASAL_INSULIN:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_LEVEMIR, false);
									put(InputFieldValues.CST_LANTUS, false);
									put(InputFieldValues.CST_HUMINSULIN_LILLY_BASAL, false);
									put(InputFieldValues.CST_INSULATARD, false);
									put(InputFieldValues.CST_INSUMAN_BASAL, false);
									put(InputFieldValues.CST_TRESIBA, false);
									put(InputFieldValues.CST_BASAL_INSULIN_OTHER, false);
									put(InputFieldValues.CST_TOUJEO, false);
								}
							});
				case CST_BASAL_INSULIN_TDD:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_MIXED_INSULIN:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_HUMALOG_MIX_25, false);
									put(InputFieldValues.CST_HUMALOG_MIX_50, false);
									put(InputFieldValues.CST_HUMINSULIN_LILLY_PROFIL_3, false);
									put(InputFieldValues.CST_INSUMAN_COMB_25, false);
									put(InputFieldValues.CST_INSUMAN_COMB_50, false);
									put(InputFieldValues.CST_MIXTARD_30, false);
									put(InputFieldValues.CST_MIXTARD_50, false);
									put(InputFieldValues.CST_NOVOMIX_30, false);
									put(InputFieldValues.CST_NOVOMIX_70, false);
									put(InputFieldValues.CST_MIXED_INSULIN_OTHER, false);
								}
							});
				case CST_MIXED_INSULIN_TDD:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_OAD_METFORMIN:
					return getTestDataProvider().createCheckBoxField(name, title);
				case CST_OAD_METFORMIN_TDD:
					return getTestDataProvider().createIntegerField(name, title);
				case CST_OAD_OTHER:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_SULFONYLUREAS, false);
									put(InputFieldValues.CST_ALPHA_GLUCOSIDASE_INHIBITORS, false);
									put(InputFieldValues.CST_GLINIDE, false);
									put(InputFieldValues.CST_GLITAZONE, false);
									put(InputFieldValues.CST_GLIPTINE, false);
									put(InputFieldValues.CST_INCRETINE, false);
									put(InputFieldValues.CST_GLIFLOZINE, false);
									put(InputFieldValues.CST_METFORMIN_GLIPTINE, false);
									put(InputFieldValues.CST_METFORMIN_GLITAZONE, false);
									put(InputFieldValues.CST_GLITAZONE_GLIMEPIRID, false);
									put(InputFieldValues.CST_OAD_OTHER, false);
									put(InputFieldValues.CST_METFORMIN_DAPAGLIFLOZINE, false);
								}
							});
				case CST_OTHER_MEDICATION:
					return getTestDataProvider().createSelectManyField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_ANTIHYPERTENSIVE_DRUGS, false);
									put(InputFieldValues.CST_LIPID_LOWERING_DRUGS, false);
									put(InputFieldValues.CST_ANTICOAGULANT_DRUGS, false);
									put(InputFieldValues.CST_PSYCHOTROPIC_DRUGS, false);
									put(InputFieldValues.CST_CORTISONE, false);
								}
							});
				case CST_DIABETES_SINCE:
					return getTestDataProvider().createDateField(name, title);
				case CST_HBA1C_PERCENT:
					return getTestDataProvider().createFloatField(name, title);
				case CST_HBA1C_MMOLPERMOL:
					return getTestDataProvider().createFloatField(name, title);
				case CST_C_PEPTIDE:
					return getTestDataProvider().createFloatField(name, title);
				case CST_RECRUITED_FROM:
					return getTestDataProvider().createAutoCompleteField(name, title);
				case CST_SUBJECT_FILE:
					return getTestDataProvider().createSelectOneDropdownField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_SUBJECT_FILE_YES, false);
									put(InputFieldValues.CST_SUBJECT_FILE_NO, false);
								}
							});
				case CST_DISTANCE:
					return getTestDataProvider().createFloatField(name, title);
				case CST_UPDATED:
					return getTestDataProvider().createSelectOneRadioField(name, title,
							new TreeMap<InputFieldValuesEnum, Boolean>() {

								{
									put(InputFieldValues.CST_UPDATED_DONE, false);
									put(InputFieldValues.CST_UPDATED_PENDING, false);
									put(InputFieldValues.CST_UPDATED_UNREACHABLE, false);
								}
							});
				case CST_UPDATED_DATE:
					return getTestDataProvider().createDateField(name, title);
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
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_HEIGHT), trial, "01 - Basisinformation", 1));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_WEIGHT), trial, "01 - Basisinformation", 2));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BMI), trial, "01 - Basisinformation", 3));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_SMOKER_YN), trial, "01 - Basisinformation", 4));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_CIGARETTES_PER_DAY), trial, "01 - Basisinformation", 5));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_VEIN_CONDITION_PROBAND), trial, "01 - Basisinformation", 6));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_VEIN_CONDITION_PYSICIAN), trial, "01 - Basisinformation", 7));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_NOTE), trial, "01 - Basisinformation", 8));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_UNDERLYING_DISEASE), trial, "02 - Medikamentöse Therapie", 1));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_DIABETES_THERAPY), trial, "02 - Medikamentöse Therapie", 2));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_NOTE), trial, "02 - Medikamentöse Therapie", 3));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_CSII_TRADE_MARK), trial, "02 - Medikamentöse Therapie", 4));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_CSII_TRADE_MARK_OTHER), trial, "02 - Medikamentöse Therapie", 5));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_SENSOR_THERAPY), trial, "02 - Medikamentöse Therapie", 6));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_SENSOR_TRADE_MARK), trial, "02 - Medikamentöse Therapie", 7));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BOLUS_INSULIN), trial, "02 - Medikamentöse Therapie", 8));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BOLUS_INSULIN_IU_BE), trial, "02 - Medikamentöse Therapie", 9));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BOLUS_INSULIN_TDD), trial, "02 - Medikamentöse Therapie", 10));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BASAL_INSULIN), trial, "02 - Medikamentöse Therapie", 11));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_BASAL_INSULIN_TDD), trial, "02 - Medikamentöse Therapie", 12));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_MIXED_INSULIN), trial, "02 - Medikamentöse Therapie", 13));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_MIXED_INSULIN_TDD), trial, "02 - Medikamentöse Therapie", 14));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_OAD_METFORMIN), trial, "02 - Medikamentöse Therapie", 15));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_OAD_METFORMIN_TDD), trial, "02 - Medikamentöse Therapie", 16));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_OAD_OTHER), trial, "02 - Medikamentöse Therapie", 17));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_OTHER_MEDICATION), trial, "02 - Medikamentöse Therapie", 18));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_DIABETES_SINCE), trial, "03 - Sonstiges", 1));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_HBA1C_PERCENT), trial, "03 - Sonstiges", 2));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_HBA1C_MMOLPERMOL), trial, "03 - Sonstiges", 3));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_C_PEPTIDE), trial, "03 - Sonstiges", 4));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_NOTE), trial, "03 - Sonstiges", 5));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_RECRUITED_FROM), trial, "03 - Sonstiges", 6));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_SUBJECT_FILE), trial, "03 - Sonstiges", 7));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_DISTANCE), trial, "03 - Sonstiges", 8));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_UPDATED), trial, "04 - Aktualisierung der Daten", 1));
		inquiries.add(getTestDataProvider().createInquiry(getInputField(InputFields.CST_UPDATED_DATE), trial, "04 - Aktualisierung der Daten", 2));
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
				case DEPARTMENT_PROBANDS:
					newCriteria.setModule(DBModule.PROBAND_DB);
					return getTestDataProvider().createCriteria(newCriteria,
							new ArrayList<SearchCriterion>() {

								{
									add(new SearchCriterion(null, "proband.department.id", CriterionRestriction.EQ, department.getId()));
								}
							});
				case DISEASE:
					newCriteria.setModule(DBModule.PROBAND_DB);
					return getTestDataProvider().createCriteria(newCriteria,
							new ArrayList<SearchCriterion>() {

								{
									InputFieldOutVO field = getInputField(InputFields.CST_UNDERLYING_DISEASE);
									InputFieldSelectionSetValue value = getTestDataProvider().getInputFieldValue(field.getId(),
											InputFieldValues.CST_DIABETES_TYPE_2);
									//add(new SearchCriterion(null, "proband.inquiryValues.inquiry.field.id", CriterionRestriction.EQ, field1.getId()));
									InquiryOutVO inquiry = getTestDataProvider().getInquiry(trial, "02 - Medikamentöse Therapie", 1);
									add(new SearchCriterion(null, "proband.inquiryValues.inquiry.id", CriterionRestriction.EQ, inquiry.getId()));
									add(new SearchCriterion(CriterionTie.AND, "proband.inquiryValues.value.selectionValues.id", CriterionRestriction.EQ, value.getId()));
								}
							});
				case DISEASE_THERAPY:
					newCriteria.setModule(DBModule.PROBAND_DB);
					return getTestDataProvider().createCriteria(newCriteria,
							new ArrayList<SearchCriterion>() {

								{
									InputFieldOutVO field = getInputField(InputFields.CST_UNDERLYING_DISEASE);
									InputFieldSelectionSetValue value = getTestDataProvider().getInputFieldValue(field.getId(),
											InputFieldValues.CST_DIABETES_TYPE_2);
									//add(new SearchCriterion(null, "proband.inquiryValues.inquiry.field.id", CriterionRestriction.EQ, field1.getId()));
									InquiryOutVO inquiry = getTestDataProvider().getInquiry(trial, "02 - Medikamentöse Therapie", 1);
									add(new SearchCriterion(null, "proband.inquiryValues.inquiry.id", CriterionRestriction.EQ, inquiry.getId()));
									add(new SearchCriterion(CriterionTie.AND, "proband.inquiryValues.value.selectionValues.id", CriterionRestriction.EQ, value.getId()));
									field = getInputField(InputFields.CST_DIABETES_THERAPY);
									value = getTestDataProvider().getInputFieldValue(field.getId(), InputFieldValues.CST_MDI_IIT_OAD);
									inquiry = getTestDataProvider().getInquiry(trial, "02 - Medikamentöse Therapie", 2);
									add(new SearchCriterion(CriterionTie.INTERSECT));
									add(new SearchCriterion(null, "proband.inquiryValues.inquiry.id", CriterionRestriction.EQ, inquiry.getId()));
									add(new SearchCriterion(CriterionTie.AND, "proband.inquiryValues.value.selectionValues.id", CriterionRestriction.EQ, value.getId()));
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
