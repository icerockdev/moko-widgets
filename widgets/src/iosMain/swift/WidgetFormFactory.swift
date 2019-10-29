/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit
import MultiPlatformLibrary

public class WidgetFormFactory {
  
  public static func setup() {
    FormWidgetViewFactoryKt.formWidgetViewFactory = { (controller, widget) -> UIView in
        let mainScroll = UIScrollView(frame: controller.view.bounds)
        let mainStack = UIStackView(frame: controller.view.bounds)
        mainScroll.keyboardDismissMode = .onDrag
        mainStack.alignment = .fill
        mainStack.distribution = .equalSpacing
        mainStack.axis = .vertical
        mainStack.spacing = CGFloat(widget.style.spacing)
        
        mainScroll.fillScrollWith(view: mainStack, scrollDirection: .vertical, insets: UIEdgeInsets(top: 0, left: 16, bottom: 16, right: 16))
        mainScroll.alwaysBounceVertical = true
        for item in widget.items {
            if let widget = item as? Widget {
                let widgetView = widget.buildView(viewFactoryContext: controller)
                mainStack.addArrangedSubview(widgetView)
           }
        }
        return mainScroll
    }
  }
}
