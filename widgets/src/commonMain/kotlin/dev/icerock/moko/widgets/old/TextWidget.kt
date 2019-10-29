/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var textWidgetViewFactory: VFC<TextWidget>

class TextWidget(
    private val factory: VFC<TextWidget> = textWidgetViewFactory,
    val text: LiveData<StringDesc>,
    val style: Style
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    enum class FontStyle {
        BODY,
        HEADER1,
        HEADER2,
        HEADER3,
        HEADER4
    }

    data class Style(
        val fontStyle: FontStyle = FontStyle.BODY
    )
}
