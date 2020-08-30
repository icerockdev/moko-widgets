/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.units

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import kotlin.jvm.JvmName

@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS", "EXPERIMENTAL_FEATURE_WARNING")
inline class UnitItemRoot private constructor(private val wrapper: Wrapper) {

    companion object {
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("fromWidgetConstExactHeight")
        fun from(widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.Exact>>): UnitItemRoot {
            Result
            return UnitItemRoot(Wrapper(widget))
        }

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("fromWidgetConstContentHeight")
        fun from(widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>): UnitItemRoot {
            return UnitItemRoot(Wrapper(widget))
        }

        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("fromWidgetAspectWidth")
        fun from(widget: Widget<WidgetSize.AspectByWidth<SizeSpec.AsParent>>): UnitItemRoot {
            return UnitItemRoot(Wrapper(widget))
        }
    }

    val widget: Widget<out WidgetSize> get() = wrapper.widget
}

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class Wrapper(val widget: Widget<out WidgetSize>)
