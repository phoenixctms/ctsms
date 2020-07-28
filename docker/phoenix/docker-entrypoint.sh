#!/bin/bash -x

# set up a properties based on environment
function set_properties {
    : "${DEFAULT_LOCALE:=en}"
    : "${DBSERVER:=db}"
    : "${DBPORT:=5432}"
    : "${DBUSER:=ctsms}"
    : "${DBNAME:=${DBUSER}}"
    : "${DBPASS:=${DBUSER}}"

    for prop in /ctsms/properties/ctsms-applicationcontext.properties /ctsms/properties/ctsms-dbtool.properties; do
        echo "database_username=${DBUSER}" >> "$prop"
        echo "database_password=${DBPASS}" >> "$prop"
        echo "database_connection_url=jdbc:postgresql://${DBSERVER}/${DBNAME}" >> "$prop"
        echo "default_locale=${DEFAULT_LOCALE}" >> "$prop"
    done
}

set_properties
exec /usr/local/tomcat/bin/catalina.sh run