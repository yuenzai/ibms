#!/bin/bash

sudo bash ~/clash-for-linux-master/start.sh && \
source /etc/profile.d/clash.sh && \
proxy_on

if [ ! $? -eq 0 ]; then
  echo "开启代理失败"
fi
