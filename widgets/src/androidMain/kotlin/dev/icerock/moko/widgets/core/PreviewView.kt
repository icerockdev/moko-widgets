/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.icerock.moko.mvvm.viewmodel.ViewModel

abstract class BasePreviewView<VM : ViewModel, Args> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    abstract fun createScreen(): Screen<VM, Args>
    abstract fun createContract(): VM

    private val screen = createScreen()

    init {
        val widget = screen.createWidget(createContract())
        val view = widget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = object : LifecycleOwner {
                    override fun getLifecycle(): Lifecycle {
                        return LifecycleRegistry(this).apply {
                            markState(Lifecycle.State.RESUMED)
                        }
                    }
                },
                parent = this
            )
        )
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(view, layoutParams)
    }
}