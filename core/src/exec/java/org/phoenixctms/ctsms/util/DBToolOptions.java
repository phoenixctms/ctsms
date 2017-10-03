package org.phoenixctms.ctsms.util;

import java.util.HashMap;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public final class DBToolOptions {

	public static final String VARIABLE_PERIOD_OPT = "vp";
	public static final String PROBAND_LIST_STATUS_LOG_LEVEL_OPT = "ll";
	public static final String VARIABLE_PERIOD_EXPLICIT_DAYS_OPT = "ed";
	public static final String ENCODING_OPT = "e";
	public static final String FORCE_OPT = "f";
	public static final String DEPARTMENT_L10N_KEY_OPT = "dlk";
	public static final String DEPARTMENT_PASSWORD_OPT = "dp";
	public static final String OLD_DEPARTMENT_PASSWORD_OPT = "odp";
	public static final String NEW_DEPARTMENT_PASSWORD_OPT = "ndp";
	public static final String USERNAME_OPT = "u";
	public static final String PASSWORD_OPT = "p";
	public static final String ID_OPT = "id";
	public static final String LIMIT_OPT = "l";
	public static final String FLUSH_REVISION_OPT = "fr";
	public static final String EMAIL_RECIPIENTS_OPT = "er";
	public static final String EMAIL_RECIPIENTS_IF_COUNT_GT_ZERO_OPT = "ericgtz";
	public static final String PROPERTIES_FILE_OPT = "pf";
	public static final String HELP_OPT = "h";
	public static final String PERMISSION_PROFILES_OPT = "pp";
	public static final String ALPHA_ID_REVISION_OPT = "air";
	public static final String ASP_REVISION_OPT = "ar";
	public static final String ICD_SYSTEMATICS_REVISION_OPT = "isr";
	public static final String SYSTEMATICS_LANG_OPT = "sl";
	public static final String OPS_CODE_REVISION_OPT = "ocr";
	public static final String OPS_SYSTEMATICS_REVISION_OPT = "osr";
	public static final String IMPORT_MIME_INVENTORY_OPT = "imi";
	public static final String IMPORT_MIME_STAFF_OPT = "ims";
	public static final String IMPORT_MIME_COURSE_OPT = "imc";
	public static final String IMPORT_MIME_TRIAL_OPT = "imt";
	public static final String IMPORT_MIME_PROBAND_OPT = "imp";
	public static final String IMPORT_MIME_INPUT_FIELD_IMAGE_OPT = "imifi";
	public static final String IMPORT_MIME_STAFF_IMAGE_OPT = "imsi";
	public static final String IMPORT_MIME_PROBAND_IMAGE_OPT = "impi";
	public static final String IMPORT_ZIP_OPT = "iz";
	public static final String IMPORT_STREET_OPT = "is";
	public static final String IMPORT_TITLE_OPT = "it";
	public static final String IMPORT_COUNTRY_OPT = "ic";
	public static final String IMPORT_BANK_OPT = "ib";
	public static final String DELETE_MISSING_OPT = "dm";
	public static final String DELETE_ORPHANED_OPT = "do";
	public static final String SCAN_ORPHANED_OPT = "so";
	public static final String SCAN_MISSING_OPT = "sm";
	public static final String INIT_OPT = "i";
	public static final String CLEAN_OPT = "c";
	// public static final String INIT_CRITERIA_TABLES_OPT = "ict";
	// public static final String CLEAR_CRITERIA_TABLES_OPT = "cct";
	public static final String IMPORT_CRITERION_PROPERTIES_OPT = "icp";
	public static final String CHANGE_DEPARTMENT_PASSWORD_INTERACTIVE_OPT = "cdpi";
	public static final String CREATE_DEPARTMENT_INTERACTIVE_OPT = "cdi";
	public static final String CREATE_USER_INTERACTIVE_OPT = "cui";
	public static final String CHANGE_DEPARTMENT_PASSWORD_OPT = "cdp";
	public static final String CHANGE_DEPARTMENT_PASSWORD_USER_OPT = "cdpu";
	public static final String CREATE_DEPARTMENT_OPT = "cd";
	public static final String CREATE_USER_OPT = "cu";
	public static final String LOAD_DEMO_DATA_OPT = "ldd";
	//public static final String CREATE_DEMO_INPUT_FIELDS_OPT1 = "cdif";
	public static final String IMPORT_INVENTORY_DOCUMENT_FILES_OPT = "iidf";
	public static final String IMPORT_STAFF_DOCUMENT_FILES_OPT = "isdf";
	public static final String IMPORT_COURSE_DOCUMENT_FILES_OPT = "icdf";
	public static final String IMPORT_TRIAL_DOCUMENT_FILES_OPT = "itdf";
	public static final String IMPORT_PROBAND_DOCUMENT_FILES_OPT = "ipdf";
	// public static final String IMPORT_INPUT_FIELD_DOCUMENT_FILES_OPT = "iifdf";
	public static final String REMOVE_INVENTORY_DOCUMENT_FILES_OPT = "ridf";
	public static final String REMOVE_STAFF_DOCUMENT_FILES_OPT = "rsdf";
	public static final String REMOVE_COURSE_DOCUMENT_FILES_OPT = "rcdf";
	public static final String REMOVE_TRIAL_DOCUMENT_FILES_OPT = "rtdf";
	public static final String REMOVE_PROBAND_DOCUMENT_FILES_OPT = "rpdf";
	// public static final String REMOVE_INPUT_FIELD_DOCUMENT_FILES_OPT = "rifdf";
	public static final String PREPARE_NOTIFICATIONS_OPT = "pn";
	public static final String SEND_NOTIFICATIONS_OPT = "sn";
	public static final String REMOVE_PROBANDS_OPT = "rp";
	public static final String DELETE_INVENTORY_OPT = "idi";
	public static final String PERFORM_INVENTORY_DEFERRED_DELETE_OPT = "pdidi";
	public static final String DELETE_STAFF_OPT = "sds";
	public static final String PERFORM_STAFF_DEFERRED_DELETE_OPT = "pdsds";
	public static final String DELETE_COURSE_OPT = "cdc";
	public static final String PERFORM_COURSE_DEFERRED_DELETE_OPT = "pdcdc";
	public static final String DELETE_TRIAL_OPT = "tdt";
	public static final String PERFORM_TRIAL_DEFERRED_DELETE_OPT = "pdtdt";
	public static final String DELETE_INQUIRY_OPT = "tdi";
	public static final String PERFORM_INQUIRY_DEFERRED_DELETE_OPT = "pdtdi";
	public static final String DELETE_ECRF_OPT = "tde";
	public static final String PERFORM_ECRF_DEFERRED_DELETE_OPT = "pdtde";
	public static final String DELETE_ECRF_FIELD_OPT = "tdef";
	public static final String PERFORM_ECRF_FIELD_DEFERRED_DELETE_OPT = "pdtdef";
	public static final String DELETE_PROBAND_OPT = "pdp";
	public static final String PERFORM_PROBAND_DEFERRED_DELETE_OPT = "pdpdp";
	public static final String DELETE_USER_OPT = "udu";
	public static final String PERFORM_USER_DEFERRED_DELETE_OPT = "pdudu";
	public static final String DELETE_INPUT_FIELD_OPT = "ifdif";
	public static final String PERFORM_INPUT_FIELD_DEFERRED_DELETE_OPT = "pdifdif";
	public static final String DELETE_SELECTION_SET_VALUE_OPT = "ifdssv";
	public static final String PERFORM_SELECTION_SET_VALUE_DEFERRED_DELETE_OPT = "pdifdssv";
	public static final String DELETE_CRITERIA_OPT = "sdc";
	public static final String PERFORM_CRITERIA_DEFERRED_DELETE_OPT = "pdsdc";
	public static final String SCAN_ALL_DEFERRED_DELETE_OPT = "sda";
	public static final String PERFORM_ALL_DEFERRED_DELETE_OPT = "pda";
	public static final String IMPORT_PERMISSION_DEFINITIONS_OPT = "ipd";
	public static final String EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT = "epdt";
	public final static String INITIALIZE_PROBAND_IMAGE_FIELDS_OPT = "ipif";
	public final static String INITIALIZE_ENCRYPTED_TRIAL_DOCUMENT_FILES_OPT = "ietdf";
	public final static String INITIALIZE_PROBAND_COMMENT_FIELDS_OPT = "ipcf";
	public static final String IMPORT_ALPHA_IDS_OPT = "iai";
	public static final String IMPORT_ICD_SYSTEMATICS_OPT = "iis";
	public static final String IMPORT_OPS_CODES_OPT = "ioc";
	public static final String IMPORT_OPS_SYSTEMATICS_OPT = "ios";
	public static final String PURGE_INVENTORY_JOURNAL = "pij";
	public static final String PURGE_STAFF_JOURNAL = "psj";
	public static final String PURGE_COURSE_JOURNAL = "pcj";
	public static final String PURGE_TRIAL_JOURNAL = "ptj";
	public static final String PURGE_PROBAND_JOURNAL = "ppj";
	public static final String PURGE_INPUT_FIELD_JOURNAL = "pifj";
	public static final String PURGE_USER_JOURNAL = "puj";
	public static final String PURGE_CRITERIA_JOURNAL = "pscj";
	public static final String EXPORT_CRITERIA_RESULT_OPT = "ecr";
	public static final String EXPORT_PROBAND_LIST_OPT = "epl";
	public static final String EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT = "eccpl";
	public static final String EXPORT_CRITERIA_CVS_OPT = "eccv";
	public static final String EXPORT_CRITERIA_PROBAND_LETTERS_OPT = "ecpl";
	public static final String EXPORT_ECRF_PDFS_OPT = "eep";
	public static final String EXPORT_AUDIT_TRAIL_OPT = "eat";
	public static final String IMPORT_INPUT_FIELDS_OPT = "iif";
	public static final String EXPORT_INPUT_FIELD_OPT = "eif";
	public static final String EXPORT_ECRFS_OPT = "ee";
	public static final String IMPORT_ECRFS_OPT = "ie";
	public static final String IMPORT_ASPS_OPT = "ia";
	public static final String VALIDATE_PENDING_ECRFS_OPT = "vpe";
	public static final String EXPORT_INVENTORY_JOURNAL_OPT = "eij";
	public static final String EXPORT_STAFF_JOURNAL_OPT = "esj";
	public static final String EXPORT_COURSE_JOURNAL_OPT = "ecj";
	public static final String EXPORT_TRIAL_JOURNAL_OPT = "etj";
	public static final String EXPORT_PROBAND_JOURNAL_OPT = "epj";
	public static final String EXPORT_INPUT_FIELD_JOURNAL_OPT = "eifj";
	public static final String EXPORT_USER_JOURNAL_OPT = "euj";
	public static final String EXPORT_CRITERIA_JOURNAL_OPT = "escj";
	public static final String EXPORT_PROBAND_APPOINTMENTS_OPT = "epa";
	private final static HashMap<String, Option> taskOptionMap = new HashMap<String, Option>();
	private final static HashMap<String, Option> optionalOptionMap = new HashMap<String, Option>();
	public final static Options options = new Options();
	static {
		OptionGroup tasks = new OptionGroup();
		tasks.addOption(registerTaskOption(CLEAN_OPT, "clear", "clear database", 0));
		tasks.addOption(registerTaskOption(INIT_OPT, "init", "initialize db by insertig required setup records", 0));
		// tasks.addOption(registerTaskOption(CLEAR_CRITERIA_TABLES_OPT,"clear_criteria_tables","clear criteria tables",0));
		// tasks.addOption(registerTaskOption(INIT_CRITERIA_TABLES_OPT,"init_criteria_tables","initialize criteria tables",0));
		tasks.addOption(registerTaskOption(IMPORT_CRITERION_PROPERTIES_OPT, "import_criterion_properties", "import criterion properties tables", 1));
		tasks.addOption(registerTaskOption(SCAN_MISSING_OPT, "scan_missing", "scan for missing external files", 0));
		tasks.addOption(registerTaskOption(SCAN_ORPHANED_OPT, "scan_orphaned", "scan for orphaned external files", 0));
		tasks.addOption(registerTaskOption(DELETE_MISSING_OPT, "delete_missing", "scan for missing external files and delete references from DB", 0));
		tasks.addOption(registerTaskOption(DELETE_ORPHANED_OPT, "delete_orphaned", "scan for orphaned external files and delete them", 0));
		tasks.addOption(registerTaskOption(IMPORT_BANK_OPT, "import_bank", "import bank identifications from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_COUNTRY_OPT, "import_country", "import country names from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_TITLE_OPT, "import_title", "import titles from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_STREET_OPT, "import_street", "import street names from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_ZIP_OPT, "import_zip", "import zip codes from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_INVENTORY_OPT, "import_mime_inventory", "import inventory file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_STAFF_OPT, "import_mime_staff", "import staff file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_COURSE_OPT, "import_mime_course", "import course file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_TRIAL_OPT, "import_mime_trial", "import trial file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_PROBAND_OPT, "import_mime_proband", "import proband file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_INPUT_FIELD_IMAGE_OPT, "import_mime_input_field_image", "import input field image file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_STAFF_IMAGE_OPT, "import_mime_staff_image", "import input staff image file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(IMPORT_MIME_PROBAND_IMAGE_OPT, "import_mime_input_field_image", "import proband image file mime types from csv/text file", 1));
		tasks.addOption(registerTaskOption(HELP_OPT, "help", "print help", 0));
		tasks.addOption(registerTaskOption(CHANGE_DEPARTMENT_PASSWORD_INTERACTIVE_OPT, "change_department_password_interactive", "change department password (interactive)", 0));
		tasks.addOption(registerTaskOption(CREATE_DEPARTMENT_INTERACTIVE_OPT, "create_department_interactive", "create new department (interactive)", 0));
		tasks.addOption(registerTaskOption(CREATE_USER_INTERACTIVE_OPT, "create_user_interactive", "create new user (interactive)", 0));
		tasks.addOption(registerTaskOption(CHANGE_DEPARTMENT_PASSWORD_OPT, "change_department_password", "change department password", 0));
		tasks.addOption(registerTaskOption(CHANGE_DEPARTMENT_PASSWORD_USER_OPT, "change_department_password_user", "change department password (user authentication)", 0));
		tasks.addOption(registerTaskOption(CREATE_DEPARTMENT_OPT, "create_department", "create new department", 0));
		tasks.addOption(registerTaskOption(CREATE_USER_OPT, "create_user", "create new user", 0));
		tasks.addOption(registerTaskOption(LOAD_DEMO_DATA_OPT, "load_demo_data", "load db with demo data records", 0));
		//tasks.addOption(registerTaskOption(CREATE_DEMO_INPUT_FIELDS_OPT, "create_demo_input_fields", "create demo input fields", 0));
		tasks.addOption(registerTaskOption(IMPORT_INVENTORY_DOCUMENT_FILES_OPT, "import_inventory_document_files", "import documents and files for an inventory entity", 1));
		tasks.addOption(registerTaskOption(IMPORT_STAFF_DOCUMENT_FILES_OPT, "import_staff_documents_files", "import documents and files for a staff entity", 1));
		tasks.addOption(registerTaskOption(IMPORT_COURSE_DOCUMENT_FILES_OPT, "import_course_documents_files", "import documents and files for a course entity", 1));
		tasks.addOption(registerTaskOption(IMPORT_TRIAL_DOCUMENT_FILES_OPT, "import_trial_documents_files", "import documents and files for a trial entity", 1));
		tasks.addOption(registerTaskOption(IMPORT_PROBAND_DOCUMENT_FILES_OPT, "import_proband_documents_files", "import documents and files for a proband entity", 1));
		// tasks.addOption(registerTaskOption(IMPORT_INPUT_FIELD_DOCUMENT_FILES_OPT,"import_input_field_documents_files","import documents and files for an input field entity",1));
		tasks.addOption(registerTaskOption(REMOVE_INVENTORY_DOCUMENT_FILES_OPT, "remove_inventory_documents_files", "remove all files of an inventory entity", 0));
		tasks.addOption(registerTaskOption(REMOVE_STAFF_DOCUMENT_FILES_OPT, "remove_staff_documents_files", "remove all files of a staff entity", 0));
		tasks.addOption(registerTaskOption(REMOVE_COURSE_DOCUMENT_FILES_OPT, "remove_course_documents_files", "remove all files of a course entity", 0));
		tasks.addOption(registerTaskOption(REMOVE_TRIAL_DOCUMENT_FILES_OPT, "remove_trial_documents_files", "remove all files of a trial entity", 0));
		tasks.addOption(registerTaskOption(REMOVE_PROBAND_DOCUMENT_FILES_OPT, "remove_proband_documents_files", "remove all files of a proband entity", 0));
		// tasks.addOption(registerTaskOption(REMOVE_INPUT_FIELD_DOCUMENT_FILES_OPT,"remove_input_field_documents_files","remove all files of a proband entity",0));
		tasks.addOption(registerTaskOption(PREPARE_NOTIFICATIONS_OPT, "prepare_notifications", "prepare notifications", 0));
		tasks.addOption(registerTaskOption(SEND_NOTIFICATIONS_OPT, "send_notifications", "send notifications", 0));
		tasks.addOption(registerTaskOption(REMOVE_PROBANDS_OPT, "remove_probands", "remove probands pending for auto-delete", 0));
		tasks.addOption(registerTaskOption(DELETE_INVENTORY_OPT, "delete_inventory", "delete inventory", 0));
		tasks.addOption(registerTaskOption(PERFORM_INVENTORY_DEFERRED_DELETE_OPT, "perform_deferred_delete_inventory", "perform deferred delete of inventory items", 0));
		tasks.addOption(registerTaskOption(DELETE_STAFF_OPT, "delete_staff", "delete staff", 0));
		tasks.addOption(registerTaskOption(PERFORM_STAFF_DEFERRED_DELETE_OPT, "perform_deferred_delete_staff", "perform deferred delete of staff items", 0));
		tasks.addOption(registerTaskOption(DELETE_COURSE_OPT, "delete_course", "delete course", 0));
		tasks.addOption(registerTaskOption(PERFORM_COURSE_DEFERRED_DELETE_OPT, "perform_deferred_delete_course", "perform deferred delete of course items", 0));
		tasks.addOption(registerTaskOption(DELETE_TRIAL_OPT, "delete_trial", "delete trial", 0));
		tasks.addOption(registerTaskOption(PERFORM_TRIAL_DEFERRED_DELETE_OPT, "perform_deferred_delete_trial", "perform deferred delete of trial items", 0));
		tasks.addOption(registerTaskOption(DELETE_INQUIRY_OPT, "delete_inquiry", "delete inquiry", 0));
		tasks.addOption(registerTaskOption(PERFORM_INQUIRY_DEFERRED_DELETE_OPT, "perform_deferred_delete_inquiry", "perform deferred delete of inquiry items", 0));
		tasks.addOption(registerTaskOption(DELETE_ECRF_OPT, "delete_ecrf", "delete eCRF", 0));
		tasks.addOption(registerTaskOption(PERFORM_ECRF_DEFERRED_DELETE_OPT, "perform_deferred_delete_ecrf", "perform deferred delete of eCRF items", 0));
		tasks.addOption(registerTaskOption(DELETE_ECRF_FIELD_OPT, "delete_ecrf_field", "delete eCRF field", 0));
		tasks.addOption(registerTaskOption(PERFORM_ECRF_FIELD_DEFERRED_DELETE_OPT, "perform_deferred_delete_ecrf_field", "perform deferred delete of eCRF field items", 0));
		tasks.addOption(registerTaskOption(DELETE_PROBAND_OPT, "delete_proband", "delete proband", 0));
		tasks.addOption(registerTaskOption(PERFORM_PROBAND_DEFERRED_DELETE_OPT, "perform_deferred_delete_proband", "perform deferred delete of proband items", 0));
		tasks.addOption(registerTaskOption(DELETE_USER_OPT, "delete_user", "delete user", 0));
		tasks.addOption(registerTaskOption(PERFORM_USER_DEFERRED_DELETE_OPT, "perform_deferred_delete_user", "perform deferred delete of user items", 0));
		tasks.addOption(registerTaskOption(DELETE_INPUT_FIELD_OPT, "delete_input_field", "delete input field", 0));
		tasks.addOption(registerTaskOption(PERFORM_INPUT_FIELD_DEFERRED_DELETE_OPT, "perform_deferred_delete_input_field", "perform deferred delete of input field items", 0));
		tasks.addOption(registerTaskOption(DELETE_SELECTION_SET_VALUE_OPT, "delete_selection_set_value", "delete selection set value", 0));
		tasks.addOption(registerTaskOption(PERFORM_SELECTION_SET_VALUE_DEFERRED_DELETE_OPT, "perform_deferred_delete_selection_set_value",
				"perform deferred delete of selection set value items", 0));
		tasks.addOption(registerTaskOption(DELETE_CRITERIA_OPT, "delete_criteria", "delete criteria", 0));
		tasks.addOption(registerTaskOption(PERFORM_CRITERIA_DEFERRED_DELETE_OPT, "perform_deferred_delete_criteria", "perform deferred delete of criteria items", 0));
		tasks.addOption(registerTaskOption(SCAN_ALL_DEFERRED_DELETE_OPT, "scan_deferred_delete_all", "scan deferred delete of all entity items", 0));
		tasks.addOption(registerTaskOption(PERFORM_ALL_DEFERRED_DELETE_OPT, "perform_deferred_delete_all", "perform deferred delete of all items", 0));
		tasks.addOption(registerTaskOption(IMPORT_PERMISSION_DEFINITIONS_OPT, "import_permission_definitions", "import permission definitions from csv/text file", 1));
		tasks.addOption(registerTaskOption(EXPORT_PERMISSION_DEFINITION_TEMPLATE_OPT, "export_permission_definition_template",
				"export permission definition template as csv/text file", 1));
		tasks.addOption(registerTaskOption(INITIALIZE_PROBAND_IMAGE_FIELDS_OPT, "initialize_proband_image_fields", "initialize proband image fields", 0));
		tasks.addOption(registerTaskOption(INITIALIZE_ENCRYPTED_TRIAL_DOCUMENT_FILES_OPT, "initialize_encrypted_trial_documents_files",
				"initialize encrypted trial documents and files", 0));
		tasks.addOption(registerTaskOption(INITIALIZE_PROBAND_COMMENT_FIELDS_OPT, "initialize_proband_comment_fields", "initialize proband comment fields", 0));
		tasks.addOption(registerTaskOption(IMPORT_ICD_SYSTEMATICS_OPT, "import_icd_systematics", "import icd systematics", 1));
		tasks.addOption(registerTaskOption(IMPORT_ALPHA_IDS_OPT, "import_alpha_ids", "import alpha ids", 1));
		tasks.addOption(registerTaskOption(IMPORT_OPS_SYSTEMATICS_OPT, "import_ops_systematics", "import ops systematics", 1));
		tasks.addOption(registerTaskOption(IMPORT_OPS_CODES_OPT, "import_ops_codes", "import ops codes", 1));
		tasks.addOption(registerTaskOption(PURGE_INVENTORY_JOURNAL, "purge_inventory_journal", "purge old inventory journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_STAFF_JOURNAL, "purge_staff_journal", "purge old staff journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_COURSE_JOURNAL, "purge_course_journal", "purge old course journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_TRIAL_JOURNAL, "purge_trial_journal", "purge old trial journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_PROBAND_JOURNAL, "purge_proband_journal", "purge old proband journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_INPUT_FIELD_JOURNAL, "purge_input_field_journal", "purge old input field journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_USER_JOURNAL, "purge_user_journal", "purge old user journal records", 0));
		tasks.addOption(registerTaskOption(PURGE_CRITERIA_JOURNAL, "purge_criteria_journal", "purge old criteria journal records", 0));
		tasks.addOption(registerTaskOption(EXPORT_CRITERIA_RESULT_OPT, "export_criteria_result",
				"export search results", 1));
		tasks.addOption(registerTaskOption(EXPORT_PROBAND_LIST_OPT, "export_proband_list",
				"export a trial's proband list", 1));
		tasks.addOption(registerTaskOption(EXPORT_CRITERIA_COURSE_PARTICIPANT_LISTS_OPT, "export_criteria_course_participant_lists",
				"export course participant lists by criteria", 1));
		tasks.addOption(registerTaskOption(EXPORT_CRITERIA_CVS_OPT, "export_criteria_cvs",
				"export CVs by criteria", 1));
		tasks.addOption(registerTaskOption(EXPORT_CRITERIA_PROBAND_LETTERS_OPT, "export_criteria_proband_letters",
				"export proband letters by criteria", 1));
		tasks.addOption(registerTaskOption(EXPORT_ECRF_PDFS_OPT, "export_ecrf_pdfs",
				"export eCRFs by trial", 1));
		// "export eCRFs by proband list entry", 1));
		tasks.addOption(registerTaskOption(EXPORT_AUDIT_TRAIL_OPT, "export_audit_trail",
				"export eCRF audit trail", 1));
		tasks.addOption(registerTaskOption(IMPORT_INPUT_FIELDS_OPT, "import_input_fields",
				"import input fields and selection set values", 1));
		tasks.addOption(registerTaskOption(EXPORT_INPUT_FIELD_OPT, "export_input_field",
				"export input field and selection set values", 1));

		tasks.addOption(registerTaskOption(IMPORT_ECRFS_OPT, "import_ecrfs",
				"import eCRFs, eCRF fields, input fields and selection set values", 1));
		tasks.addOption(registerTaskOption(EXPORT_ECRFS_OPT, "export_ecrfs",
				"export eCRFs, eCRF fields, input fields and selection set values", 1));
		tasks.addOption(registerTaskOption(IMPORT_ASPS_OPT, "import_asps",
				"import asp and substances", 1));
		tasks.addOption(registerTaskOption(VALIDATE_PENDING_ECRFS_OPT, "validate_pending_ecrfs", "validate pending ecrfs", 0));
		tasks.addOption(registerTaskOption(EXPORT_INVENTORY_JOURNAL_OPT, "export_inventory_journal",
				"export inventory journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_STAFF_JOURNAL_OPT, "export_staff_journal",
				"export staff journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_COURSE_JOURNAL_OPT, "export_course_journal",
				"export course journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_TRIAL_JOURNAL_OPT, "export_trial_journal",
				"export trial journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_PROBAND_JOURNAL_OPT, "export_proband_journal",
				"export proband journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_INPUT_FIELD_JOURNAL_OPT, "export_input_field_journal",
				"export input field journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_USER_JOURNAL_OPT, "export_user_journal",
				"export user journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_CRITERIA_JOURNAL_OPT, "export_criteria_journal",
				"export criteria journal", 1));
		tasks.addOption(registerTaskOption(EXPORT_PROBAND_APPOINTMENTS_OPT, "export_proband_appointments",
				"export proband inventory bookings of trials", 1));
		tasks.setRequired(true);
		options.addOptionGroup(tasks);
		options.addOption(registerOptionalOption(FORCE_OPT, "force", "skip confirmation promt", 0));
		options.addOption(registerOptionalOption(EMAIL_RECIPIENTS_OPT, "email_recipients", "send output to email recipients", 1));
		options.addOption(registerOptionalOption(EMAIL_RECIPIENTS_IF_COUNT_GT_ZERO_OPT, "email_recipients_if_count_gt_zero",
				"send output to email recipients, if number of processed items is greater than zero", 1));
		options.addOption(registerOptionalOption(PROPERTIES_FILE_OPT, "properties_file",
				"use a explicit properties file", 1));
		options.addOption(registerOptionalOption(DEPARTMENT_L10N_KEY_OPT, "department_l10n_key", "department l10n name", 1));
		options.addOption(registerOptionalOption(LIMIT_OPT, "limit", "limit for number of (processed) records", 1));
		options.addOption(registerOptionalOption(FLUSH_REVISION_OPT, "flush_revision", "flush alpha id, ops code, asp, asp, icd or ops systematics records prior to import",
				0));
		options.addOption(registerOptionalOption(ENCODING_OPT, "encoding", "encoding of csv/text file to import", 1));
		options.addOption(registerOptionalOption(USERNAME_OPT, "username", "username", 1));
		options.addOption(registerOptionalOption(PASSWORD_OPT, "password", "user password", 1));
		options.addOption(registerOptionalOption(PERMISSION_PROFILES_OPT, "permission_profiles", "list of permission profiles", 1));
		options.addOption(registerOptionalOption(ID_OPT, "entity_id", "record id of entity", 1));
		options.addOption(registerOptionalOption(DEPARTMENT_PASSWORD_OPT, "department_password", "department password", 1));
		options.addOption(registerOptionalOption(OLD_DEPARTMENT_PASSWORD_OPT, "old_department_password", "old department password", 1));
		options.addOption(registerOptionalOption(NEW_DEPARTMENT_PASSWORD_OPT, "new_department_password", "new department password", 1));
		options.addOption(registerOptionalOption(ALPHA_ID_REVISION_OPT, "alpha_id_revision", "alpha id catalogue revision", 1));
		options.addOption(registerOptionalOption(ICD_SYSTEMATICS_REVISION_OPT, "icd_systematics_revision", "icd systematics catalogue revision", 1));
		options.addOption(registerOptionalOption(SYSTEMATICS_LANG_OPT, "systematics_lang", "icd/ops systematics label language", 1));
		options.addOption(registerOptionalOption(OPS_CODE_REVISION_OPT, "ops_code_revision", "ops catalogue revision", 1));
		options.addOption(registerOptionalOption(OPS_SYSTEMATICS_REVISION_OPT, "ops_systematics_revision", "ops systematics catalogue revision", 1));
		options.addOption(registerOptionalOption(OPS_SYSTEMATICS_REVISION_OPT, "ops_systematics_revision", "ops systematics catalogue revision", 1));
		options.addOption(registerOptionalOption(VARIABLE_PERIOD_OPT, "period", "time period", 1));
		options.addOption(registerOptionalOption(VARIABLE_PERIOD_EXPLICIT_DAYS_OPT, "period_explicit_days", "time period in days", 1));
		options.addOption(registerOptionalOption(PROBAND_LIST_STATUS_LOG_LEVEL_OPT, "proband_list_status_log_level", "proband list status log level", 1));
		options.addOption(registerOptionalOption(ASP_REVISION_OPT, "asp_revision", "asp and substances catalogue revision", 1));
	}

	public static Option getTask(String opt) {
		return taskOptionMap.get(opt);
	}

	private static Option registerOptionalOption(String opt, String longOpt, String description, int numArgs) {
		Option option = OptionBuilder.create(opt);
		option.setRequired(false);
		option.setDescription("option: " + description);
		option.setLongOpt(longOpt);
		option.setArgs(numArgs);
		optionalOptionMap.put(opt, option);
		return option;
	}

	private static Option registerTaskOption(String opt, String longOpt, String description, int numArgs) {
		Option option = OptionBuilder.create(opt);
		option.setRequired(false);
		option.setDescription("task: " + description);
		option.setLongOpt(longOpt);
		option.setArgs(numArgs);
		taskOptionMap.put(opt, option);
		return option;
	}

	private DBToolOptions() {
	}
}
