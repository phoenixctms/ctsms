
alter table COURSE add column SHOW_COMMENT_TRAINING_RECORD_PRESET BOOLEAN;
update COURSE set SHOW_COMMENT_TRAINING_RECORD_PRESET = 'f';
alter table COURSE alter SHOW_COMMENT_TRAINING_RECORD_PRESET set not null;

alter table COURSE_PARTICIPATION_STATUS_ENTRY add column SHOW_COMMENT_TRAINING_RECORD BOOLEAN;
update COURSE_PARTICIPATION_STATUS_ENTRY set SHOW_COMMENT_TRAINING_RECORD = 'f';
alter table COURSE_PARTICIPATION_STATUS_ENTRY alter SHOW_COMMENT_TRAINING_RECORD set not null;


alter table TRIAL_STATUS_TYPE add column RELEVANT_FOR_COURSES BOOLEAN;
update TRIAL_STATUS_TYPE set RELEVANT_FOR_COURSES = 't';
update TRIAL_STATUS_TYPE set RELEVANT_FOR_COURSES = 'f' where NAME_L10N_KEY = 'cancelled';
update TRIAL_STATUS_TYPE set RELEVANT_FOR_COURSES = 'f' where NAME_L10N_KEY = 'lockdown';
update TRIAL_STATUS_TYPE set RELEVANT_FOR_COURSES = 'f' where NAME_L10N_KEY = 'migrated';
alter table TRIAL_STATUS_TYPE alter RELEVANT_FOR_COURSES set not null;

alter table COURSE_PARTICIPATION_STATUS_TYPE add column FILE_REQUIRED BOOLEAN;
update COURSE_PARTICIPATION_STATUS_TYPE set FILE_REQUIRED = 'f';
alter table COURSE_PARTICIPATION_STATUS_TYPE alter FILE_REQUIRED set not null;


update USERS set VISIBLE_INVENTORY_TAB_LIST = 'inventorytags,inventorystatus,inventorymaintenance,inventorybookings,inventorybookingsummary,inventoryhyperlinks,inventoryfiles,inventoryjournal';
update USERS set VISIBLE_STAFF_TAB_LIST = 'staffimage,stafftags,staffcontactdetails,staffaddresses,staffstatus,staffdutyrosterturns,cvpositions,courseparticipationstatus,staffassociations,staffhyperlinks,stafffiles,staffjournal';
update USERS set VISIBLE_COURSE_TAB_LIST = 'lecturers,courseinventorybookings,admincourseparticipationstatus,coursehyperlinks,coursefiles,coursejournal';
update USERS set VISIBLE_TRIAL_TAB_LIST = 'trialtags,teammembers,timelineevents,trialinventorybookings,visits,probandgroups,visitschedule,trialdutyrosterturns,probandlistentrytags,randomizationlists,inquiries,inquiryvaluesdummy,ecrfs,ecrffields,probandlistentries,trialecrfstatusentries,trialecrffieldstatusentries,reimbursements,trialassociations,trialhyperlinks,trialjobs,trialfiles,trialjournal';
update USERS set VISIBLE_PROBAND_TAB_LIST = 'probandimage,probandtags,probandcontactdetails,probandrecipients,probandaddresses,probandstatus,probandinventorybookings,diagnoses,procedures,medications,inquiryvalues,trialparticipations,probandvisitschedule,probandecrfstatusentries,bankaccounts,moneytransfers,probandjobs,probandfiles,probandjournal';
update USERS set VISIBLE_INPUT_FIELD_TAB_LIST = 'inputfieldselectionsetvalue,inputfielddummy,inputfieldassociations,inputfieldjobs,inputfieldjournal';
update USERS set VISIBLE_MASS_MAIL_TAB_LIST = 'massmailrecipients,massmailfiles,massmailjournal';
update USERS set VISIBLE_USER_TAB_LIST = 'setpassword,setpermissions,useractivity,userjournal';

insert into JOB_TYPE 
(id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
values (
nextval('hibernate_sequence'), 
'TRIAL_JOB',
'import_ecrf_data',
'import_ecrf_data',
'{12} --task=cleanup_all --task=import_ecrf_data_horizontal --task=cleanup_all -id={1} -auth={4} -jid={5} --force --skip-errors',
't',
'f',
'f',
'f',
't',
'f',
'f',
't',
null
);
