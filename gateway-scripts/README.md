要指定一个目录作为工作目录，通过 WORKDIR 设置，该变量会在脚本运行时使用

```shell
export WORKDIR=~/gateway
```

脚本可以重复执行，脚本运行时产生的文件会缓存在 tmp 目录中，如果不清理缓存重复执行脚本，相当于重启服务。如果缓存中没有需要的文件，则会重新下载。

1. [install-bacnet-stack.sh](install-bacnet-stack.sh) 安装 bacnet-stack

下载并编译 bacnet-stack，编译后的命令在 /usr/local/bin

2. [install-prometheus.sh](install-prometheus.sh) 安装 prometheus

下载 prometheus 并作为 prometheus.service 服务启动，默认监听 9090 端口，clash 的 DashBoard 也是用的 9090 端口，记得先停掉。

脚本里有个变量 GATEWAY_CODE，用来将网关产生的所有指标附加一个 gateway_code 标签，看情况修改。

3. [install-gateway.sh](install-gateway.sh) 安装采集程序

下载 JRE 和 ibms-gateway-starter 并作为 ibms-gateway.service 服务启动。

脚本里有个变量 BACNET_IFACE，用来指定 bacnet-stack 的网络接口，看情况修改。

4. [install-node_exporter.sh](install-node_exporter.sh) 安装 node_exporter（可选）

下载 node_exporter 并作为 node_exporter.service 服务启动
