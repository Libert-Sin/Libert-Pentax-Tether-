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

The following interfaces and classes are used when you obtain live view:

#### [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html)

* The `CameraEventListener` class is a event listener which receives notifications from camera devices.

#### [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html)

* The `CameraDevice` interface represents a camera device.
* This interface include methods for obtaining camera information and operating camera device.

<a name="set-up-live-view-event"></a>
## Set up Live View Event

To start live view, override the virtual method [`liveViewFrameUpdated`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#liveViewFrameUpdated-com.ricoh.camera.sdk.wireless.api.CameraDevice-byte:A-) included in the `CameraEventListener` class with the user-defined derived class.     
JPEG data is streamed all the time, which keeps calling `liveViewFrameUpdated`.

```java
class UserEventListener extends CameraEventListener {
    // Display liveViewFrame in imageView
    @Override
    public void liveViewFrameUpdated(
            CameraDevice sender, byte[] liveViewFrame) {
        final Bitmap bitmap =
                BitmapFactory.decodeByteArray(
                        liveViewFrame, 0, liveViewFrame.length);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
```

<a name="start-live-view"></a>
## Start Live View

Use the [`startLiveView`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#startLiveView--) to start live view.

```java
CameraEventListener cameraEventListener = new UserEventListener();
cameraDevice.addEventListener(cameraEventListener);
Response response = cameraDevice.startLiveView();
```

<a name="stop-live-view"></a>
## Stop Live View

Use the [`stopLiveView`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#stopLiveView--) to stop live view.

```java
Response response = cameraDevice.stopLiveView();
```

<a name="see-also"></a>
## See Also

* [Events](./events.md) - Describes events such as `liveViewFrameUpdated`.
