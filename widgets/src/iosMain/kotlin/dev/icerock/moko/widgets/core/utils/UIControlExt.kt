/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.objc.KeyValueObserverProtocol
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSSelectorFromString
import platform.Foundation.addObserver
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIView
import platform.darwin.NSObject
import kotlin.native.ref.WeakReference

fun <V : UIControl> V.setEventHandler(controlEvent: UIControlEvents, action: (V) -> Unit) {
    val target: LambdaRefTarget<V> = LambdaRefTarget(ref = this) {
        action(this)
    }

    setAssociatedObject(
        obj = this,
        key = "event-$controlEvent",
        target = target
    )

    this.addTarget(
        target = target,
        action = NSSelectorFromString("action"),
        forControlEvents = controlEvent
    )
}

fun UIGestureRecognizer.setHandler(action: () -> Unit) {
    val target = LambdaTarget(action)

    addTarget(
        target = target,
        action = NSSelectorFromString("action")
    )

    setAssociatedObject(
        obj = this,
        key = "gestureAction",
        target = target
    )
}

fun <V : UIView, CTX> V.observeKeyChanges(
    keyPath: String,
    context: CTX,
    action: (V, CTX) -> Unit
) {
    val target: ObserverObject<V> = ObserverObject(this) {
        action(this, context)
    }

    setAssociatedObject(
        obj = this,
        key = "observeKeyChanges-$keyPath",
        target = target
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

@ExportObjCClass
internal class LambdaRefTarget<T : Any>(
    ref: T,
    val lambda: T.() -> Unit
) : NSObject() {
    private val ref: WeakReference<T> = WeakReference(ref)

    @ObjCAction
    fun action() {
        val strongRef: T = ref.get() ?: return
        lambda(strongRef)
    }
}

@ExportObjCClass
internal class LambdaTarget(
    val lambda: () -> Unit
) : NSObject() {
    @ObjCAction
    fun action() {
        lambda()
    }
}
