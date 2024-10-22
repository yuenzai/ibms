## 定时任务变更

`POST` `/scheduling`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> commandType<br><br>
> `string` `enum` `required`<br><br>
> Enum options:<br>
> * **ADD_SCHEDULING** - [新增定时任务](#新增定时任务命令)，新创建的定时任务默认不会开始执行，需要调用启停定时任务命令来控制
> * **UPDATE_SCHEDULING** - [更新定时任务](#更新定时任务命令)
> * **REMOVE_SCHEDULING** - [删除定时任务](#删除定时任务命令)
> * **SWITCH_SCHEDULING** - [启停定时任务](#启停定时任务命令)
> * **RESET_SCHEDULING** - [重置定时任务状态](#重置定时任务状态命令)，用于将定时任务从错误状态中恢复

#### Response body parameters

> None

#### [Request example](scheduling-command.http)

## 查询定时任务类型

`GET` `/scheduling/tasks`

### Parameters

#### Headers

> None

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> None

#### Response body parameters

> - **ReadDeviceStatus** - 读取设备状态任务
> - **ReadDeviceStatusBatch** - 读取所有设备状态任务

### Example

#### Request example

```shell
curl http://localhost/scheduling/tasks
```

#### Response example

```json
[
  "ReadDeviceStatus",
  "ReadDeviceStatusBatch"
]
```

## 查询定时任务

`GET` `/scheduling`

### Parameters

#### Headers

> None

#### Path parameters

> None

#### Query parameters

> [page](../README.md#查询参数)

> [pagesize](../README.md#查询参数)

> max-count<br><br>
> `integer`<br><br>
> 要获取定时任务未来的触发时间的次数，默认值`5`

#### Request body parameters

> None

#### Response body parameters

> [定时任务属性](#定时任务属性)

### Example

#### Request example

```shell
curl http://localhost/scheduling
curl http://localhost/scheduling?page=1&pagesize=10&max-count=5
```

#### Response example

> None

## 数据结构

### 新增定时任务命令

> schedulingName<br><br>
> `string` `required`<br><br>
> 定时任务名称，全局唯一，不能修改

> schedulingTrigger<br><br>
> `object` `required`<br><br>
> 定时任务触发器<br>
> - [cron触发器](#cron触发器)

> schedulingTaskParams<br><br>
> `object` `required`<br><br>
> 定时任务参数
>
> - 读取设备状态任务参数
> > type<br><br>
> > `string` `literal` `required`<br><br>
> > ReadDeviceStatus
>
> > deviceCode<br><br>
> > `string` `required`<br><br>
> > 设备编码
>
> - 读取所有设备状态任务参数
> > type<br><br>
> > `string` `literal` `required`<br><br>
> > ReadDeviceStatusBatch

> description<br><br>
> `string`<br><br>
> 描述

### 更新定时任务命令

> schedulingName<br><br>
> `string` `required`<br><br>
> 定时任务名称，全局唯一，不能修改

> schedulingTrigger<br><br>
> `object`<br><br>
> 定时任务触发器<br>
> - [cron触发器](#cron触发器)

> schedulingTaskParams<br><br>
> `object`<br><br>
> 定时任务参数
>
> - 读取设备状态任务参数
> > type<br><br>
> > `string` `literal` `required`<br><br>
> > ReadDeviceStatus
>
> > deviceCode<br><br>
> > `string` `required`<br><br>
> > 设备编码
>
> - 读取所有设备状态任务参数
> > type<br><br>
> > `string` `literal` `required`<br><br>
> > ReadDeviceStatusBatch

> description<br><br>
> `string`<br><br>
> 描述

### 删除定时任务命令

> schedulingName<br><br>
> `string` `required`<br><br>
> 定时任务名称，全局唯一，不能修改

### 启停定时任务命令

> schedulingName<br><br>
> `string` `required`<br><br>
> 定时任务名称，全局唯一，不能修改

> enabled<br><br>
> `boolean` `required`<br><br>
> 启动或停止定时任务

### 重置定时任务状态命令

> schedulingName<br><br>
> `string` `required`<br><br>
> 定时任务名称，全局唯一，不能修改

### cron触发器

> type<br><br>
> `string` `enum` `required`<br><br>
> Enum options:<br><br>
> `CRON` - `cron`触发器

> expression<br><br>
> `string` `required`<br><br>
> `quartz`格式的`cron`表达式

### 定时任务属性

> schedulingName<br><br>
> `string`<br><br>
> 定时任务名称

> schedulingTrigger<br><br>
> `object`<br><br>
> 定时任务触发器

> createdDate<br><br>
> `integer`<br><br>
> 创建时间

> lastModifiedDate<br><br>
> `integer`<br><br>
> 修改时间

> schedulingState<br><br>
> `enum`<br><br>
> 定时任务状态
>
> - NONE - 空
> - NORMAL - 正常
> - PAUSED - 暂停
> - COMPLETE - 完成
> - ERROR - 错误
> - BLOCKED - 阻塞

> nextFireTimes<br><br>
> `integer`<br><br>
> 定时任务未来的触发时间

> previousFireTime<br><br>
> `integer` `nullable`<br><br>
> 定时任务上一次的触发时间
