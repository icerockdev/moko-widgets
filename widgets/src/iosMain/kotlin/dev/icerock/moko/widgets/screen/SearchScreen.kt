package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.Theme


actual abstract class SearchScreen<A : Args> actual constructor() : Screen<A>() {
    actual abstract val searchQuery: MutableLiveData<String>
    actual abstract val searchItems: LiveData<List<TableUnitItem>>
    actual abstract fun onReachEnd()
    actual open val theme: Theme? = null
    actual open val listCategory: ListWidget.Category? = null
}
