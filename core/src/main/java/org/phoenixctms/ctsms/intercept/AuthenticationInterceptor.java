package org.phoenixctms.ctsms.intercept;

import java.lang.reflect.Method;

import org.phoenixctms.ctsms.security.Authenticator;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class AuthenticationInterceptor implements MethodBeforeAdvice, AfterReturningAdvice {

	private static AuthenticationVO getAuthentication(Object[] args) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof AuthenticationVO) {
					return (AuthenticationVO) args[i];
				}
			}
		}
		return null;
	}

	private Authenticator authenticator;

	public AuthenticationInterceptor() {
	}

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
			throws Throwable {
		CoreUtil.clearUserContext();
	}

	@Override
	public void before(Method method, Object[] args, Object object) throws Throwable {
		AuthenticationVO auth = getAuthentication(args);
		String realm = CoreUtil.getServiceMethodName(method);
		if (auth != null && !CommonUtil.isEmptyString(auth.getRealm())) {
			realm = auth.getRealm();
		}
		authenticator.authenticate(auth, false, realm);
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}
}
