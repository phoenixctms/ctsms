-- authorization:
CREATE INDEX permission_service_method ON permission (service_method);
CREATE INDEX profile_permission_permission_fk_profile_active ON profile_permission (permission_fk,profile,active);

-- criteria:
CREATE INDEX criterion_property_module_property ON criterion_property (module,property);

-- file managers:
CREATE INDEX file_logical_path ON file (logical_path);
CREATE INDEX file_file_name ON file (file_name);
CREATE INDEX file_title ON file (title);
CREATE INDEX file_folder_preset_logical_path ON file_folder_preset (logical_path);
--CREATE INDEX file_inventory_fk_module_logical_path ON file (inventory_fk,module,logical_path);
--CREATE INDEX file_staff_fk_module_logical_path ON file (staff_fk,module,logical_path);
--CREATE INDEX file_course_fk_module_logical_path ON file (course_fk,module,logical_path);
--CREATE INDEX file_trial_fk_module_logical_path ON file (trial_fk,module,logical_path);
--CREATE INDEX file_proband_fk_module_logical_path ON file (proband_fk,module,logical_path);
--CREATE INDEX file_mass_mail_fk_module_logical_path ON file (mass_amil_fk,module,logical_path);

-- addresses:
CREATE INDEX street_country_name_zip_code_street_name ON street (country_name,zip_code,street_name);
CREATE INDEX street_country_name_city_name_street_name ON street (country_name,city_name,street_name);
--CREATE INDEX street_country_name_city_name ON street (country_name,city_name);

CREATE INDEX zip_country_name_city_name ON zip (country_name,city_name);
CREATE INDEX zip_country_name_zip_code ON zip (country_name,zip_code);

-- bank identification:
CREATE INDEX bank_identification_bank_code_number ON bank_identification (bank_code_number);
CREATE INDEX bank_identification_bank_name ON bank_identification (bank_name);

-- proband list tag values:
CREATE INDEX proband_list_entry_tag_value_list_entry_fk_tag_fk ON proband_list_entry_tag_value (list_entry_fk,tag_fk);

-- inquiry values:
CREATE INDEX inquiry_value_proband_fk_inquiry_fk ON inquiry_value (proband_fk,inquiry_fk);

-- ecrf status, field values, field status
CREATE INDEX ecrf_status_entry_list_entry_fk_ecrf_fk_visit_fk ON ecrf_status_entry (list_entry_fk,ecrf_fk,visit_fk);
CREATE INDEX ecrf_field_value_list_entry_fk_visit_fk_ecrf_field_fk_index_version ON ecrf_field_value (list_entry_fk,visit_fk,ecrf_field_fk,index,version);
CREATE INDEX ecrf_field_status_entry_list_entry_fk_ecrf_field_fk_index_status_fk_queue ON ecrf_field_status_entry (list_entry_fk,visit_fk,ecrf_field_fk,index,status_fk,queue);

-- categories:
CREATE INDEX inquiry_category ON inquiry (category);
CREATE INDEX input_field_category ON input_field (category);
CREATE INDEX criteria_category ON criteria (category);
CREATE INDEX duty_roster_turn_calendar ON duty_roster_turn (calendar);
CREATE INDEX duty_roster_turn_title ON duty_roster_turn (title);
CREATE INDEX ecrf_field_ecrf_fk_section ON ecrf_field (ecrf_fk,section);
CREATE INDEX ecrf_field_trial_fk_ecrf_fk_section ON ecrf_field (trial_fk,ecrf_fk,section);
CREATE INDEX inventory_booking_calendar ON duty_roster_turn (calendar);

-- duty roster:
CREATE INDEX duty_roster_turn_start_stop ON duty_roster_turn (start,stop);
CREATE INDEX duty_roster_turn_stop_start ON duty_roster_turn (stop,start);
CREATE INDEX duty_roster_turn_staff_fk_start_stop ON duty_roster_turn (staff_fk,start,stop);
CREATE INDEX duty_roster_turn_staff_fk_stop_start ON duty_roster_turn (staff_fk,stop,start);
CREATE INDEX duty_roster_turn_trial_fk_start_stop ON duty_roster_turn (trial_fk,start,stop);
CREATE INDEX duty_roster_turn_trial_fk_stop_start ON duty_roster_turn (trial_fk,stop,start);
create index trial_duty_self_allocation_locked_until_from on trial (duty_self_allocation_locked,duty_self_allocation_locked_until,duty_self_allocation_locked_from);
create index trial_duty_self_allocation_locked_from_until on trial (duty_self_allocation_locked,duty_self_allocation_locked_from,duty_self_allocation_locked_until);
create index trial_duty_self_allocation_until_from on trial (duty_self_allocation_locked,duty_self_allocation_locked_until,duty_self_allocation_locked_from);
create index trial_duty_self_allocation_from_until on trial (duty_self_allocation_locked,duty_self_allocation_locked_from,duty_self_allocation_locked_until);

