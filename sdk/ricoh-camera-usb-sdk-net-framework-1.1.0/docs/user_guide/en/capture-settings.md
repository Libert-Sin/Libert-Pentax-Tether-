---
layout: docs
---

# Capture Settings

This page describes how to set and get various capture settings of connected camera device using RICOH Camera USB SDK. Note that if you set invalid value, rounded value might be set.

### Contents

* [Core Class](#core-class)
* [Basic Usage](#basic-usage)

**Supported Capture Settings**

* [Aperture Value](#aperture-value)
* [Exposure Compensation](#exposure-compensation)
* [ISO Sensitivity Value](#iso-sensitivity-value)
* [White Balance](#white-balance)
* [Shutter Speed](#shutter-speed)
* [Storage Settings](#storage-settings)
* [Still Image Quality](#still-image-quality)
* [Still Image Capture Format](#still-image-capture-format)
* [Capture Method](#capture-method)
* [Exposure Program](#exposure-program)
* [Hyper Operation Settings](#hyper-operation-settings)
* [User Capture Settings Mode](#user-capture-settings-mode)

## Core Class

The core class of capture settings is:

#### [`CaptureSetting`](../../api_reference/html/T_Ricoh_CameraController_CaptureSetting.htm)

* The `CaptureSetting` class represents various capture settings of camera devices.
* Supported capture settings are provided as derived classes of the `CaptureSetting` class.

## Basic Usage

All the supported capture settings have corresponding classes derived from the [`CaptureSetting`](../../api_reference/html/T_Ricoh_CameraController_CaptureSetting.htm) class. This section shows you how to use manage individual capture settings using the classes. You can also manage multiple capture settings at the same time. Code examples in this section show how to manage aperture value but you can manage other settings in similar ways.

### Get Individual Capture Settings

Follow the steps below to get current value of specified capture setting.

1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`GetCaptureSettings`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_GetCaptureSettings.htm) method with the object.

The following example shows how to get current aperture value using `FNumber` object

```csharp
FNumber fNumber = new FNumber();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { fNumber });
Console.WriteLine("Current Value: {0}", fNumber.ToString());
```

### List Available Setting Values

Follow the steps below to get a list of available values for individual capture settings.
1. Generate an object of the class corresponding to the capture setting you want
1. Use the [`GetCaptureSettings`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_GetCaptureSettings.htm) method with the object
1. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the object

The following example shows how to get a list of available aperture values using `FNumber` object.

```csharp
FNumber fNumber = new FNumber();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { fNumber });
List<CaptureSetting> availableFNumberSettings = fNumber.AvailableSettings;
for (var i = 0; i < availableFNumberSettings.Count; i++)
{
    Console.WriteLine("Available Setting: {0}",
        availableFNumberSettings[i].ToString());
}
```

### Set Individual Capture Setting

Follow the steps below to set value of specified capture setting.
1. Select a class and its field corresponding to the value you want to set
1. Use the [`SetCaptureSettings`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_SetCaptureSettings.htm) method with the field

The following example shows how to set the aperture value to 5.6 using `FNumber` object.

```csharp
FNumber fNumber = FNumber.F5_6;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { fNumber });
```

### Get Multiple Capture Settings

The following example shows how to get shutter speed and exposure compensation value at the same time.

```csharp
ShutterSpeed shutterSpeed = new ShutterSpeed();
ExposureCompensation exposureCompensation = new ExposureCompensation();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() {
    shutterSpeed, exposureCompensation });
```

### Set Multiple Capture Settings

The following example shows how to set shutter speed and exposure compensation value at the same time.

```csharp
ShutterSpeed shutterSpeed = ShutterSpeed.SS1_10;
ExposureCompensation exposureCompensation = ExposureCompensation.EC2_0;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() {
    shutterSpeed, exposureCompensation });
```

## Aperture Value

Use the [`FNumber`](../../api_reference/html/T_Ricoh_CameraController_FNumber.htm) class to set and get aperture values.

### Get the Current Value & List Available Values

The following example shows how to get current aperture value using `FNumber` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `FNumber` class to get a list of available aperture values.

```csharp
FNumber fNumber = new FNumber();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { fNumber });
List<CaptureSetting> availableFNumberSettings = fNumber.AvailableSettings;

// The list above might contain the following values.
// F4.0 (F4_0), F4.5 (F4_5), F5.0 (F5_0)
```

Available setting values vary depending on conditions such as lens and focal length of the camera.

### Set Value

The following example demonstrates how to set aperture value to 5.6 using `FNumber` object.

```csharp
FNumber fNumber = FNumber.F5_6;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { fNumber });
```

## Exposure Compensation

Use the [`ExposureCompensation`](../../api_reference/html/T_Ricoh_CameraController_ExposureCompensation.htm) class to set and get exposure compensation values.

### Get the Current Value & List Available Values

The following example shows how to get current exposure compensation value using `ExposureCompensation` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `ExposureCompensation` class to get a list of available exposure compensation values.

```csharp
ExposureCompensation exposureCompensation = new ExposureCompensation();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() {
    exposureCompensation });
List<CaptureSetting> availableExposureCompensationSettings =
    exposureCompensation.AvailableSettings;

// The list above might contain the following values in case you set EV step to 1/3.
// -5EV (ECNegative5_0), -4.7EV (ECNegative4_7), -4.3EV (ECNegative4_3),
// ...
// -1EV (ECNegative1_0), -0.7EV (ECNegative0_7), -0.3EV (ECNegative0_3),
// 0 (EC0_0),
// +0.3EV (EC0_3), +0.7EV (EC0_7), +1EV (EC1_0),
// ...
// +4.3EV (EC4_3), +4.7EV (EC4_7), +5EV (EC5_0),
```

### Set Value

The following example demonstrates how to set exposure compensation to +0.3EV using `ExposureCompensation` object.

```csharp
ExposureCompensation exposureCompensation = ExposureCompensation.EC0_3;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() {
    exposureCompensation });
```

## ISO Sensitivity Value

Use the [`ISO`](../../api_reference/html/T_Ricoh_CameraController_ISO.htm) class to set and get ISO sensitivity values.

### Get the Current Value & List Available Values

The following example shows how to get current ISO sensitivity value using `ISO` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `ISO` class to get a list of available ISO sensitivity values.

```csharp
ISO iso = new ISO();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { iso });
List<CaptureSetting> availableISOSettings = iso.AvailableSettings;

// The list above might contain the following values in case you set program automatic exposure mode.
// automatic ISO setting (Auto),
// 100 (ISO100), 200 (ISO200), ... 102400 (ISO102400), 204800 (ISO204800)
```

Available setting values vary depending on conditions such as exposure mode of the camera.

### Set Value

The following example demonstrates how to set ISO sensitivity value to ISO400 using `ISO` object.

```csharp
ISO iso = ISO.ISO400;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { iso });
```

## White Balance

Use the [`WhiteBalance`](../../api_reference/html/T_Ricoh_CameraController_WhiteBalance.htm) class to set and get white balance values.

### Get the Current Value & List Available Values

The following example shows how to get current white balance value using `WhiteBalance` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `WhiteBalance` class to get a list of available white balance values.

```csharp
WhiteBalance whiteBalance = new WhiteBalance();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { whiteBalance });
List<CaptureSetting> availableWhiteBalanceSettings =
    whiteBalance.AvailableSettings;

// The list above might contain the following values.
// Auto White Balance (Auto), Daylight (Daylight), Cloudy (Cloud),
// ...
// Manual White Balance Setting 1 (Manual),
// Manual White Balance Setting 2 (Manual2),
// Manual White Balance Setting 3 (Manual3)
```

### Set Value

The following example demonstrates how to set white balance to daylight using `WhiteBalance` object.

```csharp
WhiteBalance whiteBalance = WhiteBalance.Daylight;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { whiteBalance });
```

## Shutter Speed

Use the [`ShutterSpeed`](../../api_reference/html/T_Ricoh_CameraController_ShutterSpeed.htm) class to set and get shutter speed values.

### Get the Current Value & List Available Values

The following example shows how to get current shutter speed value using `ShutterSpeed` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `ShutterSpeed` class to get a list of available shutter speed values.

```csharp
ShutterSpeed shutterSpeed = new ShutterSpeed();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { shutterSpeed });
List<CaptureSetting> availableShutterSpeedSettings =
    shutterSpeed.AvailableSettings;

// The list above might contain the following values
//      in case you set shutter priority automatic exposure mode and set EV step to 1/3.
// 1/8000 (SS1_8000), 1/6400 (SS1_6400), ...
// 0.3" (SS3_10), 0.4" (SS4_10), ...
// 1" (SS1), 1.3" (SS13_10), ...
// 25" (SS25), 30" (SS30)
```

Available setting values vary depending on conditions such as exposure mode of the camera.

### Set Value

The following example demonstrates how to set shutter speed to 1/80 using `ShutterSpeed` object.

```csharp
ShutterSpeed shutterSpeed = ShutterSpeed.SS1_80;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { shutterSpeed });
```

## Storage Settings

Use the [`StorageWriting`](../../api_reference/html/T_Ricoh_CameraController_StorageWriting.htm) class to set and get storage settings. There are only 2 available values: to save and not to save shooting results.

### Get the Current Value & List Available Values

The following example shows how to get current storage setting value using `StorageWriting` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `StorageWriting` class to get a list of available storage setting values.

If you want to obtain a bool object from the acquired [`StorageWriting`](../../api_reference/html/T_Ricoh_CameraController_StorageWriting.htm), use the [`Get`](../../api_reference/html/M_Ricoh_CameraController_StorageWritingValue_Get.htm) method of [`StorageWritingValue`](../../api_reference/html/T_Ricoh_CameraController_StorageWritingValue.htm), which stores the actual value in the [`StorageWriting`](../../api_reference/html/T_Ricoh_CameraController_StorageWriting.htm) object.

```csharp
// save(true), not save(false)
StorageWriting storageWriting = new StorageWriting();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { storageWriting });
List<CaptureSetting> availableStorageWritingSettings =
    storageWriting.AvailableSettings;
StorageWritingValue storageWritingValue =
    (StorageWritingValue)storageWriting.Value;
bool isStorageWriting = storageWritingValue.Get();
```

### Set Value

The following example demonstrates how to disable the saving option using `StorageWriting` object.

```csharp
StorageWriting storageWriting = new StorageWriting(false);
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { storageWriting });
```

## Still Image Quality

Use the [`StillImageQuality`](../../api_reference/html/T_Ricoh_CameraController_StillImageQuality.htm) class to set and get still image quality values.

### Get the Current Value & List Available Values

The following example shows how to get current still image quality value using `StillImageQuality` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `StillImageQuality` class to get a list of available still image quality values.

```csharp
StillImageQuality stillImageQuality = new StillImageQuality();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { stillImageQuality });
List<CaptureSetting> availableStillImageQualitySettings =
    stillImageQuality.AvailableSettings;

// The list above might contain the following values
// Large Image Size, Best Image Quality (LargeBest),
// Large Image Size, Better Image Quality (LargeBetter),
// Large Image Size, Good Image Quality (LargeGood),
// ...
// Extra Small Image Size, Good Image Quality (ExtraSmallGood)
```

### Set Value

The following example demonstrates how to set still image quality value to LargeBest using `StillImageQuality` object.

```csharp
StillImageQuality stillImageQuality = StillImageQuality.LargeBest;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() { stillImageQuality });
```

## Still Image Capture Format

Use the [`StillImageCaptureFormat`](../../api_reference/html/T_Ricoh_CameraController_StillImageCaptureFormat.htm) class to set and get still image capture format values.

### Get the Current Value & List Available Values

The following example shows how to get current still image capture format value using `StillImageCaptureFormat` object. Use the [`AvailableSettings`](../../api_reference/html/P_Ricoh_CameraController_CaptureSetting_AvailableSettings.htm) property of the `StillImageCaptureFormat` class to get a list of available still image capture format values.

```csharp
StillImageCaptureFormat stillImageCaptureFormat = new StillImageCaptureFormat();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() {
    stillImageCaptureFormat });
List<CaptureSetting> availableStillImageCaptureFormatSettings =
    stillImageCaptureFormat.AvailableSettings;

// The list above might contain the following values
// JPEG, PEF, DNG , PEF and JPEG, ...
```

### Set Value

The following example demonstrates how to set still image capture format value to PEF using `StillImageCaptureFormat` object.

```csharp
StillImageCaptureFormat stillImageCaptureFormat = StillImageCaptureFormat.PEF;
cameraDevice.SetCaptureSettings(new List<CaptureSetting>() {
    stillImageCaptureFormat });
```

## Capture Method

Use the [`CaptureMethod`](../../api_reference/html/T_Ricoh_CameraController_CaptureMethod.htm) class to get capture method values.
Capture method get only.

### Get the Current Value

The following example shows how to get current capture method value using `CaptureMethod` object.

```csharp
CaptureMethod captureMethod = new CaptureMethod();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { captureMethod });

// Still Image, Movie
```

## Exposure Program

Use the [`ExposureProgram`](../../api_reference/html/T_Ricoh_CameraController_ExposureProgram.htm) class to get exposure program values.
Exposure program get only.

### Get the Current Value

The following example shows how to get current exposure program value using `ExposureProgram` object.

```csharp
ExposureProgram exposureProgram = new ExposureProgram();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() { exposureProgram });

// The list above might contain the following values
// Auto, Program, ShutterSpeedPriority, ...
```

## Hyper Operation Settings

Use the [`HyperOperationEnable`](../../api_reference/html/T_Ricoh_CameraController_HyperOperationEnable.htm) class to get hyper operation settings values.
Hyper operation settings get only.
There are only 2 available values: enable and disable.

### Get the Current Value

The following example shows how to get current hyper operation settings value using `HyperOperationEnable` object.

If you want to obtain a bool object from the acquired [`HyperOperationEnable`](../../api_reference/html/T_Ricoh_CameraController_HyperOperationEnable.htm), use the [`Get`](../../api_reference/html/M_Ricoh_CameraController_HyperOperationEnableValue_Get.htm) method of [`HyperOperationEnableValue`](../../api_reference/html/T_Ricoh_CameraController_HyperOperationEnableValue.htm), which stores the actual value in the [`HyperOperationEnable`](../../api_reference/html/T_Ricoh_CameraController_HyperOperationEnable.htm) object.

```csharp
HyperOperationEnable hyperOperationEnable
     = new HyperOperationEnable();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() {
     hyperOperationEnable });
HyperOperationEnableValue hyperOperationEnableValue =
    (HyperOperationEnableValue)hyperOperationEnable.Value;
bool isHyperOperationEnableValue = hyperOperationEnableValue.Get();

// enable(true), disable(false)
```

## User Capture Settings Mode

Use the [`UserCaptureSettingsMode`](../../api_reference/html/T_Ricoh_CameraController_UserCaptureSettingsMode.htm) class to get user capture settings mode values.
User capture settings mode get only.

### Get the Current Value

The following example shows how to get current user capture settings mode value using `UserCaptureSettingsMode` object.

```csharp
UserCaptureSettingsMode userCaptureSettingsMode = new UserCaptureSettingsMode();
cameraDevice.GetCaptureSettings(new List<CaptureSetting>() {
    userCaptureSettingsMode });


// The list above might contain the following values
// Unknown, None, User, User2, User3, ...
```
