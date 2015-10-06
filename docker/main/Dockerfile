FROM iss/int-base

RUN echo 'admin=admin,admin,manager,viewer,Monitor, Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser' > /opt/jboss/jboss-fuse/etc/users.properties

ADD activemq.xml /opt/jboss/jboss-fuse/etc/
ADD keystore.jks /opt/jboss/jboss-fuse/bin/

ADD run.sh /opt/run.sh
CMD sh -c '/opt/run.sh';'bash'

ADD feature.xml /opt/jboss/jboss-fuse/deploy/sys-int-feature.xml

ADD sys-int.jar /opt/sys-int.jar
