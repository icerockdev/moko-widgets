package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.style.background.Background

expect abstract class SearchScreen<A : Args>() : Screen<A> {
    abstract val searchQuery: MutableLiveData<String>
    abstract val searchItems: LiveData<List<TableUnitItem>>
    abstract fun onReachEnd()

    open val searchHint: StringDesc?
    open val background: Background?

    open val androidBackItem: Image?
}
