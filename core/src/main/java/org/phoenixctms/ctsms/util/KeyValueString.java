package org.phoenixctms.ctsms.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.phoenixctms.ctsms.compare.ComparatorFactory;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.CriterionRestriction;
import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFValidationStatus;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.AuthenticationTypeVO;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.CourseCategoryVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionRestrictionVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFValidationStatusVO;
import org.phoenixctms.ctsms.vo.EventImportanceVO;
import org.phoenixctms.ctsms.vo.ExportStatusVO;
import org.phoenixctms.ctsms.vo.HolidayVO;
import org.phoenixctms.ctsms.vo.HyperlinkCategoryVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InventoryCategoryVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusTypeVO;
import org.phoenixctms.ctsms.vo.InventoryTagVO;
import org.phoenixctms.ctsms.vo.JournalCategoryVO;
import org.phoenixctms.ctsms.vo.JournalModuleVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.MaintenanceTypeVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailStatusTypeVO;
import org.phoenixctms.ctsms.vo.MassMailTypeVO;
import org.phoenixctms.ctsms.vo.NotificationTypeVO;
import org.phoenixctms.ctsms.vo.PaymentMethodVO;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.PrivacyConsentStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandCategoryVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandTagVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.SponsoringTypeVO;
import org.phoenixctms.ctsms.vo.StaffCategoryVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.SurveyStatusTypeVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TimelineEventTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialTagVO;
import org.phoenixctms.ctsms.vo.TrialTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.VisitTypeVO;

public class KeyValueString extends GraphEnumerator {

	private final static Comparator<Object> VO_COLLECTION_VALUES_COMPARATOR = ComparatorFactory.createReflectionId();

	public static ArrayList<KeyValueString> getKeyValuePairs(Class vo, int depth,
			boolean omitFields,
			HashMap<Class, HashSet<String>> excludedFieldMap,
			boolean mashalEntities,
			boolean enumerateReferences,
			boolean enumerateCollections,
			boolean enumerateMaps,
			String associationPathSeparator, boolean lowerCaseFieldNames) throws Exception {
		ArrayList<KeyValueString> keyValuePairs = new ArrayList<KeyValueString>();
		ArrayList<Accessor> getterChain = new ArrayList<Accessor>();
		appendProperties(org.phoenixctms.ctsms.util.KeyValueString.class, keyValuePairs, vo, getterChain, mashalEntities, depth,
				MethodTransfilter.getVoMethodTransfilter(lowerCaseFieldNames),
				VO_COLLECTION_VALUES_COMPARATOR,
				omitFields, excludedFieldMap, enumerateReferences, enumerateCollections, enumerateMaps, associationPathSeparator);
		return keyValuePairs;
	}

	public int getIndentation() {
		return getterChain.size();
	}

	public ArrayList<ArrayList<Object>> getIndexesKeys(Object vo) throws Exception {
		return getIndexKeyChains(vo);
	}

	public String getKey(ArrayList<Object> indexesKeys) throws Exception {
		return getAssociationPath(indexesKeys);
	}

	public String getValue(Locales locale, Object vo, ArrayList<Object> indexesKeys, String datetimePattern, String datePattern, String timePattern, boolean enumerateEntities,
			boolean omitFields)
			throws Exception {
		return outVOValueToString(locale, returnType, getValueOf(vo, indexesKeys), datetimePattern, datePattern, timePattern, enumerateEntities, omitFields);
	}

