/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemImageViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(SystemImageViewFactory::class)
class ImageWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ImageWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val image: LiveData<Image>,
    @Suppress("RemoveRedundantQualifierName")
    val scaleType: ImageWidget.ScaleType? = null
) : Widget<WS>(), OptionalId<ImageWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<ImageWidget<out WidgetSize>>
    interface Category : Theme.Category<ImageWidget<out WidgetSize>>

    enum class ScaleType {
        FILL,
        FIT
    }

    object DefaultCategory : Category
}
