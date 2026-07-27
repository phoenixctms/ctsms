do
$$
begin

if get_database_version() < '010901002' then

  alter table INPUT_FIELD add column TOP_COMMENT_L10N_KEY TEXT;
  alter table INPUT_FIELD rename column COMMENT_L10N_KEY TO BOTTOM_COMMENT_L10N_KEY;
  
  perform set_database_version('010901002');
  
end if;

if get_database_version() < '010901003' then

  ALTER TABLE MASS_MAIL ADD COLUMN HTML BOOLEAN;
  UPDATE MASS_MAIL SET HTML = 't';
  ALTER TABLE MASS_MAIL ALTER HTML SET NOT NULL;

  alter table MASS_MAIL add column PHONE_TO_EMAIL_FORMAT CHARACTER VARYING(1024);
  
  ALTER TABLE MASS_MAIL ALTER SUBJECT_FORMAT DROP NOT NULL;
  
  perform set_database_version('010901003');
  
end if;


if get_database_version() < '010901004' then

  ALTER TABLE TEAM_MEMBER RENAME COLUMN ecrf TO ecrf_entry;
  
  ALTER TABLE TEAM_MEMBER ADD COLUMN ecrf_design BOOLEAN;
  UPDATE TEAM_MEMBER SET ecrf_design = 'f';
  ALTER TABLE TEAM_MEMBER ALTER ecrf_design SET NOT NULL;  
  
  UPDATE permission set restriction = 'TRIAL_IDENTITY_TEAM_MEMBER_ECRF_ENTRY' where restriction = 'TRIAL_IDENTITY_TEAM_MEMBER_ECRF';

  perform set_database_version('010901004');
  
end if;

if get_database_version() < '010901005' then

  update proband_category set "delete" = 'f' where name_l10n_key = 'signup_verified';

  perform set_database_version('010901005');
  
end if;

if get_database_version() < '010901006' then

  insert into JOB_TYPE 
  (id,module,name_l10n_key,description_l10n_key,command_format,visible,daily,weekly,monthly,input_file,output_file,encrypt_file,email_recipients,trial_fk)
  values (
  nextval('hibernate_sequence'), 
  'TRIAL_JOB',
  'import_inquiry_data',
  'import_inquiry_data',
  '{13} --task=cleanup --task=import_inquiry_data_horizontal --task=cleanup -id={1} -auth={4} -jid={5} -tz={6} --force --skip-errors',
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

  perform set_database_version('010901006');
  
end if;

if get_database_version() < '010901007' then

  -- String hash columns are not indexed: concatenated substring search hashes can exceed btree row size limits,
  -- and LIKE '%hash%' does not use btree indexes anyway.

  drop index if exists medication_comment_hash;
  drop index if exists money_transfer_comment_hash;
  drop index if exists proband_contact_particulars_comment_hash;
  drop index if exists procedure_comment_hash;
  drop index if exists proband_tag_value_value_hash;
  drop index if exists proband_status_entry_comment_hash;
  drop index if exists proband_list_status_entry_reason_hash;
  drop index if exists proband_contact_particulars_data_hash;
  drop index if exists proband_contact_particulars_file_name_hash;
  drop index if exists proband_contact_particulars_citizenship_hash;
  drop index if exists proband_contact_particulars_last_name_hash;
  drop index if exists proband_contact_particulars_last_name_normalized_hash;
  drop index if exists proband_contact_particulars_first_name_hash;
  drop index if exists proband_contact_particulars_first_name_normalized_hash;
  drop index if exists proband_contact_detail_value_comment_hash;
  drop index if exists proband_contact_detail_value_value_hash;
  drop index if exists proband_address_care_of_hash;
  drop index if exists proband_address_door_number_hash;
  drop index if exists proband_address_entrance_hash;
  drop index if exists proband_address_house_number_hash;
  drop index if exists proband_address_street_name_hash;
  drop index if exists proband_address_city_name_hash;
  drop index if exists proband_address_zip_code_hash;
  drop index if exists proband_address_country_name_hash;
  drop index if exists journal_entry_title_hash;
  drop index if exists journal_entry_comment_hash;
  drop index if exists file_file_name_hash;
  drop index if exists file_comment_hash;
  drop index if exists file_title_hash;
  drop index if exists diagnosis_comment_hash;
  drop index if exists bank_account_bank_name_hash;
  drop index if exists bank_account_bic_hash;
  drop index if exists bank_account_iban_hash;
  drop index if exists bank_account_bank_code_number_hash;
  drop index if exists bank_account_account_number_hash;
  drop index if exists bank_account_account_holder_name_hash;

  perform set_database_version('010901007');

end if;

if get_database_version() < '010901008' then

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'ORANGERED', 'f', 'ic_not_signed', 'f', 'f', 'f', 'f', 't', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'PROBAND_STATUS' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'GOLD', 'f', 'screening_result_pending', 'f', 't', 't', 't', 'f', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'SCREENING' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'ORANGERED', 'f', 'ic_not_signed_re_screening', 'f', 'f', 'f', 't', 't', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed_re_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'SCREENING' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'GOLD', 'f', 're_screening_result_pending', 'f', 't', 't', 't', 'f', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'SCREENING' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'GREEN', 'f', 'ongoing_randomized', 'f', 't', 't', 'f', 'f', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'ENROLLMENT' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'candidate' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'acceptance' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_signed' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_ok' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed_re_screening' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_signed_re_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_ok' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_failure' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'dropped_out' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_ok' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_failure' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening_result_pending' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'dropped_out' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'dropped_out' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ongoing_randomized' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'completed' limit 1)
  );

  perform set_database_version('010901008');

