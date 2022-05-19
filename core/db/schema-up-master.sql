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

end
$$;
