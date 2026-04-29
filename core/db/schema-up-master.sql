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
  '{12} --task=cleanup --task=import_inquiry_data_horizontal --task=cleanup -id={1} -auth={4} -jid={5} -tz={6} --force --skip-errors',
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

end
$$;