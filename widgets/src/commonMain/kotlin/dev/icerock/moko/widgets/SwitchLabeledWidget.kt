/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

fun Theme.switchLabeled(
    switchId: SwitchWidget.Id,
    switchState: MutableLiveData<Boolean>,
    text: LiveData<StringDesc>,
    linearId: LinearWidget.Id? = null,
    textId: TextWidget.Id? = null
): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>> {
    return linear(
        size = WidgetSize.WidthAsParentHeightWrapContent,
        id = linearId
    ) {
        +switch(
            size = WidgetSize.WrapContent,
            id = switchId,
            state = switchState
        )
        +text(
            size = WidgetSize.WidthAsParentHeightWrapContent,
            id = textId,
            text = text
        )
    }
}
