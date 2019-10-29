/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import android.widget.LinearLayout
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

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