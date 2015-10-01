#!/bin/sh

/opt/jboss/jboss-fuse/bin/fuse server &
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -c standalone-apiman.xml