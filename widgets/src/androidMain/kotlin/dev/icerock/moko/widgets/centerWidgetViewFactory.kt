package com.icerockdev.mpp.widgets

import android.widget.FrameLayout

actual var centerWidgetViewFactory: VFC<CenterWidget> = { context: ViewFactoryContext,
                                                          widget: CenterWidget ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner
    val parent = context.parent

    val childParent = if (parent is FrameLayout) parent else FrameLayout(ctx)

    val childView = widget.child.buildView(ViewFactoryContext(ctx, lifecycleOwner, childParent))
    val frameLayoutParams = childView.layoutParams as? FrameLayout.LayoutParams
    frameLayoutParams?.gravity = android.view.Gravity.CENTER

    if (childParent == parent) {
        childView
    } else {
        childParent.addView(childView)
        childParent
    }
}
