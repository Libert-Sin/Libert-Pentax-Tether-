// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class AvailableCameraDeviceSettingTableViewController: UITableViewController {

    private let globals = Globals.shared
    var current: CameraDeviceSetting?
    var settingList = [CameraDeviceSetting]()

    override func viewDidLoad() {
        super.viewDidLoad()
        let btn = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.cancel,
                                  target: self,
                                  action: #selector(AvailableCaptureSettingTableViewController.close(sender:)))
        self.navigationItem.leftBarButtonItem = btn
    }

    @objc func close(sender: AnyObject) {
        dismiss(animated: true, completion: nil)
    }

    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return settingList.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: nil)

        cell.textLabel?.text = settingList[indexPath.row].value?.description
        if current == settingList[indexPath.row] {
            cell.accessoryType = .checkmark
        } else {
            cell.accessoryType = .none
        }
        return cell
    }

    override func tableView(_ tableView: UITableView, shouldHighlightRowAt indexPath: IndexPath) -> Bool {
        return current != settingList[indexPath.row]
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard current != settingList[indexPath.row] else { return }

        guard globals.canUseCamera(), let camera = globals.cameraDevice else { return }
        let response = camera.setCameraDeviceSettings(settings: [settingList[indexPath.row]])
        if response.result == .ok {
            current = settingList[indexPath.row]
            tableView.reloadData()
        } else {
            showAlert(message: "Error")
        }
    }

    private func showAlert(message: String) {
        let alert = UIAlertController(title: message, message: "", preferredStyle: .alert)
        let action = UIAlertAction(title: "close", style: .default, handler: nil)
        alert.addAction(action)
        present(alert, animated: true, completion: nil)
    }

}
