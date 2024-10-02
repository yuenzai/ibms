FROM eclipse-temurin:8-jre-jammy

RUN mkdir -p /opt/app/modules

WORKDIR /opt/app

ENV LOADER_PATH=modules

COPY bootstrap/target/ibms-bootstrap-0.0.1-SNAPSHOT.jar .

COPY target/*.jar ./modules/

CMD ["java", "-jar", "ibms-bootstrap-0.0.1-SNAPSHOT.jar"]
