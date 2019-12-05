/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import platform.UIKit.clipsToBounds
import platform.UIKit.contentMode
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultImageWidgetViewFactory actual constructor(
    style: Style
) : DefaultImageWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ImageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val imageView = UIImageView().apply {
            translatesAutoresizingMaskIntoConstraints = false

            widget.image.bind { it.apply(this) }

            when (style.scaleType) {
                ScaleType.FILL -> this.contentMode =
                    UIViewContentMode.UIViewContentModeScaleAspectFill
                ScaleType.FIT -> this.contentMode =
                    UIViewContentMode.UIViewContentModeScaleAspectFit
            }

            layer.masksToBounds = true
            clipsToBounds = true
        }

        return ViewBundle(
            view = imageView,
            size = size,
            margins = style.margins
        )
    }
}
