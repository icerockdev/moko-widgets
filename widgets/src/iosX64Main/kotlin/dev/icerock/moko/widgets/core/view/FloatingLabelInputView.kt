/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.view

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.setHandler
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CALayer
import platform.QuartzCore.CAShapeLayer
import platform.QuartzCore.CATextLayer
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCAAlignmentLeft
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIBezierPath
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventEditingDidBegin
import platform.UIKit.UIControlEventEditingDidEnd
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIScreen
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UITextField
import platform.UIKit.UIView
import platform.UIKit.addGestureRecognizer
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.setNeedsLayout
import platform.UIKit.systemFontSize
import platform.UIKit.systemGrayColor
import platform.UIKit.systemRedColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class FloatingLabelInputView actual constructor(
    private val padding: Edges<CGFloat>
) : UIView(frame = CGRectZero.readValue()) {

    actual var placeholder: String?
        get() = placeholderTextLayer.string as? String
        set(value) {
            placeholderTextLayer.string = value
            layoutPlaceholder()
        }
    actual var text: String?
        get() = textField.text
        set(value) {
            textField.text = value
            if (!textField.isFirstResponder) {
                layoutPlaceholder()
            }
        }

    actual var horizontalAlignment: TextHorizontalAlignment = TextHorizontalAlignment.CENTER
        set(value) {
            field = value
            when (value) {
                TextHorizontalAlignment.LEFT -> textField.textAlignment = NSTextAlignmentLeft
                TextHorizontalAlignment.CENTER -> textField.textAlignment = NSTextAlignmentCenter
                TextHorizontalAlignment.RIGHT -> textField.textAlignment = NSTextAlignmentRight
            }
        }

    actual var error: String?
        get() = errorLabel.text
        set(value) {
            errorLabel.text = value
        }
    actual var onFocusLost: (() -> Unit)? = null

    actual var selectedColor: UIColor = UIColor.blackColor
        set(value) {
            field = value
            underlineLayer.fillColor =
                (if (textField.isFocused()) value else deselectedColor).CGColor
        }
    actual var deselectedColor: UIColor = UIColor.systemGrayColor
        set(value) {
            field = value
            underlineLayer.fillColor =
                (if (textField.isFocused()) selectedColor else value).CGColor
        }
    actual val textField: UITextField

    private val errorLabel: UILabel
    private val underlineLayer: CAShapeLayer

    private val placeholderTextLayer: CATextLayer
    private var placeholderAnimationDuration: CGFloat = 0.25

    var placeholderTextSize: CGFloat = UIFont.systemFontSize
        set(value) {
            field = value
            placeholderTextLayer.fontSize = value
        }

    init {
        translatesAutoresizingMaskIntoConstraints = false

        val container = this

        textField = UITextField(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            container.addSubview(this)

            topAnchor.constraintEqualToAnchor(
                container.topAnchor,
                constant = 18.0 + padding.top
            ).active = true
            leadingAnchor.constraintEqualToAnchor(
                container.leadingAnchor,
                constant = padding.leading
            ).active = true
            trailingAnchor.constraintEqualToAnchor(
                container.trailingAnchor,
                constant = -padding.trailing
            ).active = true

            addTarget(
                this@FloatingLabelInputView,
                NSSelectorFromString("textFieldBeginEditing"),
                UIControlEventEditingDidBegin
            )
            addTarget(
                this@FloatingLabelInputView,
                NSSelectorFromString("textFieldEndEditing"),
                UIControlEventEditingDidEnd
            )
        }
        errorLabel = UILabel(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            container.addSubview(this)

            topAnchor.constraintEqualToAnchor(
                textField.bottomAnchor,
                constant = 4.0
            ).active = true
            leadingAnchor.constraintEqualToAnchor(
                container.leadingAnchor,
                constant = padding.leading
            ).active = true
            trailingAnchor.constraintEqualToAnchor(
                container.trailingAnchor,
                constant = -padding.trailing
            ).active = true
            bottomAnchor.constraintEqualToAnchor(
                container.bottomAnchor,
                constant = -padding.bottom
            ).active = true

            font = UIFont.systemFontOfSize(11.0)
            textColor = UIColor.systemRedColor
        }

        underlineLayer = CAShapeLayer().apply {
            fillColor = deselectedColor.CGColor
            textField.layer.insertSublayer(this, atIndex = 0U)
        }

        placeholderTextLayer = CATextLayer().apply {
            foregroundColor = UIColor.grayColor.CGColor
            fontSize = placeholderTextSize
            alignmentMode = kCAAlignmentLeft
            contentsScale = UIScreen.mainScreen.scale
            backgroundColor = UIColor.clearColor.CGColor
            anchorPoint = CGPointMake(x = 0.0, y = 0.0)
            textField.layer.addSublayer(this)
        }

        val recognizer = UITapGestureRecognizer().apply {
            setHandler(container::onTap)
        }
        addGestureRecognizer(recognizer)
    }

    private fun onTap() {
        textField.becomeFirstResponder()
    }

    override fun layoutSublayersOfLayer(layer: CALayer) {
        super.layoutSublayersOfLayer(layer)

        underlineLayer.frame = textField.layer.bounds
        underlineLayer.path = underlineLayer.bounds.let { bounds ->
            val (width, height) = bounds.useContents { this.size.width to this.size.height }
            CGRectMake(
                x = 0.0,
                y = height - 1,
                width = width,
                height = 1.0
            ).let {
                UIBezierPath.bezierPathWithRect(it).CGPath
            }
        }

        layoutPlaceholder()
    }

    internal actual fun layoutPlaceholder() {
        layoutPlaceholder(text.isNullOrEmpty().not())
    }

    internal actual fun layoutPlaceholder(
        isPlaceholderInTopState: Boolean
    ) {
        if (placeholder.isNullOrBlank()) {
            return
        }
        placeholderTextLayer.fontSize = if (isPlaceholderInTopState) {
            placeholderTextSize - 2
        } else {
            placeholderTextSize
        }
        val size = placeholderTextLayer.preferredFrameSize()
        val (width, height) = size.useContents { this.width to this.height }
        val textFieldHeight = textField.bounds().useContents { this.size.height }
        if (isPlaceholderInTopState) {
            placeholderTextLayer.frame = CGRectMake(
                x = 0.0,
                y = -textFieldHeight + 4,
                width = width,
                height = height
            )
        } else {
            placeholderTextLayer.frame = CGRectMake(
                x = 0.0,
                y = 0.0,
                width = width,
                height = height
            )
        }
        textField.setNeedsLayout()
    }

    @Suppress("unused")
    @ObjCAction
    private fun textFieldBeginEditing() {
        animate(
            duration = placeholderAnimationDuration,
            underlineColor = selectedColor,
            isPlaceholderInTopState = true
        )
    }

    @Suppress("unused")
    @ObjCAction
    private fun textFieldEndEditing() {
        animate(
            duration = placeholderAnimationDuration,
            underlineColor = deselectedColor,
            isPlaceholderInTopState = textField.text.isNullOrEmpty().not()
        )
        onFocusLost?.invoke()
    }

    actual fun applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
        textField.applyTextStyleIfNeeded(textStyle)
    }

    actual fun applyLabelStyleIfNeeded(textStyle: TextStyle<Color>?) {
        placeholderTextLayer.applyTextStyleIfNeeded(textStyle)
    }

    actual fun applyErrorStyleIfNeeded(textStyle: TextStyle<Color>?) {
        errorLabel.applyTextStyleIfNeeded(textStyle)
    }

    private fun animate(
        duration: CGFloat,
        underlineColor: UIColor,
        isPlaceholderInTopState: Boolean
    ) {
        CATransaction.begin()
        CATransaction.setAnimationDuration(duration)

        underlineLayer.fillColor = underlineColor.CGColor

        layoutPlaceholder(isPlaceholderInTopState = isPlaceholderInTopState)

        CATransaction.commit()
    }
}
