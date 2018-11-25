#!/bin/sh

mvn clean package -Ptests -Pwildfly8-managed
mvn test -Ptests -Pglassfish-managed
mvn test -Ptests -Ptomee7-remote
mvn test -Ptests -Ptomee1.7-remote