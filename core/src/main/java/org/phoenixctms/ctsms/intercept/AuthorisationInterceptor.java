package org.phoenixctms.ctsms.intercept;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.compare.EntityPositionComparator;
import org.phoenixctms.ctsms.compare.VOPositionComparator;
import org.phoenixctms.ctsms.domain.BankAccountDao;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntryDao;
import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.Criterion;
import org.phoenixctms.ctsms.domain.CriterionDao;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.CvPositionDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.DiagnosisDao;
import org.phoenixctms.ctsms.domain.DutyRosterTurnDao;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.HyperlinkDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusEntryDao;
import org.phoenixctms.ctsms.domain.InventoryTagValueDao;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Lecturer;
import org.phoenixctms.ctsms.domain.LecturerDao;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao;
import org.phoenixctms.ctsms.domain.MassMail;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.MassMailRecipientDao;
import org.phoenixctms.ctsms.domain.MedicationDao;
import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.phoenixctms.ctsms.domain.Permission;
import org.phoenixctms.ctsms.domain.PermissionDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.phoenixctms.ctsms.domain.ProcedureDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffAddressDao;
import org.phoenixctms.ctsms.domain.StaffContactDetailValueDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffStatusEntryDao;
import org.phoenixctms.ctsms.domain.StaffTagValueDao;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberDao;
import org.phoenixctms.ctsms.domain.TimelineEventDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialTagValueDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.domain.VisitScheduleItemDao;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterOverride;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterRestriction;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.AuthorisationExceptionCodes;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.springframework.aop.MethodBeforeAdvice;

public class AuthorisationInterceptor implements MethodBeforeAdvice {

	private static final String PARAMETER_GETTER_SETTER_SEPARATOR = ",";

	private static final Pattern PARAMETER_GETTER_SETTER_SEPARATOR_REGEXP = Pattern.compile(" *" + Pattern.quote(PARAMETER_GETTER_SETTER_SEPARATOR) + " *");
	private static final Pattern DEFAULT_DISJUNCTION_GROUP_SEPARATOR_REGEXP = Pattern.compile(" *: *");
	private static Object getArgument(String parameterName, Map<String, Integer> argumentIndexMap, Object[] args) {
		Integer index = argumentIndexMap.get(parameterName);
		if (index != null) {
			return args[index];
		}
		return null;
	}
	private static boolean getParameterValues(String parameterGetter, ArrayList<Object> parameterValues, Map<String, Integer> argIndexMap, Object[] args) throws Exception {
		String[] parameterGetters = PARAMETER_GETTER_SETTER_SEPARATOR_REGEXP.split(parameterGetter, -1);
		for (int i = 0; i < parameterGetters.length; i++) {
			AssociationPath getter = new AssociationPath(parameterGetters[i]);
			if (getter.isValid() && argIndexMap.containsKey(getter.getRootEntityName())) {
				Object rootParameterValue = getArgument(getter.getRootEntityName(), argIndexMap, args);
				Object parameterValue = null;
				if (getter.getPathDepth() > 0) {
					getter.dropFirstPathElement();
					parameterValue = getter.invoke(rootParameterValue, false);
				} else {
					parameterValue = rootParameterValue;
				}
				parameterValues.add(parameterValue);
			} else {
				return false;
			}
		}
		return true;
	}
	private static boolean isChild(Staff staff, Staff identity) {
		if (staff == null) {
			return false;
		}
		if (identity.equals(staff)) {
			return true;
		}
		Iterator<Staff> childrenIt = identity.getChildren().iterator();
		while (childrenIt.hasNext()) {
			if (isChild(staff, childrenIt.next())) {
				return true;
			}
		}
		return false;
	}
	private static boolean isLecturer(Course course, Staff identity) {
		if (course == null) {
			return false;
		}
		Iterator<Lecturer> lecturersIt = course.getLecturers().iterator();
		while (lecturersIt.hasNext()) {
			if (identity.equals(lecturersIt.next().getStaff())) {
				return true;
			}
		}
		return false;
	}
	private static boolean isMember(Trial trial, Staff identity, Boolean access) {
		if (trial == null) {
			return false;
		}
		Iterator<TeamMember> membersIt = trial.getMembers().iterator();
		while (membersIt.hasNext()) {
			TeamMember member = membersIt.next();
			if ((access == null || access == member.isAccess()) && identity.equals(member.getStaff())) {
				return true;
			}
		}
		return false;
	}
	private static void setArgument(String parameterName, Map<String, Integer> argumentIndexMap, Object[] args, Object value) {
		Integer index = argumentIndexMap.get(parameterName);
		if (index != null) {
			args[index] = value;
		}
	}
	private static String[] splitParameterSetter(String parameterSetter) {
		String[] result = new String[2];
		String[] headTail = PARAMETER_GETTER_SETTER_SEPARATOR_REGEXP.split(parameterSetter, -1);
		StringBuilder head = new StringBuilder();
		for (int i = 0; i < headTail.length - 1; i++) {
			if (head.length() > 0) {
				head.append(PARAMETER_GETTER_SETTER_SEPARATOR);
			}
			head.append(headTail[i]);
		}
		result[0] = head.toString();
		result[1] = headTail[headTail.length - 1];
		return result;
	}
	private PermissionDao permissionDao;
	private InventoryTagValueDao inventoryTagValueDao;
	private InventoryStatusEntryDao inventoryStatusEntryDao;
	private InventoryBookingDao inventoryBookingDao;
	private MaintenanceScheduleItemDao maintenanceItemDao;
	private StaffDao staffDao;
	private DepartmentDao departmentDao;
	private InventoryDao inventoryDao;
	private CourseDao courseDao;
	private TrialDao trialDao;
	private ProbandDao probandDao;
	private MassMailDao massMailDao;
	private MassMailRecipientDao massMailRecipientDao;
	private StaffTagValueDao staffTagValueDao;
	private StaffStatusEntryDao staffStatusEntryDao;
	private CriteriaDao criteriaDao;
	private CriterionDao criterionDao;
	private CriterionPropertyDao criterionPropertyDao;
	private CriterionTieDao criterionTieDao;
	private StaffContactDetailValueDao staffContactDetailValueDao;
	private StaffAddressDao staffAddressDao;
	private CourseParticipationStatusEntryDao courseParticipationStatusEntryDao;
	private DutyRosterTurnDao dutyRosterTurnDao;
	private LecturerDao lecturerDao;
	private TrialTagValueDao trialTagValueDao;
	private TeamMemberDao teamMemberDao;
	private TimelineEventDao timelineEventDao;
	private ProbandGroupDao probandGroupDao;
	private VisitDao visitDao;
	private ECRFDao ecrfDao;
	private ECRFFieldDao ecrfFieldDao;
	private VisitScheduleItemDao visitScheduleItemDao;
	private ProbandListEntryTagDao probandListEntryTagDao;
	private InquiryDao inquiryDao;
	private ProbandListEntryDao probandListEntryDao;
	private ProbandListStatusEntryDao probandListStatusEntryDao;
	private ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao;
	private CvPositionDao cvPositionDao;
	private JournalEntryDao journalEntryDao;
	private UserDao userDao;
	private HyperlinkDao hyperlinkDao;
	private FileDao fileDao;
	private ProbandTagValueDao probandTagValueDao;
	private ProbandStatusEntryDao probandStatusEntryDao;
	private ProbandContactDetailValueDao probandContactDetailValueDao;
	private ProbandAddressDao probandAddressDao;
	private BankAccountDao bankAccountDao;
	private MoneyTransferDao moneyTransferDao;
	private UserPermissionProfileDao userPermissionProfileDao;



