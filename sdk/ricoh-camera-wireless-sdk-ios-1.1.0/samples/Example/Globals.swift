// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import Foundation
import RICOHCameraWirelessSDK

class Globals {

    static let shared = Globals()
    private(set) var eventText = ""

    var cameraDevice: CameraDevice?
    
    private init() {}

    class func createAllCaptureSettings() -> [CaptureSetting] {
        var settings = [CaptureSetting]()
        settings.append(CaptureMethod())
        settings.append(CustomImage())
        settings.append(DigitalFilter())
        settings.append(ExposureCompensation())
        settings.append(FNumber())
        settings.append(ISO())
        settings.append(MovieCaptureFormat())
        settings.append(ShutterSpeed())
        settings.append(StillImageCaptureFormat())
        settings.append(StillImageQuality())
        settings.append(WhiteBalance())
        return settings
    }

    class func createAllCameraSettings() -> [CameraDeviceSetting] {
        var settings = [CameraDeviceSetting]()
        settings.append(CameraTime())
        settings.append(Channel())
        settings.append(DualCardSlotsMode())
        settings.append(Key())
        settings.append(SSID())
        return settings
    }

    func canUseCamera() -> Bool {
        guard let cameraDevice = cameraDevice else { return false }
        return cameraDevice.isConnected(deviceInterface: .wlan)
    }

    func addEventTextBuffer(massage: String) {
        eventText = massage + eventText
    }
    
    func saveRunningLiveview(isRunning: Bool) {
        let userDefaults = UserDefaults.standard
        userDefaults.set(isRunning, forKey: "runningLiveview")
    }
    
    func loadRunningLiveview() -> Bool {
        let userDefaults = UserDefaults.standard
        return userDefaults.bool(forKey: "runningLiveview")
    }
    
}
