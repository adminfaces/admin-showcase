FROM rmpestano/wildfly:10.1.0
MAINTAINER Rafael Pestano

COPY ./docker/standalone.conf ${WILDFLY_HOME}/bin/

COPY ./target/showcase.war ${DEPLOYMENT_DIR}

ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh -b=0.0.0.0

EXPOSE 8080
