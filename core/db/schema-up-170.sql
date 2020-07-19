
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

alter table VISIT_SCHEDULE_ITEM add column MODE VARCHAR(1024);
update VISIT_SCHEDULE_ITEM set MODE = 'STATIC';
alter table VISIT_SCHEDULE_ITEM alter MODE set not null;

alter table VISIT_SCHEDULE_ITEM alter START drop not null;
alter table VISIT_SCHEDULE_ITEM alter STOP drop not null;

update PROBAND_GROUP set TOKEN = regexp_replace(TOKEN,';',',','g') where TOKEN ~ ';';
update PROBAND_GROUP set TOKEN = regexp_replace(TOKEN,'(^[[:space:]]+)|([[:space:]]+$)','','g') where TOKEN ~ '(^[[:space:]]+)|([[:space:]]+$)';

update ECRF set NAME = concat(lpad(ECRF.POSITION::text, floor(log(greatest(coalesce((select max(POSITION) from ECRF), 1), 10)) + 1)::integer, '0'), '. ', ECRF.NAME);
update
     ECRF A
set
     NAME = (case when B.LINE_NO > 1 then concat(NAME, ' (', B.LINE_NO - 1, ')') else NAME end),
     REVISION = B.GROUP_TOKEN
from (select
    C.ID ID,
    PROBAND_GROUP.TOKEN GROUP_TOKEN,
    row_number() over(partition by C.TRIAL_FK, C.NAME, C.GROUP_FK order by C.TRIAL_FK, C.NAME, C.GROUP_FK, C.POSITION) LINE_NO
from (select
    D.TRIAL_FK,
    D.NAME
from ECRF D group by D.TRIAL_FK, D.NAME having count(ID) > 1) COLLIDING
left join ECRF C on COLLIDING.TRIAL_FK = C.TRIAL_FK and COLLIDING.NAME = C.NAME 
left join PROBAND_GROUP on C.GROUP_FK = PROBAND_GROUP.ID) B
where B.ID = A.ID;
insert into ECRF_GROUP (GROUPS_FK, ECRFS_FK) select GROUP_FK, ID from ECRF where GROUP_FK is not null;
alter table ECRF drop column GROUP_FK;
alter table ECRF drop column POSITION;

update INPUT_FIELD set TITLE_L10N_KEY = regexp_replace(TITLE_L10N_KEY,'(^[[:space:]]+)|([[:space:]]+$)','','g') where TITLE_L10N_KEY ~ '(^[[:space:]]+)|([[:space:]]+$)';