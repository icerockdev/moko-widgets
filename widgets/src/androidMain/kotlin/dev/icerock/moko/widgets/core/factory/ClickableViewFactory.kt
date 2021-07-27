/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.os.Build
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.ClickableWidget

actual class ClickableViewFactory : ViewFactory<ClickableWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ClickableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        @Suppress("UNCHECKED_CAST")
        val childViewBundle =
            widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        return childViewBundle.copy(
            view = childViewBundle.view.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = RippleDrawable(
                        ColorStateList.valueOf(Color.GRAY),
                        null,
                        null
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = RippleDrawable(
                        ColorStateList.valueOf(Color.GRAY),
                        background,
                        null
                    )
                } else {
                    // add something on old devices?
                }
                setOnClickListener { widget.onClick() }
            }
        )
    }
}
