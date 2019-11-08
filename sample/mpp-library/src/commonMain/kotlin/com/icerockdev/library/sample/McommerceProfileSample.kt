/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.fields.validate
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.input.InputType

class McommerceProfileScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: McommerceProfileViewModelContract
) {
    fun createWidget(): AnyWidget {
        return with(widgetScope) {
            linear(
                childs = listOf(
                    input(
                        id = Id.NameInput,
                        label = const("Ваше имя"),
                        field = viewModel.nameField
                    ),
                    input(
                        id = Id.BirthdayInput,
                        label = const("Дата рождения"),
                        field = viewModel.birthdayField,
                        inputType = InputType.DATE
                    ),
                    input(
                        id = Id.PhoneInput,
                        label = const("Телефон"),
                        field = viewModel.phoneField,
                        inputType = InputType.PHONE
                    ),
                    button(
                        id = Id.SubmitButton,
                        text = const("Подтвердить"),
                        onTap = viewModel::onSavePressed
                    )
                )
            )
        }
    }

    object Id {
        object NameInput : InputWidget.Id
        object BirthdayInput : InputWidget.Id
        object PhoneInput : InputWidget.Id
        object SubmitButton : ButtonWidget.Id
    }
}

interface McommerceProfileViewModelContract {
    val birthdayField: FormField<String, StringDesc>
    val phoneField: FormField<String, StringDesc>
    val nameField: FormField<String, StringDesc>

    fun onSavePressed()
}

class McommerceProfileViewModel() : ViewModel(), McommerceProfileViewModelContract {
    override val nameField: FormField<String, StringDesc> = FormField("Aleksey", liveBlock { null })
    override val phoneField: FormField<String, StringDesc> = FormField("+79999999999", liveBlock { null })
    override val birthdayField: FormField<String, StringDesc> = FormField("31.05.1993", liveBlock { null })

    private val fields = listOf(
        nameField,
        phoneField,
        birthdayField
    )

    override fun onSavePressed() {
        fields.validate()
    }
}