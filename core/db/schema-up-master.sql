do
$$
begin

if get_database_version() < '010901001' then

  create extension if not exists "uuid-ossp";

  alter table PROBAND_LIST_ENTRY add column BEACON CHARACTER VARYING(1024);
  update PROBAND_LIST_ENTRY set BEACON = uuid_generate_v4();
  alter table PROBAND_LIST_ENTRY alter BEACON set not null;

  alter table PROBAND_LIST_ENTRY add constraint proband_list_entry_beacon_key unique (BEACON);  
  
  perform set_database_version('010901001');
  
end if;

end
$$;