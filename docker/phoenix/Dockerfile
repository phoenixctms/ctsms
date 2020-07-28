FROM debian:stretch AS javabuild

RUN apt-get update && apt-get install -y \
	openjdk-8-jdk \
	git \
	maven \
 && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /ctsms/build
RUN cd /ctsms/build && \
	git clone https://github.com/phoenixctms/ctsms
RUN cd /ctsms/build/ctsms && \
	mvn install -DskipTests && \
	mvn -f core/pom.xml org.andromda.maven.plugins:andromdapp-maven-plugin:schema -Dtasks=create && \
	mvn -f core/pom.xml org.andromda.maven.plugins:andromdapp-maven-plugin:schema -Dtasks=drop

RUN mkdir /ctsms/war/
RUN cp /ctsms/build/ctsms/web/target/*.war /ctsms/war/
RUN rm -rf /ctsms/build/

FROM tomcat:8 as runtime

RUN apt-get update && apt-get install -y pwgen \
 && rm -rf /var/lib/apt/lists/*

COPY --from=javabuild /ctsms /ctsms
COPY --from=javabuild /ctsms/war/ctsms-1.6.9.war /usr/local/tomcat/webapps/ROOT.war

# prepare /ctsms directory with default-config and master-data
RUN wget -q https://raw.githubusercontent.com/phoenixctms/install-debian/master/dbtool.sh -O /ctsms/dbtool.sh
RUN wget -q https://raw.githubusercontent.com/phoenixctms/install-debian/master/clearcache.sh -O /ctsms/clearcache.sh
RUN wget -q --content-disposition https://github.com/phoenixctms/config-default/archive/master.tar.gz -O /ctsms/config.tar.gz \
    && tar xzf /ctsms/config.tar.gz -C /ctsms --strip-components 1 \
    && rm /ctsms/config.tar.gz
RUN wget -q https://api.github.com/repos/phoenixctms/master-data/tarball/master -O /ctsms/master-data.tar.gz \
    && mkdir /ctsms/master_data \
    && tar xzf /ctsms/master-data.tar.gz -C /ctsms/master_data --strip-components 1 \
    && rm /ctsms/master-data.tar.gz
RUN chmod a+x /ctsms/dbtool.sh /ctsms/clearcache.sh

RUN wget -q https://raw.githubusercontent.com/phoenixctms/install-debian/master/tomcat/workers.properties -O "/usr/local/tomcat/conf/workers.properties"
RUN chmod 640 "/usr/local/tomcat/conf/workers.properties"

ENV CTSMS_PROPERTIES /ctsms/properties
ENV CTSMS_JAVA /ctsms/java
ENV CATALINA_BASE /usr/local/tomcat

COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT [ "/docker-entrypoint.sh" ]
