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

end
$$;
