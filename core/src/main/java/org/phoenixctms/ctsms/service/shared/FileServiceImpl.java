// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::FileService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MassMail;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.PDFMerger;
import org.phoenixctms.ctsms.security.CipherStream;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.FileContentInVO;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FilePDFVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.FileService
 */
public class FileServiceImpl
		extends FileServiceBase {

	private final static java.util.regex.Pattern FILE_PATH_REGEXP = Pattern.compile("^(" + java.util.regex.Pattern.quote(CommonUtil.LOGICAL_PATH_SEPARATOR) + "[^"
			+ java.util.regex.Pattern.quote(CommonUtil.LOGICAL_PATH_SEPARATOR) + "]+)*" + java.util.regex.Pattern.quote(CommonUtil.LOGICAL_PATH_SEPARATOR) + "$");

	private static void checkContentSize(File file) throws ServiceException {
		Integer externalFileContentStreamThreshold = Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_CONTENT_STREAM_THRESHOLD, Bundle.SETTINGS,
				DefaultSettings.EXTERNAL_FILE_CONTENT_STREAM_THRESHOLD);
		if (externalFileContentStreamThreshold != null && file.isExternalFile() && file.getSize() > externalFileContentStreamThreshold) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EXTERNAL_FILE_CONTENT_STREAM_THRESHOLD_EXCEEDED,
					CommonUtil.humanReadableByteCount(externalFileContentStreamThreshold));
		}
	}

	private static JournalEntry logSystemMessage(Course course, CourseOutVO courseVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.courseOutVOToString(courseVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null))});
	}

	private static JournalEntry logSystemMessage(Inventory inventory, InventoryOutVO inventoryVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, modified, systemMessageCode, new Object[] { CommonUtil.inventoryOutVOToString(inventoryVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL,
		// null))});
	}

	private static JournalEntry logSystemMessage(Staff staff, StaffOutVO staffVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(staff, now, modified, systemMessageCode, new Object[] { CommonUtil.staffOutVOToString(staffVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, false) }); // !CommonUtil.getUseJournalEncryption(JournalModule.STAFF_JOURNAL, null))});
	}

	// private static JournalEntry logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
	// JournalEntryDao journalEntryDao) throws Exception {
	// return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
	// new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	// }
	private static boolean mkDir(java.io.File file) throws Exception {
		return file.getParentFile().mkdirs();
	}

	private static boolean rmDir(java.io.File file) throws Exception {
		java.io.File dir = file.getParentFile();
		if (dir.list().length == 0) {
			return dir.delete();
		}
		return false;
	}

	private void addAggregatedPDFFilesExportedJournalEntry(FileModule module, Long id, FilePDFVO pdfVO, Timestamp now, User user) throws Exception {
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (module) {
			case INVENTORY_DOCUMENT:
				InventoryDao inventoryDao = this.getInventoryDao();
				Inventory inventory = inventoryDao.load(id);
				logSystemMessage(inventory, inventoryDao.toInventoryOutVO(inventory), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null, journalEntryDao);
				break;
			case STAFF_DOCUMENT:
				StaffDao staffDao = this.getStaffDao();
				Staff staff = staffDao.load(id);
				logSystemMessage(staff, staffDao.toStaffOutVO(staff), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null, journalEntryDao);
				break;
			case COURSE_DOCUMENT:
				CourseDao courseDao = this.getCourseDao();
				Course course = courseDao.load(id);
				logSystemMessage(course, courseDao.toCourseOutVO(course), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null, journalEntryDao);
				break;
			case TRIAL_DOCUMENT:
				TrialDao trialDao = this.getTrialDao();
				Trial trial = trialDao.load(id);
				ServiceUtil.logSystemMessage(trial, trialDao.toTrialOutVO(trial), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null, journalEntryDao);
				break;
			case PROBAND_DOCUMENT:
				ProbandDao probandDao = this.getProbandDao();
				Proband proband = probandDao.load(id);
				ServiceUtil
						.logSystemMessage(proband, probandDao.toProbandOutVO(proband), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null, journalEntryDao); // creates
				// mixed
				// encryptions!
				break;
			case MASS_MAIL_DOCUMENT:
				MassMailDao massMailDao = this.getMassMailDao();
				MassMail massMail = massMailDao.load(id);
				ServiceUtil.logSystemMessage(massMail, massMailDao.toMassMailOutVO(massMail), now, user, SystemMessageCodes.AGGREGATED_PDF_FILES_EXPORTED, pdfVO, null,
						journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE,
						new Object[] { module.toString() }));
		}
	}

	private FileOutVO addFileUpdatedJournalEntry(File file, FileOutVO original, Timestamp now, User user) throws Exception {
		FileDao fileDao = this.getFileDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		FileOutVO result = fileDao.toFileOutVO(file);
		switch (file.getModule()) {
			case INVENTORY_DOCUMENT:
				logSystemMessage(file.getInventory(), result.getInventory(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			case STAFF_DOCUMENT:
				logSystemMessage(file.getStaff(), result.getStaff(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			case COURSE_DOCUMENT:
				logSystemMessage(file.getCourse(), result.getCourse(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			case TRIAL_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getTrial(), result.getTrial(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			case PROBAND_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getProband(), result.getProband(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			case MASS_MAIL_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getMassMail(), result.getMassMail(), now, user, SystemMessageCodes.FILE_UPDATED, result, original, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE, new Object[] { file
						.getModule().toString() }));
		}
		return result;
	}

	private void checkFileInput(FileContentInVO fileContent, FileModule module) throws ServiceException {
		checkMimeType(fileContent.getMimeType(), module);
		Integer externalFileContentSizeLimit = Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_CONTENT_SIZE_LIMIT, Bundle.SETTINGS,
				DefaultSettings.EXTERNAL_FILE_CONTENT_SIZE_LIMIT);
		if (externalFileContentSizeLimit != null && fileContent.getDatas().length > externalFileContentSizeLimit) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EXTERNAL_FILE_CONTENT_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(externalFileContentSizeLimit)); // "content size exceeds limit ({0})"
		}
	}

	private void checkFileInput(FileInVO file) throws ServiceException {
		switch (file.getModule()) {
			case INVENTORY_DOCUMENT:
				CheckIDUtil.checkInventoryId(file.getInventoryId(), this.getInventoryDao());
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_INVENTORY_ONLY_ALLOWED);
				}
				break;
			case STAFF_DOCUMENT:
				CheckIDUtil.checkStaffId(file.getStaffId(), this.getStaffDao());
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_STAFF_ONLY_ALLOWED);
				}
				break;
			case COURSE_DOCUMENT:
				CheckIDUtil.checkCourseId(file.getCourseId(), this.getCourseDao());
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_COURSE_ONLY_ALLOWED);
				}
				break;
			case TRIAL_DOCUMENT:
				ServiceUtil.checkTrialLocked(CheckIDUtil.checkTrialId(file.getTrialId(), this.getTrialDao()));
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_TRIAL_ONLY_ALLOWED);
				}
				break;
			case PROBAND_DOCUMENT:
				ServiceUtil.checkProbandLocked(CheckIDUtil.checkProbandId(file.getProbandId(), this.getProbandDao()));
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_PROBAND_ONLY_ALLOWED);
				}
				break;
			case MASS_MAIL_DOCUMENT:
				ServiceUtil.checkMassMailLocked(CheckIDUtil.checkMassMailId(file.getMassMailId(), this.getMassMailDao()));
				if (numIdsSet(file) > 1) {
					// no other references must be set...
					throw L10nUtil.initServiceException(ServiceExceptionCodes.FILE_MASS_MAIL_ONLY_ALLOWED);
				}
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE, new Object[] { file
						.getModule().toString() }));
		}
		if (!FILE_PATH_REGEXP.matcher(file.getLogicalPath()).find()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_LOGICAL_PATH, file.getLogicalPath());
		}
	}

	private void checkFileInput(FileStreamInVO fileStream, FileModule module) throws ServiceException {
		checkMimeType(fileStream.getMimeType(), module);
		Integer externalFileSizeLimit = Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_SIZE_LIMIT);
		if (externalFileSizeLimit != null && fileStream.getSize() != null && fileStream.getSize() > externalFileSizeLimit) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EXTERNAL_FILE_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(externalFileSizeLimit)); // "stream size will exceed limit ({0})"
		}
	}

	private void checkFileModuleId(FileModule module, Long id) throws Exception {
		if (id != null) { // module != null
			switch (module) {
				case INVENTORY_DOCUMENT:
					CheckIDUtil.checkInventoryId(id, this.getInventoryDao());
					break;
				case STAFF_DOCUMENT:
					CheckIDUtil.checkStaffId(id, this.getStaffDao());
					break;
				case COURSE_DOCUMENT:
					CheckIDUtil.checkCourseId(id, this.getCourseDao());
					break;
				case TRIAL_DOCUMENT:
					CheckIDUtil.checkTrialId(id, this.getTrialDao());
					break;
				case PROBAND_DOCUMENT:
					CheckIDUtil.checkProbandId(id, this.getProbandDao());
					break;
				case MASS_MAIL_DOCUMENT:
					CheckIDUtil.checkMassMailId(id, this.getMassMailDao());
					break;
				default:
					// not supported for now...
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE,
							new Object[] { module.toString() }));
			}
		}
	}

	private void checkMimeType(String mimeType, FileModule module) throws ServiceException {
		if (this.getMimeTypeDao().getCount(mimeType, module) == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.UNKNOWN_MIME_TYPE, mimeType, module);
		}
	}

	private FileOutVO createFile(File file, Timestamp now, User user) throws Exception {
		FileDao fileDao = this.getFileDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		file = fileDao.create(file);
		FileOutVO result = fileDao.toFileOutVO(file);
		switch (file.getModule()) {
			case INVENTORY_DOCUMENT:
				logSystemMessage(file.getInventory(), result.getInventory(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			case STAFF_DOCUMENT:
				logSystemMessage(file.getStaff(), result.getStaff(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			case COURSE_DOCUMENT:
				logSystemMessage(file.getCourse(), result.getCourse(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			case TRIAL_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getTrial(), result.getTrial(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			case PROBAND_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getProband(), result.getProband(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			case MASS_MAIL_DOCUMENT:
				ServiceUtil.logSystemMessage(file.getMassMail(), result.getMassMail(), now, user, SystemMessageCodes.FILE_CREATED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE, new Object[] { file
						.getModule().toString() })); // L10nUtil.initServiceException("file module {0} not supported",file.getModule());
		}
		return result;
	}

	private FileOutVO deleteFileHelper(Long fileId, Timestamp now, User user) throws Exception {
		FileDao fileDao = this.getFileDao();
		File file = CheckIDUtil.checkFileId(fileId, fileDao, LockMode.UPGRADE_NOWAIT);
		FileOutVO result = fileDao.toFileOutVO(file);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_FILE);
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		switch (file.getModule()) {
			case INVENTORY_DOCUMENT:
				Inventory inventory = file.getInventory();
				inventory.removeFiles(file);
				file.setInventory(null);
				fileDao.remove(file);
				logSystemMessage(inventory, result.getInventory(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			case STAFF_DOCUMENT:
				Staff staff = file.getStaff();
				staff.removeFiles(file);
				file.setStaff(null);
				fileDao.remove(file);
				logSystemMessage(staff, result.getStaff(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			case COURSE_DOCUMENT:
				Course course = file.getCourse();
				course.removeFiles(file);
				file.setCourse(null);
				fileDao.remove(file);
				logSystemMessage(course, result.getCourse(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			case TRIAL_DOCUMENT:
				Trial trial = file.getTrial();
				ServiceUtil.checkTrialLocked(trial);
				trial.removeFiles(file);
				file.setTrial(null);
				fileDao.remove(file);
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			case PROBAND_DOCUMENT:
				Proband proband = file.getProband();
				ServiceUtil.checkProbandLocked(proband);
				proband.removeFiles(file);
				file.setProband(null);
				fileDao.remove(file);
				ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			case MASS_MAIL_DOCUMENT:
				MassMail massMail = file.getMassMail();
				ServiceUtil.checkMassMailLocked(massMail);
				massMail.removeFiles(file);
				file.setMassMail(null);
				fileDao.remove(file);
				ServiceUtil.logSystemMessage(massMail, result.getMassMail(), now, user, SystemMessageCodes.FILE_DELETED, result, null, journalEntryDao);
				break;
			default:
				// not supported for now...
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_FILE_MODULE, DefaultMessages.UNSUPPORTED_FILE_MODULE, new Object[] { file
						.getModule().toString() }));
		}
		return result;
	}

	private boolean dropExternalFile(boolean isExternalFile, String externalFileName) throws Exception {
		if (isExternalFile) {
			String filePath = CoreUtil.getFileServiceExternalFilename(externalFileName);
			java.io.File externalFile = new java.io.File(filePath);
			if (externalFile.isFile() && externalFile.exists()) {
				boolean result = externalFile.delete();
				rmDir(externalFile);
				return result;
			}
		}
		return false;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#addFile(FileInVO, FileContentInVO)
	 */
	@Override
	protected FileOutVO handleAddFile(AuthenticationVO auth, FileInVO newFile, FileContentInVO newFileContent)
			throws Exception {
		checkFileInput(newFile);
		checkFileInput(newFileContent, newFile.getModule());
		FileDao fileDao = this.getFileDao();
		File file = fileDao.fileInVOToEntity(newFile);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(file, now, user);
		saveFileContent(file, newFileContent);
		return createFile(file, now, user);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#addFile(FileInVO, FileStreamInVO)
	 */
	@Override
	protected FileOutVO handleAddFile(AuthenticationVO auth, FileInVO newFile, FileStreamInVO newFileStream)
			throws Exception {
		checkFileInput(newFile);
		checkFileInput(newFileStream, newFile.getModule());
		FileDao fileDao = this.getFileDao();
		File file = fileDao.fileInVOToEntity(newFile);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(file, now, user);
		saveFileStream(file, newFileStream);
		try {
			return createFile(file, now, user);
		} catch (Exception e) {
			try {
				dropExternalFile(true, file.getExternalFileName());
			} catch (Exception e1) {
			}
			throw e;
		}
	}

	@Override
	protected FilePDFVO handleAggregatePDFFiles(AuthenticationVO auth, FileModule module, Long id, String logicalPath, boolean subTree, Boolean active, Boolean publicFile,
			PSFVO psf)
			throws Exception {
		checkFileModuleId(module, id);
		FileDao fileDao = this.getFileDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Collection files = fileDao.findFiles(module, id, logicalPath, subTree, active, publicFile, null, CoreUtil.PDF_MIMETYPE_STRING, psf);
		PDFMerger merger = new PDFMerger(fileDao);
		merger.setFiles(files);
		fileDao.toFileOutVOCollection(files);
		FilePDFVO pdfVO = new FilePDFVO();
		pdfVO.setContentTimestamp(now);
		pdfVO.setRequestingUser(this.getUserDao().toUserOutVO(user));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			merger.save(stream);
		} finally {
			stream.close();
		}
		byte[] documentData = stream.toByteArray();
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setFiles(files);
		StringBuilder fileName = new StringBuilder(PDFMerger.AGGREGATED_PDF_FILES_FILENAME_PREFIX);
		fileName.append(module.getValue());
		fileName.append("_");
		fileName.append(id);
		fileName.append("_");
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		addAggregatedPDFFilesExportedJournalEntry(module, id, pdfVO, now, user);
		pdfVO.setDocumentDatas(documentData);
		return pdfVO;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#deleteFile(Long)
	 */
	@Override
	protected FileOutVO handleDeleteFile(AuthenticationVO auth, Long fileId)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return deleteFileHelper(fileId, now, user);
	}

	@Override
	protected Collection<FileOutVO> handleDeleteFiles(AuthenticationVO auth, FileModule module, Long id, String logicalPath, boolean subTree, Boolean active, Boolean publicFile,
			PSFVO psf)
			throws Exception {
		checkFileModuleId(module, id);
		FileDao fileDao = this.getFileDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Iterator<File> it = fileDao.findFiles(module, id, logicalPath, subTree, active, publicFile, null, null, psf).iterator();
		ArrayList<FileOutVO> result = new ArrayList<FileOutVO>();
		while (it.hasNext()) {
			result.add(deleteFileHelper(it.next().getId(), now, user));
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#getFile(Long)
	 */
	@Override
	protected FileOutVO handleGetFile(AuthenticationVO auth, Long fileId)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File file = CheckIDUtil.checkFileId(fileId, fileDao);
		FileOutVO result = fileDao.toFileOutVO(file);
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#getFileContent(Long)
	 */
	@Override
	protected FileContentOutVO handleGetFileContent(AuthenticationVO auth, Long fileId)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File file = CheckIDUtil.checkFileId(fileId, fileDao);
		checkContentSize(file);
		FileContentOutVO result = fileDao.toFileContentOutVO(file);
		return result;
	}

	@Override
	protected long handleGetFileCount(AuthenticationVO auth, FileModule module, Long id, String logicalPath, boolean subTree, Boolean active, Boolean publicFile)
			throws Exception {
		checkFileModuleId(module, id);
		return this.getFileDao().getCount(module, id, logicalPath, subTree, active, publicFile, null, null);
	}

	@Override
	protected Collection<String> handleGetFileFolders(AuthenticationVO auth, FileModule module,
			Long id, String parentLogicalPath, boolean complete, Boolean active, Boolean publicFile, PSFVO psf) throws Exception {
		return this.getFileDao().findFileFolders(module, id, parentLogicalPath, complete, active, publicFile, null, psf);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#getFiles(FileModule, Long, String, Boolean, PSFVO)
	 */
	@Override
	protected Collection<FileOutVO> handleGetFiles(AuthenticationVO auth, FileModule module, Long id, String logicalPath, boolean subTree, Boolean active, Boolean publicFile,
			PSFVO psf)
			throws Exception {
		checkFileModuleId(module, id);
		FileDao fileDao = this.getFileDao();
		Collection files = fileDao.findFiles(module, id, logicalPath, subTree, active, publicFile, null, null, psf);
		fileDao.toFileOutVOCollection(files);
		return files;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#getFileStream(Long)
	 */
	@Override
	protected FileStreamOutVO handleGetFileStream(AuthenticationVO auth, Long fileId)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File file = CheckIDUtil.checkFileId(fileId, fileDao);
		FileStreamOutVO result = fileDao.toFileStreamOutVO(file);
		return result;
	}

	@Override
	protected long handleGetFolderSize(AuthenticationVO auth, FileModule module, Long id, String logicalPath, boolean subTree, Boolean active, Boolean publicFile)
			throws Exception {
		checkFileModuleId(module, id);
		return this.getFileDao().getFileSizeSum(module, id, logicalPath, subTree, active, publicFile, null, null);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#updateFile(FileInVO)
	 */
	@Override
	protected FileOutVO handleUpdateFile(AuthenticationVO auth, FileInVO modifiedFile)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File originalFile = CheckIDUtil.checkFileId(modifiedFile.getId(), fileDao, LockMode.UPGRADE_NOWAIT);
		checkFileInput(modifiedFile);
		if (!fileDao.toFileOutVO(originalFile).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_FILE);
		}
		FileOutVO original = fileDao.toFileOutVO(originalFile);
		fileDao.evict(originalFile);
		File file = fileDao.fileInVOToEntity(modifiedFile);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalFile, file, now, user);
		fileDao.update(file);
		return addFileUpdatedJournalEntry(file, original, now, user);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#updateFile(FileInVO, FileContentInVO)
	 */
	@Override
	protected FileOutVO handleUpdateFile(AuthenticationVO auth, FileInVO modifiedFile, FileContentInVO modifiedFileContent)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File originalFile = CheckIDUtil.checkFileId(modifiedFile.getId(), fileDao, LockMode.UPGRADE_NOWAIT);
		checkFileInput(modifiedFile);
		if (!fileDao.toFileOutVO(originalFile).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_FILE);
		}
		FileOutVO original = fileDao.toFileOutVO(originalFile);
		fileDao.evict(originalFile);
		File file = fileDao.fileInVOToEntity(modifiedFile);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalFile, file, now, user);
		saveFileContent(file, modifiedFileContent);
		fileDao.update(file);
		return addFileUpdatedJournalEntry(file, original, now, user);
	}

	/**
	 * @see org.phoenixctms.ctsms.service.shared.FileService#updateFile(FileInVO, FileStreamInVO)
	 */
	@Override
	protected FileOutVO handleUpdateFile(AuthenticationVO auth, FileInVO modifiedFile, FileStreamInVO modifiedFileStream)
			throws Exception {
		FileDao fileDao = this.getFileDao();
		File originalFile = CheckIDUtil.checkFileId(modifiedFile.getId(), fileDao, LockMode.UPGRADE_NOWAIT);
		checkFileInput(modifiedFile);
		if (!fileDao.toFileOutVO(originalFile).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_FILE);
		}
		checkFileInput(modifiedFileStream, modifiedFile.getModule());
		FileOutVO original = fileDao.toFileOutVO(originalFile);
		fileDao.evict(originalFile);
		File file = fileDao.fileInVOToEntity(modifiedFile);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalFile, file, now, user);
		saveFileStream(file, modifiedFileStream);
		try {
			fileDao.update(file);
			return addFileUpdatedJournalEntry(file, original, now, user);
		} catch (Exception e) {
			try {
				dropExternalFile(true, file.getExternalFileName());
			} catch (Exception e1) {
			}
			throw e;
		}
	}

	private int numIdsSet(FileInVO file) {
		int result = 0;
		result += (file.getInventoryId() != null) ? 1 : 0;
		result += (file.getStaffId() != null) ? 1 : 0;
		result += (file.getCourseId() != null) ? 1 : 0;
		result += (file.getTrialId() != null) ? 1 : 0;
		result += (file.getProbandId() != null) ? 1 : 0;
		result += (file.getMassMailId() != null) ? 1 : 0;
		return result;
	}

	private void saveFileContent(File file, FileContentInVO fileContent) throws Exception {
		file.setContentType(this.getMimeTypeDao().findByMimeTypeModule(fileContent.getMimeType(), file.getModule()).iterator().next());
		file.setExternalFile(false);
		if (CommonUtil.getUseFileEncryption(file.getModule())) {
			CipherText cipherText = CryptoUtil.encrypt(fileContent.getDatas());
			file.setDataIv(cipherText.getIv());
			file.setData(cipherText.getCipherText());
			cipherText = CryptoUtil.encryptValue(fileContent.getFileName());
			file.setFileNameIv(cipherText.getIv());
			file.setEncryptedFileName(cipherText.getCipherText());
			file.setFileNameHash(CryptoUtil.hashForSearch(fileContent.getFileName()));
			file.setFileName(null);
		} else {
			file.setFileName(fileContent.getFileName());
			file.setData(fileContent.getDatas());
			file.setFileNameIv(null);
			file.setEncryptedFileName(null);
			file.setDataIv(null);
			file.setFileNameHash(null);
		}
		file.setExternalFileName(null);
		file.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(fileContent.getDatas())));
		file.setSize(fileContent.getDatas().length);
	}

	private void saveFileStream(File file, FileStreamInVO fileStream) throws Exception {
		if (!Settings.getBoolean(SettingCodes.USE_EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.USE_EXTERNAL_FILE_DATA_DIR)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.EXTERNAL_FILE_DATA_DIR_DISABLED);
		}
		file.setContentType(this.getMimeTypeDao().findByMimeTypeModule(fileStream.getMimeType(), file.getModule()).iterator().next());
		file.setExternalFile(true);
		file.setData(null);
		String fileName = CoreUtil.createExternalFileName(CoreUtil.getExternalFileDirectoryPrefix(file.getModule()), fileStream.getFileName());
		java.io.File externalFile = new java.io.File(CoreUtil.getFileServiceExternalFilename(fileName));
		mkDir(externalFile);
		FileOutputStream externalFileWriter = new FileOutputStream(externalFile);
		long usableSpace;
		if (fileStream.getSize() != null && fileStream.getSize() > (usableSpace = externalFile.getUsableSpace())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INSUFFICIENT_SPACE_LEFT_ON_DISK, CommonUtil.humanReadableByteCount(usableSpace));
		}
		OutputStream externalFileStream;
		if (CommonUtil.getUseFileEncryption(file.getModule())) {
			CipherText cipherText = CryptoUtil.encryptValue(fileStream.getFileName());
			file.setFileNameIv(cipherText.getIv());
			file.setEncryptedFileName(cipherText.getCipherText());
			file.setFileNameHash(CryptoUtil.hashForSearch(fileStream.getFileName()));
			CipherStream cipherStream = CryptoUtil.createEncryptionStream(externalFileWriter);
			file.setDataIv(cipherStream.getIv());
			externalFileStream = cipherStream;
			file.setFileName(null);
		} else {
			file.setFileName(fileStream.getFileName());
			externalFileStream = externalFileWriter;
			file.setDataIv(null);
			file.setFileNameIv(null);
			file.setEncryptedFileName(null);
			file.setFileNameHash(null);
		}
		int nRead;
		long totalRead = 0;
		Integer externalFileSizeLimit = Settings.getIntNullable(SettingCodes.EXTERNAL_FILE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_SIZE_LIMIT);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		InputStream inputStream = fileStream.getStream();
		byte[] block = new byte[CommonUtil.INPUTSTREAM_BUFFER_BLOCKSIZE];
		try {
			while ((nRead = inputStream.read(block, 0, block.length)) != -1) {
				externalFileStream.write(block, 0, nRead);
				md5.update(block, 0, nRead);
				totalRead += nRead;
				if (externalFileSizeLimit != null && totalRead > externalFileSizeLimit) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.EXTERNAL_FILE_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(externalFileSizeLimit));
				}
			}
			externalFileStream.flush();
		} catch (Exception e) {
			try {
				externalFileStream.close();
				dropExternalFile(true, fileName);
			} catch (Exception e1) {
			}
			throw e;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
			try {
				externalFileStream.close();
			} catch (IOException e) {
			}
		}
		file.setExternalFileName(fileName);
		file.setMd5(CommonUtil.getHex(md5.digest()));
		file.setSize(totalRead);
	}
}