#!/bin/bash

source proxy_on.sh

./mvnw --settings .mvn/wrapper/settings.xml clean package && \
bash install-bacnet-stack.sh && \
bash install-node_exporter.sh && \
bash install-ibms.sh && \
bash install-gateway.sh

source proxy_off.sh
