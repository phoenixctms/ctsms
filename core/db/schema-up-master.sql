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

end
$$;
