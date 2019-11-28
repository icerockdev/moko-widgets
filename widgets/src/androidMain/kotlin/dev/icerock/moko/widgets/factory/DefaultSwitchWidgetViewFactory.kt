/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.res.ColorStateList
import android.widget.Switch
import androidx.core.graphics.drawable.DrawableCompat
import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class DefaultSwitchWidgetViewFactory actual constructor(
    style: Style
) : DefaultSwitchWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext

        val switch = Switch(context).apply {
            style.switchColor?.also { colorStyle ->
                val thumbStates = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    colorStyle.colors.let {
                        intArrayOf(it[0], it[1])
                    }
                )

                val trackStates = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    colorStyle.colors.let {
                        intArrayOf(it[2], it[3])
                    }
                )

                DrawableCompat.setTintList(thumbDrawable, thumbStates)
                DrawableCompat.setTintList(trackDrawable, trackStates)
            }

            applyStyle(style)
        }

        return ViewBundle(
            view = switch,
            size = size,
            margins = style.margins
        )
    }
}