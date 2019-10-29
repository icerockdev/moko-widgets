/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit

class EmptyWidgetView: UILoadableView {
  
  @IBOutlet var titleLabel: UILabel!
  
  override var nibName: String {
    return "EmptyWidgetView"
  }
  override var bundle: Bundle { return Bundle(for: self.classForCoder) }
  
  func setTitle(_ title: String) {
    titleLabel.text = title
  }
}
