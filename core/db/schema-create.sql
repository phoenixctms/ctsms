
    create table ADDRESS_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        DELIVER_PRESET BOOLEAN not null,
        MAX_OCCURRENCE INTEGER,
        STAFF BOOLEAN not null,
        PROBAND BOOLEAN not null,
        ANIMAL BOOLEAN not null,
        primary key (ID)
    );

    create table ALPHA_ID (
        ID BIGINT not null,
        REVISION CHARACTER VARYING(1024),
        ALPHA_ID CHARACTER VARYING(1024) not null,
        VALID BOOLEAN not null,
        PRIMARY_CODE CHARACTER VARYING(1024),
        ASTERISK_CODE CHARACTER VARYING(1024),
        OPTIONAL_CODE CHARACTER VARYING(1024),
        TEXT CHARACTER VARYING(1024) not null,
        HASH BIGINT not null unique,
        SYSTEMATICS_FK BIGINT not null,
        primary key (ID)
    );

    create table ANIMAL_CONTACT_PARTICULARS (
        ID BIGINT not null,
        ANIMAL_NAME CHARACTER VARYING(1024),
        DATE_OF_BIRTH DATE,
        GENDER CHARACTER VARYING(1024),
        WIDTH BIGINT,
        HEIGHT BIGINT,
        FILE_NAME CHARACTER VARYING(1024),
        FILE_SIZE BIGINT,
        FILE_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        DATA BYTEA,
        COMMENT TEXT,
        ALIAS CHARACTER VARYING(1024),
        CONTENT_TYPE_FK BIGINT,
        primary key (ID)
    );

    create table ANNOUNCEMENT (
        ID BIGINT not null,
        MESSAGE CHARACTER VARYING(1024) not null,
        TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table ASP (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        LABELING CHARACTER VARYING(1024) not null,
        REGISTRATION_NUMBER CHARACTER VARYING(1024) not null,
        PROPRIETOR CHARACTER VARYING(1024),
        REGISTRATION_DATE DATE not null,
        NARCOTIC BOOLEAN,
        PSYCHOTROPIC BOOLEAN,
        BATCH_RELEASE BOOLEAN,
        BATCH_TESTING BOOLEAN,
        BATCH_TESTING_EXCLUSION BOOLEAN,
        PRESCRIPTION CHARACTER VARYING(1024),
        DISTRIBUTION CHARACTER VARYING(1024),
        HUMAN BOOLEAN not null,
        CATEGORY CHARACTER VARYING(1024),
        REVISION CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table ASP_ATC_CODE (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        REVISION CHARACTER VARYING(1024),
        ASP_FK BIGINT not null,
        primary key (ID)
    );

    create table ASP_SUBSTANCE (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        REVISION CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table BANK_ACCOUNT (
        ID BIGINT not null,
        ENCRYPTED_ACCOUNT_HOLDER_NAME BYTEA not null unique,
        ACCOUNT_HOLDER_NAME_IV BYTEA not null unique,
        ACCOUNT_HOLDER_NAME_HASH BYTEA,
        ENCRYPTED_ACCOUNT_NUMBER BYTEA not null unique,
        ACCOUNT_NUMBER_IV BYTEA not null unique,
        ACCOUNT_NUMBER_HASH BYTEA,
        ENCRYPTED_BANK_CODE_NUMBER BYTEA not null unique,
        BANK_CODE_NUMBER_IV BYTEA not null unique,
        BANK_CODE_NUMBER_HASH BYTEA,
        ENCRYPTED_IBAN BYTEA not null unique,
        IBAN_IV BYTEA not null unique,
        IBAN_HASH BYTEA,
        ENCRYPTED_BIC BYTEA not null unique,
        BIC_IV BYTEA not null unique,
        BIC_HASH BYTEA,
        ENCRYPTED_BANK_NAME BYTEA not null unique,
        BANK_NAME_IV BYTEA not null unique,
        BANK_NAME_HASH BYTEA,
        ACTIVE BOOLEAN not null,
        NA BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        primary key (ID)
    );

    create table BANK_IDENTIFICATION (
        ID BIGINT not null,
        BANK_NAME CHARACTER VARYING(1024) not null,
        BANK_CODE_NUMBER CHARACTER VARYING(1024) not null,
        BIC CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table CONTACT_DETAIL_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        EMAIL BOOLEAN not null,
        PHONE BOOLEAN not null,
        URL BOOLEAN not null,
        NOTIFY_PRESET BOOLEAN not null,
        MAX_OCCURRENCE INTEGER,
        REG_EXP CHARACTER VARYING(1024),
        MISMATCH_MSG_L10N_KEY CHARACTER VARYING(1024),
        STAFF BOOLEAN not null,
        PROBAND BOOLEAN not null,
        ANIMAL BOOLEAN not null,
        BUSINESS BOOLEAN not null,
        primary key (ID)
    );

    create table COUNTRY (
        ID BIGINT not null,
        COUNTRY_NAME CHARACTER VARYING(1024) not null,
        COUNTRY_CODE CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table COURSE (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        DESCRIPTION TEXT,
        SELF_REGISTRATION BOOLEAN not null,
        MAX_NUMBER_OF_PARTICIPANTS BIGINT,
        PARTICIPATION_DEADLINE TIMESTAMP WITHOUT TIME ZONE,
        START DATE,
        STOP DATE not null,
        CV_TITLE CHARACTER VARYING(1024),
        SHOW_CV_PRESET BOOLEAN not null,
        CV_COMMENT_PRESET TEXT,
        SHOW_COMMENT_CV_PRESET BOOLEAN not null,
        SHOW_TRAINING_RECORD_PRESET BOOLEAN not null,
        SHOW_COMMENT_TRAINING_RECORD_PRESET BOOLEAN not null,
        CERTIFICATE BOOLEAN not null,
        EXPIRES BOOLEAN not null,
        VALIDITY_PERIOD CHARACTER VARYING(1024),
        VALIDITY_PERIOD_DAYS BIGINT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        DEPARTMENT_FK BIGINT not null,
        CATEGORY_FK BIGINT not null,
        TRIAL_FK BIGINT,
        INSTITUTION_FK BIGINT,
        CV_SECTION_PRESET_FK BIGINT,
        TRAINING_RECORD_SECTION_PRESET_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table COURSE_CATEGORY (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COLOR CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        TRIAL_REQUIRED BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table COURSE_PARTICIPATION_STATUS_ENTRY (
        ID BIGINT not null,
        COMMENT TEXT,
        SHOW_CV BOOLEAN not null,
        SHOW_COMMENT_CV BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        SHOW_TRAINING_RECORD BOOLEAN not null,
        SHOW_COMMENT_TRAINING_RECORD BOOLEAN not null,
        VERSION BIGINT not null,
        CV_SECTION_FK BIGINT,
        TRAINING_RECORD_SECTION_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        COURSE_FK BIGINT not null,
        STATUS_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        primary key (ID)
    );

    create table COURSE_PARTICIPATION_STATUS_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COLOR CHARACTER VARYING(1024) not null,
        USER_INITIAL BOOLEAN not null,
        USER_SELF_REGISTRATION_INITIAL BOOLEAN not null,
        ADMIN_INITIAL BOOLEAN not null,
        ADMIN_SELF_REGISTRATION_INITIAL BOOLEAN not null,
        ACKNOWLEDGE BOOLEAN not null,
        CANCEL BOOLEAN not null,
        PASS BOOLEAN not null,
        NOTIFY BOOLEAN not null,
        RELEVANT_FOR_COURSE_APPOINTMENTS BOOLEAN not null,
        primary key (ID)
    );

    create table CRITERIA (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        LABEL CHARACTER VARYING(1024) not null,
        CATEGORY CHARACTER VARYING(1024),
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        COMMENT TEXT,
        VERSION BIGINT not null,
        LOAD_BY_DEFAULT BOOLEAN not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table CRITERION (
        ID BIGINT not null,
        POSITION BIGINT not null,
        STRING_VALUE CHARACTER VARYING(1024),
        BOOLEAN_VALUE BOOLEAN,
        LONG_VALUE BIGINT,
        FLOAT_VALUE REAL,
        DATE_VALUE DATE,
        TIMESTAMP_VALUE TIMESTAMP WITHOUT TIME ZONE,
        TIME_VALUE TIME WITHOUT TIME ZONE,
        RESTRICTION CHARACTER VARYING(1024),
        TIE CHARACTER VARYING(1024),
        PROPERTY_NAME_L10N_KEY CHARACTER VARYING(1024),
        MODULE CHARACTER VARYING(1024),
        CRITERIA_FK BIGINT not null,
        primary key (ID)
    );

    create table CRITERION_PROPERTY (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        PROPERTY CHARACTER VARYING(1024) not null,
        VALUE_TYPE CHARACTER VARYING(1024) not null,
        SELECTION_SET_SERVICE_METHOD_NAME CHARACTER VARYING(1024),
        GET_NAME_METHOD_NAME CHARACTER VARYING(1024),
        GET_VALUE_METHOD_NAME CHARACTER VARYING(1024),
        COMPLETE_METHOD_NAME CHARACTER VARYING(1024),
        CONVERTER CHARACTER VARYING(1024),
        FILTER_ITEMS_METHOD_NAME CHARACTER VARYING(1024),
        PICKER CHARACTER VARYING(1024),
        ENTITY_NAME CHARACTER VARYING(1024),
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CRITERION_RESTRICTION (
        ID BIGINT not null,
        RESTRICTION CHARACTER VARYING(1024) not null unique,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CRITERION_TIE (
        ID BIGINT not null,
        TIE CHARACTER VARYING(1024) not null unique,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CV_POSITION (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024) not null,
        START DATE,
        STOP DATE,
        COMMENT TEXT,
        SHOW_CV BOOLEAN not null,
        SHOW_COMMENT_CV BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        INSTITUTION_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        SECTION_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        primary key (ID)
    );

    create table CV_SECTION (
        ID BIGINT not null,
        POSITION BIGINT not null unique,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        DESCRIPTION_L10N_KEY CHARACTER VARYING(1024),
        TITLE_PRESET_L10N_KEY CHARACTER VARYING(1024) not null,
        SHOW_CV_PRESET BOOLEAN not null,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table DATA_TABLE_COLUMN (
        ID BIGINT not null,
        TABLE_NAME CHARACTER VARYING(1024) not null,
        COLUMN_NAME CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        USER_FK BIGINT not null,
        primary key (ID)
    );

    create table DEPARTMENT (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        KEY_SALT BYTEA not null unique,
        KEY_IV BYTEA not null unique,
        ENCRYPTED_KEY BYTEA not null unique,
        DEPARTMENT_PASSWORD_SALT BYTEA not null unique,
        DEPARTMENT_PASSWORD_HASH BYTEA unique,
        primary key (ID)
    );

    create table DIAGNOSIS (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        CODE_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table DUTY_ROSTER_TURN (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024),
        CALENDAR CHARACTER VARYING(1024),
        SELF_ALLOCATABLE BOOLEAN not null,
        START TIMESTAMP WITHOUT TIME ZONE not null,
        STOP TIMESTAMP WITHOUT TIME ZONE not null,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT,
        STAFF_FK BIGINT,
        VISIT_SCHEDULE_ITEM_FK BIGINT,
        primary key (ID)
    );

    create table ERROR (
        ID BIGINT not null,
        TYPE CHARACTER VARYING(1024),
        MESSAGE CHARACTER VARYING(1024),
        METHOD CHARACTER VARYING(1024),
        ARGUMENTS TEXT,
        STACK_TRACE TEXT,
        USERNAME CHARACTER VARYING(1024),
        TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        primary key (ID)
    );

    create table FILE (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        LOGICAL_PATH CHARACTER VARYING(1024) not null,
        TITLE CHARACTER VARYING(1024),
        COMMENT TEXT,
        ACTIVE BOOLEAN not null,
        PUBLIC_FILE BOOLEAN not null,
        FILE_NAME CHARACTER VARYING(1024),
        SIZE BIGINT not null,
        EXTERNAL_FILE BOOLEAN not null,
        DATA BYTEA,
        EXTERNAL_FILE_NAME CHARACTER VARYING(1024),
        MD5 CHARACTER VARYING(1024) not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        ENCRYPTED_TITLE BYTEA,
        TITLE_IV BYTEA,
        TITLE_HASH BYTEA,
        ENCRYPTED_COMMENT BYTEA,
        COMMENT_IV BYTEA,
        COMMENT_HASH BYTEA,
        ENCRYPTED_FILE_NAME BYTEA,
        FILE_NAME_IV BYTEA,
        FILE_NAME_HASH BYTEA,
        DATA_IV BYTEA,
        CONTENT_TYPE_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        INVENTORY_FK BIGINT,
        STAFF_FK BIGINT,
        COURSE_FK BIGINT,
        TRIAL_FK BIGINT,
        PROBAND_FK BIGINT,
        MASS_MAIL_FK BIGINT,
        primary key (ID)
    );

    create table FILE_FOLDER_PRESET (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        LOGICAL_PATH CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table HOLIDAY (
        ID BIGINT not null,
        BASE CHARACTER VARYING(1024) not null,
        DAY INTEGER,
        MONTH INTEGER,
        WEEKDAY CHARACTER VARYING(1024),
        N INTEGER,
        OFFSET_DAYS BIGINT not null,
        HOLIDAY BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        ACTIVE BOOLEAN not null,
        primary key (ID)
    );

    create table HYPERLINK (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024) not null,
        URL CHARACTER VARYING(1024) not null,
        ACTIVE BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        CATEGORY_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        INVENTORY_FK BIGINT,
        STAFF_FK BIGINT,
        COURSE_FK BIGINT,
        TRIAL_FK BIGINT,
        primary key (ID)
    );

    create table HYPERLINK_CATEGORY (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null,
        TITLE_PRESET_L10N_KEY CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table ICD_SYST (
        ID BIGINT not null,
        REVISION CHARACTER VARYING(1024),
        CHAPTER_CODE CHARACTER VARYING(1024) not null,
        CHAPTER_PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        HASH BIGINT not null unique,
        primary key (ID)
    );

    create table ICD_SYST_BLOCK (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        LAST BOOLEAN not null,
        ICD_SYST_FK BIGINT,
        primary key (ID)
    );

    create table ICD_SYST_CATEGORY (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        LAST BOOLEAN not null,
        ICD_SYST_FK BIGINT,
        primary key (ID)
    );

    create table ICD_SYST_MODIFIER (
        ID BIGINT not null,
        SUFFIX CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        ICD_SYST_CATEGORY_FK BIGINT,
        primary key (ID)
    );

    create table INPUT_FIELD (
        ID BIGINT not null,
        FIELD_TYPE CHARACTER VARYING(1024) not null,
        TEXT_PRESET_L10N_KEY TEXT,
        REG_EXP CHARACTER VARYING(1024),
        MIN_SELECTIONS INTEGER,
        MAX_SELECTIONS INTEGER,
        BOOLEAN_PRESET BOOLEAN,
        LONG_LOWER_LIMIT BIGINT,
        LONG_UPPER_LIMIT BIGINT,
        LONG_PRESET BIGINT,
        FLOAT_LOWER_LIMIT REAL,
        FLOAT_UPPER_LIMIT REAL,
        FLOAT_PRESET REAL,
        DATE_PRESET DATE,
        TIMESTAMP_PRESET TIMESTAMP WITHOUT TIME ZONE,
        TIME_PRESET TIME WITHOUT TIME ZONE,
        LOCALIZED BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        EXTERNAL_ID CHARACTER VARYING(1024),
        TITLE_L10N_KEY CHARACTER VARYING(1024),
        CATEGORY CHARACTER VARYING(1024),
        COMMENT_L10N_KEY TEXT,
        VALIDATION_ERROR_MSG_L10N_KEY CHARACTER VARYING(1024),
        VERSION BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        MIN_DATE DATE,
        MAX_DATE DATE,
        MIN_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        MAX_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        USER_TIME_ZONE BOOLEAN not null,
        MIN_TIME TIME WITHOUT TIME ZONE,
        MAX_TIME TIME WITHOUT TIME ZONE,
        STRICT BOOLEAN,
        LEARN BOOLEAN,
        WIDTH BIGINT,
        HEIGHT BIGINT,
        FILE_NAME CHARACTER VARYING(1024),
        FILE_SIZE BIGINT,
        DATA BYTEA,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        MODIFIED_USER_FK BIGINT not null,
        CONTENT_TYPE_FK BIGINT,
        primary key (ID)
    );

    create table INPUT_FIELD_SELECTION_SET_VALUE (
        ID BIGINT not null,
        VALUE CHARACTER VARYING(1024) not null,
        LOCALIZED BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        PRESET BOOLEAN not null,
        INK_REGION BYTEA,
        STROKES_ID CHARACTER VARYING(1024),
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        FIELD_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table INPUT_FIELD_VALUE (
        ID BIGINT not null,
        STRING_VALUE TEXT,
        TRUNCATED_STRING_VALUE CHARACTER VARYING(1024),
        BOOLEAN_VALUE BOOLEAN,
        LONG_VALUE BIGINT,
        FLOAT_VALUE REAL,
        DATE_VALUE DATE,
        TIMESTAMP_VALUE TIMESTAMP WITHOUT TIME ZONE,
        TIME_VALUE TIME WITHOUT TIME ZONE,
        INK_VALUE BYTEA,
        primary key (ID)
    );

    create table INQUIRY (
        ID BIGINT not null,
        CATEGORY CHARACTER VARYING(1024),
        ACTIVE BOOLEAN not null,
        ACTIVE_SIGNUP BOOLEAN not null,
        POSITION BIGINT not null,
        DISABLED BOOLEAN not null,
        OPTIONAL BOOLEAN not null,
        EXCEL_VALUE BOOLEAN not null,
        EXCEL_DATE BOOLEAN not null,
        COMMENT TEXT,
        TITLE_L10N_KEY CHARACTER VARYING(1024),
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        JS_VARIABLE_NAME CHARACTER VARYING(1024),
        JS_VALUE_EXPRESSION TEXT,
        JS_OUTPUT_EXPRESSION TEXT,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        EXTERNAL_ID CHARACTER VARYING(1024),
        FIELD_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        primary key (ID)
    );

    create table INQUIRY_VALUE (
        ID BIGINT not null,
        VERSION BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VALUE_FK BIGINT not null unique,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        INQUIRY_FK BIGINT not null,
        primary key (ID)
    );

    create table INVENTORY (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        PIECES BIGINT not null,
        BOOKABLE BOOLEAN not null,
        MAX_OVERLAPPING_BOOKINGS BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        PARENT_FK BIGINT,
        DEPARTMENT_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        OWNER_FK BIGINT,
        CATEGORY_FK BIGINT not null,
        primary key (ID)
    );

    create table INVENTORY_BOOKING (
        ID BIGINT not null,
        CALENDAR CHARACTER VARYING(1024),
        START TIMESTAMP WITHOUT TIME ZONE not null,
        STOP TIMESTAMP WITHOUT TIME ZONE not null,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        INVENTORY_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        COURSE_FK BIGINT,
        TRIAL_FK BIGINT,
        PROBAND_FK BIGINT,
        ON_BEHALF_OF_FK BIGINT not null,
        primary key (ID)
    );

    create table INVENTORY_CATEGORY (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COLOR CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        RELEVANT_FOR_COURSE_APPOINTMENTS BOOLEAN not null,
        RELEVANT_FOR_TRIAL_APPOINTMENTS BOOLEAN not null,
        RELEVANT_FOR_PROBAND_APPOINTMENTS BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table INVENTORY_STATUS_ENTRY (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE not null,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        INVENTORY_FK BIGINT not null,
        ORIGINATOR_FK BIGINT,
        ADDRESSEE_FK BIGINT,
        primary key (ID)
    );

    create table INVENTORY_STATUS_TYPE (
        ID BIGINT not null,
        INVENTORY_ACTIVE BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        ORIGINATOR_REQUIRED BOOLEAN not null,
        ADDRESSEE_REQUIRED BOOLEAN not null,
        HIDE_AVAILABILITY BOOLEAN not null,
        primary key (ID)
    );

    create table INVENTORY_TAG (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        REG_EXP CHARACTER VARYING(1024),
        MISMATCH_MSG_L10N_KEY CHARACTER VARYING(1024),
        VISIBLE BOOLEAN not null,
        EXCEL BOOLEAN not null,
        primary key (ID)
    );

    create table INVENTORY_TAG_VALUE (
        ID BIGINT not null,
        VALUE CHARACTER VARYING(1024) not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TAG_FK BIGINT not null,
        INVENTORY_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table JOB (
        ID BIGINT not null,
        FILE_NAME CHARACTER VARYING(1024),
        FILE_SIZE BIGINT,
        DATA BYTEA,
        ENCRYPTED_FILE_NAME BYTEA,
        FILE_NAME_IV BYTEA,
        FILE_NAME_HASH BYTEA,
        DATA_IV BYTEA,
        ENCRYPTED_FILE BOOLEAN not null,
        EMAIL_RECIPIENTS CHARACTER VARYING(1024),
        STATUS CHARACTER VARYING(1024) not null,
        JOB_OUTPUT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        CONTENT_TYPE_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT,
        PROBAND_FK BIGINT,
        INPUT_FIELD_FK BIGINT,
        CRITERIA_FK BIGINT,
        primary key (ID)
    );

    create table JOB_TYPE (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null,
        DESCRIPTION_L10N_KEY CHARACTER VARYING(1024) not null,
        COMMAND_FORMAT CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        DAILY BOOLEAN not null,
        WEEKLY BOOLEAN not null,
        MONTHLY BOOLEAN not null,
        INPUT_FILE BOOLEAN not null,
        OUTPUT_FILE BOOLEAN not null,
        ENCRYPT_FILE BOOLEAN not null,
        EMAIL_RECIPIENTS BOOLEAN not null,
        TRIAL_FK BIGINT,
        primary key (ID)
    );

    create table JOURNAL_CATEGORY (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null,
        TITLE_PRESET_L10N_KEY CHARACTER VARYING(1024) not null,
        COLOR CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        PRESET BOOLEAN not null,
        primary key (ID)
    );

    create table JOURNAL_ENTRY (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024),
        COMMENT TEXT,
        REAL_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        SYSTEM_MESSAGE BOOLEAN not null,
        SYSTEM_MESSAGE_MODULE CHARACTER VARYING(1024),
        SYSTEM_MESSAGE_CODE CHARACTER VARYING(1024),
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        ENCRYPTED_COMMENT BYTEA,
        COMMENT_IV BYTEA,
        COMMENT_HASH BYTEA,
        ENCRYPTED_TITLE BYTEA,
        TITLE_IV BYTEA,
        TITLE_HASH BYTEA,
        CATEGORY_FK BIGINT,
        MODIFIED_USER_FK BIGINT,
        INVENTORY_FK BIGINT,
        STAFF_FK BIGINT,
        COURSE_FK BIGINT,
        USER_FK BIGINT,
        TRIAL_FK BIGINT,
        PROBAND_FK BIGINT,
        CRITERIA_FK BIGINT,
        INPUT_FIELD_FK BIGINT,
        MASS_MAIL_FK BIGINT,
        primary key (ID)
    );

    create table KEY_PAIR (
        ID BIGINT not null,
        PUBLIC_KEY BYTEA not null unique,
        PRIVATE_KEY_SALT BYTEA not null unique,
        PRIVATE_KEY_IV BYTEA not null unique,
        ENCRYPTED_PRIVATE_KEY BYTEA not null unique,
        primary key (ID)
    );

    create table LECTURER (
        ID BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        COURSE_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        COMPETENCE_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table LECTURER_COMPETENCE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table MAINTENANCE_SCHEDULE_ITEM (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024) not null,
        DATE DATE not null,
        CHARGE REAL not null,
        ACTIVE BOOLEAN not null,
        RECURRING BOOLEAN not null,
        RECURRENCE_PERIOD CHARACTER VARYING(1024),
        RECURRENCE_PERIOD_DAYS BIGINT,
        NOTIFY BOOLEAN not null,
        REMINDER_PERIOD CHARACTER VARYING(1024) not null,
        REMINDER_PERIOD_DAYS BIGINT,
        DISMISSED BOOLEAN not null,
        DISMISSED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        COMMENT TEXT,
        INVENTORY_FK BIGINT not null,
        RESPONSIBLE_PERSON_FK BIGINT,
        COMPANY_CONTACT_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        RESPONSIBLE_PERSON_PROXY_FK BIGINT,
        primary key (ID)
    );

    create table MAINTENANCE_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        TITLE_PRESET_L10N_KEY CHARACTER VARYING(1024) not null,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table MASS_MAIL (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        DESCRIPTION TEXT,
        START TIMESTAMP WITHOUT TIME ZONE not null,
        LOCK_AFTER_SENDING BOOLEAN not null,
        PROBAND_LIST_STATUS_RESEND BOOLEAN not null,
        FROM_ADDRESS CHARACTER VARYING(1024) not null,
        FROM_NAME CHARACTER VARYING(1024),
        LOCALE CHARACTER VARYING(1024) not null,
        MALE_SALUTATION CHARACTER VARYING(1024),
        FEMALE_SALUTATION CHARACTER VARYING(1024),
        SUBJECT_FORMAT CHARACTER VARYING(1024) not null,
        TEXT_TEMPLATE TEXT not null,
        REPLY_TO_ADDRESS CHARACTER VARYING(1024) not null,
        REPLY_TO_NAME CHARACTER VARYING(1024),
        PROBAND_TO BOOLEAN not null,
        PHYSICIAN_TO BOOLEAN not null,
        TRIAL_TEAM_TO BOOLEAN not null,
        OTHER_TO CHARACTER VARYING(1024),
        CC CHARACTER VARYING(1024),
        BCC CHARACTER VARYING(1024),
        USE_BEACON BOOLEAN not null,
        ATTACH_MASS_MAIL_FILES BOOLEAN not null,
        MASS_MAIL_FILES_LOGICAL_PATH CHARACTER VARYING(1024),
        ATTACH_TRIAL_FILES BOOLEAN not null,
        TRIAL_FILES_LOGICAL_PATH CHARACTER VARYING(1024),
        ATTACH_PROBAND_FILES BOOLEAN not null,
        PROBAND_FILES_LOGICAL_PATH CHARACTER VARYING(1024),
        ATTACH_INQUIRIES BOOLEAN not null,
        ATTACH_PROBAND_LIST_ENTRY_TAGS BOOLEAN not null,
        ATTACH_ECRFS BOOLEAN not null,
        ATTACH_PROBAND_LETTER BOOLEAN not null,
        ATTACH_REIMBURSEMENTS_PDF BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        DEPARTMENT_FK BIGINT not null,
        STATUS_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        PROBAND_LIST_STATUS_FK BIGINT,
        TRIAL_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table MASS_MAIL_RECIPIENT (
        ID BIGINT not null,
        ENCRYPTED_MIME_MESSAGE_DATA BYTEA,
        MIME_MESSAGE_DATA_IV BYTEA,
        MIME_MESSAGE_SIZE BIGINT not null,
        MIME_MESSAGE_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        BEACON CHARACTER VARYING(1024) not null unique,
        SENT BOOLEAN not null,
        CANCELLED BOOLEAN not null,
        TIMES_PROCESSED BIGINT not null,
        PROCESSED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        ERROR_MESSAGE CHARACTER VARYING(1024),
        READ BIGINT not null,
        READ_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        UNSUBSCRIBED BIGINT not null,
        UNSUBSCRIBED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MASS_MAIL_FK BIGINT not null,
        PROBAND_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table MASS_MAIL_STATUS_TYPE (
        ID BIGINT not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        SENDING BOOLEAN not null,
        LOCKED BOOLEAN not null,
        primary key (ID)
    );

    create table MASS_MAIL_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        TRIAL_REQUIRED BOOLEAN not null,
        PROBAND_LIST_STAUS_REQUIRED BOOLEAN not null,
        primary key (ID)
    );

    create table MEDICATION (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        DOSE_VALUE REAL,
        DOSE_UNIT CHARACTER VARYING(1024),
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        ASP_FK BIGINT,
        PROBAND_FK BIGINT not null,
        DIAGNOSIS_FK BIGINT,
        PROCEDURE_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table MIME_TYPE (
        ID BIGINT not null,
        MIME_TYPE CHARACTER VARYING(1024) not null,
        MODULE CHARACTER VARYING(1024) not null,
        FILE_NAME_EXTENSIONS CHARACTER VARYING(1024) not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        IMAGE BOOLEAN not null,
        primary key (ID)
    );

    create table MONEY_TRANSFER (
        ID BIGINT not null,
        METHOD CHARACTER VARYING(1024) not null,
        COST_TYPE CHARACTER VARYING(1024),
        PAID BOOLEAN not null,
        TRANSACTION_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        AMOUNT REAL not null,
        VERSION BIGINT not null,
        REFERENCE CHARACTER VARYING(1024),
        REASON_FOR_PAYMENT CHARACTER VARYING(1024),
        VOUCHER_CODE CHARACTER VARYING(1024),
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        SHOW_COMMENT BOOLEAN not null,
        BANK_ACCOUNT_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        TRIAL_FK BIGINT,
        primary key (ID)
    );

    create table NOTIFICATION (
        ID BIGINT not null,
        OBSOLETE BOOLEAN not null,
        DATE DATE not null,
        SUBJECT CHARACTER VARYING(1024),
        MESSAGE TEXT,
        TYPE_FK BIGINT not null,
        DEPARTMENT_FK BIGINT,
        MAINTENANCE_SCHEDULE_ITEM_FK BIGINT,
        INVENTORY_STATUS_ENTRY_FK BIGINT,
        INVENTORY_BOOKING_FK BIGINT,
        STAFF_STATUS_ENTRY_FK BIGINT,
        DUTY_ROSTER_TURN_FK BIGINT,
        PROBAND_STATUS_ENTRY_FK BIGINT,
        COURSE_FK BIGINT,
        VISIT_SCHEDULE_ITEM_FK BIGINT,
        COURSE_PARTICIPATION_STATUS_ENTRY_FK BIGINT,
        TIMELINE_EVENT_FK BIGINT,
        PROBAND_FK BIGINT,
        PASSWORD_FK BIGINT,
        TRIAL_FK BIGINT,
        TRIAL_TAG_FK BIGINT,
        STAFF_FK BIGINT,
        USER_FK BIGINT,
        ECRF_STATUS_ENTRY_FK BIGINT,
        ECRF_FIELD_STATUS_ENTRY_FK BIGINT,
        primary key (ID)
    );

    create table NOTIFICATION_RECIPIENT (
        ID BIGINT not null,
        SENT BOOLEAN not null,
        DROPPED BOOLEAN not null,
        TIMES_PROCESSED BIGINT not null,
        TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        ERROR_MESSAGE CHARACTER VARYING(1024),
        NOTIFICATION_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        primary key (ID)
    );

    create table NOTIFICATION_TYPE (
        ID BIGINT not null,
        TYPE CHARACTER VARYING(1024) not null unique,
        SEND BOOLEAN not null,
        SHOW BOOLEAN not null,
        COLOR CHARACTER VARYING(1024) not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        SUBJECT_L10N_KEY CHARACTER VARYING(1024),
        MESSAGE_TEMPLATE_L10N_KEY CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table OPS_CODE (
        ID BIGINT not null,
        TYPE CHARACTER VARYING(1024) not null,
        REVISION CHARACTER VARYING(1024),
        DIMDI_ID CHARACTER VARYING(1024) not null,
        FIRST_CODE CHARACTER VARYING(1024),
        SECOND_CODE CHARACTER VARYING(1024),
        TEXT CHARACTER VARYING(1024) not null,
        HASH BIGINT not null unique,
        SYSTEMATICS_FK BIGINT not null,
        primary key (ID)
    );

    create table OPS_SYST (
        ID BIGINT not null,
        REVISION CHARACTER VARYING(1024),
        CHAPTER_CODE CHARACTER VARYING(1024) not null,
        CHAPTER_PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        HASH BIGINT not null unique,
        primary key (ID)
    );

    create table OPS_SYST_BLOCK (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        LAST BOOLEAN not null,
        OPS_SYST_FK BIGINT,
        primary key (ID)
    );

    create table OPS_SYST_CATEGORY (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        LAST BOOLEAN not null,
        OPS_SYST_FK BIGINT,
        primary key (ID)
    );

    create table OPS_SYST_MODIFIER (
        ID BIGINT not null,
        SUFFIX CHARACTER VARYING(1024) not null,
        PREFERRED_RUBRIC_LABEL CHARACTER VARYING(1024) not null,
        LEVEL INTEGER not null,
        OPS_SYST_CATEGORY_FK BIGINT,
        primary key (ID)
    );

    create table ORGANISATION_CONTACT_PARTICULARS (
        ID BIGINT not null,
        ORGANISATION_NAME CHARACTER VARYING(1024) not null,
        CV_ORGANISATION_NAME CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table PASSWORD (
        ID BIGINT not null,
        PASSWORD_IV BYTEA,
        PASSWORD_SALT BYTEA,
        ENCRYPTED_PASSWORD BYTEA,
        PASSWORD_HASH_SALT BYTEA unique,
        PASSWORD_HASH BYTEA unique,
        DEPARTMENT_PASSWORD_SALT BYTEA not null unique,
        DEPARTMENT_PASSWORD_IV BYTEA not null unique,
        ENCRYPTED_DEPARTMENT_PASSWORD BYTEA not null unique,
        TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        EXPIRES BOOLEAN not null,
        PROLONGABLE BOOLEAN not null,
        VALIDITY_PERIOD CHARACTER VARYING(1024),
        VALIDITY_PERIOD_DAYS BIGINT,
        SUCCESSFUL_LOGONS BIGINT not null,
        LIMIT_LOGONS BOOLEAN not null,
        MAX_SUCCESSFUL_LOGONS BIGINT,
        LIMIT_WRONG_PASSWORD_ATTEMPTS BOOLEAN not null,
        MAX_WRONG_PASSWORD_ATTEMPTS_SINCE_LAST_SUCCESSFUL_LOGON BIGINT,
        WRONG_PASSWORD_ATTEMPTS_SINCE_LAST_SUCCESSFUL_LOGON BIGINT not null,
        LAST_LOGON_ATTEMPT_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        LAST_LOGON_ATTEMPT_HOST CHARACTER VARYING(1024),
        LAST_SUCCESSFUL_LOGON_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        LAST_SUCCESSFUL_LOGON_HOST CHARACTER VARYING(1024),
        USER_FK BIGINT not null,
        PREVIOUS_PASSWORD_FK BIGINT unique,
        primary key (ID)
    );

    create table PERMISSION (
        ID BIGINT not null,
        SERVICE_METHOD CHARACTER VARYING(1024) not null,
        IP_RANGES CHARACTER VARYING(1024),
        PARAMETER_GETTER CHARACTER VARYING(1024),
        TRANSFORMATION CHARACTER VARYING(1024),
        RESTRICTION CHARACTER VARYING(1024),
        DISJUNCTION_GROUP CHARACTER VARYING(1024),
        PARAMETER_SETTER CHARACTER VARYING(1024),
        OVERRIDE CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table PERSON_CONTACT_PARTICULARS (
        ID BIGINT not null,
        PREFIXED_TITLE1 CHARACTER VARYING(1024),
        PREFIXED_TITLE2 CHARACTER VARYING(1024),
        PREFIXED_TITLE3 CHARACTER VARYING(1024),
        FIRST_NAME CHARACTER VARYING(1024) not null,
        LAST_NAME CHARACTER VARYING(1024) not null,
        POSTPOSITIONED_TITLE1 CHARACTER VARYING(1024),
        POSTPOSITIONED_TITLE2 CHARACTER VARYING(1024),
        POSTPOSITIONED_TITLE3 CHARACTER VARYING(1024),
        CV_ACADEMIC_TITLE CHARACTER VARYING(1024),
        DATE_OF_BIRTH DATE,
        GENDER CHARACTER VARYING(1024) not null,
        CITIZENSHIP CHARACTER VARYING(1024),
        WIDTH BIGINT,
        HEIGHT BIGINT,
        FILE_NAME CHARACTER VARYING(1024),
        FILE_SIZE BIGINT,
        FILE_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        DATA BYTEA,
        SHOW_CV BOOLEAN,
        CONTENT_TYPE_FK BIGINT,
        primary key (ID)
    );

    create table PRIVACY_CONSENT_STATUS_TYPE (
        ID BIGINT not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        AUTO_DELETE BOOLEAN not null,
        primary key (ID)
    );

    create table PROBAND (
        ID BIGINT not null,
        PERSON BOOLEAN not null,
        BLINDED BOOLEAN not null,
        BEACON CHARACTER VARYING(1024) not null unique,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        AUTO_DELETE_DEADLINE DATE,
        RATING BIGINT,
        RATING_MAX BIGINT,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        PERSON_PARTICULARS_FK BIGINT unique,
        MODIFIED_USER_FK BIGINT not null,
        DEPARTMENT_FK BIGINT not null,
        CATEGORY_FK BIGINT not null,
        PRIVACY_CONSENT_STATUS_FK BIGINT not null,
        ANIMAL_PARTICULARS_FK BIGINT unique,
        PHYSICIAN_FK BIGINT,
        primary key (ID)
    );

    create table PROBAND_ADDRESS (
        ID BIGINT not null,
        ENCRYPTED_COUNTRY_NAME BYTEA not null unique,
        COUNTRY_NAME_IV BYTEA not null unique,
        COUNTRY_NAME_HASH BYTEA,
        ENCRYPTED_ZIP_CODE BYTEA not null unique,
        ZIP_CODE_IV BYTEA not null unique,
        ZIP_CODE_HASH BYTEA,
        ENCRYPTED_CITY_NAME BYTEA not null unique,
        CITY_NAME_IV BYTEA not null unique,
        CITY_NAME_HASH BYTEA,
        ENCRYPTED_STREET_NAME BYTEA not null unique,
        STREET_NAME_IV BYTEA not null unique,
        STREET_NAME_HASH BYTEA,
        ENCRYPTED_HOUSE_NUMBER BYTEA not null unique,
        HOUSE_NUMBER_IV BYTEA not null unique,
        HOUSE_NUMBER_HASH BYTEA,
        ENCRYPTED_ENTRANCE BYTEA not null unique,
        ENTRANCE_IV BYTEA not null unique,
        ENTRANCE_HASH BYTEA,
        ENCRYPTED_DOOR_NUMBER BYTEA not null unique,
        DOOR_NUMBER_IV BYTEA not null unique,
        DOOR_NUMBER_HASH BYTEA,
        DELIVER BOOLEAN not null,
        ENCRYPTED_CARE_OF BYTEA not null unique,
        CARE_OF_IV BYTEA not null unique,
        CARE_OF_HASH BYTEA,
        AFNUS BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        WIRE_TRANSFER BOOLEAN not null,
        PROBAND_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_CATEGORY (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COLOR CHARACTER VARYING(1024) not null,
        PERSON BOOLEAN not null,
        ANIMAL BOOLEAN not null,
        PRIVACY_CONSENT_CONTROL BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        PRESET BOOLEAN not null,
        LOCKED BOOLEAN not null,
        SIGNUP BOOLEAN not null,
        DELETE BOOLEAN not null,
        primary key (ID)
    );

    create table PROBAND_CONTACT_DETAIL_VALUE (
        ID BIGINT not null,
        ENCRYPTED_VALUE BYTEA not null unique,
        VALUE_IV BYTEA not null unique,
        VALUE_HASH BYTEA,
        NOTIFY BOOLEAN not null,
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        NA BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_CONTACT_PARTICULARS (
        ID BIGINT not null,
        ENCRYPTED_PREFIXED_TITLE1 BYTEA not null unique,
        PREFIXED_TITLE1_IV BYTEA not null unique,
        ENCRYPTED_PREFIXED_TITLE2 BYTEA not null unique,
        PREFIXED_TITLE2_IV BYTEA not null unique,
        ENCRYPTED_PREFIXED_TITLE3 BYTEA not null unique,
        PREFIXED_TITLE3_IV BYTEA not null unique,
        ENCRYPTED_FIRST_NAME BYTEA not null unique,
        FIRST_NAME_IV BYTEA not null unique,
        FIRST_NAME_HASH BYTEA,
        ENCRYPTED_LAST_NAME BYTEA not null unique,
        LAST_NAME_IV BYTEA not null unique,
        LAST_NAME_HASH BYTEA,
        LAST_NAME_INDEX CHARACTER VARYING(1024),
        ENCRYPTED_POSTPOSITIONED_TITLE1 BYTEA not null unique,
        POSTPOSITIONED_TITLE1_IV BYTEA not null unique,
        ENCRYPTED_POSTPOSITIONED_TITLE2 BYTEA not null unique,
        POSTPOSITIONED_TITLE2_IV BYTEA not null unique,
        ENCRYPTED_POSTPOSITIONED_TITLE3 BYTEA not null unique,
        POSTPOSITIONED_TITLE3_IV BYTEA not null unique,
        ENCRYPTED_DATE_OF_BIRTH BYTEA not null unique,
        DATE_OF_BIRTH_IV BYTEA not null unique,
        DATE_OF_BIRTH_HASH BYTEA,
        YEAR_OF_BIRTH BIGINT,
        GENDER CHARACTER VARYING(1024),
        ENCRYPTED_CITIZENSHIP BYTEA not null unique,
        CITIZENSHIP_IV BYTEA not null unique,
        CITIZENSHIP_HASH BYTEA,
        WIDTH BIGINT,
        HEIGHT BIGINT,
        ENCRYPTED_FILE_NAME BYTEA not null unique,
        FILE_NAME_IV BYTEA not null unique,
        FILE_NAME_HASH BYTEA,
        FILE_SIZE BIGINT,
        FILE_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        ENCRYPTED_DATA BYTEA not null unique,
        DATA_IV BYTEA not null unique,
        DATA_HASH BYTEA,
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        ALIAS CHARACTER VARYING(1024),
        CONTENT_TYPE_FK BIGINT,
        primary key (ID)
    );

    create table PROBAND_GROUP (
        ID BIGINT not null,
        TOKEN CHARACTER VARYING(1024) not null,
        TITLE CHARACTER VARYING(1024) not null,
        DESCRIPTION TEXT,
        RANDOMIZE BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_LIST_ENTRY (
        ID BIGINT not null,
        POSITION BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        RATING BIGINT,
        RATING_MAX BIGINT,
        PROBAND_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        GROUP_FK BIGINT,
        LAST_STATUS_FK BIGINT unique,
        primary key (ID)
    );

    create table PROBAND_LIST_ENTRY_TAG (
        ID BIGINT not null,
        TITLE_L10N_KEY CHARACTER VARYING(1024),
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        POSITION BIGINT not null,
        DISABLED BOOLEAN not null,
        OPTIONAL BOOLEAN not null,
        EXCEL_VALUE BOOLEAN not null,
        EXCEL_DATE BOOLEAN not null,
        COMMENT TEXT,
        JS_VARIABLE_NAME CHARACTER VARYING(1024),
        JS_VALUE_EXPRESSION TEXT,
        JS_OUTPUT_EXPRESSION TEXT,
        ECRF_VALUE BOOLEAN not null,
        STRATIFICATION BOOLEAN not null,
        RANDOMIZE BOOLEAN not null,
        EXTERNAL_ID CHARACTER VARYING(1024),
        FIELD_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_LIST_ENTRY_TAG_VALUE (
        ID BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        LIST_ENTRY_FK BIGINT not null,
        TAG_FK BIGINT not null,
        VALUE_FK BIGINT not null unique,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_LIST_STATUS_ENTRY (
        ID BIGINT not null,
        REASON TEXT,
        ENCRYPTED_REASON BYTEA,
        REASON_IV BYTEA,
        REASON_HASH BYTEA,
        REAL_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        STATUS_FK BIGINT not null,
        LIST_ENTRY_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_LIST_STATUS_LOG_LEVEL (
        ID BIGINT not null,
        LOG_LEVEL CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table PROBAND_LIST_STATUS_TYPE (
        ID BIGINT not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        REASON_REQUIRED BOOLEAN not null,
        BLOCKING BOOLEAN not null,
        COUNT BOOLEAN not null,
        SCREENING BOOLEAN not null,
        IC BOOLEAN not null,
        ECRF_VALUE_INPUT_ENABLED BOOLEAN not null,
        SIGNUP BOOLEAN not null,
        PERSON BOOLEAN not null,
        primary key (ID)
    );

    create table PROBAND_STATUS_ENTRY (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE not null,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROBAND_STATUS_TYPE (
        ID BIGINT not null,
        PROBAND_ACTIVE BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        PERSON BOOLEAN not null,
        ANIMAL BOOLEAN not null,
        HIDE_AVAILABILITY BOOLEAN not null,
        primary key (ID)
    );

    create table PROBAND_TAG (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        REG_EXP CHARACTER VARYING(1024),
        MISMATCH_MSG_L10N_KEY CHARACTER VARYING(1024),
        PERSON BOOLEAN not null,
        ANIMAL BOOLEAN not null,
        EXCEL BOOLEAN not null,
        primary key (ID)
    );

    create table PROBAND_TAG_VALUE (
        ID BIGINT not null,
        ENCRYPTED_VALUE BYTEA not null unique,
        VALUE_IV BYTEA not null unique,
        VALUE_HASH BYTEA,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TAG_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROCEDURE (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        ENCRYPTED_COMMENT BYTEA not null unique,
        COMMENT_IV BYTEA not null unique,
        COMMENT_HASH BYTEA,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        CODE_FK BIGINT not null,
        PROBAND_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table PROFILE_PERMISSION (
        ID BIGINT not null,
        PROFILE CHARACTER VARYING(1024) not null,
        ACTIVE BOOLEAN not null,
        PERMISSION_FK BIGINT not null,
        primary key (ID)
    );

    create table RANDOMIZATION_LIST_CODE (
        ID BIGINT not null,
        CODE CHARACTER VARYING(1024) not null,
        BROKEN BOOLEAN not null,
        REASON_FOR_BREAK TEXT,
        BREAK_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TRIAL_FK BIGINT,
        RANDOMIZATION_LIST_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        BREAK_USER_FK BIGINT,
        primary key (ID)
    );

    create table RANDOMIZATION_LIST_CODE_VALUE (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        VALUE CHARACTER VARYING(1024),
        BLINDED BOOLEAN not null,
        CODE_FK BIGINT not null,
        primary key (ID)
    );

    create table SIGNATURE (
        ID BIGINT not null,
        MODULE CHARACTER VARYING(1024) not null,
        TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        SIGNATURE_DATA BYTEA not null,
        TRIAL_FK BIGINT,
        SIGNEE_FK BIGINT not null,
        ECRF_STATUS_ENTRY_FK BIGINT,
        primary key (ID)
    );

    create table SPONSORING_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table STAFF (
        ID BIGINT not null,
        PERSON BOOLEAN not null,
        EMPLOYEE BOOLEAN not null,
        ALLOCATABLE BOOLEAN not null,
        SUPERVISOR BOOLEAN not null,
        MAX_OVERLAPPING_SHIFTS BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        PERSON_PARTICULARS_FK BIGINT unique,
        ORGANISATION_PARTICULARS_FK BIGINT unique,
        PARENT_FK BIGINT,
        DEPARTMENT_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        CATEGORY_FK BIGINT not null,
        primary key (ID)
    );

    create table STAFF_ADDRESS (
        ID BIGINT not null,
        COUNTRY_NAME CHARACTER VARYING(1024) not null,
        ZIP_CODE CHARACTER VARYING(1024) not null,
        CITY_NAME CHARACTER VARYING(1024) not null,
        STREET_NAME CHARACTER VARYING(1024) not null,
        HOUSE_NUMBER CHARACTER VARYING(1024) not null,
        ENTRANCE CHARACTER VARYING(1024),
        DOOR_NUMBER CHARACTER VARYING(1024),
        CV BOOLEAN not null,
        DELIVER BOOLEAN not null,
        CARE_OF CHARACTER VARYING(1024),
        AFNUS BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        STAFF_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table STAFF_CATEGORY (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COLOR CHARACTER VARYING(1024) not null,
        PERSON BOOLEAN not null,
        ORGANISATION BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table STAFF_CONTACT_DETAIL_VALUE (
        ID BIGINT not null,
        VALUE CHARACTER VARYING(1024),
        NOTIFY BOOLEAN not null,
        COMMENT TEXT,
        NA BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table STAFF_STATUS_ENTRY (
        ID BIGINT not null,
        START TIMESTAMP WITHOUT TIME ZONE not null,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        primary key (ID)
    );

    create table STAFF_STATUS_TYPE (
        ID BIGINT not null,
        STAFF_ACTIVE BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        HIDE_AVAILABILITY BOOLEAN not null,
        primary key (ID)
    );

    create table STAFF_TAG (
        ID BIGINT not null,
        PERSON BOOLEAN not null,
        ORGANISATION BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null,
        MAX_OCCURRENCE INTEGER,
        REG_EXP CHARACTER VARYING(1024),
        MISMATCH_MSG_L10N_KEY CHARACTER VARYING(1024),
        EXCEL BOOLEAN not null,
        TRAINING_RECORD BOOLEAN not null,
        primary key (ID)
    );

    create table STAFF_TAG_VALUE (
        ID BIGINT not null,
        VALUE CHARACTER VARYING(1024) not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TAG_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table STRATIFICATION_RANDOMIZATION_LIST (
        ID BIGINT not null,
        RANDOMIZATION_LIST TEXT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table STREET (
        ID BIGINT not null,
        COUNTRY_NAME CHARACTER VARYING(1024) not null,
        ZIP_CODE CHARACTER VARYING(1024) not null,
        CITY_NAME CHARACTER VARYING(1024) not null,
        STREET_NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table SURVEY_STATUS_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table TEAM_MEMBER (
        ID BIGINT not null,
        NOTIFY_TIMELINE_EVENT BOOLEAN not null,
        NOTIFY_ECRF_VALIDATED_STATUS BOOLEAN not null,
        NOTIFY_ECRF_REVIEW_STATUS BOOLEAN not null,
        NOTIFY_ECRF_VERIFIED_STATUS BOOLEAN not null,
        NOTIFY_ECRF_FIELD_STATUS BOOLEAN not null,
        NOTIFY_OTHER BOOLEAN not null,
        ACCESS BOOLEAN not null,
        SIGN BOOLEAN not null,
        RESOLVE BOOLEAN not null,
        VERIFY BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        ROLE_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        STAFF_FK BIGINT not null,
        primary key (ID)
    );

    create table TEAM_MEMBER_ROLE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table TIMELINE_EVENT (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024) not null,
        DESCRIPTION TEXT,
        START DATE not null,
        STOP DATE,
        SHOW BOOLEAN not null,
        IMPORTANCE CHARACTER VARYING(1024) not null,
        NOTIFY BOOLEAN not null,
        REMINDER_PERIOD CHARACTER VARYING(1024) not null,
        REMINDER_PERIOD_DAYS BIGINT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        DISMISSED BOOLEAN not null,
        DISMISSED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TYPE_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        PARENT_FK BIGINT,
        primary key (ID)
    );

    create table TIMELINE_EVENT_TYPE (
        ID BIGINT not null,
        IMPORTANCE_PRESET CHARACTER VARYING(1024) not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        VISIBLE BOOLEAN not null,
        SHOW_PRESET BOOLEAN not null,
        NOTIFY_PRESET BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        TITLE_PRESET_L10N_KEY CHARACTER VARYING(1024),
        TITLE_PRESET_TYPE CHARACTER VARYING(1024) not null,
        TITLE_PRESET_FIXED BOOLEAN not null,
        COLOR CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table TITLE (
        ID BIGINT not null,
        TITLE CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table TRAINING_RECORD_SECTION (
        ID BIGINT not null,
        POSITION BIGINT not null unique,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        DESCRIPTION_L10N_KEY CHARACTER VARYING(1024),
        SHOW_TRAINING_RECORD_PRESET BOOLEAN not null,
        VISIBLE BOOLEAN not null,
        primary key (ID)
    );

    create table TRIAL (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        TITLE TEXT not null,
        DESCRIPTION TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        RANDOM BYTEA,
        RANDOMIZATION CHARACTER VARYING(1024),
        RANDOMIZATION_LIST TEXT,
        PROBAND_ALIAS_FORMAT CHARACTER VARYING(1024),
        EXCLUSIVE_PROBANDS BOOLEAN not null,
        BLOCKING_PERIOD CHARACTER VARYING(1024),
        BLOCKING_PERIOD_DAYS BIGINT,
        DUTY_SELF_ALLOCATION_LOCKED BOOLEAN not null,
        DUTY_SELF_ALLOCATION_LOCKED_UNTIL TIMESTAMP WITHOUT TIME ZONE,
        DUTY_SELF_ALLOCATION_LOCKED_FROM TIMESTAMP WITHOUT TIME ZONE,
        SIGNUP_PROBAND_LIST BOOLEAN not null,
        SIGNUP_INQUIRIES BOOLEAN not null,
        SIGNUP_RANDOMIZE BOOLEAN not null,
        SIGNUP_DESCRIPTION TEXT,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        DEPARTMENT_FK BIGINT not null,
        STATUS_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        SPONSORING_FK BIGINT not null,
        SURVEY_STATUS_FK BIGINT not null,
        primary key (ID)
    );

    create table TRIAL_STATUS_ACTION (
        ID BIGINT not null,
        ACTION CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table TRIAL_STATUS_TYPE (
        ID BIGINT not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        LOCKDOWN BOOLEAN not null,
        INQUIRY_VALUE_INPUT_ENABLED BOOLEAN not null,
        ECRF_VALUE_INPUT_ENABLED BOOLEAN not null,
        IGNORE_TIMELINE_EVENTS BOOLEAN not null,
        RELEVANT_FOR_COURSES BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table TRIAL_TAG (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        REG_EXP CHARACTER VARYING(1024),
        MISMATCH_MSG_L10N_KEY CHARACTER VARYING(1024),
        VISIBLE BOOLEAN not null,
        NOTIFY_MISSING BOOLEAN not null,
        EXCEL BOOLEAN not null,
        PAYOFFS BOOLEAN not null,
        primary key (ID)
    );

    create table TRIAL_TAG_VALUE (
        ID BIGINT not null,
        VALUE CHARACTER VARYING(1024) not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        TAG_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table TRIAL_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VISIBLE BOOLEAN not null,
        PERSON BOOLEAN not null,
        primary key (ID)
    );

    create table USER_PERMISSION_PROFILE (
        ID BIGINT not null,
        PROFILE CHARACTER VARYING(1024) not null,
        ACTIVE BOOLEAN not null,
        VERSION BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        USER_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT,
        primary key (ID)
    );

    create table VISIT (
        ID BIGINT not null,
        TOKEN CHARACTER VARYING(1024) not null,
        TITLE CHARACTER VARYING(1024) not null,
        DESCRIPTION TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        REIMBURSEMENT REAL not null,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table VISIT_SCHEDULE_ITEM (
        ID BIGINT not null,
        TOKEN CHARACTER VARYING(1024),
        MODE CHARACTER VARYING(1024) not null,
        START TIMESTAMP WITHOUT TIME ZONE,
        STOP TIMESTAMP WITHOUT TIME ZONE,
        DURATION INTEGER,
        OFFSET_SECONDS INTEGER,
        NOTIFY BOOLEAN not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        START_TAG_FK BIGINT,
        STOP_TAG_FK BIGINT,
        GROUP_FK BIGINT,
        VISIT_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        primary key (ID)
    );

    create table VISIT_TYPE (
        ID BIGINT not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        MAX_OCCURRENCE INTEGER,
        VISIBLE BOOLEAN not null,
        COLOR CHARACTER VARYING(1024) not null,
        TRAVEL BOOLEAN not null,
        primary key (ID)
    );

    create table ZIP (
        ID BIGINT not null,
        COUNTRY_NAME CHARACTER VARYING(1024) not null,
        ZIP_CODE CHARACTER VARYING(1024) not null,
        CITY_NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table asp_ingredient (
        ASPS_FK BIGINT not null,
        SUBSTANCES_FK BIGINT not null
    );

    create table course_participation_admin_self_registration_transition (
        COURSE_PARTICIPATION_STATUS_TYPES_FK BIGINT not null,
        ADMIN_SELF_REGISTRATION_TRANSITIONS_FK BIGINT not null
    );

    create table course_participation_admin_transition (
        COURSE_PARTICIPATION_STATUS_TYPES_FK BIGINT not null,
        ADMIN_TRANSITIONS_FK BIGINT not null
    );

    create table course_participation_user_self_registration_transition (
        COURSE_PARTICIPATION_STATUS_TYPES_FK BIGINT not null,
        USER_SELF_REGISTRATION_TRANSITIONS_FK BIGINT not null
    );

    create table course_participation_user_transition (
        COURSE_PARTICIPATION_STATUS_TYPES_FK BIGINT not null,
        USER_TRANSITIONS_FK BIGINT not null
    );

    create table course_renewal (
        PRECEDING_COURSES_FK BIGINT not null,
        RENEWALS_FK BIGINT not null,
        primary key (RENEWALS_FK, PRECEDING_COURSES_FK)
    );

    create table ecrf (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null,
        TITLE TEXT not null,
        DESCRIPTION TEXT,
        EXTERNAL_ID CHARACTER VARYING(1024),
        ACTIVE BOOLEAN not null,
        REVISION CHARACTER VARYING(1024),
        DISABLED BOOLEAN not null,
        ENABLE_BROWSER_FIELD_CALCULATION BOOLEAN not null,
        CHARGE REAL not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        TRIAL_FK BIGINT not null,
        MODIFIED_USER_FK BIGINT not null,
        PROBAND_LIST_STATUS_FK BIGINT,
        primary key (ID)
    );

    create table ecrf_field (
        ID BIGINT not null,
        SECTION CHARACTER VARYING(1024),
        SERIES BOOLEAN not null,
        POSITION BIGINT not null,
        EXTERNAL_ID CHARACTER VARYING(1024),
        DISABLED BOOLEAN not null,
        OPTIONAL BOOLEAN not null,
        AUDIT_TRAIL BOOLEAN not null,
        REASON_FOR_CHANGE_REQUIRED BOOLEAN not null,
        NOTIFY BOOLEAN not null,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        JS_VARIABLE_NAME CHARACTER VARYING(1024),
        JS_VALUE_EXPRESSION TEXT,
        JS_OUTPUT_EXPRESSION TEXT,
        TITLE_L10N_KEY CHARACTER VARYING(1024),
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        MODIFIED_USER_FK BIGINT not null,
        TRIAL_FK BIGINT not null,
        ECRF_FK BIGINT not null,
        FIELD_FK BIGINT not null,
        primary key (ID)
    );

    create table ecrf_field_status_entry (
        ID BIGINT not null,
        QUEUE CHARACTER VARYING(1024) not null,
        INDEX BIGINT,
        COMMENT TEXT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        LIST_ENTRY_FK BIGINT not null,
        ECRF_FIELD_FK BIGINT not null,
        VISIT_FK BIGINT,
        MODIFIED_USER_FK BIGINT not null,
        STATUS_FK BIGINT not null,
        primary key (ID)
    );

    create table ecrf_field_status_transition (
        E_C_R_F_FIELD_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table ecrf_field_status_type (
        ID BIGINT not null,
        QUEUE CHARACTER VARYING(1024) not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        UPDATED BOOLEAN not null,
        PROPOSED BOOLEAN not null,
        RESOLVED BOOLEAN not null,
        UNLOCK_VALUE BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        COMMENT_REQUIRED BOOLEAN not null,
        VALIDATION_SUCCESS BOOLEAN not null,
        VALIDATION_ERROR BOOLEAN not null,
        VALIDATION_FAILED BOOLEAN not null,
        primary key (ID)
    );

    create table ecrf_field_value (
        ID BIGINT not null,
        INDEX BIGINT,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        REASON_FOR_CHANGE TEXT,
        CHANGE_COMMENT TEXT,
        LIST_ENTRY_FK BIGINT not null,
        ECRF_FIELD_FK BIGINT not null,
        VISIT_FK BIGINT,
        VALUE_FK BIGINT not null unique,
        MODIFIED_USER_FK BIGINT not null,
        primary key (ID)
    );

    create table ecrf_group (
        ECRFS_FK BIGINT not null,
        GROUPS_FK BIGINT not null
    );

    create table ecrf_status_action (
        ID BIGINT not null,
        ACTION CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table ecrf_status_entry (
        ID BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        VERSION BIGINT not null,
        VALIDATION_STATUS CHARACTER VARYING(1024) not null,
        VALIDATION_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE,
        VALIDATION_RESPONSE_MSG CHARACTER VARYING(1024),
        MODIFIED_USER_FK BIGINT not null,
        LIST_ENTRY_FK BIGINT not null,
        STATUS_FK BIGINT not null,
        ECRF_FK BIGINT not null,
        VISIT_FK BIGINT,
        primary key (ID)
    );

    create table ecrf_status_transition (
        E_C_R_F_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table ecrf_status_type (
        ID BIGINT not null,
        COLOR CHARACTER VARYING(1024) not null,
        INITIAL BOOLEAN not null,
        NAME_L10N_KEY CHARACTER VARYING(1024) not null unique,
        VALUE_LOCKDOWN BOOLEAN not null,
        FIELD_STATUS_LOCKDOWN BOOLEAN not null,
        NODE_STYLE_CLASS CHARACTER VARYING(1024),
        APPLY_ECRF_PROBAND_LIST_STATUS BOOLEAN not null,
        AUDIT_TRAIL BOOLEAN not null,
        REASON_FOR_CHANGE_REQUIRED BOOLEAN not null,
        VALIDATED BOOLEAN not null,
        REVIEW BOOLEAN not null,
        VERIFIED BOOLEAN not null,
        DONE BOOLEAN not null,
        primary key (ID)
    );

    create table ecrf_status_type_action (
        E_C_R_F_STATUS_TYPES_FK BIGINT not null,
        ACTIONS_FK BIGINT not null
    );

    create table ecrf_visit (
        ECRFS_FK BIGINT not null,
        VISITS_FK BIGINT not null
    );

    create table input_field_value_selection (
        INPUT_FIELD_VALUES_FK BIGINT not null,
        SELECTION_VALUES_FK BIGINT not null
    );

    create table mass_mail_status_transition (
        MASS_MAIL_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table medication_ingredient (
        SUBSTANCES_FK BIGINT not null,
        MEDICATIONS_FK BIGINT not null
    );

    create table privacy_consent_status_transition (
        PRIVACY_CONSENT_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table proband_children (
        PARENTS_FK BIGINT not null,
        CHILDREN_FK BIGINT not null,
        primary key (CHILDREN_FK, PARENTS_FK)
    );

    create table proband_list_status_transition (
        PROBAND_LIST_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table proband_list_status_type_log_level (
        PROBAND_LIST_STATUS_TYPES_FK BIGINT not null,
        LOG_LEVELS_FK BIGINT not null
    );

    create table send_department_staff_category (
        SEND_DEPARTMENT_NOTIFICATION_TYPES_FK BIGINT not null,
        SEND_DEPARTMENT_STAFF_CATEGORIES_FK BIGINT not null
    );

    create table stratification_randomization_list_selection_set_value (
        SELECTION_SET_VALUES_FK BIGINT not null,
        RANDOMIZATION_LISTS_FK BIGINT not null
    );

    create table trial_status_transition (
        TRIAL_STATUS_TYPES_FK BIGINT not null,
        TRANSITIONS_FK BIGINT not null
    );

    create table trial_status_type_action (
        TRIAL_STATUS_TYPES_FK BIGINT not null,
        ACTIONS_FK BIGINT not null
    );

    create table users (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024) not null unique,
        VERSION BIGINT not null,
        MODIFIED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE not null,
        LOCKED BOOLEAN not null,
        LOCALE CHARACTER VARYING(1024) not null,
        TIME_ZONE CHARACTER VARYING(1024) not null,
        DATE_FORMAT CHARACTER VARYING(1024),
        DECIMAL_SEPARATOR CHARACTER VARYING(1024),
        THEME CHARACTER VARYING(1024),
        AUTH_METHOD CHARACTER VARYING(1024) not null,
        SHOW_TOOLTIPS BOOLEAN not null,
        DECRYPT BOOLEAN not null,
        DECRYPT_UNTRUSTED BOOLEAN not null,
        ENABLE_INVENTORY_MODULE BOOLEAN not null,
        ENABLE_STAFF_MODULE BOOLEAN not null,
        ENABLE_COURSE_MODULE BOOLEAN not null,
        ENABLE_TRIAL_MODULE BOOLEAN not null,
        ENABLE_INPUT_FIELD_MODULE BOOLEAN not null,
        ENABLE_PROBAND_MODULE BOOLEAN not null,
        ENABLE_MASS_MAIL_MODULE BOOLEAN not null,
        ENABLE_USER_MODULE BOOLEAN not null,
        DEFERRED_DELETE BOOLEAN not null,
        DEFERRED_DELETE_REASON TEXT,
        IDENTITY_FK BIGINT,
        MODIFIED_USER_FK BIGINT,
        DEPARTMENT_FK BIGINT not null,
        KEY_PAIR_FK BIGINT not null unique,
        primary key (ID)
    );

    create table valid_criterion_property_restriction (
        CRITERION_PROPERTIES_FK BIGINT not null,
        VALID_RESTRICTIONS_FK BIGINT not null
    );

    alter table ALPHA_ID 
        add constraint ALPHA_ID_SYSTEMATICS_FKC 
        foreign key (SYSTEMATICS_FK) 
        references ICD_SYST;

    alter table ANIMAL_CONTACT_PARTICULARS 
        add constraint ANIMAL_CONTACT_PARTICULARS_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table ASP_ATC_CODE 
        add constraint ASP_ATC_CODE_ASP_FKC 
        foreign key (ASP_FK) 
        references ASP;

    alter table BANK_ACCOUNT 
        add constraint BANK_ACCOUNT_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table BANK_ACCOUNT 
        add constraint BANK_ACCOUNT_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table COURSE 
        add constraint COURSE_CV_SECTION_PRESET_FKC 
        foreign key (CV_SECTION_PRESET_FK) 
        references CV_SECTION;

    alter table COURSE 
        add constraint COURSE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table COURSE 
        add constraint COURSE_INSTITUTION_FKC 
        foreign key (INSTITUTION_FK) 
        references STAFF;

    alter table COURSE 
        add constraint COURSE_TRAINING_RECORD_SECTION_PRESET_FKC 
        foreign key (TRAINING_RECORD_SECTION_PRESET_FK) 
        references TRAINING_RECORD_SECTION;

    alter table COURSE 
        add constraint COURSE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table COURSE 
        add constraint COURSE_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references COURSE_CATEGORY;

    alter table COURSE 
        add constraint COURSE_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_TRAINING_RECORD_SECTION_FC 
        foreign key (TRAINING_RECORD_SECTION_FK) 
        references TRAINING_RECORD_SECTION;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_CV_SECTION_FKC 
        foreign key (CV_SECTION_FK) 
        references CV_SECTION;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_STATUS_FKC 
        foreign key (STATUS_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        add constraint COURSE_PARTICIPATION_STATUS_ENTRY_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table CRITERIA 
        add constraint CRITERIA_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table CRITERION 
        add constraint CRITERION_CRITERIA_FKC 
        foreign key (CRITERIA_FK) 
        references CRITERIA;

    alter table CV_POSITION 
        add constraint CV_POSITION_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table CV_POSITION 
        add constraint CV_POSITION_INSTITUTION_FKC 
        foreign key (INSTITUTION_FK) 
        references STAFF;

    alter table CV_POSITION 
        add constraint CV_POSITION_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table CV_POSITION 
        add constraint CV_POSITION_SECTION_FKC 
        foreign key (SECTION_FK) 
        references CV_SECTION;

    alter table DATA_TABLE_COLUMN 
        add constraint DATA_TABLE_COLUMN_USER_FKC 
        foreign key (USER_FK) 
        references users;

    alter table DIAGNOSIS 
        add constraint DIAGNOSIS_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table DIAGNOSIS 
        add constraint DIAGNOSIS_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table DIAGNOSIS 
        add constraint DIAGNOSIS_CODE_FKC 
        foreign key (CODE_FK) 
        references ALPHA_ID;

    alter table DUTY_ROSTER_TURN 
        add constraint DUTY_ROSTER_TURN_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table DUTY_ROSTER_TURN 
        add constraint DUTY_ROSTER_TURN_VISIT_SCHEDULE_ITEM_FKC 
        foreign key (VISIT_SCHEDULE_ITEM_FK) 
        references VISIT_SCHEDULE_ITEM;

    alter table DUTY_ROSTER_TURN 
        add constraint DUTY_ROSTER_TURN_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table DUTY_ROSTER_TURN 
        add constraint DUTY_ROSTER_TURN_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table FILE 
        add constraint FILE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table FILE 
        add constraint FILE_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table FILE 
        add constraint FILE_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table FILE 
        add constraint FILE_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table FILE 
        add constraint FILE_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table FILE 
        add constraint FILE_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table FILE 
        add constraint FILE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table FILE 
        add constraint FILE_MASS_MAIL_FKC 
        foreign key (MASS_MAIL_FK) 
        references MASS_MAIL;

    alter table HYPERLINK 
        add constraint HYPERLINK_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table HYPERLINK 
        add constraint HYPERLINK_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table HYPERLINK 
        add constraint HYPERLINK_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table HYPERLINK 
        add constraint HYPERLINK_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table HYPERLINK 
        add constraint HYPERLINK_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table HYPERLINK 
        add constraint HYPERLINK_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references HYPERLINK_CATEGORY;

    alter table ICD_SYST_BLOCK 
        add constraint ICD_SYST_BLOCK_ICD_SYST_FKC 
        foreign key (ICD_SYST_FK) 
        references ICD_SYST;

    alter table ICD_SYST_CATEGORY 
        add constraint ICD_SYST_CATEGORY_ICD_SYST_FKC 
        foreign key (ICD_SYST_FK) 
        references ICD_SYST;

    alter table ICD_SYST_MODIFIER 
        add constraint ICD_SYST_MODIFIER_ICD_SYST_CATEGORY_FKC 
        foreign key (ICD_SYST_CATEGORY_FK) 
        references ICD_SYST_CATEGORY;

    alter table INPUT_FIELD 
        add constraint INPUT_FIELD_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INPUT_FIELD 
        add constraint INPUT_FIELD_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table INPUT_FIELD_SELECTION_SET_VALUE 
        add constraint INPUT_FIELD_SELECTION_SET_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INPUT_FIELD_SELECTION_SET_VALUE 
        add constraint INPUT_FIELD_SELECTION_SET_VALUE_FIELD_FKC 
        foreign key (FIELD_FK) 
        references INPUT_FIELD;

    alter table INQUIRY 
        add constraint INQUIRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INQUIRY 
        add constraint INQUIRY_FIELD_FKC 
        foreign key (FIELD_FK) 
        references INPUT_FIELD;

    alter table INQUIRY 
        add constraint INQUIRY_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table INQUIRY_VALUE 
        add constraint INQUIRY_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INQUIRY_VALUE 
        add constraint INQUIRY_VALUE_VALUE_FKC 
        foreign key (VALUE_FK) 
        references INPUT_FIELD_VALUE;

    alter table INQUIRY_VALUE 
        add constraint INQUIRY_VALUE_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table INQUIRY_VALUE 
        add constraint INQUIRY_VALUE_INQUIRY_FKC 
        foreign key (INQUIRY_FK) 
        references INQUIRY;

    alter table INVENTORY 
        add constraint INVENTORY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INVENTORY 
        add constraint INVENTORY_OWNER_FKC 
        foreign key (OWNER_FK) 
        references STAFF;

    alter table INVENTORY 
        add constraint INVENTORY_PARENT_FKC 
        foreign key (PARENT_FK) 
        references INVENTORY;

    alter table INVENTORY 
        add constraint INVENTORY_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references INVENTORY_CATEGORY;

    alter table INVENTORY 
        add constraint INVENTORY_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_ON_BEHALF_OF_FKC 
        foreign key (ON_BEHALF_OF_FK) 
        references STAFF;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table INVENTORY_BOOKING 
        add constraint INVENTORY_BOOKING_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table INVENTORY_STATUS_ENTRY 
        add constraint INVENTORY_STATUS_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INVENTORY_STATUS_ENTRY 
        add constraint INVENTORY_STATUS_ENTRY_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table INVENTORY_STATUS_ENTRY 
        add constraint INVENTORY_STATUS_ENTRY_ORIGINATOR_FKC 
        foreign key (ORIGINATOR_FK) 
        references STAFF;

    alter table INVENTORY_STATUS_ENTRY 
        add constraint INVENTORY_STATUS_ENTRY_TYPE_FKC 
        foreign key (TYPE_FK) 
        references INVENTORY_STATUS_TYPE;

    alter table INVENTORY_STATUS_ENTRY 
        add constraint INVENTORY_STATUS_ENTRY_ADDRESSEE_FKC 
        foreign key (ADDRESSEE_FK) 
        references STAFF;

    alter table INVENTORY_TAG_VALUE 
        add constraint INVENTORY_TAG_VALUE_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table INVENTORY_TAG_VALUE 
        add constraint INVENTORY_TAG_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table INVENTORY_TAG_VALUE 
        add constraint INVENTORY_TAG_VALUE_TAG_FKC 
        foreign key (TAG_FK) 
        references INVENTORY_TAG;

    alter table JOB 
        add constraint JOB_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table JOB 
        add constraint JOB_INPUT_FIELD_FKC 
        foreign key (INPUT_FIELD_FK) 
        references INPUT_FIELD;

    alter table JOB 
        add constraint JOB_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table JOB 
        add constraint JOB_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table JOB 
        add constraint JOB_CRITERIA_FKC 
        foreign key (CRITERIA_FK) 
        references CRITERIA;

    alter table JOB 
        add constraint JOB_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table JOB 
        add constraint JOB_TYPE_FKC 
        foreign key (TYPE_FK) 
        references JOB_TYPE;

    alter table JOB_TYPE 
        add constraint JOB_TYPE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_INPUT_FIELD_FKC 
        foreign key (INPUT_FIELD_FK) 
        references INPUT_FIELD;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_USER_FKC 
        foreign key (USER_FK) 
        references users;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_CRITERIA_FKC 
        foreign key (CRITERIA_FK) 
        references CRITERIA;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references JOURNAL_CATEGORY;

    alter table JOURNAL_ENTRY 
        add constraint JOURNAL_ENTRY_MASS_MAIL_FKC 
        foreign key (MASS_MAIL_FK) 
        references MASS_MAIL;

    alter table LECTURER 
        add constraint LECTURER_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table LECTURER 
        add constraint LECTURER_COMPETENCE_FKC 
        foreign key (COMPETENCE_FK) 
        references LECTURER_COMPETENCE;

    alter table LECTURER 
        add constraint LECTURER_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table LECTURER 
        add constraint LECTURER_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_INVENTORY_FKC 
        foreign key (INVENTORY_FK) 
        references INVENTORY;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_RESPONSIBLE_PERSON_FKC 
        foreign key (RESPONSIBLE_PERSON_FK) 
        references STAFF;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_COMPANY_CONTACT_FKC 
        foreign key (COMPANY_CONTACT_FK) 
        references STAFF;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_TYPE_FKC 
        foreign key (TYPE_FK) 
        references MAINTENANCE_TYPE;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        add constraint MAINTENANCE_SCHEDULE_ITEM_RESPONSIBLE_PERSON_PROXY_FKC 
        foreign key (RESPONSIBLE_PERSON_PROXY_FK) 
        references STAFF;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_PROBAND_LIST_STATUS_FKC 
        foreign key (PROBAND_LIST_STATUS_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_STATUS_FKC 
        foreign key (STATUS_FK) 
        references MASS_MAIL_STATUS_TYPE;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_TYPE_FKC 
        foreign key (TYPE_FK) 
        references MASS_MAIL_TYPE;

    alter table MASS_MAIL 
        add constraint MASS_MAIL_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table MASS_MAIL_RECIPIENT 
        add constraint MASS_MAIL_RECIPIENT_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table MASS_MAIL_RECIPIENT 
        add constraint MASS_MAIL_RECIPIENT_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table MASS_MAIL_RECIPIENT 
        add constraint MASS_MAIL_RECIPIENT_MASS_MAIL_FKC 
        foreign key (MASS_MAIL_FK) 
        references MASS_MAIL;

    alter table MEDICATION 
        add constraint MEDICATION_PROCEDURE_FKC 
        foreign key (PROCEDURE_FK) 
        references PROCEDURE;

    alter table MEDICATION 
        add constraint MEDICATION_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table MEDICATION 
        add constraint MEDICATION_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table MEDICATION 
        add constraint MEDICATION_ASP_FKC 
        foreign key (ASP_FK) 
        references ASP;

    alter table MEDICATION 
        add constraint MEDICATION_DIAGNOSIS_FKC 
        foreign key (DIAGNOSIS_FK) 
        references DIAGNOSIS;

    alter table MONEY_TRANSFER 
        add constraint MONEY_TRANSFER_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table MONEY_TRANSFER 
        add constraint MONEY_TRANSFER_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table MONEY_TRANSFER 
        add constraint MONEY_TRANSFER_BANK_ACCOUNT_FKC 
        foreign key (BANK_ACCOUNT_FK) 
        references BANK_ACCOUNT;

    alter table MONEY_TRANSFER 
        add constraint MONEY_TRANSFER_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_INVENTORY_STATUS_ENTRY_FKC 
        foreign key (INVENTORY_STATUS_ENTRY_FK) 
        references INVENTORY_STATUS_ENTRY;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_MAINTENANCE_SCHEDULE_ITEM_FKC 
        foreign key (MAINTENANCE_SCHEDULE_ITEM_FK) 
        references MAINTENANCE_SCHEDULE_ITEM;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_PASSWORD_FKC 
        foreign key (PASSWORD_FK) 
        references PASSWORD;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_VISIT_SCHEDULE_ITEM_FKC 
        foreign key (VISIT_SCHEDULE_ITEM_FK) 
        references VISIT_SCHEDULE_ITEM;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_USER_FKC 
        foreign key (USER_FK) 
        references users;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_ECRF_STATUS_ENTRY_FKC 
        foreign key (ECRF_STATUS_ENTRY_FK) 
        references ecrf_status_entry;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_COURSE_PARTICIPATION_STATUS_ENTRY_FKC 
        foreign key (COURSE_PARTICIPATION_STATUS_ENTRY_FK) 
        references COURSE_PARTICIPATION_STATUS_ENTRY;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_ECRF_FIELD_STATUS_ENTRY_FKC 
        foreign key (ECRF_FIELD_STATUS_ENTRY_FK) 
        references ecrf_field_status_entry;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_TYPE_FKC 
        foreign key (TYPE_FK) 
        references NOTIFICATION_TYPE;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_PROBAND_STATUS_ENTRY_FKC 
        foreign key (PROBAND_STATUS_ENTRY_FK) 
        references PROBAND_STATUS_ENTRY;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_INVENTORY_BOOKING_FKC 
        foreign key (INVENTORY_BOOKING_FK) 
        references INVENTORY_BOOKING;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_DUTY_ROSTER_TURN_FKC 
        foreign key (DUTY_ROSTER_TURN_FK) 
        references DUTY_ROSTER_TURN;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_COURSE_FKC 
        foreign key (COURSE_FK) 
        references COURSE;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_STAFF_STATUS_ENTRY_FKC 
        foreign key (STAFF_STATUS_ENTRY_FK) 
        references STAFF_STATUS_ENTRY;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_TIMELINE_EVENT_FKC 
        foreign key (TIMELINE_EVENT_FK) 
        references TIMELINE_EVENT;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table NOTIFICATION 
        add constraint NOTIFICATION_TRIAL_TAG_FKC 
        foreign key (TRIAL_TAG_FK) 
        references TRIAL_TAG;

    alter table NOTIFICATION_RECIPIENT 
        add constraint NOTIFICATION_RECIPIENT_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table NOTIFICATION_RECIPIENT 
        add constraint NOTIFICATION_RECIPIENT_NOTIFICATION_FKC 
        foreign key (NOTIFICATION_FK) 
        references NOTIFICATION;

    alter table OPS_CODE 
        add constraint OPS_CODE_SYSTEMATICS_FKC 
        foreign key (SYSTEMATICS_FK) 
        references OPS_SYST;

    alter table OPS_SYST_BLOCK 
        add constraint OPS_SYST_BLOCK_OPS_SYST_FKC 
        foreign key (OPS_SYST_FK) 
        references OPS_SYST;

    alter table OPS_SYST_CATEGORY 
        add constraint OPS_SYST_CATEGORY_OPS_SYST_FKC 
        foreign key (OPS_SYST_FK) 
        references OPS_SYST;

    alter table OPS_SYST_MODIFIER 
        add constraint OPS_SYST_MODIFIER_OPS_SYST_CATEGORY_FKC 
        foreign key (OPS_SYST_CATEGORY_FK) 
        references OPS_SYST_CATEGORY;

    alter table PASSWORD 
        add constraint PASSWORD_PREVIOUS_PASSWORD_FKC 
        foreign key (PREVIOUS_PASSWORD_FK) 
        references PASSWORD;

    alter table PASSWORD 
        add constraint PASSWORD_USER_FKC 
        foreign key (USER_FK) 
        references users;

    alter table PERSON_CONTACT_PARTICULARS 
        add constraint PERSON_CONTACT_PARTICULARS_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table PROBAND 
        add constraint PROBAND_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND 
        add constraint PROBAND_PRIVACY_CONSENT_STATUS_FKC 
        foreign key (PRIVACY_CONSENT_STATUS_FK) 
        references PRIVACY_CONSENT_STATUS_TYPE;

    alter table PROBAND 
        add constraint PROBAND_ANIMAL_PARTICULARS_FKC 
        foreign key (ANIMAL_PARTICULARS_FK) 
        references ANIMAL_CONTACT_PARTICULARS;

    alter table PROBAND 
        add constraint PROBAND_PHYSICIAN_FKC 
        foreign key (PHYSICIAN_FK) 
        references STAFF;

    alter table PROBAND 
        add constraint PROBAND_PERSON_PARTICULARS_FKC 
        foreign key (PERSON_PARTICULARS_FK) 
        references PROBAND_CONTACT_PARTICULARS;

    alter table PROBAND 
        add constraint PROBAND_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references PROBAND_CATEGORY;

    alter table PROBAND 
        add constraint PROBAND_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table PROBAND_ADDRESS 
        add constraint PROBAND_ADDRESS_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_ADDRESS 
        add constraint PROBAND_ADDRESS_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROBAND_ADDRESS 
        add constraint PROBAND_ADDRESS_TYPE_FKC 
        foreign key (TYPE_FK) 
        references ADDRESS_TYPE;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        add constraint PROBAND_CONTACT_DETAIL_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        add constraint PROBAND_CONTACT_DETAIL_VALUE_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        add constraint PROBAND_CONTACT_DETAIL_VALUE_TYPE_FKC 
        foreign key (TYPE_FK) 
        references CONTACT_DETAIL_TYPE;

    alter table PROBAND_CONTACT_PARTICULARS 
        add constraint PROBAND_CONTACT_PARTICULARS_CONTENT_TYPE_FKC 
        foreign key (CONTENT_TYPE_FK) 
        references MIME_TYPE;

    alter table PROBAND_GROUP 
        add constraint PROBAND_GROUP_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_GROUP 
        add constraint PROBAND_GROUP_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table PROBAND_LIST_ENTRY 
        add constraint PROBAND_LIST_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_LIST_ENTRY 
        add constraint PROBAND_LIST_ENTRY_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROBAND_LIST_ENTRY 
        add constraint PROBAND_LIST_ENTRY_LAST_STATUS_FKC 
        foreign key (LAST_STATUS_FK) 
        references PROBAND_LIST_STATUS_ENTRY;

    alter table PROBAND_LIST_ENTRY 
        add constraint PROBAND_LIST_ENTRY_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table PROBAND_LIST_ENTRY 
        add constraint PROBAND_LIST_ENTRY_GROUP_FKC 
        foreign key (GROUP_FK) 
        references PROBAND_GROUP;

    alter table PROBAND_LIST_ENTRY_TAG 
        add constraint PROBAND_LIST_ENTRY_TAG_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_LIST_ENTRY_TAG 
        add constraint PROBAND_LIST_ENTRY_TAG_FIELD_FKC 
        foreign key (FIELD_FK) 
        references INPUT_FIELD;

    alter table PROBAND_LIST_ENTRY_TAG 
        add constraint PROBAND_LIST_ENTRY_TAG_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        add constraint PROBAND_LIST_ENTRY_TAG_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        add constraint PROBAND_LIST_ENTRY_TAG_VALUE_VALUE_FKC 
        foreign key (VALUE_FK) 
        references INPUT_FIELD_VALUE;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        add constraint PROBAND_LIST_ENTRY_TAG_VALUE_LIST_ENTRY_FKC 
        foreign key (LIST_ENTRY_FK) 
        references PROBAND_LIST_ENTRY;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        add constraint PROBAND_LIST_ENTRY_TAG_VALUE_TAG_FKC 
        foreign key (TAG_FK) 
        references PROBAND_LIST_ENTRY_TAG;

    alter table PROBAND_LIST_STATUS_ENTRY 
        add constraint PROBAND_LIST_STATUS_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_LIST_STATUS_ENTRY 
        add constraint PROBAND_LIST_STATUS_ENTRY_LIST_ENTRY_FKC 
        foreign key (LIST_ENTRY_FK) 
        references PROBAND_LIST_ENTRY;

    alter table PROBAND_LIST_STATUS_ENTRY 
        add constraint PROBAND_LIST_STATUS_ENTRY_STATUS_FKC 
        foreign key (STATUS_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table PROBAND_STATUS_ENTRY 
        add constraint PROBAND_STATUS_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_STATUS_ENTRY 
        add constraint PROBAND_STATUS_ENTRY_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROBAND_STATUS_ENTRY 
        add constraint PROBAND_STATUS_ENTRY_TYPE_FKC 
        foreign key (TYPE_FK) 
        references PROBAND_STATUS_TYPE;

    alter table PROBAND_TAG_VALUE 
        add constraint PROBAND_TAG_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROBAND_TAG_VALUE 
        add constraint PROBAND_TAG_VALUE_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROBAND_TAG_VALUE 
        add constraint PROBAND_TAG_VALUE_TAG_FKC 
        foreign key (TAG_FK) 
        references PROBAND_TAG;

    alter table PROCEDURE 
        add constraint PROCEDURE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table PROCEDURE 
        add constraint PROCEDURE_PROBAND_FKC 
        foreign key (PROBAND_FK) 
        references PROBAND;

    alter table PROCEDURE 
        add constraint PROCEDURE_CODE_FKC 
        foreign key (CODE_FK) 
        references OPS_CODE;

    alter table PROFILE_PERMISSION 
        add constraint PROFILE_PERMISSION_PERMISSION_FKC 
        foreign key (PERMISSION_FK) 
        references PERMISSION;

    alter table RANDOMIZATION_LIST_CODE 
        add constraint RANDOMIZATION_LIST_CODE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table RANDOMIZATION_LIST_CODE 
        add constraint RANDOMIZATION_LIST_CODE_RANDOMIZATION_LIST_FKC 
        foreign key (RANDOMIZATION_LIST_FK) 
        references STRATIFICATION_RANDOMIZATION_LIST;

    alter table RANDOMIZATION_LIST_CODE 
        add constraint RANDOMIZATION_LIST_CODE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table RANDOMIZATION_LIST_CODE 
        add constraint RANDOMIZATION_LIST_CODE_BREAK_USER_FKC 
        foreign key (BREAK_USER_FK) 
        references users;

    alter table RANDOMIZATION_LIST_CODE_VALUE 
        add constraint RANDOMIZATION_LIST_CODE_VALUE_CODE_FKC 
        foreign key (CODE_FK) 
        references RANDOMIZATION_LIST_CODE;

    alter table SIGNATURE 
        add constraint SIGNATURE_ECRF_STATUS_ENTRY_FKC 
        foreign key (ECRF_STATUS_ENTRY_FK) 
        references ecrf_status_entry;

    alter table SIGNATURE 
        add constraint SIGNATURE_SIGNEE_FKC 
        foreign key (SIGNEE_FK) 
        references users;

    alter table SIGNATURE 
        add constraint SIGNATURE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table STAFF 
        add constraint STAFF_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STAFF 
        add constraint STAFF_ORGANISATION_PARTICULARS_FKC 
        foreign key (ORGANISATION_PARTICULARS_FK) 
        references ORGANISATION_CONTACT_PARTICULARS;

    alter table STAFF 
        add constraint STAFF_PERSON_PARTICULARS_FKC 
        foreign key (PERSON_PARTICULARS_FK) 
        references PERSON_CONTACT_PARTICULARS;

    alter table STAFF 
        add constraint STAFF_PARENT_FKC 
        foreign key (PARENT_FK) 
        references STAFF;

    alter table STAFF 
        add constraint STAFF_CATEGORY_FKC 
        foreign key (CATEGORY_FK) 
        references STAFF_CATEGORY;

    alter table STAFF 
        add constraint STAFF_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table STAFF_ADDRESS 
        add constraint STAFF_ADDRESS_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STAFF_ADDRESS 
        add constraint STAFF_ADDRESS_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table STAFF_ADDRESS 
        add constraint STAFF_ADDRESS_TYPE_FKC 
        foreign key (TYPE_FK) 
        references ADDRESS_TYPE;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        add constraint STAFF_CONTACT_DETAIL_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        add constraint STAFF_CONTACT_DETAIL_VALUE_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        add constraint STAFF_CONTACT_DETAIL_VALUE_TYPE_FKC 
        foreign key (TYPE_FK) 
        references CONTACT_DETAIL_TYPE;

    alter table STAFF_STATUS_ENTRY 
        add constraint STAFF_STATUS_ENTRY_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STAFF_STATUS_ENTRY 
        add constraint STAFF_STATUS_ENTRY_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table STAFF_STATUS_ENTRY 
        add constraint STAFF_STATUS_ENTRY_TYPE_FKC 
        foreign key (TYPE_FK) 
        references STAFF_STATUS_TYPE;

    alter table STAFF_TAG_VALUE 
        add constraint STAFF_TAG_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STAFF_TAG_VALUE 
        add constraint STAFF_TAG_VALUE_TAG_FKC 
        foreign key (TAG_FK) 
        references STAFF_TAG;

    alter table STAFF_TAG_VALUE 
        add constraint STAFF_TAG_VALUE_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table STRATIFICATION_RANDOMIZATION_LIST 
        add constraint STRATIFICATION_RANDOMIZATION_LIST_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table STRATIFICATION_RANDOMIZATION_LIST 
        add constraint STRATIFICATION_RANDOMIZATION_LIST_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table TEAM_MEMBER 
        add constraint TEAM_MEMBER_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table TEAM_MEMBER 
        add constraint TEAM_MEMBER_STAFF_FKC 
        foreign key (STAFF_FK) 
        references STAFF;

    alter table TEAM_MEMBER 
        add constraint TEAM_MEMBER_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table TEAM_MEMBER 
        add constraint TEAM_MEMBER_ROLE_FKC 
        foreign key (ROLE_FK) 
        references TEAM_MEMBER_ROLE;

    alter table TIMELINE_EVENT 
        add constraint TIMELINE_EVENT_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table TIMELINE_EVENT 
        add constraint TIMELINE_EVENT_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table TIMELINE_EVENT 
        add constraint TIMELINE_EVENT_TYPE_FKC 
        foreign key (TYPE_FK) 
        references TIMELINE_EVENT_TYPE;

    alter table TIMELINE_EVENT 
        add constraint TIMELINE_EVENT_PARENT_FKC 
        foreign key (PARENT_FK) 
        references TIMELINE_EVENT;

    alter table TRIAL 
        add constraint TRIAL_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table TRIAL 
        add constraint TRIAL_SPONSORING_FKC 
        foreign key (SPONSORING_FK) 
        references SPONSORING_TYPE;

    alter table TRIAL 
        add constraint TRIAL_STATUS_FKC 
        foreign key (STATUS_FK) 
        references TRIAL_STATUS_TYPE;

    alter table TRIAL 
        add constraint TRIAL_TYPE_FKC 
        foreign key (TYPE_FK) 
        references TRIAL_TYPE;

    alter table TRIAL 
        add constraint TRIAL_SURVEY_STATUS_FKC 
        foreign key (SURVEY_STATUS_FK) 
        references SURVEY_STATUS_TYPE;

    alter table TRIAL 
        add constraint TRIAL_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table TRIAL_TAG_VALUE 
        add constraint TRIAL_TAG_VALUE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table TRIAL_TAG_VALUE 
        add constraint TRIAL_TAG_VALUE_TAG_FKC 
        foreign key (TAG_FK) 
        references TRIAL_TAG;

    alter table TRIAL_TAG_VALUE 
        add constraint TRIAL_TAG_VALUE_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table USER_PERMISSION_PROFILE 
        add constraint USER_PERMISSION_PROFILE_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table USER_PERMISSION_PROFILE 
        add constraint USER_PERMISSION_PROFILE_USER_FKC 
        foreign key (USER_FK) 
        references users;

    alter table VISIT 
        add constraint VISIT_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table VISIT 
        add constraint VISIT_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table VISIT 
        add constraint VISIT_TYPE_FKC 
        foreign key (TYPE_FK) 
        references VISIT_TYPE;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_START_TAG_FKC 
        foreign key (START_TAG_FK) 
        references PROBAND_LIST_ENTRY_TAG;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_STOP_TAG_FKC 
        foreign key (STOP_TAG_FK) 
        references PROBAND_LIST_ENTRY_TAG;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_GROUP_FKC 
        foreign key (GROUP_FK) 
        references PROBAND_GROUP;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table VISIT_SCHEDULE_ITEM 
        add constraint VISIT_SCHEDULE_ITEM_VISIT_FKC 
        foreign key (VISIT_FK) 
        references VISIT;

    alter table asp_ingredient 
        add constraint ASP_SUBSTANCE_ASPS_FKC 
        foreign key (ASPS_FK) 
        references ASP;

    alter table asp_ingredient 
        add constraint ASP_SUBSTANCES_FKC 
        foreign key (SUBSTANCES_FK) 
        references ASP_SUBSTANCE;

    alter table course_participation_admin_self_registration_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_ADMIN_SELF_REGISTRATION_TRC 
        foreign key (ADMIN_SELF_REGISTRATION_TRANSITIONS_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_admin_self_registration_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUP 
        foreign key (COURSE_PARTICIPATION_STATUS_TYPES_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_admin_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_ADMIN_TRANSITIONS_FKC 
        foreign key (ADMIN_TRANSITIONS_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_admin_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUN 
        foreign key (COURSE_PARTICIPATION_STATUS_TYPES_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_user_self_registration_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_USER_SELF_REGISTRATION_TRAC 
        foreign key (USER_SELF_REGISTRATION_TRANSITIONS_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_user_self_registration_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUT 
        foreign key (COURSE_PARTICIPATION_STATUS_TYPES_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_user_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_USER_TRANSITIONS_FKC 
        foreign key (USER_TRANSITIONS_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_participation_user_transition 
        add constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUC 
        foreign key (COURSE_PARTICIPATION_STATUS_TYPES_FK) 
        references COURSE_PARTICIPATION_STATUS_TYPE;

    alter table course_renewal 
        add constraint COURSE_PRECEDING_COURSES_FKC 
        foreign key (PRECEDING_COURSES_FK) 
        references COURSE;

    alter table course_renewal 
        add constraint COURSE_RENEWALS_FKC 
        foreign key (RENEWALS_FK) 
        references COURSE;

    alter table ecrf 
        add constraint ecrf_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table ecrf 
        add constraint ecrf_PROBAND_LIST_STATUS_FKC 
        foreign key (PROBAND_LIST_STATUS_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table ecrf 
        add constraint ecrf_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table ecrf_field 
        add constraint ecrf_field_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table ecrf_field 
        add constraint ecrf_field_ECRF_FKC 
        foreign key (ECRF_FK) 
        references ecrf;

    alter table ecrf_field 
        add constraint ecrf_field_FIELD_FKC 
        foreign key (FIELD_FK) 
        references INPUT_FIELD;

    alter table ecrf_field 
        add constraint ecrf_field_TRIAL_FKC 
        foreign key (TRIAL_FK) 
        references TRIAL;

    alter table ecrf_field_status_entry 
        add constraint ecrf_field_status_entry_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table ecrf_field_status_entry 
        add constraint ecrf_field_status_entry_LIST_ENTRY_FKC 
        foreign key (LIST_ENTRY_FK) 
        references PROBAND_LIST_ENTRY;

    alter table ecrf_field_status_entry 
        add constraint ecrf_field_status_entry_ECRF_FIELD_FKC 
        foreign key (ECRF_FIELD_FK) 
        references ecrf_field;

    alter table ecrf_field_status_entry 
        add constraint ecrf_field_status_entry_STATUS_FKC 
        foreign key (STATUS_FK) 
        references ecrf_field_status_type;

    alter table ecrf_field_status_entry 
        add constraint ecrf_field_status_entry_VISIT_FKC 
        foreign key (VISIT_FK) 
        references VISIT;

    alter table ecrf_field_status_transition 
        add constraint ecrf_field_status_type_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references ecrf_field_status_type;

    alter table ecrf_field_status_transition 
        add constraint ecrf_field_status_type_E_C_R_F_FIELD_STATUS_TYPES_FKC 
        foreign key (E_C_R_F_FIELD_STATUS_TYPES_FK) 
        references ecrf_field_status_type;

    alter table ecrf_field_value 
        add constraint ecrf_field_value_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table ecrf_field_value 
        add constraint ecrf_field_value_VALUE_FKC 
        foreign key (VALUE_FK) 
        references INPUT_FIELD_VALUE;

    alter table ecrf_field_value 
        add constraint ecrf_field_value_LIST_ENTRY_FKC 
        foreign key (LIST_ENTRY_FK) 
        references PROBAND_LIST_ENTRY;

    alter table ecrf_field_value 
        add constraint ecrf_field_value_ECRF_FIELD_FKC 
        foreign key (ECRF_FIELD_FK) 
        references ecrf_field;

    alter table ecrf_field_value 
        add constraint ecrf_field_value_VISIT_FKC 
        foreign key (VISIT_FK) 
        references VISIT;

    alter table ecrf_group 
        add constraint PROBAND_GROUP_ECRFS_FKC 
        foreign key (ECRFS_FK) 
        references ecrf;

    alter table ecrf_group 
        add constraint ecrf_GROUPS_FKC 
        foreign key (GROUPS_FK) 
        references PROBAND_GROUP;

    alter table ecrf_status_entry 
        add constraint ecrf_status_entry_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table ecrf_status_entry 
        add constraint ecrf_status_entry_LIST_ENTRY_FKC 
        foreign key (LIST_ENTRY_FK) 
        references PROBAND_LIST_ENTRY;

    alter table ecrf_status_entry 
        add constraint ecrf_status_entry_STATUS_FKC 
        foreign key (STATUS_FK) 
        references ecrf_status_type;

    alter table ecrf_status_entry 
        add constraint ecrf_status_entry_ECRF_FKC 
        foreign key (ECRF_FK) 
        references ecrf;

    alter table ecrf_status_entry 
        add constraint ecrf_status_entry_VISIT_FKC 
        foreign key (VISIT_FK) 
        references VISIT;

    alter table ecrf_status_transition 
        add constraint ecrf_status_type_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references ecrf_status_type;

    alter table ecrf_status_transition 
        add constraint ecrf_status_type_E_C_R_F_STATUS_TYPES_FKC 
        foreign key (E_C_R_F_STATUS_TYPES_FK) 
        references ecrf_status_type;

    alter table ecrf_status_type_action 
        add constraint ecrf_status_type_ACTIONS_FKC 
        foreign key (ACTIONS_FK) 
        references ecrf_status_action;

    alter table ecrf_status_type_action 
        add constraint ecrf_status_action_E_C_R_F_STATUS_TYPES_FKC 
        foreign key (E_C_R_F_STATUS_TYPES_FK) 
        references ecrf_status_type;

    alter table ecrf_visit 
        add constraint ecrf_VISITS_FKC 
        foreign key (VISITS_FK) 
        references VISIT;

    alter table ecrf_visit 
        add constraint VISIT_ECRFS_FKC 
        foreign key (ECRFS_FK) 
        references ecrf;

    alter table input_field_value_selection 
        add constraint INPUT_FIELD_VALUE_SELECTION_VALUES_FKC 
        foreign key (SELECTION_VALUES_FK) 
        references INPUT_FIELD_SELECTION_SET_VALUE;

    alter table input_field_value_selection 
        add constraint INPUT_FIELD_SELECTION_SET_VALUE_INPUT_FIELD_VALUES_FKC 
        foreign key (INPUT_FIELD_VALUES_FK) 
        references INPUT_FIELD_VALUE;

    alter table mass_mail_status_transition 
        add constraint MASS_MAIL_STATUS_TYPE_MASS_MAIL_STATUS_TYPES_FKC 
        foreign key (MASS_MAIL_STATUS_TYPES_FK) 
        references MASS_MAIL_STATUS_TYPE;

    alter table mass_mail_status_transition 
        add constraint MASS_MAIL_STATUS_TYPE_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references MASS_MAIL_STATUS_TYPE;

    alter table medication_ingredient 
        add constraint ASP_SUBSTANCE_MEDICATIONS_FKC 
        foreign key (MEDICATIONS_FK) 
        references MEDICATION;

    alter table medication_ingredient 
        add constraint MEDICATION_SUBSTANCES_FKC 
        foreign key (SUBSTANCES_FK) 
        references ASP_SUBSTANCE;

    alter table privacy_consent_status_transition 
        add constraint PRIVACY_CONSENT_STATUS_TYPE_PRIVACY_CONSENT_STATUS_TYPES_FKC 
        foreign key (PRIVACY_CONSENT_STATUS_TYPES_FK) 
        references PRIVACY_CONSENT_STATUS_TYPE;

    alter table privacy_consent_status_transition 
        add constraint PRIVACY_CONSENT_STATUS_TYPE_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references PRIVACY_CONSENT_STATUS_TYPE;

    alter table proband_children 
        add constraint PROBAND_PARENTS_FKC 
        foreign key (PARENTS_FK) 
        references PROBAND;

    alter table proband_children 
        add constraint PROBAND_CHILDREN_FKC 
        foreign key (CHILDREN_FK) 
        references PROBAND;

    alter table proband_list_status_transition 
        add constraint PROBAND_LIST_STATUS_TYPE_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table proband_list_status_transition 
        add constraint PROBAND_LIST_STATUS_TYPE_PROBAND_LIST_STATUS_TYPES_FKC 
        foreign key (PROBAND_LIST_STATUS_TYPES_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table proband_list_status_type_log_level 
        add constraint PROBAND_LIST_STATUS_TYPE_LOG_LEVELS_FKC 
        foreign key (LOG_LEVELS_FK) 
        references PROBAND_LIST_STATUS_LOG_LEVEL;

    alter table proband_list_status_type_log_level 
        add constraint PROBAND_LIST_STATUS_LOG_LEVEL_PROBAND_LIST_STATUS_TYPES_FKC 
        foreign key (PROBAND_LIST_STATUS_TYPES_FK) 
        references PROBAND_LIST_STATUS_TYPE;

    alter table send_department_staff_category 
        add constraint NOTIFICATION_TYPE_SEND_DEPARTMENT_STAFF_CATEGORIES_FKC 
        foreign key (SEND_DEPARTMENT_STAFF_CATEGORIES_FK) 
        references STAFF_CATEGORY;

    alter table send_department_staff_category 
        add constraint STAFF_CATEGORY_SEND_DEPARTMENT_NOTIFICATION_TYPES_FKC 
        foreign key (SEND_DEPARTMENT_NOTIFICATION_TYPES_FK) 
        references NOTIFICATION_TYPE;

    alter table stratification_randomization_list_selection_set_value 
        add constraint INPUT_FIELD_SELECTION_SET_VALUE_RANDOMIZATION_LISTS_FKC 
        foreign key (RANDOMIZATION_LISTS_FK) 
        references STRATIFICATION_RANDOMIZATION_LIST;

    alter table stratification_randomization_list_selection_set_value 
        add constraint STRATIFICATION_RANDOMIZATION_LIST_SELECTION_SET_VALUES_FKC 
        foreign key (SELECTION_SET_VALUES_FK) 
        references INPUT_FIELD_SELECTION_SET_VALUE;

    alter table trial_status_transition 
        add constraint TRIAL_STATUS_TYPE_TRIAL_STATUS_TYPES_FKC 
        foreign key (TRIAL_STATUS_TYPES_FK) 
        references TRIAL_STATUS_TYPE;

    alter table trial_status_transition 
        add constraint TRIAL_STATUS_TYPE_TRANSITIONS_FKC 
        foreign key (TRANSITIONS_FK) 
        references TRIAL_STATUS_TYPE;

    alter table trial_status_type_action 
        add constraint TRIAL_STATUS_ACTION_TRIAL_STATUS_TYPES_FKC 
        foreign key (TRIAL_STATUS_TYPES_FK) 
        references TRIAL_STATUS_TYPE;

    alter table trial_status_type_action 
        add constraint TRIAL_STATUS_TYPE_ACTIONS_FKC 
        foreign key (ACTIONS_FK) 
        references TRIAL_STATUS_ACTION;

    alter table users 
        add constraint users_MODIFIED_USER_FKC 
        foreign key (MODIFIED_USER_FK) 
        references users;

    alter table users 
        add constraint users_IDENTITY_FKC 
        foreign key (IDENTITY_FK) 
        references STAFF;

    alter table users 
        add constraint users_DEPARTMENT_FKC 
        foreign key (DEPARTMENT_FK) 
        references DEPARTMENT;

    alter table users 
        add constraint users_KEY_PAIR_FKC 
        foreign key (KEY_PAIR_FK) 
        references KEY_PAIR;

    alter table valid_criterion_property_restriction 
        add constraint CRITERION_RESTRICTION_CRITERION_PROPERTIES_FKC 
        foreign key (CRITERION_PROPERTIES_FK) 
        references CRITERION_PROPERTY;

    alter table valid_criterion_property_restriction 
        add constraint CRITERION_PROPERTY_VALID_RESTRICTIONS_FKC 
        foreign key (VALID_RESTRICTIONS_FK) 
        references CRITERION_RESTRICTION;

    create sequence hibernate_sequence;
