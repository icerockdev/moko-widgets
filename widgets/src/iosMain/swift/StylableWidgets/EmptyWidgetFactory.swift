import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryCore
import MaterialComponents.MaterialButtons
import class MaterialComponents.MaterialButtons.MDCButton

public class EmptyWidgetFactory {
    public static func setup(contentViews: [(id: String, contentView: UIView)]) {
        EmptyWidgetFactoryKt.emptyWidgetFactory = { (controller, widget) -> UIView in
            let emptyView = UIView(frame: controller.view.bounds)
            let contentView = contentViews.first{ $0.id == widget.id }?.contentView
            if let contentView = contentView {
                emptyView.fillWith(view: contentView, insets: UIEdgeInsets(top: 16, left: 0, bottom: 0, right: 0))
                emptyView.translatesAutoresizingMaskIntoConstraints = false
            }
            return emptyView
        }
    }
}

public extension UIButton {
    private func actionHandler(action:(() -> Void)? = nil) {
        struct __ { static var action :(() -> Void)? }
        if action != nil { __.action = action }
        else { __.action?() }
    }
    @objc private func triggerActionHandler() {
        self.actionHandler()
    }
    public func actionHandler(controlEvents control :UIControl.Event, ForAction action:@escaping () -> Void) {
        self.actionHandler(action: action)
        self.addTarget(self, action: #selector(triggerActionHandler), for: control)
    }
}
