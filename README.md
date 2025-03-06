## 开始之前

要进行构建，您需要安装 Git 和 JDK 17，确保您的 `JAVA_HOME` 环境变量指向解压后的 jdk 目录。

项目包含以下模块：

* [ibms-gateway-starter](ibms-gateway-starter) - 网关
* [ibms-server-starter](ibms-server-starter) - 服务端

## 构建

编译和构建所有的 jar 文件：

```shell
./mvnw clean package
```

项目中包含了一个 [settings.xml](.mvn/wrapper/settings.xml) 文件，可以从阿里云镜像仓库下载依赖项：

```shell
./mvnw --settings .mvn/wrapper/settings.xml clean package
```

第一次运行构建时，可能需要一段时间来下载 Maven 和所有构建依赖项。 一旦您启动了 Maven 并下载了依赖项，这些就会缓存在您的 `$HOME/.m2` 目录中。

## 运行

模块目录以及根目录下都有 docker-compose.yml 文件，在根目录运行会将所有模块同时启动，但要记得修改模块目录下的配置文件，也可以到模块目录下单独运行

```bash
# 修改配置文件
vi .env
# 运行
docker compose up -d
# 设置开机自启动
bash install.sh
```
