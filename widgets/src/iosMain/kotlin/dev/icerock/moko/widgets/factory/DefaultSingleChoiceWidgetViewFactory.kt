/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.SingleChoiceWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UIView

actual class DefaultSingleChoiceWidgetViewFactory actual constructor(
    style: Style
) : DefaultSingleChoiceWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: SingleChoiceWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        // TODO create view
        return ViewBundle(
            view = UIView(),
            size = size,
            margins = style.margins
        )
    }
}
