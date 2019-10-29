/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

public class CenterWidgetViewFactory {
  
  public static func setup() {
    // TODO такое вот можно легко в котлине реализовать, чтобы не требовался свифт файл дополнительный и без вызова setup обойтись можно будет
    CenterWidgetViewFactoryKt.centerWidgetViewFactory = { (controller, widget) -> UIView in
      let childView = widget.child.buildView(viewFactoryContext: controller)
      
      let rootView = UIView(frame: CGRect.zero)
      rootView.translatesAutoresizingMaskIntoConstraints = false
      let centerX = NSLayoutConstraint.init(item: childView, attribute: .centerX, relatedBy: .equal, toItem: rootView, attribute: .centerX, multiplier: 1, constant: 0)
      let centerY = NSLayoutConstraint.init(item: childView, attribute: .centerY, relatedBy: .equal, toItem: rootView, attribute: .centerY, multiplier: 1, constant: 0)
      rootView.addSubview(childView)
      NSLayoutConstraint.activate([centerX, centerY])
      
      return rootView
    }
  }
}
