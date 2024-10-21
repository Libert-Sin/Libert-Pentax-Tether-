---
layout: docs
---

# Still Image Capture

This page shows how to capture still images with connected camera device using RICOH Camera USB SDK. 

The RICOH Camera USB SDK supports single frame capture among still image capture.

### Contents

* [Related Classes](#related-classes)
* [How to Capture Still Images](#capture-still-images)
* [How to Focus and Capture Still Images](#focus-and-capture-still-images)
* [How to Check Capture Status](#check-capture-status)
* [How to Obtain Captured Image](#obtain-captured-image)
* [See Also](#see-also)

## Related Classes

The following classes are used when you capture images:

#### [`CameraImage`](../../api_reference/html/T_Ricoh_CameraController_CameraImage.htm)

* The `CameraImage` class represents image information and image data.
* This class includes properties and methods for obtaining image information and image data.

#### [`CameraEventListener`](../../api_reference/html/T_Ricoh_CameraController_CameraEventListener.htm)

* The `CameraEventListener` class is a event listener which receives notifications from camera devices.

## Capture Still Images

Use the [`StartCapture`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StartCapture.htm) method to release shutter of connected camera device.

```csharp
StartCaptureResponse startCaptureResponse = cameraDevice.StartCapture();
if (startCaptureResponse.Result == Result.OK)
{
    Console.WriteLine("Capturing StillImage has started. Capture ID: {0}",
        startCaptureResponse.Capture.ID);
}
else
{
    Console.WriteLine("Capturing StillImage is FAILED. detail: {0}",
        startCaptureResponse.Errors[0].Message);
}
```

## Focus and Capture Still Images

If you want to capture images after focusing, use the [`Focus`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_Focus.htm) method and the [`StartCapture`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StartCapture.htm) method with the `withFocus` argument `false`.

```csharp
Response response = cameraDevice.Focus();
if (response.Equals(Response.OK))
{
    Console.WriteLine("Focus has started.");
}
else
{
    Console.WriteLine("Focus is FAILED. detail: {0}",
        response.Errors[0].Message);
}
```

```csharp
bool withFocus = false;
StartCaptureResponse startCaptureResponse =
    cameraDevice.StartCapture(withFocus);
if (startCaptureResponse.Result == Result.OK)
{
    Console.WriteLine("Capturing StillImage has started. Capture ID: {0}",
        startCaptureResponse.Capture.ID);
}
else
{
    Console.WriteLine("Capturing StillImage is FAILED. detail: {0}",
        startCaptureResponse.Errors[0].Message);
}
```

## Check Capture Status

Use the [`State`](../../api_reference/html/P_Ricoh_CameraController_Capture_State.htm) property of the [`Capture`](../../api_reference/html/T_Ricoh_CameraController_Capture.htm) class to check capture status. One of the supported values of the property is `Complete`, which means the camera device has finished capturing.
You can obtain the `Capture` class with the three ways below:

1. Use the [`StartCaptureResponse`](../../api_reference/html/T_Ricoh_CameraController_StartCaptureResponse.htm) class     
The `StartCaptureResponse` class explained in sections above provides a `Capture` type property called [`Capture`](../../api_reference/html/P_Ricoh_CameraController_StartCaptureResponse_Capture.htm).
```csharp
StartCaptureResponse startCaptureResponse = cameraDevice.StartCapture();
if (startCaptureResponse.Result == Result.OK)
{
    Console.WriteLine("Capture state is {0}",
        startCaptureResponse.Capture.State);
}
```

1. Use the [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm) class     
The `CameraDevice` class has a [`CameraStatus`](../../api_reference/html/T_Ricoh_CameraController_CameraStatus.htm) property called [`Status`](../../api_reference/html/P_Ricoh_CameraController_CameraDevice_Status.htm). The `CameraStatus` class provides a `Capture` property called [`CurrentCapture`](../../api_reference/html/P_Ricoh_CameraController_CameraStatus_CurrentCapture.htm).
```csharp
StartCaptureResponse startCaptureResponse = cameraDevice.StartCapture();
if (startCaptureResponse.Result == Result.OK)
{
    Console.WriteLine("Capture state is {0}",
        cameraDevice.Status.CurrentCapture.State);
}
```

1. Use the [`CameraEventListener`](../../api_reference/html/T_Ricoh_CameraController_CameraEventListener.htm) class     
The [`CaptureComplete`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_CaptureComplete.htm) method of the `CameraEventListener` class is invoked when the camera device has finished capturing.
```csharp
class UserEventListener : CameraEventListener
{
    // Capture Complete
    public override void CaptureComplete(CameraDevice sender, Capture capture)
    {
        Console.WriteLine("Capture Complete. Capture ID: {0}", capture.ID);
    }
}
```

## Obtain Captured Image

Use the [`ImageAdded`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_ImageAdded.htm) method of the [`CameraEventListener`](../../api_reference/html/T_Ricoh_CameraController_CameraEventListener.htm) class to obtain the image you captured.

```csharp
class UserEventListener : CameraEventListener
{
    // Image Added
    public override void ImageAdded(CameraDevice sender, CameraImage image)
    {
        // Get the image and save it in the current directory
        using (FileStream fs = new FileStream(
            Environment.CurrentDirectory + Path.DirectorySeparatorChar +
            image.Name, FileMode.Create, FileAccess.Write))
        {
            Response imageGetResponse = image.GetData(fs);
            Console.WriteLine("Get Image is " +
                (imageGetResponse.Result == Result.OK ?
                    "SUCCEED." : "FAILED."));
        }
    }
}
```

## See Also

* [Movie Capture](movie-capture.md) - Demonstrates how to capture movie images.
* [Image Handling](image-handling.md) - Demonstrates image acquisition.
* [Capture Settings](capture-settings.md) - Describes available capture settings such as capturing without saving data on camera storage.
* [Events](events.md) - Describes events such as `CaptureComplete` and `ImageAdded`.
