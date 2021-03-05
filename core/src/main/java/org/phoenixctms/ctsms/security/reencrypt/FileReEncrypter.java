package org.phoenixctms.ctsms.security.reencrypt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.SecretKey;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.security.CipherStream;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.VerifyMD5InputStream;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;

public abstract class FileReEncrypter<T> extends ReEncrypter<T> {

	@Override
	protected void reEncrypt(T item, SecretKey oldDepartmentKey, SecretKey newDepartmentKey) throws Exception {
		InputStream plainTextStream = null;
		String newFileName = CoreUtil.createExternalFileName(CoreUtil.getExternalFileDirectoryPrefix(getModule(item)), getExternalFileName(item));
		java.io.File externalFile = new java.io.File(CoreUtil.getFileServiceExternalFilename(newFileName));
		CipherStream cipherStream = CryptoUtil.createEncryptionStream(newDepartmentKey, new FileOutputStream(externalFile));
		int nRead;
		long totalRead = 0;
		byte[] block = new byte[CommonUtil.INPUTSTREAM_BUFFER_BLOCKSIZE];
		try {
			plainTextStream = new VerifyMD5InputStream(
					CryptoUtil.createDecryptionStream(getIv(item), oldDepartmentKey, CoreUtil.createFileServiceFileInputStream(getExternalFileName(item))), getMd5(item));
			while ((nRead = plainTextStream.read(block, 0, block.length)) != -1) {
				cipherStream.write(block, 0, nRead);
				totalRead += nRead;
			}
			cipherStream.flush();
		} finally {
			if (plainTextStream != null) {
				try {
					plainTextStream.close(); //silence sonarcloud
				} catch (IOException e) {
				}
			}
			try {
				cipherStream.close();
			} catch (IOException e) {
			}
		}
		setExternalFileName(item, newFileName);
		setIv(item, cipherStream.getIv());
	}

	protected abstract FileModule getModule(T item);

	protected abstract String getMd5(T item);

	protected abstract String getExternalFileName(T item);

	protected abstract void setExternalFileName(T item, String externalFileName);

	protected abstract byte[] getIv(T item);

	protected abstract void setIv(T item, byte[] iv);
}
