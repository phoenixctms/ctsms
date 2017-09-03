package org.phoenixctms.ctsms.executable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.service.shared.FileService;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.springframework.beans.factory.annotation.Autowired;

public class FileSystemLoader {

	// http://www.javapractices.com/topic/TopicAction.do?Id=68
	@Autowired
	private FileDao fileDao;
	@Autowired
	private FileService fileService;
	private final static boolean FILE_ACTIVE = true;
	private final static boolean SORT_FILES = true;
	private final static boolean SKIP_ERRORS = true;
	private final static java.util.regex.Pattern PATH_SEPARATOR_REGEXP = Pattern.compile(Pattern.quote(java.io.File.separator));

	private static List<java.io.File> getFileListing(java.io.File rootDir, boolean sort, JobOutput jobOutput) throws Exception {
		validateDirectory(rootDir, false, jobOutput);
		List<java.io.File> result = getFileListingNoSort(rootDir, jobOutput);
		if (sort) {
			Collections.sort(result);
		}
		return result;
	}

	private static List<java.io.File> getFileListingNoSort(java.io.File dir, JobOutput jobOutput) throws Exception {
		List<java.io.File> result = new ArrayList<java.io.File>();
		java.io.File[] filesAndDirs = dir.listFiles();
		if (filesAndDirs != null && filesAndDirs.length > 0) {
			for (int i = 0; i < filesAndDirs.length; i++) {
				if (filesAndDirs[i] != null) {
					if (!filesAndDirs[i].isFile()) {
						if (validateDirectory(filesAndDirs[i], true, jobOutput)) {
							result.addAll(getFileListingNoSort(filesAndDirs[i], jobOutput));
						}
					} else {
						result.add(filesAndDirs[i]);
					}
				}
			}
		}
		return result;
	}

	private static boolean validateDirectory(java.io.File dir, boolean supressExceptions, JobOutput jobOutput) throws Exception {
		String errorMessage;
		if (dir == null) {
			errorMessage = "directory should not be null";
			if (supressExceptions) {
				jobOutput.println(errorMessage);
				return false;
			} else {
				throw new IllegalArgumentException(errorMessage);
			}
		}
		if (!dir.exists()) {
			errorMessage = "directory does not exist: " + dir;
			if (supressExceptions) {
				jobOutput.println(errorMessage);
				return false;
			} else {
				throw new FileNotFoundException(errorMessage);
			}
		}
		if (!dir.isDirectory()) {
			errorMessage = "is not a directory: " + dir;
			if (supressExceptions) {
				jobOutput.println(errorMessage);
				return false;
			} else {
				throw new IllegalArgumentException(errorMessage);
			}
		}
		if (!dir.canRead()) {
			errorMessage = "directory cannot be read: " + dir;
			if (supressExceptions) {
				jobOutput.println(errorMessage);
				return false;
			} else {
				throw new IllegalArgumentException(errorMessage);
			}
		}
		return true;
	}


	private JobOutput jobOutput;

	public FileSystemLoader() {

	}

