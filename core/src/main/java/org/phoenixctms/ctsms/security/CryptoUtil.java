package org.phoenixctms.ctsms.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public final class CryptoUtil {

	private static final String SALT_RANDOM_ALGORITHM = CoreUtil.RANDOM_ALGORITHM;
	private static final int PBE_KEY_ITERATIONS = 1; // 1000;
	private static final String PBE_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String SYMMETRIC_ALGORITHM = "AES";
	private static final String SYMMETRIC_ALGORITHM_MODE = SYMMETRIC_ALGORITHM + "/CBC/PKCS5Padding";
	private static final int SYMMETRIC_KEY_LENGTH = 128;
	private static final int SALT_LENGTH = 16;
	private static final String ASYMMETRIC_ALGORITHM = "RSA";
	private static final String PADDED_ASYMMETRIC_ALGORITHM = ASYMMETRIC_ALGORITHM + "/ECB/PKCS1Padding";
	private static final int ASYMMETRIC_KEY_LENGTH = 1024;
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	public static final int SEED = 100; // (new Random()).nextInt(100);
	private static final String JWT_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String JWT_ALGORITHM = "HMACSHA256";
	private static final int JWT_KEY_LENGTH = 256;
	private static final int JWT_KEY_ITERATIONS = 1;

	public static boolean checkDepartmentPassword(Department department, String plainDepartmentPassword) throws Exception {
		return Arrays.equals(hashPassword(department.getDepartmentPasswordSalt(), plainDepartmentPassword), department.getDepartmentPasswordHash());
	}

	public static boolean checkPassword(Password lastPassword, String plainPassword) throws Exception {
		return Arrays.equals(hashPassword(lastPassword.getPasswordHashSalt(), plainPassword), lastPassword.getPasswordHash());
	}

	public static InputStream createDecryptionStream(byte[] iv, byte[] salt, String password, InputStream inputStream) throws Exception {
		return new CipherInputStream(inputStream, getDecryptionCipher(iv, salt, password));
	}

	public static InputStream createDecryptionStream(byte[] iv, InputStream inputStream) throws Exception {
		return new CipherInputStream(inputStream, getDecryptionCipher(iv));
	}

	public static InputStream createDecryptionStream(byte[] iv, SecretKey departmentKey, InputStream inputStream) throws Exception {
		return new CipherInputStream(inputStream, getDecryptionCipher(iv, departmentKey));
	}

	public static CipherOutputStream createEncryptionStream(byte[] iv, byte[] salt, String password, OutputStream outputStream) throws Exception {
		return new CipherOutputStream(outputStream, getEncryptionCipher(iv, salt, password));
	}

	public static CipherOutputStream createEncryptionStream(byte[] iv, OutputStream outputStream) throws Exception {
		return new CipherOutputStream(outputStream, getEncryptionCipher(iv));
	}

	public static CipherStream createEncryptionStream(byte[] salt, String password, OutputStream outputStream) throws Exception {
		return new CipherStream(outputStream, getEncryptionCipher(salt, password));
	}

	public static CipherStream createEncryptionStream(OutputStream outputStream) throws Exception {
		return new CipherStream(outputStream, getEncryptionCipher());
	}

	public static CipherStream createEncryptionStream(SecretKey departmentKey, OutputStream outputStream) throws Exception {
		return new CipherStream(outputStream, getEncryptionCipher(departmentKey));
	}

	public static KeyPair createKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ASYMMETRIC_ALGORITHM);
		keyGen.initialize(ASYMMETRIC_KEY_LENGTH);
		return keyGen.genKeyPair();
	}

	private static Cipher getEncryptionCipher(PublicKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(PADDED_ASYMMETRIC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher;
	}

	private static Cipher getDecryptionCipher(PrivateKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(PADDED_ASYMMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher;
	}

	private static Cipher getEncryptionCipher(PrivateKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(PADDED_ASYMMETRIC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher;
	}

	private static Cipher getDecryptionCipher(PublicKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(PADDED_ASYMMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher;
	}

	public static byte[] encrypt(PublicKey key, byte[] plainText) throws Exception {
		return encrypt(getEncryptionCipher(key), plainText);
	}

	public static byte[] decrypt(PrivateKey key, byte[] cipherText) throws Exception {
		return decrypt(getDecryptionCipher(key), cipherText);
	}

	public static byte[] encrypt(PrivateKey key, byte[] plainText) throws Exception {
		return encrypt(getEncryptionCipher(key), plainText);
	}

	public static byte[] decrypt(PublicKey key, byte[] cipherText) throws Exception {
		return decrypt(getDecryptionCipher(key), cipherText);
	}

	private static SecretKey createPBEKey(byte[] salt, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (password == null || password.length() == 0) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PBE_PASSWORD_ZERO_LENGTH_ERROR, DefaultMessages.PBE_PASSWORD_ZERO_LENGTH_ERROR));
		}
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_KEY_ALGORITHM);
		PBEKey pbeKey = (PBEKey) factory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, PBE_KEY_ITERATIONS, SYMMETRIC_KEY_LENGTH));
		return new SecretKeySpec(pbeKey.getEncoded(), SYMMETRIC_ALGORITHM);
	}

	public static SecretKey createJwtKey(SecretKey departmentKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(JWT_KEY_ALGORITHM);
		PBEKey pbeKey = (PBEKey) factory.generateSecret(new PBEKeySpec(null, departmentKey.getEncoded(), JWT_KEY_ITERATIONS, JWT_KEY_LENGTH));
		return new SecretKeySpec(pbeKey.getEncoded(), JWT_ALGORITHM);
	}

	public static SecretKeySpec createRandomKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
		kgen.init(SYMMETRIC_KEY_LENGTH);
		return new SecretKeySpec(kgen.generateKey().getEncoded(), SYMMETRIC_ALGORITHM);
	}

	public static byte[] createSalt() throws NoSuchAlgorithmException {
		SecureRandom rand = SecureRandom.getInstance(SALT_RANDOM_ALGORITHM);
		byte[] salt = new byte[SALT_LENGTH];
		rand.nextBytes(salt);
		return salt;
	}

	public static byte[] decrypt(byte[] iv, byte[] cipherText) throws Exception {
		return decrypt(getDecryptionCipher(iv), cipherText);
	}

	public static byte[] decrypt(byte[] iv, byte[] salt, String password, byte[] cipherText) throws Exception {
		return decrypt(getDecryptionCipher(iv, salt, password), cipherText);
	}

	public static byte[] decrypt(byte[] iv, SecretKey departmentKey, byte[] cipherText) throws Exception {
		return decrypt(getDecryptionCipher(iv, departmentKey), cipherText);
	}

	private static byte[] decrypt(Cipher cipher, byte[] cipherText) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		// http://stackoverflow.com/questions/6181524/getting-nearly-double-the-length-when-reading-byte-from-postgres-with-jpa
		int ctLength = cipherText.length;
		byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
		int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
		ptLength += cipher.doFinal(plainText, ptLength);
		return Arrays.copyOf(plainText, ptLength);
	}

	public static byte[] decryptDepartmentKey(Department department, String plainDepartmentPassword) throws Exception {
		return decrypt(department.getKeyIv(), department.getKeySalt(), plainDepartmentPassword, department.getEncryptedKey());
	}

	public static String decryptDepartmentPassword(Password password, String plainPassword) throws Exception {
		return (String) decryptValue(password.getDepartmentPasswordIv(), password.getDepartmentPasswordSalt(), plainPassword, password.getEncryptedDepartmentPassword());
	}

	public static String decryptOtpSecret(Password password, String plainPassword) throws Exception {
		if (password.getEncryptedOtpSecret() != null) {
			return (String) decryptValue(password.getOtpSecretIv(), password.getOtpSecretSalt(), plainPassword, password.getEncryptedOtpSecret());
		}
		return null;
	}

	public static String decryptPassword(Password password, String plainDepartmentPassword) throws Exception {
		return (String) decryptValue(password.getPasswordIv(), password.getPasswordSalt(), plainDepartmentPassword, password.getEncryptedPassword());
	}

	public static byte[] decryptPrivateKey(org.phoenixctms.ctsms.domain.KeyPair keyPair, String plainDepartmentPassword) throws Exception {
		return decrypt(keyPair.getPrivateKeyIv(), keyPair.getPrivateKeySalt(), plainDepartmentPassword, keyPair.getEncryptedPrivateKey());
	}

	public static Object decryptValue(byte[] iv, byte[] encryptedValue) throws Exception {
		return CoreUtil.deserialize(decrypt(iv, encryptedValue));
	}

	public static Object decryptValue(byte[] iv, byte[] salt, String password, byte[] encryptedValue) throws Exception {
		return CoreUtil.deserialize(decrypt(iv, salt, password, encryptedValue));
	}

	public static CipherText encrypt(byte[] plainText) throws Exception {
		Cipher cipher = getEncryptionCipher();
		return new CipherText(cipher.getIV(), encrypt(cipher, plainText));
	}

	private static byte[] encrypt(byte[] iv, byte[] plainText) throws Exception {
		return encrypt(getEncryptionCipher(iv), plainText);
	}

	private static byte[] encrypt(byte[] iv, byte[] salt, String password, byte[] plainText) throws Exception {
		return encrypt(getEncryptionCipher(iv, salt, password), plainText);
	}

	public static CipherText encrypt(byte[] salt, String password, byte[] plainText) throws Exception {
		Cipher cipher = getEncryptionCipher(salt, password);
		return new CipherText(cipher.getIV(), encrypt(cipher, plainText));
	}

	public static CipherText encrypt(SecretKey departmentKey, byte[] plainText) throws Exception {
		Cipher cipher = getEncryptionCipher(departmentKey);
		return new CipherText(cipher.getIV(), encrypt(cipher, plainText));
	}

	private static byte[] encrypt(Cipher cipher, byte[] plainText) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		byte[] cipherText = new byte[cipher.getOutputSize(plainText.length)];
		int ctLength = cipher.update(plainText, 0, plainText.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		return Arrays.copyOf(cipherText, ctLength);
	}

	public static void encryptDepartmentKey(Department department, byte[] plainKey, String plainDepartmentPassword) throws Exception {
		byte[] salt;
		CipherText cipherText;
		salt = createSalt();
		cipherText = encrypt(salt, plainDepartmentPassword, plainKey);
		department.setKeyIv(cipherText.getIv());
		department.setKeySalt(salt);
		department.setEncryptedKey(cipherText.getCipherText());
		salt = createSalt();
		department.setDepartmentPasswordSalt(salt);
		department.setDepartmentPasswordHash(hashPassword(salt, plainDepartmentPassword));
	}

	private static byte[] encryptHashForSearch(byte[] md5Hash) throws Exception {
		return encryptHashForSearch(getDepartmentKey(), md5Hash);
	}

	private static byte[] encryptHashForSearch(byte[] salt, String password, byte[] md5Hash) throws Exception {
		return encryptHashForSearch(createPBEKey(salt, password), md5Hash);
	}

	private static byte[] encryptHashForSearch(SecretKey key, byte[] md5Hash) throws Exception {
		if (Settings.getBoolean(SettingCodes.HASH_FOR_SEARCH, Bundle.SETTINGS, DefaultSettings.HASH_FOR_SEARCH)) {
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(md5Hash);
		}
		return null;
	}

	public static void encryptPasswords(Password password, String plainPassword, String plainDepartmentPassword, String otpSecret) throws Exception {
		byte[] salt;
		CipherText cipherText;
		salt = createSalt();
		password.setPasswordHashSalt(salt);
		password.setPasswordHash(hashPassword(salt, plainPassword));
		salt = createSalt();
		cipherText = encryptValue(salt, plainPassword, plainDepartmentPassword);
		password.setDepartmentPasswordIv(cipherText.getIv());
		password.setDepartmentPasswordSalt(salt);
		password.setEncryptedDepartmentPassword(cipherText.getCipherText());
		salt = createSalt();
		cipherText = encryptValue(salt, plainDepartmentPassword, plainPassword);
		password.setPasswordIv(cipherText.getIv());
		password.setPasswordSalt(salt);
		password.setEncryptedPassword(cipherText.getCipherText());
		if (otpSecret != null) {
			salt = createSalt();
			cipherText = encryptValue(salt, plainPassword, otpSecret);
			password.setOtpSecretIv(cipherText.getIv());
			password.setOtpSecretSalt(salt);
			password.setEncryptedOtpSecret(cipherText.getCipherText());
		} else {
			password.setOtpSecretIv(null);
			password.setOtpSecretSalt(null);
			password.setEncryptedOtpSecret(null);
		}
	}

	public static void encryptPrivateKey(org.phoenixctms.ctsms.domain.KeyPair keyPair, byte[] plainPrivateKey, String plainDepartmentPassword) throws Exception {
		byte[] salt = createSalt();
		CipherText cipherText = encrypt(salt, plainDepartmentPassword, plainPrivateKey);
		keyPair.setPrivateKeyIv(cipherText.getIv());
		keyPair.setPrivateKeySalt(salt);
		keyPair.setEncryptedPrivateKey(cipherText.getCipherText());
	}

	private static byte[] encryptValue(byte[] iv, byte[] salt, String password, Serializable value) throws Exception {
		return encrypt(iv, salt, password, CoreUtil.serialize(value));
	}

	private static byte[] encryptValue(byte[] iv, Serializable value) throws Exception {
		return encrypt(iv, CoreUtil.serialize(value));
	}

	public static CipherText encryptValue(byte[] salt, String password, Serializable value) throws Exception {
		Cipher cipher = getEncryptionCipher(salt, password);
		return new CipherText(cipher.getIV(), encrypt(cipher, CoreUtil.serialize(value)));
	}

	public static CipherText encryptValue(Serializable value) throws Exception {
		Cipher cipher = getEncryptionCipher();
		return new CipherText(cipher.getIV(), encrypt(cipher, CoreUtil.serialize(value)));
	}

	private static Cipher getDecryptionCipher(byte[] iv) throws Exception {
		return getDecryptionCipher(iv, getDepartmentKey());
	}

	private static Cipher getDecryptionCipher(byte[] iv, byte[] salt, String password) throws Exception {
		return getDecryptionCipher(iv, createPBEKey(salt, password));
	}

	private static Cipher getDecryptionCipher(byte[] iv, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM_MODE);
		IvParameterSpec spec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		return cipher;
	}

	private static SecretKey getDepartmentKey() throws Exception {
		return CoreUtil.getUserContext().getDepartmentKey();
	}

	public static SecretKey getDepartmentKey(byte[] departmentKey) throws Exception {
		return new SecretKeySpec(departmentKey, SYMMETRIC_ALGORITHM);
	}

	private static Cipher getEncryptionCipher() throws Exception {
		return getEncryptionCipher(getDepartmentKey());
	}

	private static Cipher getEncryptionCipher(byte[] iv) throws Exception {
		return getEncryptionCipher(iv, getDepartmentKey());
	}

	private static Cipher getEncryptionCipher(byte[] iv, byte[] salt, String password) throws Exception {
		return getEncryptionCipher(iv, createPBEKey(salt, password));
	}

	private static Cipher getEncryptionCipher(byte[] iv, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM_MODE);
		IvParameterSpec spec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		return cipher;
	}

	private static Cipher getEncryptionCipher(byte[] salt, String password) throws Exception {
		return getEncryptionCipher(createPBEKey(salt, password));
	}

	private static Cipher getEncryptionCipher(SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM_MODE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher;
	}

	private static byte[] getMD5Digest(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data, 0, data.length);
		return md5.digest();
	}

	private static byte[] getMD5DigestForSearch(byte[] plainText) throws NoSuchAlgorithmException {
		if (Settings.getBoolean(SettingCodes.HASH_FOR_SEARCH, Bundle.SETTINGS, DefaultSettings.HASH_FOR_SEARCH)) {
			return getMD5Digest(plainText);
		}
		return null;
	}

	private static PrivateKey getPrivateKey() throws Exception {
		return CoreUtil.getUserContext().getPrivateKey();
	}

	public static PrivateKey getPrivateKey(byte[] privateKey) throws Exception {
		return KeyFactory.getInstance(ASYMMETRIC_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKey));
	}

	private static PublicKey getPublicKey() throws Exception {
		return CoreUtil.getUserContext().getPublicKey();
	}

	public static PublicKey getPublicKey(byte[] publicKey) throws Exception {
		return KeyFactory.getInstance(ASYMMETRIC_ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
	}

	private static byte[] hashForSearch(byte[] plainText) throws Exception {
		return encryptHashForSearch(getMD5DigestForSearch(plainText));
	}

	public static byte[] hashForSearch(byte[] salt, String password, byte[] plainText) throws Exception {
		return encryptHashForSearch(salt, password, getMD5DigestForSearch(plainText));
	}

	public static byte[] hashForSearch(byte[] salt, String password, Serializable value) throws Exception {
		return hashForSearch(salt, password, CoreUtil.serialize(value));
	}

	public static byte[] hashForSearch(Serializable value) throws Exception {
		return hashForSearch(CoreUtil.serialize(value));
	}

	public static byte[] hashForSearch(SecretKey departmentKey, byte[] plainText) throws Exception {
		return encryptHashForSearch(departmentKey, getMD5DigestForSearch(plainText));
	}

	private static byte[] hashPassword(byte[] salt, String password) throws Exception {
		return createPBEKey(salt, password).getEncoded();
	}

	public static byte[] verifyMD5(byte[] data, String md5) throws NoSuchAlgorithmException, IOException {
		if (!CommonUtil.getHex(getMD5Digest(data)).equalsIgnoreCase(md5)) {
			throw new IOException(L10nUtil.getMessage(MessageCodes.MD5_CHECK_FAILED, DefaultMessages.MD5_CHECK_FAILED));
		}
		return data;
	}

	private CryptoUtil() {
	}
	// Using PKCS #5 padding is a handy way to detect errors in the ciphertext. It is also useful when decrypting with a key other than the one with which a message was encrypted.
	// In either case, the final block of recovered plaintext will most likely not be what was encrypted. It is unlikely that random decryption errors will result in a correctly
	// formatted PKCS #5 padding block.
	// https://developer-content.emc.com/docs/rsashare/share_for_java/1.1/dev_guide/group__LEARNJCE__ENCDEC__SYM__PAD.html
}
