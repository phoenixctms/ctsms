
insert into PROBAND_LIST_STATUS_TRANSITION values ((select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='candidate'), (select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='ongoing'));

alter table COURSE add column SHOW_TRAINING_RECORD_PRESET BOOLEAN;
update COURSE set SHOW_TRAINING_RECORD_PRESET = 'f';
alter table COURSE alter SHOW_TRAINING_RECORD_PRESET set not null;

alter table COURSE_PARTICIPATION_STATUS_ENTRY add column SHOW_TRAINING_RECORD BOOLEAN;
update COURSE_PARTICIPATION_STATUS_ENTRY set SHOW_TRAINING_RECORD = 'f';
alter table COURSE_PARTICIPATION_STATUS_ENTRY alter SHOW_TRAINING_RECORD set not null;

update COURSE_PARTICIPATION_STATUS_ENTRY set CV_SECTION_FK = SECTION_FK where CV_SECTION_FK is null;
alter table COURSE_PARTICIPATION_STATUS_ENTRY drop column SECTION_FK;

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'in_house_trainings', 'in_house_trainings', 0, true, true);

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'external_trainings', 'external_trainings', 10, true, true);

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'trial_specific_trainings', 'trial_specific_trainings', 20, true, true);

alter table COURSE add column CERTIFICATE BOOLEAN;
update COURSE set CERTIFICATE = 'f';
alter table COURSE alter CERTIFICATE set not null;

alter table STAFF_TAG add column TRAINING_RECORD BOOLEAN;
update STAFF_TAG set TRAINING_RECORD = 'f';
alter table STAFF_TAG alter TRAINING_RECORD set not null;
update STAFF_TAG set TRAINING_RECORD = 't' where NAME_L10N_KEY = 'function' or NAME_L10N_KEY = 'description';
update STAFF_TAG set MAX_OCCURRENCE = 3 where NAME_L10N_KEY = 'function';
update STAFF_TAG set NAME_L10N_KEY = '01_role' where NAME_L10N_KEY = 'function';
update STAFF_TAG set NAME_L10N_KEY = '02_description' where NAME_L10N_KEY = 'description';

alter table USERS add column DECRYPT_UNTRUSTED BOOLEAN;
update USERS set DECRYPT_UNTRUSTED = 'f';
alter table USERS alter DECRYPT_UNTRUSTED set not null;