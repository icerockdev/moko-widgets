/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.StateBackground
import kotlinx.cinterop.*
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSCoder
import platform.Foundation.NSDefaultRunLoopMode
import platform.Foundation.NSRunLoop
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CADisplayLink
import platform.QuartzCore.CALayer
import platform.UIKit.*
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

fun NSObject.displayLink(action: () -> Unit): CADisplayLink {
    val target = LambdaTarget(action)

    return CADisplayLink.displayLinkWithTarget(
        target = target,
        selector = NSSelectorFromString("displayLink:")
    ).apply {
        frameInterval = 1
        addToRunLoop(NSRunLoop.currentRunLoop, NSDefaultRunLoopMode)
    }
}

class LambdaTarget(val lambda: () -> Unit) : NSObject() {

    @ObjCAction
    fun action() {
        lambda()
    }

    @ObjCAction
    fun displayLink(link: CADisplayLink) {
        lambda()
    }
}

class UIStyledButton: UIButton(frame = CGRectZero.readValue()) {
    private var normalBg: CALayer? = null
    private var disabledBg: CALayer? = null
    private var pressedBg: CALayer? = null
    
    fun applyStateBackground(background: StateBackground?) {
        if (background == null) return
        adjustsImageWhenDisabled = false
        adjustsImageWhenHighlighted = false
        normalBg = background.normal.caLayer().also {
            layer.addSublayer(it)
        }
        disabledBg = background.disabled.caLayer().also {
            layer.addSublayer(it)
        }
        pressedBg = background.pressed.caLayer().also {
            layer.addSublayer(it)
        }

        updateLayers()
    }

    override fun layoutSublayersOfLayer(layer: CALayer) {
        super.layoutSublayersOfLayer(layer)
        val (width, height) = layer.bounds.useContents { size.width to size.height }
        normalBg?.frame = CGRectMake(0.0, 0.0, width, height)
        disabledBg?.frame = CGRectMake(0.0, 0.0, width, height)
        pressedBg?.frame = CGRectMake(0.0, 0.0, width, height)
    }

    private fun updateLayers() {

        if (!isEnabled()) {
            disabledBg?.opacity = 1.0f
            normalBg?.opacity = 0f
            pressedBg?.opacity = 0f
            return
        }

        if (isHighlighted()) {
            pressedBg?.opacity = 1.0f
            normalBg?.opacity = 0f
        } else {
            normalBg?.opacity = 1.0f
            pressedBg?.opacity = 0f
        }
        disabledBg?.opacity = 0f

    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateLayers()
    }

    override fun setHighlighted(highlighted: Boolean) {
        super.setHighlighted(highlighted)
        updateLayers()
    }
}
