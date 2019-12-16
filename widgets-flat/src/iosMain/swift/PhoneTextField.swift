/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import AnyFormatKit
import UIKit

class PhoneTextField: UITextField {
  
  // MARK: - Private
  private var formatter: TextInputFormatter?
  private var prefix: String? {
    didSet {
      text = prefix
    }
  }
  
  // MARK: - LifeCycle
  override init(frame: CGRect) {
    super.init(frame: frame)
    setupView()
  }
  
  required init?(coder aDecoder: NSCoder) {
    super.init(coder: aDecoder)
    setupView()
  }
  
  private func setupView() {
    delegate = self
  }
  
  // MARK: - Public Funcs
  public func setFormat(_ string: String, prefix prefixString: String?) {
    formatter = DefaultTextInputFormatter(textPattern: string)
    prefix = prefixString
  }
}

extension PhoneTextField: UITextFieldDelegate {
  func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
    if let prefix = prefix, range.location < prefix.count {
      setCursorLocation(prefix.count)
      return false
    } else if let formatter = formatter {
      let result = formatter.formatInput(currentText: textField.text ?? "", range: range, replacementString: string)
      textField.text = result.formattedText
      textField.setCursorLocation(result.caretBeginOffset)
      textField.sendActions(for: .editingChanged)
      return false
    }
    return true
  }
}

private extension UITextField {
  func setCursorLocation(_ location: Int) {
    if let cursorLocation = position(from: beginningOfDocument, offset: location) {
      DispatchQueue.main.async { [weak self] in
        guard let strongSelf = self else { return }
        strongSelf.selectedTextRange = strongSelf.textRange(from: cursorLocation, to: cursorLocation)
      }
    }
  }
}
