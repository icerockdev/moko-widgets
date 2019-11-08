/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.screen

import com.icerockdev.library.PostUnitItem
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.collection
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.view.PaddingValues

class PostsScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): AnyWidget = widgetScope.createWidget()

    private fun WidgetScope.createWidget(): AnyWidget {
        return collection(
            id = Id.Collection,
            items = viewModel.posts.map { posts ->
                posts.map { post ->
                    PostUnitItem(
                        widgetScope = this,
                        itemId = post.id,
                        data = post
                    )
                }
            },
            styled = {
                it.copy(
                    padding = PaddingValues(4f)
                )
            }
        )
    }

    object Id {
        object Collection : CollectionWidget.Id
    }

    interface ViewModelContract {
        val posts: LiveData<List<PostItem>>
    }

    interface PostItem {
        val id: Long
        val nickname: StringDesc
        val imageUrl: String
        val viewsCount: StringDesc
        val title: StringDesc
        val tags: StringDesc?
        val onClick: () -> Unit
    }
}