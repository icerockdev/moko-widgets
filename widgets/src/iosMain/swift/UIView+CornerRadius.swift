/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import Foundation
import UIKit

public extension UIView {
    
    @IBInspectable var extCornerRadius: CGFloat {
        get {
            return layer.cornerRadius
        }
        set {
            layer.cornerRadius = newValue
            layer.masksToBounds = newValue > 0
            updateExtShadow()
        }
    }
    
    @IBInspectable var extBorderColor: UIColor {
        get {
            return UIColor(cgColor: layer.borderColor!)
        }
        set {
            layer.borderColor = newValue.cgColor
        }
    }
    
    @IBInspectable var extBorderWidth: CGFloat {
        get {
            return layer.borderWidth
        }
        set {
            layer.borderWidth = newValue
        }
    }
    
    open override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
    }
    
    public func maximizeCornerRadius() {
        self.extCornerRadius = min(self.frame.width/2, self.frame.height/2)
    }
}


extension UIView{
    @IBInspectable var extShadowColor: UIColor? {
        get {
            if let color = extShadowLayer().shadowColor {
                return UIColor(cgColor: color)
            }
            return nil
        }
        set {
            extShadowLayer().shadowColor = newValue?.cgColor
        }
    }
    @IBInspectable var extShadowOpacity: Float {
        get {
            return extShadowLayer().shadowOpacity
        }
        set {
            extShadowLayer().shadowOpacity = newValue
        }
    }
    @IBInspectable var extShadowOffset: CGSize {
        get {
            return extShadowLayer().shadowOffset
        }
        set {
            extShadowLayer().shadowOffset = newValue
        }
    }
    @IBInspectable var extShadowRadius: CGFloat {
        get {
            return extShadowLayer().shadowRadius
        }
        set {
            extShadowLayer().shadowRadius = newValue
        }
    }
 
    public func extShadowLayer() -> CAShapeLayer {
        var shadowLayer: CAShapeLayer?
        for item in self.layer.sublayers ?? [] {
            if item.name == "extShadowLayer" {
                shadowLayer = item as? CAShapeLayer
            }
        }
        guard let existingShadowLayer = shadowLayer else {
            let newShadowLayer = CAShapeLayer()
            newShadowLayer.name = "extShadowLayer"
            newShadowLayer.path = UIBezierPath(roundedRect: self.bounds, cornerRadius: layer.cornerRadius).cgPath
            newShadowLayer.fillColor = UIColor.clear.cgColor
            newShadowLayer.shadowPath = newShadowLayer.path
            layer.insertSublayer(newShadowLayer, at: 0)
            return newShadowLayer
        }
        return existingShadowLayer
    }
    
    public func updateExtShadow() {
        let shadowLayer = extShadowLayer()
            shadowLayer.removeFromSuperlayer()
            shadowLayer.shadowPath = UIBezierPath(roundedRect: self.bounds, cornerRadius: extCornerRadius).cgPath
            shadowLayer.didChangeValue(forKey: "path")
        shadowLayer.frame = self.bounds
            layer.insertSublayer(shadowLayer, at: 0)
            layer.setNeedsLayout()
            layer.layoutIfNeeded()
    }
}
