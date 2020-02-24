/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import platform.UIKit.UIViewController

actual abstract class SearchScreen<A : Args> : Screen<A>() {
    actual abstract val searchPlaceholder: StringDesc
    actual abstract val searchQuery: MutableLiveData<String>
    actual abstract val searchItems: LiveData<List<TableUnitItem>>

    override fun createViewController(isLightStatusBar: Boolean?): UIViewController {
        return SearchViewController(
            isLightStatusBar = isLightStatusBar,
            searchQuery = searchQuery,
            searchItems = searchItems,
            searchPlaceholder = searchPlaceholder
        )
    }
}
