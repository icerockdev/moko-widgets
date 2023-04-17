/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.InputLengthWidget

actual open class InputLengthViewFactory : ViewFactory<InputLengthWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: InputLengthWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        // TODO fixme
//        widget.maxLength.mergeWith(widget.child.field.data) { maxLength, userInput ->
//            if (maxLength != null && userInput.length > maxLength) {
//                widget.child.field.data.value = userInput.take(maxLength)
//            }
//        }

        return bundle
    }
}
