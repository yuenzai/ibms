#!/bin/bash

SERVICE_NAME=ibms-gateway
WORKDIR=$(dirname $(readlink -f $0))

sudo tee /etc/systemd/system/${SERVICE_NAME}.service <<EOF
[Unit]
Description=${SERVICE_NAME}
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=${WORKDIR}
ExecStart=docker compose up -d postgresql prometheus grafana gateway nginx
ExecStop=docker compose down postgresql prometheus grafana gateway nginx
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable ${SERVICE_NAME}.service

if [ ! $? -eq 0 ]; then
  echo "${SERVICE_NAME} install failed"
  sudo systemctl status ${SERVICE_NAME}.service
else
  echo "${SERVICE_NAME} installed"
fi
