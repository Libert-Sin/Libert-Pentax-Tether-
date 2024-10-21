---
layout: docs
---

# Movie Capture

This page shows how to capture movies with connected camera device using RICOH Camera USB SDK.

In order to capture movie, the following preparations are required.

* Insert the SD card in the camera body
* Operate the camera body to set the movie mode

For devices that support to capture movie, please refer to [Appendix](appendix.md).

### Contents

* [Related Classes](#related-classes)
* [How to Start Capture Movie](#start-capture-movie)
* [How to Stop Capture Movie](#stop-capture-movie)
* [See Also](#see-also)

## Related Classes

The following classes are used when you capture movie:

#### [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm)

* The `CameraDevice` class represents a camera device.
* This class includes properties and methods for obtaining camera information and operating camera device.

## Start Capture Movie

Use the [`StartCapture`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StartCapture.htm) method to start movie capturing.

```csharp
StartCaptureResponse startCaptureResponse = cameraDevice.StartCapture();
if (startCaptureResponse.Result == Result.OK)
{
    Console.WriteLine("Capturing has started. Capture ID: {0}",
        startCaptureResponse.Capture.ID);
}
else
{
    Console.WriteLine("Capturing is FAILED. detail: {0}",
        startCaptureResponse.Errors[0].Message);
}
```

## Stop Capture Movie

Use the [`StopCapture`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StopCapture.htm) method to stop movie capturing.

```csharp
Response response = cameraDevice.StopCapture();
Console.WriteLine("StopCapture is " +
   (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
```

`How to Focus and Capture`, `How to Check Capture Status`, `How to Obtain Captured Image` process is the same as [Still Image Capture](capture.md).

## See Also

* [Still Image Capture](capture.md) - Demonstrates how to capture still images.
* [Image Handling](image-handling.md) - Demonstrates image acquisition.
* [Capture Settings](capture-settings.md) - Describes available capture settings.
* [Events](events.md) - Describes events such as `CaptureComplete` and `ImageAdded`.
