/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import InputMask
import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

public class FlatInputPlatformDeps: NSObject, FlatInputViewFactoryPlatformDependency {
    public func createFlatInputWidgetView(
        widget: InputWidget<WidgetSize>,
        viewController: UIViewController,
        style: FlatInputViewFactory.Style
    ) -> UIView {
        let background = UIView()
        background.translatesAutoresizingMaskIntoConstraints = false
        
        let field = PhoneTextField()
        field.translatesAutoresizingMaskIntoConstraints = false
        
        if let color = style.textStyle?.color {
            field.textColor = color.toUIColor()
        }
        if let size = style.textStyle?.size {
            field.font = UIFont.systemFont(ofSize: CGFloat(size.intValue))
        }
        
        field.bindTextTwoWay(liveData: widget.field.data)
        widget.label.addObserver { text in
            field.placeholder = text?.localized()
        }
        
        if let inputType = widget.inputType {
            switch inputType {
            case .plainText:
                break
            case .password:
                field.isSecureTextEntry = true
                break
            case .email:
                field.keyboardType = .emailAddress
                break
            case .date:
                break
            case .phone:
                field.keyboardType = .phonePad
                break
            case .digits:
                field.keyboardType = .numberPad
                break
            default:
                fatalError("kotlin enum unknown \(inputType)")
            }
            
            if let mask = inputType.mask {
                field.setFormat(mask, prefix: nil)
            }
        }

        background.addSubview(field)
        field.leadingAnchor.constraint(equalTo: background.leadingAnchor, constant: 12).isActive = true
        background.trailingAnchor.constraint(equalTo: field.trailingAnchor, constant: 12).isActive = true
        background.bottomAnchor.constraint(equalTo: field.bottomAnchor, constant: 8).isActive = true
        field.topAnchor.constraint(equalTo: background.topAnchor, constant: 8).isActive = true
        background.backgroundColor = style.backgroundColor?.toUIColor()

        return background
    }
}
