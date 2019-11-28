/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.fields.validate
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.LinearWidget
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

class SocialProfileScreen(
    private val theme: Theme,
    private val viewModel: SocialProfileViewModelContract
) {
    fun createWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            scroll(
                id = Id.RootScroll,
                size = WidgetSize.AsParent,
                child = linear(
                    size = WidgetSize.WidthAsParentHeightWrapContent,
                    children = listOf<Widget<out WidgetSize>>(
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.NameInput,
                            label = const("Имя*"),
                            field = viewModel.nameField
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.NickNameInput,
                            label = const("Никнейм*"),
                            field = viewModel.nicknameField
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.AboutInput,
                            label = const("О себе"),
                            field = viewModel.aboutField,
                            maxLines = MutableLiveData<Int?>(null)
                        ),
                        text(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.InfoHeaderText,
                            text = const("ЛИЧНАЯ ИНФОРМАЦИЯ")
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
                            id = Id.PhoneInput,
                            label = const("Телефон"),
                            field = viewModel.phoneField,
                            inputType = InputType.PHONE
                        ),
                        input(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.BirthdayInput,
                            label = const("Дата рождения"),
                            field = viewModel.birthdayField,
                            inputType = InputType.DATE
                        ),
//                        singleChoice(
//                            id = Id.GenderChoice,
//                            label = const("Пол"),
//                            field = viewModel.genderField,
//                            values = viewModel.genders,
//                            cancelLabel = const("Отмена")
//                        ),
//                        switchLabeled(
//                            switchId = Id.AgreementSwitch,
//                            switchState = viewModel.agreement,
//                            text = const("Согласен с условиями пользования"),
//                            linearId = Id.AgreementContainer,
//                            textId = Id.AgreementText
//                        ),
                        button(
                            size = WidgetSize.WidthAsParentHeightWrapContent,
                            id = Id.SubmitButton,
                            text = const("Сохранить"),
                            onTap = viewModel::onSavePressed
                        )
                    )
                )
            )
        }
    }

    object Id {
        //        object GenderChoice : SingleChoiceWidget.Id
//        object AgreementSwitch : SwitchWidget.Id

        object RootScroll : ScrollWidget.Id
        object AgreementText : TextWidget.Id
        object AgreementContainer : LinearWidget.Id
        object NickNameInput : InputWidget.Id
        object AboutInput : InputWidget.Id
        object BirthdayInput : InputWidget.Id
        object PhoneInput : InputWidget.Id
        object EmailInput : InputWidget.Id
        object NameInput : InputWidget.Id
        object InfoHeaderText : TextWidget.Id
        object SubmitButton : ButtonWidget.Id
    }
}

interface SocialProfileViewModelContract {
    val agreement: MutableLiveData<Boolean>
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

class SocialProfileViewModel : ViewModel(), SocialProfileViewModelContract {
    override val nameField: FormField<String, StringDesc> = FormField("Aleksey", liveBlock { null })
    override val nicknameField: FormField<String, StringDesc> =
        FormField("Alex009", liveBlock { null })
    override val aboutField: FormField<String, StringDesc> =
        FormField(
            "My name is Aleksey Mikhailov, i am CTO of IceRock Development from Novosibirsk",
            liveBlock { null })
    override val emailField: FormField<String, StringDesc> =
        FormField("am@icerock.dev", liveBlock { email ->
            if (email.contains("@")) null
            else "Should contain @".desc()
        })
    override val phoneField: FormField<String, StringDesc> =
        FormField("+79999999999", liveBlock { null })
    override val birthdayField: FormField<String, StringDesc> =
        FormField("31.05.1993", liveBlock { null })
    override val genders: LiveData<List<StringDesc>> = MutableLiveData(
        initialValue = listOf(
            "Мужчина".desc(),
            "Женщина".desc()
        )
    )
    override val genderField = FormField<Int?, StringDesc>(null) { indexLiveData ->
        genders.mergeWith(indexLiveData) { genders, index ->
            if (index == null) null
            else genders[index]
        }.map {
            if (it == null) "invalid!".desc()
            else null
        }
    }
    override val agreement: MutableLiveData<Boolean> = MutableLiveData(false)

    private val fields = listOf(
        nameField,
        nicknameField,
        aboutField,
        emailField,
        phoneField,
        birthdayField,
        genderField
    )

    override fun onSavePressed() {
        fields.validate()
    }
}
