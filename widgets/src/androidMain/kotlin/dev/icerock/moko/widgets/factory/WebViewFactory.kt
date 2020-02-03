/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import dev.icerock.moko.widgets.WebViewWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

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
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = WebView(context).apply {
            applyBackgroundIfNeeded(this@WebViewFactory.background)
            applyPaddingIfNeeded(padding)
            settings.javaScriptEnabled = widget.isJavaScriptEnabled

            webViewClient = createWebViewClient(
                successRedirectUrl = widget.successRedirectUrl,
                failureRedirectUrl = widget.failureRedirectUrl,
                onSuccessBlock = widget.onSuccessBlock,
                onFailureBlock = widget.onFailureBlock,
                onPageLoadingFinished = widget.onPageLoadingFinished,
                onPageLoadingStarted = widget.onPageLoadingStarted
            )
            loadUrl(widget.targetUrl)
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }

    private fun createWebViewClient(
        successRedirectUrl: String?,
        failureRedirectUrl: String?,
        onSuccessBlock: (() -> Unit)? = null,
        onFailureBlock: (() -> Unit)? = null,
        onPageLoadingStarted: (() -> Unit)? = null,
        onPageLoadingFinished: (() -> Unit)? = null
    ): WebViewClient {
        return object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                onPageLoadingStarted?.invoke()
                super.onPageStarted(view, url, favicon)
            }

            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if(url == null)
                    return true

                handleUrlOpening(url)

                return super.shouldOverrideUrlLoading(view, url)
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(request == null)
                    return true

                val url = Uri.parse(request.url.toString()).toString()
                handleUrlOpening(url)

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                onPageLoadingFinished?.invoke()
                super.onPageFinished(view, url)
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

}
