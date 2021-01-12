/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.core.style.background.Corner
import platform.QuartzCore.CACornerMask
import platform.QuartzCore.kCALayerMaxXMaxYCorner
import platform.QuartzCore.kCALayerMaxXMinYCorner
import platform.QuartzCore.kCALayerMinXMaxYCorner
import platform.QuartzCore.kCALayerMinXMinYCorner

fun List<Corner>.toCACornerMask(): CACornerMask {
    var corners: CACornerMask = 0u
    this.forEach {
        when (it) {
            Corner.TOP_LEFT -> corners = corners + kCALayerMinXMinYCorner
            Corner.TOP_RIGHT -> corners = corners + kCALayerMaxXMinYCorner
            Corner.BOTTOM_LEFT -> corners = corners + kCALayerMinXMaxYCorner
            Corner.BOTTOM_RIGHT -> corners = corners + kCALayerMaxXMaxYCorner
        }
    }
    return corners
}