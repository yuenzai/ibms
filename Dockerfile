FROM eclipse-temurin:8-jre-jammy

RUN mkdir -p /opt/app/modules

WORKDIR /opt/app

ENV LOADER_PATH=modules