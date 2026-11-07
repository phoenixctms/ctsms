package org.phoenixctms.ctsms.selenium.trial;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.fileprocessors.ProcessorJobOutput;
import org.phoenixctms.ctsms.selenium.SeleniumTestBase;
import org.phoenixctms.ctsms.test.EcrfValidationTestVector;
import org.phoenixctms.ctsms.test.xls.XlsImporter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(EcrfDataEntryTest.class)
public class EcrfDataEntryTest extends SeleniumTestBase implements ProcessorJobOutput {

	private final static String ECRF_FILE = "ecrfs.xls";
	private final static String ECRF_FILE_ENCODING = null;
	private String departmentPassword;
	private DepartmentVO department;
	private String userName;
	private String userPassword;
	private TrialOutVO trial;
	private Long probandId;
	private XlsImporter xlsImporter;
	private final static Pattern FILE_NAME_ID_REGEXP = Pattern.compile("'([0-9a-z._-]+)' \\(file ID (\\d+)\\)");
	private final static long FILE_DOWNLOAD_TIMEOUT_MS = 60_000L;
	private final static String EXPORT_FILES_FILE_NAME_PREFIX = "ecrf_files_";
	// exported FILE values are "<zip entry path> (<md5>)", the entry path being "<proband id>/<column name>/<file name>":
	private final static Pattern EXPORTED_FILE_VALUE_REGEXP = Pattern.compile("^(.+) \\(([0-9a-fA-F]{32})\\)$");
	private String exportFile;
	private String exportFilesFile;
	private HashMap<String, String> exportedFileMd5s = new LinkedHashMap<String, String>();
	private Connection connection;

	private XlsImporter getXlsImporter() {
		if (xlsImporter == null) {
			xlsImporter = getApplicationContext().getBean(XlsImporter.class);
			xlsImporter.setJobOutput(this);
		}
		return xlsImporter;
	}

	@BeforeClass(description = "Create a department for test user, subjects and trial.")
	public void init_00_setup_department() throws Exception {
		String departmentName = "dept-" + getTestId();
		departmentPassword = departmentName;
		department = getTestDataProvider().createDepartment(departmentName, true, departmentPassword);
	}

	@BeforeClass(description = "Create a test user.")
	public void init_01_setup_user() throws Exception {
		userName = "user-" + getTestId();
		userPassword = userName;
		UserOutVO user = createUser(userName, userPassword, department.getId(), departmentPassword);
		setProviderAuth(userName, userPassword);
		getXlsImporter().setAuth(createAuth(userName, userPassword));
	}

	@BeforeClass(description = "Create a test trial.")
	public void init_02_setup_trial() throws Exception {
		String trialName = "trial-" + getTestId();
		trial = createTrial(trialName, department.getId());
	}

	@Test(description = "Try to open the trial page of the Phoenix CTMS test instance. It will redirect to the login page at first.")
	public void test_01_open_trial_page() {
		load(getUrl("/trial/trial.jsf?trialid=" + trial.getId()));
	}

	@Test(description = "Log in with the user created for this test.")
	public void test_02_login() {
		login(userName, userPassword);
	}

	@Test(description = "Execute trial job for importing a predefined eCRF setup (ecrf.xls).")
	public void test_03_import_ecrf_setup_job() throws Throwable {
		clickTab("tabView:trialjobs");
		String job = "import eCRF setup";
		clickJobTypeSelectOneRadio("tabView:trialjob_form", job);
		uploadFile("tabView:trialjob_form:jobFileUpload", getResourceFilePath(ECRF_FILE));
		waitForUploadSuccessful("tabView:trialjob_form");
		clickButton("tabView:trialjob_form:addJob");
		if (waitForAddOperationSuccessful("tabView:trialjob_form")) {
			if (waitForJobSuccessful("tabView:trialjob_form", 60l, 5l)) {
				info("loading eCRF validation test data from " + getResourceFilePath(ECRF_FILE));
				getXlsImporter().loadEcrfValidationVectors(getResourceFilePath(ECRF_FILE), ECRF_FILE_ENCODING, trial.getId());
				return;
			}
		}
		testFailed(job + " failed");
	}

