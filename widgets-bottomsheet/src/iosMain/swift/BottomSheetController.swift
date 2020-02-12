/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import FloatingPanel

private var AssociatedDelegateHandle: UInt8 = 0

@objc public class BottomSheetController: NSObject, FloatingPanelControllerDelegate {
  
  @objc public func show(
    onViewController vc: UIViewController,
    withContent view: UIView,
    onDismiss: @escaping () -> Void
  ) {
    view.updateConstraints()
    view.layoutSubviews()
  
    let maxSize = CGSize(width: UIScreen.main.bounds.width, height: UIView.layoutFittingCompressedSize.height)
    view.frame = UIScreen.main.bounds

    let floatLayout = BottomSheetLayout(
      preferredHeight: view.systemLayoutSizeFitting(
        UIView.layoutFittingCompressedSize,
        withHorizontalFittingPriority: UILayoutPriority.required,
        verticalFittingPriority: .defaultLow).height
    )
    
    let delegate = FloatingDelegate(
      floatingLayout: floatLayout,
      onDismiss: onDismiss
    )
    
    let contentVC = UIViewController()
    contentVC.view = view
    let fpc = FloatingPanelController()
    fpc.set(contentViewController: contentVC)
    fpc.delegate = delegate
    fpc.backdropView.backgroundColor = UIColor.black
    fpc.isRemovalInteractionEnabled = true
    
    objc_setAssociatedObject(fpc, &AssociatedDelegateHandle, delegate, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN)
    
    vc.present(fpc, animated: true, completion: nil)
  }
}

class FloatingDelegate: FloatingPanelControllerDelegate {
  private let floatingLayout: FloatingPanelLayout
  private let onDismiss: () -> Void
  
  init(floatingLayout: FloatingPanelLayout, onDismiss: @escaping () -> Void) {
    self.floatingLayout = floatingLayout
    self.onDismiss = onDismiss
  }
  
  func floatingPanel(_ vc: FloatingPanelController, layoutFor newCollection: UITraitCollection) -> FloatingPanelLayout? {
    return floatingLayout
  }
  
  func floatingPanelDidEndRemove(_ vc: FloatingPanelController) {
    onDismiss()
  }
}

class BottomSheetLayout: FloatingPanelLayout {
  var initialPosition: FloatingPanelPosition = .half
  
  private let preferredHeight: CGFloat
  
  init(preferredHeight: CGFloat) {
    self.preferredHeight = preferredHeight
  }
  
  func insetFor(position: FloatingPanelPosition) -> CGFloat? {
    switch (position) {
    case .half: return preferredHeight
    case .full: return 0
    case .tip: return 0
    case .hidden: return nil
    }
  }
  
  var supportedPositions: Set<FloatingPanelPosition> = [.half, .tip]
  
  func backdropAlphaFor(position: FloatingPanelPosition) -> CGFloat {
    return 0.3
  }
}
