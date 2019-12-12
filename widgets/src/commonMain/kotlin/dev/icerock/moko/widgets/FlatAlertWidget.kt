/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.style.view.WidgetSize

// TODO replace to combination of linear, text, button?
@WidgetDef
class FlatAlertWidget<WS : WidgetSize>(
    private val factory: ViewFactory<FlatAlertWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val title: LiveData<StringDesc?>?,
    val message: LiveData<StringDesc?>,
    val drawable: LiveData<ImageResource?>?,
    val buttonText: LiveData<StringDesc?>?,
    val onTap: (() -> Unit)?
) : Widget<WS>(), OptionalId<FlatAlertWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<FlatAlertWidget<out WidgetSize>>
    interface Category : Theme.Category<FlatAlertWidget<out WidgetSize>>

    object DefaultCategory : Category
}
