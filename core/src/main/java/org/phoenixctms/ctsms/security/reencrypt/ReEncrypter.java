package org.phoenixctms.ctsms.security.reencrypt;

import java.util.Collection;
import java.util.Iterator;

import javax.crypto.SecretKey;

import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.security.CryptoUtil;

public abstract class ReEncrypter<T> {

	public static SecretKey getDepartmenKey(Department department, String plainDepartmentPassword) throws Exception {
		return CryptoUtil.getDepartmentKey(CryptoUtil.decryptDepartmentKey(department, plainDepartmentPassword));
	}

	protected abstract void reEncrypt(T item, SecretKey oldDepartmentKey, SecretKey newDepartmentKey) throws Exception;

	public static <E> void reEncrypt(E item, Collection<ReEncrypter<E>> reEncrypters, SecretKey oldDepartmentKey, SecretKey newDepartmentKey) throws Exception {
		Iterator<ReEncrypter<E>> it = reEncrypters.iterator();
		while (it.hasNext()) {
			ReEncrypter<E> reEncrypter = it.next();
			if (!reEncrypter.isSkip(item)) {
				reEncrypter.reEncrypt(item, oldDepartmentKey, newDepartmentKey);
			}
		}
	}

	protected boolean isSkip(T item) {
		return false;
	}
}
