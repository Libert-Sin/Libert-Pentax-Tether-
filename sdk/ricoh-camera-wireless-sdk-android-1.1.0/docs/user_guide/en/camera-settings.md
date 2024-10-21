---
layout: docs
---

# Camera Settings

This page describes how to set and get various camera settings of connected camera device using RICOH Camera Wireless SDK.

### Contents

* [Core Class](#core-class)
* [Basic Usage](#basic-usage)

**Supported Camera Settings**

* [Date and Time](#date-and-time)
* [Wi-Fi Settings](#wi-fi-settings)
* [Dual Card Slots Mode](#dual-card-slots-mode)

<a name="core-class"></a>
## Core Class

The core class of camera settings is:

#### [`CameraDeviceSetting`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.html)

* The `CameraDeviceSetting` class represents various camera settings of the connected camera device.
* Supported camera settings are provided as derived class of the `CameraDeviceSetting` class.

<a name="basic-usage"></a>
## Basic Usage

This section shows you how to manage individual camera settings.
You can also manage multiple camera settings at the same time.
Code examples in this section shows how to manage dual card slots mode value but you can manage other settings in similar ways.

### Get Individual Camera Setting

Follow the steps below to get a current setting.

1. Generate an object of supported camera setting
1. Use the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method with the object

The following example shows how to get current dual card slots mode value using `DualCardSlotsMode` object.

```java
DualCardSlotsMode slotMode = new DualCardSlotsMode();
Response response =
    cameraDevice.getCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)slotMode));
if (response.getResult() == Result.OK) {
    System.out.printf("Current Value: %s%n", slotMode.toString());
}
```

### List Available Settings

Follow the steps below to get a list of available settings.

1. Generate an object of supported camera setting
1. Use the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method with the object
1. Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.html#getAvailableSettings--) method of the object

The following example shows how to get a list of available dual card slots mode values using `DualCardSlotsMode` object.

```java
DualCardSlotsMode slotMode = new DualCardSlotsMode();
Response response =
    cameraDevice.getCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)slotMode));
if (response.getResult() == Result.OK) {
  List<CameraDeviceSetting> availableList = slotMode.getAvailableSettings();
  for (CameraDeviceSetting setting: availableList) {
      System.out.printf("Available Setting: %s%n", setting.toString());
  }
}
```

### Set Individual Camera Setting

Follow the steps below to set a setting.

1. Select a class variable of supported camera setting
1. Use the [`setCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#setCameraDeviceSettings-java.util.List-) method with the variable

The following example shows how to set sequential use for dual card slots mode value using a `DualCardSlotsMode` object.

```java
DualCardSlotsMode slotMode = DualCardSlotsMode.SEQUENTIAL_USE;
Response response =
    cameraDevice.setCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)slotMode));
```

### Get Multiple Camera Settings

The following example shows how to get wi-fi settings value at the same time.

```java
SSID ssid = new SSID();
Key key = new Key();
Channel channel = new Channel();
Response response =
    cameraDevice.getCameraDeviceSettings(Arrays.asList(ssid, key, channel));
if (response.getResult() == Result.OK) {
    SSID.Value ssidValue = (SSID.Value) ssid.getValue();
    String strSSID = (String) ssidValue.get();
    Key.Value keyValue = (Key.Value) key.getValue();
    String strKey = (String) keyValue.get();
    List<CameraDeviceSetting> availableList = channel.getAvailableSettings();
}
```

### Set Multiple Camera Settings

The following example demonstrates how to set wi-fi settings at the same time.

```java
SSID ssid = new SSID("RICOH_CAMERA");
Key key = new Key("12345678");
Channel channel = Channel.N1;
Response response =
    cameraDevice.setCameraDeviceSettings(Arrays.asList(ssid, key, channel));
```

<a name="date-and-time"></a>
## Date and Time

Use the [`CameraTime`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraTime.html) class to set and get date and time values.

### Get the Current Value

The following example shows how to get the current date and time value of connected camera and `Date` object of the same value.

```java
CameraTime cameraTime = new CameraTime();
Response response =
    cameraDevice.getCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)cameraTime));
