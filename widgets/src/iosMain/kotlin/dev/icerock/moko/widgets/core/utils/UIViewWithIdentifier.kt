/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIAccessibilityIdentificationProtocol
import platform.UIKit.UIView

class UIViewWithIdentifier : UIView(frame = CGRectZero.readValue()),
    UIAccessibilityIdentificationProtocol {
    private var _accessibilityIdentifier: String? = null

    override fun accessibilityIdentifier(): String? {
        return _accessibilityIdentifier
    }

    override fun setAccessibilityIdentifier(accessibilityIdentifier: String?) {
        _accessibilityIdentifier = accessibilityIdentifier
    }
}

internal fun <T> T.identifier(): String where T : Widget<out WidgetSize>, T : OptionalId<*> {
    val i = id
    return if (i != null) i::class.toString()
    else this.toString()
}
