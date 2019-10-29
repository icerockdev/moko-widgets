package dev.icerock.moko.widgets.core

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class BasePreviewView<Contract, Args> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    abstract fun createScreen(): Screen<Contract, Args>
    abstract fun createContract(): Contract

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