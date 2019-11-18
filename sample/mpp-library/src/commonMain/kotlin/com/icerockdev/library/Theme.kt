/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.sample.CryptoProfileScreen
import com.icerockdev.library.sample.SocialProfileScreen
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.LinearWidget
import dev.icerock.moko.widgets.ScrollWidget
import dev.icerock.moko.widgets.SingleChoiceWidget
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.buttonStyle
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.inputStyle
import dev.icerock.moko.widgets.linearStyle
import dev.icerock.moko.widgets.scrollStyle
import dev.icerock.moko.widgets.setButtonStyle
import dev.icerock.moko.widgets.setLinearStyle
import dev.icerock.moko.widgets.setTextStyle
import dev.icerock.moko.widgets.singleChoiceStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Border
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Shape
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.Colors
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.style.view.TextAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.rgba
import dev.icerock.moko.widgets.textStyle

object Theme {
    object Color {
        val white = Color(0xFF, 0xFF, 0xFF, 0xFF)
        val redError = Color(0xFF, 0x66, 0x66, 0xFF)
        val grayText = Color(0x26, 0x26, 0x28, 0xFF)
        val gray2Text = Color(0x4A, 0x4A, 0x4A, 0xFF)
        val lightGrayText = rgba(38, 38, 40, 0.4)
        val grayUnderline = Color(0xDD, 0xDD, 0xDD, 0xFF)
    }

    val errorTextStyle: TextStyle = TextStyle(
        size = 12,
        color = Color.redError,
        fontStyle = FontStyle.MEDIUM // Roboto-regular weight 400
    )

    val inputStyle: InputWidget.Style = InputWidget.Style(
        textStyle = TextStyle(
            size = 15,
            color = Color.grayText,
            fontStyle = FontStyle.MEDIUM // Roboto-medium weight 500
        ),
        labelTextStyle = TextStyle(
            size = 12,
            color = Color.gray2Text,
            fontStyle = FontStyle.MEDIUM // Roboto-regular weight 400
        ),
        errorTextStyle = errorTextStyle,
        underLineColor = Color.grayUnderline,
        background = null
    )

    val buttonStyle: ButtonWidget.Style = ButtonWidget.Style(
        textStyle = TextStyle(
            size = 17,
            color = Color.white,
            fontStyle = FontStyle.MEDIUM // Roboto-medium weight 500
        ),
        isAllCaps = false,
        background = {
            val fill = Fill.Gradient(
                colors = listOf(
                    Color(0xB8, 0x3A, 0xF3, 0xFF),
                    Color(0x69, 0x50, 0xFB, 0xFF)
                ),
                direction = Direction.TR_BL
            )
            val normalBackground = Background(
                fill = fill,
                shape = Shape.Rectangle(cornerRadius = 16f)
            )
            val pressedBackground = normalBackground.copy(
                fill = fill.copy(
                    colors = listOf(
                        Color(0xB8, 0x3A, 0xF3, 0xBB),
                        Color(0x69, 0x50, 0xFB, 0xBB)
                    )
                )
            )
            val disabledBackground = normalBackground.copy(
                fill = fill.copy(
                    colors = listOf(
                        Color(0xB8, 0x3A, 0xF3, 0x80),
                        Color(0x69, 0x50, 0xFB, 0x80)
                    )
                )
            )

            StateBackground(
                normal = normalBackground,
                disabled = disabledBackground,
                pressed = pressedBackground
            )
        }()
    )

    val headerTextStyle: TextStyle = TextStyle(
        size = 12,
        color = Color.lightGrayText,
        fontStyle = FontStyle.MEDIUM // Roboto-medium weight 500
    )

    val headerStyle: TextWidget.Style = TextWidget.Style(
        textStyle = headerTextStyle
    )

    val profileContainerStyle: LinearWidget.Style = LinearWidget.Style(
        background = null
    )

    val singleChoiceStyle: SingleChoiceWidget.Style = SingleChoiceWidget.Style(
        textStyle = TextStyle(
            size = 15,
            color = Color.grayText,
            fontStyle = FontStyle.MEDIUM // Roboto-medium weight 500
        ),
        labelTextStyle = TextStyle(
            size = 12,
            color = Color.gray2Text,
            fontStyle = FontStyle.MEDIUM // Roboto-regular weight 400
        ),
        dropDownTextColor = null,
        underlineColor = Color.grayUnderline,
        dropDownBackground = null
    )

    val socialWidgetScope = WidgetScope {
        this.inputStyle = Theme.inputStyle
        this.textStyle = Theme.headerStyle
        this.buttonStyle = Theme.buttonStyle
        this.linearStyle = Theme.profileContainerStyle
        this.singleChoiceStyle = Theme.singleChoiceStyle

        setLinearStyle(
            linearStyle.copy(
            ), SocialProfileScreen.Id.AgreementContainer
        )
    }

