/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val widget = createContentWidget()
        val view = widget.buildView(
            ViewFactoryContext(
                context = requireContext(),
                lifecycleOwner = this,
                parent = container
            )
        ).view
        // TODO support margins?

        if (isDismissKeyboardOnTap) {
            view.setOnTouchListener { _, _ ->
                val activity = requireActivity()
                val imm = activity
                    .getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
                activity.currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                    it.clearFocus()
                }

                false
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        if (isKeyboardResizeContent) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            if (isScrollListOnKeyboardResize) {
                var lastRecyclerOffset = 0
                val recyclerView = view?.let { findRecyclerView(it) }
                recyclerView?.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy != 0) {
                            lastRecyclerOffset = recyclerView.computeVerticalScrollOffset()
                        }
                    }
                })
                recyclerView?.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    val currentOffset = recyclerView.computeVerticalScrollOffset()
                    recyclerView.scrollBy(0, (lastRecyclerOffset - currentOffset) + oldBottom - bottom)
                })
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (isKeyboardResizeContent) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        }
    }

    private fun findRecyclerView(view: View): RecyclerView? {
        return when (view) {
            is RecyclerView -> view
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    val childView = view.getChildAt(i)

                    val rv = findRecyclerView(childView)
                    if (rv != null) return rv
                }

                null
            }
            else -> null
        }
    }

    actual open val isKeyboardResizeContent: Boolean = false
    actual open val isDismissKeyboardOnTap: Boolean = false
    actual open val isScrollListOnKeyboardResize: Boolean = false
}