	@Test(description = "Enroll a new blinded subject (with auto-generated alias) in the trial's subject list.")
	public void test_04_enroll_subject() throws IOException {
		clickTab("tabView:probandlistentries");
		clickButton("tabView:probandlist_form:probandListTabView:addProbandListEntryCreateProband");
		if (waitForAddOperationSuccessful("tabView:probandlist_form")) {
			String subjectId = getTabTitle("tabView:probandlist_form:probandListTabView:probandListEntryMain").replaceFirst("^Proband:\\s*", "");
			probandId = getTestDataProvider().getProband(subjectId).getId();
			testOK("proband ID " + probandId.toString() + " enrolled: " + subjectId);
			return;
		}
		testFailed("enrolling subject failed");
	}

	@Test(description = "Enter eCRF data provided by the 'validation' spreadsheet of the eCRF setup (ecrf.xls).")
	public void test_05_enter_ecrf_values() throws IOException {
		clickTab("tabView:trialecrfstatusentries");
		filterDatatable("tabView:trialecrfstatusentry_form:trialecrfstatus_proband_list", "Proband ID", probandId.toString());
		if (getCountFromDatatableHead("tabView:trialecrfstatusentry_form:trialecrfstatus_proband_list") == 1l) {
			clickDatatableRow("tabView:trialecrfstatusentry_form:trialecrfstatus_proband_list", 0);
			Long ecrfCount = getCountFromDatatableHead("tabView:trialecrfstatusentry_form:trialecrfstatus_ecrf_list");
			for (int i = 0; i < ecrfCount; i++) {
				clickDatatableRow("tabView:trialecrfstatusentry_form:trialecrfstatus_ecrf_list", i);
				String ecrfName = getTdLabelValue("tabView:trialecrfstatusentry_form:ecrf_detail", "eCRF name");
				String ecrfRevision = getTdLabelValue("tabView:trialecrfstatusentry_form:ecrf_detail", "eCRF revision");
				clickSelectOneMenu("tabView:trialecrfstatusentry_form:section_filter", 0);
				waitForAjax();
				List<EcrfValidationTestVector> vectors = getXlsImporter().getEcrfValidationTestVectors(ecrfName, ecrfRevision);
				if (vectors != null && vectors.size() > 0) {
					Iterator<EcrfValidationTestVector> it = vectors.iterator();
					while (it.hasNext()) {
						enterValue(it.next());
					}
					clickButton("tabView:trialecrfstatusentry_form:update");
					if (!waitForUpdateOperationSuccessful("tabView:trialecrfstatusentry_form")) {
						testFailed("saving eCRF values failed");
						return;
					}
				}
			}
			testOK("eCRF values saved");
			return;
		} else {
			testFailed("cannot select proband ID " + probandId.toString() + " for eCRF data entry");
		}
	}

