// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::ToolsService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.hibernate.Cache;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.phoenixctms.ctsms.adapt.ReminderEntityAdapter;
import org.phoenixctms.ctsms.compare.ComparatorFactory;
import org.phoenixctms.ctsms.domain.AlphaIdDao;
import org.phoenixctms.ctsms.domain.Announcement;
import org.phoenixctms.ctsms.domain.AnnouncementDao;
import org.phoenixctms.ctsms.domain.AspDao;
import org.phoenixctms.ctsms.domain.AspSubstanceDao;
import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusEntry;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.KeyPairDao;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItem;
import org.phoenixctms.ctsms.domain.MassMailRecipient;
import org.phoenixctms.ctsms.domain.MassMailRecipientDao;
import org.phoenixctms.ctsms.domain.Notification;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipient;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.NotificationType;
import org.phoenixctms.ctsms.domain.OpsCodeDao;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.PasswordDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandStatusEntry;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffCategory;
import org.phoenixctms.ctsms.domain.StaffStatusEntry;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.email.NotificationEmailSender;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.security.Authenticator;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.PasswordPolicy;
import org.phoenixctms.ctsms.util.AuthorisationExceptionCodes;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AlphaIdVO;
import org.phoenixctms.ctsms.vo.AnnouncementVO;
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.vo.AspVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CalendarWeekVO;
import org.phoenixctms.ctsms.vo.DBModuleVO;
import org.phoenixctms.ctsms.vo.EventImportanceVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.HolidayVO;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.JournalModuleVO;
import org.phoenixctms.ctsms.vo.LdapEntryVO;
import org.phoenixctms.ctsms.vo.LightECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.LightInputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.LightInquiryOutVO;
import org.phoenixctms.ctsms.vo.LightProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.OpsCodeVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.PasswordOutVO;
import org.phoenixctms.ctsms.vo.PasswordPolicyVO;
import org.phoenixctms.ctsms.vo.RandomizationModeVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.TimeZoneVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.ToolsService
 */
