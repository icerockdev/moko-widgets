/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.widget.WebViewWidget
import platform.WebKit.WKNavigationDelegateProtocol
import platform.darwin.NSObject

internal expect class WebViewNavigationDelegate(
    successRedirectConfig: WebViewWidget.RedirectConfig?,
    failureRedirectConfig: WebViewWidget.RedirectConfig?,
    isPageLoading: MutableLiveData<Boolean>?
) : NSObject, WKNavigationDelegateProtocol
