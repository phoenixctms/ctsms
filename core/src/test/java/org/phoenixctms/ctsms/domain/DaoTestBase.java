package org.phoenixctms.ctsms.domain;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.IHookCallBack;

import org.phoenixctms.ctsms.TestDataProvider;

@ContextConfiguration(locations = { "/applicationContextTest.xml" })
public abstract class DaoTestBase extends AbstractTransactionalTestNGSpringContextTests {

	@Resource
	protected TestDataProvider testDataProvider;

	public DaoTestBase() {
		super();
	}

	public TestDataProvider getTestDataProvider() {
		return testDataProvider;
	}

	public void run(IHookCallBack hookCallBack) {
	}

	public void setTestDataProvider(TestDataProvider testDataProvider) {
		this.testDataProvider = testDataProvider;
	}
}
