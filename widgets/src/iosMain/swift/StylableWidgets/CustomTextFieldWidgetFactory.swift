/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import MultiPlatformLibraryMvvm
import Differ

public class CustomTextFieldWidgetFactory {
    public static func setup() {
        InputWidgetViewFactoryKt.inputWidgetViewFactory = { (controller: UIViewController, widget: InputWidget) -> UIView in
            let view = CustomWidgetTextInputView(frame: controller.view.bounds)
            
            view.onEndEdit = {
                widget.field.validate()
            }
            
          
            widget.enabled?.liveData().addObserver(observer: { (isEnable) in
              view.textField.isTextInputEnabled = isEnable?.boolValue ?? true
            })
          
            view.textField.bindTextTwoWay(liveData: widget.field.data)
            view.textField.bindPlaceholder(liveData: widget.label.liveData())
            view.textField.bindError(liveData: widget.field.error)
            view.apply(style: widget.style)
            view.apply(inputType: TextInputType(type: widget.inputType))
            return view
        }
    }
}
