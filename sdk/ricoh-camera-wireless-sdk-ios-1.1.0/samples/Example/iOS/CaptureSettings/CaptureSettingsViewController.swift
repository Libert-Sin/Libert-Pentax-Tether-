// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class CaptureSettingsViewController: UITableViewController {

    private let globals = Globals.shared
    private var settings = [CaptureSetting]()
    private var timer: Timer!

    override func viewDidLoad() {
        super.viewDidLoad()

        refreshControl = UIRefreshControl()
        refreshControl?.addTarget(self,
                                  action: #selector(CaptureSettingsViewController.refresh(sender:)),
                                  for: .valueChanged)
        tableView.tableFooterView = UIView(frame: .zero)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        timer = Timer.scheduledTimer(timeInterval: 0.5,
                                     target: self,
                                     selector: #selector(CaptureSettingsViewController.refresh(sender:)),
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
            let settings = Globals.createAllCaptureSettings()
            let response = camera.getCaptureSettings(settings: settings)
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "CaptureSettingCell", for: indexPath)
        if settings.count == 0 || indexPath.row >= settings.count {
            return cell
        }
        
        cell.textLabel?.text = settings[indexPath.row].name
        cell.detailTextLabel?.text = settings[indexPath.row].value?.description
        if settings[indexPath.row].availableSettings.count > 0 {
            cell.detailTextLabel?.textColor = UIColor.blue
        } else {
            cell.detailTextLabel?.textColor = UIColor.lightGray
        }

        return cell
    }

    override func tableView(_ tableView: UITableView, shouldHighlightRowAt indexPath: IndexPath) -> Bool {
        if settings.count == 0 || indexPath.row >= settings.count {
            return false
        }
        
        return settings[indexPath.row].availableSettings.count > 0
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if settings.count == 0 || indexPath.row >= settings.count {
            return
        }
        
        let setting = settings[indexPath.row]

        if setting.availableSettings.count > 0 {
            let vc = AvailableCaptureSettingTableViewController()
            vc.navigationItem.title = setting.name
            vc.current = setting
            vc.settingList = setting.availableSettings
            present(UINavigationController(rootViewController: vc), animated: true, completion: nil)
        }
    }

}
