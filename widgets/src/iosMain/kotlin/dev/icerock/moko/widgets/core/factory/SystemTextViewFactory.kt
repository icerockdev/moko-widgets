/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setHandler
import dev.icerock.moko.widgets.core.widget.TextWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSAttributedString
import platform.Foundation.NSAttributedStringEnumerationReverse
import platform.Foundation.NSData
import platform.Foundation.NSMakeRange
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.addAttributes
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.enumerateAttribute
import platform.Foundation.removeAttribute
import platform.UIKit.NSAttachmentAttributeName
import platform.UIKit.NSCharacterEncodingDocumentAttribute
import platform.UIKit.NSDocumentTypeDocumentAttribute
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.NSHTMLTextDocumentType
import platform.UIKit.NSLineBreakByWordWrapping
import platform.UIKit.NSLinkAttributeName
import platform.UIKit.NSParagraphStyleAttributeName
import platform.UIKit.NSStrokeColorAttributeName
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.NSUnderlineColorAttributeName
import platform.UIKit.UIApplication
import platform.UIKit.UILabel
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.addGestureRecognizer
import platform.UIKit.create
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemTextViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val textStyle: TextStyle<Color>?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
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
            applyBackgroundIfNeeded(background)

            lineBreakMode = NSLineBreakByWordWrapping

            numberOfLines = 0

            when (this@SystemTextViewFactory.textHorizontalAlignment) {
                TextHorizontalAlignment.LEFT -> textAlignment = NSTextAlignmentLeft
                TextHorizontalAlignment.CENTER -> textAlignment = NSTextAlignmentCenter
                TextHorizontalAlignment.RIGHT -> textAlignment = NSTextAlignmentRight
                null -> {
                }
            }
        }
        if (isHtmlConverted) {
            widget.text.bind {
                label.attributedText = it.localized().stringFromHtml(
                    textStyle = textStyle
                )
            }
            label.userInteractionEnabled = true

            val recognizer = UITapGestureRecognizer().apply {
                setHandler { label.openUrl() }
            }
            label.addGestureRecognizer(recognizer)
        } else {
            widget.text.bind { label.text = it.localized() }
        }

        widget.maxLines?.bind {
            label.numberOfLines = (it ?: 0).toLong()
        }

        return ViewBundle(
            view = label,
            size = size,
            margins = margins
        )
    }
}

private fun String.stringFromHtml(textStyle: TextStyle<Color>?): NSAttributedString? {
    val nsString = NSString.create(string = this)
    val data: NSData = nsString.dataUsingEncoding(NSUTF8StringEncoding) ?: return null

    return NSMutableAttributedString.create(
        data = data,
        options = mapOf<Any?, Any?>(
            NSDocumentTypeDocumentAttribute to NSHTMLTextDocumentType,
            NSCharacterEncodingDocumentAttribute to NSUTF8StringEncoding
        ),
        documentAttributes = null,
        error = null
    )?.apply {
        changeUrlColor(textStyle)
    }
}

private fun NSMutableAttributedString.changeUrlColor(textStyle: TextStyle<Color>?) {
    if (textStyle?.color == null) return

    this.enumerateAttribute(attrName = NSLinkAttributeName,
        inRange = NSMakeRange(0U, this.string.length.toULong()),
        options = 0U,
        usingBlock = { url, range, _ ->
            if (url == null) return@enumerateAttribute

            this.removeAttribute(name = NSLinkAttributeName, range = range)
            this.addAttributes(
                attrs = mapOf(
                    NSForegroundColorAttributeName to textStyle.color.toUIColor(),
                    NSUnderlineColorAttributeName to textStyle.color.toUIColor(),
                    NSStrokeColorAttributeName to textStyle.color.toUIColor(),
                    NSAttachmentAttributeName to url
                ),
                range = range
            )

            this.removeAttribute(name = NSParagraphStyleAttributeName, range = range)
        })
}

private fun UILabel.openUrl() {
    val attributed = this.attributedText ?: return

    attributed.enumerateAttribute(
        attrName = NSAttachmentAttributeName,
        inRange = NSMakeRange(0U, attributed.string.length.toULong()),
        options = NSAttributedStringEnumerationReverse,
        usingBlock = { url, _, _ ->
            val link = url as? NSURL ?: return@enumerateAttribute
            UIApplication.sharedApplication.openURL(link)
        }
    )
}
