## 查找 BACnet 设备

`GET` `/bacnet/service/who-is`

发送 Who-Is 请求

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

> deviceInstance<br><br>
> `integer`<br><br>
> BACnet设备ID

> macAddress<br><br>
> `string`<br><br>
> macAddress

> snet<br><br>
> `integer`<br><br>
> snet

> sadr<br><br>
> `string`<br><br>
> sadr

> apdu<br><br>
> `integer`<br><br>
> apdu

### Example

#### Request example

```shell
curl http://localhost/bacnet/service/who-is
```

#### Response example

```json
[
  {
    "deviceInstance": 7115,
    "macAddress": "C0:A8:0D:C6:BA:C0",
    "snet": 0,
    "sadr": "00",
    "apdu": 1476
  },
  {
    "deviceInstance": 7609,
    "macAddress": "C0:A8:0D:AF:BA:C0",
    "snet": 0,
    "sadr": "00",
    "apdu": 1476
  },
  {
    "deviceInstance": 7602,
    "macAddress": "C0:A8:0D:A8:BA:C0",
    "snet": 0,
    "sadr": "00",
    "apdu": 1476
  }
]
```

## 查询Bacnet设备属性

`GET` `/bacnet/device/{deviceInstance}/object-ids`

查询 BACnet 设备的 DEVICE （8） 对象的 OBJECT_LIST （76） 属性

### Parameters

#### Headers

> None

#### Path parameters

> deviceInstance<br><br>
> `integer` `required`<br><br>
> BACnet设备ID

#### Query parameters

> None

#### Request body parameters

> None

#### Response body parameters

> objectType<br><br>
> `integer`<br><br>
> BACnet对象类型

> objectInstance<br><br>
> `integer`<br><br>
> BACnet对象号

> objectTypeName<br><br>
> `string`<br><br>
> BACnet类型名称

### Example

#### Request example

```shell
curl http://localhost/bacnet/device/7602/object-ids
```

#### Response example

```json
[
  {
    "objectType": 0,
    "objectInstance": 0,
    "objectTypeName": "ANALOG_INPUT"
  },
  {
    "objectType": 1,
    "objectInstance": 0,
    "objectTypeName": "ANALOG_OUTPUT"
  },
  {
    "objectType": 2,
    "objectInstance": 0,
    "objectTypeName": "ANALOG_VALUE"
  }
]
```
