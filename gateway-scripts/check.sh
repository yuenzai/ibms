if [ -z $WORKDIR ]; then
  echo "缺少环境变量：WORKDIR"
  echo "export WORKDIR=~/gateway"
  exit 1
fi

if [ ! -d $WORKDIR ]; then
  mkdir $WORKDIR
  if [ ! -d $WORKDIR ]; then
    echo "目录创建失败：$WORKDIR"
    exit 1
  fi
fi

if [ ! -d tmp ]; then
  mkdir tmp
  if [ ! -d tmp ]; then
    echo "目录创建失败：./tmp"
    exit 1
  fi
fi
