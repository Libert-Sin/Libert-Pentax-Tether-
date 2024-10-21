---
layout: docs
---

# Connection

This page shows you how to make connections with cameras using RICOH Camera Wireless SDK.

### Contents

* [Related Classes](#related-classes)
* [How to Detect Cameras](#detect-cameras)
* [How to Connect to Camera](#connect-to-camera)
* [How to Disconnect from Camera](#disconnect-from-camera)
* [How to Check Connection Status](#check-connection-status)
* [See Also](#see-also)

<a name="related-classes"></a>
## Related Classes

The following interfaces and classes are used when you make a connection with cameras:

#### [`CameraDeviceDetector`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDeviceDetector.html)

* The `CameraDeviceDetector` class includes methods for detecting camera devices which are connected with Wireless LANs.

#### [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html)

* The `CameraDevice` interface represents a camera device.
* This interface includes methods for obtaining camera information and operating camera device.

<a name="detect-cameras"></a>
## Detect Cameras

Use the [`detect`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDeviceDetector.html#detect-com.ricoh.camera.sdk.wireless.api.DeviceInterface-) to obtain a list of connectable camera devices.     
To connect to a camera, you need to detect connectable camera devices first.

```java
CameraDevice cameraDevice = null;
List<CameraDevice> detectedDevices =
    CameraDeviceDetector.detect(DeviceInterface.WLAN);
if (detectedDevices.isEmpty() == false) {
    cameraDevice = detectedDevices.get(0);
}
```

<a name="connect-to-camera"></a>
## Connect to Camera

Use the [`connect`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#connect-com.ricoh.camera.sdk.wireless.api.DeviceInterface-) method to connect to one of the detected camera devices.

```java
Response response = cameraDevice.connect(DeviceInterface.WLAN);
```

<a name="disconnect-from-camera"></a>
## Disconnect from Camera

Use the [`disconnect`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#disconnect-com.ricoh.camera.sdk.wireless.api.DeviceInterface-) method to disconnect from the camera.

```java
Response response = cameraDevice.disconnect(DeviceInterface.WLAN);
```

<a name="check-connection-status"></a>
## Check Connection Status

Use the [`isConnected`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#isConnected-com.ricoh.camera.sdk.wireless.api.DeviceInterface-) method to check connection status of the camera.

```java
boolean result = cameraDevice.isConnected(DeviceInterface.WLAN);
```

<a name="see-also"></a>
## See Also

* [Events](./events.md) - Describes events such as receiving camera event notifications.