	@Test(description = "Execute trial job for exporting eCRF data as SQLite database.")
	public void test_06_export_ecrf_data_job() throws Throwable {
		setSkipScreenshot(true);
		clickTab("tabView:trialjobs");
		String job = "export eCRF data";
		clickJobTypeSelectOneRadio("tabView:trialjob_form", job);
		clickButton("tabView:trialjob_form:addJob");
		if (waitForAddOperationSuccessful("tabView:trialjob_form")) {
			if (waitForJobSuccessful("tabView:trialjob_form", 60l, 5l)) {
				Matcher matcher = FILE_NAME_ID_REGEXP.matcher(getJobOutput("tabView:trialjob_form"));
				while (matcher.find()) {
					String fileName = matcher.group(1);
					String fileId = matcher.group(2);
					if (!isExportedEcrfFile(fileName)) {
						info("skipping non-export file '" + fileName + "' (file ID " + fileId + ")");
						continue;
					}
					String url = getUrl("file?fileid=" + fileId);
					File file = new File(getTestDirectory(), fileName);
					getChromeDriver().get(url);
					waitForDownloadedFile(file);
					info(fileName + " saved");
					if (CommonUtil.getMimeType(file).equals("application/x-sqlite3")) {
						exportFile = file.getCanonicalPath();
					} else if (fileName.startsWith(EXPORT_FILES_FILE_NAME_PREFIX) && fileName.endsWith(".zip")) {
						exportFilesFile = file.getCanonicalPath();
					}
					info("file " + file.getCanonicalPath() + " saved");
				}
				if (CommonUtil.isEmptyString(exportFile)) {
					testFailed("SQLite export file not downloaded");
				}
				return;
			}
		}
		testFailed(job + " failed");
	}

	private static boolean isExportedEcrfFile(String fileName) {
		String lower = fileName.toLowerCase();
		return lower.endsWith(".db") || lower.endsWith(".csv") || lower.endsWith(".xlsx") || lower.endsWith(".xls") || lower.endsWith(".zip");
	}

	private void waitForDownloadedFile(File file) throws InterruptedException {
		long deadline = System.currentTimeMillis() + FILE_DOWNLOAD_TIMEOUT_MS;
		while (!file.exists()) {
			if (System.currentTimeMillis() >= deadline) {
				testFailed("timeout waiting for download of " + file.getName());
			}
			Thread.sleep(200);
		}
	}

	@Test(description = "Verify exported values found in the SQLite database with the expected values provided by the 'validation' spreadsheet of the eCRF setup (ecrf.xls).")
	public void test_07_verify_exported_ecrf_values() throws Throwable {
		setSkipScreenshot(true);
		List<EcrfValidationTestVector> vectors = getXlsImporter().getEcrfValidationTestVectors();
		if (vectors != null && vectors.size() > 0) {
			Iterator<EcrfValidationTestVector> it = vectors.iterator();
			getSqliteConnection().setAutoCommit(false);
			while (it.hasNext()) {
				checkExportedValue(it.next());
			}
			checkNoUnexpectedExportValues();
			getSqliteConnection().rollback();
		}
	}

	private Connection getSqliteConnection() throws SQLException {
		if (connection == null) {
			String url = "jdbc:sqlite:" + exportFile;
			connection = DriverManager.getConnection(url);
			info("SQLite database " + exportFile + " connected");
		}
		return connection;
	}

	protected void finalize() throws Throwable {
		if (connection != null) {
			try {
				connection.close();
				info("SQLite database " + exportFile + " disconnected");
			} catch (SQLException ex) {
				debug(ex.getMessage());
			}
		}
	}

	@Test(description = "Verify the files of the exported FILE values in the zip file uploaded by the eCRF data export, with the file names and MD5s found in the SQLite database.")
	public void test_08_verify_exported_ecrf_files() throws Throwable {
		setSkipScreenshot(true);
		if (exportedFileMd5s.size() > 0) {
			if (CommonUtil.isEmptyString(exportFilesFile)) {
				testFailed("eCRF files zip not downloaded");
				return;
			}
			HashMap<String, String> entryMd5s = new HashMap<String, String>();
			ZipInputStream zip = new ZipInputStream(new FileInputStream(exportFilesFile));
			try {
				ZipEntry entry;
				while ((entry = zip.getNextEntry()) != null) {
					if (!entry.isDirectory()) {
						entryMd5s.put(entry.getName(), getMd5(zip.readAllBytes()));
					}
				}
			} finally {
				zip.close();
			}
			Iterator<String> it = exportedFileMd5s.keySet().iterator();
			while (it.hasNext()) {
				String entryPath = it.next();
				Assert.assertTrue(entryMd5s.containsKey(entryPath), "zip entry " + entryPath + " exists:");
				Assert.assertEquals(entryMd5s.get(entryPath), exportedFileMd5s.get(entryPath), "zip entry " + entryPath + " MD5:");
				info("zip entry " + entryPath + " OK");
			}
			Assert.assertEquals(entryMd5s.size(), exportedFileMd5s.size(), "zip entry count:");
		} else {
			info("no exported files");
		}
	}

