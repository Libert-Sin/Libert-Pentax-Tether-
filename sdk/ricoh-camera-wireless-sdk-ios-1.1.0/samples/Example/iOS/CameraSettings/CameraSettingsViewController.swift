// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class CameraSettingsViewController: UITableViewController {

    private let globals = Globals.shared
    private var settings = [CameraDeviceSetting]()
    private var timer: Timer!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        refreshControl = UIRefreshControl()
        refreshControl?.addTarget(self,
                                  action: #selector(CameraSettingsViewController.refresh(sender:)),
                                  for: .valueChanged)
        tableView.tableFooterView = UIView(frame: .zero)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        timer = Timer.scheduledTimer(timeInterval: 0.5,
                                     target: self,
                                     selector: #selector(CameraSettingsViewController.refresh(sender:)),
                                     userInfo: nil,
                                     repeats: true)
        timer.fire()
        
        loadSettings { [weak self] in
            self?.tableView.reloadData()
        }
    }

    override func viewDidDisappear(_ animated: Bool)  {
        super.viewDidDisappear(animated)
        
        timer.invalidate()
    }
    
    @objc func refresh(sender: UIRefreshControl) {
        loadSettings { [weak self] in
            self?.tableView.reloadData()
            self?.refreshControl?.endRefreshing()
        }
    }

    private func loadSettings(completion: (() -> Void)? = nil) {
        settings.removeAll()

        guard globals.canUseCamera(), let camera = globals.cameraDevice else {
            completion?()
            return
        }

        DispatchQueue.global().sync { [weak self] in
            guard let weakSelf = self else {
                DispatchQueue.main.async { completion?() }
                return
            }
            let settings = Globals.createAllCameraSettings()
            let response = camera.getCameraDeviceSettings(settings: settings)
            guard response.result != .error else {
                DispatchQueue.main.async { completion?() }
                return
            }
            for setting in settings where setting.value != nil {
                weakSelf.settings.append(setting)
            }
            DispatchQueue.main.async { completion?() }
        }
    }

    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return settings.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CameraSettingCell", for: indexPath)
        if settings.count == 0 || indexPath.row >= settings.count {
            return cell
        }
        
        cell.textLabel?.text = settings[indexPath.row].name
        cell.detailTextLabel?.text = settings[indexPath.row].value?.description
        cell.detailTextLabel?.textColor = UIColor.blue

        return cell
    }

    override func tableView(_ tableView: UITableView, shouldHighlightRowAt indexPath: IndexPath) -> Bool {
        return true
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if settings.count == 0 || indexPath.row >= settings.count {
            return
        }
        
        let setting = settings[indexPath.row]

        if setting is CameraTime {
            presentCameraTimeSettingVC()
        }

        if setting is Channel || setting is Key || setting is SSID {
            presentWiFiSettingVC()
        }

        if setting is DualCardSlotsMode {
            presentAvailableCameraDeviceSettingTableViewController()
        }
    }

    private func presentCameraTimeSettingVC() {
        guard let cameraTimeSetting: CameraTime = getSetting() else { return }
        guard let vc = storyboard?.instantiateViewController(withIdentifier: "CameraTime") else { return }
        guard let cameraTimeVC = vc as? CameraTimeSettingViewController else { return }
        guard let value = cameraTimeSetting.value?.description else { return }
        cameraTimeVC.currentSetting = value
        present(UINavigationController(rootViewController: cameraTimeVC), animated: true, completion: nil)
    }

    private func presentWiFiSettingVC() {
        guard let SSIDSetting: SSID = getSetting() else { return }
        guard let keySetting: Key = getSetting() else { return }
        guard let channelSetting: Channel = getSetting() else { return }

        guard let vc = storyboard?.instantiateViewController(withIdentifier: "WiFi") else { return }
        guard let wifiSettingVC = vc as? WiFiSettingViewController else { return }

        guard let SSIDValue = SSIDSetting.value?.description else { return }
        wifiSettingVC.currentSSID = SSIDValue

        guard let keyValue = keySetting.value?.description else { return }
        wifiSettingVC.currentKey = keyValue

        guard let temp = channelSetting.value?.description else { return }
        guard let channelValue = Int(temp) else { return }
        wifiSettingVC.currentChannel = channelValue

        for setting in channelSetting.availableSettings {
            if let ch = setting.value?.description, let intCh = Int(ch) {
                wifiSettingVC.availableChannelList.append(intCh)
            }
        }

        present(UINavigationController(rootViewController: wifiSettingVC), animated: true, completion: nil)
    }

    private func presentAvailableCameraDeviceSettingTableViewController() {
        guard let dualCardSlotsMode: DualCardSlotsMode = getSetting() else { return }

        if dualCardSlotsMode.availableSettings.count > 0 {
            let vc = AvailableCameraDeviceSettingTableViewController()
            vc.navigationItem.title = dualCardSlotsMode.name
            vc.current = dualCardSlotsMode
            vc.settingList = dualCardSlotsMode.availableSettings
            present(UINavigationController(rootViewController: vc), animated: true, completion: nil)
        }
    }

    private func getSetting<T: CameraDeviceSetting>() -> T? {
        var target: T? = nil
        for obj in settings {
            if let cameraDeviceSetting = obj as? T {
                target = cameraDeviceSetting
            }
        }
        return target
    }

}
