/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.WebViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

/**
 * Simple WebView widget implementation. After creation the inner WebView automatically starts
 * loading web page by [targetUrl].
 * It is possible to observe the state of the page loading process by [isWebPageLoading] LiveData.
 * JavaScript for WebView is enabled by default.
 */
@Suppress("LongParameterList")
@WidgetDef(WebViewFactory::class)
class WebViewWidget<WS : WidgetSize>(
    private val factory: ViewFactory<WebViewWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val targetUrl: String,
    val isJavaScriptEnabled: Boolean = true,
    val androidIsDomStorageEnabled: Boolean = true,
    val isWebPageLoading: MutableLiveData<Boolean>? = null,
    @Suppress("RemoveRedundantQualifierName")
    val successRedirectConfig: WebViewWidget.RedirectConfig? = null,
    @Suppress("RemoveRedundantQualifierName")
    val failureRedirectConfig: WebViewWidget.RedirectConfig? = null
) : Widget<WS>(), OptionalId<WebViewWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<WebViewWidget<out WidgetSize>>
    interface Category : Theme.Category<WebViewWidget<out WidgetSize>>

    object DefaultCategory : Category

    data class RedirectConfig(val url: String, val handler: (String) -> Unit)
}
