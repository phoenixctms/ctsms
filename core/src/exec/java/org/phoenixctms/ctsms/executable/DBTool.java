package org.phoenixctms.ctsms.executable;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.executable.DemoDataProvider.SearchCriteria;
import org.phoenixctms.ctsms.executable.claml.ClamlImporter;
import org.phoenixctms.ctsms.executable.csv.CsvExporter;
import org.phoenixctms.ctsms.executable.csv.CsvImporter;
import org.phoenixctms.ctsms.executable.migration.FileDecryptInitializer;
import org.phoenixctms.ctsms.executable.migration.JournalSystemMessageCodeInitializer;
import org.phoenixctms.ctsms.executable.migration.ProbandCommentFieldInitializer;
import org.phoenixctms.ctsms.executable.migration.ProbandImageFieldInitializer;
import org.phoenixctms.ctsms.executable.migration.ProbandListStatusEntryReasonDecryptInitializer;
import org.phoenixctms.ctsms.executable.xls.XlsExporter;
import org.phoenixctms.ctsms.executable.xls.XlsImporter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Compile;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DBToolOptions;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBTool {

	private static AuthenticationVO getAuthenticationOptionValue(CommandLine line) throws Exception {
		String username;
		String password;
		if (line.hasOption(DBToolOptions.AUTH_OPT)) {
			try {
				String[] auth = (new String(Base64.decodeBase64(line.getOptionValue(DBToolOptions.AUTH_OPT)), CommonUtil.BASE64_CHARSET)).split("\n", 2);
				username = auth[0];
				password = auth[1];
			} catch (Exception e) {
				throw new IllegalArgumentException("invalid base64 auth", e);
			}
		} else {
			if (!line.hasOption(DBToolOptions.USERNAME_OPT)) {
				throw new IllegalArgumentException("username required");
			} else {
				username = line.getOptionValue(DBToolOptions.USERNAME_OPT);
			}
			if (!line.hasOption(DBToolOptions.PASSWORD_OPT)) {
				throw new IllegalArgumentException("password required");
			} else {
				password = line.getOptionValue(DBToolOptions.PASSWORD_OPT);
			}
		}
		return new AuthenticationVO(username, password, null, "localhost");
	}

	private static String getDepartmentL10nKeyOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.DEPARTMENT_L10N_KEY_OPT)) {
			throw new IllegalArgumentException("department l10n name required");
		}
		String departmentL1nKey = line.getOptionValue(DBToolOptions.DEPARTMENT_L10N_KEY_OPT);
		if (!CommonUtil.isEmptyString(departmentL1nKey)) {
			return departmentL1nKey;
		} else if (required) {
			throw new IllegalArgumentException("empty department l10n name");
		} else {
			return null;
		}
	}

	private static String getDepartmentPassword(CommandLine line) throws Exception {
		if (!line.hasOption(DBToolOptions.DEPARTMENT_PASSWORD_OPT)) {
			throw new IllegalArgumentException("department password required");
		}
		return line.getOptionValue(DBToolOptions.DEPARTMENT_PASSWORD_OPT);
	}

	private static Long getIdOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.ID_OPT)) {
			throw new IllegalArgumentException("entity id required");
		}
		try {
			return Long.parseLong(line.getOptionValue(DBToolOptions.ID_OPT));
		} catch (NumberFormatException e) {
			if (required) {
				throw new IllegalArgumentException("entity id: number required");
			} else {
				return null;
			}
		}
	}

	private static Long getJobIdOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.JOB_ID_OPT)) {
			throw new IllegalArgumentException("job id required");
		}
		try {
			return Long.parseLong(line.getOptionValue(DBToolOptions.JOB_ID_OPT));
		} catch (NumberFormatException e) {
			if (required) {
				throw new IllegalArgumentException("job id: number required");
			} else {
				return null;
			}
		}
	}

	private static Integer getLimitOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.LIMIT_OPT)) {
			throw new IllegalArgumentException("limit required");
		}
		try {
			return Integer.parseInt(line.getOptionValue(DBToolOptions.LIMIT_OPT));
		} catch (NumberFormatException e) {
			if (required) {
				throw new IllegalArgumentException("limit: number required");
			} else {
				return null;
			}
		}
	}

	private static String getNewDepartmentPassword(CommandLine line) throws Exception {
		if (!line.hasOption(DBToolOptions.NEW_DEPARTMENT_PASSWORD_OPT)) {
			throw new IllegalArgumentException("new department password required");
		}
		return line.getOptionValue(DBToolOptions.NEW_DEPARTMENT_PASSWORD_OPT);
	}

	private static ProbandListStatusLogLevel getProbandListStatusLogLevelOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.PROBAND_LIST_STATUS_LOG_LEVEL_OPT)) {
			throw new IllegalArgumentException("proband list status log level required");
		}
		String option = line.getOptionValue(DBToolOptions.PROBAND_LIST_STATUS_LOG_LEVEL_OPT);
		if (!CommonUtil.isEmptyString(option)) {
			try {
				return ProbandListStatusLogLevel.fromString(option);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("proband list status log level: level name required, one of " + StringUtils.join(ProbandListStatusLogLevel.names(), ", "));
			}
		} else if (required) {
			throw new IllegalArgumentException("empty proband list status log level");
		} else {
			return null;
		}
	}

	private static Object[] getVariablePeriodOptionValue(CommandLine line, boolean required) throws Exception {
		if (required && !line.hasOption(DBToolOptions.VARIABLE_PERIOD_OPT)) {
			throw new IllegalArgumentException("period required");
		}
		String option = line.getOptionValue(DBToolOptions.VARIABLE_PERIOD_OPT);
		if (!CommonUtil.isEmptyString(option)) {
			VariablePeriod period;
			try {
				period = VariablePeriod.fromString(option);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("period: period name required, one of " + StringUtils.join(VariablePeriod.names(), ", "));
			}
			Long explicitDays = null;
			if (VariablePeriod.EXPLICIT.equals(period)) {
				if (!line.hasOption(DBToolOptions.VARIABLE_PERIOD_EXPLICIT_DAYS_OPT)) {
					throw new IllegalArgumentException("explicit days required");
				}
				option = line.getOptionValue(DBToolOptions.VARIABLE_PERIOD_EXPLICIT_DAYS_OPT);
				try {
					explicitDays = Long.parseLong(option);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("explicit days: number required");
				}
			}
			return new Object[] { period, explicitDays };
		} else if (required) {
			throw new IllegalArgumentException("empty period");
		} else {
			return new Object[] { null, null };
		}
	}

	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();
		DBTool dbTool = null;
		CommandLine line = null;
		Option job = null;
		boolean sendEmail = false;
		try {
			line = parser.parse(DBToolOptions.options, args);
			dbTool = new DBTool(); // hibernate starts a long time...
			try {
				setPropertiesFile(line);
				if (line.hasOption(DBToolOptions.INIT_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INIT_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - master data records will be (re-)created!")) {
						dbTool.getProductionDataProvider().initializeDB();
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_CRITERION_PROPERTIES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_CRITERION_PROPERTIES_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadCriterionProperties(line.getOptionValue(DBToolOptions.IMPORT_CRITERION_PROPERTIES_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.SCAN_MISSING_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.SCAN_MISSING_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getExternalFileChecker().scanMissingExternalFiles(false) > 0l;
				} else if (line.hasOption(DBToolOptions.SCAN_ORPHANED_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.SCAN_ORPHANED_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getExternalFileChecker().scanOrphanedExternalFiles(false) > 0l;
				} else if (line.hasOption(DBToolOptions.DELETE_MISSING_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_MISSING_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - file records will be deleted!")) {
						sendEmail = dbTool.getExternalFileChecker().scanMissingExternalFiles(true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_ORPHANED_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_ORPHANED_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "file system will be modified - files will be deleted!")) {
						sendEmail = dbTool.getExternalFileChecker().scanOrphanedExternalFiles(true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_BANK_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_BANK_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadBankIdentifications(line.getOptionValue(DBToolOptions.IMPORT_BANK_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_COUNTRY_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_COUNTRY_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter()
							.loadCountries(line.getOptionValue(DBToolOptions.IMPORT_COUNTRY_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_TITLE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_TITLE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadTitles(line.getOptionValue(DBToolOptions.IMPORT_TITLE_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_STREET_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_STREET_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadStreets(line.getOptionValue(DBToolOptions.IMPORT_STREET_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							true) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_ZIP_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_ZIP_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadZips(line.getOptionValue(DBToolOptions.IMPORT_ZIP_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_INVENTORY_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_INVENTORY_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_INVENTORY_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), FileModule.INVENTORY_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_STAFF_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_STAFF_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_STAFF_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							FileModule.STAFF_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_COURSE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_COURSE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_COURSE_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							FileModule.COURSE_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_TRIAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_TRIAL_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_TRIAL_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							FileModule.TRIAL_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_PROBAND_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_PROBAND_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_PROBAND_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							FileModule.PROBAND_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_INPUT_FIELD_IMAGE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_INPUT_FIELD_IMAGE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_INPUT_FIELD_IMAGE_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), FileModule.INPUT_FIELD_IMAGE, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_STAFF_IMAGE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_STAFF_IMAGE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_STAFF_IMAGE_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), FileModule.STAFF_IMAGE, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_PROBAND_IMAGE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_PROBAND_IMAGE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_PROBAND_IMAGE_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), FileModule.PROBAND_IMAGE, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_JOB_FILE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_JOB_FILE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_JOB_FILE_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), FileModule.JOB_FILE, false) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MIME_MASS_MAIL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MIME_MASS_MAIL_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadMimeTypes(line.getOptionValue(DBToolOptions.IMPORT_MIME_MASS_MAIL_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT),
							FileModule.MASS_MAIL_DOCUMENT, false) > 0l;
				} else if (line.hasOption(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_INTERACTIVE_OPT)) {
					dbTool.getJobOutput().printPrelude(DBToolOptions.getTaskAndLockProcess(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_INTERACTIVE_OPT));
					dbTool.getDepartmentManager().interactiveChangeDepartmentPassword();
				} else if (line.hasOption(DBToolOptions.CREATE_DEPARTMENT_INTERACTIVE_OPT)) {
					dbTool.getJobOutput().printPrelude(DBToolOptions.getTaskAndLockProcess(DBToolOptions.CREATE_DEPARTMENT_INTERACTIVE_OPT));
					dbTool.getDepartmentManager().interactiveCreateDepartment();
				} else if (line.hasOption(DBToolOptions.CREATE_USER_INTERACTIVE_OPT)) {
					dbTool.getJobOutput().printPrelude(DBToolOptions.getTaskAndLockProcess(DBToolOptions.CREATE_USER_INTERACTIVE_OPT));
					dbTool.getDepartmentManager().interactiveCreateUser();
				} else if (line.hasOption(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (!line.hasOption(DBToolOptions.OLD_DEPARTMENT_PASSWORD_OPT)) {
						throw new IllegalArgumentException("old department password required");
					}
					dbTool.getDepartmentManager().changeDepartmentPassword(getDepartmentL10nKeyOptionValue(line, true), getNewDepartmentPassword(line),
							line.getOptionValue(DBToolOptions.OLD_DEPARTMENT_PASSWORD_OPT));
				} else if (line.hasOption(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_USER_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.CHANGE_DEPARTMENT_PASSWORD_USER_OPT);
					dbTool.getJobOutput().printPrelude(job);
					dbTool.getDepartmentManager().changeDepartmentPassword(getAuthenticationOptionValue(line), getNewDepartmentPassword(line));
				} else if (line.hasOption(DBToolOptions.CREATE_DEPARTMENT_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.CREATE_DEPARTMENT_OPT);
					dbTool.getJobOutput().printPrelude(job);
					dbTool.getDepartmentManager().createDepartment(getDepartmentL10nKeyOptionValue(line, true), getDepartmentPassword(line));
				} else if (line.hasOption(DBToolOptions.CREATE_USER_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.CREATE_USER_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (!line.hasOption(DBToolOptions.USERNAME_OPT)) {
						throw new IllegalArgumentException("new username required");
					}
					if (!line.hasOption(DBToolOptions.PASSWORD_OPT)) {
						throw new IllegalArgumentException("password for new user required");
					}
					dbTool.getDepartmentManager().createUser(getDepartmentL10nKeyOptionValue(line, true), getDepartmentPassword(line),
							line.getOptionValue(DBToolOptions.USERNAME_OPT), line.getOptionValue(DBToolOptions.PASSWORD_OPT),
							line.getOptionValue(DBToolOptions.USER_LANG_OPT), line.getOptionValue(DBToolOptions.PERMISSION_PROFILES_OPT));
				} else if (line.hasOption(DBToolOptions.UPDATE_PROBAND_DEPARTMENT_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.UPDATE_PROBAND_DEPARTMENT_OPT);
					dbTool.getJobOutput().printPrelude(job);
					dbTool.getServiceMethods().updateProbandDepartment(
							getAuthenticationOptionValue(line),
							getIdOptionValue(line, true), getDepartmentL10nKeyOptionValue(line, true),
							line.getOptionValue(DBToolOptions.NEW_DEPARTMENT_PASSWORD_OPT),
							line.getOptionValue(DBToolOptions.OLD_DEPARTMENT_PASSWORD_OPT));
				} else if (line.hasOption(DBToolOptions.LOAD_DEMO_DATA_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.LOAD_DEMO_DATA_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - created test data records will remain!")) {
						dbTool.getDemoDataProvider().createDepartmentsAndUsers(3, 5);
						dbTool.getDemoDataProvider().createStaff(10);
						dbTool.getDemoDataProvider().createInventory(10);
						dbTool.getDemoDataProvider().createCourses(5);
						dbTool.getDemoDataProvider().createProbands(30);
						dbTool.getDemoDataProvider().createTrials(2,
								new DemoDataProvider.SearchCriteria[] { SearchCriteria.SUBJECTS_1 },
								new Integer[] { 4 },
								new Integer[] { 2 },
								new Integer[] { 2 });
						dbTool.getDemoDataProvider().createFormScriptingTrial();
						dbTool.getDemoDataProvider().createGroupCoinRandomizationTrial(5, 100);
						dbTool.getDemoDataProvider().createCriterias();
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_INVENTORY_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_INVENTORY_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line),
							line.getOptionValue(DBToolOptions.IMPORT_INVENTORY_DOCUMENT_FILES_OPT),
							FileModule.INVENTORY_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_STAFF_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_STAFF_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line), line.getOptionValue(DBToolOptions.IMPORT_STAFF_DOCUMENT_FILES_OPT),
							FileModule.STAFF_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_COURSE_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_COURSE_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line), line.getOptionValue(DBToolOptions.IMPORT_COURSE_DOCUMENT_FILES_OPT),
							FileModule.COURSE_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_TRIAL_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_TRIAL_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line), line.getOptionValue(DBToolOptions.IMPORT_TRIAL_DOCUMENT_FILES_OPT),
							FileModule.TRIAL_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_PROBAND_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_PROBAND_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line), line.getOptionValue(DBToolOptions.IMPORT_PROBAND_DOCUMENT_FILES_OPT),
							FileModule.PROBAND_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_MASS_MAIL_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_MASS_MAIL_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getFileSystemLoader().importFiles(getAuthenticationOptionValue(line),
							line.getOptionValue(DBToolOptions.IMPORT_MASS_MAIL_DOCUMENT_FILES_OPT),
							FileModule.MASS_MAIL_DOCUMENT, getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.REMOVE_INVENTORY_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_INVENTORY_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.INVENTORY_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.REMOVE_STAFF_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_STAFF_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.STAFF_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.REMOVE_COURSE_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_COURSE_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.COURSE_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.REMOVE_TRIAL_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_TRIAL_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.TRIAL_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.REMOVE_PROBAND_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_PROBAND_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.PROBAND_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.REMOVE_MASS_MAIL_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_MASS_MAIL_DOCUMENT_FILES_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB and file system will be modified - records and files will be deleted!")) {
						sendEmail = dbTool.getFileSystemLoader().deleteFileRecords(FileModule.MASS_MAIL_DOCUMENT, getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_INVENTORY_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_INVENTORY_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.INVENTORY_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_STAFF_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_STAFF_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.STAFF_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_COURSE_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_COURSE_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.COURSE_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_TRIAL_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_TRIAL_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.TRIAL_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_PROBAND_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_PROBAND_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.PROBAND_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_INPUT_FIELD_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_INPUT_FIELD_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.INPUT_FIELD_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_USER_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_USER_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.USER_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_CRITERIA_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_CRITERIA_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.CRITERIA_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PURGE_MASS_MAIL_JOURNAL)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PURGE_MASS_MAIL_JOURNAL);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - records will be deleted!")) {
						Object[] period = getVariablePeriodOptionValue(line, true);
						sendEmail = dbTool.getJournalPurger().removeJournalEntries((VariablePeriod) period[0], (Long) period[1], JournalModule.MASS_MAIL_JOURNAL,
								getIdOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PREPARE_NOTIFICATIONS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PREPARE_NOTIFICATIONS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getNotificationSender().prepareNotifications(getDepartmentL10nKeyOptionValue(line, false)) > 0l;
				} else if (line.hasOption(DBToolOptions.SEND_NOTIFICATIONS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.SEND_NOTIFICATIONS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getNotificationSender().processNotifications(getDepartmentL10nKeyOptionValue(line, false), getLimitOptionValue(line, false)) > 0l;
				} else if (line.hasOption(DBToolOptions.SEND_MASS_MAILS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.SEND_MASS_MAILS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getMassMailSender().processMassMails(getAuthenticationOptionValue(line), getDepartmentL10nKeyOptionValue(line, false),
							getLimitOptionValue(line, false)) > 0l;
				} else if (line.hasOption(DBToolOptions.REMOVE_PROBANDS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.REMOVE_PROBANDS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - probands will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().deleteProbands(getDepartmentL10nKeyOptionValue(line, false), getLimitOptionValue(line, false)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_INVENTORY_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_INVENTORY_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - inventory will be deleted!")) {
						dbTool.getServiceMethods().deleteInventory(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_INVENTORY_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_INVENTORY_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - inventory items will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performInventoryDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_STAFF_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_STAFF_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - person/organisation will be deleted!")) {
						dbTool.getServiceMethods().deleteStaff(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_STAFF_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_STAFF_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - persons/organisations will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performStaffDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_COURSE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_COURSE_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - course will be deleted!")) {
						dbTool.getServiceMethods().deleteCourse(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_COURSE_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_COURSE_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - courses will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performCourseDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_TRIAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_TRIAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - trial will be deleted!")) {
						dbTool.getServiceMethods().deleteTrial(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_TRIAL_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_TRIAL_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - trials will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performTrialDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_INQUIRY_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_INQUIRY_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - inquiry will be deleted!")) {
						dbTool.getServiceMethods().deleteInquiry(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.DELETE_ECRF_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_ECRF_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - eCRFs will be deleted!")) {
						dbTool.getServiceMethods().deleteEcrf(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.DELETE_ECRF_FIELD_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_ECRF_FIELD_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - eCRF fields will be deleted!")) {
						dbTool.getServiceMethods().deleteEcrfField(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_INQUIRY_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_INQUIRY_DEFERRED_DELETE_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - inquiries will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performInquiryDeferredDelete(getAuthenticationOptionValue(line), getIdOptionValue(line, false), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_ECRF_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_ECRF_DEFERRED_DELETE_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - eCRFs will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performEcrfDeferredDelete(getAuthenticationOptionValue(line), getIdOptionValue(line, false), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_ECRF_FIELD_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_ECRF_FIELD_DEFERRED_DELETE_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - eCRF fields will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performEcrfFieldDeferredDelete(getAuthenticationOptionValue(line), getIdOptionValue(line, false), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_PROBAND_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_PROBAND_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - proband will be deleted!")) {
						dbTool.getServiceMethods().deleteProband(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_PROBAND_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_PROBAND_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - probands will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performProbandDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_MASS_MAIL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_MASS_MAIL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - mass mail will be deleted!")) {
						dbTool.getServiceMethods().deleteMassMail(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_MASS_MAIL_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_MASS_MAIL_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - mass mails will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performMassMailDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_USER_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_USER_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - user will be deleted!")) {
						dbTool.getServiceMethods().deleteUser(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_USER_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_USER_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - users will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performUserDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_INPUT_FIELD_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_INPUT_FIELD_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - input field will be deleted!")) {
						dbTool.getServiceMethods().deleteInputField(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_INPUT_FIELD_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_INPUT_FIELD_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - input fields will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performInputFieldDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_SELECTION_SET_VALUE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_SELECTION_SET_VALUE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - selection set value will be deleted!")) {
						dbTool.getServiceMethods().deleteSelectionSetValue(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_SELECTION_SET_VALUE_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_SELECTION_SET_VALUE_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - selection set values will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performSelectionSetValueDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.DELETE_CRITERIA_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.DELETE_CRITERIA_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - criteria will be deleted!")) {
						dbTool.getServiceMethods().deleteCriteria(getAuthenticationOptionValue(line), getIdOptionValue(line, true));
					}
				} else if (line.hasOption(DBToolOptions.PERFORM_CRITERIA_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_CRITERIA_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - criteria items will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performCriteriaDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.SCAN_ALL_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.SCAN_ALL_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getServiceMethods().performAllDeferredDelete(null, false) > 0l;
				} else if (line.hasOption(DBToolOptions.PERFORM_ALL_DEFERRED_DELETE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PERFORM_ALL_DEFERRED_DELETE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - items will be deleted!")) {
						sendEmail = dbTool.getServiceMethods().performAllDeferredDelete(getAuthenticationOptionValue(line), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_PERMISSION_DEFINITIONS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_PERMISSION_DEFINITIONS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadPermissionDefinitions(line.getOptionValue(DBToolOptions.IMPORT_PERMISSION_DEFINITIONS_OPT),
							line.getOptionValue(DBToolOptions.ENCODING_OPT), true) > 0l;
				} else if (line.hasOption(DBToolOptions.PATCH_USER_IDENTITY_DEPARTMENT_PERMISSION_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.PATCH_USER_IDENTITY_DEPARTMENT_PERMISSION_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testOverwriteFile(line, ExecUtil.formatFileName(line.getOptionValue(DBToolOptions.PATCH_USER_IDENTITY_DEPARTMENT_PERMISSION_OPT), "{0}_new.{1}"))) {
						sendEmail = dbTool.getFileExporter().exportPermissionDefinitions(
								ExecUtil.formatFileName(line.getOptionValue(DBToolOptions.PATCH_USER_IDENTITY_DEPARTMENT_PERMISSION_OPT), "{0}_new.{1}"),
								line.getOptionValue(DBToolOptions.ENCODING_OPT),
								dbTool.getCsvImporter().readPermissionDefinitionsPatchUserIdentityDepartmentPermission(
										line.getOptionValue(DBToolOptions.PATCH_USER_IDENTITY_DEPARTMENT_PERMISSION_OPT),
										line.getOptionValue(DBToolOptions.ENCODING_OPT))) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT))) {
						sendEmail = dbTool.getFileExporter().exportPermissionTemplate(line.getOptionValue(DBToolOptions.EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT),
								line.getOptionValue(DBToolOptions.ENCODING_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_PERMISSION_DEFINITIONS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PERMISSION_DEFINITIONS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					if (dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_PERMISSION_DEFINITIONS_OPT))) {
						sendEmail = dbTool.getFileExporter().exportPermissionDefinitions(line.getOptionValue(DBToolOptions.EXPORT_PERMISSION_DEFINITIONS_OPT),
								line.getOptionValue(DBToolOptions.ENCODING_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.INITIALIZE_PROBAND_IMAGE_FIELDS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INITIALIZE_PROBAND_IMAGE_FIELDS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getProbandImageFieldInitializer().update(getAuthenticationOptionValue(line)) > 0l;
				} else if (line.hasOption(DBToolOptions.INITIALIZE_PROBAND_COMMENT_FIELDS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INITIALIZE_PROBAND_COMMENT_FIELDS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getProbandCommentFieldInitializer().update(getAuthenticationOptionValue(line)) > 0l;
				} else if (line.hasOption(DBToolOptions.INITIALIZE_DECRYPTED_TRIAL_DOCUMENT_FILES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INITIALIZE_DECRYPTED_TRIAL_DOCUMENT_FILES_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getFileDecryptInitializer().update(getAuthenticationOptionValue(line)) > 0l;
				} else if (line.hasOption(DBToolOptions.INITIALIZE_DECRYPTED_PROBAND_LIST_STATUS_REASONS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INITIALIZE_DECRYPTED_PROBAND_LIST_STATUS_REASONS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getProbandListStatusEntryReasonDecryptInitializer().update(getAuthenticationOptionValue(line)) > 0l;
				} else if (line.hasOption(DBToolOptions.INITIALIZE_JOURNAL_SYSTEM_MESSAGE_CODE_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.INITIALIZE_JOURNAL_SYSTEM_MESSAGE_CODE_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getJournalSystemMessageCodeInitializer().update(getAuthenticationOptionValue(line)) > 0l;
				} else if (line.hasOption(DBToolOptions.HELP_OPT)) {
					dbTool.getJobOutput().printPrelude(DBToolOptions.getTaskAndLockProcess(DBToolOptions.HELP_OPT));
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("dbtool [task <arg>] [option1 <arg> option2 <arg> ...]", DBToolOptions.options);
				} else if (line.hasOption(DBToolOptions.IMPORT_ICD_SYSTEMATICS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_ICD_SYSTEMATICS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getClamlImporter().loadIcdSyst(line.getOptionValue(DBToolOptions.IMPORT_ICD_SYSTEMATICS_OPT),
							line.hasOption(DBToolOptions.FLUSH_REVISION_OPT),
							line.getOptionValue(DBToolOptions.ICD_SYSTEMATICS_REVISION_OPT), line.getOptionValue(DBToolOptions.SYSTEMATICS_LANG_OPT)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_ALPHA_IDS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_ALPHA_IDS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadAlphaIds(line.getOptionValue(DBToolOptions.IMPORT_ALPHA_IDS_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							line.hasOption(DBToolOptions.FLUSH_REVISION_OPT), line.getOptionValue(DBToolOptions.ALPHA_ID_REVISION_OPT),
							line.getOptionValue(DBToolOptions.ICD_SYSTEMATICS_REVISION_OPT)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_OPS_SYSTEMATICS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_OPS_SYSTEMATICS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getClamlImporter().loadOpsSyst(line.getOptionValue(DBToolOptions.IMPORT_OPS_SYSTEMATICS_OPT),
							line.hasOption(DBToolOptions.FLUSH_REVISION_OPT),
							line.getOptionValue(DBToolOptions.OPS_SYSTEMATICS_REVISION_OPT), line.getOptionValue(DBToolOptions.SYSTEMATICS_LANG_OPT)) > 0l;
				} else if (line.hasOption(DBToolOptions.IMPORT_OPS_CODES_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_OPS_CODES_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getCsvImporter().loadOpsCodes(line.getOptionValue(DBToolOptions.IMPORT_OPS_CODES_OPT), line.getOptionValue(DBToolOptions.ENCODING_OPT),
							line.hasOption(DBToolOptions.FLUSH_REVISION_OPT), line.getOptionValue(DBToolOptions.OPS_CODE_REVISION_OPT),
							line.getOptionValue(DBToolOptions.OPS_SYSTEMATICS_REVISION_OPT)) > 0l;
				} else if (line.hasOption(DBToolOptions.EXPORT_CRITERIA_RESULT_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_CRITERIA_RESULT_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_RESULT_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_RESULT_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportCriteriaResults(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_RESULT_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT))) {
						sendEmail = dbTool.getServiceMethods().renderCourseParticipantListPDFs(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_CRITERIA_CVS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_CRITERIA_CVS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_CVS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_CVS_OPT))) {
						sendEmail = dbTool.getServiceMethods().renderCvPDFs(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_CVS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_CRITERIA_PROBAND_LETTERS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_CRITERIA_PROBAND_LETTERS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_PROBAND_LETTERS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_PROBAND_LETTERS_OPT))) {
						sendEmail = dbTool.getServiceMethods().renderProbandLetterPDFs(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_PROBAND_LETTERS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_PROBAND_LIST_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PROBAND_LIST_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_PROBAND_LIST_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_PROBAND_LIST_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportProbandList(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								getProbandListStatusLogLevelOptionValue(line, false),
								line.getOptionValue(DBToolOptions.EXPORT_PROBAND_LIST_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_INPUT_FIELDS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_INPUT_FIELDS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - input fields and selection set values will be updated!")) {
						sendEmail = dbTool.getXlsImporter().loadInputFields(line.getOptionValue(DBToolOptions.IMPORT_INPUT_FIELDS_OPT), getAuthenticationOptionValue(line)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_ECRFS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_ECRFS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - eCRFs, eCRF fields, input fields and selection set values will be updated!")) {
						sendEmail = dbTool.getXlsImporter().loadEcrfs(line.getOptionValue(DBToolOptions.IMPORT_ECRFS_OPT), getAuthenticationOptionValue(line),
								getIdOptionValue(line, true)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_INPUT_FIELD_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PROBAND_LIST_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_OPT))) {
						sendEmail = dbTool.getXlsExporter().exportInputField(
								line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_OPT),
								getAuthenticationOptionValue(line), getIdOptionValue(line, true)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_ECRFS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_ECRFS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_ECRFS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_ECRFS_OPT))) {
						sendEmail = dbTool.getXlsExporter().exportEcrfs(
								line.getOptionValue(DBToolOptions.EXPORT_ECRFS_OPT),
								getAuthenticationOptionValue(line), getIdOptionValue(line, true)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_RANDOMIZATION_LISTS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_RANDOMIZATION_LISTS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (dbTool.testForced(line, "DB will be modified - randomization lists will be updated!")) {
						sendEmail = dbTool.getXlsImporter().loadRandomizationLists(line.getOptionValue(DBToolOptions.IMPORT_RANDOMIZATION_LISTS_OPT),
								getAuthenticationOptionValue(line), getIdOptionValue(line, true), true) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.IMPORT_ASPS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.IMPORT_ASPS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getXlsImporter().loadAsps(line.getOptionValue(DBToolOptions.IMPORT_ASPS_OPT),
							line.hasOption(DBToolOptions.FLUSH_REVISION_OPT), line.getOptionValue(DBToolOptions.ASP_REVISION_OPT)) > 0l;
				} else if (line.hasOption(DBToolOptions.EXPORT_ECRF_PDFS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_ECRF_PDFS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_ECRF_PDFS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_ECRF_PDFS_OPT))) {
						sendEmail = dbTool.getServiceMethods().renderEcrfPDFs(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_ECRF_PDFS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.VALIDATE_PENDING_ECRFS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.VALIDATE_PENDING_ECRFS_OPT);
					dbTool.initJob(line).printPrelude(job);
					sendEmail = dbTool.getServiceMethods().validatePendingTrialEcrfs(getAuthenticationOptionValue(line), getIdOptionValue(line, true)) > 0l;
				} else if (line.hasOption(DBToolOptions.EXPORT_AUDIT_TRAIL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_AUDIT_TRAIL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_AUDIT_TRAIL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_AUDIT_TRAIL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportAuditTrail(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_AUDIT_TRAIL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_INVENTORY_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_INVENTORY_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_INVENTORY_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_INVENTORY_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportInventoryJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_INVENTORY_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_STAFF_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_STAFF_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_STAFF_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_STAFF_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportStaffJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_STAFF_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_COURSE_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_COURSE_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_COURSE_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_COURSE_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportCourseJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_COURSE_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_TRIAL_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_TRIAL_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_TRIAL_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_TRIAL_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportTrialJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_TRIAL_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_ECRF_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_ECRF_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_ECRF_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_ECRF_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportEcrfJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_ECRF_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_PROBAND_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PROBAND_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_PROBAND_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_PROBAND_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportProbandJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_PROBAND_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_INPUT_FIELD_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_INPUT_FIELD_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportInputFieldJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_INPUT_FIELD_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_MASS_MAIL_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_MASS_MAIL_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_MASS_MAIL_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_MASS_MAIL_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportMassMailJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_MASS_MAIL_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_USER_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_USER_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_USER_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_USER_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportUserJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_USER_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_CRITERIA_JOURNAL_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_CRITERIA_JOURNAL_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_JOURNAL_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_JOURNAL_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportCriteriaJournal(getAuthenticationOptionValue(line), getIdOptionValue(line, true),
								line.getOptionValue(DBToolOptions.EXPORT_CRITERIA_JOURNAL_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_PROBAND_INVENTORY_BOOKINGS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_PROBAND_INVENTORY_BOOKINGS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_PROBAND_INVENTORY_BOOKINGS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_PROBAND_INVENTORY_BOOKINGS_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportProbandInventoryBookings(getAuthenticationOptionValue(line), getIdOptionValue(line, false),
								line.getOptionValue(DBToolOptions.EXPORT_PROBAND_INVENTORY_BOOKINGS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OPT);
					dbTool.initJob(line).printPrelude(job);
					if (CommonUtil.isEmptyString(line.getOptionValue(DBToolOptions.EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OPT))
							|| dbTool.testOverwriteFile(line, line.getOptionValue(DBToolOptions.EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OPT))) {
						sendEmail = dbTool.getServiceMethods().exportVisitScheduleAppointments(getAuthenticationOptionValue(line), getIdOptionValue(line, false),
								line.getOptionValue(DBToolOptions.EXPORT_VISIT_SCHEDULE_APPOINTMENTS_OPT)) > 0l;
					}
				} else if (line.hasOption(DBToolOptions.RUN_DAILY_JOBS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.RUN_DAILY_JOBS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getJobRunner().processJobs(getAuthenticationOptionValue(line), true, null, null) > 0l;
				} else if (line.hasOption(DBToolOptions.RUN_WEEKLY_JOBS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.RUN_WEEKLY_JOBS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getJobRunner().processJobs(getAuthenticationOptionValue(line), null, true, null) > 0l;
				} else if (line.hasOption(DBToolOptions.RUN_MONTHLY_JOBS_OPT)) {
					job = DBToolOptions.getTaskAndLockProcess(DBToolOptions.RUN_MONTHLY_JOBS_OPT);
					dbTool.getJobOutput().printPrelude(job);
					sendEmail = dbTool.getJobRunner().processJobs(getAuthenticationOptionValue(line), null, null, true) > 0l;
				}
			} catch (Throwable t) {
				if (job == null) { //lockexception
					try {
						dbTool.initJob(line);
					} catch (Exception e) {
					}
				}
				dbTool.getJobOutput().printExecutionTime(true);
				dbTool.getJobOutput().println(CoreUtil.getStackTrace(t));
				if (job != null && !CommonUtil.isEmptyString(job.getDescription())) {
					try {
						int toCount = dbTool.getJobOutput().send("ERROR - {0}", job, line, true);
						if (toCount > 0) {
							dbTool.getJobOutput().println("email sent to " + toCount + " recipients");
						}
					} catch (Exception e) {
						dbTool.getJobOutput().println(e.getMessage());
					}
				}
				dbTool.getJobOutput().flushJob(JobStatus.FAILED);
				dbTool.closeContext();
				System.exit(1);
			}
		} catch (ParseException e) {
			(new JobOutput()).println(e.getMessage());
			System.exit(1);
		}
		if (job != null && !CommonUtil.isEmptyString(job.getDescription())) {
			dbTool.getJobOutput().printExecutionTime(false);
			try {
				int toCount = dbTool.getJobOutput().send("success - {0}", job, line, sendEmail);
				if (toCount > 0) {
					dbTool.getJobOutput().println("email sent to " + toCount + " recipients");
				}
			} catch (Exception e) {
				dbTool.getJobOutput().println(e.getMessage());
			}
		}
		dbTool.getJobOutput().flushJob(JobStatus.OK);
		dbTool.closeContext();
		System.exit(0);
	}

	private static void setPropertiesFile(CommandLine line) throws Exception {
		if (line.hasOption(DBToolOptions.PROPERTIES_FILE_OPT)) {
			try {
				ExecSettings.setBundleFilename(line.getOptionValue(DBToolOptions.PROPERTIES_FILE_OPT));
			} catch (Exception e) {
				throw new IllegalArgumentException("error loading the specified properties file", e);
			}
		}
	}

	private ApplicationContext context;
	private ProductionDataProvider productionDataProvider;
	private ExternalFileChecker externalFileChecker;
	private CsvImporter csvImporter;
	private CsvExporter csvExporter;
	private XlsImporter xlsImporter;
	private XlsExporter xlsExporter;
	private DepartmentManager departmentManager;
	private DemoDataProvider demoDataProvider;
	private FileSystemLoader fileSystemLoader;
	private NotificationSender notificationSender;
	private MassMailSender massMailSender;
	private ServiceMethods serviceMethods;
	private ClamlImporter clamlImporter;
	private ProbandImageFieldInitializer probandImageFieldInitializer;
	private ProbandCommentFieldInitializer probandCommentFieldInitializer;
	private FileDecryptInitializer fileDecryptInitializer;
	private ProbandListStatusEntryReasonDecryptInitializer probandListStatusEntryReasonDecryptInitializer;
	private JournalSystemMessageCodeInitializer journalSystemMessageCodeInitializer;
	private JournalPurger journalPurger;
	private JobOutput jobOutput;
	private JobRunner jobRunner;

	private DBTool() {
		Date start = new Date();
		(new JobOutput()).printStarting();
		initContext();
		getJobOutput().setStart(start);
	}

	private void closeContext() {
		if (context != null) {
			((ClassPathXmlApplicationContext) context).close();
		}
	}

	private ClamlImporter getClamlImporter() {
		if (clamlImporter == null) {
			clamlImporter = context.getBean(ClamlImporter.class);
			clamlImporter.setJobOutput(getJobOutput());
		}
		return clamlImporter;
	}

	private CsvImporter getCsvImporter() {
		if (csvImporter == null) {
			csvImporter = context.getBean(CsvImporter.class);
			csvImporter.setJobOutput(getJobOutput());
		}
		return csvImporter;
	}

	private JobRunner getJobRunner() {
		if (jobRunner == null) {
			jobRunner = context.getBean(JobRunner.class);
			jobRunner.setJobOutput(jobOutput);
		}
		return jobRunner;
	}

	private DemoDataProvider getDemoDataProvider() {
		if (demoDataProvider == null) {
			demoDataProvider = context.getBean(DemoDataProvider.class);
			demoDataProvider.setJobOutput(getJobOutput());
		}
		return demoDataProvider;
	}

	private DepartmentManager getDepartmentManager() {
		if (departmentManager == null) {
			departmentManager = context.getBean(DepartmentManager.class);
			departmentManager.setJobOutput(getJobOutput());
		}
		return departmentManager;
	}

	private ExternalFileChecker getExternalFileChecker() {
		if (externalFileChecker == null) {
			externalFileChecker = context.getBean(ExternalFileChecker.class);
			externalFileChecker.setJobOutput(getJobOutput());
		}
		return externalFileChecker;
	}

	private FileDecryptInitializer getFileDecryptInitializer() {
		if (fileDecryptInitializer == null) {
			fileDecryptInitializer = context.getBean(FileDecryptInitializer.class);
			fileDecryptInitializer.setJobOutput(getJobOutput());
		}
		return fileDecryptInitializer;
	}

	private CsvExporter getFileExporter() {
		if (csvExporter == null) {
			csvExporter = context.getBean(CsvExporter.class);
			csvExporter.setJobOutput(getJobOutput());
		}
		return csvExporter;
	}

	private FileSystemLoader getFileSystemLoader() {
		if (fileSystemLoader == null) {
			fileSystemLoader = context.getBean(FileSystemLoader.class);
			fileSystemLoader.setJobOutput(getJobOutput());
		}
		return fileSystemLoader;
	}

	private JobOutput getJobOutput() {
		if (jobOutput == null) {
			jobOutput = context.getBean(JobOutput.class);
		}
		return jobOutput;
	}

	private JobOutput initJob(CommandLine line) throws Exception {
		Long jobId = getJobIdOptionValue(line, false);
		if (jobId != null) {
			getJobOutput().setAuthentication(getAuthenticationOptionValue(line));
		} else {
			getJobOutput().setAuthentication(null);
		}
		jobOutput.setJobId(jobId);
		return jobOutput;
	}

	private JournalPurger getJournalPurger() {
		if (journalPurger == null) {
			journalPurger = context.getBean(JournalPurger.class);
			journalPurger.setJobOutput(getJobOutput());
		}
		return journalPurger;
	}

	private JournalSystemMessageCodeInitializer getJournalSystemMessageCodeInitializer() {
		if (journalSystemMessageCodeInitializer == null) {
			journalSystemMessageCodeInitializer = context.getBean(JournalSystemMessageCodeInitializer.class);
			journalSystemMessageCodeInitializer.setJobOutput(getJobOutput());
		}
		return journalSystemMessageCodeInitializer;
	}

	private MassMailSender getMassMailSender() {
		if (massMailSender == null) {
			massMailSender = context.getBean(MassMailSender.class);
			massMailSender.setJobOutput(getJobOutput());
		}
		return massMailSender;
	}

	private NotificationSender getNotificationSender() {
		if (notificationSender == null) {
			notificationSender = context.getBean(NotificationSender.class);
			notificationSender.setJobOutput(getJobOutput());
		}
		return notificationSender;
	}

	private ProbandCommentFieldInitializer getProbandCommentFieldInitializer() {
		if (probandCommentFieldInitializer == null) {
			probandCommentFieldInitializer = context.getBean(ProbandCommentFieldInitializer.class);
			probandCommentFieldInitializer.setJobOutput(getJobOutput());
		}
		return probandCommentFieldInitializer;
	}

	private ProbandImageFieldInitializer getProbandImageFieldInitializer() {
		if (probandImageFieldInitializer == null) {
			probandImageFieldInitializer = context.getBean(ProbandImageFieldInitializer.class);
			probandImageFieldInitializer.setJobOutput(getJobOutput());
		}
		return probandImageFieldInitializer;
	}

	private ProbandListStatusEntryReasonDecryptInitializer getProbandListStatusEntryReasonDecryptInitializer() {
		if (probandListStatusEntryReasonDecryptInitializer == null) {
			probandListStatusEntryReasonDecryptInitializer = context.getBean(ProbandListStatusEntryReasonDecryptInitializer.class);
			probandListStatusEntryReasonDecryptInitializer.setJobOutput(getJobOutput());
		}
		return probandListStatusEntryReasonDecryptInitializer;
	}

	private ProductionDataProvider getProductionDataProvider() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (productionDataProvider == null) {
			String className = ExecSettings.getString(ExecSettingCodes.DATA_PROVIDER_CLASS, ExecDefaultSettings.DATA_PROVIDER_CLASS);
			if (CommonUtil.isEmptyString(className)) {
				productionDataProvider = context.getBean(ProductionDataProvider.class);
			} else {
				productionDataProvider = (ProductionDataProvider) Compile
						.loadClass(className, ExecSettings.getStringList(ExecSettingCodes.DATA_PROVIDER_SOURCE_FILES, ExecDefaultSettings.DATA_PROVIDER_SOURCE_FILES))
						.newInstance();
			}
			productionDataProvider.setJobOutput(getJobOutput());
		}
		return productionDataProvider;
	}

	private ServiceMethods getServiceMethods() {
		if (serviceMethods == null) {
			serviceMethods = context.getBean(ServiceMethods.class);
			serviceMethods.setJobOutput(getJobOutput());
		}
		return serviceMethods;
	}

	private XlsExporter getXlsExporter() {
		if (xlsExporter == null) {
			xlsExporter = context.getBean(XlsExporter.class);
			xlsExporter.setJobOutput(getJobOutput());
		}
		return xlsExporter;
	}

	private XlsImporter getXlsImporter() {
		if (xlsImporter == null) {
			xlsImporter = context.getBean(XlsImporter.class);
			xlsImporter.setJobOutput(getJobOutput());
		}
		return xlsImporter;
	}

	private void initContext() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(new String[] { "/applicationContext-exec.xml" });
		}
	}

	private boolean testForced(CommandLine line, String promptMsg) {
		if (!line.hasOption(DBToolOptions.FORCE_OPT)) {
			if (ExecUtil.confirmationPrompt(promptMsg)) {
				return true;
			}
		} else {
			getJobOutput().println(promptMsg + " (force option applied)");
			return true;
		}
		return false;
	}

	private boolean testOverwriteFile(CommandLine line, String fileName) {
		java.io.File file = new java.io.File(fileName);
		if (file.exists() && !testForced(line, fileName + " exists and will be overwritten!")) {
			return false;
		}
		return true;
	}
}
