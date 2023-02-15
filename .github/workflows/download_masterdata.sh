#!/bin/bash

mkdir /ctsms
wget --no-verbose --no-check-certificate --content-disposition https://github.com/phoenixctms/config-default/archive/master.tar.gz -O /ctsms/config.tar.gz
tar -zxvf /ctsms/config.tar.gz -C /ctsms --strip-components 1
rm /ctsms/config.tar.gz -f          
wget --no-verbose https://api.github.com/repos/phoenixctms/master-data/tarball/master -O /ctsms/master-data.tar.gz
mkdir /ctsms/master_data
tar -zxvf /ctsms/master-data.tar.gz -C /ctsms/master_data --strip-components 1
rm /ctsms/master-data.tar.gz -f

echo "db_tool=/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh" >> /home/runner/work/ctsms/properties/ctsms-settings.properties

chown ctsms:ctsms /ctsms -R
chmod 777 /ctsms -R