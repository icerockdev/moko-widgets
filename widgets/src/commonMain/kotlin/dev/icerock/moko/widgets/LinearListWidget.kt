/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var linearListWidgetViewFactory: VFC<LinearListWidget>

@WidgetDef
class LinearListWidget(
    private val factory: VFC<LinearListWidget>,
    val style: Style,
    val id: Id?,
    val items: LiveData<List<UnitItem>>,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: ((completion: () -> Unit) -> Unit)?
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View = factory(viewFactoryContext, this)

    data class Style(
        val orientation: Orientation = Orientation.VERTICAL,
        val reversed: Boolean = false,
        val size: WidgetSize = WidgetSize(),
        val background: Background? = null,
        override val padding: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues()
    ) : Padded, Margined

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    interface Id : WidgetScope.Id
}
