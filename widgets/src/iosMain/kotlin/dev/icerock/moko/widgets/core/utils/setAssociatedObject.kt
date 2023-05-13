/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.objc.setAssociatedObjectWithKey
import platform.Foundation.NSString
import platform.Foundation.NSValue
import platform.Foundation.UTF8String
import platform.Foundation.valueWithPointer

fun setAssociatedObject(obj: Any, value: Any?) {
    dev.icerock.moko.widgets.objc.setAssociatedObject(obj, value)
}

fun getAssociatedObject(obj: Any): Any? {
    return dev.icerock.moko.widgets.objc.getAssociatedObject(obj)
}

private val keys = mutableMapOf<String, NSValue>()

fun setAssociatedObject(
    obj: Any,
    key: String,
    target: Any
) {
    val keyValue: NSValue = keys[key] ?: Unit.let {
        @Suppress("CAST_NEVER_SUCCEEDS")
        NSValue.valueWithPointer((key as NSString).UTF8String)
    }.also {
        keys[key] = it
    }

    setAssociatedObjectWithKey(
        `object` = obj,
        key = keyValue,
        value = target
    )
}

fun cgColors(uiColors: List<*>?): List<*>? {
    return dev.icerock.moko.widgets.objc.cgColors(uiColors)
}
