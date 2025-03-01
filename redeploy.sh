#!/bin/bash
./mvnw --settings .mvn/wrapper/settings.xml clean package \
&& docker compose -f ibms-gateway-starter/docker-compose.yml down gateway \
&& docker compose -f ibms-gateway-starter/docker-compose.yml up -d gateway \
&& docker compose -f ibms-gateway-starter/docker-compose.yml logs -f --tail 1