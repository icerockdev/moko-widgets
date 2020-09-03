/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.associatedObject
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.widget.WebViewWidget
import dev.icerock.moko.widgets.core.utils.WebViewNavigationDelegate
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.WebKit.WKWebView

actual class WebViewFactory actual constructor(
    private val margins: MarginValues?,
    private val background: Background<Fill.Solid>?
) : ViewFactory<WebViewWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: WebViewWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val root = WKWebView().apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            configuration.preferences.javaScriptEnabled = widget.isJavaScriptEnabled

            val webViewNavDelegate = WebViewNavigationDelegate(
                successRedirectConfig = widget.successRedirectConfig,
                failureRedirectConfig = widget.failureRedirectConfig,
                isPageLoading = widget.isWebPageLoading
            )
            associatedObject = webViewNavDelegate

            setNavigationDelegate(webViewNavDelegate)
            loadRequest(request = NSURLRequest(uRL = NSURL(string = widget.targetUrl)))
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }
}
