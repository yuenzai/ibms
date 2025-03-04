#!/bin/bash

IBMS_HOME=$(pwd)

sudo tee /etc/systemd/system/ibms.service <<EOF
[Unit]
Description=Intelligent Building Management System
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=${IBMS_HOME}
ExecStart=docker compose up -d
ExecStop=docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable ibms.service && \
sudo systemctl restart ibms.service

if [ ! $? -eq 0 ]; then
  echo "ibms install failed"
  sudo systemctl status ibms.service
else
  echo "ibms install successfully"
fi