	protected String getVOFieldValue(Object vo, String field, boolean omitFields) {
		if (omitFields && OmittedFields.isOmitted(vo.getClass(), field, false)) { // no case-insensitivity since used in outVOValueToString only
			Long id = CommonUtil.getVOId(vo);
			if (id != null) {
				return id.toString();
			}
		} else {
			try {
				//xreturn (String) AssociationPath.invoke("get" + CoreUtil.capitalizeFirstChar(field, true), vo, false);
				return (String) AssociationPath.invoke(transfilter.reverseTransform(field), vo, false);
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	protected boolean isFieldOmitted(Class graph, String field) {
		return OmittedFields.isOmitted(graph, field, true); // case-insensitivity since fields can be transformed to lower case
	}

	@Override
	protected boolean isTerminalType(Object passThrough) {
		return outVOValueToString(null, returnType, null, null, null, null, (Boolean) passThrough, false) != null;
	}

	private String outVOValueToString(Locales locale, Class valueClass, Object value, String datetimePattern, String datePattern, String timePattern, boolean enumerateEntities,
			boolean omitFields) {
		if (valueClass != null) {
			if (valueClass.equals(String.class)) {
				if (value == null) {
					return "";
				}
				return (String) value;
			} else if (valueClass.equals(Long.class)) {
				if (value == null) {
					return "";
				}
				return ((Long) value).toString();
			} else if (valueClass.equals(java.lang.Long.TYPE)) {
				if (value == null) {
					return "";
				}
				return ((Long) value).toString();
			} else if (valueClass.equals(Integer.class)) {
				if (value == null) {
					return "";
				}
				return ((Integer) value).toString();
			} else if (valueClass.equals(java.lang.Integer.TYPE)) {
				if (value == null) {
					return "";
				}
				return ((Integer) value).toString();
			} else if (valueClass.equals(Boolean.class)) {
				if (value == null) {
					return "";
				}
				return ((Boolean) value).toString();
			} else if (valueClass.equals(java.lang.Boolean.TYPE)) {
				if (value == null) {
					return "";
				}
				return ((Boolean) value).toString();
			} else if (valueClass.equals(Float.class)) {
				if (value == null) {
					return "";
				}
				return ((Float) value).toString();
			} else if (valueClass.equals(java.lang.Float.TYPE)) {
				if (value == null) {
					return "";
				}
				return ((Float) value).toString();
			} else if (valueClass.equals(Double.class)) {
				if (value == null) {
					return "";
				}
				return ((Double) value).toString();
			} else if (valueClass.equals(java.lang.Double.TYPE)) {
				if (value == null) {
					return "";
				}
				return ((Double) value).toString();
			} else if (valueClass.equals(Date.class)) {
				if (value == null) {
					return "";
				}
				if (DateCalc.isTime((Date) value)) {
					return CommonUtil.formatDate((Date) value, timePattern, L10nUtil.getLocale(locale));
					// return (new SimpleDateFormat(timePattern, L10nUtil.getLocale(locale))).format(value);
				} else if (DateCalc.isDatetime((Date) value)) {
					return CommonUtil.formatDate((Date) value, datetimePattern, L10nUtil.getLocale(locale));
					// return (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(locale))).format(value);
				} else {
					return CommonUtil.formatDate((Date) value, datePattern, L10nUtil.getLocale(locale));
					// return (new SimpleDateFormat(datePattern, L10nUtil.getLocale(locale))).format(value);
				}
			} else if (valueClass.equals(Timestamp.class)) { // should not be used by any out vo
				if (value == null) {
					return "";
				}
				return CommonUtil.formatDate((Date) value, datetimePattern, L10nUtil.getLocale(locale));
				// return (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(locale))).format(value);
			} else if (valueClass.equals(Object.class)) {
				if (value == null) {
					return "";
				}
				return value.toString();
			} else if (!enumerateEntities && InventoryOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && StaffOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && CourseOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && TrialOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && ProbandOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && MassMailOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && UserOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && CriteriaOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "label", omitFields);
			} else if (!enumerateEntities && ProbandGroupOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "uniqueName", omitFields);
			} else if (!enumerateEntities && VisitOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "title", omitFields);
			} else if (!enumerateEntities && VisitScheduleItemOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "name", omitFields);
			} else if (!enumerateEntities && ProbandListStatusEntryOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getProbandListStatusTypeName(locale, ((ProbandListStatusEntryOutVO) value).getStatus().getNameL10nKey());
			} else if (!enumerateEntities && InquiryOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "uniqueName", omitFields);
			} else if (!enumerateEntities && ProbandListEntryTagOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "uniqueName", omitFields);
			} else if (!enumerateEntities && ECRFOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "uniqueName", omitFields);
			} else if (!enumerateEntities && ECRFFieldOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return getVOFieldValue(value, "uniqueName", omitFields);
			} else if (valueClass.equals(CriterionTie.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCriterionTieName(locale, ((CriterionTie) value).name());
			} else if (valueClass.equals(CriterionRestriction.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCriterionRestrictionName(locale, ((CriterionRestriction) value).name());
			} else if (valueClass.equals(DBModule.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getDBModuleName(locale, ((DBModule) value).name());
			} else if (valueClass.equals(JournalModule.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getJournalModuleName(locale, ((JournalModule) value).name());
			} else if (valueClass.equals(PermissionProfile.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPermissionProfileName(locale, ((PermissionProfile) value).name());
			} else if (valueClass.equals(PermissionProfileGroup.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPermissionProfileGroupName(locale, ((PermissionProfileGroup) value).name());
			} else if (valueClass.equals(EventImportance.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEventImportanceName(locale, ((EventImportance) value).name());
			} else if (valueClass.equals(ExportStatus.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getExportStatusName(locale, ((ExportStatus) value).name());
			} else if (valueClass.equals(ECRFValidationStatus.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEcrfValidationStatusName(locale, ((ECRFValidationStatus) value).name());
			} else if (valueClass.equals(PaymentMethod.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPaymentMethodName(locale, ((PaymentMethod) value).name());
			} else if (valueClass.equals(InputFieldType.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getInputFieldTypeName(locale, ((InputFieldType) value).name());
			} else if (valueClass.equals(VariablePeriod.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getVariablePeriodName(locale, ((VariablePeriod) value).name());
			} else if (valueClass.equals(AuthenticationType.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getAuthenticationTypeName(locale, ((AuthenticationType) value).name());
			} else if (valueClass.equals(Sex.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getSexName(locale, ((Sex) value).name());
			} else if (valueClass.equals(RandomizationMode.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getRandomizationModeName(locale, ((RandomizationMode) value).name());
			} else if (valueClass.equals(AuthenticationType.class)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getAuthenticationTypeName(locale, ((AuthenticationType) value).name());
			} else if (AddressTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getAddressTypeName(locale, ((AddressTypeVO) value).getNameL10nKey());
			} else if (ContactDetailTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getContactDetailTypeName(locale, ((ContactDetailTypeVO) value).getNameL10nKey());
			} else if (CourseCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCourseCategoryName(locale, ((CourseCategoryVO) value).getNameL10nKey());
			} else if (CourseParticipationStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCourseParticipationStatusTypeName(locale, ((CourseParticipationStatusTypeVO) value).getNameL10nKey());
			} else if (CriterionPropertyVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCriterionPropertyName(locale, ((CriterionPropertyVO) value).getNameL10nKey());
			} else if (CriterionRestrictionVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCriterionRestrictionName(locale, ((CriterionRestrictionVO) value).getNameL10nKey());
			} else if (CriterionTieVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCriterionTieName(locale, ((CriterionTieVO) value).getNameL10nKey());
			} else if (CvSectionVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getCvSectionName(locale, ((CvSectionVO) value).getNameL10nKey());
			} else if (DepartmentVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getDepartmentName(locale, ((DepartmentVO) value).getNameL10nKey());
			} else if (EventImportanceVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEventImportanceName(locale, ((EventImportanceVO) value).getNameL10nKey());
			} else if (ExportStatusVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getExportStatusName(locale, ((ExportStatusVO) value).getNameL10nKey());
			} else if (ECRFValidationStatusVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEcrfValidationStatusName(locale, ((ECRFValidationStatusVO) value).getNameL10nKey());
			} else if (HolidayVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				Collection<String> nameL10nKeys = ((HolidayVO) value).getNameL10nKeys();
				if (nameL10nKeys.size() > 0) {
					return L10nUtil.getHolidayName(locale, nameL10nKeys.iterator().next());
				} else {
					return "";
				}
			} else if (HyperlinkCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getHyperlinkCategoryName(locale, ((HyperlinkCategoryVO) value).getNameL10nKey());
			} else if (!enumerateEntities && InputFieldOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				InputFieldOutVO inputField = ((InputFieldOutVO) value);
				if (inputField.isLocalized()) {
					return L10nUtil.getInputFieldName(locale, inputField.getNameL10nKey());
				} else {
					return inputField.getName();
				}
			} else if (InputFieldSelectionSetValueOutVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				InputFieldSelectionSetValueOutVO inputFieldSelectionSetValue = ((InputFieldSelectionSetValueOutVO) value);
				if (inputFieldSelectionSetValue.isLocalized()) {
					return L10nUtil.getInputFieldSelectionSetValueName(locale, inputFieldSelectionSetValue.getNameL10nKey());
				} else {
					return inputFieldSelectionSetValue.getName();
				}
			} else if (InputFieldTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getInputFieldTypeName(locale, ((InputFieldTypeVO) value).getNameL10nKey());
			} else if (MassMailStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getMassMailStatusTypeName(locale, ((MassMailStatusTypeVO) value).getNameL10nKey());
			} else if (MassMailTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getMassMailTypeName(locale, ((MassMailTypeVO) value).getNameL10nKey());
			} else if (InventoryCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getInventoryCategoryName(locale, ((InventoryCategoryVO) value).getNameL10nKey());
			} else if (InventoryStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getInventoryStatusTypeName(locale, ((InventoryStatusTypeVO) value).getNameL10nKey());
			} else if (InventoryTagVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getInventoryTagName(locale, ((InventoryTagVO) value).getNameL10nKey());
			} else if (JournalCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getJournalCategoryName(locale, ((JournalCategoryVO) value).getNameL10nKey());
			} else if (JournalModuleVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getJournalModuleName(locale, ((JournalModuleVO) value).getNameL10nKey());
			} else if (PermissionProfileVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPermissionProfileName(locale, ((PermissionProfileVO) value).getProfile().name());
			} else if (LecturerCompetenceVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getLecturerCompetenceName(locale, ((LecturerCompetenceVO) value).getNameL10nKey());
			} else if (MaintenanceTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getMaintenanceTypeName(locale, ((MaintenanceTypeVO) value).getNameL10nKey());
			} else if (NotificationTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getNotificationTypeName(locale, ((NotificationTypeVO) value).getNameL10nKey());
			} else if (PaymentMethodVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPaymentMethodName(locale, ((PaymentMethodVO) value).getNameL10nKey());
			} else if (ProbandCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getProbandCategoryName(locale, ((ProbandCategoryVO) value).getNameL10nKey());
			} else if (PrivacyConsentStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getPrivacyConsentStatusTypeName(locale, ((PrivacyConsentStatusTypeVO) value).getNameL10nKey());
			} else if (ProbandListStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getProbandListStatusTypeName(locale, ((ProbandListStatusTypeVO) value).getNameL10nKey());
			} else if (ProbandStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getProbandStatusTypeName(locale, ((ProbandStatusTypeVO) value).getNameL10nKey());
			} else if (ProbandTagVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getProbandTagName(locale, ((ProbandTagVO) value).getNameL10nKey());
			} else if (SexVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getSexName(locale, ((SexVO) value).getNameL10nKey());
			} else if (RandomizationModeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getRandomizationModeName(locale, ((RandomizationModeVO) value).getNameL10nKey());
			} else if (StaffCategoryVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getStaffCategoryName(locale, ((StaffCategoryVO) value).getNameL10nKey());
			} else if (StaffStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getStaffStatusTypeName(locale, ((StaffStatusTypeVO) value).getNameL10nKey());
			} else if (StaffTagVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getStaffTagName(locale, ((StaffTagVO) value).getNameL10nKey());
			} else if (TeamMemberRoleVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getTeamMemberRoleName(locale, ((TeamMemberRoleVO) value).getNameL10nKey());
			} else if (TimelineEventTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getTimelineEventTypeName(locale, ((TimelineEventTypeVO) value).getNameL10nKey());
			} else if (TrialStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getTrialStatusTypeName(locale, ((TrialStatusTypeVO) value).getNameL10nKey());
			} else if (TrialTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getTrialTypeName(locale, ((TrialTypeVO) value).getNameL10nKey());
			} else if (SponsoringTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getSponsoringTypeName(locale, ((SponsoringTypeVO) value).getNameL10nKey());
			} else if (SurveyStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getSurveyStatusTypeName(locale, ((SurveyStatusTypeVO) value).getNameL10nKey());
			} else if (TrialTagVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getTrialTagName(locale, ((TrialTagVO) value).getNameL10nKey());
			} else if (ECRFStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEcrfStatusTypeName(locale, ((ECRFStatusTypeVO) value).getNameL10nKey());
			} else if (ECRFFieldStatusTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getEcrfFieldStatusTypeName(locale, ((ECRFFieldStatusTypeVO) value).getNameL10nKey());
			} else if (VariablePeriodVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getVariablePeriodName(locale, ((VariablePeriodVO) value).getNameL10nKey());
			} else if (AuthenticationTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getAuthenticationTypeName(locale, ((AuthenticationTypeVO) value).getNameL10nKey());
			} else if (VisitTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getVisitTypeName(locale, ((VisitTypeVO) value).getNameL10nKey());
			} else if (VisitTypeVO.class.isAssignableFrom(valueClass)) {
				if (value == null) {
					return "";
				}
				return L10nUtil.getVisitTypeName(locale, ((VisitTypeVO) value).getNameL10nKey());
			}
		}
		return null;
	}
}
