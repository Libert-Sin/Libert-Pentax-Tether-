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

#### [`CaptureSetting`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html)

* The `CaptureSetting` class represents various capture settings of camera devices.
* Supported capture settings are provided as derived classes of the `CaptureSetting` class.

<a name="basic-usage"></a>
## Basic Usage

All the supported capture settings have corresponding classes derived from the [`CaptureSetting`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html) class.
This section shows you how to use manage individual capture settings using the classes.
You can also manage multiple capture settings at the same time.
Code examples in this section show how to manage aperture value but you can manage other settings in similar ways.

### Get Individual Capture Settings

Follow the steps below to get current value of specified capture setting.

1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`getCaptureSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCaptureSettings-java.util.List-) method with the object.

The following example shows how to get current aperture value using `FNumber` object

```java
FNumber fNumber = new FNumber();
Response response =
    cameraDevice.getCaptureSettings(Arrays.asList((CaptureSetting)fNumber));
if (response.getResult() == Result.OK) {
    System.out.printf("Current Value: %s%n", fNumber.toString());
}
```

### List Available Setting Values

Follow the steps below to get a list of available values for individual capture settings.

1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`getCaptureSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getCaptureSettings-java.util.List-) method with the object
1. Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the object

The following example shows how to get a list of available aperture values using `FNumber` object.

```java
FNumber fNumber = new FNumber();
cameraDevice.getCaptureSettings(Arrays.asList((CaptureSetting)fNumber);
if (response.getResult() == Result.OK) {
  List<CaptureSetting> availableList = fNumber.getAvailableSettings();
  for (CaptureSetting setting: availableList) {
      System.out.printf("Available Setting: %s%n", setting.toString());
  }
}
```

### Set Individual Capture Setting

Follow the steps below to set value of specified capture setting.

1. Select a class and its field corresponding to the value you want to set
1. Use the [`setCaptureSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#setCaptureSettings-java.util.List-) method with the field

The following example shows how to set the aperture value to 5.6 using `FNumber` object.

```java
FNumber fNumber = FNumber.F5_6;
Response response =
    cameraDevice.setCaptureSettings(Arrays.asList((CaptureSetting)fNumber));
```

### Get Multiple Capture Settings

The following example shows how to get shutter speed and exposure compensation value at the same time.

```java
ShutterSpeed shutterSpeed = new ShutterSpeed();
ExposureCompensation exposureCompensation = new ExposureCompensation();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList(shutterSpeed, exposureCompensation));
```

### Set Multiple Capture Settings

The following example shows how to set shutter speed and exposure compensation value at the same time.

```java
ShutterSpeed shutterSpeed = ShutterSpeed.SS1_80;
ExposureCompensation exposureCompensation = ExposureCompensation.EC0_3;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList(shutterSpeed, exposureCompensation));
```

<a name="aperture-value"></a>
## Aperture Value

Use the [`FNumber`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/FNumber.html) class to set and get aperture values.

### Get the Current Value & List Available Values

The following example shows how to get current aperture value using `FNumber` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `FNumber` class to get a list of available aperture values.

```java
FNumber fNumber = new FNumber();
Response response =
    cameraDevice.getCaptureSettings(Arrays.asList((CaptureSetting)fNumber));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = fNumber.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set aperture value to 5.6 using `FNumber` object.

```java
FNumber fNumber = FNumber.F5_6;
Response response =
    cameraDevice.setCaptureSettings(Arrays.asList((CaptureSetting)fNumber));
```

<a name="exposure-compensation"></a>
## Exposure Compensation

Use the [`ExposureCompensation`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/ExposureCompensation.html) class to set and get exposure compensation values.

### Get the Current Value & List Available Values

The following example shows how to get current exposure compensation value using `ExposureCompensation` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `ExposureCompensation` class to get a list of available exposure compensation values.

```java
ExposureCompensation exposureCompensation = new ExposureCompensation();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)exposureCompensation));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList =
        exposureCompensation.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set exposure compensation to +0.3EV using `ExposureCompensation` object.

```java
ExposureCompensation exposureCompensation = ExposureCompensation.EC0_3;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)exposureCompensation));
```

<a name="iso-sensitivity-value"></a>
## ISO Sensitivity Value

Use the [`ISO`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/ISO.html) class to set and get ISO sensitivity values.

### Get the Current Value & List Available Values

The following example shows how to get current ISO sensitivity value using `ISO` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `ISO` class to get a list of available ISO sensitivity values.

```java
ISO iso = new ISO();
Response response =
    cameraDevice.getCaptureSettings(Arrays.asList((CaptureSetting)iso));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = iso.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set ISO sensitivity value to ISO400 using `ISO` object.

```java
ISO iso = ISO.ISO400;
Response response =
    cameraDevice.setCaptureSettings(Arrays.asList((CaptureSetting)iso));
```

<a name="white-balance"></a>
## White Balance

Use the [`WhiteBalance`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/WhiteBalance.html) class to set and get white balance values.

