package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.screen.navigation.Route

expect abstract class SearchScreen<A: Args>: Screen<A> {
    abstract val searchQuery: MutableLiveData<String>
    abstract val searchItems: LiveData<List<TableUnitItem>>
}
