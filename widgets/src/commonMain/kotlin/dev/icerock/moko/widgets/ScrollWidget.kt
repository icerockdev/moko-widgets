/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var scrollWidgetViewFactory: VFC<ScrollWidget>

@WidgetDef
class ScrollWidget(
    val factory: VFC<ScrollWidget>,
    override val style: Style,
    override val id: Id?,
    override val layoutParams: LayoutParams,
    val orientation: Orientation = Orientation.VERTICAL,
    val child: Widget
) : Widget(), Styled<ScrollWidget.Style>, OptionalId<ScrollWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class LayoutParams(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        ),
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null
    ) : Widget.LayoutParams, Margined, Padded

    data class Style(
        override val background: Background? = null
    ) : Widget.Style, Backgrounded

    interface Id : WidgetScope.Id
}