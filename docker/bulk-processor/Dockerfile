FROM perl:5-threaded

RUN apt-get update && apt-get install -y \
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
    cpanminus \
    libsys-cpuaffinity-perl \
 && rm -rf /var/lib/apt/lists/*

RUN cpanm --notest Dancer::Plugin::I18N
RUN cpanm --notest DateTime::Format::Excel
RUN cpanm --notest Spreadsheet::Reader::Format
RUN cpanm --notest Spreadsheet::Reader::ExcelXML
RUN cpanm --notest Tie::IxHash

# FIXME: should this be shared with the java environment in the Phoenix container?
RUN mkdir -p /ctsms/bulk_processor

RUN wget --content-disposition https://github.com/phoenixctms/bulk-processor/archive/master.tar.gz -O /ctsms/bulk-processor.tar.gz \
    && tar -zxvf /ctsms/bulk-processor.tar.gz -C /ctsms/bulk_processor --strip-components 1 \
    && perl /ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/WebApps/minify.pl --folder=/ctsms/bulk_processor/CTSMS/BulkProcessor/Projects/WebApps/Signup \
    && mkdir /ctsms/bulk_processor/output \
    && chmod 755 /ctsms/bulk_processor -R \
    && chmod 777 /ctsms/bulk_processor/output -R \
    && rm /ctsms/bulk-processor.tar.gz

RUN wget https://raw.githubusercontent.com/phoenixctms/install-debian/master/ecrfdataexport.sh -O /ctsms/ecrfdataexport.sh
RUN chmod 755 /ctsms/ecrfdataexport.sh
RUN wget https://raw.githubusercontent.com/phoenixctms/install-debian/master/inquirydataexport.sh -O /ctsms/inquirydataexport.sh
RUN chmod 755 /ctsms/inquirydataexport.sh