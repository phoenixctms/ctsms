#!/bin/bash

echo "email_exec_from_address=$MAIL_SENDER_ADDRESS" >> /ctsms/properties/ctsms-settings.properties
echo "email_exec_from_name=Phoenix CTSMS Automated Tests" >> /ctsms/properties/ctsms-settings.properties
echo "job_mail_sender_host=$MAIL_SENDER_HOST" >> /ctsms/properties/ctsms-applicationcontext.properties
echo "job_mail_sender_port=$MAIL_SENDER_PORT" >> /ctsms/properties/ctsms-applicationcontext.properties
echo "job_mail_sender_protocol=$MAIL_SENDER_PROTOCOL" >> /ctsms/properties/ctsms-applicationcontext.properties
echo "job_mail_sender_username=$MAIL_SENDER_USERNAME" >> /ctsms/properties/ctsms-applicationcontext.properties
echo "job_mail_sender_password=$MAIL_SENDER_PASSWORD" >> /ctsms/properties/ctsms-applicationcontext.properties
touch /ctsms/properties/ctsms-job-mail-sender.properties
chown ctsms:ctsms /ctsms/properties/ctsms-job-mail-sender.properties
chmod 777 /ctsms/properties/ctsms-job-mail-sender.properties
echo "mail.smtp.auth=$MAIL_SENDER_AUTH" >> /ctsms/properties/ctsms-job-mail-sender.properties
echo "mail.smtp.starttls.enable=$MAIL_SENDER_STARTTLS" >> /ctsms/properties/ctsms-job-mail-sender.properties
chown ctsms:ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh
chmod 755 /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh