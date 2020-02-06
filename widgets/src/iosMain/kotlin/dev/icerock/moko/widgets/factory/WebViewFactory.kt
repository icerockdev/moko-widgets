/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.WebViewWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.objc.setAssociatedObject
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.WebViewRedirectUrlHandler
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.WebKit.WKWebView
import platform.WebKit.WKNavigationDelegateProtocol
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.darwin.NSObject

actual class WebViewFactory actual constructor(
    private val margins: MarginValues?,
    private val background: Background?
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

            val webViewNavDelegate = NavigationDelegate(
                successRedirectConfig = widget.successRedirectConfig,
                failureRedirectConfig = widget.failureRedirectConfig,
                isPageLoading = widget.isWebPageLoading
            )
            setAssociatedObject(this, webViewNavDelegate)

            setNavigationDelegate(webViewNavDelegate)
            loadRequest(request = NSURLRequest(uRL = NSURL(string = widget.targetUrl)))
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }

    @Suppress("CONFLICTING_OVERLOADS")
    private class NavigationDelegate(
        successRedirectConfig: WebViewWidget.RedirectConfig?,
        failureRedirectConfig: WebViewWidget.RedirectConfig?,
        private val isPageLoading: MutableLiveData<Boolean>
    ) : NSObject(), WKNavigationDelegateProtocol {

        private val redirectUrlHandler = WebViewRedirectUrlHandler(
            successRedirectConfig = successRedirectConfig,
            failureRedirectConfig = failureRedirectConfig
        )

        override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
            isPageLoading.value = true
        }

        override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
            isPageLoading.value = false
        }

        override fun webView(
            webView: WKWebView,
            decidePolicyForNavigationAction: WKNavigationAction,
            decisionHandler: (WKNavigationActionPolicy) -> Unit
        ) {
            val requestUrl = decidePolicyForNavigationAction.request.URL
            if(requestUrl == null) {
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                return
            }

            val strRequestUrl = requestUrl.absoluteString
            if(strRequestUrl == null) {
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                return
            }

            if(redirectUrlHandler.handleUrl(strRequestUrl)) {
                // If strRequestUrl contains success or failure token, then cancel navigation.
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            } else {
                decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
            }

        }

    }

}
