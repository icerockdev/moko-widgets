/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import FloatingPanel

@objc public class BottomSheetController: NSObject {
  @objc public func show(onViewController vc: UIViewController, withContent view: UIView) {
    let contentVC = UIViewController()
    contentVC.view = view
    let fpc = FloatingPanelController()
    fpc.set(contentViewController: contentVC)
    fpc.isRemovalInteractionEnabled = true
    vc.present(fpc, animated: true, completion: nil)
  }
}
