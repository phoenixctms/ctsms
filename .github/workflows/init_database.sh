#!/bin/bash

#Initialize db
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -i -f
#Setup query criteria
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -icp /home/runner/work/ctsms/master_data/criterion_property_definitions.csv
#Setup service method permissions
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -ipd /home/runner/work/ctsms/master_data/permission_definitions.csv        
#Setup degrees and titles
#sudo -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -it /home/runner/work/ctsms/master_data/titles.csv -e ISO-8859-1
#Setup mime types
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imi /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -ims /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imc /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imt /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imp /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -immm /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imifi /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imsi /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -impi /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imjf /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1
/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imcc /home/runner/work/ctsms/master_data/mime.types -e ISO-8859-1 