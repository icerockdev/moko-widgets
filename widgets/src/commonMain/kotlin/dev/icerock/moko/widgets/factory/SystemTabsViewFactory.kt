/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Border
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemTabsViewFactory(
    tabsTintColor: Color? = null,
    selectedTitleColor: Color? = null,
    normalTitleColor: Color? = null,
    tabsBackground: Background<Fill.Solid>? = null,
    contentBackground: Background<out Fill>? = null,
    tabsPadding: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    margins: MarginValues? = null
) : ViewFactory<TabsWidget<out WidgetSize>>
