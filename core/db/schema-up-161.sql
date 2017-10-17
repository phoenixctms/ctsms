alter table FILE add column PUBLIC_FILE BOOLEAN;
update FILE set PUBLIC_FILE = 'f';
alter table FILE alter PUBLIC_FILE set not null;