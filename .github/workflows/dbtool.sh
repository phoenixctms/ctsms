#!/bin/bash
#VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
CTSMS_PROPERTIES=/ctsms/properties
CTSMS_JAVA=/ctsms/java

#$JAVA_HOME/bin/java -DCTSMS_PROPERTIES="$CTSMS_PROPERTIES" -DCTSMS_JAVA="$CTSMS_JAVA" -Dfile.encoding=Cp1252 -Djava.awt.headless=true -classpath /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/ctsms-core-$VERSION.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/* org.phoenixctms.ctsms.executable.DBTool $*

$JAVA_HOME/bin/java -DCTSMS_PROPERTIES="$CTSMS_PROPERTIES" -DCTSMS_JAVA="$CTSMS_JAVA" -Dfile.encoding=Cp1252 --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED -Djava.awt.headless=true -classpath /home/runner/work/ctsms/ctsms/web/target/ctsms-1.9.0/WEB-INF/lib/ctsms-core-1.9.0.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-1.9.0/WEB-INF/lib/* org.phoenixctms.ctsms.executable.DBTool $*