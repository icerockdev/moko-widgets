/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit

public extension UIView {
  func fillContainer(_ container: UIView, spacings: UIEdgeInsets = .zero) {
    self.translatesAutoresizingMaskIntoConstraints = false
    
    self.leftAnchor.constraint(equalTo: container.leftAnchor, constant: spacings.left).isActive = true
    self.topAnchor.constraint(equalTo: container.topAnchor, constant: spacings.top).isActive = true
    self.rightAnchor.constraint(equalTo: container.rightAnchor, constant: -spacings.right).isActive = true
    self.bottomAnchor.constraint(equalTo: container.bottomAnchor, constant: -spacings.bottom).isActive = true
  }
  
  func setChildFullfill(_ childView: UIView) {
    let equalAttributes: [NSLayoutConstraint.Attribute] = [.width, .height, .centerX, .centerY]
    NSLayoutConstraint.activate(
      equalAttributes.map({ NSLayoutConstraint(item: self, attribute: $0, relatedBy: .equal, toItem: childView, attribute: $0, multiplier: 1, constant: 0) })
    )
  }
  
  func makeSized(_ newSize: CGSize) {
    let wc = NSLayoutConstraint(item: self, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: newSize.width)
    let hc = NSLayoutConstraint(item: self, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: newSize.height)
    NSLayoutConstraint.activate([wc, hc])
  }
  
  func setSafeAreaFullFill(_ childView: UIView) {
    
    childView.translatesAutoresizingMaskIntoConstraints = false
    layoutMargins = UIEdgeInsets.zero
    NSLayoutConstraint.activate([
      NSLayoutConstraint(item: childView, attribute: .top, relatedBy: .equal, toItem: safeAreaLayoutGuide, attribute: .top, multiplier: 1.0, constant: 0),
      NSLayoutConstraint(item: childView, attribute: .bottom, relatedBy: .equal, toItem: safeAreaLayoutGuide, attribute: .bottom, multiplier: 1.0, constant: 0),
      self.centerXAnchor.constraint(equalToSystemSpacingAfter: childView.centerXAnchor, multiplier: 1.0),
      self.widthAnchor.constraint(equalTo: childView.widthAnchor, multiplier: 1.0, constant: 0)
      ])
  }
}
