// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class WiFiSettingViewController: UIViewController, UITextFieldDelegate {

    private let globals = Globals.shared
    private var currentChannelIndex = 0
    var currentSSID = ""
    var currentKey = ""
    var currentChannel = 0
    var availableChannelList = [Int]()

    @IBOutlet weak var ssidTextField: UITextField!
    @IBOutlet weak var keyTextField: UITextField!
    @IBOutlet weak var channelStepper: UIStepper!
    @IBOutlet weak var channelLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        ssidTextField.text = currentSSID
        keyTextField.text = currentKey
        for index in 0...(availableChannelList.count - 1) {
            if currentChannel == availableChannelList[index] {
                currentChannelIndex = index
            }
        }
        updateChannelLabel()

        ssidTextField.delegate = self
        keyTextField.delegate = self

        channelStepper.value = Double(currentChannelIndex)
        channelStepper.minimumValue = 0.0
        channelStepper.maximumValue = Double(availableChannelList.count - 1)

        let btn = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.cancel,
                                  target: self,
                                  action: #selector(WiFiSettingViewController.close(sender:)))
        self.navigationItem.leftBarButtonItem = btn
    }

    @objc func close(sender: AnyObject) {
        ssidTextField.endEditing(true)
        keyTextField.endEditing(true)
        dismiss(animated: true, completion: nil)
    }

    @IBAction func tappedChannelStepper(_ sender: Any) {
        currentChannelIndex = Int(channelStepper.value)
        updateChannelLabel()
    }

    @IBAction func tappedApplyButton(_ sender: Any) {
        let ssid = SSID(ssid: ssidTextField.text!)
        let key = Key(password: keyTextField.text!)
        var ch: Channel? = nil
        switch currentChannelIndex {
        case 0: ch = Channel.n0
        case 1: ch = Channel.n1
        case 2: ch = Channel.n2
        case 3: ch = Channel.n3
        case 4: ch = Channel.n4
        case 5: ch = Channel.n5
        case 6: ch = Channel.n6
        case 7: ch = Channel.n7
        case 8: ch = Channel.n8
        case 9: ch = Channel.n9
        case 10: ch = Channel.n10
        case 11: ch = Channel.n11
        default: ch = nil
        }

        guard globals.canUseCamera(), let camera = globals.cameraDevice else { return }
        let response = camera.setCameraDeviceSettings(settings: [ssid, key, ch!])
        if response.result == .ok {
            showAlert(message: "success")
        } else {
            showAlert(message: "failed")
        }
        
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }

    private func updateChannelLabel() {
        channelLabel.text = "Channel: " + availableChannelList[currentChannelIndex].description
    }

    private func showAlert(message: String) {
        let alert = UIAlertController(title: message, message: "", preferredStyle: .alert)
        let action = UIAlertAction(title: "close", style: .default, handler: nil)
        alert.addAction(action)
        present(alert, animated: true, completion: nil)
    }

}
