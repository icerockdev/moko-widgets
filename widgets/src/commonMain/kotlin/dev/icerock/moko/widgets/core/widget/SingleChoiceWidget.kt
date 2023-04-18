/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.fields.livedata.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemSingleChoiceViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@Suppress("LongParameterList")
@WidgetDef(SystemSingleChoiceViewFactory::class)
class SingleChoiceWidget<WS : WidgetSize>(
    private val factory: ViewFactory<SingleChoiceWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id,
    val field: FormField<Int?, StringDesc>,
    val label: LiveData<StringDesc>,
    val cancelLabel: LiveData<StringDesc>,
    val values: LiveData<List<StringDesc>>
) : Widget<WS>(), RequireId<SingleChoiceWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<SingleChoiceWidget<out WidgetSize>>
    interface Category : Theme.Category<SingleChoiceWidget<out WidgetSize>>

    object DefaultCategory : Category
}
