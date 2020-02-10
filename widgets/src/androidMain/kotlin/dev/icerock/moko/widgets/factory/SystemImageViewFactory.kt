/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import com.google.android.material.shape.MaterialShapeDrawable
import com.makeramen.roundedimageview.RoundedImageView
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.CornerRadiusValue
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp


actual class SystemImageViewFactory actual constructor(
    private val margins: MarginValues?,
    private val cornerRadiusValue: CornerRadiusValue? // TODO: implement radius apply to image
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
        }

        widget.image.bind(lifecycleOwner) { image ->
            if (image == null) {
                imageView.setImageDrawable(null)
                return@bind
            }

            image.loadIn(imageView)

            if (cornerRadiusValue != null) {
                imageView.cornerRadius = cornerRadiusValue.radius.dp(context)
            }
        }

        return ViewBundle(
            view = imageView,
            size = size,
            margins = margins
        )
    }
}
