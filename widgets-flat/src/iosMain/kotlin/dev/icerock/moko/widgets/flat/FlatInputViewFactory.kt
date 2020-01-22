/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.flat

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UIView
import platform.UIKit.UIViewController

actual class FlatInputViewFactory actual constructor(
    private val platformDependency: PlatformDependency,
    textStyle: TextStyle?,
    backgroundColor: Color?,
    private val margins: MarginValues?
) : ViewFactory<InputWidget<out WidgetSize>> {

    private val style = Style(
        textStyle = textStyle,
        backgroundColor = backgroundColor
    )

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val view = platformDependency.createFlatInputWidgetView(
            widget = widget,
            viewController = viewFactoryContext,
            style = style
        )
        return ViewBundle(
            view = view,
            size = size,
            margins = margins
        )
    }

    actual interface PlatformDependency {
        fun createFlatInputWidgetView(
            widget: InputWidget<out WidgetSize>,
            viewController: UIViewController,
            style: Style
        ): UIView
    }

    data class Style(
        val textStyle: TextStyle?,
        val backgroundColor: Color?
    )
}
