package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var scrollWidgetViewFactory: VFC<ScrollWidget>

@WidgetDef
class ScrollWidget(
    override val factory: VFC<ScrollWidget>,
    override val style: Style,
    override val id: Id?,
    val child: AnyWidget
) : Widget<ScrollWidget>(), Styled<ScrollWidget.Style>, OptionalId<ScrollWidget.Id> {
    data class Style(
        val size: WidgetSize = WidgetSize(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        ),
        val orientation: Orientation = Orientation.VERTICAL,
        val background: Background? = null,
        override val margins: MarginValues = MarginValues(),
        override val padding: PaddingValues = PaddingValues()
    ) : Padded, Margined

    interface Id : WidgetScope.Id
}