/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.widget.WebViewWidget
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@Suppress("CONFLICTING_OVERLOADS")
internal actual class WebViewNavigationDelegate actual constructor(
    successRedirectConfig: WebViewWidget.RedirectConfig?,
    failureRedirectConfig: WebViewWidget.RedirectConfig?,
    private val isPageLoading: MutableLiveData<Boolean>?
) : NSObject(), WKNavigationDelegateProtocol {

    private val redirectUrlHandler = WebViewRedirectUrlHandler(
        successRedirectConfig = successRedirectConfig,
        failureRedirectConfig = failureRedirectConfig
    )

    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        isPageLoading?.value = true
    }

    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        isPageLoading?.value = false
    }

    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        val requestUrl = decidePolicyForNavigationAction.request.URL
        if (requestUrl == null) {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            return
        }

        val strRequestUrl = requestUrl.absoluteString
        if (strRequestUrl == null) {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            return
        }

        if (redirectUrlHandler.handleUrl(strRequestUrl)) {
            // If strRequestUrl contains success or failure token, then cancel navigation.
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
        } else {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        }
    }
}
