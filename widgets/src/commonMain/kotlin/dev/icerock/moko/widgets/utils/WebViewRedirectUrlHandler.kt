/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.widgets.WebViewWidget

internal class WebViewRedirectUrlHandler(
    private val successRedirectConfig: WebViewWidget.RedirectConfig?,
    private val failureRedirectConfig: WebViewWidget.RedirectConfig?
) {

    /**
     * Handles [url] for [successRedirectUrl] or [failureRedirectUrl] contained in it.
     * If [url] contains [successRedirectUrl] or [failureRedirectUrl], callbacks [onSuccessBlock]
     * and [onFailureBlock] will be called and the function will return true, otherwise false.
     */
    fun handleUrl(url: String): Boolean {
        return if(successRedirectConfig != null && url.contains(successRedirectConfig.url)) {
            successRedirectConfig.handler(url)
            true
        } else if(failureRedirectConfig != null && url.contains(failureRedirectConfig.url)) {
            failureRedirectConfig.handler(url)
            true
        } else {
            false
        }
    }

}
