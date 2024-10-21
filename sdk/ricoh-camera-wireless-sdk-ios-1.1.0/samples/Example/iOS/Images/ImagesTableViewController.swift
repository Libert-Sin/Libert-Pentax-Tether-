// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class ImagesViewController: UITableViewController {

    private let globals = Globals.shared
    private var images = [CameraImage]()

    private let eventListener = EventListenerForRefresh()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        eventListener.delegate = self
        tableView.tableFooterView = UIView(frame: .zero)
        refreshControl = UIRefreshControl()
        refreshControl?.addTarget(self, action: #selector(ImagesViewController.refresh(sender:)), for: .valueChanged)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        globals.cameraDevice?.addEventListener(listener: eventListener)
        
        loadImages { [weak self] in
            self?.tableView.reloadData()
        }
    }
    
    override func viewDidDisappear(_ animated: Bool)  {
        super.viewDidDisappear(animated)
        
        globals.cameraDevice?.removeEventListener(listener: eventListener)
    }
    
    @objc func refresh(sender: UIRefreshControl) {
        let response = self.globals.cameraDevice?.updateImages()
        if response?.result == Result.error {
            
            let alert = UIAlertController(
                title: "Update images",
                message: "Failure",
                preferredStyle: .alert)
            
            var complete = false
            self.present(alert, animated: true, completion: {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.3, execute: {
                    alert.dismiss(animated: true, completion: nil)
                    complete = true
                })
            })
            
            repeat {
                RunLoop.current.run(until: Date(timeIntervalSinceNow: 0.001))
            } while !complete
            
            sender.endRefreshing()
        }
        loadImages { [weak self] in
            self?.tableView.reloadData()
            self?.refreshControl?.endRefreshing()
        }
    }
    
    private func loadImages(completion: (() -> Void)? = nil) {
        images.removeAll()

        guard globals.canUseCamera(), let camera = globals.cameraDevice else {
            completion?()
            return
        }

        DispatchQueue.global().async { [weak self] in
            guard let weakSelf = self else {
                DispatchQueue.main.async { completion?() }
                return
            }
            weakSelf.images.append(contentsOf: camera.images)
            DispatchQueue.main.async { completion?() }
        }
    }

    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return images.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "CameraImageCell",
                                                       for: indexPath) as? CameraImageTableViewCell else {
            return UITableViewCell()
        }

        let image = images[indexPath.row]
        cell.name.text = image.name
        cell.type.text = (image.type == .stillImage) ? "Still Image" : "Movie"
        if let dateTime = image.dateTime {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            cell.date.text = formatter.string(from: dateTime)
        } else {
            cell.date.text = "(nil)"
        }
        cell.storage.text = "Storage ID: " + (image.storage?.id ?? "(nil)")
        cell.hasThumbnail.text = "hasThumbnail: " + ((image.hasThumbnail) ? "true" : "false")
        return cell
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        guard let cell = sender as? CameraImageTableViewCell, let indexPath = tableView.indexPath(for: cell) else { return }
        guard let destinationVC = segue.destination as? UINavigationController else { return }
        guard let imageDetailVC = destinationVC.childViewControllers[0] as? ImageDetailViewController else { return }

        if segue.identifier == "ShowImageDetail" {
            imageDetailVC.cameraImage = images[indexPath.row]
        }
    }

}

// MARK: - CameraEventListener
extension ImagesViewController {
    
    class EventListenerForRefresh: CameraEventListener {
        weak var delegate: ImagesViewController?
        
        func imageStored(sender: CameraDevice, image: CameraImage) {
            self.delegate?.loadImages { [weak self] in
                self?.delegate?.tableView.reloadData()
                self?.delegate?.refreshControl?.endRefreshing()
            }
        }

    }
    
}
