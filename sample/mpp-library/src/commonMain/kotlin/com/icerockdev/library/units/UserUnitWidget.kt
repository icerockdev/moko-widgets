/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.clickable
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class UserUnitWidget(
    private val theme: Theme
) {
    fun createWidget(data: LiveData<Data>) =
        with(theme) {
            clickable(
                child = linear(
                    size = WidgetSize.WidthAsParentHeightWrapContent,
                    orientation = Orientation.HORIZONTAL
//                    factory = DefaultLinearWidgetViewFactory(
//                        DefaultLinearWidgetViewFactoryBase.Style(
//                            orientation = Orientation.HORIZONTAL,
//                            padding = PaddingValues(padding = 8f)
//                        )
//                    )
                ) {
                    +image(
//                        factory = DefaultImageWidgetViewFactory(
//                            DefaultImageWidgetViewFactoryBase.Style(
//                                scaleType = DefaultImageWidgetViewFactoryBase.ScaleType.FILL
//                            )
//                        ),
                        size = WidgetSize.Const(
                            width = SizeSpec.Exact(48f),
                            height = SizeSpec.Exact(48f)
                        ),
                        image = data.map { Image.network(it.avatarUrl) }
                    )
                    +linear(
                        size = WidgetSize.WidthAsParentHeightWrapContent//,
//                        factory = DefaultLinearWidgetViewFactory(
//                            DefaultLinearWidgetViewFactoryBase.Style(
//                                orientation = Orientation.VERTICAL,
//                                padding = PaddingValues(padding = 8f)
//                            )
//                        )
                    ) {
                        +text(
                            size = WidgetSize.Const(
                                SizeSpec.AsParent,
                                SizeSpec.WrapContent
                            ),
                            text = const("Name:")
                        )
                        +text(
                            size = WidgetSize.Const(
                                SizeSpec.AsParent,
                                SizeSpec.WrapContent
                            ),
                            text = data.map { it.name.desc() as StringDesc }
                        )
                    }
                },
                onClick = {
                    data.value.onClick()
                }
            )
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
