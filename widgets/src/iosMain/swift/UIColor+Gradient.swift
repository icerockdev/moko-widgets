/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit

public extension UIColor {
    
    public static func gradientColorFrom(colors: [UIColor], withSize size: CGSize, offset: CGPoint = CGPoint.zero, start: CGPoint = CGPoint.zero, finish: CGPoint? = nil) -> UIColor
    {
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        let context = UIGraphicsGetCurrentContext()
        let colorspace = CGColorSpaceCreateDeviceRGB()
        
        let cfColors = colors.compactMap({$0.cgColor}) as CFArray
        
        let gradient = CGGradient(colorsSpace: colorspace, colors: cfColors, locations: nil)
        
        let startX = start.x + offset.x
        let endX = finish?.x ?? size.width + offset.x
        let startY = start.y + offset.y
        let endY = finish?.y ?? size.height + offset.y
        context!.drawLinearGradient(gradient!, start: CGPoint(x:startX, y:startY), end: CGPoint(x:endX, y:endY), options: CGGradientDrawingOptions(rawValue: UInt32(0)))
        
        let image = UIGraphicsGetImageFromCurrentImageContext()
        
        UIGraphicsEndImageContext()
        
        let finalCColor = UIColor(patternImage: image!)
        return finalCColor
    }
    
}

