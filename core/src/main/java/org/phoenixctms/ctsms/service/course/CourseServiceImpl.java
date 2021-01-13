// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::course::CourseService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.course;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.ExpirationEntityAdapter;
import org.phoenixctms.ctsms.adapt.LecturerCompetenceTagAdapter;
import org.phoenixctms.ctsms.compare.InventoryBookingIntervalComparator;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseCategory;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntry;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusEntryDao;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusType;
import org.phoenixctms.ctsms.domain.CourseParticipationStatusTypeDao;
import org.phoenixctms.ctsms.domain.CvSection;
import org.phoenixctms.ctsms.domain.DutyRosterTurnDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.Hyperlink;
import org.phoenixctms.ctsms.domain.HyperlinkDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryBooking;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Lecturer;
import org.phoenixctms.ctsms.domain.LecturerDao;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffStatusEntryDao;
import org.phoenixctms.ctsms.domain.TrainingRecordSection;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.CourseCertificatePDFPainter;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFPainter;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFSettingCodes;
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
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseCertificatePDFVO;
import org.phoenixctms.ctsms.vo.CourseInVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryFileVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryInVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.LecturerInVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vocycle.CourseReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.course.CourseService
 */
public class CourseServiceImpl
		extends CourseServiceBase {

	private static JournalEntry logSystemMessage(Course course, CourseOutVO courseVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, user, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Inventory inventory, CourseOutVO courseVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, user, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Staff staff, CourseOutVO courseVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, user, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Trial trial, CourseOutVO courseVO, Timestamp now, User user, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, user, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, CourseOutVO courseVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private CourseParticipationStatusEntryOutVO addCourseParticipationStatusEntry(CourseParticipationStatusEntryInVO newCourseParticipationStatusEntry,
			Timestamp now, User user) throws Exception {
		ServiceUtil.checkAddCourseParticipationStatusEntryInput(newCourseParticipationStatusEntry, true, null,
				this.getStaffDao(), this.getCourseDao(), this.getCvSectionDao(), this.getTrainingRecordSectionDao(), this.getCourseParticipationStatusTypeDao(),
				this.getCourseParticipationStatusEntryDao(), this.getMimeTypeDao());
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = courseParticipationStatusEntryDao.courseParticipationStatusEntryInVOToEntity(newCourseParticipationStatusEntry);
		CoreUtil.modifyVersion(courseParticipation, now, user);
		courseParticipation = courseParticipationStatusEntryDao.create(courseParticipation);
		ServiceUtil.notifyParticipationStatusUpdated(null, courseParticipation, false, now, this.getNotificationDao());
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(courseParticipation.getCourse(), result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_CREATED, result, null,
				journalEntryDao);
		logSystemMessage(courseParticipation.getStaff(), result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_CREATED, result, null,
				journalEntryDao);
		return result;
	}

	private void checkCourseInput(CourseInVO courseIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkDepartmentId(courseIn.getDepartmentId(), this.getDepartmentDao());
		CourseCategory category = CheckIDUtil.checkCourseCategoryId(courseIn.getCategoryId(), this.getCourseCategoryDao());
		if (courseIn.getTrialId() != null) {
			Trial trial = CheckIDUtil.checkTrialId(courseIn.getTrialId(), this.getTrialDao());
			ServiceUtil.checkTrialLocked(trial);
		} else if (category.isTrialRequired()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_TRIAL_REQUIRED, L10nUtil.getCourseCategoryName(Locales.USER, category.getNameL10nKey()));
		}
		if (courseIn.getPrecedingCourseIds() != null && courseIn.getPrecedingCourseIds().size() > 0) {
			CourseDao courseDao = this.getCourseDao();
			ArrayList<Long> precedingCourseIds = new ArrayList<Long>(courseIn.getPrecedingCourseIds());
			Collections.sort(precedingCourseIds);
			Iterator<Long> it = precedingCourseIds.iterator();
			HashSet<Long> dupeCheck = new HashSet<Long>(precedingCourseIds.size());
			while (it.hasNext()) {
				Long id = it.next();
				if (id == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PRECEDING_COURSE_NULL);
				}
				Course course = CheckIDUtil.checkCourseId(id, courseDao, LockMode.PESSIMISTIC_WRITE);
				if (!dupeCheck.add(course.getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.DUPLICATE_PRECEDING_COURSE, CommonUtil.courseOutVOToString(courseDao.toCourseOutVO(course)));
				}
				// lock-safe checks ...
			}
		}
		if (courseIn.getInstitutionId() != null) {
			if (this.getStaffDao().load(courseIn.getInstitutionId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_INVALID_INSTITUTION_STAFF_ID, courseIn.getInstitutionId().toString());
			}
		}
		if (courseIn.getCvSectionPresetId() != null) {
			if (this.getCvSectionDao().load(courseIn.getCvSectionPresetId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_CV_SECTION_ID_PRESET, courseIn.getCvSectionPresetId().toString());
			}
		}
		if (courseIn.getTrainingRecordSectionPresetId() != null) {
			if (this.getTrainingRecordSectionDao().load(courseIn.getTrainingRecordSectionPresetId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_TRAINING_RECORD_SECTION_ID_PRESET, courseIn.getTrainingRecordSectionPresetId().toString());
			}
		}
		if (courseIn.isSelfRegistration()) {
			if (courseIn.getMaxNumberOfParticipants() != null && courseIn.getMaxNumberOfParticipants() < 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_NUMBER_OF_PARTICIPANTS_LESS_THAN_ZERO);
			}
			// participation deadline not restricted for now...
		} else {
			if (courseIn.getMaxNumberOfParticipants() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MAX_NUMBER_OF_PARTICIPANTS_NOT_NULL);
			}
			if (courseIn.getParticipationDeadline() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PARTICIPATION_DEADLINE_NOT_NULL);
			}
		}
		if (courseIn.getStart() != null && DateCalc.getStartOfDay(courseIn.getStart()).compareTo(DateCalc.getStartOfDay(courseIn.getStop())) >= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_START_DATE_GREATER_THAN_OR_EQUAL_TO_END_DATE);
		}
		if (courseIn.getExpires()) {
			if (courseIn.getValidityPeriod() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_VALIDITY_PERIOD_REQUIRED);
			} else if (VariablePeriod.EXPLICIT.equals(courseIn.getValidityPeriod())) {
				if (courseIn.getValidityPeriodDays() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_VALIDITY_PERIOD_EXPLICIT_DAYS_REQUIRED);
				} else if (courseIn.getValidityPeriodDays() < 1) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_VALIDITY_PERIOD_EXPLICIT_DAYS_LESS_THAN_ONE);
				}
			}
		}
		if (courseIn.getShowCvPreset()) {
			if (courseIn.getCvSectionPresetId() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_SECTION_PRESET_REQUIRED);
			}
			if (CommonUtil.isEmptyString(courseIn.getCvTitle())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_TITLE_PRESET_REQUIRED);
			}
			if (courseIn.getShowCommentCvPreset() && CommonUtil.isEmptyString(courseIn.getCvCommentPreset())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_COMMENT_PRESET_REQUIRED);
			}
		} else {
			if (courseIn.getShowCommentCvPreset()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.SHOW_CV_PRESET_DISABLED);
			}
		}
		if (courseIn.getShowTrainingRecordPreset()) {
			if (courseIn.getTrainingRecordSectionPresetId() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRAINING_RECORD_SECTION_PRESET_REQUIRED);
			}
			if (CommonUtil.isEmptyString(courseIn.getCvTitle())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_TITLE_PRESET_REQUIRED);
			}
			if (courseIn.getShowCommentTrainingRecordPreset() && CommonUtil.isEmptyString(courseIn.getCvCommentPreset())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CV_COMMENT_PRESET_REQUIRED);
			}
		} else {
			if (courseIn.getShowCommentTrainingRecordPreset()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.SHOW_TRAINING_RECORD_PRESET_DISABLED);
			}
		}
	}

	private void checkCourseLoop(Course course) throws ServiceException {
		(new CourseReflexionGraph(this.getCourseDao())).checkGraphLoop(course, true, false);
	}

	private void checkLecturerInput(LecturerInVO lecturerIn) throws ServiceException {
		(new LecturerCompetenceTagAdapter(this.getCourseDao(), this.getLecturerCompetenceDao(), this.getStaffDao())).checkTagValueInput(lecturerIn);
	}

	private void deleteCourseHelper(Course course, boolean deleteCascade, User user, Timestamp now) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		CourseOutVO result = courseDao.toCourseOutVO(course);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (deleteCascade) {
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			LecturerDao lecturerDao = this.getLecturerDao();
			Iterator<Lecturer> lecturersIt = course.getLecturers().iterator();
			while (lecturersIt.hasNext()) {
				Lecturer lecturer = lecturersIt.next();
				Staff staff = lecturer.getStaff();
				LecturerOutVO lecturerVO = lecturerDao.toLecturerOutVO(lecturer);
				logSystemMessage(staff, result, now, user, SystemMessageCodes.COURSE_DELETED_LECTURER_DELETED, lecturerVO, null, journalEntryDao);
				staff.removeLectures(lecturer);
				lecturer.setStaff(null);
				lecturer.setCourse(null);
				lecturerDao.remove(lecturer);
			}
			course.getLecturers().clear();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<InventoryBooking> bookingsIt = course.getInventoryBookings().iterator();
			while (bookingsIt.hasNext()) {
				InventoryBooking booking = bookingsIt.next();
				InventoryBookingOutVO original = inventoryBookingDao.toInventoryBookingOutVO(booking);
				booking.setCourse(null);
				CoreUtil.modifyVersion(booking, booking.getVersion(), now, user);
				inventoryBookingDao.update(booking);
				InventoryBookingOutVO bookingVO = inventoryBookingDao.toInventoryBookingOutVO(booking);
				logSystemMessage(booking.getInventory(), result, now, user, SystemMessageCodes.COURSE_DELETED_BOOKING_UPDATED, bookingVO, original, journalEntryDao);
			}
			course.getInventoryBookings().clear();
			CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
			Iterator<CourseParticipationStatusEntry> participationsIt = course.getParticipations().iterator();
			while (participationsIt.hasNext()) {
				CourseParticipationStatusEntry participation = participationsIt.next();
				Staff staff = participation.getStaff();
				CourseParticipationStatusEntryOutVO participationVO = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(participation);
				logSystemMessage(staff, result, now, user, SystemMessageCodes.COURSE_DELETED_PARTICIPATION_DELETED, participationVO, null, journalEntryDao);
				staff.removeParticipations(participation);
				participation.setStaff(null);
				participation.setCourse(null);
				ServiceUtil.removeNotifications(participation.getNotifications(), notificationDao, notificationRecipientDao);
				courseParticipationStatusEntryDao.remove(participation);
			}
			course.getParticipations().clear();
			HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
			Iterator<Hyperlink> hyperlinksIt = course.getHyperlinks().iterator();
			while (hyperlinksIt.hasNext()) {
				Hyperlink hyperlink = hyperlinksIt.next();
				hyperlink.setCourse(null);
				hyperlinkDao.remove(hyperlink);
			}
			course.getHyperlinks().clear();
			Iterator<JournalEntry> journalEntriesIt = course.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setCourse(null);
				journalEntryDao.remove(journalEntry);
			}
			course.getJournalEntries().clear();
			FileDao fileDao = this.getFileDao();
			Iterator<File> filesIt = course.getFiles().iterator();
			while (filesIt.hasNext()) {
				File file = filesIt.next();
				file.setCourse(null);
				fileDao.remove(file);
			}
			course.getFiles().clear();
			ServiceUtil.removeNotifications(course.getNotifications(), notificationDao, notificationRecipientDao);
		}
		Iterator<Course> precedingCoursesIt = course.getPrecedingCourses().iterator();
		while (precedingCoursesIt.hasNext()) {
			Course precedingCourse = precedingCoursesIt.next();
			precedingCourse.removeRenewals(course);
			courseDao.update(precedingCourse);
		}
		course.getPrecedingCourses().clear();
		Iterator<Course> renewalsIt = course.getRenewals().iterator();
		while (renewalsIt.hasNext()) {
			Course renewal = renewalsIt.next();
			renewal.removePrecedingCourses(course);
			CoreUtil.modifyVersion(renewal, renewal.getVersion(), now, user);
			courseDao.update(renewal);
			logSystemMessage(renewal, result, now, user, SystemMessageCodes.COURSE_DELETED_PRECEDING_COURSE_REMOVED, result, null, journalEntryDao);
		}
		course.getRenewals().clear();
		Staff institution = course.getInstitution();
		if (institution != null) {
			logSystemMessage(institution, result, now, user, SystemMessageCodes.COURSE_DELETED_INSTITUTION_COURSE_REMOVED, result, null, journalEntryDao);
			institution.removeInstitutionCourses(course);
			course.setInstitution(null);
		}
		Trial trial = course.getTrial();
		if (trial != null) {
			ServiceUtil.checkTrialLocked(trial);
			logSystemMessage(trial, result, now, user, SystemMessageCodes.COURSE_DELETED_TRIAL_REMOVED, result, null, journalEntryDao);
			trial.removeCourses(course);
			course.setTrial(null);
		}
		courseDao.remove(course);
	}

	@Override
	protected CourseOutVO handleAddCourse(AuthenticationVO auth, CourseInVO newCourse, Integer maxInstances, Integer maxPrecedingCoursesDepth, Integer maxRenewalsDepth)
			throws Exception {
		checkCourseInput(newCourse);
		CourseDao courseDao = this.getCourseDao();
		Course course = courseDao.courseInVOToEntity(newCourse);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(course, now, user);
		course = courseDao.create(course);
		notifyNewCourse(course, now);
		notifyExpiringCourse(course, now);
		CourseOutVO result = courseDao.toCourseOutVO(course, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(course, result, now, user, SystemMessageCodes.COURSE_CREATED, result, null, journalEntryDao);
		Trial trial = course.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result, now, user, SystemMessageCodes.COURSE_CREATED, result, null, journalEntryDao);
		}
		Staff institution = course.getInstitution();
		if (institution != null) {
			logSystemMessage(institution, result, now, user, SystemMessageCodes.COURSE_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> handleAddCourseParticipationStatusEntries(
			AuthenticationVO auth, Long courseId, Set<Long> staffIds) throws Exception {
		Course course = CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		CourseParticipationStatusType statusType = this.getCourseParticipationStatusTypeDao().findInitialStates(true, course.isSelfRegistration()).iterator().next();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<CourseParticipationStatusEntryOutVO> result = new ArrayList<CourseParticipationStatusEntryOutVO>();
		if (staffIds != null) {
			Iterator<Long> staffIt = staffIds.iterator();
			while (staffIt.hasNext()) {
				Long staffId = staffIt.next();
				CvSection cvSection = course.getCvSectionPreset();
				TrainingRecordSection trainingRecordSection = course.getTrainingRecordSectionPreset();
				CourseParticipationStatusEntryInVO newCourseParticipationStatusEntry = new CourseParticipationStatusEntryInVO();
				newCourseParticipationStatusEntry.setComment(course.getCvCommentPreset());
				newCourseParticipationStatusEntry.setCourseId(course.getId());
				newCourseParticipationStatusEntry.setCvSectionId(cvSection != null ? cvSection.getId() : null);
				newCourseParticipationStatusEntry.setShowCommentCv(course.isShowCommentCvPreset());
				newCourseParticipationStatusEntry.setShowCv(course.isShowCvPreset());
				newCourseParticipationStatusEntry.setTrainingRecordSectionId(trainingRecordSection != null ? trainingRecordSection.getId() : null);
				newCourseParticipationStatusEntry.setShowTrainingRecord(course.isShowTrainingRecordPreset());
				newCourseParticipationStatusEntry.setShowCommentTrainingRecord(course.isShowCommentTrainingRecordPreset());
				newCourseParticipationStatusEntry.setStaffId(staffId);
				newCourseParticipationStatusEntry.setStatusId(statusType.getId());
				newCourseParticipationStatusEntry.setDatas(null);
				newCourseParticipationStatusEntry.setFileName(null);
				newCourseParticipationStatusEntry.setMimeType(null);
				try {
					result.add(addCourseParticipationStatusEntry(newCourseParticipationStatusEntry, now, user));
				} catch (ServiceException e) {
					// ignore; due to existent participations....
				}
			}
		}
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleAddCourseParticipationStatusEntry(
			AuthenticationVO auth, CourseParticipationStatusEntryInVO newCourseParticipationStatusEntry)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addCourseParticipationStatusEntry(newCourseParticipationStatusEntry, now, user);
	}

	@Override
	protected LecturerOutVO handleAddLecturer(AuthenticationVO auth, LecturerInVO newLecturer)
			throws Exception {
		checkLecturerInput(newLecturer);
		LecturerDao lecturerDao = this.getLecturerDao();
		Lecturer lecturer = lecturerDao.lecturerInVOToEntity(newLecturer);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(lecturer, now, user);
		lecturer = lecturerDao.create(lecturer);
		LecturerOutVO result = lecturerDao.toLecturerOutVO(lecturer);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(lecturer.getCourse(), result.getCourse(), now, user, SystemMessageCodes.LECTURER_CREATED, result, null, journalEntryDao);
		logSystemMessage(lecturer.getStaff(), result.getCourse(), now, user, SystemMessageCodes.LECTURER_CREATED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleAdminUpdateCourseParticipationStatusEntry(
			AuthenticationVO auth, CourseParticipationStatusEntryInVO modifiedCourseParticipationStatusEntry)
			throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry originalCourseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(modifiedCourseParticipationStatusEntry.getId(),
				courseParticipationStatusEntryDao);
		CourseParticipationStatusTypeDao courseParticipationStatusTypeDao = this.getCourseParticipationStatusTypeDao();
		ServiceUtil.checkUpdateCourseParticipationStatusEntryInput(originalCourseParticipation, modifiedCourseParticipationStatusEntry, true,
				this.getCvSectionDao(), this.getTrainingRecordSectionDao(), courseParticipationStatusTypeDao, courseParticipationStatusEntryDao, this.getMimeTypeDao());
		CourseParticipationStatusEntryOutVO original = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(originalCourseParticipation);
		CourseParticipationStatusType originalCourseParticipationStatusType = originalCourseParticipation.getStatus();
		courseParticipationStatusTypeDao.evict(originalCourseParticipationStatusType);
		courseParticipationStatusEntryDao.evict(originalCourseParticipation);
		CourseParticipationStatusEntry courseParticipation = courseParticipationStatusEntryDao.courseParticipationStatusEntryInVOToEntity(modifiedCourseParticipationStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalCourseParticipation, courseParticipation, now, user);
		courseParticipationStatusEntryDao.update(courseParticipation);
		ServiceUtil.notifyParticipationStatusUpdated(originalCourseParticipationStatusType, courseParticipation, false, now, this.getNotificationDao());
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(courseParticipation.getCourse(), result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		logSystemMessage(courseParticipation.getStaff(), result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		return result;
	}

	@Override
	protected CourseOutVO handleDeleteCourse(AuthenticationVO auth, Long courseId,
			boolean defer, boolean force, String deferredDeleteReason, Integer maxInstances, Integer maxPrecedingCoursesDepth, Integer maxRenewalsDepth) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CourseOutVO result;
		if (!force && defer) {
			Course originalCourse = CheckIDUtil.checkCourseId(courseId, courseDao);
			CourseOutVO original = courseDao.toCourseOutVO(originalCourse, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
			courseDao.evict(originalCourse);
			Course course = CheckIDUtil.checkCourseId(courseId, courseDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			course.setDeferredDelete(true);
			course.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(course, originalCourse.getVersion(), now, user); // no opt. locking
			courseDao.update(course);
			result = courseDao.toCourseOutVO(course, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
			logSystemMessage(course, result, now, user, SystemMessageCodes.COURSE_MARKED_FOR_DELETION, result, original, journalEntryDao);
			Trial trial = course.getTrial();
			if (trial != null) {
				logSystemMessage(trial, result, now, user, SystemMessageCodes.COURSE_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
			Staff institution = course.getInstitution();
			if (institution != null) {
				logSystemMessage(institution, result, now, user, SystemMessageCodes.COURSE_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
			Iterator<CourseOutVO> renewalsIt = original.getRenewals().iterator();
			while (renewalsIt.hasNext()) {
				CourseOutVO renewal = renewalsIt.next();
				logSystemMessage(courseDao.load(renewal.getId()), result, now, user, SystemMessageCodes.COURSE_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
		} else {
			Course course = CheckIDUtil.checkCourseId(courseId, courseDao, LockMode.PESSIMISTIC_WRITE);
			result = courseDao.toCourseOutVO(course, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
			deleteCourseHelper(course, true, user, now);
			logSystemMessage(user, result, now, user, SystemMessageCodes.COURSE_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleDeleteCourseParticipationStatusEntry(
			AuthenticationVO auth, Long courseParticipationStatusEntryId) throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId, courseParticipationStatusEntryDao);
		Staff staff = courseParticipation.getStaff();
		Course course = courseParticipation.getCourse();
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		staff.removeParticipations(courseParticipation);
		courseParticipation.setStaff(null);
		course.removeParticipations(courseParticipation);
		courseParticipation.setCourse(null);
		ServiceUtil.removeNotifications(courseParticipation.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		courseParticipationStatusEntryDao.remove(courseParticipation);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(staff, result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		logSystemMessage(course, result.getCourse(), now, user, SystemMessageCodes.COURSE_PARTICIPATION_STATUS_ENTRY_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected LecturerOutVO handleDeleteLecturer(AuthenticationVO auth, Long lecturerId)
			throws Exception {
		LecturerDao lecturerDao = this.getLecturerDao();
		Lecturer lecturer = CheckIDUtil.checkLecturerId(lecturerId, lecturerDao);
		Course course = lecturer.getCourse();
		Staff staff = lecturer.getStaff();
		LecturerOutVO result = lecturerDao.toLecturerOutVO(lecturer);
		course.removeLecturers(lecturer);
		lecturer.setCourse(null);
		staff.removeLectures(lecturer);
		lecturer.setStaff(null);
		lecturerDao.remove(lecturer);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(course, result.getCourse(), now, user, SystemMessageCodes.LECTURER_DELETED, result, null, journalEntryDao);
		logSystemMessage(staff, result.getCourse(), now, user, SystemMessageCodes.LECTURER_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetCollidingDutyRosterTurns(
			AuthenticationVO auth, Long courseParticipationStatusEntryId, Boolean isRelevantForCourseAppointments) throws Exception {
		CourseParticipationStatusEntry courseParticipationStatusEntry = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId,
				this.getCourseParticipationStatusEntryDao());
		Collection collidingDutyRosterTurns = new HashSet();
		Long staffId = courseParticipationStatusEntry.getStaff().getId();
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Iterator<InventoryBooking> it = this.getInventoryBookingDao()
				.findByCourseSorted(courseParticipationStatusEntry.getCourse().getId(), isRelevantForCourseAppointments, false).iterator();
		while (it.hasNext()) {
			InventoryBooking courseInventoryBooking = it.next();
			collidingDutyRosterTurns.addAll(dutyRosterTurnDao.findByStaffTrialCalendarInterval(staffId, null, null, courseInventoryBooking.getStart(),
					courseInventoryBooking.getStop()));
		}
		dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(collidingDutyRosterTurns);
		return new ArrayList<DutyRosterTurnOutVO>(collidingDutyRosterTurns);
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetCollidingStaffStatusEntries(
			AuthenticationVO auth, Long courseParticipationStatusEntryId, Boolean isRelevantForCourseAppointments) throws Exception {
		CourseParticipationStatusEntry courseParticipationStatusEntry = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId,
				this.getCourseParticipationStatusEntryDao());
		Long staffId = courseParticipationStatusEntry.getStaff().getId();
		Collection collidingStaffStatusEntries = new HashSet();
		StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
		Iterator<InventoryBooking> it = this.getInventoryBookingDao()
				.findByCourseSorted(courseParticipationStatusEntry.getCourse().getId(), isRelevantForCourseAppointments, false).iterator();
		while (it.hasNext()) {
			InventoryBooking courseInventoryBooking = it.next();
			collidingStaffStatusEntries.addAll(staffStatusEntryDao.findByStaffInterval(staffId, courseInventoryBooking.getStart(), courseInventoryBooking.getStop(), false, null,
					false));
		}
		staffStatusEntryDao.toStaffStatusEntryOutVOCollection(collidingStaffStatusEntries);
		return new ArrayList<StaffStatusEntryOutVO>(collidingStaffStatusEntries);
	}

	@Override
	protected CourseOutVO handleGetCourse(AuthenticationVO auth, Long courseId, Integer maxInstances, Integer maxPrecedingCoursesDepth, Integer maxRenewalsDepth) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		Course course = CheckIDUtil.checkCourseId(courseId, courseDao);
		CourseOutVO result = courseDao.toCourseOutVO(course, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
		return result;
	}

	@Override
	protected Collection<CourseOutVO> handleGetCourseInterval(
			AuthenticationVO auth, Long departmentId, Long courseCategoryId,
			Date from, Date to) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (courseCategoryId != null) {
			CheckIDUtil.checkCourseCategoryId(courseCategoryId, this.getCourseCategoryDao());
		}
		CourseDao courseDao = this.getCourseDao();
		Collection courses = courseDao.findByDepartmentCategoryInterval(departmentId, courseCategoryId, CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to));
		courseDao.toCourseOutVOCollection(courses);
		return courses;
	}

	@Override
	protected long handleGetCourseInventoryBookingCount(
			AuthenticationVO auth, Long courseId) throws Exception {
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		return this.getInventoryBookingDao().getCount(null, null, null, courseId, null);
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCourseInventoryBookingList(
			AuthenticationVO auth, Long courseId, PSFVO psf) throws Exception {
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByCourse(courseId, psf);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		return inventoryBookings;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCourseInventoryBookingParticipantInterval(
			AuthenticationVO auth, Long staffId, Long courseDepartmentId, Long courseCategoryId, Date from, Date to, Boolean isRelevantForCourseAppointments, boolean sort)
			throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (courseDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(courseDepartmentId, this.getDepartmentDao());
		}
		if (courseCategoryId != null) {
			CheckIDUtil.checkCourseCategoryId(courseCategoryId, this.getCourseCategoryDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByCourseParticipantDepartmentCategoryInterval(staffId, courseDepartmentId, courseCategoryId,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to), isRelevantForCourseAppointments);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		if (sort) {
			inventoryBookings = new ArrayList(inventoryBookings);
			Collections.sort((ArrayList) inventoryBookings, new InventoryBookingIntervalComparator(false));
		}
		return inventoryBookings;
	}

	@Override
	protected Collection<CourseOutVO> handleGetCourseList(AuthenticationVO auth, Long courseId, Long departmentId, Integer maxInstances, PSFVO psf) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, courseDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection courses = courseDao.findByIdDepartment(courseId, departmentId, psf);
		ArrayList<CourseOutVO> result = new ArrayList<CourseOutVO>(courses.size());
		Iterator<Course> coursesIt = courses.iterator();
		while (coursesIt.hasNext()) {
			result.add(courseDao.toCourseOutVO(coursesIt.next(), maxInstances));
		}
		return result;
	}

	@Override
	protected CourseParticipationStatusEntryOutVO handleGetCourseParticipationStatusEntry(
			AuthenticationVO auth, Long courseParticipationStatusEntryId) throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId, courseParticipationStatusEntryDao);
		CourseParticipationStatusEntryOutVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		return result;
	}

	@Override
	protected long handleGetCourseParticipationStatusEntryCount(
			AuthenticationVO auth, Long staffId, Long courseId, Long statusId)
			throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		if (statusId != null) {
			CheckIDUtil.checkCourseParticipationStatusTypeId(statusId, this.getCourseParticipationStatusTypeDao());
		}
		return this.getCourseParticipationStatusEntryDao().getStaffCourseStatusCount(staffId, courseId, statusId);
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> handleGetCourseParticipationStatusEntryList(
			AuthenticationVO auth, Long staffId, Long courseId, Long statusId, PSFVO psf)
			throws Exception {
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		if (statusId != null) {
			CheckIDUtil.checkCourseParticipationStatusTypeId(statusId, this.getCourseParticipationStatusTypeDao());
		}
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		Collection courseParticipations = courseParticipationStatusEntryDao.findByStaffCourseStatus(staffId, courseId, statusId, psf);
		courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVOCollection(courseParticipations);
		return courseParticipations;
	}

	@Override
	protected Collection<CourseOutVO> handleGetExpiringCourses(AuthenticationVO auth, Date today,
			Long departmentId, Long courseCategoryId,
			VariablePeriod reminderPeriod, Long reminderPeriodDays, PSFVO psf)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (courseCategoryId != null) {
			CheckIDUtil.checkCourseCategoryId(courseCategoryId, this.getCourseCategoryDao());
		}
		ServiceUtil.checkReminderPeriod(reminderPeriod, reminderPeriodDays);
		CourseDao courseDao = this.getCourseDao();
		Collection expiringCourses = courseDao.findExpiring(today, departmentId, courseCategoryId, reminderPeriod, reminderPeriodDays, true, psf);
		courseDao.toCourseOutVOCollection(expiringCourses);
		return expiringCourses;
	}

	@Override
	protected Collection<CourseParticipationStatusEntryOutVO> handleGetExpiringParticipations(
			AuthenticationVO auth, Date today, Long courseDepartmentId, Long courseCategoryId,
			Long staffDepartmentId, Long staffCategoryId,
			VariablePeriod reminderPeriod, Long reminderPeriodDays, PSFVO psf)
			throws Exception {
		if (courseDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(courseDepartmentId, this.getDepartmentDao());
		}
		if (courseCategoryId != null) {
			CheckIDUtil.checkCourseCategoryId(courseCategoryId, this.getCourseCategoryDao());
		}
		if (staffDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(staffDepartmentId, this.getDepartmentDao());
		}
		if (staffCategoryId != null) {
			CheckIDUtil.checkStaffCategoryId(staffCategoryId, this.getStaffCategoryDao());
		}
		ServiceUtil.checkReminderPeriod(reminderPeriod, reminderPeriodDays);
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		Collection expiringParticipations = courseParticipationStatusEntryDao.findExpiring(today, courseDepartmentId, courseCategoryId, staffDepartmentId, staffCategoryId, null,
				true, reminderPeriod, reminderPeriodDays, true, psf);
		courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVOCollection(expiringParticipations);
		return expiringParticipations;
	}

	@Override
	protected LecturerOutVO handleGetLecturer(AuthenticationVO auth, Long lecturerId) throws Exception {
		LecturerDao lecturerDao = this.getLecturerDao();
		Lecturer lecturer = CheckIDUtil.checkLecturerId(lecturerId, lecturerDao);
		LecturerOutVO result = lecturerDao.toLecturerOutVO(lecturer);
		return result;
	}

	@Override
	protected long handleGetLecturerCount(AuthenticationVO auth, Long courseId,
			Long competenceId) throws Exception {
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		if (competenceId != null) {
			CheckIDUtil.checkLecturerCompetenceId(competenceId, this.getLecturerCompetenceDao());
		}
		return this.getLecturerDao().getCount(courseId, null, competenceId);
	}

	@Override
	protected Collection<LecturerOutVO> handleGetLecturerList(AuthenticationVO auth, Long courseId,
			Long competenceId, PSFVO psf) throws Exception {
		if (courseId != null) {
			CheckIDUtil.checkCourseId(courseId, this.getCourseDao());
		}
		if (competenceId != null) {
			CheckIDUtil.checkLecturerCompetenceId(competenceId, this.getLecturerCompetenceDao());
		}
		LecturerDao lecturerDao = this.getLecturerDao();
		Collection lecturers = lecturerDao.findByCourseStaffCompetence(courseId, null, competenceId, psf);
		lecturerDao.toLecturerOutVOCollection(lecturers);
		return lecturers;
	}

	@Override
	protected Collection<CourseOutVO> handleGetUpcomingCourses(AuthenticationVO auth, Date now,
			Long departmentId, Long courseCategoryId, PSFVO psf)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (courseCategoryId != null) {
			CheckIDUtil.checkCourseCategoryId(courseCategoryId, this.getCourseCategoryDao());
		}
		CourseDao courseDao = this.getCourseDao();
		Collection upcomingCourses = courseDao.findUpcoming(CommonUtil.dateToTimestamp(now), departmentId, courseCategoryId, psf);
		courseDao.toCourseOutVOCollection(upcomingCourses);
		return upcomingCourses;
	}

	@Override
	protected CourseCertificatePDFVO handleRenderBlankCourseCertificate(
			AuthenticationVO auth, Long courseId) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		Course course = CheckIDUtil.checkCourseId(courseId, courseDao);
		CourseOutVO courseVO = courseDao.toCourseOutVO(course);
		CourseParticipationStatusEntryOutVO participationDummy = new CourseParticipationStatusEntryOutVO();
		participationDummy.setCourse(courseVO);
		participationDummy.setModifiedTimestamp(courseVO.getModifiedTimestamp());
		participationDummy.setModifiedUser(courseVO.getModifiedUser());
		participationDummy.setCvSection(courseVO.getCvSectionPreset());
		participationDummy.setShowCommentCv(courseVO.getShowCommentCvPreset());
		participationDummy.setShowCv(courseVO.getShowCvPreset());
		participationDummy.setTrainingRecordSection(courseVO.getTrainingRecordSectionPreset());
		participationDummy.setShowCommentTrainingRecord(courseVO.getShowCommentTrainingRecordPreset());
		participationDummy.setShowTrainingRecord(courseVO.getShowTrainingRecordPreset());
		participationDummy.setStatus(null);
		participationDummy.setVersion(0l);
		participationDummy.setComment(courseVO.getCvCommentPreset());
		participationDummy.setHasFile(false);
		ArrayList<CourseParticipationStatusEntryOutVO> participantVOs = new ArrayList<CourseParticipationStatusEntryOutVO>();
		participantVOs.add(participationDummy);
		CourseCertificatePDFPainter painter = ServiceUtil.createCourseCertificatePDFPainter(participantVOs, this.getStaffDao(), this.getStaffAddressDao(), this.getLecturerDao(),
				this.getLecturerCompetenceDao());
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		(new PDFImprinter(painter, painter)).render();
		return painter.getPdfVO();
	}

	@Override
	protected CourseCertificatePDFVO handleRenderCourseCertificate(
			AuthenticationVO auth, Long courseParticipationStatusEntryId)
			throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId, courseParticipationStatusEntryDao);
		ArrayList<CourseParticipationStatusEntryOutVO> participantVOs = new ArrayList<CourseParticipationStatusEntryOutVO>();
		CourseParticipationStatusEntryOutVO participantVO = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVO(courseParticipation);
		participantVOs.add(participantVO);
		CourseCertificatePDFPainter painter = ServiceUtil.createCourseCertificatePDFPainter(participantVOs, this.getStaffDao(), this.getStaffAddressDao(), this.getLecturerDao(),
				this.getLecturerCompetenceDao());
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		CourseCertificatePDFVO result = painter.getPdfVO();
		logSystemMessage(courseParticipation.getCourse(), participantVO.getCourse(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				SystemMessageCodes.COURSE_CERTIFICATE_PDF_RENDERED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CourseCertificatePDFVO handleRenderCourseCertificates(
			AuthenticationVO auth, Long courseId) throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseDao courseDao = this.getCourseDao();
		Course course = CheckIDUtil.checkCourseId(courseId, courseDao);
		CourseOutVO courseVO = courseDao.toCourseOutVO(course);
		Collection participantVOs = courseParticipationStatusEntryDao.findByCourseSorted(courseId);
		courseParticipationStatusEntryDao.toCourseParticipationStatusEntryOutVOCollection(participantVOs);
		CourseCertificatePDFPainter painter = ServiceUtil.createCourseCertificatePDFPainter(participantVOs, this.getStaffDao(), this.getStaffAddressDao(), this.getLecturerDao(),
				this.getLecturerCompetenceDao());
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		CourseCertificatePDFVO result = painter.getPdfVO();
		logSystemMessage(course, courseVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.COURSE_CERTIFICATES_PDF_RENDERED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CourseParticipantListPDFVO handleRenderCourseParticipantList(
			AuthenticationVO auth, Long courseId, boolean blank) throws Exception {
		CourseDao courseDao = this.getCourseDao();
		Course course = CheckIDUtil.checkCourseId(courseId, courseDao);
		ArrayList<CourseOutVO> courseVOs = new ArrayList<CourseOutVO>();
		CourseOutVO courseVO = courseDao.toCourseOutVO(course, Settings.getInt(CourseParticipantListPDFSettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.GRAPH_MAX_COURSE_INSTANCES));
		courseVOs.add(courseVO);
		CourseParticipantListPDFPainter painter = ServiceUtil.createCourseParticipantListPDFPainter(courseVOs, blank, this.getLecturerDao(), this.getLecturerCompetenceDao(),
				this.getCourseParticipationStatusEntryDao(), this.getInventoryBookingDao());
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		CourseParticipantListPDFVO result = painter.getPdfVO();
		if (!blank) {
			logSystemMessage(course, courseVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.COURSE_PARTICIPANT_LIST_RENDERED, result, null,
					this.getJournalEntryDao());
		}
		return result;
	}

	@Override
	protected CourseOutVO handleUpdateCourse(AuthenticationVO auth, CourseInVO modifiedCourse, Integer maxInstances, Integer maxPrecedingCoursesDepth, Integer maxRenewalsDepth)
			throws Exception {
		CourseDao courseDao = this.getCourseDao();
		Course originalCourse = CheckIDUtil.checkCourseId(modifiedCourse.getId(), courseDao, LockMode.PESSIMISTIC_WRITE);
		checkCourseInput(modifiedCourse);
		if (originalCourse.isSelfRegistration() != modifiedCourse.isSelfRegistration()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.COURSE_SELF_REGISTRATION_FLAG_CHANGED);
		}
		CourseOutVO original = courseDao.toCourseOutVO(originalCourse, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
		courseDao.evict(originalCourse);
		Course course = courseDao.courseInVOToEntity(modifiedCourse);
		checkCourseLoop(course);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalCourse, course, now, user);
		courseDao.update(course);
		notifyExpiringCourse(course, now);
		CourseOutVO result = courseDao.toCourseOutVO(course, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(course, result, now, user, SystemMessageCodes.COURSE_UPDATED, result, original, journalEntryDao);
		Trial trial = course.getTrial();
		if (trial != null) {
			logSystemMessage(trial, result, now, user, SystemMessageCodes.COURSE_UPDATED, result, original, journalEntryDao);
		}
		Staff institution = course.getInstitution();
		if (institution != null) {
			logSystemMessage(institution, result, now, user, SystemMessageCodes.COURSE_UPDATED, result, original, journalEntryDao);
		}
		Iterator<CourseOutVO> renewalsIt = original.getRenewals().iterator();
		while (renewalsIt.hasNext()) {
			CourseOutVO renewal = renewalsIt.next();
			logSystemMessage(courseDao.load(renewal.getId()), result, now, user, SystemMessageCodes.COURSE_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected LecturerOutVO handleUpdateLecturer(AuthenticationVO auth, LecturerInVO modifiedLecturer)
			throws Exception {
		LecturerDao lecturerDao = this.getLecturerDao();
		Lecturer originalLecturer = CheckIDUtil.checkLecturerId(modifiedLecturer.getId(), lecturerDao);
		checkLecturerInput(modifiedLecturer);
		LecturerOutVO original = lecturerDao.toLecturerOutVO(originalLecturer);
		lecturerDao.evict(originalLecturer);
		Lecturer lecturer = lecturerDao.lecturerInVOToEntity(modifiedLecturer);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalLecturer, lecturer, now, user);
		lecturerDao.update(lecturer);
		LecturerOutVO result = lecturerDao.toLecturerOutVO(lecturer);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		logSystemMessage(lecturer.getCourse(), result.getCourse(), now, user, SystemMessageCodes.LECTURER_UPDATED, result, original, journalEntryDao);
		logSystemMessage(lecturer.getStaff(), result.getCourse(), now, user, SystemMessageCodes.LECTURER_UPDATED, result, original, journalEntryDao);
		return result;
	}

	private void notifyExpiringCourse(Course course, Date now) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		VariablePeriod expiringCourseReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD);
		Long expiringCourseReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_EXPIRING_COURSE_REMINDER_PERIOD_DAYS);
		VariablePeriod expiringCourseParticipationReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD);
		Long expiringCourseParticipationReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD_DAYS,
				Settings.Bundle.SETTINGS, DefaultSettings.NOTIFICATION_EXPIRING_COURSE_PARTICIPATION_REMINDER_PERIOD_DAYS);
		Map messageParameters = null;
		if (course.isExpires()) {
			messageParameters = CoreUtil.createEmptyTemplateModel();
			messageParameters.put(NotificationMessageTemplateParameters.COURSE_EXPIRATION_DAYS_LEFT,
					DateCalc.dateDeltaDays(now, DateCalc.addInterval(course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays())));
		}
		if (course.isExpires()
				&& now.compareTo(
						ExpirationEntityAdapter.getInstance(course, now).getReminderStart(null, null, expiringCourseReminderPeriod, expiringCourseReminderPeriodDays)) >= 0) {
			notificationDao.addExpiringCourseNotification(course, now, messageParameters);
		} else {
			ServiceUtil.cancelNotifications(course.getNotifications(), notificationDao, org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE);
		}
		HashMap<Long, Set<Long>> particiaptionCourseIdsMap = new HashMap<Long, Set<Long>>();
		Iterator<CourseParticipationStatusEntry> courseParticipationsIt = course.getParticipations().iterator();
		while (courseParticipationsIt.hasNext()) {
			CourseParticipationStatusEntry courseParticipation = courseParticipationsIt.next();
			if (course.isExpires()
					&& now.compareTo(ExpirationEntityAdapter.getInstance(courseParticipation, now, particiaptionCourseIdsMap).getReminderStart(null, null,
							expiringCourseParticipationReminderPeriod, expiringCourseParticipationReminderPeriodDays)) >= 0) {
				notificationDao.addNotification(courseParticipation, true, false, now, messageParameters);
			} else {
				ServiceUtil.cancelNotifications(courseParticipation.getNotifications(), notificationDao,
						org.phoenixctms.ctsms.enumeration.NotificationType.EXPIRING_COURSE_PARTICIPATION);
			}
		}
	}

	private void notifyNewCourse(Course course, Date now) throws Exception {
		if (course.isSelfRegistration()
				&& ((course.getParticipationDeadline() == null && now.compareTo(course.getStop()) <= 0) || (course.getParticipationDeadline() != null && now.compareTo(course
						.getParticipationDeadline()) <= 0))) {
			this.getNotificationDao().addNewCourseNotification(course, now, null);
		}
	}

	@Override
	protected CourseParticipationStatusEntryFileVO handleGetCourseParticipationStatusEntryFile(AuthenticationVO auth, Long courseParticipationStatusEntryId) throws Exception {
		CourseParticipationStatusEntryDao courseParticipationStatusEntryDao = this.getCourseParticipationStatusEntryDao();
		CourseParticipationStatusEntry courseParticipation = CheckIDUtil.checkCourseParticipationStatusEntryId(courseParticipationStatusEntryId, courseParticipationStatusEntryDao);
		CourseParticipationStatusEntryFileVO result = courseParticipationStatusEntryDao.toCourseParticipationStatusEntryFileVO(courseParticipation);
		return result;
	}
}