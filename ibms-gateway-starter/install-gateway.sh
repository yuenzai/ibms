#!/bin/bash

GATEWAY_HOME=$(pwd)

netstat -tln | grep -E '9090' >/dev/null 2>&1

if [ $? -eq 0 ]; then
  echo "端口 9090 已被占用"
  exit 1
fi

sudo tee /etc/systemd/system/ibms-gateway.service <<EOF
[Unit]
Description=Intelligent Building Management System Gateway
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=${GATEWAY_HOME}
ExecStart=docker compose up -d
ExecStop=docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable ibms-gateway.service && \
sudo systemctl restart ibms-gateway.service

if [ ! $? -eq 0 ]; then
  echo "ibms-gateway install failed"
  sudo systemctl status ibms-gateway.service
else
  echo "ibms-gateway install successfully"
fi
