import UIKit
import MultiPlatformLibrary

public enum UIViewGradientFillType {
    case linearPurple
    case linear3Red
}

public enum UIViewGradientDirection: String {
    case leftToRight = "LEFT_RIGHT"
    case rightToLeft = "RIGHT_LEFT"
    case topToBottom = "TOP_BOTTOM"
    case bottomToTop = "BOTTOM_TOP"
    case bottomLToTopR = "BL_TR"
    case bottomRToTopL = "BR_TL"
    case topRToBottomL = "TR_BL"
    case topLToBottomR = "TL_BR"
 
    public init(orientation: Orientation) {
        self = UIViewGradientDirection(rawValue: orientation.name) ?? .leftToRight
    }
}

public extension UIView {
    public func fillWithGradient(direction:UIViewGradientDirection, type:UIViewGradientFillType) -> CALayer {
        switch type {
        case .linearPurple:
            return fillWithGradient(direction:direction, gradientColors:[UIColor(named: "PurpleGradientDark")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "PurpleGradientLight")?.cgColor ?? UIColor.black.cgColor])
        case .linear3Red:
            return fillWithGradient(direction:direction, gradientColors:[UIColor(named: "RedGradientDark")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "RedGradientCenter")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "RedGradientLight")?.cgColor ?? UIColor.black.cgColor])
        }
    }
    
    public func fillingGradientLayer(direction:UIViewGradientDirection, type:UIViewGradientFillType) -> CALayer {
        switch type {
        case .linearPurple:
            return fillingGradientLayer(direction:direction, gradientColors:[UIColor(named: "PurpleGradientDark")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "PurpleGradientLight")?.cgColor ?? UIColor.black.cgColor])
        case .linear3Red:
            return fillingGradientLayer(direction:direction, gradientColors:[UIColor(named: "RedGradientDark")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "RedGradientCenter")?.cgColor ?? UIColor.black.cgColor, UIColor(named: "RedGradientLight")?.cgColor ?? UIColor.black.cgColor])
        }
    }
    
    public func fillWithGradient(direction:UIViewGradientDirection, gradientColors:[CGColor]) -> CALayer {
        let gradientLayer = self.fillingGradientLayer(direction: direction, gradientColors: gradientColors)
        self.layer.insertSublayer(gradientLayer, at: 0)
        return gradientLayer
    }
    
    public func fillingGradientLayer(direction:UIViewGradientDirection, gradientColors:[CGColor]) -> CALayer {
        let gradientLayer = CAGradientLayer()
        gradientLayer.colors = gradientColors
        gradientLayer.frame = self.bounds
        gradientLayer.masksToBounds = true
        var gradientLocations: [NSNumber] = []
        for index in 0...gradientColors.count - 1 {
            let increment = 1.0 / (Double)(gradientColors.count - 1)
            let gradientPoint = increment * (Double)(index)
            gradientLocations.append(NSNumber(value: gradientPoint))
        }
        gradientLayer.locations = gradientLocations
        switch direction {
        case .leftToRight:
            gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.5)
            gradientLayer.endPoint = CGPoint(x: 1.0, y: 0.5)
            break
        case .rightToLeft:
            gradientLayer.startPoint = CGPoint(x: 1.0, y: 0.5)
            gradientLayer.endPoint = CGPoint(x: 0.0, y: 0.5)
            break
        case .topToBottom:
            gradientLayer.startPoint = CGPoint(x: 0.5, y: 0.0)
            gradientLayer.endPoint = CGPoint(x: 0.5, y: 1)
            break
        case .bottomToTop:
            gradientLayer.startPoint = CGPoint(x: 0.5, y: 1)
            gradientLayer.endPoint = CGPoint(x: 0.5, y: 0)
            break
        case .bottomLToTopR:
            gradientLayer.startPoint = CGPoint(x: 0, y: 1)
            gradientLayer.endPoint = CGPoint(x: 1, y: 0)
            break
        case .bottomRToTopL:
            gradientLayer.startPoint = CGPoint(x: 1, y: 1)
            gradientLayer.endPoint = CGPoint(x: 0, y: 0)
            break
        case .topRToBottomL:
            gradientLayer.startPoint = CGPoint(x: 1, y: 0)
            gradientLayer.endPoint = CGPoint(x: 0, y: 1)
            break
        case .topLToBottomR:
            gradientLayer.startPoint = CGPoint(x: 0, y: 0)
            gradientLayer.endPoint = CGPoint(x: 1, y: 1)
        }
        return gradientLayer
    }
}


public extension CAGradientLayer {
    public convenience init(gradientColors:[CGColor]) {
        self.init()
        self.colors = gradientColors
        self.masksToBounds = true
        var gradientLocations: [NSNumber] = []
        for index in 0...gradientColors.count - 1 {
            let increment = 1.0 / (Double)(gradientColors.count - 1)
            let gradientPoint = increment * (Double)(index)
            gradientLocations.append(NSNumber(value: gradientPoint))
        }
        self.locations = gradientLocations
        self.startPoint = CGPoint(x: 0.0, y: 0.5)
        self.endPoint = CGPoint(x: 1.0, y: 0.5)
    }
}

