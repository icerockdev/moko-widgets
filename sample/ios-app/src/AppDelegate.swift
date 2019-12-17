/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import mokoWidgetsFlat

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        
        let app = App(widgetsPlatformDeps: self)
        app.setup()
        
        let screen = app.createRootScreen()
        let rootViewController = screen.createViewController()
        
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = rootViewController
        window?.makeKeyAndVisible()
        
        return true
    }
}

extension AppDelegate: AppWidgetsPlatformDeps {
    func createFlatInputWidgetView(
        widget: InputWidget<WidgetSize>,
        viewController: UIViewController,
        style: FlatInputViewFactory.Style
    ) -> UIView {
        return FlatInputPlatformDeps().createFlatInputWidgetView(
            widget: widget,
            viewController: viewController,
            style: style
        )
    }
}
