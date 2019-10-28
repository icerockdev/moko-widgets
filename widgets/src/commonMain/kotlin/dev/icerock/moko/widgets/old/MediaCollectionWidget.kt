package dev.icerock.moko.widgets.old

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.media.Media
import dev.icerock.moko.media.MediaType
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var mediaCollectionWidgetViewFactory: VFC<MediaCollectionWidget>

class MediaCollectionWidget(
    private val factory: VFC<MediaCollectionWidget> = mediaCollectionWidgetViewFactory,
    val type: MediaType,
    val field: FormField<List<Media>, StringDesc>,
    val itemClickListener: (Media) -> Unit,
    val addListener: () -> Unit,
    val deleteListener: (Media) -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
