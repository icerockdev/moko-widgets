package com.icerockdev.library

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.WidgetsUnitItem

class UserUnitItem(
    private val widgetScope: WidgetScope,
    itemId: Long,
    data: Data
) : WidgetsUnitItem<UserUnitItem.Data>(itemId, data) {
    override val reuseId: String = "UserUnitItem"

    override fun createWidget(data: LiveData<Data>): Widget<*> {
        return with(widgetScope) {
            linear(
                styled = {
                    it.copy(
                        size = WidgetSize(
                            width = SizeSpec.AS_PARENT,
                            height = SizeSpec.WRAP_CONTENT
                        ),
                        orientation = Orientation.VERTICAL,
                        padding = PaddingValues(top = 8f, start = 8f, end = 8f)
                    )
                },
                childs = listOf(
                    text(text = const("Name:")),
                    text(text = data.map { it.name.desc() })
                )
            )
        }
    }

    data class Data(
        val name: String,
        val avatarUrl: String,
        val onClick: () -> Unit
    )
}
