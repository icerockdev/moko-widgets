/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var mediaWidgetViewFactory: VFC<MediaWidget> = { context: ViewFactoryContext,
                                                        widget: MediaWidget ->
    TODO()
//    val ctx = context.context
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetMediaBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_media, parent, false)
//
//    binding.widget = widget
//
//    binding.widget
//    binding.selectedPhoto = widget.field.data.map {
//        if (it != null) {
//            BitmapDrawable(ctx.resources, it.platformBitmap)
//        } else {
//            null as Drawable?
//        }
//    }.ld()
//
//    binding.lifecycleOwner = context.lifecycleOwner
//    binding.root
}
