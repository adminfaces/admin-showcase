FROM airhacks/wildfly
COPY ./target/showcase.war ${DEPLOYMENT_DIR}