	private DiagnosisDao diagnosisDao;

	private MedicationDao medicationDao;

	private ProcedureDao procedureDao;

	private InquiryValueDao inquiryValueDao;

	private ProbandListEntryTagValueDao probandListEntryTagValueDao;

	private ECRFFieldValueDao ecrfFieldValueDao;

	public AuthorisationInterceptor() {
	}

	@Override
	public void before(Method method, Object[] args, Object object) throws Throwable {
		if (Settings.getBoolean(SettingCodes.ENABLE_AUTHORISATION, Bundle.SETTINGS, DefaultSettings.ENABLE_AUTHORISATION)) {
			User user = CoreUtil.getUser();
			if (user == null) {
				throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NOT_AUTHENTICATED);
			}
			Collection<Permission> permissions = permissionDao.findByServiceMethodUser(CoreUtil.getServiceMethodName(method), user.getId(), true, true);
			if (permissions.size() == 0) {
				throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_PERMISSIONS);
			}
			boolean checkIpRanges = Settings.getBoolean(SettingCodes.ENABLE_HOST_BASED_AUTHORISATION, Bundle.SETTINGS, DefaultSettings.ENABLE_HOST_BASED_AUTHORISATION);
			Map<String, Integer> argIndexMap = AssociationPath.getArgumentIndexMap(method);
			Iterator<Permission> permissionsIt = permissions.iterator();
			HashMap<String, Boolean> disjunctPermissionErrorMap = new HashMap<String, Boolean>(); // true;
			while (permissionsIt.hasNext()) {
				Permission permission = permissionsIt.next();
				if (permission.getParameterGetter() != null) {
					ServiceMethodParameterRestriction restriction = permission.getRestriction();
					if (restriction != null) {
						ArrayList<Object> parameterValues = new ArrayList<Object>();
						if (getParameterValues(permission.getParameterGetter(), parameterValues, argIndexMap, args)) {
							Object parameterValue;
							if (parameterValues.size() > 1) {
								parameterValue = parameterValues.toArray();
							} else {
								parameterValue = parameterValues.get(0);
							}
							String disjunctionGroup = permission.getDisjunctionGroup();
							boolean disjunct = disjunctionGroup != null;
							if (disjunct) {
								if (!disjunctPermissionErrorMap.containsKey(disjunctionGroup)) {
									disjunctPermissionErrorMap.put(disjunctionGroup, true);
								} else if (!disjunctPermissionErrorMap.get(disjunctionGroup)) {
									continue;
								}
							}
							try {
								if (checkIpRanges) {
									CoreUtil.checkHostIp(permission.getIpRanges(), permission.getServiceMethod());
								}
								if (parameterValue == null) {
									checkParameterRestriction(null, permission, restriction, user);
								} else if (parameterValue instanceof Collection) {
									Iterator parameterValuesIt = ((Collection) parameterValue).iterator();
									while (parameterValuesIt.hasNext()) {
										checkParameterRestriction(parameterValuesIt.next(), permission, restriction, user);
									}
								} else if (parameterValue instanceof Map) {
									Iterator parameterValuesIt = ((Map) parameterValue).values().iterator();
									while (parameterValuesIt.hasNext()) {
										checkParameterRestriction(parameterValuesIt.next(), permission, restriction, user);
									}
								} else {
									checkParameterRestriction(parameterValue, permission, restriction, user);
								}
								if (disjunct && disjunctPermissionErrorMap.get(disjunctionGroup)) {
									disjunctPermissionErrorMap.put(disjunctionGroup, false);
								}
							} catch (Exception e) {
								if (!disjunct) {
									throw e;
								}
							}
						}
					}
				} else if (permission.getParameterSetter() != null) {
					if (checkIpRanges) {
						CoreUtil.checkHostIp(permission.getIpRanges(), permission.getServiceMethod());
					}
					ServiceMethodParameterOverride override = permission.getOverride();
					if (override != null) {
						ArrayList<Object> setterRestrictionValues = new ArrayList<Object>();
						String[] setterHeadTail = splitParameterSetter(permission.getParameterSetter());
						getParameterValues(setterHeadTail[0], setterRestrictionValues, argIndexMap, args);
						AssociationPath setter = new AssociationPath(setterHeadTail[1]);
						if (setter.isValid() && argIndexMap.containsKey(setter.getRootEntityName())) {
							Object blankRootParameterValue = null;
							Object[] parameterValues = null;
							boolean write = false;
							switch (override) {
								case ANY_DEPARTMENT_ID:
									write = false;
									break;
								case USER_DEPARTMENT_ID:
									write = true;
									parameterValues = new Object[1];
									parameterValues[0] = user.getDepartment().getId();
									break;
								case ANY_DEPARTMENT_ID_FILTER:
									write = false;
									break;
								case USER_DEPARTMENT_ID_FILTER:
									write = true;
									blankRootParameterValue = new PSFVO();
									parameterValues = new Object[2];
									parameterValues[0] = "department.id";
									parameterValues[1] = Long.toString(user.getDepartment().getId());
									break;
								case INVENTORY_HYPERLINK_ACTIVE:
									if (HyperlinkModule.INVENTORY_HYPERLINK.equals(((HyperlinkModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case STAFF_HYPERLINK_ACTIVE:
									if (HyperlinkModule.STAFF_HYPERLINK.equals(((HyperlinkModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case COURSE_HYPERLINK_ACTIVE:
									if (HyperlinkModule.COURSE_HYPERLINK.equals(((HyperlinkModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case TRIAL_HYPERLINK_ACTIVE:
									if (HyperlinkModule.TRIAL_HYPERLINK.equals(((HyperlinkModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case INVENTORY_DOCUMENT_ACTIVE:
									if (FileModule.INVENTORY_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case STAFF_DOCUMENT_ACTIVE:
									if (FileModule.STAFF_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case COURSE_DOCUMENT_ACTIVE:
									if (FileModule.COURSE_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case TRIAL_DOCUMENT_ACTIVE:
									if (FileModule.TRIAL_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								case PROBAND_DOCUMENT_ACTIVE:
									if (FileModule.PROBAND_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
									// case INPUT_FIELD_DOCUMENT_ACTIVE:
									// if (((FileModule) setterRestrictionValues.get(0)) == FileModule.INPUT_FIELD_DOCUMENT) {
									// write = true;
									// parameterValues = new Object[1];
									// parameterValues[0] = true;
									// } else {
									// write = false;
									// }
									// break;
								case MASS_MAIL_DOCUMENT_ACTIVE:
									if (FileModule.MASS_MAIL_DOCUMENT.equals(((FileModule) setterRestrictionValues.get(0)))) {
										write = true;
										parameterValues = new Object[1];
										parameterValues[0] = true;
									} else {
										write = false;
									}
									break;
								default:
									throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_SERVICE_METHOD_PARAMETER_OVERRIDE,
											DefaultMessages.UNSUPPORTED_SERVICE_METHOD_PARAMETER_OVERRIDE, new Object[] { override.name() }));
							}
							if (write) {
								if (setter.getPathDepth() > 0) {
									Object rootParameterValue = getArgument(setter.getRootEntityName(), argIndexMap, args);
									if (rootParameterValue == null) {
										rootParameterValue = blankRootParameterValue;
										setArgument(setter.getRootEntityName(), argIndexMap, args, rootParameterValue);
									}
									setter.dropFirstPathElement();
									setter.invoke(rootParameterValue, false, parameterValues);
								} else {
									setArgument(setter.getRootEntityName(), argIndexMap, args, parameterValues[0]);
								}
							}
						}
					}
				}
			}
			Iterator<String> disjunctionGroupsIt = disjunctPermissionErrorMap.keySet().iterator();
			while (disjunctionGroupsIt.hasNext()) {
				String disjunctionGroup = disjunctionGroupsIt.next();
				if (disjunctPermissionErrorMap.get(disjunctionGroup)) { // && disjunctPermissionCount > 0) {
					String[] serviceMethodNameParameter = DEFAULT_DISJUNCTION_GROUP_SEPARATOR_REGEXP.split(disjunctionGroup, -1);
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_DISJUNCTIVE_RESTRICTION_NOT_SATISFIED, (Object[]) serviceMethodNameParameter);
				}
			}
		}
	}

	private void checkCriteriaForModifications(Long criteriaId, CriteriaInstantVO criteria, boolean logError) throws Exception {
		if (criteriaId == null) {
			// not allowed to perform unsaved queries
			throw ServiceUtil.initAuthorisationExceptionWithPosition(AuthorisationExceptionCodes.CRITERIA_NOT_SAVED, logError, null);
		}
		Criteria storedCriteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
		Collection<Criterion> storedCriterons = storedCriteria.getCriterions();
		Collection<CriterionInstantVO> criterons = criteria.getCriterions();
		if (criterons.size() != storedCriterons.size()) {
			// number of criterions doesn't match
			throw ServiceUtil.initAuthorisationExceptionWithPosition(AuthorisationExceptionCodes.CRITERIA_MODIFIED_DIFFERENT_NUMBER_OF_CRITERIONS, logError, null);
		}
		ArrayList<CriterionInstantVO> sortedCriterions = new ArrayList<CriterionInstantVO>(criterons);
		Collections.sort(sortedCriterions, new VOPositionComparator(false));
		ArrayList<Criterion> sortedOriginalCriterions = new ArrayList<Criterion>(storedCriterons);
		Collections.sort(sortedOriginalCriterions, new EntityPositionComparator(false));
		for (int i = 0; i < sortedCriterions.size(); i++) {
			CriterionInstantVO criterion = sortedCriterions.get(i);
			if (criterion == null) {
				throw ServiceUtil.initAuthorisationExceptionWithPosition(ServiceExceptionCodes.CRITERION_IS_NULL, logError, null);
			}
			Criterion storedCriterion = sortedOriginalCriterions.get(i);
			CriterionTie tie = criterionTieDao.findByTie(storedCriterion.getTie());
			if (!((criterion.getTieId() == null && tie == null) || (tie != null && tie.getId().equals(criterion.getTieId())))) {
				// tie mismatch
				throw ServiceUtil.initAuthorisationExceptionWithPosition(AuthorisationExceptionCodes.CRITERIA_MODIFIED_DIFFERENT_TIE, logError, criterion);
			}
			CriterionProperty property = criterionPropertyDao.findByNameL10nKey(storedCriterion.getPropertyNameL10nKey());
			if (!((criterion.getPropertyId() == null && property == null) || (property != null && property.getId().equals(criterion.getPropertyId())))) {
				throw ServiceUtil.initAuthorisationExceptionWithPosition(AuthorisationExceptionCodes.CRITERIA_MODIFIED_DIFFERENT_PROPERTY, logError, criterion);
			}
		}
	}

	private void checkParameterRestriction(Object parameterValue, Permission permission, ServiceMethodParameterRestriction restriction, User user) throws Exception {
		parameterValue = transformParameterValue(parameterValue, permission);
		Department department;
		Staff identity;
		Staff staff;
		Course course;
		Trial trial;
		File file;
		DBModule dbModule;
		CriteriaInVO criteria;
		Set<CriterionInVO> criterions;
		User parameterUser;
		switch (restriction) {
			case NOT_NULL:
				if (parameterValue == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), null);
				}
				break;
			case NULL:
				if (parameterValue != null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), parameterValue);
				}
				break;
			case ANY_DEPARTMENT:
				break;
			case USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkDepartmentId((Long) parameterValue, departmentDao);
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case ANY_STAFF:
				break;
			case STAFF_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkStaffId((Long) parameterValue, staffDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case IDENTITY:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				staff = parameterValue == null ? null : CheckIDUtil.checkStaffId((Long) parameterValue, staffDao);
				if (!identity.equals(staff)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), staff == null ? null : CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(staff)));
				}
				break;
			case IDENTITY_CHILD:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				staff = parameterValue == null ? null : CheckIDUtil.checkStaffId((Long) parameterValue, staffDao);
				if (!isChild(staff, identity)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), staff == null ? null : CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(staff)));
				}
				break;
			case ANY_INVENTORY:
				break;
			case INVENTORY_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkInventoryId((Long) parameterValue, inventoryDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case INVENTORY_IDENTITY_OWNER:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				staff = parameterValue == null ? null : CheckIDUtil.checkInventoryId((Long) parameterValue, inventoryDao).getOwner();
				if (!identity.equals(staff)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), staff == null ? null : CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(staff)));
				}
				break;
			case ANY_COURSE:
				break;
			case COURSE_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkCourseId((Long) parameterValue, courseDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case COURSE_IDENTITY_INSTITUTION:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				staff = parameterValue == null ? null : CheckIDUtil.checkCourseId((Long) parameterValue, courseDao).getInstitution();
				if (!identity.equals(staff)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), staff == null ? null : CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(staff)));
				}
				break;
			case COURSE_IDENTITY_LECTURER:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				course = parameterValue == null ? null : CheckIDUtil.checkCourseId((Long) parameterValue, courseDao);
				if (!isLecturer(course, identity)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), course == null ? null : CommonUtil.courseOutVOToString(courseDao.toCourseOutVO(course)));
				}
				break;
			case ANY_TRIAL:
				break;
			case TRIAL_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkTrialId((Long) parameterValue, trialDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case TRIAL_IDENTITY_TEAM_MEMBER:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				trial = parameterValue == null ? null : CheckIDUtil.checkTrialId((Long) parameterValue, trialDao);
				if (!isMember(trial, identity, null)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), trial == null ? null : trialDao.toTrialOutVO(trial).getName());
				}
				break;
			case TRIAL_IDENTITY_TEAM_MEMBER_ACCESS:
				identity = user.getIdentity();
				if (identity == null) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.NO_IDENTITY);
				}
				trial = parameterValue == null ? null : CheckIDUtil.checkTrialId((Long) parameterValue, trialDao);
				if (!isMember(trial, identity, true)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), trial == null ? null : trialDao.toTrialOutVO(trial).getName());
				}
				break;
			case ANY_PROBAND:
				break;
			case PROBAND_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkProbandId((Long) parameterValue, probandDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case ANY_USER:
				break;
			case USER_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkUserId((Long) parameterValue, userDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case ACTIVE_USER:
				parameterUser = parameterValue == null ? null : CheckIDUtil.checkUserId((Long) parameterValue, userDao);
				if (!user.equals(parameterUser)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), parameterUser == null ? null : parameterUser.getName());
				}
				break;
			case ANY_MASS_MAIL:
				break;
			case MASS_MAIL_USER_DEPARTMENT:
				department = parameterValue == null ? null : CheckIDUtil.checkMassMailId((Long) parameterValue, massMailDao).getDepartment();
				if (!user.getDepartment().equals(department)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), department == null ? null : departmentDao.toDepartmentVO(department).getName());
				}
				break;
			case INVENTORY_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.INVENTORY_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case STAFF_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.STAFF_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case COURSE_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.COURSE_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case TRIAL_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.TRIAL_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case PROBAND_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.PROBAND_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case USER_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.USER_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case INPUT_FIELD_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.INPUT_FIELD_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case MASS_MAIL_DB_MODULE:
				dbModule = (DBModule) parameterValue;
				if (!DBModule.MASS_MAIL_DB.equals(dbModule)) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), dbModule == null ? null : L10nUtil.getDBModuleName(Locales.USER, dbModule.name()));
				}
				break;
			case INSTANT_AND_SAVED_CRITERIA:
				break;
			case SAVED_CRITERIA:
				criteria = parameterValue == null ? null : (CriteriaInVO) ((Object[]) parameterValue)[0];
				criterions = parameterValue == null ? null : (Set<CriterionInVO>) ((Object[]) parameterValue)[1];
				checkCriteriaForModifications(criteria == null ? null : criteria.getId(), ServiceUtil.toInstant(criterions, criterionDao), true);
				break;
			case SAVED_CRITERIA_ID:
				break;
			case PUBLIC_FILE:
				file = parameterValue == null ? null : CheckIDUtil.checkFileId((Long) parameterValue, fileDao);
				if (!file.isPublicFile()) {
					throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
							permission.getParameterGetter(), file == null ? null : Long.toString(file.getId()));
				}
				break;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_SERVICE_METHOD_PARAMETER_RESTRICTION,
						DefaultMessages.UNSUPPORTED_SERVICE_METHOD_PARAMETER_RESTRICTION, new Object[] { restriction.toString() }));
		}
	}

	public void setBankAccountDao(BankAccountDao bankAccountDao) {
		this.bankAccountDao = bankAccountDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void setCourseParticipationStatusEntryDao(
			CourseParticipationStatusEntryDao courseParticipationStatusEntryDao) {
		this.courseParticipationStatusEntryDao = courseParticipationStatusEntryDao;
	}

	public void setCriteriaDao(CriteriaDao criteriaDao) {
		this.criteriaDao = criteriaDao;
	}

	public void setCriterionDao(CriterionDao criterionDao) {
		this.criterionDao = criterionDao;
	}

	public void setCriterionPropertyDao(CriterionPropertyDao criterionPropertyDao) {
		this.criterionPropertyDao = criterionPropertyDao;
	}

	public void setCriterionTieDao(CriterionTieDao criterionTieDao) {
		this.criterionTieDao = criterionTieDao;
	}

	public void setCvPositionDao(CvPositionDao cvPositionDao) {
		this.cvPositionDao = cvPositionDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public void setDiagnosisDao(DiagnosisDao diagnosisDao) {
		this.diagnosisDao = diagnosisDao;
	}

	public void setDutyRosterTurnDao(DutyRosterTurnDao dutyRosterTurnDao) {
		this.dutyRosterTurnDao = dutyRosterTurnDao;
	}

	public void setEcrfDao(ECRFDao ecrfDao) {
		this.ecrfDao = ecrfDao;
	}

	public void setEcrfFieldDao(ECRFFieldDao ecrfFieldDao) {
		this.ecrfFieldDao = ecrfFieldDao;
	}

	public void setEcrfFieldStatusEntryDao(
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao) {
		this.ecrfFieldStatusEntryDao = ecrfFieldStatusEntryDao;
	}

	public void setEcrfFieldValueDao(ECRFFieldValueDao ecrfFieldValueDao) {
		this.ecrfFieldValueDao = ecrfFieldValueDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setHyperlinkDao(HyperlinkDao hyperlinkDao) {
		this.hyperlinkDao = hyperlinkDao;
	}

	public void setInquiryDao(InquiryDao inquiryDao) {
		this.inquiryDao = inquiryDao;
	}

	public void setInquiryValueDao(InquiryValueDao inquiryValueDao) {
		this.inquiryValueDao = inquiryValueDao;
	}

	public void setInventoryBookingDao(InventoryBookingDao inventoryBookingDao) {
		this.inventoryBookingDao = inventoryBookingDao;
	}

	public void setInventoryDao(InventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}

	public void setInventoryStatusEntryDao(
			InventoryStatusEntryDao inventoryStatusEntryDao) {
		this.inventoryStatusEntryDao = inventoryStatusEntryDao;
	}

	public void setInventoryTagValueDao(InventoryTagValueDao inventoryTagValueDao) {
		this.inventoryTagValueDao = inventoryTagValueDao;
	}

	public void setJournalEntryDao(JournalEntryDao journalEntryDao) {
		this.journalEntryDao = journalEntryDao;
	}

	public void setLecturerDao(LecturerDao lecturerDao) {
		this.lecturerDao = lecturerDao;
	}

	public void setMaintenanceItemDao(MaintenanceScheduleItemDao maintenanceItemDao) {
		this.maintenanceItemDao = maintenanceItemDao;
	}

	public void setMassMailDao(MassMailDao massMailDao) {
		this.massMailDao = massMailDao;
	}

	public void setMassMailRecipientDao(MassMailRecipientDao massMailRecipientDao) {
		this.massMailRecipientDao = massMailRecipientDao;
	}

	public void setMedicationDao(MedicationDao medicationDao) {
		this.medicationDao = medicationDao;
	}

	public void setMoneyTransferDao(MoneyTransferDao moneyTransferDao) {
		this.moneyTransferDao = moneyTransferDao;
	}

	public void setPermissionDao(PermissionDao permissionDao) {
		this.permissionDao = permissionDao;
	}

	public void setProbandAddressDao(ProbandAddressDao probandAddressDao) {
		this.probandAddressDao = probandAddressDao;
	}

	public void setProbandContactDetailValueDao(
			ProbandContactDetailValueDao probandContactDetailValueDao) {
		this.probandContactDetailValueDao = probandContactDetailValueDao;
	}

	public void setProbandDao(ProbandDao probandDao) {
		this.probandDao = probandDao;
	}

	public void setProbandGroupDao(ProbandGroupDao probandGroupDao) {
		this.probandGroupDao = probandGroupDao;
	}

	public void setProbandListEntryDao(ProbandListEntryDao probandListEntryDao) {
		this.probandListEntryDao = probandListEntryDao;
	}

	public void setProbandListEntryTagDao(
			ProbandListEntryTagDao probandListEntryTagDao) {
		this.probandListEntryTagDao = probandListEntryTagDao;
	}

	public void setProbandListEntryTagValueDao(ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		this.probandListEntryTagValueDao = probandListEntryTagValueDao;
	}

	public void setProbandListStatusEntryDao(
			ProbandListStatusEntryDao probandListStatusEntryDao) {
		this.probandListStatusEntryDao = probandListStatusEntryDao;
	}

	public void setProbandStatusEntryDao(ProbandStatusEntryDao probandStatusEntryDao) {
		this.probandStatusEntryDao = probandStatusEntryDao;
	}

	public void setProbandTagValueDao(ProbandTagValueDao probandTagValueDao) {
		this.probandTagValueDao = probandTagValueDao;
	}

	public void setProcedureDao(ProcedureDao procedureDao) {
		this.procedureDao = procedureDao;
	}

	public void setStaffAddressDao(StaffAddressDao staffAddressDao) {
		this.staffAddressDao = staffAddressDao;
	}

	public void setStaffContactDetailValueDao(
			StaffContactDetailValueDao staffContactDetailValueDao) {
		this.staffContactDetailValueDao = staffContactDetailValueDao;
	}

	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	public void setStaffStatusEntryDao(StaffStatusEntryDao staffStatusEntryDao) {
		this.staffStatusEntryDao = staffStatusEntryDao;
	}

	public void setStaffTagValueDao(StaffTagValueDao staffTagValueDao) {
		this.staffTagValueDao = staffTagValueDao;
	}

	public void setTeamMemberDao(TeamMemberDao teamMemberDao) {
		this.teamMemberDao = teamMemberDao;
	}

	public void setTimelineEventDao(TimelineEventDao timelineEventDao) {
		this.timelineEventDao = timelineEventDao;
	}

	public void setTrialDao(TrialDao trialDao) {
		this.trialDao = trialDao;
	}

	public void setTrialTagValueDao(TrialTagValueDao trialTagValueDao) {
		this.trialTagValueDao = trialTagValueDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserPermissionProfileDao(
			UserPermissionProfileDao userPermissionProfileDao) {
		this.userPermissionProfileDao = userPermissionProfileDao;
	}

	public void setVisitDao(VisitDao visitDao) {
		this.visitDao = visitDao;
	}

	public void setVisitScheduleItemDao(VisitScheduleItemDao visitScheduleItemDao) {
		this.visitScheduleItemDao = visitScheduleItemDao;
	}

	private Object transformParameterValue(Object parameterValue, Permission permission) throws Exception {
		if (permission.getTransformation() != null) {
			JournalModule journalModule;
			Long id;
			HyperlinkModule hyperlinkModule;
			FileModule fileModule;
			switch (permission.getTransformation()) {
				case INVENTORY_TAG_VALUE_ID_TO_INVENTORY_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInventoryTagValueId((Long) parameterValue, inventoryTagValueDao).getInventory().getId();
				case INVENTORY_STATUS_ENTRY_ID_TO_INVENTORY_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInventoryStatusEntryId((Long) parameterValue, inventoryStatusEntryDao).getInventory().getId();
				case INVENTORY_BOOKING_ID_TO_INVENTORY_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInventoryBookingId((Long) parameterValue, inventoryBookingDao).getInventory().getId();
				case INVENTORY_BOOKING_ID_TO_STAFF_ID:
					if (parameterValue != null) {
						Staff onBehalfOf = CheckIDUtil.checkInventoryBookingId((Long) parameterValue, inventoryBookingDao).getOnBehalfOf();
						if (onBehalfOf != null) {
							return onBehalfOf.getId();
						}
					}
					return null;
				case INVENTORY_BOOKING_ID_TO_COURSE_ID:
					if (parameterValue != null) {
						Course course = CheckIDUtil.checkInventoryBookingId((Long) parameterValue, inventoryBookingDao).getCourse();
						if (course != null) {
							return course.getId();
						}
					}
					return null;
				case INVENTORY_BOOKING_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkInventoryBookingId((Long) parameterValue, inventoryBookingDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;
				case INVENTORY_BOOKING_ID_TO_PROBAND_ID:
					if (parameterValue != null) {
						Proband proband = CheckIDUtil.checkInventoryBookingId((Long) parameterValue, inventoryBookingDao).getProband();
						if (proband != null) {
							return proband.getId();
						}
					}
					return null;
				case MAINTENANCE_SCHEDULE_ITEM_ID_TO_INVENTORY_ID:
					return parameterValue == null ? null : CheckIDUtil.checkMaintenanceScheduleItemId((Long) parameterValue, maintenanceItemDao).getInventory().getId();
				case STAFF_TAG_VALUE_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkStaffTagValueId((Long) parameterValue, staffTagValueDao).getStaff().getId();
				case STAFF_STATUS_ENTRY_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkStaffStatusEntryId((Long) parameterValue, staffStatusEntryDao).getStaff().getId();
				case STAFF_CONTACT_DETAIL_VALUE_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkStaffContactDetailValueId((Long) parameterValue, staffContactDetailValueDao).getStaff().getId();
				case STAFF_ADDRESS_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkStaffAddressId((Long) parameterValue, staffAddressDao).getStaff().getId();
				case CV_POSITION_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkCvPositionId((Long) parameterValue, cvPositionDao).getStaff().getId();
				case COURSE_PARTICIPATION_STATUS_ENTRY_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkCourseParticipationStatusEntryId((Long) parameterValue, courseParticipationStatusEntryDao).getStaff()
							.getId();
				case COURSE_PARTICIPATION_STATUS_ENTRY_ID_TO_COURSE_ID:
					return parameterValue == null ? null : CheckIDUtil.checkCourseParticipationStatusEntryId((Long) parameterValue, courseParticipationStatusEntryDao).getCourse()
							.getId();
				case DUTY_ROSTER_TURN_ID_TO_STAFF_ID:
					if (parameterValue != null) {
						Staff staff = CheckIDUtil.checkDutyRosterTurnId((Long) parameterValue, dutyRosterTurnDao).getStaff();
						if (staff != null) {
							return staff.getId();
						}
					}
					return null;
				case DUTY_ROSTER_TURN_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkDutyRosterTurnId((Long) parameterValue, dutyRosterTurnDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;
				case LECTURER_ID_TO_COURSE_ID:
					return parameterValue == null ? null : CheckIDUtil.checkLecturerId((Long) parameterValue, lecturerDao).getCourse().getId();
				case LECTURER_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkLecturerId((Long) parameterValue, lecturerDao).getStaff().getId();
				case TRIAL_TAG_VALUE_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkTrialTagValueId((Long) parameterValue, trialTagValueDao).getTrial().getId();
				case TRIAL_TEAM_MEMBER_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkTeamMemberId((Long) parameterValue, teamMemberDao).getTrial().getId();
				case TRIAL_TEAM_MEMBER_ID_TO_STAFF_ID:
					return parameterValue == null ? null : CheckIDUtil.checkTeamMemberId((Long) parameterValue, teamMemberDao).getStaff().getId();
				case TIMELINE_EVENT_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkTimelineEventId((Long) parameterValue, timelineEventDao).getTrial().getId();
				case PROBAND_GROUP_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandGroupId((Long) parameterValue, probandGroupDao).getTrial().getId();
				case VISIT_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkVisitId((Long) parameterValue, visitDao).getTrial().getId();
				case ECRF_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfId((Long) parameterValue, ecrfDao).getTrial().getId();
				case ECRF_FIELD_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfFieldId((Long) parameterValue, ecrfFieldDao).getTrial().getId();
				case ECRF_FIELD_VALUE_IN_TO_TRIAL_ID:
					if (parameterValue != null) {
						Long listEntryId = ((ECRFFieldValueInVO) parameterValue).getListEntryId();
						if (listEntryId != null) {
							return CheckIDUtil.checkProbandListEntryId(listEntryId, probandListEntryDao).getTrial().getId();
						}
					}
					return null;
				case ECRF_FIELD_VALUE_IN_TO_PROBAND_ID:
					if (parameterValue != null) {
						Long listEntryId = ((ECRFFieldValueInVO) parameterValue).getListEntryId();
						if (listEntryId != null) {
							return CheckIDUtil.checkProbandListEntryId(listEntryId, probandListEntryDao).getProband().getId();
						}
					}
					return null;
				case ECRF_FIELD_VALUE_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfFieldValueId((Long) parameterValue, ecrfFieldValueDao).getListEntry().getTrial().getId();
				case ECRF_FIELD_VALUE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfFieldValueId((Long) parameterValue, ecrfFieldValueDao).getListEntry().getProband().getId();
				case VISIT_SCHEDULE_ITEM_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkVisitScheduleItemId((Long) parameterValue, visitScheduleItemDao).getTrial().getId();
				case PROBAND_LIST_ENTRY_TAG_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListEntryTagId((Long) parameterValue, probandListEntryTagDao).getTrial().getId();
				case INQUIRY_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInquiryId((Long) parameterValue, inquiryDao).getTrial().getId();
				case PROBAND_LIST_ENTRY_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListEntryId((Long) parameterValue, probandListEntryDao).getTrial().getId();
				case PROBAND_LIST_ENTRY_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListEntryId((Long) parameterValue, probandListEntryDao).getProband().getId();

				case PROBAND_LIST_STATUS_ENTRY_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListStatusEntryId((Long) parameterValue, probandListStatusEntryDao).getListEntry().getTrial()
							.getId();
				case PROBAND_LIST_STATUS_ENTRY_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListStatusEntryId((Long) parameterValue, probandListStatusEntryDao).getListEntry().getProband()
							.getId();
				case ECRF_FIELD_STATUS_ENTRY_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfFieldStatusEntryId((Long) parameterValue, ecrfFieldStatusEntryDao).getListEntry().getTrial()
							.getId();
				case ECRF_FIELD_STATUS_ENTRY_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkEcrfFieldStatusEntryId((Long) parameterValue, ecrfFieldStatusEntryDao).getListEntry().getProband()
							.getId();
				case PROBAND_LIST_ENTRY_TAG_VALUE_IN_TO_TRIAL_ID:
					if (parameterValue != null) {
						Long listEntryId = ((ProbandListEntryTagValueInVO) parameterValue).getListEntryId();
						if (listEntryId != null) {
							return CheckIDUtil.checkProbandListEntryId(listEntryId, probandListEntryDao).getTrial().getId();
						}
					}
					return null;
				case PROBAND_LIST_ENTRY_TAG_VALUE_IN_TO_PROBAND_ID:
					if (parameterValue != null) {
						Long listEntryId = ((ProbandListEntryTagValueInVO) parameterValue).getListEntryId();
						if (listEntryId != null) {
							return CheckIDUtil.checkProbandListEntryId(listEntryId, probandListEntryDao).getProband().getId();
						}
					}
					return null;
				case PROBAND_LIST_ENTRY_TAG_VALUE_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListEntryTagValueId((Long) parameterValue, probandListEntryTagValueDao).getListEntry()
							.getTrial().getId();
				case PROBAND_LIST_ENTRY_TAG_VALUE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandListEntryTagValueId((Long) parameterValue, probandListEntryTagValueDao).getListEntry()
							.getProband().getId();
				case PROBAND_TAG_VALUE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandTagValueId((Long) parameterValue, probandTagValueDao).getProband().getId();
				case PROBAND_STATUS_ENTRY_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandStatusEntryId((Long) parameterValue, probandStatusEntryDao).getProband().getId();
				case DIAGNOSIS_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkDiagnosisId((Long) parameterValue, diagnosisDao).getProband().getId();
				case PROCEDURE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProcedureId((Long) parameterValue, procedureDao).getProband().getId();
				case MEDICATION_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkMedicationId((Long) parameterValue, medicationDao).getProband().getId();
				case PROBAND_CONTACT_DETAIL_VALUE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandContactDetailValueId((Long) parameterValue, probandContactDetailValueDao).getProband().getId();
				case PROBAND_ADDRESS_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkProbandAddressId((Long) parameterValue, probandAddressDao).getProband().getId();
				case BANK_ACCOUNT_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkBankAccountId((Long) parameterValue, bankAccountDao).getProband().getId();
				case MONEY_TRANSFER_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkMoneyTransferId((Long) parameterValue, moneyTransferDao).getProband().getId();
				case MONEY_TRANSFER_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkMoneyTransferId((Long) parameterValue, moneyTransferDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;
				case INQUIRY_VALUE_IN_TO_TRIAL_ID:
					if (parameterValue != null) {
						Long inquiryId = ((InquiryValueInVO) parameterValue).getInquiryId();
						if (inquiryId != null) {
							return CheckIDUtil.checkInquiryId(inquiryId, inquiryDao).getTrial().getId();
						}
					}
					return null;
				case INQUIRY_VALUE_ID_TO_TRIAL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInquiryValueId((Long) parameterValue, inquiryValueDao).getInquiry().getTrial().getId();
				case INQUIRY_VALUE_ID_TO_PROBAND_ID:
					return parameterValue == null ? null : CheckIDUtil.checkInquiryValueId((Long) parameterValue, inquiryValueDao).getProband().getId();
				case INQUIRY_VALUE_IN_TO_PROBAND_ID:
					return parameterValue == null ? null : ((InquiryValueInVO) parameterValue).getProbandId();
				case MASS_MAIL_RECIPIENT_ID_TO_MASS_MAIL_ID:
					return parameterValue == null ? null : CheckIDUtil.checkMassMailRecipientId((Long) parameterValue, massMailRecipientDao).getMassMail().getId();
				case MASS_MAIL_RECIPIENT_ID_TO_PROBAND_ID:
					if (parameterValue != null) {
						Proband proband = CheckIDUtil.checkMassMailRecipientId((Long) parameterValue, massMailRecipientDao).getProband();
						if (proband != null) {
							return proband.getId();
						}
					}
					return null;
				case USER_PERMISSION_PROFILE_ID_TO_USER_ID:
					return parameterValue == null ? null : CheckIDUtil.checkUserPermissionProfileId((Long) parameterValue, userPermissionProfileDao).getUser().getId();
				case JOURNAL_ENTRY_ID_TO_INVENTORY_ID:
					if (parameterValue != null) {
						Inventory inventory = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getInventory();
						if (inventory != null) {
							return inventory.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_STAFF_ID:
					if (parameterValue != null) {
						Staff staff = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getStaff();
						if (staff != null) {
							return staff.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_COURSE_ID:
					if (parameterValue != null) {
						Course course = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getCourse();
						if (course != null) {
							return course.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_PROBAND_ID:
					if (parameterValue != null) {
						Proband proband = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getProband();
						if (proband != null) {
							return proband.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_USER_ID:
					if (parameterValue != null) {
						User paramterUser = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getUser();
						if (paramterUser != null) {
							return paramterUser.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_INPUT_FIELD_ID:
					if (parameterValue != null) {
						InputField inputField = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getInputField();
						if (inputField != null) {
							return inputField.getId();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_CRITERIA_DB_MODULE:
					if (parameterValue != null) {
						Criteria criteria = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getCriteria();
						if (criteria != null) {
							return criteria.getModule();
						}
					}
					return null;
				case JOURNAL_ENTRY_ID_TO_MASS_MAIL_ID:
					if (parameterValue != null) {
						MassMail massMail = CheckIDUtil.checkJournalEntryId((Long) parameterValue, journalEntryDao).getMassMail();
						if (massMail != null) {
							return massMail.getId();
						}
					}
					return null;
				case JOURNAL_MODULE_AND_ID_TO_INVENTORY_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.INVENTORY_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_STAFF_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.STAFF_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_COURSE_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.COURSE_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_TRIAL_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.TRIAL_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_PROBAND_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.PROBAND_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_USER_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.USER_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_INPUT_FIELD_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.INPUT_FIELD_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_AND_ID_TO_CRITERIA_DB_MODULE:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.CRITERIA_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					id = (Long) ((Object[]) parameterValue)[1];
					if (id != null) {
						return CheckIDUtil.checkCriteriaId(id, criteriaDao).getModule();
					}
					return null;
				case JOURNAL_MODULE_AND_ID_TO_MASS_MAIL_ID:
					journalModule = (JournalModule) ((Object[]) parameterValue)[0];
					if (!JournalModule.MASS_MAIL_JOURNAL.equals(journalModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case JOURNAL_MODULE_TO_DB_MODULE:
					journalModule = (JournalModule) (parameterValue);
					switch (journalModule) {
						case INVENTORY_JOURNAL:
							return DBModule.INVENTORY_DB;
						case STAFF_JOURNAL:
							return DBModule.STAFF_DB;
						case COURSE_JOURNAL:
							return DBModule.COURSE_DB;
						case TRIAL_JOURNAL:
							return DBModule.TRIAL_DB;
						case PROBAND_JOURNAL:
							return DBModule.PROBAND_DB;
						case USER_JOURNAL:
							return DBModule.USER_DB;
						case INPUT_FIELD_JOURNAL:
							return DBModule.INPUT_FIELD_DB;
						case MASS_MAIL_JOURNAL:
							return DBModule.MASS_MAIL_DB;
						case CRITERIA_JOURNAL:
							throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
									permission.getParameterGetter(), journalModule == null ? null : L10nUtil.getJournalModuleName(Locales.USER, journalModule.name()));
					}
				case HYPERLINK_ID_TO_INVENTORY_ID:
					if (parameterValue != null) {
						Inventory inventory = CheckIDUtil.checkHyperlinkId((Long) parameterValue, hyperlinkDao).getInventory();
						if (inventory != null) {
							return inventory.getId();
						}
					}
					return null;
				case HYPERLINK_ID_TO_STAFF_ID:
					if (parameterValue != null) {
						Staff staff = CheckIDUtil.checkHyperlinkId((Long) parameterValue, hyperlinkDao).getStaff();
						if (staff != null) {
							return staff.getId();
						}
					}
					return null;
				case HYPERLINK_ID_TO_COURSE_ID:
					if (parameterValue != null) {
						Course course = CheckIDUtil.checkHyperlinkId((Long) parameterValue, hyperlinkDao).getCourse();
						if (course != null) {
							return course.getId();
						}
					}
					return null;
				case HYPERLINK_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkHyperlinkId((Long) parameterValue, hyperlinkDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;
				case HYPERLINK_MODULE_AND_ID_TO_INVENTORY_ID:
					hyperlinkModule = (HyperlinkModule) ((Object[]) parameterValue)[0];
					if (!HyperlinkModule.INVENTORY_HYPERLINK.equals(hyperlinkModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), hyperlinkModule == null ? null : L10nUtil.getHyperlinkModuleName(Locales.USER, hyperlinkModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case HYPERLINK_MODULE_AND_ID_TO_STAFF_ID:
					hyperlinkModule = (HyperlinkModule) ((Object[]) parameterValue)[0];
					if (!HyperlinkModule.STAFF_HYPERLINK.equals(hyperlinkModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), hyperlinkModule == null ? null : L10nUtil.getHyperlinkModuleName(Locales.USER, hyperlinkModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case HYPERLINK_MODULE_AND_ID_TO_COURSE_ID:
					hyperlinkModule = (HyperlinkModule) ((Object[]) parameterValue)[0];
					if (!HyperlinkModule.COURSE_HYPERLINK.equals(hyperlinkModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), hyperlinkModule == null ? null : L10nUtil.getHyperlinkModuleName(Locales.USER, hyperlinkModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case HYPERLINK_MODULE_AND_ID_TO_TRIAL_ID:
					hyperlinkModule = (HyperlinkModule) ((Object[]) parameterValue)[0];
					if (!HyperlinkModule.TRIAL_HYPERLINK.equals(hyperlinkModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), hyperlinkModule == null ? null : L10nUtil.getHyperlinkModuleName(Locales.USER, hyperlinkModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_ID_TO_INVENTORY_ID:
					if (parameterValue != null) {
						Inventory inventory = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getInventory();
						if (inventory != null) {
							return inventory.getId();
						}
					}
					return null;
				case FILE_ID_TO_STAFF_ID:
					if (parameterValue != null) {
						Staff staff = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getStaff();
						if (staff != null) {
							return staff.getId();
						}
					}
					return null;
				case FILE_ID_TO_COURSE_ID:
					if (parameterValue != null) {
						Course course = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getCourse();
						if (course != null) {
							return course.getId();
						}
					}
					return null;
				case FILE_ID_TO_TRIAL_ID:
					if (parameterValue != null) {
						Trial trial = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getTrial();
						if (trial != null) {
							return trial.getId();
						}
					}
					return null;

				case FILE_ID_TO_PROBAND_ID:
					if (parameterValue != null) {
						Proband proband = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getProband();
						if (proband != null) {
							return proband.getId();
						}
					}
					return null;
				case FILE_ID_TO_MASS_MAIL_ID:
					if (parameterValue != null) {
						MassMail massMail = CheckIDUtil.checkFileId((Long) parameterValue, fileDao).getMassMail();
						if (massMail != null) {
							return massMail.getId();
						}
					}
					return null;
				case FILE_MODULE_AND_ID_TO_INVENTORY_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.INVENTORY_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_MODULE_AND_ID_TO_STAFF_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.STAFF_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_MODULE_AND_ID_TO_COURSE_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.COURSE_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_MODULE_AND_ID_TO_TRIAL_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.TRIAL_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_MODULE_AND_ID_TO_PROBAND_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.PROBAND_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case FILE_MODULE_AND_ID_TO_MASS_MAIL_ID:
					fileModule = (FileModule) ((Object[]) parameterValue)[0];
					if (!FileModule.MASS_MAIL_DOCUMENT.equals(fileModule)) {
						throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.PARAMETER_RESTRICTION_VIOLATED, permission.getServiceMethod(),
								permission.getParameterGetter(), fileModule == null ? null : L10nUtil.getFileModuleName(Locales.USER, fileModule.name()));
					}
					return (Long) ((Object[]) parameterValue)[1];
				case CRITERIA_ID_TO_DB_MODULE:
					return parameterValue == null ? null : CheckIDUtil.checkCriteriaId((Long) parameterValue, criteriaDao).getModule();
				default:
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_SERVICE_METHOD_PARAMETER_TRANSFORMATION,
							DefaultMessages.UNSUPPORTED_SERVICE_METHOD_PARAMETER_TRANSFORMATION, new Object[] { permission.getTransformation().name() }));
			}
		}
		return parameterValue;
	}
}
