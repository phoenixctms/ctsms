// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::JournalService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.JournalCategory;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.excel.ExcelExporter;
import org.phoenixctms.ctsms.excel.JournalExcelWriter;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.ActivityTagVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.JournalEntryInVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.JournalExcelVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.JournalService
 */
public class JournalServiceImpl
extends JournalServiceBase
{

	private static JournalEntry logSystemMessage(Course course, CourseOutVO courseVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null))});
	}

	private static JournalEntry logSystemMessage(Criteria criteria, CriteriaOutVO criteriaVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(criteria, now, modified, systemMessageCode, new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null))});
	}

	private static JournalEntry logSystemMessage(InputField inputField, InputFieldOutVO inputFieldVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inputField, now, modified, systemMessageCode, new Object[] { CommonUtil.inputFieldOutVOToString(inputFieldVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.INPUT_FIELD_JOURNAL,
		// null))});
	}

	private static JournalEntry logSystemMessage(Inventory inventory, InventoryOutVO inventoryVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, modified, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL,
		// null))});
	}

	private static JournalEntry logSystemMessage(Staff staff, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null))});
	}

	private static JournalEntry logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, UserOutVO userVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.userOutVOToString(userVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null))});
	}

	private void checkJournalEntryInput(JournalEntryInVO journalEntry) throws ServiceException {
		JournalCategory category = CheckIDUtil.checkJournalCategoryId(journalEntry.getCategoryId(), this.getJournalCategoryDao());
		switch (category.getModule()) {
			case INVENTORY_JOURNAL:
				CheckIDUtil.checkInventoryId(journalEntry.getInventoryId(), this.getInventoryDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_INVENTORY_ONLY_ALLOWED);
				}
				break;
			case STAFF_JOURNAL:
				CheckIDUtil.checkStaffId(journalEntry.getStaffId(), this.getStaffDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_STAFF_ONLY_ALLOWED);
				}
				break;
			case COURSE_JOURNAL:
				CheckIDUtil.checkCourseId(journalEntry.getCourseId(), this.getCourseDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_COURSE_ONLY_ALLOWED);
				}
				break;
			case USER_JOURNAL:
				CheckIDUtil.checkUserId(journalEntry.getUserId(), this.getUserDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_USER_ONLY_ALLOWED);
				}
				break;
			case TRIAL_JOURNAL:
				ServiceUtil.checkTrialLocked(CheckIDUtil.checkTrialId(journalEntry.getTrialId(), this.getTrialDao()));
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_TRIAL_ONLY_ALLOWED);
				}
				break;
			case PROBAND_JOURNAL:
				ServiceUtil.checkProbandLocked(CheckIDUtil.checkProbandId(journalEntry.getProbandId(), this.getProbandDao()));
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_PROBAND_ONLY_ALLOWED);
				}
				break;
			case CRITERIA_JOURNAL:
				CheckIDUtil.checkCriteriaId(journalEntry.getCriteriaId(), this.getCriteriaDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_CRITERIA_ONLY_ALLOWED);
				}
				break;
			case INPUT_FIELD_JOURNAL:
				CheckIDUtil.checkInputFieldId(journalEntry.getInputFieldId(), this.getInputFieldDao());
				if (numIdsSet(journalEntry) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.JOURNAL_ENTRY_INPUT_FIELD_ONLY_ALLOWED);
				}
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOURNAL_MODULE, DefaultMessages.UNSUPPORTED_JOURNAL_MODULE, new Object[] { category
						.getModule().toString() }));
		}
	}

	private void checkJournalModuleId(JournalModule module, Long id) throws ServiceException {
		if (id != null) { // module != null
			switch (module) {
				case INVENTORY_JOURNAL:
					CheckIDUtil.checkInventoryId(id, this.getInventoryDao());
					break;
				case STAFF_JOURNAL:
					CheckIDUtil.checkStaffId(id, this.getStaffDao());
					break;
				case COURSE_JOURNAL:
					CheckIDUtil.checkCourseId(id, this.getCourseDao());
					break;
				case USER_JOURNAL:
					CheckIDUtil.checkUserId(id, this.getUserDao());
					break;
				case TRIAL_JOURNAL:
					CheckIDUtil.checkTrialId(id, this.getTrialDao());
					break;
				case PROBAND_JOURNAL:
					CheckIDUtil.checkProbandId(id, this.getProbandDao());
					break;
				case CRITERIA_JOURNAL:
					CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao());
					break;
				case INPUT_FIELD_JOURNAL:
					CheckIDUtil.checkInputFieldId(id, this.getInputFieldDao());
					break;
				default:
					// not supported for now...
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOURNAL_MODULE, DefaultMessages.UNSUPPORTED_JOURNAL_MODULE,
							new Object[] { module.toString() }));
			}
		}
	}

	private Collection<JournalEntryOutVO> getJournalHelper(JournalModule module, Long id, PSFVO psf) throws Exception {
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection journalEntries = journalEntryDao.findJournal(module, id, psf);
		journalEntryDao.toJournalEntryOutVOCollection(journalEntries);
		return journalEntries;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JournalService#addJournalEntry(JournalEntryInVO)
	 */
	@Override
	protected JournalEntryOutVO handleAddJournalEntry(AuthenticationVO auth, JournalEntryInVO newJournalEntry)
			throws Exception
			{
		checkJournalEntryInput(newJournalEntry);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		JournalEntry journalEntry = journalEntryDao.journalEntryInVOToEntity(newJournalEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(journalEntry, now, user);
		journalEntry = journalEntryDao.create(journalEntry);
		return journalEntryDao.toJournalEntryOutVO(journalEntry);
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JournalService#deleteJournalEntry(Long)
	 */
	@Override
	protected JournalEntryOutVO handleDeleteJournalEntry(AuthenticationVO auth, Long journalEntryId)
			throws Exception
			{
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		JournalEntry journalEntry = CheckIDUtil.checkJournalEntryId(journalEntryId, journalEntryDao);
		if (journalEntry.isSystemMessage()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_SYSTEM_MESSAGE);
		}
		JournalEntryOutVO result = journalEntryDao.toJournalEntryOutVO(journalEntry);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_JOURNAL_ENTRY);
		}
		switch (journalEntry.getCategory().getModule()) {
			case INVENTORY_JOURNAL:
				Inventory inventory = journalEntry.getInventory();
				inventory.removeJournalEntries(journalEntry);
				journalEntry.setInventory(null);
				break;
			case STAFF_JOURNAL:
				Staff staff = journalEntry.getStaff();
				staff.removeJournalEntries(journalEntry);
				journalEntry.setStaff(null);
				break;
			case COURSE_JOURNAL:
				Course course = journalEntry.getCourse();
				course.removeJournalEntries(journalEntry);
				journalEntry.setCourse(null);
				break;
			case USER_JOURNAL:
				User user = journalEntry.getUser();
				user.removeJournalEntries(journalEntry);
				journalEntry.setUser(null);
				break;
			case TRIAL_JOURNAL:
				Trial trial = journalEntry.getTrial();
				ServiceUtil.checkTrialLocked(trial);
				trial.removeJournalEntries(journalEntry);
				journalEntry.setTrial(null);
				break;
			case PROBAND_JOURNAL:
				Proband proband = journalEntry.getProband();
				ServiceUtil.checkProbandLocked(proband);
				proband.removeJournalEntries(journalEntry);
				journalEntry.setProband(null);
				break;
			case CRITERIA_JOURNAL:
				Criteria criteria = journalEntry.getCriteria();
				criteria.removeJournalEntries(journalEntry);
				journalEntry.setCriteria(null);
				break;
			case INPUT_FIELD_JOURNAL:
				InputField inputField = journalEntry.getInputField();
				inputField.removeJournalEntries(journalEntry);
				journalEntry.setInputField(null);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOURNAL_MODULE, DefaultMessages.UNSUPPORTED_JOURNAL_MODULE,
						new Object[] { journalEntry.getCategory().getModule().toString() }));
		}
		journalEntryDao.remove(journalEntry);
		return result;
			}

	@Override
	protected JournalExcelVO handleExportJournal(AuthenticationVO auth, JournalModule module, Long id) throws Exception {
		JournalExcelWriter writer = new JournalExcelWriter(module, !CoreUtil.isPassDecryption());
		Inventory inventory = null;
		Staff staff = null;
		Course course = null;
		User user = null;
		Trial trial = null;
		Proband proband = null;
		Criteria criteria = null;
		InputField inputField = null;
		if (id != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					inventory = CheckIDUtil.checkInventoryId(id, this.getInventoryDao());
					writer.setInventory(this.getInventoryDao().toInventoryOutVO(inventory));
					break;
				case STAFF_JOURNAL:
					staff = CheckIDUtil.checkStaffId(id, this.getStaffDao());
					writer.setStaff(this.getStaffDao().toStaffOutVO(staff));
					break;
				case COURSE_JOURNAL:
					course = CheckIDUtil.checkCourseId(id, this.getCourseDao());
					writer.setCourse(this.getCourseDao().toCourseOutVO(course));
					break;
				case USER_JOURNAL:
					user = CheckIDUtil.checkUserId(id, this.getUserDao());
					writer.setUser(this.getUserDao().toUserOutVO(user));
					break;
				case TRIAL_JOURNAL:
					trial = CheckIDUtil.checkTrialId(id, this.getTrialDao());
					writer.setTrial(this.getTrialDao().toTrialOutVO(trial));
					break;
				case PROBAND_JOURNAL:
					proband = CheckIDUtil.checkProbandId(id, this.getProbandDao());
					writer.setProband(this.getProbandDao().toProbandOutVO(proband));
					break;
				case CRITERIA_JOURNAL:
					criteria = CheckIDUtil.checkCriteriaId(id, this.getCriteriaDao());
					writer.setCriteria(this.getCriteriaDao().toCriteriaOutVO(criteria));
					break;
				case INPUT_FIELD_JOURNAL:
					inputField = CheckIDUtil.checkInputFieldId(id, this.getInputFieldDao());
					writer.setInputField(this.getInputFieldDao().toInputFieldOutVO(inputField));
					break;
				default:
					// not supported for now...
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_JOURNAL_MODULE, DefaultMessages.UNSUPPORTED_JOURNAL_MODULE,
							new Object[] { module.toString() }));
			}
		}
		writer.setVOs(getJournalHelper(module, id, null));
		User modified = CoreUtil.getUser();
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(modified));
		(new ExcelExporter(writer, writer)).write();
		JournalExcelVO result = writer.getExcelVO();
		if (id != null) {
			// byte[] documentDataBackup = result.getDocumentDatas();
			// result.setDocumentDatas(null);
			JournalEntryDao journalEntryDao = this.getJournalEntryDao();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			switch (module) {
				case INVENTORY_JOURNAL:
					logSystemMessage(inventory, writer.getInventory(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case STAFF_JOURNAL:
					logSystemMessage(staff, writer.getStaff(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case COURSE_JOURNAL:
					logSystemMessage(course, writer.getCourse(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case USER_JOURNAL:
					logSystemMessage(user, writer.getUser(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case TRIAL_JOURNAL:
					logSystemMessage(trial, writer.getTrial(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case PROBAND_JOURNAL:
					ServiceUtil.logSystemMessage(proband, writer.getProband(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case CRITERIA_JOURNAL:
					logSystemMessage(criteria, writer.getCriteria(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				case INPUT_FIELD_JOURNAL:
					logSystemMessage(inputField, writer.getInputField(), now, modified, SystemMessageCodes.JOURNAL_EXPORTED, result, null, journalEntryDao);
					break;
				default:
			}
			// result.setDocumentDatas(documentDataBackup);
		}
		return result;
	}

	@Override
	protected Collection<JournalEntryOutVO> handleGetActivity(
			AuthenticationVO auth, JournalModule module, Long modifiedUserId,
			DBModule criteriaModule, Long entityDepartmentId, boolean limit, PSFVO psf) throws Exception {
		if (modifiedUserId != null) {
			CheckIDUtil.checkUserId(modifiedUserId, this.getUserDao());
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection journalEntries = journalEntryDao.findActivity(module, modifiedUserId, criteriaModule, entityDepartmentId, limit, psf);
		journalEntryDao.toJournalEntryOutVOCollection(journalEntries);
		return journalEntries;
	}

	@Override
	protected Collection<ActivityTagVO> handleGetActivityTags(
			AuthenticationVO auth, JournalModule module, Long modifiedUserId,
			DBModule criteriaModule, Long entityDepartmentId, Integer maxRecentJournalEntries, boolean limit) throws Exception {
		if (modifiedUserId != null) {
			CheckIDUtil.checkUserId(modifiedUserId, this.getUserDao());
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection<JournalEntry> journalEntries = journalEntryDao.findRecent(module, modifiedUserId, criteriaModule, entityDepartmentId, maxRecentJournalEntries, limit);
		ArrayList<ActivityTagVO> result = new ArrayList<ActivityTagVO>(journalEntries.size());
		Iterator<JournalEntry> it = journalEntries.iterator();
		while (it.hasNext()) {
			JournalEntry journalEntry = it.next();
			ActivityTagVO activityTag = new ActivityTagVO();
			activityTag.setCount(journalEntryDao.getActivityCount(journalEntry.getId(), limit));
			activityTag.setJournalEntry(journalEntryDao.toJournalEntryOutVO(journalEntry));
			result.add(activityTag);
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JournalService#getJournal(JournalModule, Long, PSFVO)
	 */
	@Override
	protected Collection<JournalEntryOutVO> handleGetJournal(AuthenticationVO auth, JournalModule module, Long id, PSFVO psf)
			throws Exception
			{
		checkJournalModuleId(module, id);
		return getJournalHelper(module, id, psf);
			}

	@Override
	protected long handleGetJournalCount(AuthenticationVO auth, JournalModule module, Long id)
			throws Exception
			{
		checkJournalModuleId(module, id);
		return this.getJournalEntryDao().getCount(module, id);
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JournalService#getJournalEntry(Long)
	 */
	@Override
	protected JournalEntryOutVO handleGetJournalEntry(AuthenticationVO auth, Long journalEntryId)
			throws Exception
			{
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		JournalEntry journalEntry = CheckIDUtil.checkJournalEntryId(journalEntryId, journalEntryDao);
		JournalEntryOutVO result = journalEntryDao.toJournalEntryOutVO(journalEntry);
		return result;
			}

	@Override
	protected Collection<UserOutVO> handleGetJournalUsers(AuthenticationVO auth, JournalModule module, Long id, boolean limit) throws Exception {
		checkJournalModuleId(module, id);
		Collection journalUsers = this.getJournalEntryDao().findJournalUsers(module, id, limit);
		this.getUserDao().toUserOutVOCollection(journalUsers);
		return journalUsers;
	}

	@Override
	protected Collection<JournalEntryOutVO> handleGetRecent(
			AuthenticationVO auth, JournalModule module, Long modifiedUserId,
			DBModule criteriaModule, Long entityDepartmentId, boolean limit, PSFVO psf) throws Exception {
		if (modifiedUserId != null) {
			CheckIDUtil.checkUserId(modifiedUserId, this.getUserDao());
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection journalEntries = journalEntryDao.findRecent(module, modifiedUserId, criteriaModule, entityDepartmentId, limit, psf);
		journalEntryDao.toJournalEntryOutVOCollection(journalEntries);
		return journalEntries;
	}

	@Override
	protected Collection<JournalEntryOutVO> handleGetRecent(
			AuthenticationVO auth, JournalModule module, Long modifiedUserId,
			DBModule criteriaModule, Long entityDepartmentId, Integer maxRecentJournalEntries, boolean limit) throws Exception {
		if (modifiedUserId != null) {
			CheckIDUtil.checkUserId(modifiedUserId, this.getUserDao());
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection journalEntries = journalEntryDao.findRecent(module, modifiedUserId, criteriaModule, entityDepartmentId, maxRecentJournalEntries, limit);
		journalEntryDao.toJournalEntryOutVOCollection(journalEntries);
		return journalEntries;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.JournalService#updateJournalEntry(JournalEntryInVO)
	 */
	@Override
	protected JournalEntryOutVO handleUpdateJournalEntry(AuthenticationVO auth, JournalEntryInVO modifiedJournalEntry)
			throws Exception
			{
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		JournalEntry originalJournalEntry = CheckIDUtil.checkJournalEntryId(modifiedJournalEntry.getId(), journalEntryDao);
		if (originalJournalEntry.isSystemMessage()) { // here first to avoid nullptrexc
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_UPDATE_SYSTEM_MESSAGE);
		}
		checkJournalEntryInput(modifiedJournalEntry);
		if (!journalEntryDao.toJournalEntryOutVO(originalJournalEntry).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_JOURNAL_ENTRY);
		}
		journalEntryDao.evict(originalJournalEntry);
		JournalEntry journalEntry = journalEntryDao.journalEntryInVOToEntity(modifiedJournalEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalJournalEntry, journalEntry, now, user);
		journalEntryDao.update(journalEntry);
		return journalEntryDao.toJournalEntryOutVO(journalEntry);
			}

	private int numIdsSet(JournalEntryInVO journalEntry) {
		int result = 0;
		result += (journalEntry.getInventoryId() != null) ? 1 : 0;
		result += (journalEntry.getStaffId() != null) ? 1 : 0;
		result += (journalEntry.getCourseId() != null) ? 1 : 0;
		result += (journalEntry.getUserId() != null) ? 1 : 0;
		result += (journalEntry.getTrialId() != null) ? 1 : 0;
		result += (journalEntry.getProbandId() != null) ? 1 : 0;
		result += (journalEntry.getCriteriaId() != null) ? 1 : 0;
		return result;
	}
}