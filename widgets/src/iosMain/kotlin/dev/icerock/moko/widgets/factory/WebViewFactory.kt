/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.WebViewWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import platform.UIKit.UIViewController
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.WebKit.WKWebView
import platform.WebKit.WKNavigationDelegateProtocol
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKNavigation
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
        val viewController: UIViewController = viewFactoryContext

        val root = WKWebView().apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            navigationDelegate = NavigationDelegate(
                successRedirectUrl = widget.successRedirectUrl,
                failureRedirectUrl = widget.failureRedirectUrl,
                onSuccessBlock = widget.onSuccessBlock,
                onFailureBlock = widget.onFailureBlock,
                isPageLoading = widget._isWebPageLoading
            )
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
        private val successRedirectUrl: String?,
        private val failureRedirectUrl: String?,
        private val onSuccessBlock: (() -> Unit)? = null,
        private val onFailureBlock: (() -> Unit)? = null,
        private val isPageLoading: MutableLiveData<Boolean>
    ) : NSObject(), WKNavigationDelegateProtocol {

        override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
            isPageLoading.value = true
        }

        override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
            isPageLoading.value = false
        }


        override fun webView(
            webView: WKWebView,
            didReceiveServerRedirectForProvisionalNavigation: WKNavigation?
        ) {
            isPageLoading.value = true
            webView.URL?.absoluteString()?.let { handleUrlOpening(it) }
        }

        /*
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
        */

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
