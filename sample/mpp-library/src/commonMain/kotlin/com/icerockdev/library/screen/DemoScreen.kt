/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.buttonStyle
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.core.asLiveData
import dev.icerock.moko.widgets.flatAlert
import dev.icerock.moko.widgets.flatAlertFactory
import dev.icerock.moko.widgets.flatAlertStyle
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.stateful
import dev.icerock.moko.widgets.statefulStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.background.ShapeType
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.tabs

open class DemoScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): AnyWidget {
        return with(widgetScope) {
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

            linear(
                childs = listOf(
                    linear(
                        styled = {
                            it.copy(
                                orientation = Orientation.HORIZONTAL,
                                size = WidgetSize(
                                    width = SizeSpec.AS_PARENT,
                                    height = SizeSpec.WRAP_CONTENT
                                )
                            )
                        },
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
                                factory = flatAlertFactory,
                                style = flatAlertStyle.copy(
                                    background = Background(
                                        color = 0xFFFF0000
                                    )
                                ),
                                message = "loading".desc().asLiveData()
                            )
                        },
                        data = { data ->
                            with(dataScope) {
                                tabs(
                                    tabs = listOf(
                                        TabsWidget.TabWidget(
                                            title = const("first page"),
                                            body = flatAlert(message = data.map { it?.desc() })
                                        ),
                                        TabsWidget.TabWidget(
                                            title = const("second page"),
                                            body = flatAlert(message = "SECOND".desc().asLiveData())
                                        )
                                    )
                                )
                            }
                        },
                        error = { error ->
                            errorScope.flatAlert(message = error.map { it?.desc() })
                        }
                    )
                )
            )
        }
    }


    interface ViewModelContract {
        val state: LiveData<State<String, String>>

        fun onChangeStatePressed()
    }
}
