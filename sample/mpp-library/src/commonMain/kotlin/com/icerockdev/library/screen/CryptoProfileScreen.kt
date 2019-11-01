/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import com.icerockdev.library.MainViewModel
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.text

class CryptoProfileScreen(
    private val widgetScope: WidgetScope
) : Screen<MainViewModel, CryptoProfileScreen.Args>() {
    override fun createViewModel(arguments: Args): MainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: MainViewModel): Widget {
        return with(widgetScope) {
            linear(
                childs = listOf(
                    input(
                        id = Id.NameInput,
                        label = const("Name"),
                        field = viewModel.nameField
                    ),
                    input(
                        id = Id.PhoneInput,
                        label = const("Phone number"),
                        field = viewModel.phoneField,
                        inputType = InputType.PHONE
                    ),
                    input(
                        id = Id.EmailInput,
                        label = const("Email"),
                        field = viewModel.emailField,
                        inputType = InputType.EMAIL
                    ),
                    input(
                        id = Id.PasswordInput,
                        label = const("Password"),
                        field = viewModel.passwordField,
                        inputType = InputType.PASSWORD
                    ),
                    input(
                        id = Id.RepeatPasswordInput,
                        label = const("Repeat password"),
                        field = viewModel.repeatPasswordField,
                        inputType = InputType.PASSWORD
                    ),
                    button(
                        id = Id.JoinButton,
                        text = const("Join"),
                        onTap = viewModel::onSavePressed
                    ),
                    text(
                        id = Id.DelimiterText,
                        text = const("or")
                    ),
                    button(
                        id = Id.TryDemoButton,
                        text = const("Try Demo"),
                        onTap = viewModel::onSavePressed
                    )
                )
            )
        }
    }

    object Id {
        object NameInput : InputWidget.Id
        object PhoneInput : InputWidget.Id
        object EmailInput : InputWidget.Id
        object PasswordInput : InputWidget.Id
        object RepeatPasswordInput : InputWidget.Id
        object JoinButton : ButtonWidget.Id
        object DelimiterText : TextWidget.Id
        object TryDemoButton : ButtonWidget.Id
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
