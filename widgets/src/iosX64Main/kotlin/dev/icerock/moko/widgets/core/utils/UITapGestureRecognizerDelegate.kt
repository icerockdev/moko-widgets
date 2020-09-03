/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.UIKit.UITouch
import platform.darwin.NSObject

actual class UITapGestureRecognizerDelegate actual constructor(
    private val shouldReceiveTouch: (gestureRecognizer: UIGestureRecognizer, touch: UITouch) -> Boolean
) : NSObject(), UIGestureRecognizerDelegateProtocol {
    override fun gestureRecognizer(
        gestureRecognizer: UIGestureRecognizer,
        shouldReceiveTouch: UITouch
    ): Boolean {
        return this.shouldReceiveTouch(gestureRecognizer, shouldReceiveTouch)
    }
}
