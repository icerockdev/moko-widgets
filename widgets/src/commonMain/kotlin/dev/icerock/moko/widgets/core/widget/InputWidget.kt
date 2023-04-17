/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.fields.core.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemInputViewFactory
import dev.icerock.moko.widgets.core.style.input.InputType
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@Suppress("LongParameterList")
@WidgetDef(SystemInputViewFactory::class)
class InputWidget<WS : WidgetSize>(
    private val factory: ViewFactory<InputWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id,
    val label: LiveData<StringDesc>,
    val field: FormField<String, StringDesc>,
    val enabled: LiveData<Boolean>?,
    val inputType: InputType?,
    val maxLines: LiveData<Int?>?
) : Widget<WS>(), RequireId<InputWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<InputWidget<out WidgetSize>>
    interface Category : Theme.Category<InputWidget<out WidgetSize>>

    object DefaultCategory : Category
}
