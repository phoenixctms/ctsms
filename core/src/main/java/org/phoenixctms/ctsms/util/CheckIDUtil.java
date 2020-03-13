package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.*;
import org.phoenixctms.ctsms.exception.ServiceException;

public final class CheckIDUtil {

	private final static String CHECK_ENTITY_ID_METHOD_PREFIX = "check";
	private final static String CHECK_ENTITY_ID_METHOD_SUFFIX = "Id";
	private final static MethodTransfilter LOCK_MODE_METHOD_TRANSFILTER = getLockModeMethodTransfilter();
	private final static MethodTransfilter NO_LOCK_MODE_METHOD_TRANSFILTER = getNoLockModeMethodTransfilter();

	public static AddressType checkAddressTypeId(Long typeId, AddressTypeDao addressTypeDao) throws ServiceException {
		AddressType type = addressTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ADDRESS_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static AlphaId checkAlphaIdId(Long alphaIdId, AlphaIdDao alphaIdDao) throws ServiceException {
		AlphaId alphaId = alphaIdDao.load(alphaIdId);
		if (alphaId == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ALPHA_ID_ID, alphaIdId == null ? null : alphaIdId.toString());
		}
		return alphaId;
	}

	public static AspAtcCode checkAspAtcCodeId(Long aspAtcCodeId, AspAtcCodeDao aspAtcCodeDao) throws ServiceException {
		AspAtcCode aspAtcCode = aspAtcCodeDao.load(aspAtcCodeId);
		if (aspAtcCode == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ASP_ATC_CODE_ID, aspAtcCodeId == null ? null : aspAtcCodeId.toString());
		}
		return aspAtcCode;
	}

	public static Asp checkAspId(Long aspId, AspDao aspDao) throws ServiceException {
		Asp asp = aspDao.load(aspId);
		if (asp == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ASP_ID, aspId == null ? null : aspId.toString());
		}
		return asp;
	}

	public static AspSubstance checkAspSubstanceId(Long aspSubstanceId, AspSubstanceDao aspSubstanceDao) throws ServiceException {
		AspSubstance aspSubstance = aspSubstanceDao.load(aspSubstanceId);
		if (aspSubstance == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ASP_SUBSTANCE_ID, aspSubstanceId == null ? null : aspSubstanceId.toString());
		}
		return aspSubstance;
	}

	public static BankAccount checkBankAccountId(Long bankAccountId, BankAccountDao bankAccountDao) throws ServiceException {
		BankAccount bankAccount = bankAccountDao.load(bankAccountId);
		if (bankAccount == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_BANK_ACCOUNT_ID, bankAccountId == null ? null : bankAccountId.toString());
		}
		return bankAccount;
	}

	public static ContactDetailType checkContactDetailTypeId(Long typeId, ContactDetailTypeDao contactDetailTypeDao) throws ServiceException {
		ContactDetailType type = contactDetailTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CONTACT_DETAIL_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static CourseCategory checkCourseCategoryId(Long categoryId, CourseCategoryDao courseCategoryDao) throws ServiceException {
		CourseCategory category = courseCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_COURSE_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static Course checkCourseId(Long courseId, CourseDao courseDao) throws ServiceException {
		Course course = courseDao.load(courseId);
		if (course == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_COURSE_ID, courseId == null ? null : courseId.toString());
		}
		return course;
	}

	public static Course checkCourseId(Long courseId, CourseDao courseDao, LockMode lockMode) throws ServiceException {
		Course course = courseDao.load(courseId, lockMode);
		if (course == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_COURSE_ID, courseId == null ? null : courseId.toString());
		}
		return course;
	}

	public static CourseParticipationStatusEntry checkCourseParticipationStatusEntryId(Long courseParticipationId,
			CourseParticipationStatusEntryDao courseParticipationStatusEntryDao) throws ServiceException {
		CourseParticipationStatusEntry courseParticipation = courseParticipationStatusEntryDao.load(courseParticipationId);
		if (courseParticipation == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_COURSE_PARTICIPATION_STATUS_ENTRY_ID,
					courseParticipationId == null ? null : courseParticipationId.toString());
		}
		return courseParticipation;
	}

	public static CourseParticipationStatusType checkCourseParticipationStatusTypeId(Long courseParticipationStateId,
			CourseParticipationStatusTypeDao courseParticipationStatusTypeDao) throws ServiceException {
		CourseParticipationStatusType courseParticipationState = courseParticipationStatusTypeDao.load(courseParticipationStateId);
		if (courseParticipationState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_COURSE_PARTICIPATION_STATUS_TYPE_ID, courseParticipationStateId == null ? null
					: courseParticipationStateId.toString());
		}
		return courseParticipationState;
	}

	public static Criteria checkCriteriaId(Long criteriaId, CriteriaDao criteriaDao) throws ServiceException {
		Criteria criteria = criteriaDao.load(criteriaId);
		if (criteria == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CRITERIA_ID, criteriaId == null ? null : criteriaId.toString());
		}
		return criteria;
	}

	public static Criteria checkCriteriaId(Long criteriaId, CriteriaDao criteriaDao, LockMode lockMode) throws ServiceException {
		Criteria criteria = criteriaDao.load(criteriaId, lockMode);
		if (criteria == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CRITERIA_ID, criteriaId == null ? null : criteriaId.toString());
		}
		return criteria;
	}

	public static CvPosition checkCvPositionId(Long cvPositionId, CvPositionDao cvPositionDao) throws ServiceException {
		CvPosition cvPosition = cvPositionDao.load(cvPositionId);
		if (cvPosition == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CV_POSITION_ID, cvPositionId == null ? null : cvPositionId.toString());
		}
		return cvPosition;
	}

	public static CvSection checkCvSectionId(Long sectionId, CvSectionDao cvSectionDao) throws ServiceException {
		CvSection section = cvSectionDao.load(sectionId);
		if (section == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CV_SECTION_ID, sectionId == null ? null : sectionId.toString());
		}
		return section;
	}

	public static TrainingRecordSection checkTrainingRecordSectionId(Long sectionId, TrainingRecordSectionDao trainingRecordSectionDao) throws ServiceException {
		TrainingRecordSection section = trainingRecordSectionDao.load(sectionId);
		if (section == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRAINING_RECORD_SECTION_ID, sectionId == null ? null : sectionId.toString());
		}
		return section;
	}

	public static Department checkDepartmentId(Long departmentId, DepartmentDao departmentDao) throws ServiceException {
		Department department = departmentDao.load(departmentId);
		if (department == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_DEPARTMENT_ID, departmentId == null ? null : departmentId.toString());
		}
		return department;
	}

	public static Diagnosis checkDiagnosisId(Long diagnosisId, DiagnosisDao diagnosisDao) throws ServiceException {
		Diagnosis diagnosis = diagnosisDao.load(diagnosisId);
		if (diagnosis == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_DIAGNOSIS_ID, diagnosisId == null ? null : diagnosisId.toString());
		}
		return diagnosis;
	}

	public static DutyRosterTurn checkDutyRosterTurnId(Long dutyRosterTurnId, DutyRosterTurnDao dutyRosterTurnDao) throws ServiceException {
		DutyRosterTurn dutyRosterTurn = dutyRosterTurnDao.load(dutyRosterTurnId);
		if (dutyRosterTurn == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_DUTY_ROSTER_TURN_ID, dutyRosterTurnId == null ? null : dutyRosterTurnId.toString());
		}
		return dutyRosterTurn;
	}

	public static ECRFField checkEcrfFieldId(Long ecrfFieldId, ECRFFieldDao ecrfFieldDao) throws ServiceException {
		ECRFField ecrfField = ecrfFieldDao.load(ecrfFieldId);
		if (ecrfField == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_FIELD_ID, ecrfFieldId == null ? null : ecrfFieldId.toString());
		}
		return ecrfField;
	}

	public static ECRFFieldStatusEntry checkEcrfFieldStatusEntryId(Long ecrfFieldStatusEntryId, ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao) throws ServiceException {
		ECRFFieldStatusEntry ecrfFieldStatusEntry = ecrfFieldStatusEntryDao.load(ecrfFieldStatusEntryId);
		if (ecrfFieldStatusEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_FIELD_STATUS_ENTRY_ID,
					ecrfFieldStatusEntryId == null ? null : ecrfFieldStatusEntryId.toString());
		}
		return ecrfFieldStatusEntry;
	}

	public static ECRFFieldStatusType checkEcrfFieldStatusTypeId(Long ecrfFieldStateId, ECRFFieldStatusTypeDao ecrfFieldStatusTypeDao) throws ServiceException {
		ECRFFieldStatusType ecrfFieldState = ecrfFieldStatusTypeDao.load(ecrfFieldStateId);
		if (ecrfFieldState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_FIELD_STATUS_TYPE_ID, ecrfFieldStateId == null ? null : ecrfFieldStateId.toString());
		}
		return ecrfFieldState;
	}

	public static ECRFFieldValue checkEcrfFieldValueId(Long ecrfFieldValueId, ECRFFieldValueDao ecrfFieldValueDao) throws ServiceException {
		ECRFFieldValue ecrfFieldValue = ecrfFieldValueDao.load(ecrfFieldValueId);
		if (ecrfFieldValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_FIELD_VALUE_ID, ecrfFieldValueId == null ? null : ecrfFieldValueId.toString());
		}
		return ecrfFieldValue;
	}

	public static ECRF checkEcrfId(Long ecrfId, ECRFDao ecrfDao) throws ServiceException {
		ECRF ecrf = ecrfDao.load(ecrfId);
		if (ecrf == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_ID, ecrfId == null ? null : ecrfId.toString());
		}
		return ecrf;
	}

	public static ECRF checkEcrfId(Long ecrfId, ECRFDao ecrfDao, LockMode lockMode) throws ServiceException {
		ECRF ecrf = ecrfDao.load(ecrfId, lockMode);
		if (ecrf == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_ID, ecrfId == null ? null : ecrfId.toString());
		}
		return ecrf;
	}

	public static ECRFStatusType checkEcrfStatusTypeId(Long ecrfStateId, ECRFStatusTypeDao ecrfStatusTypeDao) throws ServiceException {
		ECRFStatusType ecrfState = ecrfStatusTypeDao.load(ecrfStateId);
		if (ecrfState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ECRF_STATUS_TYPE_ID, ecrfStateId == null ? null : ecrfStateId.toString());
		}
		return ecrfState;
	}

	public static Object checkEntityId(String entityName, Long id, Object dao) throws Exception {
		return AssociationPath.findMethod(CHECK_ENTITY_ID_METHOD_PREFIX + entityName + CHECK_ENTITY_ID_METHOD_SUFFIX, NO_LOCK_MODE_METHOD_TRANSFILTER, false,
				CheckIDUtil.class.getMethods()).invoke(null, id, dao);
	}

	public static Object checkEntityId(String entityName, Long id, Object dao, LockMode lockMode) throws Exception {
		return AssociationPath.findMethod(CHECK_ENTITY_ID_METHOD_PREFIX + entityName + CHECK_ENTITY_ID_METHOD_SUFFIX, LOCK_MODE_METHOD_TRANSFILTER, false,
				CheckIDUtil.class.getMethods()).invoke(null, id, dao, lockMode);
	}

	public static File checkFileId(Long fileId, FileDao fileDao) throws ServiceException {
		File file = fileDao.load(fileId);
		if (file == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_FILE_ID, fileId == null ? null : fileId.toString());
		}
		return file;
	}

	public static File checkFileId(Long fileId, FileDao fileDao, LockMode lockMode) throws ServiceException {
		File file = fileDao.load(fileId, lockMode);
		if (file == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_FILE_ID, fileId == null ? null : fileId.toString());
		}
		return file;
	}

	public static HyperlinkCategory checkHyperlinkCategoryId(Long categoryId, HyperlinkCategoryDao hyperlinkCategoryDao) throws ServiceException {
		HyperlinkCategory category = hyperlinkCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_HYPERLINK_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static Hyperlink checkHyperlinkId(Long hyperlinkId, HyperlinkDao hyperlinkDao) throws ServiceException {
		Hyperlink hyperlink = hyperlinkDao.load(hyperlinkId);
		if (hyperlink == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_HYPERLINK_ID, hyperlinkId == null ? null : hyperlinkId.toString());
		}
		return hyperlink;
	}

	public static InputField checkInputFieldId(Long inputFieldId, InputFieldDao inputFieldDao) throws ServiceException {
		InputField inputField = inputFieldDao.load(inputFieldId);
		if (inputField == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INPUT_FIELD_ID, inputFieldId == null ? null : inputFieldId.toString());
		}
		return inputField;
	}

	public static InputField checkInputFieldId(Long inputFieldId, InputFieldDao inputFieldDao, LockMode lockMode) throws ServiceException {
		InputField inputField = inputFieldDao.load(inputFieldId, lockMode);
		if (inputField == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INPUT_FIELD_ID, inputFieldId == null ? null : inputFieldId.toString());
		}
		return inputField;
	}

	public static InputFieldSelectionSetValue checkInputFieldSelectionSetValueId(Long selectionSetValueId, InputFieldSelectionSetValueDao selectionSetValueDao)
			throws ServiceException {
		InputFieldSelectionSetValue selectionSetValue = selectionSetValueDao.load(selectionSetValueId);
		if (selectionSetValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_SELECTION_SET_VALUE_ID, selectionSetValueId == null ? null : selectionSetValueId.toString());
		}
		return selectionSetValue;
	}

	public static Inquiry checkInquiryId(Long inquiryId, InquiryDao inquiryDao) throws ServiceException {
		Inquiry inquiry = inquiryDao.load(inquiryId);
		if (inquiry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INQUIRY_ID, inquiryId == null ? null : inquiryId.toString());
		}
		return inquiry;
	}

	public static InquiryValue checkInquiryValueId(Long inquiryValueId, InquiryValueDao inquiryValueDao) throws ServiceException {
		InquiryValue inquiryValue = inquiryValueDao.load(inquiryValueId);
		if (inquiryValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INQUIRY_VALUE_ID, inquiryValueId == null ? null : inquiryValueId.toString());
		}
		return inquiryValue;
	}

	public static InventoryBooking checkInventoryBookingId(Long bookingId, InventoryBookingDao inventoryBookingDao) throws ServiceException {
		InventoryBooking booking = inventoryBookingDao.load(bookingId);
		if (booking == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_BOOKING_ID, bookingId == null ? null : bookingId.toString());
		}
		return booking;
	}

	public static InventoryCategory checkInventoryCategoryId(Long categoryId, InventoryCategoryDao inventoryCategoryDao) throws ServiceException {
		InventoryCategory category = inventoryCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static Inventory checkInventoryId(Long inventoryId, InventoryDao inventoryDao) throws ServiceException {
		Inventory inventory = inventoryDao.load(inventoryId);
		if (inventory == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_ID, inventoryId == null ? null : inventoryId.toString());
		}
		return inventory;
	}

	public static Inventory checkInventoryId(Long inventoryId, InventoryDao inventoryDao, LockMode lockMode) throws ServiceException {
		Inventory inventory = inventoryDao.load(inventoryId, lockMode);
		if (inventory == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_ID, inventoryId == null ? null : inventoryId.toString());
		}
		return inventory;
	}

	public static InventoryStatusEntry checkInventoryStatusEntryId(Long statusEntryId, InventoryStatusEntryDao inventoryStatusEntryDao) throws ServiceException {
		InventoryStatusEntry statusEntry = inventoryStatusEntryDao.load(statusEntryId);
		if (statusEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_STATUS_ENTRY_ID, statusEntryId == null ? null : statusEntryId.toString());
		}
		return statusEntry;
	}

	public static InventoryStatusType checkInventoryStatusTypeId(Long typeId, InventoryStatusTypeDao inventoryStatusTypeDao) throws ServiceException {
		InventoryStatusType statusType = inventoryStatusTypeDao.load(typeId);
		if (statusType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_STATUS_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return statusType;
	}

	public static InventoryTag checkInventoryTagId(Long tagId, InventoryTagDao inventoryTagDao) throws ServiceException {
		InventoryTag tag = inventoryTagDao.load(tagId);
		if (tag == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_TAG_ID, tagId == null ? null : tagId.toString());
		}
		return tag;
	}

	public static InventoryTagValue checkInventoryTagValueId(Long tagValueId, InventoryTagValueDao inventoryTagValueDao) throws ServiceException {
		InventoryTagValue tagValue = inventoryTagValueDao.load(tagValueId);
		if (tagValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INVENTORY_TAG_VALUE_ID, tagValueId == null ? null : tagValueId.toString());
		}
		return tagValue;
	}

	public static JournalCategory checkJournalCategoryId(Long categoryId, JournalCategoryDao journalCategoryDao) throws ServiceException {
		JournalCategory category = journalCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_JOURNAL_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static JournalEntry checkJournalEntryId(Long journalEntryId, JournalEntryDao journalEntryDao) throws ServiceException {
		JournalEntry journalEntry = journalEntryDao.load(journalEntryId);
		if (journalEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_JOURNAL_ENTRY_ID, journalEntryId == null ? null : journalEntryId.toString());
		}
		return journalEntry;
	}

	public static LecturerCompetence checkLecturerCompetenceId(Long competenceId, LecturerCompetenceDao lecturerCompetenceDao) throws ServiceException {
		LecturerCompetence competence = lecturerCompetenceDao.load(competenceId);
		if (competence == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_LECTURER_COMPETENCE_ID, competenceId == null ? null : competenceId.toString());
		}
		return competence;
	}

	public static Lecturer checkLecturerId(Long lecturerId, LecturerDao lecturerDao) throws ServiceException {
		Lecturer lecturer = lecturerDao.load(lecturerId);
		if (lecturer == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_LECTURER_ID, lecturerId == null ? null : lecturerId.toString());
		}
		return lecturer;
	}

	public static MaintenanceScheduleItem checkMaintenanceScheduleItemId(Long maintenanceItemId, MaintenanceScheduleItemDao maintenanceScheduleItemDao) throws ServiceException {
		MaintenanceScheduleItem maintenanceItem = maintenanceScheduleItemDao.load(maintenanceItemId);
		if (maintenanceItem == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MAINTENANCE_SCHEDULE_ITEM_ID, maintenanceItemId == null ? null : maintenanceItemId.toString());
		}
		return maintenanceItem;
	}

	public static MaintenanceType checkMaintenanceTypeId(Long typeId, MaintenanceTypeDao maintenanceTypeDao) throws ServiceException {
		MaintenanceType maintenanceType = maintenanceTypeDao.load(typeId);
		if (maintenanceType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MAINTENANCE_ITEM_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return maintenanceType;
	}

	public static MassMail checkMassMailId(Long massMailId, MassMailDao massMailDao) throws ServiceException {
		MassMail massMail = massMailDao.load(massMailId);
		if (massMail == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_ID, massMailId == null ? null : massMailId.toString());
		}
		return massMail;
	}

	public static MassMail checkMassMailId(Long massMailId, MassMailDao massMailDao, LockMode lockMode) throws ServiceException {
		MassMail massMail = massMailDao.load(massMailId, lockMode);
		if (massMail == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_ID, massMailId == null ? null : massMailId.toString());
		}
		return massMail;
	}

	public static MassMailRecipient checkMassMailRecipientId(Long massMailRecipientId, MassMailRecipientDao massMailRecipientDao) throws ServiceException {
		MassMailRecipient recipient = massMailRecipientDao.load(massMailRecipientId);
		if (recipient == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_RECIPIENT_ID, massMailRecipientId == null ? null : massMailRecipientId.toString());
		}
		return recipient;
	}

	public static MassMailRecipient checkMassMailRecipientId(Long massMailRecipientId, MassMailRecipientDao massMailRecipientDao, LockMode lockMode) throws ServiceException {
		MassMailRecipient recipient = massMailRecipientDao.load(massMailRecipientId, lockMode);
		if (recipient == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_RECIPIENT_ID, massMailRecipientId == null ? null : massMailRecipientId.toString());
		}
		return recipient;
	}

	public static MassMailStatusType checkMassMailStatusTypeId(Long massMailStateId, MassMailStatusTypeDao massMailStatusTypeDao) throws ServiceException {
		MassMailStatusType massMailState = massMailStatusTypeDao.load(massMailStateId);
		if (massMailState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_STATUS_TYPE_ID, massMailStateId == null ? null : massMailStateId.toString());
		}
		return massMailState;
	}

	public static MassMailType checkMassMailTypeId(Long typeId, MassMailTypeDao massMailTypeDao) throws ServiceException {
		MassMailType type = massMailTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MASS_MAIL_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static Medication checkMedicationId(Long medicationId, MedicationDao medicationDao) throws ServiceException {
		Medication medication = medicationDao.load(medicationId);
		if (medication == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MEDICATION_ID, medicationId == null ? null : medicationId.toString());
		}
		return medication;
	}

	public static MoneyTransfer checkMoneyTransferId(Long moneyTransferId, MoneyTransferDao moneyTransferDao) throws ServiceException {
		MoneyTransfer moneyTransfer = moneyTransferDao.load(moneyTransferId);
		if (moneyTransfer == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_MONEY_TRANSFER_ID, moneyTransferId == null ? null : moneyTransferId.toString());
		}
		return moneyTransfer;
	}

	public static Notification checkNotificationId(Long notificationId, NotificationDao notificationDao) throws ServiceException {
		Notification notification = notificationDao.load(notificationId);
		if (notification == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NOTIFICATION_ID, notificationId == null ? null : notificationId.toString());
		}
		return notification;
	}

	public static OpsCode checkOpsCodeId(Long opsCodeId, OpsCodeDao opsCodeDao) throws ServiceException {
		OpsCode opsCode = opsCodeDao.load(opsCodeId);
		if (opsCode == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_OPS_CODE_ID, opsCodeId == null ? null : opsCodeId.toString());
		}
		return opsCode;
	}

	public static PrivacyConsentStatusType checkPrivacyConsentStatusTypeId(Long privacyConsentStateId, PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao)
			throws ServiceException {
		PrivacyConsentStatusType privacyConsentState = privacyConsentStatusTypeDao.load(privacyConsentStateId);
		if (privacyConsentState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PRIVACY_CONSENT_STATUS_TYPE_ID,
					privacyConsentStateId == null ? null : privacyConsentStateId.toString());
		}
		return privacyConsentState;
	}

	public static ProbandAddress checkProbandAddressId(Long addressId, ProbandAddressDao probandAddressDao) throws ServiceException {
		ProbandAddress address = probandAddressDao.load(addressId);
		if (address == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_ADDRESS_ID, addressId == null ? null : addressId.toString());
		}
		return address;
	}

	public static ProbandCategory checkProbandCategoryId(Long categoryId, ProbandCategoryDao probandCategoryDao) throws ServiceException {
		ProbandCategory category = probandCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static ProbandContactDetailValue checkProbandContactDetailValueId(Long contactValueId, ProbandContactDetailValueDao probandContactDetailValueDao)
			throws ServiceException {
		ProbandContactDetailValue contactValue = probandContactDetailValueDao.load(contactValueId);
		if (contactValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_CONTACT_DETAIL_VALUE_ID, contactValueId == null ? null : contactValueId.toString());
		}
		return contactValue;
	}

	public static ProbandGroup checkProbandGroupId(Long probandGroupId, ProbandGroupDao probandGroupDao) throws ServiceException {
		ProbandGroup probandGroup = probandGroupDao.load(probandGroupId);
		if (probandGroup == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_GROUP_ID, probandGroupId == null ? null : probandGroupId.toString());
		}
		return probandGroup;
	}

	public static Proband checkProbandId(Long probandId, ProbandDao probandDao) throws ServiceException {
		Proband proband = probandDao.load(probandId);
		if (proband == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_ID, probandId == null ? null : probandId.toString());
		}
		return proband;
	}

	public static Proband checkProbandId(Long probandId, ProbandDao probandDao, LockMode lockMode) throws ServiceException {
		Proband proband = probandDao.load(probandId, lockMode);
		if (proband == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_ID, probandId == null ? null : probandId.toString());
		}
		return proband;
	}

	public static ProbandListEntry checkProbandListEntryId(Long probandListEntryId, ProbandListEntryDao probandListEntryDao) throws ServiceException {
		ProbandListEntry probandListEntry = probandListEntryDao.load(probandListEntryId);
		if (probandListEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_ENTRY_ID, probandListEntryId == null ? null : probandListEntryId.toString());
		}
		return probandListEntry;
	}

	public static ProbandListEntry checkProbandListEntryId(Long probandListEntryId, ProbandListEntryDao probandListEntryDao, LockMode lockMode) throws ServiceException {
		ProbandListEntry probandListEntry = probandListEntryDao.load(probandListEntryId, lockMode);
		if (probandListEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_ENTRY_ID, probandListEntryId == null ? null : probandListEntryId.toString());
		}
		return probandListEntry;
	}

	public static ProbandListEntryTag checkProbandListEntryTagId(Long probandListEntryTagId, ProbandListEntryTagDao probandListEntryTagDao) throws ServiceException {
		ProbandListEntryTag probandListEntryTag = probandListEntryTagDao.load(probandListEntryTagId);
		if (probandListEntryTag == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_ENTRY_TAG_ID, probandListEntryTagId == null ? null : probandListEntryTagId.toString());
		}
		return probandListEntryTag;
	}

	public static ProbandListEntryTagValue checkProbandListEntryTagValueId(Long probandListEntryTagValueId, ProbandListEntryTagValueDao probandListEntryTagValueDao)
			throws ServiceException {
		ProbandListEntryTagValue probandListEntryTagValue = probandListEntryTagValueDao.load(probandListEntryTagValueId);
		if (probandListEntryTagValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_ENTRY_TAG_VALUE_ID, probandListEntryTagValueId == null ? null
					: probandListEntryTagValueId.toString());
		}
		return probandListEntryTagValue;
	}

	public static ProbandListStatusEntry checkProbandListStatusEntryId(Long probandListStatusEntryId, ProbandListStatusEntryDao probandListStatusEntryDao) throws ServiceException {
		ProbandListStatusEntry probandListStatusEntry = probandListStatusEntryDao.load(probandListStatusEntryId);
		if (probandListStatusEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_STATUS_ENTRY_ID,
					probandListStatusEntryId == null ? null : probandListStatusEntryId.toString());
		}
		return probandListStatusEntry;
	}

	public static ProbandListStatusType checkProbandListStatusTypeId(Long probandListStatusTypeId, ProbandListStatusTypeDao probandListStatusTypeDao) throws ServiceException {
		ProbandListStatusType probandListStatusType = probandListStatusTypeDao.load(probandListStatusTypeId);
		if (probandListStatusType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_LIST_STATUS_TYPE_ID,
					probandListStatusTypeId == null ? null : probandListStatusTypeId.toString());
		}
		return probandListStatusType;
	}

	public static ProbandStatusEntry checkProbandStatusEntryId(Long statusEntryId, ProbandStatusEntryDao probandStatusEntryDao) throws ServiceException {
		ProbandStatusEntry statusEntry = probandStatusEntryDao.load(statusEntryId);
		if (statusEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_STATUS_ENTRY_ID, statusEntryId == null ? null : statusEntryId.toString());
		}
		return statusEntry;
	}

	public static ProbandStatusType checkProbandStatusTypeId(Long typeId, ProbandStatusTypeDao probandStatusTypeDao) throws ServiceException {
		ProbandStatusType statusType = probandStatusTypeDao.load(typeId);
		if (statusType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_STATUS_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return statusType;
	}

	public static ProbandTag checkProbandTagId(Long tagId, ProbandTagDao probandTagDao) throws ServiceException {
		ProbandTag tag = probandTagDao.load(tagId);
		if (tag == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_TAG_ID, tagId == null ? null : tagId.toString());
		}
		return tag;
	}

	public static ProbandTagValue checkProbandTagValueId(Long tagValueId, ProbandTagValueDao probandTagValueDao) throws ServiceException {
		ProbandTagValue tagValue = probandTagValueDao.load(tagValueId);
		if (tagValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_TAG_VALUE_ID, tagValueId == null ? null : tagValueId.toString());
		}
		return tagValue;
	}

	public static Procedure checkProcedureId(Long procedureId, ProcedureDao procedureDao) throws ServiceException {
		Procedure procedure = procedureDao.load(procedureId);
		if (procedure == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROCEDURE_ID, procedureId == null ? null : procedureId.toString());
		}
		return procedure;
	}

	public static SponsoringType checkSponsoringTypeId(Long typeId, SponsoringTypeDao sponsoringTypeDao) throws ServiceException {
		SponsoringType type = sponsoringTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_SPONSORING_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static StaffAddress checkStaffAddressId(Long addressId, StaffAddressDao staffAddressDao) throws ServiceException {
		StaffAddress address = staffAddressDao.load(addressId);
		if (address == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_ADDRESS_ID, addressId == null ? null : addressId.toString());
		}
		return address;
	}

	public static StaffCategory checkStaffCategoryId(Long categoryId, StaffCategoryDao staffCategoryDao) throws ServiceException {
		StaffCategory category = staffCategoryDao.load(categoryId);
		if (category == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_CATEGORY_ID, categoryId == null ? null : categoryId.toString());
		}
		return category;
	}

	public static StaffContactDetailValue checkStaffContactDetailValueId(Long contactValueId, StaffContactDetailValueDao staffContactDetailValueDao) throws ServiceException {
		StaffContactDetailValue contactValue = staffContactDetailValueDao.load(contactValueId);
		if (contactValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_CONTACT_DETAIL_VALUE_ID, contactValueId == null ? null : contactValueId.toString());
		}
		return contactValue;
	}

	public static Staff checkStaffId(Long staffId, StaffDao staffDao) throws ServiceException {
		Staff staff = staffDao.load(staffId);
		if (staff == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_ID, staffId == null ? null : staffId.toString());
		}
		return staff;
	}

	public static Staff checkStaffId(Long staffId, StaffDao staffDao, LockMode lockMode) throws ServiceException {
		Staff staff = staffDao.load(staffId, lockMode);
		if (staff == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_ID, staffId == null ? null : staffId.toString());
		}
		return staff;
	}

	public static StaffStatusEntry checkStaffStatusEntryId(Long statusEntryId, StaffStatusEntryDao staffStatusEntryDao) throws ServiceException {
		StaffStatusEntry statusEntry = staffStatusEntryDao.load(statusEntryId);
		if (statusEntry == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_STATUS_ENTRY_ID, statusEntryId == null ? null : statusEntryId.toString());
		}
		return statusEntry;
	}

	public static StaffStatusType checkStaffStatusTypeId(Long typeId, StaffStatusTypeDao staffStatusTypeDao) throws ServiceException {
		StaffStatusType statusType = staffStatusTypeDao.load(typeId);
		if (statusType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_STATUS_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return statusType;
	}

	public static StaffTag checkStaffTagId(Long tagId, StaffTagDao staffTagDao) throws ServiceException {
		StaffTag tag = staffTagDao.load(tagId);
		if (tag == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_TAG_ID, tagId == null ? null : tagId.toString());
		}
		return tag;
	}

	public static StaffTagValue checkStaffTagValueId(Long tagValueId, StaffTagValueDao staffTagValueDao) throws ServiceException {
		StaffTagValue tagValue = staffTagValueDao.load(tagValueId);
		if (tagValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STAFF_TAG_VALUE_ID, tagValueId == null ? null : tagValueId.toString());
		}
		return tagValue;
	}

	public static StratificationRandomizationList checkStratificationRandomizationListId(Long stratificationRandomizationListId,
			StratificationRandomizationListDao stratificationRandomizationListDao) throws ServiceException {
		StratificationRandomizationList stratificationRandomizationList = stratificationRandomizationListDao.load(stratificationRandomizationListId);
		if (stratificationRandomizationList == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_STRATIFICATION_RANDOMIZATION_LIST_ID,
					stratificationRandomizationListId == null ? null : stratificationRandomizationListId.toString());
		}
		return stratificationRandomizationList;
	}

	public static RandomizationListCode checkRandomizationListCodeId(Long randomizationListCodeId,
			RandomizationListCodeDao randomizationListCodeDao) throws ServiceException {
		RandomizationListCode randomizationListCode = randomizationListCodeDao.load(randomizationListCodeId);
		if (randomizationListCode == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_RANDOMIZATION_LIST_CODE_ID,
					randomizationListCodeId == null ? null : randomizationListCodeId.toString());
		}
		return randomizationListCode;
	}

	public static SurveyStatusType checkSurveyStatusTypeId(Long typeId, SurveyStatusTypeDao surveyStatusTypeDao) throws ServiceException {
		SurveyStatusType type = surveyStatusTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_SURVEY_STATUS_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static TeamMember checkTeamMemberId(Long teamMemberId, TeamMemberDao teamMemberDao) throws ServiceException {
		TeamMember teamMember = teamMemberDao.load(teamMemberId);
		if (teamMember == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TEAM_MEMBER_ID, teamMemberId == null ? null : teamMemberId.toString());
		}
		return teamMember;
	}

	public static TeamMemberRole checkTeamMemberRoleId(Long roleId, TeamMemberRoleDao teamMemberRoleDao) throws ServiceException {
		TeamMemberRole role = teamMemberRoleDao.load(roleId);
		if (role == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TEAM_MEMBER_ROLE_ID, roleId == null ? null : roleId.toString());
		}
		return role;
	}

	public static TimelineEvent checkTimelineEventId(Long timelineEventId, TimelineEventDao timelineEventDao) throws ServiceException {
		TimelineEvent timelineEvent = timelineEventDao.load(timelineEventId);
		if (timelineEvent == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TIMELINE_EVENT_ID, timelineEventId == null ? null : timelineEventId.toString());
		}
		return timelineEvent;
	}

	public static TimelineEventType checkTimelineEventTypeId(Long typeId, TimelineEventTypeDao timelineEventTypeDao) throws ServiceException {
		TimelineEventType type = timelineEventTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TIMELINE_EVENT_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static Trial checkTrialId(Long trialId, TrialDao trialDao) throws ServiceException {
		Trial trial = trialDao.load(trialId);
		if (trial == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_ID, trialId == null ? null : trialId.toString());
		}
		return trial;
	}

	public static Trial checkTrialId(Long trialId, TrialDao trialDao, LockMode lockMode) throws ServiceException {
		Trial trial = trialDao.load(trialId, lockMode);
		if (trial == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_ID, trialId == null ? null : trialId.toString());
		}
		return trial;
	}

	public static TrialStatusType checkTrialStatusTypeId(Long trialStateId, TrialStatusTypeDao trialStatusTypeDao) throws ServiceException {
		TrialStatusType trialState = trialStatusTypeDao.load(trialStateId);
		if (trialState == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_STATUS_TYPE_ID, trialStateId == null ? null : trialStateId.toString());
		}
		return trialState;
	}

	public static TrialTag checkTrialTagId(Long tagId, TrialTagDao trialTagDao) throws ServiceException {
		TrialTag tag = trialTagDao.load(tagId);
		if (tag == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_TAG_ID, tagId == null ? null : tagId.toString());
		}
		return tag;
	}

	public static TrialTagValue checkTrialTagValueId(Long tagValueId, TrialTagValueDao trialTagValueDao) throws ServiceException {
		TrialTagValue tagValue = trialTagValueDao.load(tagValueId);
		if (tagValue == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_TAG_VALUE_ID, tagValueId == null ? null : tagValueId.toString());
		}
		return tagValue;
	}

	public static TrialType checkTrialTypeId(Long typeId, TrialTypeDao trialTypeDao) throws ServiceException {
		TrialType type = trialTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRIAL_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static User checkUserId(Long userId, UserDao userDao) throws ServiceException {
		User user = userDao.load(userId);
		if (user == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_USER_ID, userId == null ? null : userId.toString());
		}
		return user;
	}

	public static User checkUserId(Long userId, UserDao userDao, LockMode lockMode) throws ServiceException {
		User user = userDao.load(userId, lockMode);
		if (user == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_USER_ID, userId == null ? null : userId.toString());
		}
		return user;
	}

	public static UserPermissionProfile checkUserPermissionProfileId(Long userPermissionProfileId, UserPermissionProfileDao userPermissionProfileDao) throws ServiceException {
		UserPermissionProfile userPermissionProfile = userPermissionProfileDao.load(userPermissionProfileId);
		if (userPermissionProfile == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_USER_PERMISSION_PROFILE_ID,
					userPermissionProfileId == null ? null : userPermissionProfileId.toString());
		}
		return userPermissionProfile;
	}

	public static Visit checkVisitId(Long visitId, VisitDao visitDao) throws ServiceException {
		Visit visit = visitDao.load(visitId);
		if (visit == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_VISIT_ID, visitId == null ? null : visitId.toString());
		}
		return visit;
	}

	public static VisitScheduleItem checkVisitScheduleItemId(Long visitScheduleItemId, VisitScheduleItemDao visitScheduleItemDao) throws ServiceException {
		VisitScheduleItem visitScheduleItem = visitScheduleItemDao.load(visitScheduleItemId);
		if (visitScheduleItem == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_VISIT_SCHEDULE_ITEM_ID, visitScheduleItemId == null ? null : visitScheduleItemId.toString());
		}
		return visitScheduleItem;
	}

	public static VisitType checkVisitTypeId(Long typeId, VisitTypeDao visitTypeDao) throws ServiceException {
		VisitType type = visitTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_VISIT_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static JobType checkJobTypeId(Long typeId, JobTypeDao jobTypeDao) throws ServiceException {
		JobType type = jobTypeDao.load(typeId);
		if (type == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_JOB_TYPE_ID, typeId == null ? null : typeId.toString());
		}
		return type;
	}

	public static Job checkJobId(Long jobId, JobDao jobDao) throws ServiceException {
		Job job = jobDao.load(jobId);
		if (job == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_JOB_ID, jobId == null ? null : jobId.toString());
		}
		return job;
	}

	private final static MethodTransfilter getLockModeMethodTransfilter() {
		return new MethodTransfilter() {

			@Override
			public boolean include(Method method) {
				return hasLockModeParameter(method);
			}

			@Override
			public String transform(String methodName) {
				return methodName.toLowerCase();
			}
		};
	}

	private final static MethodTransfilter getNoLockModeMethodTransfilter() {
		return new MethodTransfilter() {

			@Override
			public boolean exclude(Method method) {
				return hasLockModeParameter(method);
			}

			@Override
			public String transform(String methodName) {
				return methodName.toLowerCase();
			}
		};
	}

	private final static boolean hasLockModeParameter(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		return parameterTypes.length > 0 && LockMode.class.equals(parameterTypes[parameterTypes.length - 1]);
	}

	private CheckIDUtil() {
	}
}
