/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.ProgressBarWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleWhite
import platform.UIKit.UIColor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultProgressBarWidgetViewFactory actual constructor(
    style: Style
) : DefaultProgressBarWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ProgressBarWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val activityIndicator = UIActivityIndicatorView(
            activityIndicatorStyle = UIActivityIndicatorViewStyleWhite
        ).apply {
            translatesAutoresizingMaskIntoConstraints = false
            color = style.color?.toUIColor() ?: UIColor.darkGrayColor
            startAnimating()
        }

        return ViewBundle(
            view = activityIndicator,
            size = size,
            margins = style.margins
        )
    }
}