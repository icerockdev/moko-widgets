/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

public class FlatAlertWidgetViewFactory {
  
  public static func setup() {
    FlatAlertWidgetViewFactoryKt.flatAlertWidgetViewFactory = { (controller, widget) -> UIView in
      // TODO вью от проекта должна быть предоставлена
      let view = FlatAlertWidgetView(frame: CGRect.zero)
      view.translatesAutoresizingMaskIntoConstraints = false
      widget.message.addObserver(observer: { [weak view] message in
        view?.setMessage(message?.localized() ?? "")
      })
      
      widget.buttonText.addObserver(observer: { [weak view] text in
        view?.setButtonVisible(text != nil)
        if let text = text {
          view?.setButtonTitle(text.localized())
        }
      })

      view.onButtonTapped = {
        let _ = widget.onTap()
      }
      
      return view
    }
  }
}
