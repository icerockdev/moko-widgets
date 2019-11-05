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
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var containerWidgetViewFactory: VFC<ContainerWidget>

@WidgetDef
class ContainerWidget(
    override val factory: VFC<ContainerWidget>,
    override val style: Style,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    val childs: Map<AnyWidget, ContainerWidget.ChildSpec>
) : Widget<ContainerWidget>(),
    Styled<ContainerWidget.Style>,
    OptionalId<ContainerWidget.Id> {

    data class Style(
        override val size: WidgetSize = WidgetSize(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        ),
        override val padding: PaddingValues? = null,
        override val margins: MarginValues? = null,
        override val background: Background? = null
    ) : Widget.Style, Sized, Padded, Margined, Backgrounded

    interface Id : WidgetScope.Id

    data class ChildSpec(
        val alignment: Alignment
    )
}