/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.fields.validate
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.ScrollWidget
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.scroll
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text

class CryptoProfileScreen(
    private val theme: Theme,
    private val viewModel: CryptoProfileContract
) {
    fun createWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            scroll(
                id = Id.RootScroll,
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.AsParent),
                child = linear(
                    size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.WrapContent),
                    children = listOf<Widget<out WidgetSize>>(
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.NameInput,
                            label = const("Name"),
                            field = viewModel.nameField
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.PhoneInput,
                            label = const("Phone number"),
                            field = viewModel.phoneField,
                            inputType = InputType.PHONE
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.EmailInput,
                            label = const("Email"),
                            field = viewModel.emailField,
                            inputType = InputType.EMAIL
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.PasswordInput,
                            label = const("Password"),
                            field = viewModel.passwordField,
                            inputType = InputType.PASSWORD
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.RepeatPasswordInput,
                            label = const("Repeat password"),
                            field = viewModel.repeatPasswordField,
                            inputType = InputType.PASSWORD
                        ),
                        button(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.JoinButton,
                            text = const("Join"),
                            onTap = viewModel::onSavePressed
                        ),
                        text(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.DelimiterText,
                            text = const("or")
                        ),
                        button(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.TryDemoButton,
                            text = const("Try Demo"),
                            onTap = viewModel::onSavePressed
                        )
                    )
                )
            )
        }
    }

    object Id {
        object RootScroll : ScrollWidget.Id
        object NameInput : InputWidget.Id
        object PhoneInput : InputWidget.Id
        object EmailInput : InputWidget.Id
        object PasswordInput : InputWidget.Id
        object RepeatPasswordInput : InputWidget.Id
        object DelimiterText : TextWidget.Id
        object JoinButton : ButtonWidget.Id
        object TryDemoButton : ButtonWidget.Id
    }
}

interface CryptoProfileContract {
    val repeatPasswordField: FormField<String, StringDesc>
    val passwordField: FormField<String, StringDesc>
    val phoneField: FormField<String, StringDesc>
    val emailField: FormField<String, StringDesc>
    val nameField: FormField<String, StringDesc>

    fun onSavePressed()
}

class CryptoProfileViewModel : ViewModel(), CryptoProfileContract {
    override val nameField: FormField<String, StringDesc> = FormField("Aleksey", liveBlock { null })
    override val emailField: FormField<String, StringDesc> =
        FormField("am@icerock.dev", liveBlock { email ->
            if (email.contains("@")) null
            else "Should contain @".desc()
        })
    override val phoneField: FormField<String, StringDesc> =
        FormField("+79999999999", liveBlock { null })
    override val passwordField: FormField<String, StringDesc> =
        FormField("123456", liveBlock { null })
    override val repeatPasswordField: FormField<String, StringDesc> =
        FormField("1234567") { repeatPasswordData ->
            repeatPasswordData.mergeWith(passwordField.data) { repeatPassword, password ->
                if (repeatPassword != password) "should be same".desc() as StringDesc
                else null
            }
        }

    private val fields = listOf(
        nameField,
        emailField,
        phoneField,
        passwordField,
        repeatPasswordField
    )

    override fun onSavePressed() {
        fields.validate()
    }
}
