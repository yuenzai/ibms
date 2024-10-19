# IBMS

本文档介绍如何构建和运行 IBMS，使用 Maven 和 Docker 进行构建。

## 开始之前

要进行构建，您需要安装 Git 和 JDK 8，确保您的 `JAVA_HOME` 环境变量指向解压后的 jdk 目录。

## 下载源码

    git clone --branch dev https://github.com/yuenzai/ibms.git
    cd ibms

项目包含几个模块：

* **ibms** - 包含本项目核心功能，基于 [SpringBoot] 开发
* **bacnet** - 负责 bacnet 协议栈实现并提供 http 接口调用，依赖 [bacnet-stack] 项目实现设备通信
* **database** - 一个关系型数据库，本项目使用 MySQL（理论上支持所有关系型数据库，详情请看开发文档）
* **nginx** - http 反向代理

## 构建

要编译和构建所有的 jar 文件：

    ./mvnw clean package

项目中包含了一个 [settings.xml](.mvn/wrapper/settings.xml) 文件，可以从阿里云镜像仓库下载依赖项，提高国内的下载速度：

    ./mvnw --settings .mvn/wrapper/settings.xml clean package

第一次运行构建时，可能需要一段时间来下载 Maven 和所有构建依赖项。 一旦您启动了 Maven 并下载了依赖项，这些就会缓存在您的 `$HOME/.m2` 目录中。

由于 [bacnet-stack] 是 c 语言实现的，为了避免系统标准库版本不同所带来的影响，[bacnet] 目录包含有 Dockerfile 文件，要构建 docker 镜像，请运行：

    docker build -t ecosync/bacnet:0.0.1 ./bacnet

由于国内网络环境不畅，构建可能会容易失败，请自行解决=。=

支持通过 docker compose 进行构建：

    docker compose build

## 部署

    docker compose up -d

[bacnet]: bacnet

[SpringBoot]: https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/

[bacnet-stack]: https://github.com/yuenzai/bacnet-stack/tree/1.3.8
