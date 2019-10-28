//
//  EmptyWidgetView.swift
//  Created by Stas Rybakov on 19/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

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
