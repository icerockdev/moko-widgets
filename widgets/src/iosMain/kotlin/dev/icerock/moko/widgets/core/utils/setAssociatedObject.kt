/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

fun setAssociatedObject(obj: Any?, value: Any?) {
    dev.icerock.moko.widgets.objc.setAssociatedObject(obj, value)
}

fun getAssociatedObject(obj: Any?): Any? {
    return dev.icerock.moko.widgets.objc.getAssociatedObject(obj)
}

fun cgColors(uiColors: List<*>?): List<*>? {
    return dev.icerock.moko.widgets.objc.cgColors(uiColors)
}
