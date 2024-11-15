## 发送 Who-Is

`POST` `/bacnet/service/who-is`

发送 Who-Is

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
curl -X POST -H 'Content-Type: application/json' -d '{}' http://localhost/bacnet/service/who-is
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

## 发送 ReadPropertyMultiple

`POST` `/bacnet/service/read-property-multiple`

发送 ReadPropertyMultiple

### Parameters

#### Headers

> None

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceInstance<br><br>
> `integer` `required`<br><br>
> BACnet设备ID

> objectProperties<br><br>
> `array`<br><br>
> 要读取的 Bacnet 对象和对象的属性
> > objectType<br><br>
> > `integer` `required`<br><br>
> > BACnet对象类型
>
> > objectInstance<br><br>
> > `integer` `required`<br><br>
> > BACnet对象ID
>
> > properties<br><br>
> > `array`<br><br>
> > BACnet对象的属性
> > > propertyIdentifier<br><br>
> > > `integer` `required`<br><br>
> > > BACnet属性ID（通常用 85 表示 BACnet 属性当前值）
> >
> > > propertyArrayIndex<br><br>
> > > `integer`<br><br>
> > > 数组索引

#### Response body parameters

> deviceInstance<br><br>
> `integer`<br><br>
> BACnet设备ID

> values<br><br>
> `array`<br><br>
> 读取的数据
> > objectType<br><br>
> > `integer`<br><br>
> > BACnet对象类型
>
> > objectInstance<br><br>
> > `integer`<br><br>
> > BACnet对象ID
>
> > properties<br><br>
> > `array`<br><br>
> > 属性值
> > > propertyIdentifier<br><br>
> > > `integer`<br><br>
> > > BACnet属性ID（通常用 85 表示 BACnet 属性当前值）
> >
> > > propertyArrayIndex<br><br>
> > > `integer`<br><br>
> > > 数组索引
> >
> > > propertyValues<br><br>
> > > `array` `nullable`<br><br>
> > > 属性值，不会和`error`属性同时存在
> > > > valueType<br><br>
> > > > `string`<br><br>
> > > > 值类型
> > >
> > > > value<br><br>
> > > > `any`<br><br>
> > > > 值
> >
> > > error<br><br>
> > > `object` `nullable`<br><br>
> > > 错误，不会和`propertyValues`属性同时存在

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceInstance": 7602,
  "objectProperties": [
    {
      "objectType": 2,
      "objectInstance": 2,
      "properties": [
        {
          "propertyIdentifier": 85,
          "propertyArrayIndex": null
        }
      ]
    }
  ]
}
' \
http://localhost/bacnet/service/read-property-multiple
```

#### Response example

```json
{
  "deviceInstance": 7602,
  "values": [
    {
      "objectType": 2,
      "objectInstance": 2,
      "properties": [
        {
          "propertyIdentifier": 85,
          "propertyArrayIndex": null,
          "propertyValues": [
            {
              "value": 15.0,
              "valueType": "4"
            }
          ],
          "error": null
        }
      ]
    }
  ]
}
```

## 发送 WriteProperty

`POST` `/bacnet/service/write-property`

发送 WriteProperty

### Parameters

#### Headers

> None

#### Path parameters

> None

#### Query parameters

> None

#### Request body parameters

> deviceInstance<br><br>
> `integer` `required`<br><br>
> BACnet设备ID

> objectType<br><br>
> `integer` `required`<br><br>
> BACnet对象类型

> objectInstance<br><br>
> `integer` `required`<br><br>
> BACnet对象ID

> propertyIdentifier<br><br>
> `integer` `required`<br><br>
> BACnet属性ID（通常用 85 表示 BACnet 属性当前值）

> propertyArrayIndex<br><br>
> `integer`<br><br>
> 数组索引

> priority<br><br>
> `integer`<br><br>
> 优先级

> valueType<br><br>
> `integer` `enum` `required`<br><br>
> 要写入的值类型
> - **1** - BOOLEAN
> - **2** - UNSIGNED_INT
> - **3** - SIGNED_INT
> - **4** - REAL
> - **5** - DOUBLE

> value<br><br>
> `any` `required`<br><br>
> 要写入的值，格式取决于`valueType`

#### Response body parameters

> None

### Example

#### Request example

```shell
curl -X POST -H 'Content-Type: application/json' \
-d '
{
  "deviceInstance": 7602,
  "objectType": 2,
  "objectInstance": 2,
  "propertyIdentifier": 85,
  "propertyArrayIndex": null,
  "priority": null,
  "valueType": "4",
  "value": 15.0
}
' \
http://localhost/bacnet/service/write-property
```

#### Response example

```
None
```

## 查询Bacnet设备的对象列表

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
> BACnet对象ID

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
