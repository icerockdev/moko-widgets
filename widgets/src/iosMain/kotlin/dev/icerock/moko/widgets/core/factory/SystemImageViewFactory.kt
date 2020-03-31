/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.widget.ImageWidget
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import platform.UIKit.clipsToBounds
import platform.UIKit.contentMode
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemImageViewFactory actual constructor(
    private val margins: MarginValues?,
    private val cornerRadius: Float?
) : ViewFactory<ImageWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ImageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val imageView = UIImageView().apply {
            translatesAutoresizingMaskIntoConstraints = false

            widget.image.bind { image ->
                image.apply(this) { this.image = it }
            }

            when (widget.scaleType) {
                ImageWidget.ScaleType.FILL -> this.contentMode =
                    UIViewContentMode.UIViewContentModeScaleAspectFill
                ImageWidget.ScaleType.FIT -> this.contentMode =
                    UIViewContentMode.UIViewContentModeScaleAspectFit
            }

            layer.cornerRadius = cornerRadius?.toDouble() ?: 0.0
            layer.masksToBounds = true
            clipsToBounds = true
        }

        return ViewBundle(
            view = imageView,
            size = size,
            margins = margins
        )
    }
}
