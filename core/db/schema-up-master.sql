do
$$
begin

if get_database_version() < '010801010' then

  ALTER TABLE staff_address ADD COLUMN province CHARACTER VARYING(1024);

  ALTER TABLE proband_address ADD COLUMN ENCRYPTED_PROVINCE BYTEA,
    ADD COLUMN PROVINCE_IV BYTEA,
    ADD COLUMN PROVINCE_HASH BYTEA;

  -- select dbtool(1);

  -- ALTER TABLE proband_address ALTER COLUMN ENCRYPTED_PROVINCE SET not null, ADD unique (ENCRYPTED_PROVINCE),
  --  ALTER COLUMN PROVINCE_IV SET not null, ADD unique (PROVINCE_IV);

  ALTER TABLE street ADD COLUMN province CHARACTER VARYING(1024);

  CREATE INDEX street_country_name_province_zip_code_street_name ON street (country_name,province,zip_code,street_name);
  CREATE INDEX street_country_name_province_city_name_street_name ON street (country_name,province,city_name,street_name);
  --CREATE INDEX street_country_name_province_city_name ON street (country_name,province,city_name);

  ALTER TABLE zip ADD COLUMN province CHARACTER VARYING(1024);

  CREATE INDEX zip_country_name_province_city_name ON zip (country_name,province,city_name);
  CREATE INDEX zip_country_name_province_zip_code ON zip (country_name,province,zip_code);

  perform set_database_version('010801010');
  
end if;

if get_database_version() < '010801020' then

  ALTER TABLE users ADD COLUMN locked_untrusted BOOLEAN;
  UPDATE users SET locked_untrusted = 'f';
  ALTER TABLE users ALTER locked_untrusted SET NOT NULL;
  
  perform set_database_version('010801020');
  
end if;

if get_database_version() < '010801030' then

  create extension if not exists "uuid-ossp";

  alter table ECRF_FIELD add column REF CHARACTER VARYING(1024);
  update ECRF_FIELD set REF = left(replace(uuid_generate_v4()::text,'-',''),8);
  alter table ECRF_FIELD alter REF set not null;
  
  perform set_database_version('010801030');
  
end if;

if get_database_version() < '010801040' then

  alter table USERS add column TAB_ORIENTATION CHARACTER VARYING(1024);
  
  perform set_database_version('010801040');
  
end if;

if get_database_version() < '010801050' then

  CREATE INDEX user_permission_profile_user_fk_active_profile ON user_permission_profile (user_fk,active,profile);
  
  perform set_database_version('010801050');
  
end if;

if get_database_version() < '010801060' then

  ALTER TABLE mass_mail ADD COLUMN store_messages BOOLEAN;
  UPDATE mass_mail SET store_messages = 'f';
  ALTER TABLE mass_mail ALTER store_messages SET NOT NULL;
  
  perform set_database_version('010801060');
  
end if;

if get_database_version() < '010801070' then

  create table file_department (
      FILES_FK BIGINT not null,
      DEPARTMENTS_FK BIGINT not null
  );
  
  create table hyperlink_department (
      HYPERLINKS_FK BIGINT not null,
      DEPARTMENTS_FK BIGINT not null
  );

  alter table file_department 
      add constraint DEPARTMENT_FILES_FKC 
      foreign key (FILES_FK) 
      references FILE;

  alter table file_department 
      add constraint FILE_DEPARTMENTS_FKC 
      foreign key (DEPARTMENTS_FK) 
      references DEPARTMENT;

  alter table hyperlink_department 
      add constraint DEPARTMENT_HYPERLINKS_FKC 
      foreign key (HYPERLINKS_FK) 
      references HYPERLINK;

  alter table hyperlink_department 
      add constraint HYPERLINK_DEPARTMENTS_FKC 
      foreign key (DEPARTMENTS_FK) 
      references DEPARTMENT;
      
  perform set_database_version('010801070');
  
end if;

if get_database_version() < '010801080' then

  ALTER TABLE visit_schedule_item ADD COLUMN internal BOOLEAN;
  UPDATE visit_schedule_item SET internal = 'f';
  ALTER TABLE visit_schedule_item ALTER internal SET NOT NULL;
  
  ALTER TABLE visit_schedule_item ADD COLUMN description TEXT;
  UPDATE visit_schedule_item SET description = '';
  
  perform set_database_version('010801080');
  
end if;

if get_database_version() < '010801090' then

  insert into PROBAND_LIST_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "reason_required", "blocking", "count", "screening", "ic", "ecrf_value_input_enabled", "signup", "person")
  values (nextval('hibernate_sequence'), 'CYAN', 'f', 're_screening', 't', 'f', 'f', 't', 'f', 't', 'f', 't');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'SCREENING' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_ok' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_ok' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_failure' limit 1)
  );

  insert into PROBAND_LIST_STATUS_TRANSITION
    ("proband_list_status_types_fk","transitions_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'screening_failure' limit 1),
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 're_screening' limit 1)
  );
  
  perform set_database_version('010801090');
  
