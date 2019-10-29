/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

public class ProgressBarWidgetViewFactory {
  
  public static func setup() {
    // TODO такое вот можно легко в котлине реализовать, чтобы не требовался свифт файл дополнительный и без вызова setup обойтись можно будет
    ProgressBarWidgetViewFactoryKt.progressBarWidgetViewFactory = { (controller, widget) -> UIView in
      let indicator = UIActivityIndicatorView(style: .whiteLarge) // TODO пробрасывать через стиль виджета
      indicator.color = UIColor.gray
      indicator.translatesAutoresizingMaskIntoConstraints = false
      indicator.startAnimating()
      return indicator
    }
  }
}
