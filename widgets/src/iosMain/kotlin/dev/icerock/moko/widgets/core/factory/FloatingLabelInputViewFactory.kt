/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyInputTypeIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.input.InputType
import dev.icerock.moko.widgets.core.style.state.FocusableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.DefaultFormatterUITextFieldDelegate
import dev.icerock.moko.widgets.core.utils.DefaultTextFormatter
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.identifier
import dev.icerock.moko.widgets.core.utils.toIosPattern
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.objc.setAssociatedObject
import dev.icerock.moko.widgets.core.utils.setHandler
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSRange
import platform.Foundation.NSSelectorFromString
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
import platform.UIKit.UIReturnKeyType
import platform.UIKit.UIScreen
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.superview
import platform.UIKit.subviews
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.systemFontSize
import platform.UIKit.systemGrayColor
import platform.UIKit.systemRedColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.addGestureRecognizer

actual class FloatingLabelInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextStyle: TextStyle<Color>?,
    private val errorTextStyle: TextStyle<Color>?,
    private val underLineColor: FocusableState<Color>?,
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

        val textField =
            InputWidgetView(
                paddingEdges
            ).apply {
                translatesAutoresizingMaskIntoConstraints = false
                accessibilityIdentifier = widget.identifier()
                applyBackgroundIfNeeded(background)

                applyTextStyleIfNeeded(textStyle)
                applyErrorStyleIfNeeded(errorTextStyle)
                applyLabelStyleIfNeeded(labelTextStyle)
                applyInputTypeIfNeeded(widget.inputType)

                underLineColor?.also {
                    deselectedColor = it.unfocused.toUIColor()
                    selectedColor = it.focused.toUIColor()
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
                    TextHorizontalAlignment.CENTER -> textField.textAlignment =
                        NSTextAlignmentCenter
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
                underlineLayer.fillColor =
                    (if (textField.isFocused()) value else deselectedColor).CGColor
            }
        var deselectedColor: UIColor = UIColor.systemGrayColor
            set(value) {
                field = value
                underlineLayer.fillColor =
                    (if (textField.isFocused()) selectedColor else value).CGColor
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

        private var formatterDelegate: DefaultFormatterUITextFieldDelegate? = null

        init {
            translatesAutoresizingMaskIntoConstraints = false

            val container = this

            textField = UITextField(frame = CGRectZero.readValue()).apply {
                translatesAutoresizingMaskIntoConstraints = false

                container.addSubview(this)

                topAnchor.constraintEqualToAnchor(
                    container.topAnchor,
                    constant = 18.0 + padding.top
                ).active =
                    true
                leadingAnchor.constraintEqualToAnchor(
                    container.leadingAnchor,
                    constant = padding.leading
                ).active = true
                trailingAnchor.constraintEqualToAnchor(
                    container.trailingAnchor,
                    constant = -padding.trailing
                ).active =
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

        override fun becomeFirstResponder(): Boolean {
            return textField.becomeFirstResponder()
        }

        override fun canBecomeFirstResponder(): Boolean {
            return textField.canBecomeFirstResponder()
        }

        private fun onTap() {
            textField.becomeFirstResponder()
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

        private fun layoutPlaceholder(
            isPlaceholderInTopState: Boolean = text.isNullOrEmpty().not()
        ) {
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
            textField.returnKeyType =
                if (nextResponder(textField) == null) {
                    UIReturnKeyType.UIReturnKeyDone
                } else {
                    UIReturnKeyType.UIReturnKeyNext
                }
            animate(
                duration = placeholderAnimationDuration,
                underlineColor = selectedColor,
                isPlaceholderInTopState = true
            )
            return true
        }

        private fun nextResponder(textField: UITextField): UIView? {
            val fields = textField.superview?.superview?.subviews.orEmpty()
                .filter { (it as? InputWidgetView) != null }
            val index = fields.indexOf(this)
            if (index < 0 || index == (fields.count() - 1)) {
                return null
            }
            return fields[index+1] as? UIView
        }

        override fun textFieldShouldReturn(textField: UITextField): Boolean {
            nextResponder(textField)?.becomeFirstResponder()
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
            if (formatterDelegate != null) {
                formatterDelegate?.textField(
                    textField = textField,
                    shouldChangeCharactersInRange = shouldChangeCharactersInRange,
                    replacementString = replacementString
                )
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

        fun applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
            textField.applyTextStyleIfNeeded(textStyle)
        }

        fun applyInputTypeIfNeeded(inputType: InputType?) {
            textField.applyInputTypeIfNeeded(inputType)

            inputType?.mask?.let { mask ->
                val delegate =
                    DefaultFormatterUITextFieldDelegate(
                        inputFormatter = DefaultTextFormatter(
                            mask.toIosPattern(),
                            patternSymbol = '#'
                        )
                    )
                textField.delegate = delegate
                setAssociatedObject(textField, delegate)
            }
        }

        fun applyLabelStyleIfNeeded(textStyle: TextStyle<Color>?) {
            placeholderTextLayer.applyTextStyleIfNeeded(textStyle)
        }

        fun applyErrorStyleIfNeeded(textStyle: TextStyle<Color>?) {
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
