//
//  CustomWidgetTextField.swift
//
//  Created by Developer on 27/06/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//
import MultiPlatformLibrary
import QuartzCore
import UIKit

public class CustomWidgetTextField: UITextField {
    
    // MARK: - Public
    public var isTextInputEnabled: Bool = true
    public var layoutHeight: CGFloat?
    
    private var _placeholder: String?
    override public var placeholder: String? {
        get {
            return _placeholder
        }
        set {
            _placeholder = newValue
            layoutPlaceholder()
        }
    }
    
    override public var text: String? {
        get {
            return super.text
        }
        set {
            super.text = newValue
            if !isFirstResponder {
                layoutPlaceholder()
            }
        }
    }
    
    public func layoutPlaceholder() {
        setAttributedStringForTextLayerWith(color:
            (text?.isEmpty == true) ? placeholderInlineColor : placeholderAboveLineColor)
        
        let size = placeholderTextLayer.preferredFrameSize()
        let heightPlaceholder = size.height
        let widthPlaceholder = size.width
        if text?.isEmpty == true {
            var originY: CGFloat = 0
            if let layoutHeight = layoutHeight {
                originY = (layoutHeight - heightPlaceholder) * 0.5
            } else {
                originY = bounds.midY - heightPlaceholder * 0.5
            }
            placeholderTextLayer.frame = CGRect(x: 0,
                                                y: originY,
                                                width: widthPlaceholder,
                                                height: heightPlaceholder)
        } else {
            if placeholderTextLayer.transform.m11 != scale {
                placeholderTextLayer.transform = CATransform3DScale(placeholderTextLayer.transform,
                                                                    scale, scale, 1)
            }
            placeholderTextLayer.frame = CGRect(x: 0,
                                                y: -heightPlaceholder * 0.5,
                                                width: widthPlaceholder,
                                                height: heightPlaceholder)
        }
    }
    
    public var errorText: String? {
        didSet {
            guard let errorLabel = errorLabel else { return }
            errorLabel.text = errorText
            errorLabel.sizeToFit()
            let labelHeight = errorLabel.frame.height
            let labelWidth = errorLabel.frame.width
            errorLabel.frame = CGRect(x: self.frame.width - labelWidth,
                                      y: -labelHeight * 0.5,
                                      width: labelWidth,
                                      height: labelHeight)
        }
    }
    
    public var scale: CGFloat = 0.75
    
    // Selected/Deselected Colors
    public var selectedColor = UIColor.black
    public var deselectedColor = UIColor.gray {
        didSet {
            shape.fillColor = deselectedColor.cgColor
        }
    }
    
    // MARK: - Private
    private var placeholderTextLayer: CATextLayer!
    private var shape: CAShapeLayer!
    private weak var errorLabel: UILabel?
    
    // NotValid
    public var errorColor = UIColor.red {
        didSet {
            errorLabel?.textColor = errorColor
        }
    }
    public var errorFont = UIFont.systemFont(ofSize: 12) {
        didSet {
            errorLabel?.font = errorFont
        }
    }
    
    // PlaceholderColors
    public var placeholderInlineColor = UIColor.black
    public var placeholderAboveLineColor = UIColor.gray
    
    private var duration: Double = 0.25
    
    // MARK: - LifeCycle
    public override init(frame: CGRect) {
        super.init(frame: frame)
        updateAppearance()
    }
    
    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        updateAppearance()
    }
    
    // MARK: - Private
    private func updateAppearance() {
        
        tintColor = UIColor.black
        font = UIFont.systemFont(ofSize: 12)
        textColor = .black
        
        shape = CAShapeLayer()
        shape.fillColor = deselectedColor.cgColor
        layer.insertSublayer(shape, at: 0)
        
        placeholderTextLayer = CATextLayer()
        placeholderTextLayer.alignmentMode = .left
        placeholderTextLayer.contentsScale = UIScreen.main.scale
        placeholderTextLayer.backgroundColor = UIColor.clear.cgColor
        placeholderTextLayer.anchorPoint = CGPoint(x: 0, y: 0)
        layer.addSublayer(placeholderTextLayer)
        
        let label = UILabel()
        label.textAlignment = .right
        label.textColor = errorColor
        label.font = errorFont
        addSubview(label)
        errorLabel = label
        
        addTarget(self, action: #selector(startEditing), for: .editingDidBegin)
        addTarget(self, action: #selector(finishEditing), for: .editingDidEnd)

    }
    
    private func setAttributedStringForTextLayerWith(color: UIColor) {
        if let font = font {
            let attrStr = NSMutableAttributedString(string: _placeholder ?? "")
            attrStr.addAttribute(.foregroundColor,
                                 value: color,
                                 range: NSRange(location: 0, length: attrStr.length))
            attrStr.addAttribute(.font,
                                 value: font,
                                 range: NSRange(location: 0, length: attrStr.length))
            placeholderTextLayer.string = attrStr
        }
    }
    
    // MARK: - Overrides
    override public func layoutSubviews() {
        super.layoutSubviews()
        let bezierPath = UIBezierPath(rect: CGRect(x: 0,
                                                   y: self.frame.height - 1,
                                                   width: self.frame.width,
                                                   height: 1)).cgPath
        shape.path = bezierPath
        
    }
    
    @objc
    public func startEditing() {
        animateAppereanceWith(duration: duration, isBegin: true)
    }
    
    @objc
    public func finishEditing() {
        animateAppereanceWith(duration: duration, isBegin: false)
    }
}

// MARK: - Animation
extension CustomWidgetTextField {
    func animateAppereanceWith(duration: Double, isBegin: Bool) {
        CATransaction.begin()
        CATransaction.setAnimationDuration(duration)
        
        if isBegin {
            if text?.isEmpty == true {
                setAttributedStringForTextLayerWith(color: placeholderAboveLineColor)
                if placeholderTextLayer.transform.m11 != scale {
                    placeholderTextLayer.transform = CATransform3DScale(placeholderTextLayer.transform,
                                                                        scale, scale, 1)
                }
                placeholderTextLayer.frame.origin.y = -placeholderTextLayer.frame.height * 0.5
            }
        } else {
            shape.fillColor = deselectedColor.cgColor
            if text?.isEmpty == true {
                setAttributedStringForTextLayerWith(color: placeholderInlineColor)
                placeholderTextLayer.transform = CATransform3DIdentity
                var originY: CGFloat = 0
                if let layoutHeight = layoutHeight {
                    originY = (layoutHeight - placeholderTextLayer.frame.height) * 0.5
                } else {
                    originY = bounds.midY - placeholderTextLayer.frame.height * 0.5
                }
                placeholderTextLayer.frame.origin.y = originY
            }
        }
        CATransaction.commit()
    }
}
