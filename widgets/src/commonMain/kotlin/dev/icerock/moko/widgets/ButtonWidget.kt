/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.FactoryWidget
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef
class ButtonWidget<WS : WidgetSize>(
    override val factory: ViewFactory<ButtonWidget<WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val text: LiveData<StringDesc>,
    val enabled: LiveData<Boolean>?,
    val onTap: () -> Unit
) : FactoryWidget<ButtonWidget<WidgetSize>, WS>(), OptionalId<ButtonWidget.Id> {

    interface Id : Theme.Id
}
