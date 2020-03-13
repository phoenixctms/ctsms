// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::staff::StaffService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.staff;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.DutyRosterTurnCollisionFinder;
import org.phoenixctms.ctsms.adapt.StaffAddressTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.StaffContactDetailTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.StaffStatusEntryCollisionFinder;
import org.phoenixctms.ctsms.adapt.StaffTagAdapter;
import org.phoenixctms.ctsms.compare.DutyRosterTurnIntervalComparator;
import org.phoenixctms.ctsms.compare.StaffStatusEntryIntervalComparator;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntryDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao;
import org.phoenixctms.ctsms.domain.CvPosition;
import org.phoenixctms.ctsms.domain.CvPositionDao;
import org.phoenixctms.ctsms.domain.DutyRosterTurn;
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
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Lecturer;
import org.phoenixctms.ctsms.domain.LecturerDao;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItem;
import org.phoenixctms.ctsms.domain.MaintenanceScheduleItemDao;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipient;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffAddress;
import org.phoenixctms.ctsms.domain.StaffAddressDao;
import org.phoenixctms.ctsms.domain.StaffCategory;
import org.phoenixctms.ctsms.domain.StaffContactDetailValue;
import org.phoenixctms.ctsms.domain.StaffContactDetailValueDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffStatusEntry;
import org.phoenixctms.ctsms.domain.StaffStatusEntryDao;
import org.phoenixctms.ctsms.domain.StaffStatusType;
import org.phoenixctms.ctsms.domain.StaffTagValue;
import org.phoenixctms.ctsms.domain.StaffTagValueDao;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.domain.VisitScheduleItemDao;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.CVPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.CVPDFPainter;
import org.phoenixctms.ctsms.pdf.CVPDFSettingCodes;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
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
import org.phoenixctms.ctsms.util.date.DateInterval;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CvPDFVO;
import org.phoenixctms.ctsms.vo.CvPositionInVO;
import org.phoenixctms.ctsms.vo.CvPositionOutVO;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.DateCountVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.MaintenanceScheduleItemOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ShiftDurationSummaryVO;
import org.phoenixctms.ctsms.vo.StaffAddressInVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffImageInVO;
import org.phoenixctms.ctsms.vo.StaffImageOutVO;
import org.phoenixctms.ctsms.vo.StaffInVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryInVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffTagValueInVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.vocycle.StaffReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.staff.StaffService
 */
