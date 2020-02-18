/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.os.Build
import com.google.android.material.ripple.RippleDrawableCompat
import com.google.android.material.ripple.RippleUtils
import dev.icerock.moko.widgets.ClickableWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class ClickableViewFactory actual constructor(
) : ViewFactory<ClickableWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ClickableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
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
