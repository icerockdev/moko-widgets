/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedImageView
import dev.icerock.moko.widgets.core.widget.ImageWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.dp

actual class SystemImageViewFactory actual constructor(
    private val margins: MarginValues?,
    private val cornerRadius: Float?
) : ViewFactory<ImageWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ImageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val imageView = RoundedImageView(context)

        when (widget.scaleType) {
            ImageWidget.ScaleType.FILL -> imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            ImageWidget.ScaleType.FIT -> imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            null -> Unit
        }
        @Suppress("ComplexCondition")
        if ((size is WidgetSize.Const<*, *> &&
                    (size.width is SizeSpec.WrapContent || size.height is SizeSpec.WrapContent)) ||
            (size is WidgetSize.AspectByWidth<*> && size.width is SizeSpec.WrapContent) ||
            (size is WidgetSize.AspectByHeight<*> && size.height is SizeSpec.WrapContent)
        ) {
            imageView.adjustViewBounds = true
        }

        widget.image.bind(lifecycleOwner) { image ->
            if (image == null) {
                imageView.setImageDrawable(null)
                return@bind
            }

            image.loadIn(imageView)

            cornerRadius?.also {
                imageView.cornerRadius = cornerRadius.dp(context)
            }
        }

        return ViewBundle(
            view = imageView,
            size = size,
            margins = margins
        )
    }
}
