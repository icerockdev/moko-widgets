/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.content.res.ColorStateList
import android.widget.Switch
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.applyStyle

actual var switchWidgetViewFactory: VFC<SwitchWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val style = widget.style

    Switch(context).apply {
        applyStyle(style)

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

            thumbDrawable.setTintList(thumbStates)
            trackDrawable.setTintList(trackStates)
        }
    }
}