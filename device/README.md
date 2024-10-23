## 新增设备

`POST` `/device/add`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码，全局唯一

> deviceName<br><br>
> `string`<br><br>
> 设备名称

> path<br><br>
> `string`<br><br>
> 目录路径

> description<br><br>
> `string`<br><br>
> 描述

> deviceExtra<br><br>
> `object` `required`<br><br>
> 设备的其他属性<br>
> - [BACnet设备属性](#BACnet设备属性)

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceCode": "B831",
  "deviceName": "B831",
  "deviceExtra": {
    "type": "BACNET",
    "deviceInstance": 7601
  }
}
' \
http://localhost/device/add
```

#### Response example

    None

## 修改设备

`POST` `/device/update`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码，全局唯一

> deviceName<br><br>
> `string`<br><br>
> 设备名称

> path<br><br>
> `string`<br><br>
> 目录路径

> description<br><br>
> `string`<br><br>
> 描述

> deviceExtra<br><br>
> `object` `required`<br><br>
> 设备的其他属性<br>
> - [BACnet设备属性](#BACnet设备属性)

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceCode": "B831",
  "deviceName": "B831",
  "path": "/foo/bar",
  "description": "测试设备1",
  "deviceExtra": {
    "type": "BACNET",
    "deviceInstance": 7609
  }
}
' \
http://localhost/device/update
```

#### Response example

    None

## 删除设备

`POST` `/device/remove`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码，全局唯一

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceCode": "B831"
}
' \
http://localhost/device/remove
```

#### Response example

    None

## 保存设备点位

`POST` `/device/point/put`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码，全局唯一

> devicePoints<br><br>
> `array` `required`<br><br>
> 设备点位
>
> > devicePoints.pointCode<br><br>
> > `string` `required`<br><br>
> > 点位编码
>
> > devicePoints.pointName<br><br>
> > `string`<br><br>
> > 点位名称
>
> > devicePoints.pointExtra<br><br>
> > `object` `required`<br><br>
> > 点位的其他属性
> > - [BACnet点位属性](#BACnet点位属性)

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceCode": "B831",
  "devicePoints": [
    {
      "pointCode": "2",
      "pointName": "2",
      "pointExtra": {
        "type": "BACNET",
        "objectType": 2,
        "objectInstance": 2,
        "propertyIdentifier": 85
      }
    }
  ]
}
' \
http://localhost/device/point/put
```

#### Response example

    None

## 删除设备点位

`POST` `/device/point/remove`

### Parameters

#### Headers

> `Content-Type`: `application/json`

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码，全局唯一

> pointCodes<br><br>
> `array`<br><br>
> 点位编码

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceCode": "B831",
  "pointCodes": [
    "1",
    "2"
  ]
}
' \
http://localhost/device/point/remove
```

#### Response example

    None

## 根据设备编码查询设备

`GET` `/device/{deviceCode}`

### Parameters

#### Headers

> None

#### Path parameters

> deviceCode<br><br>
> `string` `required`<br><br>
> 设备编码

#### Query parameters

> [readonly](../README.md#查询参数)<br><br>
> 只有为`true`时才会返回`deviceStatus`字段

#### Request body parameters

> None

#### Response body parameters

> [设备属性和状态](#设备属性和状态)

### Example

#### Request example

```shell
curl http://localhost/device/B831
curl http://localhost/device/B831?readonly=true
```

#### Response example

```json
{
  "deviceCode": "B831",
  "deviceName": "B831",
  "path": "/foo/bar",
  "description": "测试设备1",
  "enabled": true,
  "deviceExtra": {
    "type": "BACNET",
    "deviceInstance": 7609
  },
  "devicePoints": [
    {
      "pointCode": "2",
      "pointName": "2",
      "pointExtra": {
        "type": "BACNET",
        "objectType": 2,
        "objectInstance": 2,
        "propertyIdentifier": 85,
        "propertyArrayIndex": null
      }
    }
  ],
  "deviceStatus": {
    "deviceCode": "B831",
    "values": {
      "2": 12.0
    },
    "timestamp": 1729672545123
  }
}
```

## 查询设备

`GET` `/device`

### Parameters

#### Headers

> None

#### Path parameters

> None

#### Query parameters

> [readonly](../README.md#查询参数)<br><br>
> 只有为`true`时才会返回`deviceStatus`字段

> [page](../README.md#查询参数)

> [pagesize](../README.md#查询参数)

#### Request body parameters

> None

#### Response body parameters

> [设备属性和状态](#设备属性和状态)

### Example

#### Request example

```shell
curl http://localhost/device
curl http://localhost/device?readonly=true
```

#### Response example

```json
[
  {
    "deviceCode": "B831",
    "deviceName": "B831",
    "path": "/foo/bar",
    "description": "测试设备1",
    "enabled": true,
    "deviceExtra": {
      "type": "BACNET",
      "deviceInstance": 7609
    },
    "devicePoints": [
      {
        "pointCode": "2",
        "pointName": "2",
        "pointExtra": {
          "type": "BACNET",
          "objectType": 2,
          "objectInstance": 2,
          "propertyIdentifier": 85,
          "propertyArrayIndex": null
        }
      }
    ],
    "deviceStatus": {
      "deviceCode": "B831",
      "values": {
        "2": 12.0
      },
      "timestamp": 1729672650127
    }
  }
]
```

## 数据结构

### BACnet设备属性

> type<br><br>
> `string` `enum` `required`<br><br>
> Enum options:<br><br>
> `BACNET` - 表示设备是一个 BACnet 设备

> deviceInstance<br><br>
> `integer` `required`<br><br>
> BACnet设备ID

### BACnet点位属性

> type<br><br>
> `string` `enum` `required`<br><br>
> Enum options:<br><br>
> `BACNET` - 表示点位是一个 BACnet 设备的点位

> objectType<br><br>
> `integer` `required`<br><br>
> BACnet对象类型

> objectInstance<br><br>
> `integer` `required`<br><br>
> BACnet对象ID

> propertyIdentifier<br><br>
> `integer` `required`<br><br>
> BACnet属性ID（通常用 85 表示 BACnet 属性当前值）

### 设备属性和状态

> deviceCode<br><br>
> `string`<br><br>
> 设备编码

> deviceName<br><br>
> `string`<br><br>
> 设备名称

> path<br><br>
> `string`<br><br>
> 目录路径

> description<br><br>
> `string`<br><br>
> 描述

> deviceExtra<br><br>
> `object`<br><br>
> 设备的其他属性<br>
> - [BACnet设备属性](#BACnet设备属性)

> devicePoints<br><br>
> `array`<br><br>
> 设备点位
>
> > devicePoints.pointCode<br><br>
> > `string`<br><br>
> > 点位编码
>
> > devicePoints.pointName<br><br>
> > `string`<br><br>
> > 点位名称
>
> > devicePoints.pointExtra<br><br>
> > `object`<br><br>
> > 点位的其他属性
> > - [BACnet点位属性](#BACnet点位属性)

> deviceStatus<br><br>
> `object` `nullable`<br><br>
> 设备状态，表示设备最后一次上报的数据，`null`表示设备未上报数据
> > deviceStatus.deviceCode<br><br>
> > `string`<br><br>
> > 设备编码
>
> > deviceStatus.values<br><br>
> > `object`<br><br>
> > 设备状态值，key是点位编码，value是点位值
>
> > deviceStatus.timestamp<br><br>
> > `integer`<br><br>
> > 设备最后一次上报数据时的时间戳（毫秒）
