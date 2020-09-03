/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.view

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.utils.Edges
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIColor
import platform.UIKit.UITextField
import platform.UIKit.UIView

expect class FloatingLabelInputView(padding: Edges<CGFloat>) : UIView {
    var placeholder: String?
    var text: String?
    var horizontalAlignment: TextHorizontalAlignment
    var error: String?
    var onFocusLost: (() -> Unit)?
    var selectedColor: UIColor
    var deselectedColor: UIColor
    val textField: UITextField

    fun applyTextStyleIfNeeded(textStyle: TextStyle<Color>?)
    fun applyLabelStyleIfNeeded(textStyle: TextStyle<Color>?)
    fun applyErrorStyleIfNeeded(textStyle: TextStyle<Color>?)

    internal fun layoutPlaceholder()
    internal fun layoutPlaceholder(isPlaceholderInTopState: Boolean)
}
