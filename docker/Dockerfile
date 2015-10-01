FROM jboss/base-jdk:8

# Set the FUSE_VERSION env variable
ENV FUSE_VERSION 6.2.0.redhat-099

# If the container is launched with re-mapped ports, these ENV vars should
# be set to the remapped values.
ENV FUSE_PUBLIC_OPENWIRE_PORT 61616
#ENV FUSE_PUBLIC_MQTT_PORT 1883
ENV FUSE_PUBLIC_AMQP_PORT 5672
#ENV FUSE_PUBLIC_STOMP_PORT 61613
ENV FUSE_PUBLIC_OPENWIRE_SSL_PORT 61617
#ENV FUSE_PUBLIC_MQTT_SSL_PORT 8883
ENV FUSE_PUBLIC_AMQP_SSL_PORT 5671
#ENV FUSE_PUBLIC_STOMP_SSL_PORT 61614

# Install fuse in the image.
COPY install.sh /opt/jboss/install.sh
#COPY jboss-fuse.zip /opt/jboss/jboss-fuse.zip
RUN /opt/jboss/install.sh


EXPOSE 8181 8101 1099 44444 61616 1883 5672 61613 61617 8883 5671 61614

#
# The following directories can hold config/data, so lets suggest the user
# mount them as volumes.
#VOLUME /opt/jboss/jboss-fuse/bin
#VOLUME /opt/jboss/jboss-fuse/etc
#VOLUME /opt/jboss/jboss-fuse/data
#VOLUME /opt/jboss/jboss-fuse/deploy

# lets default to the jboss-fuse dir so folks can more easily navigate to around the server install
WORKDIR /opt/jboss/jboss-fuse

################
#### WILDFLY ###
################

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 8.2.1.Final
ENV WILDFLY_SHA1 77161d682005f26acb9d2df5548c8623ba3a4905
ENV JBOSS_HOME /opt/jboss/wildfly

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz

# Expose the ports we're interested in
EXPOSE 8080


################
#### APIMAN ###
################


ENV APIMAN_VERSION 1.1.7.Final

RUN cd $JBOSS_HOME \
 && curl http://downloads.jboss.org/overlord/apiman/$APIMAN_VERSION/apiman-distro-wildfly8-$APIMAN_VERSION-overlay.zip -o apiman-distro-wildfly8-$APIMAN_VERSION-overlay.zip \
 && bsdtar -xf apiman-distro-wildfly8-$APIMAN_VERSION-overlay.zip \
 && rm apiman-distro-wildfly8-$APIMAN_VERSION-overlay.zip


########################
#### CUSTOM SETTINGS ###
########################

RUN echo 'admin=admin,admin,manager,viewer,Monitor, Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser' > /opt/jboss/jboss-fuse/etc/users.properties

ADD activemq.xml /opt/jboss/jboss-fuse/etc/
ADD keystore.jks /opt/jboss/jboss-fuse/bin/

ADD run.sh /opt/run.sh
CMD sh -c '/opt/run.sh';'bash'

ADD feature.xml /opt/jboss/jboss-fuse/deploy/sys-int-feature.xml

ADD sys-int.jar /opt/sys-int.jar
