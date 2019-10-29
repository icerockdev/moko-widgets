/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var formWidgetViewFactory: VFC<FormWidget>

class FormWidget(
    private val factory: VFC<FormWidget> = formWidgetViewFactory,
    val style: FormStyle,
    val isLoading: LiveData<Boolean>
) : Widget() {

    val items: MutableList<Widget> = mutableListOf()

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    fun add(widget: Widget) {
        items.add(widget)
    }

    data class FormStyle(
        val size: WidgetSize = WidgetSize(SizeSpec.WRAP_CONTENT, SizeSpec.WRAP_CONTENT),
        val orientation: Group.Orientation = Group.Orientation.VERTICAL,
        val spacing: Float = 0.0F,
        override val padding: PaddingValues = PaddingValues(0.0F)
    ) : Padded

    class Group {
        enum class Orientation {
            HORIZONTAL,
            VERTICAL
        }
    }
}
