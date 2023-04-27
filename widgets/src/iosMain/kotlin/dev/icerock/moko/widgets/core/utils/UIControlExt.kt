/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSRunLoop
import platform.Foundation.NSRunLoopCommonModes
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CADisplayLink
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_setAssociatedObject
import kotlin.native.ref.WeakReference

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

fun UIGestureRecognizer.setHandler(action: () -> Unit) {
    val target = LambdaTarget(action)

    addTarget(
        target = target,
        action = NSSelectorFromString("action")
    )

    objc_setAssociatedObject(
        `object` = this,
        key = "gestureAction".cstr,
        value = target,
        policy = OBJC_ASSOCIATION_RETAIN
    )
}

fun <T : Any, CTX> T.displayLink(
    context: CTX,
    objectForSkipCheck: (T) -> Any,
    action: (T, CTX) -> Unit
) {
    val ref: WeakReference<T> = WeakReference(this)

    var displayLink: CADisplayLink? = null

    var oldState: Any = objectForSkipCheck(this)
    val target = LambdaTarget {
        val strongRef: T? = ref.get()
        if (strongRef == null) {
            displayLink?.removeFromRunLoop(NSRunLoop.currentRunLoop, NSRunLoopCommonModes)
            displayLink = null
            return@LambdaTarget
        }

        val newState: Any = objectForSkipCheck(strongRef)
        if (newState == oldState) return@LambdaTarget

        action(strongRef, context)
    }

    CADisplayLink.displayLinkWithTarget(
        target = target,
        selector = NSSelectorFromString("displayLink:")
    ).apply {
        frameInterval = 1
        addToRunLoop(NSRunLoop.currentRunLoop, NSRunLoopCommonModes)
    }
}

class LambdaTarget(val lambda: () -> Unit) : NSObject() {

    @ObjCAction
    fun action() {
        lambda()
    }

    @ObjCAction
    @Suppress("UnusedPrivateMember")
    fun displayLink(link: CADisplayLink) {
        lambda()
    }
}
