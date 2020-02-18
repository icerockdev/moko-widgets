/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

actual fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>,
    onDismiss: () -> Unit
) {
    val context = context ?: return
    val dialog = BottomSheetDialog(context)
    dialog.setContentView(
        content.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = this,
                parent = null
            )
        ).view
    )
    dialog.setOnCancelListener { onDismiss() }
    dialog.show()
}