	private static String getMd5(byte[] data) throws Exception {
		return CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(data)).toLowerCase();
	}

	private static boolean isFile(EcrfValidationTestVector vector) {
		return InputFieldType.FILE.equals(vector.getEcrfField().getField().getFieldType().getType());
	}

	// the validation sheet holds the uploaded test resource file as input value, and its name as exported value:
	private void checkExportedFileValue(EcrfValidationTestVector vector, String value) throws Exception {
		String label = vector.toString();
		Matcher matcher = EXPORTED_FILE_VALUE_REGEXP.matcher(value != null ? value : "");
		Assert.assertTrue(matcher.find(), label + " exported value '" + value + "' format:");
		String entryPath = matcher.group(1);
		String md5 = matcher.group(2).toLowerCase();
		Assert.assertTrue(entryPath.startsWith(probandId.toString() + "/"), label + " exported file path '" + entryPath + "' proband ID:");
		Assert.assertEquals(entryPath.substring(entryPath.lastIndexOf('/') + 1), vector.getExportedValue(), label + " exported file name:");
		Assert.assertEquals(md5, getMd5(Files.readAllBytes(new File(getResourceFilePath(vector.getInputValue())).toPath())), label + " exported file MD5:");
		exportedFileMd5s.put(entryPath, md5);
	}

	private void checkExportedValue(EcrfValidationTestVector vector) throws Exception {
		String label = vector.toString();
		String selectSql = "select value from ecrf_data_vertical where ecrf_field_id = ?";
		String deleteSql = "delete from ecrf_data_vertical where ecrf_field_id = ?";
		if (vector.hasIndex()) {
			selectSql += " and (series_index + 0) = (? + 0)";
			deleteSql += " and (series_index + 0) = (? + 0)";
		}
		PreparedStatement selectPs = getSqliteConnection().prepareStatement(selectSql);
		PreparedStatement deletePs = getSqliteConnection().prepareStatement(deleteSql);
		selectPs.setString(1, Long.toString(vector.getEcrfField().getId()));
		deletePs.setString(1, Long.toString(vector.getEcrfField().getId()));
		if (vector.hasIndex()) {
			selectPs.setString(2, vector.getIndex());
			deletePs.setString(2, vector.getIndex());
		}
		ResultSet rs = selectPs.executeQuery();
		int count = 0;
		while (rs.next()) {
			if (isFile(vector)) {
				checkExportedFileValue(vector, rs.getString("value"));
			} else {
				Assert.assertEquals(rs.getString("value"), vector.getExportedValue(), label + " exported value:");
			}
			count++;
		}
		Assert.assertEquals(count, 1, label + " exported value count:");
		deletePs.execute();
		rs.close();
		selectPs.close();
		deletePs.close();
		info(label + " exported value OK: '" + vector.getExportedValue() + "'");
	}

	private void checkNoUnexpectedExportValues() throws SQLException {
		String sql = "select horizontal_colnames,series_index,value from ecrf_data_vertical";
		Statement s = getSqliteConnection().createStatement();
		ResultSet rs = s.executeQuery(sql);
		int count = 0;
		while (rs.next()) {
			info("unexpected export value " + rs.getString("horizontal_colnames") + ": '" + rs.getString("value") + "'");
			//Assert.assertEquals(rs.getString("value"), vector.getExportedValue(), label + " exported value: ");
			count++;
		}
		Assert.assertEquals(count, 0, "unexpected export values:");
		rs.close();
		s.close();
		info("no unexpected export values");
	}

	private String getFieldId(EcrfValidationTestVector vector) {
		return getFieldIdBySectionPositionName(
				vector.getEcrfField().getSection(),
				vector.getEcrfField().getPosition(),
				vector.getEcrfField().getField().getName(),
				vector.getIndex());
	}

	private void enterValue(EcrfValidationTestVector vector) throws IOException {
		switch (vector.getEcrfField().getField().getFieldType().getType()) {
			case SINGLE_LINE_TEXT:
			case MULTI_LINE_TEXT:
			case AUTOCOMPLETE:
			case INTEGER:
			case FLOAT:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						sendKeys(fieldId, vector.getInputValue());
						//if ("tabView:trialecrfstatusentry_form:ecrffield_inputs:0:section_inputs:0:singleLineText0".equals(fieldId)) {
						//	System.out.println("xxx");
						//}
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			case CHECKBOX:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						if (Boolean.parseBoolean(vector.getInputValue())) {
							clickCheckbox(fieldId);
						}
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			case DATE:
			case TIME:
			case TIMESTAMP:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						//sendKeys(fieldId,Keys.chord(Keys.CONTROL, "a"));
						//sendKeys(fieldId,Keys.DELETE);
						sendKeys(fieldId, vector.getInputValue());
						sendKeys(fieldId, Keys.ESCAPE); //close the dateselector popup as it can hide a button to click next
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			//case SKETCH:
			//break;
			case FILE:
				// the input value is the name of a test resource file to upload:
				final String filePath = getResourceFilePath(vector.getInputValue());
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						// the label is for the download link of the field, its upload control is next to it:
						uploadFile(fieldId.replace("filedownload", "fileupload"), filePath);
						// wait until the link shows the uploaded file:
						new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
								.until(ExpectedConditions.textToBePresentInElementLocated(By.id(fieldId), new File(filePath).getName()));
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						String[] values = vector.getInputValue().split("\n");
						for (int i = 0; i < values.length; i++) {
							clickSelectMany(fieldId, values[i]);
						}
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			case SELECT_ONE_RADIO_H:
			case SELECT_ONE_RADIO_V:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						clickSelectOneRadio(fieldId, vector.getInputValue());
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			case SELECT_ONE_DROPDOWN:
				new TrialEcrfFieldEntry() {

					@Override
					protected void entry() {
						String fieldId = getFieldId(vector);
						clickSelectOneMenu(fieldId, vector.getInputValue());
						saveSeriesSection(fieldId, vector.hasIndex());
					}
				}.enter();
				break;
			default:
				throw new IllegalArgumentException(vector.getEcrfField().getField().getFieldType().getType().name() + " input field type not supported");
		}
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
		newUser.setVisibleTrialTabList("probandlistentries,trialjobs,trialecrfstatusentries"); //,trialfiles");
		newUser.setEnableInputFieldModule(true);
		newUser.setVisibleInputFieldTabList(null);
		newUser.setEnableProbandModule(true);
		newUser.setVisibleProbandTabList(null);
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
		newPassword.setEnable2fa(false);
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

	private TrialOutVO createTrial(String name, long departmentId) throws Exception {
		TrialInVO newTrial = new TrialInVO();
		newTrial.setDepartmentId(departmentId);
		newTrial.setName(name);
		newTrial.setTitle(newTrial.getName());
		newTrial.setDescription(null);
		newTrial.setSignupProbandList(false);
		newTrial.setSignupInquiries(false);
		newTrial.setSignupRandomize(false);
		newTrial.setSignupDescription(null);
		newTrial.setExclusiveProbands(false);
		newTrial.setProbandAliasFormat("subject-{9,number,000}-" + getTestId());
		newTrial.setDutySelfAllocationLocked(false);
		return getTestDataProvider().createTrial(newTrial, "migration_started", "na", "na", "na");
	}

	@Override
	public void println(String line) {
		info("    " + line);
	}
}
