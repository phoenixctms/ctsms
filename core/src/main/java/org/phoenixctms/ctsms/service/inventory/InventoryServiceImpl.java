// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::inventory::InventoryService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.inventory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.InventoryBookingCollisionFinder;
import org.phoenixctms.ctsms.adapt.InventoryStatusEntryCollisionFinder;
import org.phoenixctms.ctsms.adapt.InventoryTagAdapter;
import org.phoenixctms.ctsms.adapt.ReminderEntityAdapter;
import org.phoenixctms.ctsms.compare.InventoryBookingIntervalComparator;
import org.phoenixctms.ctsms.compare.InventoryStatusEntryIntervalComparator;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntryDao;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.DutyRosterTurnDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.Hyperlink;
import org.phoenixctms.ctsms.domain.HyperlinkDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryBooking;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusEntry;
import org.phoenixctms.ctsms.domain.InventoryStatusEntryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusType;
import org.phoenixctms.ctsms.domain.InventoryTagValue;
import org.phoenixctms.ctsms.domain.InventoryTagValueDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItem;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffStatusEntryDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.excel.ExcelExporter;
import org.phoenixctms.ctsms.excel.InventoryBookingsExcelWriter;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultProbandListStatusReasons;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ProbandListStatusReasonCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.util.date.DateInterval;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingDurationSummaryVO;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingsExcelVO;
import org.phoenixctms.ctsms.vo.InventoryInVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.InventoryTagValueInVO;
import org.phoenixctms.ctsms.vo.InventoryTagValueOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemInVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vocycle.InventoryReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.inventory.InventoryService
 */
