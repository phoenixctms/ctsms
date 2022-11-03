#!/bin/bash

#Initialize db
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -i -f
#Setup query criteria
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -icp /ctsms/master_data/criterion_property_definitions.csv
#Setup service method permissions
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -ipd /ctsms/master_data/permission_definitions.csv        
#Setup degrees and titles
#sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -it /ctsms/master_data/titles.csv -e ISO-8859-1
#Setup mime types
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -ims /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imc /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imt /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imp /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -immm /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imifi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imsi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -impi /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imjf /ctsms/master_data/mime.types -e ISO-8859-1
sudo -E -u ctsms /home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh -imcc /ctsms/master_data/mime.types -e ISO-8859-1 