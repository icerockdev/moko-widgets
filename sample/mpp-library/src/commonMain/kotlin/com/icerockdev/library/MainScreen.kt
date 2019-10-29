/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.buttonStyle
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.core.asLiveData
import dev.icerock.moko.widgets.core.buildWidget
import dev.icerock.moko.widgets.flatAlert
import dev.icerock.moko.widgets.flatAlertStyle
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.linearStyle
import dev.icerock.moko.widgets.singleChoice
import dev.icerock.moko.widgets.stateful
import dev.icerock.moko.widgets.statefulStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.background.ShapeType
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.tabs
import dev.icerock.moko.widgets.text

open class MainScreen(
    private val theme: Theme
) : Screen<MainViewModel, MainScreen.Args>() {
    override fun createViewModel(arguments: Args): MainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: MainViewModel): Widget {
        return with(WidgetScope()) {
            tabs {
                tab(
                    title = const("P#1"),
                    body = socialProfileScreen(viewModel)
                )
                tab(
                    title = const("P#2"),
                    body = theme.socialWidgetScope.socialProfileScreen(viewModel)
                )
                tab(
                    title = const("P#3"),
                    body = theme.mcommerceWidgetScope.mcommerceProfileScreen(viewModel)
                )
                tab(
                    title = const("P#4"),
                    body = theme.cryptoWidgetScope.cryptoProfileScreen(viewModel)
                )
                tab(
                    title = const("D"),
                    body = demoScreen(viewModel)
                )
            }
        }
    }

    private fun WidgetScope.socialProfileScreen(viewModel: MainViewModel): Widget {
        return linear(
            childs = listOf(
                input(
                    label = const("Имя*"),
                    field = viewModel.nameField
                ),
                input(
                    label = const("Никнейм*"),
                    field = viewModel.nicknameField
                ),
                input(
                    label = const("О себе"),
                    field = viewModel.aboutField,
                    maxLines = const(null)
                ),
                text(
                    text = const("ЛИЧНАЯ ИНФОРМАЦИЯ")
                ),
                input(
                    label = const("Email"),
                    field = viewModel.emailField,
                    inputType = InputType.EMAIL
                ),
                input(
                    label = const("Телефон"),
                    field = viewModel.phoneField,
                    inputType = InputType.PHONE
                ),
                input(
                    label = const("Дата рождения"),
                    field = viewModel.birthdayField,
                    inputType = InputType.DATE
                ),
                singleChoice(
                    label = const("Пол"),
                    field = viewModel.genderField,
                    values = viewModel.genders,
                    cancelLabel = const("Отмена")
                ),
                button(
                    text = const("Сохранить"),
                    onTap = viewModel::onSavePressed
                )
            )
        )
    }

    private fun WidgetScope.mcommerceProfileScreen(viewModel: MainViewModel): Widget {
        return linear(
            childs = listOf(
                input(
                    label = const("Ваше имя"),
                    field = viewModel.nameField
                ),
                input(
                    label = const("Дата рождения"),
                    field = viewModel.birthdayField,
                    inputType = InputType.DATE
                ),
                input(
                    label = const("Телефон"),
                    field = viewModel.phoneField,
                    inputType = InputType.PHONE
                ),
                button(
                    text = const("Подтвердить"),
                    onTap = viewModel::onSavePressed
                )
            )
        )
    }

    private fun WidgetScope.cryptoProfileScreen(viewModel: MainViewModel): Widget {
        return linear(
            childs = listOf(
                input(
                    label = const("Name"),
                    field = viewModel.nameField
                ),
                input(
                    label = const("Phone number"),
                    field = viewModel.phoneField,
                    inputType = InputType.PHONE
                ),
                input(
                    label = const("Email"),
                    field = viewModel.emailField,
                    inputType = InputType.EMAIL
                ),
                input(
                    label = const("Password"),
                    field = viewModel.passwordField,
                    inputType = InputType.PASSWORD
                ),
                input(
                    label = const("Repeat password"),
                    field = viewModel.repeatPasswordField,
                    inputType = InputType.PASSWORD
                ),
                button(
                    text = const("Join"),
                    onTap = viewModel::onSavePressed
                ),
                text(
                    text = const("or")
                ),
                button(
                    text = const("Try Demo"),
                    onTap = viewModel::onSavePressed
                )
            )
        )
    }

    private fun WidgetScope.demoScreen(viewModel: MainViewModel): Widget {
        val errorScope = childScope {
            this.flatAlertStyle = flatAlertStyle.copy(
                background = Background(
                    color = 0xFF00FF00
                )
            )
        }
        val dataScope = childScope {
            this.flatAlertStyle = flatAlertStyle.copy(
                background = Background(
                    color = 0xFFFF00FF
                )
            )
        }
        val buttonsScope = childScope {
            this.buttonStyle = buttonStyle.copy(
                size = WidgetSize(
                    width = SizeSpec.WRAP_CONTENT,
                    height = SizeSpec.WRAP_CONTENT
                )
            )
        }

        return linear(
            childs = listOf(
                linear(
                    style = linearStyle.copy(
                        orientation = Orientation.HORIZONTAL,
                        size = WidgetSize(
                            width = SizeSpec.AS_PARENT,
                            height = SizeSpec.WRAP_CONTENT
                        )
                    ),
                    childs = with(buttonsScope) {
                        listOf(
                            button(
                                text = "change state".desc().asLiveData(),
                                onTap = viewModel::onChangeStatePressed
                            ),
                            button(
                                text = "just button".desc().asLiveData(),
                                onTap = {}
                            )
                        )
                    }
                ),
                stateful(
                    style = statefulStyle.copy(
                        background = statefulStyle.background.copy(
                            shape = statefulStyle.background.shape.copy(type = ShapeType.OVAL)
                        )
                    ),
                    state = viewModel.state,
                    empty = {
                        flatAlert(message = "empty".desc().asLiveData())
                    },
                    loading = {
                        flatAlert(
                            style = flatAlertStyle.copy(
                                background = Background(
                                    color = 0xFFFF0000
                                )
                            ),
                            message = "loading".desc().asLiveData()
                        )
                    },
                    data = { data ->
                        buildWidget(dataScope) {
                            tabs {
                                tab(
                                    title = "first page".desc().asLiveData(),
                                    body = flatAlert(message = data.map { it?.desc() })
                                )
                                tab(
                                    title = "second page".desc().asLiveData(),
                                    body = flatAlert(message = "SECOND".desc().asLiveData())
                                )
                            }
                        }
                    },
                    error = { error ->
                        errorScope.flatAlert(message = error.map { it?.desc() })
                    }
                )
            )
        )
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
