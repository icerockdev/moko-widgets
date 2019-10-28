package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.media.Bitmap
import com.icerockdev.mpp.core.media.CanceledException
import com.icerockdev.mpp.core.media.MediaController
import com.icerockdev.mpp.core.media.MediaControllerSource
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.core.resources.desc
import com.icerockdev.mpp.coroutines.UI
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.View
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.Widget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

expect var mediaWidgetViewFactory: VFC<MediaWidget>

class MediaWidget(
    private val _factory: VFC<MediaWidget> = mediaWidgetViewFactory,
    val type: MediaFormField.Type,
    val field: FormField<Bitmap?, StringDesc>,
    val onTap: () -> Unit
) : Widget() {
    private val coroutineScope = CoroutineScope(context = UI)

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    fun onWidgetPressed() {
        onTap()
    }
}

fun FormWidget.mediaField(
    type: MediaFormField.Type,
    field: FormField<Bitmap?, StringDesc>,
    onTap: () -> Unit
) {
    val widget = MediaWidget(
        type = type,
        field = field,
        onTap = onTap
    )
    add(widget)
}