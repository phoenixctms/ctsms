#!/bin/bash

apt-get -q -y -o=Dpkg::Use-Pty=0 install tomcat10
systemctl stop tomcat10
usermod --append --groups ctsms tomcat
#VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
#sed -r -i "s/^JAVA_OPTS.+/JAVA_OPTS=\"-server -Djava.awt.headless=true -Xms$XMS -Xmx$XMX -Xss$XSS -XX:+UseParallelGC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:+CMSClassUnloadingEnabled -XX:ReservedCodeCacheSize=$PERM\"/" /etc/default/tomcat9
echo "CTSMS_PROPERTIES=$CTSMS_PROPERTIES" >>/etc/default/tomcat10
echo "CTSMS_JAVA=$CTSMS_JAVA" >>/etc/default/tomcat10
echo "JDK_JAVA_OPTIONS=--add-opens java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED" >>/etc/default/tomcat10
sed -r -i "s|# Lifecycle|EnvironmentFile=/etc/default/tomcat10\\n\\n# Lifecycle|" /usr/lib/systemd/system/tomcat10.service
sed -r -i "s|# Security|# Security\\nReadWritePaths=/ctsms/external_files/ /ctsms/bulk_processor/output/ /ctsms/ /tmp/|" /usr/lib/systemd/system/tomcat10.service
tar -xvzf /home/runner/work/ctsms/ctsms/.github/workflows/jakartaee-migration-1.0.8-bin.tar.gz -C /ctsms
/ctsms/jakartaee-migration-1.0.8/bin/migrate.sh /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION.war /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION-migrated.war
chmod 755 /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION-migrated.war
rm /var/lib/tomcat10/webapps/ROOT/ -rf
#cp /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/ctsms-core-$VERSION.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/*
cp /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION-migrated.war /var/lib/tomcat9/webapps/ROOT.war
systemctl daemon-reload
systemctl start tomcat10
