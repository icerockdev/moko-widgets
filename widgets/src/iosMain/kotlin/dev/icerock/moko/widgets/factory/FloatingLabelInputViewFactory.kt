/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyInputType
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.*
import dev.icerock.moko.widgets.utils.*
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.QuartzCore.CALayer
import platform.QuartzCore.CAShapeLayer
import platform.QuartzCore.CATextLayer
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCAAlignmentLeft
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIAccessibilityIdentificationProtocol
import platform.UIKit.UIBezierPath
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UIScreen
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.systemFontSize
import platform.UIKit.systemGrayColor
import platform.UIKit.systemRedColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class FloatingLabelInputViewFactory actual constructor(
    private val background: Background?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle?,
    private val labelTextStyle: TextStyle?,
    private val errorTextStyle: TextStyle?,
    private val underLineColor: Color?,
    private val underLineFocusedColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val paddingEdges = padding.run {
            Edges<CGFloat>(
                top = this?.top?.toDouble() ?: 0.0,
                leading = this?.start?.toDouble() ?: 0.0,
                bottom = this?.bottom?.toDouble() ?: 0.0,
                trailing = this?.end?.toDouble() ?: 0.0
            )
        }

        val textField = InputWidgetView(paddingEdges).apply {
            translatesAutoresizingMaskIntoConstraints = false
            accessibilityIdentifier = widget.identifier()
            applyBackgroundIfNeeded(background)

            applyTextStyleIfNeeded(textStyle)
            applyErrorStyleIfNeeded(errorTextStyle)
            applyLabelStyleIfNeeded(labelTextStyle)
            applyInputTypeIfNeeded(inputType = widget.inputType)
            underLineColor?.let {
                deselectedColor = it.toUIColor()
            }
            underLineFocusedColor?.let {
                selectedColor = it.toUIColor()
            }

            textChanged = { newValue ->
                val currentValue = widget.field.data.value

                if (currentValue != newValue) {
                    widget.field.data.value = newValue
                }
            }

            onFocusLost = {
                widget.field.validate()
            }

            if (textHorizontalAlignment != null) {
                horizontalAlignment = textHorizontalAlignment
            }
        }

        widget.enabled?.bind { textField.enabled = it }
        widget.label.bind { textField.placeholder = it.localized() }
        widget.field.data.bind { textField.text = it }
        widget.field.error.bind { textField.error = it?.localized() }

        return ViewBundle(
            view = textField,
            size = size,
            margins = margins
        )
    }

    class InputWidgetView(
        private val padding: Edges<CGFloat>
    ) : UIView(frame = CGRectZero.readValue()), UITextFieldDelegateProtocol,
        UIAccessibilityIdentificationProtocol {

        var placeholder: String?
            get() = placeholderTextLayer.string as? String
            set(value) {
                placeholderTextLayer.string = value
                layoutPlaceholder()
            }
        var text: String?
            get() = textField.text
            set(value) {
                textField.text = value
                if (!isFirstResponder) {
                    layoutPlaceholder()
                }
            }

        var horizontalAlignment: TextHorizontalAlignment = TextHorizontalAlignment.CENTER
            set(value) {
                field = value
                when (value) {
                    TextHorizontalAlignment.LEFT -> textField.textAlignment = NSTextAlignmentLeft
                    TextHorizontalAlignment.CENTER -> textField.textAlignment = NSTextAlignmentCenter
                    TextHorizontalAlignment.RIGHT -> textField.textAlignment = NSTextAlignmentRight
                    null -> {
                    }
                }
            }

        var error: String?
            get() = errorLabel.text
            set(value) {
                errorLabel.text = value
            }
        var enabled: Boolean
            get() = textField.enabled
            set(value) {
                textField.enabled = value
            }
        var textChanged: ((text: String) -> Unit)? = null
        var onFocusLost: (() -> Unit)? = null

        var selectedColor: UIColor = UIColor.blackColor
            set(value) {
                field = value
                underlineLayer.fillColor = (if (textField.isFocused()) value else deselectedColor).CGColor
            }
        var deselectedColor: UIColor = UIColor.systemGrayColor
            set(value) {
                field = value
                underlineLayer.fillColor = (if (textField.isFocused()) selectedColor else value).CGColor
            }
        private val textField: UITextField

        private val errorLabel: UILabel
        private val underlineLayer: CAShapeLayer

        private val placeholderTextLayer: CATextLayer
        private var placeholderAnimationDuration: CGFloat = 0.25

        var placeholderTextSize: CGFloat = UIFont.systemFontSize
            set(value) {
                field = value
                placeholderTextLayer.fontSize = value
            }

        private var _accessibilityIdentifier: String? = null

        private var inputFormatter: DefaultTextFormatter? = null

        init {
            translatesAutoresizingMaskIntoConstraints = false

            val container = this

            textField = UITextField(frame = CGRectZero.readValue()).apply {
                translatesAutoresizingMaskIntoConstraints = false

                container.addSubview(this)

                topAnchor.constraintEqualToAnchor(container.topAnchor, constant = 18.0 + padding.top).active =
                    true
                leadingAnchor.constraintEqualToAnchor(container.leadingAnchor, constant = padding.leading).active = true
                trailingAnchor.constraintEqualToAnchor(container.trailingAnchor, constant = -padding.trailing).active =
                    true

                delegate = this@InputWidgetView

                addTarget(
                    this@InputWidgetView,
                    NSSelectorFromString("textDidChanged"),
                    UIControlEventEditingChanged
                )
            }
            errorLabel = UILabel(frame = CGRectZero.readValue()).apply {
                translatesAutoresizingMaskIntoConstraints = false

                container.addSubview(this)

                topAnchor.constraintEqualToAnchor(textField.bottomAnchor, constant = 4.0).active =
                    true
                leadingAnchor.constraintEqualToAnchor(container.leadingAnchor, constant = padding.leading).active = true
                trailingAnchor.constraintEqualToAnchor(container.trailingAnchor, constant = -padding.trailing).active =
                    true
                bottomAnchor.constraintEqualToAnchor(container.bottomAnchor, constant = -padding.bottom).active = true

                font = UIFont.systemFontOfSize(11.0)
                textColor = UIColor.systemRedColor
            }

            underlineLayer = CAShapeLayer().apply {
                fillColor = deselectedColor.CGColor
                textField.layer.insertSublayer(this, atIndex = 0)
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
        }

        override fun layoutSublayersOfLayer(layer: CALayer) {
            super.layoutSublayersOfLayer(layer)

            underlineLayer.frame = textField.layer.bounds
            underlineLayer.path = underlineLayer.bounds.let {
                val (width, height) = it.useContents { this.size.width to this.size.height }
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

        private fun layoutPlaceholder(isPlaceholderInTopState: Boolean = text.isNullOrEmpty().not()) {
            placeholderTextLayer.fontSize = if (isPlaceholderInTopState) {
                placeholderTextSize - 2
            } else {
                placeholderTextSize
            }
            val size = placeholderTextLayer.preferredFrameSize()
            val (width, height) = size.useContents { this.width to this.height }
            val textFieldHeight = textField.bounds.useContents { this.size.height }
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
        }

        override fun textFieldShouldBeginEditing(textField: UITextField): Boolean {
            animate(
                duration = placeholderAnimationDuration,
                underlineColor = selectedColor,
                isPlaceholderInTopState = true
            )
            return true
        }

        override fun textFieldDidEndEditing(textField: UITextField) {
            animate(
                duration = placeholderAnimationDuration,
                underlineColor = deselectedColor,
                isPlaceholderInTopState = textField.text.isNullOrEmpty().not()
            )
            onFocusLost?.invoke()
        }

        override fun textField(
            textField: UITextField,
            shouldChangeCharactersInRange: CValue<NSRange>,
            replacementString: String
        ): Boolean {
            if (inputFormatter != null) {
                val nsString = NSString.create(string = textField.text ?: "")
                val newText = nsString.stringByReplacingCharactersInRange(range = shouldChangeCharactersInRange, withString = replacementString)
                val unformattedText = inputFormatter?.unformat(newText) ?: ""
                println(replacementString)
                println(newText)

                textField.text = inputFormatter?.format(unformattedText)
                textDidChanged()
                return false
            } else {
                return true
            }
        }
        override fun accessibilityIdentifier(): String? {
            return _accessibilityIdentifier
        }

        override fun setAccessibilityIdentifier(accessibilityIdentifier: String?) {
            _accessibilityIdentifier = accessibilityIdentifier
        }

        @ObjCAction
        private fun textDidChanged() {
            textChanged?.invoke(textField.text.orEmpty())
        }

        fun applyTextStyleIfNeeded(textStyle: TextStyle?) {
            textField.applyTextStyleIfNeeded(textStyle)
        }

        fun applyInputTypeIfNeeded(inputType: InputType?) {
            if (inputType != null) {
                textField.applyInputType(inputType)
                if (inputType.mask != null) {
                    val newMask = inputType.mask
                        .replace("0", "#")
                        .replace("[","")
                        .replace("]", "")
                    inputFormatter = DefaultTextFormatter(
                        textPattern = newMask,
                        patternSymbol = '#'
                    )
                }
            }
        }

        fun applyLabelStyleIfNeeded(textStyle: TextStyle?) {
            placeholderTextLayer.applyTextStyleIfNeeded(textStyle)
        }

        fun applyErrorStyleIfNeeded(textStyle: TextStyle?) {
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

}
