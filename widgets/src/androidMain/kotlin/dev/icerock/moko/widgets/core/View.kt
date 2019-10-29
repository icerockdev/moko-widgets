/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

actual typealias View = android.view.View

actual class ViewFactoryContext(
    val context: android.content.Context,
    val lifecycleOwner: LifecycleOwner,
    val parent: ViewGroup? = null
)
