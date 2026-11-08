#!/bin/bash

set -e

mkdir /ctsms
wget --no-verbose --tries=5 --waitretry=10 --retry-on-http-error=429,500,502,503,504 --no-check-certificate --content-disposition https://github.com/phoenixctms/config-default/archive/master.tar.gz -O /ctsms/config.tar.gz
tar -zxvf /ctsms/config.tar.gz -C /ctsms --strip-components 1
rm /ctsms/config.tar.gz -f          
wget --no-verbose --tries=5 --waitretry=10 --retry-on-http-error=429,500,502,503,504 https://api.github.com/repos/phoenixctms/master-data/tarball/master -O /ctsms/master-data.tar.gz
mkdir /ctsms/master_data
tar -zxvf /ctsms/master-data.tar.gz -C /ctsms/master_data --strip-components 1
rm /ctsms/master-data.tar.gz -f
chown ctsms:ctsms /ctsms -R
chmod 777 /ctsms -R