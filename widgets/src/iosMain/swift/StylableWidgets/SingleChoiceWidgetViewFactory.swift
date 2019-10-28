import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ
import RxSwift
import RxCocoa
import SwifterSwift

public class SingleChoiceWidgetViewFactory {
    public static func setup() {
        SingleChoiceWidgetViewFactoryKt.singleChoiceWidgetViewFactory = { (controller, widget) -> UIView in
            let view = SingleChoiceWidgetView(frame: controller.view.bounds)
            view.fixSize(width: nil, height: 50)
            view.titleLabel.bindText(liveData: widget.label.liveData())
            view.apply(style: widget.style)
            let choicesData: NSArray = widget.values.value ?? []
            let choicesDesc: [StringDesc] = choicesData.flatMap({$0 as? StringDesc})
            let choices: [String] = choicesDesc.flatMap({$0.localized()})
            view.setChoices(choices: choices,
                            tintColor: UIColor(argbInt: widget.style.dropDownTextColor),
                            cancelText: widget.cancelLabel.liveData().value?.localized() ?? "Cancel",
                            controller: controller)
            view.bindChoice(liveData: widget.field.data, choices: choices)
            return view
        }
    }
}

extension UIViewController {
    func showChoiceSheet(choices: [String],
                         tintColor: UIColor,
                         cancelText: String,
                         choiceHandler: @escaping ((_ choice: String) -> Void), cancellable: Bool) {
        let alertViewController = UIAlertController(title: nil,
                                                    message: nil,
                                                    preferredStyle: .actionSheet)
        alertViewController.view.tintColor = tintColor
        for choice in choices {
            let choiceAction = UIAlertAction(title: choice,
                                             style: .default,
                                             handler: { [weak self] _ in
                                                choiceHandler(choice)
            })
            alertViewController.addAction(choiceAction)
        }
        if cancellable {
            let cancelAction = UIAlertAction(title: cancelText,
                                             style: .cancel,
                                             handler: nil)
            alertViewController.addAction(cancelAction)
        }
        self.present(alertViewController, animated: true, completion: nil)
    }
}
