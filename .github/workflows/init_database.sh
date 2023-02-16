#!/bin/bash


CATALINA_BASE=${CATALINA_BASE:-/var/lib/tomcat9}
printenv
ls /var/lib/tomcat9/webapps


#Initialize db
/ctsms/dbtool.sh -i -f
#Setup query criteria
/ctsms/dbtool.sh -icp /ctsms/master_data/criterion_property_definitions.csv
#Setup service method permissions
/ctsms/dbtool.sh -ipd /ctsms/master_data/permission_definitions.csv        
#Setup degrees and titles
#sudo -u ctsms /ctsms/dbtool.sh -it /ctsms/master_data/titles.csv -e ISO-8859-1
#Setup mime types
/ctsms/dbtool.sh -imi /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -ims /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imc /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imt /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imp /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -immm /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imifi /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imsi /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -impi /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imjf /ctsms/master_data/mime.types -e ISO-8859-1
/ctsms/dbtool.sh -imcc /ctsms/master_data/mime.types -e ISO-8859-1 