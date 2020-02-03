/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.WebViewWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.UIViewWithIdentifier
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.identifier
import dev.icerock.moko.widgets.utils.applySizeToChild
import kotlinx.cinterop.ObjCClass
import objcnames.classes.Protocol
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIViewController
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.WebKit.WKWebView
import platform.WebKit.WKNavigationDelegateProtocol
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationType
import platform.darwin.NSObject

actual class WebViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background?
) : ViewFactory<WebViewWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: WebViewWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val root = WKWebView().apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            loadRequest(request = NSURLRequest(uRL = NSURL(string = widget.targetUrl)))
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }

    private inner class NavigationDelegate(
        private val successRedirectUrl: String?,
        private val failureRedirectUrl: String?,
        private val onSuccessBlock: (() -> Unit)? = null,
        private val onFailureBlock: (() -> Unit)? = null,
        private val onPageLoadingStarted: (() -> Unit)? = null,
        private val onPageLoadingFinished: (() -> Unit)? = null
    ) : NSObject(), WKNavigationDelegateProtocol {

        override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
            onPageLoadingFinished?.invoke()
        }

        /*
        override fun webView(
            webView: WKWebView,
            didReceiveServerRedirectForProvisionalNavigation: WKNavigation?
        ) {
            onPageLoadingStarted?.invoke()
            super.webView(
                webView = webView,
                didReceiveServerRedirectForProvisionalNavigation = didReceiveServerRedirectForProvisionalNavigation
            )
        }
        */

        override fun webView(
            webView: WKWebView,
            decidePolicyForNavigationAction: WKNavigationAction,
            decisionHandler: (WKNavigationActionPolicy) -> Unit
        ) {
            decidePolicyForNavigationAction.request.URL?.let { nsurl ->
                nsurl.absoluteString()?.let { strUrl ->
                    if(handleUrlOpening(strUrl)) {
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                    } else {
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
                    }
                } ?: decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            } ?: decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
        }

        private fun handleUrlOpening(url: String): Boolean {
            return if(successRedirectUrl != null && url.contains(successRedirectUrl)) {
                onSuccessBlock?.invoke()
                true
            } else if(failureRedirectUrl != null && url.contains(failureRedirectUrl)) {
                onFailureBlock?.invoke()
                true
            } else {
                false
            }
        }

    }

}
