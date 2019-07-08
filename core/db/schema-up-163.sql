
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_corrected');
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_data_na');
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_closed');

alter table PROBAND_GROUP add column RANDOMIZE BOOLEAN;
update PROBAND_GROUP set RANDOMIZE = 'f';
alter table PROBAND_GROUP alter RANDOMIZE set not null;

alter table PROBAND_LIST_ENTRY_TAG add column STRATIFICATION BOOLEAN;
update PROBAND_LIST_ENTRY_TAG set STRATIFICATION = 'f';
alter table PROBAND_LIST_ENTRY_TAG alter STRATIFICATION set not null;

alter table PROBAND_LIST_ENTRY_TAG add column RANDOMIZE BOOLEAN;
update PROBAND_LIST_ENTRY_TAG set RANDOMIZE = 'f';
alter table PROBAND_LIST_ENTRY_TAG alter RANDOMIZE set not null;

alter table TRIAL add column SIGNUP_RANDOMIZE BOOLEAN;
update TRIAL set SIGNUP_RANDOMIZE = 'f';
alter table TRIAL alter SIGNUP_RANDOMIZE set not null;

update ecrf_status_type set audit_trail = 't' where name_l10n_key = 'in_progress';

alter table INPUT_FIELD add column USER_TIME_ZONE BOOLEAN;
update INPUT_FIELD set USER_TIME_ZONE = 'f';
alter table INPUT_FIELD alter USER_TIME_ZONE set not null;

alter table PROBAND_LIST_STATUS_ENTRY alter ENCRYPTED_REASON drop not null;
alter table PROBAND_LIST_STATUS_ENTRY alter REASON_IV drop not null;

alter table USERS add column ENABLE_INVENTORY_MODULE BOOLEAN;
update USERS set ENABLE_INVENTORY_MODULE = 't';
alter table USERS alter ENABLE_INVENTORY_MODULE set not null;

alter table USERS add column ENABLE_STAFF_MODULE BOOLEAN;
update USERS set ENABLE_STAFF_MODULE = 't';
alter table USERS alter ENABLE_STAFF_MODULE set not null;

alter table USERS add column ENABLE_COURSE_MODULE BOOLEAN;
update USERS set ENABLE_COURSE_MODULE = 't';
alter table USERS alter ENABLE_COURSE_MODULE set not null;

alter table USERS add column ENABLE_TRIAL_MODULE BOOLEAN;
update USERS set ENABLE_TRIAL_MODULE = 't';
alter table USERS alter ENABLE_TRIAL_MODULE set not null;

alter table USERS add column ENABLE_INPUT_FIELD_MODULE BOOLEAN;
update USERS set ENABLE_INPUT_FIELD_MODULE = 't';
alter table USERS alter ENABLE_INPUT_FIELD_MODULE set not null;

alter table USERS add column ENABLE_PROBAND_MODULE BOOLEAN;
update USERS set ENABLE_PROBAND_MODULE = 't';
alter table USERS alter ENABLE_PROBAND_MODULE set not null;

alter table USERS add column ENABLE_MASS_MAIL_MODULE BOOLEAN;
update USERS set ENABLE_MASS_MAIL_MODULE = 't';
alter table USERS alter ENABLE_MASS_MAIL_MODULE set not null;

alter table USERS add column ENABLE_USER_MODULE BOOLEAN;
update USERS set ENABLE_USER_MODULE = 't';
alter table USERS alter ENABLE_USER_MODULE set not null;

alter table ECRF_STATUS_ENTRY drop column export_status;
alter table ECRF_STATUS_ENTRY drop column export_timestamp;
alter table ECRF_STATUS_ENTRY drop column export_response_msg;

alter table MONEY_TRANSFER drop column export_status;
alter table MONEY_TRANSFER drop column export_timestamp;
alter table MONEY_TRANSFER drop column export_response_msg;

