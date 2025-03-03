## 网关

构建成功后再部署

### 环境变量

```shell
# BACnet网络接口，默认值：eth0
BACNET_IFACE=ens18
# 网关地址，prometheus-agent会从该地址采集数据，默认值：localhost:8080
GATEWAY_HOST=localhost:8080
# 网关编码，所有从 prometheus-agent 采集到的指标都会附加一个 gateway_code 标签，建议为每个网关都设置一个网关编码，方便区分指标来源
GATEWAY_CODE=
```

修改 prometheus.yml 文件的配置，将 remote_write.url 设为 IBMS 的地址，后缀 api/v1/write 是固定的，不需要修改。

```shell
bash install-gateway.sh
```
