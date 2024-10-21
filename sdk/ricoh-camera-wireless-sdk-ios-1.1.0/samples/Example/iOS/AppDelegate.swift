// Copyright (c) 2017 Ricoh Co., Ltd. All rights reserved.

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    private let globals = Globals.shared
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        return true
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        guard globals.loadRunningLiveview(), globals.canUseCamera() else {
            return
        }
        
        // Resets liveview status
        _ = globals.cameraDevice?.stopLiveView()
        
        _ = globals.cameraDevice?.startLiveView()
    }
}
