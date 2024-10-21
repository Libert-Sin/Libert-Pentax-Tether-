---
layout: docs
---

# Capture Settings

This page describes how to set and get various capture settings of connected camera device using RICOH Camera Wireless SDK.

### Contents

* [Core Class](#core-class)
* [Basic Usage](#basic-usage)

**Supported Capture Settings**

* [Aperture Value](#aperture-value)
* [Exposure Compensation](#exposure-compensation)
* [ISO Sensitivity Value](#iso-sensitivity-value)
* [White Balance](#white-balance)
* [Shutter Speed](#shutter-speed)
* [Custom Image](#custom-image)
* [Digital Filter](#digital-filter)
* [Still Image Capture Format](#still-image-capture-format)
* [Still Image Quality](#still-image-quality)
* [Movie Capture Format](#movie-capture-format)

<a name="core-class"></a>
## Core Class

The core class of capture settings is:

#### [`CaptureSetting`](../../api_reference/Classes/CaptureSetting.html)

* The `CaptureSetting` class represents various capture settings of camera devices.
* Supported capture settings are provided as derived classes of the `CaptureSetting` class.

<a name="basic-usage"></a>
## Basic Usage

All the supported capture settings have corresponding classes derived from the [`CaptureSetting`](../../api_reference/Classes/CaptureSetting.html) class.
This section shows you how to use manage individual capture settings using the classes.
You can also manage multiple capture settings at the same time.
Code examples in this section show how to manage aperture value but you can manage other settings in similar ways.

### Get Individual Capture Settings

Follow the steps below to get current value of specified capture setting.

1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`getCaptureSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP18getCaptureSettingsAA8ResponseCSayAA0G7SettingCG8settings_tF) method with the object.

The following example shows how to get current aperture value using `FNumber` object

```swift
let fNumber = FNumber()
let response = cameraDevice.getCaptureSettings(settings: [fNumber])
if response.result == Result.ok {
    print("Current Value: \(fNumber.description)")
}
```

### List Available Setting Values

Follow the steps below to get a list of available values for individual capture settings.

1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`getCaptureSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP18getCaptureSettingsAA8ResponseCSayAA0G7SettingCG8settings_tF) method with the object
1. Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the object

The following example shows how to get a list of available aperture values using `FNumber` object.

```swift
let fNumber = FNumber()
let response = cameraDevice.getCaptureSettings(settings: [fNumber])
if response.result == Result.ok {
    for setting in fNumber.availableSettings {
        print("Available Setting: \(setting.description)")
    }
}
```

### Set Individual Capture Setting

Follow the steps below to set value of specified capture setting.

1. Select a class and its field corresponding to the value you want to set
1. Use the [`setCaptureSettings`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP18setCaptureSettingsAA8ResponseCSayAA0G7SettingCG8settings_tF) method with the field

The following example shows how to set the aperture value to 5.6 using `FNumber` object.

```java
let fNumber = FNumber.f5_6
let response = cameraDevice.setCaptureSettings(settings: [fNumber])
```

### Get Multiple Capture Settings

The following example shows how to get shutter speed and exposure compensation value at the same time.

```swift
let shutterSpeed = ShutterSpeed()
let exposureCompensation = ExposureCompensation()
let response =
    cameraDevice.getCaptureSettings(settings: [shutterSpeed, exposureCompensation])
```

### Set Multiple Capture Settings

The following example shows how to set shutter speed and exposure compensation value at the same time.

```swift
let shutterSpeed = ShutterSpeed.ss1_80
let exposureCompensation = ExposureCompensation.ec0_3
let response =
    cameraDevice.setCaptureSettings(settings: [shutterSpeed, exposureCompensation])
```

<a name="aperture-value"></a>
## Aperture Value

Use the [`FNumber`](../../api_reference/Classes/FNumber.html) class to set and get aperture values.

### Get the Current Value & List Available Values

The following example shows how to get current aperture value using `FNumber` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `FNumber` class to get a list of available aperture values.

```swift
let fNumber = FNumber()
let response = cameraDevice.getCaptureSettings(settings: [fNumber])
if response.result == Result.ok {
    let availableList = fNumber.availableSettings
}
```

### Set Value

The following example demonstrates how to set aperture value to 5.6 using `FNumber` object.

```swift
let fNumber = FNumber.f5_6
let response = cameraDevice.setCaptureSettings(settings: [fNumber])
```

<a name="exposure-compensation"></a>
## Exposure Compensation

Use the [`ExposureCompensation`](../../api_reference/Classes/ExposureCompensation.html) class to set and get exposure compensation values.

### Get the Current Value & List Available Values

The following example shows how to get current exposure compensation value using `ExposureCompensation` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `ExposureCompensation` class to get a list of available exposure compensation values.

```swift
let exposureCompensation = ExposureCompensation()
let response = cameraDevice.getCaptureSettings(settings: [exposureCompensation])
if response.result == Result.ok {
    let availableList = exposureCompensation.availableSettings
}
```

### Set Value

The following example demonstrates how to set exposure compensation to +0.3EV using `ExposureCompensation` object.

```swift
let exposureCompensation = ExposureCompensation.ec0_3
let response = cameraDevice.setCaptureSettings(settings: [exposureCompensation])
```

<a name="iso-sensitivity-value"></a>
## ISO Sensitivity Value

Use the [`ISO`](../../api_reference/Classes/ISO.html) class to set and get ISO sensitivity values.

### Get the Current Value & List Available Values

The following example shows how to get current ISO sensitivity value using `ISO` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `ISO` class to get a list of available ISO sensitivity values.

```swift
let iso = ISO()
let response = cameraDevice.getCaptureSettings(settings: [iso])
if response.result == Result.ok {
    let availableList = iso.availableSettings
}
```

### Set Value

The following example demonstrates how to set ISO sensitivity value to ISO400 using `ISO` object.

```swift
let iso = ISO.ISO400
let response = cameraDevice.setCaptureSettings(settings: [iso])
```

<a name="white-balance"></a>
## White Balance

Use the [`WhiteBalance`](../../api_reference/Classes/WhiteBalance.html) class to set and get white balance values.

### Get the Current Value & List Available Values

The following example shows how to get current white balance value using `WhiteBalance` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `WhiteBalance` class to get a list of available white balance values.

```swift
let whiteBalance = WhiteBalance()
let response = cameraDevice.getCaptureSettings(settings: [whiteBalance])
if response.result == Result.ok {
    let availableList = whiteBalance.availableSettings
}
```

### Set Value

The following example demonstrates how to set white balance to daylight using `WhiteBalance` object.

```swift
let whiteBalance = WhiteBalance.daylight
let response = cameraDevice.setCaptureSettings(settings: [whiteBalance])
```

<a name="shutter-speed"></a>
## Shutter Speed

Use the [`ShutterSpeed`](../../api_reference/Classes/ShutterSpeed.html) class to set and get shutter speed values.

### Get the Current Value & List Available Values

The following example shows how to get current shutter speed value using `ShutterSpeed` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `ShutterSpeed` class to get a list of available shutter speed values.

```swift
let shutterSpeed = ShutterSpeed()
let response = cameraDevice.getCaptureSettings(settings: [shutterSpeed])
if response.result == Result.ok {
    let availableList = shutterSpeed.availableSettings
}
```

### Set Value

The following example demonstrates how to set shutter speed to 1/80 using `ShutterSpeed` object.

```swift
let shutterSpeed = ShutterSpeed.ss1_80
let response = cameraDevice.setCaptureSettings(settings: [shutterSpeed])
```

<a name="custom-image"></a>
## Custom Image

Use the [`CustomImage`](../../api_reference/Classes/CustomImage.html) class to set and get custom image values.

### Get the Current Value & List Available Values

The following example shows how to get current custom image value using `CustomImage` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `CustomImage` class to get a list of available custom image values.

```swift
let customImage = CustomImage()
let response = cameraDevice.getCaptureSettings(settings: [customImage])
if response.result == Result.ok {
    let availableList = customImage.availableSettings
}
```

### Set Value

The following example demonstrates how to set custom image to landscape using `CustomImage` object.

```swift
let customImage = CustomImage.landscape
let response = cameraDevice.setCaptureSettings(settings: [customImage])
```

<a name="digital-filter"></a>
## Digital Filter

Use the [`DigitalFilter`](../../api_reference/Classes/DigitalFilter.html) class to set and get digital filter values.

### Get the Current Value & List Available Values

The following example shows how to get current digital filter value using `DigitalFilter` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `DigitalFilter` class to get a list of available digital filter values.

```swift
let digitalFilter = DigitalFilter()
let response = cameraDevice.getCaptureSettings(settings: [digitalFilter])
if response.result == Result.ok {
    let availableList = digitalFilter.availableSettings
}
```

### Set Value

The following example demonstrates how to set digital filter to toy camera using `DigitalFilter` object.

```swift
let digitalFilter = DigitalFilter.toyCamera
let response = cameraDevice.setCaptureSettings(settings: [digitalFilter])
```

<a name="still-image-capture-format"></a>
## Still Image Capture Format

Use the [`StillImageCaptureFormat`](../../api_reference/Classes/StillImageCaptureFormat.html) class to set and get still image capture format values.

### Get the Current Value & List Available Values

The following example shows how to get current still image capture format value using `StillImageCaptureFormat` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `StillImageCaptureFormat` class to get a list of available still image capture format values.

```swift
let stillImageCaptureFormat = StillImageCaptureFormat()
let response = cameraDevice.getCaptureSettings(settings: [stillImageCaptureFormat])
if response.result == Result.ok {
    let availableList = stillImageCaptureFormat.availableSettings
}
```

### Set Value

The following example demonstrates how to set still image capture format to RAW(DNG) using `StillImageCaptureFormat` object.

```swift
let stillImageCaptureFormat = StillImageCaptureFormat.DNG
let response = cameraDevice.setCaptureSettings(settings: [stillImageCaptureFormat])
```

<a name="still-imag-quality"></a>
## Still Image Quality

Use the [`StillImageQuality`](../../api_reference/Classes/StillImageQuality.html) class to set and get still image quality values.

### Get the Current Value & List Available Values

The following example shows how to get current still image quality value using `StillImageQuality` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `StillImageQuality` class to get a list of available still image quality values.

```swift
let stillImageQuality = StillImageQuality()
let response = cameraDevice.getCaptureSettings(settings: [stillImageQuality])
if response.result == Result.ok {
    let availableList = stillImageQuality.availableSettings
}
```

### Set Value

The following example demonstrates how to set still image quality to "Recorded Pixels":Large and "Quality Level":Best using `StillImageQuality` object.

```swift
let stillImageQuality = StillImageQuality.largeBest
let response = cameraDevice.setCaptureSettings(settings: [stillImageQuality])
```

<a name="movie-capture-format"></a>
## Movie Capture Format

Use the [`MovieCaptureFormat`](../../api_reference/Classes/MovieCaptureFormat.html) class to set and get movie capture format values.

### Get the Current Value & List Available Values

The following example shows how to get current movie capture format value using `MovieCaptureFormat` object.
Use the [`availableSettings`](../../api_reference/Classes/CaptureSetting.html#/s:22RICOHCameraWirelessSDK14CaptureSettingC17availableSettingsSayACGv) property of the `MovieCaptureFormat` class to get a list of available movie capture format values.

```swift
let movieCaptureFormat = MovieCaptureFormat()
let response = cameraDevice.getCaptureSettings(settings: [movieCaptureFormat])
if response.result == Result.ok {
    let availableList = movieCaptureFormat.availableSettings
}
```

### Set Value

The following example demonstrates how to set movie capture format to "Still Image Size":FullHD and "Frame Rate":60i using `MovieCaptureFormat` object.

```swift
let movieCaptureFormat = MovieCaptureFormat.fullHD60i
let response = cameraDevice.setCaptureSettings(settings: [movieCaptureFormat])
```

