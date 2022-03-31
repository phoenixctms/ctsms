package org.phoenixctms.ctsms.intercept;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.domain.ErrorDao;
import org.phoenixctms.ctsms.enumeration.ExceptionType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.service.proband.ProbandService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.springframework.aop.ThrowsAdvice;

public class ErrorLogger implements ThrowsAdvice {

	private final static int MAX_STACK_TRACE_LENGTH = 1024;

	private static ArrayList<String> obfuscatePasswords(Method method, Object[] args) {
		ArrayList<String> passwords;
		if (method != null && args != null && args.length > 0) {
			passwords = new ArrayList<String>(args.length);
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof AuthenticationVO) {
					AuthenticationVO auth = (AuthenticationVO) args[i];
					passwords.add(auth.getPassword());
					auth.setPassword(CoreUtil.OBFUSCATED_STRING);
					passwords.add(auth.getLocalPassword());
					auth.setLocalPassword(CoreUtil.OBFUSCATED_STRING);
					//passwords.add(auth.getOtp());
					//auth.setOtp(CoreUtil.OBFUSCATED_STRING);
				} else if (args[i] instanceof PasswordInVO) {
					PasswordInVO passwordInVO = (PasswordInVO) args[i];
					passwords.add(passwordInVO.getPassword());
					passwordInVO.setPassword(CoreUtil.OBFUSCATED_STRING);
				}
			}
			String methodName = method.getName();
			String serviceName = method.getDeclaringClass().getName();
			if (ToolsService.class.getName().equals(serviceName) && "changeDepartmentPassword".equals(methodName)) {
				passwords.add((String) args[1]);
				args[1] = CoreUtil.OBFUSCATED_STRING;
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
			} else if (ProbandService.class.getName().equals(serviceName) && "updateProbandDepartmentPassword".equals(methodName)) {
				passwords.add((String) args[3]);
				args[3] = CoreUtil.OBFUSCATED_STRING;
				passwords.add((String) args[4]);
				args[4] = CoreUtil.OBFUSCATED_STRING;
			} else if (ToolsService.class.getName().equals(serviceName) && "addUser".equals(methodName)) {
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "addUser".equals(methodName)) {
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "updateUser".equals(methodName)) {
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
				passwords.add((String) args[3]);
				args[3] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "setPassword".equals(methodName)) {
				passwords.add((String) args[1]);
				args[1] = CoreUtil.OBFUSCATED_STRING;
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "adminSetPassword".equals(methodName)) {
				passwords.add((String) args[3]);
				args[3] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "getOTPRegistrationInfo".equals(methodName)) {
				passwords.add((String) args[2]);
				args[2] = CoreUtil.OBFUSCATED_STRING;
			} else if (UserService.class.getName().equals(serviceName) && "verifyOTP".equals(methodName)) {
				passwords.add((String) args[3]);
				args[3] = CoreUtil.OBFUSCATED_STRING;
			}
		} else {
			passwords = new ArrayList<String>();
		}
		return passwords;
	}

	private static void restorePasswords(ArrayList<String> passwords, Method method, Object[] args) {
		if (passwords != null && passwords.size() > 0 && method != null && args != null && args.length > 0) {
			int passwordIndex = 0;
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof AuthenticationVO) {
					AuthenticationVO auth = (AuthenticationVO) args[i];
					auth.setPassword(passwords.get(passwordIndex));
					passwordIndex++;
					auth.setLocalPassword(passwords.get(passwordIndex));
					passwordIndex++;
					//auth.setOtp(passwords.get(passwordIndex));
					//passwordIndex++;
				} else if (args[i] instanceof PasswordInVO) {
					PasswordInVO passwordInVO = (PasswordInVO) args[i];
					passwordInVO.setPassword(passwords.get(passwordIndex));
					passwordIndex++;
				}
			}
			String methodName = method.getName();
			String serviceName = method.getDeclaringClass().getName();
			if (ToolsService.class.getName().equals(serviceName) && "changeDepartmentPassword".equals(methodName)) {
				args[1] = passwords.get(passwordIndex);
				passwordIndex++;
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (ProbandService.class.getName().equals(serviceName) && "updateProbandDepartmentPassword".equals(methodName)) {
				args[3] = passwords.get(passwordIndex);
				passwordIndex++;
				args[4] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (ToolsService.class.getName().equals(serviceName) && "addUser".equals(methodName)) {
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "addUser".equals(methodName)) {
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "updateUser".equals(methodName)) {
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
				args[3] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "setPassword".equals(methodName)) {
				args[1] = passwords.get(passwordIndex);
				passwordIndex++;
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "adminSetPassword".equals(methodName)) {
				args[3] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "getOTPRegistrationInfo".equals(methodName)) {
				args[2] = passwords.get(passwordIndex);
				passwordIndex++;
			} else if (UserService.class.getName().equals(serviceName) && "verifyOTP".equals(methodName)) {
				args[3] = passwords.get(passwordIndex);
				passwordIndex++;
			}
		}
	}

	ErrorDao errorDao;

	public ErrorLogger() {
	}

	public void afterThrowing(Method method, Object[] args, Object target, Throwable ex) {
		if (ex instanceof ServiceException) {
			if (Settings.getBoolean(SettingCodes.ENABLE_SERVICE_EXCEPTION_LOGGER, Bundle.SETTINGS, DefaultSettings.ENABLE_SERVICE_EXCEPTION_LOGGER)
					&& ((ServiceException) ex).isLogError()) {
				logError(method, args, ex, ExceptionType.SERVICE);
			}
		} else if (ex instanceof AuthenticationException) {
			if (Settings.getBoolean(SettingCodes.ENABLE_AUTHENTICATION_EXCEPTION_LOGGER, Bundle.SETTINGS, DefaultSettings.ENABLE_AUTHENTICATION_EXCEPTION_LOGGER)) {
				logError(method, args, ex, ExceptionType.AUTHENTICATION);
			}
		} else if (ex instanceof AuthorisationException) {
			if (Settings.getBoolean(SettingCodes.ENABLE_AUTHORISATION_EXCEPTION_LOGGER, Bundle.SETTINGS, DefaultSettings.ENABLE_AUTHORISATION_EXCEPTION_LOGGER)) {
				logError(method, args, ex, ExceptionType.AUTHORISATION);
			}
		} else {
			if (Settings.getBoolean(SettingCodes.ENABLE_OTHER_EXCEPTION_LOGGER, Bundle.SETTINGS, DefaultSettings.ENABLE_OTHER_EXCEPTION_LOGGER)) {
				logError(method, args, ex, ExceptionType.OTHER);
			}
		}
		CoreUtil.clearUserContext();
	}

	private void logError(Method method, Object[] args, Throwable ex, ExceptionType type) {
		org.phoenixctms.ctsms.domain.Error error = org.phoenixctms.ctsms.domain.Error.Factory
				.newInstance();
		error.setType(type);
		try {
			error.setMethod(method.toString());
		} catch (Exception e) {
		}
		if (args != null) {
			ArrayList<String> passwords = obfuscatePasswords(method, args);
			try {
				error.setArguments(CoreUtil.toXML(args, true));
			} catch (Exception e) {
			} finally {
				restorePasswords(passwords, method, args);
			}
		}
		if (ex != null) {
			try {
				error.setMessage(ex.getMessage());
			} catch (Exception e) {
			}
			try {
				error.setStackTrace(CoreUtil.getStackTrace(ex).substring(0, MAX_STACK_TRACE_LENGTH - 1));
			} catch (Exception e) {
			}
		}
		error.setTimestamp(new Timestamp(System.currentTimeMillis()));
		UserContext context = CoreUtil.getUserContext();
		error.setUsername(context.getName());
		if (!ExceptionType.AUTHENTICATION.equals(type) || !CommonUtil.isEmptyString(context.getName())) { // filter http auth
			try {
				errorDao.createTxn(error);
			} catch (Exception e) {
			}
		}
	}

	public void setErrorDao(ErrorDao errorDao) {
		this.errorDao = errorDao;
	}
}