
alter table PASSWORD add column ENABLE2FA BOOLEAN;
update PASSWORD set ENABLE2FA = 'f';
alter table PASSWORD alter ENABLE2FA set not null;

alter table PASSWORD add column SHOW_OTP_REGISTRATION_INFO BOOLEAN;
update PASSWORD set SHOW_OTP_REGISTRATION_INFO = 'f';
alter table PASSWORD alter SHOW_OTP_REGISTRATION_INFO set not null;
