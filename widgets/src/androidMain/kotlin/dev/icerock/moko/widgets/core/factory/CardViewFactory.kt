/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import dev.icerock.moko.widgets.core.widget.CardWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize

actual class CardViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<out Fill>?
) : ViewFactory<CardWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CardWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = CardView(context).apply {
            applyBackgroundIfNeeded(this@CardViewFactory.background)
            applyPaddingIfNeeded(padding)
            radius = this@CardViewFactory.background?.cornerRadius ?: 0f
        }

        val childBundle = widget.child.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = root
            )
        )
        val childView = childBundle.view
        val childSize = childBundle.size
        val childMargins = childBundle.margins

        root.addView(
            childView,
            ViewGroup.MarginLayoutParams(
                childSize.width.toPlatformSize(dm),
                childSize.height.toPlatformSize(dm)
            ).apply {
                childMargins?.let { applyMargin(dm, it) }
            }
        )

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }
}
