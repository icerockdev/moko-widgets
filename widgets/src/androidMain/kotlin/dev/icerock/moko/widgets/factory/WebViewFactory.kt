/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.graphics.Bitmap
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

            webViewClient = CustomWebViewClient(widget._isWebPageLoading)
            loadUrl(widget.targetUrl)
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }

    private class CustomWebViewClient(
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

    }

}
