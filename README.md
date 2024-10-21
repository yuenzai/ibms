# IBMS

本文档介绍如何构建和运行 IBMS，使用 Maven 和 Docker 进行构建。

## 开始之前

要进行构建，您需要安装 Git 和 JDK 8，确保您的 `JAVA_HOME` 环境变量指向解压后的 jdk 目录。

## 下载源码

```shell
git clone --branch dev https://github.com/yuenzai/ibms.git
cd ibms
```

项目包含几个模块：

* **ibms** - 包含本项目核心功能，基于 [SpringBoot] 开发
* **bacnet** - 负责 bacnet 协议栈实现并提供 http 接口调用，依赖 [bacnet-stack] 项目实现设备通信
* **database** - 一个关系型数据库，本项目使用 MySQL
* **nginx** - http 反向代理

## 构建

要编译和构建所有的 jar 文件：

```shell
./mvnw clean package
```

项目中包含了一个 [settings.xml](.mvn/wrapper/settings.xml) 文件，可以从阿里云镜像仓库下载依赖项，提高国内的下载速度：

```shell
./mvnw --settings .mvn/wrapper/settings.xml clean package
```

第一次运行构建时，可能需要一段时间来下载 Maven 和所有构建依赖项。 一旦您启动了 Maven 并下载了依赖项，这些就会缓存在您的 `$HOME/.m2` 目录中。

由于 [bacnet-stack] 是 c 语言实现的，为了避免系统标准库版本不同所带来的影响，[bacnet] 目录包含有 Dockerfile 文件，要构建 docker 镜像，请运行：

```shell
docker build -t ecosync/bacnet:0.0.1 ./bacnet
```

由于国内网络环境不畅，构建可能会容易失败，请自行解决=。=

支持通过 docker compose 进行构建：

```shell
docker compose build
```

## 部署

```shell
docker compose up -d
```

当通过`docker compose`部署时，会将数据挂载到宿主机目录以防止数据丢失，环境变量`DATA_DIR`指定挂载的目录路径，默认值为`/tmp`，可通过[.env](.env)文件修改。

## API

### 设计规范

API接口没有按照 RESTful 风格来设计，而是按照 [CQRS] 将接口划分为`Command`和`Query`两种类型。

- **Command** - 表示写操作，使用`POST`方法，命令的执行结果通过`Status Code`表示：
    - **2xx** - 表示命令执行成功，`ResponseBody`不会返回任何内容
    - **4xx** - 表示客户端错误，`ResponseBody`返回错误信息
    - **5xx** - 表示服务端错误，`ResponseBody`返回错误信息
- **Query** - 表示读操作，使用`GET`方法，通过`url`定位资源，并且有一些[通用的查询参数](#查询参数)，原则上读操作不会失败（待定），`ResponseBody`不会对查询结果封装，直接返回原始数据。

### 查询参数

> readonly<br><br>
> type:`boolean` default:`false`<br><br>
> 是否查询[读模型](#读模型)的数据，默认值为`false`
> - `true` - 表示查询`读模型`的数据
> - `false` - 表示查询`写模型`的数据

> page<br><br>
> type:`integer`<br><br>
> 页码，从1开始，和`pagesize`同时传递

> pagesize<br><br>
> type:`integer`<br><br>
> 分页大小，和`page`同时传递

### 分页数据结构

是否传递`page`和`pagesize`参数会影响接口返回的数据结构，调用前请注意。

```json
{
  "content": [
    {
      ...
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

### 接口文档

- [BACnet](bacnet/README.md)
- [设备](device/README.md)
- [定时任务](scheduling/README.md)

## Glossary

> ### 读模型
> 当`tps`和`qps`差距过大，为了保证系统能稳定运行，会将数据分别存放在写库和读库，写库和读库之间保证`最终一致性`。
>
> 对于查询写库来说，查询读库对系统压力更小，并且读库的数据结构会比写库更丰富，但缺点是读库的数据不一定能立刻和写库同步。
>
> 比如当用户新增了一条数据，这时候前端页面通常会查询分页接口并返回列表页，如果这个分页接口数据是来自读库，那么有可能会查询不到刚刚用户新增的那条数据。
>
> 建议前端在处理类似用例时，从写库查询数据。

[bacnet]: bacnet

[SpringBoot]: https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/

[bacnet-stack]: https://github.com/yuenzai/bacnet-stack/tree/1.3.8

[CQRS]: https://learn.microsoft.com/zh-cn/azure/architecture/patterns/cqrs