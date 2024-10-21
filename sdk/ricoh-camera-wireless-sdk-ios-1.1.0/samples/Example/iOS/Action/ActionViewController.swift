// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class ActionViewController: UIViewController {

    private let globals = Globals.shared
    private let eventListener = EventListener()

    private let queue = DispatchQueue(label: "com.ricoh.RICOHCameraWirelessSDK.Example.ActionViewController")

    @IBOutlet weak var liveview: UIImageView!
    @IBOutlet weak var actionStateLabel: UILabel!
    @IBOutlet weak var updateTimeLabel: UILabel!
    
    @IBAction func startLiveviewButtonDidPush(_ sender: Any) {
        startLiveview()
    }

    @IBAction func stopLiveviewButtonDidPush(_ sender: Any) {
        stopLiveview()
    }

    @IBAction func shootButtonDidPush(_ sender: Any) {
        shoot(withFocus: false)
    }

    @IBAction func shootWithFocusButtonDidPush(_ sender: Any) {
        shoot(withFocus: true)
    }

    @IBAction func stopShootButtonDidPush(_ sender: Any) {
        stopShoot()
    }

    // MARK: View Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()
        actionStateLabel.text = ""
        updateTimeLabel.text = ""
        eventListener.delegate = self
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        globals.cameraDevice?.addEventListener(listener: eventListener)
    }
    
    override func viewDidDisappear(_ animated: Bool)  {
        super.viewDidDisappear(animated)
        
        globals.cameraDevice?.removeEventListener(listener: eventListener)
    }
    
    private func startLiveview() {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            guard weakSelf.globals.canUseCamera() else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to start liveview")
                }
                return
            }

            let response = weakSelf.globals.cameraDevice?.startLiveView()
            if response?.result == .ok {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Succeed to start liveview")
                }
                weakSelf.globals.saveRunningLiveview(isRunning: true)
            } else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to start liveview")
                }
            }
        }
    }

    private func stopLiveview() {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            guard weakSelf.globals.canUseCamera() else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to stop liveview")
                }
                return
            }

            let response = weakSelf.globals.cameraDevice?.stopLiveView()
            if response?.result == .ok {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Succeed to stop liveview")
                }
                weakSelf.globals.saveRunningLiveview(isRunning: false)
            } else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to stop liveview")
                }
            }
        }
    }

    private func shoot(withFocus: Bool) {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            let actionName = withFocus ? "start shoot with focus" : "start shoot"

            guard weakSelf.globals.canUseCamera() else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to " + actionName)
                }
                return
            }

            do {
                let response = try weakSelf.globals.cameraDevice?.startCapture(withFocus: withFocus)
                if response?.result == .ok {
                    DispatchQueue.main.async {
                        weakSelf.showActionState(message: "Succeed to " + actionName)
                    }
                } else {
                    DispatchQueue.main.async {
                        weakSelf.showActionState(message: "Failed to " + actionName)
                    }
                }
            } catch CameraSDKError.unsupportedOperation {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Not support to " + actionName)
                }
            } catch {
                //do nothing
            }
        }
    }

    private func stopShoot() {
        queue.async { [weak self] in
            guard let weakSelf = self else { return }

            guard weakSelf.globals.canUseCamera() else {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Failed to stop shoot")
                }
                return
            }

            do {
                let response = try weakSelf.globals.cameraDevice?.stopCapture()
                if response?.result == .ok {
                    DispatchQueue.main.async {
                        weakSelf.showActionState(message: "Succeed to stop shoot")
                    }
                } else {
                    DispatchQueue.main.async {
                        weakSelf.showActionState(message: "Failed to stop shoot")
                    }
                }
            } catch CameraSDKError.unsupportedOperation {
                DispatchQueue.main.async {
                    weakSelf.showActionState(message: "Not support to stop shoot")
                }
            } catch {
                //do nothing
            }
        }
    }

    private func showActionState(message: String) {
        actionStateLabel.text = message
    }

    private func showUpdateTime() {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        let time = formatter.string(from: Date())
        updateTimeLabel.text = "Updated: " + time
    }

}

// MARK: - CameraEventListener
extension ActionViewController {

    class EventListener: CameraEventListener {
        weak var delegate: ActionViewController?

        func liveViewFrameUpdated(sender: CameraDevice, liveViewFrame: Data) {
            delegate?.liveview.image = UIImage(data: liveViewFrame)
            delegate?.showUpdateTime()
        }

    }

}
