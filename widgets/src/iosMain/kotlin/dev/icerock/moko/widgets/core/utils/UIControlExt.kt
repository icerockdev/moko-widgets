/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.objc.KeyValueObserverProtocol
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSSelectorFromString
import platform.Foundation.addObserver
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_setAssociatedObject
import kotlin.native.ref.WeakReference

fun <V: UIControl> V.setEventHandler(controlEvent: UIControlEvents, action: (V) -> Unit) {
    val weakReference: WeakReference<V> = WeakReference(this)
    val target = LambdaTarget {
        val strongRef: V = weakReference.get() ?: return@LambdaTarget

        action(strongRef)
    }

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

fun <V : UIView, CTX> V.onBoundsChanged(
    context: CTX,
    action: (V, CTX) -> Unit
) {
    val ref: WeakReference<V> = WeakReference(this)

    val target = ObserverObject {
        val strongRef: V = ref.get() ?: return@ObserverObject

        action(strongRef, context)
    }

    objc_setAssociatedObject(
        `object` = this,
        key = "onBoundsChanged".cstr,
        value = target,
        policy = OBJC_ASSOCIATION_RETAIN
    )

    this.addObserver(
        observer = target,
        forKeyPath = "bounds",
        options = NSKeyValueObservingOptionNew,
        context = null
    )
}

private class ObserverObject(
    private val lambda: () -> Unit
) : NSObject(), KeyValueObserverProtocol {

    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        lambda()
    }
}

class LambdaTarget(val lambda: () -> Unit) : NSObject() {

    @ObjCAction
    fun action() {
        lambda()
    }
}
