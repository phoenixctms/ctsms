
insert into PROBAND_LIST_STATUS_TRANSITION values ((select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='candidate'), (select ID from PROBAND_LIST_STATUS_TYPE where NAME_L10N_KEY='ongoing'));