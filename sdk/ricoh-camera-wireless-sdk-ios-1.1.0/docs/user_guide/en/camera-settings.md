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

#### [`CameraDeviceSetting`](../../api_reference/Classes/CameraDeviceSetting.html)

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
1. Use the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method with the object

The following example shows how to get current dual card slots mode value using `DualCardSlotsMode` object.

```swift
let slotMode = DualCardSlotsMode()
let response = cameraDevice.getCameraDeviceSettings(settings: [slotMode])
if response.result == Result.ok {
    print("Current Value: \(slotMode.description)")
}
```

### List Available Settings

Follow the steps below to get a list of available settings.

1. Generate an object of supported camera setting
1. Use the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method with the object
1. Use the [`availableSettings`](../../api_reference/Classes/CameraDeviceSetting.html#/s:22RICOHCameraWirelessSDK19CameraDeviceSettingC17availableSettingsSayACGv) property of the object

The following example shows how to get a list of available dual card slots mode values using `DualCardSlotsMode` object.

```swift
let slotMode = DualCardSlotsMode()
let response = cameraDevice.getCameraDeviceSettings(settings: [slotMode])
if response.result == Result.ok {
    for setting in slotMode.availableSettings {
        print("Available Setting: \(setting.description)")
    }
}
```

### Set Individual Camera Setting

Follow the steps below to set a setting.

1. Select a class variable of supported camera setting
1. Use the [`setCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03setdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method with the variable

The following example shows how to set sequential use for dual card slots mode value using a `DualCardSlotsMode` object.

```swift
let slotMode = DualCardSlotsMode.sequentialUse
let response = cameraDevice.setCameraDeviceSettings(settings: [slotMode])
```

### Get Multiple Camera Settings

The following example shows how to get wi-fi settings value at the same time.

```swift
let ssid = SSID()
let key = Key()
let channel = Channel()
let response =
    cameraDevice.getCameraDeviceSettings(settings: [ssid, key, channel])
if response.result == Result.ok {
    let ssidValue = ssid.value
    let keyValue = key.value
    let channelValue = channel.value
    let availableList = channel.availableSettings
}
```

### Set Multiple Camera Settings

The following example demonstrates how to set wi-fi settings at the same time.

```swift
let ssid = SSID(ssid: "RICOH_CAMERA")
let key = Key(password: "12345678")
let channel = Channel.n1
let response =
    cameraDevice.setCameraDeviceSettings(settings: [ssid, key, channel])
```

<a name="date-and-time"></a>
## Date and Time

Use the [`CameraTime`](../../api_reference/Classes/CameraTime.html) class to set and get date and time values.

### Get the Current Value

The following example shows how to get the current date and time value of connected camera and `Date` object of the same value.

```swift
let cameraTime = CameraTime()
let response = cameraDevice.getCameraDeviceSettings(settings: [cameraTime])
if response.result == Result.ok {
    let cameraTimeValue = cameraTime.value
    let dateCameraTime = cameraTimeValue?.object
}
```

You can use the `CameraTime` class and the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to get the current date and time of connected camera.
Also, if you want a `Date` object of the acquired `CameraTime`, you can use the [`object`](../../api_reference/Classes/CameraDeviceSetting/Value.html#/s:22RICOHCameraWirelessSDK19CameraDeviceSettingC5ValueC6objectSo8NSObjectCv) property of [`CameraTime.Value`](../../api_reference/Classes/CameraDeviceSetting/Value.html) which stores the actual value in the [`CameraTime`](../../api_reference/Classes/CameraTime.html) object.

### Set Value

The following example demonstrates how to set camera device's date and time value.

```swift
let cameraTime = CameraTime(date: Date())
let response = cameraDevice.setCameraDeviceSettings(settings: [cameraTime])
```

You can use the `CameraTime` class and the [`setCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03setdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to set a specified `Date` object.

<a name="wi-fi-settings"></a>
## Wi-Fi Settings

Wi-Fi Settings consists of SSID, password and channel.     

* Use the [`SSID`](../../api_reference/Classes/SSID.html) class to set and get SSID values.     
* Use the [`Key`](../../api_reference/Classes/Key.html) class to set and get password values.     
* Use the [`Channel`](../../api_reference/Classes/Channel.html) class to set and get channel values.

### Get the Current Value & List Available Values

The following example shows how to get current SSID, password and channel value using `SSID`, `Key` and `Channel` object.

```swift
let ssid = SSID()
let key = Key()
let channel = Channel()
let response =
    cameraDevice.getCameraDeviceSettings(settings: [ssid, key, channel])
if response.result == Result.ok {
    let ssidValue = ssid.value
    let strSSID = ssidValue?.object
    let keyValue = key.value
    let strKey = keyValue?.object
    let channelValue = channel.value
    let availableList = channel.availableSettings
}
```

You can use the `SSID` class and the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to get the current ssid value of connected camera.
Also, if you want a `String` object of the acquired `SSID`, you can use the [`object`](../../api_reference/Classes/CameraDeviceSetting/Value.html#/s:22RICOHCameraWirelessSDK19CameraDeviceSettingC5ValueC6objectSo8NSObjectCv) property of [`SSID.Value`](../../api_reference/Classes/SSID.html#/s:22RICOHCameraWirelessSDK4SSIDC5ValueC) which stores the actual value in the [`SSID`](../../api_reference/Classes/SSID.html) object.

You can use the `Key` class and the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to get the current password value of connected camera.
Also, if you want a `String` object of the acquired `Key`, you can use the [`object`](../../api_reference/Classes/CameraDeviceSetting/Value.html#/s:22RICOHCameraWirelessSDK19CameraDeviceSettingC5ValueC6objectSo8NSObjectCv) property of [`Key.Value`](../../api_reference/Classes/Key.html#/s:22RICOHCameraWirelessSDK3KeyC5ValueC) which stores the actual value in the [`Key`](../../api_reference/Classes/Key.html) object.

You can use the `Channel` class and the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to get the current channel value of connected camera.
Also, you can use the [`availableSettings`](../../api_reference/Classes/CameraDeviceSetting.html#/s:22RICOHCameraWirelessSDK19CameraDeviceSettingC17availableSettingsSayACGv) property of the `Channel` class to get a list of available channel values.

### Set Value

The following example demonstrates how to set;

* SSID to "RICOH_CAMERA" using `SSID` object
* password to "12345678" using `Key` object
* channel to 1 using `Channel` object

```swift
let ssid = ScSID(ssid: "RICOH_CAMERA")
let key = Key(password: "12345678")
let channel = Channel.n1
let response =
    cameraDevice.setCameraDeviceSettings(settings: [ssid, key, channel])
```

`SSID` class, `Key` class and `Channel` are need to set at same time.

You can use the `SSID` and `Key` class and the [`setCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03setdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method to set a specified `String` object.

<a name="dual-card-slots-mode"></a>
## Dual Card Slots Mode

Use the [`DualCardSlotsMode`](../../api_reference/Classes/DualCardSlotsMode.html) class to set and get dual card slots mode values.

### Get the Current Value & List Available Values

The following example shows how to get current dual card slots mode value using `DualCardSlotsMode` object.
Use the [`getCameraDeviceSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP03getdE8SettingsAA8ResponseCSayAA0dE7SettingCG8settings_tF) method of the `DualCardSlotsMode` class to get a list of available dual card slots mode values.

```swift
let slotMode = DualCardSlotsMode()
let response = cameraDevice.getCameraDeviceSettings(settings: [slotMode])
if response.result == Result.ok {
    let availableList = slotMode.availableSettings
}
```

### Set Value

The following example demonstrates how to set dual card slots mode to sequential use using `DualCardSlotsMode` object.

```swift
let slotMode = DualCardSlotsMode.sequentialUse
let response = cameraDevice.setCameraDeviceSettings(settings: [slotMode])
```
