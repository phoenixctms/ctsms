do
$$
declare
  r record;
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

if get_database_version() < '010901012' then

  -- align table indexes with index-create.sql:

  -- rename indexes created under earlier names (drop them if the current name exists as well):
  for r in select * from (values
      ('journal_entry', 'journal_entry_inventory_system_message_code', 'journal_entry_inventory_fk_system_message_code'),
      ('journal_entry', 'journal_entry_staff_system_message_code', 'journal_entry_staff_fk_system_message_code'),
      ('journal_entry', 'journal_entry_course_system_message_code', 'journal_entry_course_fk_system_message_code'),
      ('journal_entry', 'journal_entry_trial_system_message_code', 'journal_entry_trial_fk_system_message_code'),
      ('journal_entry', 'journal_entry_proband_system_message_code', 'journal_entry_proband_fk_system_message_code'),
      ('journal_entry', 'journal_entry_inputfield_system_message_code', 'journal_entry_inputfield_fk_system_message_code'),
      ('journal_entry', 'journal_entry_user_system_message_code', 'journal_entry_user_fk_system_message_code'),
      ('journal_entry', 'journal_entry_criteria_system_message_code', 'journal_entry_criteria_fk_system_message_code'),
      ('ecrf_field', 'ecrf_field_ecrf_section', 'ecrf_field_ecrf_fk_section'),
      ('ecrf_field', 'ecrf_field_trial_ecrf_section', 'ecrf_field_trial_fk_ecrf_fk_section'),
      ('permission', 'service_method', 'permission_service_method'),
      ('ecrf_field_value', 'ecrf_field_value_ecrf_field_list_entry_index_version', 'ecrf_field_value_ecrf_field_fk_list_entry_fk_index_version'),
      ('inquiry', 'ecrf_field_trial_fk_category', 'inquiry_trial_fk_category')
    ) as t(table_name, old_name, new_name) loop
    if exists (select 1 from pg_index where indexrelid = to_regclass(r.old_name) and indrelid = to_regclass(r.table_name)) then
      if to_regclass(r.new_name) is null then
        execute format('ALTER INDEX %I RENAME TO %I', r.old_name, r.new_name);
      else
        execute format('DROP INDEX %I', r.old_name);
      end if;
    end if;
  end loop;

  -- inventory_booking_calendar was created on duty_roster_turn (duplicate of duty_roster_turn_calendar):
  if exists (select 1 from pg_index where indexrelid = to_regclass('inventory_booking_calendar') and indrelid = to_regclass('duty_roster_turn')) then
    DROP INDEX inventory_booking_calendar;
  end if;

  -- duplicates of trial_duty_self_allocation_locked_until_from and trial_duty_self_allocation_locked_from_until:
  DROP INDEX IF EXISTS trial_duty_self_allocation_until_from;
  DROP INDEX IF EXISTS trial_duty_self_allocation_from_until;

  -- duplicates of the unique constraint indexes:
  DROP INDEX IF EXISTS password_password_hash;
  DROP INDEX IF EXISTS department_department_password_hash;

  -- replaced by the visit_schedule_item mode indexes:
  DROP INDEX IF EXISTS visit_schedule_item_start_stop;
  DROP INDEX IF EXISTS visit_schedule_item_stop_start;
  DROP INDEX IF EXISTS visit_schedule_item_trial_fk_start_stop;
  DROP INDEX IF EXISTS visit_schedule_item_trial_fk_stop_start;

  -- create missing indexes (including the new foreign key indexes):
  CREATE INDEX IF NOT EXISTS permission_service_method ON permission (service_method);
  CREATE INDEX IF NOT EXISTS profile_permission_permission_fk_profile_active ON profile_permission (permission_fk,profile,active);
  CREATE INDEX IF NOT EXISTS user_permission_profile_user_fk_active_profile ON user_permission_profile (user_fk,active,profile);
  CREATE INDEX IF NOT EXISTS criterion_property_module_property ON criterion_property (module,property);
  CREATE INDEX IF NOT EXISTS file_logical_path ON file (logical_path);
  CREATE INDEX IF NOT EXISTS file_file_name ON file (file_name);
  CREATE INDEX IF NOT EXISTS file_title ON file (title);
  CREATE INDEX IF NOT EXISTS file_folder_preset_logical_path ON file_folder_preset (logical_path);
  CREATE INDEX IF NOT EXISTS street_country_name_zip_code_street_name ON street (country_name,zip_code,street_name);
  CREATE INDEX IF NOT EXISTS street_country_name_city_name_street_name ON street (country_name,city_name,street_name);
  CREATE INDEX IF NOT EXISTS street_country_name_province_zip_code_street_name ON street (country_name,province,zip_code,street_name);
  CREATE INDEX IF NOT EXISTS street_country_name_province_city_name_street_name ON street (country_name,province,city_name,street_name);
  CREATE INDEX IF NOT EXISTS zip_country_name_city_name ON zip (country_name,city_name);
  CREATE INDEX IF NOT EXISTS zip_country_name_zip_code ON zip (country_name,zip_code);
  CREATE INDEX IF NOT EXISTS zip_country_name_province_city_name ON zip (country_name,province,city_name);
  CREATE INDEX IF NOT EXISTS zip_country_name_province_zip_code ON zip (country_name,province,zip_code);
  CREATE INDEX IF NOT EXISTS bank_identification_bank_code_number ON bank_identification (bank_code_number);
  CREATE INDEX IF NOT EXISTS bank_identification_bank_name ON bank_identification (bank_name);
  CREATE INDEX IF NOT EXISTS proband_list_entry_tag_value_list_entry_fk_tag_fk ON proband_list_entry_tag_value (list_entry_fk,tag_fk);
  CREATE INDEX IF NOT EXISTS inquiry_value_proband_fk_inquiry_fk ON inquiry_value (proband_fk,inquiry_fk);
  CREATE INDEX IF NOT EXISTS ecrf_status_entry_list_entry_fk_ecrf_fk_visit_fk ON ecrf_status_entry (list_entry_fk,ecrf_fk,visit_fk);
  CREATE INDEX IF NOT EXISTS ecrf_field_value_list_entry_fk_visit_fk_ecrf_field_fk_index_version ON ecrf_field_value (list_entry_fk,visit_fk,ecrf_field_fk,index,version);
  CREATE INDEX IF NOT EXISTS ecrf_field_status_entry_list_entry_fk_ecrf_field_fk_index_status_fk_queue ON ecrf_field_status_entry (list_entry_fk,visit_fk,ecrf_field_fk,index,status_fk,queue);
  CREATE INDEX IF NOT EXISTS ecrf_field_value_ecrf_field_fk_list_entry_fk_index_version ON ecrf_field_value (ecrf_field_fk,list_entry_fk,index,version);
  CREATE INDEX IF NOT EXISTS ecrf_field_status_entry_ecrf_field_fk_list_entry_fk_index_queue_status_fk ON ecrf_field_status_entry (ecrf_field_fk,list_entry_fk,index,queue,status_fk);
  CREATE INDEX IF NOT EXISTS inquiry_category ON inquiry (category);
  CREATE INDEX IF NOT EXISTS inquiry_trial_fk_category ON inquiry (trial_fk,category);
  CREATE INDEX IF NOT EXISTS input_field_category ON input_field (category);
  CREATE INDEX IF NOT EXISTS criteria_category ON criteria (category);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_calendar ON duty_roster_turn (calendar);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_title ON duty_roster_turn (title);
  CREATE INDEX IF NOT EXISTS ecrf_field_ecrf_fk_section ON ecrf_field (ecrf_fk,section);
  CREATE INDEX IF NOT EXISTS ecrf_field_trial_fk_ecrf_fk_section ON ecrf_field (trial_fk,ecrf_fk,section);
  CREATE INDEX IF NOT EXISTS inventory_booking_calendar ON inventory_booking (calendar);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_start_stop ON duty_roster_turn (start,stop);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_stop_start ON duty_roster_turn (stop,start);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_staff_fk_start_stop ON duty_roster_turn (staff_fk,start,stop);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_staff_fk_stop_start ON duty_roster_turn (staff_fk,stop,start);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_trial_fk_start_stop ON duty_roster_turn (trial_fk,start,stop);
  CREATE INDEX IF NOT EXISTS duty_roster_turn_trial_fk_stop_start ON duty_roster_turn (trial_fk,stop,start);
  CREATE INDEX IF NOT EXISTS trial_duty_self_allocation_locked_until_from ON trial (duty_self_allocation_locked,duty_self_allocation_locked_until,duty_self_allocation_locked_from);
  CREATE INDEX IF NOT EXISTS trial_duty_self_allocation_locked_from_until ON trial (duty_self_allocation_locked,duty_self_allocation_locked_from,duty_self_allocation_locked_until);
  CREATE INDEX IF NOT EXISTS inventory_booking_start_stop ON inventory_booking (start,stop);
  CREATE INDEX IF NOT EXISTS inventory_booking_stop_start ON inventory_booking (stop,start);
  CREATE INDEX IF NOT EXISTS inventory_booking_inventory_fk_start_stop ON inventory_booking (inventory_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_booking_inventory_fk_stop_start ON inventory_booking (inventory_fk,stop,start);
  CREATE INDEX IF NOT EXISTS inventory_booking_trial_fk_start_stop ON inventory_booking (trial_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_booking_trial_fk_stop_start ON inventory_booking (trial_fk,stop,start);
  CREATE INDEX IF NOT EXISTS inventory_booking_course_fk_start_stop ON inventory_booking (course_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_booking_course_fk_stop_start ON inventory_booking (course_fk,stop,start);
  CREATE INDEX IF NOT EXISTS inventory_booking_proband_fk_start_stop ON inventory_booking (proband_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_booking_proband_fk_stop_start ON inventory_booking (proband_fk,stop,start);
  CREATE INDEX IF NOT EXISTS inventory_status_entry_inventory_fk_start_stop ON inventory_status_entry (inventory_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_status_entry_inventory_fk_stop_start ON inventory_status_entry (inventory_fk,stop,start);
  CREATE INDEX IF NOT EXISTS inventory_status_entry_start_stop ON inventory_status_entry (start,stop);
  CREATE INDEX IF NOT EXISTS inventory_status_entry_stop_start ON inventory_status_entry (stop,start);
  CREATE INDEX IF NOT EXISTS inventory_status_type_fk_entry_start_stop ON inventory_status_entry (type_fk,start,stop);
  CREATE INDEX IF NOT EXISTS inventory_status_type_fk_entry_stop_start ON inventory_status_entry (type_fk,stop,start);
  CREATE INDEX IF NOT EXISTS staff_status_entry_staff_fk_start_stop ON staff_status_entry (staff_fk,start,stop);
  CREATE INDEX IF NOT EXISTS staff_status_entry_staff_fk_stop_start ON staff_status_entry (staff_fk,stop,start);
  CREATE INDEX IF NOT EXISTS staff_status_entry_type_fk_start_stop ON staff_status_entry (type_fk,start,stop);
  CREATE INDEX IF NOT EXISTS staff_status_entry_type_fk_stop_start ON staff_status_entry (type_fk,stop,start);
  CREATE INDEX IF NOT EXISTS staff_status_entry_start_stop ON staff_status_entry (start,stop);
  CREATE INDEX IF NOT EXISTS staff_status_entry_stop_start ON staff_status_entry (stop,start);
  CREATE INDEX IF NOT EXISTS proband_status_entry_proband_fk_start_stop ON proband_status_entry (proband_fk,start,stop);
  CREATE INDEX IF NOT EXISTS proband_status_entry_proband_fk_stop_start ON proband_status_entry (proband_fk,stop,start);
  CREATE INDEX IF NOT EXISTS proband_status_entry_start_stop ON proband_status_entry (start,stop);
  CREATE INDEX IF NOT EXISTS proband_status_entry_stop_start ON proband_status_entry (stop,start);
  CREATE INDEX IF NOT EXISTS proband_status_type_fk_entry_start_stop ON proband_status_entry (type_fk,start,stop);
  CREATE INDEX IF NOT EXISTS proband_status_type_fk_entry_stop_start ON proband_status_entry (type_fk,stop,start);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_mode_start_stop ON visit_schedule_item (mode,start,stop);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_mode_stop_start ON visit_schedule_item (mode,stop,start);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_mode_start_stop ON visit_schedule_item (trial_fk,mode,start,stop);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_mode_stop_start ON visit_schedule_item (trial_fk,mode,stop,start);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_mode_start_tag_fk_stop_tag_fk ON visit_schedule_item (mode,start_tag_fk,stop_tag_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_mode_stop_tag_fk_start_tag_fk ON visit_schedule_item (mode,stop_tag_fk,start_tag_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_mode_start_tag_fk_stop_tag_fk ON visit_schedule_item (trial_fk,mode,start_tag_fk,stop_tag_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_mode_stop_tag_fk_start_tag_fk ON visit_schedule_item (trial_fk,mode,stop_tag_fk,start_tag_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_visit_fk_group_fk ON visit_schedule_item (visit_fk,group_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_group_fk_visit_fk ON visit_schedule_item (group_fk,visit_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_visit_fk_group_fk ON visit_schedule_item (trial_fk,visit_fk,group_fk);
  CREATE INDEX IF NOT EXISTS visit_schedule_item_trial_fk_group_fk_visit_fk ON visit_schedule_item (trial_fk,group_fk,visit_fk);
  CREATE INDEX IF NOT EXISTS input_field_timestmap_value ON input_field_value (timestamp_value);
  CREATE INDEX IF NOT EXISTS input_field_date_value ON input_field_value (date_value);
  CREATE INDEX IF NOT EXISTS journal_entry_modified_timestamp ON journal_entry (modified_timestamp);
  CREATE INDEX IF NOT EXISTS journal_entry_inventory_fk_system_message_code ON journal_entry (inventory_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_staff_fk_system_message_code ON journal_entry (staff_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_course_fk_system_message_code ON journal_entry (course_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_trial_fk_system_message_code ON journal_entry (trial_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_proband_fk_system_message_code ON journal_entry (proband_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_inputfield_fk_system_message_code ON journal_entry (input_field_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_user_fk_system_message_code ON journal_entry (user_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_massmail_fk_system_message_code ON journal_entry (mass_mail_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_criteria_fk_system_message_code ON journal_entry (criteria_fk,system_message_code);
  CREATE INDEX IF NOT EXISTS journal_entry_category_fk ON journal_entry (category_fk);
  CREATE INDEX IF NOT EXISTS notification_proband_fk ON notification (proband_fk);
  CREATE INDEX IF NOT EXISTS notification_visit_schedule_item_fk_proband_fk ON notification (visit_schedule_item_fk,proband_fk);
  CREATE INDEX IF NOT EXISTS notification_ecrf_status_entry_fk ON notification (ecrf_status_entry_fk);
  CREATE INDEX IF NOT EXISTS notification_ecrf_field_status_entry_fk ON notification (ecrf_field_status_entry_fk);
  CREATE INDEX IF NOT EXISTS notification_proband_status_entry_fk ON notification (proband_status_entry_fk);
  CREATE INDEX IF NOT EXISTS notification_recipient_notification_fk ON notification_recipient (notification_fk);
  CREATE INDEX IF NOT EXISTS notification_recipient_staff_fk ON notification_recipient (staff_fk);
  CREATE INDEX IF NOT EXISTS proband_contact_particulars_date_of_birth_hash ON proband_contact_particulars (date_of_birth_hash);
  CREATE INDEX IF NOT EXISTS proband_contact_particulars_alias_normalized ON proband_contact_particulars (alias_normalized);
  CREATE INDEX IF NOT EXISTS person_contact_particulars_first_name_normalized ON person_contact_particulars (first_name_normalized);
  CREATE INDEX IF NOT EXISTS person_contact_particulars_last_name_normalized ON person_contact_particulars (last_name_normalized);
  CREATE INDEX IF NOT EXISTS organisation_contact_particulars_organisation_name_normalized ON organisation_contact_particulars (organisation_name_normalized);
  CREATE INDEX IF NOT EXISTS data_table_column_user_fk_table_name_column_name ON data_table_column (user_fk,table_name,column_name);

  perform set_database_version('010901012');

end if;

end
$$;