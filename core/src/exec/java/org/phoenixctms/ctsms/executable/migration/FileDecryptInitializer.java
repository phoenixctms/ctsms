package org.phoenixctms.ctsms.executable.migration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.VerifyMD5InputStream;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class FileDecryptInitializer extends EncryptedFieldInitializer {

	private static String decryptExternalFile(File file) throws Exception {
		InputStream fileStream = null;
		String newFileName = CoreUtil.createExternalFileName(CoreUtil.getExternalFileDirectoryPrefix(file.getModule()), file.getFileName());
		java.io.File externalFile = new java.io.File(CoreUtil.getFileServiceExternalFilename(newFileName));
		FileOutputStream externalFileStream = new FileOutputStream(externalFile);
		int nRead;
		long totalRead = 0;
		byte[] block = new byte[CommonUtil.INPUTSTREAM_BUFFER_BLOCKSIZE];
		try {
			fileStream = new VerifyMD5InputStream(
					CryptoUtil.createDecryptionStream(file.getDataIv(), CoreUtil.createFileServiceFileInputStream(file.getExternalFileName())), file.getMd5());
			while ((nRead = fileStream.read(block, 0, block.length)) != -1) {
				externalFileStream.write(block, 0, nRead);
				totalRead += nRead;
			}
			externalFileStream.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileStream != null) {
				try {
					fileStream.close(); //silence sonarcloud
				} catch (IOException e) {
				}
			}
			try {
				externalFileStream.close();
			} catch (IOException e) {
			}
		}
		return newFileName;
	}

	@Autowired
	private FileDao fileDao;
	private ChunkedDaoOperationAdapter<FileDao, File> fileProcessor;

	public FileDecryptInitializer() {
	}

	@Override
	public long update(AuthenticationVO auth) throws Exception {
		authenticate(auth);
		final FileModule module = FileModule.TRIAL_DOCUMENT;
		fileProcessor = new ChunkedDaoOperationAdapter<FileDao, File>(fileDao) {

			@Override
			protected boolean process(Collection<File> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(File file, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				if (file.isExternalFile() && module.equals(file.getModule())) {
					try {
						file.setExternalFileName(decryptExternalFile(file));
						file.setFileName((String) CryptoUtil.decryptValue(file.getFileNameIv(), file.getEncryptedFileName()));
						file.setTitle((String) CryptoUtil.decryptValue(file.getTitleIv(), file.getEncryptedTitle()));
						file.setComment((String) CryptoUtil.decryptValue(file.getCommentIv(), file.getEncryptedComment()));
						file.setTitleHash(null);
						file.setEncryptedTitle(null);
						file.setTitleIv(null);
						file.setCommentHash(null);
						file.setEncryptedComment(null);
						file.setCommentIv(null);
						file.setDataIv(null);
						file.setFileNameIv(null);
						file.setEncryptedFileName(null);
						file.setFileNameHash(null);
						this.dao.update(file);
						jobOutput.println("row updated");
						inOut.put("updated", ((Long) inOut.get("updated")) + 1l);
					} catch (Exception e) {
						jobOutput.println("row skipped: " + e.getMessage());
						inOut.put("skipped", ((Long) inOut.get("skipped")) + 1l);
					}
				}
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("updated", 0l);
		passThrough.put("skipped", 0l);
		fileProcessor.processEach(PageSizes.BIG, passThrough);
		long updated = (Long) passThrough.get("updated");
		long skipped = (Long) passThrough.get("skipped");
		jobOutput.println(updated + " rows updated, " + skipped + " skipped");
		return updated;
	}
}