### Get the Current Value & List Available Values

The following example shows how to get current white balance value using `WhiteBalance` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `WhiteBalance` class to get a list of available white balance values.

```java
WhiteBalance whiteBalance = new WhiteBalance();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)whiteBalance));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = whiteBalance.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set white balance to daylight using `WhiteBalance` object.

```java
WhiteBalance whiteBalance = WhiteBalance.DAYLIGHT;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)whiteBalance));
```

<a name="shutter-speed"></a>
## Shutter Speed

Use the [`ShutterSpeed`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/ShutterSpeed.html) class to set and get shutter speed values.

### Get the Current Value & List Available Values

The following example shows how to get current shutter speed value using `ShutterSpeed` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `ShutterSpeed` class to get a list of available shutter speed values.

```java
ShutterSpeed shutterSpeed = new ShutterSpeed();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)shutterSpeed));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = shutterSpeed.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set shutter speed to 1/80 using `ShutterSpeed` object.

```java
ShutterSpeed shutterSpeed = ShutterSpeed.SS1_80;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)shutterSpeed));
```

<a name="custom-image"></a>
## Custom Image

Use the [`CustomImage`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CustomImage.html) class to set and get custom image values.

### Get the Current Value & List Available Values

The following example shows how to get current custom image value using `CustomImage` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `CustomImage` class to get a list of available custom image values.

```java
CustomImage customImage = new CustomImage();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)customImage));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = customImage.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set custom image to landscape using `CustomImage` object.

```java
CustomImage customImage = CustomImage.LANDSCAPE;
Response response =
    cameraDevice.setCaptureSettings(Arrays.asList((CaptureSetting)customImage));
```

<a name="digital-filter"></a>
## Digital Filter

Use the [`DigitalFilter`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/DigitalFilter.html) class to set and get digital filter values.

### Get the Current Value & List Available Values

The following example shows how to get current digital filter value using `DigitalFilter` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `DigitalFilter` class to get a list of available digital filter values.

```java
DigitalFilter digitalFilter = new DigitalFilter();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)digitalFilter));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList = digitalFilter.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set digital filter to toy camera using `DigitalFilter` object.

```java
DigitalFilter digitalFilter = DigitalFilter.TOY_CAMERA;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)digitalFilter));
```

<a name="still-image-capture-format"></a>
## Still Image Capture Format

Use the [`StillImageCaptureFormat`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/StillImageCaptureFormat.html) class to set and get still image capture format values.

### Get the Current Value & List Available Values

The following example shows how to get current still image capture format value using `StillImageCaptureFormat` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `StillImageCaptureFormat` class to get a list of available still image capture format values.

```java
StillImageCaptureFormat stillImageCaptureFormat = new StillImageCaptureFormat();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)stillImageCaptureFormat));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList =
        stillImageCaptureFormat.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set still image capture format to RAW(DNG) using `StillImageCaptureFormat` object.

```java
StillImageCaptureFormat stillImageCaptureFormat = StillImageCaptureFormat.DNG;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)stillImageCaptureFormat));
```

<a name="still-imag-quality"></a>
## Still Image Quality

Use the [`StillImageQuality`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/StillImageQuality.html) class to set and get still image quality values.

### Get the Current Value & List Available Values

The following example shows how to get current still image quality value using `StillImageQuality` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `StillImageQuality` class to get a list of available still image quality values.

```java
StillImageQuality stillImageQuality = new StillImageQuality();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)stillImageQuality));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList =
        stillImageQuality.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set still image quality to "Recorded Pixels":Large and "Quality Level":Best using `StillImageQuality` object.

```java
StillImageQuality stillImageQuality = StillImageQuality.LARGE_BEST;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)stillImageQuality));
```

<a name="movie-capture-format"></a>
## Movie Capture Format

Use the [`MovieCaptureFormat`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/MovieCaptureFormat.html) class to set and get movie capture format values.

### Get the Current Value & List Available Values

The following example shows how to get current movie capture format value using `MovieCaptureFormat` object.
Use the [`getAvailableSettings`](../../api_reference/com/ricoh/camera/sdk/wireless/api/setting/capture/CaptureSetting.html#getAvailableSettings--) method of the `MovieCaptureFormat` class to get a list of available movie capture format values.

```java
MovieCaptureFormat movieCaptureFormat = new MovieCaptureFormat();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting)movieCaptureFormat));
if (response.getResult() == Result.OK) {
    List<CaptureSetting> availableList =
        movieCaptureFormat.getAvailableSettings();
}
```

### Set Value

The following example demonstrates how to set movie capture format to "Still Image Size":FullHD and "Frame Rate":60i using `MovieCaptureFormat` object.

```java
MovieCaptureFormat movieCaptureFormat = MovieCaptureFormat.FULLHD60I;
Response response =
    cameraDevice.setCaptureSettings(
        Arrays.asList((CaptureSetting)movieCaptureFormat));
```
