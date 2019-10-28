//
//  EmptyWidgetViewFactory.swift
//  Created by Stas Rybakov on 20/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryCore

public class EmptyWidgetViewFactory {
  
  public static func setup() {
    EmptyAlertWidgetViewFactoryKt.emptyAlertWidgetViewFactory = { (controller, widget) -> UIView in
      // TODO вью от проекта должна быть предоставлена
      let emptyView = EmptyWidgetView(frame: controller.view.bounds)
      emptyView.translatesAutoresizingMaskIntoConstraints = false
      widget.message.addObserver(observer: { message in
        emptyView.setTitle(message?.localized() ?? "")
      })
      
      return emptyView
    }
  }
}
