package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.filesystem.File
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.View
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.Widget

expect var filesWidgetViewFactory: VFC<FilesWidget>

class FilesWidget(
    private val _factory: VFC<FilesWidget> = filesWidgetViewFactory,
    val field: FormField<List<File>, StringDesc>,
    val addListener: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)
}

fun FormWidget.filesField(
    field: FormField<List<File>, StringDesc>,
    addListener: (() -> Unit)
) {
    val widget = FilesWidget(
        field = field,
        addListener = addListener
    )
    add(widget)
}