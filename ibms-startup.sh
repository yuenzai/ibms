#!/bin/bash

IBMS_HOME=$(pwd)

sudo tee /etc/systemd/system/ibms.service <<EOF
[Unit]
Description=ibms
After=syslog.target network.target

[Service]
User=metadata
Group=metadata

Type=exec
ExecStart=java -jar ${IBMS_HOME}/ibms-starter/target/ibms-starter-0.0.1-SNAPSHOT.jar
WorkingDirectory=${IBMS_HOME}
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF
