/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.AnyWidget

expect abstract class WidgetScreen<Arg : Args>() : Screen<Arg> {
    abstract fun createContentWidget(): AnyWidget
}
