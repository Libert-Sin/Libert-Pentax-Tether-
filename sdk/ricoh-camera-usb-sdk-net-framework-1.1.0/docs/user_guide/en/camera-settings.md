---
layout: docs
---

# Camera Settings

This page describes how to set and get various camera settings of connected camera device using RICOH Camera USB SDK.

### Contents

* [Core Class](#core-class)

**Supported Camera Settings**

* [Date and Time](#date-and-time)
* [Live View Specification](#live-view-specification)

## Core Class

The core class of camera settings is:

#### [`CameraDeviceSetting`](../../api_reference/html/T_Ricoh_CameraController_CameraDeviceSetting.htm)

* The `CameraDeviceSetting` class represents various camera settings of the connected camera device.
* Supported camera settings are provided as derived class of the `CameraDeviceSetting` class.


## Date and Time

Use the [`CameraTime`](../../api_reference/html/T_Ricoh_CameraController_CameraTime.htm) class to set and get date and time of connected camera.

### Get the Current Value

The following example demonstrates how to get the current date and time of connected camera and `System.DateTime` object of the same value.

```csharp
CameraTime cameraTime = new CameraTime();
cameraDevice.GetCameraDeviceSettings(
    new List<CameraDeviceSetting>() { cameraTime });
CameraTimeValue cameraTimeValue = (CameraTimeValue)cameraTime.Value;
DateTime cameraDateTime = cameraTimeValue.Get();
```

If you want to obtain a `System.DateTime` object from the acquired [`CameraTime`](../../api_reference/html/T_Ricoh_CameraController_CameraTime.htm),
 use the [`Get`](../../api_reference/html/M_Ricoh_CameraController_CameraTimeValue_Get.htm)
 method of [`CameraTimeValue`](../../api_reference/html/T_Ricoh_CameraController_CameraTimeValue.htm),
 which stores the actual value in the [`CameraTime`](../../api_reference/html/T_Ricoh_CameraController_CameraTime.htm) object.

### Set Value

The following example demonstrates how to set camera device's date and time.

```csharp
CameraTime cameraTime = new CameraTime(DateTime.Now);
cameraDevice.SetCameraDeviceSettings(
    new List<CameraDeviceSetting>() { cameraTime });
```

You can use the `CameraTime` class and the [`SetCameraDeviceSettings`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_SetCameraDeviceSettings.htm) method to set a specified `DateTime` object.

## Live View Specification

Use the [`LiveViewSpecification`](../../api_reference/html/T_Ricoh_CameraController_LiveViewSpecification.htm) class to get current live view specification.

### Get Value

The following example demonstrates how to get live view specification.

```csharp
LiveViewSpecification liveViewSpecification = new LiveViewSpecification();
cameraDevice.GetCameraDeviceSettings(
    new List<CameraDeviceSetting>() { liveViewSpecification }); ;
LiveViewSpecificationValue liveViewSpecificationValue =
    (LiveViewSpecificationValue)liveViewSpecification.Value;
LiveViewImage liveViewImage = liveViewSpecificationValue.Get();
// Image: 720x480,
// FocusArea:
//     (0.1, 0.166666), (0.9, 0.166666),
//     (0.1, 0.833333), (0.9, 0.833333)
```

If you want to obtain a [`LiveViewImage`](../../api_reference/html/T_Ricoh_CameraController_LiveViewImage.htm) object
 from the acquired [`LiveViewSpecification`](../../api_reference/html/T_Ricoh_CameraController_LiveViewSpecification.htm),
 use the [`Get`](../../api_reference/html/M_Ricoh_CameraController_LiveViewSpecificationValue_Get.htm) method of
 [`LiveViewSpecificationValue`](../../api_reference/html/T_Ricoh_CameraController_LiveViewSpecificationValue.htm),
 which stores the actual value in the [`LiveViewSpecification`](../../api_reference/html/T_Ricoh_CameraController_LiveViewSpecification.htm) object.

[`LiveViewImage`](../../api_reference/html/T_Ricoh_CameraController_LiveViewImage.htm) object has height and width size of the live view image and the range where focus position can be specified on the live view image.
[`FocusArea`](../../api_reference/html/F_Ricoh_CameraController_LiveViewImage_FocusArea.htm) represents the upper left corner of the live view image as (0.0, 0.0), the upper right as (1.0, 0.0), the lower left as (0.0, 1.0), and the lower right as (1.0, 1.0).
The inside of the area represented by each point included in [`FocusArea`](../../api_reference/html/F_Ricoh_CameraController_LiveViewImage_FocusArea.htm) is the position specifiable range.

This information is used by [`Focus with Specified Position`](live-view.md).