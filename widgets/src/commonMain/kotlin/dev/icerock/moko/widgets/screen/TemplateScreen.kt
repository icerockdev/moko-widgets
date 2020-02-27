package dev.icerock.moko.widgets.screen

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text

class TemplateScreen (
    private val labelText: StringDesc,
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {
    override fun createContentWidget() : Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> = with(theme) {
        constraint(size = WidgetSize.AsParent) {
            val copyrightText = +text(
                size = WidgetSize.WrapContent,
                text = const(labelText)
            )

            constraints {
                copyrightText centerXToCenterX root
                copyrightText centerYToCenterY root
            }
        }
    }
}
