#!/bin/bash

apt-get -q -y install tomcat9
systemctl stop tomcat9
VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
chmod 755 /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION.war
rm /var/lib/tomcat9/webapps/ROOT/ -rf
cp /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION.war /var/lib/tomcat9/webapps/ROOT.war
systemctl start tomcat9