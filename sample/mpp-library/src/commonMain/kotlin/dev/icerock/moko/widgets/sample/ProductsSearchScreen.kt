/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.list
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import dev.icerock.moko.widgets.units.UnitItemRoot
import dev.icerock.moko.widgets.units.WidgetsTableUnitItem

class ProductsSearchScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {

    private val viewModel: ProductsSearchViewModel by lazy {
        getViewModel { ProductsSearchViewModel() }
    }

    override val navigationBar: NavigationBar
        get() = NavigationBar.Search(
            title = "Products search".desc(),
            styles = NavigationBar.Styles(
                backgroundColor = Color(0xFFFFFFFF),
                tintColor = Color(0x111111FF)
            ),
            searchQuery = viewModel.searchQuery,
            searchPlaceholder = "Product title".desc(),
            androidSearchBackground = Background(
                fill = Fill.Solid(color = Color(0xF2F2F2FF)),
                cornerRadius = 2f
            )
        )

    override fun createContentWidget() = with(theme) {
        constraint(size = WidgetSize.AsParent) {
            val results = +list(
                size = WidgetSize.Const(width = SizeSpec.MatchConstraint, height = SizeSpec.MatchConstraint),
                items = viewModel.products.map { products ->
                    products.map { ProductUnitItem(theme, itemId = it.hashCode().toLong(), data = it) as TableUnitItem }
                },
                id = Ids.ResultsList
            )

            constraints {
                results topToTop root.safeArea
                results leftRightToLeftRight root
                results bottomToBottom root.safeArea
            }
        }
    }

    object Ids {
        object ResultsList : ListWidget.Id
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

class ProductsSearchViewModel : ViewModel() {
    val searchQuery = MutableLiveData(initialValue = "")

    private val _products: List<String> = List(size = 50) { "It's product $it" }

    val products: LiveData<List<String>> = searchQuery.map { query ->
        _products
            .filter { it.contains(query) }
    }
}
