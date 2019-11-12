/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.localized
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UILabel
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var textWidgetViewFactory: VFC<TextWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val label = UILabel(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
    label.translatesAutoresizingMaskIntoConstraints = false

    widget.text.bind {
        label.text = it.localized()
    }

    label
}
