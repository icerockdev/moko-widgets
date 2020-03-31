/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.widget.constraint
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.widget.progressBar
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.WidgetScreen
import dev.icerock.moko.widgets.core.screen.getArgument
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.visibility
import dev.icerock.moko.widgets.core.widget.webView

class InfoWebViewScreen(
    private val theme: Theme
) : WidgetScreen<Args.Parcel<InfoWebViewScreen.WebViewArgs>>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.Normal("WebView Sample".desc())

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val inputArgs = getArgument()

        val isWebViewPageLoading = MutableLiveData(false)

        return with(theme) {
            constraint(
                size = WidgetSize.AsParent
            ) {
                val webViewConstraintItem = +webView(
                    size = WidgetSize.AsParent,
                    isJavaScriptEnabled = true,
                    targetUrl = inputArgs.targetUrl,
                    isWebPageLoading = isWebViewPageLoading
                )

                val progressVisibilityConstraintItem = +visibility(
                    child = progressBar(
                        size = WidgetSize.WrapContent
                    ),
                    showed = isWebViewPageLoading
                )

                constraints {
                    webViewConstraintItem leftRightToLeftRight root
                    webViewConstraintItem topToTop root.safeArea

                    progressVisibilityConstraintItem topToTop root
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
