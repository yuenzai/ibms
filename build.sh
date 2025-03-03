#!/bin/bash
./mvnw --settings .mvn/wrapper/settings.xml clean package && \
tar -zcvf ~/ibms-gateway.tar.gz -C ibms-gateway-starter/target/ ibms-gateway-starter-0.0.1-SNAPSHOT.jar
