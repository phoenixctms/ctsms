#!/bin/bash
#sudo $JAVA_HOME/bin/java -Dfile.encoding=Cp1252 -Dwebdriver.chrome.driver=/usr/bin/chromedriver -classpath /home/runner/work/ctsms/ctsms/core/target/test-classes/:/home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar $*



javac -classpath /home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar /home/runner/work/ctsms/ctsms/core/src/test/java/org/phoenixctms/ctsms/selenium/testChrome.java
java -classpath /home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar /home/runner/work/ctsms/ctsms/core/src/test/java/org/phoenixctms/ctsms/selenium/testChrome.class

#sudo $JAVA_HOME/bin/javac -classpath /home/runner/work/ctsms/ctsms/core/target/test-classes/:/home/runner/work/ctsms/ctsms/selenium-server-standalone-3.141.59.jar /home/runner/work/ctsms/ctsms/core/target/test-classes/ 