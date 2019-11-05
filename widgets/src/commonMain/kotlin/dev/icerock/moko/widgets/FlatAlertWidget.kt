/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.DrawableResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background

expect var flatAlertWidgetViewFactory: VFC<FlatAlertWidget>

@WidgetDef
class FlatAlertWidget(
    override val factory: VFC<FlatAlertWidget>,
    override val style: Style,
    override val id: Id?,
    val title: LiveData<StringDesc?>?,
    val message: LiveData<StringDesc?>,
    val drawable: LiveData<DrawableResource?>?,
    val buttonText: LiveData<StringDesc?>?,
    val onTap: (() -> Unit)?
) : Widget<FlatAlertWidget>(),
    Styled<FlatAlertWidget.Style>,
    OptionalId<FlatAlertWidget.Id> {

    data class Style(
        val background: Background = Background()
    )

    object Id : WidgetScope.Id
}
