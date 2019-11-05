package com.icerockdev.library.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linearList

class UsersScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): Widget {
        return with(widgetScope) {
            linearList(
                items = viewModel.items
            )
        }
    }

    interface ViewModelContract {
        val items: LiveData<List<UnitItem>>
    }
}