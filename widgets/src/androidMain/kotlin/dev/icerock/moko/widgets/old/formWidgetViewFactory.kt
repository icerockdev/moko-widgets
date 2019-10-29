/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var formWidgetViewFactory: VFC<FormWidget> = { context: ViewFactoryContext,
                                                      widget: FormWidget ->
    TODO()

//    val ctx = context.context
//    val style = widget.style
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetFormBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_form, parent, false)
//
//    binding.widget = widget
//
//    binding.formLayout.apply {
//        orientation = when (style.orientation) {
//            FormWidget.Group.Orientation.VERTICAL -> LinearLayout.VERTICAL
//            FormWidget.Group.Orientation.HORIZONTAL -> LinearLayout.HORIZONTAL
//        }
//
//        val density = ctx.resources.displayMetrics.density
//
//        setPadding(
//            (style.paddings.start * density).toInt(),
//            (style.paddings.top * density).toInt(),
//            (style.paddings.end * density).toInt(),
//            (style.paddings.bottom * density).toInt()
//        )
//
//        clipChildren = false
//        clipToPadding = false
//    }
//
//    val layoutFactoryContext = dev.icerock.moko.widgets.core.ViewFactoryContext(
//        context = ctx,
//        lifecycleOwner = context.lifecycleOwner,
//        parent = binding.formLayout
//    )
//    widget.items.forEach {
//        binding.formLayout.addView(it.buildView(layoutFactoryContext))
//    }
//
//    binding.setLifecycleOwner(context.lifecycleOwner)
//    binding.root
}