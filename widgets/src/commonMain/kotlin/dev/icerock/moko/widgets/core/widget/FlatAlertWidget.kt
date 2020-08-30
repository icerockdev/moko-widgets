/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

fun <WS : WidgetSize> Theme.flatAlert(
    constraintWidgetFactory: ViewFactory<ConstraintWidget<out WidgetSize>>? = null,
    textWidgetFactory: ViewFactory<TextWidget<out WidgetSize>>? = null,
    buttonWidgetFactory: ViewFactory<ButtonWidget<out WidgetSize>>? = null,
    size: WS,
    message: LiveData<StringDesc?>,
    buttonText: LiveData<StringDesc?>,
    onTap: () -> Unit
): Widget<WS> = constraint(
    size = size,
    widgetFactory = constraintWidgetFactory
) {
    val msg = +text(
        id = FlatAlertIds.Message,
        size = WidgetSize.Const(width = SizeSpec.MatchConstraint, height = SizeSpec.WrapContent),
        text = message.map { it ?: "".desc() },
        widgetFactory = textWidgetFactory
    )
    val submitBtn = +button(
        id = FlatAlertIds.Button,
        size = WidgetSize.WrapContent,
        content = ButtonWidget.Content.Text(Value.liveData(buttonText)),
        onTap = onTap,
        widgetFactory = buttonWidgetFactory
    )

    constraints {
        msg centerYToCenterY root
        msg centerXToCenterX root

        submitBtn topToBottom msg offset 8
        submitBtn centerXToCenterX root
    }
}

object FlatAlertIds {
    object Message : TextWidget.Id
    object Button : ButtonWidget.Id
}
