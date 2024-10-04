FROM eclipse-temurin:8-jre-jammy

COPY target/ibms-bacnet-0.0.1-SNAPSHOT.jar .

RUN mkdir -p /opt/app /opt/lib && \
    apt-get update && \
    apt-get install -y cmake git && \
    mv ibms-bacnet-0.0.1-SNAPSHOT.jar /opt/app/

WORKDIR /opt/lib

RUN git clone --branch v1.7.18 https://github.com/DaveGamble/cJSON.git && \
    mkdir -p cJSON/build && \
    cmake -S cJSON -B cJSON/build && \
    make -C cJSON/build install && \
    rm -rf cJSON

RUN git clone --branch bacnet-stack-1.3 --single-branch https://github.com/yuenzai/bacnet-stack.git && \
    mkdir -p bacnet-stack/build && \
    cmake -S bacnet-stack -B bacnet-stack/build && \
    make -C bacnet-stack/build clean all && \
    cp bacnet-stack/build/whois bacnet-stack/build/readpropm bacnet-stack/build/writeprop bacnet-stack/build/writepropm . && \
    rm -rf bacnet-stack && \
    mv ./* /usr/local/bin

WORKDIR /opt/app

CMD ["java", "-jar", "ibms-bacnet-0.0.1-SNAPSHOT.jar"]
