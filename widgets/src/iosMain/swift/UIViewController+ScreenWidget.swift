//
//  UIViewController+ScreenWidget.swift
//  Created by Andrey Tchernov on 13/06/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

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
