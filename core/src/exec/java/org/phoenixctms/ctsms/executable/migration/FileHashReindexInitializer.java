package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.springframework.beans.factory.annotation.Autowired;

public class FileHashReindexInitializer extends EncryptedStringHashReindexInitializer<File, FileDao> {

	@Autowired
	private FileDao fileDao;

	@Override
	protected FileDao getDao() {
		return fileDao;
	}

	@Override
	protected void persist(FileDao dao, File entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<File>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("fileNameHash", File::getFileNameIv, File::getEncryptedFileName, File::getFileNameHash, File::setFileNameHash),
				encryptedStringField("titleHash", File::getTitleIv, File::getEncryptedTitle, File::getTitleHash, File::setTitleHash),
				encryptedStringField("commentHash", File::getCommentIv, File::getEncryptedComment, File::getCommentHash, File::setCommentHash));
	}
}
