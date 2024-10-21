---
layout: docs
---

# Image Handling

This page shows how to handle images in a connected camera storage using RICOH Camera USB SDK.

### Contents

* [Related Classes](#related-classes)
* [How to List Images](#list-images)
* [How to Obtain an Image](#obtain-an-image)
* [How to Obtain Image Thumbnails](#obtain-image-thumbnails)
* [How to Delete Images](#delete-images)

## Related Classes

The following classes are used when you manage images:

#### [`CameraImage`](../../api_reference/html/T_Ricoh_CameraController_CameraImage.htm)

* The `CameraImage` class represents image information and image data.
* This class includes properties and methods for obtaining image information and image data.

#### [`CameraDevice`](../../api_reference/html/T_Ricoh_CameraController_CameraDevice.htm)

* The `CameraDevice` class represents a camera device.
* This class includes properties and methods for obtaining camera information and operating camera device.

#### [`CameraStorage`](../../api_reference/html/T_Ricoh_CameraController_CameraStorage.htm)

* The `CameraStorage` class represents a storage of camera device.

## List Images

Use the [`Images`](../../api_reference/html/P_Ricoh_CameraController_CameraDevice_Images.htm) property of the `CameraDevice` class to obtain a list of images stored in the connected camera device.

```csharp
for (var i = 0; i < cameraDevice.Storages.Count; i++)
{
    var storage = cameraDevice.Storages[i];
    Console.WriteLine("\n[{0}]\n  Storage ID: 0x{1}",
        i, uint.Parse(storage.ID).ToString("x"));
    Console.WriteLine("  StorageListImagesState: {0}",
        storage.ListImagesState.ToString());
}
for (var i = 0; i < cameraDevice.Images.Count; i++)
{
    var image = cameraDevice.Images[i];
    Console.WriteLine("  [{0}] Storage: {1}, Name: {2}, Type: {3}, Format: {4},
        Size: {5}, ID: {6}, HasThumbnail: {7}, Date: {8}",
        i, cameraDevice.Storages.IndexOf(image.Storage), image.Name,
        image.Type, image.Format, image.Size, image.ID, image.HasThumbnail,
        image.DateTime);
}
```

The image list in camera storage is synchronized automatically in background process when the camera is connected and when the image list is updated. The image list returned by `Images` is sorted in descending order of date and time. The `CameraDevice` class includes the `CameraStorage` property called  [`Storages`](../../api_reference/html/P_Ricoh_CameraController_CameraDevice_Storages.htm).  The [`ListImagesState`](../../api_reference/html/P_Ricoh_CameraController_CameraStorage_ListImagesState.htm) property of the `CameraStorage` class represents acquisition status of the image list for each storage.

**Note:** The image list is synchronized automatically in background. Therefore, a loop index of the image list may change while looping through. To obtain a snapshot of the image list, use the `ToList` method.

```csharp
List<CameraImage> images = cameraDevice.Images.ToList();
```

## Obtain an Image

You can also specify index of the [`Images`](../../api_reference/html/P_Ricoh_CameraController_CameraDevice_Images.htm) property to obtain a single image.

```csharp
// Get the latest image
CameraImage image = cameraDevice.Images[0];
using (FileStream fs = new FileStream(
    Environment.CurrentDirectory + Path.DirectorySeparatorChar + image.Name,
    FileMode.Create, FileAccess.Write))
{
    Response imageGetResponse = image.GetData(fs);
    Console.WriteLine("Get Image is " +
        (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
    if (imageGetResponse.Result == Result.OK)
    {
        Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory +
            Path.DirectorySeparatorChar + image.Name);
    }
    else
    {
        foreach (Error error in imageGetResponse.Errors)
        {
            Console.WriteLine("Error Code: " + error.Code.ToString() +
                " / Error Message: "+ error.Message);
        }
    }
}
```

## Obtain Image Thumbnails

Use the [`GetThumbnail`](../../api_reference/html/M_Ricoh_CameraController_CameraImage_GetThumbnail.htm) method to obtain image thumbnails.

```csharp
// Get the thumbnail of latest image
CameraImage image = cameraDevice.Images[0];
string fileNameWithoutExtension = Path.GetFileNameWithoutExtension(image.Name);
using (FileStream fs = new FileStream( Environment.CurrentDirectory +
    Path.DirectorySeparatorChar + fileNameWithoutExtension + "_thumb.JPG",
    FileMode.Create, FileAccess.Write))
{
    Response imageGetResponse = image.GetThumbnail(fs);
    Console.WriteLine("Get Thumbnail Image is " +
        (imageGetResponse.Result == Result.OK ? "SUCCEED." : "FAILED."));
    if (imageGetResponse.Result == Result.OK)
    {
        Console.WriteLine("Image Path: {0}", Environment.CurrentDirectory +
            Path.DirectorySeparatorChar +
            fileNameWithoutExtension + "_thumb.JPG");
    }
    else
    {
        foreach (Error error in imageGetResponse.Errors)
        {
            Console.WriteLine("Error Code: " + error.Code.ToString() +
                " / Error Message: " + error.Message);
        }
    }
}
```

## Delete Images

Use the [`Delete`](../../api_reference/html/M_Ricoh_CameraController_CameraImage_Delete.htm) method to delete images in camera storage.

```csharp
// Delete the latest image
CameraImage deleteTarget = cameraDevice.Images[0];
Response response = deleteTarget.Delete();
Console.WriteLine("Delete Image({0}) is {1}", deleteTarget.Name,
    (response.Result == Result.OK ? "SUCCEED." : "FAILED."));
```
