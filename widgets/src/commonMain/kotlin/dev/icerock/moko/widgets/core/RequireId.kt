/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.style.view.WidgetSize

interface RequireId<T : Theme.Id<out Widget<out WidgetSize>>> : OptionalId<T> {
    override val id: T
}
