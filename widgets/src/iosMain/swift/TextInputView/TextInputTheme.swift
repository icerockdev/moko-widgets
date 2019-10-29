/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import MaterialComponents.MaterialContainerScheme
import MaterialComponents.MaterialTextFields_Theming
import MaterialComponents.MaterialTextFields_TypographyThemer
import MaterialComponents.MaterialTextFields_ColorThemer



class TextInputTheme {
    func schemeTheme() {
        
        let input = MDCTextField(frame: CGRect.zero)
        
        let controller = MDCTextInputControllerFilled(textInput: input)
        
        self.applySchemeTheme(inputController: controller)
        self.applyTypographyTheme()
        self.applyColorTheme()
        
    }
    
    
    
    func applySchemeTheme(inputController: MDCTextInputControllerFilled) {
        
        let containerScheme = MDCContainerScheme()
        
        // containerScheme.
        
        inputController.applyTheme(withScheme: containerScheme)
        
    }
    
    func applyColorTheme() {
        
        let textInput = MDCTextField(frame: CGRect.zero)
        
        let inputController = MDCTextInputControllerFilled(textInput: textInput)
        
        
        
        let colorScheme = MDCSemanticColorScheme()
        
          // colorScheme.asdf
        
        // Applying to a text field
        
        MDCTextFieldColorThemer.apply(colorScheme as! MDCColorScheme, to: inputController)
        
        
        
        // Applying to an input controller
        
        MDCTextFieldColorThemer.applySemanticColorScheme(colorScheme, to: inputController)
        
        
        
        // Applying to a specific class type of inputController
        
        MDCTextFieldColorThemer.apply(colorScheme,
                                      
                                      toAllControllersOfClass: MDCTextInputControllerUnderline.self)
    }
    
    func applyTypographyTheme() {
        
        let textInput = MDCTextField(frame: CGRect.zero)
        
        let inputController = MDCTextInputControllerFilled(textInput: textInput)
        
        let typographyScheme = MDCTypographyScheme()
        
        //   MDCTextFieldTypographyThemer.apply(typographyScheme, to: textInput)
        
        //    MDCTextFieldTypographyThemer.apply(typographyScheme, to: inputController)
        
        MDCTextFieldTypographyThemer.apply(typographyScheme,
                                           
                                           toAllControllersOfClass: MDCTextInputControllerUnderline.self)
        
    }
    
    
    
}



//extension MDCTextInputController {

//    func applySchemeTheme() {

//        let containerScheme = MDCContainerScheme()

//        self.applyTheme(withScheme: containerScheme)

//    }

//    func applyColorTheme() {

//        let colorScheme = MDCSemanticColorScheme()

//        // Applying to a text field

//        MDCTextFieldColorThemer.apply(colorScheme as! MDCColorScheme, to: self.textInput)

//

//        // Applying to an input controller

//        MDCTextFieldColorThemer.applySemanticColorScheme(colorScheme, to: inputController)

//

//        // Applying to a specific class type of inputController

//        MDCTextFieldColorThemer.applySemanticColorScheme(colorScheme,

//                                                         toAllControllersOfClass: MDCTextInputControllerUnderline.self)

//

//

//    }

//    func applyTypographyTheme() {

//        let typographyScheme = MDCTypographyScheme()

//        MDCTextFieldTypographyThemer.apply(typographyScheme, to: textInput as! MDCTextInput)

//        MDCTextFieldTypographyThemer.apply(typographyScheme, to: self)

//        MDCTextFieldTypographyThemer.apply(typographyScheme,

//                                           toAllControllersOfClass: MDCTextInputControllerUnderline.self)

//    }

//

//}

//
