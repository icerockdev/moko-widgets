/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.core.asLiveData
import dev.icerock.moko.widgets.flatAlert
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.progressBar
import dev.icerock.moko.widgets.stateful
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.tabs
import dev.icerock.moko.widgets.text

open class StateScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: StateViewModelContract
) {
    fun createWidget(): Widget {
        return with(widgetScope) {
            linear(
                childs = listOf(
                    button(
                        text = "change state".desc().asLiveData(),
                        onTap = viewModel::onChangeStatePressed
                    ),
                    stateful(
                        state = viewModel.state,
                        empty = {
                            container(
                                childs = mapOf(
                                    text(
                                        styled = {
                                            it.copy(
                                                size = WidgetSize.Const(
                                                    width = SizeSpec.WRAP_CONTENT,
                                                    height = SizeSpec.WRAP_CONTENT
                                                )
                                            )
                                        },
                                        text = const("empty")
                                    ) to Alignment.CENTER
                                )
                            )
                        },
                        loading = {
                            container(
                                childs = mapOf(
                                    progressBar() to Alignment.CENTER
                                )
                            )
                        },
                        data = { data ->
                            tabs(
                                tabs = listOf(
                                    TabsWidget.TabWidget(
                                        title = const("first page"),
                                        body = flatAlertWrapped(message = data.map { it?.desc() })
                                    ),
                                    TabsWidget.TabWidget(
                                        title = const("second page"),
                                        body = flatAlertWrapped(message = "SECOND".desc().asLiveData())
                                    )
                                )
                            )
                        },
                        error = { error ->
                            flatAlertWrapped(message = error.map { it?.desc() })
                        }
                    )
                )
            )
        }
    }

    private fun WidgetScope.flatAlertWrapped(message: LiveData<StringDesc?>): Widget {
        return container(
            styled = {
                it.copy(
                    size = WidgetSize.Const(
                        width = SizeSpec.AS_PARENT,
                        height = SizeSpec.AS_PARENT
                    )
                )
            },
            childs = mapOf(
                flatAlert(message = message) to Alignment.CENTER
            )
        )
    }
}

interface StateViewModelContract {
    val state: LiveData<State<String, String>>

    fun onChangeStatePressed()
}

class StateViewModel : ViewModel(), StateViewModelContract {
    private val _state: MutableLiveData<State<String, String>> = MutableLiveData(initialValue = State.Empty())
    override val state: LiveData<State<String, String>> = _state

    override fun onChangeStatePressed() {
        when (state.value) {
            is State.Empty -> _state.value = State.Loading()
            is State.Loading -> {
                _state.value = State.Data(data = "hello!")
            }
            is State.Data -> _state.value = State.Error(error = "this is error")
            is State.Error -> _state.value = State.Empty()
        }
    }
}
