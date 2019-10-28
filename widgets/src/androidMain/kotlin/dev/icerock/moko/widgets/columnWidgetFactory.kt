package dev.icerock.moko.widgets

import android.view.ViewGroup
import android.widget.LinearLayout
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.ext.toLinearLayoutOrientation
import dev.icerock.moko.widgets.style.ext.toPlatformSize

actual var linearWidgetFactory: VFC<LinearWidget> = { context, widget ->
    val ctx = context.context
    val dm = ctx.resources.displayMetrics
    val style = widget.style

    val container = LinearLayout(ctx).apply {
        layoutParams = ViewGroup.LayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        )
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