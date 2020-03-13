package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFValidationStatus;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationTypeVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.DBModuleVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ECRFValidationStatusVO;
import org.phoenixctms.ctsms.vo.EventImportanceVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.JobStatusVO;
import org.phoenixctms.ctsms.vo.JournalModuleVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.PermissionProfileGroupVO;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.springframework.beans.factory.annotation.Autowired;

public final class L10nUtil {

	public enum Locales {
		USER,
		JOURNAL,
		AUDIT_TRAIL,
		NOTIFICATION,
		PROBAND_LIST_STATUS_ENTRY_REASON,
		CV_PDF,
		REIMBURSEMENTS_PDF,
		COURSE_PARTICIPANT_LIST_PDF,
		PROBAND_LETTER_PDF,
		COURSE_CERTIFICATE_PDF,
		ECRF_PDF,
		INQUIRIES_PDF,
		PROBAND_LIST_ENTRY_TAGS_PDF,
		DEFAULT,
		DE,
		EN
	}

	private final static String CREATE_ENUMERATION_VO_METHOD_PREFIX = "create";
	private static String departmentsBundleBasename;
	private static String inventoryCategoriesBundleBasename;
	private static String inventoryStatusTypesBundleBasename;
	private static String inventoryTagsBundleBasename;
	private static String maintenanceTypesBundleBasename;
	private static String maintenanceTitlePresetsBundleBasename;
	private static String serviceExceptionMessagesBundleBasename;
	private static String authorisationExceptionMessagesBundleBasename;
	private static String authenticationExceptionMessagesBundleBasename;
	private static String staffCategoriesBundleBasename;
	private static String staffTagsBundleBasename;
	private static String addressTypesBundleBasename;
	private static String contactDetailTypesBundleBasename;
	private static String courseParticipationStatusTypesBundleBasename;
	private static String cvSectionsBundleBasename;
	private static String cvSectionTitlePresetBundleBasename;
	private static String cvSectionDescriptionBundleBasename;
	private static String trainingRecordSectionsBundleBasename;
	private static String trainingRecordSectionDescriptionBundleBasename;
	private static String staffStatusTypesBundleBasename;
	private static String cvPdfLabelsBundleBasename;
	private static String ecrfPdfLabelsBundleBasename;
	private static String inquiriesPdfLabelsBundleBasename;
	private static String probandListEntryTagsPdfLabelsBundleBasename;
	private static String reimbursementsPdfLabelsBundleBasename;
	private static String courseParticipantListPdfLabelsBundleBasename;
	private static String probandLetterPdfLabelsBundleBasename;
	private static String courseCertificatePdfLabelsBundleBasename;
	private static String searchResultExcelLabelsBundleBasename;
	private static String journalExcelLabelsBundleBasename;
	private static String probandListExcelLabelsBundleBasename;
	private static String visitScheduleExcelLabelsBundleBasename;
	private static String auditTrailExcelLabelsBundleBasename;
	private static String inventoryBookingsExcelLabelsBundleBasename;
	private static String teamMembersExcelLabelsBundleBasename;
	private static String reimbursementsExcelLabelsBundleBasename;
	private static String courseCategoriesBundleBasename;
	private static String lecturerCompetencesBundleBasename;
	private static String probandCategoriesBundleBasename;
	private static String privacyConsentStatusTypeBundleBasename;
	private static String messagesBundleBasename;
	private static String authenticationTypeNamesBundleBasename;
	private static String variablePeriodNamesBundleBasename;
	private static String inputFieldTypeNamesBundleBasename;
	private static String eventImportanceNamesBundleBasename;
	private static String sexNamesBundleBasename;
	private static String randomizationModeNamesBundleBasename;
	private static String jobStatusNamesBundleBasename;
	private static String ecrfValidationStatusNamesBundleBasename;
	private static String paymentMethodNamesBundleBasename;
	private static String dbModuleNamesBundleBasename;
	private static String journalModuleNamesBundleBasename;
	private static String hyperlinkModuleNamesBundleBasename;
	private static String fileModuleNamesBundleBasename;
	private static String jobModuleNamesBundleBasename;
	private static String criterionTiesBundleBasename;
	private static String criterionRestrictionsBundleBasename;
	private static String criterionPropertiesBundleBasename;
	private static String hyperlinkCategoriesBundleBasename;
	private static String hyperlinkTitlePresetsBundleBasename;
	private static String jobTypeNamesBundleBasename;
	private static String jobTypeDescriptionsBundleBasename;
	private static String journalCategoriesBundleBasename;
	private static String journalTitlePresetsBundleBasename;
	private static String systemMessageTitlesBundleBasename;
	private static String systemMessageCommentsBundleBasename;
	private static String systemMessageCommentFieldLabelsBundleBasename;
	private static String auditTrailChangeCommentFieldLabelsBundleBasename;
	private static String probandListStatusReasonsBundleBasename;
	private static String holidayBundleBasename;
	private static String notificationTypeNamesBundleBasename;
	private static String notificationSubjectsBundleBasename;
	private static String notificationMessageTemplatesBundleBasename;
	private static String permissionProfileNamesBundleBasename;
	private static String permissionProfileGroupNamesBundleBasename;
	private static Locale journalDatabaseWriteLocale;
	private static Locale auditTrailDatabaseWriteLocale;
	private static Locale notificationsDatabaseWriteLocale;
	private static Locale probandListStatusReasonsDatabaseWriteLocale;
	private static Locale cvPdfLocale;
	private static Locale reimbursementsPdfLocale;
	private static Locale ecrfPdfLocale;
	private static Locale inquiriesPdfLocale;
	private static Locale probandListEntryTagsPdfLocale;
	private static Locale courseParticipantListPdfLocale;
	private static Locale probandLetterPdfLocale;
	private static Locale courseCertificatePdfLocale;
	private static String visitTypesBundleBasename;
	private static String trialTagsBundleBasename;
	private static String trialStatusTypesBundleBasename;
	private static String massMailStatusTypesBundleBasename;
	private static String ecrfStatusTypesBundleBasename;
	private static String ecrfFieldStatusTypesBundleBasename;
	private static String trialTypesBundleBasename;
	private static String massMailTypesBundleBasename;
	private static String sponsoringTypesBundleBasename;
	private static String surveyStatusTypesBundleBasename;
	private static String probandStatusTypesBundleBasename;
	private static String probandTagsBundleBasename;
	private static String timelineEventTypesBundleBasename;
	private static String timelineEventTitlePresetsBundleBasename;
	private static String teamMemberRolesBundleBasename;
	private static String probandListStatusTypesBundleBasename;
	private static String inputFieldSelectionSetValuesBundleBasename;
	private static String inputFieldValidationErrorMsgsBundleBasename;
	private static String inputFieldTitlesBundleBasename;
	private static String inputFieldTextPresetsBundleBasename;
	private static String inputFieldNamesBundleBasename;
	private static String inputFieldCommentsBundleBasename;

	public static boolean containsAuditTrailChangeCommentFieldLabel(Locales locale, String l10nKey) {
		return CommonUtil.bundleContainsKey(l10nKey, getBundle(locale, auditTrailChangeCommentFieldLabelsBundleBasename));
	}

	public static boolean containsProbandListStatusReason(Locales locale, String l10nKey) {
		return CommonUtil.bundleContainsKey(l10nKey, getBundle(locale, probandListStatusReasonsBundleBasename));
	}

	public static boolean containsReimbursementsPdfLabel(Locales locale, String l10nKey) {
		return CommonUtil.bundleContainsKey(l10nKey, getBundle(locale, reimbursementsPdfLabelsBundleBasename));
	}

	public static boolean containsSystemMessageCommentFieldLabel(Locales locale, String l10nKey) {
		return CommonUtil.bundleContainsKey(l10nKey, getBundle(locale, systemMessageCommentFieldLabelsBundleBasename));
	}

