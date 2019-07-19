
    alter table ALPHA_ID 
        drop constraint ALPHA_ID_SYSTEMATICS_FKC;

    alter table ANIMAL_CONTACT_PARTICULARS 
        drop constraint ANIMAL_CONTACT_PARTICULARS_CONTENT_TYPE_FKC;

    alter table ASP_ATC_CODE 
        drop constraint ASP_ATC_CODE_ASP_FKC;

    alter table BANK_ACCOUNT 
        drop constraint BANK_ACCOUNT_MODIFIED_USER_FKC;

    alter table BANK_ACCOUNT 
        drop constraint BANK_ACCOUNT_PROBAND_FKC;

    alter table COURSE 
        drop constraint COURSE_CV_SECTION_PRESET_FKC;

    alter table COURSE 
        drop constraint COURSE_MODIFIED_USER_FKC;

    alter table COURSE 
        drop constraint COURSE_INSTITUTION_FKC;

    alter table COURSE 
        drop constraint COURSE_TRIAL_FKC;

    alter table COURSE 
        drop constraint COURSE_CATEGORY_FKC;

    alter table COURSE 
        drop constraint COURSE_DEPARTMENT_FKC;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        drop constraint COURSE_PARTICIPATION_STATUS_ENTRY_MODIFIED_USER_FKC;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        drop constraint COURSE_PARTICIPATION_STATUS_ENTRY_COURSE_FKC;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        drop constraint COURSE_PARTICIPATION_STATUS_ENTRY_STATUS_FKC;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        drop constraint COURSE_PARTICIPATION_STATUS_ENTRY_STAFF_FKC;

    alter table COURSE_PARTICIPATION_STATUS_ENTRY 
        drop constraint COURSE_PARTICIPATION_STATUS_ENTRY_SECTION_FKC;

    alter table CRITERIA 
        drop constraint CRITERIA_MODIFIED_USER_FKC;

    alter table CRITERION 
        drop constraint CRITERION_CRITERIA_FKC;

    alter table CV_POSITION 
        drop constraint CV_POSITION_MODIFIED_USER_FKC;

    alter table CV_POSITION 
        drop constraint CV_POSITION_INSTITUTION_FKC;

    alter table CV_POSITION 
        drop constraint CV_POSITION_STAFF_FKC;

    alter table CV_POSITION 
        drop constraint CV_POSITION_SECTION_FKC;

    alter table DATA_TABLE_COLUMN 
        drop constraint DATA_TABLE_COLUMN_USER_FKC;

    alter table DIAGNOSIS 
        drop constraint DIAGNOSIS_MODIFIED_USER_FKC;

    alter table DIAGNOSIS 
        drop constraint DIAGNOSIS_PROBAND_FKC;

    alter table DIAGNOSIS 
        drop constraint DIAGNOSIS_CODE_FKC;

    alter table DUTY_ROSTER_TURN 
        drop constraint DUTY_ROSTER_TURN_MODIFIED_USER_FKC;

    alter table DUTY_ROSTER_TURN 
        drop constraint DUTY_ROSTER_TURN_VISIT_SCHEDULE_ITEM_FKC;

    alter table DUTY_ROSTER_TURN 
        drop constraint DUTY_ROSTER_TURN_STAFF_FKC;

    alter table DUTY_ROSTER_TURN 
        drop constraint DUTY_ROSTER_TURN_TRIAL_FKC;

    alter table FILE 
        drop constraint FILE_MODIFIED_USER_FKC;

    alter table FILE 
        drop constraint FILE_INVENTORY_FKC;

    alter table FILE 
        drop constraint FILE_PROBAND_FKC;

    alter table FILE 
        drop constraint FILE_CONTENT_TYPE_FKC;

    alter table FILE 
        drop constraint FILE_COURSE_FKC;

    alter table FILE 
        drop constraint FILE_STAFF_FKC;

    alter table FILE 
        drop constraint FILE_TRIAL_FKC;

    alter table FILE 
        drop constraint FILE_MASS_MAIL_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_MODIFIED_USER_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_INVENTORY_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_COURSE_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_STAFF_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_TRIAL_FKC;

    alter table HYPERLINK 
        drop constraint HYPERLINK_CATEGORY_FKC;

    alter table ICD_SYST_BLOCK 
        drop constraint ICD_SYST_BLOCK_ICD_SYST_FKC;

    alter table ICD_SYST_CATEGORY 
        drop constraint ICD_SYST_CATEGORY_ICD_SYST_FKC;

    alter table ICD_SYST_MODIFIER 
        drop constraint ICD_SYST_MODIFIER_ICD_SYST_CATEGORY_FKC;

    alter table INPUT_FIELD 
        drop constraint INPUT_FIELD_MODIFIED_USER_FKC;

    alter table INPUT_FIELD 
        drop constraint INPUT_FIELD_CONTENT_TYPE_FKC;

    alter table INPUT_FIELD_SELECTION_SET_VALUE 
        drop constraint INPUT_FIELD_SELECTION_SET_VALUE_MODIFIED_USER_FKC;

    alter table INPUT_FIELD_SELECTION_SET_VALUE 
        drop constraint INPUT_FIELD_SELECTION_SET_VALUE_FIELD_FKC;

    alter table INQUIRY 
        drop constraint INQUIRY_MODIFIED_USER_FKC;

    alter table INQUIRY 
        drop constraint INQUIRY_FIELD_FKC;

    alter table INQUIRY 
        drop constraint INQUIRY_TRIAL_FKC;

    alter table INQUIRY_VALUE 
        drop constraint INQUIRY_VALUE_MODIFIED_USER_FKC;

    alter table INQUIRY_VALUE 
        drop constraint INQUIRY_VALUE_VALUE_FKC;

    alter table INQUIRY_VALUE 
        drop constraint INQUIRY_VALUE_PROBAND_FKC;

    alter table INQUIRY_VALUE 
        drop constraint INQUIRY_VALUE_INQUIRY_FKC;

    alter table INVENTORY 
        drop constraint INVENTORY_MODIFIED_USER_FKC;

    alter table INVENTORY 
        drop constraint INVENTORY_OWNER_FKC;

    alter table INVENTORY 
        drop constraint INVENTORY_PARENT_FKC;

    alter table INVENTORY 
        drop constraint INVENTORY_CATEGORY_FKC;

    alter table INVENTORY 
        drop constraint INVENTORY_DEPARTMENT_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_INVENTORY_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_MODIFIED_USER_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_PROBAND_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_ON_BEHALF_OF_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_COURSE_FKC;

    alter table INVENTORY_BOOKING 
        drop constraint INVENTORY_BOOKING_TRIAL_FKC;

    alter table INVENTORY_STATUS_ENTRY 
        drop constraint INVENTORY_STATUS_ENTRY_MODIFIED_USER_FKC;

    alter table INVENTORY_STATUS_ENTRY 
        drop constraint INVENTORY_STATUS_ENTRY_INVENTORY_FKC;

    alter table INVENTORY_STATUS_ENTRY 
        drop constraint INVENTORY_STATUS_ENTRY_ORIGINATOR_FKC;

    alter table INVENTORY_STATUS_ENTRY 
        drop constraint INVENTORY_STATUS_ENTRY_TYPE_FKC;

    alter table INVENTORY_STATUS_ENTRY 
        drop constraint INVENTORY_STATUS_ENTRY_ADDRESSEE_FKC;

    alter table INVENTORY_TAG_VALUE 
        drop constraint INVENTORY_TAG_VALUE_INVENTORY_FKC;

    alter table INVENTORY_TAG_VALUE 
        drop constraint INVENTORY_TAG_VALUE_MODIFIED_USER_FKC;

    alter table INVENTORY_TAG_VALUE 
        drop constraint INVENTORY_TAG_VALUE_TAG_FKC;

    alter table JOB 
        drop constraint JOB_MODIFIED_USER_FKC;

    alter table JOB 
        drop constraint JOB_INPUT_FIELD_FKC;

    alter table JOB 
        drop constraint JOB_PROBAND_FKC;

    alter table JOB 
        drop constraint JOB_CONTENT_TYPE_FKC;

    alter table JOB 
        drop constraint JOB_CRITERIA_FKC;

    alter table JOB 
        drop constraint JOB_TRIAL_FKC;

    alter table JOB 
        drop constraint JOB_TYPE_FKC;

    alter table JOB_TYPE 
        drop constraint JOB_TYPE_TRIAL_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_MODIFIED_USER_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_INVENTORY_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_INPUT_FIELD_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_PROBAND_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_USER_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_COURSE_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_CRITERIA_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_STAFF_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_TRIAL_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_CATEGORY_FKC;

    alter table JOURNAL_ENTRY 
        drop constraint JOURNAL_ENTRY_MASS_MAIL_FKC;

    alter table LECTURER 
        drop constraint LECTURER_MODIFIED_USER_FKC;

    alter table LECTURER 
        drop constraint LECTURER_COMPETENCE_FKC;

    alter table LECTURER 
        drop constraint LECTURER_COURSE_FKC;

    alter table LECTURER 
        drop constraint LECTURER_STAFF_FKC;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        drop constraint MAINTENANCE_SCHEDULE_ITEM_INVENTORY_FKC;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        drop constraint MAINTENANCE_SCHEDULE_ITEM_MODIFIED_USER_FKC;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        drop constraint MAINTENANCE_SCHEDULE_ITEM_RESPONSIBLE_PERSON_FKC;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        drop constraint MAINTENANCE_SCHEDULE_ITEM_COMPANY_CONTACT_FKC;

    alter table MAINTENANCE_SCHEDULE_ITEM 
        drop constraint MAINTENANCE_SCHEDULE_ITEM_TYPE_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_MODIFIED_USER_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_PROBAND_LIST_STATUS_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_STATUS_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_TRIAL_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_TYPE_FKC;

    alter table MASS_MAIL 
        drop constraint MASS_MAIL_DEPARTMENT_FKC;

    alter table MASS_MAIL_RECIPIENT 
        drop constraint MASS_MAIL_RECIPIENT_MODIFIED_USER_FKC;

    alter table MASS_MAIL_RECIPIENT 
        drop constraint MASS_MAIL_RECIPIENT_PROBAND_FKC;

    alter table MASS_MAIL_RECIPIENT 
        drop constraint MASS_MAIL_RECIPIENT_MASS_MAIL_FKC;

    alter table MEDICATION 
        drop constraint MEDICATION_PROCEDURE_FKC;

    alter table MEDICATION 
        drop constraint MEDICATION_MODIFIED_USER_FKC;

    alter table MEDICATION 
        drop constraint MEDICATION_PROBAND_FKC;

    alter table MEDICATION 
        drop constraint MEDICATION_ASP_FKC;

    alter table MEDICATION 
        drop constraint MEDICATION_DIAGNOSIS_FKC;

    alter table MONEY_TRANSFER 
        drop constraint MONEY_TRANSFER_MODIFIED_USER_FKC;

    alter table MONEY_TRANSFER 
        drop constraint MONEY_TRANSFER_PROBAND_FKC;

    alter table MONEY_TRANSFER 
        drop constraint MONEY_TRANSFER_BANK_ACCOUNT_FKC;

    alter table MONEY_TRANSFER 
        drop constraint MONEY_TRANSFER_TRIAL_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_INVENTORY_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_PROBAND_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_MAINTENANCE_SCHEDULE_ITEM_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_PASSWORD_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_VISIT_SCHEDULE_ITEM_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_USER_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_ECRF_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_COURSE_PARTICIPATION_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_ECRF_FIELD_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_STAFF_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_TRIAL_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_TYPE_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_PROBAND_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_INVENTORY_BOOKING_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_DUTY_ROSTER_TURN_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_COURSE_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_STAFF_STATUS_ENTRY_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_TIMELINE_EVENT_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_DEPARTMENT_FKC;

    alter table NOTIFICATION 
        drop constraint NOTIFICATION_TRIAL_TAG_FKC;

    alter table NOTIFICATION_RECIPIENT 
        drop constraint NOTIFICATION_RECIPIENT_STAFF_FKC;

    alter table NOTIFICATION_RECIPIENT 
        drop constraint NOTIFICATION_RECIPIENT_NOTIFICATION_FKC;

    alter table OPS_CODE 
        drop constraint OPS_CODE_SYSTEMATICS_FKC;

    alter table OPS_SYST_BLOCK 
        drop constraint OPS_SYST_BLOCK_OPS_SYST_FKC;

    alter table OPS_SYST_CATEGORY 
        drop constraint OPS_SYST_CATEGORY_OPS_SYST_FKC;

    alter table OPS_SYST_MODIFIER 
        drop constraint OPS_SYST_MODIFIER_OPS_SYST_CATEGORY_FKC;

    alter table PASSWORD 
        drop constraint PASSWORD_PREVIOUS_PASSWORD_FKC;

    alter table PASSWORD 
        drop constraint PASSWORD_USER_FKC;

    alter table PERSON_CONTACT_PARTICULARS 
        drop constraint PERSON_CONTACT_PARTICULARS_CONTENT_TYPE_FKC;

    alter table PROBAND 
        drop constraint PROBAND_MODIFIED_USER_FKC;

    alter table PROBAND 
        drop constraint PROBAND_PRIVACY_CONSENT_STATUS_FKC;

    alter table PROBAND 
        drop constraint PROBAND_ANIMAL_PARTICULARS_FKC;

    alter table PROBAND 
        drop constraint PROBAND_PHYSICIAN_FKC;

    alter table PROBAND 
        drop constraint PROBAND_PERSON_PARTICULARS_FKC;

    alter table PROBAND 
        drop constraint PROBAND_CATEGORY_FKC;

    alter table PROBAND 
        drop constraint PROBAND_DEPARTMENT_FKC;

    alter table PROBAND_ADDRESS 
        drop constraint PROBAND_ADDRESS_MODIFIED_USER_FKC;

    alter table PROBAND_ADDRESS 
        drop constraint PROBAND_ADDRESS_PROBAND_FKC;

    alter table PROBAND_ADDRESS 
        drop constraint PROBAND_ADDRESS_TYPE_FKC;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        drop constraint PROBAND_CONTACT_DETAIL_VALUE_MODIFIED_USER_FKC;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        drop constraint PROBAND_CONTACT_DETAIL_VALUE_PROBAND_FKC;

    alter table PROBAND_CONTACT_DETAIL_VALUE 
        drop constraint PROBAND_CONTACT_DETAIL_VALUE_TYPE_FKC;

    alter table PROBAND_CONTACT_PARTICULARS 
        drop constraint PROBAND_CONTACT_PARTICULARS_CONTENT_TYPE_FKC;

    alter table PROBAND_GROUP 
        drop constraint PROBAND_GROUP_MODIFIED_USER_FKC;

    alter table PROBAND_GROUP 
        drop constraint PROBAND_GROUP_TRIAL_FKC;

    alter table PROBAND_LIST_ENTRY 
        drop constraint PROBAND_LIST_ENTRY_MODIFIED_USER_FKC;

    alter table PROBAND_LIST_ENTRY 
        drop constraint PROBAND_LIST_ENTRY_PROBAND_FKC;

    alter table PROBAND_LIST_ENTRY 
        drop constraint PROBAND_LIST_ENTRY_LAST_STATUS_FKC;

    alter table PROBAND_LIST_ENTRY 
        drop constraint PROBAND_LIST_ENTRY_TRIAL_FKC;

    alter table PROBAND_LIST_ENTRY 
        drop constraint PROBAND_LIST_ENTRY_GROUP_FKC;

    alter table PROBAND_LIST_ENTRY_TAG 
        drop constraint PROBAND_LIST_ENTRY_TAG_MODIFIED_USER_FKC;

    alter table PROBAND_LIST_ENTRY_TAG 
        drop constraint PROBAND_LIST_ENTRY_TAG_FIELD_FKC;

    alter table PROBAND_LIST_ENTRY_TAG 
        drop constraint PROBAND_LIST_ENTRY_TAG_TRIAL_FKC;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        drop constraint PROBAND_LIST_ENTRY_TAG_VALUE_MODIFIED_USER_FKC;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        drop constraint PROBAND_LIST_ENTRY_TAG_VALUE_VALUE_FKC;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        drop constraint PROBAND_LIST_ENTRY_TAG_VALUE_LIST_ENTRY_FKC;

    alter table PROBAND_LIST_ENTRY_TAG_VALUE 
        drop constraint PROBAND_LIST_ENTRY_TAG_VALUE_TAG_FKC;

    alter table PROBAND_LIST_STATUS_ENTRY 
        drop constraint PROBAND_LIST_STATUS_ENTRY_MODIFIED_USER_FKC;

    alter table PROBAND_LIST_STATUS_ENTRY 
        drop constraint PROBAND_LIST_STATUS_ENTRY_LIST_ENTRY_FKC;

    alter table PROBAND_LIST_STATUS_ENTRY 
        drop constraint PROBAND_LIST_STATUS_ENTRY_STATUS_FKC;

    alter table PROBAND_STATUS_ENTRY 
        drop constraint PROBAND_STATUS_ENTRY_MODIFIED_USER_FKC;

    alter table PROBAND_STATUS_ENTRY 
        drop constraint PROBAND_STATUS_ENTRY_PROBAND_FKC;

    alter table PROBAND_STATUS_ENTRY 
        drop constraint PROBAND_STATUS_ENTRY_TYPE_FKC;

    alter table PROBAND_TAG_VALUE 
        drop constraint PROBAND_TAG_VALUE_MODIFIED_USER_FKC;

    alter table PROBAND_TAG_VALUE 
        drop constraint PROBAND_TAG_VALUE_PROBAND_FKC;

    alter table PROBAND_TAG_VALUE 
        drop constraint PROBAND_TAG_VALUE_TAG_FKC;

    alter table PROCEDURE 
        drop constraint PROCEDURE_MODIFIED_USER_FKC;

    alter table PROCEDURE 
        drop constraint PROCEDURE_PROBAND_FKC;

    alter table PROCEDURE 
        drop constraint PROCEDURE_CODE_FKC;

    alter table PROFILE_PERMISSION 
        drop constraint PROFILE_PERMISSION_PERMISSION_FKC;

    alter table SIGNATURE 
        drop constraint SIGNATURE_ECRF_STATUS_ENTRY_FKC;

    alter table SIGNATURE 
        drop constraint SIGNATURE_SIGNEE_FKC;

    alter table SIGNATURE 
        drop constraint SIGNATURE_TRIAL_FKC;

    alter table STAFF 
        drop constraint STAFF_MODIFIED_USER_FKC;

    alter table STAFF 
        drop constraint STAFF_ORGANISATION_PARTICULARS_FKC;

    alter table STAFF 
        drop constraint STAFF_PERSON_PARTICULARS_FKC;

    alter table STAFF 
        drop constraint STAFF_PARENT_FKC;

    alter table STAFF 
        drop constraint STAFF_CATEGORY_FKC;

    alter table STAFF 
        drop constraint STAFF_DEPARTMENT_FKC;

    alter table STAFF_ADDRESS 
        drop constraint STAFF_ADDRESS_MODIFIED_USER_FKC;

    alter table STAFF_ADDRESS 
        drop constraint STAFF_ADDRESS_STAFF_FKC;

    alter table STAFF_ADDRESS 
        drop constraint STAFF_ADDRESS_TYPE_FKC;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        drop constraint STAFF_CONTACT_DETAIL_VALUE_MODIFIED_USER_FKC;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        drop constraint STAFF_CONTACT_DETAIL_VALUE_STAFF_FKC;

    alter table STAFF_CONTACT_DETAIL_VALUE 
        drop constraint STAFF_CONTACT_DETAIL_VALUE_TYPE_FKC;

    alter table STAFF_STATUS_ENTRY 
        drop constraint STAFF_STATUS_ENTRY_MODIFIED_USER_FKC;

    alter table STAFF_STATUS_ENTRY 
        drop constraint STAFF_STATUS_ENTRY_STAFF_FKC;

    alter table STAFF_STATUS_ENTRY 
        drop constraint STAFF_STATUS_ENTRY_TYPE_FKC;

    alter table STAFF_TAG_VALUE 
        drop constraint STAFF_TAG_VALUE_MODIFIED_USER_FKC;

    alter table STAFF_TAG_VALUE 
        drop constraint STAFF_TAG_VALUE_TAG_FKC;

    alter table STAFF_TAG_VALUE 
        drop constraint STAFF_TAG_VALUE_STAFF_FKC;

    alter table STRATIFICATION_RANDOMIZATION_LIST 
        drop constraint STRATIFICATION_RANDOMIZATION_LIST_MODIFIED_USER_FKC;

    alter table STRATIFICATION_RANDOMIZATION_LIST 
        drop constraint STRATIFICATION_RANDOMIZATION_LIST_TRIAL_FKC;

    alter table TEAM_MEMBER 
        drop constraint TEAM_MEMBER_MODIFIED_USER_FKC;

    alter table TEAM_MEMBER 
        drop constraint TEAM_MEMBER_STAFF_FKC;

    alter table TEAM_MEMBER 
        drop constraint TEAM_MEMBER_TRIAL_FKC;

    alter table TEAM_MEMBER 
        drop constraint TEAM_MEMBER_ROLE_FKC;

    alter table TIMELINE_EVENT 
        drop constraint TIMELINE_EVENT_MODIFIED_USER_FKC;

    alter table TIMELINE_EVENT 
        drop constraint TIMELINE_EVENT_TRIAL_FKC;

    alter table TIMELINE_EVENT 
        drop constraint TIMELINE_EVENT_TYPE_FKC;

    alter table TRIAL 
        drop constraint TRIAL_MODIFIED_USER_FKC;

    alter table TRIAL 
        drop constraint TRIAL_SPONSORING_FKC;

    alter table TRIAL 
        drop constraint TRIAL_STATUS_FKC;

    alter table TRIAL 
        drop constraint TRIAL_TYPE_FKC;

    alter table TRIAL 
        drop constraint TRIAL_SURVEY_STATUS_FKC;

    alter table TRIAL 
        drop constraint TRIAL_DEPARTMENT_FKC;

    alter table TRIAL_TAG_VALUE 
        drop constraint TRIAL_TAG_VALUE_MODIFIED_USER_FKC;

    alter table TRIAL_TAG_VALUE 
        drop constraint TRIAL_TAG_VALUE_TAG_FKC;

    alter table TRIAL_TAG_VALUE 
        drop constraint TRIAL_TAG_VALUE_TRIAL_FKC;

    alter table USER_PERMISSION_PROFILE 
        drop constraint USER_PERMISSION_PROFILE_MODIFIED_USER_FKC;

    alter table USER_PERMISSION_PROFILE 
        drop constraint USER_PERMISSION_PROFILE_USER_FKC;

    alter table VISIT 
        drop constraint VISIT_MODIFIED_USER_FKC;

    alter table VISIT 
        drop constraint VISIT_TRIAL_FKC;

    alter table VISIT 
        drop constraint VISIT_TYPE_FKC;

    alter table VISIT_SCHEDULE_ITEM 
        drop constraint VISIT_SCHEDULE_ITEM_MODIFIED_USER_FKC;

    alter table VISIT_SCHEDULE_ITEM 
        drop constraint VISIT_SCHEDULE_ITEM_GROUP_FKC;

    alter table VISIT_SCHEDULE_ITEM 
        drop constraint VISIT_SCHEDULE_ITEM_TRIAL_FKC;

    alter table VISIT_SCHEDULE_ITEM 
        drop constraint VISIT_SCHEDULE_ITEM_VISIT_FKC;

    alter table asp_ingredient 
        drop constraint ASP_SUBSTANCE_ASPS_FKC;

    alter table asp_ingredient 
        drop constraint ASP_SUBSTANCES_FKC;

    alter table course_participation_admin_self_registration_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_ADMIN_SELF_REGISTRATION_TRC;

    alter table course_participation_admin_self_registration_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUP;

    alter table course_participation_admin_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_ADMIN_TRANSITIONS_FKC;

    alter table course_participation_admin_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUN;

    alter table course_participation_user_self_registration_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_USER_SELF_REGISTRATION_TRAC;

    alter table course_participation_user_self_registration_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUT;

    alter table course_participation_user_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_USER_TRANSITIONS_FKC;

    alter table course_participation_user_transition 
        drop constraint COURSE_PARTICIPATION_STATUS_TYPE_COURSE_PARTICIPATION_STATUC;

    alter table course_renewal 
        drop constraint COURSE_PRECEDING_COURSES_FKC;

    alter table course_renewal 
        drop constraint COURSE_RENEWALS_FKC;

    alter table ecrf 
        drop constraint ecrf_MODIFIED_USER_FKC;

    alter table ecrf 
        drop constraint ecrf_PROBAND_LIST_STATUS_FKC;

    alter table ecrf 
        drop constraint ecrf_TRIAL_FKC;

    alter table ecrf 
        drop constraint ecrf_GROUP_FKC;

    alter table ecrf 
        drop constraint ecrf_VISIT_FKC;

    alter table ecrf_field 
        drop constraint ecrf_field_MODIFIED_USER_FKC;

    alter table ecrf_field 
        drop constraint ecrf_field_ECRF_FKC;

    alter table ecrf_field 
        drop constraint ecrf_field_FIELD_FKC;

    alter table ecrf_field 
        drop constraint ecrf_field_TRIAL_FKC;

    alter table ecrf_field_status_entry 
        drop constraint ecrf_field_status_entry_MODIFIED_USER_FKC;

    alter table ecrf_field_status_entry 
        drop constraint ecrf_field_status_entry_LIST_ENTRY_FKC;

    alter table ecrf_field_status_entry 
        drop constraint ecrf_field_status_entry_ECRF_FIELD_FKC;

    alter table ecrf_field_status_entry 
        drop constraint ecrf_field_status_entry_STATUS_FKC;

    alter table ecrf_field_status_transition 
        drop constraint ecrf_field_status_type_TRANSITIONS_FKC;

    alter table ecrf_field_status_transition 
        drop constraint ecrf_field_status_type_E_C_R_F_FIELD_STATUS_TYPES_FKC;

    alter table ecrf_field_value 
        drop constraint ecrf_field_value_MODIFIED_USER_FKC;

    alter table ecrf_field_value 
        drop constraint ecrf_field_value_VALUE_FKC;

    alter table ecrf_field_value 
        drop constraint ecrf_field_value_LIST_ENTRY_FKC;

    alter table ecrf_field_value 
        drop constraint ecrf_field_value_ECRF_FIELD_FKC;

    alter table ecrf_status_entry 
        drop constraint ecrf_status_entry_MODIFIED_USER_FKC;

    alter table ecrf_status_entry 
        drop constraint ecrf_status_entry_LIST_ENTRY_FKC;

    alter table ecrf_status_entry 
        drop constraint ecrf_status_entry_STATUS_FKC;

    alter table ecrf_status_entry 
        drop constraint ecrf_status_entry_ECRF_FKC;

    alter table ecrf_status_transition 
        drop constraint ecrf_status_type_TRANSITIONS_FKC;

    alter table ecrf_status_transition 
        drop constraint ecrf_status_type_E_C_R_F_STATUS_TYPES_FKC;

    alter table ecrf_status_type_action 
        drop constraint ecrf_status_type_ACTIONS_FKC;

    alter table ecrf_status_type_action 
        drop constraint ecrf_status_action_E_C_R_F_STATUS_TYPES_FKC;

    alter table input_field_value_selection 
        drop constraint INPUT_FIELD_VALUE_SELECTION_VALUES_FKC;

    alter table input_field_value_selection 
        drop constraint INPUT_FIELD_SELECTION_SET_VALUE_INPUT_FIELD_VALUES_FKC;

    alter table mass_mail_status_transition 
        drop constraint MASS_MAIL_STATUS_TYPE_MASS_MAIL_STATUS_TYPES_FKC;

    alter table mass_mail_status_transition 
        drop constraint MASS_MAIL_STATUS_TYPE_TRANSITIONS_FKC;

    alter table medication_ingredient 
        drop constraint ASP_SUBSTANCE_MEDICATIONS_FKC;

    alter table medication_ingredient 
        drop constraint MEDICATION_SUBSTANCES_FKC;

    alter table privacy_consent_status_transition 
        drop constraint PRIVACY_CONSENT_STATUS_TYPE_PRIVACY_CONSENT_STATUS_TYPES_FKC;

    alter table privacy_consent_status_transition 
        drop constraint PRIVACY_CONSENT_STATUS_TYPE_TRANSITIONS_FKC;

    alter table proband_children 
        drop constraint PROBAND_PARENTS_FKC;

    alter table proband_children 
        drop constraint PROBAND_CHILDREN_FKC;

    alter table proband_list_status_transition 
        drop constraint PROBAND_LIST_STATUS_TYPE_TRANSITIONS_FKC;

    alter table proband_list_status_transition 
        drop constraint PROBAND_LIST_STATUS_TYPE_PROBAND_LIST_STATUS_TYPES_FKC;

    alter table proband_list_status_type_log_level 
        drop constraint PROBAND_LIST_STATUS_TYPE_LOG_LEVELS_FKC;

    alter table proband_list_status_type_log_level 
        drop constraint PROBAND_LIST_STATUS_LOG_LEVEL_PROBAND_LIST_STATUS_TYPES_FKC;

    alter table send_department_staff_category 
        drop constraint NOTIFICATION_TYPE_SEND_DEPARTMENT_STAFF_CATEGORIES_FKC;

    alter table send_department_staff_category 
        drop constraint STAFF_CATEGORY_SEND_DEPARTMENT_NOTIFICATION_TYPES_FKC;

    alter table stratification_randomization_list_selection_set_value 
        drop constraint INPUT_FIELD_SELECTION_SET_VALUE_RANDOMIZATION_LISTS_FKC;

    alter table stratification_randomization_list_selection_set_value 
        drop constraint STRATIFICATION_RANDOMIZATION_LIST_SELECTION_SET_VALUES_FKC;

    alter table trial_status_transition 
        drop constraint TRIAL_STATUS_TYPE_TRIAL_STATUS_TYPES_FKC;

    alter table trial_status_transition 
        drop constraint TRIAL_STATUS_TYPE_TRANSITIONS_FKC;

    alter table trial_status_type_action 
        drop constraint TRIAL_STATUS_ACTION_TRIAL_STATUS_TYPES_FKC;

    alter table trial_status_type_action 
        drop constraint TRIAL_STATUS_TYPE_ACTIONS_FKC;

    alter table users 
        drop constraint users_MODIFIED_USER_FKC;

    alter table users 
        drop constraint users_IDENTITY_FKC;

    alter table users 
        drop constraint users_DEPARTMENT_FKC;

    alter table users 
        drop constraint users_KEY_PAIR_FKC;

    alter table valid_criterion_property_restriction 
        drop constraint CRITERION_RESTRICTION_CRITERION_PROPERTIES_FKC;

    alter table valid_criterion_property_restriction 
        drop constraint CRITERION_PROPERTY_VALID_RESTRICTIONS_FKC;

    drop table ADDRESS_TYPE cascade;

    drop table ALPHA_ID cascade;

    drop table ANIMAL_CONTACT_PARTICULARS cascade;

    drop table ANNOUNCEMENT cascade;

    drop table ASP cascade;

    drop table ASP_ATC_CODE cascade;

    drop table ASP_SUBSTANCE cascade;

    drop table BANK_ACCOUNT cascade;

    drop table BANK_IDENTIFICATION cascade;

    drop table CONTACT_DETAIL_TYPE cascade;

    drop table COUNTRY cascade;

    drop table COURSE cascade;

    drop table COURSE_CATEGORY cascade;

    drop table COURSE_PARTICIPATION_STATUS_ENTRY cascade;

    drop table COURSE_PARTICIPATION_STATUS_TYPE cascade;

    drop table CRITERIA cascade;

    drop table CRITERION cascade;

    drop table CRITERION_PROPERTY cascade;

    drop table CRITERION_RESTRICTION cascade;

    drop table CRITERION_TIE cascade;

    drop table CV_POSITION cascade;

    drop table CV_SECTION cascade;

    drop table DATA_TABLE_COLUMN cascade;

    drop table DEPARTMENT cascade;

    drop table DIAGNOSIS cascade;

    drop table DUTY_ROSTER_TURN cascade;

    drop table ERROR cascade;

    drop table FILE cascade;

    drop table FILE_FOLDER_PRESET cascade;

    drop table HOLIDAY cascade;

    drop table HYPERLINK cascade;

    drop table HYPERLINK_CATEGORY cascade;

    drop table ICD_SYST cascade;

    drop table ICD_SYST_BLOCK cascade;

    drop table ICD_SYST_CATEGORY cascade;

    drop table ICD_SYST_MODIFIER cascade;

    drop table INPUT_FIELD cascade;

    drop table INPUT_FIELD_SELECTION_SET_VALUE cascade;

    drop table INPUT_FIELD_VALUE cascade;

    drop table INQUIRY cascade;

    drop table INQUIRY_VALUE cascade;

    drop table INVENTORY cascade;

    drop table INVENTORY_BOOKING cascade;

    drop table INVENTORY_CATEGORY cascade;

    drop table INVENTORY_STATUS_ENTRY cascade;

    drop table INVENTORY_STATUS_TYPE cascade;

    drop table INVENTORY_TAG cascade;

    drop table INVENTORY_TAG_VALUE cascade;

    drop table JOB cascade;

    drop table JOB_TYPE cascade;

    drop table JOURNAL_CATEGORY cascade;

    drop table JOURNAL_ENTRY cascade;

    drop table KEY_PAIR cascade;

    drop table LECTURER cascade;

    drop table LECTURER_COMPETENCE cascade;

    drop table MAINTENANCE_SCHEDULE_ITEM cascade;

    drop table MAINTENANCE_TYPE cascade;

    drop table MASS_MAIL cascade;

    drop table MASS_MAIL_RECIPIENT cascade;

    drop table MASS_MAIL_STATUS_TYPE cascade;

    drop table MASS_MAIL_TYPE cascade;

    drop table MEDICATION cascade;

    drop table MIME_TYPE cascade;

    drop table MONEY_TRANSFER cascade;

    drop table NOTIFICATION cascade;

    drop table NOTIFICATION_RECIPIENT cascade;

    drop table NOTIFICATION_TYPE cascade;

    drop table OPS_CODE cascade;

    drop table OPS_SYST cascade;

    drop table OPS_SYST_BLOCK cascade;

    drop table OPS_SYST_CATEGORY cascade;

    drop table OPS_SYST_MODIFIER cascade;

    drop table ORGANISATION_CONTACT_PARTICULARS cascade;

    drop table PASSWORD cascade;

    drop table PERMISSION cascade;

    drop table PERSON_CONTACT_PARTICULARS cascade;

    drop table PRIVACY_CONSENT_STATUS_TYPE cascade;

    drop table PROBAND cascade;

    drop table PROBAND_ADDRESS cascade;

    drop table PROBAND_CATEGORY cascade;

    drop table PROBAND_CONTACT_DETAIL_VALUE cascade;

    drop table PROBAND_CONTACT_PARTICULARS cascade;

    drop table PROBAND_GROUP cascade;

    drop table PROBAND_LIST_ENTRY cascade;

    drop table PROBAND_LIST_ENTRY_TAG cascade;

    drop table PROBAND_LIST_ENTRY_TAG_VALUE cascade;

    drop table PROBAND_LIST_STATUS_ENTRY cascade;

    drop table PROBAND_LIST_STATUS_LOG_LEVEL cascade;

    drop table PROBAND_LIST_STATUS_TYPE cascade;

    drop table PROBAND_STATUS_ENTRY cascade;

    drop table PROBAND_STATUS_TYPE cascade;

    drop table PROBAND_TAG cascade;

    drop table PROBAND_TAG_VALUE cascade;

    drop table PROCEDURE cascade;

    drop table PROFILE_PERMISSION cascade;

    drop table SIGNATURE cascade;

    drop table SPONSORING_TYPE cascade;

    drop table STAFF cascade;

    drop table STAFF_ADDRESS cascade;

    drop table STAFF_CATEGORY cascade;

    drop table STAFF_CONTACT_DETAIL_VALUE cascade;

    drop table STAFF_STATUS_ENTRY cascade;

    drop table STAFF_STATUS_TYPE cascade;

    drop table STAFF_TAG cascade;

    drop table STAFF_TAG_VALUE cascade;

    drop table STRATIFICATION_RANDOMIZATION_LIST cascade;

    drop table STREET cascade;

    drop table SURVEY_STATUS_TYPE cascade;

    drop table TEAM_MEMBER cascade;

    drop table TEAM_MEMBER_ROLE cascade;

    drop table TIMELINE_EVENT cascade;

    drop table TIMELINE_EVENT_TYPE cascade;

    drop table TITLE cascade;

    drop table TRIAL cascade;

    drop table TRIAL_STATUS_ACTION cascade;

    drop table TRIAL_STATUS_TYPE cascade;

    drop table TRIAL_TAG cascade;

    drop table TRIAL_TAG_VALUE cascade;

    drop table TRIAL_TYPE cascade;

    drop table USER_PERMISSION_PROFILE cascade;

    drop table VISIT cascade;

    drop table VISIT_SCHEDULE_ITEM cascade;

    drop table VISIT_TYPE cascade;

    drop table ZIP cascade;

    drop table asp_ingredient cascade;

    drop table course_participation_admin_self_registration_transition cascade;

    drop table course_participation_admin_transition cascade;

    drop table course_participation_user_self_registration_transition cascade;

    drop table course_participation_user_transition cascade;

    drop table course_renewal cascade;

    drop table ecrf cascade;

    drop table ecrf_field cascade;

    drop table ecrf_field_status_entry cascade;

    drop table ecrf_field_status_transition cascade;

    drop table ecrf_field_status_type cascade;

    drop table ecrf_field_value cascade;

    drop table ecrf_status_action cascade;

    drop table ecrf_status_entry cascade;

    drop table ecrf_status_transition cascade;

    drop table ecrf_status_type cascade;

    drop table ecrf_status_type_action cascade;

    drop table input_field_value_selection cascade;

    drop table mass_mail_status_transition cascade;

    drop table medication_ingredient cascade;

    drop table privacy_consent_status_transition cascade;

    drop table proband_children cascade;

    drop table proband_list_status_transition cascade;

    drop table proband_list_status_type_log_level cascade;

    drop table send_department_staff_category cascade;

    drop table stratification_randomization_list_selection_set_value cascade;

    drop table trial_status_transition cascade;

    drop table trial_status_type_action cascade;

    drop table users cascade;

    drop table valid_criterion_property_restriction cascade;

    drop sequence hibernate_sequence;
