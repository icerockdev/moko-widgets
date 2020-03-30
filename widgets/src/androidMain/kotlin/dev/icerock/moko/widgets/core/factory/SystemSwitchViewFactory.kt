/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.widget.Switch
import androidx.core.graphics.drawable.DrawableCompat
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.widget.SwitchWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.background.buildBackground
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.androidId
import dev.icerock.moko.widgets.core.utils.bindNotNull

actual class SystemSwitchViewFactory actual constructor(
    private val background: Background<out Fill>?,
    private val tintColor: Color?,
    private val margins: MarginValues?
) : ViewFactory<SwitchWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext

        val switch = Switch(context).apply {
            id = widget.id.androidId

            tintColor?.also { color ->
                DrawableCompat.setTint(thumbDrawable, color.argb.toInt())
                DrawableCompat.setTint(trackDrawable, color.argb.toInt())
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
