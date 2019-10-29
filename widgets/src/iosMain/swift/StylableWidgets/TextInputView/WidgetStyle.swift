/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryWidgetsForms

enum FontStyle: String  {
    case BOLD = "BOLD"
    case MEDIUM = "MEDIUM"
    init(nameValue: String?) {
        self = FontStyle(rawValue: nameValue ?? "") ?? .MEDIUM
    }
}

enum WidgetSize: String  {
    case AS_PARENT = "AS_PARENT"
    case WRAP_CONTENT = "WRAP_CONTENT"
    init(nameValue: String?) {
        self = WidgetSize(rawValue: nameValue ?? "") ?? .AS_PARENT
    }
}

extension UIColor {
    convenience init(argbInt: Int) {
        let alpha = CGFloat((argbInt & 0xFF000000) >> 24) / 0xFF
        let red = CGFloat((argbInt & 0x00FF0000) >> 16) / 0xFF
        let green = CGFloat((argbInt & 0x0000FF00) >> 8) / 0xFF
        let blue =  CGFloat(argbInt & 0x000000FF) / 0xFF
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    convenience init(argbInt: Int32) {
        self.init(argbInt: Int(argbInt))
    }
    
    convenience init(argbInt: NSNumber) {
        self.init(argbInt: argbInt.int32Value)
    }
}

extension UIEdgeInsets {
    init(margins: MarginValues) {
        self.init(top: CGFloat(margins.top), left: CGFloat(margins.start), bottom: CGFloat(margins.bottom), right: CGFloat(margins.end))
    }
}
