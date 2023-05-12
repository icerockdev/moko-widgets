/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.objc.KeyValueObserverProtocol
import dev.icerock.moko.widgets.objc.setAssociatedObjectWithKey
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSString
import platform.Foundation.NSValue
import platform.Foundation.UTF8String
import platform.Foundation.addObserver
import platform.Foundation.valueWithPointer
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_setAssociatedObject
import kotlin.native.ref.WeakReference

fun <V : UIControl> V.setEventHandler(controlEvent: UIControlEvents, action: (V) -> Unit) {
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

private val keys = mutableMapOf<String, NSValue>()

fun <V : UIView, CTX> V.observeKeyChanges(
    keyPath: String,
    context: CTX,
    action: (V, CTX) -> Unit
) {
    val target: ObserverObject<V> = ObserverObject(this) {
        action(this, context)
    }

    val keyString = "observeKeyChanges-$keyPath"
    val key: NSValue = keys[keyString] ?: let {
        NSValue.valueWithPointer((keyString as NSString).UTF8String)
    }.also {
        keys[keyString] = it
    }

    setAssociatedObjectWithKey(
        `object` = this,
        key = key,
        value = target
    )

    this.addObserver(
        observer = target,
        forKeyPath = keyPath,
        options = NSKeyValueObservingOptionNew,
        context = null
    )
}

@ExportObjCClass
private class ObserverObject<T : Any>(
    ref: T,
    private val lambda: T.() -> Unit
) : NSObject(), KeyValueObserverProtocol {
    private val ref: WeakReference<T> = WeakReference(ref)

    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        val strongRef: T = ref.get() ?: return
        lambda.invoke(strongRef)
    }
}

class LambdaTarget(val lambda: () -> Unit) : NSObject() {

    @ObjCAction
    fun action() {
        lambda()
    }
}
