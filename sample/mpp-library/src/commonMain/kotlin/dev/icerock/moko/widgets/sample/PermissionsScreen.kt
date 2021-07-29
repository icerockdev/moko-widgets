/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import dev.icerock.moko.widgets.core.widget.button
import dev.icerock.moko.widgets.core.widget.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.permissions.bind
import dev.icerock.moko.widgets.permissions.createPermissionsController
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.WidgetScreen
import dev.icerock.moko.widgets.core.screen.getViewModel
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.text
import kotlinx.coroutines.launch

class PermissionsScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal(
        title = "Permissions sample".desc()
    )

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val viewModel = getViewModel {
            PermissionsViewModel(createPermissionsController())
        }
        viewModel.permissionsController.bind(this)

        return with(theme) {
            constraint(size = WidgetSize.AsParent) {
                val stateText = +text(
                    size = WidgetSize.WrapContent,
                    text = viewModel.statusText
                )

                val requestBtn = +button(
                    size = WidgetSize.WrapContent,
                    content = ButtonWidget.Content.Text(Value.data("request".desc())),
                    onTap = viewModel::onRequestPressed
                )

                constraints {
                    stateText centerXToCenterX root
                    stateText centerYToCenterY root

                    requestBtn centerXToCenterX root
                    requestBtn topToBottom stateText
                }
            }
        }
    }
}

class PermissionsViewModel(
    val permissionsController: PermissionsController
) : ViewModel() {
    private val _statusText = MutableLiveData(initialValue = "undefined")
    val statusText: LiveData<StringDesc> = _statusText.map { it.desc() }

    @Suppress("TooGenericExceptionCaught")
    fun onRequestPressed() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _statusText.value = "camera granted"
            } catch (exception: Exception) {
                _statusText.value = "camera denied: $exception"
            }
        }
    }
}