public class ToolsServiceImpl
		extends ToolsServiceBase {

	private static HashSet<Long> createSendDepartmentStaffCategorySet(NotificationType notificationType) {
		Collection<StaffCategory> sendDepartmentStaffCategories = notificationType.getSendDepartmentStaffCategories();
		HashSet<Long> result = new HashSet<Long>(sendDepartmentStaffCategories.size());
		Iterator<StaffCategory> it = sendDepartmentStaffCategories.iterator();
		while (it.hasNext()) {
			result.add(it.next().getId());
		}
		return result;
	}

	private static ArrayList<Notification> filterNotifications(Collection<Notification> notifications, org.phoenixctms.ctsms.enumeration.NotificationType notificationType,
			Boolean obsolete) throws Exception {
		ArrayList<Notification> result = new ArrayList<Notification>(notifications);
		Iterator<Notification> notificationsIt = notifications.iterator();
		while (notificationsIt.hasNext()) {
			Notification notification = notificationsIt.next();
			if ((obsolete == null || notification.isObsolete() == obsolete) && notification.getType().getType().equals(notificationType)) {
				result.add(notification);
			}
		}
		return result;
	}

	private static JournalEntry logSystemMessage(User user, UserOutVO userVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.userOutVOToString(userVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private NotificationEmailSender notificationEmailSender;
	private Authenticator authenticator;
	private SessionFactory sessionFactory;

	private Collection<LdapEntryVO> completeLdapEntry(String nameInfix,
			AuthenticationType authMethod, Integer limit) throws Exception {
		if (!CommonUtil.isEmptyString(nameInfix) && authMethod != null) {
			try {
				return authenticator.search(authMethod, limit, nameInfix);
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}
		return new ArrayList<LdapEntryVO>();
	}

	private LdapEntryVO getLdapEntry(AuthenticationVO auth, String username, AuthenticationType authMethod) throws Exception {
		if (username != null && username.length() > 0 && authMethod != null) {
			CoreUtil.setUser(auth, this.getUserDao()); // exception message localization
			List<LdapEntryVO> ldapEntries = null;
			try {
				ldapEntries = authenticator.searchAuth(authMethod, username);
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
			if (ldapEntries.size() == 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ZERO_LDAP_USERS, username);
			} else if (ldapEntries.size() > 1) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MULTIPLE_LDAP_USERS, username, ldapEntries.size());
			}
			return ldapEntries.get(0);
		}
		return null;
	}

	@Override
	protected Date handleAddIntervals(Date date, VariablePeriod period,
			Long explicitDays, int n) throws Exception {
		return DateCalc.addIntervals(date, period, explicitDays, n);
	}

	@Override
	protected UserOutVO handleAddUser(UserInVO newUser, PasswordInVO newPassword, String plainDepartmentPassword) throws Exception {
		UserDao userDao = this.getUserDao();
		ServiceUtil.checkUsernameExists(newUser.getName(), userDao);
		ServiceUtil.checkUserInput(newUser, null, plainDepartmentPassword, this.getDepartmentDao(), this.getStaffDao());
		if (!PasswordPolicy.USER.isAdminIgnorePolicy()) {
			PasswordPolicy.USER.checkStrength(newPassword.getPassword());
		}
		ServiceUtil.checkLogonLimitations(newPassword);
		PasswordDao passwordDao = this.getPasswordDao();
		User user = userDao.userInVOToEntity(newUser);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User modified = null;
		CoreUtil.modifyVersion(user, now, modified);
		ServiceUtil.createKeyPair(user, plainDepartmentPassword, this.getKeyPairDao());
		user = userDao.create(user);
		ServiceUtil.createPassword(true, passwordDao.passwordInVOToEntity(newPassword), user, now, null, newPassword.getPassword(), plainDepartmentPassword, passwordDao,
				this.getJournalEntryDao());
		UserOutVO result = userDao.toUserOutVO(user);
		logSystemMessage(user, result, now, modified, SystemMessageCodes.USER_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected void handleChangeDepartmentPassword(Long departmentId, String plainNewDepartmentPassword, String plainOldDepartmentPassword) throws Exception {
		Department department = CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		PasswordPolicy.DEPARTMENT.checkStrength(plainNewDepartmentPassword);
		if (!CryptoUtil.checkDepartmentPassword(department, plainOldDepartmentPassword)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.OLD_DEPARTMENT_PASSWORD_WRONG);
		}
		CryptoUtil.encryptDepartmentKey(department, CryptoUtil.decryptDepartmentKey(department, plainOldDepartmentPassword), plainNewDepartmentPassword);
		this.getDepartmentDao().update(department);
		KeyPairDao keyPairDao = this.getKeyPairDao();
		PasswordDao passwordDao = this.getPasswordDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		UserDao userDao = this.getUserDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Iterator<User> usersIt = department.getUsers().iterator();
		while (usersIt.hasNext()) {
			User user = usersIt.next();
			userDao.lock(user, LockMode.PESSIMISTIC_WRITE);
			ServiceUtil.updateUserDepartmentPassword(user, plainNewDepartmentPassword, plainOldDepartmentPassword, keyPairDao, passwordDao);
			logSystemMessage(user, userDao.toUserOutVO(user), now, null, SystemMessageCodes.DEPARTMENT_PASSWORD_CHANGED, null, null, journalEntryDao);
		}
	}

	@Override
	protected void handleClearCache() throws Exception {
		// TODO Auto-generated method stub
		// cacheManager.clearAll();
		Session session = sessionFactory.getCurrentSession();
		if (session != null) {
			session.clear(); // internal cache clear
		}
		Cache cache = sessionFactory.getCache();
		//
		// if (cache != null) {
		// cache.ev..evictAllRegions(); // Evict data from all query regions.
		// }
		// sessionFactory.getCache().evictQueryRegions();
		// sessionFactory.getCache().evictDefaultQueryRegion();
		// sessionFactory.getCache().evictCollectionRegions();
		// sessionFactory.getCache().evictEntityRegions();
		if (cache != null) {
			cache.evictCollectionRegions();
			cache.evictDefaultQueryRegion();
			cache.evictEntityRegions();
			cache.evictQueryRegions();
		}
		// cache.evictNaturalIdRegions();
		// // {
		// // Session s = (Session)em.getDelegate();
		// //SessionFactory sf = s.getSessionFactory();
		// Map classMetadata = sessionFactory.getAllClassMetadata();
		// Iterator it = classMetadata.values().iterator();
		// while (it.hasNext()) {
		// //for (EntityPersister ep : classMetadata.values()) {
		// EntityPersister ep = (EntityPersister) it.next();
		// if (ep.hasCache()) {
		// sessionFactory.evictEntity(ep..getCache().getRegionName());
		// }
		// }
		// Map collMetadata = sf.getAllCollectionMetadata();
		// for (AbstractCollectionPersister acp : collMetadata.values()) {
		// if (acp.hasCache()) {
		// sf.evictCollection(acp.getCache().getRegionName());
		// }
		// }
		//
		// return;
		// // }
	}

	@Override
	protected Collection<AlphaIdVO> handleCompleteAlphaId(AuthenticationVO auth, String textInfix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		AlphaIdDao alphaIdDao = this.getAlphaIdDao();
		Collection alphaIds = alphaIdDao.findAlphaIds(textInfix, limit);
		alphaIdDao.toAlphaIdVOCollection(alphaIds);
		return alphaIds;
	}

	@Override
	protected Collection<String> handleCompleteAlphaIdCode(AuthenticationVO auth, String codePrefix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getAlphaIdDao().findAlphaIdCodes(codePrefix, limit);
	}

	@Override
	protected Collection<String> handleCompleteAlphaIdText(AuthenticationVO auth, String textInfix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getAlphaIdDao().findAlphaIdTexts(textInfix, limit);
	}

	@Override
	protected Collection<AspVO> handleCompleteAsp(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		AspDao aspDao = this.getAspDao();
		Collection asps = aspDao.findAsps(nameInfix, limit);
		aspDao.toAspVOCollection(asps);
		return asps;
	}

	@Override
	protected Collection<String> handleCompleteAspAtcCodeCode(AuthenticationVO auth, String codePrefix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getAspAtcCodeDao().findAspAtcCodeCodes(codePrefix, limit);
	}

	@Override
	protected Collection<String> handleCompleteAspName(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getAspDao().findAspNames(nameInfix, limit);
	}

	@Override
	protected Collection<AspSubstanceVO> handleCompleteAspSubstance(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		AspSubstanceDao aspSubstanceDao = this.getAspSubstanceDao();
		Collection substances = aspSubstanceDao.findAspSubstances(nameInfix, limit);
		aspSubstanceDao.toAspSubstanceVOCollection(substances);
		return substances;
	}

	@Override
	protected Collection<String> handleCompleteAspSubstanceName(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getAspSubstanceDao().findAspSubstanceNames(nameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteBankCodeNumber(AuthenticationVO auth, String bankCodeNumberPrefix, String bicPrefix, String bankNameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getBankIdentificationDao().findBankCodeNumbers(bankCodeNumberPrefix, bicPrefix, bankNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteBankName(AuthenticationVO auth, String bankNameInfix, String bankCodeNumberPrefix, String bicPrefix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getBankIdentificationDao().findBankNames(bankCodeNumberPrefix, bicPrefix, bankNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteBic(AuthenticationVO auth, String bicPrefix, String bankCodeNumberPrefix, String bankNameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getBankIdentificationDao().findBics(bankCodeNumberPrefix, bicPrefix, bankNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteCityName(AuthenticationVO auth, String cityNameInfix, String countryNameInfix,
			String zipCodePrefix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getZipDao().findCityNames(countryNameInfix, zipCodePrefix, cityNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteCountryName(AuthenticationVO auth, String countryNameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getCountryDao().findCountryNames(countryNameInfix, limit);
	}

	@Override
	protected Collection<LightECRFFieldOutVO> handleCompleteEcrfField(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection ecrfFields = ecrfFieldDao.findAllSorted(nameInfix, limit);
		ecrfFieldDao.toLightECRFFieldOutVOCollection(ecrfFields);
		return ecrfFields;
	}

	@Override
	protected Collection<InputFieldOutVO> handleCompleteEcrfFieldInputField(AuthenticationVO auth, String fieldNameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByEcrfFieldsSorted(fieldNameInfix, limit);
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleCompleteEcrfFieldInputFieldSelectionSetValue(AuthenticationVO auth, String nameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = selectionSetValueDao.findUsedByEcrfFieldsSorted(nameInfix, limit);
		selectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<String> handleCompleteIcdSystBlockPreferredRubricLabel(
			AuthenticationVO auth, String preferredRubricLabelInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getIcdSystBlockDao().findBlockPreferredRubricLabels(preferredRubricLabelInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteIcdSystCategoryPreferredRubricLabel(
			AuthenticationVO auth, String preferredRubricLabelInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getIcdSystCategoryDao().findCategoryPreferredRubricLabels(preferredRubricLabelInfix, limit);
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> handleCompleteInputFieldSelectionSetValue(AuthenticationVO auth, String nameInfix, Long inputFieldId, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		// no check for inputFieldId for performance reasons...
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = inputFieldSelectionSetValueDao.findSelectionSetValues(inputFieldId, nameInfix, limit);
		inputFieldSelectionSetValueDao.toInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<String> handleCompleteInputFieldSelectionSetValueValue(AuthenticationVO auth, String valueInfix, Long inputFieldId, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		// no check for inputFieldId for performance reasons...
		return this.getInputFieldSelectionSetValueDao().findValues(inputFieldId, valueInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteInputFieldTextValue(
			AuthenticationVO auth, String textValueInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getInputFieldValueDao().findTextValues(textValueInfix, limit);
	}

	@Override
	protected Collection<LightInquiryOutVO> handleCompleteInquiry(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findAllSorted(nameInfix, limit);
		inquiryDao.toLightInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InputFieldOutVO> handleCompleteInquiryInputField(AuthenticationVO auth, String fieldNameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByInquiriesSorted(fieldNameInfix, limit);
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleCompleteInquiryInputFieldSelectionSetValue(AuthenticationVO auth, String nameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = selectionSetValueDao.findUsedByInquiriesSorted(nameInfix, limit);
		selectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<LdapEntryVO> handleCompleteLdapEntry1(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return completeLdapEntry(nameInfix, AuthenticationType.LDAP1, limit);
	}

	@Override
	protected Collection<LdapEntryVO> handleCompleteLdapEntry2(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return completeLdapEntry(nameInfix, AuthenticationType.LDAP2, limit);
	}

	@Override
	protected Collection<String> handleCompleteMedicationDoseUnit(AuthenticationVO auth, String doseUnitPrefix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getMedicationDao().findDoseUnits(doseUnitPrefix, limit);
	}

	@Override
	protected Collection<OpsCodeVO> handleCompleteOpsCode(AuthenticationVO auth, String textInfix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		OpsCodeDao opsCodeDao = this.getOpsCodeDao();
		Collection opsCodes = opsCodeDao.findOpsCodes(textInfix, limit);
		opsCodeDao.toOpsCodeVOCollection(opsCodes);
		return opsCodes;
	}

	@Override
	protected Collection<String> handleCompleteOpsCodeCode(AuthenticationVO auth, String codePrefix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getOpsCodeDao().findOpsCodeCodes(codePrefix, limit);
	}

	@Override
	protected Collection<String> handleCompleteOpsCodeText(AuthenticationVO auth, String textInfix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getOpsCodeDao().findOpsCodeTexts(textInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteOpsSystBlockPreferredRubricLabel(
			AuthenticationVO auth, String preferredRubricLabelInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getOpsSystBlockDao().findBlockPreferredRubricLabels(preferredRubricLabelInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteOpsSystCategoryPreferredRubricLabel(
			AuthenticationVO auth, String preferredRubricLabelInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getOpsSystCategoryDao().findCategoryPreferredRubricLabels(preferredRubricLabelInfix, limit);
	}

	@Override
	protected Collection<LightProbandListEntryTagOutVO> handleCompleteProbandListEntryTag(AuthenticationVO auth, String nameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		ProbandListEntryTagDao listEntryTagDao = this.getProbandListEntryTagDao();
		Collection tags = listEntryTagDao.findAllSorted(nameInfix, limit);
		listEntryTagDao.toLightProbandListEntryTagOutVOCollection(tags);
		return tags;
	}

	@Override
	protected Collection<InputFieldOutVO> handleCompleteProbandListEntryTagInputField(AuthenticationVO auth, String fieldNameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findUsedByListEntryTagsSorted(fieldNameInfix, limit);
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected Collection<LightInputFieldSelectionSetValueOutVO> handleCompleteProbandListEntryTagInputFieldSelectionSetValue(AuthenticationVO auth, String nameInfix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = selectionSetValueDao.findUsedByListEntryTagsSorted(nameInfix, limit);
		selectionSetValueDao.toLightInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected Collection<String> handleCompleteStreetName(AuthenticationVO auth, String streetNameInfix, String countryName,
			String zipCode, String cityName, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getStreetDao().findStreetNames(countryName, zipCode, cityName, streetNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteSystemMessageCode(AuthenticationVO auth, String systemMessageCodeInfix,
			Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		ArrayList<String> result = new ArrayList<String>();
		if (limit == null) {
			limit = Settings.getIntNullable(SettingCodes.SYSTEM_MESSAGE_CODE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
					DefaultSettings.SYSTEM_MESSAGE_CODE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT);
		}
		Pattern systemMessageCodeRegex = null;
		if (!CommonUtil.isEmptyString(systemMessageCodeInfix)) {
			systemMessageCodeRegex = CommonUtil.createSqlLikeRegexp(CommonUtil.SQL_LIKE_PERCENT_WILDCARD + systemMessageCodeInfix + CommonUtil.SQL_LIKE_PERCENT_WILDCARD, true);
		}
		Iterator<String> codesIt = CoreUtil.SYSTEM_MESSAGE_CODES.iterator();
		while (codesIt.hasNext() && (limit == null || result.size() < limit)) { // no trie for searching 300 entries
			String code = codesIt.next();
			if (systemMessageCodeRegex == null || systemMessageCodeRegex.matcher(code).find()) {
				result.add(code);
			}
		}
		return result;
	}

	@Override
	protected Collection<TimeZoneVO> handleCompleteTimeZone(AuthenticationVO auth, String nameInfix, Integer limit) {
		CoreUtil.setUser(auth, this.getUserDao());
		if (nameInfix != null) {
			nameInfix = nameInfix.toLowerCase();
		}
		if (limit == null) {
			limit = Settings.getIntNullable(SettingCodes.TIMEZONE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
					DefaultSettings.TIMEZONE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT);
		}
		ArrayList<TimeZone> timeZones = CoreUtil.getTimeZones();
		ArrayList<TimeZoneVO> result = new ArrayList<TimeZoneVO>(timeZones.size());
		Iterator<TimeZone> it = timeZones.iterator();
		Locale userLocale = L10nUtil.getLocale(Locales.USER);
		while (it.hasNext() && (limit == null || result.size() < limit)) {
			TimeZone timeZone = it.next();
			TimeZoneVO timeZoneVO = new TimeZoneVO();
			timeZoneVO.setTimeZoneID(CommonUtil.timeZoneToString(timeZone));
			timeZoneVO.setName(CommonUtil.timeZoneToDisplayString(timeZone, userLocale));
			if (CommonUtil.isEmptyString(nameInfix)
					|| timeZoneVO.getName().toLowerCase().contains(nameInfix)
					|| timeZoneVO.getTimeZoneID().toLowerCase().contains(nameInfix)) {
				timeZoneVO.setRawOffset(timeZone.getRawOffset());
				result.add(timeZoneVO);
			}
		}
		return result;
	}

	@Override
	protected Collection<String> handleCompleteTitle(AuthenticationVO auth, String titlePrefix, Integer limit)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getTitleDao().findTitles(titlePrefix, limit);
	}

	@Override
	protected Collection<String> handleCompleteZipCode(AuthenticationVO auth, String zipCodePrefix, String countryNameInfix,
			String cityNameInfix, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getZipDao().findZipCodes(countryNameInfix, zipCodePrefix, cityNameInfix, limit);
	}

	@Override
	protected Collection<String> handleCompleteZipCodeByStreetName(AuthenticationVO auth, String zipCodePrefix, String countryName,
			String cityName, String streetName, Integer limit) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return this.getStreetDao().findZipCodes(countryName, zipCodePrefix, cityName, streetName, limit);
	}

	@Override
	protected long handleDeleteProbands(Long departmentId, PSFVO psf) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Date today = new Date();
		ProbandDao probandDao = this.getProbandDao();
		long count = 0;
		final HashMap<Long, Long> countMap = new HashMap<Long, Long>();
		Iterator<Proband> toBeAutoDeletedIt = this.getProbandDao().findToBeAutoDeleted(today, departmentId, null, null, null, true, true, psf).iterator();
		while (toBeAutoDeletedIt.hasNext()) {
			Proband proband = toBeAutoDeletedIt.next();
			Long probandDepartmentId = proband.getDepartment().getId();
			ServiceUtil.removeProband(proband, null, true,
					null,
					new Timestamp(System.currentTimeMillis()),
					probandDao,
					this.getProbandContactParticularsDao(),
					this.getAnimalContactParticularsDao(),
					this.getJournalEntryDao(),
					this.getNotificationDao(),
					this.getNotificationRecipientDao(),
					this.getProbandTagValueDao(),
					this.getProbandContactDetailValueDao(),
					this.getProbandAddressDao(),
					this.getProbandStatusEntryDao(),
					this.getDiagnosisDao(),
					this.getProcedureDao(),
					this.getMedicationDao(),
					this.getInventoryBookingDao(),
					this.getMoneyTransferDao(),
					this.getBankAccountDao(),
					this.getProbandListStatusEntryDao(),
					this.getProbandListEntryDao(),
					this.getProbandListEntryTagValueDao(),
					this.getInputFieldValueDao(),
					this.getInquiryValueDao(),
					this.getECRFFieldValueDao(),
					this.getECRFFieldStatusEntryDao(),
					this.getSignatureDao(),
					this.getECRFStatusEntryDao(),
					this.getMassMailRecipientDao(),
					this.getFileDao());
			if (countMap.containsKey(probandDepartmentId)) {
				countMap.put(probandDepartmentId, countMap.get(probandDepartmentId) + 1l);
			} else {
				countMap.put(probandDepartmentId, 1l);
			}
			count++;
		}
		if (count > 0) {
			NotificationDao notificationDao = this.getNotificationDao();
			DepartmentDao departmentDao = this.getDepartmentDao();
			Iterator<Long> departmentIt = countMap.keySet().iterator();
			while (departmentIt.hasNext()) {
				final Long probandDepartmentId = departmentIt.next();
				Map messageParameters = CoreUtil.createEmptyTemplateModel();
				messageParameters.put(NotificationMessageTemplateParameters.NUMBER_OF_PROBANDS_DELETED, countMap.get(probandDepartmentId).toString());
				notificationDao.addNotification(departmentDao.load(probandDepartmentId), today, messageParameters);
			}
		}
		return count;
	}

	@Override
	protected String handleGetAllowedFileExtensionsPattern(FileModule module, Boolean image)
			throws Exception {
		Collection<String> fileNameExtensions = this.getMimeTypeDao().findFileNameExtensions(module, image);
		StringBuilder result = new StringBuilder();
		String delimiter;
		if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
			result.append("/(\\.|\\/)(");
			delimiter = "|";
		} else {
			delimiter = ";";
		}
		HashSet<String> dupeCheck = new HashSet<String>();
		boolean first = true;
		Iterator<String> it = fileNameExtensions.iterator();
		while (it.hasNext()) {
			String fileNameExtensionsString = it.next();
			if (fileNameExtensionsString != null && fileNameExtensionsString.length() > 0) {
				if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
					if (dupeCheck.add(fileNameExtensionsString)) {
						if (first) {
							first = false;
						} else {
							result.append(delimiter);
						}
						result.append("(");
						result.append(fileNameExtensionsString);
						result.append(")");
						try {
							Pattern.compile(result.toString() + ")$/");
						} catch (PatternSyntaxException e) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_REGEXP_PATTERN_FROM_FILE_EXTENSION, fileNameExtensionsString, e.getMessage());
						}
					}
				} else {
					String[] fileNameExtensionsArray = fileNameExtensionsString.split(java.util.regex.Pattern.quote(delimiter), -1);
					for (int i = 0; i < fileNameExtensionsArray.length; i++) {
						if (fileNameExtensionsArray[i].length() == 0) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_EXTENSION_ZERO_LENGTH);
						} else if (!CommonUtil.FILE_EXTENSION_PATTERN_REGEXP.matcher(fileNameExtensionsArray[i]).find()) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_FILE_EXTENSION, fileNameExtensionsArray[i]);
						} else if (dupeCheck.add(fileNameExtensionsArray[i])) {
							if (first) {
								first = false;
							} else {
								result.append(delimiter);
							}
							result.append(fileNameExtensionsArray[i]);
						}
					}
				}
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_EXTENSIONS_REQUIRED);
			}
		}
		if (dupeCheck.size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_MIME_TYPES_OR_FILE_EXTENSIONS);
		}
		if (CommonUtil.FILE_EXTENSION_REGEXP_MODE) {
			result.append(")$/");
		}
		return result.toString();
	}

	@Override
	protected AnnouncementVO handleGetAnnouncement() throws Exception {
		AnnouncementDao announcementDao = this.getAnnouncementDao();
		Announcement announcement = announcementDao.getAnnouncement();
		if (announcement != null) {
			return announcementDao.toAnnouncementVO(announcement);
		}
		return null;
	}

	@Override
	protected CalendarWeekVO handleGetCalendarWeek(Date date) throws Exception {
		return DateCalc.getCalendarWeek(date);
	}

	@Override
	protected String handleGetCurrencySymbol() throws Exception {
		return Settings.getString(SettingCodes.CURRENCY_SYMBOL, Bundle.SETTINGS, DefaultSettings.CURRENCY_SYMBOL);
	}

	@Override
	protected String handleGetDefaultLocale() throws Exception {
		return CommonUtil.localeToString(Locale.getDefault());
	}

	@Override
	protected String handleGetDefaultTimeZone() throws Exception {
		return CommonUtil.timeZoneToString(TimeZone.getDefault());
	}

	@Override
	protected String handleGetEmailDomainName() throws Exception {
		return Settings.getEmailDomainName();
	}

	@Override
	protected Collection<HolidayVO> handleGetHolidays(AuthenticationVO auth, Date from, Date to, Boolean holiday)
			throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return DateCalc.getHolidays(from, to, holiday, this.getHolidayDao());
	}

	@Override
	protected String handleGetHttpBaseUrl() throws Exception {
		return Settings.getHttpBaseUrl();
	}

	@Override
	protected String handleGetHttpDomainName() throws Exception {
		return Settings.getHttpDomainName();
	}

	@Override
	protected String handleGetHttpHost() throws Exception {
		return Settings.getHttpHost();
	}

	@Override
	protected String handleGetHttpScheme() throws Exception {
		return Settings.getHttpScheme();
	}

	@Override
	protected InputFieldImageVO handleGetInputFieldImage(Long inputFieldId)
			throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = CheckIDUtil.checkInputFieldId(inputFieldId, inputFieldDao);
		return inputFieldDao.toInputFieldImageVO(inputField);
	}

	@Override
	protected Integer handleGetInputFieldImageUploadSizeLimit()
			throws Exception {
		return Settings.getIntNullable(SettingCodes.INPUT_FIELD_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_IMAGE_SIZE_LIMIT);
	}

	@Override
	protected String handleGetInstanceName() throws Exception {
		return Settings.getInstanceName();
	}

	@Override
	protected LdapEntryVO handleGetLdapEntry1(AuthenticationVO auth, String username) throws Exception {
		return getLdapEntry(auth, username, AuthenticationType.LDAP1);
	}

	@Override
	protected LdapEntryVO handleGetLdapEntry2(AuthenticationVO auth, String username) throws Exception {
		return getLdapEntry(auth, username, AuthenticationType.LDAP2);
	}

	@Override
	protected DBModuleVO handleGetLocalizedDBModule(AuthenticationVO auth, DBModule module) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createDBModuleVO(Locales.USER, module);
	}

	@Override
	protected EventImportanceVO handleGetLocalizedEventImportance(AuthenticationVO auth, EventImportance importance) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createEventImportanceVO(Locales.USER, importance);
	}

	@Override
	protected InputFieldTypeVO handleGetLocalizedInputFieldType(AuthenticationVO auth, InputFieldType fieldType) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createInputFieldTypeVO(Locales.USER, fieldType);
	}

	@Override
	protected JournalModuleVO handleGetLocalizedJournalModule(AuthenticationVO auth, JournalModule module) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createJournalModuleVO(Locales.USER, module);
	}

	@Override
	protected RandomizationModeVO handleGetLocalizedRandomizationMode(AuthenticationVO auth, RandomizationMode mode) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createRandomizationModeVO(Locales.USER, mode);
	}

	@Override
	protected SexVO handleGetLocalizedSex(AuthenticationVO auth, Sex sex) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createSexVO(Locales.USER, sex);
	}

	@Override
	protected VariablePeriodVO handleGetLocalizedVariablePeriod(AuthenticationVO auth, VariablePeriod period) throws Exception {
		CoreUtil.setUser(auth, this.getUserDao());
		return L10nUtil.createVariablePeriodVO(Locales.USER, period);
	}

	@Override
	protected PasswordInVO handleGetNewPassword(AuthenticationVO auth) throws Exception {
		authenticator.authenticate(auth, false);
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword);
		newPassword.setPassword(PasswordPolicy.USER.getDummyPassword());
		return newPassword;
	}

	@Override
	protected PasswordPolicyVO handleGetPasswordPolicy(AuthenticationVO auth) throws Exception {
		authenticator.authenticate(auth, false);
		return PasswordPolicy.USER.getPasswordPolicyVO();
	}

	@Override
	protected Integer handleGetProbandImageUploadSizeLimit()
			throws Exception {
		return Settings.getIntNullable(SettingCodes.PROBAND_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.PROBAND_IMAGE_SIZE_LIMIT);
	}

	@Override
	protected FileStreamOutVO handleGetPublicFileStream(Long fileId)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File file = CheckIDUtil.checkFileId(fileId, fileDao);
		if (CommonUtil.getUseFileEncryption(file.getModule())) {
			throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.ENCRYPTED_FILE, fileId.toString());
		}
		if (!file.isPublicFile()) {
			throw L10nUtil.initAuthorisationException(AuthorisationExceptionCodes.FILE_NOT_PUBLIC, fileId.toString());
		}
		FileStreamOutVO result = fileDao.toFileStreamOutVO(file);
		return result;
	}

	@Override
	protected Integer handleGetStaffImageUploadSizeLimit()
			throws Exception {
		return Settings.getIntNullable(SettingCodes.STAFF_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.STAFF_IMAGE_SIZE_LIMIT);
	}

	@Override
	protected Integer handleGetUploadSizeLimit() throws Exception {
		if (Settings.getBoolean(SettingCodes.USE_EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.USE_EXTERNAL_FILE_DATA_DIR)) {
			return Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_SIZE_LIMIT);
		} else {
			return Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_CONTENT_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_CONTENT_SIZE_LIMIT);
		}
	}

	@Override
	protected boolean handleIsHoliday(Date date) throws Exception {
		return DateCalc.isHoliday(date, this.getHolidayDao());
	}

	@Override
	protected boolean handleIsStreamUploadEnabled() throws Exception {
		return Settings.getBoolean(SettingCodes.USE_EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.USE_EXTERNAL_FILE_DATA_DIR);
	}

	@Override
	protected boolean handleIsTrustedHost(String host) throws Exception {
		return CoreUtil.checkHostIp(host);
	}

	@Override
	protected PasswordOutVO handleLogon(AuthenticationVO auth) throws Exception {
		Password lastPassword = null;
		User user = null;
		Timestamp now = null;
		PasswordDao passwordDao = this.getPasswordDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		PasswordOutVO lastPasswordVO;
		UserOutVO userVO;
		try {
			lastPassword = authenticator.authenticate(auth, true); // ServiceUtil.authenticate(auth, true, this.getUserDao(), passwordDao);
			user = lastPassword.getUser();
			now = lastPassword.getLastSuccessfulLogonTimestamp();
			lastPasswordVO = passwordDao.toPasswordOutVO(lastPassword);
			userVO = lastPasswordVO.getUser();
			logSystemMessage(user, userVO, now, user, SystemMessageCodes.SUCCESSFUL_LOGON, lastPasswordVO, null, journalEntryDao);
		} catch (AuthenticationException e) {
			lastPassword = CoreUtil.getLastPassword();
			user = CoreUtil.getUser();
			if (user != null) {
				now = new Timestamp(System.currentTimeMillis());
				lastPasswordVO = lastPassword != null ? passwordDao.toPasswordOutVO(lastPassword) : null;
				userVO = lastPasswordVO != null ? lastPasswordVO.getUser() : this.getUserDao().toUserOutVO(user);
				logSystemMessage(user, userVO, now, user, SystemMessageCodes.FAILED_LOGON, lastPasswordVO, null, journalEntryDao);
			}
			throw e;
		} finally {
			if (lastPassword != null) {
				passwordDao.update(lastPassword);
			}
		}
		return passwordDao.toPasswordOutVO(lastPassword);
	}

	@Override
	protected void handleMarkMassMailRead(String beacon) throws Exception {
		CommonUtil.parseUUID(beacon);
		MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
		MassMailRecipient recipient = massMailRecipientDao.searchUniqueBeacon(beacon);
		if (recipient == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_RECIPIENT_BEACON_NOT_FOUND, beacon);
		}
		massMailRecipientDao.refresh(recipient, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		recipient.setRead(recipient.getRead() + 1l);
		recipient.setReadTimestamp(now);
		CoreUtil.modifyVersion(recipient, recipient.getVersion(), recipient.getModifiedTimestamp(), recipient.getModifiedUser());
		massMailRecipientDao.update(recipient);
	}

	@Override
	protected void handleNoOp() throws Exception {
	}

	@Override
	protected long handlePrepareNotifications(Long departmentId) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Date today = new Date();
		NotificationDao notificationDao = this.getNotificationDao();
		long count = 0;
		Iterator<MaintenanceScheduleItem> maintenanceScheduleIt = this.getMaintenanceScheduleItemDao()
				.findMaintenanceSchedule(today, null, departmentId, null, null, true, false, null)
				.iterator();
		while (maintenanceScheduleIt.hasNext()) {
			MaintenanceScheduleItem maintenanceScheduleItem = maintenanceScheduleIt.next();
			Collection<Notification> notifications = filterNotifications(maintenanceScheduleItem.getNotifications(),
					org.phoenixctms.ctsms.enumeration.NotificationType.MAINTENANCE_REMINDER, false);
			if (notifications.size() == 0) {
				if (notificationDao.addNotification(maintenanceScheduleItem, today, null) != null) {
					count++;
				}
			} else if (maintenanceScheduleItem.isRecurring()) {
				if (ReminderEntityAdapter.getInstance(maintenanceScheduleItem).getReminderStart(today, false, null, null)
						.compareTo(Collections.max(notifications, ComparatorFactory.createSafeLong(Notification::getId)).getDate()) > 0) {
					if (notificationDao.addNotification(maintenanceScheduleItem, today, null) != null) {
						count++;
					}
				}
			}
		}
		Iterator<InventoryStatusEntry> inventoryStatusIt = this.getInventoryStatusEntryDao()
				.findInventoryStatus(CommonUtil.dateToTimestamp(today), null, departmentId, null, false, null, null).iterator();
		while (inventoryStatusIt.hasNext()) {
			InventoryStatusEntry inventoryStatusEntry = inventoryStatusIt.next();
			if (!ServiceUtil.testNotificationExists(inventoryStatusEntry.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.INVENTORY_INACTIVE, false)) {
				if (notificationDao.addNotification(inventoryStatusEntry, today, null) != null) {
					count++;
				}
			}
		}
		Iterator<StaffStatusEntry> staffStatusIt = this.getStaffStatusEntryDao().findStaffStatus(CommonUtil.dateToTimestamp(today), null, departmentId, null, false, null, null)
				.iterator();
		while (staffStatusIt.hasNext()) {
			StaffStatusEntry staffStatusEntry = staffStatusIt.next();
			if (!ServiceUtil.testNotificationExists(staffStatusEntry.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.STAFF_INACTIVE, false)) {
				if (notificationDao.addNotification(staffStatusEntry, today, null) != null) {
					count++;
				}
			}
		}
		Iterator<ProbandStatusEntry> probandStatusIt = this.getProbandStatusEntryDao()
				.findProbandStatus(CommonUtil.dateToTimestamp(today), null, departmentId, null, false, null, null).iterator();
		while (probandStatusIt.hasNext()) {
			ProbandStatusEntry probandStatusEntry = probandStatusIt.next();
			if (!ServiceUtil.testNotificationExists(probandStatusEntry.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.PROBAND_INACTIVE, false)) {
				if (notificationDao.addNotification(probandStatusEntry, today, null) != null) {
					count++;
				}
			}
		}
		VariablePeriod expiringCourseReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD);
		Long expiringCourseReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD_DAYS);
		Iterator<Course> expiringCourseIt = this.getCourseDao().findExpiring(today, departmentId, null, expiringCourseReminderPeriod, expiringCourseReminderPeriodDays, false, null)
				.iterator();
		while (expiringCourseIt.hasNext()) {
			Course course = expiringCourseIt.next();
			if (!ServiceUtil.testNotificationExists(course.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE, false)) {
				Map messageParameters = CoreUtil.createEmptyTemplateModel();
				messageParameters.put(NotificationMessageTemplateParameters.COURSE_EXPIRATION_DAYS_LEFT,
						DateCalc.dateDeltaDays(today, DateCalc.addInterval(course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays())));
				notificationDao.addExpiringCourseNotification(course, today, messageParameters);
				count++;
			}
		}
		VariablePeriod expiringCourseParticipationReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD);
		Long expiringCourseParticipationReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD_DAYS,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD_DAYS);
		Iterator<CourseParticipationStatusEntry> courseParticipationStatusEntryIt = this.getCourseParticipationStatusEntryDao()
				.findExpiring(today, null, null, departmentId, null, null, true, expiringCourseParticipationReminderPeriod, expiringCourseParticipationReminderPeriodDays, false,
						null)
				.iterator();
		while (courseParticipationStatusEntryIt.hasNext()) {
			CourseParticipationStatusEntry courseParticipationStatusEntry = courseParticipationStatusEntryIt.next();
			if (!ServiceUtil.testNotificationExists(courseParticipationStatusEntry.getNotifications(),
					org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE_PARTICIPATION,
					false)) {
				Course course = courseParticipationStatusEntry.getCourse();
				Map messageParameters = CoreUtil.createEmptyTemplateModel();
				messageParameters.put(NotificationMessageTemplateParameters.COURSE_EXPIRATION_DAYS_LEFT,
						DateCalc.dateDeltaDays(today, DateCalc.addInterval(course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays())));
				if (notificationDao.addNotification(courseParticipationStatusEntry, true, false, today, messageParameters) != null) {
					count++;
				}
			}
		}
		Iterator<TimelineEvent> timelineScheduleIt = this.getTimelineEventDao().findTimelineSchedule(today, null, departmentId, null, true, false, false, null).iterator();
		while (timelineScheduleIt.hasNext()) {
			TimelineEvent timelineEvent = timelineScheduleIt.next();
			if (!ServiceUtil.testNotificationExists(timelineEvent.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.TIMELINE_EVENT_REMINDER, false)) {
				if (notificationDao.addNotification(timelineEvent, today, null) != null) {
					count++;
				}
			}
		}
		VariablePeriod visitScheduleItemReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD);
		Long visitScheduleItemReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS);
		Iterator<VisitScheduleItem> visitScheduleItemScheduleIt = this.getVisitScheduleItemDao()
				.findVisitScheduleItemSchedule(today, null, departmentId, true, false, visitScheduleItemReminderPeriod, visitScheduleItemReminderPeriodDays, false, null)
				.iterator();
		while (visitScheduleItemScheduleIt.hasNext()) {
			VisitScheduleItem visitScheduleItem = visitScheduleItemScheduleIt.next();
			if (!ServiceUtil.testNotificationExists(visitScheduleItem.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.VISIT_SCHEDULE_ITEM_REMINDER, false)) {
				if (notificationDao.addNotification(visitScheduleItem, today, null) != null) {
					count++;
				}
			}
		}
		VariablePeriod expiringProbandAutoDeleteReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_PROBAND_AUTO_DELETE_REMINDER_PERIOD,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_PROBAND_AUTO_DELETE_REMINDER_PERIOD);
		Long expiringProbandAutoDeleteReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_PROBAND_AUTO_DELETE_REMINDER_PERIOD_DAYS,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_PROBAND_AUTO_DELETE_REMINDER_PERIOD_DAYS);
		Iterator<Proband> toBeAutoDeletedIt = this.getProbandDao()
				.findToBeAutoDeleted(today, departmentId, null, expiringProbandAutoDeleteReminderPeriod, expiringProbandAutoDeleteReminderPeriodDays, true, false, null).iterator();
		while (toBeAutoDeletedIt.hasNext()) {
			Proband proband = toBeAutoDeletedIt.next();
			if (!ServiceUtil.testNotificationExists(proband.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_PROBAND_AUTO_DELETE, false)) {
				Map messageParameters = CoreUtil.createEmptyTemplateModel();
				messageParameters.put(NotificationMessageTemplateParameters.PROBAND_AUTO_DELETE_DAYS_LEFT, DateCalc.dateDeltaDays(today, proband.getAutoDeleteDeadline()));
				if (notificationDao.addNotification(proband, today, messageParameters) != null) {
					count++;
				}
			}
		}
		VariablePeriod expiringPasswordReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_PASSWORD_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_PASSWORD_REMINDER_PERIOD);
		Long expiringPasswordReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_PASSWORD_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_PASSWORD_REMINDER_PERIOD_DAYS);
		Iterator<Password> expiringPasswordIt = this.getPasswordDao()
				.findExpiring(today, departmentId, AuthenticationType.LOCAL, expiringPasswordReminderPeriod, expiringPasswordReminderPeriodDays, true, false, null).iterator();
		while (expiringPasswordIt.hasNext()) {
			Password password = expiringPasswordIt.next();
			if (!ServiceUtil.testNotificationExists(password.getNotifications(), org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_PASSWORD, false)) {
				Map messageParameters = CoreUtil.createEmptyTemplateModel();
				messageParameters.put(NotificationMessageTemplateParameters.PASSWORD_EXPIRATION_DAYS_LEFT,
						DateCalc.dateDeltaDays(today, ServiceUtil.getLogonExpirationDate(password)));
				if (notificationDao.addNotification(password, today, messageParameters) != null) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	protected long handleProcessNotifications(Long departmentId, PSFVO psf) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		long totalEmailCount = 0;
		int delayMillis = Settings.getInt(SettingCodes.EMAIL_PROCESS_NOTIFICATIONS_DELAY_MILLIS, Bundle.SETTINGS, DefaultSettings.EMAIL_PROCESS_NOTIFICATIONS_DELAY_MILLIS);
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
		Iterator<NotificationRecipient> recipientsIt = notificationRecipientDao.findPending(null, departmentId, psf).iterator();
		while (recipientsIt.hasNext()) {
			NotificationRecipient recipient = recipientsIt.next();
			Notification notification = recipient.getNotification();
			NotificationType notificationType = notification.getType();
			Staff recipientStaff = recipient.getStaff();
			HashSet<Long> sendDepartmentStaffCategoryIds = createSendDepartmentStaffCategorySet(notificationType);
			boolean sent = false;
			boolean dropped = false;
			int toCount = 0;
			if (sendDepartmentStaffCategoryIds.contains(recipientStaff.getCategory().getId())) {
				try {
					toCount = notificationEmailSender.prepareAndSend(notification, recipientStaff);
					if (toCount > 0) {
						sent = true;
						if (delayMillis > 0) {
							Thread.currentThread();
							Thread.sleep(delayMillis);
						}
					} else {
						dropped = true;
					}
				} catch (Throwable t) {
					recipient.setErrorMessage(t.getMessage());
					if (delayMillis > 0) {
						Thread.currentThread();
						Thread.sleep(delayMillis);
					}
				}
			} else {
				dropped = true;
			}
			recipient.setTimesProcessed(recipient.getTimesProcessed() + 1l);
			recipient.setTimestamp(new Timestamp(System.currentTimeMillis()));
			recipient.setSent(sent);
			recipient.setDropped(dropped);
			notificationRecipientDao.update(recipient);
			notificationRecipientDao.commitAndResumeTransaction();
			// this.updateNotificationRecipient(recipient);
			// sessionFactory.getCurrentSession().getTransaction().commit();
			totalEmailCount += toCount;
		}
		return totalEmailCount;
	}

	@Override
	protected Date handleSubIntervals(Date date, VariablePeriod period,
			Long explicitDays, int n) throws Exception {
		return DateCalc.subIntervals(date, period, explicitDays, n);
	}

	@Override
	protected void handleUnsubscribeProbandEmail(String beacon) throws Exception {
		CommonUtil.parseUUID(beacon);
		// if (auth != null) {
		// authenticator.authenticate(auth, false);
		MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Proband proband = this.getProbandDao().searchUniqueBeacon(beacon);
		if (proband == null) {
			MassMailRecipient recipient = massMailRecipientDao.searchUniqueBeacon(beacon);
			if (recipient != null) {
				massMailRecipientDao.refresh(recipient, LockMode.PESSIMISTIC_WRITE);
				recipient.setUnsubscribed(recipient.getUnsubscribed() + 1l);
				recipient.setUnsubscribedTimestamp(now);
				CoreUtil.modifyVersion(recipient, recipient.getVersion(), recipient.getModifiedTimestamp(), recipient.getModifiedUser());
				massMailRecipientDao.update(recipient);
				proband = recipient.getProband();
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MASS_MAIL_RECIPIENT_BEACON_NOT_FOUND, beacon);
			}
		}
		ProbandContactDetailValueDao probandContactDetailValueDao = this.getProbandContactDetailValueDao();
		if (proband != null) {
			this.getProbandDao().lock(proband, LockMode.PESSIMISTIC_WRITE);
			Iterator<ProbandContactDetailValue> contactsIt = proband.getContactDetailValues().iterator();
			while (contactsIt.hasNext()) {
				ProbandContactDetailValue contact = contactsIt.next();
				ContactDetailType contactType;
				if (!contact.isNa() && contact.isNotify() && (contactType = contact.getType()).isEmail()) {
					contact.setNotify(false);
					CoreUtil.modifyVersion(contact, contact.getVersion(), now, contact.getModifiedUser());
					probandContactDetailValueDao.update(contact);
				}
			}
		}
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setNotificationEmailSender(NotificationEmailSender notificationEmailSender) {
		this.notificationEmailSender = notificationEmailSender;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}