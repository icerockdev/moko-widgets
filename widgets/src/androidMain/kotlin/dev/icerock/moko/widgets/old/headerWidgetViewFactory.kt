/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

//actual var headerWidgetViewFactory: VFC<HeaderWidget> = { context: ViewFactoryContext,
//                                                          widget: HeaderWidget ->
//    TODO()
//    val layoutInflater = LayoutInflater.from(context.context)
//    val binding: WidgetHeaderBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_header, context.parent, false)
//
//    binding.widget = widget
//
//    val style = widget.style
//
//    with(binding.textView) {
//        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
//            setDpMargins(
//                context.context.resources,
//                style.margins.start, style.margins.top, style.margins.end, style.margins.bottom
//            )
//        }
//
//        setTextColor(style.textStyle.color)
//        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textStyle.size.toFloat())
//
//        style.background?.let {
//            background = it.buildBackground(context.context)
//        }
//    }
//
//    binding.lifecycleOwner = context.lifecycleOwner
//
//    binding.root
//}
