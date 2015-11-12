#!/bin/sh

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -c standalone-apiman.xml &
/opt/jboss/jboss-fuse/bin/fuse &
/opt/apiman/setup.sh &
/opt/jboss/jboss-fuse/bin/client -r 15 -d 10 < /opt/fuse-setup
