/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.applyStateBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.state.PressableState
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class SystemButtonViewFactory actual constructor(
    private val background: PressableState<Background<out Fill>>?,
    private val textStyle: TextStyle<PressableState<Color>>?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val androidElevationEnabled: Boolean?
) : ViewFactory<ButtonWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val ctx = viewFactoryContext.androidContext

        // it is hell. Compose save us! ImageButton is ImageView, not Button!
        val button: View = when (widget.content) {
            is ButtonWidget.Content.Text -> {
                Button(ctx).apply {
                    widget.content.text.bind(viewFactoryContext.lifecycleOwner) { text ->
                        this.text = text?.toString(ctx)
                    }
                    applyTextStyleIfNeeded(textStyle)
                    this@SystemButtonViewFactory.isAllCaps?.also { isAllCaps = it }
                }
            }
            is ButtonWidget.Content.Icon -> {
                ImageButton(ctx).apply {
                    widget.content.image.bind(viewFactoryContext.lifecycleOwner) { image ->
                        image.loadIn(this)
                    }
                    background = null
                }
            }
        }

        button.applyStateBackgroundIfNeeded(background)
        button.applyPaddingIfNeeded(padding)

        if (androidElevationEnabled == false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.stateListAnimator = null
        }

        widget.enabled?.bind(viewFactoryContext.lifecycleOwner) { enabled ->
            button.isEnabled = enabled == true
        }

        button.setOnClickListener {
            widget.onTap()
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = margins
        )
    }
}
