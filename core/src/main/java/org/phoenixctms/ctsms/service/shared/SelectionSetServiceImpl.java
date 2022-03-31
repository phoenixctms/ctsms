// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::SelectionSetService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.phoenixctms.ctsms.adapt.InventoryTagAdapter;
import org.phoenixctms.ctsms.adapt.LecturerCompetenceTagAdapter;
import org.phoenixctms.ctsms.adapt.ProbandAddressTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.ProbandContactDetailTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.ProbandTagAdapter;
import org.phoenixctms.ctsms.adapt.StaffAddressTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.StaffContactDetailTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.StaffTagAdapter;
import org.phoenixctms.ctsms.adapt.TeamMemberRoleTagAdapter;
import org.phoenixctms.ctsms.adapt.TimelineEventTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.TrialTagAdapter;
import org.phoenixctms.ctsms.adapt.VisitTypeTagAdapter;
import org.phoenixctms.ctsms.domain.AddressType;
import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.AlphaId;
import org.phoenixctms.ctsms.domain.AlphaIdDao;
import org.phoenixctms.ctsms.domain.Asp;
import org.phoenixctms.ctsms.domain.AspAtcCode;
import org.phoenixctms.ctsms.domain.AspAtcCodeDao;
import org.phoenixctms.ctsms.domain.AspDao;
import org.phoenixctms.ctsms.domain.AspSubstance;
import org.phoenixctms.ctsms.domain.AspSubstanceDao;
import org.phoenixctms.ctsms.domain.BankIdentificationDao;
import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.CountryDao;
import org.phoenixctms.ctsms.domain.CourseCategoryDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.CvSection;
import org.phoenixctms.ctsms.domain.CvSectionDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusType;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusTypeDao;
import org.phoenixctms.ctsms.domain.ECRFStatusType;
import org.phoenixctms.ctsms.domain.ECRFStatusTypeDao;
import org.phoenixctms.ctsms.domain.HyperlinkCategory;
import org.phoenixctms.ctsms.domain.HyperlinkCategoryDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InventoryCategoryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusType;
import org.phoenixctms.ctsms.domain.InventoryStatusTypeDao;
import org.phoenixctms.ctsms.domain.InventoryTag;
import org.phoenixctms.ctsms.domain.InventoryTagDao;
import org.phoenixctms.ctsms.domain.JobType;
import org.phoenixctms.ctsms.domain.JobTypeDao;
import org.phoenixctms.ctsms.domain.JournalCategory;
import org.phoenixctms.ctsms.domain.JournalCategoryDao;
import org.phoenixctms.ctsms.domain.LecturerCompetence;
import org.phoenixctms.ctsms.domain.LecturerCompetenceDao;
import org.phoenixctms.ctsms.domain.MaintenanceType;
import org.phoenixctms.ctsms.domain.MaintenanceTypeDao;
import org.phoenixctms.ctsms.domain.MassMailStatusType;
import org.phoenixctms.ctsms.domain.MassMailStatusTypeDao;
import org.phoenixctms.ctsms.domain.MassMailType;
import org.phoenixctms.ctsms.domain.MassMailTypeDao;
import org.phoenixctms.ctsms.domain.NotificationType;
import org.phoenixctms.ctsms.domain.NotificationTypeDao;
import org.phoenixctms.ctsms.domain.OpsCode;
import org.phoenixctms.ctsms.domain.OpsCodeDao;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusType;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao;
import org.phoenixctms.ctsms.domain.ProbandCategory;
import org.phoenixctms.ctsms.domain.ProbandCategoryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao;
import org.phoenixctms.ctsms.domain.ProbandStatusType;
import org.phoenixctms.ctsms.domain.ProbandStatusTypeDao;
import org.phoenixctms.ctsms.domain.ProbandTag;
import org.phoenixctms.ctsms.domain.ProbandTagDao;
import org.phoenixctms.ctsms.domain.SponsoringType;
import org.phoenixctms.ctsms.domain.SponsoringTypeDao;
import org.phoenixctms.ctsms.domain.StaffCategoryDao;
import org.phoenixctms.ctsms.domain.StaffStatusType;
import org.phoenixctms.ctsms.domain.StaffStatusTypeDao;
import org.phoenixctms.ctsms.domain.StaffTag;
import org.phoenixctms.ctsms.domain.StaffTagDao;
import org.phoenixctms.ctsms.domain.StreetDao;
import org.phoenixctms.ctsms.domain.SurveyStatusType;
import org.phoenixctms.ctsms.domain.SurveyStatusTypeDao;
import org.phoenixctms.ctsms.domain.TeamMemberRole;
import org.phoenixctms.ctsms.domain.TeamMemberRoleDao;
import org.phoenixctms.ctsms.domain.TimelineEventType;
import org.phoenixctms.ctsms.domain.TimelineEventTypeDao;
import org.phoenixctms.ctsms.domain.TrainingRecordSection;
import org.phoenixctms.ctsms.domain.TrainingRecordSectionDao;
import org.phoenixctms.ctsms.domain.TrialStatusType;
import org.phoenixctms.ctsms.domain.TrialStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialTag;
import org.phoenixctms.ctsms.domain.TrialTagDao;
import org.phoenixctms.ctsms.domain.TrialType;
import org.phoenixctms.ctsms.domain.TrialTypeDao;
import org.phoenixctms.ctsms.domain.VisitType;
import org.phoenixctms.ctsms.domain.VisitTypeDao;
import org.phoenixctms.ctsms.domain.ZipDao;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.ECRFValidationStatus;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.OTPAuthenticatorType;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.AlphaIdVO;
import org.phoenixctms.ctsms.vo.AspAtcCodeVO;
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.vo.AspVO;
import org.phoenixctms.ctsms.vo.AuthenticationTypeVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.BankIdentificationVO;
import org.phoenixctms.ctsms.vo.BooleanVO;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.CountryVO;
import org.phoenixctms.ctsms.vo.CourseCategoryVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionRestrictionVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DBModuleVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFValidationStatusVO;
import org.phoenixctms.ctsms.vo.EventImportanceVO;
import org.phoenixctms.ctsms.vo.HyperlinkCategoryVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.InventoryCategoryVO;
import org.phoenixctms.ctsms.vo.InventoryStatusTypeVO;
import org.phoenixctms.ctsms.vo.InventoryTagVO;
import org.phoenixctms.ctsms.vo.JobStatusVO;
import org.phoenixctms.ctsms.vo.JobTypeVO;
import org.phoenixctms.ctsms.vo.JournalCategoryVO;
import org.phoenixctms.ctsms.vo.JournalModuleVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LightECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.LightInputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.LightInquiryOutVO;
import org.phoenixctms.ctsms.vo.LightProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.LocaleVO;
import org.phoenixctms.ctsms.vo.MaintenanceTypeVO;
import org.phoenixctms.ctsms.vo.MassMailStatusTypeVO;
import org.phoenixctms.ctsms.vo.MassMailTypeVO;
import org.phoenixctms.ctsms.vo.NotificationTypeVO;
import org.phoenixctms.ctsms.vo.OTPAuthenticatorTypeVO;
import org.phoenixctms.ctsms.vo.OpsCodeVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.PrivacyConsentStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandCategoryVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandTagVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.SponsoringTypeVO;
import org.phoenixctms.ctsms.vo.StaffCategoryVO;
import org.phoenixctms.ctsms.vo.StaffStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StreetVO;
import org.phoenixctms.ctsms.vo.SurveyStatusTypeVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.vo.TimelineEventTypeVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialTagVO;
import org.phoenixctms.ctsms.vo.TrialTypeVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.vo.VisitScheduleDateModeVO;
import org.phoenixctms.ctsms.vo.VisitTypeVO;
import org.phoenixctms.ctsms.vo.ZipVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.SelectionSetService
 */
