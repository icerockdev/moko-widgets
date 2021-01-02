/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.InputLengthViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(InputLengthViewFactory::class)
class InputLengthWidget<WS : WidgetSize>(
    private val factory: ViewFactory<InputLengthWidget<out WidgetSize>>,
    override val id: Id?,
    val child: InputWidget<WS>,
    val maxLength: LiveData<Int?>
) : Widget<WS>(), OptionalId<InputLengthWidget.Id> {
    override val size: WS = child.size

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<InputLengthWidget<out WidgetSize>>
    interface Category : Theme.Category<InputLengthWidget<out WidgetSize>>

    object DefaultCategory : Category
}
