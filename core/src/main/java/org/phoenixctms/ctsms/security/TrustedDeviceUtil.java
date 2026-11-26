package org.phoenixctms.ctsms.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;

import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.domain.TrustedDevice;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

/**
 * Remembered devices: after a 2FA logon from outside the trusted ip ranges, the browser gets a random token (a cookie).
 * Within trusted_device_days, a logon with this token needs no 2FA and the session may decrypt like from a trusted host.
 * Only the SHA-256 hash of the token is stored (TrustedDevice.deviceToken), so a database dump cannot be replayed.
 */
public final class TrustedDeviceUtil {

	private static final int TOKEN_BYTES = 32;
	private static final int COLUMN_LENGTH = 1024;
	private static final long MILLIS_PER_DAY = 24l * 60l * 60l * 1000l;
	private static final SecureRandom RANDOM = new SecureRandom();

	public static long getDays() {
		return Settings.getLong(SettingCodes.TRUSTED_DEVICE_DAYS, Bundle.SETTINGS, DefaultSettings.TRUSTED_DEVICE_DAYS);
	}

	public static int getMax() {
		return Math.max(1, Settings.getInt(SettingCodes.TRUSTED_DEVICES_MAX, Bundle.SETTINGS, DefaultSettings.TRUSTED_DEVICES_MAX));
	}

	public static boolean isEnabled() {
		return getDays() > 0l;
	}

	public static String createToken() {
		byte[] token = new byte[TOKEN_BYTES];
		RANDOM.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
	}

	public static String hashToken(String token) {
		try {
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Timestamp getExpiration(TrustedDevice device) {
		return new Timestamp(device.getCreated().getTime() + getDays() * MILLIS_PER_DAY);
	}

	public static boolean isExpired(TrustedDevice device, Date now) {
		return !now.before(getExpiration(device));
	}

	/**
	 * A device counts only for the password it was remembered with (a new password forgets all devices) and until it expires.
	 */
	public static boolean isValid(TrustedDevice device, Password lastPassword, Date now) {
		return device != null && lastPassword != null && lastPassword.equals(device.getPassword()) && !isExpired(device, now);
	}

	public static boolean hasToken(String token) {
		return !CommonUtil.isEmptyString(token);
	}

	/**
	 * Set while a drop removes the browser the call authenticated with. The web tier reads and clears it so that
	 * browser's cookie is expired. The token itself is not put on the value object.
	 */
	private static final ThreadLocal<Boolean> REMOVED_CURRENT = new ThreadLocal<Boolean>();

	public static void resetRemovedCurrent() {
		REMOVED_CURRENT.set(Boolean.FALSE);
	}

	public static void noteIfCurrent(TrustedDevice device, String token) {
		if (hasToken(token) && device != null && hashToken(token).equals(device.getDeviceToken())) {
			REMOVED_CURRENT.set(Boolean.TRUE);
		}
	}

	public static boolean consumeRemovedCurrent() {
		Boolean removed = REMOVED_CURRENT.get();
		REMOVED_CURRENT.remove();
		return Boolean.TRUE.equals(removed);
	}

	/**
	 * Host and user agent as stored (CHARACTER VARYING(1024)), null stays null.
	 */
	public static String clipColumnValue(String value) {
		if (value != null && value.length() > COLUMN_LENGTH) {
			return value.substring(0, COLUMN_LENGTH);
		}
		return value;
	}

	private TrustedDeviceUtil() {
	}
}
