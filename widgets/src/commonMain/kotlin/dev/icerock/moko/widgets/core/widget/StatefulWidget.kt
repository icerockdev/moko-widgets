/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.data
import dev.icerock.moko.mvvm.livedata.error
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.StatefulViewFactory
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@Suppress("LongParameterList")
@WidgetDef(StatefulViewFactory::class)
class StatefulWidget<WS : WidgetSize, T, E> constructor(
    private val factory: ViewFactory<StatefulWidget<out WidgetSize, *, *>>,
    override val size: WS,
    override val id: Id?,
    val state: LiveData<ResourceState<T, E>>,
    empty: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    loading: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    data: (LiveData<T?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    error: (LiveData<E?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
) : Widget<WS>(), OptionalId<StatefulWidget.Id> {

    val emptyWidget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> = empty()
    val loadingWidget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> = loading()
    val dataWidget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> =
        data(state.data())
    val errorWidget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> =
        error(state.error())

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<StatefulWidget<out WidgetSize, *, *>>
    interface Category : Theme.Category<StatefulWidget<out WidgetSize, *, *>>

    object DefaultCategory : Category
}