-- inventory bookings:
CREATE INDEX inventory_booking_start_stop ON inventory_booking (start,stop);
CREATE INDEX inventory_booking_stop_start ON inventory_booking (stop,start);
CREATE INDEX inventory_booking_inventory_fk_start_stop ON inventory_booking (inventory_fk,start,stop);
CREATE INDEX inventory_booking_inventory_fk_stop_start ON inventory_booking (inventory_fk,stop,start);
CREATE INDEX inventory_booking_trial_fk_start_stop ON inventory_booking (trial_fk,start,stop);
CREATE INDEX inventory_booking_trial_fk_stop_start ON inventory_booking (trial_fk,stop,start);
CREATE INDEX inventory_booking_course_fk_start_stop ON inventory_booking (course_fk,start,stop);
CREATE INDEX inventory_booking_course_fk_stop_start ON inventory_booking (course_fk,stop,start);
CREATE INDEX inventory_booking_proband_fk_start_stop ON inventory_booking (proband_fk,start,stop);
CREATE INDEX inventory_booking_proband_fk_stop_start ON inventory_booking (proband_fk,stop,start);
CREATE INDEX inventory_status_entry_inventory_fk_start_stop ON inventory_status_entry (inventory_fk,start,stop);
CREATE INDEX inventory_status_entry_inventory_fk_stop_start ON inventory_status_entry (inventory_fk,stop,start);
CREATE INDEX inventory_status_entry_start_stop ON inventory_status_entry (start,stop);
CREATE INDEX inventory_status_entry_stop_start ON inventory_status_entry (stop,start);
CREATE INDEX inventory_status_type_fk_entry_start_stop ON inventory_status_entry (type_fk,start,stop);
CREATE INDEX inventory_status_type_fk_entry_stop_start ON inventory_status_entry (type_fk,stop,start);

-- staff status:
CREATE INDEX staff_status_entry_staff_fk_start_stop ON staff_status_entry (staff_fk,start,stop);
CREATE INDEX staff_status_entry_staff_fk_stop_start ON staff_status_entry (staff_fk,stop,start);
CREATE INDEX staff_status_entry_type_fk_start_stop ON staff_status_entry (type_fk,start,stop);
CREATE INDEX staff_status_entry_type_fk_stop_start ON staff_status_entry (type_fk,stop,start);
CREATE INDEX staff_status_entry_start_stop ON staff_status_entry (start,stop);
CREATE INDEX staff_status_entry_stop_start ON staff_status_entry (stop,start);

-- proband status:
CREATE INDEX proband_status_entry_proband_fk_start_stop ON proband_status_entry (proband_fk,start,stop);
CREATE INDEX proband_status_entry_proband_fk_stop_start ON proband_status_entry (proband_fk,stop,start);
CREATE INDEX proband_status_entry_start_stop ON proband_status_entry (start,stop);
CREATE INDEX proband_status_entry_stop_start ON proband_status_entry (stop,start);
CREATE INDEX proband_status_type_fk_entry_start_stop ON proband_status_entry (type_fk,start,stop);
CREATE INDEX proband_status_type_fk_entry_stop_start ON proband_status_entry (type_fk,stop,start);

-- visit schedule items:
CREATE INDEX visit_schedule_item_mode_start_stop ON visit_schedule_item (mode,start,stop);
CREATE INDEX visit_schedule_item_mode_stop_start ON visit_schedule_item (mode,stop,start);
CREATE INDEX visit_schedule_item_trial_fk_mode_start_stop ON visit_schedule_item (trial_fk,mode,start,stop);
CREATE INDEX visit_schedule_item_trial_fk_mode_stop_start ON visit_schedule_item (trial_fk,mode,stop,start);

CREATE INDEX visit_schedule_item_mode_start_tag_fk_stop_tag_fk ON visit_schedule_item (mode,start_tag_fk,stop_tag_fk);
CREATE INDEX visit_schedule_item_mode_stop_tag_fk_start_tag_fk ON visit_schedule_item (mode,stop_tag_fk,start_tag_fk);
CREATE INDEX visit_schedule_item_trial_fk_mode_start_tag_fk_stop_tag_fk ON visit_schedule_item (trial_fk,mode,start_tag_fk,stop_tag_fk);
CREATE INDEX visit_schedule_item_trial_fk_mode_stop_tag_fk_start_tag_fk ON visit_schedule_item (trial_fk,mode,stop_tag_fk,start_tag_fk);

CREATE INDEX visit_schedule_item_visit_fk_group_fk ON visit_schedule_item (visit_fk,group_fk);
CREATE INDEX visit_schedule_item_group_fk_visit_fk ON visit_schedule_item (group_fk,visit_fk);
CREATE INDEX visit_schedule_item_trial_fk_visit_fk_group_fk ON visit_schedule_item (trial_fk,visit_fk,group_fk);
CREATE INDEX visit_schedule_item_trial_fk_group_fk_visit_fk ON visit_schedule_item (trial_fk,group_fk,visit_fk);

