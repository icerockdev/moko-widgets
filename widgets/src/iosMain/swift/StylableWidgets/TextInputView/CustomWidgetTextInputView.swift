//
//  WidgetTextInputView.swift
//  GetChallenge
//
//  Created by Ivan Krylov on 30/08/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import Foundation
import UIKit
import MaterialComponents.MaterialContainerScheme
import MaterialComponents.MaterialTextFields_Theming
import MaterialComponents.MaterialTextFields_TypographyThemer
import MaterialComponents.MaterialTextFields_ColorThemer
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm
import MultiPlatformLibraryWidgetsForms
import MultiPlatformLibraryCore

import InputMask

public class CustomWidgetTextInputView: UIView, MaskedTextFieldDelegateListener, UITextFieldDelegate {
    
    let textField: CustomWidgetTextField
    let listener: MaskedTextFieldDelegate
    
    public var onBeginEdit: (() -> (Void))?
    public var onEndEdit: (() -> (Void))?

    public override init(frame: CGRect) {
        textField = CustomWidgetTextField(frame: frame)
        listener = MaskedTextFieldDelegate()
        super.init(frame: frame)
        updateAppereance()
    }
    
    required init?(coder aDecoder: NSCoder) {
        guard let input = CustomWidgetTextField(coder: aDecoder) else { return nil }
        textField = input
        listener = MaskedTextFieldDelegate()
        super.init(coder: aDecoder)
        updateAppereance()
    }
    
    private func updateAppereance() {
        listener.onMaskedTextChangedCallback = { textField, _, _ in
            textField.sendActions(for: UIControl.Event.editingChanged)
            NotificationCenter.default.post(name: UITextField.textDidChangeNotification, object: textField)
        }
        listener.autocompleteOnFocus = false
        textField.delegate = self
        translatesAutoresizingMaskIntoConstraints = false
    }
    
    public func apply(inputType: TextInputType) {
        switch inputType {
        case .plainText:
            break
        case .email:
            self.textField.keyboardType = .emailAddress
            break
        case .password:
            self.textField.isSecureTextEntry = true
            break
        case .date(let mask):
            if let mask = mask {
                self.textField.delegate = listener
                self.listener.delegate = self
                listener.primaryMaskFormat = mask
            }
            self.textField.keyboardType = .numberPad
            break
        case .phone(let mask):
            if let mask = mask {
                self.textField.delegate = listener
                self.listener.delegate = self
                listener.primaryMaskFormat = mask
            }
            self.textField.keyboardType = .phonePad
            break
        case .digits:
            self.textField.keyboardType = .numberPad
            break
        }
    }
    
    public func apply(style: InputWidget.InputStyle) {
        textField.removeFromSuperview()
        
        textField.textColor = UIColor(argbInt: Int(style.textStyle.color))

        textField.font = getFontFrom(style: style.textStyle)

        textField.placeholderInlineColor = UIColor(argbInt: Int(style.textStyle.color))
        textField.placeholderAboveLineColor = UIColor(argbInt: Int(style.labelTextStyle.color))

        textField.selectedColor = UIColor(argbInt: Int(style.underLineColor))
        textField.deselectedColor = UIColor(argbInt: Int(style.underLineColor))
        
        textField.tintColor = UIColor(argbInt: Int(style.textStyle.color))
        
        textField.errorColor = UIColor(argbInt: Int(style.errorTextStyle.color))
        textField.errorFont = getFontFrom(style: style.errorTextStyle)
            
        textField.fixSize(width: nil, height: 50)
        
        fillWith(view: textField, insets: UIEdgeInsets(margins: style.margins))
        
        // TODO: Если придумать как получить середину у текстфилда, то эти строчки не нужны
        // Точней как взять то понятно, просто в момент построения у текстфилда еще нет как такого frame
        // Приходится брать из высоты-константы
        textField.layoutHeight = 50
        textField.layoutPlaceholder()
    }
    
    public func getFontFrom(style: TextStyle) -> UIFont {
        let fontStyle = FontStyle(nameValue: style.fontStyle.name)
        switch fontStyle {
        case .BOLD:
            return UIFont.systemFont(ofSize: CGFloat(style.size)).bold
        case .MEDIUM:
            return UIFont.systemFont(ofSize: CGFloat(style.size), weight: .medium)
        }
    }
    
    public func textFieldDidEndEditing(_ textField: UITextField) {
        onEndEdit?()
    }
    
    public func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        return self.textField.isTextInputEnabled
    }
}
