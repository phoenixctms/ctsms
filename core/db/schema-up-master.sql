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

end
$$;