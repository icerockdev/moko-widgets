/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.NSLayoutDimension
import platform.UIKit.UIView
import platform.UIKit.heightAnchor
import platform.UIKit.superview
import platform.UIKit.widthAnchor

fun <T : UIView> T.applySize(size: WidgetSize): T {
    println("apply $size to $this")

    fun applyToDimension(myAnchor: NSLayoutDimension, parentAnchor: NSLayoutDimension?, constSize: Int) {
        when (constSize) {
            SizeSpec.AS_PARENT -> {
                //myAnchor.constraintEqualToAnchor(parentAnchor).active = true
            }
            SizeSpec.WRAP_CONTENT -> {
                // nothing (intristic size by default)
            }
            else -> myAnchor.constraintEqualToConstant(constSize.toDouble()).active = true
        }
    }

    when (size) {
        is WidgetSize.Const -> {
            applyToDimension(widthAnchor, superview?.widthAnchor, size.width)
            applyToDimension(heightAnchor, superview?.heightAnchor, size.height)
        }
        is WidgetSize.AspectByWidth -> {
            applyToDimension(widthAnchor, superview?.widthAnchor, size.width)
            heightAnchor.constraintEqualToAnchor(widthAnchor, 1 / size.aspectRatio.toDouble()).active = true
        }
        is WidgetSize.AspectByHeight -> {
            applyToDimension(heightAnchor, superview?.heightAnchor, size.height)
            widthAnchor.constraintEqualToAnchor(heightAnchor, size.aspectRatio.toDouble()).active = true
        }
    }

    return this
}
