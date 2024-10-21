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

The following protocols and classes are used when you make a connection with cameras:

#### [`CameraDeviceDetector`](../../api_reference/Classes/CameraDeviceDetector.html)

* The `CameraDeviceDetector` class includes methods for detecting camera devices which are connected with Wireless LANs.

#### [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html)

* The `CameraDevice` protocol represents a camera device.
* This protocol includes methods for obtaining camera information and operating camera device.

<a name="detect-cameras"></a>
## Detect Cameras

Use the [`detect`](../../api_reference/Classes/CameraDeviceDetector.html#/s:22RICOHCameraWirelessSDK20CameraDeviceDetectorC6detectSayAA0dE0_pGAA0E9InterfaceO06deviceH0_tFZ) to obtain a list of connectable camera devices.     
To connect to a camera, you need to detect connectable camera devices first.

```swift
let detectedDevices = CameraDeviceDetector.detect(deviceInterface: .wlan)
if detectedDevices.isEmpty {
    return
}
let cameraDevice = detectedDevices[0]
```

<a name="connect-to-camera"></a>
## Connect to Camera

Use the [`connect`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP7connectAA8ResponseCAA0E9InterfaceO06deviceH0_tF) method to connect to one of the detected camera devices.

```swift
let Response = cameraDevice.connect(deviceInterface: .wlan)
```

<a name="disconnect-from-camera"></a>
## Disconnect from Camera

Use the [`disconnect`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP10disconnectAA8ResponseCAA0E9InterfaceO06deviceH0_tF) method to disconnect from the camera.

```swift
let Response = cameraDevice.disconnect(deviceInterface: .wlan)
```

<a name="check-connection-status"></a>
## Check Connection Status

Use the [`isConnected`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP11isConnectedSbAA0E9InterfaceO06deviceH0_tF) method to check connection status of the camera.

```swift
let result = cameraDevice.isConnected(deviceInterface: .wlan)
```

<a name="see-also"></a>
## See Also

* [Events](./events.md) - Describes events such as receiving camera event notifications.
