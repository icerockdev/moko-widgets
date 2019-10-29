/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ
import class MultiPlatformLibrary.Date

// TODO MEDIUM надо вернуть в вариант с диалогами - инпуты теперь через InputWidget с типом Date можно
public class DateWidgetViewFactory {
    public static func setup() {
        DateWidgetViewFactoryKt.dateWidgetViewFactory = { (controller, widget) -> UIView in
            let view = DateWidgetView(frame: controller.view.bounds)
            view.onFinishEdit = { widget.field.validate() }
          
            view.textField.bindPlaceholder(liveData: widget.label.liveData())
            view.controller.bindError(liveData: widget.field.error)
            view.textField.bindDate(liveData: widget.field.data)
          
            view.apply(style: widget.style)
            return view
        }
    }
}

protocol DateWidgetViewStylable {
    func apply(style: DateWidget.DateFieldStyle)
}

class DateWidgetView: MaterialTextInput, DateWidgetViewStylable {
    func apply(style: DateWidget.DateFieldStyle) {
        self.textField.removeFromSuperview()
        self.fillWith(view: textField, insets: UIEdgeInsets(margins: style.margins))
        controller.borderFillColor = .white
        textField.textColor = UIColor(argbInt: style.textStyle.color)
        controller.normalColor = UIColor(argbInt: style.underLineColor)
        controller.activeColor = UIColor(argbInt: style.underLineColor)
        controller.floatingPlaceholderActiveColor = UIColor(argbInt: style.textStyle.color)
    }
}

extension UITextField {
  func bindDate(liveData: MutableLiveData<Date>) {
    setDate(date: liveData.value)
    liveData.addObserver { [weak self] date in
      self?.setDate(date: date)
    }
  }
  
  private func setDate(date: Date?) {
    if let day = date?.day, let month = date?.month, let year = date?.year {
      let dateString = "\(day).\(month).\(year)"
      self.text = dateString
    }
  }
}
