/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import FloatingPanel

private var AssociatedDelegateHandle: UInt8 = 0

@objc public class BottomSheetController: NSObject, FloatingPanelControllerDelegate {
  
  private weak var controller: FloatingPanelController?
  private var onDismiss: ((Bool) -> Void)?
  
  @objc public func show(
    onViewController vc: UIViewController,
    withContent view: UIView,
    onDismiss: @escaping (Bool) -> Void
  ) {
    view.updateConstraints()
    view.layoutSubviews()
    
    let maxSize = CGSize(
      width: UIScreen.main.bounds.width,
      height: UIView.layoutFittingCompressedSize.height
    )
    view.frame = UIScreen.main.bounds
    
    let prefSize = view.systemLayoutSizeFitting(
      maxSize,
      withHorizontalFittingPriority: UILayoutPriority.required,
      verticalFittingPriority: .defaultHigh
    )
    
    let floatLayout = BottomSheetLayout(
      preferredHeight: prefSize.height
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
    
    view.superview?.layer.maskedCorners = [CACornerMask.layerMinXMinYCorner, CACornerMask.layerMaxXMinYCorner]
    view.superview?.layer.masksToBounds = true
    view.superview?.layer.cornerRadius = 14
    
    controller = fpc
    self.onDismiss = onDismiss
    
    objc_setAssociatedObject(fpc, &AssociatedDelegateHandle, delegate, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN)
    
    vc.present(fpc, animated: true, completion: nil)
  }
  
  @objc public func dismiss() {
    controller?.dismiss(animated: true, completion: nil)
    self.onDismiss?(true)
  }
}

class FloatingDelegate: FloatingPanelControllerDelegate {
  private let floatingLayout: FloatingPanelLayout
  private let onDismiss: (Bool) -> Void
  
  init(floatingLayout: FloatingPanelLayout, onDismiss: @escaping (Bool) -> Void) {
    self.floatingLayout = floatingLayout
    self.onDismiss = onDismiss
  }
  
  func floatingPanel(_ vc: FloatingPanelController, layoutFor newCollection: UITraitCollection) -> FloatingPanelLayout? {
    return floatingLayout
  }
  
  func floatingPanelDidEndDecelerating(_ vc: FloatingPanelController) {
    if vc.position == .hidden {
      vc.removePanelFromParent(animated: true)
      vc.dismiss(animated: false, completion: nil)
      onDismiss(false)
    }
  }
  
  func floatingPanelDidEndRemove(_ vc: FloatingPanelController) {
    onDismiss(false)
  }
}

class BottomSheetLayout: FloatingPanelLayout {
  var initialPosition: FloatingPanelPosition = .full
  
  private let preferredHeight: CGFloat
  
  init(preferredHeight: CGFloat) {
    self.preferredHeight = preferredHeight
  }
  
  func insetFor(position: FloatingPanelPosition) -> CGFloat? {
    switch (position) {
    case .half: return 0
    case .full: return UIScreen.main.bounds.size.height - preferredHeight - 30.0
    case .tip: return 0
    case .hidden: return 0
    }
  }
  
  var supportedPositions: Set<FloatingPanelPosition> = [.full, .hidden]
  
  func backdropAlphaFor(position: FloatingPanelPosition) -> CGFloat {
    return 0.3
  }
}
