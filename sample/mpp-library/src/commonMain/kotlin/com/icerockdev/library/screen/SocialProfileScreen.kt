/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.SingleChoiceWidget
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.singleChoice
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.text

class SocialProfileScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): Widget {
        return with(widgetScope) {
            linear(
                childs = listOf(
                    input(
                        id = Id.NameInput,
                        label = const("Имя*"),
                        field = viewModel.nameField
                    ),
                    input(
                        id = Id.NickNameInput,
                        label = const("Никнейм*"),
                        field = viewModel.nicknameField
                    ),
                    input(
                        id = Id.AboutInput,
                        label = const("О себе"),
                        field = viewModel.aboutField,
                        maxLines = const(null)
                    ),
                    text(
                        id = Id.InfoHeaderText,
                        text = const("ЛИЧНАЯ ИНФОРМАЦИЯ")
                    ),
                    input(
                        id = Id.EmailInput,
                        label = const("Email"),
                        field = viewModel.emailField,
                        inputType = InputType.EMAIL
                    ),
                    input(
                        id = Id.PhoneInput,
                        label = const("Телефон"),
                        field = viewModel.phoneField,
                        inputType = InputType.PHONE
                    ),
                    input(
                        id = Id.BirthdayInput,
                        label = const("Дата рождения"),
                        field = viewModel.birthdayField,
                        inputType = InputType.DATE
                    ),
                    singleChoice(
                        id = Id.GenderChoice,
                        label = const("Пол"),
                        field = viewModel.genderField,
                        values = viewModel.genders,
                        cancelLabel = const("Отмена")
                    ),
                    button(
                        id = Id.SubmitButton,
                        text = const("Сохранить"),
                        onTap = viewModel::onSavePressed
                    )
                )
            )
        }
    }

    object Id {
        object NameInput : InputWidget.Id
        object NickNameInput : InputWidget.Id
        object AboutInput : InputWidget.Id
        object InfoHeaderText : TextWidget.Id
        object BirthdayInput : InputWidget.Id
        object PhoneInput : InputWidget.Id
        object EmailInput : InputWidget.Id
        object SubmitButton : ButtonWidget.Id
        object GenderChoice : SingleChoiceWidget.Id
    }

    interface ViewModelContract {
        val genders: LiveData<List<StringDesc>>
        val genderField: FormField<Int?, StringDesc>
        val birthdayField: FormField<String, StringDesc>
        val phoneField: FormField<String, StringDesc>
        val emailField: FormField<String, StringDesc>
        val aboutField: FormField<String, StringDesc>
        val nicknameField: FormField<String, StringDesc>
        val nameField: FormField<String, StringDesc>

        fun onSavePressed()
    }
}
