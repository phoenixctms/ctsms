#!/bin/bash
CTSMS_PROPERTIES=${CTSMS_PROPERTIES:-/ctsms/properties}
CTSMS_JAVA=${CTSMS_JAVA:-/ctsms/java}
CORE_JAR=$(ls /ctsms/build/ctsms/web/target/ctsms-*/WEB-INF/lib/ctsms-core-*.jar)
LIB_JARS=$(ls /ctsms/build/ctsms/web/target/ctsms-*/WEB-INF/lib/*.jar | tr '\n' ':')
java -DCTSMS_PROPERTIES="$CTSMS_PROPERTIES" -DCTSMS_JAVA="$CTSMS_JAVA" -Dfile.encoding=Cp1252 -Djava.awt.headless=true --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED -Xms2048m -Xmx4096m -Xss256k -XX:+UseParallelGC -XX:MaxGCPauseMillis=1500 -XX:GCTimeRatio=9 -XX:ReservedCodeCacheSize=256m -classpath $CORE_JAR:$LIB_JARS org.phoenixctms.ctsms.executable.DBTool $*