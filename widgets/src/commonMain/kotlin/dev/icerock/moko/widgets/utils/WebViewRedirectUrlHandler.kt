/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

internal class WebViewRedirectUrlHandler(
    private val successRedirectUrl: String?,
    private val failureRedirectUrl: String?,
    private val onSuccessBlock: ((String) -> Unit)? = null,
    private val onFailureBlock: ((String) -> Unit)? = null
) {

    /**
     * Handles [url] for [successRedirectUrl] or [failureRedirectUrl] contained in it.
     * If [url] contains [successRedirectUrl] or [failureRedirectUrl], callbacks [onSuccessBlock]
     * and [onFailureBlock] will be called and the function will return true, otherwise false.
     */
    fun handleUrl(url: String): Boolean {
        return if(successRedirectUrl != null && url.contains(successRedirectUrl)) {
            onSuccessBlock?.invoke(url)
            true
        } else if(failureRedirectUrl != null && url.contains(failureRedirectUrl)) {
            onFailureBlock?.invoke(url)
            true
        } else {
            false
        }
    }

}
