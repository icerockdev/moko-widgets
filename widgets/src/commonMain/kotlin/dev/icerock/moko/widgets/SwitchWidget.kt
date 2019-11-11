/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.ColorStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var switchWidgetViewFactory: VFC<SwitchWidget>

@WidgetDef
class SwitchWidget(
    val factory: VFC<SwitchWidget>,
    override val style: Style,
    override val id: Id,
    val state: MutableLiveData<Boolean>
) : Widget(), Styled<SwitchWidget.Style>, RequireId<SwitchWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    /**
     * @property size @see dev.icerock.moko.widgets.style.view.Sized
     * @property padding @see dev.icerock.moko.widgets.style.view.Padded
     * @property margins @see dev.icerock.moko.widgets.style.view.Margined
     * @property background @see dev.icerock.moko.widgets.style.view.Backgrounded
     * @property switchColor switch background, might be null if default
     */
    data class Style(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.WRAP_CONTENT,
            height = SizeSpec.WRAP_CONTENT
        ),
        override val padding: PaddingValues? = null,
        override val margins: MarginValues? = null,
        override val background: Background? = null,
        val switchColor: ColorStyle? = null
    ) : Widget.Style, Sized, Padded, Margined, Backgrounded

    interface Id : WidgetScope.Id
}
