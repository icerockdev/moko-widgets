/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var flatAlertWidgetViewFactory: VFC<FlatAlertWidget>

@WidgetDef
class FlatAlertWidget(
    val factory: VFC<FlatAlertWidget>,
    override val style: Style,
    override val id: Id?,
    val title: LiveData<StringDesc?>?,
    val message: LiveData<StringDesc?>,
    val drawable: LiveData<ImageResource?>?,
    val buttonText: LiveData<StringDesc?>?,
    val onTap: (() -> Unit)?
) : Widget(), Styled<FlatAlertWidget.Style>, OptionalId<FlatAlertWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class Style(
        override val size: WidgetSize = WidgetSize.Const(
            width = SizeSpec.WrapContent,
            height = SizeSpec.WrapContent
        ),
        override val background: Background? = null
    ) : Widget.Style, Sized, Backgrounded

    object Id : WidgetScope.Id
}
