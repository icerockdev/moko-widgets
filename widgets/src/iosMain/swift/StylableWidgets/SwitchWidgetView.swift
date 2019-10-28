//
//  SwitchWidgetView.swift
//  GetChallenge
//
//  Created by Stanislav Rachenko on 19/07/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import Foundation
import MultiPlatformLibrary
import MultiPlatformLibraryCore
import MultiPlatformLibraryWidgetsForms

public class SwitchWidgetViewFactory {
    public static func setup() {
        SwitchWidgetViewFactoryKt.switchWidgetViewFactory = { (controller, widget) -> UIView in
            let view = SwitchWidgetView(frame: controller.view.bounds)
            view.titleLabel.bindText(liveData: widget.label.liveData())
            view.switchView.bindValueTwoWay(liveData: widget.state)
            view.fixSize(width: nil, height: 50)
            view.apply(style: widget.style)
            return view
        }
    }
}

class SwitchWidgetView: UILoadableView {
    
    // MARK: - Public
    var switchGradientColor: UIColor?
    
    // MARK: - IBOutlets
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var switchView: UISwitch!
    @IBOutlet weak var rightPadding: NSLayoutConstraint!
    @IBOutlet weak var leftPadding: NSLayoutConstraint!
    
    // MARK: - Overrides
    override var nibName: String {
        return String(describing: type(of: self))
    }
    
    override var bundle: Bundle {
        return Bundle(for: self.classForCoder)
    }
    
    // TODO: На ios 13 какая то проблема с градиентом
//    override func draw(_ rect: CGRect) {
//        super.draw(rect)
//        let leftColor = UIColor(hex: 0x774CF9) ?? UIColor.black
//        let rightColor = UIColor(hex: 0xB83AF3) ?? UIColor.black
//        let gradientColors = [leftColor, rightColor]
//        switchGradientColor = UIColor.gradientColorFrom(colors: gradientColors,
//                                                        withSize: switchView.size)
//        applySwitchColors()
//    }
//
//    override func layoutSubviews() {
//        super.layoutSubviews()
//        applySwitchColors()
//    }
    
    // MARK: - LifeCycle
    convenience init() {
        self.init(frame: CGRect.zero)
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }
    
    // MARK: - Public Func
    func apply(style: SwitchWidget.SwitchStyle) {
        leftPadding.constant = style.margins.start.cgFloat
        rightPadding.constant = style.margins.end.cgFloat
        
        titleLabel.textColor = UIColor(argbInt: style.labelTextStyle.color)
        titleLabel.font = getFontFrom(style: style.labelTextStyle)
    }
    
    // MARK: - Private Func
    public func getFontFrom(style: TextStyle) -> UIFont {
        let fontStyle = FontStyle(nameValue: style.fontStyle.name)
        switch fontStyle {
        case .BOLD:
            return UIFont.systemFont(ofSize: CGFloat(style.size)).bold
        case .MEDIUM:
            return UIFont.systemFont(ofSize: CGFloat(style.size), weight: .medium)
        }
    }
    
    private func setupView() {
        translatesAutoresizingMaskIntoConstraints = false
        applySwitchColors()
    }
    
    func applySwitchColors() {
        switchView.onTintColor = UIColor(hex: 0x774CF9) ?? UIColor.black
        switchView.tintColor = UIColor(hexString: "8466FF")
        switchView.maximizeCornerRadius()
        switchView.layer.masksToBounds = true
    }
}
