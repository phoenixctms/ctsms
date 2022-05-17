#!/bin/bash
CTSMS_PROPERTIES=${CTSMS_PROPERTIES:-/ctsms/properties}
CTSMS_JAVA=${CTSMS_JAVA:-/ctsms/java}
java -DCTSMS_PROPERTIES="$CTSMS_PROPERTIES" -DCTSMS_JAVA="$CTSMS_JAVA" -Dfile.encoding=Cp1252 -Djava.awt.headless=true -Xms2048m -Xmx4096m -Xss256k -XX:+UseParallelGC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:+CMSClassUnloadingEnabled -XX:ReservedCodeCacheSize=256m -classpath /ctsms/build/ctsms/web/target/ctsms-1.8.1/WEB-INF/lib/ctsms-core-1.8.1.jar:/ctsms/build/ctsms/web/target/ctsms-1.8.1/WEB-INF/lib/* org.phoenixctms.ctsms.executable.DBTool $*