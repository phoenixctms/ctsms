#!/bin/bash
set -e

mvn -f core/pom.xml org.andromda.maven.plugins:andromdapp-maven-plugin:schema -Dtasks=create --no-transfer-progress -c
sudo useradd ctsms -p '*' --groups sudo
#sudo apt-get install --yes postgresql
# The runner image ships PostgreSQL from apt.postgresql.org (PGDG), but without that repository
# configured, so apt would pick Ubuntu's postgresql-plperl, which depends on Ubuntu's own server build.
# Add the PGDG repository and install the plperl package matching the installed major version.
PGVER=$(ls /usr/lib/postgresql | sort -V | tail -1)
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh -y
sudo apt-get -q -y install "postgresql-plperl-$PGVER"
sudo sed -r -i "s|#*join_collapse_limit.*|join_collapse_limit = 1|" "/etc/postgresql/$PGVER/main/postgresql.conf"
sudo service postgresql start
sudo -u postgres psql -v ON_ERROR_STOP=1 postgres -c "CREATE USER ctsms WITH PASSWORD 'ctsms';"
sudo -u postgres psql -v ON_ERROR_STOP=1 postgres -c "CREATE DATABASE ctsms;"
sudo -u postgres psql -v ON_ERROR_STOP=1 postgres -c "GRANT ALL PRIVILEGES ON DATABASE ctsms to ctsms;"
sudo -u postgres psql -v ON_ERROR_STOP=1 postgres -c "ALTER DATABASE ctsms OWNER TO ctsms;"
sudo -u postgres psql -v ON_ERROR_STOP=1 ctsms < /home/runner/work/ctsms/ctsms/core/db/dbtool.sql
sudo -u ctsms psql -v ON_ERROR_STOP=1 -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/schema-create.sql
sudo -u ctsms psql -v ON_ERROR_STOP=1 -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/index-create.sql
sudo -u ctsms psql -v ON_ERROR_STOP=1 -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/schema-set-version.sql
sudo service postgresql restart
