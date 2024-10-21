---
layout: docs
---

# Camera Information

This page shows you how to obtain camera information using RICOH Camera Wireless SDK.

### Contents

* [Related Classes](#related-classes)
* [How to Obtain Camera Infomation](#obtain-camera-information)
* [How to Obtain Camera Status](#obtain-camera-status)

<a name="related-classes"></a>
## Related Classes

The following interfaces and classes are used when you obtain camera information:

#### [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html)

* The `CameraDevice` protocol represents a camera device.
* This protocol includes methods for obtaining camera information and operating camera device.

#### [`CameraStatus`](../../api_reference/Protocols/CameraStatus.html)

* The `CameraStatus` protocol represents camera status.

<a name="obtain-camera-information"></a>
## Obtain Camera Information

Use `CameraDevice`'s methods to obtain camera information such as model name and firmware version.

```swift
// Manufacturer Name
let manufacturer = cameraDevice.manufacturer
// Model Name
let model = cameraDevice.model
// Firmware Version
let firmwareVersion = cameraDevice.firmwareVersion
// Serial Number
let serialNumber = cameraDevice.serialNumber
```

<a name="obtain-camera-status"></a>
## Obtain Camera Status

Use `CameraStatus`'s method to obtain camera status such as charge level.

```swift
// Battery Level(%)
let batteryLevel = cameraDevice.status.batteryLevel
```
