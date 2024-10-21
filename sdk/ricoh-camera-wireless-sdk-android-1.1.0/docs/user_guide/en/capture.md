---
layout: docs
---

# Capture

This page shows how to capture images with connected camera device using RICOH Camera Wireless SDK.
RICOH Camera Wireless SDK supports single frame shooting.

Depending on the model, there are some captures not supported.
When not supported capture is started, `UnsupportedOperationException` is thrown.

### Contents

* [Related Classes](#related-classes)
* [How to Capture Still Images](#capture-still-images)
* [How to Focus and Capture Still Images](#focus-and-capture-still-images)
* [How to Check Capture Status](#check-capture-status)
* [How to Obtain Captured Image](#obtain-captured-image)
* [See Also](#see-also)

<a name="related-classes"></a>
## Related Classes

The following interfaces and classes are used when you capture images:

#### [`CameraImage`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraImage.html)

* The `CameraImage` interface represents image information and image data.
* This interface includes methods for obtaining image information and image data.

#### [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html)

* The `CameraEventListener` class is a event listener which receives notifications from camera devices.

<a name="capture-still-images"></a>
## Capture Still Images

Use the [`startCapture`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#startCapture-boolean-) method with `CaptureMethod.STILL_IMAGE` to capture still image with connected camera device.

```java
CaptureMethod captureMethod = new CaptureMethod();
Response response =
    cameraDevice.getCaptureSettings(
        Arrays.asList((CaptureSetting) captureMethod));
if (response.getResult() == Result.ERROR) {
    return;
}

if (CaptureMethod.STILL_IMAGE.equals(captureMethod) == false) {
    List<CaptureSetting> availableList = captureMethod.getAvailableSettings();
    if (availableList.contains(CaptureMethod.STILL_IMAGE)) {
        response = cameraDevice.setCaptureSettings(
            Arrays.asList((CaptureSetting) CaptureMethod.STILL_IMAGE));
        if (response.getResult() == Result.ERROR) {
            return;
        }
    } else {
        System.out.println("Capturing StillImage is not available.");
        return;
    }
}

StartCaptureResponse startCaptureResponse = null;
try {
    startCaptureResponse = cameraDevice.startCapture(true);
} catch (UnsupportedOperationException e) {
    System.out.println("Capturing StillImage is not supported.");
    return;
}

if (startCaptureResponse.getResult() == Result.OK) {
    System.out.printf("Capturing StillImage has started. Capture ID: %s%n",
        startCaptureResponse.getCapture().getId());
} else {
    System.out.printf("Capturing StillImage is FAILED. detail: %s%n",
        startCaptureResponse.getErrors().get(0).getMessage());
}
```

<a name="focus-and-capture-still-images"></a>
## Focus and Capture Still Images

If you want to capture images after focusing, use the [`focus`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#focus--) method and the [`startCapture`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#startCapture-boolean-) method with the withFocus argument false.

```java
Response response = null;
try {
    response = cameraDevice.focus();
} catch (UnsupportedOperationException e) {
    System.out.println("Focus is not supported.");
    return;
}

if (response.getResult() == Result.OK) {
    System.out.println("Focus has started.");
} else {
    System.out.printf("Focus is FAILED. detail: %s%n",
        response.getErrors().get(0).getMessage());
    return;
}

CaptureMethod captureMethod = new CaptureMethod();
response = cameraDevice.getCaptureSettings(
    Arrays.asList((CaptureSetting) captureMethod));
if (response.getResult() == Result.ERROR) {
    return;
}

if (CaptureMethod.STILL_IMAGE.equals(captureMethod) == false) {
    List<CaptureSetting> availableList = captureMethod.getAvailableSettings();
    if (availableList.contains(CaptureMethod.STILL_IMAGE)) {
        response = cameraDevice.setCaptureSettings(
            Arrays.asList((CaptureSetting) CaptureMethod.STILL_IMAGE));
        if (response.getResult() == Result.ERROR) {
            return;
        }
    } else {
        System.out.println("Capturing StillImage is not available.");
        return;
    }
}

StartCaptureResponse startCaptureResponse = null;
try {
    startCaptureResponse = cameraDevice.startCapture(false);
} catch (UnsupportedOperationException e) {
    System.out.println("Capturing StillImage is not supported.");
    return;
}

if (startCaptureResponse.getResult() == Result.OK) {
    System.out.printf("Capturing StillImage has started. Capture ID: %s%n",
        startCaptureResponse.getCapture().getId());
} else {
    System.out.printf("Capturing StillImage is FAILED. detail: %s%n",
        startCaptureResponse.getErrors().get(0).getMessage());
}
```

<a name="check-capture-status"></a>
## Check Capture Status

Use the [`CaptureState`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CaptureState.html) returned the [`getState`](../../api_reference/com/ricoh/camera/sdk/wireless/api/Capture.html#getState--) method of the [`Capture`](../../api_reference/com/ricoh/camera/sdk/wireless/api/Capture.html) interface to check capture status.
One of the supported values of the status is `Complete`, which means the camera device has finished capturing.

You can obtain the `Capture` class with the three ways below:

* Use the [`StartCaptureResponse`](../../api_reference/com/ricoh/camera/sdk/wireless/api/response/StartCaptureResponse.html) class

The `StartCaptureResponse` class explained in sections above provides a `Capture` called [`getCapture`](../../api_reference/com/ricoh/camera/sdk/wireless/api/response/StartCaptureResponse.html#getCapture--).

```java
StartCaptureResponse startCaptureResponse = cameraDevice.startCapture(false);
if (startCaptureResponse.getResult() == Result.OK) {
    Capture capture = startCaptureResponse.getCapture();
    System.out.printf("Capture state is %s%n", capture.getState());
}
```

* Use the [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html) class

The `CameraDevice` class has a [`CameraStatus`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStatus.html) called [`getStatus`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getStatus--).
The `CameraStatus` class provides a `Capture` called [`getCurrentCapture`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStatus.html#getCurrentCapture--).

```java
Capture currentCapture = cameraDevice.getStatus().getCurrentCapture();
if (currentCapture != null) {
    System.out.printf("Capture state is %s%n", currentCapture.getState());
}
```

* Use the [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html) class

The [`captureComplete`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#captureComplete-com.ricoh.camera.sdk.wireless.api.CameraDevice-com.ricoh.camera.sdk.wireless.api.Capture-) method of the [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html) class is invoked when the camera device has finished capturing.

```java
class UserEventListener extends CameraEventListener {
    // Capture Complete
    @Override
    public void captureComplete(CameraDevice sender, Capture capture) {
        System.out.printf("Capture Complete. Caputure ID: %s%n", capture.getId());
    }
}
```

<a name="obtain-captured-image"></a>
## Obtain Captured Image

Use the [`imageStored`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html#imageStored-com.ricoh.camera.sdk.wireless.api.CameraDevice-com.ricoh.camera.sdk.wireless.api.CameraImage-) method of the [`CameraEventListener`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraEventListener.html) class to obtain the image you captured.

```java
class UserEventListener extends CameraEventListener {
    // Image Stored
    @Override
    public void imageStored(CameraDevice sender, CameraImage image) {
        System.out.printf("Image Stored. Image Name: %s%n", image.getName());
    }
}
```

<a name="see-also"></a>
## See Also

* [Image Handling](./image-handling.md) - Demonstrates image acquisition.
* [Capture Settings](./capture-settings.md) - Describes available capture settings such as capturing without saving data on camera storage.
* [Events](./events.md) - Describes events such as `captureComplete` and `imageStored`.