	public static AuthenticationTypeVO createAuthenticationTypeVO(Locales locale, AuthenticationType method) {
		AuthenticationTypeVO methodVO;
		if (method != null) {
			methodVO = new AuthenticationTypeVO();
			methodVO.setMethod(method);
			methodVO.setNameL10nKey(method.name());
			methodVO.setName(getAuthenticationTypeName(locale, method.name()));
		} else {
			methodVO = null;
		}
		return methodVO;
	}

	public static DBModuleVO createDBModuleVO(Locales locale, DBModule module) {
		DBModuleVO dbModuleVO;
		if (module != null) {
			dbModuleVO = new DBModuleVO();
			dbModuleVO.setModule(module);
			dbModuleVO.setNameL10nKey(module.name());
			dbModuleVO.setName(getDBModuleName(locale, module.name()));
		} else {
			dbModuleVO = null;
		}
		return dbModuleVO;
	}

	public static ECRFValidationStatusVO createEcrfValidationStatusVO(Locales locale, ECRFValidationStatus validationStatus) {
		ECRFValidationStatusVO validationStatusVO;
		if (validationStatus != null) {
			validationStatusVO = new ECRFValidationStatusVO();
			validationStatusVO.setValidationStatus(validationStatus);
			validationStatusVO.setNameL10nKey(validationStatus.name());
			validationStatusVO.setName(getEcrfValidationStatusName(locale, validationStatus.name()));
		} else {
			validationStatusVO = null;
		}
		return validationStatusVO;
	}

	public static Object createEnumerationVO(Locales locale, String enumName, Object enumItem) throws Exception {
		return AssociationPath.findMethod(CREATE_ENUMERATION_VO_METHOD_PREFIX + CoreUtil.getEnumerationValueObjectName(enumName), false,
				L10nUtil.class.getMethods()).invoke(null, locale, enumItem);
	}

	public static EventImportanceVO createEventImportanceVO(Locales locale, EventImportance eventImportance) {
		EventImportanceVO eventImportanceVO;
		if (eventImportance != null) {
			eventImportanceVO = new EventImportanceVO();
			eventImportanceVO.setImportance(eventImportance);
			eventImportanceVO.setNameL10nKey(eventImportance.name());
			eventImportanceVO.setName(getEventImportanceName(locale, eventImportance.name()));
		} else {
			eventImportanceVO = null;
		}
		return eventImportanceVO;
	}

	public static JobStatusVO createJobStatusVO(Locales locale, JobStatus jobStatus) {
		JobStatusVO jobStatusVO;
		if (jobStatus != null) {
			jobStatusVO = new JobStatusVO();
			jobStatusVO.setJobStatus(jobStatus);
			jobStatusVO.setNameL10nKey(jobStatus.name());
			jobStatusVO.setName(getJobStatusName(locale, jobStatus.name()));
		} else {
			jobStatusVO = null;
		}
		return jobStatusVO;
	}

	public static InputFieldTypeVO createInputFieldTypeVO(Locales locale, InputFieldType fieldType) {
		InputFieldTypeVO inputFieldTypeVO;
		if (fieldType != null) {
			inputFieldTypeVO = new InputFieldTypeVO();
			inputFieldTypeVO.setType(fieldType);
			inputFieldTypeVO.setNameL10nKey(fieldType.name());
			inputFieldTypeVO.setName(getInputFieldTypeName(locale, fieldType.name()));
		} else {
			inputFieldTypeVO = null;
		}
		return inputFieldTypeVO;
	}

	public static JournalModuleVO createJournalModuleVO(Locales locale, JournalModule module) {
		JournalModuleVO journalModuleVO;
		if (module != null) {
			journalModuleVO = new JournalModuleVO();
			journalModuleVO.setModule(module);
			journalModuleVO.setNameL10nKey(module.name());
			journalModuleVO.setName(getJournalModuleName(locale, module.name()));
		} else {
			journalModuleVO = null;
		}
		return journalModuleVO;
	}

	public static PaymentMethodVO createPaymentMethodVO(Locales locale, PaymentMethod paymentMethod) {
		PaymentMethodVO paymentMethodVO;
		if (paymentMethod != null) {
			paymentMethodVO = new PaymentMethodVO();
			paymentMethodVO.setPaymentMethod(paymentMethod);
			paymentMethodVO.setNameL10nKey(paymentMethod.name());
			paymentMethodVO.setName(getPaymentMethodName(locale, paymentMethod.name()));
		} else {
			paymentMethodVO = null;
		}
		return paymentMethodVO;
	}

	public static PermissionProfileVO createPermissionProfileVO(Locales locale, PermissionProfile profile) {
		PermissionProfileVO permissionProfileVO;
		if (profile != null) {
			permissionProfileVO = new PermissionProfileVO();
			permissionProfileVO.setProfile(profile);
			permissionProfileVO.setProfileName(getPermissionProfileName(locale, profile.name()));
			PermissionProfileGroup group = PermissionProfileGrouping.getGroupFromPermissionProfile(profile);
			if (group != null) {
				PermissionProfileGroupVO permissionProfileGroupVO = new PermissionProfileGroupVO();
				permissionProfileGroupVO.setProfileGroup(group);
				permissionProfileGroupVO.setProfileGroupName(getPermissionProfileGroupName(locale, group.name()));
				permissionProfileVO.setProfileGroup(permissionProfileGroupVO);
			}
		} else {
			permissionProfileVO = null;
		}
		return permissionProfileVO;
	}

	public static RandomizationModeVO createRandomizationModeVO(Locales locale, RandomizationMode mode) {
		RandomizationModeVO modeVO;
		if (mode != null) {
			modeVO = new RandomizationModeVO();
			modeVO.setMode(mode);
			modeVO.setNameL10nKey(mode.name());
			modeVO.setName(getRandomizationModeName(locale, mode.name()));
		} else {
			modeVO = null;
		}
		return modeVO;
	}

	public static SexVO createSexVO(Locales locale, Sex sex) {
		SexVO sexVO;
		if (sex != null) {
			sexVO = new SexVO();
			sexVO.setSex(sex);
			sexVO.setNameL10nKey(sex.name());
			sexVO.setName(getSexName(locale, sex.name()));
		} else {
			sexVO = null;
		}
		return sexVO;
	}

	public static VariablePeriodVO createVariablePeriodVO(Locales locale, VariablePeriod period) {
		VariablePeriodVO variablePeriodVO;
		if (period != null) {
			variablePeriodVO = new VariablePeriodVO();
			variablePeriodVO.setPeriod(period);
			variablePeriodVO.setNameL10nKey(period.name());
			variablePeriodVO.setName(getVariablePeriodName(locale, period.name()));
		} else {
			variablePeriodVO = null;
		}
		return variablePeriodVO;
	}

