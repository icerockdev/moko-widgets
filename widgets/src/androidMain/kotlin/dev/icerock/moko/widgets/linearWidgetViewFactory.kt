/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.toLinearLayoutOrientation
import dev.icerock.moko.widgets.style.withSize
import dev.icerock.moko.widgets.view.MarginedLinearLayout

actual var linearWidgetViewFactory: VFC<LinearWidget> = { context, widget ->
    val ctx = context.context
    val style = widget.style

    val container = MarginedLinearLayout(ctx).apply {
        orientation = widget.orientation.toLinearLayoutOrientation()
    }

    widget.children.forEach { child ->
        val view = child.buildView(
            ViewFactoryContext(
                context = ctx,
                lifecycleOwner = context.lifecycleOwner,
                parent = container
            )
        )
        container.addView(view)
    }

    container.withSize(widget.layoutParams.size).apply { applyStyle(style) }
}
