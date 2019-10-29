/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.*
import dev.icerock.moko.widgets.core.*
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.background.ShapeType
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

open class MainScreen(
    private val widgetScope: WidgetScope
) : Screen<IMainViewModel, MainScreen.Args>() {
    override fun createViewModel(arguments: Args): IMainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: IMainViewModel): Widget {
        return buildWidget(scope = widgetScope) {
            val errorScope = childScope {
                this.flatAlertStyle = flatAlertStyle.copy(
                    background = Background(
                        color = 0xFF00FF00.toInt()
                    )
                )
            }
            val dataScope = childScope {
                this.flatAlertStyle = flatAlertStyle.copy(
                    background = Background(
                        color = 0xFFFF00FF.toInt()
                    )
                )
            }

            linear(
                childs = listOf(
                    linear(
                        style = linearStyle.copy(
                            orientation = Orientation.HORIZONTAL,
                            size = WidgetSize(
                                width = SizeSpec.AS_PARENT,
                                height = SizeSpec.WRAP_CONTENT
                            )
                        ),
                        childs = listOf(
                            button(
                                text = "change state".desc().asLiveData(),
                                onTap = viewModel::onChangeStatePressed
                            ),
                            button(
                                text = "just button".desc().asLiveData(),
                                onTap = {}
                            )
                        )
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
                                        color = 0xFFFF0000.toInt()
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
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
