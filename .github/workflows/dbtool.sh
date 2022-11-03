#!/bin/bash
#VERSION=$(grep -oP '<application.version>\K[^<]+' /home/runner/work/ctsms/ctsms/pom.xml)
#CTSMS_PROPERTIES=/ctsms/properties
#CTSMS_JAVA=/ctsms/java
$JAVA_HOME/bin/java -DCTSMS_PROPERTIES="${{ env.CTSMS_PROPERTIES }}" -DCTSMS_JAVA="${{ env.CTSMS_JAVA }}" -Dfile.encoding=Cp1252 -Djava.awt.headless=true -classpath /home/runner/work/ctsms/ctsms/web/target/ctsms-$VERSION/WEB-INF/lib/ctsms-core-$VERSION.jar:/home/runner/work/ctsms/ctsms/web/target/ctsms-${{ env.VERSION }}/WEB-INF/lib/* org.phoenixctms.ctsms.executable.DBTool $*