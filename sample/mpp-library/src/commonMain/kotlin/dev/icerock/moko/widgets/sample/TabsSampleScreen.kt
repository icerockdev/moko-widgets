/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import com.icerockdev.library.units.UserUnitWidget
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.widget.ContainerWidget
import dev.icerock.moko.widgets.core.widget.ListWidget
import dev.icerock.moko.widgets.core.widget.TabsWidget
import dev.icerock.moko.widgets.core.widget.constraint
import dev.icerock.moko.widgets.core.widget.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.factory.ContainerViewFactory
import dev.icerock.moko.widgets.core.factory.SystemTabsViewFactory
import dev.icerock.moko.widgets.core.widget.linear
import dev.icerock.moko.widgets.core.widget.list
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.WidgetScreen
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Direction
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.SelectableState
import dev.icerock.moko.widgets.core.style.view.Colors
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.tabs
import dev.icerock.moko.widgets.core.utils.platformSpecific

class TabsSampleScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal(
        title = "Tabs sample".desc(),
        styles = NavigationBar.Styles(
            backgroundColor = backgroundColor,
            tintColor = tintColor,
            textStyle = TextStyle(
                color = textColor
            ),
            isShadowEnabled = false
        )
    )

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            constraint(size = WidgetSize.AsParent) {
                val tabs = +tabs(size = WidgetSize.Const(SizeSpec.MatchConstraint, SizeSpec.MatchConstraint)) {
                    tab(
                        title = const("Active".desc()),
                        body = buildContent()
                    )

                    tab(
                        title = const("Done".desc()),
                        body = buildContent()
                    )
                }

                constraints {
                    tabs topToTop root.safeArea
                    tabs leftRightToLeftRight root
                    tabs bottomToBottom root.safeArea
                }
            }
        }
    }

    private fun Theme.buildContent() = {
        val items = List(20) {
            UserUnitWidget.TableUnitItem(
                theme = this@buildContent,
                itemId = it.toLong(),
                data = UserUnitWidget.Data(
                    name = "item $it",
                    avatarUrl = "https://i.imgur.com/cVDadwb.png",
                    onClick = {}
                )
            ) as TableUnitItem
        }

        linear(size = WidgetSize.AsParent) {
            +container(
                size = WidgetSize.Const(
                    width = SizeSpec.AsParent,
                    height = SizeSpec.Exact(platformSpecific(android = 4f, ios = 2f))
                )
            ) {}

            +list(
                size = WidgetSize.AsParent,
                id = Ids.List,
                items = const(items)
            )
        }
    }()

    object Ids {
        object List : ListWidget.Id
    }

    companion object {
        val tintColor = Color(0xD20C0AFF)
        val backgroundColor = Color(0xFFFFFFFF)
        val textColor = Color(0x151515FF)

        fun configureDefaultTheme(theme: Theme.Builder) = with(theme) {
            factory[TabsWidget.DefaultCategory] = SystemTabsViewFactory(
                tabsTintColor = tintColor,
                tabsBackground = Background(
                    fill = Fill.Solid(backgroundColor)
                ),
                tabsPadding = platformSpecific(
                    android = null,
                    ios = PaddingValues(start = 16f, end = 16f, bottom = 16f)
                ),
                titleColor = SelectableState(
                    selected = platformSpecific(android = null, ios = Colors.white),
                    unselected = null
                )
            )
            factory[ContainerWidget.DefaultCategory] = ContainerViewFactory(
                background = Background(
                    fill = Fill.Gradient(
                        colors = listOf(Color(0x00000000), Color(0x00000010)),
                        direction = Direction.BOTTOM_TOP
                    )
                )
            )
        }
    }
}
