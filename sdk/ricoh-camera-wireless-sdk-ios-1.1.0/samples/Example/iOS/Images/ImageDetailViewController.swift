// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit
import RICOHCameraWirelessSDK

class ImageDetailViewController: UIViewController {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var stateLabel: UILabel!
    
    var cameraImage: CameraImage?

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.title = cameraImage?.name
        stateLabel.text = ""
    }

    @IBAction func tappedCloseButton(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }

    @IBAction func tappedGetDataButton(_ sender: Any) {
        updateStateLabel(message: "getting data")
        guard let name = cameraImage?.name else {
            updateStateLabel(message: "error")
            return
        }

        let destinationPath = createFileURL(fineName: name)
        if cameraImage?.format == ImageFormat.jpeg {
            getData(path: destinationPath) { [weak self] result in
                if result == true {
                    self?.showImage(filePath: destinationPath)
                    self?.updateStateLabel(message: "completion")
                } else {
                    self?.updateStateLabel(message: "error")
                }
            }
        } else {
            getData(path: destinationPath) { [weak self] result in
                if result == true {
                    self?.updateStateLabel(message: "completion (show only jpeg image)")
                } else {
                    self?.updateStateLabel(message: "error")
                }
            }
        }
    }

    @IBAction func tappedGetThumbnailButton(_ sender: Any) {
        updateStateLabel(message: "getting thumbnail")
        guard let name = cameraImage?.name else {
            updateStateLabel(message: "error")
            return
        }

        let destinationPath = createFileURL(fineName: name)
        getThumbnail(path: destinationPath) { [weak self] result in
            if result == true {
                self?.showImage(filePath: destinationPath)
                self?.updateStateLabel(message: "completion")
            } else {
                self?.updateStateLabel(message: "error")
            }
        }
    }
 
    func showImage(filePath: String) {
        imageView.image = UIImage(contentsOfFile: filePath)
    }

    func updateStateLabel(message: String) {
        stateLabel.text = message
    }

    private func createFileURL(fineName: String) -> String {
        let dir = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)[0]
        return dir.appendingPathComponent(fineName).path
    }

    private func getData(path: String, completion: ((Bool) -> Void)? = nil) {
        guard let outputStream = OutputStream(toFileAtPath: path, append: false) else {
            completion?(false)
            return
        }

        DispatchQueue.global().async { [weak self] in
            guard let weakSelf = self, let cameraImage = weakSelf.cameraImage else {
                DispatchQueue.main.async { completion?(false) }
                return
            }

            do {
                let response = try cameraImage.getData(outputStream: outputStream)
                if response.result == .ok {
                    DispatchQueue.main.async { completion?(true) }
                } else {
                    DispatchQueue.main.async { completion?(false) }
                }
            } catch {
                DispatchQueue.main.async { completion?(false) }
            }
        }
    }

    private func getThumbnail(path: String, completion: ((Bool) -> Void)? = nil) {
        guard let outputStream = OutputStream(toFileAtPath: path, append: false) else {
            completion?(false)
            return
        }

        DispatchQueue.global().async { [weak self] in
            guard let weakSelf = self, let cameraImage = weakSelf.cameraImage else {
                DispatchQueue.main.async { completion?(false) }
                return
            }

            do {
                let response = try cameraImage.getThumbnail(outputStream: outputStream)
                if response.result == .ok {
                    DispatchQueue.main.async { completion?(true) }
                } else {
                    DispatchQueue.main.async { completion?(false) }
                }
            } catch {
                DispatchQueue.main.async { completion?(false) }
            }
        }
    }

}
