/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.utils.applySize
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleWhiteLarge
import platform.UIKit.UIColor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var progressBarWidgetViewFactory: VFC<ProgressBarWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val activityIndicator = UIActivityIndicatorView(activityIndicatorStyle = UIActivityIndicatorViewStyleWhiteLarge)
    activityIndicator.color = style.color?.toUIColor() ?: UIColor.darkGrayColor
    activityIndicator.translatesAutoresizingMaskIntoConstraints = false
    activityIndicator.startAnimating()

    activityIndicator
}
