
alter table PROBAND_GROUP add column RANDOMIZE BOOLEAN;
update PROBAND_GROUP set RANDOMIZE = 'f';
alter table PROBAND_GROUP alter RANDOMIZE set not null;

alter table PROBAND_LIST_ENTRY_TAG add column STRATIFICATION BOOLEAN;
update PROBAND_LIST_ENTRY_TAG set STRATIFICATION = 'f';
alter table PROBAND_LIST_ENTRY_TAG alter STRATIFICATION set not null;

alter table PROBAND_LIST_ENTRY_TAG add column RANDOMIZE BOOLEAN;
update PROBAND_LIST_ENTRY_TAG set RANDOMIZE = 'f';
alter table PROBAND_LIST_ENTRY_TAG alter RANDOMIZE set not null;