if (response.getResult() == Result.OK) {
    CameraTime.Value cameraTimeValue =
        (CameraTime.Value) cameraTime.getValue();
    Date dateCameraTime = (Date) cameraTimeValue.get();
}
```

You can use the `CameraTime` class and the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method to get the current date and time of connected camera.
Also, if you want a `Date` object of the acquired `CameraTime`, you can use the [`get`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.Value.html#get--) method of [`CameraTime.Value`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraTime.Value.html) which stores the actual value in the [`CameraTime`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraTime.html) object.

### Set Value

The following example demonstrates how to set camera device's date and time value.

```java
CameraTime cameraTime = new CameraTime(new Date());
Response response =
    cameraDevice.setCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)cameraTime));
```

You can use the `CameraTime` class and the [`setCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#setCameraDeviceSettings-java.util.List-) method to set a specified `Date` object.

<a name="wi-fi-settings"></a>
## Wi-Fi Settings

Wi-Fi Settings consists of SSID, password and channel.     

* Use the [`SSID`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/SSID.html) class to set and get SSID values.     
* Use the [`Key`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/Key.html) class to set and get password values.     
* Use the [`Channel`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/Channel.html) class to set and get channel values.

### Get the Current Value & List Available Values

The following example shows how to get current SSID, password and channel value using `SSID`, `Key` and `Channel` object.

```java
SSID ssid = new SSID();
Key key = new Key();
Channel channel = new Channel();
Response response =
    cameraDevice.getCameraDeviceSettings(Arrays.asList(ssid, key, channel));
if (response.getResult() == Result.OK) {
    SSID.Value ssidValue = (SSID.Value) ssid.getValue();
    String strSSID = (String) ssidValue.get();
    Key.Value keyValue = (Key.Value) key.getValue();
    String strKey = (String) keyValue.get();
    List<CameraDeviceSetting> availableList = channel.getAvailableSettings();
}
```

You can use the `SSID` class and the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method to get the current ssid value of connected camera.
Also, if you want a `String` object of the acquired `SSID`, you can use the [`get`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.Value.html#get--) method of [`SSID.Value`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/SSID.Value.html) which stores the actual value in the [`SSID`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/SSID.html) object.

You can use the `Key` class and the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method to get the current password value of connected camera.
Also, if you want a `String` object of the acquired `Key`, you can use the [`get`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.Value.html#get--) method of [`Key.Value`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/Key.Value.html) which stores the actual value in the [`Key`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/Key.html) object.

You can use the `Channel` class and the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method to get the current channel value of connected camera.
Also, you can use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/CameraDeviceSetting.html#getAvailableSettings--) method of the `Channel` class to get a list of available channel values.

### Set Value

The following example demonstrates how to set;

* SSID to "RICOH_CAMERA" using `SSID` object
* password to "12345678" using `Key` object
* channel to 1 using `Channel` object

```java
SSID ssid = new SSID("RICOH_CAMERA");
Key key = new Key("12345678");
Channel channel = Channel.N1;
Response response =
    cameraDevice.setCameraDeviceSettings(Arrays.asList(ssid, key, channel));
```

`SSID` class, `Key` class and `Channel` are need to set at same time.

You can use the `SSID` and `Key` class and the [`setCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#setCameraDeviceSettings-java.util.List-) method to set a specified `String` object.

<a name="dual-card-slots-mode"></a>
## Dual Card Slots Mode

Use the [`DualCardSlotsMode`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/camera/DualCardSlotsMode.html) class to set and get dual card slots mode values.

### Get the Current Value & List Available Values

The following example shows how to get current dual card slots mode value using `DualCardSlotsMode` object.
Use the [`getCameraDeviceSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCameraDeviceSettings-java.util.List-) method of the `DualCardSlotsMode` class to get a list of available dual card slots mode values.

```java
DualCardSlotsMode slotMode = new DualCardSlotsMode();
Response response =
    cameraDevice.getCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)slotMode));
if (response.getResult() == Result.OK) {
    List<CameraDeviceSetting> availableList = slotMode.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set dual card slots mode to sequential use using `DualCardSlotsMode` object.

```java
DualCardSlotsMode slotMode = DualCardSlotsMode.SEQUENTIAL_USE;
Response response =
    cameraDevice.setCameraDeviceSettings(
        Arrays.asList((CameraDeviceSetting)slotMode));
```
