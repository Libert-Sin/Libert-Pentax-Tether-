---
layout: docs
---

# Camera Information

This page shows you how to obtain camera information using RICOH Camera USB SDK.

### Contents

* [Related Classes](#related-classes)
* [How to Obtain Camera Infomation](#obtain-camera-information)
* [How to Obtain Camera Status](#obtain-camera-status)

## Related Classes

The following classes are used when you obtain camera information:

#### [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm)

* The `CameraDevice` class represents a camera device.
* This class includes properties and methods for obtaining camera information and operating camera device.

#### [`CameraStatus`](../../api_reference/html/T_Ricoh_CameraController_CameraStatus.htm)

* The `CameraStatus` class represents camera status.

## Obtain Camera Information

Use `CameraDevice`'s properties to obtain camera information such as model name and firmware version.

```csharp
// Manufacturer Name
string manufacturer = cameraDevice.Manufacturer;
// Model Name
string model = cameraDevice.Model;
// Firmware Version
string firmwareVersion = cameraDevice.FirmwareVersion;
// Serial Number
string serialNumber = cameraDevice.SerialNumber;
```

## Obtain Camera Status

Use `CameraStatus`'s properties to obtain camera status such as charge level.

```csharp
// Battery Level(%)
//     100(FULL), 67, 33, 0
uint batteryLevel = cameraDevice.Status.BatteryLevel;
```
