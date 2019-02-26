
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_corrected');
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_data_na');
delete from ECRF_FIELD_STATUS_TRANSITION where e_c_r_f_field_status_types_fk = (select ID from ECRF_FIELD_STATUS_TYPE where NAME_L10N_KEY='validation_closed');