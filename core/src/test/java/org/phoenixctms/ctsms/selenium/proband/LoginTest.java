package org.phoenixctms.ctsms.selenium.proband;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.selenium.SeleniumTestBase;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(LoginTest.class)
public class LoginTest extends SeleniumTestBase {

	private String departmentPassword;
	private DepartmentVO department;
	private String userName;
	private String userPassword;

	@BeforeClass
	public void init_00_setup_department() throws Exception {
		String departmentName = "dept-" + getTestId();
		departmentPassword = departmentName;
		department = getTestDataProvider().createDepartment(departmentName, true, departmentPassword);
	}

	@BeforeClass
	public void init_01_setup_user() throws Exception {
		userName = "user-" + getTestId();
		userPassword = userName;
		UserOutVO user = createUser(userName, userPassword, department.getId(), departmentPassword);
		setProviderAuth(userName, userPassword);
	}

	@Test
	public void test_01_open_proband_page() {
		load(getUrl("/proband/proband.jsf"));
	}

	@Test
	public void test_02_login() {
		login(userName, userPassword);
	}

	private UserOutVO createUser(String name, String password, long departmentId, String departmentPassword) throws Exception {
		UserInVO newUser = new UserInVO();
		newUser.setLocale(CommonUtil.localeToString(Locale.getDefault()));
		newUser.setTimeZone(CommonUtil.timeZoneToString(TimeZone.getDefault()));
		newUser.setDateFormat(null);
		newUser.setDecimalSeparator(null);
		newUser.setLocked(false);
		newUser.setLockedUntrusted(false);
		newUser.setShowTooltips(false);
		newUser.setDecrypt(true);
		newUser.setDecryptUntrusted(false);
		newUser.setEnableInventoryModule(true);
		newUser.setVisibleInventoryTabList(null);
		newUser.setEnableStaffModule(true);
		newUser.setVisibleStaffTabList(null);
		newUser.setEnableCourseModule(true);
		newUser.setVisibleCourseTabList(null);
		newUser.setEnableTrialModule(true);
		newUser.setVisibleTrialTabList(null);
		newUser.setEnableInputFieldModule(true);
		newUser.setVisibleInputFieldTabList(null);
		newUser.setEnableProbandModule(true);
		newUser.setVisibleProbandTabList("inquiryvalues");
		newUser.setEnableMassMailModule(true);
		newUser.setVisibleMassMailTabList(null);
		newUser.setEnableUserModule(true);
		newUser.setVisibleUserTabList(null);
		newUser.setAuthMethod(AuthenticationType.LOCAL);
		newUser.setParentId(null);
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword);
		newUser.setDepartmentId(departmentId);
		newUser.setName(name);
		newPassword.setPassword(password);
		return getTestDataProvider().createUser(newUser, newPassword, departmentPassword, new ArrayList<PermissionProfile>() {

			{
				add(PermissionProfile.INVENTORY_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.STAFF_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.COURSE_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.TRIAL_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.PROBAND_MASTER_ALL_DEPARTMENTS);
				add(PermissionProfile.USER_ALL_DEPARTMENTS);
				add(PermissionProfile.INPUT_FIELD_MASTER);
				add(PermissionProfile.MASS_MAIL_DETAIL_ALL_DEPARTMENTS);
				add(PermissionProfile.INVENTORY_MASTER_SEARCH);
				add(PermissionProfile.STAFF_MASTER_SEARCH);
				add(PermissionProfile.COURSE_MASTER_SEARCH);
				add(PermissionProfile.TRIAL_MASTER_SEARCH);
				add(PermissionProfile.PROBAND_MASTER_SEARCH);
				add(PermissionProfile.USER_MASTER_SEARCH);
				add(PermissionProfile.INPUT_FIELD_MASTER_SEARCH);
				add(PermissionProfile.MASS_MAIL_MASTER_SEARCH);
			}
		});
	}
}
