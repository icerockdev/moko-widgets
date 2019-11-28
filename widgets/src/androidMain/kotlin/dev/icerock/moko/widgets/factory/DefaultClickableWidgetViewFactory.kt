/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ClickableWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class DefaultClickableWidgetViewFactory actual constructor() :
    DefaultClickableWidgetViewFactoryBase() {

    override fun <WS : WidgetSize> build(
        widget: ClickableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val childViewBundle =
            widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        return childViewBundle.copy(
            view = childViewBundle.view.apply {
                setOnClickListener { widget.onClick() }
            }
        )
    }
}
