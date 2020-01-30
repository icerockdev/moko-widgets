/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.*
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*
import platform.Foundation.*

actual class SystemTextViewFactory actual constructor(
    private val background: Background?,
    private val textStyle: TextStyle?,
    private val textAlignment: TextAlignment?,
    private val margins: MarginValues?,
    private val isHtmlConverted: Boolean
) : ViewFactory<TextWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: TextWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val label = UILabel(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyTextStyleIfNeeded(textStyle)

            numberOfLines = 0

            setContentCompressionResistancePriority(749f, UILayoutConstraintAxisHorizontal)
            setContentCompressionResistancePriority(749f, UILayoutConstraintAxisVertical)

            when (this@SystemTextViewFactory.textAlignment) {
                TextAlignment.LEFT -> textAlignment = NSTextAlignmentLeft
                TextAlignment.CENTER -> textAlignment = NSTextAlignmentCenter
                TextAlignment.RIGHT -> textAlignment = NSTextAlignmentRight
                null -> {
                }
            }
        }
        if (isHtmlConverted) {
            widget.text.bind { label.attributedText = it.localized().stringFromHtml(
                textStyle = textStyle
            ) }
            label.userInteractionEnabled = true

            val recognizer = UITapGestureRecognizer().apply {
                setHandler { label.openUrl() }
            }
            label.addGestureRecognizer(recognizer)
        } else {
            widget.text.bind { label.text = it.localized() }
        }

        val wrapper = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            backgroundColor = UIColor.clearColor
            applyBackgroundIfNeeded(background)
        }

        wrapper.addSubview(label)
        label.topAnchor.constraintEqualToAnchor(wrapper.topAnchor).active = true
        label.leadingAnchor.constraintEqualToAnchor(wrapper.leadingAnchor).active = true
        wrapper.trailingAnchor.constraintEqualToAnchor(label.trailingAnchor).active = true
        wrapper.bottomAnchor.constraintEqualToAnchor(label.bottomAnchor).active = true
        
        return ViewBundle(
            view = wrapper,
            size = size,
            margins = margins
        )
    }
}

fun String.stringFromHtml(textStyle: TextStyle?): NSAttributedString? {
    val nsString = NSString.create(string = this)
    val data: NSData? = nsString.dataUsingEncoding(NSUTF8StringEncoding)
    if (data == null) {
        return null
    }
    val attributed = NSMutableAttributedString.create(
        data = data,
        options = mapOf<Any?, Any?>(
            NSDocumentTypeDocumentAttribute to NSHTMLTextDocumentType,
            NSCharacterEncodingDocumentAttribute to NSUTF8StringEncoding
        ),
        documentAttributes = null,
        error = null)
    return attributed
}

fun UILabel.openUrl() {
    val attributed = this.attributedText ?: return

    attributed.enumerateAttribute(
        attrName = NSLinkAttributeName,
        inRange = NSMakeRange(0, attributed.string.length.toULong()),
        options = NSAttributedStringEnumerationReverse,
        usingBlock = { url, _, _ ->
            val link = url as? NSURL ?: return@enumerateAttribute
            UIApplication.sharedApplication.openURL(link)
        }
    )

}