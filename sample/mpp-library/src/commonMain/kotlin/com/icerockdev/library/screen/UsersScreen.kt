/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.collection
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.list
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.tabs

class UsersScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): AnyWidget {
        return with(widgetScope) {
            tabs(
                tabs = listOf(
                    TabsWidget.TabWidget(
                        title = const("list"),
                        body = list(
                            id = Id.List,
                            items = viewModel.items,
                            styled = {
                                it.copy(
                                    padding = PaddingValues(8f)
                                )
                            }
                        )
                    ),
                    TabsWidget.TabWidget(
                        title = const("collection"),
                        body = collection(
                            id = Id.Collection,
                            items = viewModel.items,
                            styled = {
                                it.copy(
                                    padding = PaddingValues(8f)
                                )
                            }
                        )
                    )
                )
            )
        }
    }

    object Id {
        object List : ListWidget.Id
        object Collection : CollectionWidget.Id
    }

    interface ViewModelContract {
        val items: LiveData<List<UnitItem>>
    }
}