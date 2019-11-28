/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class DefaultImageWidgetViewFactory actual constructor(
    style: Style
) : DefaultImageWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ImageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val imageView = ImageView(context).apply {
            applyStyle(style)
        }

        when (style.scaleType) {
            ScaleType.FILL -> imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            ScaleType.FIT -> imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        widget.image.bind(lifecycleOwner) { image ->
            if (image == null) {
                imageView.setImageDrawable(null)
                return@bind
            }

            Glide.with(imageView)
                .let { image.loadIn(it) }
                .into(imageView)
        }

        return ViewBundle(
            view = imageView,
            size = size,
            margins = style.margins
        )
    }
}
