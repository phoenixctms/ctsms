#!/bin/bash
CTSMS_PROPERTIES=/ctsms/properties
mvn surefire:test -Dtest=$1 -DskipTests=false -Dwebdriver.chrome.driver="/usr/bin/chromedriver" -Dctsms.test.baseurl="http://localhost:8080" -Dctsms.test.directory="/home/runner/work/ctsms/testoutput" -DsurefireReportsDirectory="/home/runner/work/ctsms/testoutput" -Dctsms.test.windowsize="2000,3000" -Dctsms.test.emailrecipients="rkrenn@phoenixctms.org" --no-transfer-progress 
#sudo $JAVA_HOME/bin/java -Dfile.encoding=Cp1252 -Dwebdriver.chrome.driver=/usr/bin/chromedriver -classpath /home/runner/work/ctsms/ctsms/core/target/test-classes/:/home/runner/work/ctsms/ctsms/selenium-server-standalone-4.4.0.jar $*
#javac -classpath /home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar /home/runner/work/ctsms/ctsms/core/src/test/java/org/phoenixctms/ctsms/selenium/$1.java
#sudo java -Dwebdriver.chrome.driver=/usr/bin/chromedriver -classpath /home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar:/home/runner/work/ctsms/ctsms/core/src/test/java/ org.phoenixctms.ctsms.selenium.$1
