#!/bin/bash

bash ~/clash-for-linux-master/shutdown.sh > /dev/null 2>&1 && \
proxy_off

if [ ! $? -eq 0 ]; then
  echo "关闭代理失败"
fi
