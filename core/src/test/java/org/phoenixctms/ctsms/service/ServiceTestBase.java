package org.phoenixctms.ctsms.service;

import javax.annotation.Resource;

import org.springframework.aop.framework.Advised;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.IHookCallBack;

import org.phoenixctms.ctsms.PrincipalStore;
import org.phoenixctms.ctsms.TestDataProvider;
import org.phoenixctms.ctsms.UserContext;

@ContextConfiguration(locations = { "/applicationContextTest.xml" })
public abstract class ServiceTestBase extends AbstractTransactionalTestNGSpringContextTests {

	@Resource
	protected TestDataProvider testDataProvider;

	public ServiceTestBase() {
		super();
	}

	protected Object getServiceImpl(Object serviceProxy) {
		try {
			Advised proxy = (Advised) serviceProxy;
			return proxy.getTargetSource().getTarget();
		} catch (Exception e) {
			throw new Error("Could not get implementation class from service proxy!", e);
		}
	}

	public TestDataProvider getTestDataProvider() {
		return testDataProvider;
	}

	public void run(IHookCallBack hookCallBack) {
	}

	public void setTestDataProvider(TestDataProvider testDataProvider) {
		this.testDataProvider = testDataProvider;
	}

	protected void setUserContext(final UserContext userContext) {
		PrincipalStore.set(userContext);
	}
}
