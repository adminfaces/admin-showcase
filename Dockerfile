FROM jboss/wildfly
COPY ./target/showcase.war ${JBOSS_HOME}/standalone/deployments/
