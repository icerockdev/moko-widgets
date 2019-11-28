/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

inline class UnitItemRoot constructor(private val wrapper: Wrapper) {
    constructor(
        widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.Exact>>
    ) : this(wrapper = Wrapper(widget))

    constructor(
        widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>
    ) : this(wrapper = Wrapper(widget))

    constructor(widget: Widget<WidgetSize.AspectByWidth<SizeSpec.AsParent>>) : this(
        wrapper = Wrapper(widget)
    )

    val widget: Widget<out WidgetSize> get() = wrapper.widget
}

inline class Wrapper(val widget: Widget<out WidgetSize>)