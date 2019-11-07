/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var progressBarWidgetViewFactory: VFC<ProgressBarWidget>

@WidgetDef
class ProgressBarWidget(
    override val factory: VFC<ProgressBarWidget>,
    override val style: Style,
    override val id: Id?
) : Widget<ProgressBarWidget>(),
    Styled<ProgressBarWidget.Style>,
    OptionalId<ProgressBarWidget.Id> {

    data class Style(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.WRAP_CONTENT,
            height = SizeSpec.WRAP_CONTENT
        ),
        override val padding: PaddingValues? = null,
        override val margins: MarginValues? = null,
        val color: Color? = null
    ) : Widget.Style, Padded, Margined, Sized

    interface Id : WidgetScope.Id
}