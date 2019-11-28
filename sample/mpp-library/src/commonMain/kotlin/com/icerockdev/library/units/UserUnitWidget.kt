/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class UserUnitWidget(
    private val theme: Theme
) {
    fun createWidget(data: LiveData<Data>) =
        with(theme) {
            container(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.WrapContent),
                children = emptyMap()
            )
//            linear(
//                styled = {
//                    it.copy(
//                        size = WidgetSize.Const(
//                            width = SizeSpec.AsParent,
//                            height = SizeSpec.WrapContent
//                        ),
//                        orientation = Orientation.HORIZONTAL,
//                        padding = PaddingValues(padding = 8f)
//                    )
//                },
//                children = listOf(
//                    image(
//                        styled = {
//                            it.copy(
//                                size = WidgetSize.Const(
//                                    width = SizeSpec.Exact(48f),
//                                    height = SizeSpec.Exact(48f)
//                                ),
//                                scaleType = ImageWidget.ScaleType.FILL
//                            )
//                        },
//                        image = data.map { Image.network(it.avatarUrl) }
//                    ),
//                    linear(
//                        styled = {
//                            it.copy(
//                                size = WidgetSize.Const(
//                                    width = SizeSpec.AsParent,
//                                    height = SizeSpec.WrapContent
//                                ),
//                                orientation = Orientation.VERTICAL,
//                                padding = PaddingValues(start = 8f)
//                            )
//                        },
//                        children = listOf(
//                            text(text = const("Name:")),
//                            text(text = data.map { it.name.desc() })
//                        )
//                    )
//                )
//            )
        }

    data class Data(
        val name: String,
        val avatarUrl: String,
        val onClick: () -> Unit
    )

    class TableUnitItem(
        theme: Theme,
        itemId: Long,
        data: Data
    ) : WidgetsTableUnitItem<Data>(itemId, data) {
        private val unitWidget = UserUnitWidget(theme)

        override val reuseId: String = "UserUnitItem"

        override fun createWidget(data: LiveData<Data>): UnitItemRoot {
            return unitWidget.createWidget(data).let { UnitItemRoot.from(it) }
        }
    }

    class CollectionUnitItem(
        theme: Theme,
        itemId: Long,
        data: Data
    ) : WidgetsCollectionUnitItem<Data>(itemId, data) {
        private val unitWidget = UserUnitWidget(theme)

        override val reuseId: String = "UserUnitItem"

        override fun createWidget(data: LiveData<Data>): UnitItemRoot {
            return unitWidget.createWidget(data).let { UnitItemRoot.from(it) }
        }
    }
}