end if;

if get_database_version() < '010901009' then

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'cancelled' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'cancelled' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed_re_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_not_signed_re_screening' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'dropped_out' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'dropped_out' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'completed' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'completed' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'animal_dropped_out' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'animal_dropped_out' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'animal_completed' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'animal_completed' limit 1)
  );

  perform set_database_version('010901009');

end if;

if get_database_version() < '010901010' then

  ALTER TABLE MASS_MAIL_TYPE ADD COLUMN ECRFS_REQUIRED BOOLEAN;
  UPDATE MASS_MAIL_TYPE SET ECRFS_REQUIRED = 'f';
  ALTER TABLE MASS_MAIL_TYPE ALTER ECRFS_REQUIRED SET NOT NULL;

  insert into MASS_MAIL_TYPE
    ("id", "name_l10n_key", "visible", "trial_required", "proband_list_staus_required", "visit_schedule_items_required", "ecrfs_required")
  values (nextval('hibernate_sequence'), 'ecrf_status', 't', 't', 'f', 'f', 't');

  create table mass_mail_ecrf (
    ECRFS_FK BIGINT not null,
    MASS_MAILS_FK BIGINT not null
  );

  alter table mass_mail_ecrf
    add constraint MASS_MAIL_ECRFS_FKC
    foreign key (ECRFS_FK)
    references ecrf;

  alter table mass_mail_ecrf
    add constraint ecrf_MASS_MAILS_FKC
    foreign key (MASS_MAILS_FK)
    references MASS_MAIL;

  insert into ecrf_status_action
    ("id", "action")
  values (nextval('hibernate_sequence'), 'ADD_MASSMAIL_RECIPIENT');

  insert into ecrf_status_type_action
    ("e_c_r_f_status_types_fk", "actions_fk")
  select
    est."id",
    esa."id"
  from
    ecrf_status_type est,
    ecrf_status_action esa
  where
    esa."action" = 'ADD_MASSMAIL_RECIPIENT';

  perform set_database_version('010901010');

end if;

if get_database_version() < '010901011' then

  ALTER TABLE MASS_MAIL_TYPE RENAME COLUMN PROBAND_LIST_STAUS_REQUIRED TO PROBAND_LIST_STATUS_REQUIRED;

  ALTER TABLE MASS_MAIL_TYPE ADD COLUMN ECRF_STATUS_REQUIRED BOOLEAN;
  UPDATE MASS_MAIL_TYPE SET ECRF_STATUS_REQUIRED = 'f';
  ALTER TABLE MASS_MAIL_TYPE ALTER ECRF_STATUS_REQUIRED SET NOT NULL;

  UPDATE MASS_MAIL_TYPE SET ECRF_STATUS_REQUIRED = 't' WHERE name_l10n_key = 'ecrf_status';

  ALTER TABLE MASS_MAIL ADD COLUMN ECRF_STATUS_RESEND BOOLEAN;
  UPDATE MASS_MAIL SET ECRF_STATUS_RESEND = 'f';
  ALTER TABLE MASS_MAIL ALTER ECRF_STATUS_RESEND SET NOT NULL;

  ALTER TABLE MASS_MAIL ADD COLUMN ECRF_STATUS_FK BIGINT;

  alter table MASS_MAIL
    add constraint MASS_MAIL_ECRF_STATUS_FKC
    foreign key (ECRF_STATUS_FK)
    references ecrf_status_type;

  perform set_database_version('010901011');

end if;

end
$$;