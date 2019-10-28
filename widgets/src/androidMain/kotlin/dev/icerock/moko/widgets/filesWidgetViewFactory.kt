package com.icerockdev.mpp.widgets.forms

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.core.filesystem.File
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetFilesBinding
import com.icerockdev.mpp.mvvm.livedata.map

actual var filesWidgetViewFactory: VFC<FilesWidget> = { context: ViewFactoryContext,
                                                        widget: FilesWidget ->

    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetFilesBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_files, parent, false)

    binding.bindList(widget)

    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
private fun WidgetFilesBinding.bindList(fileWidget: FilesWidget) {
    widget = fileWidget
    files = fileWidget.field.data.map { files ->
        files.map { file ->
            ItemCreateDocument()
                .setFile(file)
                .setOnItemDelete({
                    removeFile(fileWidget, file)
                } as kotlin.jvm.functions.Function0<Unit>)
        }.plus(
            ItemCreateDocumentAdd()
                .setOnAddClick({
                    fileWidget.addListener()
                } as kotlin.jvm.functions.Function0<Unit>)
        )
    }.ld()
}

private fun removeFile(fileWidget: FilesWidget, file: File) {
    val list = mutableListOf<File>()
    list.addAll(fileWidget.field.value())
    list.remove(file)
    fileWidget.field.data.postValue(list)
}
