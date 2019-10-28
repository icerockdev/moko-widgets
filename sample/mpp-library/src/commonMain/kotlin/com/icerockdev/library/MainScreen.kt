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
import dev.icerock.moko.widgets.style.background.ShapeType

class MainScreen(
    private val widgetScope: WidgetScope
) : Screen<MainViewModel, MainScreen.Args>() {
    override fun createViewModel(arguments: Args): MainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: MainViewModel): Widget {
        return buildWidget(scope = widgetScope) {
            val errorScope = childScope {
                flatAlertStyle = flatAlertStyle.copy(
                    background = Background(
                        color = 0xFFFF0000.toInt()
                    )
                )
            }
            val dataScope = childScope {
                flatAlertStyle = flatAlertStyle.copy(
                    background = Background(
                        color = 0xFF000000.toInt()
                    )
                )
            }

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
                    dataScope.tabs {
                        tab(
                            title = "first page".desc().asLiveData(),
                            body = flatAlert(message = data.map { it?.desc() })
                        )
                        tab(
                            title = "second page".desc().asLiveData(),
                            body = flatAlert(message = "SECOND".desc().asLiveData())
                        )
                    }
                },
                error = { error ->
                    errorScope.flatAlert(message = error.map { it?.desc() })
                }
            )
        }
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
