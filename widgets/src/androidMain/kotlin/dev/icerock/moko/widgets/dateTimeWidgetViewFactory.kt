package com.icerockdev.mpp.widgets.forms

import android.widget.LinearLayout
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext

actual var dateTimeWidgetViewFactory: VFC<DateTimeWidget> = { context: ViewFactoryContext,
                                                              widget: DateTimeWidget ->

    val linearLayout = LinearLayout(context.context)
    linearLayout.orientation = when (widget.orientation) {
        FormWidget.Group.Orientation.VERTICAL -> LinearLayout.VERTICAL
        FormWidget.Group.Orientation.HORIZONTAL -> LinearLayout.HORIZONTAL
    }

    widget.items.forEach {
        linearLayout.addView(it.buildView(context))
    }
    linearLayout
}