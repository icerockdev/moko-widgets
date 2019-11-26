/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class UserUnitWidget(
    private val widgetScope: WidgetScope
) {
    fun createWidget(data: LiveData<Data>): Widget {
        return with(widgetScope) {
            linear(
                styled = {
                    it.copy(
                        size = WidgetSize.Const(
                            width = SizeSpec.AsParent,
                            height = SizeSpec.WrapContent
                        ),
                        orientation = Orientation.HORIZONTAL,
                        padding = PaddingValues(padding = 8f)
                    )
                },
                children = listOf(
                    image(
                        styled = {
                            it.copy(
                                size = WidgetSize.Const(
                                    width = SizeSpec.Exact(48f),
                                    height = SizeSpec.Exact(48f)
                                ),
                                scaleType = ImageWidget.ScaleType.FILL
                            )
                        },
                        image = data.map { Image.network(it.avatarUrl) }
                    ),
                    linear(
                        styled = {
                            it.copy(
                                size = WidgetSize.Const(
                                    width = SizeSpec.AsParent,
                                    height = SizeSpec.WrapContent
                                ),
                                orientation = Orientation.VERTICAL,
                                padding = PaddingValues(start = 8f)
                            )
                        },
                        children = listOf(
                            text(text = const("Name:")),
                            text(text = data.map { it.name.desc() })
                        )
                    )
                )
            )
        }
    }

    data class Data(
        val name: String,
        val avatarUrl: String,
        val onClick: () -> Unit
    )

    class TableUnitItem(
        widgetScope: WidgetScope,
        itemId: Long,
        data: Data
    ) : WidgetsTableUnitItem<Data>(itemId, data) {
        private val unitWidget = UserUnitWidget(widgetScope)

        override val reuseId: String = "UserUnitItem"

        override fun createWidget(data: LiveData<Data>): Widget {
            return unitWidget.createWidget(data)
        }
    }

    class CollectionUnitItem(
        widgetScope: WidgetScope,
        itemId: Long,
        data: Data
    ) : WidgetsCollectionUnitItem<Data>(itemId, data) {
        private val unitWidget = UserUnitWidget(widgetScope)

        override val reuseId: String = "UserUnitItem"

        override fun createWidget(data: LiveData<Data>): Widget {
            return unitWidget.createWidget(data)
        }
    }
}
