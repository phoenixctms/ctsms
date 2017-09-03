package org.phoenixctms.ctsms.executable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.springframework.beans.factory.annotation.Autowired;

public class ExternalFileChecker {

	@Autowired
	protected FileDao fileDao;
	private boolean useExternalFileDataDir;
	private String externalFileDataDir;
	private JobOutput jobOutput;

	public ExternalFileChecker() throws Exception {
		useExternalFileDataDir = Settings.getBoolean(SettingCodes.USE_EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.USE_EXTERNAL_FILE_DATA_DIR);
		if (useExternalFileDataDir) {
			externalFileDataDir = Settings.getDirectory(SettingCodes.EXTERNAL_FILE_DATA_DIR, Bundle.SETTINGS, DefaultSettings.EXTERNAL_FILE_DATA_DIR);
		} else {
			externalFileDataDir = null;
		}
	}

	public long scanMissingExternalFiles(boolean delete) throws Exception {
		long missingFileCount = 0;
		if (useExternalFileDataDir) {
			ChunkedDaoOperationAdapter<FileDao, File> fileProcessor = new ChunkedDaoOperationAdapter<FileDao, File>(fileDao) {

				@Override
				protected boolean isIncrementPageNumber() {
					return true;
				}

				@Override
				protected boolean process(Collection<File> page, Object passThrough) throws Exception {
					return false;
				}

				@Override
				protected boolean process(File entity, Object passThrough) throws Exception {
					if (entity.isExternalFile() && !testExternalFileExists(entity)) {
						Map<String, Object> inOut = (Map<String, Object>) passThrough;
						if (((Boolean) inOut.get("delete"))) {
							dao.remove(entity);
							jobOutput.println("file record ID " + entity.getId() + " removed - refers to missing file " + entity.getExternalFileName());
						} else {
							jobOutput.println("file record ID " + entity.getId() + " found - refers to missing file " + entity.getExternalFileName());
						}
						inOut.put("missingFileCount", ((Long) inOut.get("missingFileCount")) + 1l);
					}
					return true;
				}
			};
			Map<String, Object> passThrough = new HashMap<String, Object>();
			passThrough.put("delete", delete);
			passThrough.put("missingFileCount", 0l);
			fileProcessor.processEach(passThrough);
			missingFileCount = (Long) passThrough.get("missingFileCount");
		}
		jobOutput.println(missingFileCount + " file records with missing external files");
		return missingFileCount;
	}

	public long scanOrphanedExternalFiles(boolean delete) throws Exception {
		if (useExternalFileDataDir) {
			long orphanedFileCount = scanOrphanedExternalFilesHelper(new java.io.File(externalFileDataDir), delete, 0);
			jobOutput.println(orphanedFileCount + " foreign files in " + externalFileDataDir);
			return orphanedFileCount;
		} else {
			jobOutput.println("external file datadir disabled");
			return 0;
		}
	}

	private long scanOrphanedExternalFilesHelper(java.io.File dirFile, boolean delete, long orphanedFileCount) throws Exception {
		if (dirFile.isDirectory()) {
			String[] filesAndDirs = dirFile.list();
			for (int i = 0; i < filesAndDirs.length; i++) {
				orphanedFileCount = scanOrphanedExternalFilesHelper(new java.io.File(dirFile, filesAndDirs[i]), delete, orphanedFileCount);
			}
		} else if (dirFile.isFile()) {
			if (testExternalFileOrphaned(dirFile)) {
				if (delete) {
					dirFile.delete();
					jobOutput.println("orphaned file deleted: " + dirFile.getCanonicalPath());
				} else {
					jobOutput.println("orphaned file found: " + dirFile.getCanonicalPath());
				}
				orphanedFileCount++;
			}
		}
		return orphanedFileCount;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	private boolean testExternalFileExists(File file) throws Exception {
		return (new java.io.File(CoreUtil.getFileServiceExternalFilename(file.getExternalFileName()))).exists();
	}

	private boolean testExternalFileOrphaned(java.io.File file) throws Exception {
		String externalFileName = file.getCanonicalPath();
		if (externalFileName.startsWith(externalFileDataDir)) {
			externalFileName = externalFileName.substring(externalFileDataDir.length() + 1);
			for (int i = 0; i < CommonUtil.FILE_PATH_SEPARATORS.length; i++) {
				if (fileDao.getCount(CommonUtil.sanitizeFilePath(externalFileName, CommonUtil.FILE_PATH_SEPARATORS[i])) > 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
