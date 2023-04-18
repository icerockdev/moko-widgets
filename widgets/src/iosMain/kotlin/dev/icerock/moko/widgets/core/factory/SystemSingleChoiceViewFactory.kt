/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.SingleChoiceWidget
import platform.UIKit.UIView

@Suppress("LongParameterList", "UnusedPrivateMember")
actual class SystemSingleChoiceViewFactory actual constructor(
    private val textStyle: TextStyle<Color>?,
    private val labelTextStyle: TextStyle<Color>?,
    private val dropDownTextColor: Color?,
    private val underlineColor: Color?,
    private val dropDownBackground: Background<Fill.Solid>?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<SingleChoiceWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: SingleChoiceWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        // TODO create view
        return ViewBundle(
            view = UIView(),
            size = size,
            margins = margins
        )
    }
}
