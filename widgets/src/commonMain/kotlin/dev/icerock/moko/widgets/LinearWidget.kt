/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var linearWidgetViewFactory: VFC<LinearWidget>

@WidgetDef
class LinearWidget(
    override val factory: VFC<LinearWidget>,
    override val style: Style,
    override val id: Id?,
    val childs: List<AnyWidget>
) : Widget<LinearWidget>(),
    Styled<LinearWidget.Style>,
    OptionalId<LinearWidget.Id> {

    data class Style(
        val background: Background? = null,
        val orientation: Orientation = Orientation.VERTICAL,
        val size: WidgetSize = WidgetSize(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        ),
        override val padding: PaddingValues = PaddingValues()
    ) : Padded

    interface Id : WidgetScope.Id
}
