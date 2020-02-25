/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.SearchScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class ProductsSearchScreen(
    private val theme: Theme
) : SearchScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal(
        title = "Products search".desc(),
        styles = NavigationBar.Normal.Styles(backgroundColor = Color(0xAA3333FF))
    )

    private val products: List<String> = List(size = 50) { "It's product $it" }

    override val searchPlaceholder: StringDesc = "Search".desc()
    override val searchQuery: MutableLiveData<String> = MutableLiveData(initialValue = "")
    override val searchItems: LiveData<List<TableUnitItem>> = searchQuery.map { query ->
        products
            .filter { it.contains(query) }
            .map { ProductUnitItem(theme, itemId = it.hashCode().toLong(), data = it) }
    }

    class ProductUnitItem(
        private val theme: Theme,
        itemId: Long,
        data: String
    ) : WidgetsTableUnitItem<String>(itemId, data) {
        override val reuseId: String = "ProductUnitItem"

        override fun createWidget(data: LiveData<String>): UnitItemRoot {
            return with(theme) {
                constraint(
                    size = WidgetSize.WidthAsParentHeightWrapContent
                ) {
                    val title = +text(
                        size = WidgetSize.WidthAsParentHeightWrapContent,
                        text = data.map { it.desc() as StringDesc }
                    )

                    constraints {
                        title topToTop root offset 8
                        title leftRightToLeftRight root offset 16
                        title bottomToBottom root
                    }
                }
            }.let { UnitItemRoot.from(it) }
        }
    }
}
