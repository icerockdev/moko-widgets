/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.flat

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.factory.SystemInputViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class FlatInputViewFactory actual constructor(
    platformDependency: PlatformDependency,
    textColor: Color?,
    textSize: Int?,
    backgroundColor: Color?
) : ViewFactory<InputWidget<out WidgetSize>> {

    private val defaultViewFactory = SystemInputViewFactory(
        background = Background(
            fill = backgroundColor?.let { Fill.Solid(color = it) }
        ),
        textStyle = TextStyle(
            size = textSize,
            color = textColor
        ),
        underLineColor = Color(0x00000000),
        underLineFocusedColor = Color(0x00000000)
    )

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        return defaultViewFactory.build(widget, size, viewFactoryContext)
    }

    // nothing required from android side
    actual interface PlatformDependency
}
