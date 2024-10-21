// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class CameraTimeSettingViewController: UIViewController, UITextFieldDelegate {

    var currentSetting: String = ""
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var status: UILabel!
    private let globals = Globals.shared

    override func viewDidLoad() {
        super.viewDidLoad()

        textField.delegate = self
        status.text = ""
        textField.text = currentSetting
        let btn = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.cancel,
                                  target: self,
                                  action: #selector(CameraTimeSettingViewController.close(sender:)))
        self.navigationItem.leftBarButtonItem = btn
    }

    @objc func close(sender: AnyObject) {
        textField.endEditing(true)
        dismiss(animated: true, completion: nil)
    }

    @IBAction func tappedApplyButton(_ sender: Any) {
        guard let value = textField.text else { return }
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        guard let date = formatter.date(from: value) else { return }

        guard globals.canUseCamera(), let camera = globals.cameraDevice else { return }
        let time = CameraTime(date: date)
        let response = camera.setCameraDeviceSettings(settings: [time])
        if response.result == .ok {
            status.text = "success"
        } else {
            status.text = "failed"
        }
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }

}
