/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.widgets.LinearWidget
import dev.icerock.moko.widgets.ScrollWidget
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.scroll
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

abstract class ScrollContentScreen<A: Args>(
    protected val theme: Theme
) : WidgetScreen<A>() {

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            constraint(size = WidgetSize.AsParent) {

                val content = +content()

                constraints {
                    content topToTop root.safeArea
                    content leftRightToLeftRight root
                    content bottomToBottom root.safeArea
                }
            }
        }
    }

    private fun Theme.content() = scroll(
        size = WidgetSize.Const(
            width = SizeSpec.AsParent,
            height = SizeSpec.MatchConstraint
        ),
        id = Ids.Scroll,
        child = linear(
            id = Ids.ScrollContent,
            size = WidgetSize.WidthAsParentHeightWrapContent
        ) {
            fillLinear(this@content)
        }
    )

    abstract fun LinearWidget.ChildrenBuilder.fillLinear(theme: Theme)

    object Ids {
        object Scroll : ScrollWidget.Id
        object ScrollContent : LinearWidget.Id
    }
}
