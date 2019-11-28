/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import com.icerockdev.library.sample.PostsViewModelContract
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.factory.DefaultContainerWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultContainerWidgetViewFactoryBase
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem

class PostCollectionUnitItem(
    private val theme: Theme,
    itemId: Long,
    data: PostsViewModelContract.PostItem
) : WidgetsCollectionUnitItem<PostsViewModelContract.PostItem>(itemId, data) {
    override val reuseId: String = "PostUnitItem"


    override fun createWidget(data: LiveData<PostsViewModelContract.PostItem>): UnitItemRoot {
        return with(theme) {
            UnitItemRoot.from(createBody(data))
        }
    }

    private fun Theme.createBody(data: LiveData<PostsViewModelContract.PostItem>) =
        container(
            factory = DefaultContainerWidgetViewFactory(
                DefaultContainerWidgetViewFactoryBase.Style(
                    background = Background(
                        fill = Fill.Solid(Color(0x66, 0x66, 0x66, 0xFF))
                    ),
                    margins = MarginValues(4f)
                )
            ),
            size = WidgetSize.AspectByWidth(
                width = SizeSpec.AsParent,
                aspectRatio = 0.73f
            ),
            children = mapOf(
//                image(
//                    styled = {
//                        it.copy(
//                            size = WidgetSize.Const(
//                                width = SizeSpec.AsParent,
//                                height = SizeSpec.AsParent
//                            ),
//                            scaleType = ImageWidget.ScaleType.FILL
//                        )
//                    },
//                    image = data.map { Image.network(it.imageUrl) }
//                ) to Alignment.CENTER,
                createHeader(data) to Alignment.TOP,
                createFooter(data) to Alignment.BOTTOM
            )
        )

    private fun Theme.createHeader(data: LiveData<PostsViewModelContract.PostItem>) =
        container(
            factory = DefaultContainerWidgetViewFactory(
                DefaultContainerWidgetViewFactoryBase.Style(
                    background = Background(
                        fill = Fill.Gradient(
                            colors = listOf(
                                Color(0x00, 0x00, 0x00, 0x99),
                                Color(0x00, 0x00, 0x00, 0x00)
                            ),
                            direction = Direction.TOP_BOTTOM
                        )
                    ),
                    padding = PaddingValues(bottom = 8f)
                )
            ),
            size = WidgetSize.Const(
                width = SizeSpec.AsParent,
                height = SizeSpec.WrapContent
            ),
            children = mapOf(
//                text(
//                    styled = {
//                        it.copy(
//                            padding = PaddingValues(8f),
//                            textStyle = TextStyle(
//                                size = 12,
//                                color = Colors.white
//                            )
//                        )
//                    },
//                    text = data.map { it.nickname }
//                ) to Alignment.CENTER
            )
        )

    private fun Theme.createFooter(
        data: LiveData<PostsViewModelContract.PostItem>
    ): Widget<out WidgetSize> {
        val regularItems = listOf<Widget<out WidgetSize>>(
//            text(
//                styled = {
//                    it.copy(
//                        textStyle = TextStyle(
//                            size = 12,
//                            color = Colors.white
//                        )
//                    )
//                },
//                text = data.map { it.viewsCount }
//            ),
//            text(
//                styled = {
//                    it.copy(
//                        textStyle = TextStyle(
//                            size = 11,
//                            color = Colors.white
//                        )
//                    )
//                },
//                text = data.map { it.title }
//            )
        )
        return container(
            size = WidgetSize.Const(
                width = SizeSpec.AsParent,
                height = SizeSpec.WrapContent
            ),
            factory = DefaultContainerWidgetViewFactory(
                DefaultContainerWidgetViewFactoryBase.Style(
                    background = Background(
                        fill = Fill.Gradient(
                            colors = listOf(
                                Color(0x00, 0x00, 0x00, 0x99),
                                Color(0x00, 0x00, 0x00, 0x00)
                            ),
                            direction = Direction.BOTTOM_TOP
                        )
                    ),
                    padding = PaddingValues(top = 16f)
                )
            ),
            children = mapOf(
//                linear(
//                    styled = {
//                        it.copy(
//                            size = WidgetSize.Const(
//                                width = SizeSpec.AsParent,
//                                height = SizeSpec.WrapContent
//                            ),
//                            padding = PaddingValues(8f)
//                        )
//                    },
//                    children = regularItems
//                ) to Alignment.CENTER
            )
        )
    }
}
