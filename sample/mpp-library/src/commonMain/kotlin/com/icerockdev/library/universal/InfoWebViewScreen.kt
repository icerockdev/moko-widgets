/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.progressBar
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getArgument
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.visibility
import dev.icerock.moko.widgets.webView

class InfoWebViewScreen(
    private val theme: Theme
) : WidgetScreen<Args.Parcel<InfoWebViewScreen.WebViewArgs>>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal("WebView Sample".desc())

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val inputArgs = getArgument()

        var isWebViewPageLoading: LiveData<Boolean>

        return with(theme) {
            constraint(
                size = WidgetSize.AsParent
            ) {
                val webViewConstraintItem = webView(
                    size = WidgetSize.AsParent,
                    isJavaScriptEnabled = true,
                    targetUrl = inputArgs.targetUrl
                ).let { webViewWidget ->
                    isWebViewPageLoading = webViewWidget.isWebPageLoading
                    +webViewWidget
                }

                val progressVisibilityConstraintItem = progressBar(
                    size = WidgetSize.WrapContent
                ).let {
                    +visibility(
                        child = it,
                        showed = isWebViewPageLoading
                    )
                }

                constraints {
                    webViewConstraintItem leftRightToLeftRight root
                    webViewConstraintItem topToTop root.safeArea

                    progressVisibilityConstraintItem centerYToCenterY root
                    progressVisibilityConstraintItem centerXToCenterX root
                }

            }
        }
    }

    @Parcelize
    data class WebViewArgs(
        val targetUrl: String
    ) : Parcelable

}
