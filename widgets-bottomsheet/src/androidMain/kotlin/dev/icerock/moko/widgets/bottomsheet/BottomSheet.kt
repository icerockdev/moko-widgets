/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

actual fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>,
    onDismiss: (isSelfDismissed: Boolean) -> Unit
): SelfDismisser? {
    val context = context ?: return null
    val dialog = DismissedBottomSheetDialog(context, onDismiss)
    dialog.setContentView(
        content.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = this,
                parent = null
            )
        ).view
    )
    dialog.setOnCancelListener { onDismiss(false) }
    dialog.show()
    return dialog
}

private class DismissedBottomSheetDialog(context: Context, val onDismiss: (Boolean) -> Unit) :
    BottomSheetDialog(context), SelfDismisser {
    override fun dismissSelf() {
        this.dismiss()
        onDismiss(true)
    }
}