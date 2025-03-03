#!/bin/bash

bash ~/clash-for-linux-master/start.sh > /dev/null 2>&1 && \
source /etc/profile.d/clash.sh && \
proxy_on

if [ ! $? -eq 0 ]; then
  echo "开启代理失败"
fi
