package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import platform.UIKit.UIView

actual var scrollWidgetViewFactory: VFC<ScrollWidget> = { _, _ ->
    UIView()
}