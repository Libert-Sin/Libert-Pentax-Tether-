---
layout: docs
---

# Known Issues

## Known Issues

The following table shows the known issues of RICOH Camera Wireless SDK.

Issue (Devices) | Solution
-- | --
Obtaining a large size image such as 4GB with [`getData`][getData] may not complete (K-1 Mark II, KP, K-70, K-1) | Please obtain directly a large size image from SD card.
When FHD50i or FHD60i of [`MovieCaptureFormat`][MovieCaptureFormat] is set by [`setCaptureSetting`][setCaptureSetting], FHD30p is applied to camera (K-1 Mark II, K-1) | Please operate the camera body to set.
When there is large amount of files on the SD card, it take time to get image list, and sometime there is a case when it is slow to response from camera  (K-1 Mark II, KP, K-70, K-1) | Please use SD card on which there is not large amount of files.

[getData]:../../api_reference/Protocols/CameraImage.html#/s:22RICOHCameraWirelessSDK11CameraImageP7getDataAA8ResponseCSo12OutputStreamC06outputJ0_tKF
[MovieCaptureFormat]:../../api_reference/Classes/MovieCaptureFormat.html
[setCaptureSetting]:../../api_reference/Protocols/CameraDevice.html#/s:22RICOHCameraWirelessSDK12CameraDeviceP18setCaptureSettingsAA8ResponseCSayAA0G7SettingCG8settings_tF
