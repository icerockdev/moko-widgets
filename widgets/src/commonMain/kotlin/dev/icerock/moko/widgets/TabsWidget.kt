/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background

expect var tabsWidgetViewFactory: VFC<TabsWidget>

@WidgetDef
class TabsWidget(
    override val factory: VFC<TabsWidget>,
    override val style: Style,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    val tabs: List<TabsWidget.TabWidget> // for correct codegen
) : Widget<TabsWidget>(),
    Styled<TabsWidget.Style>,
    OptionalId<TabsWidget.Id> {

    class TabWidget(
        val title: LiveData<StringDesc>,
        val body: AnyWidget
    )

    data class Style(
        val background: Background = Background()
    )

    interface Id : WidgetScope.Id
}
