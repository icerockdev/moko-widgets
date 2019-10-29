/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var inputWidgetViewFactory: VFC<InputWidget>

class InputWidget(
    private val factory: VFC<InputWidget> = inputWidgetViewFactory,
    val label: LiveData<StringDesc>,
    val field: FormField<String, StringDesc>,
    val enabled: LiveData<Boolean>? = null,
    val style: InputStyle,
    val inputType: InputType = InputType.PLAIN_TEXT,
    val maxLines: LiveData<Int?>? = null
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property errorTextStyle error message text style
     * @property counterEnabled true, if need to show symbol counter
     * @property underLineColor color of the underline
     * @property paddings @see com.icerockdev.mpp.widget.style.view.Padded
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     */
    data class InputStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(size = 12),
        val errorTextStyle: TextStyle = labelTextStyle,
        val counterEnabled: Boolean = false,
        val underLineColor: Int = 0xFFFFFFFF.toInt(),
        override val paddings: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Padded, Margined
}