	public static String getAddressTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, addressTypesBundleBasename), DefaultMessages.ADDRESS_TYPE_NAME);
	}

	public static String getAuditTrailChangeCommentFieldLabel(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, auditTrailChangeCommentFieldLabelsBundleBasename), DefaultMessages.AUDIT_TRAIL_CHANGE_COMMENT_FIELD_LABEL);
	}

	public static ArrayList<String> getAuditTrailExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, auditTrailExcelLabelsBundleBasename), defaultValue);
	}

	public static String getAuditTrailExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, auditTrailExcelLabelsBundleBasename), defaultName);
	}

	public static String getAuthenticationTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, authenticationTypeNamesBundleBasename), DefaultMessages.AUTHENTICATION_TYPE_NAME);
	}

	private static ResourceBundle getBundle(Locales locale, String baseName) {
		return CommonUtil.getBundle(baseName, getLocale(locale));
	}

	public static String getContactDetailTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, contactDetailTypesBundleBasename), DefaultMessages.CONTACT_DETAIL_TYPE_NAME);
	}

	public static String getCourseCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, courseCategoriesBundleBasename), DefaultMessages.COURSE_CATEGORY_NAME);
	}

	public static String getCourseCertificatePDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, courseCertificatePdfLabelsBundleBasename), defaultName);
	}

	public static String getCourseParticipantListPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, courseParticipantListPdfLabelsBundleBasename), defaultName);
	}

	public static String getCourseParticipationStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, courseParticipationStatusTypesBundleBasename), DefaultMessages.COURSE_PARTICIPATION_STATUS_TYPE_NAME);
	}

	public static String getCriterionPropertyName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, criterionPropertiesBundleBasename), DefaultMessages.CRITERION_PROPERTY_NAME);
	}

	public static String getCriterionRestrictionName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, criterionRestrictionsBundleBasename), DefaultMessages.CRITERION_RESTRICTION_NAME);
	}

	public static String getCriterionTieName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, criterionTiesBundleBasename), DefaultMessages.CRITERION_TIE_NAME);
	}

	public static String getCVPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, cvPdfLabelsBundleBasename), defaultName);
	}

	public static String getCvSectionDescription(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, cvSectionDescriptionBundleBasename), DefaultMessages.CV_SECTION_DESCRIPTION);
	}

	public static String getCvSectionName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, cvSectionsBundleBasename), DefaultMessages.CV_SECTION_NAME);
	}

	public static String getCvSectionTitlePreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, cvSectionTitlePresetBundleBasename), DefaultMessages.CV_SECTION_TITLE_PRESET);
	}

	public static String getTrainingRecordSectionDescription(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, trainingRecordSectionDescriptionBundleBasename), DefaultMessages.TRAINING_RECORD_SECTION_DESCRIPTION);
	}

	public static String getTrainingRecordSectionName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, trainingRecordSectionsBundleBasename), DefaultMessages.TRAINING_RECORD_SECTION_NAME);
	}

	public static String getDBModuleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, dbModuleNamesBundleBasename), DefaultMessages.DB_MODULE_NAME);
	}

	public static String getDepartmentL10nKey(String l10nKey, CourseOutVO course) {
		if (course != null) {
			return getDepartmentL10nKey(l10nKey, course.getDepartment());
		}
		return l10nKey;
	}

	public static String getDepartmentL10nKey(String l10nKey, DepartmentVO department) {
		StringBuilder result = new StringBuilder(l10nKey);
		if (department != null) {
			result.append("_");
			result.append(department.getNameL10nKey());
		}
		return result.toString();
	}

	public static String getDepartmentL10nKey(String l10nKey, ProbandOutVO proband) {
		if (proband != null) {
			return getDepartmentL10nKey(l10nKey, proband.getDepartment());
		}
		return l10nKey;
	}

	public static String getDepartmentL10nKey(String l10nKey, StaffOutVO staff) {
		if (staff != null) {
			return getDepartmentL10nKey(l10nKey, staff.getDepartment());
		}
		return l10nKey;
	}

	public static String getDepartmentL10nKey(String l10nKey, TrialOutVO trial) {
		if (trial != null) {
			return getDepartmentL10nKey(l10nKey, trial.getDepartment());
		}
		return l10nKey;
	}

	public static String getDepartmentName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, departmentsBundleBasename), DefaultMessages.DEPARTMENT_NAME);
	}

	public static String getEcrfFieldStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, ecrfFieldStatusTypesBundleBasename), DefaultMessages.ECRF_FIELD_STATUS_TYPE_NAME);
	}

	public static String getEcrfPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, ecrfPdfLabelsBundleBasename), defaultName);
	}

	public static String getEcrfStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, ecrfStatusTypesBundleBasename), DefaultMessages.ECRF_STATUS_TYPE_NAME);
	}

	public static String getEcrfValidationStatusName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, ecrfValidationStatusNamesBundleBasename), DefaultMessages.ECRF_VALIDATION_STATUS_NAME);
	}

	public static String getEventImportanceName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, eventImportanceNamesBundleBasename), DefaultMessages.EVENT_IMPORTANCE_NAME);
	}

	public static String getJobStatusName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, jobStatusNamesBundleBasename), DefaultMessages.JOB_STATUS_NAME);
	}

	public static String getFileModuleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, fileModuleNamesBundleBasename), DefaultMessages.FILE_MODULE_NAME);
	}

	public static String getJobModuleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, jobModuleNamesBundleBasename), DefaultMessages.JOB_MODULE_NAME);
	}

	public static String getHolidayName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, holidayBundleBasename), DefaultMessages.HOLIDAY_NAME);
	}

	public static String getHyperlinkCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, hyperlinkCategoriesBundleBasename), DefaultMessages.HYPERLINK_CATEGORY_NAME);
	}

	public static String getJobTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, jobTypeNamesBundleBasename), DefaultMessages.JOB_TYPE_NAME);
	}

	public static String getJobTypeDescription(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, jobTypeDescriptionsBundleBasename), DefaultMessages.JOB_TYPE_DESCRIPTION);
	}

	public static String getHyperlinkModuleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, hyperlinkModuleNamesBundleBasename), DefaultMessages.HYPERLINK_MODULE_NAME);
	}

	public static String getHyperlinkTitlePreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, hyperlinkTitlePresetsBundleBasename), DefaultMessages.HYPERLINK_TITLE_PRESET);
	}

	public static String getInputFieldComment(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldCommentsBundleBasename), DefaultMessages.INPUT_FIELD_COMMENT);
	}

	public static String getInputFieldName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldNamesBundleBasename), DefaultMessages.INPUT_FIELD_NAME);
	}

	public static String getInputFieldSelectionSetValueName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldSelectionSetValuesBundleBasename), DefaultMessages.INPUT_FIELD_SELECTEION_SET_VALUE_NAME);
	}

	public static String getInputFieldTextPreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldTextPresetsBundleBasename), DefaultMessages.INPUT_FIELD_TEXT_PRESET);
	}

	public static String getInputFieldTitle(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldTitlesBundleBasename), DefaultMessages.INPUT_FIELD_TITLE);
	}

	public static String getInputFieldTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldTypeNamesBundleBasename), DefaultMessages.INPUT_FIELD_TYPE_NAME);
	}

	public static String getInputFieldValidationErrorMsg(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inputFieldValidationErrorMsgsBundleBasename), DefaultMessages.INPUT_FIELD_VALIDATION_ERROR_MSG);
	}

	public static String getInquiriesPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, inquiriesPdfLabelsBundleBasename), defaultName);
	}

	public static ArrayList<String> getInventoryBookingsExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, inventoryBookingsExcelLabelsBundleBasename), defaultValue);
	}

	public static String getInventoryBookingsExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, inventoryBookingsExcelLabelsBundleBasename), defaultName);
	}

	public static String getInventoryCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inventoryCategoriesBundleBasename), DefaultMessages.INVENTORY_CATEGORY_NAME);
	}

	public static String getInventoryStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inventoryStatusTypesBundleBasename), DefaultMessages.INVENTORY_STATUS_TYPE_NAME);
	}

	public static String getInventoryTagName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, inventoryTagsBundleBasename), DefaultMessages.INVENTORY_TAG_NAME);
	}

	public static String getJournalCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, journalCategoriesBundleBasename), DefaultMessages.JOURNAL_CATEGORY_NAME);
	}

	public static ArrayList<String> getJournalExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, journalExcelLabelsBundleBasename), defaultValue);
	}

	public static String getJournalExcelLabel(Locales locale, String l10nKey, String defaultName) {
		return CommonUtil.getString(l10nKey, getBundle(locale, journalExcelLabelsBundleBasename), defaultName);
	}

	public static String getJournalModuleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, journalModuleNamesBundleBasename), DefaultMessages.JOURNAL_MODULE_NAME);
	}

	public static String getJournalTitlePreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, journalTitlePresetsBundleBasename), DefaultMessages.JOURNAL_TITLE_PRESET);
	}

	public static String getLecturerCompetenceName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, lecturerCompetencesBundleBasename), DefaultMessages.LECTURER_COMPETENCE_NAME);
	}

	public static Locale getLocale(Locales locale) {
		switch (locale) {
			case USER:
				return CoreUtil.getUserContext().getLocale();
			case JOURNAL:
				return journalDatabaseWriteLocale;
			case AUDIT_TRAIL:
				return auditTrailDatabaseWriteLocale;
			case NOTIFICATION:
				return notificationsDatabaseWriteLocale;
			case PROBAND_LIST_STATUS_ENTRY_REASON:
				return probandListStatusReasonsDatabaseWriteLocale;
			case CV_PDF:
				return cvPdfLocale;
			case REIMBURSEMENTS_PDF:
				return reimbursementsPdfLocale;
			case COURSE_PARTICIPANT_LIST_PDF:
				return courseParticipantListPdfLocale;
			case PROBAND_LETTER_PDF:
				return probandLetterPdfLocale;
			case COURSE_CERTIFICATE_PDF:
				return courseCertificatePdfLocale;
			case ECRF_PDF:
				return ecrfPdfLocale;
			case INQUIRIES_PDF:
				return inquiriesPdfLocale;
			case PROBAND_LIST_ENTRY_TAGS_PDF:
				return probandListEntryTagsPdfLocale;
			case DE:
				return Locale.GERMAN;
			case EN:
				return Locale.ENGLISH;
			case DEFAULT:
			default:
				return Locale.getDefault();
		}
	}

	public static Locales getLocales(Locale locale) {
		if (Locale.GERMAN.equals(locale)) {
			return Locales.DE;
		} else if (Locale.ENGLISH.equals(locale)) {
			return Locales.EN;
		}
		return Locales.DEFAULT;
	}

	public static Locales getLocales(String locale) {
		try {
			return getLocales(CommonUtil.localeFromString(locale));
		} catch (Exception e) {
			return Locales.DEFAULT;
		}
	}

	public static String getMaintenanceTitlePreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, maintenanceTitlePresetsBundleBasename), DefaultMessages.MAINTENANCE_TITLE_PRESET);
	}

	public static String getMaintenanceTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, maintenanceTypesBundleBasename), DefaultMessages.MAINTENANCE_TYPE_NAME);
	}

	public static String getMassMailStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, massMailStatusTypesBundleBasename), DefaultMessages.MASS_MAIL_STATUS_TYPE_NAME);
	}

	public static String getMassMailTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, massMailTypesBundleBasename), DefaultMessages.MASS_MAIL_TYPE_NAME);
	}

	public static String getMessage(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, messagesBundleBasename), defaultName);
	}

	public static String getMessage(String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(Locales.USER, messagesBundleBasename), defaultName);
	}

	public static String getNotificationMessageTemplate(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, notificationMessageTemplatesBundleBasename), DefaultMessages.NOTIFICATION_MESSAGE_TEMPLATE);
	}

	public static String getNotificationSubject(Locales locale, String l10nKey, Object[] args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, notificationSubjectsBundleBasename), DefaultMessages.NOTIFICATION_SUBJECT);
	}

	public static String getNotificationTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, notificationTypeNamesBundleBasename), DefaultMessages.NOTIFICATION_TYPE_NAME);
	}

	public static String getPaymentMethodName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, paymentMethodNamesBundleBasename), DefaultMessages.PAYMENT_METHOD_NAME);
	}

	public static String getPermissionProfileGroupName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, permissionProfileGroupNamesBundleBasename), DefaultMessages.PERMISSION_PROFILE_GROUP_NAME);
	}

	public static String getPermissionProfileName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, permissionProfileNamesBundleBasename), DefaultMessages.PERMISSION_PROFILE_NAME);
	}

	public static String getPrivacyConsentStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, privacyConsentStatusTypeBundleBasename), DefaultMessages.PRIVACY_CONSENT_STATUS_TYPE_NAME);
	}

	public static String getProbandCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, probandCategoriesBundleBasename), DefaultMessages.PROBAND_CATEGORY_NAME);
	}

	public static String getProbandLetterPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, probandLetterPdfLabelsBundleBasename), defaultName);
	}

	public static String getProbandListEntryTagsPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, probandListEntryTagsPdfLabelsBundleBasename), defaultName);
	}

	public static ArrayList<String> getProbandListExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, probandListExcelLabelsBundleBasename), defaultValue);
	}

	public static String getProbandListExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, probandListExcelLabelsBundleBasename), defaultName);
	}

	public static String getProbandListStatusReason(Locales locale, String l10nKey, String defaultReason, Object[] args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, probandListStatusReasonsBundleBasename), defaultReason);
	}

	public static String getProbandListStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, probandListStatusTypesBundleBasename), DefaultMessages.PROBAND_LIST_STATUS_TYPE_NAME);
	}

	public static String getProbandStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, probandStatusTypesBundleBasename), DefaultMessages.PROBAND_STATUS_TYPE_NAME);
	}

	public static String getProbandTagName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, probandTagsBundleBasename), DefaultMessages.PROBAND_TAG_NAME);
	}

	public static String getRandomizationModeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, randomizationModeNamesBundleBasename), DefaultMessages.RANDOMIZATION_MODE_NAME);
	}

	public static ArrayList<String> getReimbursementsExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, reimbursementsExcelLabelsBundleBasename), defaultValue);
	}

	public static String getReimbursementsExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, reimbursementsExcelLabelsBundleBasename), defaultName);
	}

	public static String getReimbursementsPDFLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, reimbursementsPdfLabelsBundleBasename), defaultName);
	}

	public static ArrayList<String> getSearchResultExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, searchResultExcelLabelsBundleBasename), defaultValue);
	}

	public static String getSearchResultExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, searchResultExcelLabelsBundleBasename), defaultName);
	}

	public static String getSexName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, sexNamesBundleBasename), DefaultMessages.SEX_NAME);
	}

	public static String getSponsoringTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, sponsoringTypesBundleBasename), DefaultMessages.SPONSORING_TYPE_NAME);
	}

	public static String getStaffCategoryName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, staffCategoriesBundleBasename), DefaultMessages.STAFF_CATEGORY_NAME);
	}

	public static String getStaffStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, staffStatusTypesBundleBasename), DefaultMessages.STAFF_STATUS_TYPE_NAME);
	}

	public static String getStaffTagName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, staffTagsBundleBasename), DefaultMessages.STAFF_TAG_NAME);
	}

	public static String getString(Locales locale, String l10nKey, String defaultName) {
		return CommonUtil.getString(l10nKey, getBundle(locale, messagesBundleBasename), defaultName);
	}

	public static String getString(String l10nKey, String defaultName) {
		return CommonUtil.getString(l10nKey, getBundle(Locales.USER, messagesBundleBasename), defaultName);
	}

	public static String getSurveyStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, surveyStatusTypesBundleBasename), DefaultMessages.SURVEY_STATUS_TYPE_NAME);
	}

	public static String getSystemMessageComment(Locales locale, String l10nKey, Object[] args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, systemMessageCommentsBundleBasename), DefaultMessages.SYSTEM_MESSAGE_COMMENT);
	}

	public static String getSystemMessageCommentFieldLabel(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, systemMessageCommentFieldLabelsBundleBasename), DefaultMessages.SYSTEM_MESSAGE_COMMENT_FIELD_LABEL);
	}

	public static String getSystemMessageTitle(Locales locale, String l10nKey, Object[] args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, systemMessageTitlesBundleBasename), DefaultMessages.SYSTEM_MESSAGE_TITLE);
	}

	public static String getSystemMessageTitleFormat(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, systemMessageTitlesBundleBasename), null);
	}

	public static String getTeamMemberRoleName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, teamMemberRolesBundleBasename), DefaultMessages.TEAM_MEMBER_ROLE_NAME);
	}

	public static ArrayList<String> getTeamMembersExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, teamMembersExcelLabelsBundleBasename), defaultValue);
	}

	public static String getTeamMembersExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, teamMembersExcelLabelsBundleBasename), defaultName);
	}

	public static String getTimelineEventTitlePreset(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, timelineEventTitlePresetsBundleBasename), DefaultMessages.TIMELINE_EVENT_TITLE_PRESET);
	}

	public static String getTimelineEventTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, timelineEventTypesBundleBasename), DefaultMessages.TIMELINE_EVENT_TYPE_NAME);
	}

	public static String getTrialStatusTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, trialStatusTypesBundleBasename), DefaultMessages.TRIAL_STATUS_TYPE_NAME);
	}

	public static String getTrialTagName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, trialTagsBundleBasename), DefaultMessages.TRIAL_TAG_NAME);
	}

	public static String getTrialTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, trialTypesBundleBasename), DefaultMessages.TRIAL_TYPE_NAME);
	}

	public static String getVariablePeriodName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, variablePeriodNamesBundleBasename), DefaultMessages.VARIABLE_PERIOD_NAME);
	}

	public static ArrayList<String> getVisitScheduleExcelColumns(Locales locale, String l10nKey, ArrayList<String> defaultValue) {
		return CommonUtil.getValueStringList(l10nKey, getBundle(locale, visitScheduleExcelLabelsBundleBasename), defaultValue);
	}

	public static String getVisitScheduleExcelLabel(Locales locale, String l10nKey, String defaultName, Object... args) {
		return CommonUtil.getMessage(l10nKey, args, getBundle(locale, visitScheduleExcelLabelsBundleBasename), defaultName);
	}

	public static String getVisitTypeName(Locales locale, String l10nKey) {
		return CommonUtil.getString(l10nKey, getBundle(locale, visitTypesBundleBasename), DefaultMessages.VISIT_TYPE_NAME);
	}

	public static AuthenticationException initAuthenticationException(String errorCode, Object... args) {
		String message = CommonUtil.getMessage(errorCode, args, getBundle(Locales.USER, authenticationExceptionMessagesBundleBasename),
				DefaultMessages.AUTHENTICATIONEXCEPTION_MESSAGE);
		AuthenticationException result = new AuthenticationException(message);
		result.setErrorCode(errorCode);
		return result;
	}

	public static AuthorisationException initAuthorisationException(String errorCode, Object... args) {
		String message = CommonUtil.getMessage(errorCode, args, getBundle(Locales.USER, authorisationExceptionMessagesBundleBasename),
				DefaultMessages.AUTHORISATIONEXCEPTION_MESSAGE);
		AuthorisationException result = new AuthorisationException(message);
		result.setErrorCode(errorCode);
		return result;
	}

	public static ServiceException initServiceException(String errorCode, Object... args) {
		String message = CommonUtil.getMessage(errorCode, args, getBundle(Locales.USER, serviceExceptionMessagesBundleBasename), DefaultMessages.SERVICEEXCEPTION_MESSAGE);
		ServiceException result = new ServiceException(message);
		result.setErrorCode(errorCode);
		return result;
	}

	private L10nUtil() {
	}

	private Locale getLocaleFromString(String locale) {
		if (CoreUtil.checkSystemLocale(locale)) {
			return CommonUtil.localeFromString(locale);
		} else {
			throw new IllegalArgumentException(getMessage(MessageCodes.INVALID_LOCALE, DefaultMessages.INVALID_LOCALE, locale));
		}
	}

	@Autowired(required = true)
	public void setAddressTypesBundleBasename(
			String addressTypesBundleBasename) {
		L10nUtil.addressTypesBundleBasename = addressTypesBundleBasename;
		getBundle(Locales.DEFAULT, addressTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setAuditTrailChangeCommentFieldLabelsBundleBasename(
			String auditTrailChangeCommentFieldLabelsBundleBasename) {
		L10nUtil.auditTrailChangeCommentFieldLabelsBundleBasename = auditTrailChangeCommentFieldLabelsBundleBasename;
		getBundle(Locales.DEFAULT, auditTrailChangeCommentFieldLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setAuditTrailDatabaseWriteLocale(
			String auditTrailDatabaseWriteLocale) {
		L10nUtil.auditTrailDatabaseWriteLocale = getLocaleFromString(auditTrailDatabaseWriteLocale);
	}

	@Autowired(required = true)
	public void setAuditTrailExcelLabelsBundleBasename(
			String auditTrailExcelLabelsBundleBasename) {
		L10nUtil.auditTrailExcelLabelsBundleBasename = auditTrailExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, auditTrailExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setAuthenticationExceptionMessagesBundleBasename(
			String authenticationExceptionMessagesBundleBasename) {
		L10nUtil.authenticationExceptionMessagesBundleBasename = authenticationExceptionMessagesBundleBasename;
		getBundle(Locales.DEFAULT, authenticationExceptionMessagesBundleBasename);
	}

	@Autowired(required = true)
	public void setAuthenticationTypeNamesBundleBasename(
			String authenticationTypeNamesBundleBasename) {
		L10nUtil.authenticationTypeNamesBundleBasename = authenticationTypeNamesBundleBasename;
		getBundle(Locales.DEFAULT, authenticationTypeNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setAuthorisationExceptionMessagesBundleBasename(
			String authorisationExceptionMessagesBundleBasename) {
		L10nUtil.authorisationExceptionMessagesBundleBasename = authorisationExceptionMessagesBundleBasename;
		getBundle(Locales.DEFAULT, authorisationExceptionMessagesBundleBasename);
	}

	@Autowired(required = true)
	public void setContactDetailTypesBundleBasename(
			String contactDetailTypesBundleBasename) {
		L10nUtil.contactDetailTypesBundleBasename = contactDetailTypesBundleBasename;
		getBundle(Locales.DEFAULT, contactDetailTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseCategoriesBundleBasename(
			String courseCategoriesBundleBasename) {
		L10nUtil.courseCategoriesBundleBasename = courseCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, courseCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseCertificatePdfLabelsBundleBasename(
			String courseCertificatePdfLabelsBundleBasename) {
		L10nUtil.courseCertificatePdfLabelsBundleBasename = courseCertificatePdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, courseCertificatePdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseCertificatePdfLocale(
			String courseCertificatePdfLocale) {
		L10nUtil.courseCertificatePdfLocale = getLocaleFromString(courseCertificatePdfLocale);
	}

	@Autowired(required = true)
	public void setCourseParticipantListPdfLabelsBundleBasename(
			String courseParticipantListPdfLabelsBundleBasename) {
		L10nUtil.courseParticipantListPdfLabelsBundleBasename = courseParticipantListPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, courseParticipantListPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setCourseParticipantListPdfLocale(
			String courseParticipantListPdfLocale) {
		L10nUtil.courseParticipantListPdfLocale = getLocaleFromString(courseParticipantListPdfLocale);
	}

	@Autowired(required = true)
	public void setCourseParticipationStatusTypesBundleBasename(
			String courseParticipationStatusTypesBundleBasename) {
		L10nUtil.courseParticipationStatusTypesBundleBasename = courseParticipationStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, courseParticipationStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setCriterionPropertiesBundleBasename(
			String criterionPropertiesBundleBasename) {
		L10nUtil.criterionPropertiesBundleBasename = criterionPropertiesBundleBasename;
		getBundle(Locales.DEFAULT, criterionPropertiesBundleBasename);
	}

	@Autowired(required = true)
	public void setCriterionRestrictionsBundleBasename(
			String criterionRestrictionsBundleBasename) {
		L10nUtil.criterionRestrictionsBundleBasename = criterionRestrictionsBundleBasename;
		getBundle(Locales.DEFAULT, criterionRestrictionsBundleBasename);
	}

	@Autowired(required = true)
	public void setCriterionTiesBundleBasename(
			String criterionTiesBundleBasename) {
		L10nUtil.criterionTiesBundleBasename = criterionTiesBundleBasename;
		getBundle(Locales.DEFAULT, criterionTiesBundleBasename);
	}

	@Autowired(required = true)
	public void setCvPdfLabelsBundleBasename(
			String cvPdfLabelsBundleBasename) {
		L10nUtil.cvPdfLabelsBundleBasename = cvPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, cvPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setCvPdfLocale(
			String cvPdfLocale) {
		L10nUtil.cvPdfLocale = getLocaleFromString(cvPdfLocale);
	}

	@Autowired(required = true)
	public void setCvSectionDescriptionBundleBasename(
			String cvSectionDescriptionBundleBasename) {
		L10nUtil.cvSectionDescriptionBundleBasename = cvSectionDescriptionBundleBasename;
		getBundle(Locales.DEFAULT, cvSectionDescriptionBundleBasename);
	}

	@Autowired(required = true)
	public void setCvSectionsBundleBasename(
			String cvSectionsBundleBasename) {
		L10nUtil.cvSectionsBundleBasename = cvSectionsBundleBasename;
		getBundle(Locales.DEFAULT, cvSectionsBundleBasename);
	}

	@Autowired(required = true)
	public void setCvSectionTitlePresetBundleBasename(
			String cvSectionTitlePresetBundleBasename) {
		L10nUtil.cvSectionTitlePresetBundleBasename = cvSectionTitlePresetBundleBasename;
		getBundle(Locales.DEFAULT, cvSectionTitlePresetBundleBasename);
	}

	@Autowired(required = true)
	public void setTrainingRecordSectionDescriptionBundleBasename(
			String trainingRecordSectionDescriptionBundleBasename) {
		L10nUtil.trainingRecordSectionDescriptionBundleBasename = trainingRecordSectionDescriptionBundleBasename;
		getBundle(Locales.DEFAULT, trainingRecordSectionDescriptionBundleBasename);
	}

	@Autowired(required = true)
	public void setTrainingRecordSectionsBundleBasename(
			String trainingRecordSectionsBundleBasename) {
		L10nUtil.trainingRecordSectionsBundleBasename = trainingRecordSectionsBundleBasename;
		getBundle(Locales.DEFAULT, trainingRecordSectionsBundleBasename);
	}

	@Autowired(required = true)
	public void setDbModuleNamesBundleBasename(
			String dbModuleNamesBundleBasename) {
		L10nUtil.dbModuleNamesBundleBasename = dbModuleNamesBundleBasename;
		getBundle(Locales.DEFAULT, dbModuleNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setDefaultLocale(
			String locale) {
		Locale.setDefault(getLocaleFromString(locale));
	}

	@Autowired(required = true)
	public void setDepartmentsBundleBasename(
			String departmentsBundleBasename) {
		L10nUtil.departmentsBundleBasename = departmentsBundleBasename;
		getBundle(Locales.DEFAULT, departmentsBundleBasename);
	}

	@Autowired(required = true)
	public void setEcrfFieldStatusTypesBundleBasename(
			String ecrfFieldStatusTypesBundleBasename) {
		L10nUtil.ecrfFieldStatusTypesBundleBasename = ecrfFieldStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, ecrfFieldStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setEcrfPdfLabelsBundleBasename(
			String ecrfPdfLabelsBundleBasename) {
		L10nUtil.ecrfPdfLabelsBundleBasename = ecrfPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, ecrfPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setEcrfPdfLocale(
			String ecrfPdfLocale) {
		L10nUtil.ecrfPdfLocale = getLocaleFromString(ecrfPdfLocale);
	}

	@Autowired(required = true)
	public void setEcrfStatusTypesBundleBasename(
			String ecrfStatusTypesBundleBasename) {
		L10nUtil.ecrfStatusTypesBundleBasename = ecrfStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, ecrfStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setEcrfValidationStatusNamesBundleBasename(
			String ecrfValidationStatusNamesBundleBasename) {
		L10nUtil.ecrfValidationStatusNamesBundleBasename = ecrfValidationStatusNamesBundleBasename;
		getBundle(Locales.DEFAULT, ecrfValidationStatusNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setEventImportanceNamesBundleBasename(
			String eventImportanceNamesBundleBasename) {
		L10nUtil.eventImportanceNamesBundleBasename = eventImportanceNamesBundleBasename;
		getBundle(Locales.DEFAULT, eventImportanceNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setJobStatusNamesBundleBasename(
			String jobStatusNamesBundleBasename) {
		L10nUtil.jobStatusNamesBundleBasename = jobStatusNamesBundleBasename;
		getBundle(Locales.DEFAULT, jobStatusNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setFileModuleNamesBundleBasename(
			String fileModuleNamesBundleBasename) {
		L10nUtil.fileModuleNamesBundleBasename = fileModuleNamesBundleBasename;
		getBundle(Locales.DEFAULT, fileModuleNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setJobModuleNamesBundleBasename(
			String jobModuleNamesBundleBasename) {
		L10nUtil.jobModuleNamesBundleBasename = jobModuleNamesBundleBasename;
		getBundle(Locales.DEFAULT, jobModuleNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setHolidayBundleBasename(
			String holidayBundleBasename) {
		L10nUtil.holidayBundleBasename = holidayBundleBasename;
		getBundle(Locales.DEFAULT, holidayBundleBasename);
	}

	@Autowired(required = true)
	public void setHyperlinkCategoriesBundleBasename(
			String hyperlinkCategoriesBundleBasename) {
		L10nUtil.hyperlinkCategoriesBundleBasename = hyperlinkCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, hyperlinkCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setJobTypeNamesBundleBasename(
			String jobTypeNamesBundleBasename) {
		L10nUtil.jobTypeNamesBundleBasename = jobTypeNamesBundleBasename;
		getBundle(Locales.DEFAULT, jobTypeNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setJobTypeDescriptionsBundleBasename(
			String jobTypeDescriptionsBundleBasename) {
		L10nUtil.jobTypeDescriptionsBundleBasename = jobTypeDescriptionsBundleBasename;
		getBundle(Locales.DEFAULT, jobTypeDescriptionsBundleBasename);
	}

	@Autowired(required = true)
	public void setHyperlinkModuleNamesBundleBasename(
			String hyperlinkModuleNamesBundleBasename) {
		L10nUtil.hyperlinkModuleNamesBundleBasename = hyperlinkModuleNamesBundleBasename;
		getBundle(Locales.DEFAULT, hyperlinkModuleNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setHyperlinkTitlePresetsBundleBasename(
			String hyperlinkTitlePresetsBundleBasename) {
		L10nUtil.hyperlinkTitlePresetsBundleBasename = hyperlinkTitlePresetsBundleBasename;
		getBundle(Locales.DEFAULT, hyperlinkTitlePresetsBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldCommentsBundleBasename(
			String inputFieldCommentsBundleBasename) {
		L10nUtil.inputFieldCommentsBundleBasename = inputFieldCommentsBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldCommentsBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldNamesBundleBasename(
			String inputFieldNamesBundleBasename) {
		L10nUtil.inputFieldNamesBundleBasename = inputFieldNamesBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldSelectionSetValuesBundleBasename(
			String inputFieldSelectionSetValuesBundleBasename) {
		L10nUtil.inputFieldSelectionSetValuesBundleBasename = inputFieldSelectionSetValuesBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldSelectionSetValuesBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldTextPresetsBundleBasename(
			String inputFieldTextPresetsBundleBasename) {
		L10nUtil.inputFieldTextPresetsBundleBasename = inputFieldTextPresetsBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldTextPresetsBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldTitlesBundleBasename(
			String inputFieldTitlesBundleBasename) {
		L10nUtil.inputFieldTitlesBundleBasename = inputFieldTitlesBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldTitlesBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldTypeNamesBundleBasename(
			String inputFieldTypeNamesBundleBasename) {
		L10nUtil.inputFieldTypeNamesBundleBasename = inputFieldTypeNamesBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldTypeNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setInputFieldValidationErrorMsgsBundleBasename(
			String inputFieldValidationErrorMsgsBundleBasename) {
		L10nUtil.inputFieldValidationErrorMsgsBundleBasename = inputFieldValidationErrorMsgsBundleBasename;
		getBundle(Locales.DEFAULT, inputFieldValidationErrorMsgsBundleBasename);
	}

	@Autowired(required = true)
	public void setInquiriesPdfLabelsBundleBasename(
			String inquiriesPdfLabelsBundleBasename) {
		L10nUtil.inquiriesPdfLabelsBundleBasename = inquiriesPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, inquiriesPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setInquiriesPdfLocale(
			String inquiriesPdfLocale) {
		L10nUtil.inquiriesPdfLocale = getLocaleFromString(inquiriesPdfLocale);
	}

	@Autowired(required = true)
	public void setInventoryBookingsExcelLabelsBundleBasename(
			String inventoryBookingsExcelLabelsBundleBasename) {
		L10nUtil.inventoryBookingsExcelLabelsBundleBasename = inventoryBookingsExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, inventoryBookingsExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setInventoryCategoriesBundleBasename(
			String inventoryCategoriesBundleBasename) {
		L10nUtil.inventoryCategoriesBundleBasename = inventoryCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, inventoryCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setInventoryStatusTypesBundleBasename(
			String inventoryStatusTypesBundleBasename) {
		L10nUtil.inventoryStatusTypesBundleBasename = inventoryStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, inventoryStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setInventoryTagsBundleBasename(
			String inventoryTagsBundleBasename) {
		L10nUtil.inventoryTagsBundleBasename = inventoryTagsBundleBasename;
		getBundle(Locales.DEFAULT, inventoryTagsBundleBasename);
	}

	@Autowired(required = true)
	public void setJournalCategoriesBundleBasename(
			String journalCategoriesBundleBasename) {
		L10nUtil.journalCategoriesBundleBasename = journalCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, journalCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setJournalDatabaseWriteLocale(
			String journalDatabaseWriteLocale) {
		L10nUtil.journalDatabaseWriteLocale = getLocaleFromString(journalDatabaseWriteLocale);
	}

	@Autowired(required = true)
	public void setJournalExcelLabelsBundleBasename(
			String journalExcelLabelsBundleBasename) {
		L10nUtil.journalExcelLabelsBundleBasename = journalExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, journalExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setJournalModuleNamesBundleBasename(
			String journalModuleNamesBundleBasename) {
		L10nUtil.journalModuleNamesBundleBasename = journalModuleNamesBundleBasename;
		getBundle(Locales.DEFAULT, journalModuleNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setJournalTitlePresetsBundleBasename(
			String journalTitlePresetsBundleBasename) {
		L10nUtil.journalTitlePresetsBundleBasename = journalTitlePresetsBundleBasename;
		getBundle(Locales.DEFAULT, journalTitlePresetsBundleBasename);
	}

	@Autowired(required = true)
	public void setLecturerCompetencesBundleBasename(
			String lecturerCompetencesBundleBasename) {
		L10nUtil.lecturerCompetencesBundleBasename = lecturerCompetencesBundleBasename;
		getBundle(Locales.DEFAULT, lecturerCompetencesBundleBasename);
	}

	@Autowired(required = true)
	public void setMaintenanceTitlePresetsBundleBasename(
			String maintenanceTitlePresetsBundleBasename) {
		L10nUtil.maintenanceTitlePresetsBundleBasename = maintenanceTitlePresetsBundleBasename;
		getBundle(Locales.DEFAULT, maintenanceTitlePresetsBundleBasename);
	}

	@Autowired(required = true)
	public void setMaintenanceTypesBundleBasename(
			String maintenanceTypesBundleBasename) {
		L10nUtil.maintenanceTypesBundleBasename = maintenanceTypesBundleBasename;
		getBundle(Locales.DEFAULT, maintenanceTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setMassMailStatusTypesBundleBasename(
			String massMailStatusTypesBundleBasename) {
		L10nUtil.massMailStatusTypesBundleBasename = massMailStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, massMailStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setMassMailTypesBundleBasename(
			String massMailTypesBundleBasename) {
		L10nUtil.massMailTypesBundleBasename = massMailTypesBundleBasename;
		getBundle(Locales.DEFAULT, massMailTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setMessagesBundleBasename(
			String messagesBundleBasename) {
		L10nUtil.messagesBundleBasename = messagesBundleBasename;
		getBundle(Locales.DEFAULT, messagesBundleBasename);
	}

	@Autowired(required = true)
	public void setNotificationMessageTemplatesBundleBasename(
			String notificationMessageTemplatesBundleBasename) {
		L10nUtil.notificationMessageTemplatesBundleBasename = notificationMessageTemplatesBundleBasename;
		getBundle(Locales.DEFAULT, notificationMessageTemplatesBundleBasename);
	}

	@Autowired(required = true)
	public void setNotificationsDatabaseWriteLocale(
			String notificationsDatabaseWriteLocale) {
		L10nUtil.notificationsDatabaseWriteLocale = getLocaleFromString(notificationsDatabaseWriteLocale);
	}

	@Autowired(required = true)
	public void setNotificationSubjectsBundleBasename(
			String notificationSubjectsBundleBasename) {
		L10nUtil.notificationSubjectsBundleBasename = notificationSubjectsBundleBasename;
		getBundle(Locales.DEFAULT, notificationSubjectsBundleBasename);
	}

	@Autowired(required = true)
	public void setNotificationTypeNamesBundleBasename(
			String notificationTypeNamesBundleBasename) {
		L10nUtil.notificationTypeNamesBundleBasename = notificationTypeNamesBundleBasename;
		getBundle(Locales.DEFAULT, notificationTypeNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setPaymentMethodNamesBundleBasename(
			String paymentMethodNamesBundleBasename) {
		L10nUtil.paymentMethodNamesBundleBasename = paymentMethodNamesBundleBasename;
		getBundle(Locales.DEFAULT, paymentMethodNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setPermissionProfileGroupNamesBundleBasename(
			String permissionProfileGroupNamesBundleBasename) {
		L10nUtil.permissionProfileGroupNamesBundleBasename = permissionProfileGroupNamesBundleBasename;
		getBundle(Locales.DEFAULT, permissionProfileGroupNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setPermissionProfileNamesBundleBasename(
			String permissionProfileNamesBundleBasename) {
		L10nUtil.permissionProfileNamesBundleBasename = permissionProfileNamesBundleBasename;
		getBundle(Locales.DEFAULT, permissionProfileNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setPrivacyConsentStatusTypeBundleBasename(
			String privacyConsentStatusTypeBundleBasename) {
		L10nUtil.privacyConsentStatusTypeBundleBasename = privacyConsentStatusTypeBundleBasename;
		getBundle(Locales.DEFAULT, privacyConsentStatusTypeBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandCategoriesBundleBasename(
			String probandCategoriesBundleBasename) {
		L10nUtil.probandCategoriesBundleBasename = probandCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, probandCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandLetterPdfLabelsBundleBasename(
			String probandLetterPdfLabelsBundleBasename) {
		L10nUtil.probandLetterPdfLabelsBundleBasename = probandLetterPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, probandLetterPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandLetterPdfLocale(
			String probandLetterPdfLocale) {
		L10nUtil.probandLetterPdfLocale = getLocaleFromString(probandLetterPdfLocale);
	}

	@Autowired(required = true)
	public void setProbandListEntryTagsPdfLabelsBundleBasename(
			String probandListEntryTagsPdfLabelsBundleBasename) {
		L10nUtil.probandListEntryTagsPdfLabelsBundleBasename = probandListEntryTagsPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, probandListEntryTagsPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandListEntryTagsPdfLocale(
			String probandListEntryTagsPdfLocale) {
		L10nUtil.probandListEntryTagsPdfLocale = getLocaleFromString(probandListEntryTagsPdfLocale);
	}

	@Autowired(required = true)
	public void setProbandListExcelLabelsBundleBasename(
			String probandListExcelLabelsBundleBasename) {
		L10nUtil.probandListExcelLabelsBundleBasename = probandListExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, probandListExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandListStatusReasonsBundleBasename(
			String probandListStatusReasonsBundleBasename) {
		L10nUtil.probandListStatusReasonsBundleBasename = probandListStatusReasonsBundleBasename;
		getBundle(Locales.DEFAULT, probandListStatusReasonsBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandListStatusReasonsDatabaseWriteLocale(
			String probandListStatusReasonsDatabaseWriteLocale) {
		L10nUtil.probandListStatusReasonsDatabaseWriteLocale = getLocaleFromString(probandListStatusReasonsDatabaseWriteLocale);
	}

	@Autowired(required = true)
	public void setProbandListStatusTypesBundleBasename(
			String probandListStatusTypesBundleBasename) {
		L10nUtil.probandListStatusTypesBundleBasename = probandListStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, probandListStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandStatusTypesBundleBasename(
			String probandStatusTypesBundleBasename) {
		L10nUtil.probandStatusTypesBundleBasename = probandStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, probandStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setProbandTagsBundleBasename(
			String probandTagsBundleBasename) {
		L10nUtil.probandTagsBundleBasename = probandTagsBundleBasename;
		getBundle(Locales.DEFAULT, probandTagsBundleBasename);
	}

	@Autowired(required = true)
	public void setRandomizationModeNamesBundleBasename(
			String randomizationModeNamesBundleBasename) {
		L10nUtil.randomizationModeNamesBundleBasename = randomizationModeNamesBundleBasename;
		getBundle(Locales.DEFAULT, randomizationModeNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setReimbursementsExcelLabelsBundleBasename(
			String reimbursementsExcelLabelsBundleBasename) {
		L10nUtil.reimbursementsExcelLabelsBundleBasename = reimbursementsExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, reimbursementsExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setReimbursementsPdfLabelsBundleBasename(
			String reimbursementsPdfLabelsBundleBasename) {
		L10nUtil.reimbursementsPdfLabelsBundleBasename = reimbursementsPdfLabelsBundleBasename;
		getBundle(Locales.DEFAULT, reimbursementsPdfLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setReimbursementsPdfLocale(
			String reimbursementsPdfLocale) {
		L10nUtil.reimbursementsPdfLocale = getLocaleFromString(reimbursementsPdfLocale);
	}

	@Autowired(required = true)
	public void setSearchResultExcelLabelsBundleBasename(
			String searchResultExcelLabelsBundleBasename) {
		L10nUtil.searchResultExcelLabelsBundleBasename = searchResultExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, searchResultExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setServiceExceptionMessagesBundleBasename(
			String serviceExceptionMessagesBundleBasename) {
		L10nUtil.serviceExceptionMessagesBundleBasename = serviceExceptionMessagesBundleBasename;
		getBundle(Locales.DEFAULT, serviceExceptionMessagesBundleBasename);
	}

	@Autowired(required = true)
	public void setSexNamesBundleBasename(
			String sexNamesBundleBasename) {
		L10nUtil.sexNamesBundleBasename = sexNamesBundleBasename;
		getBundle(Locales.DEFAULT, sexNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setSponsoringTypesBundleBasename(
			String sponsoringTypesBundleBasename) {
		L10nUtil.sponsoringTypesBundleBasename = sponsoringTypesBundleBasename;
		getBundle(Locales.DEFAULT, sponsoringTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setStaffCategoriesBundleBasename(
			String staffCategoriesBundleBasename) {
		L10nUtil.staffCategoriesBundleBasename = staffCategoriesBundleBasename;
		getBundle(Locales.DEFAULT, staffCategoriesBundleBasename);
	}

	@Autowired(required = true)
	public void setStaffStatusTypesBundleBasename(
			String staffStatusTypesBundleBasename) {
		L10nUtil.staffStatusTypesBundleBasename = staffStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, staffStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setStaffTagsBundleBasename(
			String staffTagsBundleBasename) {
		L10nUtil.staffTagsBundleBasename = staffTagsBundleBasename;
		getBundle(Locales.DEFAULT, staffTagsBundleBasename);
	}

	@Autowired(required = true)
	public void setSurveyStatusTypesBundleBasename(
			String surveyStatusTypesBundleBasename) {
		L10nUtil.surveyStatusTypesBundleBasename = surveyStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, surveyStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setSystemMessageCommentFieldLabelsBundleBasename(
			String systemMessageCommentFieldLabelsBundleBasename) {
		L10nUtil.systemMessageCommentFieldLabelsBundleBasename = systemMessageCommentFieldLabelsBundleBasename;
		getBundle(Locales.DEFAULT, systemMessageCommentFieldLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setSystemMessageCommentsBundleBasename(
			String systemMessageCommentsBundleBasename) {
		L10nUtil.systemMessageCommentsBundleBasename = systemMessageCommentsBundleBasename;
		getBundle(Locales.DEFAULT, systemMessageCommentsBundleBasename);
	}

	@Autowired(required = true)
	public void setSystemMessageTitlesBundleBasename(
			String systemMessageTitlesBundleBasename) {
		L10nUtil.systemMessageTitlesBundleBasename = systemMessageTitlesBundleBasename;
		getBundle(Locales.DEFAULT, systemMessageTitlesBundleBasename);
	}

	@Autowired(required = true)
	public void setTeamMemberRolesBundleBasename(
			String teamMemberRolesBundleBasename) {
		L10nUtil.teamMemberRolesBundleBasename = teamMemberRolesBundleBasename;
		getBundle(Locales.DEFAULT, teamMemberRolesBundleBasename);
	}

	@Autowired(required = true)
	public void setTeamMembersExcelLabelsBundleBasename(
			String teamMembersExcelLabelsBundleBasename) {
		L10nUtil.teamMembersExcelLabelsBundleBasename = teamMembersExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, teamMembersExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setTimelineEventTitlePresetsBundleBasename(
			String timelineEventTitlePresetsBundleBasename) {
		L10nUtil.timelineEventTitlePresetsBundleBasename = timelineEventTitlePresetsBundleBasename;
		getBundle(Locales.DEFAULT, timelineEventTitlePresetsBundleBasename);
	}

	@Autowired(required = true)
	public void setTimelineEventTypesBundleBasename(
			String timelineEventTypesBundleBasename) {
		L10nUtil.timelineEventTypesBundleBasename = timelineEventTypesBundleBasename;
		getBundle(Locales.DEFAULT, timelineEventTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setTimeZone(
			String timeZoneID) {
		if (CoreUtil.checkTimeZone(timeZoneID)) {
			TimeZone.setDefault(CommonUtil.timeZoneFromString(timeZoneID));
		} else {
			throw new IllegalArgumentException(getMessage(MessageCodes.INVALID_TIME_ZONE, DefaultMessages.INVALID_TIME_ZONE, timeZoneID));
		}
	}

	@Autowired(required = true)
	public void setTrialStatusTypesBundleBasename(
			String trialStatusTypesBundleBasename) {
		L10nUtil.trialStatusTypesBundleBasename = trialStatusTypesBundleBasename;
		getBundle(Locales.DEFAULT, trialStatusTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setTrialTagsBundleBasename(
			String trialTagsBundleBasename) {
		L10nUtil.trialTagsBundleBasename = trialTagsBundleBasename;
		getBundle(Locales.DEFAULT, trialTagsBundleBasename);
	}

	@Autowired(required = true)
	public void setTrialTypesBundleBasename(
			String trialTypesBundleBasename) {
		L10nUtil.trialTypesBundleBasename = trialTypesBundleBasename;
		getBundle(Locales.DEFAULT, trialTypesBundleBasename);
	}

	@Autowired(required = true)
	public void setVariablePeriodNamesBundleBasename(
			String variablePeriodNamesBundleBasename) {
		L10nUtil.variablePeriodNamesBundleBasename = variablePeriodNamesBundleBasename;
		getBundle(Locales.DEFAULT, variablePeriodNamesBundleBasename);
	}

	@Autowired(required = true)
	public void setVisitScheduleExcelLabelsBundleBasename(
			String visitScheduleExcelLabelsBundleBasename) {
		L10nUtil.visitScheduleExcelLabelsBundleBasename = visitScheduleExcelLabelsBundleBasename;
		getBundle(Locales.DEFAULT, visitScheduleExcelLabelsBundleBasename);
	}

	@Autowired(required = true)
	public void setVisitTypesBundleBasename(
			String visitTypesBundleBasename) {
		L10nUtil.visitTypesBundleBasename = visitTypesBundleBasename;
		getBundle(Locales.DEFAULT, visitTypesBundleBasename);
	}
}
