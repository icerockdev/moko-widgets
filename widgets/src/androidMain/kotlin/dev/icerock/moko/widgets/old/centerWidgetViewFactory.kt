/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var centerWidgetViewFactory: VFC<CenterWidget> = { context: ViewFactoryContext,
                                                          widget: CenterWidget ->
    TODO()
//    val ctx = context.context
//    val lifecycleOwner = context.lifecycleOwner
//    val parent = context.parent
//
//    val childParent = if (parent is FrameLayout) parent else FrameLayout(ctx)
//
//    val childView = widget.child.buildView(
//        dev.icerock.moko.widgets.core.ViewFactoryContext(
//            ctx,
//            lifecycleOwner,
//            childParent
//        )
//    )
//    val frameLayoutParams = childView.layoutParams as? FrameLayout.LayoutParams
//    frameLayoutParams?.gravity = android.view.Gravity.CENTER
//
//    if (childParent == parent) {
//        childView
//    } else {
//        childParent.addView(childView)
//        childParent
//    }
}
