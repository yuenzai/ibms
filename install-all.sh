#!/bin/bash

source proxy_on.sh

./mvnw --settings .mvn/wrapper/settings.xml clean package && \
bash install-ibms.sh && \
bash install-gateway.sh

source proxy_off.sh
