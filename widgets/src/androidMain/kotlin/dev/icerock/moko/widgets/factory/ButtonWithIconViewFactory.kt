/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.Gravity
import com.google.android.material.button.MaterialButton
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.R
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
import dev.icerock.moko.widgets.style.toStateList
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class ButtonWithIconViewFactory actual constructor(
    private val background: PressableState<Background<out Fill>>?,
    private val textStyle: TextStyle<PressableState<Color>>?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val androidElevationEnabled: Boolean?,
    private val iconGravity: IconGravity?,
    private val iconPadding: Float?,
    private val icon: PressableState<ImageResource>
) : ViewFactory<ButtonWidget<out WidgetSize>> {

    @SuppressLint("RestrictedApi")
    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val ctx = viewFactoryContext.androidContext
        val caps = isAllCaps
        val icDrawable = icon.toStateList(context = ctx)
        val icGravity = when (iconGravity) {
            IconGravity.START -> MaterialButton.ICON_GRAVITY_START
            IconGravity.END -> MaterialButton.ICON_GRAVITY_END
            IconGravity.TEXT_START, null -> MaterialButton.ICON_GRAVITY_TEXT_START
            IconGravity.TEXT_END -> MaterialButton.ICON_GRAVITY_TEXT_END
        }
        val icPadding = iconPadding ?: 0.0f
        val materialThemeWrapper = ContextThemeWrapper(ctx, R.style.Theme_MaterialComponents_NoActionBar)

        val button: MaterialButton = when (widget.content) {
            is ButtonWidget.Content.Text -> {
                MaterialButton(materialThemeWrapper).apply {
                    widget.content.text.bind(viewFactoryContext.lifecycleOwner) { text ->
                        this.text = text?.toString(ctx)
                    }
                    applyTextStyleIfNeeded(textStyle)
                    caps?.also { isAllCaps = it }
                    icon = icDrawable
                    iconGravity = icGravity
                    iconPadding = icPadding.toInt()

                    when (this@ButtonWithIconViewFactory.iconGravity) {
                        IconGravity.START -> gravity = Gravity.END
                        IconGravity.END -> gravity = Gravity.START
                        else -> {
                        }
                    }
                }
            }
            else -> throw Exception("Not supported content type")
        }

        button.applyStateBackgroundIfNeeded(background)
        button.supportBackgroundTintList = null
        button.supportBackgroundTintMode = null
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
