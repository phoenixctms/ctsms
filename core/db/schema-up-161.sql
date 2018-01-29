alter table FILE add column PUBLIC_FILE BOOLEAN;
update FILE set PUBLIC_FILE = 'f';
alter table FILE alter PUBLIC_FILE set not null;

alter table TRIAL_STATUS_TYPE add column ECRF_VALUE_INPUT_ENABLED BOOLEAN;
update TRIAL_STATUS_TYPE set ECRF_VALUE_INPUT_ENABLED = INQUIRY_VALUE_INPUT_ENABLED;
alter table TRIAL_STATUS_TYPE alter ECRF_VALUE_INPUT_ENABLED set not null;

insert into NOTIFICATION_TYPE values (nextval('hibernate_sequence'), 'STAFF_INACTIVE_VISIT_SCHEDULE_ITEM', 't', 't', 'LIGHTPINK', 'ctsms-icon-warning', 'STAFF_INACTIVE_VISIT_SCHEDULE_ITEM', 'STAFF_INACTIVE_VISIT_SCHEDULE_ITEM', 'STAFF_INACTIVE_VISIT_SCHEDULE_ITEM');

insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='institution'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='division'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='vendor'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='sponsor'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='manager'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='investigator'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='auditor'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='scientist'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='cra'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='contractor'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='external'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='nurse'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='clerk'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='student'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='physician'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='coordinator'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='personnel'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='person'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='graduand'));
insert into SEND_DEPARTMENT_STAFF_CATEGORY values ((select ID from NOTIFICATION_TYPE where TYPE='STAFF_INACTIVE_VISIT_SCHEDULE_ITEM'), (select ID from STAFF_CATEGORY where NAME_L10N_KEY='ordination'));

alter table STAFF add column SUPERVISOR BOOLEAN;
update STAFF set SUPERVISOR = 'f';
alter table STAFF alter SUPERVISOR set not null;

create index journal_entry_inventory_system_message_code ON JOURNAL_ENTRY (INVENTORY_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_staff_system_message_code ON JOURNAL_ENTRY (STAFF_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_course_system_message_code ON JOURNAL_ENTRY (COURSE_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_trial_system_message_code ON JOURNAL_ENTRY (TRIAL_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_proband_system_message_code ON JOURNAL_ENTRY (PROBAND_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_inputfield_system_message_code ON JOURNAL_ENTRY (INPUT_FIELD_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_user_system_message_code ON JOURNAL_ENTRY (USER_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_criteria_system_message_code ON JOURNAL_ENTRY (CRITERIA_FK,SYSTEM_MESSAGE_CODE);

insert into ECRF_STATUS_TRANSITION values ((select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='complete_signed'), (select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='complete_signed'));
insert into ECRF_STATUS_TRANSITION values ((select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='incomplete_signed'), (select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='incomplete_signed'));
insert into ECRF_STATUS_TRANSITION values ((select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='skipped_signed'), (select ID from ECRF_STATUS_TYPE where NAME_L10N_KEY='skipped_signed'));