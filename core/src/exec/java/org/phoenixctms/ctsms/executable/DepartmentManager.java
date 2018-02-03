package org.phoenixctms.ctsms.executable;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.PermissionDao;
import org.phoenixctms.ctsms.domain.ProfilePermissionDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfile;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.security.Authenticator;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.PasswordPolicy;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.PermissionProfileGrouping;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentManager {

	private static final String DEPARTMENT_NAME = "ID {0}: {1} - {2}";
	private static final String PROFILE_NAME = "{0}: {1} - {2}";
	private static final boolean DEFAULT_DEPARTMENT_VISIBLE = true;
	private static final boolean USER_ONE_TIME_LOGON = false;
	private static final Pattern PROFILE_SEPARATOR_REGEXP = Pattern.compile(" *, *");
	private static String getDefaultLocale() {
		return CommonUtil.localeToString(Locale.getDefault());
	}
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ToolsService toolsService;
	@Autowired
	private UserPermissionProfileDao userPermissionProfileDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private ProfilePermissionDao profilePermissionDao;
	private Authenticator authenticator;

	private JobOutput jobOutput;

	public DepartmentManager() {
	}

	private ArrayList<UserPermissionProfile> addUserPermissionProfiles(UserOutVO userVO, ArrayList<PermissionProfile> profiles) throws Exception {
		User user = userDao.load(userVO.getId());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ArrayList<UserPermissionProfile> result = new ArrayList<UserPermissionProfile>(profiles.size());
		Iterator<PermissionProfile> profilesIt = profiles.iterator();
		while (profilesIt.hasNext()) {
			PermissionProfile profile = profilesIt.next();
			UserPermissionProfile userPermissionProfile = UserPermissionProfile.Factory.newInstance();
			userPermissionProfile.setActive(true);
			userPermissionProfile.setProfile(profile);
			userPermissionProfile.setUser(user);
			CoreUtil.modifyVersion(userPermissionProfile, now, null);
			result.add(userPermissionProfileDao.create(userPermissionProfile));
			jobOutput.println("permission profile " + profile.toString() + " added");
		}
		return result;
	}

	public void changeDepartmentPassword(AuthenticationVO auth, String plainNewDepartmentPassword) throws Exception { // for password mutation job?
		jobOutput.println("user: " + auth.getUsername());
		authenticator.authenticate(auth, false);
		User user = CoreUtil.getUser();
		Department department = user.getDepartment();
		jobOutput.println("department l10n key: " + department.getNameL10nKey());
		toolsService.changeDepartmentPassword(department.getId(), plainNewDepartmentPassword, CoreUtil.getUserContext().getPlainDepartmentPassword());
		CoreUtil.getUserContext().reset();
		jobOutput.println("department password change successful");
	}

	public void changeDepartmentPassword(String l10nKey, String plainNewDepartmentPassword, String plainOldDepartmentPassword) throws Exception {
		jobOutput.println("department l10n key: " + l10nKey);
		toolsService.changeDepartmentPassword(ExecUtil.departmentL10nKeyToId(l10nKey, departmentDao, jobOutput), plainNewDepartmentPassword, plainOldDepartmentPassword);
		jobOutput.println("department password change successful");
	}

	private ArrayList<PermissionProfile> checkProfileList(String profileList) throws Exception {
		ArrayList<PermissionProfile> result = new ArrayList<PermissionProfile>();
		if (!CommonUtil.isEmptyString(profileList)) {
			String[] profiles = PROFILE_SEPARATOR_REGEXP.split(profileList, -1);
			HashSet<PermissionProfileGroup> groups = new HashSet<PermissionProfileGroup>();
			for (int i = 0; i < profiles.length; i++) {
				PermissionProfile profile = PermissionProfile.fromString(profiles[i]);
				PermissionProfileGroup group = PermissionProfileGrouping.getGroupFromPermissionProfile(profile);
				if (groups.add(group)) {
					result.add(profile);
				} else {
					throw new IllegalArgumentException("exactly one profile of group " + L10nUtil.getPermissionProfileGroupName(Locales.USER, group.name())
					+ " required - remove profile " + profile.toString() + " from list");
				}
			}
			PermissionProfileGroup[] profileGroups = PermissionProfileGroup.values();
			for (int i = 0; i < profileGroups.length; i++) {
				if (!groups.contains(profileGroups[i])) {
					throw new IllegalArgumentException("exactly one profile of group " + L10nUtil.getPermissionProfileGroupName(Locales.USER, profileGroups[i].name())
					+ " required");
				}
			}
		}
		return result;
	}

	private Department createDepartment(String nameL10nKey, boolean visible, String plainDepartmentPassword) throws Exception {
		Department department = Department.Factory.newInstance();
		department.setNameL10nKey(nameL10nKey);
		department.setVisible(visible);
		CryptoUtil.encryptDepartmentKey(department, CryptoUtil.createRandomKey().getEncoded(), plainDepartmentPassword);
		department = departmentDao.create(department);
		return department;
	}

	public void createDepartment(String nameL10nKey, String plainDepartmentPassword) throws Exception {
		jobOutput.println("l10n key: " + nameL10nKey);
		createDepartment(nameL10nKey, true, plainDepartmentPassword);
		jobOutput.println("department created");
	}



	public void createUser(String departmentL10nKey, String plainDepartmentPassword, String username, String password, String locale, String profileList) throws Exception {
		jobOutput.println("department l10n key: " + departmentL10nKey);
		jobOutput.println("username: " + username);
		if (CommonUtil.isEmptyString(locale)) {
			locale = getDefaultLocale();
		}
		jobOutput.println("langauge: " + locale);
		jobOutput.println("permission profiles: " + profileList);
		UserInVO newUser = getNewUser(ExecUtil.departmentL10nKeyToId(departmentL10nKey, departmentDao, jobOutput), username, locale);
		PasswordInVO newPassword = getNewPassword(password);
		ArrayList<PermissionProfile> profiles = checkProfileList(profileList);
		UserOutVO user = toolsService.addUser(newUser, newPassword, plainDepartmentPassword);
		jobOutput.println("user created");
		addUserPermissionProfiles(user, profiles);
	}

	private PasswordInVO getNewPassword(String password) throws Exception {
		PasswordInVO newPassword = new PasswordInVO();
		ServiceUtil.applyLogonLimitations(newPassword);
		if (USER_ONE_TIME_LOGON) {
			ServiceUtil.applyOneTimeLogonLimitation(newPassword);
		}
		newPassword.setPassword(password);
		return newPassword;
	}

	private UserInVO getNewUser(Long departmentId, String name, String locale) throws Exception {
		UserInVO newUser = new UserInVO();
		newUser.setLocale(locale); // CommonUtil.localeToString(Locale.getDefault()));
		newUser.setTimeZone(CommonUtil.timeZoneToString(TimeZone.getDefault()));
		newUser.setLocked(false);
		newUser.setShowTooltips(true);
		newUser.setDecrypt(true);
		newUser.setAuthMethod(AuthenticationType.LOCAL);
		newUser.setDepartmentId(departmentId);
		newUser.setName(name);
		return newUser;
	}

	public void interactiveChangeDepartmentPassword() {
		Scanner in = ExecUtil.getScanner();
		try {
			printDepartments();
			System.out.print("enter department id for department password change:");
			long departmentId = in.nextLong();
			in.nextLine();
			String plainOldDepartmentPassword = ExecUtil.readPassword(in, "enter current department password:");
			printDepartmentPasswordPolicy();
			String plainNewDepartmentPassword = readConfirmedPassword(in, "enter new department password:", "confirm new department password:");
			if (ExecUtil.confirmationPrompt(in, "ready.")) {
				toolsService.changeDepartmentPassword(departmentId, plainNewDepartmentPassword, plainOldDepartmentPassword);
				System.out.println("department password change successful");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}

	public void interactiveCreateDepartment() {
		Scanner in = ExecUtil.getScanner();
		try {
			printDepartments();
			System.out.print("enter new department name l10n key:");
			String nameL10nKey = in.nextLine();
			printDepartmentPasswordPolicy();
			String plainDepartmentPassword = readConfirmedPassword(in, "enter department password:", "confirm department password:");
			PasswordPolicy.DEPARTMENT.checkStrength(plainDepartmentPassword);
			createDepartment(nameL10nKey, DEFAULT_DEPARTMENT_VISIBLE, plainDepartmentPassword);
			System.out.println("department created");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}

	public void interactiveCreateUser() {
		Scanner in = ExecUtil.getScanner();
		try {
			printDepartments();
			System.out.print("enter department id for new user:");
			Long departmentId = in.nextLong();
			in.nextLine();
			String plainDepartmentPassword = ExecUtil.readPassword(in, "enter department password:");
			System.out.print("enter new username:");
			String username = in.nextLine();
			System.out.print("enter user language [" + getDefaultLocale() + "]:");
			UserInVO newUser = getNewUser(departmentId, username, in.nextLine());
			printPasswordPolicy();
			PasswordInVO newPassword = getNewPassword(ExecUtil.readPassword(in, "enter desired user password:"));
			printPermissionProfiles();
			System.out.print("enter separated list of permission profiles:");
			ArrayList<PermissionProfile> profiles = checkProfileList(in.nextLine());
			UserOutVO user = toolsService.addUser(newUser, newPassword, plainDepartmentPassword);
			System.out.println("user created");
			addUserPermissionProfiles(user, profiles);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}

	private void printDepartmentPasswordPolicy() throws NoSuchAlgorithmException {
		System.out.println("department password policy requirements:");
		System.out.println(PasswordPolicy.DEPARTMENT.getRequirements());
		System.out.println("a random passphrase that meets department password policy requirements:");
		System.out.println(PasswordPolicy.DEPARTMENT.getDummyPassword());
	}

	private void printDepartments() {
		System.out.println("departments:");
		Iterator<Department> it = departmentDao.loadAllSorted(0, 0).iterator();
		while (it.hasNext()) {
			DepartmentVO department = departmentDao.toDepartmentVO(it.next());
			System.out.println(MessageFormat.format(DEPARTMENT_NAME, Long.toString(department.getId()), department.getNameL10nKey(), department.getName()));
		}
	}

	private void printPasswordPolicy() throws NoSuchAlgorithmException {
		System.out.println("password policy requirements:");
		System.out.println(PasswordPolicy.USER.getRequirements());
		System.out.println("a random password that meets password policy requirements:");
		System.out.println(PasswordPolicy.USER.getDummyPassword());
	}

	private void printPermissionProfiles() throws Exception {
		System.out.println("permission profiles:");
		Iterator<PermissionProfileVO> profileIt = ServiceUtil.getPermissionProfiles(null, Locales.USER).iterator();
		while (profileIt.hasNext()) {
			PermissionProfileVO profileVO = profileIt.next();
			System.out
			.println(MessageFormat.format(PROFILE_NAME, profileVO.getProfile().toString(), profileVO.getProfileGroup().getProfileGroupName(), profileVO.getProfileName()));
		}
	}

	private String readConfirmedPassword(Scanner in, String prompt, String confirmationPrompt) throws IOException {
		String plainNewDepartmentPassword = ExecUtil.readPassword(in, prompt);
		String passwordConfirmation = ExecUtil.readPassword(in, confirmationPrompt);
		if (!plainNewDepartmentPassword.equals(passwordConfirmation)) {
			throw new IllegalArgumentException("password and confirmation differ");
		}
		return plainNewDepartmentPassword;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
