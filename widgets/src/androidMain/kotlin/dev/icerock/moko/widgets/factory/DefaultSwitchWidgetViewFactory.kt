/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.res.ColorStateList
import android.widget.Switch
import androidx.core.graphics.drawable.DrawableCompat
import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.ColorStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bindNotNull

actual class SystemSwitchViewFactory actual constructor(
    private val background: Background?,
    private val switchColor: ColorStyle?,
    private val margins: MarginValues?
) : ViewFactory<SwitchWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext

        val switch = Switch(context).apply {
            switchColor?.also { colorStyle ->
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

            this@SystemSwitchViewFactory.background?.also {
                this.background = it.buildBackground(context)
            }
        }

        widget.state.bindNotNull(viewFactoryContext.lifecycleOwner) {
            switch.isChecked = it
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            widget.state.value = isChecked
        }

        return ViewBundle(
            view = switch,
            size = size,
            margins = margins
        )
    }
}
