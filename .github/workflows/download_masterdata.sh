#!/bin/bash

#mkdir /ctsms
wget --no-verbose --no-check-certificate --content-disposition https://github.com/phoenixctms/config-default/archive/master.tar.gz -O /home/runner/work/ctsms/config.tar.gz
tar -zxvf /home/runner/work/ctsms/config.tar.gz -C /home/runner/work/ctsms --strip-components 1
rm /home/runner/work/ctsms/config.tar.gz -f          
wget --no-verbose https://api.github.com/repos/phoenixctms/master-data/tarball/master -O /home/runner/work/ctsms/master-data.tar.gz
mkdir /home/runner/work/ctsms/master_data
tar -zxvf /home/runner/work/ctsms/master-data.tar.gz -C /home/runner/work/ctsms/master_data --strip-components 1
rm /home/runner/work/ctsms/master-data.tar.gz -f

echo "\nexternal_file_data_dir=/home/runner/work/ctsms/external_files" >> /home/runner/work/ctsms/properties/ctsms-settings.properties

echo "\ndb_tool=/home/runner/work/ctsms/ctsms/.github/workflows/dbtool.sh" >> /home/runner/work/ctsms/properties/ctsms-settings.properties
echo "\necrf_exporter_process_pl=perl /home/runner/work/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfExporter/process.pl --config=config.job.cfg" >> /home/runner/work/ctsms/properties/ctsms-settings.properties
echo "\necrf_importer_process_pl=perl /home/runner/work/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfImporter/process.pl --config=config.job.cfg" >> /home/runner/work/ctsms/properties/ctsms-settings.properties
echo "\ninquiry_exporter_process_pl=perl /home/runner/work/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/InquiryExporter/process.pl --config=config.job.cfg" >> /home/runner/work/ctsms/properties/ctsms-settings.properties

sed -r -i "s/dbtool_lock_file_name=[^=]+/dbtool_lock_file_name=\/home\/runner\/work\/ctsms\/{0}.lock/" /home/runner/work/ctsms/properties/ctsms-dbtool.properties

chown ctsms:ctsms /home/runner/work/ctsms -R
chmod 777 /home/runner/work/ctsms -R

cat /home/runner/work/ctsms/properties/ctsms-settings.properties