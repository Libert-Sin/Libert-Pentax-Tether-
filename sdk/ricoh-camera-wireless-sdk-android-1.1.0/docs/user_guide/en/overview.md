---
layout: docs
permalink: /docs/camera-wireless-sdk-android/
---

# RICOH Camera Wireless SDK for Android

RICOH Camera Wireless SDK enables your app to operate cameras. This page shows how to use SDK packages and a sample app, which comes with the SDK.

### Contents

* [Requirements](#requirements)
* [Quick Start](#quick-start)
* [Classes](#classes)
* [Versioning Rules](#versioning-rules)

<a name="requirements"></a>
## Requirements

### Supported Devices

* PENTAX K-1 Mark II
* PENTAX KP
* PENTAX K-70
* PENTAX K-1

### Supported Environments

* Java Development Kit (JDK) 1.7 or later

RICOH Camera Wireless SDK requires following OSSs.

| OSS | License |
|:------------|:-------------|
| [Jackson](https://github.com/FasterXML/jackson) | [Apache License 2.0](https://github.com/FasterXML/jackson/wiki/FAQ) |
| [OkHttp](http://square.github.io/okhttp/) | [Apache License 2.0](http://square.github.io/okhttp/#license) |
| [SLF4J Android](https://www.slf4j.org/android/) | [MIT License](https://www.slf4j.org/license.html) |
| [SnakeYAML](https://bitbucket.org/asomov/snakeyaml) | [Apache License 2.0](https://github.com/asomov/snakeyaml/blob/master/LICENSE.txt) |

<a name="quick-start"></a>
## Quick Start

This section is designed to describe the two approaches to get started with RICOH Camera Wireless SDK.
You can run Sample App to understand what the SDK can do and how it works.
Also, you can add the SDK to your existing app to enhance it.

### Run Sample App

Try out Sample App to understand RICOH Camera Wireless SDK quickly. Sample App has functions such as:

* Connecting to your camera
* Capturing images
* Changing camera settings

You can run the app you build from the source code.
(The app was tested in Android Studio 2.2.2.)

### Integrate SDK into Your App

Follow the instructions below to integrate RICOH Camera Wireless SDK with your existing app.
You can also refer to Sample App's source code for the basic usage.

* Add `ricoh-camera-wireless-sdk-x.x.x.jar` file to `libs` folder of your module.  

* Modifiy `build.gradle` of your module as follows.

```
android {
    packagingOptions {
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/NOTICE'
    }
}

dependencies {
  compile fileTree(include: ['*.jar'], dir: 'libs')
  compile 'com.fasterxml.jackson.core:jackson-core:2.4.4'
  compile 'com.fasterxml.jackson.core:jackson-annotations:2.4.4'
  compile 'com.fasterxml.jackson.core:jackson-databind:2.4.4'
  compile 'com.squareup.okhttp3:okhttp:3.4.2'
  compile 'com.squareup.okhttp3:okhttp-ws:3.4.2'
  compile 'org.slf4j:slf4j-android:1.6.1-RC1'
  compile 'org.yaml:snakeyaml:1.18:android'
}
```
* Add following setting to `AndroidManifest.xml` to allow applications to open network sockets.

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

* Explore the quick references of each operation and events to enhance your app.

    * [How to connect to cameras](./connection.md)
    * [How to get camera information](./camera-information.md)
    * [How to capture images](./capture.md)
    * [How to handle images](./image-handling.md)
    * [How to obtain live view](./live-view.md)
    * [How to receive camera events](./events.md)
    * [Capture Settings](./capture-settings.md)
    * [Camera Settings](./camera-settings.md)
    * [Appendix](./appendix.md)

<a name="classes"></a>
## Classes

`com.ricoh.camera.sdk.wireless.api` package provides functions for camera operations such as shooting and acquiring images. For detailed class reference, visit [`JavaDoc`](../../api_reference/index.html).

### CameraDevice Interface

CameraDevice Interface manages camera information and operates cameras.
See [`CameraDevice Interface in JavaDoc`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraDevice.html) for detailed interface reference.

### CameraImage Interface

CameraImage Interface manages image information and image data.
See [`CameraImage Interface in JavaDoc`](../../api_reference/com/ricoh/camera/sdk/wireless/api/CameraImage.html) for detailed interface reference.

<a name="versioning-rules"></a>
## Versioning Rules

RICOH Camera Wireless SDK conforms to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).
See below for versioning rules of the SDK.

### Versioning Syntax

RICOH Camera Wireless SDK is versioned in accordance with the following syntax:

{MAJOR}.{MINOR}.{PATCH}

Field Name  |Description
------------|---
MAJOR       |Major Version is<br>incremented when some changes that don't have backwards-compatibility with functionality occur.
MINOR       |Minor Version is<br>incremented when some interfaces are changed or added while keep having backwards-compatibility with functionality.<br>In this case, some build errors and other compatible issues may occur in your existing applications.
PATCH       |Patch Version is<br>incremented when some security fixes and bug fixes are made while keep having backwards-compatibility with functionality.<br>In this case, existing application build and compatibility with functionality is guaranteed.
