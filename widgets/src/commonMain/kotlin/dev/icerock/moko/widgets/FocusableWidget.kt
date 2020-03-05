/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.factory.FocusableInputFactory
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef(FocusableInputFactory::class)
class FocusableWidget<WS : WidgetSize>(
    private val factory: ViewFactory<FocusableWidget<out WidgetSize>>,
    override val id: Id?,
    val child: InputWidget<WS>,
    val isFocused: MutableLiveData<Boolean>
) : Widget<WS>(), OptionalId<FocusableWidget.Id> {

    override val size: WS = child.size

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<FocusableWidget<out WidgetSize>>
    interface Category : Theme.Category<FocusableWidget<out WidgetSize>>

    object DefaultCategory : Category
}