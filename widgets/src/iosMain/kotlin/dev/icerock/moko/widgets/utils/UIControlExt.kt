/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_setAssociatedObject

fun UIControl.setEventHandler(controlEvent: UIControlEvents, action: () -> Unit) {
    val target = LambdaTarget(action)

    addTarget(
        target = target,
        action = NSSelectorFromString("action"),
        forControlEvents = controlEvent
    )

    objc_setAssociatedObject(
        `object` = this,
        key = "event$controlEvent".cstr,
        value = target,
        policy = OBJC_ASSOCIATION_RETAIN
    )
}

class LambdaTarget(val lambda: () -> Unit) : NSObject() {

    @ObjCAction
    fun action() {
        lambda()
    }
}
