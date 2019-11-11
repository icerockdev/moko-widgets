/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import com.icerockdev.library.units.PostUnitItem
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.collection
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.view.PaddingValues

class PostsScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: PostsViewModelContract
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
}

interface PostsViewModelContract {
    val posts: LiveData<List<PostItem>>

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

class PostsViewModel : ViewModel(), PostsViewModelContract {
    private val _postsTitle: MutableLiveData<List<String>> = MutableLiveData(
        initialValue = List(20) { "Test $it post" }
    )
    override val posts: LiveData<List<PostsViewModelContract.PostItem>> = _postsTitle.map { titles ->
        titles.map { title ->
            val id = title.hashCode().toLong()
            PostItem(
                id = id,
                nickname = "@alex009".desc(),
                imageUrl = "https://images.unsplash.com/photo-1531804055935-76f44d7c3621",
                viewsCount = "24.5K".desc(),
                title = title.desc(),
                tags = null,
                onClick = { println("clicked $id!") }
            )
        }
    }

    data class PostItem(
        override val id: Long,
        override val nickname: StringDesc,
        override val imageUrl: String,
        override val viewsCount: StringDesc,
        override val title: StringDesc,
        override val tags: StringDesc?,
        override val onClick: () -> Unit
    ) : PostsViewModelContract.PostItem
}