/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.factory.WebViewFactory
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef(WebViewFactory::class)
class WebViewWidget<WS : WidgetSize>(
    private val factory: ViewFactory<WebViewWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val targetUrl: String,
    val isJavaScriptEnabled: Boolean = true
) : Widget<WS>(), OptionalId<WebViewWidget.Id> {

    internal val _isWebPageLoading = MutableLiveData(false)

    /**
     * The state that describes a web page being loaded at the moment.
     */
    val isWebPageLoading: LiveData<Boolean> = _isWebPageLoading.readOnly()

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<WebViewWidget<out WidgetSize>>
    interface Category : Theme.Category<WebViewWidget<out WidgetSize>>

    object DefaultCategory : Category
}
