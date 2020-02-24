package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.style.view.WidgetSize

expect abstract class SearchScreen<A: Args>(
    theme: Theme,
    size: WidgetSize,
    id: ListWidget.Id
): Screen<A> {
    abstract val searchQuery: MutableLiveData<String>
    abstract val searchItems: LiveData<List<TableUnitItem>>
}