CREATE INDEX input_field_timestmap_value ON input_field_value (timestamp_value);
CREATE INDEX input_field_date_value ON input_field_value (date_value);

-- journal:
CREATE INDEX journal_entry_modified_timestamp ON journal_entry (modified_timestamp);

create index journal_entry_inventory_fk_system_message_code ON JOURNAL_ENTRY (INVENTORY_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_staff_fk_system_message_code ON JOURNAL_ENTRY (STAFF_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_course_fk_system_message_code ON JOURNAL_ENTRY (COURSE_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_trial_fk_system_message_code ON JOURNAL_ENTRY (TRIAL_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_proband_fk_system_message_code ON JOURNAL_ENTRY (PROBAND_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_inputfield_fk_system_message_code ON JOURNAL_ENTRY (INPUT_FIELD_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_user_fk_system_message_code ON JOURNAL_ENTRY (USER_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_massmail_fk_system_message_code ON JOURNAL_ENTRY (MASS_MAIL_FK,SYSTEM_MESSAGE_CODE);
create index journal_entry_criteria_fk_system_message_code ON JOURNAL_ENTRY (CRITERIA_FK,SYSTEM_MESSAGE_CODE);

-- hash columns:
--SELECT 'CREATE INDEX ' || table_name || '_' || column_name || ' ON ' || table_name || ' (' || column_name || ');' FROM information_schema.columns where table_catalog='ctsms' and right(column_name,5)='_hash';

CREATE INDEX medication_comment_hash ON medication (comment_hash);
CREATE INDEX money_transfer_comment_hash ON money_transfer (comment_hash);
CREATE INDEX proband_contact_particulars_comment_hash ON proband_contact_particulars (comment_hash);
CREATE INDEX procedure_comment_hash ON procedure (comment_hash);
CREATE INDEX proband_tag_value_value_hash ON proband_tag_value (value_hash);
CREATE INDEX proband_status_entry_comment_hash ON proband_status_entry (comment_hash);
CREATE INDEX proband_list_status_entry_reason_hash ON proband_list_status_entry (reason_hash);
CREATE INDEX proband_contact_particulars_data_hash ON proband_contact_particulars (data_hash);
CREATE INDEX proband_contact_particulars_file_name_hash ON proband_contact_particulars (file_name_hash);
CREATE INDEX proband_contact_particulars_citizenship_hash ON proband_contact_particulars (citizenship_hash);
CREATE INDEX proband_contact_particulars_date_of_birth_hash ON proband_contact_particulars (date_of_birth_hash);
CREATE INDEX proband_contact_particulars_last_name_hash ON proband_contact_particulars (last_name_hash);
CREATE INDEX proband_contact_particulars_first_name_hash ON proband_contact_particulars (first_name_hash);
CREATE INDEX proband_contact_detail_value_comment_hash ON proband_contact_detail_value (comment_hash);
CREATE INDEX proband_contact_detail_value_value_hash ON proband_contact_detail_value (value_hash);
CREATE INDEX proband_address_care_of_hash ON proband_address (care_of_hash);
CREATE INDEX proband_address_door_number_hash ON proband_address (door_number_hash);
CREATE INDEX proband_address_entrance_hash ON proband_address (entrance_hash);
CREATE INDEX proband_address_house_number_hash ON proband_address (house_number_hash);
CREATE INDEX proband_address_street_name_hash ON proband_address (street_name_hash);
CREATE INDEX proband_address_city_name_hash ON proband_address (city_name_hash);
CREATE INDEX proband_address_zip_code_hash ON proband_address (zip_code_hash);
CREATE INDEX proband_address_country_name_hash ON proband_address (country_name_hash);
CREATE INDEX password_password_hash ON password (password_hash);
CREATE INDEX journal_entry_title_hash ON journal_entry (title_hash);
CREATE INDEX journal_entry_comment_hash ON journal_entry (comment_hash);
CREATE INDEX file_file_name_hash ON file (file_name_hash);
CREATE INDEX file_comment_hash ON file (comment_hash);
CREATE INDEX file_title_hash ON file (title_hash);
CREATE INDEX diagnosis_comment_hash ON diagnosis (comment_hash);
CREATE INDEX department_department_password_hash ON department (department_password_hash);
CREATE INDEX bank_account_bank_name_hash ON bank_account (bank_name_hash);
CREATE INDEX bank_account_bic_hash ON bank_account (bic_hash);
CREATE INDEX bank_account_iban_hash ON bank_account (iban_hash);
CREATE INDEX bank_account_bank_code_number_hash ON bank_account (bank_code_number_hash);
CREATE INDEX bank_account_account_number_hash ON bank_account (account_number_hash);
CREATE INDEX bank_account_account_holder_name_hash ON bank_account (account_holder_name_hash);


CREATE INDEX data_table_column_user_fk_table_name_column_name ON data_table_column (user_fk, table_name, column_name);