/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

/**
 * Size specs.
 *
 * @AS_PARENT - dimension is expected to be same as parent's with respect for margins and parent's paddings
 * @WRAP_CONTENT - dimension is expected to be wrapping child views with respect for paddings
 * @AUTO - no self-dimension (parent container should resolve it)
 */
object SizeSpec {
    const val AS_PARENT: Int = -1
    const val WRAP_CONTENT: Int = -2
    const val AUTO: Int = -3
}