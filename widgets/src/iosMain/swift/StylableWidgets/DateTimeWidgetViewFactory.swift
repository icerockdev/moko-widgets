import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ

public class DateTimeWidgetViewFactory {
    public static func setup() {
        DateTimeWidgetViewFactoryKt.dateTimeWidgetViewFactory = { (controller, widget) -> UIView in
            let view = UILabel(frame: CGRect.zero)
            view.text = "DateTimeWidgetViewFactory stub"
            return view
        }
    }
}

