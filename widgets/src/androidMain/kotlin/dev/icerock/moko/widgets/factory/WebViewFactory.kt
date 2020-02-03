/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.WebViewWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class WebViewFactory actual constructor(
    private val margins: MarginValues?,
    private val background: Background?
) : ViewFactory<WebViewWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: WebViewWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext

        val root = WebView(context).apply {
            applyBackgroundIfNeeded(this@WebViewFactory.background)
            settings.javaScriptEnabled = widget.isJavaScriptEnabled

            webViewClient = CustomWebViewClient(
                successRedirectUrl = widget.successRedirectUrl,
                failureRedirectUrl = widget.failureRedirectUrl,
                onSuccessBlock = widget.onSuccessBlock,
                onFailureBlock = widget.onFailureBlock,
                isPageLoading = widget._isWebPageLoading
            )
            loadUrl(widget.targetUrl)
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }

    private class CustomWebViewClient(
        private val successRedirectUrl: String?,
        private val failureRedirectUrl: String?,
        private val onSuccessBlock: (() -> Unit)? = null,
        private val onFailureBlock: (() -> Unit)? = null,
        private val isPageLoading: MutableLiveData<Boolean>
    ) : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            isPageLoading.value = true

            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            isPageLoading.value = false
            super.onPageFinished(view, url)
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url == null)
                return true

            handleUrlOpening(url)

            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request == null)
                return true

            val url = Uri.parse(request.url.toString()).toString()
            handleUrlOpening(url)

            return super.shouldOverrideUrlLoading(view, request)
        }

        private fun handleUrlOpening(url: String): Boolean {
            return if (successRedirectUrl != null && url.contains(successRedirectUrl)) {
                onSuccessBlock?.invoke()
                true
            } else if (failureRedirectUrl != null && url.contains(failureRedirectUrl)) {
                onFailureBlock?.invoke()
                true
            } else {
                false
            }
        }

    }

}
