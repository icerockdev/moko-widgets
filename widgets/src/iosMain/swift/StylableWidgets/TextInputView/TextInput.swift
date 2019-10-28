import Foundation
import UIKit
import MaterialComponents.MaterialContainerScheme
import MaterialComponents.MaterialTextFields_Theming
import MaterialComponents.MaterialTextFields_TypographyThemer
import MaterialComponents.MaterialTextFields_ColorThemer
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm
import MultiPlatformLibraryWidgetsForms
import InputMask

protocol InputStylable {
  func apply(style: InputWidget.InputStyle)
}

protocol InputTyped {
  func apply(inputType: TextInputType)
}

extension MDCSemanticColorScheme: InputStylable {
  convenience init(style: InputWidget.InputStyle) {
    self.init()
    self.apply(style: style)
  }
  
  func apply(style: InputWidget.InputStyle) {
    // style.underLineColor
    //
    //        primaryColor = UIColor(argbInt: Int(style.textStyle.color))
    //        primaryColorVariant = UIColor.yellow
    //        secondaryColor = UIColor(argbInt: Int(style.labelTextStyle.color))
    //        errorColor = UIColor(argbInt: Int(style.errorTextStyle.color))
    //        surfaceColor = UIColor.white
    //        backgroundColor = UIColor.white
    //        onPrimaryColor = UIColor.red
    //        onSecondaryColor = UIColor.green
    //       // onSurfaceColor = UIColor.blue
    //        onBackgroundColor = UIColor.purple
    //
  }
}

extension MDCShapeScheme: InputStylable {
  convenience init(style: InputWidget.InputStyle) {
    self.init()
    self.apply(style: style)
  }
  
  func apply(style: InputWidget.InputStyle) {
    //TODO: shape styling
    //TODO: size
    //let size: WidgetSize = WidgetSize.init(nameValue: style.size.width.name ?? "")
    //        switch size {
    //        case .AS_PARENT :
    //            break
    //        case .WRAP_CONTENT:
    //            break
    //        }
  }
}

extension MDCTypographyScheme: InputStylable {
  convenience init(style: InputWidget.InputStyle) {
    self.init()
    self.apply(style: style)
  }
  
  func apply(style: InputWidget.InputStyle) {
    //TODO: fonts styling
    let fontStyle: FontStyle = FontStyle.init(nameValue: style.errorTextStyle.fontStyle.name ?? "")
    switch fontStyle {
    case .BOLD :
      break
    case .MEDIUM:
      break
    }
    
    //  style.background.
  }
}

public class MaterialTextInput: UIView, InputStylable, InputTyped, MaskedTextFieldDelegateListener, UITextFieldDelegate  {
  let textField: MDCTextField
  let controller: MDCTextInputControllerFilled
  let listener: MaskedTextFieldDelegate
  var onFinishEdit: (()->())? = nil
  
  public override init(frame: CGRect) {
    textField = MDCTextField(frame: frame)
    controller = MDCTextInputControllerFilled(textInput: textField)
    listener = MaskedTextFieldDelegate()
    super.init(frame: frame)
    setupView()
  }
  
  required init?(coder aDecoder: NSCoder) {
    guard let input = MDCTextField(coder: aDecoder) else { return nil }
    textField = input
    controller = MDCTextInputControllerFilled(textInput: textField)
    listener = MaskedTextFieldDelegate()
    super.init(coder: aDecoder)
    setupView()
  }
  
  func setupView() {
    listener.onMaskedTextChangedCallback = { textField, _, _ in
      textField.sendActions(for: UIControl.Event.editingChanged)
      NotificationCenter.default.post(name: UITextField.textDidChangeNotification, object: textField)
    }
    // нужно чтобы не ломалась анимация плейсхолдера у телефона (автокомплит не верно кидает нотификации)
    listener.autocompleteOnFocus = false
    textField.delegate = self
    self.translatesAutoresizingMaskIntoConstraints = false
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
    let containerScheme = MDCContainerScheme()
    containerScheme.shapeScheme = MDCShapeScheme(style: style)
    containerScheme.typographyScheme = MDCTypographyScheme(style: style)
    controller.applyTheme(withScheme: containerScheme)
    textField.removeFromSuperview()
    controller.borderFillColor = .white
    textField.textColor = UIColor(argbInt: style.textStyle.color)
    controller.normalColor = UIColor(argbInt: style.underLineColor)
    controller.activeColor = UIColor(argbInt: style.underLineColor)
    controller.errorColor = UIColor(argbInt: style.errorTextStyle.color)
    controller.floatingPlaceholderActiveColor = UIColor(argbInt: style.textStyle.color)
    controller.textInsets(UIEdgeInsets.zero)
    
    applyFont(style: style.textStyle)
    self.fillWith(view: textField, insets: UIEdgeInsets(margins: style.margins))
  }
  
  func applyFont(style: TextStyle) {
    let fontStyle = FontStyle(nameValue: style.fontStyle.name)
    switch fontStyle {
    case .BOLD:
      self.textField.font = UIFont.systemFont(ofSize: CGFloat(style.size)).bold
      break
    case .MEDIUM:
      self.textField.font = UIFont.systemFont(ofSize: CGFloat(style.size), weight: .medium)
      break
    }
  }
  
  // TODO: Заменить на удобную конфигурвцию стиля на нативе. Сейчас нет возможности нормально инициализировать стили.
  public func applyDefaultStyle() {
    let containerScheme = MDCContainerScheme()
    containerScheme.shapeScheme = MDCShapeScheme()
    containerScheme.typographyScheme = MDCTypographyScheme()
    controller.applyTheme(withScheme: containerScheme)
    textField.removeFromSuperview()
    controller.borderFillColor = .white
    textField.textColor = UIColor(red: 38, green: 38, blue: 40)
    controller.normalColor = UIColor(red: 221, green: 221, blue: 221)
    controller.activeColor = UIColor(red: 221, green: 221, blue: 221)
    controller.errorColor = UIColor(red: 255, green: 102, blue: 102)
    controller.floatingPlaceholderActiveColor = UIColor(red: 38, green: 38, blue: 40)
    controller.textInsets(UIEdgeInsets.zero)
    self.fillWith(view: textField)
  }
  
  public func textFieldDidBeginEditing(_ textField: UITextField) {
    NotificationCenter.default.post(name: UITextField.textDidBeginEditingNotification, object: textField)
  }
  
  public func textFieldDidEndEditing(_ textField: UITextField, reason: UITextField.DidEndEditingReason) {
    onFinishEdit?()
  }
}


