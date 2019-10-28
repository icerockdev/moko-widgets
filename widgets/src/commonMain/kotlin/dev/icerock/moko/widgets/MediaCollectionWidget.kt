package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.media.Media
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.View
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.Widget

expect var mediaCollectionWidgetViewFactory: VFC<MediaCollectionWidget>

class MediaCollectionWidget(
    private val _factory: VFC<MediaCollectionWidget> = mediaCollectionWidgetViewFactory,
    val type: Media.MediaType,
    val field: FormField<List<Media>, StringDesc>,
    val itemClickListener: (Media) -> Unit,
    val addListener: () -> Unit,
    val deleteListener: (Media) -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)
}

fun FormWidget.mediaCollectionField(
    type: Media.MediaType,
    field: FormField<List<Media>, StringDesc>,
    itemClickListener: (Media) -> Unit,
    addListener: (() -> Unit),
    deleteListener: ((Media) -> Unit)
) {
    val widget = MediaCollectionWidget(
        type = type,
        field = field,
        itemClickListener = itemClickListener,
        addListener = addListener,
        deleteListener = deleteListener
    )
    add(widget)
}