	public long deleteFileRecords(final FileModule module, final long id, final boolean deleteFiles) throws Exception {
		jobOutput.println(module + " entity id: " + id);
		ChunkedDaoOperationAdapter<FileDao, File> fileRemover = new ChunkedDaoOperationAdapter<FileDao, File>(fileDao) {

			@Override
			protected boolean isIncrementPageNumber() {
				return false;
			}

			@Override
			protected java.util.Collection<File> load(int pageNumber, int pageSize) throws Exception {
				PSFVO psf = new PSFVO();
				psf.setSortField("id");
				psf.setSortOrder(true);
				if (pageNumber > 0 && pageSize > 0) {
					psf.setFirst(pageNumber - 1);
					psf.setPageSize(pageSize);
				}
				return dao.findFiles(module, id, null, null, null, null, psf);
			}

			@Override
			protected boolean process(Collection<File> page, Object passThrough) throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				ArrayList<String> externalFileNames = null;
				if (deleteFiles) {
					externalFileNames = new ArrayList<String>(page.size());
					Iterator<File> it = page.iterator();
					while (it.hasNext()) {
						externalFileNames.add(it.next().getExternalFileName());
					}
				}
				dao.remove(page);
				int filesDeleted = 0;
				if (externalFileNames != null && externalFileNames.size() > 0) {
					Iterator<String> it = externalFileNames.iterator();
					while (it.hasNext()) {
						if ((new java.io.File(CoreUtil.getFileServiceExternalFilename(it.next()))).delete()) {
							filesDeleted++;
						}
					}
				}
				inOut.put("fileRecordsRemoved", ((Long) inOut.get("fileRecordsRemoved")) + page.size());
				inOut.put("filesDeleted", ((Long) inOut.get("filesDeleted")) + filesDeleted);
				return true;
			}

			@Override
			protected boolean process(File entity, Object passThrough) throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				String externalFileName = deleteFiles ? entity.getExternalFileName() : null;
				dao.remove(entity);
				if (externalFileName != null) {
					if ((new java.io.File(CoreUtil.getFileServiceExternalFilename(externalFileName))).delete()) {
						inOut.put("filesDeleted", ((Long) inOut.get("filesDeleted")) + 1l);
					}
				}
				inOut.put("fileRecordsRemoved", ((Long) inOut.get("fileRecordsRemoved")) + 1l);
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("fileRecordsRemoved", 0l);
		passThrough.put("filesDeleted", 0l);
		fileRemover.processPages(passThrough);
		long fileRecordsRemoved = (Long) passThrough.get("fileRecordsRemoved");
		long filesDeleted = (Long) passThrough.get("filesDeleted");
		jobOutput.println(fileRecordsRemoved + " file records removed and " + filesDeleted + " files deleted");
		return fileRecordsRemoved;
	}

	public long importFiles(AuthenticationVO auth, String dir, FileModule module, long id) throws Throwable {
		jobOutput.println("user: " + auth.getUsername());
		jobOutput.println(module + " entity id: " + id);
		java.io.File rootDir = new java.io.File(dir);
		int rootDirLength = rootDir.getCanonicalPath().length();
		String logicalPathSeparatorReplacement = Matcher.quoteReplacement(CommonUtil.LOGICAL_PATH_SEPARATOR);
		Iterator<java.io.File> filesIt = getFileListing(rootDir, SORT_FILES, jobOutput).iterator();
		String timestamp = Settings.getSimpleDateFormat(SettingCodes.JOURNAL_ENTRY_COMMENT_DATETIME_PATTERN, Bundle.SETTINGS,
				DefaultSettings.JOURNAL_ENTRY_COMMENT_DATETIME_PATTERN, Locales.JOURNAL).format(jobOutput.getStart());
		long fileCount = 0;
		while (filesIt.hasNext()) {
			java.io.File file = filesIt.next();
			FileInVO newFile = new FileInVO();
			newFile.setActive(FILE_ACTIVE);
			switch (module) {
				case INVENTORY_DOCUMENT:
					newFile.setInventoryId(id);
					break;
				case STAFF_DOCUMENT:
					newFile.setStaffId(id);
					break;
				case COURSE_DOCUMENT:
					newFile.setCourseId(id);
					break;
				case TRIAL_DOCUMENT:
					newFile.setTrialId(id);
					break;
				case PROBAND_DOCUMENT:
					newFile.setProbandId(id);
					break;
					// case INPUT_FIELD_DOCUMENT:
					// newFile.setInputFieldId(id);
					// break;
				default:
			}
			newFile.setModule(module);
			newFile.setLogicalPath(CommonUtil.fixLogicalPathFolderName(PATH_SEPARATOR_REGEXP.matcher(file.getParentFile().getCanonicalPath().substring(rootDirLength)).replaceAll(
					logicalPathSeparatorReplacement)));
			newFile.setComment("file system import: " + timestamp);
			newFile.setTitle(file.getName());
			FileStreamInVO newFileStream = new FileStreamInVO();
			newFileStream.setFileName(file.getName());
			newFileStream.setSize(file.length());
			FileInputStream inputStream = null;
			try {
				newFileStream.setMimeType(ExecUtil.getMimeType(file));
				inputStream = new FileInputStream(file);
				newFileStream.setStream(inputStream);
				fileService.addFile(auth, newFile, newFileStream);
				fileCount++;
			} catch (Throwable t) {
				if (!SKIP_ERRORS) {
					throw t;
				} else {
					jobOutput.println(t.getMessage());
				}
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}
		jobOutput.println(fileCount + " file records created");
		return fileCount;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
