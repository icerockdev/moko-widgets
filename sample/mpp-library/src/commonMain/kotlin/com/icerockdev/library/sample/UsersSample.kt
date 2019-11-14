/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.collection
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.list
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.tabs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: UsersViewModelContract
) {
    fun createWidget(): Widget {
        return with(widgetScope) {
            tabs(
                tabs = listOf(
                    TabsWidget.Tab(
                        title = const("list"),
                        body = list(
                            id = Id.List,
                            items = viewModel.items,
                            styled = {
                                it.copy(
                                    padding = PaddingValues(8f)
                                )
                            },
                            onRefresh = viewModel::refresh,
                            onReachEnd = viewModel::loadNextPage
                        )
                    ),
                    TabsWidget.Tab(
                        title = const("collection"),
                        body = collection(
                            id = Id.Collection,
                            items = viewModel.items,
                            styled = {
                                it.copy(
                                    padding = PaddingValues(8f)
                                )
                            },
                            onRefresh = viewModel::refresh,
                            onReachEnd = viewModel::loadNextPage
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
}

interface UsersViewModelContract {
    val items: LiveData<List<UnitItem>>

    fun refresh(completion: () -> Unit)
    fun loadNextPage()
}

class UsersViewModel(
    private val unitsFactory: UnitsFactory
) : ViewModel(), UsersViewModelContract {
    private val _items: MutableLiveData<List<String>> = MutableLiveData(
        initialValue = listOf(
            "Aleksey Mikhailov",
            "Alexandr Pogrebnyak",
            "Andrey Breslav",
            "Nikolay Igotti"
        )
    )
    override val items: LiveData<List<UnitItem>> = _items.map { items ->
        items.map { name ->
            val id = name.hashCode().toLong()
            unitsFactory.createUserUnit(
                itemId = id,
                name = name,
                avatarUrl = "https://avatars0.githubusercontent.com/u/5010169"
            ) {
                println("clicked $name user")
            }
        }
    }

    private var refreshJob: Job? = null
    private var nextPageJob: Job? = null

    override fun refresh(completion: () -> Unit) {
        if (refreshJob?.isActive == true || nextPageJob?.isActive == true) {
            completion()
            return
        }

        refreshJob = viewModelScope.launch {
            delay(1000)

            val currentItems = _items.value
            _items.value = currentItems.shuffled()

            completion()
        }
    }

    override fun loadNextPage() {
        if (refreshJob?.isActive == true || nextPageJob?.isActive == true) return

        nextPageJob = viewModelScope.launch {
            val currentItems = _items.value
            _items.value = currentItems.plus(currentItems)
        }
    }

    interface UnitsFactory {
        fun createUserUnit(
            itemId: Long,
            name: String,
            avatarUrl: String,
            onClick: () -> Unit
        ): UnitItem
    }
}
