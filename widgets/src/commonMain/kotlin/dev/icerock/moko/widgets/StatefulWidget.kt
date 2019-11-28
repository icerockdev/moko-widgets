/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.data
import dev.icerock.moko.mvvm.livedata.error
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.factory.DefaultStatefulWidgetViewFactory
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class StatefulWidget<WS : WidgetSize, T, E> constructor(
    private val factory: ViewFactory<StatefulWidget<out WidgetSize, *, *>>,
    override val size: WS,
    override val id: Id?,
    val state: LiveData<State<T, E>>,
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

    interface Id : Theme.Id
}

// manual writed boilerplate (plugin can't generate it because Widget use generic)
private object StatefulWidgetFactoryKey :
    Theme.Key<ViewFactory<StatefulWidget<out WidgetSize, *, *>>>

val Theme.statefulFactory: ViewFactory<StatefulWidget<out WidgetSize, *, *>>
        by Theme.readProperty(StatefulWidgetFactoryKey) { DefaultStatefulWidgetViewFactory() }

var Theme.Builder.statefulFactory: ViewFactory<StatefulWidget<out WidgetSize, *, *>>
        by Theme.readWriteProperty(StatefulWidgetFactoryKey, Theme::statefulFactory)

fun <WS : WidgetSize, T, E> Theme.stateful(
    factory: ViewFactory<StatefulWidget<out WidgetSize, *, *>> = this.statefulFactory,
    id: StatefulWidget.Id? = null,
    size: WS,
    state: LiveData<State<T, E>>,
    empty: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    loading: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    data: (LiveData<T?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    error: (LiveData<E?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
): StatefulWidget<WS, T, E> {
    return StatefulWidget(
        factory = factory,
        size = size,
        id = id,
        state = state,
        empty = empty,
        loading = loading,
        data = data,
        error = error
    )
}

fun Theme.getStatefulFactory(id: StatefulWidget.Id): ViewFactory<StatefulWidget<out WidgetSize, *, *>> {
    return getIdProperty(id, StatefulWidgetFactoryKey, ::statefulFactory)
}

fun Theme.Builder.setStatefulFactory(
    factory: ViewFactory<StatefulWidget<out WidgetSize, *, *>>,
    vararg ids: CollectionWidget.Id
) {
    ids.forEach { setIdProperty(it, StatefulWidgetFactoryKey, factory) }
}

fun <WS : WidgetSize, T, E> Theme.stateful(
    id: StatefulWidget.Id? = null,
    size: WS,
    state: LiveData<State<T, E>>,
    empty: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    loading: () -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    data: (LiveData<T?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>,
    error: (LiveData<E?>) -> Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
): StatefulWidget<WS, T, E> {
    return StatefulWidget(
        factory = id?.let { this.getStatefulFactory(it) } ?: this.statefulFactory,
        size = size,
        id = id,
        state = state,
        empty = empty,
        loading = loading,
        data = data,
        error = error
    )
}
