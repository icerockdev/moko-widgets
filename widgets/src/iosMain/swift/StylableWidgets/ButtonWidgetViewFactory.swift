import Foundation
import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ
import MaterialComponents.MaterialContainerScheme
import MaterialComponents.MaterialButtons
import MaterialComponents.MaterialButtonBar_ColorThemer
import MultiPlatformLibrary
import MultiPlatformLibraryWidgetsForms

public class ButtonWidgetViewFactory {
    public static func setup() {
        ButtonWidgetViewFactoryKt.buttonWidgetViewFactory = { (controller, widget) -> UIView in
            let view = MaterialButton(frame: controller.view.bounds)
            widget.style.background?.shape.corners.bottomLeft
            view.apply(style: widget.style)
            view.onTap = {
                // Нужно ли держать weak ссылку? бывает widget приходил nil. Попробовать держать strong ссылку и смотреть появится ли баг снова. Если да, то уже смотреть когда он обнуляется. Если нет, убрать проверку на nil
                if widget != nil {
                    widget.onTap()
                }
            }
            
            view.button.bindTitle(liveData: widget.text.liveData())
            if let enabledLiveData = widget.enabled?.liveData() {
                view.bindEnabled(liveData: enabledLiveData)
            }
            return view
        }
    }
}

protocol ButtonStylable {
    func apply(style: ButtonWidget.ButtonStyle)
}

class MaterialButton: UIView, ButtonStylable {
    
    let button: MDCButton
    var isAllCaps = false {
        didSet {
            self.setTitle(string: self.button.titleLabel?.text, for: .normal)
        }
    }
    var onTap: ()->() = {}
    private var backgroundEnabledLayer: CALayer?
    private var backgroundDisabledLayer: CALayer?
    
    override init(frame: CGRect) {
        button = MDCButton(frame: frame)
        button.disabledAlpha = 1
        button.setBackgroundColor(.clear)
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        guard let button = MDCButton(coder: aDecoder) else { return nil }
        self.button = button
        super.init(coder: aDecoder)
        setupView()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        backgroundEnabledLayer?.frame = button.bounds
        backgroundDisabledLayer?.frame = button.bounds
    }
    
    func setupView() {
        self.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(self.tapAction), for: .touchUpInside)
    }

    @objc private func tapAction() {
        self.onTap()
    }
    
    func bindEnabled(liveData: LiveData<KotlinBoolean>) {
        setEnabled(
            enabled: liveData.value
        )
        liveData.addObserver { [weak self] enabled in
            self?.setEnabled(
                enabled: enabled
            )
        }
    }
    
    private func setEnabled(enabled: KotlinBoolean?) {
        button.isEnabled = enabled?.boolValue ?? true
        guard let backgroundEnabledLayer = backgroundEnabledLayer,
            let backgroundDisabledLayer = backgroundDisabledLayer
            else { return }
        
        if enabled?.boolValue == false {
            backgroundEnabledLayer.opacity = 0
            backgroundDisabledLayer.opacity = 1
        } else {
            backgroundEnabledLayer.opacity = 1
            backgroundDisabledLayer.opacity = 0
        }
    }
    
    func setTitle(string: String?, for state: UIControl.State) {
        var title = string
        if isAllCaps {
            title = string?.uppercased()
        } else {
            title = string?.capitalized
        }
        button.setTitle(title, for: .normal)
    }
    
    func applyToTitle(style: TextStyle) {
        let fontStyle = FontStyle(nameValue: style.fontStyle.name)
        self.button.setTitleColor(UIColor(argbInt: style.color), for: .normal)
        self.button.setTitleColor(UIColor(argbInt: style.color), for: .disabled)

        switch fontStyle {
        case .BOLD:
            self.button.setTitleFont(UIFont.systemFont(ofSize: CGFloat(style.size)).bold, for: .normal)
            break
        case .MEDIUM:
            self.button.setTitleFont(UIFont.systemFont(ofSize: CGFloat(style.size), weight: .medium), for: .normal)
            break
        }
    }
    
    func apply(style: ButtonWidget.ButtonStyle) {
        backgroundDisabledLayer?.removeFromSuperlayer()
        backgroundEnabledLayer?.removeFromSuperlayer()
        
        applyToTitle(style: style.textStyle)
        applyBackground(style: style.background)
        button.removeFromSuperview()
        self.fillWith(view: button, insets: UIEdgeInsets(margins: style.margins))
        button.fixSize(width: nil, height: 50)
        button.isUppercaseTitle = style.isAllCaps
        isAllCaps = style.isAllCaps
    }
    
    func applyBackground(style: Background?) {
        if let style = style {
            if let colors = style.colors as? [NSNumber], colors.count > 0 {
                if colors.count == 1 {
                    button.setBackgroundColor(UIColor(argbInt: colors.first!), for: .normal)
                } else if colors.count > 1 {
                   backgroundEnabledLayer = applyGradientBackground(colors: colors,
                                                                    orientation: style.orientation)
                }
            }
            if let colors = style.colorsDisabled as? [NSNumber], colors.count > 0 {
                if colors.count == 1 {
                    button.setBackgroundColor(UIColor(argbInt: colors.first!), for: .disabled)
                } else if colors.count > 1 {
                    backgroundDisabledLayer = applyGradientBackground(colors: colors,
                                                                      orientation: style.orientation)
                }
            }
        } else {
            button.setBackgroundColor(.lightGray, for: .normal)
            button.setBackgroundColor(.lightGray, for: .disabled)
        }
 
        let radius = CGFloat(style?.shape.corners.topLeft ?? 0)
        button.layer.cornerRadius = radius
        button.layer.masksToBounds = radius > 0
    }
    
    func applyGradientBackground(colors: [NSNumber], orientation: Orientation) -> CALayer {
        var gradientColors = colors.flatMap({ UIColor(argbInt: $0).cgColor} )
        let layer = button.fillWithGradient(direction: UIViewGradientDirection(orientation: orientation),
                                        gradientColors: gradientColors)
        self.setNeedsLayout()
        self.layoutIfNeeded()
        return layer
    }
}
