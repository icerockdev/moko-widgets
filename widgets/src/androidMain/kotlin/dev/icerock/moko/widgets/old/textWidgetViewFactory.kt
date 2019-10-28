package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var textWidgetViewFactory: VFC<TextWidget> = { context: ViewFactoryContext,
                                                      widget: TextWidget ->
    TODO()
//    val ctx = context.context
//    val lifecycleOwner = context.lifecycleOwner
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetTextBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_text, parent, false)
//    binding.widget = widget
//    binding.lifecycleOwner = lifecycleOwner
//    binding.root
//
//    binding.root
}
