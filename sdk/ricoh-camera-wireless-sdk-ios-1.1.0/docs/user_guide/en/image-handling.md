---
layout: docs
---

# Image Handling

This page shows how to handle images in a connected camera storage using RICOH Camera Wireless SDK.

### Contents

* [Related Classes](#related-classes)
* [How to List Images](#list-images)
* [How to Obtain an Image](#obtain-an-image)
* [How to Obtain Image Thumbnails](#obtain-image-thumbnails)
* [How to Delete Images](#delete-images)

<a name="related-classes"></a>
## Related Classes

The following protocols and classes are used when you manage images:

#### [`CameraImage`](../../api_reference/Protocols/CameraImage.html)

* The `CameraImage` protocol represents image information and image data.
* This protocol includes methods for obtaining image information and image data.

#### [`CameraDevice`](../../api_reference/Protocols/CameraDevice.html)

* The `CameraDevice` protocol represents a camera device.
* This protocol includes methods for obtaining camera information and operating camera device.

#### [`CameraStorage`](../../api_reference/Protocols/CameraStorage.html)

* The `CameraStorage` protocol represents a storage of camera device.

<a name="list-images"></a>
## List Images

Use the [`images`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP6imagesSayAA0D5Image_pGv) property of the `CameraDevice` protocol to obtain a list of images stored in the connected camera device.

```swift
for image in cameraDevice.images {
    print("Image Name: \(image.name)")
}
```

The image list in camera storage is synchronized automatically in background process when the camera is connected and when the image list is updated.
The image list is sorted in descending order of date and time.

Similarly, use the [`images`](../../api_reference/Protocols/CameraStorage.html#/s:22RICOHCameraWirelessSDK13CameraStorageP6imagesSayAA0D5Image_pGv) property of the `CameraStorage` protocol to obtain a list of images stored in camera storage.
The [`listImagesState`](../../api_reference/Protocols/CameraStorage.html#/s:22RICOHCameraWirelessSDK13CameraStorageP15listImagesStateAA0e4ListgH0Ov) property of the `CameraStorage` protocol represents acquisition status of the image list for each storage.

Such as after manipulating the main body to delete an image, there are cases when the image list is not latest.
In this case, use [`updateImages`](../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP12updateImagesAA8ResponseCyF) method to update the image list.

<a name="obtain-an-image"></a>
## Obtain an Image

Use the [`getData`](../../api_reference/Protocols/CameraImage.html#/s:22RICOHCameraWirelessSDK11CameraImageP7getDataAA8ResponseCSo12OutputStreamC06outputJ0_tKF) method to obtain a single image.

```swift
guard let outputStream = OutputStream(toFileAtPath: path, append: false) else {
    return
}
outputStream.open()
defer {
    outputStream.close()
}
do {
    let response = try image.getData(outputStream: outputStream)
} catch {
    print("Error is occurred while getting data.")
}
```

<a name="obtain-image-thumbnails"></a>
## Obtain Image Thumbnails

Use the [`getThumbnail`](../../api_reference/Protocols/CameraImage.html#/s:22RICOHCameraWirelessSDK11CameraImageP12getThumbnailAA8ResponseCSo12OutputStreamC06outputJ0_tKF) method to obtain image thumbnails.

```swift
guard let outputStream = OutputStream(toFileAtPath: path, append: false) else {
    return
}
outputStream.open()
defer {
    outputStream.close()
}
do {
    let response = try image.getThumbnail(outputStream: outputStream)
} catch {
    print("Error is occured while getting data.")
}
```

<a name="delete-images"></a>
## Delete Images

Deleting images is not supported on The SDK.
