---
layout: docs
---

# Capture

This page shows how to capture images with connected camera device using RICOH Camera Wireless SDK.
RICOH Camera Wireless SDK supports single frame shooting.

Depending on the model, there are some captures not supported.
When not supported capture is started, `CameraSDKError.unsupportedOperation` is thrown.

### Contents

* [Related Classes](#related-classes)
* [How to Capture Still Images](#capture-still-images)
* [How to Focus and Capture Still Images](#focus-and-capture-still-images)
* [How to Check Capture Status](#check-capture-status)
* [How to Obtain Captured Image](#obtain-captured-image)
* [See Also](#see-also)

<a name="related-classes"></a>
## Related Classes

The following protocols and classes are used when you capture images:

#### [`CameraImage`](../../api_reference/Protocols/CameraImage.html)

* The `CameraImage` protocol represents image information and image data.
* This protocol includes methods for obtaining image information and image data.

#### [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html)

* The `CameraEventListener` protocol is a event listener which receives notifications from camera devices.

<a name="capture-still-images"></a>
## Capture Still Images

Use the [`startCapture`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP12startCaptureAA05StartG8ResponseCSb9withFocus_tF) method with `CaptureMethod.stillImage` and the withFocus argument true to capture still image with connected camera device.

```swift
let captureMethod = CaptureMethod()
var response = cameraDevice.getCaptureSettings(settings: [captureMethod])
if response.result == Result.error {
    return
}

if captureMethod != CaptureMethod.stillImage {
    if captureMethod.availableSettings.contains(CaptureMethod.stillImage) {
        response =
            cameraDevice.setCaptureSettings(settings: [CaptureMethod.stillImage])
        if response.result == Result.error {
            return
        }
    } else {
        print("Capturing StillImage is not available.")
        return
    }
}

let startCaptureResponse: StartCaptureResponse
do {
    try startCaptureResponse = cameraDevice.startCapture(withFocus: true)
} catch {
    print("Capturing StillImage is not supported.")
    return
}

if startCaptureResponse.result == Result.ok {
    print("Capturing StillImage has started."
        + " Capture ID: \(startCaptureResponse.capture?.id)")
} else {
    print("Capturing StillImage is FAILED."
        + "detail: \(startCaptureResponse.errors[0].message)")
}
```

<a name="focus-and-capture-still-images"></a>
## Focus and Capture Still Images

If you want to capture images after focusing, use the [`focus`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP5focusAA8ResponseCyF) method and the [`startCapture`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP12startCaptureAA05StartG8ResponseCSb9withFocus_tF) method with the withFocus argument false.

```swift
var response: Response
do {
    try response = cameraDevice.focus()
} catch {
    print("Focus is not supported.")
    return
}

if response.result == Result.ok {
    print("Focus has started.")
} else {
    print("Focus is FAILED. detail: \(response.errors[0].message)")
    return
}

let captureMethod = CaptureMethod()
response = cameraDevice.getCaptureSettings(settings: [captureMethod])
if response.result == Result.error {
    return
}

if captureMethod != CaptureMethod.stillImage {
    if captureMethod.availableSettings.contains(CaptureMethod.stillImage) {
        response =
            cameraDevice.setCaptureSettings(settings: [CaptureMethod.stillImage])
        if response.result == Result.error {
            return
        }
    } else {
        print("Capturing StillImage is not available.")
        return
    }
}

let startCaptureResponse: StartCaptureResponse
do {
    try startCaptureResponse = cameraDevice.startCapture(withFocus: false)
} catch {
    print("Capturing StillImage is not supported.")
    return
}

if startCaptureResponse.result == Result.ok {
    print("Capturing StillImage has started."
        + " Capture ID: \(startCaptureResponse.capture?.id)")
} else {
    print("Capturing StillImage is FAILED."
        + " detail: \(startCaptureResponse.errors[0].message)")
}
```

<a name="check-capture-status"></a>
## Check Capture Status

Use the [`CaptureState`](../../api_reference/Enums/CaptureState.html) returned the [`state`](../../api_reference/Protocols/Capture.html#/s:22RICOHCameraWirelessSDK7CaptureP5stateAA0D5StateOv) property of the [`Capture`](../../api_reference/Protocols/Capture.html) protocol to check capture status.
One of the supported values of the status is `complete`, which means the camera device has finished capturing.

You can obtain the `Capture` class with the three ways below:

* Use the [`StartCaptureResponse`](../../api_reference/Classes/StartCaptureResponse.html) class

The `StartCaptureResponse` class explained in sections above provides a `Capture` called [`capture`](../../api_reference/Classes/StartCaptureResponse.html#/s:22RICOHCameraWirelessSDK20StartCaptureResponseC7captureAA0E0_pSgv) property.

```swift
let startCaptureResponse = cameraDevice.startCapture(withFocus: false)
if startCaptureResponse.result == Result.ok {
    let capture = startCaptureResponse.capture
    print("Capture state is \(capture?.state)");
}
```

* Use the [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html) protocol

The `CameraDevice` class has a [`CameraStatus`](../../api_reference/Protocols/CameraStatus.html) called [`status`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP6statusAA0D6Status_pv--) property.
The `CameraStatus` class provides a `Capture` called [`currentCapture`](../../api_reference/Protocols/CameraStatus.html#/s:22RICOHCameraWirelessSDK12CameraStatusP14currentCaptureAA0G0_pSgv) property.

```swift
if let currentCapture = cameraDevice.status.currentCapture {
    print("Capture state is \(currentCapture.state)");
}
```

* Use the [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html) protocol

The [`captureComplete`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP15captureCompleteyAA0D6Device_p6sender_AA7Capture_p0G0tF) method of the [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html) class is invoked when the camera device has finished capturing.

```swift
class UserEventListener: CameraEventListener {
    // Capture Complete
    func captureComplete(sender: CameraDevice, capture: Capture) {
        print("Capture Complete. Caputure ID: \(capture.id)")
    }
}
```

<a name="obtain-captured-image"></a>
## Obtain Captured Image

Use the [`imageStored`](../../api_reference/Protocols/CameraEventListener.html#/s:22RICOHCameraWirelessSDK19CameraEventListenerP11imageStoredyAA0D6Device_p6sender_AA0D5Image_p0G0tF) method of the [`CameraEventListener`](../../api_reference/Protocols/CameraEventListener.html) class to obtain the image you captured.

```swift
class UserEventListener: CameraEventListener {
    // Image Stored
    func imageStored(sender: CameraDevice, image: CameraImage) {
        print("Image Stored. Image Name: \(image.name)")
    }
}
```

<a name="see-also"></a>
## See Also

* [Image Handling](./image-handling.md) - Demonstrates image acquisition.
* [Capture Settings](./capture-settings.md) - Describes available capture settings such as capturing without saving data on camera storage.
* [Events](./events.md) - Describes events such as `captureComplete` and `imageStored`.
