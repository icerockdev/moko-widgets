//
//  FlatAlertWidgetView.swift
//  Created by Andrey Tchernov on 14/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit

class FlatAlertWidgetView: UILoadableView {

  override var nibName: String {
    return "FlatAlertWidgetView"
  }
  override var bundle: Bundle { return Bundle(for: self.classForCoder) }

  var onButtonTapped: (() -> Void)?

  @IBOutlet private weak var messageLabel: UILabel!
  @IBOutlet private weak var actionButton: UIButton!
  @IBOutlet private weak var iconImageView: UIImageView!

  override func awakeFromNib() {
    super.awakeFromNib()
    iconImageView.image = iconImageView.image?.withRenderingMode(.alwaysTemplate)
  }

  func setMessage(_ message: String) {
    self.messageLabel.text = message
  }

  func setButtonTitle(_ title: String) {
    actionButton.setTitle(title, for: .normal)
  }

  func setButtonVisible(_ visible: Bool) {
    actionButton.isHidden = !visible
  }

  @IBAction private func onButtonTap(_ sender: UIButton!) {
    onButtonTapped?()
  }
}

extension UIImageView {
  @IBInspectable var imageTintColor: UIColor! {
    set {
      tintColor = newValue
    }
    get {
      return tintColor
    }
  }
}
