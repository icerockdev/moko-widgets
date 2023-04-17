/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

fun setAssociatedObject(`object`: Any?, value: Any?) {
    dev.icerock.moko.widgets.objc.setAssociatedObject(`object`, value)
}

fun getAssociatedObject(`object`: Any?): Any? {
    return dev.icerock.moko.widgets.objc.getAssociatedObject(`object`)
}

fun cgColors(uiColors: List<*>?): List<*>? {
    return dev.icerock.moko.widgets.objc.cgColors(uiColors)
}
