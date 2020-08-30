/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemSwitchViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(SystemSwitchViewFactory::class)
class SwitchWidget<WS : WidgetSize>(
    private val factory: ViewFactory<SwitchWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id,
    val state: MutableLiveData<Boolean>
) : Widget<WS>(), RequireId<SwitchWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<SwitchWidget<out WidgetSize>>
    interface Category : Theme.Category<SwitchWidget<out WidgetSize>>

    object DefaultCategory : Category
}
