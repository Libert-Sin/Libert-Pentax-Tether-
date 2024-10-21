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

The following interfaces and classes are used when you manage images:

#### [`CameraImage`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraImage.html)

* The `CameraImage` interface represents image information and image data.
* This interface includes methods for obtaining image information and image data.

#### [`CameraDevice`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html)

* The `CameraDevice` interface represents a camera device.
* This interface includes methods for obtaining camera information and operating camera device.

#### [`CameraStorage`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStorage.html)

* The `CameraStorage` interface represents a storage of camera device.

<a name="list-images"></a>
## List Images

Use the [`getImages`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#getImages--) method of the `CameraDevice` interface to obtain a list of images stored in the connected camera device.

```java
for (CameraImage image: cameraDevice.getImages()) {
    System.out.printf("Image Name: %s%n", image.getName());
}
```

The image list in camera storage is synchronized automatically in background process when the camera is connected and when the image list is updated.
The image list is sorted in descending order of date and time.

Similarly, use the [`getImages`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStorage.html#getImages--) method of the `CameraStorage` interface to obtain a list of images stored in camera storage.
The [`getListImagesState`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraStorage.html#getListImagesState--) method of the `CameraStorage` interface represents acquisition status of the image list for each storage.

Such as after manipulating the main body to delete an image, there are cases when the image list is not latest.
In this case, use [`updateImages`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html#updateImages--) method to update the image list.

<a name="obtain-an-image"></a>
## Obtain an Image

Use the [`getData`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraImage.html#getData-java.io.OutputStream-) method to obtain a single image.

```java
FileOutputStream outputStream = null;
Response response = null;
try {
    outputStream = new FileOutputStream(new File(fileName));
    response = image.getData(outputStream);
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (outputStream != null) {
        try {
            outputStream.close();
        } catch (IOException e) {
            //do nothing
        }
    }
}
```

<a name="obtain-image-thumbnails"></a>
## Obtain Image Thumbnails

Use the [`getThumbnail`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraImage.html#getThumbnail-java.io.OutputStream-) method to obtain image thumbnails.

```java
FileOutputStream outputStream = null;
Response response = null;
try {
    outputStream = new FileOutputStream(new File(fileName));
    response = image.getThumbnail(outputStream);
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (outputStream != null) {
        try {
            outputStream.close();
        } catch (IOException e) {
            //do nothing
        }
    }
}
```

<a name="delete-images"></a>
## Delete Images

Deleting images is not supported on The SDK.
