---
layout: docs
permalink: /docs/camera-wireless-sdk-ios/
---

# RICOH Camera Wireless SDK for iOS

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

* iOS8 or later

RICOH Camera Wireless SDK requires following OSSs.

| OSS | License |
|:------------|:-------------|
| [Alamofire](https://github.com/Alamofire/Alamofire) | [MIT License](https://github.com/Alamofire/Alamofire/blob/master/LICENSE) |
| [SwiftWebSocket](https://github.com/tidwall/SwiftWebSocket) | [MIT License](https://github.com/tidwall/SwiftWebSocket/blob/master/LICENSE) |

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
(The app was tested in Xcode 9.1.)

Sample App is added required frameworks using [Carthage](https://github.com/Carthage/Carthage).

Before open Sample App in Xcode for the first time, install Carthage and run `carthage update --platform ios`.

### Integrate SDK into Your App

Follow the instructions below to integrate RICOH Camera Wireless SDK with your existing app.
You can also refer to Sample App's source code for the basic usage.

* Integrate required frameworks ([Alamofire](https://github.com/Alamofire/Alamofire), [SwiftWebSocket](https://github.com/tidwall/SwiftWebSocket)) into your app

* integrate "RICOHCameraWirelessSDK.framework" into your app

     * On your app targets’ “General” settings tab, in the “Embedded Binaries” section, add "RICOHCameraWirelessSDK.framework"

     * On your app targets’ “Build Settings” settings tab, in the “Search Paths” section, add a path of "RICOHCameraWirelessSDK.framework" to "FRAMEWORK_SEARCH_PATHS"

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

`RICOHCameraWirelessSDK` framework provides functions for camera operations such as shooting and acquiring images. For detailed class reference, visit [`API Reference`](../../api_reference/index.html).

### CameraDevice protocol

CameraDevice protocol manages camera information and operates cameras.
See [`CameraDevice protocol in API Reference`](../../api_reference/Protocols/CameraDevice.html) for detailed protocol reference.

### CameraImage protocol

CameraImage protocol manages image information and image data.
See [`CameraImage protocol in API Reference`](../../api_reference/Protocols/CameraImage.html) for detailed protocol reference.

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