    val mcommerceWidgetScope = WidgetScope {
        this.inputStyle = Theme.inputStyle.copy(
            textStyle = TextStyle(
                size = 16,
                color = Color(0x4B, 0x4F, 0x4E, 0xFF),
                fontStyle = FontStyle.MEDIUM // ProximaNova-Semibold weight 600
            ),
            labelTextStyle = TextStyle(
                size = 12,
                color = Color(0x99, 0x9E, 0x9C, 0xFF), // #999e9cff
                fontStyle = FontStyle.MEDIUM // ProximaNova-Regular weight 400
            ),
            underLineColor = Color(0x00, 0x00, 0x00, 0x1F) // #0000001f
        )
        this.buttonStyle = Theme.buttonStyle.copy(
            textStyle = TextStyle(
                size = 17,
                color = Color.white,
                fontStyle = FontStyle.MEDIUM // Roboto-medium weight 500
            ),
            background = {
                val normalBackground = Background(
                    fill = Fill.Solid(color = Color(0x00, 0x49, 0x2C, 0xFF)),
                    shape = Shape.Rectangle(cornerRadius = 4f)
                )
                val disabledBackground = normalBackground.copy(
                    fill = Fill.Solid(color = Color(0x00, 0x49, 0x2C, 0x80))
                )
                val pressedBackground = normalBackground.copy(
                    fill = Fill.Solid(color = Color(0x00, 0x49, 0x2C, 0xBB))
                )
                StateBackground(
                    normal = normalBackground,
                    disabled = disabledBackground,
                    pressed = pressedBackground
                )
            }()
        )
        this.linearStyle = Theme.profileContainerStyle
    }

    val cryptoWidgetScope = WidgetScope {
        inputStyle = Theme.inputStyle.copy(
            textStyle = TextStyle(
                size = 16,
                color = Colors.white, // #ffffffff
                fontStyle = FontStyle.MEDIUM // Gotham Pro
            ),
            labelTextStyle = TextStyle(
                size = 12,
                color = Color(0xA6, 0xA6, 0xA6, 0xFF), // #a6a6a6ff
                fontStyle = FontStyle.MEDIUM // Gotham Pro
            ),
            underLineColor = Colors.white
        )
        textStyle = Theme.headerStyle.copy(
            textStyle = TextStyle(
                size = 14,
                color = Colors.white, // #ffffffff
                fontStyle = FontStyle.MEDIUM // Gotham Pro
            )
        )
        buttonStyle = Theme.buttonStyle.copy(
            background = {
                val normalBackground = Background(
                    fill = Fill.Solid(color = Color(0x13, 0x75, 0xF8, 0xFF)),
                    shape = Shape.Rectangle(cornerRadius = 22f)
                )
                val disabledBackground = normalBackground.copy(
                    fill = Fill.Solid(color = Color(0x13, 0x75, 0xF8, 0x80))
                )
                val pressedBackground = normalBackground.copy(
                    fill = Fill.Solid(color = Color(0x13, 0x75, 0xF8, 0xBB))
                )
                StateBackground(
                    normal = normalBackground,
                    disabled = disabledBackground,
                    pressed = pressedBackground
                )
            }()
        )
        setButtonStyle(
            buttonStyle.copy(
                background = {
                    val normalBackground = Background(
                        fill = Fill.Solid(color = Color(0x30, 0x30, 0x30, 0xFF)),
                        shape = Shape.Rectangle(cornerRadius = 22f),
                        border = Border(
                            color = Colors.white,
                            width = 1f
                        )
                    )
                    val disabledBackground = normalBackground.copy(
                        fill = Fill.Solid(color = Color(0x30, 0x30, 0x30, 0x80))
                    )
                    val pressedBackground = normalBackground.copy(
                        fill = Fill.Solid(color = Color(0x30, 0x30, 0x30, 0xBB))
                    )
                    StateBackground(
                        normal = normalBackground,
                        disabled = disabledBackground,
                        pressed = pressedBackground
                    )
                }()
            ), CryptoProfileScreen.Id.TryDemoButton
        )
        setTextStyle(
            textStyle.copy(
                textAlignment = TextAlignment.CENTER
            ), CryptoProfileScreen.Id.DelimiterText
        )
        linearStyle = Theme.profileContainerStyle
        scrollStyle = ScrollWidget.Style(
            background = Background(
                fill = Fill.Solid(Colors.black)
            )
        )
        singleChoiceStyle = Theme.singleChoiceStyle
    }
}
