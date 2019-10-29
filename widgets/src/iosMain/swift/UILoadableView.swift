/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit

open class UILoadableView: UIView {

  @IBOutlet weak fileprivate var view: UIView!

  open var nibName: String { return "UILoadableView" } //Not exists, must override
  open var bundle: Bundle { return Bundle.main } //can override in specific cases

  public required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    xibSetUp()
  }

  public override init(frame: CGRect) {
    super.init(frame: frame)
    xibSetUp()
  }

  private func xibSetUp() {
    // setup the view from .xib
    view = loadViewFromNib()
    view.frame = self.bounds
    view.autoresizingMask = [UIView.AutoresizingMask.flexibleWidth, UIView.AutoresizingMask.flexibleHeight]
    addSubview(view)
  }
  
  private func loadViewFromNib() -> UIView {
    // grabs the appropriate bundle
    let nib = UINib(nibName: nibName, bundle: bundle)
    print(nib.description)
    let view = nib.instantiate(withOwner: self, options: nil)[0] as? UIView ?? UIView()
    print(view.description)
    return view
  }
}
