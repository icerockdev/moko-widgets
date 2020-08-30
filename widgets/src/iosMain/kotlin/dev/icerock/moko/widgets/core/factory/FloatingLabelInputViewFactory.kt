/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.utils.bind
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.FocusableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.setHandler
import dev.icerock.moko.widgets.core.widget.InputWidget
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

actual class FloatingLabelInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    override val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextStyle: TextStyle<Color>?,
    private val errorTextStyle: TextStyle<Color>?,
    private val underLineColor: FocusableState<Color>?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : BaseInputViewFactory<FloatingLabelInputViewFactory.InputWidgetView>(),
    ViewFactory<InputWidget<out WidgetSize>> {

    override fun createTextField(widget: InputWidget<out WidgetSize>): Pair<InputWidgetView, UITextField> {
        val paddingEdges = padding.run {
            Edges(
                top = this?.top?.toDouble() ?: 0.0,
                leading = this?.start?.toDouble() ?: 0.0,
                bottom = this?.bottom?.toDouble() ?: 0.0,
                trailing = this?.end?.toDouble() ?: 0.0
            )
        }

        val inputView = InputWidgetView(paddingEdges).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(this@FloatingLabelInputViewFactory.background)

            applyTextStyleIfNeeded(textStyle)
            applyErrorStyleIfNeeded(errorTextStyle)
            applyLabelStyleIfNeeded(labelTextStyle)
            widget.inputType?.applyTo(this.textField)

            underLineColor?.also {
                deselectedColor = it.unfocused.toUIColor()
                selectedColor = it.focused.toUIColor()
            }

            onFocusLost = {
                widget.field.validate()
            }

            if (textHorizontalAlignment != null) {
                horizontalAlignment = textHorizontalAlignment
            }
        }

        return inputView to inputView.textField
    }

    override fun bindLabel(label: LiveData<StringDesc>, rootView: InputWidgetView, textField: UITextField) {
        label.bind(rootView) { placeholder = it.localized() }
    }

    override fun bindFieldToTextField(
        field: FormField<String, StringDesc>,
        rootView: InputWidgetView,
        textField: UITextField
    ) {
        super.bindFieldToTextField(field, rootView, textField)
        field.data.bind(rootView) {
            if (!textField.isEditing()) {
                rootView.layoutPlaceholder()
            }
        }
        field.error.bind(rootView) { error = it?.localized() }
    }

    class InputWidgetView(
        private val padding: Edges<CGFloat>
    ) : UIView(frame = CGRectZero.readValue()) {

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
                if (!textField.isFirstResponder) {
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
                }
            }

        var error: String?
            get() = errorLabel.text
            set(value) {
                errorLabel.text = value
            }
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
        val textField: UITextField

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
                    this@InputWidgetView,
                    NSSelectorFromString("textFieldBeginEditing"),
                    UIControlEventEditingDidBegin
                )
                addTarget(
                    this@InputWidgetView,
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

        internal fun layoutPlaceholder(
            isPlaceholderInTopState: Boolean = text.isNullOrEmpty().not()
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

        fun applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
            textField.applyTextStyleIfNeeded(textStyle)
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
