/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import com.icerockdev.library.SharedFactory
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class WidgetsScreen(
    private val sharedFactory: SharedFactory,
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            container(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.AsParent),
                children = emptyMap()
            )
//            tabs(
//                tabs = listOf(
//                    TabsWidget.Tab(
//                        title = const("P#2"),
//                        body = SocialProfileScreen(
//                            theme = AppTheme.socialWidgetScope,
//                            viewModel = SocialProfileViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("P#4"),
//                        body = CryptoProfileScreen(
//                            theme = AppTheme.cryptoWidgetScope,
//                            viewModel = CryptoProfileViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("P#1"),
//                        body = SocialProfileScreen(
//                            theme = this,
//                            viewModel = SocialProfileViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("P#3"),
//                        body = McommerceProfileScreen(
//                            theme = AppTheme.mcommerceWidgetScope,
//                            viewModel = McommerceProfileViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("D"),
//                        body = StateScreen(
//                            theme = this,
//                            viewModel = StateViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("P"),
//                        body = PostsScreen(
//                            theme = this,
//                            viewModel = PostsViewModel()
//                        ).createWidget()
//                    ),
//                    TabsWidget.Tab(
//                        title = const("U"),
//                        body = UsersScreen(
//                            theme = this,
//                            viewModel = UsersViewModel(sharedFactory.usersUnitsFactory)
//                        ).createWidget()
//                    )
//                )
//            )
        }
    }
}