public class InventoryServiceImpl
		extends InventoryServiceBase {

	private static JournalEntry logSystemMessage(Course course, InventoryOutVO inventoryVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, user, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Inventory inventory, InventoryOutVO inventoryVO, Timestamp now, User user, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, user, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Proband proband, InventoryOutVO inventoryVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(proband, now, user, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Staff staff, InventoryOutVO inventoryVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, user, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Trial trial, InventoryOutVO inventoryVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, user, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, InventoryOutVO inventoryVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private ProbandListStatusEntryOutVO addInventoryBookingProbandListStatusEntry(String reasonL10nKey, String reasonNoCalendarL10nKey, InventoryBooking inventoryBooking,
			Long probandListStatusTypeId, Timestamp now, User user) throws Exception {
		Trial trial;
		Proband proband;
		ProbandListEntry probandListEntry;
		if (inventoryBooking.getCourse() == null
				&& (trial = inventoryBooking.getTrial()) != null
				&& (proband = inventoryBooking.getProband()) != null
				&& (probandListEntry = this.getProbandListEntryDao().findByTrialProband(trial.getId(), proband.getId())) != null) {
			// ProbandListEntry probandListEntry = it.next();
			Object[] args;
			String l10nKey;
			String comment = CommonUtil.isEmptyString(inventoryBooking.getComment()) ? ""
					: L10nUtil.getProbandListStatusReason(Locales.PROBAND_LIST_STATUS_ENTRY_REASON,
							ProbandListStatusReasonCodes.BOOKING_COMMENT, DefaultProbandListStatusReasons.BOOKING_COMMENT,
							new Object[] { inventoryBooking.getComment() });
			String datetimePattern = Settings.getString(SettingCodes.PROBAND_LIST_STATUS_REASON_DATETIME_PATTERN, Bundle.SETTINGS,
					DefaultSettings.PROBAND_LIST_STATUS_REASON_DATETIME_PATTERN);
			//String datePattern = Settings.getString(SettingCodes.PROBAND_LIST_STATUS_REASON_DATETIME_PATTERN, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_STATUS_REASON_DATETIME_PATTERN);
			if (!CommonUtil.isEmptyString(inventoryBooking.getCalendar())) {
				args = new Object[] {
						CommonUtil.inventoryOutVOToString(this.getInventoryDao().toInventoryOutVO(inventoryBooking.getInventory())),
						CommonUtil.formatDate(inventoryBooking.getStart(), datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON)),
						CommonUtil.formatDate(inventoryBooking.getStop(), datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON)),
						// (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON))).format(inventoryBooking.getStart()),
						// (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON))).format(inventoryBooking.getStop()),
						CommonUtil.staffOutVOToString(this.getStaffDao().toStaffOutVO(inventoryBooking.getOnBehalfOf())),
						inventoryBooking.getCalendar(),
						comment
				};
				l10nKey = reasonL10nKey;
			} else {
				args = new Object[] {
						CommonUtil.inventoryOutVOToString(this.getInventoryDao().toInventoryOutVO(inventoryBooking.getInventory())),
						CommonUtil.formatDate(inventoryBooking.getStart(), datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON)),
						CommonUtil.formatDate(inventoryBooking.getStop(), datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON)),
						// (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON))).format(inventoryBooking.getStart()),
						// (new SimpleDateFormat(datetimePattern, L10nUtil.getLocale(Locales.PROBAND_LIST_STATUS_ENTRY_REASON))).format(inventoryBooking.getStop()),
						CommonUtil.staffOutVOToString(this.getStaffDao().toStaffOutVO(inventoryBooking.getOnBehalfOf())),
						comment
				};
				l10nKey = reasonNoCalendarL10nKey;
			}
			return ServiceUtil.addProbandListStatusEntry(probandListEntry, null, l10nKey, args, now, probandListStatusTypeId, now, user,
					this.getProbandDao(),
					this.getProbandListEntryDao(),
					this.getProbandListStatusEntryDao(),
					this.getProbandListStatusTypeDao(),
					this.getTrialDao(),
					this.getMassMailDao(),
					this.getMassMailRecipientDao(),
					this.getJournalEntryDao());
		}
		return null;
	}

	private void checkInventoryBookingInput(InventoryBookingInVO inventoryBookingIn) throws ServiceException {
		// referential checks
		Inventory inventory = CheckIDUtil.checkInventoryId(inventoryBookingIn.getInventoryId(), this.getInventoryDao());
		Long behalfOfId = inventoryBookingIn.getOnBehalfOfId();
		if (this.getStaffDao().load(behalfOfId) == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_INVALID_BEHALF_OF_STAFF_ID, behalfOfId == null ? null : behalfOfId.toString());
		}
		Course course = null;
		if (inventoryBookingIn.getCourseId() != null) {
			course = CheckIDUtil.checkCourseId(inventoryBookingIn.getCourseId(), this.getCourseDao());
		}
		Proband proband = null;
		if (inventoryBookingIn.getProbandId() != null) {
			proband = CheckIDUtil.checkProbandId(inventoryBookingIn.getProbandId(), this.getProbandDao());
			ServiceUtil.checkProbandLocked(proband);
		}
		Trial trial = null;
		if (inventoryBookingIn.getTrialId() != null) {
			trial = CheckIDUtil.checkTrialId(inventoryBookingIn.getTrialId(), this.getTrialDao());
			ServiceUtil.checkTrialLocked(trial);
		}
		// other input checks
		if (!inventory.isBookable()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_INVENTORY_NOT_BOOKABLE);
		}
		if (course == null && proband == null && trial == null && CommonUtil.isEmptyString(inventoryBookingIn.getComment())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_COURSE_TRIAL_PROBAND_OR_COMMENT_REQUIRED);
		}
		if (inventoryBookingIn.getStop() != null && inventoryBookingIn.getStop().compareTo(inventoryBookingIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_END_TIMESTAMP_LESS_THAN_OR_EQUAL_TO_START_TIMESTAMP);
		}
		if ((new InventoryBookingCollisionFinder(this.getInventoryDao(), this.getInventoryBookingDao())).collides(inventoryBookingIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_MAX_OVERLAPPING_EXCEEDED, inventory.getMaxOverlappingBookings());
		}
	}

	private void checkInventoryInput(InventoryInVO inventoryIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkDepartmentId(inventoryIn.getDepartmentId(), this.getDepartmentDao());
		CheckIDUtil.checkInventoryCategoryId(inventoryIn.getCategoryId(), this.getInventoryCategoryDao());
		if (inventoryIn.getParentId() != null) {
			if (this.getInventoryDao().load(inventoryIn.getParentId(), LockMode.PESSIMISTIC_WRITE) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PARENT_INVENTORY_ID, inventoryIn.getParentId().toString());
			}
		}
		if (inventoryIn.getOwnerId() != null) {
			if (this.getStaffDao().load(inventoryIn.getOwnerId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_OWNER_STAFF_ID, inventoryIn.getOwnerId().toString());
			}
		}
		// other input checks
		if (inventoryIn.getPieces() < 1) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PIECES_LESS_THAN_ONE);
		}
		if (inventoryIn.getBookable()) {
			if (inventoryIn.getMaxOverlappingBookings() < 1) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_OVERLAPPING_BOOKINGS_LESS_THAN_ONE);
			}
		} else {
			if (inventoryIn.getMaxOverlappingBookings() != 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_OVERLAPPING_BOOKINGS_NOT_EQUAL_TO_ZERO);
			}
		}
	}

	private void checkInventoryLoop(Inventory inventory) throws ServiceException {
		(new InventoryReflexionGraph(this.getInventoryDao())).checkGraphLoop(inventory, true, false);
	}

	private void checkInventoryStatusEntryInput(InventoryStatusEntryInVO statusEntryIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkInventoryId(statusEntryIn.getInventoryId(), this.getInventoryDao());
		if (statusEntryIn.getOriginatorId() != null) {
			CheckIDUtil.checkStaffId(statusEntryIn.getOriginatorId(), this.getStaffDao());
		}
		if (statusEntryIn.getAddresseeId() != null) {
			CheckIDUtil.checkStaffId(statusEntryIn.getAddresseeId(), this.getStaffDao());
		}
		InventoryStatusType statusType = CheckIDUtil.checkInventoryStatusTypeId(statusEntryIn.getTypeId(), this.getInventoryStatusTypeDao());
		// other input checks
		if (statusType.isOriginatorRequired() && statusEntryIn.getOriginatorId() == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_STATUS_ENTRY_ORIGINATOR_REQUIRED,
					L10nUtil.getStaffStatusTypeName(Locales.USER, L10nUtil.getInventoryStatusTypeName(Locales.USER, statusType.getNameL10nKey())));
		}
		if (statusType.isAddresseeRequired() && statusEntryIn.getAddresseeId() == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_STATUS_ENTRY_ADDRESSEE_REQUIRED,
					L10nUtil.getStaffStatusTypeName(Locales.USER, L10nUtil.getInventoryStatusTypeName(Locales.USER, statusType.getNameL10nKey())));
		}
		if (statusEntryIn.getStop() != null && statusEntryIn.getStop().compareTo(statusEntryIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_STATUS_ENTRY_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		// should we allow concurrent status entries?:
		if ((new InventoryStatusEntryCollisionFinder(this.getInventoryDao(), this.getInventoryStatusEntryDao())).collides(statusEntryIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_STATUS_ENTRY_OVERLAPPING);
		}
	}

	private void checkInventoryTagValueInput(InventoryTagValueInVO tagValueIn) throws ServiceException {
		(new InventoryTagAdapter(this.getInventoryDao(), this.getInventoryTagDao())).checkTagValueInput(tagValueIn);
	}

	private void checkMaintenanceScheduleItemInput(MaintenanceScheduleItemInVO maintenanceScheduleItemIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkInventoryId(maintenanceScheduleItemIn.getInventoryId(), this.getInventoryDao());
		CheckIDUtil.checkMaintenanceTypeId(maintenanceScheduleItemIn.getTypeId(), this.getMaintenanceTypeDao());
		if (maintenanceScheduleItemIn.getCompanyContactId() != null) {
			if (this.getStaffDao().load(maintenanceScheduleItemIn.getCompanyContactId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_INVALID_COMPANY_CONTACT_STAFF_ID, maintenanceScheduleItemIn
						.getCompanyContactId().toString());
			}
		}
		if (maintenanceScheduleItemIn.getResponsiblePersonId() != null) {
			checkResponsiblePersonId(maintenanceScheduleItemIn.getResponsiblePersonId(), this.getStaffDao());
		}
		// other input checks
		if (maintenanceScheduleItemIn.getRecurring()) {
			if (maintenanceScheduleItemIn.getRecurrencePeriod() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_RECURRENCE_PERIOD_REQUIRED);
			} else if (VariablePeriod.EXPLICIT.equals(maintenanceScheduleItemIn.getRecurrencePeriod())) {
				if (maintenanceScheduleItemIn.getRecurrencePeriodDays() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_RECURRENCE_PERIOD_EXPLICIT_DAYS_REQUIRED);
				} else if (maintenanceScheduleItemIn.getRecurrencePeriodDays() < 1) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_RECURRENCE_PERIOD_EXPLICIT_DAYS_LESS_THAN_ONE);
				}
			}
		}
		ServiceUtil.checkReminderPeriod(maintenanceScheduleItemIn.getReminderPeriod(), maintenanceScheduleItemIn.getReminderPeriodDays());
		if (maintenanceScheduleItemIn.getRecurring()
				&& DateCalc.compareVariablePeriodMinDays(maintenanceScheduleItemIn.getReminderPeriod(), maintenanceScheduleItemIn.getReminderPeriodDays(),
						maintenanceScheduleItemIn.getRecurrencePeriod(), maintenanceScheduleItemIn.getRecurrencePeriodDays()) >= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_REMINDER_PERIOD_GREATER_THAN_OR_EQUAL_TO_RECURRENCE_PERIOD);
		}
		if (maintenanceScheduleItemIn.isNotify() && maintenanceScheduleItemIn.getResponsiblePersonId() == null) {
			// reminder without person to email makes no sense... we don't want to send it to the modifying user
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_RESPONSIBLE_PERSON_REQUIRED);
		}
		if (maintenanceScheduleItemIn.getCharge() < 0.0f) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_CHARGE_NEGATIVE);
		}
	}

	private Staff checkResponsiblePersonId(Long responsiblePersonId, StaffDao staffDao) throws ServiceException {
		Staff responsiblePerson = staffDao.load(responsiblePersonId);
		if (responsiblePerson == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MAINTENANCE_SCHEDULE_ITEM_INVALID_RESPONSIBLE_PERSON_STAFF_ID, responsiblePersonId == null ? null
					: responsiblePersonId.toString());
		}
		return responsiblePerson;
	}

	private void deleteInventoryHelper(Inventory inventory, boolean deleteCascade, User user, Timestamp now) throws Exception {
		InventoryDao inventoryDao = this.getInventoryDao();
		InventoryOutVO result = inventoryDao.toInventoryOutVO(inventory);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (deleteCascade) {
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<InventoryBooking> bookingsIt = inventory.getBookings().iterator();
			while (bookingsIt.hasNext()) {
				InventoryBooking booking = bookingsIt.next();
				InventoryBookingOutVO bookingVO = inventoryBookingDao.toInventoryBookingOutVO(booking);
				Trial trial = booking.getTrial();
				Course course = booking.getCourse();
				Proband proband = booking.getProband();
				Staff onBehalfOf = booking.getOnBehalfOf();
				logSystemMessage(onBehalfOf, result, now, user, SystemMessageCodes.INVENTORY_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
				onBehalfOf.removeInventoryBookings(booking);
				booking.setOnBehalfOf(null);
				if (trial != null) {
					ServiceUtil.checkTrialLocked(trial);
					logSystemMessage(trial, result, now, user, SystemMessageCodes.INVENTORY_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					trial.removeInventoryBookings(booking);
					booking.setTrial(null);
				}
				if (course != null) {
					logSystemMessage(course, result, now, user, SystemMessageCodes.INVENTORY_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					course.removeInventoryBookings(booking);
					booking.setCourse(null);
				}
				if (proband != null) {
					ServiceUtil.checkProbandLocked(proband);
					logSystemMessage(proband, result, now, user, SystemMessageCodes.INVENTORY_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					proband.removeInventoryBookings(booking);
					booking.setProband(null);
				}
				booking.setInventory(null);
				ServiceUtil.removeNotifications(booking.getNotifications(), notificationDao, notificationRecipientDao);
				inventoryBookingDao.remove(booking);
			}
			inventory.getBookings().clear();
			MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
			Iterator<MaintenanceScheduleItem> scheduleItemsIt = inventory.getMaintenanceScheduleItems().iterator();
			while (scheduleItemsIt.hasNext()) {
				MaintenanceScheduleItem maintenanceItem = scheduleItemsIt.next();
				MaintenanceScheduleItemOutVO maintenanceItemVO = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceItem);
				Staff companyContact = maintenanceItem.getCompanyContact();
				Staff responsiblePerson = maintenanceItem.getResponsiblePerson();
				if (companyContact != null) {
					logSystemMessage(companyContact, result, now, user, SystemMessageCodes.INVENTORY_DELETED_MAINTENANCE_ITEM_DELETED, maintenanceItemVO, null, journalEntryDao);
					companyContact.removeCompanyContactMaintenanceItems(maintenanceItem);
					maintenanceItem.setCompanyContact(null);
				}
				if (responsiblePerson != null) {
					logSystemMessage(responsiblePerson, result, now, user, SystemMessageCodes.INVENTORY_DELETED_MAINTENANCE_ITEM_DELETED, maintenanceItemVO, null, journalEntryDao);
					responsiblePerson.removeResponsiblePersonMaintenanceItems(maintenanceItem);
					maintenanceItem.setResponsiblePerson(null);
				}
				maintenanceItem.setInventory(null);
				ServiceUtil.removeNotifications(maintenanceItem.getNotifications(), notificationDao, notificationRecipientDao);
				maintenanceScheduleItemDao.remove(maintenanceItem);
			}
			inventory.getMaintenanceScheduleItems().clear();
			InventoryStatusEntryDao inventoryStatusEntryDao = this.getInventoryStatusEntryDao();
			Iterator<InventoryStatusEntry> statusEntriesIt = inventory.getStatusEntries().iterator();
			while (statusEntriesIt.hasNext()) {
				InventoryStatusEntry statusEntry = statusEntriesIt.next();
				InventoryStatusEntryOutVO statusEntryVO = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(statusEntry);
				Staff originator = statusEntry.getOriginator();
				if (originator != null) {
					logSystemMessage(originator, result, now, user, SystemMessageCodes.INVENTORY_DELETED_STATUS_ENTRY_DELETED, statusEntryVO, null, journalEntryDao);
					originator.removeLoans(statusEntry);
					statusEntry.setOriginator(null);
				}
				Staff addressee = statusEntry.getAddressee();
				if (addressee != null) {
					logSystemMessage(addressee, result, now, user, SystemMessageCodes.INVENTORY_DELETED_STATUS_ENTRY_DELETED, statusEntryVO, null, journalEntryDao);
					addressee.removeLendings(statusEntry);
					statusEntry.setAddressee(null);
				}
				statusEntry.setInventory(null);
				ServiceUtil.removeNotifications(statusEntry.getNotifications(), notificationDao, notificationRecipientDao);
				inventoryStatusEntryDao.remove(statusEntry);
			}
			inventory.getStatusEntries().clear();
			InventoryTagValueDao inventoryTagValueDao = this.getInventoryTagValueDao();
			Iterator<InventoryTagValue> tagValuesIt = inventory.getTagValues().iterator();
			while (tagValuesIt.hasNext()) {
				InventoryTagValue tagValue = tagValuesIt.next();
				tagValue.setInventory(null);
				inventoryTagValueDao.remove(tagValue);
			}
			inventory.getTagValues().clear();
			HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
			Iterator<Hyperlink> hyperlinksIt = inventory.getHyperlinks().iterator();
			while (hyperlinksIt.hasNext()) {
				Hyperlink hyperlink = hyperlinksIt.next();
				hyperlink.setInventory(null);
				hyperlinkDao.remove(hyperlink);
			}
			inventory.getHyperlinks().clear();
			Iterator<JournalEntry> journalEntriesIt = inventory.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setInventory(null);
				journalEntryDao.remove(journalEntry);
			}
			inventory.getJournalEntries().clear();
			FileDao fileDao = this.getFileDao();
			Iterator<File> filesIt = inventory.getFiles().iterator();
			while (filesIt.hasNext()) {
				File file = filesIt.next();
				file.setInventory(null);
				fileDao.remove(file);
			}
			inventory.getFiles().clear();
		}
		Iterator<Inventory> childrenIt = inventory.getChildren().iterator();
		while (childrenIt.hasNext()) {
			Inventory child = childrenIt.next();
			child.setParent(null);
			CoreUtil.modifyVersion(child, child.getVersion(), now, user);
			inventoryDao.update(child);
			InventoryOutVO childVO = inventoryDao.toInventoryOutVO(child);
		}
		inventory.getChildren().clear();
		Inventory parent = inventory.getParent();
		if (parent != null) {
			parent.removeChildren(inventory);
			inventory.setParent(null);
			inventoryDao.update(parent);
		}
		Staff owner = inventory.getOwner();
		if (owner != null) {
			logSystemMessage(owner, result, now, user, SystemMessageCodes.INVENTORY_DELETED_OWNERSHIP_REMOVED, result, null, journalEntryDao);
			owner.removeInventories(inventory);
			inventory.setOwner(null);
		}
		inventoryDao.remove(inventory);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.InventoryService#addInventoryCategory(InventoryInVO)
	 */
	@Override
	protected InventoryOutVO handleAddInventory(AuthenticationVO auth, InventoryInVO newInventory, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		checkInventoryInput(newInventory);
		InventoryDao inventoryDao = this.getInventoryDao();
		Inventory inventory = inventoryDao.inventoryInVOToEntity(newInventory);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(inventory, now, user);
		inventory = inventoryDao.create(inventory);
		InventoryOutVO result = inventoryDao.toInventoryOutVO(inventory, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventory, result, now, user, SystemMessageCodes.INVENTORY_CREATED, result, null, journalEntryDao);
		Staff owner = inventory.getOwner();
		if (owner != null) {
			logSystemMessage(owner, result, now, user, SystemMessageCodes.INVENTORY_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryBookingOutVO handleAddInventoryBooking(AuthenticationVO auth, InventoryBookingInVO newInventoryBooking, Long probandListStatusTypeId)
			throws Exception {
		checkInventoryBookingInput(newInventoryBooking);
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		InventoryBooking inventoryBooking = inventoryBookingDao.inventoryBookingInVOToEntity(newInventoryBooking);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(inventoryBooking, now, user);
		inventoryBooking = inventoryBookingDao.create(inventoryBooking);
		try {
			addInventoryBookingProbandListStatusEntry(ProbandListStatusReasonCodes.BOOKING_CREATED, ProbandListStatusReasonCodes.BOOKING_CREATED_NO_CALENDAR, inventoryBooking,
					probandListStatusTypeId, now, user);
		} catch (ServiceException e) {
			// already end state
		}
		InventoryBookingOutVO result = inventoryBookingDao.toInventoryBookingOutVO(inventoryBooking);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventoryBooking.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_CREATED, result, null, journalEntryDao);
		logSystemMessage(inventoryBooking.getOnBehalfOf(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_CREATED, result, null, journalEntryDao);
		Trial trial = inventoryBooking.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_CREATED, result, null, journalEntryDao);
		}
		Course course = inventoryBooking.getCourse();
		if (course != null) {
			logSystemMessage(course, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_CREATED, result, null, journalEntryDao);
		}
		Proband proband = inventoryBooking.getProband();
		if (proband != null) {
			logSystemMessage(proband, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryStatusEntryOutVO handleAddInventoryStatusEntry(AuthenticationVO auth, InventoryStatusEntryInVO newInventoryStatusEntry) throws Exception {
		checkInventoryStatusEntryInput(newInventoryStatusEntry);
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		InventoryStatusEntry statusEntry = statusEntryDao.inventoryStatusEntryInVOToEntity(newInventoryStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(statusEntry, now, user);
		statusEntry = statusEntryDao.create(statusEntry);
		notifyInventoryInactive(statusEntry, now);
		InventoryStatusEntryOutVO result = statusEntryDao.toInventoryStatusEntryOutVO(statusEntry);
		logSystemMessage(statusEntry.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_STATUS_ENTRY_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected InventoryTagValueOutVO handleAddInventoryTagValue(AuthenticationVO auth, InventoryTagValueInVO newInventoryTagValue) throws Exception {
		checkInventoryTagValueInput(newInventoryTagValue);
		InventoryTagValueDao tagValueDao = this.getInventoryTagValueDao();
		InventoryTagValue tagValue = tagValueDao.inventoryTagValueInVOToEntity(newInventoryTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(tagValue, now, user);
		tagValue = tagValueDao.create(tagValue);
		InventoryTagValueOutVO result = tagValueDao.toInventoryTagValueOutVO(tagValue);
		logSystemMessage(tagValue.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_TAG_VALUE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MaintenanceScheduleItemOutVO handleAddMaintenanceScheduleItem(AuthenticationVO auth, MaintenanceScheduleItemInVO newMaintenanceScheduleItem)
			throws Exception {
		checkMaintenanceScheduleItemInput(newMaintenanceScheduleItem);
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		MaintenanceScheduleItem maintenanceScheduleItem = maintenanceScheduleItemDao.maintenanceScheduleItemInVOToEntity(newMaintenanceScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(maintenanceScheduleItem, now, user);
		maintenanceScheduleItem.setDismissedTimestamp(now);
		maintenanceScheduleItem = maintenanceScheduleItemDao.create(maintenanceScheduleItem);
		notifyMaintenanceReminder(maintenanceScheduleItem, now);
		MaintenanceScheduleItemOutVO result = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(maintenanceScheduleItem.getInventory(), result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_CREATED, result, null,
				journalEntryDao);
		Staff companyContact = maintenanceScheduleItem.getCompanyContact();
		if (companyContact != null) {
			logSystemMessage(companyContact, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_CREATED, result, null, journalEntryDao);
		}
		Staff responsiblePerson = maintenanceScheduleItem.getResponsiblePerson();
		if (responsiblePerson != null) {
			logSystemMessage(responsiblePerson, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryOutVO handleDeleteInventory(AuthenticationVO auth, Long inventoryId, boolean defer, boolean force, String deferredDeleteReason, Integer maxInstances,
			Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		InventoryDao inventoryDao = this.getInventoryDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InventoryOutVO result;
		if (!force && defer) {
			Inventory originalInventory = CheckIDUtil.checkInventoryId(inventoryId, inventoryDao);
			InventoryOutVO original = inventoryDao.toInventoryOutVO(originalInventory, maxInstances, maxParentDepth, maxChildrenDepth);
			inventoryDao.evict(originalInventory);
			Inventory inventory = CheckIDUtil.checkInventoryId(inventoryId, inventoryDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			inventory.setDeferredDelete(true);
			inventory.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(inventory, originalInventory.getVersion(), now, user); // no opt. locking
			inventoryDao.update(inventory);
			result = inventoryDao.toInventoryOutVO(inventory, maxInstances, maxParentDepth, maxChildrenDepth);
			logSystemMessage(inventory, result, now, user, SystemMessageCodes.INVENTORY_MARKED_FOR_DELETION, result, original, journalEntryDao);
			Staff owner = inventory.getOwner();
			if (owner != null) {
				logSystemMessage(owner, result, now, user, SystemMessageCodes.INVENTORY_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
		} else {
			Inventory inventory = CheckIDUtil.checkInventoryId(inventoryId, inventoryDao);
			result = inventoryDao.toInventoryOutVO(inventory, maxInstances, maxParentDepth, maxChildrenDepth);
			deleteInventoryHelper(inventory, true, user, now);
			logSystemMessage(user, result, now, user, SystemMessageCodes.INVENTORY_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryBookingOutVO handleDeleteInventoryBooking(AuthenticationVO auth, Long inventoryBookingId, Long probandListStatusTypeId)
			throws Exception {
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		InventoryBooking inventoryBooking = CheckIDUtil.checkInventoryBookingId(inventoryBookingId, inventoryBookingDao);
		Inventory inventory = inventoryBooking.getInventory();
		Staff onBehalfOf = inventoryBooking.getOnBehalfOf();
		Proband proband = inventoryBooking.getProband();
		ServiceUtil.checkProbandLocked(proband);
		Trial trial = inventoryBooking.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		Course course = inventoryBooking.getCourse();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		try {
			addInventoryBookingProbandListStatusEntry(ProbandListStatusReasonCodes.BOOKING_DELETED, ProbandListStatusReasonCodes.BOOKING_DELETED_NO_CALENDAR, inventoryBooking,
					probandListStatusTypeId, now, user);
		} catch (ServiceException e) {
			// already end state
		}
		InventoryBookingOutVO result = inventoryBookingDao.toInventoryBookingOutVO(inventoryBooking);
		inventory.removeBookings(inventoryBooking);
		inventoryBooking.setInventory(null);
		onBehalfOf.removeInventoryBookings(inventoryBooking);
		inventoryBooking.setOnBehalfOf(null);
		if (proband != null) {
			proband.removeInventoryBookings(inventoryBooking);
			inventoryBooking.setProband(null);
		}
		if (trial != null) {
			trial.removeInventoryBookings(inventoryBooking);
			inventoryBooking.setTrial(null);
		}
		if (course != null) {
			course.removeInventoryBookings(inventoryBooking);
			inventoryBooking.setCourse(null);
		}
		ServiceUtil.removeNotifications(inventoryBooking.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		inventoryBookingDao.remove(inventoryBooking);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_DELETED, result, null, journalEntryDao);
		logSystemMessage(onBehalfOf, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_DELETED, result, null, journalEntryDao);
		if (proband != null) {
			logSystemMessage(proband, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_DELETED, result, null, journalEntryDao);
		}
		if (trial != null) {
			logSystemMessage(trial, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_DELETED, result, null, journalEntryDao);
		}
		if (course != null) {
			logSystemMessage(course, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryStatusEntryOutVO handleDeleteInventoryStatusEntry(AuthenticationVO auth, Long inventoryStatusEntryId) throws Exception {
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		InventoryStatusEntry statusEntry = CheckIDUtil.checkInventoryStatusEntryId(inventoryStatusEntryId, statusEntryDao);
		Inventory inventory = statusEntry.getInventory();
		Staff originator = statusEntry.getOriginator();
		InventoryStatusEntryOutVO result = statusEntryDao.toInventoryStatusEntryOutVO(statusEntry);
		inventory.removeStatusEntries(statusEntry);
		statusEntry.setInventory(null);
		if (originator != null) {
			originator.removeLoans(statusEntry);
			statusEntry.setOriginator(null);
		}
		ServiceUtil.removeNotifications(statusEntry.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		statusEntryDao.remove(statusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		if (originator != null) {
			logSystemMessage(originator, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryTagValueOutVO handleDeleteInventoryTagValue(AuthenticationVO auth, Long inventoryTagValueId)
			throws Exception {
		InventoryTagValueDao tagValueDao = this.getInventoryTagValueDao();
		InventoryTagValue tagValue = CheckIDUtil.checkInventoryTagValueId(inventoryTagValueId, tagValueDao);
		Inventory inventory = tagValue.getInventory();
		InventoryTagValueOutVO result = tagValueDao.toInventoryTagValueOutVO(tagValue);
		inventory.removeTagValues(tagValue);
		tagValue.setInventory(null);
		tagValueDao.remove(tagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_TAG_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MaintenanceScheduleItemOutVO handleDeleteMaintenanceScheduleItem(AuthenticationVO auth, Long maintenanceScheduleItemId)
			throws Exception {
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		MaintenanceScheduleItem maintenanceScheduleItem = CheckIDUtil.checkMaintenanceScheduleItemId(maintenanceScheduleItemId, maintenanceScheduleItemDao);
		Inventory inventory = maintenanceScheduleItem.getInventory();
		Staff companyContact = maintenanceScheduleItem.getCompanyContact();
		Staff responsiblePerson = maintenanceScheduleItem.getResponsiblePerson();
		MaintenanceScheduleItemOutVO result = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		inventory.removeMaintenanceScheduleItems(maintenanceScheduleItem);
		maintenanceScheduleItem.setInventory(null);
		if (companyContact != null) {
			companyContact.removeCompanyContactMaintenanceItems(maintenanceScheduleItem);
			maintenanceScheduleItem.setCompanyContact(null);
		}
		if (responsiblePerson != null) {
			responsiblePerson.removeResponsiblePersonMaintenanceItems(maintenanceScheduleItem);
			maintenanceScheduleItem.setResponsiblePerson(null);
		}
		ServiceUtil.removeNotifications(maintenanceScheduleItem.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		maintenanceScheduleItemDao.remove(maintenanceScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DELETED, result, null, journalEntryDao);
		if (companyContact != null) {
			logSystemMessage(companyContact, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DELETED, result, null, journalEntryDao);
		}
		if (responsiblePerson != null) {
			logSystemMessage(responsiblePerson, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryBookingsExcelVO handleExportInventoryBookings(AuthenticationVO auth, Long probandDepartmentId, Long courseDepartmentId, Long trialDepartmentId,
			String calendar, Date from, Date to, Boolean isProbandAppointment,
			Boolean isRelevantForProbandAppointments, Boolean isCourseAppointment, Boolean isRelevantForCourseAppointments, Boolean isTrialAppointment,
			Boolean isRelevantForTrialAppointments) throws Exception {
		DepartmentDao departmentDao = this.getDepartmentDao();
		DepartmentVO probandDepartmentVO = null;
		if (probandDepartmentId != null) {
			probandDepartmentVO = departmentDao.toDepartmentVO(CheckIDUtil.checkDepartmentId(probandDepartmentId, departmentDao));
		}
		DepartmentVO courseDepartmentVO = null;
		if (courseDepartmentId != null) {
			courseDepartmentVO = departmentDao.toDepartmentVO(CheckIDUtil.checkDepartmentId(courseDepartmentId, departmentDao));
		}
		DepartmentVO trialDepartmentVO = null;
		if (trialDepartmentId != null) {
			trialDepartmentVO = departmentDao.toDepartmentVO(CheckIDUtil.checkDepartmentId(trialDepartmentId, departmentDao));
		}
		InventoryBookingsExcelWriter writer = new InventoryBookingsExcelWriter(!CoreUtil.isPassDecryption());
		writer.setProbandDepartment(probandDepartmentVO);
		writer.setCourseDepartment(courseDepartmentVO);
		writer.setTrialDepartment(trialDepartmentVO);
		writer.setCalendar(calendar);
		writer.setFrom(from);
		writer.setTo(to);
		//writer.s
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection VOs = inventoryBookingDao.findByAppointmentDepartmentsCalendarInterval(
				probandDepartmentId,
				courseDepartmentId,
				trialDepartmentId,
				calendar, CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to), isProbandAppointment, isRelevantForProbandAppointments, isCourseAppointment,
				isRelevantForCourseAppointments, isTrialAppointment, isRelevantForTrialAppointments);
		inventoryBookingDao.toInventoryBookingOutVOCollection(VOs);
		writer.setVOs(VOs);
		//writer.setDistinctColumnNames(distinctColumnNames);
		//writer.setDistinctFieldRows(distinctFieldRows);
		User user = CoreUtil.getUser();
		// UserOutVO userVO = this.getUserDao().toUserOutVO(user);
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new ExcelExporter(writer, writer)).write();
		InventoryBookingsExcelVO result = writer.getExcelVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(user, null, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.INVENTORY_BOOKINGS_EXPORTED, result,
				null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected InventoryBookingDurationSummaryVO handleGetBookingDurationSummary(
			AuthenticationVO auth, Long inventoryId, String calendar, Date from, Date to)
			throws Exception {
		InventoryBookingDurationSummaryVO result = new InventoryBookingDurationSummaryVO();
		if (inventoryId != null) {
			result.setInventory(this.getInventoryDao().toInventoryOutVO(CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao())));
		}
		result.setStart(from);
		result.setStop(to);
		result.setCalendar(calendar);
		ServiceUtil.populateBookingDurationSummary(inventoryId == null, result, this.getInventoryBookingDao(), this.getInventoryStatusEntryDao());
		return result;
	}

	@Override
	protected Collection<String> handleGetCalendars(AuthenticationVO auth,
			Long inventoryDepartmentId, Long inventoryId, Long probandId, Long courseId, Long trialId,
			String calendarPrefix, Integer limit) throws Exception {
		if (inventoryDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(inventoryDepartmentId, this.getDepartmentDao());
		}
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		return this.getInventoryBookingDao().findCalendars(inventoryDepartmentId, inventoryId, probandId, courseId, trialId, calendarPrefix, limit);
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> handleGetCollidingCourseParticipationStatusEntries(
			AuthenticationVO auth, Long courseInventoryBookingId, Boolean isRelevantForCourseAppointments) throws Exception {
		InventoryBooking courseInventoryBooking = CheckIDUtil.checkInventoryBookingId(courseInventoryBookingId, this.getInventoryBookingDao());
		Collection collidingCourseParticipationStatusEntries;
		Course course = courseInventoryBooking.getCourse();
		if (course != null
				&& (isRelevantForCourseAppointments == null || isRelevantForCourseAppointments.booleanValue() == courseInventoryBooking.getInventory().getCategory()
						.isRelevantForCourseAppointments())) {
			Long courseId = course.getId();
			collidingCourseParticipationStatusEntries = new ArrayList();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
			Iterator<CourseParticipationStatusEntry> participationsIt = courseParticipationStatusEntryDao.findByStaffCourseRelevantForCourseAppointments(null, courseId,
					isRelevantForCourseAppointments).iterator();
			while (participationsIt.hasNext()) {
				CourseParticipationStatusEntry courseParticipationStatusEntry = participationsIt.next();
				Iterator<CourseParticipationStatusEntry> otherCourseParticipationStatusEntriesIt = courseParticipationStatusEntryDao
						.findByStaffCourseRelevantForCourseAppointments(courseParticipationStatusEntry.getStaff().getId(), null, isRelevantForCourseAppointments).iterator();
				while (otherCourseParticipationStatusEntriesIt.hasNext()) {
					CourseParticipationStatusEntry otherCourseParticipationStatusEntry = otherCourseParticipationStatusEntriesIt.next();
					Long otherCourseId = otherCourseParticipationStatusEntry.getCourse().getId();
					if (!courseId.equals(otherCourseId)) {
						if (inventoryBookingDao.getCourseIntervalCount(otherCourseId, courseInventoryBooking.getStart(), courseInventoryBooking.getStop(),
								isRelevantForCourseAppointments) > 0) {
							collidingCourseParticipationStatusEntries.add(otherCourseParticipationStatusEntry);
						}
					}
				}
			}
			courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVOCollection(collidingCourseParticipationStatusEntries);
		} else {
			collidingCourseParticipationStatusEntries = new ArrayList<CourseParticipationStatusEntryOutVO>();
		}
		return collidingCourseParticipationStatusEntries;
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetCollidingDutyRosterTurns(
			AuthenticationVO auth, Long courseInventoryBookingId, Boolean isRelevantForCourseAppointments) throws Exception {
		InventoryBooking courseInventoryBooking = CheckIDUtil.checkInventoryBookingId(courseInventoryBookingId, this.getInventoryBookingDao());
		Course course = courseInventoryBooking.getCourse();
		if (course != null
				&& (isRelevantForCourseAppointments == null || isRelevantForCourseAppointments.booleanValue() == courseInventoryBooking.getInventory().getCategory()
						.isRelevantForCourseAppointments())) {
			Collection collidingDutyRosterTurns = new HashSet(); // new ArrayList();
			DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
			Iterator<CourseParticipationStatusEntry> it = course.getParticipations().iterator();
			while (it.hasNext()) {
				CourseParticipationStatusEntry courseParticipationStatusEntry = it.next();
				collidingDutyRosterTurns.addAll(dutyRosterTurnDao.findByStaffTrialCalendarInterval(courseParticipationStatusEntry.getStaff().getId(), null, null,
						courseInventoryBooking.getStart(), courseInventoryBooking.getStop()));
			}
			dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(collidingDutyRosterTurns);
			return new ArrayList<DutyRosterTurnOutVO>(collidingDutyRosterTurns);
		} else {
			return new ArrayList<DutyRosterTurnOutVO>();
		}
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCollidingInventoryBookings(
			AuthenticationVO auth, Long inventoryStatusEntryId) throws Exception {
		InventoryStatusEntry inventoryStatus = CheckIDUtil.checkInventoryStatusEntryId(inventoryStatusEntryId, this.getInventoryStatusEntryDao());
		Collection collidingInventoryBookings;
		if (!inventoryStatus.getType().isInventoryActive()) {
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			collidingInventoryBookings = inventoryBookingDao.findByInventoryCalendarInterval(inventoryStatus.getInventory().getId(), null, inventoryStatus.getStart(),
					inventoryStatus.getStop());
			inventoryBookingDao.toInventoryBookingOutVOCollection(collidingInventoryBookings);
		} else {
			collidingInventoryBookings = new ArrayList<InventoryBookingOutVO>();
		}
		return collidingInventoryBookings;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetCollidingInventoryStatusEntries(
			AuthenticationVO auth, Long inventoryBookingId) throws Exception {
		InventoryBooking inventoryBooking = CheckIDUtil.checkInventoryBookingId(inventoryBookingId, this.getInventoryBookingDao());
		Collection collidingInventoryStatusEntries;
		InventoryStatusEntryDao inventoryStatusEntryDao = this.getInventoryStatusEntryDao();
		collidingInventoryStatusEntries = inventoryStatusEntryDao.findByInventoryInterval(inventoryBooking.getInventory().getId(), inventoryBooking.getStart(),
				inventoryBooking.getStop(), false, false);
		inventoryStatusEntryDao.toInventoryStatusEntryOutVOCollection(collidingInventoryStatusEntries);
		return collidingInventoryStatusEntries;
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetCollidingProbandStatusEntries(
			AuthenticationVO auth, Long probandInventoryBookingId, Boolean isRelevantForProbandAppointments) throws Exception {
		InventoryBooking probandInventoryBooking = CheckIDUtil.checkInventoryBookingId(probandInventoryBookingId, this.getInventoryBookingDao());
		Collection collidingProbandStatusEntries;
		Proband proband = probandInventoryBooking.getProband();
		if (proband != null
				&& (isRelevantForProbandAppointments == null || isRelevantForProbandAppointments.booleanValue() == probandInventoryBooking.getInventory().getCategory()
						.isRelevantForProbandAppointments())) {
			ProbandStatusEntryDao probandStatusEntryDao = this.getProbandStatusEntryDao();
			collidingProbandStatusEntries = probandStatusEntryDao.findByProbandInterval(proband.getId(), probandInventoryBooking.getStart(), probandInventoryBooking.getStop(),
					false, null);
			probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
		} else {
			collidingProbandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>();
		}
		return collidingProbandStatusEntries;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetCollidingStaffStatusEntries(
			AuthenticationVO auth, Long courseInventoryBookingId, Boolean isRelevantForCourseAppointments) throws Exception {
		InventoryBooking courseInventoryBooking = CheckIDUtil.checkInventoryBookingId(courseInventoryBookingId, this.getInventoryBookingDao());
		Course course = courseInventoryBooking.getCourse();
		if (course != null
				&& (isRelevantForCourseAppointments == null || isRelevantForCourseAppointments.booleanValue() == courseInventoryBooking.getInventory().getCategory()
						.isRelevantForCourseAppointments())) {
			Collection collidingStaffStatusEntries = new HashSet(); // new ArrayList();
			StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
			Iterator<CourseParticipationStatusEntry> it = course.getParticipations().iterator();
			while (it.hasNext()) {
				CourseParticipationStatusEntry courseParticipationStatusEntry = it.next();
				collidingStaffStatusEntries.addAll(staffStatusEntryDao.findByStaffInterval(courseParticipationStatusEntry.getStaff().getId(), courseInventoryBooking.getStart(),
						courseInventoryBooking.getStop(), false, null, false));
			}
			staffStatusEntryDao.toStaffStatusEntryOutVOCollection(collidingStaffStatusEntries);
			return new ArrayList<StaffStatusEntryOutVO>(collidingStaffStatusEntries);
		} else {
			return new ArrayList<StaffStatusEntryOutVO>();
		}
	}

	@Override
	protected InventoryOutVO handleGetInventory(AuthenticationVO auth, Long inventoryId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		InventoryDao inventoryDao = this.getInventoryDao();
		Inventory inventory = CheckIDUtil.checkInventoryId(inventoryId, inventoryDao);
		InventoryOutVO result = inventoryDao.toInventoryOutVO(inventory, maxInstances, maxParentDepth, maxChildrenDepth);
		return result;
	}

	@Override
	protected InventoryBookingOutVO handleGetInventoryBooking(AuthenticationVO auth, Long inventoryBookingId)
			throws Exception {
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		InventoryBooking inventoryBooking = CheckIDUtil.checkInventoryBookingId(inventoryBookingId, inventoryBookingDao);
		InventoryBookingOutVO result = inventoryBookingDao.toInventoryBookingOutVO(inventoryBooking);
		return result;
	}

	@Override
	protected long handleGetInventoryBookingCount(AuthenticationVO auth, Long inventoryId) throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		return this.getInventoryBookingDao().getCount(inventoryId, null, null, null, null);
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetInventoryBookingInterval(AuthenticationVO auth, Long departmentId, Long inventoryCategoryId,
			Long inventoryId, Long onBehalfOfId, Long probandId, Long courseId, Long trialId, String calendar, Date from, Date to, boolean sort) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (inventoryCategoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(inventoryCategoryId, this.getInventoryCategoryDao());
		}
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		if (onBehalfOfId != null && this.getStaffDao().load(onBehalfOfId) == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_BOOKING_INVALID_BEHALF_OF_STAFF_ID, onBehalfOfId == null ? null : onBehalfOfId.toString());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByDepartmentCategoryInventoryOnBehalfOfProbandCourseTrialCalendarInterval(departmentId, inventoryCategoryId,
				inventoryId, onBehalfOfId, probandId, courseId, trialId, calendar, CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to));
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		if (sort) {
			inventoryBookings = new ArrayList(inventoryBookings);
			Collections.sort((ArrayList) inventoryBookings, new InventoryBookingIntervalComparator(false));
		}
		return inventoryBookings;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetInventoryBookingList(AuthenticationVO auth, Long inventoryId, PSFVO psf)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByInventory(inventoryId, psf);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		return inventoryBookings;
	}

	@Override
	protected Collection<InventoryOutVO> handleGetInventoryList(AuthenticationVO auth, Long inventoryId, Long departmentId, Integer maxInstances, PSFVO psf) throws Exception {
		InventoryDao inventoryDao = this.getInventoryDao();
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, inventoryDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection inventories = inventoryDao.findByIdDepartment(inventoryId, departmentId, psf);
		ArrayList<InventoryOutVO> result = new ArrayList<InventoryOutVO>(inventories.size());
		Iterator<Inventory> inventoriesIt = inventories.iterator();
		while (inventoriesIt.hasNext()) {
			result.add(inventoryDao.toInventoryOutVO(inventoriesIt.next(), maxInstances));
		}
		return result;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetInventoryStatus(
			AuthenticationVO auth, Date now, Long inventoryId, Long departmentId,
			Long inventoryCategoryId, Boolean inventoryActive, Boolean hideAvailability, PSFVO psf)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (inventoryCategoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(inventoryCategoryId, this.getInventoryCategoryDao());
		}
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		Collection inventoryStatusEntries = statusEntryDao.findInventoryStatus(CommonUtil.dateToTimestamp(now), inventoryId, departmentId, inventoryCategoryId, inventoryActive,
				hideAvailability, psf);
		statusEntryDao.toInventoryStatusEntryOutVOCollection(inventoryStatusEntries);
		return inventoryStatusEntries;
	}

	@Override
	protected InventoryStatusEntryOutVO handleGetInventoryStatusEntry(AuthenticationVO auth, Long inventoryStatusEntryId) throws Exception {
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		InventoryStatusEntry statusEntry = CheckIDUtil.checkInventoryStatusEntryId(inventoryStatusEntryId, statusEntryDao);
		InventoryStatusEntryOutVO result = statusEntryDao.toInventoryStatusEntryOutVO(statusEntry);
		return result;
	}

	@Override
	protected long handleGetInventoryStatusEntryCount(AuthenticationVO auth, Long inventoryId) throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		return this.getInventoryStatusEntryDao().getCount(inventoryId);
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetInventoryStatusEntryInterval(AuthenticationVO auth, Long departmentId, Long inventoryCategoryId,
			Long statusTypeId, Boolean hideAvailability, Date from, Date to, boolean sort) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (inventoryCategoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(inventoryCategoryId, this.getInventoryCategoryDao());
		}
		if (statusTypeId != null) {
			CheckIDUtil.checkInventoryStatusTypeId(statusTypeId, this.getInventoryStatusTypeDao());
		}
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		Collection inventoryStatusEntries = statusEntryDao.findByDepartmentCategoryInterval(departmentId, inventoryCategoryId, CommonUtil.dateToTimestamp(from),
				CommonUtil.dateToTimestamp(to), statusTypeId, null, null, hideAvailability); // false,true);
		statusEntryDao.toInventoryStatusEntryOutVOCollection(inventoryStatusEntries);
		if (sort) {
			inventoryStatusEntries = new ArrayList(inventoryStatusEntries);
			Collections.sort((ArrayList) inventoryStatusEntries, new InventoryStatusEntryIntervalComparator(false));
		}
		return inventoryStatusEntries;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetInventoryStatusEntryList(AuthenticationVO auth, Long inventoryId, PSFVO psf) throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		Collection inventoryStatusEntries = statusEntryDao.findByInventory(inventoryId, psf);
		statusEntryDao.toInventoryStatusEntryOutVOCollection(inventoryStatusEntries);
		return inventoryStatusEntries;
	}

	@Override
	protected InventoryTagValueOutVO handleGetInventoryTagValue(AuthenticationVO auth, Long inventoryTagValueId)
			throws Exception {
		InventoryTagValueDao tagValueDao = this.getInventoryTagValueDao();
		InventoryTagValue tagValue = CheckIDUtil.checkInventoryTagValueId(inventoryTagValueId, tagValueDao);
		InventoryTagValueOutVO result = tagValueDao.toInventoryTagValueOutVO(tagValue);
		return result;
	}

	@Override
	protected long handleGetInventoryTagValueCount(AuthenticationVO auth, Long inventoryId)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		return this.getInventoryTagValueDao().getCount(inventoryId);
	}

	@Override
	protected Collection<InventoryTagValueOutVO> handleGetInventoryTagValueList(AuthenticationVO auth, Long inventoryId, PSFVO psf)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		InventoryTagValueDao tagValueDao = this.getInventoryTagValueDao();
		Collection inventoryTagValues = tagValueDao.findByInventory(inventoryId, psf);
		tagValueDao.toInventoryTagValueOutVOCollection(inventoryTagValues);
		return inventoryTagValues;
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> handleGetMaintenanceInterval(
			AuthenticationVO auth, Long inventoryId, Long departmentId,
			Long inventoryCategoryId, Long responsiblePersonId, Boolean notify,
			Date from, Date to) throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (inventoryCategoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(inventoryCategoryId, this.getInventoryCategoryDao());
		}
		if (responsiblePersonId != null) {
			checkResponsiblePersonId(responsiblePersonId, this.getStaffDao());
		}
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		Collection maintenanceScheduleItems = maintenanceScheduleItemDao.findMaintenanceInterval(inventoryId, departmentId, inventoryCategoryId, responsiblePersonId, notify,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to));
		ArrayList<MaintenanceScheduleItemOutVO> result = new ArrayList<MaintenanceScheduleItemOutVO>(maintenanceScheduleItems.size());
		Iterator maintenanceScheduleItemsIt = maintenanceScheduleItems.iterator();
		while (maintenanceScheduleItemsIt.hasNext()) {
			MaintenanceScheduleItem maintenanceScheduleItem = (MaintenanceScheduleItem) maintenanceScheduleItemsIt.next();
			MaintenanceScheduleItemOutVO maintenanceScheduleItemVO = new MaintenanceScheduleItemOutVO();
			ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(maintenanceScheduleItem);
			maintenanceScheduleItemVO.setReminderStart(reminderItem.getReminderStart(from, true, null, null));
			maintenanceScheduleItemVO.setNextRecurrence(reminderItem.getNextRecurrence(from, true));
			maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem, maintenanceScheduleItemVO);
			result.add(maintenanceScheduleItemVO);
		}
		return result;
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> handleGetMaintenanceSchedule(
			AuthenticationVO auth, Date today, Long inventoryId, Long departmentId,
			Long inventoryCategoryId, Long responsiblePersonId,
			Boolean reminder, PSFVO psf) throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (inventoryCategoryId != null) {
			CheckIDUtil.checkInventoryCategoryId(inventoryCategoryId, this.getInventoryCategoryDao());
		}
		if (responsiblePersonId != null) {
			checkResponsiblePersonId(responsiblePersonId, this.getStaffDao());
		}
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		Collection maintenanceScheduleItems = maintenanceScheduleItemDao.findMaintenanceSchedule(today, inventoryId, departmentId, inventoryCategoryId, responsiblePersonId,
				reminder, true, psf);
		maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVOCollection(maintenanceScheduleItems);
		return maintenanceScheduleItems;
	}

	@Override
	protected MaintenanceScheduleItemOutVO handleGetMaintenanceScheduleItem(AuthenticationVO auth, Long maintenanceScheduleItemId)
			throws Exception {
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		MaintenanceScheduleItem maintenanceScheduleItem = CheckIDUtil.checkMaintenanceScheduleItemId(maintenanceScheduleItemId, maintenanceScheduleItemDao);
		MaintenanceScheduleItemOutVO result = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		return result;
	}

	@Override
	protected long handleGetMaintenanceScheduleItemCount(AuthenticationVO auth, Long inventoryId, Boolean active)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		return this.getMaintenanceScheduleItemDao().getCount(inventoryId, active);
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> handleGetMaintenanceScheduleItemList(AuthenticationVO auth, Long inventoryId, Boolean active, PSFVO psf)
			throws Exception {
		if (inventoryId != null) {
			CheckIDUtil.checkInventoryId(inventoryId, this.getInventoryDao());
		}
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		Collection maintenanceScheduleItems = maintenanceScheduleItemDao.findByInventoryActive(inventoryId, active, psf);
		maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVOCollection(maintenanceScheduleItems);
		return maintenanceScheduleItems;
	}

	@Override
	protected MaintenanceScheduleItemOutVO handleSetMaintenaceScheduleItemDismissed(
			AuthenticationVO auth, Long maintenanceScheduleItemId, Long version, boolean dismissed) throws Exception {
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		MaintenanceScheduleItem maintenanceScheduleItem = CheckIDUtil.checkMaintenanceScheduleItemId(maintenanceScheduleItemId, maintenanceScheduleItemDao);
		MaintenanceScheduleItemOutVO original = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(maintenanceScheduleItem, version.longValue(), now, user);
		maintenanceScheduleItem.setDismissed(dismissed);
		maintenanceScheduleItem.setDismissedTimestamp(now);
		maintenanceScheduleItemDao.update(maintenanceScheduleItem);
		notifyMaintenanceReminder(maintenanceScheduleItem, now);
		MaintenanceScheduleItemOutVO result = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(maintenanceScheduleItem.getInventory(), result.getInventory(), now, user, dismissed ? SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DISMISSED_SET
				: SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DISMISSED_UNSET, result, original, journalEntryDao);
		Staff responsiblePerson = maintenanceScheduleItem.getResponsiblePerson();
		if (responsiblePerson != null) {
			logSystemMessage(responsiblePerson, result.getInventory(), now, user, dismissed ? SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DISMISSED_SET
					: SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_DISMISSED_UNSET, result, original, journalEntryDao);
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.InventoryService#updateInventoryCategory(InventoryInVO)
	 */
	@Override
	protected InventoryOutVO handleUpdateInventory(AuthenticationVO auth, InventoryInVO modifiedInventory, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		InventoryDao inventoryDao = this.getInventoryDao();
		Inventory originalInventory = CheckIDUtil.checkInventoryId(modifiedInventory.getId(), inventoryDao, LockMode.PESSIMISTIC_WRITE);
		checkInventoryInput(modifiedInventory);
		InventoryOutVO original = inventoryDao.toInventoryOutVO(originalInventory, maxInstances, maxParentDepth, maxChildrenDepth);
		inventoryDao.evict(originalInventory);
		Inventory inventory = inventoryDao.inventoryInVOToEntity(modifiedInventory);
		checkInventoryLoop(inventory);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalInventory, inventory, now, user);
		inventoryDao.update(inventory);
		InventoryOutVO result = inventoryDao.toInventoryOutVO(inventory, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventory, result, now, user, SystemMessageCodes.INVENTORY_UPDATED, result, original, journalEntryDao);
		Staff owner = inventory.getOwner();
		if (owner != null) {
			logSystemMessage(owner, result, now, user, SystemMessageCodes.INVENTORY_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryBookingOutVO handleUpdateInventoryBooking(AuthenticationVO auth, InventoryBookingInVO modifiedInventoryBooking, Long probandListStatusTypeId)
			throws Exception {
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		InventoryBooking originalInventoryBooking = CheckIDUtil.checkInventoryBookingId(modifiedInventoryBooking.getId(), inventoryBookingDao);
		checkInventoryBookingInput(modifiedInventoryBooking);
		InventoryBookingOutVO original = inventoryBookingDao.toInventoryBookingOutVO(originalInventoryBooking);
		inventoryBookingDao.evict(originalInventoryBooking);
		InventoryBooking inventoryBooking = inventoryBookingDao.inventoryBookingInVOToEntity(modifiedInventoryBooking);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalInventoryBooking, inventoryBooking, now, user);
		inventoryBookingDao.update(inventoryBooking);
		try {
			addInventoryBookingProbandListStatusEntry(ProbandListStatusReasonCodes.BOOKING_UPDATED, ProbandListStatusReasonCodes.BOOKING_UPDATED_NO_CALENDAR, inventoryBooking,
					probandListStatusTypeId, now, user);
		} catch (ServiceException e) {
			// already end state
		}
		InventoryBookingOutVO result = inventoryBookingDao.toInventoryBookingOutVO(inventoryBooking);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(inventoryBooking.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_UPDATED, result, original, journalEntryDao);
		logSystemMessage(inventoryBooking.getOnBehalfOf(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_UPDATED, result, original, journalEntryDao);
		Trial trial = inventoryBooking.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_UPDATED, result, original, journalEntryDao);
		}
		Course course = inventoryBooking.getCourse();
		if (course != null) {
			logSystemMessage(course, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_UPDATED, result, original, journalEntryDao);
		}
		Proband proband = inventoryBooking.getProband();
		if (proband != null) {
			logSystemMessage(proband, result.getInventory(), now, user, SystemMessageCodes.INVENTORY_BOOKING_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InventoryStatusEntryOutVO handleUpdateInventoryStatusEntry(AuthenticationVO auth, InventoryStatusEntryInVO modifiedInventoryStatusEntry)
			throws Exception {
		InventoryStatusEntryDao statusEntryDao = this.getInventoryStatusEntryDao();
		InventoryStatusEntry originalStatusEntry = CheckIDUtil.checkInventoryStatusEntryId(modifiedInventoryStatusEntry.getId(), statusEntryDao);
		checkInventoryStatusEntryInput(modifiedInventoryStatusEntry);
		InventoryStatusEntryOutVO original = statusEntryDao.toInventoryStatusEntryOutVO(originalStatusEntry);
		statusEntryDao.evict(originalStatusEntry);
		InventoryStatusEntry statusEntry = statusEntryDao.inventoryStatusEntryInVOToEntity(modifiedInventoryStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalStatusEntry, statusEntry, now, user);
		statusEntryDao.update(statusEntry);
		notifyInventoryInactive(statusEntry, now);
		InventoryStatusEntryOutVO result = statusEntryDao.toInventoryStatusEntryOutVO(statusEntry);
		logSystemMessage(statusEntry.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_STATUS_ENTRY_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected InventoryTagValueOutVO handleUpdateInventoryTagValue(AuthenticationVO auth, InventoryTagValueInVO modifiedInventoryTagValue) throws Exception {
		InventoryTagValueDao tagValueDao = this.getInventoryTagValueDao();
		InventoryTagValue originalTagValue = CheckIDUtil.checkInventoryTagValueId(modifiedInventoryTagValue.getId(), tagValueDao);
		checkInventoryTagValueInput(modifiedInventoryTagValue);
		InventoryTagValueOutVO original = tagValueDao.toInventoryTagValueOutVO(originalTagValue);
		tagValueDao.evict(originalTagValue);
		InventoryTagValue tagValue = tagValueDao.inventoryTagValueInVOToEntity(modifiedInventoryTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTagValue, tagValue, now, user);
		tagValueDao.update(tagValue);
		InventoryTagValueOutVO result = tagValueDao.toInventoryTagValueOutVO(tagValue);
		logSystemMessage(tagValue.getInventory(), result.getInventory(), now, user, SystemMessageCodes.INVENTORY_TAG_VALUE_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MaintenanceScheduleItemOutVO handleUpdateMaintenanceScheduleItem(AuthenticationVO auth, MaintenanceScheduleItemInVO modifiedMaintenanceScheduleItem)
			throws Exception {
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		MaintenanceScheduleItem originalMaintenanceScheduleItem = CheckIDUtil.checkMaintenanceScheduleItemId(modifiedMaintenanceScheduleItem.getId(), maintenanceScheduleItemDao);
		checkMaintenanceScheduleItemInput(modifiedMaintenanceScheduleItem);
		MaintenanceScheduleItemOutVO original = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(originalMaintenanceScheduleItem);
		maintenanceScheduleItemDao.evict(originalMaintenanceScheduleItem);
		MaintenanceScheduleItem maintenanceScheduleItem = maintenanceScheduleItemDao.maintenanceScheduleItemInVOToEntity(modifiedMaintenanceScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalMaintenanceScheduleItem, maintenanceScheduleItem, now, user);
		maintenanceScheduleItem.setDismissedTimestamp(now);
		maintenanceScheduleItemDao.update(maintenanceScheduleItem);
		notifyMaintenanceReminder(maintenanceScheduleItem, now);
		MaintenanceScheduleItemOutVO result = maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVO(maintenanceScheduleItem);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(maintenanceScheduleItem.getInventory(), result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_UPDATED, result, original,
				journalEntryDao);
		Staff companyContact = maintenanceScheduleItem.getCompanyContact();
		if (companyContact != null) {
			logSystemMessage(companyContact, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_UPDATED, result, original, journalEntryDao);
		}
		Staff responsiblePerson = maintenanceScheduleItem.getResponsiblePerson();
		if (responsiblePerson != null) {
			logSystemMessage(responsiblePerson, result.getInventory(), now, user, SystemMessageCodes.MAINTENANCE_SCHEDULE_ITEM_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	private void notifyInventoryInactive(InventoryStatusEntry statusEntry, Date now) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		ServiceUtil.cancelNotifications(statusEntry.getNotifications(), notificationDao, null); // clears inventory_active AND inventory inactive booking notifications
		if (!statusEntry.getType().isInventoryActive()) {
			if ((new DateInterval(statusEntry.getStart(), statusEntry.getStop())).contains(now)) {
				notificationDao.addNotification(statusEntry, now, null);
			}
			Iterator<InventoryBooking> it = this.getInventoryBookingDao()
					.findByInventoryCalendarInterval(statusEntry.getInventory().getId(), null, statusEntry.getStart(), statusEntry.getStop()).iterator();
			while (it.hasNext()) {
				notificationDao.addNotification(it.next(), statusEntry, now, null);
			}
		}
	}

	private void notifyMaintenanceReminder(MaintenanceScheduleItem maintenanceScheduleItem, Date now) throws Exception {
		ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(maintenanceScheduleItem);
		Date reminderStart = reminderItem.getReminderStart(now, false, null, null);
		if (maintenanceScheduleItem.isActive() && maintenanceScheduleItem.isNotify() && now.compareTo(reminderStart) >= 0 && !reminderItem.isRecurrenceDismissed(reminderStart)) {
			this.getNotificationDao().addNotification(maintenanceScheduleItem, now, null);
		} else {
			ServiceUtil.cancelNotifications(maintenanceScheduleItem.getNotifications(), this.getNotificationDao(),
					org.phoenixctms.ctsms.enumeration.NotificationType.MAINTENANCE_REMINDER);
		}
	}
}