/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import com.icerockdev.library.MainViewModel
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.ViewModelProvider
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.tabs

open class MainScreen(
    private val widgetScope: WidgetScope,
    private val cryptoScope: WidgetScope,
    private val social1Scope: WidgetScope,
    private val social2Scope: WidgetScope,
    private val mcommerceScope: WidgetScope,
    private val viewModelProvider: ViewModelProvider<MainViewModel, Args>
) : Screen<MainViewModel, MainScreen.Args>(),
    ViewModelProvider<MainViewModel, MainScreen.Args> by viewModelProvider {

    override fun createWidget(viewModel: MainViewModel): AnyWidget {
        return with(widgetScope) {
            tabs(
                tabs = listOf(
                    TabsWidget.TabWidget(
                        title = const("P#2"),
                        body = social2Scope.socialProfileScreen(viewModel)
                    ),
                    TabsWidget.TabWidget(
                        title = const("U"),
                        body = UsersScreen(this, viewModel).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#4"),
                        body = cryptoScope.cryptoProfileScreen(viewModel)
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#1"),
                        body = social1Scope.socialProfileScreen(viewModel)
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#3"),
                        body = mcommerceScope.mcommerceProfileScreen(viewModel)
                    ),
                    TabsWidget.TabWidget(
                        title = const("D"),
                        body = demoScreen(viewModel)
                    )
                )
            )
        }
    }

    private fun WidgetScope.socialProfileScreen(viewModel: MainViewModel): AnyWidget {
        return SocialProfileScreen(this, viewModel).createWidget()
    }

    private fun WidgetScope.mcommerceProfileScreen(viewModel: MainViewModel): AnyWidget {
        return McommerceProfileScreen(this, viewModel).createWidget()
    }

    private fun WidgetScope.cryptoProfileScreen(viewModel: MainViewModel): AnyWidget {
        return CryptoProfileScreen(this, viewModel).createWidget()
    }

    private fun WidgetScope.demoScreen(viewModel: MainViewModel): AnyWidget {
        return DemoScreen(this, viewModel).createWidget()
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
