/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import dev.icerock.moko.widgets.core.widget.button
import dev.icerock.moko.widgets.core.widget.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.widget.flatAlert
import dev.icerock.moko.widgets.core.widget.linear
import dev.icerock.moko.widgets.core.widget.progressBar
import dev.icerock.moko.widgets.core.widget.stateful
import dev.icerock.moko.widgets.core.style.background.Orientation
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.tabs
import dev.icerock.moko.widgets.core.widget.text
import dev.icerock.moko.widgets.core.utils.asLiveData

open class StateScreen(
    private val theme: Theme,
    private val viewModel: StateViewModelContract
) {
    fun createWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            linear(size = WidgetSize.AsParent) {
                +linear(
                    size = WidgetSize.WidthAsParentHeightWrapContent,
                    orientation = Orientation.HORIZONTAL
                ) {
                    +button(
                        size = WidgetSize.WrapContent,
                        content = ButtonWidget.Content.Text(Value.data("change state".desc())),
                        onTap = viewModel::onChangeStatePressed
                    )
                    +button(
                        size = WidgetSize.WrapContent,
                        content = ButtonWidget.Content.Text(Value.data("just log button".desc())),
                        onTap = { println("pressed!") }
                    )
                }
                +stateful(
                    size = WidgetSize.AsParent,
                    state = viewModel.state,
                    empty = {
                        container(size = WidgetSize.AsParent) {
                            center {
                                text(
                                    size = WidgetSize.WrapContent,
                                    text = const("empty")
                                )
                            }
                        }
                    },
                    loading = {
                        container(size = WidgetSize.AsParent) {
                            center {
                                progressBar(size = WidgetSize.WrapContent)
                            }
                        }
                    },
                    data = { data ->
                        tabs(size = WidgetSize.AsParent) {
                            tab(
                                title = const("first page"),
                                body = flatAlertWrapped(message = data.map { it?.desc() })
                            )
                            tab(
                                title = const("second page"),
                                body = flatAlertWrapped(message = "SECOND".desc().asLiveData())
                            )
                        }
                    },
                    error = { error ->
                        flatAlertWrapped(message = error.map { it?.desc() })
                    }
                )
            }
        }
    }

    private fun Theme.flatAlertWrapped(message: LiveData<StringDesc?>) =
        flatAlert(
            size = WidgetSize.AsParent,
            message = message.map { it ?: "".desc() as StringDesc? },
            buttonText = const("press me".desc() as StringDesc?),
            onTap = { println("pressed") }
        )
}

interface StateViewModelContract {
    val state: LiveData<ResourceState<String, String>>

    fun onChangeStatePressed()
}

class StateViewModel : ViewModel(), StateViewModelContract {
    private val _state: MutableLiveData<ResourceState<String, String>> =
        MutableLiveData(initialValue = ResourceState.Empty())
    override val state: LiveData<ResourceState<String, String>> = _state

    override fun onChangeStatePressed() {
        when (state.value) {
            is ResourceState.Empty -> _state.value = ResourceState.Loading()
            is ResourceState.Loading -> {
                _state.value = ResourceState.Success(data = "hello!")
            }
            is ResourceState.Success -> _state.value = ResourceState.Failed(error = "this is error")
            is ResourceState.Failed -> _state.value = ResourceState.Empty()
        }
    }
}
