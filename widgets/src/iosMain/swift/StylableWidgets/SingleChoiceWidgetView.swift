/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import MultiPlatformLibrary
import UIKit
import RxSwift
import RxCocoa

class SingleChoiceWidgetView: UILoadableView {
    
    // MARK: - IBOutlets
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak private var choseLabel: UILabel!
    @IBOutlet weak private var leftConstraint: NSLayoutConstraint!
    @IBOutlet weak private var rightConstraint: NSLayoutConstraint!
    @IBOutlet weak private var tapView: UIView!
    
    // MARK: - Private
    private let disposeBag = DisposeBag()
    private let tapGesture = UITapGestureRecognizer()
    
    // MARK: - Overrides
    override var nibName: String {
        return String(describing: type(of: self))
    }
    
    override var bundle: Bundle {
        return Bundle(for: self.classForCoder)
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupOnTap()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupOnTap()
    }
    
    // MARK: - Public Func
    func apply(style: SingleChoiceWidget.SingleChoiceWidgetStyle) {
        leftConstraint.constant = style.margins.start.cgFloat
        rightConstraint.constant = style.margins.end.cgFloat
        
        titleLabel.textColor = UIColor(argbInt: style.labelTextStyle.color)
        choseLabel.textColor = UIColor(argbInt: style.textStyle.color)
        
        titleLabel.font = getFontFrom(style: style.labelTextStyle)
        choseLabel.font = getFontFrom(style: style.textStyle)
    }
    
    func setChoices(choices: [String],
                    tintColor: UIColor,
                    cancelText: String,
                    controller: UIViewController) {
        tapGesture.rx.event.bind(onNext: { [weak self] recognizer in
            controller.showChoiceSheet(choices: choices,
                                       tintColor: tintColor,
                                       cancelText: cancelText,
                                       choiceHandler: { [weak self] choice in
                                        self?.choseLabel.text = choice
                },
                                       cancellable: true)
        }).disposed(by: disposeBag)
    }
    
    // MARK: - Private Func
    private func setupOnTap() {
        tapView.addGestureRecognizer(tapGesture)
    }
    
    public func getFontFrom(style: TextStyle) -> UIFont {
        let fontStyle = FontStyle(nameValue: style.fontStyle.name)
        switch fontStyle {
        case .BOLD:
            return UIFont.systemFont(ofSize: CGFloat(style.size)).bold
        case .MEDIUM:
            return UIFont.systemFont(ofSize: CGFloat(style.size), weight: .medium)
        }
    }
    
}

extension SingleChoiceWidgetView {
    
    func bindChoice(liveData: LiveData<KotlinInt>, choices: [String]) {
        setChoice(index: liveData.value, choices: choices)
        liveData.addObserver { [weak self] index in
            self?.setChoice(index: index, choices: choices)
        }
    }
    
    private func setChoice(index: KotlinInt?, choices: [String]) {
        let intChoice = index as? Int ?? 0
        self.choseLabel.text = choices.indices.contains(intChoice)
            ? choices[intChoice]
            : "internal error: wrong index"
    }
    
}
