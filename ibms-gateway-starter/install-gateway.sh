#!/bin/bash

GATEWAY_HOME=$(pwd)

sudo tee /etc/systemd/system/ibms-gateway.service <<EOF
[Unit]
Description=IBMS Gateway
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
Environment="BACNET_IFACE=$BACNET_IFACE"
Environment="GATEWAY_HOST=$GATEWAY_HOST"
Environment="GATEWAY_CODE=$GATEWAY_CODE"
WorkingDirectory=${GATEWAY_HOME}
ExecStart=docker compose up -d
ExecStop=docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable ibms-gateway.service && \
sudo systemctl restart ibms-gateway.service && \
sudo systemctl status ibms-gateway.service
