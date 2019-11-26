/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.data
import dev.icerock.moko.mvvm.livedata.error
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>>

class StatefulWidget<T, E> private constructor(
    val factory: VFC<StatefulWidget<T, E>>,
    override val style: Style,
    override val id: Id?,
    val stateLiveData: LiveData<State<T, E>>,
    val emptyWidget: Widget,
    val loadingWidget: Widget,
    val dataWidget: Widget,
    val errorWidget: Widget
) : Widget(), Styled<StatefulWidget.Style>, OptionalId<StatefulWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    constructor(
        factory: VFC<StatefulWidget<T, E>>,
        id: Id?,
        style: Style,
        stateLiveData: LiveData<State<T, E>>,
        emptyWidget: () -> Widget,
        loadingWidget: () -> Widget,
        dataWidget: (LiveData<T?>) -> Widget,
        errorWidget: (LiveData<E?>) -> Widget
    ) : this(
        factory = factory,
        id = id,
        style = style,
        stateLiveData = stateLiveData,
        emptyWidget = emptyWidget(),
        loadingWidget = loadingWidget(),
        dataWidget = dataWidget(stateLiveData.data()),
        errorWidget = errorWidget(stateLiveData.error())
    )

    data class Style(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.AsParent,
            height = SizeSpec.AsParent
        ),
        override val background: Background? = null
    ) : Widget.Style, Sized, Backgrounded

    internal object FactoryKey : WidgetScope.Key<VFC<StatefulWidget<*, *>>>
    internal object StyleKey : WidgetScope.Key<Style>

    interface Id : WidgetScope.Id
}

val WidgetScope.statefulFactory: VFC<StatefulWidget<*, *>>
        by WidgetScope.readProperty(StatefulWidget.FactoryKey, ::statefulWidgetViewFactory)

var WidgetScope.Builder.statefulFactory: VFC<StatefulWidget<*, *>>
        by WidgetScope.readWriteProperty(StatefulWidget.FactoryKey, WidgetScope::statefulFactory)

val WidgetScope.statefulStyle: StatefulWidget.Style
        by WidgetScope.readProperty(StatefulWidget.StyleKey) { StatefulWidget.Style() }

var WidgetScope.Builder.statefulStyle: StatefulWidget.Style
        by WidgetScope.readWriteProperty(StatefulWidget.StyleKey, WidgetScope::statefulStyle)

fun <T, E> WidgetScope.stateful(
    factory: VFC<StatefulWidget<T, E>> = this.statefulFactory,
    style: StatefulWidget.Style = this.statefulStyle,
    id: StatefulWidget.Id? = null,
    state: LiveData<State<T, E>>,
    empty: () -> Widget,
    loading: () -> Widget,
    data: (LiveData<T?>) -> Widget,
    error: (LiveData<E?>) -> Widget
): StatefulWidget<T, E> {
    return StatefulWidget(
        factory = factory,
        style = style,
        id = id,
        stateLiveData = state,
        emptyWidget = empty,
        loadingWidget = loading,
        dataWidget = data,
        errorWidget = error
    )
}
