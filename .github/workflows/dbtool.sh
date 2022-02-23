#!/bin/bash
CTSMS_PROPERTIES=/ctsms/properties
$JAVA_HOME/bin/java -DCTSMS_PROPERTIES="$CTSMS_PROPERTIES" -DCTSMS_JAVA="$CTSMS_JAVA" -Dfile.encoding=Cp1252 -Djava.awt.headless=true -classpath /home/runner/work/ctsms/ctsms/web/target/ctsms-1.7.0/WEB-INF/lib/ctsms-core-1.7.0.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-1.8.0/WEB-INF/lib/* org.phoenixctms.ctsms.executable.DBTool $*