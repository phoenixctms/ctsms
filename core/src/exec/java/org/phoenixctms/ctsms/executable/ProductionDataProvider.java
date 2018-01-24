package org.phoenixctms.ctsms.executable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.phoenixctms.ctsms.domain.*;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HolidayBaseDate;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.TimelineEventTitlePresetType;
import org.phoenixctms.ctsms.enumeration.Weekday;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.TableSizes;
import org.phoenixctms.ctsms.util.ChunkedRemoveAll;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductionDataProvider {

	private static final String STUDENT_STAFF_CATEGORY_NAME_L10N_KEY = "student";
	private static final String CLERK_STAFF_CATEGORY_NAME_L10N_KEY = "clerk";
	private static final String NURSE_STAFF_CATEGORY_NAME_L10N_KEY = "nurse";
	private static final String EXTERNAL_STAFF_CATEGORY_NAME_L10N_KEY = "external";
	private static final String CONTRACTOR_STAFF_CATEGORY_NAME_L10N_KEY = "contractor";
	private static final String CRA_STAFF_CATEGORY_NAME_L10N_KEY = "cra";
	private static final String SCIENTIST_STAFF_CATEGORY_NAME_L10N_KEY = "scientist";
	private static final String AUDITOR_STAFF_CATEGORY_NAME_L10N_KEY = "auditor";
	private static final String INVESTIGATOR_STAFF_CATEGORY_NAME_L10N_KEY = "investigator";
	private static final String MANAGER_STAFF_CATEGORY_NAME_L10N_KEY = "manager";
	private static final String SPONSOR_STAFF_CATEGORY_NAME_L10N_KEY = "sponsor";
	private static final String VENDOR_STAFF_CATEGORY_NAME_L10N_KEY = "vendor";
	private static final String DIVISION_STAFF_CATEGORY_NAME_L10N_KEY = "division";
	private static final String INSTITUTION_STAFF_CATEGORY_NAME_L10N_KEY = "institution";
	private static final String PHYSICIAN_STAFF_CATEGORY_NAME_L10N_KEY = "physician";
	private static final String COORDINATOR_STAFF_CATEGORY_NAME_L10N_KEY = "coordinator";
	private static final String PERSONNEL_STAFF_CATEGORY_NAME_L10N_KEY = "personnel";
	private static final String PERSON_STAFF_CATEGORY_NAME_L10N_KEY = "person";
	private static final String GRADUAND_STAFF_CATEGORY_NAME_L10N_KEY = "graduand";
	private static final String ORDINATION_STAFF_CATEGORY_NAME_L10N_KEY = "ordination";
	private static final ArrayList<String> ALL_STAFF_CATEGORY_NAME_L10N_KEYS = new ArrayList<String>();
	static {
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(STUDENT_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(CLERK_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(NURSE_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(EXTERNAL_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(CONTRACTOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(CRA_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(SCIENTIST_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(AUDITOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(INVESTIGATOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(MANAGER_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(SPONSOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(VENDOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(ORDINATION_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(DIVISION_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(INSTITUTION_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(PHYSICIAN_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(COORDINATOR_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(PERSONNEL_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(PERSON_STAFF_CATEGORY_NAME_L10N_KEY);
		ALL_STAFF_CATEGORY_NAME_L10N_KEYS.add(GRADUAND_STAFF_CATEGORY_NAME_L10N_KEY);
	}
	@Autowired
	protected DepartmentDao departmentDao;
	@Autowired
	protected NotificationRecipientDao notificationRecipientDao;
	@Autowired
	protected NotificationDao notificationDao;
	@Autowired
	protected NotificationTypeDao notificationTypeDao;
	@Autowired
	protected AnnouncementDao announcementDao;
	@Autowired
	protected InventoryDao inventoryDao;
	@Autowired
	protected InventoryCategoryDao inventoryCategoryDao;
	@Autowired
	protected InventoryStatusEntryDao inventoryStatusEntryDao;
	@Autowired
	protected InventoryStatusTypeDao inventoryStatusTypeDao;
	@Autowired
	protected InventoryTagValueDao inventoryTagValueDao;
	@Autowired
	protected InventoryTagDao inventoryTagDao;
	@Autowired
	protected MaintenanceScheduleItemDao maintenanceScheduleItemDao;
	@Autowired
	protected MaintenanceTypeDao maintenanceTypeDao;
	@Autowired
	protected InventoryBookingDao inventoryBookingDao;
	@Autowired
	protected StaffDao staffDao;
	@Autowired
	protected PersonContactParticularsDao personContactParticularsDao;
	@Autowired
	protected OrganisationContactParticularsDao organisationContactParticularsDao;
	@Autowired
	protected StaffCategoryDao staffCategoryDao;
	@Autowired
	protected StaffTagDao staffTagDao;
	@Autowired
	protected StaffTagValueDao staffTagValueDao;
	@Autowired
	protected StaffContactDetailValueDao staffContactDetailValueDao;
	@Autowired
	protected StaffStatusEntryDao staffStatusEntryDao;
	@Autowired
	protected StaffStatusTypeDao staffStatusTypeDao;
	@Autowired
	protected StaffAddressDao staffAddressDao;
	@Autowired
	protected CvPositionDao cvPositionDao;
	@Autowired
	protected CvSectionDao cvSectionDao;
	@Autowired
	protected CourseParticipationStatusEntryDao courseParticipationStatusEntryDao;
	@Autowired
	protected CourseParticipationStatusTypeDao courseParticipationStatusTypeDao;
	@Autowired
	protected DutyRosterTurnDao dutyRosterTurnDao;
	@Autowired
	protected CourseDao courseDao;
	@Autowired
	protected CourseCategoryDao courseCategoryDao;
	@Autowired
	protected LecturerDao lecturerDao;
	@Autowired
	protected LecturerCompetenceDao lecturerCompetenceDao;
	@Autowired
	protected ContactDetailTypeDao contactDetailTypeDao;
	@Autowired
	protected AddressTypeDao addressTypeDao;
	@Autowired
	protected TitleDao titleDao;
	@Autowired
	protected CountryDao countryDao;
	@Autowired
	protected ZipDao zipDao;
	@Autowired
	protected BankIdentificationDao bankIdentificationDao;
	@Autowired
	protected StreetDao streetDao;
	@Autowired
	protected HolidayDao holidayDao;
	@Autowired
	protected SignatureDao signatureDao;
	@Autowired
	protected TrialDao trialDao;
	@Autowired
	protected TrialTagValueDao trialTagValueDao;
	@Autowired
	protected TrialTagDao trialTagDao;
	@Autowired
	protected TeamMemberDao teamMemberDao;
	@Autowired
	protected TeamMemberRoleDao teamMemberRoleDao;
	@Autowired
	protected TimelineEventDao timelineEventDao;
	@Autowired
	protected TimelineEventTypeDao timelineEventTypeDao;
	@Autowired
	protected TrialStatusTypeDao trialStatusTypeDao;
	@Autowired
	protected TrialStatusActionDao trialStatusActionDao;
	@Autowired
	protected ProbandGroupDao probandGroupDao;
	@Autowired
	protected VisitDao visitDao;
	@Autowired
	protected VisitTypeDao visitTypeDao;
	@Autowired
	protected VisitScheduleItemDao visitScheduleItemDao;
	@Autowired
	protected ProbandListEntryDao probandListEntryDao;
	@Autowired
	protected ProbandListStatusTypeDao probandListStatusTypeDao;
	@Autowired
	protected ProbandListStatusEntryDao probandListStatusEntryDao;
	@Autowired
	protected ProbandListStatusLogLevelDao probandListStatusLogLevelDao;
	@Autowired
	protected ProbandListEntryTagValueDao probandListEntryTagValueDao;
	@Autowired
	protected ProbandListEntryTagDao probandListEntryTagDao;
	@Autowired
	protected InquiryDao inquiryDao;
	@Autowired
	protected InputFieldDao inputFieldDao;
	@Autowired
	protected InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	@Autowired
	protected InputFieldValueDao inputFieldValueDao;
	@Autowired
	protected ProbandDao probandDao;
	@Autowired
	protected ProbandCategoryDao probandCategoryDao;
	@Autowired
	protected ProbandContactParticularsDao probandContactParticularsDao;
	@Autowired
	protected ProbandTagDao probandTagDao;
	@Autowired
	protected ProbandTagValueDao probandTagValueDao;
	@Autowired
	protected ProbandStatusEntryDao probandStatusEntryDao;
	@Autowired
	protected ProbandStatusTypeDao probandStatusTypeDao;
	@Autowired
	protected ProbandContactDetailValueDao probandContactDetailValueDao;
	@Autowired
	protected ProbandAddressDao probandAddressDao;
	@Autowired
	protected BankAccountDao bankAccountDao;
	@Autowired
	protected MoneyTransferDao moneyTransferDao;
	@Autowired
	protected InquiryValueDao inquiryValueDao;
	@Autowired
	protected PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao;
	@Autowired
	protected DiagnosisDao diagnosisDao;
	@Autowired
	protected ProcedureDao procedureDao;
	@Autowired
	protected UserDao userDao;
	@Autowired
	protected KeyPairDao keyPairDao;
	@Autowired
	protected PasswordDao passwordDao;
	@Autowired
	protected CriteriaDao criteriaDao;
	@Autowired
	protected CriterionDao criterionDao;
	@Autowired
	protected CriterionPropertyDao criterionPropertyDao;
	@Autowired
	protected CriterionRestrictionDao criterionRestrictionDao;
	@Autowired
	protected CriterionTieDao criterionTieDao;
	@Autowired
	protected HyperlinkCategoryDao hyperlinkCategoryDao;
	@Autowired
	protected HyperlinkDao hyperlinkDao;
	@Autowired
	protected JournalCategoryDao journalCategoryDao;
	@Autowired
	protected JournalEntryDao journalEntryDao;
	@Autowired
	protected FileDao fileDao;
	@Autowired
	protected FileFolderPresetDao fileFolderPresetDao;
	@Autowired
	protected MimeTypeDao mimeTypeDao;
	@Autowired
	protected ErrorDao errorDao;
	@Autowired
	protected UserPermissionProfileDao userPermissionProfileDao;
	@Autowired
	protected ProfilePermissionDao profilePermissionDao;
	@Autowired
	protected PermissionDao permissionDao;
	@Autowired
	protected AlphaIdDao alphaIdDao;
	@Autowired
	protected OpsCodeDao opsCodeDao;
	@Autowired
	protected IcdSystDao icdSystDao;
	@Autowired
	protected IcdSystBlockDao icdSystBlockDao;
	@Autowired
	protected IcdSystCategoryDao icdSystCategoryDao;
	private JobOutput jobOutput;
	@Autowired
	protected OpsSystDao opsSystDao;
	@Autowired
	protected OpsSystBlockDao opsSystBlockDao;
	@Autowired
	protected OpsSystCategoryDao opsSystCategoryDao;
	@Autowired
	protected IcdSystModifierDao icdSystModifierDao;
	@Autowired
	protected OpsSystModifierDao opsSystModifierDao;
	@Autowired
	protected TrialTypeDao trialTypeDao;
	@Autowired
	protected SponsoringTypeDao sponsoringTypeDao;
	@Autowired
	protected SurveyStatusTypeDao surveyStatusTypeDao;

	@Autowired
	protected ECRFDao eCRFDao; // varnames must match bean ids in applicationContext.xml
	@Autowired
	protected ECRFFieldDao eCRFFieldDao;
	@Autowired
	protected ECRFFieldValueDao eCRFFieldValueDao;
	@Autowired
	protected ECRFStatusActionDao eCRFStatusActionDao;
	@Autowired
	protected ECRFStatusEntryDao eCRFStatusEntryDao;
	@Autowired
	protected ECRFStatusTypeDao eCRFStatusTypeDao;
	@Autowired
	protected ECRFFieldStatusEntryDao eCRFFieldStatusEntryDao;
	@Autowired
	protected ECRFFieldStatusTypeDao eCRFFieldStatusTypeDao;

	public void clearDB() throws Exception {
		ChunkedRemoveAll.remove(hyperlinkDao);
		ChunkedRemoveAll.remove(hyperlinkCategoryDao);
		jobOutput.println("hyperlink tables cleared");
		ChunkedRemoveAll.remove(journalEntryDao);
		ChunkedRemoveAll.remove(journalCategoryDao);
		jobOutput.println("journal tables cleared");
		ChunkedRemoveAll.remove(signatureDao);
		jobOutput.println("digital signature table cleared");
		ChunkedRemoveAll.remove(criterionDao);
		ChunkedRemoveAll.remove(criteriaDao);
		ChunkedRemoveAll.remove(criterionPropertyDao);
		ChunkedRemoveAll.remove(criterionTieDao);
		ChunkedRemoveAll.remove(criterionRestrictionDao);
		jobOutput.println("search related tables cleared");
		ChunkedRemoveAll.remove(fileDao);
		ChunkedRemoveAll.remove(fileFolderPresetDao);
		jobOutput.println("file attachment tables cleared");
		ChunkedRemoveAll.remove(holidayDao);
		jobOutput.println("holiday table cleared");
		ChunkedRemoveAll.remove(announcementDao);
		jobOutput.println("announcement table cleared");
		ChunkedRemoveAll.remove(notificationRecipientDao);
		ChunkedRemoveAll.remove(notificationDao);
		ChunkedRemoveAll.remove(notificationTypeDao);
		jobOutput.println("notification tables cleared");
		ChunkedRemoveAll.remove(inventoryStatusEntryDao);
		ChunkedRemoveAll.remove(inventoryStatusTypeDao);
		ChunkedRemoveAll.remove(inventoryTagValueDao);
		ChunkedRemoveAll.remove(inventoryTagDao);
		ChunkedRemoveAll.remove(maintenanceScheduleItemDao);
		ChunkedRemoveAll.remove(maintenanceTypeDao);
		ChunkedRemoveAll.remove(inventoryBookingDao);
		// unlinkInventoryParent();
		ChunkedRemoveAll.remove(inventoryDao);
		ChunkedRemoveAll.remove(inventoryCategoryDao);
		jobOutput.println("inventory db tables cleared");
		ChunkedRemoveAll.remove(staffTagValueDao);
		ChunkedRemoveAll.remove(staffTagDao);
		ChunkedRemoveAll.remove(staffContactDetailValueDao);
		ChunkedRemoveAll.remove(staffStatusEntryDao);
		ChunkedRemoveAll.remove(staffStatusTypeDao);
		ChunkedRemoveAll.remove(staffAddressDao);
		ChunkedRemoveAll.remove(cvPositionDao);
		ChunkedRemoveAll.remove(courseParticipationStatusEntryDao);
		ChunkedRemoveAll.remove(courseParticipationStatusTypeDao);
		jobOutput.println("staff db tables cleared (1)");
		ChunkedRemoveAll.remove(lecturerDao);
		ChunkedRemoveAll.remove(lecturerCompetenceDao);
		// unlinkCourseRenewals();
		// unlinkCoursePrecedingCourses();
		ChunkedRemoveAll.remove(courseDao);
		ChunkedRemoveAll.remove(cvSectionDao);
		ChunkedRemoveAll.remove(courseCategoryDao);
		jobOutput.println("course db tables cleared");
		ChunkedRemoveAll.remove(trialTagValueDao);
		ChunkedRemoveAll.remove(trialTagDao);
		ChunkedRemoveAll.remove(teamMemberDao);
		ChunkedRemoveAll.remove(teamMemberRoleDao);
		ChunkedRemoveAll.remove(timelineEventDao);
		ChunkedRemoveAll.remove(timelineEventTypeDao);
		ChunkedRemoveAll.remove(eCRFFieldValueDao);
		ChunkedRemoveAll.remove(eCRFFieldStatusEntryDao);
		ChunkedRemoveAll.remove(eCRFFieldDao);
		ChunkedRemoveAll.remove(eCRFStatusEntryDao);
		ChunkedRemoveAll.remove(eCRFDao);
		unlinkProbandListEntryLastStatus();
		ChunkedRemoveAll.remove(probandListStatusEntryDao);
		ChunkedRemoveAll.remove(probandListStatusTypeDao);
		ChunkedRemoveAll.remove(probandListStatusLogLevelDao);
		ChunkedRemoveAll.remove(probandListEntryTagValueDao);
		ChunkedRemoveAll.remove(probandListEntryTagDao);
		ChunkedRemoveAll.remove(probandListEntryDao);
		ChunkedRemoveAll.remove(moneyTransferDao);
		ChunkedRemoveAll.remove(dutyRosterTurnDao);
		ChunkedRemoveAll.remove(visitScheduleItemDao);
		ChunkedRemoveAll.remove(probandGroupDao);
		ChunkedRemoveAll.remove(visitDao);
		ChunkedRemoveAll.remove(visitTypeDao);
		ChunkedRemoveAll.remove(inquiryValueDao);
		ChunkedRemoveAll.remove(inquiryDao);
		ChunkedRemoveAll.remove(trialDao);
		ChunkedRemoveAll.remove(trialStatusTypeDao);
		ChunkedRemoveAll.remove(trialStatusActionDao);
		ChunkedRemoveAll.remove(trialTypeDao);
		ChunkedRemoveAll.remove(sponsoringTypeDao);
		ChunkedRemoveAll.remove(surveyStatusTypeDao);
		ChunkedRemoveAll.remove(eCRFStatusTypeDao);
		ChunkedRemoveAll.remove(eCRFStatusActionDao);
		ChunkedRemoveAll.remove(eCRFFieldStatusTypeDao);
		jobOutput.println("trial db tables cleared");
		ChunkedRemoveAll.remove(inputFieldValueDao);
		ChunkedRemoveAll.remove(inputFieldSelectionSetValueDao);
		ChunkedRemoveAll.remove(inputFieldDao);
		jobOutput.println("input field tables cleared");
		ChunkedRemoveAll.remove(probandTagValueDao);
		ChunkedRemoveAll.remove(probandTagDao);
		ChunkedRemoveAll.remove(probandStatusEntryDao);
		ChunkedRemoveAll.remove(probandStatusTypeDao);
		ChunkedRemoveAll.remove(diagnosisDao);
		ChunkedRemoveAll.remove(procedureDao);
		ChunkedRemoveAll.remove(probandContactDetailValueDao);
		ChunkedRemoveAll.remove(probandAddressDao);
		ChunkedRemoveAll.remove(bankAccountDao);
		ChunkedRemoveAll.remove(probandDao);
		ChunkedRemoveAll.remove(probandContactParticularsDao);
		ChunkedRemoveAll.remove(probandCategoryDao);
		ChunkedRemoveAll.remove(privacyConsentStatusTypeDao);
		jobOutput.println("proband db tables cleared");
		unlinkUserIdentity();
		// unlinkStaffParent();
		ChunkedRemoveAll.remove(staffDao);
		ChunkedRemoveAll.remove(personContactParticularsDao);
		ChunkedRemoveAll.remove(organisationContactParticularsDao);
		ChunkedRemoveAll.remove(staffCategoryDao);
		jobOutput.println("staff db tables cleared (2)");
		ChunkedRemoveAll.remove(passwordDao);
		ChunkedRemoveAll.remove(userPermissionProfileDao);
		ChunkedRemoveAll.remove(userDao);
		ChunkedRemoveAll.remove(keyPairDao);
		jobOutput.println("user db tables cleared");
		ChunkedRemoveAll.remove(profilePermissionDao);
		ChunkedRemoveAll.remove(permissionDao);
		jobOutput.println("permission db tables cleared");
		ChunkedRemoveAll.remove(contactDetailTypeDao);
		ChunkedRemoveAll.remove(addressTypeDao);
		ChunkedRemoveAll.remove(departmentDao);
		jobOutput.println("shared db tables cleared");
		ChunkedRemoveAll.remove(titleDao);
		ChunkedRemoveAll.remove(streetDao, TableSizes.BIG);
		ChunkedRemoveAll.remove(zipDao);
		ChunkedRemoveAll.remove(countryDao);
		ChunkedRemoveAll.remove(bankIdentificationDao);
		ChunkedRemoveAll.remove(alphaIdDao);
		ChunkedRemoveAll.remove(icdSystModifierDao);
		ChunkedRemoveAll.remove(icdSystBlockDao);
		ChunkedRemoveAll.remove(icdSystCategoryDao);
		ChunkedRemoveAll.remove(icdSystDao);
		ChunkedRemoveAll.remove(opsCodeDao);
		ChunkedRemoveAll.remove(opsSystModifierDao);
		ChunkedRemoveAll.remove(opsSystBlockDao);
		ChunkedRemoveAll.remove(opsSystCategoryDao);
		ChunkedRemoveAll.remove(opsSystDao);
		ChunkedRemoveAll.remove(mimeTypeDao);
		jobOutput.println("autocomplete db tables cleared");
		ChunkedRemoveAll.remove(errorDao);
		jobOutput.println("error table cleared");
	}

	private AddressType createAddressType(String nameL10nKey, Integer maxOccurrence, boolean deliverPreset, boolean staff, boolean proband, boolean animal) {
		AddressType addressType = AddressType.Factory.newInstance();
		addressType.setNameL10nKey(nameL10nKey);
		addressType.setMaxOccurrence(maxOccurrence);
		addressType.setDeliverPreset(deliverPreset);
		addressType.setStaff(staff);
		addressType.setProband(proband);
		addressType.setAnimal(animal);
		addressType = addressTypeDao.create(addressType);
		return addressType;
	}

	// public void clearCriteriaTables() throws Exception {
	//
	// journalEntryDao.remove(journalEntryDao.search(new Search(new SearchParameter[] {
	// new SearchParameter("criteria.id",SearchParameter.NOT_NULL_COMPARATOR) })));
	//
	// ChunckedRemoveAll.remove(criterionDao);
	// ChunckedRemoveAll.remove(criteriaDao);
	// ChunckedRemoveAll.remove(criterionPropertyDao);
	// ChunckedRemoveAll.remove(criterionTieDao);
	// ChunckedRemoveAll.remove(criterionRestrictionDao);
	// jobOutput.println("search related tables cleared");
	//
	// }
	private void createAddressTypes() {
		createAddressType("business", null, false, true, true, false);
		createAddressType("home", null, true, true, true, false);
		createAddressType("other", null, true, true, true, false);
		createAddressType("term_time", null, true, true, false, false);
		createAddressType("location", null, true, false, false, true);
		jobOutput.println("address types created");
	}

	private ContactDetailType createContactDetailType(String nameL10nKey, boolean email, boolean phone, boolean url, boolean notifyPreset, Integer maxOccurrence, String regExp,
			String mismatchMsgL10nKey, boolean staff, boolean proband, boolean animal) {
		ContactDetailType type = ContactDetailType.Factory.newInstance();
		type.setNameL10nKey(nameL10nKey);
		type.setEmail(email);
		type.setPhone(phone);
		type.setUrl(url);
		type.setNotifyPreset(notifyPreset);
		type.setMaxOccurrence(maxOccurrence);
		type.setRegExp(regExp);
		type.setMismatchMsgL10nKey(mismatchMsgL10nKey);
		type.setStaff(staff);
		type.setProband(proband);
		type.setAnimal(animal);
		type = contactDetailTypeDao.create(type);
		return type;
	}

	private void createContactDetailTypes() {
		String urlRegExp = "^([hH][tT][tT][pP][sS]?|[fF][tT][pP])://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+([/?].+)?$"; // http://answers.oreilly.com/topic/280-how-to-validate-urls-with-regular-expressions/
		String emailRegExp = "^[\\w-]+(\\.[\\w-]+)*@([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*?\\.[a-zA-Z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$"; // http://regexlib.com/UserPatterns.aspx?authorId=2c58598d-b6ac-4952-9a6a-bf2e6ae7dddc
		String phoneNumberRegExp = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$"; // http://stackoverflow.com/questions/2113908/what-regular-expression-will-match-valid-international-phone-numbers
		// String phoneNumberRegExp =
		// "^+(999|998|997|996|995|994|993|992|991|990|979|978|977|976|975|974|973|972|971|970|969|968|967|966|965|964|963|962|961|960|899|898|897|896|895|894|893|892|891|890|889|888|887|886|885|884|883|882|881|880|879|878|877|876|875|874|873|872|871|870|859|858|857|856|855|854|853|852|851|850|839|838|837|836|835|834|833|832|831|830|809|808|807|806|805|804|803|802|801|800|699|698|697|696|695|694|693|692|691|690|689|688|687|686|685|684|683|682|681|680|679|678|677|676|675|674|673|672|671|670|599|598|597|596|595|594|593|592|591|590|509|508|507|506|505|504|503|502|501|500|429|428|427|426|425|424|423|422|421|420|389|388|387|386|385|384|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|296|295|294|293|292|291|290|289|288|287|286|285|284|283|282|281|280|269|268|267|266|265|264|263|262|261|260|259|258|257|256|255|254|253|252|251|250|249|248|247|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|219|218|217|216|215|214|213|212|211|210|98|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1)[0-9]{0,14}$";
		createContactDetailType("business_phone_number", false, true, false, false, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, true, true, false);
		createContactDetailType("office_phone_number", false, true, false, false, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, true, false, false);
		createContactDetailType("home_phone_number", false, true, false, false, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, true, true, false);
		createContactDetailType("mobile_phone_number", false, true, false, true, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, true, true, false);
		createContactDetailType("fax_number", false, false, false, false, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, true, true, false);
		createContactDetailType("business_email_address", true, false, false, false, null, emailRegExp, ServiceExceptionCodes.INVALID_EMAIL_ADDRESS, true, true, false);
		createContactDetailType("other_email_address", true, false, false, true, null, emailRegExp, ServiceExceptionCodes.INVALID_EMAIL_ADDRESS, true, true, false);
		createContactDetailType("web_page_url", false, false, true, false, null, urlRegExp, ServiceExceptionCodes.INVALID_URL, true, true, false);
		createContactDetailType("care_phone_number", false, true, false, false, null, phoneNumberRegExp, ServiceExceptionCodes.INVALID_PHONE_NUMBER, false, false, true);
		// createContactDetailType("skype_user_name",false,false,false,false,null,null,null,true,true);
		jobOutput.println("contact detail types created");
	}

	private void createCourseCategories() {
		createCourseCategory("gcp", Color.LEMONCHIFFON, true, false, "ctsms-coursecategory-gcp");
		createCourseCategory("study_specific", Color.MOCCASIN, true, true, "ctsms-coursecategory-study-specific");
		createCourseCategory("continuing_education", Color.LIGHTYELLOW, true, false, "ctsms-coursecategory-continuing-education");
		createCourseCategory("first_aid", Color.PALEGOLDENROD, true, false, "ctsms-coursecategory-first-aid");
		jobOutput.println("course categories created");
	}

	private CourseCategory createCourseCategory(String nameL10nKey, Color color, boolean visible, boolean trialRequired, String nodeStyleClass) {
		CourseCategory courseCategory = CourseCategory.Factory.newInstance();
		courseCategory.setNameL10nKey(nameL10nKey);
		courseCategory.setColor(color);
		courseCategory.setVisible(visible);
		courseCategory.setNodeStyleClass(nodeStyleClass);
		courseCategory.setTrialRequired(trialRequired);
		courseCategory = courseCategoryDao.create(courseCategory);
		return courseCategory;
	}

	private CourseParticipationStatusType createCourseParticipationStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			boolean userInitial,
			boolean userSelfRegistrationInitial,
			boolean adminInitial,
			boolean adminSelfRegistrationInitial,
			boolean acknowledge,
			boolean cancel,
			boolean pass,
			boolean relevantForCourseAppointments,
			boolean notify) {
		CourseParticipationStatusType courseParticipationType = CourseParticipationStatusType.Factory.newInstance();
		courseParticipationType.setNameL10nKey(nameL10nKey);
		courseParticipationType.setColor(color);
		courseParticipationType.setUserInitial(userInitial);
		courseParticipationType.setUserSelfRegistrationInitial(userSelfRegistrationInitial);
		courseParticipationType.setAdminInitial(adminInitial);
		courseParticipationType.setAdminSelfRegistrationInitial(adminSelfRegistrationInitial);
		courseParticipationType.setAcknowledge(acknowledge);
		courseParticipationType.setCancel(cancel);
		courseParticipationType.setPass(pass);
		courseParticipationType.setRelevantForCourseAppointments(relevantForCourseAppointments);
		courseParticipationType.setNotify(notify);
		courseParticipationType = courseParticipationStatusTypeDao.create(courseParticipationType);
		return courseParticipationType;
	}

	private void createCourseParticipationStatusTypeEntries() {
		CourseParticipationStatusType passedParticipationStatusType = createCourseParticipationStatusType("passed", Color.LIGHTGREEN,
				false, false, false, false,
				false, false, true, false,
				true
				);
		CourseParticipationStatusType failedParticipationStatusType = createCourseParticipationStatusType("failed", Color.RED,
				false, false, false, false,
				false, false, false, false,
				true
				);
		CourseParticipationStatusType cancelledParticipationStatusType = createCourseParticipationStatusType("cancelled", Color.DARKORANGE,
				false, false, false, false,
				false, true, false, false,
				true
				);
		CourseParticipationStatusType unregisteredParticipationStatusType = createCourseParticipationStatusType("unregistered", Color.LIGHTGREY,
				false, false, false, false,
				false, false, false, false,
				true
				);
		CourseParticipationStatusType absentParticipationStatusType = createCourseParticipationStatusType("absent", Color.DARKORANGE,
				false, false, false, false,
				false, false, false, false,
				true
				);
		CourseParticipationStatusType acknowledgedParticipationStatusType = createCourseParticipationStatusType("acknowledged", Color.ORANGE,
				false, true, false, false,
				true, false, false, true,
				true
				);
		CourseParticipationStatusType invitedParticipationStatusType = createCourseParticipationStatusType("invited", Color.YELLOW,
				false, false, false, true,
				false, false, false, true,
				true
				);
		CourseParticipationStatusType registeredParticipationStatusType = createCourseParticipationStatusType("registered", Color.ORANGE,
				false, false, true, false,
				false, false, false, true,
				true
				);
		updateCourseParticipationStatusType(
				passedParticipationStatusType,
				getCourseParticipationTransitions(passedParticipationStatusType),
				getCourseParticipationTransitions(passedParticipationStatusType),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		updateCourseParticipationStatusType(
				failedParticipationStatusType,
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		updateCourseParticipationStatusType(
				cancelledParticipationStatusType,
				getCourseParticipationTransitions(cancelledParticipationStatusType, registeredParticipationStatusType, acknowledgedParticipationStatusType),
				getCourseParticipationTransitions(cancelledParticipationStatusType, invitedParticipationStatusType, acknowledgedParticipationStatusType),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		updateCourseParticipationStatusType(
				unregisteredParticipationStatusType,
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		updateCourseParticipationStatusType(
				absentParticipationStatusType,
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		updateCourseParticipationStatusType(
				acknowledgedParticipationStatusType,
				getCourseParticipationTransitions(cancelledParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType),
				getCourseParticipationTransitions(cancelledParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // passedParticipationStatusType,unregisteredParticipationStatusType,absentParticipationStatusType)
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // passedParticipationStatusType,unregisteredParticipationStatusType),
				);
		updateCourseParticipationStatusType(
				invitedParticipationStatusType,
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(invitedParticipationStatusType, acknowledgedParticipationStatusType, cancelledParticipationStatusType),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // ()
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // passedParticipationStatusType,unregisteredParticipationStatusType),
				);
		updateCourseParticipationStatusType(
				registeredParticipationStatusType,
				getCourseParticipationTransitions(registeredParticipationStatusType, acknowledgedParticipationStatusType, cancelledParticipationStatusType),
				getCourseParticipationTransitions(),
				getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
						unregisteredParticipationStatusType, absentParticipationStatusType, acknowledgedParticipationStatusType, registeredParticipationStatusType), // passedParticipationStatusType,unregisteredParticipationStatusType,absentParticipationStatusType)
						getCourseParticipationTransitions(passedParticipationStatusType, failedParticipationStatusType, cancelledParticipationStatusType,
								unregisteredParticipationStatusType, acknowledgedParticipationStatusType, invitedParticipationStatusType) // ()
				);
		jobOutput.println("course participation states created");
	}

	private CriterionRestriction createCriterionRestriction(org.phoenixctms.ctsms.enumeration.CriterionRestriction r) {
		CriterionRestriction restriction = CriterionRestriction.Factory.newInstance();
		restriction.setNameL10nKey(r.name());
		restriction.setRestriction(r);
		restriction = criterionRestrictionDao.create(restriction);
		return restriction;
	}

	private void createCriterionRestrictionEntries() {
		org.phoenixctms.ctsms.enumeration.CriterionRestriction[] restrictions = org.phoenixctms.ctsms.enumeration.CriterionRestriction.values();
		for (int i = 0; i < restrictions.length; i++) {
			createCriterionRestriction(restrictions[i]);
		}
	}

	private CriterionTie createCriterionTie(org.phoenixctms.ctsms.enumeration.CriterionTie t) {
		CriterionTie tie = CriterionTie.Factory.newInstance();
		tie.setNameL10nKey(t.name());
		tie.setTie(t);
		tie = criterionTieDao.create(tie);
		return tie;
	}

	private void createCriterionTieEntries() {
		org.phoenixctms.ctsms.enumeration.CriterionTie[] ties = org.phoenixctms.ctsms.enumeration.CriterionTie.values();
		for (int i = 0; i < ties.length; i++) {
			createCriterionTie(ties[i]);
		}
	}

	private CvSection createCvSection(String nameL10nKey, String titlePresetL10nKey, String descriptionL10nKey, long position, boolean showCvPreset, boolean visible) {
		CvSection cvSection = CvSection.Factory.newInstance();
		cvSection.setNameL10nKey(nameL10nKey);
		cvSection.setTitlePresetL10nKey(titlePresetL10nKey);
		cvSection.setDescriptionL10nKey(descriptionL10nKey);
		cvSection.setPosition(position);
		cvSection.setShowCvPreset(showCvPreset);
		cvSection.setVisible(visible);
		cvSection = cvSectionDao.create(cvSection);
		return cvSection;
	}

	private void createCvSections() {
		createCvSection("present_position", "present_position", null, 0, true, true);
		createCvSection("relevant_education", "relevant_education", "relevant_education", 10, true, true);
		createCvSection("relevant_previous_positions", "relevant_previous_positions", "relevant_previous_positions", 20, true, true);
		// createCvSection("relevant_job_related_training","relevant_job_related_training","relevant_job_related_training",3,true,true);
		createCvSection("relevant_clinical_trial_and_research_experience", "relevant_clinical_trial_and_research_experience", null, 30, true, true);
		createCvSection("documentation_of_gcp_training_and_other_trainings", "documentation_of_gcp_training_and_other_trainings",
				"documentation_of_gcp_training_and_other_trainings", 40, true, true);
		jobOutput.println("cv sections created");
	}

	private ECRFFieldStatusType createEcrfFieldStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			ECRFFieldStatusQueue queue,
			boolean initial,
			boolean updated,
			boolean proposed,
			boolean resolved,
			boolean commentRequired,
			boolean unlockValue) {
		return createEcrfFieldStatusType(nameL10nKey, color, queue, initial, updated, proposed, resolved, false, false, false, commentRequired, unlockValue);
	}
	private ECRFFieldStatusType createEcrfFieldStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			ECRFFieldStatusQueue queue,
			boolean initial,
			boolean updated,
			boolean proposed,
			boolean resolved,
			boolean validationError,
			boolean validationFailed,
			boolean validationSuccess,
			boolean commentRequired,
			boolean unlockValue) {
		ECRFFieldStatusType ecrfFieldStatusType = ECRFFieldStatusType.Factory.newInstance();
		ecrfFieldStatusType.setColor(color);
		ecrfFieldStatusType.setInitial(initial);
		ecrfFieldStatusType.setQueue(queue);
		ecrfFieldStatusType.setCommentRequired(commentRequired);
		ecrfFieldStatusType.setUpdated(updated);
		ecrfFieldStatusType.setProposed(proposed);
		ecrfFieldStatusType.setResolved(resolved);
		ecrfFieldStatusType.setUnlockValue(unlockValue);
		ecrfFieldStatusType.setValidationError(validationError);
		ecrfFieldStatusType.setValidationFailed(validationFailed);
		ecrfFieldStatusType.setValidationSuccess(validationSuccess);
		ecrfFieldStatusType.setNameL10nKey(nameL10nKey);

		ecrfFieldStatusType = eCRFFieldStatusTypeDao.create(ecrfFieldStatusType);
		return ecrfFieldStatusType;
	}


	private void createEcrfFieldStatusTypeEntries() {
		// boolean initial,
		// boolean request,
		// boolean response,
		// boolean resolved,
		// boolean validationError,
		// boolean validationFailed,
		// boolean validationSuccess,
		// boolean commentRequired
		ECRFFieldStatusType annotationEcrfFieldStatusType = createEcrfFieldStatusType(
				"annotation",
				Color.GAINSBORO,
				ECRFFieldStatusQueue.ANNOTATION,
				true,
				false,
				false,
				true,
				true,
				false);

		updateEcrfFieldStatusType(annotationEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				annotationEcrfFieldStatusType
				));

		ECRFFieldStatusType validationSuccessEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_success",
				Color.LIMEGREEN,
				ECRFFieldStatusQueue.VALIDATION,
				true,
				false,
				false,
				true,
				false, false, true,
				false,
				false);
		ECRFFieldStatusType validationFailedEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_failed",
				Color.RED,
				ECRFFieldStatusQueue.VALIDATION,
				true,
				false,
				false,
				false,
				false, true, false,
				true,
				true);
		ECRFFieldStatusType validationErrorEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_error",
				Color.SALMON,
				ECRFFieldStatusQueue.VALIDATION,
				true,
				false,
				false,
				false,
				true, false, false,
				true,
				true);
		ECRFFieldStatusType validationUpdateEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_udate",
				Color.ORANGE,
				ECRFFieldStatusQueue.VALIDATION,
				false,
				true,
				false,
				false,
				true,
				true);
		// ECRFFieldStatusType validationUpdateRequestEcrfFieldStatusType = createEcrfFieldStatusType(
		// "validation_udate_request",
		// Color.ORANGE,
		// ECRFFieldStatusQueue.VALIDATION,
		// false,
		// true,
		// false,
		// false,
		// true,
		// true);
		ECRFFieldStatusType validationProposedResolutionEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_proposed_resolution",
				Color.LIGHTSKYBLUE, // Color.GOLD, // LIGHTSTEELBLUE,
				ECRFFieldStatusQueue.VALIDATION,
				false,
				false,
				true,
				false,
				true,
				false);

		ECRFFieldStatusType validationCorrectedEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_corrected",
				Color.LIMEGREEN, // PALEGREEN,
				ECRFFieldStatusQueue.VALIDATION,
				false,
				false,
				false,
				true,
				false,
				false);
		ECRFFieldStatusType validationDataNaEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_data_na",
				Color.DARKGREY, // PALEGREEN,
				ECRFFieldStatusQueue.VALIDATION,
				false,
				false,
				false,
				true,
				false,
				false);
		ECRFFieldStatusType validationClosedEcrfFieldStatusType = createEcrfFieldStatusType(
				"validation_closed",
				Color.GAINSBORO, // LIGHTGREEN,
				ECRFFieldStatusQueue.VALIDATION,
				false,
				false,
				false,
				true,
				true,
				false);
		updateEcrfFieldStatusType(validationSuccessEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationFailedEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationSuccessEcrfFieldStatusType,
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType,
				validationClosedEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationErrorEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationSuccessEcrfFieldStatusType,
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType,
				validationUpdateEcrfFieldStatusType,
				validationProposedResolutionEcrfFieldStatusType,
				validationClosedEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationUpdateEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationUpdateEcrfFieldStatusType,
				validationProposedResolutionEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationProposedResolutionEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationUpdateEcrfFieldStatusType,
				validationCorrectedEcrfFieldStatusType,
				validationDataNaEcrfFieldStatusType,
				validationClosedEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationCorrectedEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationSuccessEcrfFieldStatusType,
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationDataNaEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationSuccessEcrfFieldStatusType,
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(validationClosedEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				validationSuccessEcrfFieldStatusType,
				validationFailedEcrfFieldStatusType,
				validationErrorEcrfFieldStatusType
				));

		ECRFFieldStatusType queryNewEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_new",
				Color.SALMON,
				ECRFFieldStatusQueue.QUERY,
				true,
				false,
				false,
				false,
				true,
				true);
		ECRFFieldStatusType queryUpdateEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_udate",
				Color.ORANGE, // DARKORANGE,
				ECRFFieldStatusQueue.QUERY,
				false,
				true,
				false,
				false,
				true,
				true);
		// ECRFFieldStatusType queryUpdateRequestEcrfFieldStatusType = createEcrfFieldStatusType(
		// "query_udate_request",
		// Color.ORANGE,
		// ECRFFieldStatusQueue.QUERY,
		// false,
		// true,
		// false,
		// false,
		// true,
		// true);
		ECRFFieldStatusType queryProposedResolutionEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_proposed_resolution",
				Color.LIGHTSKYBLUE, // .GOLD, // LIGHTSTEELBLUE,
				ECRFFieldStatusQueue.QUERY,
				false,
				false,
				true,
				false,
				true,
				false);
		ECRFFieldStatusType queryCorrectedEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_corrected",
				Color.LIMEGREEN,
				ECRFFieldStatusQueue.QUERY,
				false,
				false,
				false,
				true,
				false,
				false);
		ECRFFieldStatusType queryDataNaEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_data_na",
				Color.DARKGREY,
				ECRFFieldStatusQueue.QUERY,
				false,
				false,
				false,
				true,
				false,
				false);
		ECRFFieldStatusType queryClosedEcrfFieldStatusType = createEcrfFieldStatusType(
				"query_closed",
				Color.GAINSBORO,
				ECRFFieldStatusQueue.QUERY,
				false,
				false,
				false,
				true,
				true,
				false);
		updateEcrfFieldStatusType(queryNewEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				queryUpdateEcrfFieldStatusType,
				queryProposedResolutionEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(queryUpdateEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				queryUpdateEcrfFieldStatusType,
				queryProposedResolutionEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(queryProposedResolutionEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				queryUpdateEcrfFieldStatusType,
				queryCorrectedEcrfFieldStatusType,
				queryDataNaEcrfFieldStatusType,
				queryClosedEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(queryCorrectedEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				// queryResolutionEcrfFieldStatusType,
				queryNewEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(queryDataNaEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				// queryResolutionEcrfFieldStatusType,
				queryNewEcrfFieldStatusType
				));
		updateEcrfFieldStatusType(queryClosedEcrfFieldStatusType, getEcrfFieldStatusTransitions(
				// queryResolutionEcrfFieldStatusType,
				queryNewEcrfFieldStatusType
				));
		jobOutput.println("eCRF field states created");
	}

	private ECRFStatusAction createEcrfStatusAction(org.phoenixctms.ctsms.enumeration.ECRFStatusAction a) { // , long seq) {
		ECRFStatusAction action = ECRFStatusAction.Factory.newInstance();
		action.setAction(a);
		action = eCRFStatusActionDao.create(action);
		return action;
	}

	private void createEcrfStatusActions() {
		org.phoenixctms.ctsms.enumeration.ECRFStatusAction[] actions = org.phoenixctms.ctsms.enumeration.ECRFStatusAction.values();
		for (int i = 0; i < actions.length; i++) {
			createEcrfStatusAction(actions[i]); // ,i);
		}
		jobOutput.println("eCRF status actions created");
	}

	private ECRFStatusType createEcrfStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			String nodeStyleClass,
			boolean initial,
			boolean valueLockdown,
			boolean fieldStatusLockdown,
			boolean applyEcrfProbandListStatus,
			boolean auditTrail,
			boolean reasonForChangeRequired,
			boolean validated,
			boolean review,
			boolean verified,
			boolean done,
			Set<ECRFStatusAction> actions) {
		ECRFStatusType ecrfStatusType = ECRFStatusType.Factory.newInstance();
		ecrfStatusType.setColor(color);
		ecrfStatusType.setNodeStyleClass(nodeStyleClass);
		ecrfStatusType.setInitial(initial);
		ecrfStatusType.setValueLockdown(valueLockdown);
		ecrfStatusType.setFieldStatusLockdown(fieldStatusLockdown);
		ecrfStatusType.setApplyEcrfProbandListStatus(applyEcrfProbandListStatus);
		ecrfStatusType.setAuditTrail(auditTrail);
		ecrfStatusType.setReasonForChangeRequired(reasonForChangeRequired);
		ecrfStatusType.setValidated(validated);
		ecrfStatusType.setReview(review);
		ecrfStatusType.setVerified(verified);
		ecrfStatusType.setDone(done);
		// ecrfStatusType.setIgnoreTimelineEvents(ignoreTimelineEvents);
		// ecrfStatusType.setInquiryValueInputEnabled(inquiryValueInputEnabled);
		ecrfStatusType.setNameL10nKey(nameL10nKey);
		ecrfStatusType.setActions(actions);
		ecrfStatusType = eCRFStatusTypeDao.create(ecrfStatusType);
		return ecrfStatusType;
	}

	private void createEcrfStatusTypeEntries() {

		ECRFStatusType inProgressEcrfStatusType = createEcrfStatusType("in_progress", Color.ANTIQUEWHITE,
				"ui-icon-pencil",
				true,
				false,
				false,
				false,
				false, // true, -> slow page saving
				false,
				false,
				false,
				false,
				false,
				getEcrfStatusActions(
						// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);
		ECRFStatusType skippedEcrfStatusType = createEcrfStatusType("skipped", Color.DARKGREY,
				"ui-icon-arrowthick-1-e", // "ui-icon-cancel", // "ui-icon-seek-end",
				true,
				true, // false, // true,
				true,
				false, // true,
				false,
				false,
				false,
				true, // false,
				false,
				false, // true,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CLEAR_STATUS_ENTRIES, org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CLEAR_VALUES,
						// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
						// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);
		ECRFStatusType skippedVerifiedEcrfStatusType = createEcrfStatusType(
				"skipped_verified",
				Color.LIGHTSKYBLUE, // .LIGHTSTEELBLUE,
				"ui-icon-circle-arrow-e", // "ui-icon-circle-check",
				false,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				false,
				getEcrfStatusActions(// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_UNRESOLVED_FIELD_STATUS_ENTRIES,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
				);
		ECRFStatusType skippedSignedEcrfStatusType = createEcrfStatusType("skipped_signed", Color.ORCHID,
				"ui-icon-radio-on", // "ui-icon-locked",
				false,
				true,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);
		ECRFStatusType validatedEcrfStatusType = createEcrfStatusType("validated", Color.YELLOWGREEN,
				"ui-icon-gear", // "ui-icon-check",
				false,
				true,
				false,
				false,
				true,
				false,
				true,
				false,
				false,
				false,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_MISSING_VALUES,org.phoenixctms.ctsms.enumeration.ECRFStatusAction.VALIDATE_VALUES,
						// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
				);
		ECRFStatusType completeEcrfStatusType = createEcrfStatusType(
				"complete",
				Color.LIGHTYELLOW,
				"ui-icon-check", // "ui-icon-flag",
				false,
				true,
				false,
				false,
				true,
				true,
				false,
				true,
				false,
				false,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
				);
		ECRFStatusType completeVerifiedEcrfStatusType = createEcrfStatusType(
				"complete_verified",
				Color.LIGHTSKYBLUE, // .LIGHTSTEELBLUE,
				"ui-icon-circle-check",
				false,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				false,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_UNRESOLVED_FIELD_STATUS_ENTRIES,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
				);
		ECRFStatusType completeSignedEcrfStatusType = createEcrfStatusType("complete_signed", Color.ORCHID,
				"ui-icon-bullet", // "ui-icon-locked",
				false,
				true,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);
		ECRFStatusType incompleteEcrfStatusType = createEcrfStatusType("incomplete", Color.TOMATO,
				"ui-icon-close", // "ui-icon-circle-close",
				false,
				true,
				false,
				false, // true,
				true,
				true,
				false,
				true, // false,
				false,
				false, // true,
				getEcrfStatusActions(// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
						// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);
		ECRFStatusType incompleteVerifiedEcrfStatusType = createEcrfStatusType(
				"incomplete_verified",
				Color.LIGHTSKYBLUE, // .LIGHTSTEELBLUE,
				"ui-icon-circle-close", // "ui-icon-circle-check",
				false,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				false,
				getEcrfStatusActions(// org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_UNRESOLVED_FIELD_STATUS_ENTRIES,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)
				);
		ECRFStatusType incompleteSignedEcrfStatusType = createEcrfStatusType("incomplete_signed", Color.ORCHID,
				"ui-icon-radio-off", // "ui-icon-locked",
				false,
				true,
				true,
				true,
				false,
				false,
				false,
				false,
				false,
				true,
				getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CREATE_PROBAND_LIST_STATUS_ENTRY,
						org.phoenixctms.ctsms.enumeration.ECRFStatusAction.CANCEL_NOTIFICATIONS)
				);

		updateEcrfStatusType(inProgressEcrfStatusType,
				getEcrfStatusTransitions(
						skippedEcrfStatusType,
						validatedEcrfStatusType,
						incompleteEcrfStatusType
						));

		updateEcrfStatusType(skippedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						skippedVerifiedEcrfStatusType
						));
		updateEcrfStatusType(skippedVerifiedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						skippedSignedEcrfStatusType
						));
		updateEcrfStatusType(skippedSignedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType
						));
		updateEcrfStatusType(validatedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						completeEcrfStatusType
						));
		updateEcrfStatusType(completeEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						completeVerifiedEcrfStatusType
						));
		updateEcrfStatusType(completeVerifiedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						completeSignedEcrfStatusType
						));
		updateEcrfStatusType(completeSignedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType
						));
		updateEcrfStatusType(incompleteEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						incompleteVerifiedEcrfStatusType
						));
		updateEcrfStatusType(incompleteVerifiedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType,
						incompleteSignedEcrfStatusType
						));
		updateEcrfStatusType(incompleteSignedEcrfStatusType,
				getEcrfStatusTransitions(
						inProgressEcrfStatusType
						));

		jobOutput.println("eCRF states created");
	}

	private FileFolderPreset createFileFolderPreset(FileModule module, String logicalPath) {
		FileFolderPreset fileFolderPreset = FileFolderPreset.Factory.newInstance();
		fileFolderPreset.setModule(module);
		fileFolderPreset.setLogicalPath(logicalPath);
		fileFolderPreset = fileFolderPresetDao.create(fileFolderPreset);
		return fileFolderPreset;
	}

	private void createFileFolderPresets() {
		// createFileFolderPreset(FileModule.INVENTORY_DOCUMENT,CommonUtil.fixLogicalPathFolderName("01 - Manuals"));
		// createFileFolderPreset(FileModule.INVENTORY_DOCUMENT,CommonUtil.fixLogicalPathFolderName("02 - Receipts"));
		// createFileFolderPreset(FileModule.INVENTORY_DOCUMENT,CommonUtil.fixLogicalPathFolderName("03 - Reports & Certificates"));
		// createFileFolderPreset(FileModule.INVENTORY_DOCUMENT,CommonUtil.fixLogicalPathFolderName("04 - End-Use-Certificates"));
		// createFileFolderPreset(FileModule.INVENTORY_DOCUMENT,CommonUtil.fixLogicalPathFolderName("05 - Miscellaneous"));
		createFileFolderPreset(FileModule.INVENTORY_DOCUMENT, CommonUtil.fixLogicalPathFolderName("Manuals"));
		createFileFolderPreset(FileModule.INVENTORY_DOCUMENT, CommonUtil.fixLogicalPathFolderName("Reports und Zertifikate"));
		createFileFolderPreset(FileModule.INVENTORY_DOCUMENT, CommonUtil.fixLogicalPathFolderName("Rechnungen"));
		createFileFolderPreset(FileModule.INVENTORY_DOCUMENT, CommonUtil.fixLogicalPathFolderName("Sonstige Dokumente"));
		// 1. Bewerbungsunterlagen
		// 2. Zeugnisse
		// 3. Schulungsdokumente
		// 4. Verschiedenes
		// |-> 4.1. Zeitaufzeichnung
		// |-> 4.2. Publikationen
		createFileFolderPreset(FileModule.STAFF_DOCUMENT, CommonUtil.fixLogicalPathFolderName("1. Bewerbungsunterlagen"));
		createFileFolderPreset(FileModule.STAFF_DOCUMENT, CommonUtil.fixLogicalPathFolderName("2. Zeugnisse"));
		createFileFolderPreset(FileModule.STAFF_DOCUMENT, CommonUtil.fixLogicalPathFolderName("3. Schulungsdokumente"));
		createFileFolderPreset(FileModule.STAFF_DOCUMENT, CommonUtil.fixLogicalPathFolderName("4. Verschiedenes/4.1. Zeitaufzeichnung"));
		createFileFolderPreset(FileModule.STAFF_DOCUMENT, CommonUtil.fixLogicalPathFolderName("4. Verschiedenes/4.2. Publikationen"));
		createFileFolderPreset(FileModule.COURSE_DOCUMENT, CommonUtil.fixLogicalPathFolderName("01 - Course Material"));
		createFileFolderPreset(FileModule.COURSE_DOCUMENT, CommonUtil.fixLogicalPathFolderName("02 - Participation Lists"));
		createFileFolderPreset(FileModule.COURSE_DOCUMENT, CommonUtil.fixLogicalPathFolderName("03 - Live Recordings"));
		createFileFolderPreset(FileModule.COURSE_DOCUMENT, CommonUtil.fixLogicalPathFolderName("04 - Miscellaneous"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("01 - Proband Information"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("02 - Protocol"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("03 - CRF"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("04 - Project Management"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("05 - Ethical Review Committee"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("06 - Recruitment"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("07 - Meetings"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("08 - IB"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("09 - Medication"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("10 - Checklists"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("11 - TMM"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("12 - Hotel"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("13 - Proband Journal"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("14 - Lab"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("15 - SOP"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("16 - Trial Guide"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("17 - Kitchen"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("18 - Q&A"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("19 - Medical Board"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("20 - Shipment"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("21 - Note to File"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("22 - DCF"));
		// createFileFolderPreset(FileModule.TRIAL_DOCUMENT,CommonUtil.fixLogicalPathFolderName("23 - Final Report"));
		// /1. Projektmanagement
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("1. Projektmanagement/1.1. PM Dokumente"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("1. Projektmanagement/1.2. Projektmeetingaufzeichungen"));
		// /2. Prüferinformation
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("2. Prüferinformation/2.1. IB Fachinformation"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("2. Prüferinformation/2.2. Sicherheitsinformationen"));
		// /3. Protokoll
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("3. Protokoll/3.1. Protokoll unterschrieben"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("3. Protokoll/3.2. Amendments"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("4. CRF"));
		// /5. Quelldaten
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("5. Quelldaten/5.1. Source Data Form"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("5. Quelldaten/5.2. Source Data Verification Agreement Form"));
		// /6. Ethik
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.1. Antrag und Meldungen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.2. Mitteilungen der EK"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.3. Versicherungsdokumente"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.4. Werbematerialien"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.5. Meldungen v. Nebenwirkungen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("6. Ethik/6.6. Berichte"));
		// /7. Behörden
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("7. Behörden/7.1. Meldungen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("7. Behörden/7.2. Mitteilungen der Behörden"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("7. Behörden/7.3. EudraCT Dokumentation"));
		// /8. Ärztliche Direktion
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("8. Ärztliche Direktion/8.1. Meldungen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("8. Ärztliche Direktion/8.2. Mitteilungen der ärztlichen Direktion"));
		// /9. Labor
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("9. Labor/9.1. Qualitätszertifikate"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("9. Labor/9.2. Lebensläufe"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("9. Labor/9.3. Normalwerte"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("9. Labor/9.4. Labordokumente"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("10. Vereinbarungen"));
		// /11. ProbandenInnen
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.1. Patienteninformation"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.2. Patienteninformationen unterschrieben"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.3. Ausgehändigte Dokumente und Materialien"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.4. Subject LOGs"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.5. Verzeichnis aufbewahrter Körperflüssigkeiten"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.6. Rekrutierung"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("11. ProbandenInnen/11.7. Verpflegung"));
		// /12. MitarbeiterInnen
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("12. MitarbeiterInnen/12.1. Log of staff, Delegation of tasks"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("12. MitarbeiterInnen/12.2. Lebensläufe, Interessens-konfliktserklärungen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("12. MitarbeiterInnen/12.3. Schulungsdokumentation"));
		// /13. Material
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("13. Material/13.1. Anweisungen zu Handhabung"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("13. Material/13.2. Versandunterlagen"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("13. Material/13.3. Bilanzierungsdokumente"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("13. Material/13.4. Entsorgungsdokumente"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("14. Randomisierung"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("15. Monitoringberichte"));
		// /16. Kommunikation
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("16. Kommunikation/16.1. Korrespondenz"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("16. Kommunikation/16.2. Berichte"));
		// /17. Arbeitsdokumente
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("17. Arbeitsdokumente/17.1. SOPs"));
		createFileFolderPreset(FileModule.TRIAL_DOCUMENT, CommonUtil.fixLogicalPathFolderName("17. Arbeitsdokumente/17.2. Weitere Arbeitsdokumente"));
		// 1. Datenschutzerklärung
		// 2. Identifikationsnachweis
		// 3. Befunde
		// 4. Verschiedenes
		createFileFolderPreset(FileModule.PROBAND_DOCUMENT, CommonUtil.fixLogicalPathFolderName("1. Datenschutzerklärung"));
		createFileFolderPreset(FileModule.PROBAND_DOCUMENT, CommonUtil.fixLogicalPathFolderName("2. Identifikationsnachweis"));
		createFileFolderPreset(FileModule.PROBAND_DOCUMENT, CommonUtil.fixLogicalPathFolderName("3. Befunde"));
		createFileFolderPreset(FileModule.PROBAND_DOCUMENT, CommonUtil.fixLogicalPathFolderName("4. Verschiedenes"));
		createFileFolderPreset(FileModule.PROBAND_DOCUMENT, CommonUtil.fixLogicalPathFolderName("5. Belege"));
		// createFileFolderPreset(FileModule.INPUT_FIELD_DOCUMENT,CommonUtil.fixLogicalPathFolderName("01 - Background Images"));
		// createFileFolderPreset(FileModule.INPUT_FIELD_DOCUMENT,CommonUtil.fixLogicalPathFolderName("02 - Miscellaneous"));
		jobOutput.println("file folder presets created");
	}

	private Holiday createHoliday(HolidayBaseDate base, Integer month, Integer day, Weekday weekday, Integer n, long offsetDays, boolean isHoliday, boolean active,
			String nameL10nKey) {
		Holiday holiday = Holiday.Factory.newInstance();
		holiday.setBase(base);
		holiday.setMonth(month);
		holiday.setDay(day);
		holiday.setWeekday(weekday);
		holiday.setN(n);
		holiday.setOffsetDays(offsetDays);
		holiday.setHoliday(isHoliday);
		holiday.setActive(active);
		holiday.setNameL10nKey(nameL10nKey);
		holiday = holidayDao.create(holiday);
		return holiday;
	}

	private void createHolidayEntries() {
		createHoliday(HolidayBaseDate.WEEKDAY, null, null, Weekday.SATURDAY, null, 0, true, true, "saturday");
		createHoliday(HolidayBaseDate.WEEKDAY, null, null, Weekday.SUNDAY, null, 0, true, true, "sunday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -52, false, true, "womens_carnival_day");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -48, false, true, "rose_monday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -47, false, true, "fat_tuesday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -46, false, true, "ash_wednesday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -7, false, true, "palm_sunday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -3, false, true, "maundy_thursday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, -2, false, true, "good_friday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 0, true, true, "easter_sunday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 1, true, true, "easter_monday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 7, false, true, "low_sunday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 39, true, true, "ascension_day");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 49, true, true, "whit_sunday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 50, true, true, "whit_monday");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 60, true, true, "corpus_christi");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 12, 24, Weekday.SUNDAY, 1, -21, true, true, "first_sunday_in_advent");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 12, 24, Weekday.SUNDAY, 1, -14, true, true, "second_sunday_in_advent");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 12, 24, Weekday.SUNDAY, 1, -7, true, true, "third_sunday_in_advent");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 12, 24, Weekday.SUNDAY, 1, 0, true, true, "fourth_sunday_in_advent");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 5, 1, Weekday.SUNDAY, 2, 0, true, true, "mothers_day");
		createHoliday(HolidayBaseDate.EASTER_DATE, null, null, null, null, 39, false, true, "fathers_day");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 3, 31, Weekday.SUNDAY, 1, 0, false, true, "begin_dst");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_BEFORE_DATE, 10, 31, Weekday.SUNDAY, 1, 0, false, true, "end_dst");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 10, 1, Weekday.SUNDAY, 1, 0, false, true, "harvest_festival");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 11, 1, Weekday.THURSDAY, 4, 0, false, true, "thanksgiving_day");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 11, 1, Weekday.SUNDAY, 3, 0, false, true, "memorial_day");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 11, 1, Weekday.WEDNESDAY, 3, 0, false, true, "penance_day");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 11, 1, Weekday.SUNDAY, 4, 0, false, true, "sunday_in_commemoration_of_the_dead");
		// createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 7,1,Weekday.SATURDAY ,1, 0,false,true,"international_day_of_cooperatives");
		createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE, 9, 1, Weekday.TUESDAY, 3, 0, false, true, "internationl_day_of_peace");
		// createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE,10,1,Weekday.MONDAY ,1, 0,false,true,"world_habitat_day");
		// createHoliday(HolidayBaseDate.NTH_WEEKDAY_AFTER_DATE,10,1,Weekday.WEDNESDAY,2, 0,false,true,"international_day_for_disaster_reduction");
		createHoliday(HolidayBaseDate.STATIC_DATE, 2, 29, null, null, 0, false, true, "leap_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 20, null, null, 0, false, true, "beginning_of_spring");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 21, null, null, 0, false, true, "beginning_of_summer");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 22, null, null, 0, false, true, "beginning_of_autumn");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 21, null, null, 0, false, true, "beginning_of_winter");
		createHoliday(HolidayBaseDate.STATIC_DATE, 1, 1, null, null, 0, true, true, "new_years_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 1, 6, null, null, 0, true, true, "twelfth_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 1, null, null, 0, false, true, "labor_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 26, null, null, 0, true, true, "austrian_national_holiday");
		createHoliday(HolidayBaseDate.STATIC_DATE, 8, 15, null, null, 0, true, true, "assumption_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 1, null, null, 0, true, true, "national_holiday");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 1, null, null, 0, true, true, "allhallows");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 2, null, null, 0, false, true, "all_souls_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 8, null, null, 0, true, true, "immaculate_conception");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 24, null, null, 0, true, true, "christmas_eve");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 25, null, null, 0, true, true, "christmas_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 26, null, null, 0, true, true, "boxing_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 31, null, null, 0, true, true, "new_years_eve");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 31, null, null, 0, false, true, "reformation_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 31, null, null, 0, false, true, "halloween");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 11, null, null, 0, false, true, "martinmas");
		// createHoliday(HolidayBaseDate.STATIC_DATE,1,11,null,null, 0,false,true,"world_laughter_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 1, 25, null, null, 0, false, true, "world_leprosy_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 1, 27, null, null, 0, false, true, "international_holocaust_remembrance_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 2, 4, null, null, 0, false, true, "world_cancer_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 2, 14, null, null, 0, false, true, "valentines_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 2, 20, null, null, 0, false, true, "world_day_of_social_justice");
		// createHoliday(HolidayBaseDate.STATIC_DATE,2,21,null,null, 0,false,true,"welttag_der_gaestefuehrer");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 8, null, null, 0, false, true, "iwd");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 15, null, null, 0, false, true, "world_consumer_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 17, null, null, 0, false, true, "saint_patricks_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 21, null, null, 0, false, true, "international_day_for_elimination_of_racial_discrimination");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 21, null, null, 0, false, true, "world_down_syndrome_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 22, null, null, 0, false, true, "world_water_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 23, null, null, 0, false, true, "world_meteorological_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 24, null, null, 0, false, true, "world_tuberculosis_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,3,26,null,null, 0,false,true,"world_tuberculosis_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,3,27,null,null, 0,false,true,"world_theatre_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 4, 2, null, null, 0, false, true, "world_autism_awareness_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 4, 7, null, null, 0, false, true, "world_health_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,4,21,null,null, 0,false,true,"internationaler_tag_der_ruhe");
		// createHoliday(HolidayBaseDate.STATIC_DATE,4,21,null,null, 0,false,true,"welttag_des_baums");
		createHoliday(HolidayBaseDate.STATIC_DATE, 4, 22, null, null, 0, false, true, "international_earth_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 4, 23, null, null, 0, false, true, "world_intellectual_property_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 4, 25, null, null, 0, false, true, "world_malaria_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,5,3,null,null, 0,false,true,"tag_der_sonne");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 3, null, null, 0, false, true, "world_press_freedom_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 5, null, null, 0, false, true, "europe_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 6, null, null, 0, false, true, "indd");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 8, null, null, 0, false, true, "world_red_cross_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 15, null, null, 0, false, true, "international_day_of_families");
		// createHoliday(HolidayBaseDate.STATIC_DATE,5,17,null,null, 0,false,true,"world_telecommunication_and_information_society_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 21, null, null, 0, false, true, "world_day_for_cultural_diversity");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 22, null, null, 0, false, true, "international_day_for_biological_diversity");
		// createHoliday(HolidayBaseDate.STATIC_DATE,5,25,null,null, 0,false,true,"tag_der_freiheit_afrikas");
		createHoliday(HolidayBaseDate.STATIC_DATE, 5, 31, null, null, 0, false, true, "world_no_tobacco_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 2, null, null, 0, false, true, "organ_donation_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 5, null, null, 0, false, true, "world_environment_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 6, null, null, 0, false, true, "day_of_the_visually_impaired");
		// createHoliday(HolidayBaseDate.STATIC_DATE,6,17,null,null, 0,false,true,"internationaler_tag_zur_bekaempfung_der_ausbreitung_von_wuestengebieten");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 14, null, null, 0, false, true, "world_blood_donor_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 3, 16, null, null, 0, false, true, "world_sleep_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,6,20,null,null, 0,false,true,"weltjongliertag");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 24, null, null, 0, false, true, "midsummers_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 26, null, null, 0, false, true, "international_day_against_drug_abuse_and_illicit_trafficking");
		createHoliday(HolidayBaseDate.STATIC_DATE, 6, 27, null, null, 0, false, true, "saint_swithund_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 7, 11, null, null, 0, false, true, "world_population_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,8,9,null,null, 0,false,true,"internationaler_tag_der_indigenen_voelker");
		createHoliday(HolidayBaseDate.STATIC_DATE, 7, 28, null, null, 0, false, true, "world_hepatitis_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,9,1,null,null, 0,false,true,"weltfriedenstag");
		createHoliday(HolidayBaseDate.STATIC_DATE, 8, 19, null, null, 0, false, true, "world_humanitarian_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 8, null, null, 0, false, true, "international_literacy_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,9,16,null,null, 0,false,true,"internationaler_tag_zum_schutz_der_ozonschicht");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 10, null, null, 0, false, true, "world_suicide_prevention_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 20, null, null, 0, false, true, "international_childrens_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 21, null, null, 0, false, true, "world_alzheimers_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,9,22,null,null, 0,false,true,"autofreier_tag_in_europa");
		// createHoliday(HolidayBaseDate.STATIC_DATE,9,24,null,null, 0,false,true,"tag_der_raumfahrt");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 25, null, null, 0, false, true, "dental_hygiene_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 9, 28, null, null, 0, false, true, "world_rabies_day");
		// world_heart_day"); //nur 2012?
		// createHoliday(HolidayBaseDate.STATIC_DATE,9,27,null,null, 0,false,true,"welttourismustag");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 1, null, null, 0, false, true, "international_day_of_older_persons");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 1, null, null, 0, false, true, "world_music_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 4, null, null, 0, false, true, "refugee_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 4, null, null, 0, false, true, "world_animal_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 5, null, null, 0, false, true, "epilepsy_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 10, null, null, 0, false, true, "world_mental_health_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,11,null,null, 0,false,true,"world_sight_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,5,null,null, 0,false,true,"weltposttag");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,5,null,null, 0,false,true,"welttag_des_lehrers");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,9,null,null, 0,false,true,"tag_des_weltpostvereins");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,15,null,null, 0,false,true,"tag_des_weissen_stockes");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 16, null, null, 0, false, true, "world_food_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 17, null, null, 0, false, true, "international_day_for_the_eradication_of_poverty");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 24, null, null, 0, false, true, "united_nations_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,10,24,null,null, 0,false,true,"welttag_der_information_ueber_entwicklungsfragen");
		createHoliday(HolidayBaseDate.STATIC_DATE, 10, 30, null, null, 0, false, true, "international_savings_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 3, null, null, 0, false, true, "mens_world_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 14, null, null, 0, false, true, "world_diabetes_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 16, null, null, 0, false, true, "international_day_for_tolerance");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 17, null, null, 0, false, true, "world_chronic_obstructive_pulmonary_disease_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,11,20,null,null, 0,false,true,"tag_der_industrialisierung_afrikas");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 20, null, null, 0, false, true, "universal_childrens_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,11,21,null,null, 0,false,true,"weltfernsehtag");
		createHoliday(HolidayBaseDate.STATIC_DATE, 11, 23, null, null, 0, false, true, "international_day_for_the_elimination_of_violence_against_women");
		// createHoliday(HolidayBaseDate.STATIC_DATE,11,29,null,null, 0,false,true,"internationaler_tag_der_solidaritaet_mit_dem_palaestinensischen_volk");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 1, null, null, 0, false, true, "world_aids_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 3, null, null, 0, false, true, "international_day_of_persons_with_disabilities");
		// createHoliday(HolidayBaseDate.STATIC_DATE,12,5,null,null, 0,false,true,"internationaler_entwickungshelfertag_fuer_die_wirtschaftliche_und_soziale_entwicklung");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 6, null, null, 0, false, true, "saint_nicholas_day");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 9, null, null, 0, false, true, "international_anti_corruption_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,12,7,null,null, 0,false,true,"internationaler_tag_der_zivilen_luftfahrt");
		createHoliday(HolidayBaseDate.STATIC_DATE, 12, 10, null, null, 0, false, true, "human_rights_day");
		// createHoliday(HolidayBaseDate.STATIC_DATE,12,12,null,null, 0,false,true,"weltkinderfernsehtag");
		jobOutput.println("holidays created");
	}

	private void createHyperlinkCategories() {
		createHyperlinkCategory(HyperlinkModule.INVENTORY_HYPERLINK, "manual", "manual_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.INVENTORY_HYPERLINK, "customer_service", "customer_service_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.INVENTORY_HYPERLINK, "product_info", "product_info_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.INVENTORY_HYPERLINK, "sop", "sop_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.STAFF_HYPERLINK, "website", "website_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.COURSE_HYPERLINK, "course_material", "course_material_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.COURSE_HYPERLINK, "mug_online_course", "mug_online_course_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.TRIAL_HYPERLINK, "trial_related_resource", "trial_related_resource_title_preset", true);
		createHyperlinkCategory(HyperlinkModule.TRIAL_HYPERLINK, "trial_related_sop", "trial_related_sop_title_preset", true);
		jobOutput.println("hyperlink categories created");
	}

	private HyperlinkCategory createHyperlinkCategory(HyperlinkModule module, String nameL10nKey, String titlePresetL10nKey, boolean visible) {
		HyperlinkCategory hyperlinkCategory = HyperlinkCategory.Factory.newInstance();
		hyperlinkCategory.setModule(module);
		hyperlinkCategory.setNameL10nKey(nameL10nKey);
		hyperlinkCategory.setTitlePresetL10nKey(titlePresetL10nKey);
		hyperlinkCategory.setVisible(visible);
		hyperlinkCategory = hyperlinkCategoryDao.create(hyperlinkCategory);
		return hyperlinkCategory;
	}

	private void createInventoryCategories() {
		createInventoryCategory("building", Color.LIGHTSTEELBLUE, true, true, true, true, "ctsms-inventorycategory-building");
		createInventoryCategory("room", Color.POWDERBLUE, true, true, true, true, "ctsms-inventorycategory-room");
		createInventoryCategory("bed", Color.LIGHTBLUE, false, true, true, true, "ctsms-inventorycategory-bed");
		createInventoryCategory("basic_equipment", Color.DODGERBLUE, true, true, true, true, "ctsms-inventorycategory-basic-equipment");
		createInventoryCategory("study_specific_equipment", Color.CORNFLOWERBLUE, true, true, true, true, "ctsms-inventorycategory-study-specific-equipment");
		createInventoryCategory("laboratory_equipment", Color.STEELBLUE, true, true, true, true, "ctsms-inventorycategory-laboratory-equipment");
		createInventoryCategory("examination_room", Color.MEDIUMTURQUOISE, true, true, true, true, "ctsms-inventorycategory-examination-room");
		createInventoryCategory("expendable", Color.SKYBLUE, false, true, true, true, "ctsms-inventorycategory-expendable");
		createInventoryCategory("substance", Color.LIGHTSKYBLUE, false, true, true, true, "ctsms-inventorycategory-substance");
		createInventoryCategory("drug", Color.DEEPSKYBLUE, true, true, true, true, "ctsms-inventorycategory-drug");
		createInventoryCategory("av", Color.MEDIUMSLATEBLUE, true, true, true, true, "ctsms-inventorycategory-av");
		createInventoryCategory("other", Color.CADETBLUE, false, true, true, true, "ctsms-inventorycategory-other");
		jobOutput.println("inventory categories created");
	}

	private InventoryCategory createInventoryCategory(String nameL10nKey, Color color, boolean relevantForCourseAppointments, boolean relevantForTrialAppointments,
			boolean relevantForProbandAppointments, boolean visible, String nodeStyleClass) {
		InventoryCategory inventoryCategory = InventoryCategory.Factory.newInstance();
		inventoryCategory.setNameL10nKey(nameL10nKey);
		inventoryCategory.setColor(color);
		inventoryCategory.setVisible(visible);
		inventoryCategory.setRelevantForCourseAppointments(relevantForCourseAppointments);
		inventoryCategory.setRelevantForTrialAppointments(relevantForTrialAppointments);
		inventoryCategory.setRelevantForProbandAppointments(relevantForProbandAppointments);
		inventoryCategory.setNodeStyleClass(nodeStyleClass);
		inventoryCategory = inventoryCategoryDao.create(inventoryCategory);
		return inventoryCategory;
	}

	private InventoryStatusType createInventoryStatusType(String nameL10nKey, boolean inventoryActive, boolean originatorRequired, boolean addresseeRequired, boolean visible,
			boolean hideAvailability) {
		InventoryStatusType statusType = InventoryStatusType.Factory.newInstance();
		statusType.setNameL10nKey(nameL10nKey);
		statusType.setInventoryActive(inventoryActive);
		statusType.setVisible(visible);
		statusType.setHideAvailability(hideAvailability);
		statusType.setOriginatorRequired(originatorRequired);
		statusType.setAddresseeRequired(addresseeRequired);
		statusType = inventoryStatusTypeDao.create(statusType);
		return statusType;
	}

	private void createInventoryStatusTypes() {
		createInventoryStatusType("lent", false, true, true, true, false);
		createInventoryStatusType("defective", false, false, false, true, true);
		createInventoryStatusType("in_repair", false, true, true, true, false);
		createInventoryStatusType("servicing", false, true, true, true, false);
		createInventoryStatusType("inspection", false, true, true, true, false);
		createInventoryStatusType("calibration", false, true, true, true, false);
		createInventoryStatusType("end_of_life", false, false, false, true, true);
		createInventoryStatusType("out_of_stock", false, false, false, true, false);
		createInventoryStatusType("quarantine", false, false, false, true, false);
		jobOutput.println("inventory status types created");
	}

	private InventoryTag createInventoryTag(String nameL10nKey, Integer maxOccurrence, String regExp, String mismatchMsgL10nKey, boolean visible, boolean excel) {
		InventoryTag tag = InventoryTag.Factory.newInstance();
		tag.setNameL10nKey(nameL10nKey);
		tag.setMaxOccurrence(maxOccurrence);
		tag.setRegExp(regExp);
		tag.setMismatchMsgL10nKey(mismatchMsgL10nKey);
		tag.setVisible(visible);
		tag.setExcel(excel);
		tag = inventoryTagDao.create(tag);
		return tag;
	}

	private void createInventoryTags() {
		createInventoryTag("serial_number", 1, null, null, true, true);
		createInventoryTag("model", 1, null, null, true, true);
		createInventoryTag("mug_inventory_number", 1, null, null, true, true);
		createInventoryTag("n_number", 1, null, null, true, true);
		jobOutput.println("inventory tags created");
	}

	private void createJournalCategories() {
		createJournalCategory(JournalModule.INVENTORY_JOURNAL, "information", "information_title_preset", true, Color.LIGHTCORAL, "ctsms-journalcategory-instruction", false);
		createJournalCategory(JournalModule.INVENTORY_JOURNAL, "call", "call_title_preset", true, Color.PALEGREEN, "ctsms-journalcategory-call", true);
		createJournalCategory(JournalModule.INVENTORY_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", false);
		createJournalCategory(JournalModule.STAFF_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", true);
		createJournalCategory(JournalModule.COURSE_JOURNAL, "announcement", "announcement_title_preset", true, Color.SEASHELL, "ctsms-journalcategory-announcement", true);
		createJournalCategory(JournalModule.COURSE_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", false);
		createJournalCategory(JournalModule.USER_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", true);
		createJournalCategory(JournalModule.INPUT_FIELD_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", true);
		createJournalCategory(JournalModule.TRIAL_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", true);
		createJournalCategory(JournalModule.PROBAND_JOURNAL, "call", "call_title_preset", true, Color.LIGHTYELLOW, "ctsms-journalcategory-call", true);
		createJournalCategory(JournalModule.PROBAND_JOURNAL, "mail", "mail_title_preset", true, Color.SEASHELL, "ctsms-journalcategory-mail", false);
		createJournalCategory(JournalModule.PROBAND_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", false);
		createJournalCategory(JournalModule.CRITERIA_JOURNAL, "general", "general_title_preset", true, Color.PAPAYAWHIP, "ctsms-journalcategory-general", false);
		createJournalCategory(JournalModule.CRITERIA_JOURNAL, "instruction", "instruction_title_preset", true, Color.PALEGREEN, "ctsms-journalcategory-instruction", true);
		jobOutput.println("journal categories created");
	}

	private JournalCategory createJournalCategory(
			JournalModule module,
			String nameL10nKey,
			String titlePresetL10nKey,
			boolean visible,
			Color color,
			String nodeStyleClass,
			boolean preset) {
		JournalCategory journalCategory = JournalCategory.Factory.newInstance();
		journalCategory.setModule(module);
		journalCategory.setNameL10nKey(nameL10nKey);
		journalCategory.setTitlePresetL10nKey(titlePresetL10nKey);
		journalCategory.setVisible(visible);
		journalCategory.setColor(color);
		journalCategory.setNodeStyleClass(nodeStyleClass);
		journalCategory.setPreset(preset);
		journalCategory = journalCategoryDao.create(journalCategory);
		return journalCategory;
	}

	private LecturerCompetence createLecturerCompetence(String nameL10nKey, Integer maxOccurrence, boolean visible) {
		LecturerCompetence competence = LecturerCompetence.Factory.newInstance();
		competence.setNameL10nKey(nameL10nKey);
		competence.setMaxOccurrence(maxOccurrence);
		competence.setVisible(visible);
		competence = lecturerCompetenceDao.create(competence);
		return competence;
	}

	private void createLecturerCompetences() {
		createLecturerCompetence("lecturer", null, true);
		createLecturerCompetence("course_instructor", null, true);
		createLecturerCompetence("course_supervisor", null, true);
		createLecturerCompetence("examiner", null, true);
		createLecturerCompetence("assistant", null, true);
		jobOutput.println("lecturer competences created");
	}

	private MaintenanceType createMaintenanceType(String nameL10nKey, String titlePresetL10nKey, boolean visible) {
		MaintenanceType maintenanceType = MaintenanceType.Factory.newInstance();
		maintenanceType.setNameL10nKey(nameL10nKey);
		maintenanceType.setTitlePresetL10nKey(titlePresetL10nKey);
		maintenanceType.setVisible(visible);
		maintenanceType = maintenanceTypeDao.create(maintenanceType);
		return maintenanceType;
	}

	private void createMaintenanceTypes() {
		createMaintenanceType("servicing", "servicing_title_preset", true);
		createMaintenanceType("inspection", "inspection_title_preset", true);
		createMaintenanceType("calibration", "calibration_title_preset", true);
		createMaintenanceType("warranty_expiration", "warranty_expiration_title_preset", true);
		createMaintenanceType("reorder", "reorder_title_preset", true);
		createMaintenanceType("other", "other_title_preset", true);
		jobOutput.println("maintenance reminder types created");
	}

	private NotificationType createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType type,
			org.phoenixctms.ctsms.enumeration.Color color,
			String nodeStyleClass,
			boolean send,
			boolean show,
			Set<StaffCategory> sendDepartmentStaffCategories) {
		NotificationType notificationType = NotificationType.Factory.newInstance();
		notificationType.setType(type);
		notificationType.setNameL10nKey(type.name());
		notificationType.setSubjectL10nKey(type.toString());
		notificationType.setMessageTemplateL10nKey(type.name());
		notificationType.setSend(send);
		notificationType.setShow(show);
		notificationType.setColor(color);
		notificationType.setNodeStyleClass(nodeStyleClass);
		notificationType.setSendDepartmentStaffCategories(sendDepartmentStaffCategories == null ? new HashSet<StaffCategory>() : sendDepartmentStaffCategories);
		notificationType = notificationTypeDao.create(notificationType);
		return notificationType;
	}

	private void createNotificationTypes() {
		HashSet<StaffCategory> allStaffCategories = new HashSet<StaffCategory>(ALL_STAFF_CATEGORY_NAME_L10N_KEYS.size());
		Iterator<String> it = ALL_STAFF_CATEGORY_NAME_L10N_KEYS.iterator();
		while (it.hasNext()) {
			allStaffCategories.add(staffCategoryDao.searchUniqueNameL10nKey(it.next()));
		}
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.MAINTENANCE_REMINDER,
				org.phoenixctms.ctsms.enumeration.Color.ALICEBLUE,
				"ctsms-icon-maintenance",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.INVENTORY_INACTIVE,
				org.phoenixctms.ctsms.enumeration.Color.WHITESMOKE,
				null, // "ctsms-icon-inventoryentity",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.INVENTORY_INACTIVE_BOOKING,
				org.phoenixctms.ctsms.enumeration.Color.SEASHELL,
				"ctsms-icon-warning",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.STAFF_INACTIVE,
				org.phoenixctms.ctsms.enumeration.Color.PAPAYAWHIP,
				null, // "ctsms-icon-staffentity",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.STAFF_INACTIVE_DUTY_ROSTER_TURN,
				org.phoenixctms.ctsms.enumeration.Color.OLDLACE,
				"ctsms-icon-warning",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.PROBAND_INACTIVE,
				org.phoenixctms.ctsms.enumeration.Color.GHOSTWHITE,
				"ctsms-icon-probandentity",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.PROBAND_INACTIVE_VISIT_SCHEDULE_ITEM,
				org.phoenixctms.ctsms.enumeration.Color.ANTIQUEWHITE,
				"ctsms-icon-warning",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM,
				org.phoenixctms.ctsms.enumeration.Color.LIGHTPINK,
				"ctsms-icon-warning",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE,
				org.phoenixctms.ctsms.enumeration.Color.LIGHTYELLOW,
				"ctsms-icon-courseexpiration",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE_PARTICIPATION,
				org.phoenixctms.ctsms.enumeration.Color.CORAL,
				"ctsms-icon-admincourseexpiration",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.COURSE_PARTICIPATION_STATUS_UPDATED,
				org.phoenixctms.ctsms.enumeration.Color.SNOW,
				null, // "ctsms-icon-courseentity",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.TIMELINE_EVENT_REMINDER,
				org.phoenixctms.ctsms.enumeration.Color.LAVENDERBLUSH,
				null, // "ctsms-icon-timelineevents",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.VISIT_SCHEDULE_ITEM_REMINDER,
				org.phoenixctms.ctsms.enumeration.Color.GOLD,
				"ctsms-icon-timelineevents",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_PROBAND_AUTO_DELETE,
				org.phoenixctms.ctsms.enumeration.Color.MISTYROSE,
				"ctsms-icon-autodelete",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_PASSWORD,
				org.phoenixctms.ctsms.enumeration.Color.BEIGE,
				"ctsms-icon-key",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.TRIAL_STATUS_UPDATED,
				org.phoenixctms.ctsms.enumeration.Color.FLORALWHITE,
				null, // "ctsms-icon-trialentity",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.PROBANDS_DELETED,
				org.phoenixctms.ctsms.enumeration.Color.LIGHTCORAL,
				"ctsms-icon-information",
				false, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.TRIAL_TAG_MISSING,
				org.phoenixctms.ctsms.enumeration.Color.PALEGOLDENROD,
				"ctsms-icon-trialentity",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.DUTY_ROSTER_TURN_UPDATED,
				org.phoenixctms.ctsms.enumeration.Color.WHITE,
				"ctsms-icon-calendar",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.DUTY_ROSTER_TURN_ASSIGNED,
				org.phoenixctms.ctsms.enumeration.Color.WHITE,
				"ctsms-icon-calendar",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.DUTY_ROSTER_TURN_UNASSIGNED,
				org.phoenixctms.ctsms.enumeration.Color.WHITE,
				"ctsms-icon-calendar",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.NEW_COURSE,
				org.phoenixctms.ctsms.enumeration.Color.CORNSILK,
				null,
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.USER_ACCOUNT,
				org.phoenixctms.ctsms.enumeration.Color.PALEGREEN,
				"ctsms-icon-identity",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.DUTY_ROSTER_TURN_DELETED,
				org.phoenixctms.ctsms.enumeration.Color.WHITE,
				"ctsms-icon-calendar",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.ECRF_STATUS_UPDATED,
				org.phoenixctms.ctsms.enumeration.Color.FLORALWHITE,
				null, // "ctsms-icon-trialentity",
				true, true,
				allStaffCategories);
		createNotificationType(org.phoenixctms.ctsms.enumeration.NotificationType.NEW_ECRF_FIELD_STATUS,
				org.phoenixctms.ctsms.enumeration.Color.FLORALWHITE,
				null, // "ctsms-icon-trialentity",
				true, true,
				allStaffCategories);
		jobOutput.println("notification types created");
	}

	private PrivacyConsentStatusType createPrivacyConsentStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			boolean initial,
			boolean autoDelete) {
		PrivacyConsentStatusType privacyConsentStatusType = PrivacyConsentStatusType.Factory.newInstance();
		privacyConsentStatusType.setColor(color);
		privacyConsentStatusType.setInitial(initial);
		privacyConsentStatusType.setAutoDelete(autoDelete);
		privacyConsentStatusType.setNameL10nKey(nameL10nKey);
		privacyConsentStatusType = privacyConsentStatusTypeDao.create(privacyConsentStatusType);
		return privacyConsentStatusType;
	}

	private void createPrivacyConsentStatusTypeEntries() {
		PrivacyConsentStatusType registeredPrivacyConsentStatusType = createPrivacyConsentStatusType("registered",
				Color.LIMEGREEN,
				true,
				true
				);
		PrivacyConsentStatusType existingPrivacyConsentOkPrivacyConsentStatusType = createPrivacyConsentStatusType("existing_privacy_consent_ok",
				Color.YELLOW,
				false,
				false
				);
		PrivacyConsentStatusType privacyConsentSentPrivacyConsentStatusType = createPrivacyConsentStatusType("privacy_consent_sent",
				Color.LEMONCHIFFON,
				false,
				true
				);
		PrivacyConsentStatusType privacyConsentReceivedPrivacyConsentStatusType = createPrivacyConsentStatusType("privacy_consent_received",
				Color.KHAKI,
				false,
				false
				);
		PrivacyConsentStatusType privacyConsentNotReceivedPrivacyConsentStatusType = createPrivacyConsentStatusType("privacy_consent_not_received",
				Color.PAPAYAWHIP,
				false,
				true
				);
		updatePrivacyConsentStatusType(
				registeredPrivacyConsentStatusType,
				getPrivacyConsentStatusTransitions(registeredPrivacyConsentStatusType, existingPrivacyConsentOkPrivacyConsentStatusType,
						privacyConsentSentPrivacyConsentStatusType,
						privacyConsentReceivedPrivacyConsentStatusType));
		updatePrivacyConsentStatusType(existingPrivacyConsentOkPrivacyConsentStatusType,
				getPrivacyConsentStatusTransitions(existingPrivacyConsentOkPrivacyConsentStatusType, privacyConsentSentPrivacyConsentStatusType));
		updatePrivacyConsentStatusType(
				privacyConsentSentPrivacyConsentStatusType,
				getPrivacyConsentStatusTransitions(privacyConsentSentPrivacyConsentStatusType, privacyConsentReceivedPrivacyConsentStatusType,
						privacyConsentNotReceivedPrivacyConsentStatusType));
		updatePrivacyConsentStatusType(privacyConsentNotReceivedPrivacyConsentStatusType,
				getPrivacyConsentStatusTransitions(privacyConsentNotReceivedPrivacyConsentStatusType, privacyConsentSentPrivacyConsentStatusType));
		updatePrivacyConsentStatusType(privacyConsentReceivedPrivacyConsentStatusType,
				getPrivacyConsentStatusTransitions(privacyConsentReceivedPrivacyConsentStatusType, privacyConsentNotReceivedPrivacyConsentStatusType));
		jobOutput.println("privacy consent states created");
	}

	private void createProbandCategories() {
		createProbandCategory("new_person", Color.WHITESMOKE, true, false, true, false, "ctsms-probandcategory-new-person", true, false, false);
		createProbandCategory("new_animal", Color.WHITESMOKE, false, true, false, false, "ctsms-probandcategory-new-animal", true, false, false);
		createProbandCategory("signup", Color.ANTIQUEWHITE, false, true, true, true, "ctsms-probandcategory-signup", true, true, false);
		createProbandCategory("migrated", Color.SEASHELL, true, true, false, false, "ctsms-probandcategory-migrated", false, false, false);
		createProbandCategory("test", Color.PAPAYAWHIP, true, true, false, false, "ctsms-probandcategory-test", false, false, false);
		createProbandCategory("unable_to_reach", Color.GHOSTWHITE, false, true, true, false, "ctsms-probandcategory-unable-to-reach", false, false, false);
		createProbandCategory("to_be_deleted", Color.ORANGERED, true, true, false, true, "ctsms-probandcategory-to-be-deleted", false, false, true);
		createProbandCategory("duplicate", Color.MISTYROSE, true, true, false, false, "ctsms-probandcategory-duplicate", false, false, true);
		createProbandCategory("locked", Color.GAINSBORO, true, true, false, false, "ctsms-probandcategory-locked", false, false, true);
		createProbandCategory("participant_to_be_deleted", Color.SALMON, false, true, false, false, "ctsms-probandcategory-participant-to-be-deleted", false, false, true);
		// createProbandCategory("new", Color.WHITESMOKE, true, true, "ctsms-probandcategory-new", true, false);
		// createProbandCategory("migrated", Color.SEASHELL, true, false, "ctsms-probandcategory-migrated", false, false);
		// createProbandCategory("test", Color.PAPAYAWHIP, true, true, "ctsms-probandcategory-test", false, false);
		// createProbandCategory("unable_to_reach", Color.GHOSTWHITE, true, true, "ctsms-probandcategory-unable-to-reach", false, false);
		// createProbandCategory("to_be_deleted", Color.ORANGERED, true, true, "ctsms-probandcategory-to-be-deleted", false, true);
		// createProbandCategory("duplicate", Color.MISTYROSE, true, true, "ctsms-probandcategory-duplicate", false, true);
		// createProbandCategory("locked", Color.GAINSBORO, true, false, "ctsms-probandcategory-locked", false, true);
		// createProbandCategory("participant_to_be_deleted", Color.SALMON, true, false, "ctsms-probandcategory-participant-to-be-deleted", false, true);
		jobOutput.println("proband categories created");
	}

	private ProbandCategory createProbandCategory(String nameL10nKey, Color color, boolean person, boolean animal, boolean privacyConsentControl, boolean delete,
			String nodeStyleClass,
			boolean preset,
			boolean signup,
			boolean locked) {
		ProbandCategory probandCategory = ProbandCategory.Factory.newInstance();
		probandCategory.setNameL10nKey(nameL10nKey);
		probandCategory.setColor(color);
		probandCategory.setPerson(person);
		probandCategory.setAnimal(animal);
		probandCategory.setPrivacyConsentControl(privacyConsentControl);
		probandCategory.setDelete(delete);
		probandCategory.setNodeStyleClass(nodeStyleClass);
		probandCategory.setPreset(preset);
		probandCategory.setLocked(locked);
		probandCategory.setSignup(signup);
		probandCategory = probandCategoryDao.create(probandCategory);
		return probandCategory;
	}

	private ProbandListStatusLogLevel createProbandListStatusLogLevel(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel l) {
		ProbandListStatusLogLevel logLevel = ProbandListStatusLogLevel.Factory.newInstance();
		logLevel.setLogLevel(l);
		logLevel = probandListStatusLogLevelDao.create(logLevel);
		return logLevel;
	}

	private void createProbandListStatusLogLevels() {
		org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel[] logLevels = org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.values();
		for (int i = 0; i < logLevels.length; i++) {
			createProbandListStatusLogLevel(logLevels[i]);
		}
		jobOutput.println("proband status log levels created");
	}

	private ProbandListStatusType createProbandListStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			boolean initial,
			boolean reasonRequired,
			boolean blocking,
			boolean count,
			boolean screening,
			boolean ic,
			boolean ecrfValueInputEnabled,
			boolean signup,
			boolean person,
			Set<ProbandListStatusLogLevel> logLevels) {
		ProbandListStatusType probandListStatusType = ProbandListStatusType.Factory.newInstance();
		probandListStatusType.setColor(color);
		probandListStatusType.setInitial(initial);
		probandListStatusType.setReasonRequired(reasonRequired);
		probandListStatusType.setNameL10nKey(nameL10nKey);
		probandListStatusType.setBlocking(blocking);
		probandListStatusType.setCount(count); // count to participation/group size, i.e. "not dropped out/cancelled/screening failure"
		probandListStatusType.setScreening(screening);
		probandListStatusType.setIc(ic);
		probandListStatusType.setEcrfValueInputEnabled(ecrfValueInputEnabled);
		probandListStatusType.setSignup(signup);
		probandListStatusType.setPerson(person);
		probandListStatusType.setLogLevels(logLevels);
		probandListStatusType = probandListStatusTypeDao.create(probandListStatusType);
		return probandListStatusType;
	}

	private void createProbandListStatusTypeEntries() {
		ProbandListStatusType candidateProbandListStatusType = createProbandListStatusType("candidate", Color.LIGHTYELLOW,
				true,
				false,
				true,
				false,
				false,
				false,
				true,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType signupProbandListStatusType = createProbandListStatusType("signup", Color.YELLOW,
				true,
				false,
				true,
				false,
				false,
				false,
				true,
				true,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType contactedProbandListStatusType = createProbandListStatusType("contacted", Color.ORANGE,
				false,
				false,
				true,
				false,
				false,
				false,
				true,
				false,
				true,
				getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.PRE_SCREENING, org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.SICL)
				);
		ProbandListStatusType cancelledProbandListStatusType = createProbandListStatusType("cancelled", Color.RED,
				false,
				false,
				false,
				false,
				false,
				false,
				true, // false,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType acceptanceProbandListStatusType = createProbandListStatusType("acceptance", Color.DARKORANGE,
				false,
				false,
				true,
				true,
				false,
				false,
				true,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType icSignedProbandListStatusType = createProbandListStatusType("ic_signed", Color.SPRINGGREEN,
				false,
				false,
				true,
				true,
				false,
				true,
				true,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType screeningOkProbandListStatusType = createProbandListStatusType("screening_ok", Color.LIMEGREEN,
				false,
				false,
				true,
				true,
				true,
				false,
				true,
				false,
				true,
				getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.SCREENING)
				);
		ProbandListStatusType screeningFailureProbandListStatusType = createProbandListStatusType("screening_failure", Color.ORANGERED,
				false,
				true,
				false,
				false,
				true,
				false,
				true, // false,
				false,
				true,
				getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.SCREENING)
				);
		ProbandListStatusType ongoingProbandListStatusType = createProbandListStatusType("ongoing", Color.LIME,
				false,
				false,
				true,
				true,
				false,
				false,
				true,
				false,
				true,
				getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.ENROLLMENT)
				);
		ProbandListStatusType droppedOutProbandListStatusType = createProbandListStatusType("dropped_out", Color.TOMATO,
				false,
				true,
				false,
				false,
				false,
				false,
				true, // false,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType completedProbandListStatusType = createProbandListStatusType("completed", Color.MEDIUMSEAGREEN,
				false,
				false,
				true,
				true,
				false,
				false,
				true, // false,
				false,
				true,
				getProbandListStatusLogLevels()
				);
		updateProbandListStatusType(candidateProbandListStatusType,
				getProbandListStatusTransitions(candidateProbandListStatusType, contactedProbandListStatusType, cancelledProbandListStatusType));
		updateProbandListStatusType(signupProbandListStatusType,
				getProbandListStatusTransitions(signupProbandListStatusType, contactedProbandListStatusType, cancelledProbandListStatusType));
		updateProbandListStatusType(contactedProbandListStatusType,
				getProbandListStatusTransitions(contactedProbandListStatusType, cancelledProbandListStatusType, acceptanceProbandListStatusType));
		updateProbandListStatusType(cancelledProbandListStatusType,
				getProbandListStatusTransitions());
		updateProbandListStatusType(acceptanceProbandListStatusType,
				getProbandListStatusTransitions(contactedProbandListStatusType, acceptanceProbandListStatusType, icSignedProbandListStatusType, cancelledProbandListStatusType));
		updateProbandListStatusType(
				icSignedProbandListStatusType,
				getProbandListStatusTransitions(icSignedProbandListStatusType, screeningOkProbandListStatusType, screeningFailureProbandListStatusType,
						droppedOutProbandListStatusType));
		updateProbandListStatusType(screeningOkProbandListStatusType,
				getProbandListStatusTransitions(screeningOkProbandListStatusType, ongoingProbandListStatusType, droppedOutProbandListStatusType));
		updateProbandListStatusType(screeningFailureProbandListStatusType,
				getProbandListStatusTransitions());
		updateProbandListStatusType(ongoingProbandListStatusType,
				getProbandListStatusTransitions(ongoingProbandListStatusType, droppedOutProbandListStatusType, completedProbandListStatusType));
		updateProbandListStatusType(droppedOutProbandListStatusType,
				getProbandListStatusTransitions());
		updateProbandListStatusType(completedProbandListStatusType,
				getProbandListStatusTransitions());
		ProbandListStatusType animalUnderTestProbandListStatusType = createProbandListStatusType("animal_under_test", Color.LIGHTYELLOW,
				true,
				false,
				true,
				true,
				false,
				false,
				true,
				false,
				false,
				getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel.ENROLLMENT)
				);
		ProbandListStatusType animalDroppedOutProbandListStatusType = createProbandListStatusType("animal_dropped_out", Color.TOMATO,
				false,
				true,
				false,
				false,
				false,
				false,
				true, // false,
				false,
				false,
				getProbandListStatusLogLevels()
				);
		ProbandListStatusType animalCompletedProbandListStatusType = createProbandListStatusType("animal_completed", Color.MEDIUMSEAGREEN,
				false,
				false,
				true,
				true,
				false,
				false,
				true, // false,
				false,
				false,
				getProbandListStatusLogLevels()
				);
		updateProbandListStatusType(animalUnderTestProbandListStatusType,
				getProbandListStatusTransitions(animalUnderTestProbandListStatusType, animalDroppedOutProbandListStatusType, animalCompletedProbandListStatusType));
		updateProbandListStatusType(animalDroppedOutProbandListStatusType,
				getProbandListStatusTransitions());
		updateProbandListStatusType(animalCompletedProbandListStatusType,
				getProbandListStatusTransitions());
		jobOutput.println("proband states created");
	}

	private ProbandStatusType createProbandStatusType(String nameL10nKey, boolean probandActive, boolean person, boolean animal, boolean hideAvailability) {
		ProbandStatusType statusType = ProbandStatusType.Factory.newInstance();
		statusType.setNameL10nKey(nameL10nKey);
		statusType.setProbandActive(probandActive);
		statusType.setPerson(person);
		statusType.setAnimal(animal);
		statusType.setHideAvailability(hideAvailability);
		statusType = probandStatusTypeDao.create(statusType);
		return statusType;
	}

	private void createProbandStatusTypes() {
		createProbandStatusType("vacation", false, true, false, false);
		createProbandStatusType("sick", false, true, false, false);
		createProbandStatusType("no_time", false, true, false, false);
		createProbandStatusType("cancelled", false, true, false, false);
		createProbandStatusType("other_trial", false, true, true, false);
		createProbandStatusType("na", false, false, true, false);
		createProbandStatusType("deceased", false, true, true, false);
		jobOutput.println("proband status types created");
	}

	private ProbandTag createProbandTag(String nameL10nKey, Integer maxOccurrence, String regExp, String mismatchMsgL10nKey, boolean person, boolean animal, boolean excel) {
		ProbandTag tag = ProbandTag.Factory.newInstance();
		tag.setNameL10nKey(nameL10nKey);
		tag.setMaxOccurrence(maxOccurrence);
		tag.setRegExp(regExp);
		tag.setMismatchMsgL10nKey(mismatchMsgL10nKey);
		tag.setPerson(person);
		tag.setAnimal(animal);
		tag.setExcel(excel);
		tag = probandTagDao.create(tag);
		return tag;
	}

	private void createProbandTags() {
		createProbandTag("social_security_number", 1, null, null, true, false, true);
		createProbandTag("subject_id", null, null, null, true, true, true);
		createProbandTag("unique_id", 1, null, null, true, true, true);
		createProbandTag("species", null, null, null, false, true, true);
		jobOutput.println("proband tags created");
	}

	private SponsoringType createSponsoringType(String nameL10nKey, boolean visible) {
		SponsoringType sponsoringType = SponsoringType.Factory.newInstance();
		sponsoringType.setNameL10nKey(nameL10nKey);
		sponsoringType.setVisible(visible);
		sponsoringType = sponsoringTypeDao.create(sponsoringType);
		return sponsoringType;
	}

	private void createSponsoringTypes() {
		createSponsoringType("commissioned_trial", true);
		createSponsoringType("academic_trial", true);
		createSponsoringType("na", true);
		jobOutput.println("sponsoring types created");
	}

	private void createStaffCategories() {
		createStaffCategory(INSTITUTION_STAFF_CATEGORY_NAME_L10N_KEY, Color.NAVAJOWHITE, false, true, "ctsms-staffcategory-institution");
		createStaffCategory(DIVISION_STAFF_CATEGORY_NAME_L10N_KEY, Color.WHEAT, false, true, "ctsms-staffcategory-division");
		createStaffCategory(VENDOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.BURLYWOOD, false, true, "ctsms-staffcategory-vendor");
		createStaffCategory(ORDINATION_STAFF_CATEGORY_NAME_L10N_KEY, Color.FUCHSIA, false, true, "ctsms-staffcategory-ordination");
		createStaffCategory(SPONSOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.TAN, false, true, "ctsms-staffcategory-sponsor");
		createStaffCategory(MANAGER_STAFF_CATEGORY_NAME_L10N_KEY, Color.MAGENTA, true, false, "ctsms-staffcategory-manager");
		createStaffCategory(INVESTIGATOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.FUCHSIA, false, false, "ctsms-staffcategory-investigator");
		createStaffCategory(AUDITOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.VIOLET, false, false, "ctsms-staffcategory-auditor");
		createStaffCategory(SCIENTIST_STAFF_CATEGORY_NAME_L10N_KEY, Color.ORCHID, true, false, "ctsms-staffcategory-scientist");
		createStaffCategory(CRA_STAFF_CATEGORY_NAME_L10N_KEY, Color.MEDIUMORCHID, false, false, "ctsms-staffcategory-cra");
		createStaffCategory(CONTRACTOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.THISTLE, true, false, "ctsms-staffcategory-contractor");
		createStaffCategory(EXTERNAL_STAFF_CATEGORY_NAME_L10N_KEY, Color.LAVENDER, false, false, "ctsms-staffcategory-external");
		createStaffCategory(NURSE_STAFF_CATEGORY_NAME_L10N_KEY, Color.MEDIUMPURPLE, true, false, "ctsms-staffcategory-nurse");
		createStaffCategory(CLERK_STAFF_CATEGORY_NAME_L10N_KEY, Color.DARKORCHID, false, false, "ctsms-staffcategory-clerk");
		createStaffCategory(STUDENT_STAFF_CATEGORY_NAME_L10N_KEY, Color.PLUM, true, false, "ctsms-staffcategory-student");
		// Leitung
		// Arzt/Ärztin
		// Study Nurse
		// Studienkoordinatorin
		// Student/in
		// Allgemeines Personal
		// Lieferanten
		// Personen
		createStaffCategory(PHYSICIAN_STAFF_CATEGORY_NAME_L10N_KEY, Color.FUCHSIA, true, false, "ctsms-staffcategory-pysician");
		createStaffCategory(COORDINATOR_STAFF_CATEGORY_NAME_L10N_KEY, Color.MEDIUMORCHID, true, false, "ctsms-staffcategory-coordinator");
		createStaffCategory(PERSONNEL_STAFF_CATEGORY_NAME_L10N_KEY, Color.THISTLE, true, false, "ctsms-staffcategory-personnel");
		createStaffCategory(PERSON_STAFF_CATEGORY_NAME_L10N_KEY, Color.LAVENDER, true, false, "ctsms-staffcategory-person");
		createStaffCategory(GRADUAND_STAFF_CATEGORY_NAME_L10N_KEY, Color.BLUEVIOLET, true, false, "ctsms-staffcategory-graduand");
		jobOutput.println("staff categories created");
	}

	private StaffCategory createStaffCategory(String nameL10nKey, Color color, boolean person, boolean organisation, String nodeStyleClass) {
		StaffCategory staffCategory = StaffCategory.Factory.newInstance();
		staffCategory.setNameL10nKey(nameL10nKey);
		staffCategory.setColor(color);
		staffCategory.setPerson(person);
		staffCategory.setOrganisation(organisation);
		staffCategory.setNodeStyleClass(nodeStyleClass);
		staffCategory = staffCategoryDao.create(staffCategory);
		return staffCategory;
	}

	private StaffStatusType createStaffStatusType(String nameL10nKey, boolean staffActive, boolean visible, boolean hideAvailability) {
		StaffStatusType statusType = StaffStatusType.Factory.newInstance();
		statusType.setNameL10nKey(nameL10nKey);
		statusType.setStaffActive(staffActive);
		statusType.setVisible(visible);
		statusType.setHideAvailability(hideAvailability);
		statusType = staffStatusTypeDao.create(statusType);
		return statusType;
	}

	private void createStaffStatusTypes() {
		createStaffStatusType("vacation", false, true, false);
		createStaffStatusType("continuing_education", false, true, false);
		createStaffStatusType("compensatory_time", false, true, false);
		createStaffStatusType("bureaucratic_affairs", false, true, false);
		createStaffStatusType("consultation", false, true, false);
		createStaffStatusType("sick_leave", false, true, false);
		createStaffStatusType("business_trip", false, true, false);
		createStaffStatusType("maternity_leave", false, true, true);
		createStaffStatusType("retirement", false, true, true);
		createStaffStatusType("unavailable", false, true, false);
		createStaffStatusType("employment", true, true, true);
		jobOutput.println("staff status types created");
	}

	private StaffTag createStaffTag(String nameL10nKey, Integer maxOccurrence, String regExp, String mismatchMsgL10nKey, boolean person, boolean organisation, boolean excel) {
		StaffTag tag = StaffTag.Factory.newInstance();
		tag.setPerson(person);
		tag.setOrganisation(organisation);
		tag.setNameL10nKey(nameL10nKey);
		tag.setMaxOccurrence(maxOccurrence);
		tag.setRegExp(regExp);
		tag.setMismatchMsgL10nKey(mismatchMsgL10nKey);
		tag.setExcel(excel);
		tag = staffTagDao.create(tag);
		return tag;
	}

	private void createStaffTags() {
		String dateRegExp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
		createStaffTag("legal_form", 1, null, null, false, true, true);
		createStaffTag("vat_number", 1, null, null, false, true, true);
		createStaffTag("commercial_reg_number", 1, null, null, false, true, true);
		createStaffTag("court_of_jurisdiction", 1, null, null, false, true, true);
		createStaffTag("function", 1, null, null, true, false, true);
		createStaffTag("description", 1, null, null, true, true, true);
		createStaffTag("social_security_number", 1, null, null, true, false, true);
		createStaffTag("badge_number", 1, null, null, false, false, true);
		createStaffTag("mug_badge_number", 1, null, null, true, false, true);
		createStaffTag("klinikum_badge_number", 1, null, null, true, false, true);
		createStaffTag("entry_date", 1, dateRegExp, ServiceExceptionCodes.INVALID_DATE_FORMAT, false, false, true);
		jobOutput.println("staff tags created");
	}

	private SurveyStatusType createSurveyStatusType(String nameL10nKey, boolean visible) {
		SurveyStatusType surveyStatusType = SurveyStatusType.Factory.newInstance();
		surveyStatusType.setNameL10nKey(nameL10nKey);
		surveyStatusType.setVisible(visible);
		surveyStatusType = surveyStatusTypeDao.create(surveyStatusType);
		return surveyStatusType;
	}

	private void createSurveyStatusTypes() {
		createSurveyStatusType("pending", true);
		createSurveyStatusType("yes", true);
		createSurveyStatusType("na", true);
		jobOutput.println("survey status types created");
	}

	private TeamMemberRole createTeamMemberRole(String nameL10nKey, Integer maxOccurrence, boolean visible) {
		TeamMemberRole role = TeamMemberRole.Factory.newInstance();
		role.setNameL10nKey(nameL10nKey);
		role.setMaxOccurrence(maxOccurrence);
		role.setVisible(visible);
		role = teamMemberRoleDao.create(role);
		return role;
	}

	private void createTeamMemberRoles() {
		createTeamMemberRole("sponsor", null, true);
		createTeamMemberRole("principal_investigator", null, true);
		// createTeamMemberRole("co_investigator",null,true);
		createTeamMemberRole("project_manager", null, true);
		createTeamMemberRole("assistant_to_project_management", null, true);
		createTeamMemberRole("student", null, true);
		createTeamMemberRole("study_nurse", null, true);
		createTeamMemberRole("quality_manager", null, true);
		createTeamMemberRole("monitor", null, true);
		createTeamMemberRole("project_staff", null, true);
		// createTeamMemberRole("academic_assistant",null,true);
		jobOutput.println("trial team member roles created");
	}

	private TimelineEventType createTimelineEventType(String nameL10nKey, Integer maxOccurrence, boolean visible, boolean showPreset, boolean notifyPreset,
			EventImportance importancePreset, String nodeStyleClass, Color color, TimelineEventTitlePresetType titlePresetType, String titlePresetL10nKey, boolean titlePresetFixed) {
		TimelineEventType type = TimelineEventType.Factory.newInstance();
		type.setNameL10nKey(nameL10nKey);
		type.setMaxOccurrence(maxOccurrence);
		type.setVisible(visible);
		type.setShowPreset(showPreset);
		type.setNotifyPreset(notifyPreset);
		type.setImportancePreset(importancePreset);
		type.setColor(color);
		type.setNodeStyleClass(nodeStyleClass);
		type.setTitlePresetType(titlePresetType);
		type.setTitlePresetL10nKey(titlePresetL10nKey);
		type.setTitlePresetFixed(titlePresetFixed);
		type = timelineEventTypeDao.create(type);
		return type;
	}

	private void createTimelineEventTypes() {
		createTimelineEventType("trial", 1, true, true, false, EventImportance.HIGH, "ctsms-icon-timeline-trial", Color.ALICEBLUE, TimelineEventTitlePresetType.TRIAL_NAME, null,
				true); // "../resources/images/timeline/blue_circle.png"
		createTimelineEventType("phase", null, true, true, false, EventImportance.LOW, "ctsms-icon-timeline-phase", Color.WHITESMOKE, TimelineEventTitlePresetType.NONE, null,
				false); // "../resources/images/timeline/orange_triangle.png"
		createTimelineEventType("deadline", null, true, true, true, EventImportance.NORMAL, "ctsms-icon-timeline-deadline", Color.SEASHELL, TimelineEventTitlePresetType.NONE,
				null, false); // "../resources/images/timeline/red_marker.png"
		createTimelineEventType("event", null, true, true, false, EventImportance.NORMAL, "ctsms-icon-timeline-event", Color.WHITESMOKE, TimelineEventTitlePresetType.NONE,
				null, false); // "../resources/images/timeline/red_marker.png"
		createTimelineEventType("phase_initiation", 1, true, true, false, EventImportance.LOW, "ctsms-icon-timeline-phase-initiation", Color.PAPAYAWHIP,
				TimelineEventTitlePresetType.TEXT, "phase_initiation_title_preset", true);
		createTimelineEventType("phase_design", 1, true, true, false, EventImportance.LOW, "ctsms-icon-timeline-phase-design", Color.OLDLACE, TimelineEventTitlePresetType.TEXT,
				"phase_design_title_preset", true);
		createTimelineEventType("phase_recruitment", 1, true, true, false, EventImportance.LOW, "ctsms-icon-timeline-phase-recruitment", Color.SKYBLUE,
				TimelineEventTitlePresetType.TEXT,
				"phase_recruitment_title_preset", true);
		createTimelineEventType("phase_carrying_out", 1, true, true, false, EventImportance.NORMAL, "ctsms-icon-timeline-phase-carrying-out", Color.GHOSTWHITE,
				TimelineEventTitlePresetType.TEXT, "phase_carrying_out_title_preset", true);
		createTimelineEventType("phase_phase_out", 1, true, true, false, EventImportance.LOW, "ctsms-icon-timeline-phase-phase-out", Color.ANTIQUEWHITE,
				TimelineEventTitlePresetType.TEXT, "phase_phase_out_title_preset", true);
		createTimelineEventType("deadline_kickoff", 1, true, true, true, EventImportance.LOW, "ctsms-icon-timeline-deadline-kickoff", Color.LIGHTYELLOW,
				TimelineEventTitlePresetType.TEXT, "deadline_kickoff_title_preset", true);
		createTimelineEventType("deadline_initiation", 1, true, true, true, EventImportance.LOW, "ctsms-icon-timeline-deadline-initiation", Color.FLORALWHITE,
				TimelineEventTitlePresetType.TEXT, "deadline_initiation_title_preset", true);
		createTimelineEventType("deadline_ethical_review_committee_submission", 1, true, true, true, EventImportance.NORMAL,
				"ctsms-icon-timeline-deadline-ethical-review-committee-submission", Color.LIGHTCORAL, TimelineEventTitlePresetType.TEXT,
				"deadline_ethical_review_committee_submission_title_preset", true);
		createTimelineEventType("deadline_ethical_review_committee_vote_expiration", null, true, true, true, EventImportance.NORMAL,
				"ctsms-icon-timeline-deadline-ethical-review-committee-vote-expiration", Color.CORAL, TimelineEventTitlePresetType.TEXT,
				"deadline_ethical_review_committee_vote_expiration_title_preset", true);
		createTimelineEventType("deadline_project_order", 1, true, true, true, EventImportance.LOWEST, "ctsms-icon-timeline-deadline-project-order", Color.SEASHELL,
				TimelineEventTitlePresetType.TEXT, "deadline_project_order_title_preset", true);
		createTimelineEventType("deadline_contract", 1, true, true, true, EventImportance.LOWEST, "ctsms-icon-timeline-deadline-contract", Color.SEASHELL,
				TimelineEventTitlePresetType.TEXT, "deadline_contract_title_preset", true);
		createTimelineEventType("deadline_inform_legal_department", null, true, true, true, EventImportance.LOWEST, "ctsms-icon-timeline-deadline-inform-legal-department",
				Color.SEASHELL, TimelineEventTitlePresetType.TEXT, "deadline_inform_legal_department_title_preset", true);
		createTimelineEventType("deadline_fpfv", 1, true, true, true, EventImportance.LOWEST, "ctsms-icon-timeline-deadline-fpfv", Color.GHOSTWHITE,
				TimelineEventTitlePresetType.TEXT, "deadline_fpfv_title_preset", true);
		createTimelineEventType("deadline_lplv", 1, true, true, true, EventImportance.LOWEST, "ctsms-icon-timeline-deadline-lplv", Color.ANTIQUEWHITE,
				TimelineEventTitlePresetType.TEXT, "deadline_lplv_title_preset", true);
		jobOutput.println("timeline event types created");
	}

	private TrialStatusAction createTrialStatusAction(org.phoenixctms.ctsms.enumeration.TrialStatusAction a) { // , long seq) {
		TrialStatusAction action = TrialStatusAction.Factory.newInstance();
		action.setAction(a);
		action = trialStatusActionDao.create(action);
		return action;
	}

	private void createTrialStatusActions() {
		org.phoenixctms.ctsms.enumeration.TrialStatusAction[] actions = org.phoenixctms.ctsms.enumeration.TrialStatusAction.values();
		for (int i = 0; i < actions.length; i++) {
			createTrialStatusAction(actions[i]); // ,i);
		}
		jobOutput.println("trial status actions created");
	}

	private TrialStatusType createTrialStatusType(String nameL10nKey,
			org.phoenixctms.ctsms.enumeration.Color color,
			String nodeStyleClass,
			boolean initial,
			boolean lockdown,
			boolean inquiryValueInputEnabled,
			boolean ecrfValueInputEnabled,
			boolean ignoreTimelineEvents,
			Set<TrialStatusAction> actions) {
		TrialStatusType trialStatusType = TrialStatusType.Factory.newInstance();
		trialStatusType.setColor(color);
		trialStatusType.setNodeStyleClass(nodeStyleClass);
		trialStatusType.setInitial(initial);
		trialStatusType.setLockdown(lockdown);
		trialStatusType.setIgnoreTimelineEvents(ignoreTimelineEvents);
		trialStatusType.setInquiryValueInputEnabled(inquiryValueInputEnabled);
		trialStatusType.setEcrfValueInputEnabled(ecrfValueInputEnabled);
		trialStatusType.setNameL10nKey(nameL10nKey);
		trialStatusType.setActions(actions);
		trialStatusType = trialStatusTypeDao.create(trialStatusType);
		return trialStatusType;
	}

	private void createTrialStatusTypeEntries() {
		TrialStatusType plannedTrialStatusType = createTrialStatusType("planned", Color.LIMEGREEN,
				"ctsms-trialstatus-planned",
				true,
				false,
				false,
				false,
				false,
				getTrialStatusActions()
				);
		TrialStatusType fixedTrialStatusType = createTrialStatusType("fixed", Color.GREENYELLOW,
				"ctsms-trialstatus-fixed",
				true,
				false,
				false,
				false,
				false,
				getTrialStatusActions()
				);
		TrialStatusType startedTrialStatusType = createTrialStatusType("started", Color.LIME,
				"ctsms-trialstatus-started",
				false,
				false,
				true,
				true,
				false,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS)
				);
		TrialStatusType closedTrialStatusType = createTrialStatusType("closed", Color.ORCHID,
				"ctsms-trialstatus-closed",
				false,
				false,
				false,
				false,
				false,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS, org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_MISSING_TRIAL_TAG)
				);
		TrialStatusType lockdownTrialStatusType = createTrialStatusType("lockdown", Color.MISTYROSE,
				"ctsms-trialstatus-lockdown",
				false,
				true,
				false,
				false,
				true,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS, org.phoenixctms.ctsms.enumeration.TrialStatusAction.SIGN_TRIAL)
				);
		TrialStatusType cancelledTrialStatusType = createTrialStatusType("cancelled", Color.LIGHTGREY,
				"ctsms-trialstatus-cancelled",
				false,
				true,
				false,
				false,
				true,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS)
				);
		TrialStatusType interruptedTrialStatusType = createTrialStatusType("interrupted", Color.TOMATO,
				"ctsms-trialstatus-interrupted",
				false,
				true,
				false,
				false,
				false,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS)
				);
		TrialStatusType migrationStartedTrialStatusType = createTrialStatusType("migration_started", Color.DARKSEAGREEN,
				"ctsms-trialstatus-migrationstarted",
				true,
				false,
				true,
				true,
				false,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS)
				);
		TrialStatusType migratedTrialStatusType = createTrialStatusType("migrated", Color.DARKGREY,
				"ctsms-trialstatus-migrated",
				false,
				false,
				false,
				false,
				false,
				getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_TRIAL_STATUS, org.phoenixctms.ctsms.enumeration.TrialStatusAction.NOTIFY_MISSING_TRIAL_TAG)
				);
		updateTrialStatusType(plannedTrialStatusType,
				getTrialStatusTransitions(plannedTrialStatusType, fixedTrialStatusType, startedTrialStatusType, cancelledTrialStatusType));
		updateTrialStatusType(fixedTrialStatusType,
				getTrialStatusTransitions(plannedTrialStatusType, fixedTrialStatusType, startedTrialStatusType, cancelledTrialStatusType));
		updateTrialStatusType(startedTrialStatusType,
				getTrialStatusTransitions(startedTrialStatusType, closedTrialStatusType, cancelledTrialStatusType, interruptedTrialStatusType));
		updateTrialStatusType(closedTrialStatusType,
				getTrialStatusTransitions(startedTrialStatusType, closedTrialStatusType, cancelledTrialStatusType, interruptedTrialStatusType, lockdownTrialStatusType));
		updateTrialStatusType(cancelledTrialStatusType,
				getTrialStatusTransitions(cancelledTrialStatusType));
		updateTrialStatusType(interruptedTrialStatusType,
				getTrialStatusTransitions(startedTrialStatusType, closedTrialStatusType, cancelledTrialStatusType, interruptedTrialStatusType));
		updateTrialStatusType(lockdownTrialStatusType,
				getTrialStatusTransitions(lockdownTrialStatusType));
		updateTrialStatusType(migrationStartedTrialStatusType,
				getTrialStatusTransitions(migrationStartedTrialStatusType, migratedTrialStatusType, cancelledTrialStatusType));
		updateTrialStatusType(migratedTrialStatusType,
				getTrialStatusTransitions(migratedTrialStatusType, migrationStartedTrialStatusType, cancelledTrialStatusType, lockdownTrialStatusType));
		jobOutput.println("trial states created");
	}

	private TrialTag createTrialTag(String nameL10nKey, Integer maxOccurrence, String regExp, String mismatchMsgL10nKey, boolean visible, boolean notifyMissing, boolean excel,
			boolean payoffs) {
		TrialTag tag = TrialTag.Factory.newInstance();
		tag.setNameL10nKey(nameL10nKey);
		tag.setMaxOccurrence(maxOccurrence);
		tag.setRegExp(regExp);
		tag.setMismatchMsgL10nKey(mismatchMsgL10nKey);
		tag.setVisible(visible);
		tag.setNotifyMissing(notifyMissing);
		tag.setExcel(excel);
		tag.setPayoffs(payoffs);
		tag = trialTagDao.create(tag);
		return tag;
	}

	private void createTrialTags() {
		createTrialTag("ethical_review_committee_number", 1, "", "", true, true, true, false);
		createTrialTag("basg_ages_file_number", 1, "", "", true, true, true, false);
		createTrialTag("eudract_reference_number", 1, "", "", true, true, true, false);
		createTrialTag("clinicaltrialsgov_id", 1, "", "", true, true, true, false);
		createTrialTag("eudamed_reference_number", 1, "", "", true, true, true, false);
		createTrialTag("billing_address", 1, "", "", true, false, false, true);
		createTrialTag("internal_order_number", 1, "", "", true, true, true, true);
		createTrialTag("copy_code", 1, "", "", true, false, true, false);
		createTrialTag("pm_id", 1, "", "", true, true, true, false);
		createTrialTag("utn", 1, "", "", true, true, true, false);
		jobOutput.println("trial tags created");
	}

	private TrialType createTrialType(String nameL10nKey, boolean visible, boolean person) {
		TrialType trialType = TrialType.Factory.newInstance();
		trialType.setNameL10nKey(nameL10nKey);
		trialType.setVisible(visible);
		trialType.setPerson(visible);
		trialType = trialTypeDao.create(trialType);
		return trialType;
	}

	private void createTrialTypes() {
		createTrialType("amg", true, true);
		createTrialType("mpg", true, true);
		createTrialType("amg_mpg", true, true);
		createTrialType("in_vitro_diagnostics", true, true);
		createTrialType("medical_procedure", true, true);
		createTrialType("human_fundamental_research", true, true);
		createTrialType("biobank", true, true);
		createTrialType("prospective", true, true);
		createTrialType("retrospective", true, true);
		createTrialType("animal_testing", true, false);
		createTrialType("other", true, true);
		createTrialType("na", true, true);
		jobOutput.println("trial types created");
	}

	private VisitType createVisitType(String nameL10nKey, Integer maxOccurrence, boolean visible, boolean travel, Color color) {
		VisitType type = VisitType.Factory.newInstance();
		type.setNameL10nKey(nameL10nKey);
		type.setMaxOccurrence(maxOccurrence);
		type.setVisible(visible);
		type.setColor(color);
		type.setTravel(travel);
		type = visitTypeDao.create(type);
		return type;
	}

	private void createVisitTypes() {
		// createVisitType("pre_screening",null,true,Color.ORANGE);
		createVisitType("screening", null, true, true, Color.TOMATO);
		createVisitType("outpatient", null, true, true, Color.KHAKI); // application
		createVisitType("inpatient", null, true, true, Color.YELLOWGREEN); // treatment
		createVisitType("phone", null, true, false, Color.OLIVE);
		createVisitType("final", null, true, true, Color.ORCHID);
		jobOutput.println("visit types created");
	}

	private HashSet<CourseParticipationStatusType> getCourseParticipationTransitions(CourseParticipationStatusType... types) {
		HashSet<CourseParticipationStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<CourseParticipationStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private HashSet<ECRFFieldStatusType> getEcrfFieldStatusTransitions(ECRFFieldStatusType... types) {
		HashSet<ECRFFieldStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<ECRFFieldStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private HashSet<ECRFStatusAction> getEcrfStatusActions(org.phoenixctms.ctsms.enumeration.ECRFStatusAction... actions) {
		HashSet<ECRFStatusAction> result = null;
		if (actions != null && actions.length > 0) {
			result = new HashSet<ECRFStatusAction>();
			for (int i = 0; i < actions.length; i++) {
				result.add(eCRFStatusActionDao.searchUniqueAction(actions[i]));
			}
		}
		return result;
	}

	private HashSet<ECRFStatusType> getEcrfStatusTransitions(ECRFStatusType... types) {
		HashSet<ECRFStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<ECRFStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private HashSet<PrivacyConsentStatusType> getPrivacyConsentStatusTransitions(PrivacyConsentStatusType... types) {
		HashSet<PrivacyConsentStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<PrivacyConsentStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private HashSet<ProbandListStatusLogLevel> getProbandListStatusLogLevels(org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel... logLevels) {
		HashSet<ProbandListStatusLogLevel> result = null;
		if (logLevels != null && logLevels.length > 0) {
			result = new HashSet<ProbandListStatusLogLevel>();
			for (int i = 0; i < logLevels.length; i++) {
				result.add(probandListStatusLogLevelDao.searchUniqueLogLevel(logLevels[i]));
			}
		}
		return result;
	}

	private HashSet<ProbandListStatusType> getProbandListStatusTransitions(ProbandListStatusType... types) {
		HashSet<ProbandListStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<ProbandListStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private HashSet<TrialStatusAction> getTrialStatusActions(org.phoenixctms.ctsms.enumeration.TrialStatusAction... actions) {
		HashSet<TrialStatusAction> result = null;
		if (actions != null && actions.length > 0) {
			result = new HashSet<TrialStatusAction>();
			for (int i = 0; i < actions.length; i++) {
				result.add(trialStatusActionDao.searchUniqueAction(actions[i]));
			}
		}
		return result;
	}

	private HashSet<TrialStatusType> getTrialStatusTransitions(TrialStatusType... types) {
		HashSet<TrialStatusType> result = null;
		if (types != null && types.length > 0) {
			result = new HashSet<TrialStatusType>();
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
		}
		return result;
	}

	private void initializeCriteriaTables() {
		createCriterionRestrictionEntries();
		jobOutput.println("search criterion restrictions created");
		createCriterionTieEntries();
		jobOutput.println("search criterion ties created");
	}

	public void initializeDB() {
		initializeCriteriaTables();
		createHyperlinkCategories();
		createJournalCategories();
		createFileFolderPresets();
		createHolidayEntries();
		createInventoryCategories();
		createInventoryTags();
		createInventoryStatusTypes();
		createMaintenanceTypes();
		createStaffCategories();
		createNotificationTypes();
		createStaffTags();
		createContactDetailTypes();
		createStaffStatusTypes();
		createAddressTypes();
		createCvSections();
		createCourseParticipationStatusTypeEntries();
		createCourseCategories();
		createLecturerCompetences();
		createTrialStatusActions();
		createTrialStatusTypeEntries();
		createTrialTypes();
		createSponsoringTypes();
		createSurveyStatusTypes();
		createTrialTags();
		createTeamMemberRoles();
		createTimelineEventTypes();
		createVisitTypes();
		createEcrfStatusActions();
		createEcrfStatusTypeEntries();
		createEcrfFieldStatusTypeEntries();
		createProbandListStatusLogLevels();
		createProbandListStatusTypeEntries();
		createPrivacyConsentStatusTypeEntries();
		createProbandCategories();
		createProbandTags();
		createProbandStatusTypes();
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	private void unlinkProbandListEntryLastStatus() throws Exception {
		ChunkedDaoOperationAdapter<ProbandListEntryDao, ProbandListEntry> probandListEntryProcessor = new ChunkedDaoOperationAdapter<ProbandListEntryDao, ProbandListEntry>(
				probandListEntryDao) {

			@Override
			protected boolean process(Collection<ProbandListEntry> page,
					Object passThrough) throws Exception {
				Iterator<ProbandListEntry> it = page.iterator();
				while (it.hasNext()) {
					it.next().setLastStatus(null);
				}
				dao.update(page);
				return true;
			}

			@Override
			protected boolean process(ProbandListEntry entity, Object passThrough)
					throws Exception {
				entity.setLastStatus(null);
				dao.update(entity);
				return true;
			}
		};
		probandListEntryProcessor.processPages(null);
	}

	private void unlinkUserIdentity() throws Exception {
		ChunkedDaoOperationAdapter<UserDao, User> userProcessor = new ChunkedDaoOperationAdapter<UserDao, User>(userDao) {

			@Override
			protected boolean process(Collection<User> page,
					Object passThrough) throws Exception {
				Iterator<User> it = page.iterator();
				while (it.hasNext()) {
					it.next().setIdentity(null);
				}
				dao.update(page);
				return true;
			}

			@Override
			protected boolean process(User entity, Object passThrough)
					throws Exception {
				entity.setIdentity(null);
				dao.update(entity);
				return true;
			}
		};
		userProcessor.processPages(null);
	}

	private void updateCourseParticipationStatusType(CourseParticipationStatusType courseParticipationType,
			Set<CourseParticipationStatusType> userTransitions,
			Set<CourseParticipationStatusType> userSelfRegistrationTransitions,
			Set<CourseParticipationStatusType> adminTransitions,
			Set<CourseParticipationStatusType> adminSelfRegistrationTransitions) {
		courseParticipationType.setUserTransitions(userTransitions);
		courseParticipationType.setUserSelfRegistrationTransitions(userSelfRegistrationTransitions);
		courseParticipationType.setAdminTransitions(adminTransitions);
		courseParticipationType.setAdminSelfRegistrationTransitions(adminSelfRegistrationTransitions);
		courseParticipationStatusTypeDao.update(courseParticipationType);
	}

	private void updateEcrfFieldStatusType(ECRFFieldStatusType ecrfFieldStatusType,
			Set<ECRFFieldStatusType> transitions) {
		ecrfFieldStatusType.setTransitions(transitions);
		eCRFFieldStatusTypeDao.update(ecrfFieldStatusType);
	}

	private void updateEcrfStatusType(ECRFStatusType ecrfStatusType,
			Set<ECRFStatusType> transitions) {
		ecrfStatusType.setTransitions(transitions);
		eCRFStatusTypeDao.update(ecrfStatusType);
	}

	private void updatePrivacyConsentStatusType(PrivacyConsentStatusType privacyConsentStatusType,
			Set<PrivacyConsentStatusType> transitions) {
		privacyConsentStatusType.setTransitions(transitions);
		privacyConsentStatusTypeDao.update(privacyConsentStatusType);
	}

	private void updateProbandListStatusType(ProbandListStatusType probandListStatusType,
			Set<ProbandListStatusType> transitions) {
		probandListStatusType.setTransitions(transitions);
		probandListStatusTypeDao.update(probandListStatusType);
	}

	private void updateTrialStatusType(TrialStatusType trialStatusType,
			Set<TrialStatusType> transitions) {
		trialStatusType.setTransitions(transitions);
		trialStatusTypeDao.update(trialStatusType);
	}
}
