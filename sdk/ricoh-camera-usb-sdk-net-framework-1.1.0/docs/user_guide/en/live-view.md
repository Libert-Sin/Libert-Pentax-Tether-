---
layout: docs
---

# Live View

This page shows how to obtain live view of connected camera devices using RICOH Camera USB SDK.

For devices that support to live view, please refer to [Appendix](appendix.md).

### Contents

* [Related Classes](#related-classes)
* [How to Set up Live View Event](#set-up-live-view-event)
* [How to Start Live View](#start-live-view)
* [How to Adjust Focus](#adjust-focus)
* [How to Focus at Specified Position](#focus-at-specified-position)
* [How to Capture with Focusing at Specified Position](#capture-with-focusing-at-specified-position)
* [How to Stop Live View](#stop-live-view)
* [See Also](#see-also)

## Related Classes

The following classes are used when you obtain live view:

#### [`CameraEventListener`](../../api_reference/html/T_Ricoh_CameraController_CameraEventListener.htm)

* The `CameraEventListener` class is a event listener which receives notifications from camera devices.

#### [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm)

* The `CameraDevice` class represents a camera device.
* This class includes properties and methods for obtaining camera information and operating camera device.

## Set up Live View Event

To start live view, override the virtual method [`LiveViewFrameUpdated`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_LiveViewFrameUpdated.htm) included in the `CameraEventListener` class with the user-defined derived class. JPEG data is streamed all the time, which keeps calling `LiveViewFrameUpdated`.

```csharp
class EventListener : CameraEventListener
{
    public override void LiveViewFrameUpdated(
        CameraDevice sender,
        byte[] liveViewFrame)
    {
        // Display liveViewFrame in Image control (Name: image) of WPF
        var memoryStream = new MemoryStream(liveViewFrame);
        var bitmapImage = new BitmapImage();
        bitmapImage.BeginInit();
        bitmapImage.StreamSource = memoryStream;
        bitmapImage.EndInit();
        image.Source = bitmapImage;
    }
}
```

## Start Live View

Use the [`StartLiveView`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StartLiveView.htm) to start live view.

```csharp
CameraEventListener cameraEventListener = new EventListener();
cameraDevice.EventListeners.Add(cameraEventListener);
Response response = cameraDevice.StartLiveView();
```

## Adjust Focus

If you want to adjust the focus while checking the live view, use the [`Focus`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_Focus_1.htm) method with the `adjustment` argument.
Argument represents the moving direction and moving amount of the focus.
If the argument is positive, the focus moves forward. If the argument is negative, the focus moves backward.

```csharp
int adjustment = 100;
Response response = cameraDevice.Focus(adjustment);
```

## Focus at Specified Position

If you want focus at specified position while checking the live view, use the [`Focus`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_Focus_2.htm) method with the `focusPoint` argument.

The position that can be specified varies depending on the devices and setting conditions, and information can be get from the [`Live View Specification`](camera-settings.md).

```csharp
double positionX = 0.2
double positionY = 0.5
Vector focusPoint = new Vector(positionX, positionY)
Response response = cameraDevice.Focus(focusPoint);
```

## Capture with Focusing at Specified Position

If you want to capture with focusing at specified position while checking the live view, use the [`StartCapture`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StartCapture_1.htm) method with the `focusPoint` argument.

The position that can be specified varies depending on the devices and setting conditions, and information can be get from the [`Live View Specification`](camera-settings.md).

```csharp
double positionX = 0.2
double positionY = 0.5
Vector focusPoint = new Vector(positionX, positionY)
Response response = cameraDevice.StartCapture(focusPoint);
```

## Stop Live View

Use the [`StopLiveView`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_StopLiveView.htm) to stop live view.

```csharp
Response response = cameraDevice.StopLiveView();
```

## See Also

* [Camera Settings](camera-settings.md) - Describes camera settings.
* [Events](events.md) - Describes events such as `LiveViewFrameUpdated`.
