// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class DeviceViewController: UIViewController {

    private let globals = Globals.shared
    private let eventListener = EventListenerForShow()

    private let queue = DispatchQueue(label: "com.ricoh.RICOHCameraWirelessSDK.Example.DeviceViewController")
    private var timer: DispatchSourceTimer?

    // MARK: IBOutlets

    @IBOutlet weak var manufacturerLabel: UILabel!
    @IBOutlet weak var modelLabel: UILabel!
    @IBOutlet weak var firmwareVersionLabel: UILabel!
    @IBOutlet weak var serialNumberLabel: UILabel!
    @IBOutlet weak var batteryLevelLabel: UILabel!
    @IBOutlet weak var storageInfoTextView: UITextView!
    @IBOutlet weak var eventTextView: UITextView!
    @IBOutlet weak var connectionStateLabel: UILabel!

    // MARK: IBActions

    @IBAction func connectButtonDidPush(_ sender: Any) {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            if let cameraDevice = weakSelf.globals.cameraDevice {
                if !cameraDevice.isConnected(deviceInterface: .wlan) {
                    let devices = CameraDeviceDetector.detect(deviceInterface: .wlan)
                    if !devices.isEmpty  {
                        weakSelf.globals.cameraDevice = weakSelf.addEventListenerIfNeed(cameraDevice: devices[0])
                    }
                }
            } else {
                let devices = CameraDeviceDetector.detect(deviceInterface: .wlan)
                guard !devices.isEmpty else {
                    DispatchQueue.main.async {
                        weakSelf.showDeviceInfo()
                        weakSelf.showConnectionState(message: "Can not find cameras")
                    }
                    return
                }
                weakSelf.globals.cameraDevice = weakSelf.addEventListenerIfNeed(cameraDevice: devices[0])
            }

            let response = weakSelf.globals.cameraDevice?.connect(deviceInterface: .wlan)
            if response?.result == .ok {
                DispatchQueue.main.async {
                    weakSelf.showEvent(message: "connected!")
                    weakSelf.showDeviceInfo()
                    weakSelf.showConnectionState(message: "Succeed to connect")
                }
            } else {
                DispatchQueue.main.async {
                    weakSelf.showDeviceInfo()
                    weakSelf.showConnectionState(message: "Failed to connect")
                }
            }
        }
    }

    @IBAction func disconnectButtonDidPush(_ sender: Any) {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            guard let cameraDevice = weakSelf.globals.cameraDevice else {
                DispatchQueue.main.async {
                    weakSelf.showDeviceInfo()
                    weakSelf.showConnectionState(message: "Can not find cameras")
                }
                return
            }
            cameraDevice.removeEventListener(listener: weakSelf.eventListener)
            let response = cameraDevice.disconnect(deviceInterface: .wlan)
            if response.result == .ok {
                DispatchQueue.main.async {
                    weakSelf.showEvent(message: "disconnected!")
                    weakSelf.showConnectionState(message: "Succeed to disconnect")
                }
                weakSelf.globals.saveRunningLiveview(isRunning: false)
            } else {
                DispatchQueue.main.async {
                    weakSelf.showConnectionState(message: "Failed to disconnect")
                }
            }
        }
    }

    // MARK: View Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()
        eventListener.delegate = self
        clearDeviceInfo()
        clearEvent()
        clearConnectionState()

        timer = DispatchSource.makeTimerSource()
        timer?.schedule(deadline: .now(), repeating: 0.5)
        timer?.setEventHandler { [weak self] in
            guard let weakSelf = self else { return }
            DispatchQueue.main.async {
                weakSelf.showDeviceInfo()
            }
        }
        timer?.resume()
    }

    // MARK: Private Methods

    private func addEventListenerIfNeed(cameraDevice: CameraDevice) -> CameraDevice {
        cameraDevice.addEventListener(listener: eventListener)
        return cameraDevice
    }

}

// MARK: - CameraEventListener
extension DeviceViewController {

    class EventListenerForShow: CameraEventListener {
        weak var delegate: DeviceViewController?

        func imageStored(sender: CameraDevice, image: CameraImage) {
            self.delegate?.showEvent(message: "ImageStored " + image.name)
        }

        func captureComplete(sender: CameraDevice, capture: Capture) {
            self.delegate?.showEvent(message: "CaptureComplete " + capture.id)
        }

        func deviceDisconnected(sender: CameraDevice) {
            self.delegate?.showEvent(message: "DeviceDisconnected")
        }
    }

}

// MARK: - Operate SubView
extension DeviceViewController {

    private func showDeviceInfo() {
        guard let cameraDevice = globals.cameraDevice else {
            clearDeviceInfo()
            return
        }

        manufacturerLabel.text = "manufacturer: " + cameraDevice.manufacturer
        modelLabel.text = "model: " + cameraDevice.model
        firmwareVersionLabel.text = "firmwareVersion: " + cameraDevice.firmwareVersion
        serialNumberLabel.text = "serialNumber: " + cameraDevice.serialNumber
        batteryLevelLabel.text = "batteryLevel: " + cameraDevice.status.batteryLevel.description

        storageInfoTextView.text = ""
        for storage in cameraDevice.storages {
            var message = "[" + storage.id + "]"
            message += " available:" + storage.isAvailable.description
            message += ", remain:" + storage.remainingPictures.description

            switch storage.type {
            case .fixedRam: message += ", type:fixedRam"
            case .removableRam: message += ", type:removableRam"
            }

            switch storage.permission {
            case .read: message += ", permission:read"
            case .readWrite: message += ", permission:readWrite"
            case .unknown: message += ", permission:unknown"
            }

            switch storage.listImagesState {
            case .interrupted: message += ", state:interrupted"
            case .listed: message += ", state:listed"
            case .listing: message += ", state:listing"
            case .notListed: message += ", state:notListed"
            case .pending: message += ", state:pending"
            }
            message += "\n"

            storageInfoTextView.text = storageInfoTextView.text + message
        }
    }

    private func showEvent(message: String) {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        let time = formatter.string(from: Date())
        eventTextView.text = "[" + time + "] " + message + "\n" + eventTextView.text
    }

    private func showConnectionState(message: String) {
        connectionStateLabel.text = message
    }

    private func clearDeviceInfo() {
        manufacturerLabel.text = "manufacturer: "
        modelLabel.text = "model: "
        firmwareVersionLabel.text = "firmwareVersion: "
        serialNumberLabel.text = "serialNumber: "
        batteryLevelLabel.text = "batteryLevel: "
        storageInfoTextView.text = ""
    }

    private func clearEvent() {
        eventTextView.text = ""
    }

    private func clearConnectionState() {
        connectionStateLabel.text = ""
    }

}
