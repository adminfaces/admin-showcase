#!/bin/sh
mvn clean package && cd docker && ./build.sh && cd ../ && ./docker/run.sh