#!/bin/bash
./mvnw --settings .mvn/wrapper/settings.xml clean package

if [ ! $? -eq 0 ]; then
  exit 1
fi

docker compose down gateway && \
docker compose up -d gateway && \
docker compose logs -f --tail 1 gateway
