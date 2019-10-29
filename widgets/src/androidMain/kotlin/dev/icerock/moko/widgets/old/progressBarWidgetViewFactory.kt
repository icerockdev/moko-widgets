/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import android.view.LayoutInflater
import dev.icerock.moko.widgets.R
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var progressBarWidgetViewFactory: VFC<ProgressBarWidget> = { context: ViewFactoryContext,
                                                                    widget: ProgressBarWidget ->
    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)

    layoutInflater.inflate(R.layout.widget_progressbar, parent, false)
}
