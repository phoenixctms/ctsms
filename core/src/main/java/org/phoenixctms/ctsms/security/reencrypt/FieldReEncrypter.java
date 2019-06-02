package org.phoenixctms.ctsms.security.reencrypt;

import javax.crypto.SecretKey;

import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;

public abstract class FieldReEncrypter<T> extends ReEncrypter<T> {

	@Override
	protected void reEncrypt(T item, SecretKey oldDepartmentKey, SecretKey newDepartmentKey) throws Exception {
		byte[] plainText = CryptoUtil.decrypt(getIv(item), oldDepartmentKey, getEncrypted(item));
		CipherText cipherText = CryptoUtil.encrypt(newDepartmentKey, plainText);
		setIv(item, cipherText.getIv());
		setEncrypted(item, cipherText.getCipherText());
		setHash(item, CryptoUtil.hashForSearch(newDepartmentKey, plainText));
	}

	protected abstract byte[] getIv(T item);

	protected abstract byte[] getEncrypted(T item);

	protected abstract void setIv(T item, byte[] iv);

	protected abstract void setEncrypted(T item, byte[] cipherText);

	protected abstract void setHash(T item, byte[] hash);
}
