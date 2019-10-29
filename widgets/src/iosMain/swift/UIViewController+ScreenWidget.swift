/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

public extension UIViewController {
    func attachScreenWidget(_ widget: ScreenWidget) {
        attachScreenWidget(widget, toView: view)
    }
    
    func attachScreenWidget(_ widget: ScreenWidget, toView widgetContainer: UIView) {
        let widgetView = widget.build().buildView(viewFactoryContext: self)
        widgetContainer.addSubview(widgetView)
        widgetContainer.setSafeAreaFullFill(widgetView)
        updateViewConstraints()
    }
}
