/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.widget.ListWidget
import dev.icerock.moko.widgets.collection.CollectionWidget
import dev.icerock.moko.widgets.collection.collection
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.widget.list
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.tabs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersScreen(
    private val theme: Theme,
    private val viewModel: UsersViewModelContract
) {
    fun createWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            tabs(size = WidgetSize.AsParent) {
                tab(
                    title = const("list"),
                    body = list(
                        size = WidgetSize.AsParent,
                        id = Id.List,
                        items = viewModel.tableItems,
                        onRefresh = viewModel::refresh,
                        onReachEnd = viewModel::loadNextPage
                    )
                )
                tab(
                    title = const("collection"),
                    body = collection(
                        size = WidgetSize.AsParent,
                        id = Id.Collection,
                        items = viewModel.collectionItems,
                        onRefresh = viewModel::refresh,
                        onReachEnd = viewModel::loadNextPage
                    )
                )
            }
        }
    }

    object Id {
        object List : ListWidget.Id
        object Collection : CollectionWidget.Id
    }
}

interface UsersViewModelContract {
    val tableItems: LiveData<List<TableUnitItem>>
    val collectionItems: LiveData<List<CollectionUnitItem>>

    fun refresh(completion: () -> Unit)
    fun loadNextPage()
}

class UsersViewModel(
    private val unitsFactory: UnitsFactory
) : ViewModel(), UsersViewModelContract {
    private val _loadNextPage = MutableLiveData(false)
    private val _items: MutableLiveData<List<Pair<String, String>>> = MutableLiveData(
        initialValue = listOf(
            "Aleksey Mikhailov" to "https://avatars0.githubusercontent.com/u/5010169",
            "Alexandr Pogrebnyak" to "https://avatars1.githubusercontent.com/u/10958304"
        )
    )
    override val tableItems: LiveData<List<TableUnitItem>> = _items.map { items ->
        items.map { (name, avatarUrl) ->
            val id = name.hashCode().toLong()
            unitsFactory.createUserTableUnit(
                itemId = id,
                name = name,
                avatarUrl = avatarUrl
            ) {
                println("clicked $name user")
            }
        }
    }.mergeWith(_loadNextPage) { items, loadPage ->
        if (loadPage) {
            items.plus(unitsFactory.createLoadingTableUnit(-1))
        } else {
            items
        }
    }
    override val collectionItems: LiveData<List<CollectionUnitItem>> = _items.map { items ->
        items.map { (name, avatarUrl) ->
            val id = name.hashCode().toLong()
            unitsFactory.createUserCollectionUnit(
                itemId = id,
                name = name,
                avatarUrl = avatarUrl
            ) {
                println("clicked $name user")
            }
        }
    }.mergeWith(_loadNextPage) { items, loadPage ->
        if (loadPage) {
            items.plus(unitsFactory.createLoadingCollectionUnit(-1))
        } else {
            items
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
            _loadNextPage.value = true

            delay(1000)

            _loadNextPage.value = false

            val currentItems = _items.value
            _items.value = currentItems.plus(currentItems.map { it.copy(first = it.first + "+") })
        }
    }

    interface UnitsFactory {
        fun createUserTableUnit(
            itemId: Long,
            name: String,
            avatarUrl: String,
            onClick: () -> Unit
        ): TableUnitItem

        fun createUserCollectionUnit(
            itemId: Long,
            name: String,
            avatarUrl: String,
            onClick: () -> Unit
        ): CollectionUnitItem

        fun createLoadingTableUnit(
            itemId: Long
        ): TableUnitItem

        fun createLoadingCollectionUnit(
            itemId: Long
        ): CollectionUnitItem
    }
}
