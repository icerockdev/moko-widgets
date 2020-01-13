/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import com.icerockdev.library.MR
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.input
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.listen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.screen.navigation.route
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text

class LoginScreen(
    private val theme: Theme,
    private val mainRoute: Route<Unit>,
    private val loginViewModelFactory: (EventsDispatcher<LoginViewModel.EventsListener>) -> LoginViewModel
) : WidgetScreen<Args.Empty>(), NavigationItem, LoginViewModel.EventsListener {

    override val navigationBar: NavigationBar = NavigationBar.None

    override val isKeyboardResizeContent: Boolean = true
    override val isDismissKeyboardOnTap: Boolean = true

    override fun createContentWidget() = with(theme) {
        val viewModel = getViewModel {
            loginViewModelFactory(createEventsDispatcher())
        }
        viewModel.eventsDispatcher.listen(this@LoginScreen, this@LoginScreen)

        constraint(size = WidgetSize.AsParent) {
            val logoImage = +image(
                size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.WrapContent),
                image = const(Image.resource(MR.images.logo)),
                scaleType = ImageWidget.ScaleType.FIT
            )

            val emailInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.EmailInputId,
                label = const("Email".desc() as StringDesc),
                field = viewModel.emailField,
                inputType = InputType.PHONE
            )
            val passwordInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.PasswordInputId,
                label = const("Password".desc() as StringDesc),
                field = viewModel.passwordField
            )
            val loginButton = +button(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.Exact(50f)),
                content = ButtonWidget.Content.Text(Value.data("Login".desc())),
                onTap = viewModel::onLoginPressed
            )

            val registerButton = +button(
                id = Id.RegistrationButtonId,
                size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.Exact(40f)),
                content = ButtonWidget.Content.Text(Value.data("Registration".desc())),
                onTap = viewModel::onRegistrationPressed
            )

            val copyrightText = +text(
                size = WidgetSize.WrapContent,
                text = const("IceRock Development")
            )

            constraints {
                passwordInput centerYToCenterY root
                passwordInput leftRightToLeftRight root offset 16

                emailInput bottomToTop passwordInput offset 8
                emailInput leftRightToLeftRight root offset 16

                loginButton topToBottom passwordInput
                loginButton leftRightToLeftRight root

                registerButton topToBottom loginButton
                registerButton rightToRight root

                // logo image height must be automatic ?
                logoImage centerXToCenterX root
                logoImage.verticalCenterBetween(
                    top = root.top,
                    bottom = emailInput.top
                )

                copyrightText centerXToCenterX root
                copyrightText bottomToBottom root.safeArea offset 8
            }
        }
    }

    object Id {
        object EmailInputId : InputWidget.Id
        object PasswordInputId : InputWidget.Id
        object RegistrationButtonId : ButtonWidget.Id
    }

    interface Parent {
        fun routeToMain()
    }

    override fun routeToMain() {
        mainRoute.route(this)
    }
}

class LoginViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>
) : ViewModel(), EventsDispatcherOwner<LoginViewModel.EventsListener> {
    val emailField = FormField<String, StringDesc>("", liveBlock { null })
    val passwordField = FormField<String, StringDesc>("", liveBlock { null })

    fun onLoginPressed() {
        eventsDispatcher.dispatchEvent { routeToMain() }
    }

    fun onRegistrationPressed() {}

    interface EventsListener {
        fun routeToMain()
    }
}
