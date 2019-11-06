package com.icerockdev.library.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linearList
import dev.icerock.moko.widgets.units.styled

class UsersScreen(
    private val widgetScope: WidgetScope,
    private val viewModel: ViewModelContract
) {
    fun createWidget(): AnyWidget {
        return with(widgetScope) {
            linearList(
                //TODO: Relocate mapping in linearList(...)
                items = viewModel.items.map{ units ->
                    units.map { it.styled(this) }
                }
            )
        }
    }

    interface ViewModelContract {
        val items: LiveData<List<UnitItem>>
    }
}