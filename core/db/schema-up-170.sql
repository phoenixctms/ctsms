
insert into PROBAND_LIST_STATUS_TRANSITION values ((select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='candidate'), (select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='ongoing'));

alter table COURSE add column SHOW_TRAINING_RECORD_PRESET BOOLEAN;
update COURSE set SHOW_TRAINING_RECORD_PRESET = 'f';
alter table COURSE alter SHOW_TRAINING_RECORD_PRESET set not null;

alter table COURSE_PARTICIPATION_STATUS_ENTRY add column SHOW_TRAINING_RECORD BOOLEAN;
update COURSE_PARTICIPATION_STATUS_ENTRY set SHOW_TRAINING_RECORD = 'f';
alter table COURSE_PARTICIPATION_STATUS_ENTRY alter SHOW_TRAINING_RECORD set not null;

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'in_house_trainings', 'in_house_trainings', 0, true, true);

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'external_trainings', 'external_trainings', 10, true, true);

insert into TRAINING_RECORD_SECTION 
(id,name_l10n_key,description_l10n_key,position,show_training_record_preset,visible)
values (nextval('hibernate_sequence'), 'trial_specific_trainings', 'trial_specific_trainings', 20, true, true);
