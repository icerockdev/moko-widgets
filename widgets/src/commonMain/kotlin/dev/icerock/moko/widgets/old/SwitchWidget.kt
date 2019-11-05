/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.style.ColorStyle
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var switchWidgetViewFactory: VFC<SwitchWidget>

class SwitchWidget()
//    private val factory: VFC<SwitchWidget> = switchWidgetViewFactory,
//    val style: SwitchStyle,
//    val label: LiveData<StringDesc>,
//    val state: MutableLiveData<Boolean>
//) : Widget() {
//
//    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
//        factory(viewFactoryContext, this)
//
//    /**
//     * @property size desired size of widget
//     * @property labelTextStyle floating label text style
//     * @property padding @see com.icerockdev.mpp.widget.style.view.Padded
//     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
//     * @property background widget's background, might be null if not required
//     * @property switchColor switch background, might be null if default
//     */
//
//    //TODO: Add icon
//
//    data class SwitchStyle(
//        val size: WidgetSize = WidgetSize(),
//        val labelTextStyle: TextStyle = TextStyle(size = 14),
//        override val padding: PaddingValues = PaddingValues(),
//        override val margins: MarginValues = MarginValues(),
//        val background: Background? = null,
//        val switchColor: ColorStyle? = null
//    ) : Padded, Margined
//}
