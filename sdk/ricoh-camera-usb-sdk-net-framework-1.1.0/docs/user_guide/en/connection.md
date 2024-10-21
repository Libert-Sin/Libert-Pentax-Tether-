---
layout: docs
---

# Connection

This page shows you how to make connections with cameras using RICOH Camera USB SDK.

### Contents

* [Related Classes](#related-classes)
* [How to Detect Cameras](#detect-cameras)
* [How to Connect to Camera](#connect-to-camera)
* [How to Disconnect from Camera](#disconnect-from-camera)
* [How to Check Connection Status](#check-connection-status)
* [See Also](#see-also)

## Related Classes

The following classes are used when you make a connection with cameras:

#### [`CameraDeviceDetector`](../../api_reference/html/T_Ricoh_CameraController_CameraDeviceDetector.htm)

* The `CameraDeviceDetector` class includes methods for detecting camera devices which are connected with USBs.

#### [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm)

* The `CameraDevice` class represents a camera device.
* This class includes properties and methods for obtaining camera information and operating camera device.

## Detect Cameras

Use the [`Detect`](../../api_reference/html/M_Ricoh_CameraController_CameraDeviceDetector_Detect.htm) to obtain a list of connectable camera devices.  
To connect to a camera, you need to detect connectable camera devices first.

```csharp
DeviceInterface deviceInterface = DeviceInterface.USB;
List<CameraDevice> detectedCameraDevices =
    CameraDeviceDetector.Detect(deviceInterface);
```

## Connect to Camera

Use the  [`Connect`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_Connect.htm) method to connect to one of the detected camera devices.

```csharp
CameraDevice cameraDevice = detectedCameraDevices.First();
Response response = cameraDevice.Connect(deviceInterface);
```

## Disconnect from Camera

Use the [`Disconnect`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_Disconnect.htm) method to disconnect from the camera.

```csharp
Response response = cameraDevice.Disconnect(deviceInterface);
```

## Check Connection Status

Use the [`IsConnected`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_IsConnected.htm) method to check connection status of the camera.

```csharp
bool isConnected = cameraDevice.IsConnected(deviceInterface);
```

## See Also

* [Events](events.md) - Describes events such as receiving camera event notifications.
