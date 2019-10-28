package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget>

class SingleChoiceWidget(
    private val factory: VFC<SingleChoiceWidget> = singleChoiceWidgetViewFactory,
    val label: LiveData<StringDesc>,
    val cancelLabel: LiveData<StringDesc>,
    val values: LiveData<List<StringDesc>>,
    val field: FormField<Int, StringDesc>,
    val style: SingleChoiceWidgetStyle = SingleChoiceWidgetStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property dropDownBackground widget's dropdown view background, might be null if not required
     */
    data class SingleChoiceWidgetStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val dropDownTextColor: Int = 0xFF000000.toInt(),
        val underlineColor: Int = 0xFF000000.toInt(),
        override val margins: MarginValues = MarginValues(),
        val dropDownBackground: Background? = null
    ) : Margined
}