end if;

if get_database_version() < '010801091' then

  ALTER TABLE team_member ADD COLUMN ecrf BOOLEAN;
  UPDATE team_member SET ecrf = (sign or resolve or verify);
  ALTER TABLE team_member ALTER ecrf SET NOT NULL;
  
  perform set_database_version('010801091');
  
end if;

if get_database_version() < '010801092' then

  insert into PROBAND_LIST_STATUS_LOG_LEVEL
    ("id", "log_level")
  values (nextval('hibernate_sequence'), 'PROBAND_STATUS');

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'candidate' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'PROBAND_STATUS' limit 1)
  );
  
  perform set_database_version('010801092');

end if;

if get_database_version() < '010801093' then

  alter table PROBAND_CONTACT_PARTICULARS add column FIRST_NAME_NORMALIZED_HASH BYTEA;
  CREATE INDEX proband_contact_particulars_first_name_normalized_hash ON proband_contact_particulars (first_name_normalized_hash);
  
  alter table PROBAND_CONTACT_PARTICULARS add column LAST_NAME_NORMALIZED_HASH BYTEA;
  CREATE INDEX proband_contact_particulars_last_name_normalized_hash ON proband_contact_particulars (last_name_normalized_hash);
  
  alter table PROBAND_CONTACT_PARTICULARS add column ALIAS_NORMALIZED CHARACTER VARYING(1024);
  CREATE INDEX proband_contact_particulars_alias_normalized ON proband_contact_particulars (alias_normalized);  
  
  alter table ORGANISATION_CONTACT_PARTICULARS add column ORGANISATION_NAME_NORMALIZED CHARACTER VARYING(1024);
  update ORGANISATION_CONTACT_PARTICULARS set ORGANISATION_NAME_NORMALIZED = ORGANISATION_NAME;
  alter table ORGANISATION_CONTACT_PARTICULARS alter ORGANISATION_NAME_NORMALIZED set not null;
  CREATE INDEX organisation_contact_particulars_organisation_name_normalized ON organisation_contact_particulars (organisation_name_normalized);
  
  alter table PERSON_CONTACT_PARTICULARS add column FIRST_NAME_NORMALIZED CHARACTER VARYING(1024);
  update PERSON_CONTACT_PARTICULARS set FIRST_NAME_NORMALIZED = FIRST_NAME;
  alter table PERSON_CONTACT_PARTICULARS alter FIRST_NAME_NORMALIZED set not null;
  CREATE INDEX person_contact_particulars_first_name_normalized ON person_contact_particulars (first_name_normalized);
  
  alter table PERSON_CONTACT_PARTICULARS add column LAST_NAME_NORMALIZED CHARACTER VARYING(1024);
  update PERSON_CONTACT_PARTICULARS set LAST_NAME_NORMALIZED = LAST_NAME;
  alter table PERSON_CONTACT_PARTICULARS alter LAST_NAME_NORMALIZED set not null;
  CREATE INDEX person_contact_particulars_last_name_normalized ON person_contact_particulars (last_name_normalized);    
  
  perform set_database_version('010801093');

end if;

if get_database_version() < '010801094' then

  insert into ecrf_field_status_transition
    ("e_c_r_f_field_status_types_fk","transitions_fk")
  values (
    (select id from ecrf_field_status_type where name_l10n_key = 'validation_error' limit 1),
    (select id from ecrf_field_status_type where name_l10n_key = 'validation_data_na' limit 1)
  );
  
  insert into ecrf_field_status_transition
    ("e_c_r_f_field_status_types_fk","transitions_fk")
  values (
    (select id from ecrf_field_status_type where name_l10n_key = 'validation_error' limit 1),
    (select id from ecrf_field_status_type where name_l10n_key = 'validation_corrected' limit 1)
  );  

  perform set_database_version('010801094');
  
end if;

if get_database_version() < '010801095' then

  delete from PROBAND_LIST_STATUS_TYPE_LOG_LEVEL where log_levels_fk = (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'PROBAND_STATUS' limit 1);

  insert into PROBAND_LIST_STATUS_TYPE_LOG_LEVEL
    ("proband_list_status_types_fk","log_levels_fk")
  values (
    (select id from PROBAND_LIST_STATUS_TYPE where name_l10n_key = 'ic_signed' limit 1),
    (select id from PROBAND_LIST_STATUS_LOG_LEVEL where log_level = 'PROBAND_STATUS' limit 1)
  );
  
  perform set_database_version('010801095');

end if;

