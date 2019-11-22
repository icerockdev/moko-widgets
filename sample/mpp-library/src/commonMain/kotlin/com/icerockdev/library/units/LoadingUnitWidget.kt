/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.progressBar
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.Colors
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class LoadingUnitWidget(
    private val widgetScope: WidgetScope
) {
    fun createWidget(data: LiveData<Unit>): Widget {
        return with(widgetScope) {
            container(
                styled = {
                    it.copy(
                        size = WidgetSize.Const(
                            width = SizeSpec.AS_PARENT,
                            height = 48
                        )
                    )
                },
                children = mapOf(
                    progressBar(
                        styled = {
                            it.copy(
                                size = WidgetSize.Const(width = 24, height = 24),
                                color = Colors.black
                            )
                        }
                    ) to Alignment.CENTER
                )
            )
        }
    }

    class TableUnitItem(
        widgetScope: WidgetScope,
        itemId: Long
    ) : WidgetsTableUnitItem<Unit>(itemId, Unit) {
        private val unitWidget = LoadingUnitWidget(widgetScope)

        override val reuseId: String = "LoadingUnitItem"

        override fun createWidget(data: LiveData<Unit>): Widget {
            return unitWidget.createWidget(data)
        }
    }

    class CollectionUnitItem(
        widgetScope: WidgetScope,
        itemId: Long
    ) : WidgetsCollectionUnitItem<Unit>(itemId, Unit) {
        private val unitWidget = LoadingUnitWidget(widgetScope)

        override val reuseId: String = "LoadingUnitItem"

        override fun createWidget(data: LiveData<Unit>): Widget {
            return unitWidget.createWidget(data)
        }
    }
}
