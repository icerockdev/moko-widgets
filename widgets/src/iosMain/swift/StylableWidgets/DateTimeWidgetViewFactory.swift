/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

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

