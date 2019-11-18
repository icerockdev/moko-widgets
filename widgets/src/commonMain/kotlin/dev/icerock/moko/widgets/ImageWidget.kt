/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var imageWidgetViewFactory: VFC<ImageWidget>

@WidgetDef
class ImageWidget(
    val factory: VFC<ImageWidget>,
    override val style: Style,
    override val id: Id?,
    override val layoutParams: Widget.LayoutParams,
    val image: LiveData<Image>
) : Widget(), Styled<ImageWidget.Style>, OptionalId<ImageWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class LayoutParams(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        ),
        override val margins: MarginValues? = null
    ) : Widget.LayoutParams, Margined

    data class Style(
        val scaleType: ScaleType? = null
    ) : Widget.Style

    enum class ScaleType {
        FILL,
        FIT
    }

    interface Id : WidgetScope.Id
}
