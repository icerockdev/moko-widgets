/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import androidx.core.view.ViewCompat
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.WidgetSize

val <T : Widget<out WidgetSize>> Theme.Id<T>.androidId: Int
    get() {
        // #61 here should be constant and unique id for every Id object. Or android SaveInstanceState will not work :(
        return ViewCompat.generateViewId() // this::class.java.hashCode()
    }
