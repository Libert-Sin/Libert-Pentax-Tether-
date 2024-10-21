---
layout: docs
---

# Events

You can receive camera events of the connected camera using the [`CameraEventListener`][CameraEventListener] class.


### Contents

* [Supported Camera Events](#supported-camera-events)
* [How to Register Events](#register-events)
* [Capture Settings Supported by CaptureSettingsChanged](#capture-settings-supported-by-capturesettingschanged)
* [See Also](#see-also)

## Supported Camera Events

RICOH Camera USB SDK supports the following camera events:

* [`ImageAdded`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_ImageAdded.htm) is called when image is added. You can transfer image data before ImageStored is called.
* [`ImageStored`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_ImageStored.htm) is called when image has been stored to the camera storage.
* [`CaptureComplete`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_CaptureComplete.htm) is called when capturing an image is completed.
* [`DeviceDisconnected`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_DeviceDisconnected.htm) is called when the camera you've already made a connection is disconnected.
* [`LiveViewFrameUpdated`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_LiveViewFrameUpdated.htm) is called when live view frame data is updated.
* [`CaptureSettingsChanged`](../../api_reference/html/M_Ricoh_CameraController_CameraEventListener_CaptureSettingsChanged.htm) is called when supported capture settings are changed.
 It does not be called if you change directly with [`SetCaptureSettings`](../../api_reference/html/M_Ricoh_CameraController_CameraDevice_SetCaptureSettings.htm) method.

## Register Events

Override the virtual method corresponding to the event you want to receive in your derived class of [`CameraEventListener`][CameraEventListener]. You can also see [Live View](live-view.md) for an example of `LiveViewFrameUpdated`.

```csharp
class UserEventListener : CameraEventListener
{
    public override void ImageAdded(CameraDevice sender, CameraImage image)
    {
        Console.WriteLine(
            "Image Added. Name: {0}, Type: {1}, Format: {2}, Size: {3}, " +
            "ID: {4}", image.Name, image.Type, image.Format, image.Size,
            image.ID);
    }
    public override void ImageStored(CameraDevice sender, CameraImage image)
    {
        Console.WriteLine(
            "Image Stored. Name: {0}, Type: {1}, Format: {2}, Size: {3}, " +
            "ID: {4}", image.Name, image.Type, image.Format, image.Size,
            image.ID);
    }
    public override void CaptureComplete(CameraDevice sender, Capture capture)
    {
        Console.WriteLine("Capture Complete. Capture ID: {0}", capture.ID);
    }
    public override void DeviceDisconnected(
        CameraDevice sender,
        DeviceInterface deviceInterface)
    {
        Console.WriteLine("Device Disconnected.");
    }
    public override void CaptureSettingsChanged(CameraDevice sender,
        List<CaptureSetting> newSettings)
    {
        Console.WriteLine("CaptureSettingsChanged:");
        foreach (var setting in newSettings)
        {
            if (setting is ShutterSpeed) {
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is WhiteBalance) {
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is FNumber) {
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is ISO) { 
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is ExposureCompensation) { 
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is ExposureProgram) { 
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is HyperOperationEnable) { 
                Console.WriteLine("  {0}", setting.ToString()); }
            else if (setting is UserCaptureSettingsMode) { 
                Console.WriteLine("  {0}", setting.ToString()); }
        }
    }
}
```

And register the event listener as follows:

```csharp
CameraEventListener userEventListener = new UserEventListener();
cameraDevice.EventListeners.Add(userEventListener);
```
Registered events are called when corresponding events occur at [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm).

## Capture Settings Supported by CaptureSettingsChanged

The [Capture Settings](capture-settings.md) to be notified are as follows.

* Aperture Value
* Exposure Compensation
* ISO Sensitivity Value
* White Balance
* Shutter Speed
* Exposure Program
* Hyper Operation Settings
* User Capture Settings Mode

## See Also

* [Still Image Capture](capture.md) - Demonstrates how to capture still images.
* [Connection](connection.md) - Demonstrates how to make connections with cameras.
* [Live View](live-view.md) - Describes `LiveViewFrameUpdated`.
* [Capture Settings](capture-settings.md) - Describes available capture settings.

[CameraEventListener]: ../../api_reference/html/T_Ricoh_CameraController_CameraEventListener.htm
