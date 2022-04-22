create or replace function get_database_version()
returns text as $$
  select coalesce((select description from pg_shdescription join pg_database on objoid = pg_database.oid where datname = 'ctsms' limit 1),'000000000');
$$ language sql stable;

create or replace function set_database_version(text)
returns void as $$
begin
  raise notice 'database schema version set to %', $1;
  execute format('comment on database ctsms is %L',$1);
end
$$ language plpgsql;

select set_database_version('010801000');