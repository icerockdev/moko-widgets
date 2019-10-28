package com.icerockdev.mpp.widgets.forms

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.mvvm.livedata.map
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetMediaBinding

actual var mediaWidgetViewFactory: VFC<MediaWidget> = { context: ViewFactoryContext,
                                                        widget: MediaWidget ->

    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetMediaBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_media, parent, false)

    binding.widget = widget

    binding.widget
    binding.selectedPhoto = widget.field.data.map {
        if (it != null) {
            BitmapDrawable(ctx.resources, it.platformBitmap)
        } else {
           null as Drawable?
        }
    }.ld()

    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}
