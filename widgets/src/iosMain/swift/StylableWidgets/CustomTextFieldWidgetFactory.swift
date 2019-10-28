//
//  CustomTextFieldWidgetFactory.swift
//  GetChallenge
//
//  Created by Ivan Krylov on 30/08/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

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