alter table PROBAND_LIST_ENTRY drop column export_status;
alter table PROBAND_LIST_ENTRY drop column export_timestamp;
alter table PROBAND_LIST_ENTRY drop column export_response_msg;

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'import_ecrfs',
'import_ecrfs',
'"{0}" -ie="" -id={1} -u="{2}" -p="{3}" -jid={4} -f',
't',
'f',
'f',
'f',
't',
'f',
'f',
'f',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_ecrfs',
'export_ecrfs',
'"{0}" -ee="" -id={1} -u="{2}" -p="{3}" -jid={4} -f',
't',
'f',
'f',
'f',
'f',
't',
'f',
'f',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_trial_journal',
'export_trial_journal',
'"{0}" -etj="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_ecrf_journal',
'export_ecrf_journal',
'"{0}" -eej="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_proband_list',
'export_proband_list',
'"{0}" -epl="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_enrollment_log',
'export_enrollment_log',
'"{0}" -epl="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" -ll="ENROLLMENT"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_screening_log',
'export_screening_log',
'"{0}" -epl="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" -ll="SCREENING"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_pre_screening_log',
'export_pre_screening_log',
'"{0}" -epl="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" -ll="PRE_SCREENING"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_sicl',
'export_sicl',
'"{0}" -epl="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" -ll="SICL"',
't',
'f',
'f',
'f',
'f',
't',
't',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'validate_pending_ecrfs',
'validate_pending_ecrfs',
'"{0}" -vpe -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
'f',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_audit_trail',
'export_audit_trail',
'"{0}" -eat="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_ecrf_data',
'export_ecrf_data',
'{10} --task=cleanup_all --task=export_ecrf_data_vertical --task=export_ecrf_data_horizontal --task=publish_ecrf_data_sqlite --task=publish_ecrf_data_horizontal_csv --task=publish_ecrf_data_xls --task=cleanup_all -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" --upload --force',
't',
'f',
'f',
'f',
'f',
'f',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'export_ecrf_pdfs',
'export_ecrf_pdfs',
'{10} --task=cleanup_all --task=publish_ecrf_data_pdfs --task=cleanup_all -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}" --upload --force',
't',
'f',
'f',
'f',
'f',
'f',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INPUT_FIELD_JOB',
'export_input_field',
'export_input_field',
'"{0}" -eif="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INPUT_FIELD_JOB',
'import_input_fields',
'import_input_fields',
'"{0}" -iif="" -u="{2}" -p="{3}" -jid={4} -er="{5}" -f',
't',
'f',
'f',
'f',
't',
'f',
'f',
'f',
null
);


insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INVENTORY_CRITERIA_JOB',
'export_inventory_criteria_result',
'export_inventory_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'STAFF_CRITERIA_JOB',
'export_staff_criteria_result',
'export_staff_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'COURSE_CRITERIA_JOB',
'export_course_criteria_result',
'export_course_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_CRITERIA_JOB',
'export_trial_criteria_result',
'export_trial_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INPUT_FIELD_CRITERIA_JOB',
'export_input_field_criteria_result',
'export_input_field_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'PROBAND_CRITERIA_JOB',
'export_proband_criteria_result',
'export_proband_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'MASS_MAIL_CRITERIA_JOB',
'export_mass_mail_criteria_result',
'export_mass_mail_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'USER_CRITERIA_JOB',
'export_user_criteria_result',
'export_user_criteria_result',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
'f',
'f',
't',
'f',
't',
null
);



insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INVENTORY_CRITERIA_JOB',
'export_inventory_criteria_result_monthly',
'export_inventory_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'STAFF_CRITERIA_JOB',
'export_staff_criteria_result_monthly',
'export_staff_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'COURSE_CRITERIA_JOB',
'export_course_criteria_result_monthly',
'export_course_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_CRITERIA_JOB',
'export_trial_criteria_result_monthly',
'export_trial_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'INPUT_FIELD_CRITERIA_JOB',
'export_input_field_criteria_result_monthly',
'export_input_field_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'PROBAND_CRITERIA_JOB',
'export_proband_criteria_result_monthly',
'export_proband_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'MASS_MAIL_CRITERIA_JOB',
'export_mass_mail_criteria_result_monthly',
'export_mass_mail_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'USER_CRITERIA_JOB',
'export_user_criteria_result_monthly',
'export_user_criteria_result_monthly',
'"{0}" -ecr="" -id={1} -u="{2}" -p="{3}" -jid={4} -er="{5}"',
't',
'f',
'f',
't',
'f',
't',
'f',
't',
null
);