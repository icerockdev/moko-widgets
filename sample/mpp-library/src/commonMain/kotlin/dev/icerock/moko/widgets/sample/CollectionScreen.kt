/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.widget.ImageWidget
import dev.icerock.moko.widgets.core.widget.ListWidget
import dev.icerock.moko.widgets.collection.CollectionWidget
import dev.icerock.moko.widgets.collection.collection
import dev.icerock.moko.widgets.core.widget.constraint
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.widget.image
import dev.icerock.moko.widgets.imagenetwork.network
import dev.icerock.moko.widgets.core.widget.list
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.WidgetScreen
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.units.UnitItemRoot
import dev.icerock.moko.widgets.core.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.core.units.WidgetsTableUnitItem

class CollectionScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal(title = "Collection in list".desc())

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            constraint(size = WidgetSize.AsParent) {

                val content = +content()

                constraints {
                    content topToTop root.safeArea
                    content leftRightToLeftRight root
                    content bottomToBottom root.safeArea
                }
            }
        }
    }

    private fun Theme.content() = list(
        size = WidgetSize.Const(
            width = SizeSpec.AsParent,
            height = SizeSpec.MatchConstraint
        ),
        id = Ids.List,
        items = listItems()
    )

    private fun Theme.listItems() = List<List<String>>(10) {
        listOf<String>(
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
            "https://cdn.pixabay.com/photo/2015/02/24/15/41/dog-647528__340.jpg",
            "https://images.pexels.com/photos/814499/pexels-photo-814499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            "https://dab1nmslvvntp.cloudfront.net/wp-content/uploads/2016/03/1458289957powerful-images3.jpg"
        )
    }.mapIndexed { index, urls ->
        ImageSliderUnit(
            itemId = index.toLong(),
            data = urls.map { Image.network(it) },
            theme = this
        ) as TableUnitItem
    }.let { MutableLiveData(it) }

    object Ids {
        object List : ListWidget.Id
    }
}

class ImageSliderUnit(
    itemId: Long,
    data: List<Image>,
    private val theme: Theme
) : WidgetsTableUnitItem<List<Image>>(itemId, data) {
    override val reuseId: String = "ImageSliderUnit"

    override fun createWidget(data: LiveData<List<Image>>): UnitItemRoot {
        return UnitItemRoot.from(theme.createUnitWidget(data))
    }

    private fun Theme.createUnitWidget(data: LiveData<List<Image>>) = collection(
        size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.Exact(182f)),
        id = Id.Collection,
        items = data.map {
            it.mapIndexed { index, image ->
                CollectionImageUnitItem(
                    itemId = index.toLong(),
                    data = image,
                    theme = theme
                ) as CollectionUnitItem
            }
        }
    )

    object Id {
        object Collection : CollectionWidget.Id
    }
}

class CollectionImageUnitItem(
    itemId: Long,
    data: Image,
    private val theme: Theme
) : WidgetsCollectionUnitItem<Image>(itemId, data) {
    override val reuseId: String = "CollectionImageUnitItem"

    override fun createWidget(data: LiveData<Image>): Widget<out WidgetSize> {
        return with(theme) {
            image(
                size = WidgetSize.Const(SizeSpec.Exact(312f), SizeSpec.Exact(182f)),
                id = Id.Image,
                scaleType = ImageWidget.ScaleType.FILL,
                image = data
            )
        }
    }

    object Id {
        object Image : ImageWidget.Id
    }
}
