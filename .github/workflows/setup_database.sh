#!/bin/bash

mvn -f core/pom.xml org.andromda.maven.plugins:andromdapp-maven-plugin:schema -Dtasks=create
sudo useradd ctsms -p '*' --groups sudo
#sudo apt-get install --yes postgresql
sudo apt-get -q -y install postgresql-plperl
sudo sed -r -i "s|#*join_collapse_limit.*|join_collapse_limit = 1|" /etc/postgresql/14/main/postgresql.conf
sudo service postgresql start
sudo -u postgres psql postgres -c "CREATE USER ctsms WITH PASSWORD 'ctsms';"
sudo -u postgres psql postgres -c "CREATE DATABASE ctsms;"
sudo -u postgres psql postgres -c "GRANT ALL PRIVILEGES ON DATABASE ctsms to ctsms;"
sudo -u postgres psql postgres -c "ALTER DATABASE ctsms OWNER TO ctsms;"
sudo -u postgres psql ctsms < /home/runner/work/ctsms/ctsms/core/db/dbtool.sql
sudo -u ctsms psql -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/schema-create.sql
sudo -u ctsms psql -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/index-create.sql
sudo -u ctsms psql -U ctsms ctsms < /home/runner/work/ctsms/ctsms/core/db/schema-set-version.sql
sudo service postgresql restart