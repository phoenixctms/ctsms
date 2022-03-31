package org.phoenixctms.ctsms.util;

public interface AuthenticationExceptionCodes {

	public final static String AUTHENTICATION_REQUIRED = "authentication_required";
	public final static String LOCAL_PASSWORD_REQUIRED = "local_password_required";
	public final static String NO_LOCAL_PASSWORD_SET = "no_local_password_set";
	public final static String NO_PASSWORD_SET = "no_password_set";
	public final static String PASSWORD_EXPIRED = "password_expired";
	public final static String SUCCESSFUL_LOGON_LIMIT_EXCEEDED = "successful_logon_limit_exceeded";
	public final static String UNKNOWN_USER = "unknown_user";
	public final static String USER_MARKED_FOR_DELETION = "user_marked_for_deletion";
	public final static String USER_LOCKED = "user_locked";
	public final static String WRONG_LOCAL_PASSWORD = "wrong_local_password";
	public static final String WRONG_PASSWORD = "wrong_password";
	public final static String WRONG_PASSWORD_ATTEMPT_LIMIT_EXCEEDED = "wrong_password_attempt_limit_exceeded";
	public final static String WRONG_REMOTE_PASSWORD = "wrong_remote_password";
	public final static String INVALID_JWT = "invalid_jwt";
	public final static String INVALID_OTP = "invalid_otp";
}
