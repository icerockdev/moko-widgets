/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

actual typealias View = android.view.View

actual data class ViewFactoryContext(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val parent: ViewGroup? = null
) {
    val androidContext: Context get() = parent?.context ?: context
}
