---
layout: docs
---

# Live View

This page shows how to obtain live view of connected camera devices using RICOH Camera Wireless SDK.

### Contents

* [Related Classes](#related-classes)
* [How to Set up Live View Event](#set-up-live-view-event)
* [How to Start Live View](#start-live-view)
* [How to Stop Live View](#stop-live-view)
* [See Also](#see-also)

<a name="related-classes"></a>
## Related Classes

The following protocols and classes are used when you obtain live view:

#### [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html)

* The `CameraEventListener` protocol is a event listener which receives notifications from camera devices.

#### [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html)

* The `CameraDevice` protocol represents a camera device.
* This protocol include methods for obtaining camera information and operating camera device.

<a name="set-up-live-view-event"></a>
## Set up Live View Event

To start live view, override the virtual method [`liveViewFrameUpdated`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP20liveViewFrameUpdatedyAA0D6Device_p6sender_10Foundation4DataV0ghI0tF) included in the `CameraEventListener` protocol with the user-defined derived class.     
JPEG data is streamed all the time, which keeps calling `liveViewFrameUpdated`.

```swift
class UserEventListener: CameraEventListener {
    // Display liveViewFrame in imageView
    func liveViewFrameUpdated(sender: CameraDevice, liveViewFrame: Data) {
        let image = UIImage(data: liveViewFrame)
        imageView.image = image
    }
}
```

<a name="start-live-view"></a>
## Start Live View

Use the [`startLiveView`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP13startLiveViewAA8ResponseCyF) to start live view.

```swift
let cameraEventListener = EventListener()
cameraDevice.addEventListener(listener: cameraEventListener)
let response = cameraDevice.startLiveView()
```

<a name="stop-live-view"></a>
## Stop Live View

Use the [`stopLiveView`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP12stopLiveViewAA8ResponseCyF) to stop live view.

```swift
let response = cameraDevice.stopLiveView()
```

<a name="see-also"></a>
## See Also

* [Events](./events.md) - Describes events such as `liveViewFrameUpdated`.
