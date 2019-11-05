package dev.icerock.moko.widgets

import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.applyPadding
import dev.icerock.moko.widgets.style.ext.toPlatformSize

actual var scrollWidgetViewFactory: VFC<ScrollWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = widget.style
    val dm = context.resources.displayMetrics

    val scrollView = if (style.orientation == Orientation.VERTICAL) ScrollView(context)
    else HorizontalScrollView(context)

    with(scrollView) {
        layoutParams = ViewGroup.MarginLayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            applyMargin(context, style.margins)
        }
        applyPadding(style.padding)

        style.background?.also { background = it.buildBackground(context) }

        val body = widget.child.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = this
            )
        )

        addView(body)
    }

    scrollView
}