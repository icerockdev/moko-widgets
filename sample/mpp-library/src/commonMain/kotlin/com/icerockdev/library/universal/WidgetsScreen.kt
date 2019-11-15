/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import com.icerockdev.library.SharedFactory
import com.icerockdev.library.Theme
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
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.tabs

class WidgetsScreen : WidgetScreen<Args.Empty>() {
    private val sharedFactory = SharedFactory() // TODO change system

    override fun createContentWidget(): Widget {
        return with(WidgetScope()) {
            tabs(
                tabs = listOf(
                    TabsWidget.Tab(
                        title = const("P#2"),
                        body = SocialProfileScreen(
                            widgetScope = Theme.socialWidgetScope,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("P#4"),
                        body = CryptoProfileScreen(
                            widgetScope = Theme.cryptoWidgetScope,
                            viewModel = CryptoProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("P#1"),
                        body = SocialProfileScreen(
                            widgetScope = this,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("P#3"),
                        body = McommerceProfileScreen(
                            widgetScope = Theme.mcommerceWidgetScope,
                            viewModel = McommerceProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("D"),
                        body = StateScreen(
                            widgetScope = this,
                            viewModel = StateViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("P"),
                        body = PostsScreen(
                            widgetScope = this,
                            viewModel = PostsViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.Tab(
                        title = const("U"),
                        body = UsersScreen(
                            widgetScope = this,
                            viewModel = UsersViewModel(sharedFactory.usersUnitsFactory)
                        ).createWidget()
                    )
                )
            )
        }
    }
}