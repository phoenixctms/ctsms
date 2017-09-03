// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::HyperlinkService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.Hyperlink;
import org.phoenixctms.ctsms.domain.HyperlinkCategory;
import org.phoenixctms.ctsms.domain.HyperlinkDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
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
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.HyperlinkInVO;
import org.phoenixctms.ctsms.vo.HyperlinkOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService
 */
public class HyperlinkServiceImpl
extends HyperlinkServiceBase
{

	private final static java.util.regex.Pattern URL_REGEXP = Pattern.compile("^(https?|ftp)://[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].+)?$", Pattern.CASE_INSENSITIVE);

	private static JournalEntry logSystemMessage(Course course, CourseOutVO courseVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null))});
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

	private void checkHyperlinkInput(HyperlinkInVO hyperlink) throws ServiceException {
		HyperlinkCategory category = CheckIDUtil.checkHyperlinkCategoryId(hyperlink.getCategoryId(), this.getHyperlinkCategoryDao());
		switch (category.getModule()) {
			case INVENTORY_HYPERLINK:
				CheckIDUtil.checkInventoryId(hyperlink.getInventoryId(), this.getInventoryDao());
				if (numIdsSet(hyperlink) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.HYPERLINK_INVENTORY_ONLY_ALLOWED);
				}
				break;
			case STAFF_HYPERLINK:
				CheckIDUtil.checkStaffId(hyperlink.getStaffId(), this.getStaffDao());
				if (numIdsSet(hyperlink) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.HYPERLINK_STAFF_ONLY_ALLOWED);
				}
				break;
			case COURSE_HYPERLINK:
				CheckIDUtil.checkCourseId(hyperlink.getCourseId(), this.getCourseDao());
				if (numIdsSet(hyperlink) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.HYPERLINK_COURSE_ONLY_ALLOWED);
				}
				break;
			case TRIAL_HYPERLINK:
				ServiceUtil.checkTrialLocked(CheckIDUtil.checkTrialId(hyperlink.getTrialId(), this.getTrialDao()));
				if (numIdsSet(hyperlink) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.HYPERLINK_TRIAL_ONLY_ALLOWED);
				}
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HYPERLINK_MODULE, DefaultMessages.UNSUPPORTED_HYPERLINK_MODULE,
						new Object[] { category.getModule().toString() }));
		}
		if (!URL_REGEXP.matcher(hyperlink.getUrl()).matches()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_URL, hyperlink.getUrl());
		}
	}

	private void checkHyperlinkModuleId(HyperlinkModule module, Long id) throws ServiceException {
		if (id != null) { // module != null &&
			switch (module) {
				case INVENTORY_HYPERLINK:
					CheckIDUtil.checkInventoryId(id, this.getInventoryDao());
					break;
				case STAFF_HYPERLINK:
					CheckIDUtil.checkStaffId(id, this.getStaffDao());
					break;
				case COURSE_HYPERLINK:
					CheckIDUtil.checkCourseId(id, this.getCourseDao());
					break;
				case TRIAL_HYPERLINK:
					CheckIDUtil.checkTrialId(id, this.getTrialDao());
					break;
				default:
					// not supported for now...
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HYPERLINK_MODULE, DefaultMessages.UNSUPPORTED_HYPERLINK_MODULE,
							new Object[] { module.toString() }));
			}
		}
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService#addHyperlink(HyperlinkInVO)
	 */
	@Override
	protected HyperlinkOutVO handleAddHyperlink(AuthenticationVO auth, HyperlinkInVO newHyperlink)
			throws Exception
			{
		checkHyperlinkInput(newHyperlink);
		HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
		Hyperlink hyperlink = hyperlinkDao.hyperlinkInVOToEntity(newHyperlink);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.modifyVersion(hyperlink, now, user);
		hyperlink = hyperlinkDao.create(hyperlink);
		HyperlinkOutVO result = hyperlinkDao.toHyperlinkOutVO(hyperlink);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (hyperlink.getCategory().getModule()) {
			case INVENTORY_HYPERLINK:
				logSystemMessage(hyperlink.getInventory(), result.getInventory(), now, user, SystemMessageCodes.HYPERLINK_CREATED, result, null, journalEntryDao);
				break;
			case STAFF_HYPERLINK:
				logSystemMessage(hyperlink.getStaff(), result.getStaff(), now, user, SystemMessageCodes.HYPERLINK_CREATED, result, null, journalEntryDao);
				break;
			case COURSE_HYPERLINK:
				logSystemMessage(hyperlink.getCourse(), result.getCourse(), now, user, SystemMessageCodes.HYPERLINK_CREATED, result, null, journalEntryDao);
				break;
			case TRIAL_HYPERLINK:
				logSystemMessage(hyperlink.getTrial(), result.getTrial(), now, user, SystemMessageCodes.HYPERLINK_CREATED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HYPERLINK_MODULE, DefaultMessages.UNSUPPORTED_HYPERLINK_MODULE,
						new Object[] { hyperlink.getCategory().getModule().toString() }));
		}
		return result;
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService#deleteHyperlink(Long)
	 */
	@Override
	protected HyperlinkOutVO handleDeleteHyperlink(AuthenticationVO auth, Long hyperlinkId)
			throws Exception
			{
		HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
		Hyperlink hyperlink = CheckIDUtil.checkHyperlinkId(hyperlinkId, hyperlinkDao);
		HyperlinkOutVO result = hyperlinkDao.toHyperlinkOutVO(hyperlink);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (hyperlink.getCategory().getModule()) {
			case INVENTORY_HYPERLINK:
				Inventory inventory = hyperlink.getInventory();
				inventory.removeHyperlinks(hyperlink);
				hyperlink.setInventory(null);
				hyperlinkDao.remove(hyperlink);
				logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.HYPERLINK_DELETED, result, null, journalEntryDao);
				break;
			case STAFF_HYPERLINK:
				Staff staff = hyperlink.getStaff();
				staff.removeHyperlinks(hyperlink);
				hyperlink.setStaff(null);
				hyperlinkDao.remove(hyperlink);
				logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.HYPERLINK_DELETED, result, null, journalEntryDao);
				break;
			case COURSE_HYPERLINK:
				Course course = hyperlink.getCourse();
				course.removeHyperlinks(hyperlink);
				hyperlink.setCourse(null);
				hyperlinkDao.remove(hyperlink);
				logSystemMessage(course, result.getCourse(), now, user, SystemMessageCodes.HYPERLINK_DELETED, result, null, journalEntryDao);
				break;
			case TRIAL_HYPERLINK:
				Trial trial = hyperlink.getTrial();
				ServiceUtil.checkTrialLocked(trial);
				trial.removeHyperlinks(hyperlink);
				hyperlink.setTrial(null);
				hyperlinkDao.remove(hyperlink);
				logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.HYPERLINK_DELETED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HYPERLINK_MODULE, DefaultMessages.UNSUPPORTED_HYPERLINK_MODULE,
						new Object[] { hyperlink.getCategory().getModule().toString() }));
		}
		return result;
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService#getHyperlink(Long)
	 */
	@Override
	protected HyperlinkOutVO handleGetHyperlink(AuthenticationVO auth, Long hyperlinkId)
			throws Exception
			{
		HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
		Hyperlink hyperlink = CheckIDUtil.checkHyperlinkId(hyperlinkId, hyperlinkDao);
		HyperlinkOutVO result = hyperlinkDao.toHyperlinkOutVO(hyperlink);
		return result;
			}

	@Override
	protected long handleGetHyperlinkCount(AuthenticationVO auth, HyperlinkModule module, Long id, Boolean active)
			throws Exception
			{
		checkHyperlinkModuleId(module, id);
		return this.getHyperlinkDao().getCount(module, id, active);
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService#getHyperlinks(HyperlinkModule, Long, PSFVO)
	 */
	@Override
	protected Collection<HyperlinkOutVO> handleGetHyperlinks(AuthenticationVO auth, HyperlinkModule module, Long id, Boolean active, PSFVO psf)
			throws Exception
			{
		checkHyperlinkModuleId(module, id);
		HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
		Collection hyperlinks = hyperlinkDao.findHyperlinks(module, id, active, psf);
		hyperlinkDao.toHyperlinkOutVOCollection(hyperlinks);
		return hyperlinks;
			}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.HyperlinkService#updateHyperlink(HyperlinkInVO, Long)
	 */
	@Override
	protected HyperlinkOutVO handleUpdateHyperlink(AuthenticationVO auth, HyperlinkInVO modifiedHyperlink)
			throws Exception
			{
		HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
		Hyperlink originalHyperlink = CheckIDUtil.checkHyperlinkId(modifiedHyperlink.getId(), hyperlinkDao);
		checkHyperlinkInput(modifiedHyperlink);
		HyperlinkOutVO original = hyperlinkDao.toHyperlinkOutVO(originalHyperlink);
		hyperlinkDao.evict(originalHyperlink);
		Hyperlink hyperlink = hyperlinkDao.hyperlinkInVOToEntity(modifiedHyperlink);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.modifyVersion(originalHyperlink, hyperlink, now, user);
		hyperlinkDao.update(hyperlink);
		HyperlinkOutVO result = hyperlinkDao.toHyperlinkOutVO(hyperlink);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (hyperlink.getCategory().getModule()) {
			case INVENTORY_HYPERLINK:
				logSystemMessage(hyperlink.getInventory(), result.getInventory(), now, user, SystemMessageCodes.HYPERLINK_UPDATED, result, original, journalEntryDao);
				break;
			case STAFF_HYPERLINK:
				logSystemMessage(hyperlink.getStaff(), result.getStaff(), now, user, SystemMessageCodes.HYPERLINK_UPDATED, result, original, journalEntryDao);
				break;
			case COURSE_HYPERLINK:
				logSystemMessage(hyperlink.getCourse(), result.getCourse(), now, user, SystemMessageCodes.HYPERLINK_UPDATED, result, original, journalEntryDao);
				break;
			case TRIAL_HYPERLINK:
				logSystemMessage(hyperlink.getTrial(), result.getTrial(), now, user, SystemMessageCodes.HYPERLINK_UPDATED, result, original, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_HYPERLINK_MODULE, DefaultMessages.UNSUPPORTED_HYPERLINK_MODULE,
						new Object[] { hyperlink.getCategory().getModule().toString() }));
		}
		return result;
			}

	private int numIdsSet(HyperlinkInVO hyperlink) {
		int result = 0;
		result += (hyperlink.getInventoryId() != null) ? 1 : 0;
		result += (hyperlink.getStaffId() != null) ? 1 : 0;
		result += (hyperlink.getCourseId() != null) ? 1 : 0;
		result += (hyperlink.getTrialId() != null) ? 1 : 0;
		return result;
	}
}