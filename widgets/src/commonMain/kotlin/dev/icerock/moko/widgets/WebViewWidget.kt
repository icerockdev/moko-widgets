/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.factory.ContainerViewFactory
import dev.icerock.moko.widgets.factory.WebViewFactory
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef(WebViewFactory::class)
class WebViewWidget<WS : WidgetSize>(
    private val factory: ViewFactory<WebViewWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val isJavaScriptEnabled: Boolean = true,
    val targetUrl: String,
    val successRedirectUrl: String? = null,
    val onSuccessBlock: (() -> Unit)? = null,
    val failureRedirectUrl: String? = null,
    val onFailureBlock: (() -> Unit)? = null,
    val onPageLoadingStarted: (() -> Unit)? = null,
    val onPageLoadingFinished: (() -> Unit)? = null
) : Widget<WS>(), OptionalId<WebViewWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<WebViewWidget<out WidgetSize>>
    interface Category : Theme.Category<WebViewWidget<out WidgetSize>>

    object DefaultCategory : Category
}
