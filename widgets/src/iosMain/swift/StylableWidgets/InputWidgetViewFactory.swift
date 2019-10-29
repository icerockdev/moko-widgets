/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import MultiPlatformLibraryMvvm
import Differ

public class InputWidgetViewFactory {
  public static func setup() {
    InputWidgetViewFactoryKt.inputWidgetViewFactory = { (controller: UIViewController, widget: InputWidget) -> UIView in
      let view = MaterialTextInput(frame: controller.view.bounds)
      view.onFinishEdit = {
        widget.field.validate()
      }
      
      view.textField.bindPlaceholder(liveData: widget.label.liveData())
      view.textField.bindTextTwoWay(liveData: widget.field.data)
      view.controller.bindError(liveData: widget.field.error)
      
      view.apply(style: widget.style)
      view.apply(inputType: TextInputType(type: widget.inputType))
      return view
    }
  }
}

public enum TextInputType {
  case email
  case plainText
  case password
  case date(mask: String?)
  case phone(mask: String?)
  case digits
  
  init(type: InputType) {
    if type == InputType.email { self = .email }
    else if type == InputType.plainText { self = .plainText }
    else if type == InputType.password { self = .password }
    else if type == InputType.date { self = .date(mask: type.mask) }
    else if type == InputType.phone { self = .phone(mask: type.mask) }
    else if type == InputType.digits { self = .digits }
    else { self = .plainText }
  }
}
