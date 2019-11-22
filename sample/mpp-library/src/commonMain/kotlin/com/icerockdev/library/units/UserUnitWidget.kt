/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.linear
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
                        size = dev.icerock.moko.widgets.style.view.WidgetSize.Const(
                            width = dev.icerock.moko.widgets.style.view.SizeSpec.AS_PARENT,
                            height = dev.icerock.moko.widgets.style.view.SizeSpec.WRAP_CONTENT
                        ),
                        orientation = dev.icerock.moko.widgets.style.background.Orientation.HORIZONTAL,
                        padding = dev.icerock.moko.widgets.style.view.PaddingValues(padding = 8f)
                    )
                },
                children = listOf(
                    image(
                        styled = {
                            it.copy(
                                size = dev.icerock.moko.widgets.style.view.WidgetSize.Const(
                                    width = 48,
                                    height = 48
                                ),
                                scaleType = ImageWidget.ScaleType.FILL
                            )
                        },
                        image = data.map { dev.icerock.moko.widgets.core.Image.network(it.avatarUrl) }
                    ),
                    linear(
                        styled = {
                            it.copy(
                                size = dev.icerock.moko.widgets.style.view.WidgetSize.Const(
                                    width = dev.icerock.moko.widgets.style.view.SizeSpec.AS_PARENT,
                                    height = dev.icerock.moko.widgets.style.view.SizeSpec.WRAP_CONTENT
                                ),
                                orientation = dev.icerock.moko.widgets.style.background.Orientation.VERTICAL,
                                padding = dev.icerock.moko.widgets.style.view.PaddingValues(start = 8f)
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
