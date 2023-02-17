#!/bin/bash

apt-get -q -y -o=Dpkg::Use-Pty=0 install \
libarchive-zip-perl \
libconfig-any-perl \
libdata-dump-perl \
libdata-dumper-concise-perl \
libdata-uuid-perl \
libdata-validate-ip-perl \
libdate-calc-perl \
libdate-manip-perl \
libdatetime-format-iso8601-perl \
libdatetime-format-strptime-perl \
libdatetime-perl \
libdatetime-timezone-perl \
libdbd-csv-perl \
libdbd-mysql-perl \
libdbd-sqlite3-perl \
tdsodbc \
libdbd-odbc-perl \
libdigest-md5-perl \
libemail-mime-attachment-stripper-perl \
libemail-mime-perl \
libgearman-client-perl \
libhtml-parser-perl \
libintl-perl \
libio-compress-perl \
libio-socket-ssl-perl \
libjson-xs-perl \
liblog-log4perl-perl \
libmail-imapclient-perl \
libmarpa-r2-perl \
libmime-base64-perl \
libmime-lite-perl \
libmime-tools-perl \
libnet-address-ip-local-perl \
libnet-smtp-ssl-perl \
libole-storage-lite-perl \
libphp-serialization-perl \
libexcel-writer-xlsx-perl \
libspreadsheet-parseexcel-perl \
libstring-mkpasswd-perl \
libtext-csv-xs-perl \
libtie-ixhash-perl \
libtime-warp-perl \
liburi-find-perl \
libuuid-perl \
libwww-perl \
libxml-dumper-perl \
libxml-libxml-perl \
libyaml-libyaml-perl \
libyaml-tiny-perl \
libtemplate-perl \
libdancer-perl \
libdbd-pg-perl \
libredis-perl \
libjson-perl \
libplack-perl \
libcache-memcached-perl \
libdancer-session-memcached-perl \
libgraphviz-perl \
gnuplot \
imagemagick \
ghostscript \
build-essential \
libtest-utf8-perl \
libmoosex-hasdefaults-perl \
cpanminus
sed -r -i 's/^\s*(<policy domain="coder" rights="none" pattern="PS" \/>)\s*$/<!--\1-->/' /etc/ImageMagick-6/policy.xml
if [ "$(lsb_release -d | grep -Ei 'debian')" ]; then
  apt-get -q -y -o=Dpkg::Use-Pty=0 install libsys-cpuaffinity-perl
else
  cpanm Sys::CpuAffinity
  cpanm threads::shared
fi
cpanm --notest Dancer::Plugin::I18N
cpanm --notest DateTime::Format::Excel
cpanm --notest Spreadsheet::Reader::Format
cpanm --notest Spreadsheet::Reader::ExcelXML
wget --no-verbose --no-check-certificate --content-disposition https://github.com/phoenixctms/bulk-processor/archive/master.tar.gz -O /ctsms/bulk-processor.tar.gz
tar -zxvf /ctsms/bulk-processor.tar.gz -C /ctsms/bulk_processor --strip-components 1
perl /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/WebApps/minify.pl --folder=/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/WebApps/Signup
mkdir /ctsms/bulk_processor/output
chown ctsms:ctsms /ctsms/bulk_processor -R
chmod 755 /ctsms/bulk_processor -R
chmod 777 /ctsms/bulk_processor/output -R
rm /ctsms/bulk-processor.tar.gz -f
wget --no-verbose https://raw.githubusercontent.com/phoenixctms/install-debian/master/ecrfdataexport.sh -O /ctsms/ecrfdataexport.sh
chown ctsms:ctsms /ctsms/ecrfdataexport.sh
chmod 755 /ctsms/ecrfdataexport.sh
wget --no-verbose https://raw.githubusercontent.com/phoenixctms/install-debian/master/ecrfdataimport.sh -O /ctsms/ecrfdataimport.sh
chown ctsms:ctsms /ctsms/ecrfdataimport.sh
chmod 755 /ctsms/ecrfdataimport.sh
wget --no-verbose https://raw.githubusercontent.com/phoenixctms/install-debian/master/inquirydataexport.sh -O /ctsms/inquirydataexport.sh
chown ctsms:ctsms /ctsms/inquirydataexport.sh
chmod 755 /ctsms/inquirydataexport.sh

echo "publish_public_file: 1" >>/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/ETL/EcrfExporter/settings.yml