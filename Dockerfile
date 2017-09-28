FROM jboss/wildfly

COPY ./docker/standalone.conf ${JBOSS_HOME}/bin/

COPY ./target/showcase.war ${JBOSS_HOME}/standalone/deployments/
