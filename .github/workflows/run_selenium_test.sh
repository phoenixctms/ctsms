#!/bin/bash
VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
$JAVA_HOME/bin/java -Dfile.encoding=Cp1252 -Dwebdriver.chrome.driver=/usr/bin/chromedriver -classpath /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/ctsms-core-$VERSION.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/* $*