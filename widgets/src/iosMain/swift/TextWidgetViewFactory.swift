/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

public class TextWidgetViewFactory {
  
  public static func setup() {
    // TODO такое вот можно легко в котлине реализовать, чтобы не требовался свифт файл дополнительный и без вызова setup обойтись можно будет
    TextWidgetViewFactoryKt.textWidgetViewFactory = { (controller, widget) -> UIView in
      let label = UILabel(frame: CGRect.zero)
      label.translatesAutoresizingMaskIntoConstraints = false
      widget.text.liveData().addObserver(observer: { [weak label] text in
        label?.text = text?.localized()
      })
      return label
    }
  }
}
