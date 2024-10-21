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

#### [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html)

* The `CameraDevice` interface represents a camera device.
* This interface includes methods for obtaining camera information and operating camera device.

#### [`CameraStatus`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStatus.html)

* The `CameraStatus` interface represents camera status.

<a name="obtain-camera-information"></a>
## Obtain Camera Information

Use `CameraDevice`'s methods to obtain camera information such as model name and firmware version.

```java
// Manufacturer Name
String manufacturer = cameraDevice.getManufacturer();
// Model Name
String model = cameraDevice.getModel();
// Firmware Version
String firmwareVersion = cameraDevice.getFirmwareVersion();
// Serial Number
String serialNumber = cameraDevice.getSerialNumber();
```

<a name="obtain-camera-status"></a>
## Obtain Camera Status

Use `CameraStatus`'s method to obtain camera status such as charge level.

```java
// Battery Level(%)
int batteryLevel = cameraDevice.getStatus().getBatteryLevel();
```
