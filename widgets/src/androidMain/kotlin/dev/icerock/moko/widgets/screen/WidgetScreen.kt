/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.app.Service
import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>

    override fun createView(context: Context, parent: ViewGroup?): View {
        val widget = createContentWidget()
        val view = widget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = this,
                parent = parent
            )
        ).view
        // TODO support margins?

        if (isDismissKeyboardOnTap) {
            view.setOnClickListener {
                val activity = requireActivity()
                val imm = activity
                    .getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
                activity.currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        if (isKeyboardResizeContent) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onPause() {
        super.onPause()

        if (isKeyboardResizeContent) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        }
    }

    actual open val isKeyboardResizeContent: Boolean = false
    actual open val isDismissKeyboardOnTap: Boolean = false
}
