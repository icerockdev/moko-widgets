/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import FloatingPanel

private var AssociatedDelegateHandle: UInt8 = 0

@objc public class DateBottomSheetController: NSObject, FloatingPanelControllerDelegate {

  private weak var controller: FloatingPanelController?
  private var onDismiss: ((Bool) -> Void)?

  @objc public func show(
    onViewController vc: UIViewController,
    withContent view: UIView,
    onDismiss: @escaping (Bool) -> Void
  ) {
    view.updateConstraints()
    view.layoutSubviews()

    let maxSize = CGSize(width: UIScreen.main.bounds.width, height: UIView.layoutFittingCompressedSize.height)
    view.frame = UIScreen.main.bounds

    let floatLayout = BottomSheetLayout(
        preferredHeight: 250.0
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
    fpc.surfaceView.grabberHandle.isHidden = true
    fpc.surfaceView.grabberHandleSize.height = 0
    fpc.surfaceView.grabberHandlePadding = 0
    //fpc.surfaceView.contentInsets = .zero

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

    func floatingPanel(_ vc: FloatingPanelController, layoutFor newCollection: UITraitCollection) -> FloatingPanelLayout {
    return floatingLayout
  }

  func floatingPanelDidEndDecelerating(_ vc: FloatingPanelController) {
    if vc.state == .hidden {
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
    private let preferredHeight: CGFloat
    
    var position: FloatingPanelPosition {
        return .bottom
    }
    
    var initialState: FloatingPanelState {
        return .half
    }
    
    var anchors: [FloatingPanelState : FloatingPanelLayoutAnchoring] {
        return [
            .half: FloatingPanelLayoutAnchor(absoluteInset: preferredHeight, edge: .bottom, referenceGuide: .safeArea)
        ]
    }
    
    init(preferredHeight: CGFloat) {
        self.preferredHeight = preferredHeight
    }
    
    func prepareLayout(surfaceView: UIView, in view: UIView) -> [NSLayoutConstraint] {
        return [
            surfaceView.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 0.0),
            surfaceView.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: 0.0),
        ]
    }
    
    func backdropAlpha(for state: FloatingPanelState) -> CGFloat {
        return  0.3
    }
}
