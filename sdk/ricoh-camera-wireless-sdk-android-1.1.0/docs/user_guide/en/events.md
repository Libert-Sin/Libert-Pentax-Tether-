---
layout: docs
---

# Events

You can receive camera events of the connected camera using the [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html) class.

### Contents

* [Supported Camera Events](#supported-camera-events)
* [How to Register Events](#register-events)
* [See Also](#see-also)

<a name="supported-camera-events"></a>
## Supported Camera Events

RICOH Camera Wireless SDK supports the following camera events:

* [`imageStored`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#imageStored-com.ricoh.camera.sdk.wireless.api.CameraDevice-com.ricoh.camera.sdk.wireless.api.CameraImage-) is called when image is stored to the camera storage.
* [`captureComplete`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#captureComplete-com.ricoh.camera.sdk.wireless.api.CameraDevice-com.ricoh.camera.sdk.wireless.api.Capture-) is called when capturing an image is completed.
* [`deviceDisconnected`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#deviceDisconnected-com.ricoh.camera.sdk.wireless.api.CameraDevice-) is called when the camera you've already made a connection is disconnected.
* [`liveViewFrameUpdated`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#liveViewFrameUpdated-com.ricoh.camera.sdk.wireless.api.CameraDevice-byte:A-) is called when live view frame data is updated.

<a name="register-events"></a>
## Register Events

Override the virtual method corresponding to the event you want to receive in your derived class of [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html).
You can also see [Live View](./live-view.md) for an example of `liveViewFrameUpdated`.

```java
class UserEventListener extends CameraEventListener {
    @Override
    public void imageStored(CameraDevice sender, CameraImage image) {
        System.out.printf("Image Stored. Name: %s%n", image.getName());
    }

    @Override
    public void captureComplete(CameraDevice sender, Capture capture) {
        System.out.printf("Capture Complete. Caputure ID: %s%n", capture.getId());
    }

    @Override
    public void deviceDisconnected(CameraDevice sender) {
        System.out.println("Device Disconnected.");
    }
}
```

And register the event listener as follows:

```java
CameraEventListener userEventListener = new UserEventListener();
cameraDevice.addEventListener(userEventListener);
```

Registered events are called when corresponding events occur at [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html).

<a name="see-also"></a>
## See Also

* [Capture](./capture.md) - Demonstrates how to capture images.
* [Connection](./connection.md) - Demonstrates how to make connections with cameras.
* [Live View](./live-view.md) - Describes `liveViewFrameUpdated`.
