/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class LoadingUnitWidget(
    private val theme: Theme
) {
    fun createWidget(data: LiveData<Unit>): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.Exact>> {
        return with(theme) {
            container(
                size = WidgetSize.Const(
                    width = SizeSpec.AsParent,
                    height = SizeSpec.Exact(48f)
                ),
                children = mapOf(
//                    progressBar(
//                        styled = {
//                            it.copy(
//                                size = WidgetSize.Const(
//                                    width = SizeSpec.Exact(24f),
//                                    height = SizeSpec.Exact(24f)
//                                ),
//                                color = Colors.black
//                            )
//                        }
//                    ) to Alignment.CENTER
                )
            )
        }
    }

    class TableUnitItem(
        theme: Theme,
        itemId: Long
    ) : WidgetsTableUnitItem<Unit>(itemId, Unit) {
        private val unitWidget = LoadingUnitWidget(theme)

        override val reuseId: String = "LoadingUnitItem"

        override fun createWidget(data: LiveData<Unit>): UnitItemRoot {
            return unitWidget.createWidget(data).let { UnitItemRoot.from(it) }
        }
    }

    class CollectionUnitItem(
        theme: Theme,
        itemId: Long
    ) : WidgetsCollectionUnitItem<Unit>(itemId, Unit) {
        private val unitWidget = LoadingUnitWidget(theme)

        override val reuseId: String = "LoadingUnitItem"

        override fun createWidget(data: LiveData<Unit>): UnitItemRoot {
            return unitWidget.createWidget(data).let { UnitItemRoot.from(it) }
        }
    }
}
