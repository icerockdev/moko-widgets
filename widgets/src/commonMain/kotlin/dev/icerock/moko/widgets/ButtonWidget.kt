/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var buttonWidgetViewFactory: VFC<ButtonWidget>

@WidgetDef
class ButtonWidget(
    override val factory: VFC<ButtonWidget>,
    override val style: Style,
    override val id: Id?,
    val text: LiveData<StringDesc>,
    val enabled: LiveData<Boolean>?,
    val onTap: () -> Unit
) : Widget<ButtonWidget>(),
    Styled<ButtonWidget.Style>,
    OptionalId<ButtonWidget.Id> {

    data class Style(
        override val size: WidgetSize = WidgetSize(
            width = SizeSpec.WRAP_CONTENT,
            height = SizeSpec.WRAP_CONTENT
        ),
        override val background: Background? = null,
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null,
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean? = null
    ) : Widget.Style, Margined, Sized, Backgrounded, Padded

    interface Id : WidgetScope.Id
}
