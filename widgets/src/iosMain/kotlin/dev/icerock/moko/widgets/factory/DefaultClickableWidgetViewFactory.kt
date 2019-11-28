/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ClickableWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.setHandler
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.addGestureRecognizer

actual class DefaultClickableWidgetViewFactory actual constructor() :
    DefaultClickableWidgetViewFactoryBase() {

    override fun <WS : WidgetSize> build(
        widget: ClickableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val childViewBundle =
            widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        childViewBundle.view.apply {
            val recognizer = UITapGestureRecognizer().apply {
                setHandler(widget.onClick)
            }
            addGestureRecognizer(recognizer)
            userInteractionEnabled = true
        }

        return childViewBundle
    }
}
