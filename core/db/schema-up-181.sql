alter table PASSWORD add column ENABLE2FA BOOLEAN;
update PASSWORD set ENABLE2FA = 'f';
alter table PASSWORD alter ENABLE2FA set not null;

alter table PASSWORD add column OTP_TYPE CHARACTER VARYING(1024);
alter table PASSWORD add column OTP_SECRET_SALT BYTEA unique;
alter table PASSWORD add column OTP_SECRET_IV BYTEA unique;
alter table PASSWORD add column ENCRYPTED_OTP_SECRET BYTEA unique;

alter table PASSWORD add column SHOW_OTP_REGISTRATION_INFO CHARACTER VARYING(1024);
update PASSWORD set SHOW_OTP_REGISTRATION_INFO = 'f';
alter table PASSWORD alter SHOW_OTP_REGISTRATION_INFO set not null;
