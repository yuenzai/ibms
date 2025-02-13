FROM eclipse-temurin:17-jre-jammy

RUN mkdir -p /opt/app/data /opt/lib && \
    apt-get update && \
    apt-get install -y cmake git \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt/lib

RUN git clone --branch v1.7.18 https://github.com/DaveGamble/cJSON.git && \
    mkdir -p cJSON/build && \
    cmake -S cJSON -B cJSON/build && \
    make -C cJSON/build install && \
    rm -rf cJSON

RUN git clone --branch 1.3.8 --single-branch https://github.com/yuenzai/bacnet-stack.git && \
    mkdir -p bacnet-stack/build && \
    cmake -S bacnet-stack -B bacnet-stack/build && \
    make -C bacnet-stack/build clean all && \
    cp bacnet-stack/build/whois bacnet-stack/build/readpropm bacnet-stack/build/writeprop bacnet-stack/build/writepropm . && \
    rm -rf bacnet-stack && \
    mv ./* /usr/local/bin

WORKDIR /opt/app