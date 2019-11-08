/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import com.icerockdev.library.sample.CryptoProfileScreen
import com.icerockdev.library.sample.CryptoProfileViewModel
import com.icerockdev.library.sample.McommerceProfileScreen
import com.icerockdev.library.sample.McommerceProfileViewModel
import com.icerockdev.library.sample.PostsScreen
import com.icerockdev.library.sample.PostsViewModel
import com.icerockdev.library.sample.SocialProfileScreen
import com.icerockdev.library.sample.SocialProfileViewModel
import com.icerockdev.library.sample.StateScreen
import com.icerockdev.library.sample.StateViewModel
import com.icerockdev.library.sample.UsersScreen
import com.icerockdev.library.sample.UsersViewModel
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.ViewModelProvider
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.tabs

open class HostScreen(
    private val widgetScope: WidgetScope,
    private val cryptoScope: WidgetScope,
    private val social1Scope: WidgetScope,
    private val social2Scope: WidgetScope,
    private val mcommerceScope: WidgetScope,
    private val usersUnitsFactory: UsersViewModel.UnitsFactory
) : Screen<HostScreen.DummyVM, HostScreen.Args>(),
    ViewModelProvider<HostScreen.DummyVM, HostScreen.Args> {

    // delete later, unused idea
    override fun createViewModel(arguments: Args): DummyVM = DummyVM()

    override fun createWidget(viewModel: DummyVM): AnyWidget {
        return with(widgetScope) {
            tabs(
                tabs = listOf(
                    TabsWidget.TabWidget(
                        title = const("P"),
                        body = PostsScreen(
                            widgetScope = this,
                            viewModel = PostsViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("U"),
                        body = UsersScreen(
                            widgetScope = this,
                            viewModel = UsersViewModel(usersUnitsFactory)
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#2"),
                        body = SocialProfileScreen(
                            widgetScope = social2Scope,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#4"),
                        body = CryptoProfileScreen(
                            widgetScope = cryptoScope,
                            viewModel = CryptoProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#1"),
                        body = SocialProfileScreen(
                            widgetScope = social1Scope,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#3"),
                        body = McommerceProfileScreen(
                            widgetScope = mcommerceScope,
                            viewModel = McommerceProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("D"),
                        body = StateScreen(
                            widgetScope = this,
                            viewModel = StateViewModel()
                        ).createWidget()
                    )
                )
            )
        }
    }

    @Parcelize
    data class Args(val title: String) : Parcelable

    class DummyVM : ViewModel()
}
