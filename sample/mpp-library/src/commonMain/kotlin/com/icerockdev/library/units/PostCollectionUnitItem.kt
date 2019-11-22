/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import com.icerockdev.library.sample.PostsViewModelContract
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.Colors
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem

class PostCollectionUnitItem(
    private val widgetScope: WidgetScope,
    itemId: Long,
    data: PostsViewModelContract.PostItem
) : WidgetsCollectionUnitItem<PostsViewModelContract.PostItem>(itemId, data) {
    override val reuseId: String = "PostUnitItem"

    override fun createWidget(data: LiveData<PostsViewModelContract.PostItem>): Widget {
        return with(widgetScope) {
            createBody(data)
        }
    }

    private fun WidgetScope.createBody(data: LiveData<PostsViewModelContract.PostItem>): Widget {
        return container(
            styled = {
                it.copy(
                    size = WidgetSize.AspectByWidth(
                        width = SizeSpec.AS_PARENT,
                        aspectRatio = 0.73f
                    ),
                    background = Background(
                        fill = Fill.Solid(Color(0x66, 0x66, 0x66, 0xFF))
                    ),
                    margins = MarginValues(4f)
                )
            },
            children = mapOf(
                image(
                    styled = {
                        it.copy(
                            size = WidgetSize.Const(
                                width = SizeSpec.AS_PARENT,
                                height = SizeSpec.AS_PARENT
                            ),
                            scaleType = ImageWidget.ScaleType.FILL
                        )
                    },
                    image = data.map { Image.network(it.imageUrl) }
                ) to Alignment.CENTER,
                createHeader(data) to Alignment.TOP,
                createFooter(data) to Alignment.BOTTOM
            )
        )
    }

    private fun WidgetScope.createHeader(data: LiveData<PostsViewModelContract.PostItem>): Widget {
        return container(
            styled = {
                it.copy(
                    size = WidgetSize.Const(
                        width = SizeSpec.AS_PARENT,
                        height = SizeSpec.WRAP_CONTENT
                    ),
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
            },
            children = mapOf(
                text(
                    styled = {
                        it.copy(
                            padding = PaddingValues(8f),
                            textStyle = TextStyle(
                                size = 12,
                                color = Colors.white
                            )
                        )
                    },
                    text = data.map { it.nickname }
                ) to Alignment.CENTER
            )
        )
    }

    private fun WidgetScope.createFooter(data: LiveData<PostsViewModelContract.PostItem>): Widget {
        val regularItems = listOf(
            text(
                styled = {
                    it.copy(
                        textStyle = TextStyle(
                            size = 12,
                            color = Colors.white
                        )
                    )
                },
                text = data.map { it.viewsCount }
            ),
            text(
                styled = {
                    it.copy(
                        textStyle = TextStyle(
                            size = 11,
                            color = Colors.white
                        )
                    )
                },
                text = data.map { it.title }
            )
        )
        return container(
            styled = {
                it.copy(
                    size = WidgetSize.Const(
                        width = SizeSpec.AS_PARENT,
                        height = SizeSpec.WRAP_CONTENT
                    ),
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
            },
            children = mapOf(
                linear(
                    styled = {
                        it.copy(
                            size = WidgetSize.Const(
                                width = SizeSpec.AS_PARENT,
                                height = SizeSpec.WRAP_CONTENT
                            ),
                            padding = PaddingValues(8f)
                        )
                    },
                    children = regularItems
                ) to Alignment.CENTER
            )
        )
    }
}
