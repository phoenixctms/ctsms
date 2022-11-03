#!/bin/bash

mkdir /ctsms
wget --no-verbose --no-check-certificate --content-disposition https://github.com/phoenixctms/config-default/archive/master.tar.gz -O /ctsms/config.tar.gz
tar -zxvf /ctsms/config.tar.gz -C /ctsms --strip-components 1
rm /ctsms/config.tar.gz -f          
wget --no-verbose https://api.github.com/repos/phoenixctms/master-data/tarball/master -O /ctsms/master-data.tar.gz
mkdir /ctsms/master_data
tar -zxvf /ctsms/master-data.tar.gz -C /ctsms/master_data --strip-components 1
rm /ctsms/master-data.tar.gz -f
chown ctsms:ctsms /ctsms -R
chmod 777 /ctsms -R

VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
BRANCH=${GITHUB_HEAD_REF:-${GITHUB_REF#refs/heads/}}
COMMIT=$(git log --format=%B -n 1 "$GITHUB_SHA" | sed -e 's/merge \([a-z0-9]\+\) into [a-z0-9]\+/\1/gi')
echo "application.version=$VERSION [$COMMIT]" >>/ctsms/properties/ctsms-settings.properties