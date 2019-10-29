/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var dateTimeWidgetViewFactory: VFC<DateTimeWidget>

class DateTimeWidget(
    private val factory: VFC<DateTimeWidget> = dateTimeWidgetViewFactory,
    val orientation: FormWidget.Group.Orientation
) : Widget() {

    val items: MutableList<Widget> = mutableListOf()

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    fun add(widget: Widget) {
        items.add(widget)
    }
}