public class StaffServiceImpl
		extends StaffServiceBase {

	private static void checkDutyRosterTurnStaff(Staff staff) throws ServiceException {
		// other input checks
		if (staff != null && !staff.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_STAFF_NOT_PERSON);
		}
		if (staff != null && !staff.isEmployee()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_STAFF_NOT_EMPLOYEE);
		}
		if (staff != null && !staff.isAllocatable()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_STAFF_NOT_ALLOCATABLE);
		}
	}

	private static JournalEntry logSystemMessage(Course course, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Inventory inventory, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Proband proband, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Staff staff, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private final void addStaffInactiveVisitScheduleItemNotification(VisitScheduleItem visitScheduleItem, StaffStatusEntry statusEntry, Date now, Date date,
			Collection inactiveStaff) {
		Integer staffLimit = Settings.getIntNullable(SettingCodes.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_STAFF_LIMIT, Bundle.SETTINGS,
				DefaultSettings.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_STAFF_LIMIT);
		NotificationDao notificationDao = this.getNotificationDao();
		Map messageParameters = CoreUtil.createEmptyTemplateModel();
		messageParameters.put(NotificationMessageTemplateParameters.INACTIVE_STAFF, inactiveStaff);
		messageParameters.put(NotificationMessageTemplateParameters.INACTIVE_STAFF_LIMIT, staffLimit);
		if (date != null) {
			messageParameters.put(NotificationMessageTemplateParameters.VISIT_SCHEDULE_ITEM_DAY_DATE,
					Settings.getSimpleDateFormat(SettingCodes.NOTIFICATION_TEMPLATE_MODEL_DATE_PATTERN, Bundle.SETTINGS,
							DefaultSettings.NOTIFICATION_TEMPLATE_MODEL_DATE_PATTERN, Locales.NOTIFICATION).format(
									date));
		} else {
			messageParameters.put(NotificationMessageTemplateParameters.VISIT_SCHEDULE_ITEM_DAY_DATE, null);
		}
		notificationDao.addNotification(visitScheduleItem, statusEntry.getStaff(), now, messageParameters);
	}

	private void checkCvPositionInput(CvPositionInVO cvPositionIn) throws ServiceException {
		// referential checks
		Staff staff = CheckIDUtil.checkStaffId(cvPositionIn.getStaffId(), this.getStaffDao());
		if (cvPositionIn.getSectionId() != null) {
			CheckIDUtil.checkCvSectionId(cvPositionIn.getSectionId(), this.getCvSectionDao());
		}
		if (cvPositionIn.getInstitutionId() != null) {
			if (this.getStaffDao().load(cvPositionIn.getInstitutionId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_INVALID_INSTITUTION_STAFF_ID, cvPositionIn.getInstitutionId().toString());
			}
		}
		// other input checks
		if (!staff.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_STAFF_NOT_PERSON);
		}
		if (cvPositionIn.getShowCv() && cvPositionIn.getSectionId() == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_CV_SECTION_REQUIRED);
		}
		if (!cvPositionIn.getShowCv() && cvPositionIn.getShowCommentCv()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_SHOW_CV_DISABLED);
		}
		if (cvPositionIn.getStop() != null && cvPositionIn.getStart() != null
				&& DateCalc.getStartOfDay(cvPositionIn.getStop()).compareTo(DateCalc.getStartOfDay(cvPositionIn.getStart())) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_START_DATE_GREATER_THAN_OR_EQUAL_TO_END_DATE);
		}
	}

	private void checkDutyRosterTurnInput(DutyRosterTurnInVO dutyRosterTurnIn) throws ServiceException {
		// referential checks
		Staff staff = null;
		if (dutyRosterTurnIn.getStaffId() != null) {
			staff = CheckIDUtil.checkStaffId(dutyRosterTurnIn.getStaffId(), this.getStaffDao());
		}
		Trial trial = null;
		if (dutyRosterTurnIn.getTrialId() != null) {
			trial = CheckIDUtil.checkTrialId(dutyRosterTurnIn.getTrialId(), this.getTrialDao());
			ServiceUtil.checkTrialLocked(trial);
		}
		VisitScheduleItem visitScheduleItem = null;
		if (dutyRosterTurnIn.getVisitScheduleItemId() != null) {
			visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(dutyRosterTurnIn.getVisitScheduleItemId(), this.getVisitScheduleItemDao());
			ServiceUtil.checkTrialLocked(visitScheduleItem.getTrial());
		}
		if (visitScheduleItem != null && trial == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_TRIAL_REQUIRED);
		}
		if (trial != null && visitScheduleItem != null && !trial.equals(visitScheduleItem.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_WRONG_TRIAL);
		}
		checkDutyRosterTurnStaff(staff);
		if (dutyRosterTurnIn.getStop() != null && dutyRosterTurnIn.getStop().compareTo(dutyRosterTurnIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_END_TIMESTAMP_LESS_THAN_OR_EQUAL_TO_START_TIMESTAMP);
		}
		if (staff != null) {
			if ((new DutyRosterTurnCollisionFinder(this.getStaffDao(), this.getDutyRosterTurnDao())).collides(dutyRosterTurnIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_MAX_OVERLAPPING_EXCEEDED, staff.getMaxOverlappingShifts());
			}
		}
	}

	private void checkSelfRegistrationCourseParticipationStatusEntryDeletion(CourseParticipationStatusEntry courseParticipation) throws ServiceException {
		Course course = courseParticipation.getCourse();
		if (!course.isSelfRegistration()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_PARTICIPATION_COURSE_ADMIN_REGISTRATION);
		}
		if (course.getParticipationDeadline() != null && course.getParticipationDeadline().compareTo(CommonUtil.dateToTimestamp(new Date())) < 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_PARTICIPATION_DEADLINE_EXCEEDED);
		}
		if (this.getCourseParticipationStatusTypeDao().findTransitions(courseParticipation.getStatus().getId(), false, true).size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_PARTICIPATION_TERMINAL_STATE,
					L10nUtil.getCourseParticipationStatusTypeName(Locales.USER, courseParticipation.getStatus().getNameL10nKey()));
		}
	}

	private void checkStaffAddressInput(StaffAddressInVO addressIn) throws ServiceException {
		(new StaffAddressTypeTagAdapter(this.getStaffDao(), this.getAddressTypeDao(), this.getStaffAddressDao())).checkTagValueInput(addressIn);
	}

	private void checkStaffContactDetailValueInput(StaffContactDetailValueInVO contactValueIn) throws ServiceException {
		(new StaffContactDetailTypeTagAdapter(this.getStaffDao(), this.getContactDetailTypeDao())).checkTagValueInput(contactValueIn);
	}

	private void checkStaffImageInput(boolean isPerson, StaffImageInVO staffImage) throws ServiceException {
		// other input checks
		if (isPerson) {
			if (staffImage.getDatas() != null && staffImage.getDatas().length > 0) {
				Integer staffImageSizeLimit = Settings.getIntNullable(SettingCodes.STAFF_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.STAFF_IMAGE_SIZE_LIMIT);
				if (staffImageSizeLimit != null && staffImage.getDatas().length > staffImageSizeLimit) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(staffImageSizeLimit));
				}
				if (staffImage.getMimeType() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_MIME_TYPE_REQUIRED);
				}
				Iterator<MimeType> it = this.getMimeTypeDao().findByMimeTypeModule(staffImage.getMimeType(), FileModule.STAFF_IMAGE).iterator();
				if (!it.hasNext()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_MIME_TYPE_UNKNOWN, staffImage.getMimeType());
				}
				if (!it.next().isImage()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_MIME_TYPE_NO_IMAGE, staffImage.getMimeType());
				}
				Dimension imageDimension = CoreUtil.getImageDimension(staffImage.getDatas());
				if (imageDimension == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_CANNOT_READ_DIMENSIONS);
				} else {
					Integer staffImageMinWidth = Settings.getIntNullable(SettingCodes.STAFF_IMAGE_MIN_WIDTH, Bundle.SETTINGS, DefaultSettings.STAFF_IMAGE_MIN_WIDTH);
					Integer staffImageMinHeight = Settings.getIntNullable(SettingCodes.STAFF_IMAGE_MIN_HEIGHT, Bundle.SETTINGS, DefaultSettings.STAFF_IMAGE_MIN_HEIGHT);
					if (staffImageMinWidth != null && imageDimension.getWidth() < (double) staffImageMinWidth) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_WIDTH_LESS_THAN_LIMIT, staffImageMinWidth);
					}
					if (staffImageMinHeight != null && imageDimension.getHeight() < (double) staffImageMinHeight) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_HEIGHT_LESS_THAN_LIMIT, staffImageMinHeight);
					}
				}
			} else {
				if (staffImage.getShowCv()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_SHOW_CV_SET);
				}
			}
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_IMAGE_STAFF_NOT_PERSON);
		}
	}

	private void checkStaffInput(StaffInVO staffIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkDepartmentId(staffIn.getDepartmentId(), this.getDepartmentDao());
		StaffCategory category = CheckIDUtil.checkStaffCategoryId(staffIn.getCategoryId(), this.getStaffCategoryDao());
		if (staffIn.getParentId() != null) {
			if (this.getStaffDao().load(staffIn.getParentId(), LockMode.PESSIMISTIC_WRITE) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PARENT_STAFF_ID, staffIn.getParentId().toString());
			}
		}
		// other input checks
		if (staffIn.isPerson()) {
			if (!category.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CATEGORY_NOT_FOR_PERSON_ENTRIES,
						L10nUtil.getStaffCategoryName(Locales.USER, category.getNameL10nKey()));
			}
			if (staffIn.isAllocatable() && !staffIn.isEmployee()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.EMPLOYEE_FLAG_NOT_SET);
			}
			if (CommonUtil.isEmptyString(staffIn.getFirstName())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_FIRST_NAME_REQUIRED);
			}
			if (CommonUtil.isEmptyString(staffIn.getLastName())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_LAST_NAME_REQUIRED);
			}
			if (staffIn.isEmployee()) {
				if (staffIn.getDateOfBirth() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_DATE_OF_BIRTH_REQUIRED);
				} else if (DateCalc.getStartOfDay(staffIn.getDateOfBirth()).compareTo(new Date()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_DATE_OF_BIRTH_IN_THE_FUTURE);
				}
			}
			if (staffIn.getGender() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_GENDER_REQUIRED);
			}
			if (staffIn.getOrganisationName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ORGANISATION_NAME_NOT_NULL);
			}
			if (staffIn.getCvOrganisationName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ORGANISATION_CV_NAME_NOT_NULL);
			}
		} else {
			if (!category.isOrganisation()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CATEGORY_NOT_FOR_ORGANISATION_ENTRIES,
						L10nUtil.getStaffCategoryName(Locales.USER, category.getNameL10nKey()));
			}
			if (staffIn.isAllocatable()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ALLOCATABLE_FLAG_SET);
			}
			if (staffIn.isEmployee()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.EMPLOYEE_FLAG_SET);
			}
			if (CommonUtil.isEmptyString(staffIn.getOrganisationName())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ORGANISATION_NAME_REQUIRED);
			}
			if (staffIn.getPrefixedTitle1() != null || staffIn.getPrefixedTitle2() != null || staffIn.getPrefixedTitle3() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_PREFIXED_TITLES_NOT_NULL);
			}
			if (staffIn.getFirstName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_FIRST_NAME_NOT_NULL);
			}
			if (staffIn.getLastName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_LAST_NAME_NOT_NULL);
			}
			if (staffIn.getPostpositionedTitle1() != null || staffIn.getPostpositionedTitle2() != null || staffIn.getPostpositionedTitle3() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_POSTPOSITIONED_TITLES_NOT_NULL);
			}
			if (staffIn.getCvAcademicTitle() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CV_ACADEMIC_TITLE_NOT_NULL);
			}
			if (staffIn.getDateOfBirth() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_DATE_OF_BIRTH_NOT_NULL);
			}
			if (staffIn.getGender() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_GENDER_NOT_NULL);
			}
			if (staffIn.getCitizenship() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CITIZENSHIP_NOT_NULL);
			}
		}
		if (staffIn.getAllocatable()) {
			if (staffIn.getMaxOverlappingShifts() < 1) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_OVERLAPPING_SHIFTS_LESS_THAN_ONE);
			}
		} else {
			if (staffIn.getMaxOverlappingShifts() != 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_OVERLAPPING_SHIFTS_NOT_EQUAL_TO_ZERO);
			}
		}
	}

	private void checkStaffLoop(Staff staff) throws ServiceException {
		(new StaffReflexionGraph(this.getStaffDao())).checkGraphLoop(staff, true, false);
	}

	private void checkStaffStatusEntryInput(StaffStatusEntryInVO statusEntryIn) throws ServiceException {
		// referential checks
		Staff staff = CheckIDUtil.checkStaffId(statusEntryIn.getStaffId(), this.getStaffDao());
		StaffStatusType statusType = CheckIDUtil.checkStaffStatusTypeId(statusEntryIn.getTypeId(), this.getStaffStatusTypeDao());
		// other input checks
		if (!staff.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_STATUS_ENTRY_STAFF_NOT_PERSON);
		}
		if (!staff.isEmployee()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_STATUS_ENTRY_STAFF_NOT_EMPLOYEE);
		}
		if (statusEntryIn.getStop() != null && statusEntryIn.getStop().compareTo(statusEntryIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_STATUS_ENTRY_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		if ((new StaffStatusEntryCollisionFinder(this.getStaffDao(), this.getStaffStatusEntryDao())).collides(statusEntryIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_STATUS_ENTRY_OVERLAPPING);
		}
	}

	private void checkStaffTagValueInput(StaffTagValueInVO tagValueIn) throws ServiceException {
		(new StaffTagAdapter(this.getStaffDao(), this.getStaffTagDao())).checkTagValueInput(tagValueIn);
	}

	private void deleteStaffHelper(Staff staff, boolean deleteCascade, User user, Timestamp now) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		StaffOutVO result = staffDao.toStaffOutVO(staff);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (deleteCascade) {
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			StaffTagValueDao staffTagValueDao = this.getStaffTagValueDao();
			Iterator<StaffTagValue> tagValuesIt = staff.getTagValues().iterator();
			while (tagValuesIt.hasNext()) {
				StaffTagValue tagValue = tagValuesIt.next();
				tagValue.setStaff(null);
				staffTagValueDao.remove(tagValue);
			}
			staff.getTagValues().clear();
			StaffContactDetailValueDao staffContactDetailValueDao = this.getStaffContactDetailValueDao();
			Iterator<StaffContactDetailValue> contactDetailValuesIt = staff.getContactDetailValues().iterator();
			while (contactDetailValuesIt.hasNext()) {
				StaffContactDetailValue contactDetailValue = contactDetailValuesIt.next();
				contactDetailValue.setStaff(null);
				staffContactDetailValueDao.remove(contactDetailValue);
			}
			staff.getContactDetailValues().clear();
			StaffAddressDao staffAddressDao = this.getStaffAddressDao();
			Iterator<StaffAddress> addressesIt = staff.getAddresses().iterator();
			while (addressesIt.hasNext()) {
				StaffAddress address = addressesIt.next();
				address.setStaff(null);
				staffAddressDao.remove(address);
			}
			staff.getAddresses().clear();
			StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
			Iterator<StaffStatusEntry> statusEntriesIt = staff.getStatusEntries().iterator();
			while (statusEntriesIt.hasNext()) {
				StaffStatusEntry statusEntry = statusEntriesIt.next();
				statusEntry.setStaff(null);
				ServiceUtil.removeNotifications(statusEntry.getNotifications(), notificationDao, notificationRecipientDao);
				staffStatusEntryDao.remove(statusEntry);
			}
			staff.getStatusEntries().clear();
			CvPositionDao cvPositionDao = this.getCvPositionDao();
			Iterator<CvPosition> cvPositionsIt = staff.getCvPositions().iterator();
			while (cvPositionsIt.hasNext()) {
				CvPosition cvPosition = cvPositionsIt.next();
				cvPosition.setStaff(null);
				cvPositionDao.remove(cvPosition);
			}
			staff.getCvPositions().clear();
			CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
			Iterator<CourseParticipationStatusEntry> participationsIt = staff.getParticipations().iterator();
			while (participationsIt.hasNext()) {
				CourseParticipationStatusEntry participation = participationsIt.next();
				Course course = participation.getCourse();
				CourseParticipationStatusEntryOutVO participationVO = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(participation);
				logSystemMessage(course, result, now, user, SystemMessageCodes.STAFF_DELETED_PARTICIPATION_DELETED, participationVO, null, journalEntryDao);
				course.removeParticipations(participation);
				participation.setCourse(null);
				participation.setStaff(null);
				ServiceUtil.removeNotifications(participation.getNotifications(), notificationDao, notificationRecipientDao);
				courseParticipationStatusEntryDao.remove(participation);
			}
			staff.getParticipations().clear();
			DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
			Iterator<DutyRosterTurn> dutyRosterTurnIt = staff.getDutyRosterTurns().iterator();
			while (dutyRosterTurnIt.hasNext()) {
				DutyRosterTurn dutyRosterTurn = dutyRosterTurnIt.next();
				DutyRosterTurnOutVO original = null;
				Trial trial = dutyRosterTurn.getTrial();
				VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
				if (trial != null) {
					original = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					ServiceUtil.checkTrialLocked(trial);
				} else if (visitScheduleItem != null) {
					original = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					ServiceUtil.checkTrialLocked(visitScheduleItem.getTrial());
				}
				dutyRosterTurn.setStaff(null);
				CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
				dutyRosterTurnDao.update(dutyRosterTurn);
				if (trial != null) {
					DutyRosterTurnOutVO dutyRosterTurnVO = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.STAFF_DELETED_DUTY_ROSTER_TURN_UPDATED, dutyRosterTurnVO, original, journalEntryDao);
				} else if (visitScheduleItem != null) {
					DutyRosterTurnOutVO dutyRosterTurnVO = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result, now, user, SystemMessageCodes.STAFF_DELETED_DUTY_ROSTER_TURN_UPDATED, dutyRosterTurnVO,
							original,
							journalEntryDao);
				}
			}
			staff.getDutyRosterTurns().clear();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<InventoryBooking> bookingsIt = staff.getInventoryBookings().iterator();
			while (bookingsIt.hasNext()) {
				InventoryBooking booking = bookingsIt.next();
				InventoryBookingOutVO bookingVO = inventoryBookingDao.toInventoryBookingOutVO(booking);
				Trial trial = booking.getTrial();
				Course course = booking.getCourse();
				Proband proband = booking.getProband();
				Inventory inventory = booking.getInventory();
				logSystemMessage(inventory, result, now, user, SystemMessageCodes.STAFF_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
				inventory.removeBookings(booking);
				booking.setInventory(null);
				if (trial != null) {
					ServiceUtil.checkTrialLocked(trial);
					ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.STAFF_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					trial.removeInventoryBookings(booking);
					booking.setTrial(null);
				}
				if (course != null) {
					logSystemMessage(course, result, now, user, SystemMessageCodes.STAFF_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					course.removeInventoryBookings(booking);
					booking.setCourse(null);
				}
				if (proband != null) {
					ServiceUtil.checkProbandLocked(proband);
					logSystemMessage(proband, result, now, user, SystemMessageCodes.STAFF_DELETED_BOOKING_DELETED, bookingVO, null, journalEntryDao);
					proband.removeInventoryBookings(booking);
					booking.setProband(null);
				}
				booking.setOnBehalfOf(null);
				ServiceUtil.removeNotifications(booking.getNotifications(), notificationDao, notificationRecipientDao);
				inventoryBookingDao.remove(booking);
			}
			staff.getInventoryBookings().clear();
			MaintenanceScheduleItemDao maintenanceItemScheduleDao = this.getMaintenanceScheduleItemDao();
			Iterator<MaintenanceScheduleItem> responsiblePersonMaintenanceItemsIt = staff.getResponsiblePersonMaintenanceItems().iterator();
			while (responsiblePersonMaintenanceItemsIt.hasNext()) {
				MaintenanceScheduleItem responsiblePersonMaintenanceItem = responsiblePersonMaintenanceItemsIt.next();
				MaintenanceScheduleItemOutVO original = maintenanceItemScheduleDao.toMaintenanceScheduleItemOutVO(responsiblePersonMaintenanceItem);
				responsiblePersonMaintenanceItem.setResponsiblePerson(null);
				responsiblePersonMaintenanceItem.setNotify(false);
				CoreUtil.modifyVersion(responsiblePersonMaintenanceItem, responsiblePersonMaintenanceItem.getVersion(), now, user);
				maintenanceItemScheduleDao.update(responsiblePersonMaintenanceItem);
				MaintenanceScheduleItemOutVO responsiblePersonMaintenanceItemVO = maintenanceItemScheduleDao.toMaintenanceScheduleItemOutVO(responsiblePersonMaintenanceItem);
				logSystemMessage(responsiblePersonMaintenanceItem.getInventory(), result, now, user, SystemMessageCodes.STAFF_DELETED_MAINTENANCE_ITEM_UPDATED,
						responsiblePersonMaintenanceItemVO, original, journalEntryDao);
			}
			staff.getResponsiblePersonMaintenanceItems().clear();
			Iterator<MaintenanceScheduleItem> responsiblePersonProxyMaintenanceItemsIt = staff.getResponsiblePersonProxyMaintenanceItems().iterator();
			while (responsiblePersonProxyMaintenanceItemsIt.hasNext()) {
				MaintenanceScheduleItem responsiblePersonProxyMaintenanceItem = responsiblePersonProxyMaintenanceItemsIt.next();
				MaintenanceScheduleItemOutVO original = maintenanceItemScheduleDao.toMaintenanceScheduleItemOutVO(responsiblePersonProxyMaintenanceItem);
				responsiblePersonProxyMaintenanceItem.setResponsiblePersonProxy(null);
				responsiblePersonProxyMaintenanceItem.setNotify(false);
				CoreUtil.modifyVersion(responsiblePersonProxyMaintenanceItem, responsiblePersonProxyMaintenanceItem.getVersion(), now, user);
				maintenanceItemScheduleDao.update(responsiblePersonProxyMaintenanceItem);
				MaintenanceScheduleItemOutVO responsiblePersonProxyMaintenanceItemVO = maintenanceItemScheduleDao
						.toMaintenanceScheduleItemOutVO(responsiblePersonProxyMaintenanceItem);
				logSystemMessage(responsiblePersonProxyMaintenanceItem.getInventory(), result, now, user, SystemMessageCodes.STAFF_DELETED_MAINTENANCE_ITEM_UPDATED,
						responsiblePersonProxyMaintenanceItemVO, original, journalEntryDao);
			}
			staff.getResponsiblePersonProxyMaintenanceItems().clear();
			Iterator<MaintenanceScheduleItem> companyContactMaintenanceItemsIt = staff.getCompanyContactMaintenanceItems().iterator();
			while (companyContactMaintenanceItemsIt.hasNext()) {
				MaintenanceScheduleItem companyContactMaintenanceItem = companyContactMaintenanceItemsIt.next();
				MaintenanceScheduleItemOutVO original = maintenanceItemScheduleDao.toMaintenanceScheduleItemOutVO(companyContactMaintenanceItem);
				companyContactMaintenanceItem.setCompanyContact(null);
				CoreUtil.modifyVersion(companyContactMaintenanceItem, companyContactMaintenanceItem.getVersion(), now, user);
				maintenanceItemScheduleDao.update(companyContactMaintenanceItem);
				MaintenanceScheduleItemOutVO companyContactMaintenanceItemVO = maintenanceItemScheduleDao.toMaintenanceScheduleItemOutVO(companyContactMaintenanceItem);
				logSystemMessage(companyContactMaintenanceItem.getInventory(), result, now, user, SystemMessageCodes.STAFF_DELETED_MAINTENANCE_ITEM_UPDATED,
						companyContactMaintenanceItemVO, original, journalEntryDao);
			}
			staff.getCompanyContactMaintenanceItems().clear();
			InventoryStatusEntryDao inventoryStatusEntryDao = this.getInventoryStatusEntryDao();
			Iterator<InventoryStatusEntry> inventoryStatusEntryDaoIt = staff.getLoans().iterator();
			while (inventoryStatusEntryDaoIt.hasNext()) {
				InventoryStatusEntry inventoryStatusEntry = inventoryStatusEntryDaoIt.next();
				if (inventoryStatusEntry.getType().isOriginatorRequired()) {
					InventoryStatusEntryOutVO inventoryStatusEntryVO = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					Inventory inventory = inventoryStatusEntry.getInventory();
					logSystemMessage(inventory, result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_DELETED, inventoryStatusEntryVO, null, journalEntryDao);
					inventory.removeStatusEntries(inventoryStatusEntry);
					inventoryStatusEntry.setInventory(null);
					inventoryStatusEntry.setOriginator(null);
					Staff addressee = inventoryStatusEntry.getAddressee();
					if (addressee != null) {
						logSystemMessage(addressee, result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_DELETED, inventoryStatusEntryVO, null, journalEntryDao);
						addressee.removeLendings(inventoryStatusEntry);
					}
					ServiceUtil.removeNotifications(inventoryStatusEntry.getNotifications(), notificationDao, notificationRecipientDao);
					inventoryStatusEntryDao.remove(inventoryStatusEntry);
				} else {
					InventoryStatusEntryOutVO original = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					inventoryStatusEntry.setOriginator(null);
					CoreUtil.modifyVersion(inventoryStatusEntry, inventoryStatusEntry.getVersion(), now, user);
					inventoryStatusEntryDao.update(inventoryStatusEntry);
					InventoryStatusEntryOutVO inventoryStatusEntryVO = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					logSystemMessage(inventoryStatusEntry.getInventory(), result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_UPDATED, inventoryStatusEntryVO,
							original, journalEntryDao);
				}
			}
			staff.getLoans().clear();
			inventoryStatusEntryDaoIt = staff.getLendings().iterator();
			while (inventoryStatusEntryDaoIt.hasNext()) {
				InventoryStatusEntry inventoryStatusEntry = inventoryStatusEntryDaoIt.next();
				if (inventoryStatusEntry.getType().isAddresseeRequired()) {
					InventoryStatusEntryOutVO inventoryStatusEntryVO = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					Inventory inventory = inventoryStatusEntry.getInventory();
					logSystemMessage(inventory, result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_DELETED, inventoryStatusEntryVO, null, journalEntryDao);
					inventory.removeStatusEntries(inventoryStatusEntry);
					inventoryStatusEntry.setInventory(null);
					inventoryStatusEntry.setAddressee(null);
					Staff origiantor = inventoryStatusEntry.getOriginator();
					if (origiantor != null) {
						logSystemMessage(origiantor, result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_DELETED, inventoryStatusEntryVO, null, journalEntryDao);
						origiantor.removeLoans(inventoryStatusEntry);
					}
					ServiceUtil.removeNotifications(inventoryStatusEntry.getNotifications(), notificationDao, notificationRecipientDao);
					inventoryStatusEntryDao.remove(inventoryStatusEntry);
				} else {
					InventoryStatusEntryOutVO original = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					inventoryStatusEntry.setAddressee(null);
					CoreUtil.modifyVersion(inventoryStatusEntry, inventoryStatusEntry.getVersion(), now, user);
					inventoryStatusEntryDao.update(inventoryStatusEntry);
					InventoryStatusEntryOutVO inventoryStatusEntryVO = inventoryStatusEntryDao.toInventoryStatusEntryOutVO(inventoryStatusEntry);
					logSystemMessage(inventoryStatusEntry.getInventory(), result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_STATUS_UPDATED, inventoryStatusEntryVO,
							original, journalEntryDao);
				}
			}
			staff.getLendings().clear();
			InventoryDao inventoryDao = this.getInventoryDao();
			Iterator<Inventory> inventoriesIt = staff.getInventories().iterator();
			while (inventoriesIt.hasNext()) {
				Inventory inventory = inventoriesIt.next();
				InventoryOutVO original = inventoryDao.toInventoryOutVO(inventory);
				inventory.setOwner(null);
				CoreUtil.modifyVersion(inventory, inventory.getVersion(), now, user);
				inventoryDao.update(inventory);
				InventoryOutVO inventoryVO = inventoryDao.toInventoryOutVO(inventory);
				logSystemMessage(inventory, result, now, user, SystemMessageCodes.STAFF_DELETED_INVENTORY_UPDATED, inventoryVO, original, journalEntryDao);
			}
			staff.getInventories().clear();
			CourseDao courseDao = this.getCourseDao();
			Iterator<Course> institutionCoursesIt = staff.getInstitutionCourses().iterator();
			while (institutionCoursesIt.hasNext()) {
				Course course = institutionCoursesIt.next();
				CourseOutVO original = courseDao.toCourseOutVO(course);
				course.setInstitution(null);
				CoreUtil.modifyVersion(course, course.getVersion(), now, user);
				courseDao.update(course);
				CourseOutVO courseVO = courseDao.toCourseOutVO(course);
				logSystemMessage(course, result, now, user, SystemMessageCodes.STAFF_DELETED_COURSE_UPDATED, courseVO, original, journalEntryDao);
			}
			staff.getInstitutionCourses().clear();
			Iterator<CvPosition> institutionCvPositionsIt = staff.getInstitutionCvPositions().iterator();
			while (institutionCvPositionsIt.hasNext()) {
				CvPosition institutionCvPosition = institutionCvPositionsIt.next();
				CvPositionOutVO original = cvPositionDao.toCvPositionOutVO(institutionCvPosition);
				Staff cvPositionStaff = institutionCvPosition.getStaff();
				institutionCvPosition.setInstitution(null);
				CoreUtil.modifyVersion(institutionCvPosition, institutionCvPosition.getVersion(), now, user);
				cvPositionDao.update(institutionCvPosition);
				CvPositionOutVO institutionCvPositionVO = cvPositionDao.toCvPositionOutVO(institutionCvPosition);
				logSystemMessage(cvPositionStaff, result, now, user, SystemMessageCodes.STAFF_DELETED_CV_POSITION_UPDATED, institutionCvPositionVO, original, journalEntryDao);
			}
			staff.getInstitutionCvPositions().clear();
			LecturerDao lecturerDao = this.getLecturerDao();
			Iterator<Lecturer> lecturesIt = staff.getLectures().iterator();
			while (lecturesIt.hasNext()) {
				Lecturer lecturer = lecturesIt.next();
				Course course = lecturer.getCourse();
				LecturerOutVO lecturerVO = lecturerDao.toLecturerOutVO(lecturer);
				logSystemMessage(course, result, now, user, SystemMessageCodes.STAFF_DELETED_LECTURER_DELETED, lecturerVO, null, journalEntryDao);
				course.removeLecturers(lecturer);
				lecturer.setCourse(null);
				lecturer.setStaff(null);
				lecturerDao.remove(lecturer);
			}
			staff.getLectures().clear();
			TeamMemberDao teamMemberDao = this.getTeamMemberDao();
			Iterator<TeamMember> trialMemberShipsIt = staff.getTrialMemberships().iterator();
			while (trialMemberShipsIt.hasNext()) {
				TeamMember trialMembership = trialMemberShipsIt.next();
				Trial trial = trialMembership.getTrial();
				ServiceUtil.checkTrialLocked(trial);
				TeamMemberOutVO trialMembershipVO = teamMemberDao.toTeamMemberOutVO(trialMembership);
				ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.STAFF_DELETED_TEAM_MEMBER_DELETED, trialMembershipVO, null, journalEntryDao);
				trial.removeMembers(trialMembership);
				trialMembership.setTrial(null);
				trialMembership.setStaff(null);
				teamMemberDao.remove(trialMembership);
			}
			staff.getTrialMemberships().clear();
			UserDao userDao = this.getUserDao();
			Iterator<User> accountsIt = staff.getAccounts().iterator();
			while (accountsIt.hasNext()) {
				User account = accountsIt.next();
				UserOutVO original = userDao.toUserOutVO(account);
				account.setIdentity(null);
				CoreUtil.modifyVersion(account, account.getVersion(), now, user);
				userDao.update(account);
				UserOutVO accountVO = userDao.toUserOutVO(account);
				logSystemMessage(account, result, now, user, SystemMessageCodes.STAFF_DELETED_USER_UPDATED, accountVO, original, journalEntryDao);
			}
			staff.getAccounts().clear();
			HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
			Iterator<Hyperlink> hyperlinksIt = staff.getHyperlinks().iterator();
			while (hyperlinksIt.hasNext()) {
				Hyperlink hyperlink = hyperlinksIt.next();
				hyperlink.setStaff(null);
				hyperlinkDao.remove(hyperlink);
			}
			ProbandDao probandDao = this.getProbandDao();
			Iterator<Proband> patientsIt = staff.getPatients().iterator();
			while (patientsIt.hasNext()) {
				Proband patient = patientsIt.next();
				ProbandOutVO original = probandDao.toProbandOutVO(patient);
				patient.setPhysician(null);
				CoreUtil.modifyVersion(patient, patient.getVersion(), now, user);
				probandDao.update(patient);
				ProbandOutVO patientVO = probandDao.toProbandOutVO(patient);
				logSystemMessage(patient, result, now, user, SystemMessageCodes.STAFF_DELETED_PROBAND_UPDATED, patientVO, original, journalEntryDao);
			}
			staff.getPatients().clear();
			staff.getHyperlinks().clear();
			Iterator<JournalEntry> journalEntriesIt = staff.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setStaff(null);
				journalEntryDao.remove(journalEntry);
			}
			staff.getJournalEntries().clear();
			FileDao fileDao = this.getFileDao();
			Iterator<File> filesIt = staff.getFiles().iterator();
			while (filesIt.hasNext()) {
				File file = filesIt.next();
				file.setStaff(null);
				fileDao.remove(file);
			}
			staff.getFiles().clear();
			ServiceUtil.removeNotifications(staff.getNotifications(), notificationDao, notificationRecipientDao);
			Iterator<NotificationRecipient> notificationRecipientsIt = staff.getNotificationReceipts().iterator();
			while (notificationRecipientsIt.hasNext()) {
				NotificationRecipient recipient = notificationRecipientsIt.next();
				recipient.setStaff(null);
				recipient.setNotification(null);
				notificationRecipientDao.remove(recipient);
			}
			staff.getNotificationReceipts().clear();
		}
		Iterator<Staff> childrenIt = staff.getChildren().iterator();
		while (childrenIt.hasNext()) {
			Staff child = childrenIt.next();
			child.setParent(null);
			CoreUtil.modifyVersion(child, child.getVersion(), now, user);
			staffDao.update(child);
		}
		staff.getChildren().clear();
		Staff parent = staff.getParent();
		if (parent != null) {
			parent.removeChildren(staff);
			staff.setParent(null);
			staffDao.update(parent);
		}
		PersonContactParticulars personParticulars = staff.getPersonParticulars();
		OrganisationContactParticulars organisationParticulars = staff.getOrganisationParticulars();
		staffDao.remove(staff);
		if (personParticulars != null) {
			this.getPersonContactParticularsDao().remove(personParticulars);
		}
		if (organisationParticulars != null) {
			this.getOrganisationContactParticularsDao().remove(organisationParticulars);
		}
	}

	@Override
	protected CvPositionOutVO handleAddCvPosition(AuthenticationVO auth, CvPositionInVO newCvPosition)
			throws Exception {
		checkCvPositionInput(newCvPosition);
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		CvPosition cvPosition = cvPositionDao.cvPositionInVOToEntity(newCvPosition);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(cvPosition, now, user);
		cvPosition = cvPositionDao.create(cvPosition);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		CvPositionOutVO result = cvPositionDao.toCvPositionOutVO(cvPosition);
		logSystemMessage(cvPosition.getStaff(), result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_CREATED, result, null, journalEntryDao);
		Staff institution = cvPosition.getInstitution();
		if (institution != null) {
			logSystemMessage(institution, result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected DutyRosterTurnOutVO handleAddDutyRosterTurn(
			AuthenticationVO auth, DutyRosterTurnInVO newDutyRosterTurn) throws Exception {
		checkDutyRosterTurnInput(newDutyRosterTurn);
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		DutyRosterTurn dutyRosterTurn = dutyRosterTurnDao.dutyRosterTurnInVOToEntity(newDutyRosterTurn);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(dutyRosterTurn, now, user);
		dutyRosterTurn = dutyRosterTurnDao.create(dutyRosterTurn);
		notifyDutyRosterTurn(null, dutyRosterTurn, now);
		DutyRosterTurnOutVO result = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Staff staff = dutyRosterTurn.getStaff();
		if (staff != null) {
			if (result.getTrial() != null) {
				ServiceUtil.logSystemMessage(staff, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			} else if (result.getVisitScheduleItem() != null) {
				ServiceUtil
						.logSystemMessage(staff, result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			} else {
				logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			}
		}
		Trial trial = dutyRosterTurn.getTrial();
		VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
		if (trial != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(trial, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			}
		} else if (visitScheduleItem != null) {
			if (result.getStaff() != null) {
				ServiceUtil
						.logSystemMessage(visitScheduleItem.getTrial(), result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED, result, null, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_CREATED,
						result, null, journalEntryDao);
			}
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.staff.StaffService#addStaff(StaffInVO)
	 */
	@Override
	protected StaffOutVO handleAddStaff(AuthenticationVO auth, StaffInVO newStaff, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		checkStaffInput(newStaff);
		StaffDao staffDao = this.getStaffDao();
		Staff staff = staffDao.staffInVOToEntity(newStaff);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(staff, now, user);
		PersonContactParticulars personParticulars = staff.getPersonParticulars();
		OrganisationContactParticulars organisationParticulars = staff.getOrganisationParticulars();
		if (personParticulars != null) {
			this.getPersonContactParticularsDao().create(personParticulars);
		}
		if (organisationParticulars != null) {
			this.getOrganisationContactParticularsDao().create(organisationParticulars);
		}
		staff = staffDao.create(staff);
		StaffOutVO result = staffDao.toStaffOutVO(staff, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(staff, result, now, user, SystemMessageCodes.STAFF_CREATED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected StaffAddressOutVO handleAddStaffAddress(
			AuthenticationVO auth, StaffAddressInVO newStaffAddress) throws Exception {
		checkStaffAddressInput(newStaffAddress);
		StaffAddressDao addressDao = this.getStaffAddressDao();
		StaffAddress address = addressDao.staffAddressInVOToEntity(newStaffAddress);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(address, now, user);
		address = addressDao.create(address);
		StaffAddressOutVO result = addressDao.toStaffAddressOutVO(address);
		logSystemMessage(address.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_ADDRESS_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffContactDetailValueOutVO handleAddStaffContactDetailValue(
			AuthenticationVO auth, StaffContactDetailValueInVO newStaffContactDetailValue)
			throws Exception {
		checkStaffContactDetailValueInput(newStaffContactDetailValue);
		StaffContactDetailValueDao contactValueDao = this.getStaffContactDetailValueDao();
		StaffContactDetailValue contactValue = contactValueDao.staffContactDetailValueInVOToEntity(newStaffContactDetailValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(contactValue, now, user);
		contactValue = contactValueDao.create(contactValue);
		StaffContactDetailValueOutVO result = contactValueDao.toStaffContactDetailValueOutVO(contactValue);
		logSystemMessage(contactValue.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_CONTACT_DETAIL_VALUE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffStatusEntryOutVO handleAddStaffStatusEntry(
			AuthenticationVO auth, StaffStatusEntryInVO newStaffStatusEntry) throws Exception {
		checkStaffStatusEntryInput(newStaffStatusEntry);
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		StaffStatusEntry statusEntry = statusEntryDao.staffStatusEntryInVOToEntity(newStaffStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(statusEntry, now, user);
		statusEntry = statusEntryDao.create(statusEntry);
		notifyStaffInactive(statusEntry, now);
		StaffStatusEntryOutVO result = statusEntryDao.toStaffStatusEntryOutVO(statusEntry);
		logSystemMessage(statusEntry.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_STATUS_ENTRY_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffTagValueOutVO handleAddStaffTagValue(
			AuthenticationVO auth, StaffTagValueInVO newStaffTagValue) throws Exception {
		checkStaffTagValueInput(newStaffTagValue);
		StaffTagValueDao tagValueDao = this.getStaffTagValueDao();
		StaffTagValue tagValue = tagValueDao.staffTagValueInVOToEntity(newStaffTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(tagValue, now, user);
		tagValue = tagValueDao.create(tagValue);
		StaffTagValueOutVO result = tagValueDao.toStaffTagValueOutVO(tagValue);
		logSystemMessage(tagValue.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_TAG_VALUE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CvPositionOutVO handleDeleteCvPosition(AuthenticationVO auth, Long cvPositionId)
			throws Exception {
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		CvPosition cvPosition = CheckIDUtil.checkCvPositionId(cvPositionId, cvPositionDao);
		Staff staff = cvPosition.getStaff();
		Staff institution = cvPosition.getInstitution();
		CvPositionOutVO result = cvPositionDao.toCvPositionOutVO(cvPosition);
		staff.removeCvPositions(cvPosition);
		cvPosition.setStaff(null);
		if (institution != null) {
			institution.removeCvPositions(cvPosition);
			cvPosition.setInstitution(null);
		}
		cvPositionDao.remove(cvPosition);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_DELETED, result, null, journalEntryDao);
		if (institution != null) {
			logSystemMessage(institution, result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected DutyRosterTurnOutVO handleDeleteDutyRosterTurn(
			AuthenticationVO auth, Long dutyRosterTurnId) throws Exception {
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		DutyRosterTurn dutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(dutyRosterTurnId, dutyRosterTurnDao);
		Staff staff = dutyRosterTurn.getStaff();
		Trial trial = dutyRosterTurn.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
		if (visitScheduleItem != null) {
			ServiceUtil.checkTrialLocked(visitScheduleItem.getTrial());
		}
		DutyRosterTurnOutVO result = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		notifyDutyRosterTurnDeleted(dutyRosterTurn, user, now); // before staff is cleared
		if (staff != null) {
			staff.removeDutyRosterTurns(dutyRosterTurn);
			dutyRosterTurn.setStaff(null);
		}
		if (trial != null) {
			trial.removeDutyRosterTurns(dutyRosterTurn);
			dutyRosterTurn.setTrial(null);
		}
		if (visitScheduleItem != null) {
			visitScheduleItem.removeDutyRosterTurns(dutyRosterTurn);
			dutyRosterTurn.setVisitScheduleItem(null);
		}
		ServiceUtil.removeNotifications(dutyRosterTurn.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		dutyRosterTurnDao.remove(dutyRosterTurn);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (staff != null) {
			if (result.getTrial() != null) {
				ServiceUtil.logSystemMessage(staff, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			} else if (result.getVisitScheduleItem() != null) {
				ServiceUtil
						.logSystemMessage(staff, result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			} else {
				logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			}
		}
		if (trial != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(trial, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			}
		} else if (visitScheduleItem != null) {
			if (result.getStaff() != null) {
				ServiceUtil
						.logSystemMessage(visitScheduleItem.getTrial(), result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED, result, null, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_DELETED,
						result, null, journalEntryDao);
			}
		}
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleDeleteSelfRegistrationCourseParticipation(
			AuthenticationVO auth, Long courseParticipationStatusEntryId) throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId, courseParticipationStatusEntryDao);
		checkSelfRegistrationCourseParticipationStatusEntryDeletion(courseParticipation);
		Staff staff = courseParticipation.getStaff();
		Course course = courseParticipation.getCourse();
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		staff.removeParticipations(courseParticipation);
		courseParticipation.setStaff(null);
		course.removeParticipations(courseParticipation);
		courseParticipation.setCourse(null);
		ServiceUtil.removeNotifications(courseParticipation.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		courseParticipationStatusEntryDao.remove(courseParticipation);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		logSystemMessage(course, result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.staff.StaffService#deleteStaff(Long)
	 */
	@Override
	protected StaffOutVO handleDeleteStaff(AuthenticationVO auth, Long staffId, boolean defer, boolean force, String deferredDeleteReason, Integer maxInstances,
			Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		StaffDao staffDao = this.getStaffDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		User user = CoreUtil.getUser();
		Staff identity = user.getIdentity();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		StaffOutVO result;
		if (!force && defer) {
			Staff originalStaff = CheckIDUtil.checkStaffId(staffId, staffDao);
			if (identity != null && identity.equals(originalStaff)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_ACTIVE_IDENTITY);
			}
			StaffOutVO original = staffDao.toStaffOutVO(originalStaff, maxInstances, maxParentDepth, maxChildrenDepth);
			staffDao.evict(originalStaff);
			Staff staff = CheckIDUtil.checkStaffId(staffId, staffDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			staff.setDeferredDelete(true);
			staff.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(staff, originalStaff.getVersion(), now, user); // no opt. locking
			staffDao.update(staff);
			result = staffDao.toStaffOutVO(staff, maxInstances, maxParentDepth, maxChildrenDepth);
			logSystemMessage(staff, result, now, user, SystemMessageCodes.STAFF_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			Staff staff = CheckIDUtil.checkStaffId(staffId, staffDao, LockMode.PESSIMISTIC_WRITE);
			if (identity != null && identity.equals(staff)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_ACTIVE_IDENTITY);
			}
			result = staffDao.toStaffOutVO(staff, maxInstances, maxParentDepth, maxChildrenDepth);
			deleteStaffHelper(staff, true, user, now);
			logSystemMessage(user, result, now, user, SystemMessageCodes.STAFF_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected StaffAddressOutVO handleDeleteStaffAddress(AuthenticationVO auth, Long staffAddressId)
			throws Exception {
		StaffAddressDao addressDao = this.getStaffAddressDao();
		StaffAddress address = CheckIDUtil.checkStaffAddressId(staffAddressId, addressDao);
		Staff staff = address.getStaff();
		StaffAddressOutVO result = addressDao.toStaffAddressOutVO(address);
		staff.removeAddresses(address);
		address.setStaff(null);
		addressDao.remove(address);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.STAFF_ADDRESS_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffContactDetailValueOutVO handleDeleteStaffContactDetailValue(
			AuthenticationVO auth, Long staffContactDetailValueId) throws Exception {
		StaffContactDetailValueDao contactValueDao = this.getStaffContactDetailValueDao();
		StaffContactDetailValue contactValue = CheckIDUtil.checkStaffContactDetailValueId(staffContactDetailValueId, contactValueDao);
		Staff staff = contactValue.getStaff();
		StaffContactDetailValueOutVO result = contactValueDao.toStaffContactDetailValueOutVO(contactValue);
		staff.removeContactDetailValues(contactValue);
		contactValue.setStaff(null);
		contactValueDao.remove(contactValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.STAFF_CONTACT_DETAIL_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffStatusEntryOutVO handleDeleteStaffStatusEntry(
			AuthenticationVO auth, Long staffStatusEntryId) throws Exception {
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		StaffStatusEntry statusEntry = CheckIDUtil.checkStaffStatusEntryId(staffStatusEntryId, statusEntryDao);
		Staff staff = statusEntry.getStaff();
		StaffStatusEntryOutVO result = statusEntryDao.toStaffStatusEntryOutVO(statusEntry);
		staff.removeStatusEntries(statusEntry);
		statusEntry.setStaff(null);
		ServiceUtil.removeNotifications(statusEntry.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		statusEntryDao.remove(statusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.STAFF_STATUS_ENTRY_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffTagValueOutVO handleDeleteStaffTagValue(
			AuthenticationVO auth, Long staffTagValueId) throws Exception {
		StaffTagValueDao tagValueDao = this.getStaffTagValueDao();
		StaffTagValue tagValue = CheckIDUtil.checkStaffTagValueId(staffTagValueId, tagValueDao);
		Staff staff = tagValue.getStaff();
		StaffTagValueOutVO result = tagValueDao.toStaffTagValueOutVO(tagValue);
		staff.removeTagValues(tagValue);
		tagValue.setStaff(null);
		tagValueDao.remove(tagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.STAFF_TAG_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<UserOutVO> handleGetAccounts(AuthenticationVO auth, Long staffId, PSFVO psf)
			throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		UserDao userDao = this.getUserDao();
		Collection accounts = userDao.findByIdentity(staffId, psf);
		userDao.toUserOutVOCollection(accounts);
		return accounts;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetAddresseeInventoryStatusEntryList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		InventoryStatusEntryDao inventoryStatusEntryDao = this.getInventoryStatusEntryDao();
		Collection inventoryStatusEntries = inventoryStatusEntryDao.findByAddressee(staffId, psf);
		inventoryStatusEntryDao.toInventoryStatusEntryOutVOCollection(inventoryStatusEntries);
		return inventoryStatusEntries;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCollidingCourseInventoryBookingsByCourseParticipationStatusEntry(
			AuthenticationVO auth, Long courseParticipationStatusEntryId, Boolean isRelevantForCourseAppointments) throws Exception {
		CourseParticipationStatusEntry courseParticipationStatusEntry = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId,
				this.getCourseParticipationStatusEntryDao());
		if (isRelevantForCourseAppointments == null
				|| isRelevantForCourseAppointments.booleanValue() == courseParticipationStatusEntry.getStatus().isRelevantForCourseAppointments()) {
			Collection collidingCourseInventoryBookings = new HashSet();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Collection<InventoryBooking> courseInventoryBookings = inventoryBookingDao.findByCourseSorted(courseParticipationStatusEntry.getCourse().getId(),
					isRelevantForCourseAppointments, false);
			Iterator<CourseParticipationStatusEntry> participationsIt = this.getCourseParticipationStatusEntryDao()
					.findByStaffCourseRelevantForCourseAppointments(courseParticipationStatusEntry.getStaff().getId(), null, isRelevantForCourseAppointments).iterator();
			while (participationsIt.hasNext()) {
				CourseParticipationStatusEntry otherCourseParticipationStatusEntry = participationsIt.next();
				if (!courseParticipationStatusEntry.equals(otherCourseParticipationStatusEntry)) {
					Iterator<InventoryBooking> courseInventoryBookingsIt = courseInventoryBookings.iterator();
					while (courseInventoryBookingsIt.hasNext()) {
						InventoryBooking courseInventoryBooking = courseInventoryBookingsIt.next();
						collidingCourseInventoryBookings.addAll(inventoryBookingDao.findByCourseCalendarInterval(otherCourseParticipationStatusEntry.getCourse().getId(), null,
								courseInventoryBooking.getStart(), courseInventoryBooking.getStop(), isRelevantForCourseAppointments));
					}
				}
			}
			inventoryBookingDao.toInventoryBookingOutVOCollection(collidingCourseInventoryBookings);
			return new ArrayList<InventoryBookingOutVO>(collidingCourseInventoryBookings);
		} else {
			return new ArrayList<InventoryBookingOutVO>();
		}
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCollidingCourseInventoryBookingsByDutyRosterTurn(
			AuthenticationVO auth, Long dutyRosterTurnId, Boolean isRelevantForCourseAppointments) throws Exception {
		DutyRosterTurn dutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(dutyRosterTurnId, this.getDutyRosterTurnDao());
		Staff staff = dutyRosterTurn.getStaff();
		if (staff != null) {
			Collection collidingCourseInventoryBookings = new HashSet();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<CourseParticipationStatusEntry> it = staff.getParticipations().iterator();
			while (it.hasNext()) {
				CourseParticipationStatusEntry courseParticipationStatusEntry = it.next();
				collidingCourseInventoryBookings.addAll(inventoryBookingDao.findByCourseCalendarInterval(courseParticipationStatusEntry.getCourse().getId(), null,
						dutyRosterTurn.getStart(), dutyRosterTurn.getStop(), isRelevantForCourseAppointments));
			}
			inventoryBookingDao.toInventoryBookingOutVOCollection(collidingCourseInventoryBookings);
			return new ArrayList<InventoryBookingOutVO>(collidingCourseInventoryBookings);
		} else {
			return new ArrayList<InventoryBookingOutVO>();
		}
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCollidingCourseInventoryBookingsByStaffStatusEntry(
			AuthenticationVO auth, Long staffStatusEntryId, Boolean isRelevantForCourseAppointments) throws Exception {
		StaffStatusEntry staffStatus = CheckIDUtil.checkStaffStatusEntryId(staffStatusEntryId, this.getStaffStatusEntryDao());
		if (!staffStatus.getType().isStaffActive()) {
			Collection collidingCourseInventoryBookings = new HashSet();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<CourseParticipationStatusEntry> it = staffStatus.getStaff().getParticipations().iterator();
			while (it.hasNext()) {
				CourseParticipationStatusEntry courseParticipationStatusEntry = it.next();
				collidingCourseInventoryBookings.addAll(inventoryBookingDao.findByCourseCalendarInterval(courseParticipationStatusEntry.getCourse().getId(), null,
						staffStatus.getStart(), staffStatus.getStop(), isRelevantForCourseAppointments));
			}
			inventoryBookingDao.toInventoryBookingOutVOCollection(collidingCourseInventoryBookings);
			return new ArrayList<InventoryBookingOutVO>(collidingCourseInventoryBookings);
		} else {
			return new ArrayList<InventoryBookingOutVO>();
		}
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetCollidingDutyRosterTurns(
			AuthenticationVO auth, Long staffStatusEntryId) throws Exception {
		StaffStatusEntry staffStatus = CheckIDUtil.checkStaffStatusEntryId(staffStatusEntryId, this.getStaffStatusEntryDao());
		Collection collidingDutyRosterTurns;
		if (!staffStatus.getType().isStaffActive()) {
			DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
			collidingDutyRosterTurns = dutyRosterTurnDao
					.findByStaffTrialCalendarInterval(staffStatus.getStaff().getId(), null, null, staffStatus.getStart(), staffStatus.getStop());
			dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(collidingDutyRosterTurns);
		} else {
			collidingDutyRosterTurns = new ArrayList<DutyRosterTurnOutVO>();
		}
		return collidingDutyRosterTurns;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetCollidingStaffStatusEntries(
			AuthenticationVO auth, Long dutyRosterTurnId) throws Exception {
		DutyRosterTurn dutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(dutyRosterTurnId, this.getDutyRosterTurnDao());
		Collection collidingStaffStatusEntries;
		Staff staff = dutyRosterTurn.getStaff();
		if (staff != null) {
			StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
			collidingStaffStatusEntries = staffStatusEntryDao.findByStaffInterval(staff.getId(), dutyRosterTurn.getStart(), dutyRosterTurn.getStop(), false, true, false);
			staffStatusEntryDao.toStaffStatusEntryOutVOCollection(collidingStaffStatusEntries);
		} else {
			collidingStaffStatusEntries = new ArrayList<StaffStatusEntryOutVO>();
		}
		return collidingStaffStatusEntries;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetCollidingStaffStatusEntriesInterval(
			AuthenticationVO auth, Long staffId, Date start, Date stop) throws Exception {
		CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
		Collection collidingStaffStatusEntries = staffStatusEntryDao
				.findByStaffInterval(staffId, CommonUtil.dateToTimestamp(start), CommonUtil.dateToTimestamp(stop), false, true, false);
		staffStatusEntryDao.toStaffStatusEntryOutVOCollection(collidingStaffStatusEntries);
		return collidingStaffStatusEntries;
	}

	@Override
	protected Collection<DateCountVO> handleGetCollidingStaffStatusIntervalDayCounts(AuthenticationVO auth, Long departmentId, Date start, Date stop) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		ArrayList<DateCountVO> result = new ArrayList<DateCountVO>();
		Iterator<Date> datesIt = (new DateInterval(start, stop)).getEnumeratedDates().iterator();
		StaffDao staffDao = this.getStaffDao();
		while (datesIt.hasNext()) {
			Date date = datesIt.next();
			long count = staffDao.getCountByDepartmentStatusInterval(departmentId,
					CommonUtil.dateToTimestamp(DateCalc.getStartOfDay(date)), CommonUtil.dateToTimestamp(DateCalc.getEndOfDay(date)), false, true, false);
			if (count > 0l) {
				result.add(new DateCountVO(count, date));
			}
		}
		return result;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetCollidingVisitScheduleItems(
			AuthenticationVO auth, Long staffStatusEntryId, Long trialDepartmentId) throws Exception {
		StaffStatusEntry staffStatusEntry = CheckIDUtil.checkStaffStatusEntryId(staffStatusEntryId, this.getStaffStatusEntryDao());
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (!staffStatusEntry.getType().isStaffActive()) {
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			Collection collidingVisitScheduleItems = visitScheduleItemDao.findByDepartmentTravelInterval(trialDepartmentId, staffStatusEntry.getStart(),
					staffStatusEntry.getStop(), null);
			visitScheduleItemDao.toVisitScheduleItemOutVOCollection(collidingVisitScheduleItems);
			return collidingVisitScheduleItems;
		} else {
			return new ArrayList<VisitScheduleItemOutVO>();
		}
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> handleGetCompanyContactMaintenanceItemList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		Collection maintenanceScheduleItems = maintenanceScheduleItemDao.findByCompanyContact(staffId, psf);
		maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVOCollection(maintenanceScheduleItems);
		return maintenanceScheduleItems;
	}

	@Override
	protected CvPositionOutVO handleGetCvPosition(AuthenticationVO auth, Long cvPositionId)
			throws Exception {
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		CvPosition cvPosition = CheckIDUtil.checkCvPositionId(cvPositionId, cvPositionDao);
		CvPositionOutVO result = cvPositionDao.toCvPositionOutVO(cvPosition);
		return result;
	}

	@Override
	protected long handleGetCvPositionCount(AuthenticationVO auth, Long staffId, Long sectionId) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (sectionId != null) {
			CheckIDUtil.checkCvSectionId(sectionId, this.getCvSectionDao());
		}
		return this.getCvPositionDao().getCount(staffId, sectionId, null);
	}

	@Override
	protected Collection<CvPositionOutVO> handleGetCvPositionList(AuthenticationVO auth, Long staffId,
			PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		Collection cvPositions = cvPositionDao.findByStaffSection(staffId, null, null, psf);
		cvPositionDao.toCvPositionOutVOCollection(cvPositions);
		return cvPositions;
	}

	@Override
	protected Collection<CvPositionPDFVO> handleGetCvPositions(AuthenticationVO auth, Long staffId,
			Long sectionId) throws Exception {
		CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		if (sectionId != null) {
			CheckIDUtil.checkCvSectionId(sectionId, this.getCvSectionDao());
		}
		return ServiceUtil.loadCvPositions(staffId, sectionId, this.getCvPositionDao(), this.getCourseParticipationStatusEntryDao());
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetDutyRoster(AuthenticationVO auth, Long staffId,
			PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Collection dutyRosterTurns = dutyRosterTurnDao.findByStaff(staffId, psf);
		dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(dutyRosterTurns);
		return dutyRosterTurns;
	}

	@Override
	protected long handleGetDutyRosterCount(AuthenticationVO auth, Long staffId) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getDutyRosterTurnDao().getCount(staffId, null);
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetDutyRosterInterval(
			AuthenticationVO auth, Long departmentId, Long staffCategoryId, String calendar, Date from, Date to, boolean sort)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (staffCategoryId != null) {
			CheckIDUtil.checkStaffCategoryId(staffCategoryId, this.getStaffCategoryDao());
		}
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Collection dutyRosterTurns = dutyRosterTurnDao.findByDepartmentCategoryCalendarInterval(departmentId, staffCategoryId, true, calendar, CommonUtil.dateToTimestamp(from),
				CommonUtil.dateToTimestamp(to));
		dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(dutyRosterTurns);
		if (sort) {
			dutyRosterTurns = new ArrayList(dutyRosterTurns);
			Collections.sort((ArrayList) dutyRosterTurns, new DutyRosterTurnIntervalComparator(false));
		}
		return dutyRosterTurns;
	}

	@Override
	protected DutyRosterTurnOutVO handleGetDutyRosterTurn(AuthenticationVO auth, Long dutyRosterTurnId)
			throws Exception {
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		DutyRosterTurn dutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(dutyRosterTurnId, dutyRosterTurnDao);
		DutyRosterTurnOutVO result = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		return result;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> handleGetExpiringParticipations(
			AuthenticationVO auth, Date today, Long staffId, VariablePeriod reminderPeriod,
			Long reminderPeriodDays, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		ServiceUtil.checkReminderPeriod(reminderPeriod, reminderPeriodDays);
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		Collection expiringParticipations = courseParticipationStatusEntryDao.findExpiring(today, null, null, null, null, staffId, null, reminderPeriod, reminderPeriodDays, true,
				psf);
		courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVOCollection(expiringParticipations);
		return expiringParticipations;
	}

	@Override
	protected Collection<CourseOutVO> handleGetInstitutionCourseList(AuthenticationVO auth, Long staffId,
			PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		CourseDao courseDao = this.getCourseDao();
		Collection courses = courseDao.findByInstitution(staffId, psf);
		courseDao.toCourseOutVOCollection(courses);
		return courses;
	}

	@Override
	protected Collection<CvPositionOutVO> handleGetInstitutionCvPositionList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		Collection cvPositions = cvPositionDao.findByInstitution(staffId, psf);
		cvPositionDao.toCvPositionOutVOCollection(cvPositions);
		return cvPositions;
	}

	@Override
	protected Collection<LecturerOutVO> handleGetLectureList(AuthenticationVO auth, Long staffId, Long competenceId,
			PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (competenceId != null) {
			CheckIDUtil.checkLecturerCompetenceId(competenceId, this.getLecturerCompetenceDao());
		}
		LecturerDao lecturerDao = this.getLecturerDao();
		Collection lectures = lecturerDao.findByCourseStaffCompetence(null, staffId, competenceId, psf);
		lecturerDao.toLecturerOutVOCollection(lectures);
		return lectures;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetOnBehalfOfInventoryBookingList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByOnBehalfOf(staffId, psf);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		return inventoryBookings;
	}

	@Override
	protected Collection<InventoryStatusEntryOutVO> handleGetOriginatorInventoryStatusEntryList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		InventoryStatusEntryDao inventoryStatusEntryDao = this.getInventoryStatusEntryDao();
		Collection inventoryStatusEntries = inventoryStatusEntryDao.findByOriginator(staffId, psf);
		inventoryStatusEntryDao.toInventoryStatusEntryOutVOCollection(inventoryStatusEntries);
		return inventoryStatusEntries;
	}

	@Override
	protected Collection<InventoryOutVO> handleGetOwnershipInventoryList(AuthenticationVO auth, Long staffId,
			PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		InventoryDao inventoryDao = this.getInventoryDao();
		Collection inventories = inventoryDao.findByOwner(staffId, psf);
		inventoryDao.toInventoryOutVOCollection(inventories);
		return inventories;
	}

	@Override
	protected Collection<ProbandOutVO> handleGetPatientList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		ProbandDao probandDao = this.getProbandDao();
		Collection probands = probandDao.findByPhysician(staffId, psf);
		probandDao.toProbandOutVOCollection(probands);
		return probands;
	}

	@Override
	protected Collection<MaintenanceScheduleItemOutVO> handleGetResponsiblePersonMaintenanceItemList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		MaintenanceScheduleItemDao maintenanceScheduleItemDao = this.getMaintenanceScheduleItemDao();
		Collection maintenanceScheduleItems = maintenanceScheduleItemDao.findByResponsiblePerson(staffId, staffId, psf);
		maintenanceScheduleItemDao.toMaintenanceScheduleItemOutVOCollection(maintenanceScheduleItems);
		return maintenanceScheduleItems;
	}

	@Override
	protected ShiftDurationSummaryVO handleGetShiftDurationSummary(
			AuthenticationVO auth, Long trialId, String calendar, Date from, Date to)
			throws Exception {
		ShiftDurationSummaryVO result = new ShiftDurationSummaryVO();
		if (trialId != null) {
			result.setTrial(this.getTrialDao().toTrialOutVO(CheckIDUtil.checkTrialId(trialId, this.getTrialDao())));
		}
		result.setStart(from);
		result.setStop(to);
		result.setCalendar(calendar);
		ServiceUtil.populateShiftDurationSummary(false, result, this.getDutyRosterTurnDao(), this.getHolidayDao(), this.getStaffStatusEntryDao());
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.staff.StaffService#getStaff(Long)
	 */
	@Override
	protected StaffOutVO handleGetStaff(AuthenticationVO auth, Long staffId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		Staff staff = CheckIDUtil.checkStaffId(staffId, staffDao);
		StaffOutVO result = staffDao.toStaffOutVO(staff, maxInstances, maxParentDepth, maxChildrenDepth);
		return result;
	}

	@Override
	protected StaffAddressOutVO handleGetStaffAddress(AuthenticationVO auth, Long staffAddressId)
			throws Exception {
		StaffAddressDao addressDao = this.getStaffAddressDao();
		StaffAddress address = CheckIDUtil.checkStaffAddressId(staffAddressId, addressDao);
		StaffAddressOutVO result = addressDao.toStaffAddressOutVO(address);
		return result;
	}

	@Override
	protected long handleGetStaffAddressCount(
			AuthenticationVO auth, Long staffId) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getStaffAddressDao().getCount(staffId, null, null, null);
	}

	@Override
	protected Collection<StaffAddressOutVO> handleGetStaffAddressList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		StaffAddressDao addressDao = this.getStaffAddressDao();
		Collection staffAddresses = addressDao.findByStaff(staffId, null, null, null, psf);
		addressDao.toStaffAddressOutVOCollection(staffAddresses);
		return staffAddresses;
	}

	@Override
	protected StaffContactDetailValueOutVO handleGetStaffContactDetailValue(
			AuthenticationVO auth, Long staffContactDetailValueId) throws Exception {
		StaffContactDetailValueDao contactValueDao = this.getStaffContactDetailValueDao();
		StaffContactDetailValue contactValue = CheckIDUtil.checkStaffContactDetailValueId(staffContactDetailValueId, contactValueDao);
		StaffContactDetailValueOutVO result = contactValueDao.toStaffContactDetailValueOutVO(contactValue);
		return result;
	}

	@Override
	protected long handleGetStaffContactDetailValueCount(
			AuthenticationVO auth, Long staffId, Boolean na) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getStaffContactDetailValueDao().getCount(staffId, null, na, null, null);
	}

	@Override
	protected Collection<StaffContactDetailValueOutVO> handleGetStaffContactDetailValueList(
			AuthenticationVO auth, Long staffId, Boolean na, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		StaffContactDetailValueDao contactValueDao = this.getStaffContactDetailValueDao();
		Collection staffContactValues = contactValueDao.findByStaff(staffId, null, na, null, null, psf);
		contactValueDao.toStaffContactDetailValueOutVOCollection(staffContactValues);
		return staffContactValues;
	}

	@Override
	protected StaffImageOutVO handleGetStaffImage(AuthenticationVO auth,
			Long staffId) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		Staff staff = CheckIDUtil.checkStaffId(staffId, staffDao);
		StaffImageOutVO result = staffDao.toStaffImageOutVO(staff);
		return result;
	}

	@Override
	protected Collection<StaffOutVO> handleGetStaffList(AuthenticationVO auth, Long staffId, Long departmentId, Integer maxInstances, PSFVO psf) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, staffDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection staff = staffDao.findByIdDepartment(staffId, departmentId, psf);
		ArrayList<StaffOutVO> result = new ArrayList<StaffOutVO>(staff.size());
		Iterator<Staff> staffIt = staff.iterator();
		while (staffIt.hasNext()) {
			result.add(staffDao.toStaffOutVO(staffIt.next(), maxInstances));
		}
		return result;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetStaffStatus(AuthenticationVO auth, Date now,
			Long staffId, Long departmentId, Long staffCategoryId,
			Boolean staffActive, Boolean hideAvailability, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (staffCategoryId != null) {
			CheckIDUtil.checkStaffCategoryId(staffCategoryId, this.getStaffCategoryDao());
		}
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		Collection staffStatusEntries = statusEntryDao.findStaffStatus(CommonUtil.dateToTimestamp(now), staffId, departmentId, staffCategoryId, staffActive, hideAvailability, psf);
		statusEntryDao.toStaffStatusEntryOutVOCollection(staffStatusEntries);
		return staffStatusEntries;
	}

	@Override
	protected StaffStatusEntryOutVO handleGetStaffStatusEntry(
			AuthenticationVO auth, Long staffStatusEntryId) throws Exception {
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		StaffStatusEntry statusEntry = CheckIDUtil.checkStaffStatusEntryId(staffStatusEntryId, statusEntryDao);
		StaffStatusEntryOutVO result = statusEntryDao.toStaffStatusEntryOutVO(statusEntry);
		return result;
	}

	@Override
	protected long handleGetStaffStatusEntryCount(AuthenticationVO auth, Long staffId) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getStaffStatusEntryDao().getCount(staffId);
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetStaffStatusEntryInterval(
			AuthenticationVO auth, Long departmentId, Long staffCategoryId, Boolean hideAvailability, Date from, Date to, boolean sort)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (staffCategoryId != null) {
			CheckIDUtil.checkStaffCategoryId(staffCategoryId, this.getStaffCategoryDao());
		}
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		Collection staffStatusEntries = statusEntryDao.findByDepartmentCategoryInterval(departmentId, staffCategoryId, CommonUtil.dateToTimestamp(from),
				CommonUtil.dateToTimestamp(to), null, null, hideAvailability);
		statusEntryDao.toStaffStatusEntryOutVOCollection(staffStatusEntries);
		if (sort) {
			staffStatusEntries = new ArrayList(staffStatusEntries);
			Collections.sort((ArrayList) staffStatusEntries, new StaffStatusEntryIntervalComparator(false));
		}
		return staffStatusEntries;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetStaffStatusEntryList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		Collection staffStatusEntries = statusEntryDao.findByStaff(staffId, psf);
		statusEntryDao.toStaffStatusEntryOutVOCollection(staffStatusEntries);
		return staffStatusEntries;
	}

	@Override
	protected StaffTagValueOutVO handleGetStaffTagValue(
			AuthenticationVO auth, Long staffTagValueId) throws Exception {
		StaffTagValueDao tagValueDao = this.getStaffTagValueDao();
		StaffTagValue tagValue = CheckIDUtil.checkStaffTagValueId(staffTagValueId, tagValueDao);
		StaffTagValueOutVO result = tagValueDao.toStaffTagValueOutVO(tagValue);
		return result;
	}

	@Override
	protected long handleGetStaffTagValueCount(AuthenticationVO auth, Long staffId)
			throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getStaffTagValueDao().getCount(staffId);
	}

	@Override
	protected Collection<StaffTagValueOutVO> handleGetStaffTagValueList(
			AuthenticationVO auth, Long staffId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		StaffTagValueDao tagValueDao = this.getStaffTagValueDao();
		Collection staffTagValues = tagValueDao.findByStaff(staffId, psf);
		tagValueDao.toStaffTagValueOutVOCollection(staffTagValues);
		return staffTagValues;
	}

	@Override
	protected Collection<TeamMemberOutVO> handleGetTrialMembershipList(
			AuthenticationVO auth, Long staffId, Long roleId, PSFVO psf) throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (roleId != null) {
			CheckIDUtil.checkTeamMemberRoleId(roleId, this.getTeamMemberRoleDao());
		}
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		Collection teamMembers = teamMemberDao.findByTrialStaffRole(null, staffId, roleId, null, psf);
		teamMemberDao.toTeamMemberOutVOCollection(teamMembers);
		return teamMembers;
	}

	@Override
	protected Collection<CourseOutVO> handleGetUpcomingCourses(
			AuthenticationVO auth, Date now, Long staffId, PSFVO psf) throws Exception {
		CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		CourseDao courseDao = this.getCourseDao();
		Collection upcomingCourses = courseDao.findUpcoming(CommonUtil.dateToTimestamp(now), staffId, psf);
		courseDao.toCourseOutVOCollection(upcomingCourses);
		return upcomingCourses;
	}

	@Override
	protected Collection<CourseOutVO> handleGetUpcomingRenewalCourses(
			AuthenticationVO auth, Date now, Long courseId, Long staffId)
			throws Exception {
		CourseDao courseDao = this.getCourseDao();
		CheckIDUtil.checkCourseId(courseId, courseDao);
		CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		Collection upcomingRenewalCourses = courseDao.findUpcomingRenewals(CommonUtil.dateToTimestamp(now), courseId, staffId);
		courseDao.toCourseOutVOCollection(upcomingRenewalCourses);
		return upcomingRenewalCourses;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleParticipateSelfRegistrationCourse(
			AuthenticationVO auth, CourseParticipationStatusEntryInVO newCourseParticipationStatusEntry)
			throws Exception {
		ServiceUtil.checkAddCourseParticipationStatusEntryInput(newCourseParticipationStatusEntry, false, true,
				this.getStaffDao(), this.getCourseDao(), this.getCvSectionDao(), this.getTrainingRecordSectionDao(), this.getCourseParticipationStatusTypeDao(),
				this.getCourseParticipationStatusEntryDao());
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = courseParticipationStatusEntryDao.courseParticipationStatusEntryInVOToEntity(newCourseParticipationStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(courseParticipation, now, user);
		courseParticipation = courseParticipationStatusEntryDao.create(courseParticipation);
		ServiceUtil.notifyParticipationStatusUpdated(null, courseParticipation, true, now, this.getNotificationDao());
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(courseParticipation.getStaff(), result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_CREATED, result, null, journalEntryDao);
		logSystemMessage(courseParticipation.getCourse(), result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_CREATED, result, null,
				journalEntryDao);
		return result;
	}

	@Override
	protected CvPDFVO handleRenderCvPDF(AuthenticationVO auth, Long staffId) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		Staff staff = CheckIDUtil.checkStaffId(staffId, staffDao);
		if (!staff.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_POSITION_STAFF_NOT_PERSON);
		}
		ArrayList<StaffOutVO> staffVOs = new ArrayList<StaffOutVO>();
		StaffOutVO staffVO = staffDao.toStaffOutVO(staff,
				Settings.getInt(CVPDFSettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.CV_PDF, CVPDFDefaultSettings.GRAPH_MAX_STAFF_INSTANCES));
		staffVOs.add(staffVO);
		CVPDFPainter painter = ServiceUtil.createCVPDFPainter(staffVOs, this.getStaffDao(), this.getCvSectionDao(), this.getCvPositionDao(),
				this.getCourseParticipationStatusEntryDao(), this.getStaffAddressDao());
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		CvPDFVO result = painter.getPdfVO();
		logSystemMessage(staff, staffVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.CV_PDF_RENDERED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected DutyRosterTurnOutVO handleSelfAllocateDutyRosterTurn(
			AuthenticationVO auth, Long dutyRosterTurnId, Long version, boolean allocate) throws Exception {
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		DutyRosterTurn dutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(dutyRosterTurnId, dutyRosterTurnDao);
		if (!dutyRosterTurn.isSelfAllocatable()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_NOT_SELF_ALLOCATABLE);
		}
		DutyRosterTurnOutVO original = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		User user = CoreUtil.getUser();
		Staff identity = user.getIdentity();
		if (identity == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_NO_ACTIVE_IDENTITY);
		}
		Trial trial = dutyRosterTurn.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
		if (visitScheduleItem != null) {
			ServiceUtil.checkTrialLocked(visitScheduleItem.getTrial());
		}
		Staff oldStaff = dutyRosterTurn.getStaff();
		if (trial != null && dutyRosterTurn.getStart() != null && trial.isDutySelfAllocationLocked() && oldStaff != null) {
			if (trial.getDutySelfAllocationLockedUntil() == null && trial.getDutySelfAllocationLockedFrom() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_SELF_ALLOCATION_LOCKED,
						CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			} else if (trial.getDutySelfAllocationLockedUntil() != null && trial.getDutySelfAllocationLockedUntil().compareTo(dutyRosterTurn.getStart()) > 0) {
				throw L10nUtil.initServiceException(
						ServiceExceptionCodes.DUTY_ROSTER_TURN_SELF_ALLOCATION_LOCKED_UNTIL,
						Settings.getSimpleDateFormat(SettingCodes.EXCEPTION_DATE_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.EXCEPTION_DATE_TIME_PATTERN, Locales.USER).format(
								trial.getDutySelfAllocationLockedUntil()),
						CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			} else if (trial.getDutySelfAllocationLockedFrom() != null && trial.getDutySelfAllocationLockedFrom().compareTo(dutyRosterTurn.getStart()) <= 0) {
				throw L10nUtil.initServiceException(
						ServiceExceptionCodes.DUTY_ROSTER_TURN_SELF_ALLOCATION_LOCKED_FROM,
						Settings.getSimpleDateFormat(SettingCodes.EXCEPTION_DATE_TIME_PATTERN, Bundle.SETTINGS, DefaultSettings.EXCEPTION_DATE_TIME_PATTERN, Locales.USER).format(
								trial.getDutySelfAllocationLockedFrom()),
						CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			}
		}
		if (allocate) {
			if (oldStaff != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_ALREADY_ALLOCATED);
			}
			checkDutyRosterTurnStaff(identity);
			DutyRosterTurnInVO dutyRosterTurnIn = dutyRosterTurnDao.toDutyRosterTurnInVO(dutyRosterTurn);
			dutyRosterTurnIn.setStaffId(identity.getId()); // ok, no argument manipulated
			if ((new DutyRosterTurnCollisionFinder(this.getStaffDao(), dutyRosterTurnDao)).collides(dutyRosterTurnIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_MAX_OVERLAPPING_EXCEEDED, identity.getMaxOverlappingShifts());
			}
		} else {
			if (!identity.equals(oldStaff)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DUTY_ROSTER_TURN_ACTIVE_IDENTITY_NOT_EQUAL_TO_OLD_STAFF);
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		CoreUtil.modifyVersion(dutyRosterTurn, version.longValue(), now, user);
		if (allocate) {
			dutyRosterTurn.setStaff(identity);
			identity.addDutyRosterTurns(dutyRosterTurn);
		} else {
			dutyRosterTurn.setStaff(null);
			identity.removeDutyRosterTurns(dutyRosterTurn);
		}
		dutyRosterTurnDao.update(dutyRosterTurn);
		notifyDutyRosterTurn(oldStaff, dutyRosterTurn, now);
		DutyRosterTurnOutVO result = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (result.getTrial() != null) {
			ServiceUtil.logSystemMessage(identity, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
		} else if (result.getVisitScheduleItem() != null) {
			ServiceUtil.logSystemMessage(identity, result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original,
					journalEntryDao);
		} else {
			logSystemMessage(identity, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
		}
		if (trial != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(trial, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			}
		} else if (visitScheduleItem != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original,
						journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED,
						result, original, journalEntryDao);
			}
		}
		return result;
	}

	@Override
	protected StaffImageOutVO handleSetStaffImage(AuthenticationVO auth,
			StaffImageInVO staffImage) throws Exception {
		StaffDao staffDao = this.getStaffDao();
		Staff originalStaff = CheckIDUtil.checkStaffId(staffImage.getId(), staffDao);
		checkStaffImageInput(originalStaff.isPerson(), staffImage);
		StaffImageOutVO original = staffDao.toStaffImageOutVO(originalStaff);
		boolean hasImage = original.getHasImage();
		boolean cleared = staffImage.getDatas() == null || staffImage.getDatas().length == 0;
		staffDao.evict(originalStaff);
		Staff staff = staffDao.staffImageInVOToEntity(staffImage);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalStaff, staff, now, user);
		staffDao.update(staff);
		StaffImageOutVO result = staffDao.toStaffImageOutVO(staff);
		logSystemMessage(staff, staffDao.toStaffOutVO(staff), now, user, cleared ? SystemMessageCodes.STAFF_IMAGE_CLEARED
				: hasImage ? SystemMessageCodes.STAFF_IMAGE_UPDATED
						: SystemMessageCodes.STAFF_IMAGE_CREATED,
				result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CvPositionOutVO handleUpdateCvPosition(
			AuthenticationVO auth, CvPositionInVO modifiedCvPosition) throws Exception {
		CvPositionDao cvPositionDao = this.getCvPositionDao();
		CvPosition originalCvPosition = CheckIDUtil.checkCvPositionId(modifiedCvPosition.getId(), cvPositionDao);
		checkCvPositionInput(modifiedCvPosition);
		CvPositionOutVO original = cvPositionDao.toCvPositionOutVO(originalCvPosition);
		cvPositionDao.evict(originalCvPosition);
		CvPosition cvPosition = cvPositionDao.cvPositionInVOToEntity(modifiedCvPosition);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalCvPosition, cvPosition, now, user);
		cvPositionDao.update(cvPosition);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		CvPositionOutVO result = cvPositionDao.toCvPositionOutVO(cvPosition);
		logSystemMessage(cvPosition.getStaff(), result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_UPDATED, result, original, journalEntryDao);
		Staff institution = cvPosition.getInstitution();
		if (institution != null) {
			logSystemMessage(institution, result.getStaff(), now, user, SystemMessageCodes.CV_POSITION_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected DutyRosterTurnOutVO handleUpdateDutyRosterTurn(
			AuthenticationVO auth, DutyRosterTurnInVO modifiedDutyRosterTurn) throws Exception {
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		DutyRosterTurn originalDutyRosterTurn = CheckIDUtil.checkDutyRosterTurnId(modifiedDutyRosterTurn.getId(), dutyRosterTurnDao);
		checkDutyRosterTurnInput(modifiedDutyRosterTurn);
		Staff oldStaff = originalDutyRosterTurn.getStaff();
		DutyRosterTurnOutVO original = dutyRosterTurnDao.toDutyRosterTurnOutVO(originalDutyRosterTurn);
		dutyRosterTurnDao.evict(originalDutyRosterTurn);
		DutyRosterTurn dutyRosterTurn = dutyRosterTurnDao.dutyRosterTurnInVOToEntity(modifiedDutyRosterTurn);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalDutyRosterTurn, dutyRosterTurn, now, user);
		dutyRosterTurnDao.update(dutyRosterTurn);
		notifyDutyRosterTurn(oldStaff, dutyRosterTurn, now);
		DutyRosterTurnOutVO result = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Staff staff = dutyRosterTurn.getStaff();
		if (staff != null) {
			if (result.getTrial() != null) {
				ServiceUtil.logSystemMessage(staff, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			} else if (result.getVisitScheduleItem() != null) {
				ServiceUtil.logSystemMessage(staff, result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original,
						journalEntryDao);
			} else {
				logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			}
		}
		Trial trial = dutyRosterTurn.getTrial();
		VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
		if (trial != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(trial, result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original, journalEntryDao);
			}
		} else if (visitScheduleItem != null) {
			if (result.getStaff() != null) {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getStaff(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED, result, original,
						journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getVisitScheduleItem().getTrial(), now, user, SystemMessageCodes.DUTY_ROSTER_TURN_UPDATED,
						result, original, journalEntryDao);
			}
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.staff.StaffService#updateStaff(StaffInVO)
	 */
	@Override
	protected StaffOutVO handleUpdateStaff(AuthenticationVO auth, StaffInVO modifiedStaff, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		StaffDao staffDao = this.getStaffDao();
		Staff originalStaff = CheckIDUtil.checkStaffId(modifiedStaff.getId(), staffDao, LockMode.PESSIMISTIC_WRITE);
		checkStaffInput(modifiedStaff);
		if (originalStaff.isPerson() != modifiedStaff.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_PERSON_FLAG_CHANGED);
		}
		StaffOutVO original = staffDao.toStaffOutVO(originalStaff, maxInstances, maxParentDepth, maxChildrenDepth);
		staffDao.evict(originalStaff);
		Staff staff = staffDao.staffInVOToEntity(modifiedStaff);
		checkStaffLoop(staff);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalStaff, staff, now, user);
		staffDao.update(staff);
		StaffOutVO result = staffDao.toStaffOutVO(staff, maxInstances, maxParentDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(staff, result, now, user, SystemMessageCodes.STAFF_UPDATED, result, original, journalEntryDao);
		return result;
	}

	@Override
	protected StaffAddressOutVO handleUpdateStaffAddress(
			AuthenticationVO auth, StaffAddressInVO modifiedStaffAddress) throws Exception {
		StaffAddressDao addressDao = this.getStaffAddressDao();
		StaffAddress originalAddress = CheckIDUtil.checkStaffAddressId(modifiedStaffAddress.getId(), addressDao);
		checkStaffAddressInput(modifiedStaffAddress);
		StaffAddressOutVO original = addressDao.toStaffAddressOutVO(originalAddress);
		addressDao.evict(originalAddress);
		StaffAddress address = addressDao.staffAddressInVOToEntity(modifiedStaffAddress);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalAddress, address, now, user);
		addressDao.update(address);
		StaffAddressOutVO result = addressDao.toStaffAddressOutVO(address);
		logSystemMessage(address.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_ADDRESS_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffContactDetailValueOutVO handleUpdateStaffContactDetailValue(
			AuthenticationVO auth, StaffContactDetailValueInVO modifiedStaffContactDetailValue)
			throws Exception {
		StaffContactDetailValueDao contactValueDao = this.getStaffContactDetailValueDao();
		StaffContactDetailValue originalContactValue = CheckIDUtil.checkStaffContactDetailValueId(modifiedStaffContactDetailValue.getId(), contactValueDao);
		checkStaffContactDetailValueInput(modifiedStaffContactDetailValue);
		StaffContactDetailValueOutVO original = contactValueDao.toStaffContactDetailValueOutVO(originalContactValue);
		contactValueDao.evict(originalContactValue);
		StaffContactDetailValue contactValue = contactValueDao.staffContactDetailValueInVOToEntity(modifiedStaffContactDetailValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalContactValue, contactValue, now, user);
		contactValueDao.update(contactValue);
		StaffContactDetailValueOutVO result = contactValueDao.toStaffContactDetailValueOutVO(contactValue);
		logSystemMessage(contactValue.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_CONTACT_DETAIL_VALUE_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffStatusEntryOutVO handleUpdateStaffStatusEntry(
			AuthenticationVO auth, StaffStatusEntryInVO modifiedStaffStatusEntry) throws Exception {
		StaffStatusEntryDao statusEntryDao = this.getStaffStatusEntryDao();
		StaffStatusEntry originalStatusEntry = CheckIDUtil.checkStaffStatusEntryId(modifiedStaffStatusEntry.getId(), statusEntryDao);
		checkStaffStatusEntryInput(modifiedStaffStatusEntry);
		StaffStatusEntryOutVO original = statusEntryDao.toStaffStatusEntryOutVO(originalStatusEntry);
		statusEntryDao.evict(originalStatusEntry);
		StaffStatusEntry statusEntry = statusEntryDao.staffStatusEntryInVOToEntity(modifiedStaffStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalStatusEntry, statusEntry, now, user);
		statusEntryDao.update(statusEntry);
		notifyStaffInactive(statusEntry, now);
		StaffStatusEntryOutVO result = statusEntryDao.toStaffStatusEntryOutVO(statusEntry);
		logSystemMessage(statusEntry.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_STATUS_ENTRY_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StaffTagValueOutVO handleUpdateStaffTagValue(
			AuthenticationVO auth, StaffTagValueInVO modifiedStaffTagValue)
			throws Exception {
		StaffTagValueDao tagValueDao = this.getStaffTagValueDao();
		StaffTagValue originalTagValue = CheckIDUtil.checkStaffTagValueId(modifiedStaffTagValue.getId(), tagValueDao);
		checkStaffTagValueInput(modifiedStaffTagValue);
		StaffTagValueOutVO original = tagValueDao.toStaffTagValueOutVO(originalTagValue);
		tagValueDao.evict(originalTagValue);
		StaffTagValue tagValue = tagValueDao.staffTagValueInVOToEntity(modifiedStaffTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTagValue, tagValue, now, user);
		tagValueDao.update(tagValue);
		StaffTagValueOutVO result = tagValueDao.toStaffTagValueOutVO(tagValue);
		logSystemMessage(tagValue.getStaff(), result.getStaff(), now, user, SystemMessageCodes.STAFF_TAG_VALUE_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleUserUpdateCourseParticipationStatusEntry(
			AuthenticationVO auth, CourseParticipationStatusEntryInVO modifiedCourseParticipationStatusEntry)
			throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry originalCourseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(modifiedCourseParticipationStatusEntry.getId(),
				courseParticipationStatusEntryDao);
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		ServiceUtil.checkUpdateCourseParticipationStatusEntryInput(originalCourseParticipation, modifiedCourseParticipationStatusEntry, false,
				this.getCvSectionDao(), this.getTrainingRecordSectionDao(), courseParticipationStatusTypeDao, this.getCourseParticipationStatusEntryDao());
		CourseParticipationStatusEntryOutVO original = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(originalCourseParticipation);
		CourseParticipationStatusType originalCourseParticipationStatusType = originalCourseParticipation.getStatus();
		courseParticipationStatusTypeDao.evict(originalCourseParticipationStatusType);
		courseParticipationStatusEntryDao.evict(originalCourseParticipation);
		CourseParticipationStatusEntry courseParticipation = courseParticipationStatusEntryDao.courseParticipationStatusEntryInVOToEntity(modifiedCourseParticipationStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalCourseParticipation, courseParticipation, now, user);
		courseParticipationStatusEntryDao.update(courseParticipation);
		ServiceUtil.notifyParticipationStatusUpdated(originalCourseParticipationStatusType, courseParticipation, true, now, this.getNotificationDao());
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(courseParticipation.getStaff(), result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		logSystemMessage(courseParticipation.getCourse(), result.getStaff(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		return result;
	}

	private void notifyDutyRosterTurn(Staff oldStaff, DutyRosterTurn dutyRosterTurn, Date now) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		Staff newStaff = dutyRosterTurn.getStaff();
		if (oldStaff != null && newStaff != null) {
			if (oldStaff.equals(newStaff)) {
				notificationDao.addNotification(dutyRosterTurn, oldStaff, null, now, null);
			} else {
				notificationDao.addNotification(dutyRosterTurn, oldStaff, false, now, null);
				notificationDao.addNotification(dutyRosterTurn, newStaff, true, now, null);
			}
		} else if (oldStaff == null && newStaff != null) {
			notificationDao.addNotification(dutyRosterTurn, newStaff, true, now, null);
		} else if (oldStaff != null && newStaff == null) {
			notificationDao.addNotification(dutyRosterTurn, oldStaff, false, now, null);
		} else {
			return;
		}
	}

	private void notifyDutyRosterTurnDeleted(DutyRosterTurn dutyRosterTurn, User modified, Date now) throws Exception {
		if (dutyRosterTurn.getStaff() != null) {
			NotificationDao notificationDao = this.getNotificationDao();
			notificationDao.addNotification(dutyRosterTurn, modified, now, null);
		}
	}

	private void notifyStaffInactive(StaffStatusEntry statusEntry, Date now) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		ServiceUtil.cancelNotifications(statusEntry.getNotifications(), notificationDao, null); // clears inventory_active AND inventory inactive booking notifications
		if (!statusEntry.getType().isStaffActive()) {
			if ((new DateInterval(statusEntry.getStart(), statusEntry.getStop())).contains(now)) {
				notificationDao.addNotification(statusEntry, now, null);
			}
			Iterator<DutyRosterTurn> dutyRosterTurnsIt = this.getDutyRosterTurnDao()
					.findByStaffTrialCalendarInterval(statusEntry.getStaff().getId(), null, null, statusEntry.getStart(), statusEntry.getStop()).iterator();
			while (dutyRosterTurnsIt.hasNext()) {
				notificationDao.addNotification(dutyRosterTurnsIt.next(), statusEntry, now, null);
			}
			Integer staffLimit = Settings.getIntNullable(SettingCodes.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_STAFF_LIMIT, Bundle.SETTINGS,
					DefaultSettings.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_STAFF_LIMIT);
			if (staffLimit != null && staffLimit > 0 && !statusEntry.getType().isHideAvailability()
					&& !(new DateInterval(statusEntry.getStart(), statusEntry.getStop())).isOver(now)) {
				boolean perDay = Settings.getBoolean(SettingCodes.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_PER_DAY, Bundle.SETTINGS,
						DefaultSettings.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_PER_DAY);
				boolean allTrials = Settings.getBoolean(SettingCodes.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_ALL_TRIALS, Bundle.SETTINGS,
						DefaultSettings.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_ALL_TRIALS);
				boolean allStaff = Settings.getBoolean(SettingCodes.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_ALL_STAFF, Bundle.SETTINGS,
						DefaultSettings.STAFF_INACTIVE_VISIT_SCHEDULE_ITEM_NOTIFICATION_ALL_STAFF);
				StaffDao staffDao = this.getStaffDao();
				HashMap<Date, Collection> dateMap = new HashMap<Date, Collection>();
				Iterator<VisitScheduleItem> visitScheduleItemsIt = this.getVisitScheduleItemDao()
						.findByDepartmentTravelInterval(allTrials ? null : statusEntry.getStaff().getDepartment().getId(), statusEntry.getStart(), statusEntry.getStop(), null)
						.iterator();
				while (visitScheduleItemsIt.hasNext()) {
					VisitScheduleItem visitScheduleItem = visitScheduleItemsIt.next();
					if (perDay) {
						Iterator<Date> datesIt = (new DateInterval(visitScheduleItem.getStart(), visitScheduleItem.getStop())).getEnumeratedDates().iterator();
						while (datesIt.hasNext()) {
							Date date = datesIt.next();
							Collection inactiveStaff;
							if (dateMap.containsKey(date)) {
								inactiveStaff = dateMap.get(date);
								if (inactiveStaff != null) {
									addStaffInactiveVisitScheduleItemNotification(visitScheduleItem, statusEntry, now, date, inactiveStaff);
								}
							} else {
								inactiveStaff = staffDao.findByDepartmentStatusInterval(allStaff ? null : statusEntry.getStaff().getDepartment().getId(),
										CommonUtil.dateToTimestamp(DateCalc.getStartOfDay(date)), CommonUtil.dateToTimestamp(DateCalc.getEndOfDay(date)), false, true, false, null);
								if (inactiveStaff.size() >= staffLimit) {
									staffDao.toStaffOutVOCollection(inactiveStaff);
									dateMap.put(date, inactiveStaff);
									addStaffInactiveVisitScheduleItemNotification(visitScheduleItem, statusEntry, now, date, inactiveStaff);
									break;
								} else {
									dateMap.put(date, null);
								}
							}
						}
					} else {
						Collection inactiveStaff = staffDao.findByDepartmentStatusInterval(allStaff ? null : statusEntry.getStaff().getDepartment().getId(),
								visitScheduleItem.getStart(), visitScheduleItem.getStop(), false, true, false, null);
						if (inactiveStaff.size() >= staffLimit) {
							staffDao.toStaffOutVOCollection(inactiveStaff);
							addStaffInactiveVisitScheduleItemNotification(visitScheduleItem, statusEntry, now, null, inactiveStaff);
						}
					}
				}
			}
		}
	}
}