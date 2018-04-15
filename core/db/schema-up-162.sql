
create extension if not exists "uuid-ossp";
alter table PROBAND add column BEACON CHARACTER VARYING(1024);
update PROBAND set BEACON = uuid_generate_v4();
alter table PROBAND alter BEACON set not null;
alter table PROBAND add constraint proband_beacon_key unique (BEACON);

alter table USERS add column DECRYPT BOOLEAN;
update USERS set DECRYPT = 't';
alter table USERS alter DECRYPT set not null;

insert into FILE_FOLDER_PRESET values (nextval('hibernate_sequence'), 'MASS_MAIL_DOCUMENT', '/01 - Attachments/');
insert into FILE_FOLDER_PRESET values (nextval('hibernate_sequence'), 'MASS_MAIL_DOCUMENT', '/02 - Images/');
insert into FILE_FOLDER_PRESET values (nextval('hibernate_sequence'), 'MASS_MAIL_DOCUMENT', '/03 - Miscellaneous/');

insert into JOURNAL_CATEGORY values (nextval('hibernate_sequence'), 'MASS_MAIL_JOURNAL', 'general', 'general_title_preset', 't', 'ctsms-journalcategory-general', 'PAPAYAWHIP', 't');

insert into MASS_MAIL_STATUS_TYPE values (nextval('hibernate_sequence'), 'TOMATO', 't', 'paused', 'ctsms-massmailstatus-paused', 'f', 'f');
insert into MASS_MAIL_STATUS_TYPE values (nextval('hibernate_sequence'), 'LIME', 'f', 'sending', 'ctsms-massmailstatus-sending', 't', 'f');
insert into MASS_MAIL_STATUS_TYPE values (nextval('hibernate_sequence'), 'MISTYROSE', 'f', 'closed', 'ctsms-massmailstatus-closed', 'f', 't');

insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'));

insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'));

insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='paused'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='sending'));
insert into MASS_MAIL_STATUS_TRANSITION values ((select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'), (select ID from MASS_MAIL_STATUS_TYPE where NAME_L10N_KEY='closed'));

insert into MASS_MAIL_TYPE values (nextval('hibernate_sequence'), 'regulatory', 't', 'f', 'f');
insert into MASS_MAIL_TYPE values (nextval('hibernate_sequence'), 'welcome', 't', 'f', 'f');
insert into MASS_MAIL_TYPE values (nextval('hibernate_sequence'), 'newsletter', 't', 'f', 'f');
insert into MASS_MAIL_TYPE values (nextval('hibernate_sequence'), 'study_specific', 't', 't', 'f');
insert into MASS_MAIL_TYPE values (nextval('hibernate_sequence'), 'enrollment', 't', 't', 't');

insert into PROBAND_CATEGORY values (nextval('hibernate_sequence'), 'signup_verified', 'PALEGREEN', 't', 'ctsms-probandcategory-signup-verified', 't', 'f', 'f', 't', 't', 'f');

alter table CONTACT_DETAIL_TYPE add column BUSINESS BOOLEAN;
update CONTACT_DETAIL_TYPE set BUSINESS = 'f';
update CONTACT_DETAIL_TYPE set BUSINESS = 't' where name_l10n_key like 'business%';
alter table CONTACT_DETAIL_TYPE alter BUSINESS set not null;




