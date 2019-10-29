/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ

public class HeaderWidgetViewFactory {
    public static func setup() {
        HeaderWidgetViewFactoryKt.headerWidgetViewFactory = { (controller, widget) -> UIView in
            let view = HeaderView(frame: CGRect(x: 0, y: 0, width: 0, height: 40))

            view.label.bindText(liveData: widget.text.liveData(), formatter: { string in
                return string?.uppercased()
            })
            view.apply(style: widget.style)
            // TODO Необходимо спроектировать стили для всех видов виджетов. Иначе придётся всегда хардкодить размеры.
            view.fixSize(width: nil, height: 40)
            view.layoutSubviews()
            return view
        }
    }
}

protocol HeaderStylable {
    func apply(style: HeaderWidget.HeaderStyle)
}

class HeaderView: UIView, HeaderStylable {
    
    let label: UILabel
    
    override init(frame: CGRect) {
        label = UILabel(frame: frame)
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        guard let label = UILabel(coder: aDecoder) else { return nil }
        self.label = label
        super.init(coder: aDecoder)
        setupView()
    }
    
    func setupView() {
        self.translatesAutoresizingMaskIntoConstraints = true
        label.fixSize(width: nil, height: 20)
    }
    
    func apply(style: HeaderWidget.HeaderStyle) {
        // TODO: apply style
        // WidgetSize(nameValue: style.size.first?.name) //width
        // WidgetSize(nameValue: style.size.second?.name) //height
        style.textStyle.size
        // style.textStyle.color
        // FontStyle(nameValue: style.textStyle.fontStyle.name)

        label.removeFromSuperview()
        let fontStyle = FontStyle(nameValue: style.textStyle.fontStyle.name)
        self.label.textColor = UIColor(argbInt: style.textStyle.color)
        switch fontStyle {
        case .BOLD:
            self.label.font = UIFont.systemFont(ofSize: CGFloat(14)).bold
            break
        case .MEDIUM:
            self.label.font = UIFont.systemFont(ofSize: CGFloat(14))
            break
        }
        self.fillWith(view: label, pinning: [.left, .bottom, .right])
    }
}
