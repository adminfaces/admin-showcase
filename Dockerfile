FROM jboss/wildfly:10.1.0.Final

COPY ./docker/standalone.conf ${JBOSS_HOME}/bin/

COPY ./target/showcase.war ${JBOSS_HOME}/standalone/deployments/
