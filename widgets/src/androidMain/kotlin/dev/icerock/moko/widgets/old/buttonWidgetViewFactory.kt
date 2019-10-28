package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var buttonWidgetViewFactory: VFC<ButtonWidget> = { context: ViewFactoryContext,
                                                          widget: ButtonWidget ->
    TODO()
//    val ctx = context.context
//    val dm = ctx.resources.displayMetrics
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetButtonBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_button, parent, false)
//
//    val style = widget.style
//
//    with(binding.buttonWidget) {
//        layoutParams = LinearLayout.LayoutParams(
//            widget.style.size.width.toPlatformSize(dm),
//            widget.style.size.height.toPlatformSize(dm)
//        ).apply {
//            setDpMargins(
//                resources = ctx.resources,
//                marginStart = style.margins.start,
//                marginTop = style.margins.top,
//                marginEnd = style.margins.end,
//                marginBottom = style.margins.bottom
//            )
//        }
//
//        style.background?.let {
//            val rippleDrawable = RippleDrawable(
//                ColorStateList.valueOf(Color.GRAY),
//                it.buildBackground(ctx), null
//            )
//
//            background = rippleDrawable
//        }
//        setTextColor(style.textStyle.color)
//        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textStyle.size.toFloat())
//        isAllCaps = style.isAllCaps
//    }
//
//    binding.widget = widget
//    binding.lifecycleOwner = context.lifecycleOwner
//    binding.root
}
