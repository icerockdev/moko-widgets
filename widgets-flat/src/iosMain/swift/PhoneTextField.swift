/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import InputMask

@objc public class PhoneTextField: UITextField {
  
  // MARK: - Private
  private var maskedListener: MaskedTextInputListener?
  
  // MARK: - Public Funcs
  func setFormat(_ string: String, prefix prefixString: String?) {
    maskedListener = MaskedTextInputListener()
    maskedListener?.primaryMaskFormat = string
    delegate = maskedListener
  }
}

@objc public class FlatInputField: UIView {
  @objc public private(set) var textField: PhoneTextField!
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    commonInit()
  }
  
  required init?(coder: NSCoder) {
    super.init(coder: coder)
    
    commonInit()
  }
  
  private func commonInit() {
    translatesAutoresizingMaskIntoConstraints = false
    
    textField = PhoneTextField()
    textField.translatesAutoresizingMaskIntoConstraints = false
//
//    if let color = style.textStyle?.color {
//        field.textColor = color.toUIColor()
//    }
//    if let size = style.textStyle?.size {
//        field.font = UIFont.systemFont(ofSize: CGFloat(size.intValue))
//    }
//
//    field.bindTextTwoWay(liveData: widget.field.data)
//    widget.label.addObserver { text in
//        field.placeholder = text?.localized()
//    }
//
//    if let inputType = widget.inputType {
//        switch inputType {
//        case .plainText:
//            break
//        case .password:
//            field.isSecureTextEntry = true
//            break
//        case .email:
//            field.keyboardType = .emailAddress
//            break
//        case .date:
//            break
//        case .phone:
//            field.keyboardType = .phonePad
//            break
//        case .digits:
//            field.keyboardType = .numberPad
//            break
//        default:
//            fatalError("kotlin enum unknown \(inputType)")
//        }
//
//        if let mask = inputType.mask {
//            field.setFormat(mask, prefix: nil)
//        }
//    }
//    backgroundColor = style.backgroundColor?.toUIColor()

    addSubview(textField)
    textField.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 12).isActive = true
    trailingAnchor.constraint(equalTo: textField.trailingAnchor, constant: 12).isActive = true
    bottomAnchor.constraint(equalTo: textField.bottomAnchor, constant: 8).isActive = true
    textField.topAnchor.constraint(equalTo: topAnchor, constant: 8).isActive = true
  }
  
  @objc public func setFormat(_ format: String) {
    textField.setFormat(format, prefix: nil)
  }
}
