#!/bin/bash
./mvnw --settings .mvn/wrapper/settings.xml clean package \
&& docker compose down gateway \
&& docker compose up -d gateway \
&& docker compose logs -f --tail 1