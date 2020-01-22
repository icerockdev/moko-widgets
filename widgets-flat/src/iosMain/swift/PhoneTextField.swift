/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import InputMask

class PhoneTextField: UITextField {
  
  // MARK: - Private
  private var maskedListener: MaskedTextInputListener?
  
  // MARK: - Public Funcs
  public func setFormat(_ string: String, prefix prefixString: String?) {
    maskedListener = MaskedTextInputListener()
    maskedListener?.primaryMaskFormat = string
    delegate = maskedListener
  }
}
