/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.core.filesystem.File
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var filesWidgetViewFactory: VFC<FilesWidget>

class FilesWidget(
    private val factory: VFC<FilesWidget> = filesWidgetViewFactory,
    val field: FormField<List<File>, StringDesc>,
    val addListener: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