if get_database_version() < '010801096' then

  ALTER TABLE privacy_consent_status_type ADD COLUMN confirm BOOLEAN;
  UPDATE privacy_consent_status_type SET confirm = 'f';
  ALTER TABLE privacy_consent_status_type ALTER confirm SET NOT NULL;
  
  ALTER TABLE mass_mail_recipient ADD COLUMN CONFIRMED BIGINT;
  UPDATE mass_mail_recipient SET confirmed = 0;
  ALTER TABLE mass_mail_recipient ALTER confirmed SET NOT NULL;  
  
  ALTER TABLE mass_mail_recipient ADD COLUMN CONFIRMED_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE;
  
  update privacy_consent_status_type set color = 'YELLOW' where name_l10n_key = 'registered';
  update privacy_consent_status_type set color = 'KHAKI' where name_l10n_key = 'existing_privacy_consent_ok';
  update privacy_consent_status_type set color = 'LIME' where name_l10n_key = 'privacy_consent_received';

  insert into PRIVACY_CONSENT_STATUS_TYPE
    ("id", "color", "initial", "name_l10n_key", "auto_delete", "confirm")
  values (nextval('hibernate_sequence'), 'LIMEGREEN', 'f', 'confirmed', 'f', 't');

  insert into PRIVACY_CONSENT_STATUS_TRANSITION
    ("privacy_consent_status_types_fk","transitions_fk")
  values (
    (select id from PRIVACY_CONSENT_STATUS_TYPE where name_l10n_key = 'confirmed' limit 1),
    (select id from PRIVACY_CONSENT_STATUS_TYPE where name_l10n_key = 'confirmed' limit 1)
  );
  
  perform set_database_version('010801096');

end if;

if get_database_version() < '010801097' then

  ALTER TABLE mass_mail ADD COLUMN attach_visit_plans BOOLEAN;
  UPDATE mass_mail SET attach_visit_plans = 'f';
  ALTER TABLE mass_mail ALTER attach_visit_plans SET NOT NULL;
  
  perform set_database_version('010801097');

end if;

if get_database_version() < '010801098' then

  ALTER TABLE MASS_MAIL_TYPE ADD COLUMN VISIT_SCHEDULE_ITEMS_REQUIRED BOOLEAN;
  UPDATE MASS_MAIL_TYPE SET VISIT_SCHEDULE_ITEMS_REQUIRED = 'f';
  ALTER TABLE MASS_MAIL_TYPE ALTER VISIT_SCHEDULE_ITEMS_REQUIRED SET NOT NULL;
  
  alter table MASS_MAIL_RECIPIENT add column TOKEN CHARACTER VARYING(1024);
  
  insert into MASS_MAIL_TYPE
    ("id", "name_l10n_key", "visible", "trial_required", "proband_list_staus_required", "visit_schedule_items_required")
  values (nextval('hibernate_sequence'), 'visit_reminder', 't', 't', 'f', 't');
  
  create table mass_mail_visit_schedule_item (
    MASS_MAILS_FK BIGINT not null,
    VISIT_SCHEDULE_ITEMS_FK BIGINT not null
  );

  alter table mass_mail_visit_schedule_item 
    add constraint VISIT_SCHEDULE_ITEM_MASS_MAILS_FKC 
    foreign key (MASS_MAILS_FK) 
    references MASS_MAIL;

  alter table mass_mail_visit_schedule_item 
    add constraint MASS_MAIL_VISIT_SCHEDULE_ITEMS_FKC 
    foreign key (VISIT_SCHEDULE_ITEMS_FK) 
    references VISIT_SCHEDULE_ITEM;
  
  perform set_database_version('010801098');

end if;

if get_database_version() < '010801099' then

  INSERT INTO job_type
  ("id", "module", "name_l10n_key", "description_l10n_key", "command_format", "visible", "daily", "weekly", "monthly", "input_file", "output_file", "encrypt_file", "email_recipients", "trial_fk") VALUES
  (nextval('hibernate_sequence'), 'TRIAL_JOB', 'export_done_ecrf_pdfs', 'export_done_ecrf_pdfs', '{10} --task=cleanup_all --task=publish_ecrf_data_pdfs --task=cleanup_all -id={1} -auth={4} -jid={5} --upload --signed --force', 't', 'f', 'f', 'f', 'f', 'f', 'f', 't', NULL);

  INSERT INTO job_type
  ("id", "module", "name_l10n_key", "description_l10n_key", "command_format", "visible", "daily", "weekly", "monthly", "input_file", "output_file", "encrypt_file", "email_recipients", "trial_fk") VALUES
  (nextval('hibernate_sequence'), 'TRIAL_JOB', 'export_done_ecrf_data', 'export_done_ecrf_data', '{10} --task=cleanup_all --task=export_ecrf_data_vertical --task=export_ecrf_data_horizontal --task=publish_ecrf_data_sqlite --task=publish_ecrf_data_horizontal_csv --task=publish_ecrf_data_xls --task=cleanup_all -id={1} -auth={4} -jid={5} -tz={6} --upload --signed --force', 't', 'f', 'f', 'f', 'f', 'f', 'f', 't', NULL);
  
  perform set_database_version('010801099');

end if;
 
end
$$;
