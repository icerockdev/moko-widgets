package com.icerockdev.library.screen

import com.icerockdev.library.MainViewModel
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.input.InputType

class McommerceProfileScreen(
    private val widgetScope: WidgetScope
) : Screen<MainViewModel, McommerceProfileScreen.Args>() {
    override fun createViewModel(arguments: Args): MainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: MainViewModel): Widget {
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

    @Parcelize
    data class Args(val title: String) : Parcelable
}
