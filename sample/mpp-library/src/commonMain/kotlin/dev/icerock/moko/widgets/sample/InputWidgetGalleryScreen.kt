/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.widget.LinearWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.widget.input
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.style.view.WidgetSize

class InputWidgetGalleryScreen(
    theme: Theme,
    private val inputs: List<InputInfo>
) : ScrollContentScreen<Args.Empty>(theme), NavigationItem {
    override val navigationBar: NavigationBar = NavigationBar.Normal(title = "InputWidget".desc())

    override fun LinearWidget.ChildrenBuilder.fillLinear(theme: Theme) {
        with(theme) {
            inputs.forEach { inputInfo ->
                +input(
                    size = WidgetSize.WidthAsParentHeightWrapContent,
                    id = inputInfo.id,
                    label = const(inputInfo.label),
                    field = FormField(initialValue = "", validation = liveBlock { null })
                )
            }
        }
    }

    data class InputInfo(
        val id: InputWidget.Id,
        val label: StringDesc
    )
}
