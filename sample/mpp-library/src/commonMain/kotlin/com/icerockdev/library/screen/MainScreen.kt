/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import com.icerockdev.library.MainViewModel
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.tabs

open class MainScreen(
    private val widgetScope: WidgetScope,
    private val cryptoScope: WidgetScope,
    private val social1Scope: WidgetScope,
    private val social2Scope: WidgetScope,
    private val mcommerceScope: WidgetScope
) : Screen<MainViewModel, MainScreen.Args>() {
    override fun createViewModel(arguments: Args): MainViewModel {
        return MainViewModel(title = arguments.title)
    }

    override fun createWidget(viewModel: MainViewModel): Widget {
        return with(widgetScope) {
            tabs {
                tab(
                    title = const("P#4"),
                    body = cryptoScope.cryptoProfileScreen(viewModel)
                )
                tab(
                    title = const("P#1"),
                    body = social1Scope.socialProfileScreen(viewModel)
                )
                tab(
                    title = const("P#2"),
                    body = social2Scope.socialProfileScreen(viewModel)
                )
                tab(
                    title = const("P#3"),
                    body = mcommerceScope.mcommerceProfileScreen(viewModel)
                )
                tab(
                    title = const("D"),
                    body = demoScreen(viewModel)
                )
            }
        }
    }

    private fun WidgetScope.socialProfileScreen(viewModel: MainViewModel): Widget {
        return SocialProfileScreen(this).createWidget(viewModel)
    }

    private fun WidgetScope.mcommerceProfileScreen(viewModel: MainViewModel): Widget {
        return McommerceProfileScreen(this).createWidget(viewModel)
    }

    private fun WidgetScope.cryptoProfileScreen(viewModel: MainViewModel): Widget {
        return CryptoProfileScreen(this).createWidget(viewModel)
    }

    private fun WidgetScope.demoScreen(viewModel: MainViewModel): Widget {
        return DemoScreen(this).createWidget(viewModel)
    }

    @Parcelize
    data class Args(val title: String) : Parcelable
}
