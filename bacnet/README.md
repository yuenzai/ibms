# 查找 BACnet 设备

`GET` `/bacnet/service/who-is`

发送 Who-Is 请求

## Parameters

### Headers

> None

### Path parameters

> None

### Query parameters

> None

### Request body parameters

> None

### Response body parameters

> `deviceInstance` `integer`<br><br>
> BACnet设备ID

> `macAddress` `string`<br><br>
> macAddress

> `snet` `integer`<br><br>
> snet

> `sadr` `string`<br><br>
> sadr

> `apdu` `integer`<br><br>
> apdu

## Example

### Request example

```shell
curl http://localhost/bacnet/service/who-is
```

### Response example

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

----

# 查询Bacnet设备属性

`GET` `/bacnet/device/{deviceInstance}/object-ids`

查询 BACnet 设备的 DEVICE （8） 对象的 OBJECT_LIST （76） 属性

## Parameters

### Headers

> None

### Path parameters

> `deviceInstance` `integer` `required`<br><br>
> BACnet设备ID

### Query parameters

> None

### Request body parameters

> None

### Response body parameters

> `objectType` `integer`<br><br>
> BACnet对象类型

> `objectInstance` `integer`<br><br>
> BACnet对象号

> `objectTypeName` `string`<br><br>
> BACnet类型名称

## Example

### Request example

```shell
curl http://localhost/bacnet/device/7602/object-ids
```

### Response example

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

---