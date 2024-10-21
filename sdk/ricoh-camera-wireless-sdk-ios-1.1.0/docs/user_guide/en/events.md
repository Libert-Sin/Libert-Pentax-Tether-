---
layout: docs
---

# Events

You can receive camera events of the connected camera using the [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html) protocol.

### Contents

* [Supported Camera Events](#supported-camera-events)
* [How to Register Events](#register-events)
* [See Also](#see-also)

<a name="supported-camera-events"></a>
## Supported Camera Events

RICOH Camera Wireless SDK supports the following camera events:

* [`imageStored`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP11imageStoredyAA0D6Device_p6sender_AA0D5Image_p0G0tF) is called when image is stored to the camera storage.
* [`captureComplete`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP15captureCompleteyAA0D6Device_p6sender_AA7Capture_p0G0tF) is called when capturing an image is completed.
* [`deviceDisconnected`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP18deviceDisconnectedyAA0D6Device_p6sender_tF) is called when the camera you've already made a connection is disconnected.
* [`liveViewFrameUpdated`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP20liveViewFrameUpdatedyAA0D6Device_p6sender_10Foundation4DataV0ghI0tF) is called when live view frame data is updated.

<a name="register-events"></a>
## Register Events

Override the virtual method corresponding to the event you want to receive in your derived class of [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html).
You can also see [Live View](./live-view.md) for an example of `liveViewFrameUpdated`.

```swift
class UserEventListener: CameraEventListener {

    func imageStored(sender: CameraDevice, image: CameraImage) {
        print("Image Stored. Image Name: \(image.name)")
    }

    func captureComplete(sender: CameraDevice, capture: Capture) {
        print("Capture Complete. Caputure ID: \(capture.id)")
    }

    func deviceDisconnected(sender: CameraDevice) {
        print("Device Disconnected.")
    }
}
```

And register the event listener as follows:

```swift
let userEventListener = UserEventListener()
cameraDevice.addEventListener(listener: userEventListener)
```

Registered events are called when corresponding events occur at [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html).

<a name="see-also"></a>
## See Also

* [Capture](./capture.md) - Demonstrates how to capture images.
* [Connection](./connection.md) - Demonstrates how to make connections with cameras.
* [Live View](./live-view.md) - Describes `liveViewFrameUpdated`.
