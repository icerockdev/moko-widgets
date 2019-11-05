/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.widget.LinearLayout
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.toLinearLayoutOrientation

actual var linearWidgetViewFactory: VFC<LinearWidget> = { context, widget ->
    val ctx = context.context
    val style = widget.style

    val container = LinearLayout(ctx).apply {
        applyStyle(style)
        orientation = style.orientation.toLinearLayoutOrientation()
    }

    widget.childs.forEach { child ->
        val view = child.buildView(
            ViewFactoryContext(
                context = ctx,
                lifecycleOwner = context.lifecycleOwner,
                parent = container
            )
        )
        container.addView(view)
    }

    container
}
