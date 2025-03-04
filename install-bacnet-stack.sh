#!/bin/bash

if [ ! -f /var/lib/apt/periodic/update-success-stamp ] || [ $(stat -c %Y /var/lib/apt/periodic/update-success-stamp) -lt $(date -d '1 day ago' +%s) ]; then
  sudo apt-get update || exit 1;
else
  echo "apt already updated"
fi

if ! command -v cmake &> /dev/null || ! command -v git &> /dev/null; then
  sudo apt-get install -y cmake git || exit 1;
else
  echo "cmake and git already installed"
fi

cd /tmp

if [ ! -d cJSON ]; then
  git clone --branch v1.7.18 https://github.com/DaveGamble/cJSON.git || exit 1;
else
  git -C cJSON pull
fi

if [ ! -d bacnet-stack ]; then
  git clone --branch 1.3.8 --single-branch https://github.com/yuenzai/bacnet-stack.git || exit 1;
else
  git -C bacnet-stack pull
fi

mkdir -p cJSON/build && \
cmake -S cJSON -B cJSON/build && \
sudo make -C cJSON/build install

mkdir -p bacnet-stack/build && \
cmake -S bacnet-stack -B bacnet-stack/build && \
sudo make -C bacnet-stack/build clean all

sudo cp bacnet-stack/build/{whois,readpropm,writeprop} /usr/local/bin

echo "bacnet-stack installed"
