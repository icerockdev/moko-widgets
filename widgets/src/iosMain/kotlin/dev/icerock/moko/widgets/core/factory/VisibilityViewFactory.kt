/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.widget.VisibilityWidget
import platform.UIKit.hidden

actual class VisibilityViewFactory actual constructor() :
    ViewFactory<VisibilityWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: VisibilityWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        @Suppress("UNCHECKED_CAST")
        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        widget.showed.bind { bundle.view.hidden = it.not() }

        return bundle
    }
}