public class SelectionSetServiceImpl
		extends SelectionSetServiceBase {

	private static NotificationType checkNotificationTypeId(Long notificationTypeId, NotificationTypeDao notificationTypeDao) throws ServiceException {
		NotificationType notificationType = notificationTypeDao.load(notificationTypeId);
		if (notificationType == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NOTIFICATION_TYPE_ID, notificationTypeId == null ? null : notificationTypeId.toString());
		}
		return notificationType;
	}

	private Collection<HyperlinkCategoryVO> getHyperlinkCategoriesHelper(HyperlinkModule module, Long categoryId) throws Exception {
		HyperlinkCategoryDao hyperlinkCategoryDao = this.getHyperlinkCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkHyperlinkCategoryId(categoryId, hyperlinkCategoryDao);
		}
		Collection categories = hyperlinkCategoryDao.findByModuleVisibleId(module, true, categoryId);
		hyperlinkCategoryDao.toHyperlinkCategoryVOCollection(categories);
		return categories;
	}

	private Collection<JobTypeVO> getJobTypesHelper(JobModule module, Long typeId, Long trialId) throws Exception {
		JobTypeDao jobTypeDao = this.getJobTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkJobTypeId(typeId, jobTypeDao);
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		Collection jobs = jobTypeDao.findByModuleTrialVisibleId(module, trialId, true, typeId);
		jobTypeDao.toJobTypeVOCollection(jobs);
		return jobs;
	}

	private Collection<JournalCategoryVO> getJournalCategoriesHelper(JournalModule module, Long categoryId) throws Exception {
		JournalCategoryDao journalCategoryDao = this.getJournalCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkJournalCategoryId(categoryId, journalCategoryDao);
		}
		Collection categories = journalCategoryDao.findByModuleVisibleId(module, true, categoryId);
		journalCategoryDao.toJournalCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected AddressTypeVO handleGetAddressType(AuthenticationVO auth, Long typeId) throws Exception {
		AddressTypeDao addressTypeDao = this.getAddressTypeDao();
		AddressType addressType = CheckIDUtil.checkAddressTypeId(typeId, addressTypeDao);
		return addressTypeDao.toAddressTypeVO(addressType);
	}

	@Override
	protected Collection<AddressTypeVO> handleGetAllAddressTypes(AuthenticationVO auth)
			throws Exception {
		AddressTypeDao addressTypeDao = this.getAddressTypeDao();
		Collection addressTypes = addressTypeDao.loadAllSorted(0, 0);
		addressTypeDao.toAddressTypeVOCollection(addressTypes);
		return addressTypes;
	}

	@Override
	protected Collection<ContactDetailTypeVO> handleGetAllContactDetailTypes(AuthenticationVO auth)
			throws Exception {
		ContactDetailTypeDao contactDetailTypeDao = this.getContactDetailTypeDao();
		Collection contactTypes = contactDetailTypeDao.loadAllSorted(0, 0);
		contactDetailTypeDao.toContactDetailTypeVOCollection(contactTypes);
		return contactTypes;
	}

	@Override
	protected Collection<CourseCategoryVO> handleGetAllCourseCategories(AuthenticationVO auth)
			throws Exception {
		CourseCategoryDao courseCategoryDao = this.getCourseCategoryDao();
		Collection categories = courseCategoryDao.loadAllSorted(0, 0);
		courseCategoryDao.toCourseCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected Collection<CourseParticipationStatusTypeVO> handleGetAllCourseParticipationStatusTypes(AuthenticationVO auth)
			throws Exception {
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		Collection participationStates = courseParticipationStatusTypeDao.loadAllSorted(0, 0);
		courseParticipationStatusTypeDao.toCourseParticipationStatusTypeVOCollection(participationStates);
		return participationStates;
	}

	@Override
	protected Collection<CriterionRestrictionVO> handleGetAllCriteriaRestrictions(AuthenticationVO auth)
			throws Exception {
		CriterionRestrictionDao criterionRestrictionDao = this.getCriterionRestrictionDao();
		Collection restrictions = criterionRestrictionDao.loadAllSorted(0, 0);
		criterionRestrictionDao.toCriterionRestrictionVOCollection(restrictions);
		return restrictions;
	}

	@Override
	protected Collection<CriterionTieVO> handleGetAllCriterionTies(AuthenticationVO auth)
			throws Exception {
		CriterionTieDao criterionTieDao = this.getCriterionTieDao();
		Collection ties = criterionTieDao.loadAllSorted(0, 0);
		criterionTieDao.toCriterionTieVOCollection(ties);
		return ties;
	}

	@Override
	protected Collection<CvSectionVO> handleGetAllCvSections(AuthenticationVO auth) throws Exception {
		CvSectionDao cvSectionDao = this.getCvSectionDao();
		Collection cvSections = cvSectionDao.loadAllSorted(0, 0);
		cvSectionDao.toCvSectionVOCollection(cvSections);
		return cvSections;
	}

	@Override
	protected Collection<TrainingRecordSectionVO> handleGetAllTrainingRecordSections(AuthenticationVO auth) throws Exception {
		TrainingRecordSectionDao trainingRecordSectionDao = this.getTrainingRecordSectionDao();
		Collection trainingRecordSections = trainingRecordSectionDao.loadAllSorted(0, 0);
		trainingRecordSectionDao.toTrainingRecordSectionVOCollection(trainingRecordSections);
		return trainingRecordSections;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.SelectionSetService#getAllDepartments()
	 */
	@Override
	protected Collection<DepartmentVO> handleGetAllDepartments(AuthenticationVO auth)
			throws Exception {
		DepartmentDao departmentDao = this.getDepartmentDao();
		Collection departments = departmentDao.loadAllSorted(0, 0);
		departmentDao.toDepartmentVOCollection(departments);
		return departments;
	}

	@Override
	protected Collection<InputFieldOutVO> handleGetAllEcrfFieldInputFields(AuthenticationVO auth) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByEcrfFieldsSorted();
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleGetAllEcrfFieldInputFieldSelectionSetValues(AuthenticationVO auth) throws Exception {
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = inputFieldSelectionSetValueDao.findUsedByEcrfFieldsSorted();
		inputFieldSelectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<LightECRFFieldOutVO> handleGetAllEcrfFields(AuthenticationVO auth) throws Exception {
		// this can get expenisive!
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection ecrfFields = ecrfFieldDao.findAllSorted();
		ecrfFieldDao.toLightECRFFieldOutVOCollection(ecrfFields);
		return ecrfFields;
	}

	@Override
	protected Collection<ECRFFieldStatusTypeVO> handleGetAllEcrfFieldStatusTypes(AuthenticationVO auth, ECRFFieldStatusQueue queue, Boolean system)
			throws Exception {
		ECRFFieldStatusTypeDao ecrfFieldStatusTypeDao = this.getECRFFieldStatusTypeDao();
		Collection ecrfFieldStates = ecrfFieldStatusTypeDao.findByQueue(queue, system);
		ecrfFieldStatusTypeDao.toECRFFieldStatusTypeVOCollection(ecrfFieldStates);
		return ecrfFieldStates;
	}

	@Override
	protected Collection<ECRFStatusTypeVO> handleGetAllEcrfStatusTypes(AuthenticationVO auth)
			throws Exception {
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		Collection ecrfStates = ecrfStatusTypeDao.loadAllSorted(0, 0);
		ecrfStatusTypeDao.toECRFStatusTypeVOCollection(ecrfStates);
		return ecrfStates;
	}

	@Override
	protected Collection<LightInquiryOutVO> handleGetAllInquiries(AuthenticationVO auth) throws Exception {
		// this can get expenisive!
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findAllSorted();
		inquiryDao.toLightInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InputFieldOutVO> handleGetAllInquiryInputFields(AuthenticationVO auth) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByInquiriesSorted();
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleGetAllInquiryInputFieldSelectionSetValues(AuthenticationVO auth) throws Exception {
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = inputFieldSelectionSetValueDao.findUsedByInquiriesSorted();
		inputFieldSelectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.SelectionSetService#getAllInventoryCategories()
	 */
	@Override
	protected Collection<InventoryCategoryVO> handleGetAllInventoryCategories(AuthenticationVO auth)
			throws Exception {
		InventoryCategoryDao inventoryCategoryDao = this.getInventoryCategoryDao();
		Collection categories = inventoryCategoryDao.loadAllSorted(0, 0);
		inventoryCategoryDao.toInventoryCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected Collection<InventoryStatusTypeVO> handleGetAllInventoryStatusTypes(AuthenticationVO auth) throws Exception {
		InventoryStatusTypeDao inventoryStatusTypeDao = this.getInventoryStatusTypeDao();
		Collection statusTypes = inventoryStatusTypeDao.loadAllSorted(0, 0);
		inventoryStatusTypeDao.toInventoryStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected Collection<InventoryTagVO> handleGetAllInventoryTags(AuthenticationVO auth) throws Exception {
		InventoryTagDao inventoryTagDao = this.getInventoryTagDao();
		Collection tags = inventoryTagDao.loadAllSorted(0, 0);
		inventoryTagDao.toInventoryTagVOCollection(tags);
		return tags;
	}

	@Override
	protected Collection<LecturerCompetenceVO> handleGetAllLecturerCompetences(AuthenticationVO auth)
			throws Exception {
		LecturerCompetenceDao lecturerCompetenceDao = this.getLecturerCompetenceDao();
		Collection competences = lecturerCompetenceDao.loadAllSorted(0, 0);
		lecturerCompetenceDao.toLecturerCompetenceVOCollection(competences);
		return competences;
	}

	@Override
	protected Collection<MaintenanceTypeVO> handleGetAllMaintenanceTypes(AuthenticationVO auth) throws Exception {
		MaintenanceTypeDao maintenanceTypeDao = this.getMaintenanceTypeDao();
		Collection mainteanceTypes = maintenanceTypeDao.loadAllSorted(0, 0);
		maintenanceTypeDao.toMaintenanceTypeVOCollection(mainteanceTypes);
		return mainteanceTypes;
	}

	@Override
	protected Collection<MassMailStatusTypeVO> handleGetAllMassMailStatusTypes(AuthenticationVO auth)
			throws Exception {
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		Collection massMailStates = massMailStatusTypeDao.loadAllSorted(0, 0);
		massMailStatusTypeDao.toMassMailStatusTypeVOCollection(massMailStates);
		return massMailStates;
	}

	@Override
	protected Collection<MassMailTypeVO> handleGetAllMassMailTypes(
			AuthenticationVO auth) throws Exception {
		MassMailTypeDao massMailTypeDao = this.getMassMailTypeDao();
		Collection massMailTypes = massMailTypeDao.loadAllSorted(0, 0);
		massMailTypeDao.toMassMailTypeVOCollection(massMailTypes);
		return massMailTypes;
	}

	@Override
	protected Collection<NotificationTypeVO> handleGetAllNotificationTypes(
			AuthenticationVO auth) throws Exception {
		NotificationTypeDao notificationTypeDao = this.getNotificationTypeDao();
		Collection notificationTypes = notificationTypeDao.loadAllSorted(0, 0);
		notificationTypeDao.toNotificationTypeVOCollection(notificationTypes);
		return notificationTypes;
	}

	@Override
	protected Collection<PrivacyConsentStatusTypeVO> handleGetAllPrivacyConsentStatusTypes(
			AuthenticationVO auth) throws Exception {
		PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao = this.getPrivacyConsentStatusTypeDao();
		Collection privacyConsentStates = privacyConsentStatusTypeDao.loadAllSorted(0, 0);
		privacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVOCollection(privacyConsentStates);
		return privacyConsentStates;
	}

	@Override
	protected Collection<ProbandCategoryVO> handleGetAllProbandCategories(
			AuthenticationVO auth) throws Exception {
		ProbandCategoryDao probandCategoryDao = this.getProbandCategoryDao();
		Collection categories = probandCategoryDao.loadAllSorted(0, 0);
		probandCategoryDao.toProbandCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected Collection<InputFieldOutVO> handleGetAllProbandListEntryTagInputFields(AuthenticationVO auth) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByListEntryTagsSorted();
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleGetAllProbandListEntryTagInputFieldSelectionSetValues(AuthenticationVO auth) throws Exception {
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = inputFieldSelectionSetValueDao.findUsedByListEntryTagsSorted();
		inputFieldSelectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<LightProbandListEntryTagOutVO> handleGetAllProbandListEntryTags(AuthenticationVO auth) throws Exception {
		// this can get expensive!
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listEntryTags = probandListEntryTagDao.findAllSorted();
		probandListEntryTagDao.toLightProbandListEntryTagOutVOCollection(listEntryTags);
		return listEntryTags;
	}

	@Override
	protected Collection<ProbandListStatusTypeVO> handleGetAllProbandListStatusTypes(AuthenticationVO auth, Boolean person)
			throws Exception {
		ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();
		Collection probandStates = probandListStatusTypeDao.findByPerson(person);
		probandListStatusTypeDao.toProbandListStatusTypeVOCollection(probandStates);
		return probandStates;
	}

	@Override
	protected Collection<ProbandStatusTypeVO> handleGetAllProbandStatusTypes(AuthenticationVO auth)
			throws Exception {
		ProbandStatusTypeDao probandStatusTypeDao = this.getProbandStatusTypeDao();
		Collection statusTypes = probandStatusTypeDao.loadAllSorted(0, 0);
		probandStatusTypeDao.toProbandStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected Collection<ProbandTagVO> handleGetAllProbandTags(AuthenticationVO auth) throws Exception {
		ProbandTagDao probandTagDao = this.getProbandTagDao();
		Collection tags = probandTagDao.loadAllSorted(0, 0);
		probandTagDao.toProbandTagVOCollection(tags);
		return tags;
	}

	@Override
	protected Collection<SponsoringTypeVO> handleGetAllSponsoringTypes(
			AuthenticationVO auth) throws Exception {
		SponsoringTypeDao sponsoringTypeDao = this.getSponsoringTypeDao();
		Collection sponsoringTypes = sponsoringTypeDao.loadAllSorted(0, 0);
		sponsoringTypeDao.toSponsoringTypeVOCollection(sponsoringTypes);
		return sponsoringTypes;
	}

	@Override
	protected Collection<StaffCategoryVO> handleGetAllStaffCategories(AuthenticationVO auth)
			throws Exception {
		StaffCategoryDao staffCategoryDao = this.getStaffCategoryDao();
		Collection categories = staffCategoryDao.loadAllSorted(0, 0);
		staffCategoryDao.toStaffCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected Collection<StaffStatusTypeVO> handleGetAllStaffStatusTypes(AuthenticationVO auth)
			throws Exception {
		StaffStatusTypeDao staffStatusTypeDao = this.getStaffStatusTypeDao();
		Collection statusTypes = staffStatusTypeDao.loadAllSorted(0, 0);
		staffStatusTypeDao.toStaffStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected Collection<StaffTagVO> handleGetAllStaffTags(AuthenticationVO auth)
			throws Exception {
		StaffTagDao staffTagDao = this.getStaffTagDao();
		Collection tags = staffTagDao.loadAllSorted(0, 0);
		staffTagDao.toStaffTagVOCollection(tags);
		return tags;
	}

	@Override
	protected Collection<SurveyStatusTypeVO> handleGetAllSurveyStatusTypes(
			AuthenticationVO auth) throws Exception {
		SurveyStatusTypeDao surveyStatusTypeDao = this.getSurveyStatusTypeDao();
		Collection surveyStatusTypes = surveyStatusTypeDao.loadAllSorted(0, 0);
		surveyStatusTypeDao.toSurveyStatusTypeVOCollection(surveyStatusTypes);
		return surveyStatusTypes;
	}

	@Override
	protected Collection<TeamMemberRoleVO> handleGetAllTeamMemberRoles(AuthenticationVO auth)
			throws Exception {
		TeamMemberRoleDao teamMemberRoleDao = this.getTeamMemberRoleDao();
		Collection roles = teamMemberRoleDao.loadAllSorted(0, 0);
		teamMemberRoleDao.toTeamMemberRoleVOCollection(roles);
		return roles;
	}

	@Override
	protected Collection<TimelineEventTypeVO> handleGetAllTimelineEventTypes(AuthenticationVO auth)
			throws Exception {
		TimelineEventTypeDao timelineEventTypeDao = this.getTimelineEventTypeDao();
		Collection types = timelineEventTypeDao.loadAllSorted(0, 0);
		timelineEventTypeDao.toTimelineEventTypeVOCollection(types);
		return types;
	}

	@Override
	protected Collection<TrialStatusTypeVO> handleGetAllTrialStatusTypes(AuthenticationVO auth)
			throws Exception {
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		Collection trialStates = trialStatusTypeDao.loadAllSorted(0, 0);
		trialStatusTypeDao.toTrialStatusTypeVOCollection(trialStates);
		return trialStates;
	}

	@Override
	protected Collection<TrialTagVO> handleGetAllTrialTags(AuthenticationVO auth) throws Exception {
		TrialTagDao trialTagDao = this.getTrialTagDao();
		Collection tags = trialTagDao.loadAllSorted(0, 0);
		trialTagDao.toTrialTagVOCollection(tags);
		return tags;
	}

	@Override
	protected Collection<TrialTypeVO> handleGetAllTrialTypes(
			AuthenticationVO auth) throws Exception {
		TrialTypeDao trialTypeDao = this.getTrialTypeDao();
		Collection trialTypes = trialTypeDao.loadAllSorted(0, 0);
		trialTypeDao.toTrialTypeVOCollection(trialTypes);
		return trialTypes;
	}

	@Override
	protected Collection<VisitTypeVO> handleGetAllVisitTypes(AuthenticationVO auth) throws Exception {
		VisitTypeDao visitTypeDao = this.getVisitTypeDao();
		Collection types = visitTypeDao.loadAllSorted(0, 0);
		visitTypeDao.toVisitTypeVOCollection(types);
		return types;
	}

	@Override
	protected AlphaIdVO handleGetAlphaId(AuthenticationVO auth, Long alphaIdId)
			throws Exception {
		AlphaIdDao alphaIdDao = this.getAlphaIdDao();
		AlphaId alphaId = CheckIDUtil.checkAlphaIdId(alphaIdId, alphaIdDao);
		return alphaIdDao.toAlphaIdVO(alphaId);
	}

	@Override
	protected AspVO handleGetAsp(AuthenticationVO auth, Long aspId)
			throws Exception {
		AspDao aspDao = this.getAspDao();
		Asp asp = CheckIDUtil.checkAspId(aspId, aspDao);
		return aspDao.toAspVO(asp);
	}

	@Override
	protected AspAtcCodeVO handleGetAspAtcCode(AuthenticationVO auth, Long aspAtcCodeId) throws Exception {
		AspAtcCodeDao aspAtcCodeDao = this.getAspAtcCodeDao();
		AspAtcCode atcCode = CheckIDUtil.checkAspAtcCodeId(aspAtcCodeId, aspAtcCodeDao);
		return aspAtcCodeDao.toAspAtcCodeVO(atcCode);
	}

	@Override
	protected AspSubstanceVO handleGetAspSubstance(AuthenticationVO auth, Long aspSubstanceId)
			throws Exception {
		AspSubstanceDao aspSubstanceDao = this.getAspSubstanceDao();
		AspSubstance asp = CheckIDUtil.checkAspSubstanceId(aspSubstanceId, aspSubstanceDao);
		return aspSubstanceDao.toAspSubstanceVO(asp);
	}

	@Override
	protected Collection<AuthenticationTypeVO> handleGetAuthenticationTypes(
			AuthenticationVO auth) throws Exception {
		Collection<AuthenticationTypeVO> result;
		AuthenticationType[] authenticationTypes = AuthenticationType.values();
		if (authenticationTypes != null) {
			result = new ArrayList<AuthenticationTypeVO>(authenticationTypes.length);
			for (int i = 0; i < authenticationTypes.length; i++) {
				result.add(L10nUtil.createAuthenticationTypeVO(Locales.USER, authenticationTypes[i]));
			}
		} else {
			result = new ArrayList<AuthenticationTypeVO>();
		}
		return result;
	}

	@Override
	protected Collection<InventoryTagVO> handleGetAvailableInventoryTags(AuthenticationVO auth, Long inventoryId, Long tagId) throws Exception {
		return (new InventoryTagAdapter(this.getInventoryDao(), this.getInventoryTagDao())).getAvailableTags(inventoryId, tagId);
	}

	@Override
	protected Collection<LecturerCompetenceVO> handleGetAvailableLecturerCompetences(
			AuthenticationVO auth, Long courseId, Long competenceId) throws Exception {
		return (new LecturerCompetenceTagAdapter(this.getCourseDao(), this.getLecturerCompetenceDao())).getAvailableTags(courseId, competenceId);
	}

	@Override
	protected Collection<AddressTypeVO> handleGetAvailableProbandAddressTypes(
			AuthenticationVO auth, Boolean person, Boolean animal, Long probandId, Long typeId) throws Exception {
		return (new ProbandAddressTypeTagAdapter(person, animal, this.getProbandDao(), this.getAddressTypeDao())).getAvailableTags(probandId, typeId);
	}

	@Override
	protected Collection<ContactDetailTypeVO> handleGetAvailableProbandContactDetailTypes(AuthenticationVO auth, Boolean person, Boolean animal, Long probandId, Long typeId)
			throws Exception {
		return (new ProbandContactDetailTypeTagAdapter(person, animal, this.getProbandDao(), this.getContactDetailTypeDao())).getAvailableTags(probandId, typeId);
	}

	@Override
	protected Collection<ProbandTagVO> handleGetAvailableProbandTags(AuthenticationVO auth, Boolean person, Boolean animal, Long probandId,
			Long tagId) throws Exception {
		return (new ProbandTagAdapter(person, animal, this.getProbandDao(), this.getProbandTagDao())).getAvailableTags(probandId, tagId);
	}

	@Override
	protected Collection<AddressTypeVO> handleGetAvailableStaffAddressTypes(
			AuthenticationVO auth, Long staffId, Long typeId) throws Exception {
		return (new StaffAddressTypeTagAdapter(this.getStaffDao(), this.getAddressTypeDao())).getAvailableTags(staffId, typeId);
	}

	@Override
	protected Collection<ContactDetailTypeVO> handleGetAvailableStaffContactDetailTypes(AuthenticationVO auth, Long staffId, Long typeId) throws Exception {
		return (new StaffContactDetailTypeTagAdapter(this.getStaffDao(), this.getContactDetailTypeDao())).getAvailableTags(staffId, typeId);
	}

	@Override
	protected Collection<StaffTagVO> handleGetAvailableStaffTags(
			AuthenticationVO auth, Boolean person, Boolean organisation, Long staffId, Long tagId) throws Exception {
		return (new StaffTagAdapter(person, organisation, this.getStaffDao(), this.getStaffTagDao())).getAvailableTags(staffId, tagId);
	}

	@Override
	protected Collection<TeamMemberRoleVO> handleGetAvailableTeamMemberRoles(
			AuthenticationVO auth, Long trialId, Long roleId) throws Exception {
		return (new TeamMemberRoleTagAdapter(this.getTrialDao(), this.getTeamMemberRoleDao())).getAvailableTags(trialId, roleId);
	}

	@Override
	protected Collection<TimelineEventTypeVO> handleGetAvailableTimelineEventTypes(
			AuthenticationVO auth, Long trialId, Long typeId) throws Exception {
		return (new TimelineEventTypeTagAdapter(this.getTrialDao(), this.getTimelineEventTypeDao(), this.getTimelineEventDao())).getAvailableTags(trialId, typeId);
	}

	@Override
	protected Collection<TrialTagVO> handleGetAvailableTrialTags(AuthenticationVO auth, Long trialId,
			Long tagId) throws Exception {
		return (new TrialTagAdapter(this.getTrialDao(), this.getTrialTagDao())).getAvailableTags(trialId, tagId);
	}

	@Override
	protected Collection<VisitTypeVO> handleGetAvailableVisitTypes(
			AuthenticationVO auth, Long trialId, Long typeId) throws Exception {
		return (new VisitTypeTagAdapter(this.getTrialDao(), this.getVisitTypeDao())).getAvailableTags(trialId, typeId);
	}

	@Override
	protected Collection<BankIdentificationVO> handleGetBankIdentifications(AuthenticationVO auth, String bankCodeNumberPrefix, String bicPrefix, String bankNameInfix,
			Integer limit)
			throws Exception {
		BankIdentificationDao bankIdentificationDao = this.getBankIdentificationDao();
		Collection bankIdentifications = bankIdentificationDao.findBankIdentifications(bankCodeNumberPrefix, bicPrefix, bankNameInfix, limit);
		bankIdentificationDao.toBankIdentificationVOCollection(bankIdentifications);
		return bankIdentifications;
	}

	@Override
	protected Collection<BooleanVO> handleGetBooleans(AuthenticationVO auth) throws Exception {
		Collection<BooleanVO> result = new ArrayList<BooleanVO>(2);
		BooleanVO booleanVO = new BooleanVO();
		booleanVO.setValue(false);
		booleanVO.setNameL10nKey(MessageCodes.FALSE);
		booleanVO.setName(L10nUtil.getString(MessageCodes.FALSE, DefaultMessages.FALSE));
		result.add(booleanVO);
		booleanVO = new BooleanVO();
		booleanVO.setValue(true);
		booleanVO.setNameL10nKey(MessageCodes.TRUE);
		booleanVO.setName(L10nUtil.getString(MessageCodes.TRUE, DefaultMessages.TRUE));
		result.add(booleanVO);
		return result;
	}

	@Override
	protected ContactDetailTypeVO handleGetContactDetailType(AuthenticationVO auth, Long typeId)
			throws Exception {
		ContactDetailTypeDao contactDetailTypeDao = this.getContactDetailTypeDao();
		ContactDetailType contactType = CheckIDUtil.checkContactDetailTypeId(typeId, contactDetailTypeDao);
		return contactDetailTypeDao.toContactDetailTypeVO(contactType);
	}

	@Override
	protected Collection<CountryVO> handleGetCountries(AuthenticationVO auth, String countryNameInfix, Integer limit)
			throws Exception {
		CountryDao countryDao = this.getCountryDao();
		Collection countries = countryDao.findCountries(countryNameInfix, null, limit);
		countryDao.toCountryVOCollection(countries);
		return countries;
	}

	@Override
	protected Collection<CourseCategoryVO> handleGetCourseCategories(
			AuthenticationVO auth, Long categoryId) throws Exception {
		CourseCategoryDao courseCategoryDao = this.getCourseCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkCourseCategoryId(categoryId, courseCategoryDao);
		}
		Collection categories = courseCategoryDao.findByVisibleId(true,
				categoryId);
		courseCategoryDao.toCourseCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected CourseCategoryVO handleGetCourseCategory(AuthenticationVO auth,
			Long categoryId) throws Exception {
		CourseCategoryDao courseCategoryDao = this.getCourseCategoryDao();
		return courseCategoryDao.toCourseCategoryVO(CheckIDUtil.checkCourseCategoryId(categoryId, courseCategoryDao));
	}

	@Override
	protected Collection<HyperlinkCategoryVO> handleGetCourseHyperlinkCategories(AuthenticationVO auth)
			throws Exception {
		return getHyperlinkCategoriesHelper(HyperlinkModule.COURSE_HYPERLINK, null);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetCourseJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.COURSE_JOURNAL, null);
	}

	@Override
	protected CourseParticipationStatusTypeVO handleGetCourseParticipationStatusType(AuthenticationVO auth, Long typeId) throws Exception {
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		CourseParticipationStatusType statusType = CheckIDUtil.checkCourseParticipationStatusTypeId(typeId, courseParticipationStatusTypeDao);
		return courseParticipationStatusTypeDao.toCourseParticipationStatusTypeVO(statusType);
	}

	@Override
	protected Collection<CourseParticipationStatusTypeVO> handleGetCourseParticipationStatusTypeTransitions(
			AuthenticationVO auth, Long typeId, boolean admin, boolean selfRegistration)
			throws Exception {
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkCourseParticipationStatusTypeId(typeId, courseParticipationStatusTypeDao);
		}
		Collection participationStates = courseParticipationStatusTypeDao.findTransitions(typeId, admin, selfRegistration);
		courseParticipationStatusTypeDao.toCourseParticipationStatusTypeVOCollection(participationStates);
		return participationStates;
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetCriteriaJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.CRITERIA_JOURNAL, null);
	}

	@Override
	protected Collection<CriterionPropertyVO> handleGetCriterionProperties(
			AuthenticationVO auth, DBModule module) throws Exception {
		CriterionPropertyDao criterionPropertyDao = this.getCriterionPropertyDao();
		Collection properties = criterionPropertyDao.findByModule(module);
		criterionPropertyDao.toCriterionPropertyVOCollection(properties);
		return properties;
	}

	@Override
	protected CvSectionVO handleGetCvSection(AuthenticationVO auth, Long sectionId) throws Exception {
		CvSectionDao cvSectionDao = this.getCvSectionDao();
		CvSection cvSection = CheckIDUtil.checkCvSectionId(sectionId, cvSectionDao);
		return cvSectionDao.toCvSectionVO(cvSection);
	}

	@Override
	protected Collection<CvSectionVO> handleGetCvSections(AuthenticationVO auth, Long sectionId) throws Exception {
		CvSectionDao cvSectionDao = this.getCvSectionDao();
		if (sectionId != null) {
			CheckIDUtil.checkCvSectionId(sectionId, cvSectionDao);
		}
		Collection cvSections = cvSectionDao.findByVisibleIdSorted(true, sectionId);
		cvSectionDao.toCvSectionVOCollection(cvSections);
		return cvSections;
	}

	@Override
	protected TrainingRecordSectionVO handleGetTrainingRecordSection(AuthenticationVO auth, Long sectionId) throws Exception {
		TrainingRecordSectionDao trainingRecordSectionDao = this.getTrainingRecordSectionDao();
		TrainingRecordSection trainingRecordSection = CheckIDUtil.checkTrainingRecordSectionId(sectionId, trainingRecordSectionDao);
		return trainingRecordSectionDao.toTrainingRecordSectionVO(trainingRecordSection);
	}

	@Override
	protected Collection<TrainingRecordSectionVO> handleGetTrainingRecordSections(AuthenticationVO auth, Long sectionId) throws Exception {
		TrainingRecordSectionDao trainingRecordSectionDao = this.getTrainingRecordSectionDao();
		if (sectionId != null) {
			CheckIDUtil.checkTrainingRecordSectionId(sectionId, trainingRecordSectionDao);
		}
		Collection trainingRecordSections = trainingRecordSectionDao.findByVisibleIdSorted(true, sectionId);
		trainingRecordSectionDao.toTrainingRecordSectionVOCollection(trainingRecordSections);
		return trainingRecordSections;
	}

	@Override
	protected Collection<String> handleGetDateFormats(
			AuthenticationVO auth) throws Exception {
		return CoreUtil.getDateFormats();
	}

	@Override
	protected Collection<DBModuleVO> handleGetDBModules(AuthenticationVO auth)
			throws Exception {
		Collection<DBModuleVO> result;
		DBModule[] dbModules = DBModule.values();
		if (dbModules != null) {
			result = new ArrayList<DBModuleVO>(dbModules.length);
			for (int i = 0; i < dbModules.length; i++) {
				result.add(L10nUtil.createDBModuleVO(Locales.USER, dbModules[i]));
			}
		} else {
			result = new ArrayList<DBModuleVO>();
		}
		return result;
	}

	@Override
	protected Collection<String> handleGetDecimalSeparators(
			AuthenticationVO auth) throws Exception {
		return CoreUtil.getDecimalSeparatos();
	}

	@Override
	protected Collection<DepartmentVO> handleGetDepartments(AuthenticationVO auth, Long departmentId)
			throws Exception {
		DepartmentDao departmentDao = this.getDepartmentDao();
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, departmentDao);
		}
		Collection departments = departmentDao.findByVisibleId(true, departmentId);
		departmentDao.toDepartmentVOCollection(departments);
		return departments;
	}

	@Override
	protected ECRFFieldStatusTypeVO handleGetEcrfFieldStatusType(AuthenticationVO auth, Long typeId) throws Exception {
		ECRFFieldStatusTypeDao ecrfFieldStatusTypeDao = this.getECRFFieldStatusTypeDao();
		ECRFFieldStatusType statusType = CheckIDUtil.checkEcrfFieldStatusTypeId(typeId, ecrfFieldStatusTypeDao);
		return ecrfFieldStatusTypeDao.toECRFFieldStatusTypeVO(statusType);
	}

	@Override
	protected Collection<ECRFFieldStatusTypeVO> handleGetEcrfFieldStatusTypeTransitions(
			AuthenticationVO auth, Long typeId, Boolean system) throws Exception {
		ECRFFieldStatusTypeDao ecrfFieldStatusTypeDao = this.getECRFFieldStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkEcrfFieldStatusTypeId(typeId, ecrfFieldStatusTypeDao);
		}
		Collection ecrfFieldStates = ecrfFieldStatusTypeDao.findTransitions(typeId, system);
		ecrfFieldStatusTypeDao.toECRFFieldStatusTypeVOCollection(ecrfFieldStates);
		return ecrfFieldStates;
	}

	@Override
	protected ECRFStatusTypeVO handleGetEcrfStatusType(AuthenticationVO auth, Long typeId) throws Exception {
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		ECRFStatusType statusType = CheckIDUtil.checkEcrfStatusTypeId(typeId, ecrfStatusTypeDao);
		return ecrfStatusTypeDao.toECRFStatusTypeVO(statusType);
	}

	@Override
	protected Collection<ECRFStatusTypeVO> handleGetEcrfStatusTypeTransitions(
			AuthenticationVO auth, Long typeId) throws Exception {
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkEcrfStatusTypeId(typeId, ecrfStatusTypeDao);
		}
		Collection ecrfStates = ecrfStatusTypeDao.findTransitions(typeId);
		ecrfStatusTypeDao.toECRFStatusTypeVOCollection(ecrfStates);
		return ecrfStates;
	}

	@Override
	protected Collection<ECRFValidationStatusVO> handleGetEcrfValidationStates(AuthenticationVO auth) throws Exception {
		Collection<ECRFValidationStatusVO> result;
		ECRFValidationStatus[] validationStates = ECRFValidationStatus.values();
		if (validationStates != null) {
			result = new ArrayList<ECRFValidationStatusVO>(validationStates.length);
			for (int i = 0; i < validationStates.length; i++) {
				result.add(L10nUtil.createEcrfValidationStatusVO(Locales.USER, validationStates[i]));
			}
		} else {
			result = new ArrayList<ECRFValidationStatusVO>();
		}
		return result;
	}

	@Override
	protected Collection<EventImportanceVO> handleGetEventImportances(AuthenticationVO auth)
			throws Exception {
		Collection<EventImportanceVO> result;
		EventImportance[] eventImportances = EventImportance.values();
		if (eventImportances != null) {
			result = new ArrayList<EventImportanceVO>(eventImportances.length);
			for (int i = 0; i < eventImportances.length; i++) {
				result.add(L10nUtil.createEventImportanceVO(Locales.USER, eventImportances[i]));
			}
		} else {
			result = new ArrayList<EventImportanceVO>();
		}
		return result;
	}

	@Override
	protected Collection<JobStatusVO> handleGetJobStates(AuthenticationVO auth) throws Exception {
		Collection<JobStatusVO> result;
		JobStatus[] jobStates = JobStatus.values();
		if (jobStates != null) {
			result = new ArrayList<JobStatusVO>(jobStates.length);
			for (int i = 0; i < jobStates.length; i++) {
				result.add(L10nUtil.createJobStatusVO(Locales.USER, jobStates[i]));
			}
		} else {
			result = new ArrayList<JobStatusVO>();
		}
		return result;
	}

	@Override
	protected Collection<HyperlinkCategoryVO> handleGetHyperlinkCategories(AuthenticationVO auth, HyperlinkModule module, Long categoryId)
			throws Exception {
		return getHyperlinkCategoriesHelper(module, categoryId);
	}

	@Override
	protected HyperlinkCategoryVO handleGetHyperlinkCategory(AuthenticationVO auth, Long categoryId)
			throws Exception {
		HyperlinkCategoryDao hyperlinkCategoryDao = this.getHyperlinkCategoryDao();
		HyperlinkCategory category = CheckIDUtil.checkHyperlinkCategoryId(categoryId, hyperlinkCategoryDao);
		return hyperlinkCategoryDao.toHyperlinkCategoryVO(category);
	}

	@Override
	protected Collection<CourseParticipationStatusTypeVO> handleGetInitialCourseParticipationStatusTypes(
			AuthenticationVO auth, boolean admin, boolean selfRegistration) throws Exception {
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		Collection participationStates = courseParticipationStatusTypeDao.findInitialStates(admin, selfRegistration);
		courseParticipationStatusTypeDao.toCourseParticipationStatusTypeVOCollection(participationStates);
		return participationStates;
	}

	@Override
	protected Collection<ECRFFieldStatusTypeVO> handleGetInitialEcrfFieldStatusTypes(AuthenticationVO auth, ECRFFieldStatusQueue queue, Boolean system) throws Exception {
		ECRFFieldStatusTypeDao ecrfFieldStatusTypeDao = this.getECRFFieldStatusTypeDao();
		Collection ecrfFieldStates = ecrfFieldStatusTypeDao.findInitialStates(queue, system);
		ecrfFieldStatusTypeDao.toECRFFieldStatusTypeVOCollection(ecrfFieldStates);
		return ecrfFieldStates;
	}

	@Override
	protected Collection<ECRFStatusTypeVO> handleGetInitialEcrfStatusTypes(AuthenticationVO auth) throws Exception {
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		Collection ecrfStates = ecrfStatusTypeDao.findInitialStates();
		ecrfStatusTypeDao.toECRFStatusTypeVOCollection(ecrfStates);
		return ecrfStates;
	}

	@Override
	protected Collection<MassMailStatusTypeVO> handleGetInitialMassMailStatusTypes(AuthenticationVO auth)
			throws Exception {
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		Collection massMailStates = massMailStatusTypeDao.findInitialStates();
		massMailStatusTypeDao.toMassMailStatusTypeVOCollection(massMailStates);
		return massMailStates;
	}

	@Override
	protected Collection<PrivacyConsentStatusTypeVO> handleGetInitialPrivacyConsentStatusTypes(
			AuthenticationVO auth) throws Exception {
		PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao = this.getPrivacyConsentStatusTypeDao();
		Collection privacyConsentStates = privacyConsentStatusTypeDao.findInitialStates();
		privacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVOCollection(privacyConsentStates);
		return privacyConsentStates;
	}

	@Override
	protected Collection<ProbandListStatusTypeVO> handleGetInitialProbandListStatusTypes(AuthenticationVO auth, Boolean signup, Boolean person)
			throws Exception {
		ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();
		Collection probandStates = probandListStatusTypeDao.findInitialStates(signup, person);
		probandListStatusTypeDao.toProbandListStatusTypeVOCollection(probandStates);
		return probandStates;
	}

	@Override
	protected Collection<TrialStatusTypeVO> handleGetInitialTrialStatusTypes(AuthenticationVO auth)
			throws Exception {
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		Collection trialStates = trialStatusTypeDao.findInitialStates();
		trialStatusTypeDao.toTrialStatusTypeVOCollection(trialStates);
		return trialStates;
	}

	@Override
	protected Collection<String> handleGetInputFieldCategories(
			AuthenticationVO auth, InputFieldType fieldType,
			String categoryPrefix, Integer limit) throws Exception {
		return this.getInputFieldDao().findCategories(fieldType, categoryPrefix, limit);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetInputFieldJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.INPUT_FIELD_JOURNAL, null);
	}

	@Override
	protected Collection<InputFieldTypeVO> handleGetInputFieldTypes(AuthenticationVO auth)
			throws Exception {
		Collection<InputFieldTypeVO> result;
		InputFieldType[] inputFieldTypes = InputFieldType.values();
		if (inputFieldTypes != null) {
			result = new ArrayList<InputFieldTypeVO>(inputFieldTypes.length);
			for (int i = 0; i < inputFieldTypes.length; i++) {
				result.add(L10nUtil.createInputFieldTypeVO(Locales.USER, inputFieldTypes[i]));
			}
		} else {
			result = new ArrayList<InputFieldTypeVO>();
		}
		return result;
	}

	@Override
	protected Collection<InventoryCategoryVO> handleGetInventoryCategories(AuthenticationVO auth, Long categoryId)
			throws Exception {
		InventoryCategoryDao inventoryCategoryDao = this.getInventoryCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(categoryId, inventoryCategoryDao);
		}
		Collection categories = inventoryCategoryDao.findByVisibleId(true, categoryId);
		inventoryCategoryDao.toInventoryCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected InventoryCategoryVO handleGetInventoryCategory(
			AuthenticationVO auth, Long categoryId) throws Exception {
		InventoryCategoryDao inventoryCategoryDao = this.getInventoryCategoryDao();
		return inventoryCategoryDao.toInventoryCategoryVO(CheckIDUtil.checkInventoryCategoryId(categoryId, inventoryCategoryDao));
	}

	@Override
	protected Collection<HyperlinkCategoryVO> handleGetInventoryHyperlinkCategories(AuthenticationVO auth)
			throws Exception {
		return getHyperlinkCategoriesHelper(HyperlinkModule.INVENTORY_HYPERLINK, null);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetInventoryJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.INVENTORY_JOURNAL, null);
	}

	@Override
	protected InventoryStatusTypeVO handleGetInventoryStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		InventoryStatusTypeDao inventoryStatusTypeDao = this.getInventoryStatusTypeDao();
		InventoryStatusType statusType = CheckIDUtil.checkInventoryStatusTypeId(typeId, inventoryStatusTypeDao);
		return inventoryStatusTypeDao.toInventoryStatusTypeVO(statusType);
	}

	@Override
	protected Collection<InventoryStatusTypeVO> handleGetInventoryStatusTypes(AuthenticationVO auth, Long typeId) throws Exception {
		InventoryStatusTypeDao inventoryStatusTypeDao = this.getInventoryStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkInventoryStatusTypeId(typeId, inventoryStatusTypeDao);
		}
		Collection statusTypes = inventoryStatusTypeDao.findByVisibleId(true, typeId);
		inventoryStatusTypeDao.toInventoryStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected InventoryTagVO handleGetInventoryTag(AuthenticationVO auth, Long tagId)
			throws Exception {
		InventoryTagDao inventoryTagDao = this.getInventoryTagDao();
		InventoryTag tag = CheckIDUtil.checkInventoryTagId(tagId, inventoryTagDao);
		return inventoryTagDao.toInventoryTagVO(tag);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetJournalCategories(AuthenticationVO auth, JournalModule module, Long categoryId)
			throws Exception {
		return getJournalCategoriesHelper(module, categoryId);
	}

	@Override
	protected JournalCategoryVO handleGetJournalCategory(AuthenticationVO auth, Long categoryId)
			throws Exception {
		JournalCategoryDao journalCategoryDao = this.getJournalCategoryDao();
		JournalCategory category = CheckIDUtil.checkJournalCategoryId(categoryId, journalCategoryDao);
		return journalCategoryDao.toJournalCategoryVO(category);
	}

	@Override
	protected JournalCategoryVO handleGetJournalCategoryPreset(AuthenticationVO auth,
			JournalModule module) throws Exception {
		JournalCategoryDao journalCategoryDao = this.getJournalCategoryDao();
		JournalCategory journalCategory = journalCategoryDao.findPreset(module);
		return journalCategory == null ? null : journalCategoryDao.toJournalCategoryVO(journalCategory);
	}

	@Override
	protected Collection<JournalModuleVO> handleGetJournalModules(
			AuthenticationVO auth) throws Exception {
		Collection<JournalModuleVO> result;
		JournalModule[] journalModules = JournalModule.values();
		if (journalModules != null) {
			result = new ArrayList<JournalModuleVO>(journalModules.length);
			for (int i = 0; i < journalModules.length; i++) {
				switch (journalModules[i]) {
					case ECRF_JOURNAL:
						break;
					default:
						result.add(L10nUtil.createJournalModuleVO(Locales.USER, journalModules[i]));
						break;
				}
			}
		} else {
			result = new ArrayList<JournalModuleVO>();
		}
		return result;
	}

	@Override
	protected LecturerCompetenceVO handleGetLecturerCompetence(AuthenticationVO auth, Long competenceId)
			throws Exception {
		LecturerCompetenceDao lecturerCompetenceDao = this.getLecturerCompetenceDao();
		LecturerCompetence competence = CheckIDUtil.checkLecturerCompetenceId(competenceId, lecturerCompetenceDao);
		return lecturerCompetenceDao.toLecturerCompetenceVO(competence);
	}

	@Override
	protected LocaleVO handleGetLocale(AuthenticationVO auth, String language)
			throws Exception {
		Locale locale = CommonUtil.localeFromString(language);
		LocaleVO localeVO = new LocaleVO();
		localeVO.setLanguage(CommonUtil.localeToString(locale));
		localeVO.setName(CommonUtil.localeToDisplayString(locale, L10nUtil.getLocale(Locales.USER)));
		return localeVO;
	}

	@Override
	protected Collection<LocaleVO> handleGetLocales(AuthenticationVO auth)
			throws Exception {
		ArrayList<Locale> supportedLocales = CoreUtil.getSupportedLocales();
		ArrayList<LocaleVO> result = new ArrayList<LocaleVO>(supportedLocales.size());
		Iterator<Locale> it = supportedLocales.iterator();
		Locale userLocale = L10nUtil.getLocale(Locales.USER);
		while (it.hasNext()) {
			Locale locale = it.next();
			LocaleVO localeVO = new LocaleVO();
			localeVO.setLanguage(CommonUtil.localeToString(locale));
			localeVO.setName(CommonUtil.localeToDisplayString(locale, userLocale));
			result.add(localeVO);
		}
		return result;
	}

	@Override
	protected MaintenanceTypeVO handleGetMaintenanceType(AuthenticationVO auth, Long typeId)
			throws Exception {
		MaintenanceTypeDao maintenanceTypeDao = this.getMaintenanceTypeDao();
		MaintenanceType maintenanceType = CheckIDUtil.checkMaintenanceTypeId(typeId, maintenanceTypeDao);
		return maintenanceTypeDao.toMaintenanceTypeVO(maintenanceType);
	}

	@Override
	protected Collection<MaintenanceTypeVO> handleGetMaintenanceTypes(AuthenticationVO auth, Long typeId) throws Exception {
		MaintenanceTypeDao maintenanceTypeDao = this.getMaintenanceTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkMaintenanceTypeId(typeId, maintenanceTypeDao);
		}
		Collection mainteanceTypes = maintenanceTypeDao.findByVisibleId(true, typeId);
		maintenanceTypeDao.toMaintenanceTypeVOCollection(mainteanceTypes);
		return mainteanceTypes;
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetMassMailJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.MASS_MAIL_JOURNAL, null);
	}

	@Override
	protected MassMailStatusTypeVO handleGetMassMailStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		MassMailStatusType statusType = CheckIDUtil.checkMassMailStatusTypeId(typeId, massMailStatusTypeDao);
		return massMailStatusTypeDao.toMassMailStatusTypeVO(statusType);
	}

	@Override
	protected Collection<MassMailStatusTypeVO> handleGetMassMailStatusTypeTransitions(
			AuthenticationVO auth, Long typeId) throws Exception {
		MassMailStatusTypeDao massMailStatusTypeDao = this.getMassMailStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkMassMailStatusTypeId(typeId, massMailStatusTypeDao);
		}
		Collection massMailStates = massMailStatusTypeDao.findTransitions(typeId);
		massMailStatusTypeDao.toMassMailStatusTypeVOCollection(massMailStates);
		return massMailStates;
	}

	@Override
	protected MassMailTypeVO handleGetMassMailType(AuthenticationVO auth, Long typeId)
			throws Exception {
		MassMailTypeDao massMailTypeDao = this.getMassMailTypeDao();
		MassMailType massMailType = CheckIDUtil.checkMassMailTypeId(typeId, massMailTypeDao);
		return massMailTypeDao.toMassMailTypeVO(massMailType);
	}

	@Override
	protected Collection<MassMailTypeVO> handleGetMassMailTypes(
			AuthenticationVO auth, Long typeId) throws Exception {
		MassMailTypeDao massMailTypeDao = this.getMassMailTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkMassMailTypeId(typeId, massMailTypeDao);
		}
		Collection massMailTypes = massMailTypeDao.findByVisibleId(true, typeId);
		massMailTypeDao.toMassMailTypeVOCollection(massMailTypes);
		return massMailTypes;
	}

	@Override
	protected NotificationTypeVO handleGetNotificationType(
			AuthenticationVO auth, Long typeId) throws Exception {
		NotificationTypeDao notificationTypeDao = this.getNotificationTypeDao();
		NotificationType notificationType = checkNotificationTypeId(typeId, notificationTypeDao);
		return notificationTypeDao.toNotificationTypeVO(notificationType);
	}

	@Override
	protected OpsCodeVO handleGetOpsCode(AuthenticationVO auth, Long opsCodeId)
			throws Exception {
		OpsCodeDao opsCodeDao = this.getOpsCodeDao();
		OpsCode opsCode = CheckIDUtil.checkOpsCodeId(opsCodeId, opsCodeDao);
		return opsCodeDao.toOpsCodeVO(opsCode);
	}

	@Override
	protected Collection<PaymentMethodVO> handleGetPaymentMethods(AuthenticationVO auth) throws Exception {
		Collection<PaymentMethodVO> result;
		PaymentMethod[] paymentMethods = PaymentMethod.values();
		if (paymentMethods != null) {
			result = new ArrayList<PaymentMethodVO>(paymentMethods.length);
			for (int i = 0; i < paymentMethods.length; i++) {
				result.add(L10nUtil.createPaymentMethodVO(Locales.USER, paymentMethods[i]));
			}
		} else {
			result = new ArrayList<PaymentMethodVO>();
		}
		return result;
	}

	@Override
	protected Collection<VisitScheduleDateModeVO> handleGetVisitScheduleDateModes(AuthenticationVO auth) throws Exception {
		Collection<VisitScheduleDateModeVO> result;
		VisitScheduleDateMode[] modes = VisitScheduleDateMode.values();
		if (modes != null) {
			result = new ArrayList<VisitScheduleDateModeVO>(modes.length);
			for (int i = 0; i < modes.length; i++) {
				result.add(L10nUtil.createVisitScheduleDateModeVO(Locales.USER, modes[i]));
			}
		} else {
			result = new ArrayList<VisitScheduleDateModeVO>();
		}
		return result;
	}

	@Override
	protected Collection<PermissionProfileVO> handleGetPermissionProfiles(
			AuthenticationVO auth, PermissionProfileGroup profileGroup) throws Exception {
		return ServiceUtil.getPermissionProfiles(profileGroup, Locales.USER);
	}

	@Override
	protected PrivacyConsentStatusTypeVO handleGetPrivacyConsentStatusType(
			AuthenticationVO auth, Long typeId) throws Exception {
		PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao = this.getPrivacyConsentStatusTypeDao();
		PrivacyConsentStatusType statusType = CheckIDUtil.checkPrivacyConsentStatusTypeId(typeId, privacyConsentStatusTypeDao);
		return privacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVO(statusType);
	}

	@Override
	protected Collection<PrivacyConsentStatusTypeVO> handleGetPrivacyConsentStatusTypeTransitions(
			AuthenticationVO auth, Long typeId) throws Exception {
		PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao = this.getPrivacyConsentStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkPrivacyConsentStatusTypeId(typeId, privacyConsentStatusTypeDao);
		}
		Collection privacyConsentStates = privacyConsentStatusTypeDao.findTransitions(typeId);
		privacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVOCollection(privacyConsentStates);
		return privacyConsentStates;
	}

	@Override
	protected Collection<ProbandCategoryVO> handleGetProbandCategories(
			AuthenticationVO auth, Boolean person, Boolean animal, Long categoryId) throws Exception {
		ProbandCategoryDao probandCategoryDao = this.getProbandCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkProbandCategoryId(categoryId, probandCategoryDao);
		}
		Collection categories = probandCategoryDao.findByPersonAnimalId(person, animal, categoryId);
		probandCategoryDao.toProbandCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected ProbandCategoryVO handleGetProbandCategory(AuthenticationVO auth,
			Long categoryId) throws Exception {
		ProbandCategoryDao probandCategoryDao = this.getProbandCategoryDao();
		return probandCategoryDao.toProbandCategoryVO(CheckIDUtil.checkProbandCategoryId(categoryId, probandCategoryDao));
	}

	@Override
	protected ProbandCategoryVO handleGetProbandCategoryPreset(AuthenticationVO auth, boolean signup, boolean person)
			throws Exception {
		ProbandCategoryDao probandCategoryDao = this.getProbandCategoryDao();
		ProbandCategory probandCategory = probandCategoryDao.findPreset(signup, person);
		return probandCategory == null ? null : probandCategoryDao.toProbandCategoryVO(probandCategory);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetProbandJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.PROBAND_JOURNAL, null);
	}

	@Override
	protected ProbandListStatusTypeVO handleGetProbandListStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();
		ProbandListStatusType statusType = CheckIDUtil.checkProbandListStatusTypeId(typeId, probandListStatusTypeDao);
		return probandListStatusTypeDao.toProbandListStatusTypeVO(statusType);
	}

	@Override
	protected Collection<ProbandListStatusTypeVO> handleGetProbandListStatusTypeTransitions(
			AuthenticationVO auth, Long typeId) throws Exception {
		ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkProbandListStatusTypeId(typeId, probandListStatusTypeDao);
		}
		Collection trialStates = probandListStatusTypeDao.findTransitions(typeId);
		probandListStatusTypeDao.toProbandListStatusTypeVOCollection(trialStates);
		return trialStates;
	}

	@Override
	protected ProbandStatusTypeVO handleGetProbandStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		ProbandStatusTypeDao probandStatusTypeDao = this.getProbandStatusTypeDao();
		ProbandStatusType statusType = CheckIDUtil.checkProbandStatusTypeId(typeId, probandStatusTypeDao);
		return probandStatusTypeDao.toProbandStatusTypeVO(statusType);
	}

	@Override
	protected Collection<ProbandStatusTypeVO> handleGetProbandStatusTypes(AuthenticationVO auth, Boolean person, Boolean animal, Long typeId)
			throws Exception {
		ProbandStatusTypeDao probandStatusTypeDao = this.getProbandStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkProbandStatusTypeId(typeId, probandStatusTypeDao);
		}
		Collection statusTypes = probandStatusTypeDao.findByPersonAnimalId(person, animal, typeId);
		probandStatusTypeDao.toProbandStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected ProbandTagVO handleGetProbandTag(AuthenticationVO auth, Long tagId) throws Exception {
		ProbandTagDao probandTagDao = this.getProbandTagDao();
		ProbandTag tag = CheckIDUtil.checkProbandTagId(tagId, probandTagDao);
		return probandTagDao.toProbandTagVO(tag);
	}

	@Override
	protected Collection<RandomizationModeVO> handleGetRandomizationModes(AuthenticationVO auth) throws Exception {
		Collection<RandomizationModeVO> result;
		RandomizationMode[] modes = RandomizationMode.values();
		if (modes != null) {
			result = new ArrayList<RandomizationModeVO>(modes.length);
			for (int i = 0; i < modes.length; i++) {
				result.add(L10nUtil.createRandomizationModeVO(Locales.USER, modes[i]));
			}
		} else {
			result = new ArrayList<RandomizationModeVO>();
		}
		return result;
	}

	@Override
	protected Collection<SexVO> handleGetSexes(AuthenticationVO auth) throws Exception {
		Collection<SexVO> result;
		Sex[] sexes = Sex.values();
		if (sexes != null) {
			result = new ArrayList<SexVO>(sexes.length);
			for (int i = 0; i < sexes.length; i++) {
				result.add(L10nUtil.createSexVO(Locales.USER, sexes[i]));
			}
		} else {
			result = new ArrayList<SexVO>();
		}
		return result;
	}

	@Override
	protected SponsoringTypeVO handleGetSponsoringType(AuthenticationVO auth,
			Long typeId) throws Exception {
		SponsoringTypeDao sponsoringTypeDao = this.getSponsoringTypeDao();
		SponsoringType sponsoringType = CheckIDUtil.checkSponsoringTypeId(typeId, sponsoringTypeDao);
		return sponsoringTypeDao.toSponsoringTypeVO(sponsoringType);
	}

	@Override
	protected Collection<SponsoringTypeVO> handleGetSponsoringTypes(
			AuthenticationVO auth, Long typeId) throws Exception {
		SponsoringTypeDao sponsoringTypeDao = this.getSponsoringTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkSponsoringTypeId(typeId, sponsoringTypeDao);
		}
		Collection sponsoringTypes = sponsoringTypeDao.findByVisibleId(true, typeId);
		sponsoringTypeDao.toSponsoringTypeVOCollection(sponsoringTypes);
		return sponsoringTypes;
	}

	@Override
	protected Collection<StaffCategoryVO> handleGetStaffCategories(AuthenticationVO auth, Boolean person, Boolean organisation, Long categoryId)
			throws Exception {
		StaffCategoryDao staffCategoryDao = this.getStaffCategoryDao();
		if (categoryId != null) {
			CheckIDUtil.checkStaffCategoryId(categoryId, staffCategoryDao);
		}
		Collection categories = staffCategoryDao.findByPersonOrganisationId(person, organisation, categoryId);
		staffCategoryDao.toStaffCategoryVOCollection(categories);
		return categories;
	}

	@Override
	protected StaffCategoryVO handleGetStaffCategory(AuthenticationVO auth,
			Long categoryId) throws Exception {
		StaffCategoryDao staffCategoryDao = this.getStaffCategoryDao();
		return staffCategoryDao.toStaffCategoryVO(CheckIDUtil.checkStaffCategoryId(categoryId, staffCategoryDao));
	}

	@Override
	protected Collection<HyperlinkCategoryVO> handleGetStaffHyperlinkCategories(AuthenticationVO auth)
			throws Exception {
		return getHyperlinkCategoriesHelper(HyperlinkModule.STAFF_HYPERLINK, null);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetStaffJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.STAFF_JOURNAL, null);
	}

	@Override
	protected StaffStatusTypeVO handleGetStaffStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		StaffStatusTypeDao staffStatusTypeDao = this.getStaffStatusTypeDao();
		StaffStatusType statusType = CheckIDUtil.checkStaffStatusTypeId(typeId, staffStatusTypeDao);
		return staffStatusTypeDao.toStaffStatusTypeVO(statusType);
	}

	@Override
	protected Collection<StaffStatusTypeVO> handleGetStaffStatusTypes(AuthenticationVO auth, Long typeId)
			throws Exception {
		StaffStatusTypeDao staffStatusTypeDao = this.getStaffStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkStaffStatusTypeId(typeId, staffStatusTypeDao);
		}
		Collection statusTypes = staffStatusTypeDao.findByVisibleId(true, typeId);
		staffStatusTypeDao.toStaffStatusTypeVOCollection(statusTypes);
		return statusTypes;
	}

	@Override
	protected StaffTagVO handleGetStaffTag(AuthenticationVO auth, Long tagId)
			throws Exception {
		StaffTagDao staffTagDao = this.getStaffTagDao();
		StaffTag tag = CheckIDUtil.checkStaffTagId(tagId, staffTagDao);
		return staffTagDao.toStaffTagVO(tag);
	}

	@Override
	protected Collection<StreetVO> handleGetStreets(AuthenticationVO auth, String countryName,
			String zipCode, String cityName, String streetNameInfix, Integer limit)
			throws Exception {
		StreetDao streetDao = this.getStreetDao();
		Collection streets = streetDao.findStreets(countryName, zipCode, cityName, streetNameInfix, limit);
		streetDao.toStreetVOCollection(streets);
		return streets;
	}

	@Override
	protected SurveyStatusTypeVO handleGetSurveyStatusType(
			AuthenticationVO auth, Long typeId) throws Exception {
		SurveyStatusTypeDao surveyStatusTypeDao = this.getSurveyStatusTypeDao();
		SurveyStatusType surveyStatusType = CheckIDUtil.checkSurveyStatusTypeId(typeId, surveyStatusTypeDao);
		return surveyStatusTypeDao.toSurveyStatusTypeVO(surveyStatusType);
	}

	@Override
	protected Collection<SurveyStatusTypeVO> handleGetSurveyStatusTypes(
			AuthenticationVO auth, Long typeId) throws Exception {
		SurveyStatusTypeDao surveyStatusTypeDao = this.getSurveyStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkSurveyStatusTypeId(typeId, surveyStatusTypeDao);
		}
		Collection surveyStatusTypes = surveyStatusTypeDao.findByVisibleId(true, typeId);
		surveyStatusTypeDao.toSurveyStatusTypeVOCollection(surveyStatusTypes);
		return surveyStatusTypes;
	}

	@Override
	protected TeamMemberRoleVO handleGetTeamMemberRole(AuthenticationVO auth, Long roleId)
			throws Exception {
		TeamMemberRoleDao teamMemberRoleDao = this.getTeamMemberRoleDao();
		TeamMemberRole role = CheckIDUtil.checkTeamMemberRoleId(roleId, teamMemberRoleDao);
		return teamMemberRoleDao.toTeamMemberRoleVO(role);
	}

	@Override
	protected TimelineEventTypeVO handleGetTimelineEventType(AuthenticationVO auth, Long typeId)
			throws Exception {
		TimelineEventTypeDao timelineEventTypeDao = this.getTimelineEventTypeDao();
		TimelineEventType type = CheckIDUtil.checkTimelineEventTypeId(typeId, timelineEventTypeDao);
		return timelineEventTypeDao.toTimelineEventTypeVO(type);
	}

	@Override
	protected TimeZoneVO handleGetTimeZone(AuthenticationVO auth, String timeZoneID)
			throws Exception {
		TimeZone timeZone = CommonUtil.timeZoneFromString(timeZoneID);
		TimeZoneVO timeZoneVO = new TimeZoneVO();
		timeZoneVO.setTimeZoneID(CommonUtil.timeZoneToString(timeZone));
		timeZoneVO.setName(CommonUtil.timeZoneToDisplayString(timeZone, L10nUtil.getLocale(Locales.USER)));
		timeZoneVO.setRawOffset(timeZone.getRawOffset());
		return timeZoneVO;
	}

	@Override
	protected Collection<TimeZoneVO> handleGetTimeZones(AuthenticationVO auth)
			throws Exception {
		ArrayList<TimeZone> timeZones = CoreUtil.getTimeZones();
		ArrayList<TimeZoneVO> result = new ArrayList<TimeZoneVO>(timeZones.size());
		Iterator<TimeZone> it = timeZones.iterator();
		Locale userLocale = L10nUtil.getLocale(Locales.USER);
		while (it.hasNext()) {
			TimeZone timeZone = it.next();
			TimeZoneVO timeZoneVO = new TimeZoneVO();
			timeZoneVO.setTimeZoneID(CommonUtil.timeZoneToString(timeZone));
			timeZoneVO.setName(CommonUtil.timeZoneToDisplayString(timeZone, userLocale));
			timeZoneVO.setRawOffset(timeZone.getRawOffset());
			result.add(timeZoneVO);
		}
		return result;
	}

	@Override
	protected Collection<HyperlinkCategoryVO> handleGetTrialHyperlinkCategories(AuthenticationVO auth)
			throws Exception {
		return getHyperlinkCategoriesHelper(HyperlinkModule.TRIAL_HYPERLINK, null);
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetTrialJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.TRIAL_JOURNAL, null);
	}

	@Override
	protected TrialStatusTypeVO handleGetTrialStatusType(AuthenticationVO auth, Long typeId)
			throws Exception {
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		TrialStatusType statusType = CheckIDUtil.checkTrialStatusTypeId(typeId, trialStatusTypeDao);
		return trialStatusTypeDao.toTrialStatusTypeVO(statusType);
	}

	@Override
	protected Collection<TrialStatusTypeVO> handleGetTrialStatusTypeTransitions(
			AuthenticationVO auth, Long typeId) throws Exception {
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkTrialStatusTypeId(typeId, trialStatusTypeDao);
		}
		Collection trialStates = trialStatusTypeDao.findTransitions(typeId);
		trialStatusTypeDao.toTrialStatusTypeVOCollection(trialStates);
		return trialStates;
	}

	@Override
	protected TrialTagVO handleGetTrialTag(AuthenticationVO auth, Long tagId) throws Exception {
		TrialTagDao trialTagDao = this.getTrialTagDao();
		TrialTag tag = CheckIDUtil.checkTrialTagId(tagId, trialTagDao);
		return trialTagDao.toTrialTagVO(tag);
	}

	@Override
	protected TrialTypeVO handleGetTrialType(AuthenticationVO auth, Long typeId)
			throws Exception {
		TrialTypeDao trialTypeDao = this.getTrialTypeDao();
		TrialType trialType = CheckIDUtil.checkTrialTypeId(typeId, trialTypeDao);
		return trialTypeDao.toTrialTypeVO(trialType);
	}

	@Override
	protected Collection<TrialTypeVO> handleGetTrialTypes(
			AuthenticationVO auth, Long typeId) throws Exception {
		TrialTypeDao trialTypeDao = this.getTrialTypeDao();
		if (typeId != null) {
			CheckIDUtil.checkTrialTypeId(typeId, trialTypeDao);
		}
		Collection trialTypes = trialTypeDao.findByVisibleId(true, typeId);
		trialTypeDao.toTrialTypeVOCollection(trialTypes);
		return trialTypes;
	}

	@Override
	protected Collection<JournalCategoryVO> handleGetUserJournalCategories(AuthenticationVO auth)
			throws Exception {
		return getJournalCategoriesHelper(JournalModule.USER_JOURNAL, null);
	}

	@Override
	protected Collection<VariablePeriodVO> handleGetVariablePeriods(AuthenticationVO auth)
			throws Exception {
		Collection<VariablePeriodVO> result;
		VariablePeriod[] variablePeriods = VariablePeriod.values();
		if (variablePeriods != null) {
			result = new ArrayList<VariablePeriodVO>(variablePeriods.length);
			for (int i = 0; i < variablePeriods.length; i++) {
				result.add(L10nUtil.createVariablePeriodVO(Locales.USER, variablePeriods[i]));
			}
		} else {
			result = new ArrayList<VariablePeriodVO>();
		}
		return result;
	}

	@Override
	protected Collection<VariablePeriodVO> handleGetVariablePeriodsWoExplicit(AuthenticationVO auth)
			throws Exception {
		Collection<VariablePeriodVO> result;
		VariablePeriod[] variablePeriods = VariablePeriod.values();
		if (variablePeriods != null) {
			result = new ArrayList<VariablePeriodVO>(variablePeriods.length - 1);
			for (int i = 0; i < variablePeriods.length; i++) {
				if (!VariablePeriod.EXPLICIT.equals(variablePeriods[i])) {
					result.add(L10nUtil.createVariablePeriodVO(Locales.USER, variablePeriods[i]));
				}
			}
		} else {
			result = new ArrayList<VariablePeriodVO>();
		}
		return result;
	}

	@Override
	protected VisitTypeVO handleGetVisitType(AuthenticationVO auth, Long typeId)
			throws Exception {
		VisitTypeDao visitTypeDao = this.getVisitTypeDao();
		VisitType type = CheckIDUtil.checkVisitTypeId(typeId, visitTypeDao);
		return visitTypeDao.toVisitTypeVO(type);
	}

	@Override
	protected Collection<ZipVO> handleGetZips(AuthenticationVO auth, String countryNameInfix,
			String zipCodePrefix, String cityNameInfix, Integer limit) throws Exception {
		ZipDao zipDao = this.getZipDao();
		Collection zips = zipDao.findZips(countryNameInfix, zipCodePrefix, cityNameInfix, limit);
		zipDao.toZipVOCollection(zips);
		return zips;
	}

	@Override
	protected DepartmentVO handleGetDepartment(AuthenticationVO auth, Long departmentId) throws Exception {
		DepartmentDao departmentDao = this.getDepartmentDao();
		Department department = CheckIDUtil.checkDepartmentId(departmentId, departmentDao);
		return departmentDao.toDepartmentVO(department);
	}

	@Override
	protected Collection<JobTypeVO> handleGetTrialJobTypes(AuthenticationVO auth, Long trialId) throws Exception {
		return getJobTypesHelper(JobModule.TRIAL_JOB, null, trialId);
	}

	@Override
	protected Collection<JobTypeVO> handleGetJobTypes(AuthenticationVO auth, JobModule module, Long typeId, Long trialId) throws Exception {
		return getJobTypesHelper(module, typeId, trialId);
	}

	@Override
	protected JobTypeVO handleGetJobType(AuthenticationVO auth, Long typeId) throws Exception {
		JobTypeDao jobTypeDao = this.getJobTypeDao();
		JobType job = CheckIDUtil.checkJobTypeId(typeId, jobTypeDao);
		return jobTypeDao.toJobTypeVO(job);
	}

	@Override
	protected Collection<JobTypeVO> handleGetProbandJobTypes(AuthenticationVO auth) throws Exception {
		return getJobTypesHelper(JobModule.PROBAND_JOB, null, null);
	}

	@Override
	protected Collection<JobTypeVO> handleGetInputFieldJobTypes(AuthenticationVO auth) throws Exception {
		return getJobTypesHelper(JobModule.INPUT_FIELD_JOB, null, null);
	}

	@Override
	protected Collection<OTPAuthenticatorTypeVO> handleGetOTPAuthenticatorTypes(AuthenticationVO auth) throws Exception {
		Collection<OTPAuthenticatorTypeVO> result;
		OTPAuthenticatorType[] types = OTPAuthenticatorType.values();
		if (types != null) {
			result = new ArrayList<OTPAuthenticatorTypeVO>(types.length);
			for (int i = 0; i < types.length; i++) {
				result.add(L10nUtil.createOTPAuthenticatorTypeVO(Locales.USER, types[i]));
			}
		} else {
			result = new ArrayList<OTPAuthenticatorTypeVO>();
		}
		return result;
	}
}