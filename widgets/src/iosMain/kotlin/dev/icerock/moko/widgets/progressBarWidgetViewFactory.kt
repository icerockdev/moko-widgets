/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.VFC
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleWhite
import platform.UIKit.UIColor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var progressBarWidgetViewFactory: VFC<ProgressBarWidget> = { viewController, widget ->
    val style = widget.style

    // TODO add change of size style
    UIActivityIndicatorView(activityIndicatorStyle = UIActivityIndicatorViewStyleWhite).apply {
        translatesAutoresizingMaskIntoConstraints = false
        color = style.color?.toUIColor() ?: UIColor.darkGrayColor
        startAnimating()
    }
}
