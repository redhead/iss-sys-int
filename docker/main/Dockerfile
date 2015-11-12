FROM iss/int-base

RUN echo 'admin=admin,admin,manager,viewer,Monitor, Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser' > /opt/jboss/jboss-fuse/etc/users.properties

ADD activemq.xml /opt/jboss/jboss-fuse/etc/
ADD keystore.jks /opt/jboss/jboss-fuse/bin/

ADD sys-int.jar /opt/sys-int.jar
ADD feature.xml /opt/sys-int-feature.xml
ADD fuse-setup /opt/fuse-setup

ADD apiman/gateway_ks.jks /opt/jboss/wildfly/gateway_ks.jks
ADD apiman/gateway_ts.jks /opt/jboss/wildfly/gateway_ts.jks
ADD apiman/apiman.properties /opt/jboss/apiman.properties
ADD apiman/standalone-apiman.xml /opt/jboss/wildfly/standalone/configuration/standalone-apiman.xml

RUN cat /opt/jboss/apiman.properties >> /opt/jboss/wildfly/standalone/configuration/apiman.properties

ADD apiman/setup /opt/apiman/

ADD run.sh /opt/run.sh
CMD sh -c '/opt/run.sh';'bash'
