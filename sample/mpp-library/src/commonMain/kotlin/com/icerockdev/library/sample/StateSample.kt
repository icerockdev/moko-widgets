/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

open class StateScreen(
    private val theme: Theme,
    private val viewModel: StateViewModelContract
) {
    fun createWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            linear(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.AsParent),
                children = listOf(
                    button(
                        size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.WrapContent),
                        text = const("change state"),
                        onTap = viewModel::onChangeStatePressed
                    )//,
//                    stateful(
//                        state = viewModel.state,
//                        empty = {
//                            container(
//                                children = mapOf(
//                                    text(
//                                        styled = {
//                                            it.copy(
//                                                size = WidgetSize.Const(
//                                                    width = SizeSpec.WrapContent,
//                                                    height = SizeSpec.WrapContent
//                                                )
//                                            )
//                                        },
//                                        text = const("empty")
//                                    ) to Alignment.CENTER
//                                )
//                            )
//                        },
//                        loading = {
//                            container(
//                                children = mapOf(
//                                    progressBar() to Alignment.CENTER
//                                )
//                            )
//                        },
//                        data = { data ->
//                            tabs(
//                                tabs = listOf(
//                                    TabsWidget.Tab(
//                                        title = const("first page"),
//                                        body = flatAlertWrapped(message = data.map { it?.desc() })
//                                    ),
//                                    TabsWidget.Tab(
//                                        title = const("second page"),
//                                        body = flatAlertWrapped(message = "SECOND".desc().asLiveData())
//                                    )
//                                )
//                            )
//                        },
//                        error = { error ->
//                            flatAlertWrapped(message = error.map { it?.desc() })
//                        }
//                    )
                )
            )
        }
    }

    private fun Theme.flatAlertWrapped(message: LiveData<StringDesc?>): Widget<out WidgetSize> {
        return container(
            size = WidgetSize.Const(
                width = SizeSpec.AsParent,
                height = SizeSpec.AsParent
            ),
            children = mapOf(
//                flatAlert(message = message) to Alignment.CENTER
            )
        )
    }
}

interface StateViewModelContract {
    val state: LiveData<State<String, String>>

    fun onChangeStatePressed()
}

class StateViewModel : ViewModel(), StateViewModelContract {
    private val _state: MutableLiveData<State<String, String>> =
        MutableLiveData(initialValue = State.Empty())
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
