/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.withSize
import dev.icerock.moko.widgets.utils.bind

actual var imageWidgetViewFactory: VFC<ImageWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = widget.style

    val imageView = ImageView(context)

    when (style.scaleType) {
        ImageWidget.ScaleType.FILL -> imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageWidget.ScaleType.FIT -> imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
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

    imageView.withSize(widget.layoutParams.size).apply { applyStyle(style) }
}